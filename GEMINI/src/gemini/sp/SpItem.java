// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import gemini.util.Assert;

import java.util.Enumeration;
import java.util.Observable;

//
// Implements Enumeration.  Can be used to iterate over the SpItem's
// children.
//
final class SpChildren implements Enumeration, java.io.Serializable
{
   private SpItem _curChild;

//
// Construct with an item.
//
public SpChildren(SpItem item)
{
   _curChild = item.child();
}

//
// Return true if there are more children over which to enumerate.
//
public boolean hasMoreElements()
{
   return _curChild != null;
}

//
// Get the next child.
//
public Object nextElement()
{
   SpItem item = _curChild;
   _curChild = _curChild.next();
   return item;
}

}


/**
 * The base class for all Science Program items.  It implements common
 * features and services.
 */
public class SpItem extends Observable implements Cloneable, java.io.Serializable
{
   /** The title attribute. */
   public static final String ATTR_TITLE = "title";

   /**
    * The default name for items that haven't been stored in the ODB.
    * The ODB assigns a unique name once the item is stored.
    */
   public static final String NO_NAME    = "new";

   /**
    * The item's name as assigned by the ODB.
    *
    * @see #NO_NAME
    */
   protected String    _name;

   /**
    * The item's type.  This could be seen as a bit redundant perhaps, since
    * each item has its own subclass.  Used to translate the item to SGML/IDL.
    *
    * @see #type
    */
   protected SpType _type;

   /**
    * The item's table of attributes and values.
    */
   protected SpAvTable _avTable;

   /**
    * The state machine associated with this item's AV table.
    * It is used to mark that an item has had its attribute's updated
    * and to support discarding changes.
    */
   protected SpAvEditState _editAvFSM;
   
   /**
    * The state machine associated with the program or plan as a whole.
    * It is used to mark whether the program has been updated.
    */
    protected SpEditState _editFSM;

   /**
    * The "next" SpItem related to this item.  Items are arranged in a
    * hierarchy with a single parent, and a linked list of children.  The
    * _next field points to the next sibling in a list of children.
    *    * @see #next()
    */
   protected SpItem    _next       = null;

   /**
    * The parent of this SpItem.
    *
    * @see #_next
    * @see #parent()
    */
   protected SpItem    _parent     = null;

   /**
    * The first child of this item.  Other children can be found by following
    * the _firstChild's _next links.
    *
    * @see #_next
    * @see #child()
    */
   protected SpItem    _firstChild = null;

   /**
    * Generic user data stored on behalf of the client.
    *
    * @see #getClientData
    * @see #setClientData
    */
   protected Object _clientData;

/**
 * Construct with type.
 */
protected SpItem(SpType type)
{
   _name      = NO_NAME;
   _type      = type;
   _editAvFSM = new SpAvEditState(this);

   _avTable = new SpAvTable();
   _avTable.setStateMachine(_editAvFSM);
}

/**
 * Get a meaningful title for the item, based upon the value of the
 * "title" attribute but potentially containing additional information.
 * Subclasses will override this method to provide specialized title
 * information.  The title returned by this method is suitable for
 * printing or display.
 *
 * @see #getTitleAttr
 */
public String
getTitle()
{
   String title     = _type.getReadable();

   // Append the title in the "title" attribute
   String attrTitle = getTitleAttr();
   if ((attrTitle != null) && !(attrTitle.equals(""))) {
      title = title + ": " + attrTitle;
   }
   return title;
}

/**
 * Get the title of the item, returning null if there is none.
 * This method simply returns what is stored in the "title" attribute,
 * not a processed title.
 *
 * @see #getTitle
 */
public String
getTitleAttr()
{
   return _avTable.get(ATTR_TITLE);
}

/**
 * Set the title of the item.  This method simply replaces the current
 * value of the "title" attribute.
 */
public void
setTitleAttr(String title)
{
   _avTable.set(ATTR_TITLE, title);
}

/**
 * Get the SpObsData associated with the context that this item is in.
 */
public SpObsData
getObsData()
{
   // Subclasses that maintain an ObsData reference must subclass
   // this method to return it (See SpObsContextItem).  For most items,
   // this implementation is correct:
   if (_parent == null) {
      return null;
   }
   return _parent.getObsData();
}

/**
 * Get the edit state of the program or plan that this item is in.
 * @return SpEditState.EDITED or SpEditState.UNEDITED
 */
public final int
getEditState()
{
   if (_editFSM != null) {
      return _editFSM.getState();
   }
   return SpEditState.UNEDITED;
}

/**
 * Get the SpEditState class associated with the program or plan that
 * this item is in.  The SpEditState can be used to watch for hierarchy
 * or edit changes.
 */
public final SpEditState
getEditFSM()
{
   return _editFSM;
}

//
// This method is used internally to keep the SpEditState reference
// the same for every item in the program or plan.
//
final void
setEditFSM(SpEditState fsm)
{
   if (fsm == _editFSM) {
      return;
   }

   if (_editFSM != null) {
      if (_editFSM == fsm) {
         return;
      }
      deleteObserver(_editFSM);
      _editAvFSM.deleteObserver(_editFSM);
   }
   
   _editFSM = fsm;

   if (fsm != null) {
      addObserver(fsm);
      _editAvFSM.addObserver(fsm);
   }

   SpItem child = _firstChild;
   while (child != null) {
      child.setEditFSM(fsm);
      child = child.next();
   }
}


/**
 * The the parent of the item.
 */
public final SpItem parent()	{ return _parent; }

/**
 * Get the first child of the item.  Only one pointer is stored.  The other
 * children can be found by following the sibling links of the first child.
 *
 * @see #next
 */
public final SpItem child()     { return _firstChild; }

/**
 * Get the next sibling.  Siblings are stored in a linked list and order
 * is important.  For instance, observation components are kept at the
 * front of the list, other item types must come after.
 */
public final SpItem next()	{ return _next; }

/**
 * Get the previous sibling.  This should be tracked all the time in a
 * doublely-linked list to avoid this calculation.
 *
 * @see #next
 */
public final SpItem
prev()
{
   if (_parent == null) {
      return null;
   }

   SpItem child = _parent.child();
   if (child == this) {
      return null;
   }

   while (child.next() != this) {
      child = child.next();
   }

   return child;
}

/**
 * Get an Enumeration of the item's children.
 */
public final Enumeration
children()
{
   return new SpChildren(this);
}

/**
 * Return the number of children this item has.
 */
public final int
childCount()
{
   int i = 0;
   for (SpItem child = child(); child != null; child=child.next()) {
      ++i;
   }
   return i;
}

/**
 * Get the root of the hiearchy that this item is embedded within.
 */
public final SpItem
getRootItem()
{
   if (_parent == null) {
      return this;
   }

   return _parent.getRootItem();
}


/**
 * Get the name of the item.  Names are set by the ODB when the item is
 * first stored.  Until then, it has the default name.
 *
 * @see #_name
 * @see #NO_NAME
 */
public final String name()	{ return _name; }

/**
 * Get the item's type.  SpType is like an enumerated type in C++.  Each
 * type is mapped one-to-one with its own object.
 * 
 * @see #_type
 */
public final SpType type()	{ return _type; }

/**
 * Get the String for the item's type.  This is a derived attribute.
 * 
 * @see #_type
 */
public final String typeStr()	{ return _type.getType(); }

/**
 * Get the String for the item's subtype.  This is a derived attribute.
 * 
 * @see #_type
 */
public final String subtypeStr() { return _type.getSubtype(); }

//
// Set the item's parent.
//
// @see #_parent
// @see SpTreeMan
//
void
parent(SpItem newParent)
{
   _parent = newParent;
}

//
// Set the item's next sibling.
//
// @see #_next
// @see SpTreeMan
//
void
next(SpItem newSib) {
   _next = newSib;
}

//
// Set the item's name. Changed for ORAC version to be a public method.
//
// @see #_name
//
public final void
name(String name)
{
   _name = name;
   setChanged(); notifyObservers();
}

/**
 * Set the item's user data.  Opaque user data is stored on behalf of the
 * client.  The user data should implement SpCloneableClientData if the
 * data should be cloned when the SpItem is cloned.
 *
 * @see #_clientData
 * @see SpCloneableClientData
 */
public final void
setClientData(Object o)
{
   _clientData = o;
}

/**
 * Get the item's user data.
 *
 * @see #_clientData
 */
public final Object
getClientData()
{
   return _clientData;
}

/**
 * Implement the clone method, but leave it protected because this is
 * only a shallow clone.  Clients outside of this package that aren't
 * subclasses should use the copyShallow and copyDeep methods (that
 * themselves end up calling clone).
 *
 * @see #deepCopy
 * @see #shallowCopy
 */
protected Object
clone()
{
   SpItem spClone;
   try {
      spClone = (SpItem) super.clone();
   } catch (CloneNotSupportedException ex) {
      // Won't happen, since SpItem implements cloneable ...
      return null;
   }

   spClone._name      = NO_NAME;
   //spClone.type : is okay to share reference
   spClone._avTable   = _avTable.copy();
   spClone._editAvFSM = new SpAvEditState(spClone);
   spClone._avTable.setStateMachine(spClone._editAvFSM);

   // Null the hierarchy related fields, since the copy hasn't been
   // inserted anywhere yet.
   spClone._editFSM   = null;
   spClone._next       = null;
   spClone._parent     = null;
   spClone._firstChild = null;

   // Clone the client data, if possible
   if ((_clientData != null) && (_clientData instanceof SpCloneableClientData)) {
      SpCloneableClientData ccd = (SpCloneableClientData) _clientData;
      spClone._clientData       = ccd.clone(spClone);
   } else {
      spClone._clientData = null;
   }
   return spClone;
}

/**
 * Make a deep copy of this item.  Copies the attributes, children, and user
 * data, but not the name, siblings, or parents.
 */
public SpItem
deepCopy()
{
   SpItem spCopy = (SpItem) clone();

   // Copy each child
   SpItem lastChild = null;
   for (SpItem child = _firstChild; child != null; child = child.next()) {
      SpItem spChildCopy = (SpItem) child.deepCopy();
      spCopy.doInsert( spChildCopy, lastChild);
      lastChild = spChildCopy;
   }

   return spCopy;
}

/**
 * Make a shallow copy of this item.  Copies the attributes and user
 * data, but not the children, name, siblings, or parents.
 */
public SpItem
shallowCopy()
{
   return (SpItem) clone();
}

/**
 * Returns true if this item has a name other than the default one.
 * If so, we can assume that the item has been stored in the ODB. 
 */
public final boolean
hasBeenNamed()
{
   return !_name.equals(NO_NAME);
}

/**
 * Get the item's id.  This is formed by concatenating the id of its
 * parent (if any) with its name, separated by a colon.
 */
public final String
id()
{
   if (_parent != null) {
      return _parent.id() + ":" + _name;
   }
   return _name;
}

/**
 * Get the SpAvEditState associated with this SpItem.  Every item has its
 * own SpAvEditState, which may be used to track changes to the attributes
 * maintained by this item.
 */
public final SpAvEditState
getAvEditFSM()
{
   return _editAvFSM;
}


/**
 * Get the edit state of this SpItem.
 * @see SpAvEditState
 * @return SpAvEditState.UNEDITED or SpAvEditState.EDITED or SpAvEditState.EDIT_UNDONE
 */
public final int
getAvEditState()
{
   if (_editAvFSM != null) {
      return _editAvFSM.getState();
   } else {
      return SpAvEditState.UNEDITED;
   }
}


/**
 * Get the attribute/value table associated with this item.
 */
public final SpAvTable
getTable()
{
   return _avTable;
}

/**
 * Set the attribute value table associated with this item.
 */
protected void
setTable(SpAvTable avTable)
{
   _avTable = avTable;
   _avTable.setStateMachine( _editAvFSM );

   setChanged(); notifyObservers();
}

/**
 * Replace the AV table with the given one.
 */
protected void
replaceTable(SpAvTable avTable)
{
   if (avTable == _avTable) {
      return;
   }
   _editAvFSM.editPending();
   _avTable = avTable;
   _avTable.setStateMachine( _editAvFSM );

   setChanged(); notifyObservers();
}


/**
 * Do the actual work of inserting a child into the tree.  This is the
 * method that should be overridden if any special behavior is required.
 * It does the insertion work for a single child without worrying about
 * notifying the edit state of a hierarchy change.
 *
 * @param newChild The new SpItem to insert
 * @param afterChild The child SpItem after which to insert the new child.
 * (If null, the new child is inserted first.
 *
 */
protected void
doInsert(SpItem newChild, SpItem afterChild)
{
   if (afterChild == null) {
      newChild.parent(this);
      newChild.next(_firstChild);
      _firstChild = newChild;
      newChild.setEditFSM( _editFSM );

   } else {
      Assert.notFalse(afterChild.parent() == this);
      newChild.parent(this);
      newChild.next(afterChild.next());
      afterChild.next(newChild);
      newChild.setEditFSM( _editFSM );
   }
}

/**
 * Insert one or more children after the given child.  If afterChild is
 * null, the newChildren will be inserted first.
 */
protected void
insert(SpItem[] newChildren, SpItem afterChild)
{
   doInsert(newChildren[0], afterChild);
   for (int i=1; i<newChildren.length; ++i) {
      doInsert(newChildren[i], newChildren[i-1]);
   }
   if (_editFSM != null) _editFSM.notifyAdded(this, newChildren, afterChild);
}

/**
 * Do the actual work of extracting a child from the tree.  This is the
 * method that should be overridden if any special behavior is required.
 * It does the extraction work for a single child without worrying about
 * notifying the edit state of a hierarchy change.
 */
protected void
doExtract(SpItem child)
{
   Assert.notFalse(child.parent() == this);

   SpInsertInfo reinsert;
 
   if (child == _firstChild) {
      _firstChild = _firstChild.next();
   } else {
      SpItem prev = _firstChild;
      while (prev.next() != child) {
         prev = prev.next();
      }
      prev.next(child.next());
   }
 
   child.parent(null);
}

/**
 * Extract a set of children from the tree.
 */
protected void
extract(SpItem[] children)
{
   for (int i=0; i<children.length; ++i) {
      doExtract(children[i]);
   }
   _editFSM.notifyRemoved(this, children);
}

/**
 * Extract myself from my parent.
 */
protected void
extract()
{
   if (_parent != null) {
      SpItem[] spItemA = { this };
      _parent.extract(spItemA);
   }
}

/**
 * Move the given children to the newParent, placing them after the
 * "afterChild" if (non-null).
 */
protected void
move(SpItem[] children, SpItem newParent, SpItem afterChild)
{
   doExtract(children[0]);
   newParent.doInsert(children[0], afterChild);

   for (int i=1; i<children.length; ++i) {
      doExtract(children[i]);
      newParent.doInsert(children[i], children[i-1]);
   }

   _editFSM.notifyMoved(this, children, newParent, afterChild);
}

/**
 * Find a child item with the given name.
 */
public final SpItem
find(String name)
{
   for (SpItem child = _firstChild; child != null; child = child.next()) {
      if (child.name().equals(name)) {
         return child;
      }
   }
   return null;
}


/**
 * Print a formatted string representation of the item for debugging.
 */
public void
print()
{
   print("");
}

/**
 * Print a formatted string representation of the item with the given
 * indentation for debugging.
 */
public void
print(String indentStr)
{
   String t = name() + ": " + typeStr() + " (" + subtypeStr() + ")";
   System.out.println(indentStr + t);

   indentStr = indentStr + "   ";
  
   // Print the attributes
   Enumeration keys = _avTable.attributes();
   while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      System.out.println(indentStr+key+" ("+_avTable.getDescription(key)+")");
      System.out.println(indentStr + "--> " + _avTable.getAll(key).toString());
   }

   // Print the children
   for (SpItem child = _firstChild; child != null; child = child.next()) {
      child.print(indentStr + "   ");
   }
}

}
