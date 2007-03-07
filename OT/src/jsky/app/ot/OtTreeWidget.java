/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot;

import java.util.Enumeration;
import java.util.Vector;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import jsky.app.ot.gui.MultiSelTreeNodeWidget;
import jsky.app.ot.gui.MultiSelTreeWidget;
import jsky.app.ot.gui.TreeWidgetCellRenderer;
import gemini.sp.SpHierarchyChangeObserver;
import gemini.sp.SpInsertData;
import gemini.sp.SpItem;
import gemini.sp.SpRootItem;
import gemini.sp.SpLibrary;
import gemini.sp.SpObs;
import gemini.sp.SpMSB;
import gemini.sp.SpNote;
import gemini.sp.SpSurveyContainer;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpObsComp;
import gemini.sp.obsComp.SpInstObsComp;
import orac.util.SpItemUtilities;
import orac.jcmt.inst.*;
import orac.jcmt.iter.*;
import jsky.app.ot.util.Assert;
import jsky.app.ot.util.ClipboardHelper;
import ot.util.DialogUtil;
import ot.OtAdvancedTreeDropTarget;
import ot.OtAdvancedTreeDragSource;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeExpansionEvent;

/**
 * A helper class used when an attempt to place an observation component in
 * a scope in which another of the same subtype already exists.
 */
final class ConfirmAVClobber {
    String messg;

    public ConfirmAVClobber(SpItem[] spItems) {
	if ((spItems.length == 1) && (spItems[0] instanceof SpObsComp)) {
	    String sub = spItems[0].type().getReadable();
	    messg = "A component of subtype `" + sub + "' already " +
		"exists in this scope.  Replace it?";
	} else {
	    messg = "One or more components of the same subtype already " +
		"exist in this scope.  Replace them?"; 
	}
    }

    public boolean okToClobber() {
	int answer = DialogUtil.confirm(messg);
	if (answer == JOptionPane.YES_OPTION) {
	    return true;
	}
	return false;
    }
}


/**
 * A TreeWidget extension that contains/displays the structure of a Science
 * Program or Plan.  The TreeNodeWidgets contained in the OtTreeWidget are
 * OtTreeNodeWidgets representing a given SpItem.
 *
 * <p>The tree represents the hierarchy of Science Program items.  Methods
 * for adding, removing, and moving items around in the tree are provided.
 */
public final class OtTreeWidget extends MultiSelTreeWidget
    implements OtGuiAttributes, SpHierarchyChangeObserver, TreeExpansionListener {

    // The OtTreeNodeWidget of the root node.
    private OtTreeNodeWidget _otTNW;

    // The SpItem associated with the root node.  This is the program or
    // plan contained in the tree.
    private SpRootItem       _spProg;

    // Information global to the program or plan.
    private ProgramInfo      _progInfo;

    // If true, ignore action events
    private boolean ignoreActions = false; // added by MFO (31 July 2001)

    protected static Color GREEN = new Color(0, 192, 0);


    /**
     * Default constructor, for the Bongo builder.
     */
    public OtTreeWidget() {
	// Add a drag source to the FileTree
	OtAdvancedTreeDragSource source = new OtAdvancedTreeDragSource(this);

	// Add a drop target to the FileTree
	OtAdvancedTreeDropTarget target = new OtAdvancedTreeDropTarget(this, source);

	tree.addTreeExpansionListener(this);
    }

    /**
     * Return the program.
     */
    public SpRootItem getProg() {
	return _spProg;
    }

    /**
     * Set the ProgramInfo for the program being edited in this tree.
     */
    public void	setInfo(ProgramInfo pi) {
	_progInfo = pi;
    }

    //
    // Add tree nodes for all the children of the program item.  Returns
    // the selected node if any.  The selected node is determined by
    // the GUI_SELECTED attribute being true.
    //
    private final OtTreeNodeWidget _populateTree(OtTreeNodeWidget rootTNW) {
	OtTreeNodeWidget selected = null;
	SpItem spItem = rootTNW.getItem();

	int index = 0;
	Enumeration children = spItem.children();
	while (children.hasMoreElements()) {
	    SpItem           child = (SpItem) children.nextElement();
	    OtTreeNodeWidget tnw   = ((OtClientData) child.getClientData()).tnw;
	    tnw.setTree(this);
	    tnw.setText(child.getTitle());
	    rootTNW.add(index, tnw);

	    OtTreeNodeWidget lastSelected = _populateTree(tnw);
	    if (selected == null) {
		selected = lastSelected;
	    }

	    // Was this node selected?
	    if (child.getTable().getBool(GUI_SELECTED)) {

		// Arbitrarily prefer the first selected node encountered if there
		// is a conflict.  This can happen when your program or plan has
		// observation links.  Observation links contain observations
		// and upon a fetch the most recent version of the observation
		// is recalled.  It may have been selected at the time the
		// program was stored.

		if (selected == null) {
		    selected = tnw;
		}
		child.getTable().set(GUI_SELECTED, false);
	    }

	    // Make sure this node is expanded/collapsed correctly.
	    ignoreActions = true;  // added by MFO (31 July 2001)
	    tnw.setCollapsed( child.getTable().getBool(GUI_COLLAPSED) );
	    ignoreActions = false; // added by MFO (31 July 2001)
 
	    ++index;
	}

	return selected;
    }


    /**
     * Collapses/expands tree nodes recursively according to the
     * .gui.collapsed value of their SpItem. 
     */
    public void updateNodeExpansions() {

	Enumeration allOffsprings = _otTNW.depthFirstEnumeration();
	OtTreeNodeWidget otTreeNodeWidget = null;

	while (allOffsprings.hasMoreElements()) {
	    otTreeNodeWidget = (OtTreeNodeWidget)allOffsprings.nextElement();

	    ignoreActions = true;
	    otTreeNodeWidget.setCollapsed( otTreeNodeWidget.getItem().getTable().getBool(GUI_COLLAPSED) );
	    ignoreActions = false;
	}
    }

    private void reapChildren(SpItem item) {
	Enumeration e = item.children();
	while (e.hasMoreElements()) {
	    reapChildren((SpItem)e.nextElement());
	}
	if ( item.getTable() != null ) item.getTable().noNotifyRmAll();
	((OtTreeNodeWidget)((OtClientData) item.getClientData()).tnw).rmIcons();
    }

    public void resetProg() {
	clear();
	// Go through all the cildren and clear them up...
	reapChildren(_spProg);
	_spProg.getTable().noNotifyRmAll();
	_spProg = null;
    }


    /**
     * Forget about the current program and start over with the given
     * program.
     */
    public void	resetProg(SpRootItem prog) {

	// Erase the tree and reassign the program root
	clear();
	if (_spProg != null) {
	    _spProg.getEditFSM().deleteHierarchyChangeObserver(this);
	    _spProg.getTable().noNotifyRmAll();
	}
	_spProg = prog;
	_spProg.getEditFSM().addHierarchyChangeObserver(this);
	_spProg.getEditFSM().addHierarchyChangeObserver(SpItemUtilities.getHierarchyChangeUtil());

	// Create a new tree node widget for the program root
	_otTNW = ((OtClientData) prog.getClientData()).tnw;
	_otTNW.setTree(this);
	_otTNW.setText(prog.getTitle());

	// Fill in the tree with the program items
	add(_otTNW);
	OtTreeNodeWidget selected = _populateTree(_otTNW);

	// Init the tree display
	if (selected == null) {
	    selected = _otTNW;
	}
	selected.select();  // selects the tree node
    
        // added by MFO (06 July 2001)
	tree.expandRow(0);
    }

    /**
     * Get the selected node and remember that it was selected by storing a
     * special attribute.  When the program is returned, we will use this
     * attribute to reselect this node.
     */
    public void	rememberSelection() {
	OtTreeNodeWidget selected = (OtTreeNodeWidget) getSelectedNode();
	selected.getItem().getTable().set(GUI_SELECTED, true);
    }

    /**
     * Get the selected item (from the selected node).
     */
    public SpItem getSelectedItem() {
	OtTreeNodeWidget tnw = (OtTreeNodeWidget) getSelectedNode();
	if (tnw == null) return null;
	return tnw.getItem();
    }

    /**
     * Get an array of SpItems associated with the multi-selected nodes.
     */
    public SpItem[] getMultiSelectedItems()  {
	Vector v = getMultiSelectNodes();
	if ((v == null) || (v.size() == 0)) return null;

	int n = v.size();

	SpItem[] spItemA = new SpItem[ n ];
	for (int i=0; i<n; ++i) {
	    spItemA[i] = ((OtTreeNodeWidget) v.elementAt(i)).getItem();
	}

	return spItemA;
    }

    /**
     * Get a copy of each SpItem associated with the multi-selected nodes.
     */
    public SpItem[] getMultiSelectedItemsCopy() {
	SpItem[] spItemA = getMultiSelectedItems();
	if (spItemA == null) return null;

	for (int i=0; i<spItemA.length; ++i) {
	    spItemA[i] = spItemA[i].deepCopy();
	}

	return spItemA;
    }

    /**
     * Add a new item relative to the selected item.
     */
    public SpItem addItem(SpItem newItem)  {
	SpItem[] newItems = { newItem };
	newItems = addItems(newItems);

	if (newItems == null) return null;
	// The following has been added to allow revision tracking of
	// libraries
	if (_spProg instanceof SpLibrary) {
	    if (newItem.typeStr().equals("og")) {
// 		System.out.println("newItem is an instance of SpMSB");
		((SpMSB)newItem).setLibraryRevision();
	    }
	    else if ( newItem.typeStr().equals("ob") && ((SpObs)newItem).isMSB() ) {
// 		System.out.println("newItem is an instance of SpObs");
		((SpObs)newItem).setLibraryRevision();
	    }
	    else {
	    }
	}


	// The following is used specifically to handle JCMT
	// Hetrodyne observations.
	/*
	 * If we are adding a JCMT instrument, we need to check all of
	 * the observations in scope and update then correctly for the
	 * type of instrument.
	 */
	if (newItem instanceof orac.jcmt.inst.SpInstSCUBA || 
	    newItem instanceof orac.jcmt.inst.SpInstHeterodyne) {
	    SpItem newItemsRoot = SpTreeMan.findRootItem(newItem);
	    // Get all of the SpIterObs components below this
	    Vector obsVector    = SpTreeMan.findAllItems(newItemsRoot,"orac.jcmt.iter.SpIterFocusObs");
	    obsVector.addAll(SpTreeMan.findAllItems(newItemsRoot,"orac.jcmt.iter.SpIterJiggleObs"));
	    obsVector.addAll(SpTreeMan.findAllItems(newItemsRoot,"orac.jcmt.iter.SpIterNoiseObs"));
	    obsVector.addAll(SpTreeMan.findAllItems(newItemsRoot,"orac.jcmt.iter.SpIterPointingObs"));
	    obsVector.addAll(SpTreeMan.findAllItems(newItemsRoot,"orac.jcmt.iter.SpIterRasterObs"));
	    obsVector.addAll(SpTreeMan.findAllItems(newItemsRoot,"orac.jcmt.iter.SpIterSkydipObs"));
	    obsVector.addAll(SpTreeMan.findAllItems(newItemsRoot,"orac.jcmt.iter.SpIterStareObs"));
	    for (int i=0; i<obsVector.size(); i++) {
		// Make sure that the observation is associated with the new item and not
		// some other existing component
		SpItem obsContext     = SpTreeMan.findObsContext(((SpIterJCMTObs)obsVector.get(i)));
		SpInstObsComp obsComp = SpTreeMan.findInstrumentInContext(obsContext);
		while (obsComp == null) {
		    obsContext     = obsContext.parent();
		    if (obsContext == null) break;
		    obsComp = SpTreeMan.findInstrumentInContext(obsContext);
		}
		if ( obsComp != null && obsComp.equals(newItem)) {
		    if (newItem instanceof SpInstHeterodyne) {
			((SpIterJCMTObs)obsVector.get(i)).setupForHeterodyne();
		    }
		    else {
			((SpIterJCMTObs)obsVector.get(i)).setupForSCUBA();
		    }
		}
	    }
	}

	/*
	 * If we are adding an observe "eye" find out what instrument
	 * is being used.  If it is a JCMT instrument, update its
	 * attributes accordingly.
	 */
	if (newItem instanceof SpIterJCMTObs) {
	    // Find out what instrument is asscociated with this observation
	    SpItem obsContext     = SpTreeMan.findObsContext(newItem);
	    SpInstObsComp obsComp = SpTreeMan.findInstrumentInContext(obsContext);
	    while (obsComp == null) {
		obsContext     = obsContext.parent();
		if (obsContext == null) break;
		obsComp = SpTreeMan.findInstrumentInContext(obsContext);
	    }
	    if ( obsComp != null ) {
		if ( obsComp instanceof SpInstHeterodyne ) {
		    ((SpIterJCMTObs)newItem).setupForHeterodyne();
		}
		else {
		    ((SpIterJCMTObs)newItem).setupForSCUBA();
		}
	    }
	}
	// END OF ADDITIONAL CODE
	return newItems[0];
    }


    /**
	 * Add all the items relative to the selected item.
	 */
	public SpItem[] addItems( SpItem[] newItems )
	{

		// Figure out which node was selected, if none choose the root node
		OtTreeNodeWidget destTNW = ( OtTreeNodeWidget ) getSelectedNode();
		if( destTNW == null )
		{
			// Add to the top level, the Science Program root
			destTNW = _otTNW;
		}
		SpItem destItem = destTNW.getItem();

		/*
		 * The following are additional checks needed for the survey component. The following checks are performed (but only when inserting an SpMSB or an SpObs) - 1. If the Survey Component is outside an MSB 1.1 Only one MSB can be added, but any number of SpObs can be added within that MSB 1.2 A single SpObs can also be added but it must be marked as an SpMSB 2. If the Survey Component is inside an MSB 2.1 We can not add another SpMSB within the survey component 2.2 Any number of SpObs can be added inside the survey component 2.3 Make sure that the SpObs is not an MSB (needed for copy/paste)
		 */
		// See if we have a Survey component is scope
		boolean scInScope = false; // Survey Container in scope
		boolean scInMSB = false; // Survey Container in MSB
		SpItem sc = null;
		if( destItem instanceof SpSurveyContainer )
		{
			scInScope = true;
			sc = destItem;
		}
		SpItem parent = destItem.parent();
		while( parent != null )
		{
			if( parent instanceof SpSurveyContainer )
			{
				scInScope = true;
				sc = parent;
			}
			if( parent instanceof SpMSB && scInScope )
			{
				scInMSB = true;
			}
			parent = parent.parent();
		}

		// Now do the checks. We only need to do it if we have a survey component is scope
		if( scInScope )
		{
			for( int i = 0 ; i < newItems.length ; i++ )
			{
				if( newItems[ i ] instanceof SpMSB )
				{
					if( !scInMSB )
					{
						// If the newItem is an SpObs, we can legally insert it if
						// the destination is an MSB (insertInside)
						// or an SpObs which is not an MSB (insertAfter)
						if( newItems[ i ] instanceof SpObs && ( destItem instanceof SpMSB || ( destItem instanceof SpObs && !( ( SpObs ) destItem ).isMSB() ) ) )
						{
							continue;
						}
						Enumeration children = sc.children();
						while( children.hasMoreElements() )
						{
							SpItem child = ( SpItem ) children.nextElement();
							if( child instanceof SpObs && ( ( SpObs ) child ).isMSB() )
							{
								DialogUtil.error( this , "A Survey folder can contain at most 1 MSB and the current Obs is an MSB" );
								return null;
							}
							else if( child instanceof SpMSB && !( child instanceof SpObs ) )
							{
								DialogUtil.error( this , "A Survey folder can contain at most 1 MSB" );
								return null;
							}
						}
					}
					else
					{
						if( !( newItems[ i ] instanceof SpObs ) )
						{
							DialogUtil.error( this , "A Survey folder within an MSB can not contain another MSB" );
							return null;
						}
					}
				}
			}
		}

		// Make a special case - we can not add a skydip for a ACSIS
		// heterodyne observation
		if( !canAddSkydip( destItem , newItems ) )
		{
			DialogUtil.error( this , "Can not add a Skydip to an ACSIS heterodyne observation" );
			return null;
		}

		// make another
		if( !canAddNoise( destItem , newItems ) )
		{
			DialogUtil.error( this , "Can not add a Noise to an ACSIS heterodyne observation" );
			return null;
		}

		// First see if we can insert the items inside the selected node
		SpInsertData spID;
		spID = SpTreeMan.evalInsertInside( newItems , destItem );

		if( spID == null )
		{
			// Couldn't insert inside, so try inserting after.
			spID = SpTreeMan.evalInsertAfter( newItems , destItem );

			if( spID == null )
			{
				// Couldn't insert after either, so just give up.
				String noun = "item";
				if( newItems.length > 1 )
					noun += "s";

				DialogUtil.error( this , "Couldn't add the new " + noun + " at this point." );
				return null;
			}
		}

		return addItems( spID );
	}


    /**
	 * Add the new items using the given insertion information.
	 */
    public SpItem[] addItems(SpInsertData spID) {
	if (spID.replaceItems != null) {
	    ConfirmAVClobber clob = new ConfirmAVClobber(spID.replaceItems);
	    if (!clob.okToClobber()) return null;
	}

	if (!_addItems(spID)) return null;
	return spID.items;
    }

  /**
	 * Inserts an item after the currently selected item.
	 * 
	 * This method is currently used for inserting notes when the Note button (jsky.app.o.OtTreeToolBar) is clicked.
	 * 
	 * MFO: May 28, 2001
	 * 
	 * @param newItem
	 *            the item to be inserted.
	 */
  public SpItem [] insertItemAfter(SpItem newItem) {
    SpInsertData spID = SpTreeMan.evalInsertAfter(newItem, getSelectedItem());

    if(spID == null) {
	spID = SpTreeMan.evalInsertInside(newItem, getSelectedItem());
	if (spID == null) {
	    return null;
	}
    }

    return addItems( spID );
  }


    // Add items without worrying about clobber.
    private boolean _addItems(SpInsertData spID)  {
	if (_progInfo.online) {
	    if (!_addItemsToODB(spID)) return false;
	}

	// Add the new item to the local copy of the program
	SpTreeMan.insert(spID);
	return true;
    }

    //
    // Add items to the ODB.
    //
    // WARNING: There are several transactions with the database here.
    // If something goes wrong in the middle, then the database version
    // of the program won't match the OT version!
    //
    private boolean _addItemsToODB(SpInsertData spID) {
	/* XXX allan
	WindowManager.setBusy();
	ODBServer server = SpServer.getServer();
	SpProgKey pk     = _progInfo.progKey; Assert.notNull(pk);
	SpAccess  ac     = _progInfo.access;	 Assert.notNull(ac);

	// Remove the items that will be replaced
	if (spID.replaceItems != null) {
	    for (int i=0; i<spID.replaceItems.length; ++i) {
		if (!server.remove(ac, pk, spID.replaceItems[i].id())) {
		    WindowManager.setIdle();
		    new ErrorBox("Couldn't remove the item: " +
				 server.getProblemDescr());
		    return false;
		}
	    }
	}
      
	SpItem[] newItems = spID.items;
	String      refID = spID.referant.id();

	try {
	    // Send the first item
	    switch (spID.result) {
	    case SpTreeMan.INS_AFTER:
		newItems[0] =server.add(ac, pk, newItems[0], refID, ODBServer.SIBLING);
		break;
	    case SpTreeMan.INS_INSIDE:
		newItems[0] =server.add(ac, pk, newItems[0], refID, ODBServer.CHILD);
		break;
		//case SpTreeMan.INS_REPLACE:
		//      if ( !server.replaceAV(ac, pk, refId, newItem.getTable())) {
		//         newItem = null;
		//      }
		//      break;
	    }

	    if (newItems[0] == null) {
		new ErrorBox("Couldn't add the item:" + server.getProblemDescr());
		return false;
	    }

	    // Send the rest of the items.
	    for (int i=1; i<newItems.length; ++i) {
		refID       = newItems[i-1].id();
		newItems[i] = server.add(ac, pk, newItems[i], refID, ODBServer.SIBLING);

		if (newItems[i] == null) {
		    new ErrorBox("Couldn't add the item:" + server.getProblemDescr());
		    return false;
		}
	    }

	} finally {
	    WindowManager.setIdle();
	}
	XXX allan */
	return true;
    }


    /**
     * Remove the selected item (unless its the root, the Science Program itself).
     */
    public SpItem rmSelectedItem() {
	OtTreeNodeWidget tnw = (OtTreeNodeWidget) getSelectedNode();
	if (tnw == null) {
	    DialogUtil.error(this, "You have to select an item to delete.");
	    return null;
	}
	if (tnw == (OtTreeNodeWidget) getRoot()) {
	    DialogUtil.error(this, "You can't delete the Science Program!");
	    return null;
	}

	SpItem[] spItemA = { tnw.getItem() };
	return rmItems(spItemA) ? spItemA[0] : null;
    }

    /**
     * Remove the multi-selected items.
     */
    public SpItem[] rmMultiSelectedItems() {
	SpItem[] spItemA = getMultiSelectedItems();
	if (spItemA == null) return null;
	return rmItems(spItemA) ? spItemA : null;
    }

    /**
     * rmMultiSelectedItems doesn't seem to work with the new multi selection in MultiSelTreeWidget anymore.
     * 
     * Replaced with a quick hack which seems to work in most cases.
     * MFO: May 29, 2001
     */
    public void rmAllSelectedItems() {
      TreePath [] paths = getSelectionPaths();
      boolean attemptToDeleteRoot = false;
      if (paths != null && paths.length > 0) {
	for (int i = 0; i < paths.length; i++) {
          tree.setSelectionPath(paths[i]);
	  // This is a hack to prevent freezing when the Science Program node is dropped
	  // into the waste bin under Linux.
	  // Normally one would just try to apply rmSelectedItem without checking whether it is
	  // the Science Program node. rmSelectedItem would then call
	  // DialogUtil.error("You have to select an item to delete."). But this can cause the OT under Linux
	  // to freeze. However, throwing an Exception and catching it in the calling class and calling
	  // DialogUtil.error there works fine. MFO, May 31, 2001
	  if((OtTreeNodeWidget) getSelectedNode() == (OtTreeNodeWidget) getRoot()) {
            attemptToDeleteRoot = true;
	  }
	  else {
	    rmSelectedItem();
	  }  
	}

	if(attemptToDeleteRoot) {
          throw new IllegalArgumentException("You can't delete the Science Program!");
	}
      }
    }

    /**
     * Remove the given items from the tree.
     */
    public boolean rmItems(SpItem[] spItems) {
	// Make sure the srcItem can be extracted from its current position
	if (!SpTreeMan.evalExtract(spItems)) {
	    return false;
	}

	// If online, then send the change to the server
	if (_progInfo.online) {
	    if (!_rmItemsFromODB(spItems)) return false;
	}

	// Update the program and tree
	SpTreeMan.extract(spItems);
	return true;
    }

    //
    // Remove the items from the ODB.
    //
    // WARNING: There are several transactions with the database here.
    // If something goes wrong in the middle, then the database version
    // of the program won't match the OT version!
    //
    private boolean _rmItemsFromODB(SpItem[] spItems) {
	/* XXX allan
	WindowManager.setBusy();
	ODBServer server = SpServer.getServer();
	SpProgKey pk     = _progInfo.progKey;	Assert.notNull(pk);
	SpAccess  ac     = _progInfo.access;	Assert.notNull(ac);

	try {
	    for (int i=0; i<spItems.length; ++i) {
		if (!server.remove(ac, pk, spItems[i].id())) {
		    DialogUtil.error("Couldn't remove the item: "+server.getProblemDescr());
		    return false;
		}
	    }
	} finally {
	    WindowManager.setIdle();
	}
	XXX allan */
	return true;
    }

    /**
     * Cut the selected item and place it on the clipboard.
     */
    public void	cutToClipboard()  {
	if (multipleItemsSelected()) {
	    SpItem[] spItemA = rmMultiSelectedItems();
	    if (spItemA == null) return;

	    // Put the items in the clipboard
	    ClipboardHelper.setClipboard(spItemA);
	} else {
	    SpItem spItem = rmSelectedItem();
	    if (spItem == null) return;

	    // Put the item in the clipboard
	    ClipboardHelper.setClipboard(spItem);
	}
    }

    /**
     * Copy the selected item to the clipboard.
     */
    public void	copyToClipboard()  {
	if (multipleItemsSelected()) {
	    SpItem[] spItemA = getMultiSelectedItemsCopy();
	    ClipboardHelper.setClipboard(spItemA);

	} else {
	    OtTreeNodeWidget tnw = (OtTreeNodeWidget) getSelectedNode();
	    if (tnw == null) return;

	    SpItem spItem = tnw.getItem();
	    SpItem spCopy = spItem.deepCopy();
	    ClipboardHelper.setClipboard(spCopy);
	}
    }

    /**
     * Paste an item from the clipboard.
     */
    public void	pasteFromClipboard() {
	Object obj = ClipboardHelper.getClipboard();
	if (obj == null) return;


	if (obj instanceof SpItem) {
	    SpItem spCopy = ((SpItem) obj).deepCopy();
	    addItem( spCopy );
	} else if (obj instanceof SpItem[]) {
	    SpItem[] a = (SpItem[]) obj;
	    for (int i=0; i<a.length; ++i) {
		a[i] = a[i].deepCopy();
	    }

	    addItems(a);
	}
    }


    /**
     * Move the items from their current position based upon the information
     * in the SpInsertData structure.
     */
    public void	mvItems(SpInsertData spID) {
	// Make sure the items can be extracted from their current position.
	if (!SpTreeMan.evalExtract(spID.items)) return;

	// If necessary, ask if ok to clober.
	if (spID.replaceItems != null) {
	    ConfirmAVClobber clob = new ConfirmAVClobber(spID.replaceItems);
	    if (!clob.okToClobber()) return;
	}

	// Move the items without worrying about clobber.
	_mvItems(spID);
    }

    //
    // Move the items in the ODB.
    //
    // WARNING: There are several transactions with the database here.
    // If something goes wrong in the middle, then the database version
    // of the program won't match the OT version!
    //
    private boolean _mvItemsInODB(SpInsertData spID) {
	/* XXX allan
	if (spID.replaceItems != null) {
	    if (!_rmItemsFromODB(spID.replaceItems)) return false;
	}

	WindowManager.setBusy();

	ODBServer server = SpServer.getServer();
	SpProgKey pk     = _progInfo.progKey;	Assert.notNull(pk);
	SpAccess  ac     = _progInfo.access;		Assert.notNull(ac);

	int posn = ODBServer.SIBLING;
	if (spID.result == SpTreeMan.INS_INSIDE) {
	    posn = ODBServer.CHILD;
	}

	SpItem[] spItems  = spID.items;
	SpItem   destItem = spID.referant;

	try {
	    // Move the first item.
	    if (!server.move(ac,pk, spItems[0].id(), destItem.id(), posn)) {
		String problem = server.getProblemDescr();
		DialogUtil.error("Couldn't remove the item: " + problem);
		return false;
	    }

	    // Move the remaining items.
	    posn = ODBServer.SIBLING;
	    for (int i=1; i<spItems.length; ++i) {
		destItem = spItems[i-1];
		if (!server.move(ac, pk, spItems[i].id(), destItem.id(), posn)) {
		    String problem = server.getProblemDescr();
		    DialogUtil.error("Couldn't remove the item: " + problem);
		    return false;
		}
	    }
	} finally {
	    WindowManager.setIdle();
	}
	XXX allan */
	return true;
    }

    //
    // This move item method does the actual work of moving a given item,
    // without worrying about clobber.
    //
    private void _mvItems(SpInsertData spID) {
	// Send the change to the ODB if editing online
	if (_progInfo.online) {
	    if (!_mvItemsInODB(spID)) return;
	}

	OtTreeNodeWidget tnw = (OtTreeNodeWidget) getSelectedNode();

	//SpTreeMan.extract(spID.items);
	//SpTreeMan.insert(spID);
	SpTreeMan.move(spID);

	// If tnw is still in this tree, then select it.
	if (tnw.getTreeWidget() == this) {
	    tnw.setSelected(true);
	}
    }

    /**
     * Print the program to stdout
     */
    public void	showProg()  {
	_spProg.print();
    }

    /**
     * Get the TNW for the given spItem, creating it if it doesn't exist.
     */
    private final OtTreeNodeWidget _getSpItemTNW(SpItem spItem) {
	OtClientData cd = (OtClientData) spItem.getClientData();
	Assert.notNull(cd);

	OtTreeNodeWidget tnw = cd.tnw;
	tnw.setTree(this);
	if ((spItem.child() != null) && (tnw.getWidgetCount() == 0)) {
	    _populateTree(tnw);
	}
	return tnw;
    }

    /**
     * Items were added to the program, add corresponding tree nodes.
     */
    public void	spItemsAdded(SpItem parent, SpItem[] children, SpItem afterChild)  {
// 	System.out.println("------------- spItemsAdded");
// 	System.out.println("*** parent     = " + parent);
// 	System.out.println("*** chilren:");
// 	for (int i=0; i<children.length; ++i) {
// 	   System.out.println("    " + children[i]);
// 	}
// 	System.out.println("*** afterChild = " + afterChild);
// 	System.out.println("--------------------------");


	// Get the parent TNW
	OtTreeNodeWidget parentTNW = ((OtClientData) parent.getClientData()).tnw;
	parentTNW.setTree(this);

	// Find the position (pos) where the first child should be added.  This
	// should be 0 (first) if afterChild is null, or else whatever the index
	// of afterChild is plus one if not null.
	int pos = 0;
	if (afterChild != null) {
	    OtTreeNodeWidget tnw = ((OtClientData) afterChild.getClientData()).tnw;
	    tnw.setTree(this);
	    pos                  = tnw.getMyIndex() + 1;
	}

	// Add the children TNW.
	MultiSelTreeNodeWidget[] tnwA = new MultiSelTreeNodeWidget[children.length];
	for (int i=0; i<children.length; ++i) {
	    tnwA[i] = _getSpItemTNW(children[i]);
	    parentTNW.add(pos++, tnwA[i]);
	    tnwA[i].setTree(this);
	}

	// Make sure the parent is showing and expanded.
	parentTNW.expand();
	parentTNW.uncover();

	// Multi-select the new set
	setMultiSelectNodes(tnwA);
    }

    // Remove the nodes without trying to select a new node
    private void _spItemsRemoved(SpItem parent, SpItem[] children) {
	// Remove each child.
	for (int i=0; i<children.length; ++i) {
	    OtTreeNodeWidget tnw = ((OtClientData) children[i].getClientData()).tnw;
	    tnw.remove();
	}

	// Repaint the widget containing the parentTNW
	OtTreeNodeWidget parentTNW = ((OtClientData) parent.getClientData()).tnw;
	parentTNW.setTree(this);

	/* XXX allan
	org.freebongo.gui.Widget grandparent = parentTNW.getParent();
	if (grandparent != null) {
	    grandparent.repaint();
	} else {
	    parentTNW.repaint();
	}
	XXX allan */
    }

    /**
     * Items were removed from the program, remove corresponding tree nodes.
     */
    public void	spItemsRemoved(SpItem parent, SpItem[] children) {
	// See if any one of these nodes is the selected node.
	OtTreeNodeWidget selectedTNW = null;
	for (int i=0; i<children.length; ++i) {
	    // Is this tree node selected?
	    OtTreeNodeWidget tnw = ((OtClientData) children[i].getClientData()).tnw;
	    if (tnw.getSelectedNode() != null) {
		selectedTNW = tnw;
		break;
	    }
	}

	// The selected tree node will be removed, so select something else.
	if (selectedTNW != null) {
	    // Select some other tree node.  First try the next node following
	    // the group, then try the previous node before the group, then
	    // failing those, just select the parent.

	    OtTreeNodeWidget parentTNW = (OtTreeNodeWidget) selectedTNW.getParent();
	    Vector v = parentTNW.getChildren();

	    SpItem        spItem = children[ children.length - 1 ];
	    OtTreeNodeWidget tnw = ((OtClientData) spItem.getClientData()).tnw;

	    int i = tnw.getMyIndex();
	    if (i+1 < v.size()) {
		((OtTreeNodeWidget) v.elementAt(i+1)).select();
	    } else {
		spItem = children[0];
		tnw    = ((OtClientData) spItem.getClientData()).tnw;
		i      = tnw.getMyIndex();
		if (i > 0) {
		    ((OtTreeNodeWidget) v.elementAt(i-1)).select();
		} else {
		    parentTNW.select();
		}
	    }
	}

	_spItemsRemoved(parent, children);
    }

    /**
     * Notification that items have been moved from a node in the program
     * to another node in the program.
     */
    public void	spItemsMoved(SpItem oldParent, SpItem[] children,
			     SpItem newParent, SpItem afterChild) {
	_spItemsRemoved(oldParent, children);
	spItemsAdded(newParent, children, afterChild);
    }


    /**
     * Sets .gui.collapsed to true in the respective SpItem.
     *
     * Added by MFO (31 July 2001).
     */
    public void treeCollapsed(TreeExpansionEvent e) {
    
	if(ignoreActions) {
	    return;
	}

	((OtTreeNodeWidget)e.getPath().getLastPathComponent()).getItem().getTable().set(GUI_COLLAPSED, true);
    }

    /**
     * Sets .gui.collapsed to false in the respective SpItem.
     *
     * Added by MFO (31 July 2001).
     */
    public void treeExpanded(TreeExpansionEvent e) {
    
	if(ignoreActions) {
	    return;
	}

	((OtTreeNodeWidget)e.getPath().getLastPathComponent()).getItem().getTable().set(GUI_COLLAPSED, false);
    }    


    /**
     * This method implements multiple colour display of the text strings of
     * Observation and MSB components in the tree.
     *
     * MSB black: {@link gemini.sp.SpMSB#getNumberRemaining()} = 1 or more.
     * MSB gray:  {@link gemini.sp.SpMSB#getNumberRemaining()} = 0.
     *
     * Observation black: Observation is not optional.
     * Observation green: Observation is optional.
     */
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
						  boolean selected,
                                                  boolean expanded,
						  boolean leaf,
						  int row,
						  boolean hasFocus) {

      OtTreeNodeWidget treeNodeWidget = null;

      if((value != null) && value instanceof OtTreeNodeWidget) {
        treeNodeWidget = (OtTreeNodeWidget)value;
      }

      if( (treeNodeWidget == null) || ( (!(treeNodeWidget.getItem() instanceof SpMSB)) &&
                                        (!(treeNodeWidget.getItem() instanceof SpNote)) ) ) {
        return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
      }

      TreeWidgetCellRenderer resultComponent =
        (TreeWidgetCellRenderer)super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

      if((treeNodeWidget != null) &&(treeNodeWidget.getItem() instanceof SpMSB)) {
        SpMSB spMSB = (SpMSB)treeNodeWidget.getItem();

	if(spMSB.getNumberRemaining() < 1) {
          resultComponent.setForeground(Color.gray);
	}
	if (spMSB.isSuspended()) {
	    resultComponent.setForeground(Color.red);
	}
      }


      if((treeNodeWidget != null) && (treeNodeWidget.getItem() instanceof SpObs)) {
        SpObs spObs = (SpObs)treeNodeWidget.getItem();

        // Note that only SpObs with getIsMSB() == false can be optional. So unless there is something wrong with
	// the Science program spObs.isOptional() implies spObs.getIsMSB() == false.
	if(spObs.isOptional()) {
          resultComponent.setForeground(GREEN);
	}
	if ( spObs.isMSB() && spObs.isSuspended() ) {
	    resultComponent.setForeground(Color.red);
	}
      }

      if((treeNodeWidget != null) && (treeNodeWidget.getItem() instanceof SpNote)) {
	if(((SpNote)treeNodeWidget.getItem()).isObserveInstruction()) {
          resultComponent.setForeground(Color.blue);
	}
      }

      return resultComponent;
    }

    public void autoAssignPriority() {
	int numberMSBs = 0;
	int nMin = 0;
	int nLow = 0;

	// Find all of the children of the program
	Enumeration children = _spProg.children();
	// For now just count the MSBs and Obs that are MSBs
	while (children.hasMoreElements()) {
	    SpItem child = (SpItem) children.nextElement();
	    if ( (child instanceof SpMSB) ||
		 (child instanceof SpObs && ((SpObs)child).isMSB()) ) {
		numberMSBs ++;
	    }
	}
	if ( numberMSBs > 99) { // Limit to priority
	    float x = (float)numberMSBs/99;
	    // Get the smallest number of priorities that can be assigned to high ranking MSBs
	    nMin = (int) x;
	    nLow = 99*(nMin+1)-numberMSBs;
	    children = _spProg.children();
	    int priority = 1;
	    int priorityCount = 1;
	    int msbCount = 1;
	    boolean updateDone = false;
	    System.out.println("Should update nMin at msbCount of "+(nLow*nMin));
	    while (children.hasMoreElements()) {
		SpItem child = (SpItem) children.nextElement();
		if ( (child instanceof SpMSB) ||
		     (child instanceof SpObs && ((SpObs)child).isMSB()) ) {
		    System.out.println("setting msb "+msbCount+" to priority "+priority);
		    ((SpMSB)child).setPriority(priority);
		    if ( msbCount == nLow*nMin && !updateDone ) {
			System.out.println("Restting nMib at msbCount "+msbCount);
			nMin++;
			priority++;
			priorityCount = 1;
			msbCount++;
			updateDone = true;
			continue;
		    }
		    if (priorityCount == nMin) {
			priorityCount=1;
			priority++;
			msbCount++;
			continue;
		    }
		    msbCount++;
		    priorityCount++;
		}
	    }
	}
	else {
	    int priority = 1;
	    children = _spProg.children();
	    while (children.hasMoreElements()) {
		SpItem child = (SpItem) children.nextElement();
		if ( (child instanceof SpMSB) ||
		     (child instanceof SpObs && ((SpObs)child).isMSB()) ) {
		    ((SpMSB)child).setPriority(priority);
		    priority++;
		}
	    }
	}
    }

    private boolean canAddSkydip( SpItem target , SpItem[] items )
	{
		SpInstObsComp inst = SpTreeMan.findInstrument( target );
		if( !( inst instanceof SpInstHeterodyne ) )
			return true ;
		for( int i = 0 ; i < items.length ; i++ )
		{
			String subType = items[ i ].subtypeStr() ;
			if( subType.equals( "skydipObs" ) )
			{
				return false;
			}
		}
		return true;
	}  

	private boolean canAddNoise( SpItem target , SpItem[] items )
	{
		SpInstObsComp inst = SpTreeMan.findInstrument( target );
		if( !( inst instanceof SpInstHeterodyne ) )
			return true ;
		for( int i = 0 ; i < items.length ; i++ )
		{
			String subType = items[ i ].subtypeStr() ;
			if( subType.equals( "noiseObs" ) )
			{
				return false;
			}
		}
		return true;
	}    
}

