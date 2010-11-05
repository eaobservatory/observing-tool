// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
//
package gemini.sp ;

import gemini.util.Assert ;

import java.util.Enumeration ;
import java.util.Observable ;

//
// Implements Enumeration. Can be used to iterate over the SpItem's
// children.
//
@SuppressWarnings( "serial" )
final class SpChildren implements Enumeration<SpItem> , java.io.Serializable
{

	private SpItem _curChild ;

	//
	// Construct with an item.
	//
	public SpChildren( SpItem item )
	{
		_curChild = item.child() ;
	}

	//
	// Return true if there are more children over which to enumerate.
	//
	public boolean hasMoreElements()
	{
		return _curChild != null ;
	}

	//
	// Get the next child.
	//
	public SpItem nextElement()
	{
		SpItem item = _curChild ;
		_curChild = _curChild.next() ;
		return item ;
	}

}

/**
 * The base class for all Science Program items.
 * 
 * It implements common features and services.
 * 
 * <h3>SpItem and XML</h3>
 * 
 * (added by M.Folger@roe.ac.uk, 27 March 2002))
 * <p>
 * Methods for reading and writing XML have been added.
 * <p>
 * <ul>
 * Writing XML
 * <li>{@link #toXML()}
 * <li>{@link #toXML(java.lang.String,java.lang.StringBuffer)}
 * <li>{@link #processAvAttribute(java.lang.String,java.lang.String)}
 * <li>{@link #avToXml(java.lang.String,java.lang.String)}
 * </ul>
 * <ul>
 * Reading XML (These methods are called from an external XML Parser, see
 * {@link orac.util.SpInputXML}.)
 * <li>{@link #processXmlElementStart(java.lang.String)}
 * <li>{@link #processXmlElementContent(java.lang.String,java.lang.String)}
 * <li>{@link #processXmlElementContent(java.lang.String,java.lang.String,int)}
 * <li>{@link #processXmlElementEnd(java.lang.String)}
 * <li>{@link #processXmlAttribute(java.lang.String,java.lang.String,java.lang.String)}
 * </ul>
 * 
 * <h4>SpAvTable attributes vs XML attributes</h4>
 * Note that <i>SpAvTable attributes</i> are generally represented by <i>XML
 * elements</i>. <i>XML attributes</i> represent special cases of SpAvTable
 * attributes (see
 * {@link #processAvAttribute(java.lang.String, java.lang.String, java.lang.StringBuffer))})
 * Make sure you do not confuse SpAvTable attributes and XML attributes when
 * reading this documentation.
 */
@SuppressWarnings( "serial" )
public class SpItem extends Observable implements Cloneable , java.io.Serializable
{

	/** The title attribute. */
	public static final String ATTR_TITLE = "title" ;

	/**
     * The default name for items that haven't been stored in the ODB. The ODB
     * assigns a unique name once the item is stored.
     */
	public static final String NO_NAME = "new" ;

	public static final String XML_ATTR_TYPE = "type" ;

	public static final String XML_ATTR_SUBTYPE = "subtype" ;

	public static final String XML_META_PREFIX = "meta" ;

	/** The class name is used as XML element name for an SpItem. */
	protected String _className ;

	/**
     * The item's name as assigned by the ODB.
     * 
     * @see #NO_NAME
     */
	protected String _name ;

	/**
     * The item's type. This could be seen as a bit redundant perhaps, since
     * each item has its own subclass. Used to translate the item to SGML/IDL.
     * 
     * @see #type
     */
	protected SpType _type ;

	/**
     * The item's table of attributes and values.
     */
	protected SpAvTable _avTable ;

	/**
     * The state machine associated with this item's AV table. It is used to
     * mark that an item has had its attribute's updated and to support
     * discarding changes.
     */
	protected SpAvEditState _editAvFSM ;

	/**
     * The state machine associated with the program or plan as a whole. It is
     * used to mark whether the program has been updated.
     */
	protected SpEditState _editFSM ;

	/**
     * The "next" SpItem related to this item. Items are arranged in a hierarchy
     * with a single parent, and a linked list of children. The _next field
     * points to the next sibling in a list of children. *
     * 
     * @see #next()
     */
	protected SpItem _next = null ;

	/**
     * The parent of this SpItem.
     * 
     * @see #_next
     * @see #parent()
     */
	protected SpItem _parent = null ;

	/**
     * The first child of this item. Other children can be found by following
     * the _firstChild's _next links.
     * 
     * @see #_next
     * @see #child()
     */
	protected SpItem _firstChild = null ;

	/**
     * Generic user data stored on behalf of the client.
     * 
     * @see #getClientData
     * @see #setClientData
     */
	protected Object _clientData ;

	/**
     * Construct with type.
     */
	protected SpItem( SpType type )
	{
		_name = NO_NAME ;
		_type = type ;
		_editAvFSM = new SpAvEditState( this ) ;

		_avTable = new SpAvTable() ;
		_avTable.setStateMachine( _editAvFSM ) ;

		_className = getClass().getSimpleName() ;
	}

	/**
     * Get a meaningful title for the item, based upon the value of the "title"
     * attribute but potentially containing additional information. Subclasses
     * will override this method to provide specialized title information. The
     * title returned by this method is suitable for printing or display.
     * 
     * @see #getTitleAttr
     */
	public String getTitle()
	{
		String title = _type.getReadable() ;

		// Append the title in the "title" attribute
		String attrTitle = getTitleAttr() ;
		if( ( attrTitle != null ) && !( attrTitle.equals( "" ) ) )
			title = title + ": " + attrTitle ;
		return title ;
	}

	/**
     * Get the title of the item, returning null if there is none. This method
     * simply returns what is stored in the "title" attribute, not a processed
     * title.
     * 
     * @see #getTitle
     */
	public String getTitleAttr()
	{
		return _avTable.get( ATTR_TITLE ) ;
	}

	/**
     * Set the title of the item. This method simply replaces the current value
     * of the "title" attribute.
     */
	public void setTitleAttr( String title )
	{
		_avTable.set( ATTR_TITLE , title ) ;
	}

	/**
     * Get the SpObsData associated with the context that this item is in.
     */
	public SpObsData getObsData()
	{
		// Subclasses that maintain an ObsData reference must subclass
		// this method to return it (See SpObsContextItem). For most items,
		// this implementation is correct:
		if( _parent == null )
			return null ;
		return _parent.getObsData() ;
	}

	/**
     * Get the edit state of the program or plan that this item is in.
     * 
     * @return SpEditState.EDITED or SpEditState.UNEDITED
     */
	public final int getEditState()
	{
		if( _editFSM != null )
			return _editFSM.getState() ;
		return SpEditState.UNEDITED ;
	}

	/**
     * Get the SpEditState class associated with the program or plan that this
     * item is in. The SpEditState can be used to watch for hierarchy or edit
     * changes.
     */
	public final SpEditState getEditFSM()
	{
		if( _editFSM == null )
		{
			SpItem temParent = parent() ;
			if( temParent != null )
				_editFSM = temParent.getEditFSM() ;
		}
		return _editFSM ;
	}

	//
	// This method is used internally to keep the SpEditState reference
	// the same for every item in the program or plan.
	//
	public void setEditFSM( SpEditState fsm )
	{
		if( fsm == _editFSM )
			return ;

		if( _editFSM != null )
		{
			if( _editFSM == fsm )
				return ;
			deleteObserver( _editFSM ) ;
			_editAvFSM.deleteObserver( _editFSM ) ;
		}

		_editFSM = fsm ;

		if( fsm != null )
		{
			addObserver( fsm ) ;
			_editAvFSM.addObserver( fsm ) ;
		}

		SpItem child = _firstChild ;
		while( child != null )
		{
			child.setEditFSM( fsm ) ;
			child = child.next() ;
		}
	}

	/**
     * The the parent of the item.
     */
	public SpItem parent()
	{
		return _parent ;
	}

	/**
     * Get the first child of the item. Only one pointer is stored. The other
     * children can be found by following the sibling links of the first child.
     * 
     * @see #next
     */
	public final SpItem child()
	{
		return _firstChild ;
	}

	public final SpItem lastChild()
	{
		SpItem child = child() ;

		while( ( child != null ) && ( child.next() != null ) )
			child = child.next() ;

		return child ;
	}

	/**
     * Get the next sibling. Siblings are stored in a linked list and order is
     * important. For instance, observation components are kept at the front of
     * the list, other item types must come after.
     */
	public final SpItem next()
	{
		return _next ;
	}

	/**
     * Get the previous sibling. This should be tracked all the time in a
     * doublely-linked list to avoid this calculation.
     * 
     * @see #next
     */
	public final SpItem prev()
	{
		if( _parent == null )
			return null ;

		SpItem child = _parent.child() ;
		if( child == this )
			return null ;

		while( child.next() != this )
			child = child.next() ;

		return child ;
	}

	/**
     * Get an Enumeration of the item's children.
     */
	public final Enumeration<SpItem> children()
	{
		return new SpChildren( this ) ;
	}

	/**
     * Return the number of children this item has.
     */
	public final int childCount()
	{
		int i = 0 ;
		for( SpItem child = child() ; child != null ; child = child.next() )
			++i ;
		return i ;
	}

	/**
     * Get the root of the hiearchy that this item is embedded within.
     */
	public final SpItem getRootItem()
	{
		if( _parent == null )
			return this ;

		return _parent.getRootItem() ;
	}

	/**
     * Get the name of the item. Names are set by the ODB when the item is first
     * stored. Until then, it has the default name.
     * 
     * @see #_name
     * @see #NO_NAME
     */
	public final String name()
	{
		return _name ;
	}

	/**
     * Get the item's type. SpType is like an enumerated type in C++. Each type
     * is mapped one-to-one with its own object.
     * 
     * @see #_type
     */
	public final SpType type()
	{
		return _type ;
	}

	/**
     * Get the String for the item's type. This is a derived attribute.
     * 
     * @see #_type
     */
	public final String typeStr()
	{
		return _type.getType() ;
	}

	/**
     * Get the String for the item's subtype. This is a derived attribute.
     * 
     * @see #_type
     */
	public final String subtypeStr()
	{
		return _type.getSubtype() ;
	}

	//
	// Set the item's parent.
	//
	// @see #_parent
	// @see SpTreeMan
	//
	void parent( SpItem newParent )
	{
		_parent = newParent ;
	}

	//
	// Set the item's next sibling.
	//
	// @see #_next
	// @see SpTreeMan
	//
	void next( SpItem newSib )
	{
		_next = newSib ;
	}

	//
	// Set the item's name. Changed for ORAC version to be a public method.
	//
	// @see #_name
	//
	public final void name( String name )
	{
		_name = name ;
		setChanged() ;
		notifyObservers() ;
	}

	/**
     * Set the item's user data. Opaque user data is stored on behalf of the
     * client. The user data should implement SpCloneableClientData if the data
     * should be cloned when the SpItem is cloned.
     * 
     * @see #_clientData
     * @see SpCloneableClientData
     */
	public final void setClientData( Object o )
	{
		_clientData = o ;
	}

	/**
     * Get the item's user data.
     * 
     * @see #_clientData
     */
	public final Object getClientData()
	{
		return _clientData ;
	}

	/**
     * Implement the clone method, but leave it protected because this is only a
     * shallow clone. Clients outside of this package that aren't subclasses
     * should use the copyShallow and copyDeep methods (that themselves end up
     * calling clone).
     * 
     * @see #deepCopy
     * @see #shallowCopy
     */
	protected Object clone()
	{
		SpItem spClone ;
		try
		{
			spClone = ( SpItem )super.clone() ;
		}
		catch( CloneNotSupportedException ex )
		{
			// Won't happen, since SpItem implements cloneable ...
			return null ;
		}

		spClone._name = NO_NAME ;
		// spClone.type : is okay to share reference
		spClone._avTable = _avTable.copy() ;
		spClone._editAvFSM = new SpAvEditState( spClone ) ;
		spClone._avTable.setStateMachine( spClone._editAvFSM ) ;

		// Null the hierarchy related fields, since the copy hasn't been
		// inserted anywhere yet.
		spClone._editFSM = null ;
		spClone._next = null ;
		spClone._parent = null ;
		spClone._firstChild = null ;

		// Clone the client data, if possible
		if( ( _clientData != null ) && ( _clientData instanceof SpCloneableClientData ) )
		{
			SpCloneableClientData ccd = ( SpCloneableClientData )_clientData ;
			spClone._clientData = ccd.clone( spClone ) ;
		}
		else
		{
			spClone._clientData = null ;
		}
		return spClone ;
	}

	/**
     * Make a deep copy of this item. Copies the attributes, children, and user
     * data, but not the name, siblings, or parents.
     */
	public SpItem deepCopy()
	{
		SpItem spCopy = ( SpItem )clone() ;

		// Copy each child
		SpItem lastChild = null ;
		for( SpItem child = _firstChild ; child != null ; child = child.next() )
		{
			SpItem spChildCopy = child.deepCopy() ;
			spCopy.doInsert( spChildCopy , lastChild ) ;
			lastChild = spChildCopy ;
		}

		return spCopy ;
	}

	/**
     * Make a shallow copy of this item. Copies the attributes and user data,
     * but not the children, name, siblings, or parents.
     */
	public SpItem shallowCopy()
	{
		return ( SpItem )clone() ;
	}

	/**
     * Returns true if this item has a name other than the default one. If so,
     * we can assume that the item has been stored in the ODB.
     */
	public final boolean hasBeenNamed()
	{
		return !_name.equals( NO_NAME ) ;
	}

	/**
     * Get the item's id. This is formed by concatenating the id of its parent
     * (if any) with its name, separated by a colon.
     */
	public final String id()
	{
		if( _parent != null )
			return _parent.id() + ":" + _name ;
		return _name ;
	}

	/**
     * Get the SpAvEditState associated with this SpItem. Every item has its own
     * SpAvEditState, which may be used to track changes to the attributes
     * maintained by this item.
     */
	public final SpAvEditState getAvEditFSM()
	{
		return _editAvFSM ;
	}

	/**
     * Get the edit state of this SpItem.
     * 
     * @see SpAvEditState
     * @return SpAvEditState.UNEDITED or SpAvEditState.EDITED or
     *         SpAvEditState.EDIT_UNDONE
     */
	public final int getAvEditState()
	{
		if( _editAvFSM != null )
			return _editAvFSM.getState() ;
		else
			return SpAvEditState.UNEDITED ;
	}

	/**
     * Get the attribute/value table associated with this item.
     */
	public final SpAvTable getTable()
	{
		return _avTable ;
	}

	/**
     * Set the attribute value table associated with this item.
     */
	protected void setTable( SpAvTable avTable )
	{
		_avTable = avTable ;
		_avTable.setStateMachine( _editAvFSM ) ;

		setChanged() ;
		notifyObservers() ;
	}

	/**
     * Replace the AV table with the given one.
     */
	protected void replaceTable( SpAvTable avTable )
	{
		if( avTable == _avTable )
			return ;

		_editAvFSM.editPending() ;
		_avTable = avTable ;
		_avTable.setStateMachine( _editAvFSM ) ;

		setChanged() ;
		notifyObservers() ;
	}

	/**
     * Do the actual work of inserting a child into the tree. This is the method
     * that should be overridden if any special behavior is required. It does
     * the insertion work for a single child without worrying about notifying
     * the edit state of a hierarchy change.
     * 
     * @param newChild
     *            The new SpItem to insert
     * @param afterChild
     *            The child SpItem after which to insert the new child. (If
     *            null, the new child is inserted first.
     * 
     */
	protected void doInsert( SpItem newChild , SpItem afterChild )
	{
		if( afterChild == null )
		{
			newChild.parent( this ) ;
			newChild.next( _firstChild ) ;
			_firstChild = newChild ;
			newChild.setEditFSM( _editFSM ) ;
		}
		else
		{
			Assert.notFalse( afterChild.parent() == this ) ;
			newChild.parent( this ) ;
			newChild.next( afterChild.next() ) ;
			afterChild.next( newChild ) ;
			newChild.setEditFSM( _editFSM ) ;
		}
	}

	/**
     * Insert one or more children after the given child. If afterChild is null,
     * the newChildren will be inserted first.
     */
	protected void insert( SpItem[] newChildren , SpItem afterChild )
	{
		doInsert( newChildren[ 0 ] , afterChild ) ;
		for( int i = 1 ; i < newChildren.length ; ++i )
			doInsert( newChildren[ i ] , newChildren[ i - 1 ] ) ;
		if( _editFSM != null )
			_editFSM.notifyAdded( this , newChildren , afterChild ) ;
	}

	/**
     * Do the actual work of extracting a child from the tree. This is the
     * method that should be overridden if any special behavior is required. It
     * does the extraction work for a single child without worrying about
     * notifying the edit state of a hierarchy change.
     */
	protected void doExtract( SpItem child )
	{
		Assert.notFalse( child.parent() == this ) ;

		if( child == _firstChild )
		{
			_firstChild = _firstChild.next() ;
		}
		else
		{
			SpItem prev = _firstChild ;
			while( prev.next() != child )
				prev = prev.next() ;
			prev.next( child.next() ) ;
		}
		child.parent( null ) ;
	}

	/**
     * Extract a set of children from the tree.
     */
	protected void extract( SpItem[] children )
	{
		for( int i = 0 ; i < children.length ; ++i )
			doExtract( children[ i ] ) ;
		if( _editFSM != null )
			_editFSM.notifyRemoved( this , children ) ;
	}

	/**
     * Extract myself from my parent.
     */
	protected void extract()
	{
		if( _parent != null )
		{
			SpItem[] spItemA = { this } ;
			_parent.extract( spItemA ) ;
		}
	}

	/**
     * Move the given children to the newParent, placing them after the
     * "afterChild" if (non-null).
     */
	protected void move( SpItem[] children , SpItem newParent , SpItem afterChild )
	{
		doExtract( children[ 0 ] ) ;
		newParent.doInsert( children[ 0 ] , afterChild ) ;

		for( int i = 1 ; i < children.length ; ++i )
		{
			doExtract( children[ i ] ) ;
			newParent.doInsert( children[ i ] , children[ i - 1 ] ) ;
		}

		if( _editFSM != null )
			_editFSM.notifyMoved( this , children , newParent , afterChild ) ;
	}

	/**
     * Find a child item with the given name.
     */
	public final SpItem find( String name )
	{
		for( SpItem child = _firstChild ; child != null ; child = child.next() )
		{
			if( child.name().equals( name ) )
				return child ;
		}
		return null ;
	}

	/**
     * Print a formatted string representation of the item for debugging.
     */
	public void print()
	{
		print( "" ) ;
	}

	/**
     * Print a formatted string representation of the item with the given
     * indentation for debugging.
     */
	public void print( String indentStr )
	{
		String t = name() + ": " + typeStr() + " (" + subtypeStr() + ")" ;
		System.out.println( indentStr + t ) ;

		indentStr = indentStr + "   " ;

		// Print the attributes
		Enumeration<String> keys = _avTable.attributes() ;
		while( keys.hasMoreElements() )
		{
			String key = keys.nextElement() ;
			System.out.println( indentStr + key + " (" + _avTable.getDescription( key ) + ")" ) ;
			System.out.println( indentStr + "--> " + _avTable.getAll( key ).toString() ) ;
		}

		// Print the children
		for( SpItem child = _firstChild ; child != null ; child = child.next() )
			child.print( indentStr + "   " ) ;
	}

	public String getXmlElementName()
	{
		return _className ;
	}

	/**
     * Returns XML representation of this SpItem.
     * 
     * Could be overridden by subclasses to change their XML representation. But
     * it is recommened to override
     * {@link #processAvAttribute(java.lang.String,java.lang.String,java.lang.StringBuffer)}.
     * This allows to keep the basic form
     * 
     * <pre><tt>
     * &lt ;SpItem &lt ;i&gt ;user attributes (XML representation of SpAvTable attributes starting with ':')&lt ;/i&gt ; type=&quot ;x&quot ; sybtype=&quot ;y&quot ;&gt ;
     *   &lt ;i&gt ;XML representation of SpAvTable&lt ;/i&gt ;
     * &lt ;SpItem&gt ;
     * </tt></pre>
     */
	public String toXML()
	{
		StringBuffer buffer = new StringBuffer() ;

		toXML( "" , buffer ) ;

		return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + buffer.toString() ;
	}

	/**
     * Appends an XML representation of this SpItem to a StringBuffer.
     * 
     * @param indent
     *            String by which the XML representation of this SpItem should
     *            be indented
     * @param xmlBuffer
     *            StringBuffer to which the XML representation of this SpItem is
     *            appended
     */
	protected void toXML( String indent , StringBuffer xmlBuffer )
	{
		Enumeration<String> avAttributes ;
		String avAttr ;
		SpItem child ;

		xmlBuffer.append( "\n" + indent + "<" + _className ) ;

		// Get those AV table attributes that are to represented by XML
        // attributes
		// i.e. those that start with the character ':'.
		avAttributes = _avTable.attributes( ":" ) ;

		// Write the XML attributes
		while( avAttributes.hasMoreElements() )
		{
			avAttr = avAttributes.nextElement() ;
			if( avAttr.indexOf( "xmlns" ) != -1 || avAttr.indexOf( "schemaLocation" ) != -1 )
				continue ;
			xmlBuffer.append( " " + avAttr.substring( 1 ) + "=\"" + _avTable.get( avAttr ) + "\"" ) ;
		}

		// Add type and subtype as XML attributes
		if( typeStr().startsWith( "p" ) )
		{
			xmlBuffer.append( " type=\"" + typeStr() + "\" subtype=\"" + subtypeStr() + "\"\n" ) ;
			xmlBuffer.append( "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" ) ;
			xmlBuffer.append( "\txmlns=\"http://omp.jach.hawaii.edu/schema/TOML\">\n" ) ;
		}
		else
		{
			xmlBuffer.append( " type=\"" + typeStr() + "\" subtype=\"" + subtypeStr() + "\">" ) ;
		}

		// Now get hold of and process all AV table attributes.
		// The once starting with the character ':' have been dealt with already
		// and will be ignored by processAvAttribute().
		avAttributes = _avTable.attributes() ;

		while( avAttributes.hasMoreElements() )
			processAvAttribute( avAttributes.nextElement() , indent , xmlBuffer ) ;

		// Deal with the child items.
		child = child() ;
		while( child != null )
		{
			child.toXML( indent + "  " , xmlBuffer ) ;
			child = child.next() ;
		}
		xmlBuffer.append( "\n" + indent + "</" + _className + ">" ) ;
	}

	/**
     * Turns an AV table entry into XML.
     * 
     * 
     * <table border cols=3 width="100%">
     * <tr>
     * <td><b>SpAvTable attribute string</b> (ATTR_...)</td>
     * <td><b>value</b></td>
     * <td><b>XML representation</b></td>
     * </tr>
     * <tr>
     * <td><tt>"attribute"</tt></td>
     * <td><tt>"value"</tt></td>
     * <td>&lt ;value&gt ;12.3&lt ;/attribute&gt ;</td>
     * </tr>
     * <tr>
     * <td>Example:<br>
     * <tt>"expTime"</tt></td>
     * <td><tt>12.3</tt></td>
     * <td>&lt ;expTime&gt ;12.3&lt ;/expTime&gt ;</td>
     * </tr>
     * <tr>
     * <td span=3><br>
     * </td>
     * </tr>
     * <tr>
     * <td><tt>"attribute"</tt></td>
     * <td>value Vector (Java Vector)</td>
     * <td>
     * 
     * <pre>
     * &lt ;attribute&gt ;
     * 	 &lt ;value&gt ;value Vector entry 1&lt ;/value&gt ;
     * 	 &lt ;value&gt ;value Vector entry 2&lt ;/value&gt ;
     * 	 &lt ;value&gt ;value Vector entry 3&lt ;/value&gt ;
     * 	 &lt ;/attribute&gt ;
     * </pre>
     * 
     * </td>
     * </tr>
     * <tr>
     * <td>Example<br>
     * <tt>"expTimes"</tt></td>
     * <td>Java Vector containing 12.3, 23.4, 34.5</td>
     * <td>
     * 
     * <pre>
     * &lt ;expTimes&gt ;
     * 	 &lt ;value&gt ;12.3&lt ;/value&gt ;
     * 	 &lt ;value&gt ;23.4&lt ;/value&gt ;
     * 	 &lt ;value&gt ;34.4&lt ;/value&gt ;
     * 	 &lt ;/expTimes&gt ;
     * </pre>
     * 
     * </td>
     * </tr>
     * <tr>
     * <td span=3><br>
     * </td>
     * </tr>
     * <tr>
     * <td><tt>":attribute"</tt></td>
     * <td><tt>"value"</tt></td>
     * <td>&lt ;SpItem attribute="value"&gt ; ... &lt ;/SpItem&gt ;<br>
     * where SpItem is any subclass of SpItem</td>
     * </tr>
     * <tr>
     * <td>Example<br>
     * <tt>":optional"</tt></td>
     * <td><tt>"true"</tt></td>
     * <td>&lt ;SpItem optional="true"&gt ; ... &lt ;/SpItem&gt ;<br>
     * where SpItem is any subclass of SpItem</td>
     * </tr>
     * <tr>
     * <td span=3><br>
     * </td>
     * </tr>
     * <tr>
     * <td><tt>"attrPrefix:attrSuffix"</tt></td>
     * <td><tt>"value"</tt></td>
     * <td>&lt ;attrPrefix attrSuffix="value"/&gt ;</td>
     * </tr>
     * <tr>
     * <td>Example<br>
     * <tt>"acquisation:mode"</tt></td>
     * <td><tt>"imaging"</tt></td>
     * <td>&lt ;acquisation mode="imaging"/&gt ;</td>
     * </tr>
     * <tr>
     * <td span=3><br>
     * </td>
     * </tr>
     * <tr>
     * <td><tt>".string1.string2.string3 ..."</tt></td>
     * <td><tt>"value"</tt></td>
     * <td>&lt ;meta_string1_string2_string3 ...&gt ;
     * value&lt ;/meta_string1_string2_string3 ...&gt ;</td>
     * </tr>
     * <tr>
     * <td>Example<br>
     * <tt>"gui.collapsed"</tt></td>
     * <td><tt>"true"</tt></td>
     * <td>&lt ;meta_gui_collapsed&gt ; true&lt ;/meta_gui_collapsed&gt ;</td>
     * </tr>
     * </table>
     * 
     * <h3>Reserved Strings</h3>
     * 
     * In order to allow the XML conversion described above to work the
     * following reserved strings must not be used as AV table attribute names
     * (ATTR_...):
     * <ul>
     * <li><tt><b>value</b></tt> Used to encode value Vectors.
     * <li><tt><b>:type</b></tt> Used to encode the the type string of the
     * SpItem (see {@link #typeStr()}).
     * <li><tt><b>:subtype</b></tt> Used to encode the the subtype string of
     * the SpItem (see {@link #subtypeStr()}).
     * <li><tt><b>meta...</b></tt> The prfix meta is used to encode
     * attributes starting and containing the character '.'.
     * </ul>
     * 
     * <h3>SpAvTable attributes that cannot be converted</h3>
     * 
     * Appart from attributes starting with the character '.' (e.g.
     * ".gui.collapsed") attributes should not contain characters that cannot be
     * used in XML element or attribute names. Normally '.' is such a forbidden
     * character but if an attribute starts with '.' then it is converted to and
     * from XML as described in the table above.
     * 
     * <h3>name and :name</h3>
     * 
     * If one is table entry is <tt>acquisation:mode = "imaging"</tt> and
     * another is <tt>acquisation = "on"</tt> then they will be translated as
     * 
     * <pre>
     *   &lt ;acquisation mode=&quot ;imaging&quot ;&gt ;
     *   &lt ;acquisation&gt ;on&lt ;/acquisation&gt ;
     * </pre>
     * 
     * and <b>not</b> as
     * 
     * <pre>
     *   &lt ;acquisation mode=&quot ;imaging&quot ;&gt ;on&lt ;/acquisation&gt ;
     * </pre>
     * 
     * <h3>Overriding processAvAttribute()</h3>
     * 
     * Subclasses can override this method. In that case all the above rules and
     * restrictions could become meaningless. In a typical case the method
     * processAvAttribute would check for a particular attribute and depending
     * on which attribute it is either super.processAvAttribute() (i.e.
     * SpItem.processAvAttribute) would be called (for example for standard
     * attrbutes such as ".gui.collapsed") or the subclass would create a
     * differend XML representation. <b>In the latter case some or all of the
     * XML reading methods {@link #processXmlElementStart},
     * {@link #processXmlElementContent},{@link #processXmlElementEnd},{@link #processXmlAttribute}
     * will need to be adjusted accordingly so that the XML produced by
     * processAvAttribute can be read back in again.</b>
     * 
     * @param avAttr
     *            SpAvTable attribute String
     * @param indent
     *            String by which the XML representation of this SpAvTable entry
     *            should be indented
     * @param xmlBuffer
     *            StringBuffer to which the XML representation of this SpAvTable
     *            is appended
     */
	protected void processAvAttribute( String avAttr , String indent , StringBuffer xmlBuffer )
	{
		if( avAttr.indexOf( "xmlns" ) != -1 )
			return ;

		if( !avAttr.startsWith( ":" ) )
		{
			if( _avTable.size( avAttr ) == 1 )
			{
				xmlBuffer.append( "\n  " + indent + avToXml( avAttr , _avTable.get( avAttr ) ) ) ;
			}
			else
			{
				String value ;
				xmlBuffer.append( "\n  " + indent + "<" + avAttr + ">" ) ;

				for( int i = 0 ; i < _avTable.size( avAttr ) ; i++ )
				{
					value = _avTable.get( avAttr , i ) ;

					if( ( value != null ) && ( value.length() > 0 ) )
						xmlBuffer.append( "\n    " + indent + "<value>" + value + "</value>" ) ;
					else
						xmlBuffer.append( "\n    " + indent + "<value>" + "</value>" ) ;
				}
				xmlBuffer.append( "\n  " + indent + "</" + avAttr + ">" ) ;
			}
		}
	}

	/**
     * This method can be called by an external XML parser to indicate the start
     * of an XML element.
     * 
     * Subclassed that read and write their individual XML format can override
     * this method.
     * 
     * @param name
     *            XML element name
     * 
     * @see #processXmlElementContent(java.lang.String,java.lang.String,int)
     */
	public void processXmlElementStart( String name ){}

	/**
     * This method can be called by an external XML parser when an XML element
     * is parsed.
     * 
     * Subclassed that read and write their individual XML format can override
     * this method.
     * 
     * @param name
     *            XML element name
     * @param value
     *            Characters inside the XML element
     * 
     */
	public void processXmlElementContent( String name , String value )
	{
		processXmlElementContent( name , value , 0 ) ;
	}

	/**
     * This method can be called by an external XML parser when an XML element
     * is parsed.
     * 
     * This methods is used when the XML contains a number of
     * <tt>&lt ;value&gt ;text&lt ;/value&gt ;</tt> elements. These value elements
     * are used as the XML representation of mulitple values of one
     * {@link gemini.sp.SpAvTable} attribute. Each SpAvTable attribute can have
     * multiple values each of which is stored at a certain position in a
     * Vector. (see {@link gemini.sp.SpAvTable#get(java.lang.String,int)},
     * {@link gemini.sp.SpAvTable#set(java.lang.String,java.lang.String,int)}
     * etc.)
     * 
     * Subclassed that read and write their individual XML format can override
     * this method.
     * 
     * @param name
     *            XML element name
     * @param value
     *            <tt><i>value</i></tt> in
     *            <tt>&lt ;value&gt ;<i>value</i>&lt ;/value&gt ;</tt> element
     *            number <i>pos</i>
     * @param pos
     *            Indicates which
     *            <tt>&lt ;value&gt ;<i>value</i>&lt ;/value&gt ;</tt> element's
     *            value is returned. Corresponds to the position of the value in
     *            side the Vector of the SpAvTable attribute <i>name</i>.
     */
	public void processXmlElementContent( String name , String value , int pos )
	{
		if( ( name != null ) && ( name.length() > 0 ) )
		{
			// Hacky fix for chnage in priority - try to work out a better fix later
			if( name.equals( "priority" ) )
			{
				if( value.equals( "High" ) )
					value = "1" ;
				else if( value.equals( "Medium" ) )
					value = "49" ;
				if( value.equals( "Low" ) )
					value = "99" ;
			}
			else if( name.equals( "velocityFrame" ) && value.equals( "LSR" ) )
			{
				value = "LSRK" ;
			}

			if( name.startsWith( XML_META_PREFIX ) )
				_avTable.noNotifySet( name.substring( XML_META_PREFIX.length() ).replace( '_' , '.' ) , value , pos ) ;
			else
				_avTable.noNotifySet( name , value , pos ) ;
		}
	}

	/**
     * This method can be called by an external XML parser to indicate the end
     * of an XML element.
     * 
     * Subclassed that read and write their individual XML format can override
     * this method.
     * 
     * @param name
     *            XML element name
     * 
     * @see #processXmlElementContent(java.lang.String,java.lang.String,int)
     */
	public void processXmlElementEnd( String name ){}

	/**
     * This method can be called by an external XML parser when an XML attribute
     * is parsed.
     * 
     * Subclassed that read and write their individual XML format can override
     * this method.
     * 
     * @param elementName
     *            name of the XML element which has this XML attribute
     * @param attributeName
     *            XML attribute name
     * @param value
     *            value of the XML attribute
     */
	public void processXmlAttribute( String elementName , String attributeName , String value )
	{
		if( ( attributeName == null ) || ( value == null ) || ( attributeName.length() < 1 ) || ( value.length() < 1 ) )
			return ;

		if( attributeName.equals( XML_ATTR_TYPE ) || attributeName.equals( XML_ATTR_SUBTYPE ) )
			return ;

		/*
         * If the elementName is the class name, i.e. it is an XML attribute of
         * the <SpItem> XML element, then the av attribute is ":attributeName".
         * Otherwise the av attribute is "elementName:attributeName".
         */
		if( _className.equals( elementName ) )
			_avTable.noNotifySet( ":" + attributeName , value , 0 ) ;
		else
			_avTable.noNotifySet( elementName + ":" + attributeName , value , 0 ) ;
	}

	/**
     * Convenience method.
     */
	protected final String avToXml( String avAttr , String value )
	{
		// Special case for SpNoteRef - strip any trailing numeric before writing...
		if( avAttr.startsWith( "SpNoteRef" ) )
			avAttr = "SpNoteRef:idref" ;

		if( avAttr.indexOf( ':' ) < 0 )
		{
			if( avAttr.startsWith( "." ) )
			{
				String tag = XML_META_PREFIX + avAttr.replace( '.' , '_' ) ;
				return "<" + tag + ">" + value + "</" + tag + ">" ;
			}
			else
			{
				return "<" + avAttr + ">" + asciiToXml( value ) + "</" + avAttr + ">" ;
			}
		}
		else
		{
			int index = avAttr.indexOf( ':' ) ;
			return "<" + avAttr.substring( 0 , index ) + " " + avAttr.substring( index + 1 ) + "=\"" + value + "\"/>" ;
		}
	}
	
	// copied from gemini.util.XmlUtil
	public String asciiToXml( String ascii )
	{
		if( ascii == null )
			return "" ;

		StringBuffer result = new StringBuffer() ;

		for( int i = 0 ; i < ascii.length() ; i++ )
		{
			switch( ascii.charAt( i ) )
			{
				case '<' :
					result.append( "&lt;" ) ;
					break ;
				case '>' :
					result.append( "&gt;" ) ;
					break ;
				case '&' :
					result.append( "&amp;" ) ;
					break ;
				default :
					result.append( ascii.charAt( i ) ) ;
			}
		}

		return result.toString() ;
	}
}
