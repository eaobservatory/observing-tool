/*
 * Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * $Id$
 */

package jsky.app.ot ;

import java.util.Enumeration ;
import java.util.Vector ;
import java.awt.Color ;
import java.awt.Component ;
import javax.swing.JOptionPane ;
import javax.swing.JTree ;
import javax.swing.tree.TreePath ;
import jsky.app.ot.gui.MultiSelTreeNodeWidget ;
import jsky.app.ot.gui.MultiSelTreeWidget ;
import jsky.app.ot.gui.TreeWidgetCellRenderer ;
import gemini.sp.SpHierarchyChangeObserver ;
import gemini.sp.SpInsertData ;
import gemini.sp.SpItem ;
import gemini.sp.SpRootItem ;
import gemini.sp.SpLibrary ;
import gemini.sp.SpObs ;
import gemini.sp.SpObsContextItem;
import gemini.sp.SpMSB ;
import gemini.sp.SpNote ;
import gemini.sp.SpSurveyContainer ;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpObsComp ;
import gemini.sp.obsComp.SpInstObsComp ;
import orac.util.SpItemUtilities ;
import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.inst.SpInstSCUBA2 ;
import orac.jcmt.iter.SpIterJCMTObs ;
import gemini.util.Assert ;
import jsky.app.ot.util.ClipboardHelper ;
import ot.util.DialogUtil ;
import ot.OtAdvancedTreeDropTarget ;
import ot.OtAdvancedTreeDragSource ;
import javax.swing.event.TreeExpansionListener ;
import javax.swing.event.TreeExpansionEvent ;
import javax.swing.tree.TreeNode ;

import orac.jcmt.iter.SpIterFocusObs ;
import orac.jcmt.iter.SpIterJiggleObs ;
import orac.jcmt.iter.SpIterNoiseObs ;
import orac.jcmt.iter.SpIterPointingObs ;
import orac.jcmt.iter.SpIterRasterObs ;
import orac.jcmt.iter.SpIterSkydipObs ;
import orac.jcmt.iter.SpIterStareObs ;
import orac.jcmt.iter.SpIterFlatObs ;

/**
 * A helper class used when an attempt to place an observation component in
 * a scope in which another of the same subtype already exists.
 */
final class ConfirmAVClobber
{
	public ConfirmAVClobber(){}

	public static boolean okToClobber( SpItem[] spItems )
	{
		String messg ;
		boolean canClobber = false ;
		if( spItems != null && spItems.length > 0 )
		{
			SpItem spItem = spItems[ 0 ] ;
			String sub = spItem.type().getReadable() ;
			if( ( spItems.length == 1 ) && ( spItem instanceof SpObsComp ) )
				messg = "A component of subtype `" + sub + "' already exists in this scope.  Replace it?" ;
			else
				messg = "One or more components of the same subtype already exist in this scope.  Replace them?" ;
			int answer = DialogUtil.confirm( messg ) ;
			canClobber = ( answer == JOptionPane.YES_OPTION ) ;
		}
		return canClobber ;
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
@SuppressWarnings( "serial" )
public final class OtTreeWidget extends MultiSelTreeWidget implements OtGuiAttributes , SpHierarchyChangeObserver , TreeExpansionListener
{
	// The OtTreeNodeWidget of the root node.
	private OtTreeNodeWidget _otTNW ;

	// The SpItem associated with the root node.  This is the program or plan contained in the tree.
	private SpRootItem _spProg ;

	// If true, ignore action events
	private boolean ignoreActions = false ; // added by MFO (31 July 2001)
	protected static Color GREEN = new Color( 0 , 192 , 0 ) ;

	/**
	 * Default constructor, for the Bongo builder.
	 */
	public OtTreeWidget()
	{
		// Add a drag source to the FileTree
		OtAdvancedTreeDragSource source = new OtAdvancedTreeDragSource( this ) ;

		// Add a drop target to the FileTree
		new OtAdvancedTreeDropTarget( this , source ) ;

		tree.addTreeExpansionListener( this ) ;
	}

	/**
	 * Return the program.
	 */
	public SpRootItem getProg()
	{
		return _spProg ;
	}

	//
	// Add tree nodes for all the children of the program item.  Returns
	// the selected node if any.  The selected node is determined by
	// the GUI_SELECTED attribute being true.
	//
	private final OtTreeNodeWidget _populateTree( OtTreeNodeWidget rootTNW )
	{
		OtTreeNodeWidget selected = null ;
		SpItem spItem = rootTNW.getItem() ;

		int index = 0 ;
		Enumeration<SpItem> children = spItem.children() ;
		while( children.hasMoreElements() )
		{
			SpItem child = children.nextElement() ;
			OtTreeNodeWidget tnw = getTreeNodeWidgetForSpItem( child ) ;
			if( tnw != null )
			{
				tnw.setTree( this ) ;
				tnw.setText( child.getTitle() ) ;
				rootTNW.add( index , tnw ) ;
	
				OtTreeNodeWidget lastSelected = _populateTree( tnw ) ;
				if( selected == null )
					selected = lastSelected ;
	
				// Was this node selected?
				if( child.getTable().getBool( GUI_SELECTED ) )
				{
					/*
					 * Arbitrarily prefer the first selected node encountered if there is a conflict.  
					 * This can happen when your program or plan has observation links.  
					 * Observation links contain observations and upon a fetch the most recent version of the observation is recalled.  
					 * It may have been selected at the time the program was stored.
					 */
	
					if( selected == null )
						selected = tnw ;
	
					child.getTable().set( GUI_SELECTED , false ) ;
				}
	
				// Make sure this node is expanded/collapsed correctly.
				ignoreActions = true ; // added by MFO (31 July 2001)
				tnw.setCollapsed( child.getTable().getBool( GUI_COLLAPSED ) ) ;
				ignoreActions = false ; // added by MFO (31 July 2001)
	
				++index ;
			}
		}
		return selected ;
	}

	/**
	 * Collapses/expands tree nodes recursively according to the
	 * .gui.collapsed value of their SpItem. 
	 */
	public void updateNodeExpansions()
	{
		Enumeration<?> allOffsprings = _otTNW.depthFirstEnumeration() ;
		OtTreeNodeWidget otTreeNodeWidget = null ;

		while( allOffsprings.hasMoreElements() )
		{
			otTreeNodeWidget = ( OtTreeNodeWidget )allOffsprings.nextElement() ;

			ignoreActions = true ;
			otTreeNodeWidget.setCollapsed( otTreeNodeWidget.getItem().getTable().getBool( GUI_COLLAPSED ) ) ;
			ignoreActions = false ;
		}
	}

	private void reapChildren( SpItem item )
	{
		Enumeration<SpItem> e = item.children() ;
		while( e.hasMoreElements() )
			reapChildren( e.nextElement() ) ;

		if( item.getTable() != null )
			item.getTable().noNotifyRmAll() ;
		
		OtTreeNodeWidget otnw = getTreeNodeWidgetForSpItem( item ) ;
		if( otnw != null )
			otnw.rmIcons() ;
	}

	public void resetProg()
	{
		clear() ;
		// Go through all the cildren and clear them up...
		reapChildren( _spProg ) ;
		_spProg.getTable().noNotifyRmAll() ;
		_spProg = null ;
	}

	/**
	 * Forget about the current program and start over with the given
	 * program.
	 */
	public void resetProg( SpRootItem prog )
	{
		// Erase the tree and reassign the program root
		clear() ;
		if( _spProg != null )
		{
			_spProg.getEditFSM().deleteHierarchyChangeObserver( this ) ;
			_spProg.getTable().noNotifyRmAll() ;
		}
		_spProg = prog ;
		_spProg.getEditFSM().addHierarchyChangeObserver( this ) ;
		_spProg.getEditFSM().addHierarchyChangeObserver( SpItemUtilities.getHierarchyChangeUtil() ) ;

		// Create a new tree node widget for the program root
		_otTNW = ( ( OtClientData )prog.getClientData() ).tnw ;
		_otTNW.setTree( this ) ;
		_otTNW.setText( prog.getTitle() ) ;

		// Fill in the tree with the program items
		add( _otTNW ) ;
		OtTreeNodeWidget selected = _populateTree( _otTNW ) ;

		// Init the tree display
		if( selected == null )
			selected = _otTNW ;

		selected.select() ; // selects the tree node

		// added by MFO (06 July 2001)
		tree.expandRow( 0 ) ;
	}

	/**
	 * Get the selected node and remember that it was selected by storing a
	 * special attribute.  When the program is returned, we will use this
	 * attribute to reselect this node.
	 */
	public void rememberSelection()
	{
		OtTreeNodeWidget selected = ( OtTreeNodeWidget )getSelectedNode() ;
		selected.getItem().getTable().set( GUI_SELECTED , true ) ;
	}

	/**
	 * Get the selected item (from the selected node).
	 */
	public SpItem getSelectedItem()
	{
		SpItem returnable = null ;
		OtTreeNodeWidget tnw = ( OtTreeNodeWidget )getSelectedNode() ;
		if( tnw != null )
			returnable = tnw.getItem() ;
		
		return returnable ;
	}

	/**
	 * Get an array of SpItems associated with the multi-selected nodes.
	 */
	public SpItem[] getMultiSelectedItems()
	{
		SpItem[] spItemA = null ;
		Vector<Object> v = getMultiSelectNodes() ;
		if( ( v != null ) && ( v.size() != 0 ) )
		{
			int n = v.size() ;
	
			spItemA = new SpItem[ n ] ;
			for( int i = 0 ; i < n ; ++i )
				spItemA[ i ] = ( ( OtTreeNodeWidget )v.elementAt( i ) ).getItem() ;
		}
		return spItemA ;
	}

	/**
	 * Get a copy of each SpItem associated with the multi-selected nodes.
	 */
	public SpItem[] getMultiSelectedItemsCopy()
	{
		SpItem[] spItemA = getMultiSelectedItems() ;
		if( spItemA != null )
		{
			for( int i = 0 ; i < spItemA.length ; ++i )
				spItemA[ i ] = spItemA[ i ].deepCopy() ;
		}
		return spItemA ;
	}

	/**
	 * Add a new item relative to the selected item.
	 */
	public SpItem addItem( SpItem newItem )
	{
		SpItem[] newItems = { newItem } ;
		newItems = addItems( newItems ) ;

		if( newItems == null )
			return null ;
		// The following has been added to allow revision tracking of libraries
		if( _spProg instanceof SpLibrary )
		{
			if( newItem.typeStr().equals( "og" ) )
				( ( SpMSB )newItem ).setLibraryRevision() ;
			else if( newItem.typeStr().equals( "ob" ) && ( ( SpObs )newItem ).isMSB() )
				( ( SpObs )newItem ).setLibraryRevision() ;
		}

		// The following is used specifically to handle JCMT Hetrodyne observations.
		/*
		 * If we are adding a JCMT instrument, we need to check all of the observations in scope and update then correctly for the type of instrument.
		 */
		if( newItem instanceof SpInstSCUBA2 || newItem instanceof SpInstHeterodyne )
		{
			SpItem newItemsRoot = SpTreeMan.findRootItem( newItem ) ;
			// Get all of the SpIterObs components below this
			Vector<SpItem> obsVector = SpTreeMan.findAllItems( newItemsRoot , SpIterFocusObs.class.getName() ) ;
			obsVector.addAll( SpTreeMan.findAllItems( newItemsRoot , SpIterJiggleObs.class.getName() ) ) ;
			obsVector.addAll( SpTreeMan.findAllItems( newItemsRoot , SpIterNoiseObs.class.getName() ) ) ;
			obsVector.addAll( SpTreeMan.findAllItems( newItemsRoot , SpIterPointingObs.class.getName() ) ) ;
			obsVector.addAll( SpTreeMan.findAllItems( newItemsRoot , SpIterRasterObs.class.getName() ) ) ;
			obsVector.addAll( SpTreeMan.findAllItems( newItemsRoot , SpIterSkydipObs.class.getName() ) ) ;
			obsVector.addAll( SpTreeMan.findAllItems( newItemsRoot , SpIterStareObs.class.getName() ) ) ;
			obsVector.addAll( SpTreeMan.findAllItems( newItemsRoot , SpIterFlatObs.class.getName() ) ) ;
			for( SpItem item : obsVector )
			{
				// Make sure that the observation is associated with the new item and not some other existing component
				SpIterJCMTObs jcmtObs = ( SpIterJCMTObs )item ;
				SpItem obsContext = SpTreeMan.findObsContext( jcmtObs ) ;
				SpInstObsComp obsComp = SpTreeMan.findInstrumentInContext( obsContext ) ;
				while( obsComp == null )
				{
					obsContext = obsContext.parent() ;
					if( obsContext == null )
						break ;
					obsComp = SpTreeMan.findInstrumentInContext( obsContext ) ;
				}
				if( obsComp != null && obsComp.equals( newItem ) )
				{					
					if( newItem instanceof SpInstHeterodyne )
						jcmtObs.setupForHeterodyne() ;
					else if( newItem instanceof SpInstSCUBA2 )
						jcmtObs.setupForSCUBA2() ;
				}
			}
		}

		/*
		 * If we are adding an observe "eye" find out what instrument is being used. 
		 * If it is a JCMT instrument, update its attributes accordingly.
		 */
		if( newItem instanceof SpIterJCMTObs )
		{
			// Find out what instrument is asscociated with this observation
			SpItem obsContext = SpTreeMan.findObsContext( newItem ) ;
			SpInstObsComp obsComp = SpTreeMan.findInstrumentInContext( obsContext ) ;
			while( obsComp == null )
			{
				obsContext = obsContext.parent() ;
				if( obsContext == null )
					break ;
				obsComp = SpTreeMan.findInstrumentInContext( obsContext ) ;
			}
			if( obsComp != null )
			{
				if( obsComp instanceof SpInstHeterodyne )
					( ( SpIterJCMTObs )newItem ).setupForHeterodyne() ;
				else if( obsComp instanceof SpInstSCUBA2 )
					( ( SpIterJCMTObs )newItem ).setupForSCUBA2() ;
			}
		}
		OtTreeNodeWidget tnw = getTreeNodeWidgetForSpItem( newItem ) ;
		if( tnw != null )
			tnw.setSelected( true ) ;
		
		// END OF ADDITIONAL CODE
		return newItems[ 0 ] ;
	}

	/**
	 * Add all the items relative to the selected item.
	 */
	public SpItem[] addItems( SpItem[] newItems )
	{

		// Figure out which node was selected, if none choose the root node
		OtTreeNodeWidget destTNW = ( OtTreeNodeWidget )getSelectedNode() ;
		// Add to the top level, the Science Program root
		if( destTNW == null )
			destTNW = _otTNW ;

		SpItem destItem = destTNW.getItem() ;

		/*
		 * The following are additional checks needed for the survey component. 
		 * The following checks are performed (but only when inserting an SpMSB or an SpObs) - 1. 
		 * If the Survey Component is outside an MSB 1.1 Only one MSB can be added, 
		 * but any number of SpObs can be added within that MSB 1.2 
		 * A single SpObs can also be added but it must be marked as an SpMSB 2. 
		 * If the Survey Component is inside an MSB 2.1 We can not add another SpMSB within the survey component 2.2 
		 * Any number of SpObs can be added inside the survey component 2.3 
		 * Make sure that the SpObs is not an MSB (needed for copy/paste)
		 */
		// See if we have a Survey component is scope
		boolean scInScope = false ; // Survey Container in scope
		boolean scInMSB = false ; // Survey Container in MSB
		SpItem sc = null ;
		if( destItem instanceof SpSurveyContainer )
		{
			scInScope = true ;
			sc = destItem ;
		}
		SpItem parent = destItem.parent() ;
		while( parent != null )
		{
			if( parent instanceof SpSurveyContainer )
			{
				scInScope = true ;
				sc = parent ;
			}
			if( parent instanceof SpMSB && scInScope )
			{
				scInMSB = true ;
			}
			parent = parent.parent() ;
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
						/*
						 * If the newItem is an SpObs, we can legally insert it if
						 * the destination is an MSB (insertInside) or an SpObs which is not an MSB (insertAfter)
						 */
						if( newItems[ i ] instanceof SpObs && ( destItem instanceof SpMSB || ( destItem instanceof SpObs && !( ( SpObs )destItem ).isMSB() ) ) )
							continue ;

						Enumeration<SpItem> children = sc.children() ;
						while( children.hasMoreElements() )
						{
							SpItem child = children.nextElement() ;
							if( child instanceof SpObs && (( SpObs )child).isMSB() )
							{
								DialogUtil.error( this , "A Survey folder can contain at most 1 MSB and the current Obs is an MSB" ) ;
								return null ;
							}
							else if( child instanceof SpMSB && !( child instanceof SpObs ) )
							{
								DialogUtil.error( this , "A Survey folder can contain at most 1 MSB" ) ;
								return null ;
							}
						}
					}
					else
					{
						if( !( newItems[ i ] instanceof SpObs ) )
						{
							DialogUtil.error( this , "A Survey folder within an MSB can not contain another MSB" ) ;
							return null ;
						}
					}
				}
			}
		}

		if( !canAddEyeToNonACSIS( destItem , newItems ) || !canAddEyeToSCUBA2( destItem , newItems ) )
				return null ;

		if( !canAddSCUBA2( destItem , newItems ) || !canAddHeterodyne( destItem , newItems ) )
			return null ;

		// First see if we can insert the items inside the selected node
		SpInsertData spID ;
		spID = SpTreeMan.evalInsertInside( newItems , destItem ) ;

		if( spID == null )
		{
			// Couldn't insert inside, so try inserting after.
			spID = SpTreeMan.evalInsertAfter( newItems , destItem ) ;

			if( spID == null )
			{
				// Couldn't insert after either, so just give up.
				String noun = "item" ;
				if( newItems.length > 1 )
					noun += "s" ;

				DialogUtil.error( this , "Couldn't add the new " + noun + " at this point." ) ;
				return null ;
			}
		}

		return addItems( spID ) ;
	}

	/**
	 * Add the new items using the given insertion information.
	 */
	public SpItem[] addItems( SpInsertData spID )
	{
		SpItem[] spItems = null ;
		if( ( spID.replaceItems == null || ConfirmAVClobber.okToClobber( spID.replaceItems ) ) && _addItems( spID ) )
			spItems = spID.items ;
		
		return spItems ;
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
	public SpItem[] insertItemAfter( SpItem newItem )
	{
		SpItem[] spItems = null ;
		SpInsertData spID = SpTreeMan.evalInsertAfter( newItem , getSelectedItem() ) ;

		if( spID == null )
			spID = SpTreeMan.evalInsertInside( newItem , getSelectedItem() ) ;
		
		if( spID != null )
			spItems = addItems( spID ) ;
		
		return spItems ;
	}

	// Add items without worrying about clobber.
	private boolean _addItems( SpInsertData spID )
	{
		// Add the new item to the local copy of the program
		SpTreeMan.insert( spID ) ;
		return true ;
	}

	/**
	 * Remove the selected item (unless its the root, the Science Program itself).
	 */
	public SpItem rmSelectedItem()
	{
		SpItem spItem = null ;
		OtTreeNodeWidget tnw = ( OtTreeNodeWidget )getSelectedNode() ;
		if( tnw == null )
		{
			DialogUtil.error( this , "You have to select an item to delete." ) ;
		}
		else if( tnw == ( OtTreeNodeWidget )getRoot() )
		{
			DialogUtil.error( this , "You can't delete the Science Program!" ) ;
		}
		else
		{
			SpItem[] spItemA = { tnw.getItem() } ;
			spItem = rmItems( spItemA ) ? spItemA[ 0 ] : null ;
		}
		return spItem ;
	}

	/**
	 * Remove the multi-selected items.
	 */
	public SpItem[] rmMultiSelectedItems()
	{
		SpItem[] spItemA = getMultiSelectedItems() ;
		if( spItemA != null )
			spItemA = rmItems( spItemA ) ? spItemA : null ;
		return spItemA ;
	}

	/**
	 * rmMultiSelectedItems doesn't seem to work with the new multi selection in MultiSelTreeWidget anymore.
	 * 
	 * Replaced with a quick hack which seems to work in most cases.
	 * MFO: May 29, 2001
	 */
	public void rmAllSelectedItems()
	{
		TreePath[] paths = getSelectionPaths() ;
		boolean attemptToDeleteRoot = false ;
		if( paths != null && paths.length > 0 )
		{
			for( int i = 0 ; i < paths.length ; i++ )
			{
				tree.setSelectionPath( paths[ i ] ) ;
				/*
				 * This is a hack to prevent freezing when the Science Program node is dropped into the waste bin under Linux.
				 * Normally one would just try to apply rmSelectedItem without checking whether it is the Science Program node. 
				 * rmSelectedItem would then call DialogUtil.error("You have to select an item to delete."). 
				 * But this can cause the OT under Linux to freeze. 
				 * However, throwing an Exception and catching it in the calling class and calling
				 * DialogUtil.error there works fine. MFO, May 31, 2001
				 */
				if( ( OtTreeNodeWidget )getSelectedNode() == ( OtTreeNodeWidget )getRoot() )
					attemptToDeleteRoot = true ;
				else
					rmSelectedItem() ;
			}

			if( attemptToDeleteRoot )
				throw new IllegalArgumentException( "You can't delete the Science Program!" ) ;
		}
	}

	/**
	 * Remove the given items from the tree.
	 */
	public boolean rmItems( SpItem[] spItems )
	{
		// Make sure the srcItem can be extracted from its current position, if so, update the program and tree.
		boolean success = false ;
		if( SpTreeMan.evalExtract( spItems ) )
			success = SpTreeMan.extract( spItems ) ;
		return success ;
	}

	/**
	 * Cut the selected item and place it on the clipboard.
	 */
	public void cutToClipboard()
	{
		if( multipleItemsSelected() )
		{
			SpItem[] spItemA = rmMultiSelectedItems() ;
			if( spItemA != null )
				ClipboardHelper.setClipboard( spItemA ) ;
		}
		else
		{
			SpItem spItem = rmSelectedItem() ;
			if( spItem != null )
				ClipboardHelper.setClipboard( spItem ) ;
		}
	}

	/**
	 * Copy the selected item to the clipboard.
	 */
	public void copyToClipboard()
	{
		if( multipleItemsSelected() )
		{
			SpItem[] spItemA = getMultiSelectedItemsCopy() ;
			ClipboardHelper.setClipboard( spItemA ) ;
		}
		else
		{
			OtTreeNodeWidget tnw = ( OtTreeNodeWidget )getSelectedNode() ;
			if( tnw != null )
			{
				SpItem spItem = tnw.getItem() ;
				SpItem spCopy = spItem.deepCopy() ;
				ClipboardHelper.setClipboard( spCopy ) ;
			}
		}
	}

	/**
	 * Paste an item from the clipboard.
	 */
	public void pasteFromClipboard()
	{
		Object obj = ClipboardHelper.getClipboard() ;
		if( obj != null )
		{
			if( obj instanceof SpItem )
			{
				SpItem spCopy = ( ( SpItem )obj ).deepCopy() ;
				addItem( spCopy ) ;
			}
			else if( obj instanceof SpItem[] )
			{
				SpItem[] a = ( SpItem[] )obj ;
				for( int i = 0 ; i < a.length ; ++i )
					a[ i ] = a[ i ].deepCopy() ;
				addItems( a ) ;
			}
		}
	}

	/**
	 * Move the items from their current position based upon the information
	 * in the SpInsertData structure.
	 */
	public void mvItems( SpInsertData spID )
	{
		// Make sure the items can be extracted from their current position.
		if( SpTreeMan.evalExtract( spID.items ) )
		{
			// If necessary, ask if ok to clober.
			if( spID.replaceItems == null || ConfirmAVClobber.okToClobber( spID.replaceItems ) )
				_mvItems( spID ) ; // Move the items without worrying about clobber.
		}
	}

	//
	// This move item method does the actual work of moving a given item,
	// without worrying about clobber.
	//
	private void _mvItems( SpInsertData spID )
	{
		OtTreeNodeWidget tnw = ( OtTreeNodeWidget )getSelectedNode() ;

		SpTreeMan.move( spID ) ;

		// If tnw is still in this tree, then select it.
		if( tnw.getTreeWidget() == this )
			tnw.setSelected( true ) ;
	}

	/**
	 * Print the program to stdout
	 */
	public void showProg()
	{
		_spProg.print() ;
	}

	/**
	 * Get the TNW for the given spItem, creating it if it doesn't exist.
	 */
	private final OtTreeNodeWidget _getSpItemTNW( SpItem spItem )
	{
		OtClientData cd = ( OtClientData )spItem.getClientData() ;
		Assert.notNull( cd ) ;

		OtTreeNodeWidget tnw = cd.tnw ;
		tnw.setTree( this ) ;
		if( ( spItem.child() != null ) && ( tnw.getWidgetCount() == 0 ) )
			_populateTree( tnw ) ;

		return tnw ;
	}

	/**
	 * Items were added to the program, add corresponding tree nodes.
	 */
	public void spItemsAdded( SpItem parent , SpItem[] children , SpItem afterChild )
	{
		// Get the parent TNW
		OtTreeNodeWidget parentTNW = getTreeNodeWidgetForSpItem( parent ) ;
		if( parentTNW != null )
		{
			parentTNW.setTree( this ) ;

			// Find the position (pos) where the first child should be added.  
			// This should be 0 (first) if afterChild is null, or else whatever the index of afterChild is plus one if not null.
			int pos = 0 ;
			if( afterChild != null )
			{
				OtTreeNodeWidget tnw = getTreeNodeWidgetForSpItem( afterChild ) ;
				if( tnw != null )
				{
					tnw.setTree( this ) ;
					pos = tnw.getMyIndex() + 1 ;
				}
			}
	
			// Add the children TNW.
			MultiSelTreeNodeWidget[] tnwA = new MultiSelTreeNodeWidget[ children.length ] ;
			for( int i = 0 ; i < children.length ; ++i )
			{
				tnwA[ i ] = _getSpItemTNW( children[ i ] ) ;
				parentTNW.add( pos++ , tnwA[ i ] ) ;
				tnwA[ i ].setTree( this ) ;
			}
	
			// Make sure the parent is showing and expanded.
			parentTNW.expand() ;
			parentTNW.uncover() ;
	
			// Multi-select the new set
			setMultiSelectNodes( tnwA ) ;
		}
	}

	// Remove the nodes without trying to select a new node
	private void _spItemsRemoved( SpItem parent , SpItem[] children )
	{
		// Remove each child.
		for( int i = 0 ; i < children.length ; ++i )
		{
			OtTreeNodeWidget tnw = getTreeNodeWidgetForSpItem( children[ i ] ) ;
			if( tnw != null )
				tnw.remove() ;
		}

		// Repaint the widget containing the parentTNW
		OtTreeNodeWidget parentTNW = getTreeNodeWidgetForSpItem( parent ) ;
		parentTNW.setTree( this ) ;
	}

	/**
	 * Items were removed from the program, remove corresponding tree nodes.
	 */
	public void spItemsRemoved( SpItem parent , SpItem[] children )
	{
		// See if any one of these nodes is the selected node.
		OtTreeNodeWidget selectedTNW = null ;
		for( int i = 0 ; i < children.length ; ++i )
		{
			// Is this tree node selected?
			OtTreeNodeWidget tnw = getTreeNodeWidgetForSpItem( children[ i ] ) ;
			if( tnw.getSelectedNode() != null )
			{
				selectedTNW = tnw ;
				break ;
			}
		}

		// The selected tree node will be removed, so select something else.
		if( selectedTNW != null )
		{
			// Select some other tree node.  First try the next node following
			// the group, then try the previous node before the group, then failing those, just select the parent.

			OtTreeNodeWidget parentTNW = ( OtTreeNodeWidget )selectedTNW.getParent() ;
			Vector<TreeNode> v = parentTNW.getChildren() ;

			SpItem spItem = children[ children.length - 1 ] ;
			OtTreeNodeWidget tnw = getTreeNodeWidgetForSpItem( spItem ) ;
			if( tnw != null )
			{
				int i = tnw.getMyIndex() ;
				if( i + 1 < v.size() )
				{
					( ( OtTreeNodeWidget )v.elementAt( i + 1 ) ).select() ;
				}
				else
				{
					spItem = children[ 0 ] ;
					tnw = getTreeNodeWidgetForSpItem( spItem ) ;
					i = tnw.getMyIndex() ;
					if( i > 0 )
						( ( OtTreeNodeWidget )v.elementAt( i - 1 ) ).select() ;
					else
						parentTNW.select() ;
				}
			}
		}

		_spItemsRemoved( parent , children ) ;
	}

	/**
	 * Notification that items have been moved from a node in the program
	 * to another node in the program.
	 */
	public void spItemsMoved( SpItem oldParent , SpItem[] children , SpItem newParent , SpItem afterChild )
	{
		_spItemsRemoved( oldParent , children ) ;
		spItemsAdded( newParent , children , afterChild ) ;
	}

	/**
	 * Sets .gui.collapsed to true in the respective SpItem.
	 *
	 * Added by MFO (31 July 2001).
	 */
	public void treeCollapsed( TreeExpansionEvent e )
	{
		if( !ignoreActions )
			(( OtTreeNodeWidget )e.getPath().getLastPathComponent()).getItem().getTable().set( GUI_COLLAPSED , true ) ;
	}

	/**
	 * Sets .gui.collapsed to false in the respective SpItem.
	 *
	 * Added by MFO (31 July 2001).
	 */
	public void treeExpanded( TreeExpansionEvent e )
	{
		if( !ignoreActions )
			(( OtTreeNodeWidget )e.getPath().getLastPathComponent()).getItem().getTable().set( GUI_COLLAPSED , false ) ;
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
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		TreeWidgetCellRenderer resultComponent = (TreeWidgetCellRenderer) 
			super.getTreeCellRendererComponent(tree, value, selected,
				expanded, leaf, row, hasFocus);

		if ((value == null) || ! (value instanceof OtTreeNodeWidget)) {
			return resultComponent;
		}
		
		SpItem item = ((OtTreeNodeWidget) value).getItem();


		if (item instanceof SpNote) {
			if (((SpNote) item).isObserveInstruction())
				resultComponent.setForeground(Color.blue);
		}
		else if (item.isSuspended()) {
			resultComponent.setForeground(Color.red);
		}
		else if (item.isDisabled()) {
			resultComponent.setForeground(Color.gray);
		}
		else if (item.isOptional()) {
			resultComponent.setForeground(GREEN);
		}

		return resultComponent;
	}

	/**
	 * Generates priority numbers.
	 *
	 * Simple class for use when the number of entries is less than the
	 * allowable range of priorities.
	 */
	private static class PrioritySequence {
		protected int priority = 1;

		/**
		 * Factory method which returns the appropriate type of
		 * PrioritySequence depending on the number of entries.
		 */
		public static PrioritySequence prepareSequence(int numberMSBs) {
			if (numberMSBs > 99) {
				return new PrioritySequenceStepped(numberMSBs);
			}
			else {
				return new PrioritySequence();
			}
		}

		// Prevent direct access to constructor.
		protected PrioritySequence() {}

		/**
		 * Return the next priority number in sequence.
		 *
		 * The number is obtained from calculateNext() and
		 * compared with the allowed range.
		 */
		public int next() {
			int value = calculateNext();
			if (value < 1) {
				System.err.println("Warning: sequence returned priority less than 1");
				return 1;
			}
			if (value > 99) {
				System.err.println("Warning: sequence returned priority over 99");
				return 99;
			}
			return value;
		}

		/**
		 * Calculate next sequence number.
		 *
		 * This implementation simply returns numbers increasing by one each call.
		 */
		protected int calculateNext() {
			return priority ++;
		}
	}

	/**
	 * Generates priority numbers with a step in the gradient.
	 *
	 * Class for use when the number of entries exceeds the allowable
	 * range of priorities.
	 */
	private static class PrioritySequenceStepped extends PrioritySequence {
		private int nMin = 0;
		private int nLow = 0;
		private int priorityCount = 1;
		private int msbCount = 1;
		boolean updateDone = false;

		protected PrioritySequenceStepped(int numberMSBs) {
			float x = (float) numberMSBs / 99;

			// Get the smallest number of priorities that can be assigned to high ranking MSBs

			// nMin is n/99 - remainder/99
			nMin = (int) x;

			// This gives 99 - remainder
			nLow = 99 * (nMin + 1) - numberMSBs;

			//System.out.println("Should update nMin at msbCount of " + (nLow * nMin));
		}

		protected int calculateNext() {
			int value = priority;

			if (msbCount == nLow * nMin && !updateDone) {
				//System.out.println("Restting nMin at msbCount " + msbCount);
				nMin ++;
				priority ++;
				priorityCount = 1;
				updateDone = true;
			}
			else if (priorityCount == nMin) {
				priorityCount = 1;
				priority ++;
			}
			else {
				priorityCount ++;
			}

			msbCount ++;

			//System.out.println("Setting msb " + msbCount + " to priority " + value);
			return value;
		}
	}

	/**
	 * Generates a sequence of priority numbers by repeating a fixed value.
	 */
	private static class PrioritySequenceFixed extends PrioritySequence {
		public PrioritySequenceFixed(int priority) {this.priority = priority;}
		protected int calculateNext() {return priority;}
	}

	/**
	 * Automatically re-assigns priorities to the elements of a
	 * Science Programme based on the order in which they appear.
	 */
	public void autoAssignPriority(boolean selected)
	{
		MSBCounter counter = new MSBCounter();

		SpObsContextItem.applyAction(counter, getEnumForApply(selected));

		MSBPrioritize prioritize = new MSBPrioritize(
			PrioritySequence.prepareSequence(counter.getCount()));

		SpObsContextItem.applyAction(prioritize, getEnumForApply(selected));
	}

	/**
	 * Assigns the given priority to all elements.
	 */
	public void autoAssignPriorityFixed(boolean selected, int priority) {
		SpObsContextItem.applyAction(new MSBPrioritize(
			new PrioritySequenceFixed(priority)),
			 getEnumForApply(selected));
	}

	/**
	 * Alters the priority of all elements by the given amount.
	 */
	public void autoAssignPriorityShift(boolean selected, int delta) {
		SpObsContextItem.applyAction(
			new MSBPriorityShift(delta),
			 getEnumForApply(selected));
	}

	private static class MSBCounter implements SpItem.SpItemAction {
		private int n = 0;
		public int getCount() {return n;}
		public void apply (SpItem child) {
			if ((child instanceof SpMSB) && ((SpMSB) child).isMSB()) {
				n ++;
			}
			else if ((child instanceof SpSurveyContainer)
					&& ! ((SpSurveyContainer) child).hasMSBParent()) {
				n += ((SpSurveyContainer) child).size();
			}
		}
	}

	private static class MSBPrioritize implements SpItem.SpItemAction {
		private PrioritySequence seq;
		public MSBPrioritize(PrioritySequence seq) {this.seq = seq;}
		public void apply(SpItem child) {
			if ((child instanceof SpMSB) && ((SpMSB) child).isMSB()) {
				int priority = seq.next();
				((SpMSB)child).setPriority(priority);
			}
			else if ((child instanceof SpSurveyContainer)
					&& ! ((SpSurveyContainer) child).hasMSBParent()) {
				SpSurveyContainer survey = (SpSurveyContainer) child;
				int n = survey.size();
				for (int i = 0; i < n; i ++) {
					int priority = seq.next();
					survey.setPriority(priority, i);
				}
			}
		}
	}

	private static class MSBPriorityShift implements SpItem.SpItemAction {
		private int delta;
		public MSBPriorityShift(int delta) {this.delta = delta;}
		public void apply(SpItem child) {
			if ((child instanceof SpMSB) && ((SpMSB) child).isMSB()) {
				int priority = ((SpMSB)child).getPriority() + delta;
				if (priority < 1) {priority = 1;}
				if (priority > 99) {priority = 99;}
				((SpMSB)child).setPriority(priority);
			}
			else if ((child instanceof SpSurveyContainer)
					&& ! ((SpSurveyContainer) child).hasMSBParent()) {
				SpSurveyContainer survey = (SpSurveyContainer) child;
				int n = survey.size();
				for (int i = 0; i < n; i ++) {
					int priority = survey.getPriority(i) + delta;
					if (priority < 1) {priority = 1;}
					if (priority > 99) {priority = 99;}
					survey.setPriority(priority, i);
				}
			}
		}
	}

	private Enumeration<SpItem>getEnumForApply(boolean selected) {
		if (selected) return getMultiSelectedItemsEnumerationWithoutRecursion();
		return _spProg.children();
	}

	/**
	 * Construct an enumeration safe for recursive application.
	 *
	 * This is really just intended for use with the prioritization
	 * methods, and something better should be constructed for other usage.
	 *
	 * Turns the result from getMultiSelectedItems into a Vector and then
	 * goes through it and removes all items with a parent in that vector.
	 *
	 * Not very efficient as Vector uses Enumeration rather than Iterator
	 * and so you can't remove items from it.  However we need
	 * Enumeration to match what SpItem.children() returns.  Ideally we
	 * would move to using the modern collections.
	 *
	 * Also remove items which are children of Survey Containers as they
	 * may need to be handled specially.
	 */
	private Enumeration<SpItem> getMultiSelectedItemsEnumerationWithoutRecursion() {
		Vector<SpItem> v = new Vector<SpItem>();
		SpItem[] array = getMultiSelectedItems();
		for (int i = 0; i < array.length; i ++) {
			v.add(array[i]);
		}

		Vector<SpItem> filtered = new Vector<SpItem>();
		Enumeration<SpItem> e = v.elements();
		ITEM: while (e.hasMoreElements()) {
			SpItem item = e.nextElement();
			if ((item.parent() != null) 
					&& (item.parent() instanceof
						SpSurveyContainer)) {
				continue ITEM;
			}

			Enumeration<SpItem> sub = v.elements();
			while (sub.hasMoreElements()) {
				SpItem potentialParent = sub.nextElement();
				if (item.hasParentRecursive(potentialParent)) {
					continue ITEM;
				}
			}
			filtered.add(item);
		}

		return filtered.elements();
	}

	private boolean canAddSCUBA2( SpItem target , SpItem[] items )
	{
		boolean canAdd = true ;
		String[] eyes = { "jiggle" } ;
		Class<?> tabooClass = SpInstSCUBA2.class ;
		for( SpItem item : items )
		{
			if( tabooClass.isInstance( item ) )
			{
				canAdd = canAddInstrument( target , eyes ) ;
				break ;
			}
		}
		if( !canAdd )
			DialogUtil.error( this , "Can not add SCUBA-2 due to conflicting eye in observation." ) ;
		return canAdd ;
	}

	private boolean canAddHeterodyne( SpItem target , SpItem[] items )
	{
		boolean canAdd = true ;
		String[] eyes = { "dream" , "arrayTest" , "noise", "flat" , "FTS2" } ;
		Class<?> tabooClass = SpInstHeterodyne.class ;
		for( SpItem item : items )
		{
			if( tabooClass.isInstance( item ) )
			{
				canAdd = canAddInstrument( target , eyes ) ;
				break ;
			}
		}
		if( !canAdd )
			DialogUtil.error( this , "Can not add Heterodyne due to conflicting eye in observation." ) ;
		return canAdd ;
	}

	private boolean canAddInstrument( SpItem target , String[] eyes )
	{
		boolean canAdd = true ;
		if( target.childCount() > 0 )
		{
			Enumeration<SpItem> children = target.children() ;
			while( children.hasMoreElements() && canAdd )
			{
				SpItem child = children.nextElement() ;
				if( child instanceof SpInstObsComp )
					break ;
				canAdd = canAddInstrument( child , eyes ) ;
				for( String eye : eyes )
				{
					if( child.subtypeStr().equals( eye + "Obs" ) )
					{
						canAdd = false ;
						break ;
					}
				}
			}
		}
		return canAdd ;
	}

	private boolean canAddEyeToSCUBA2( SpItem target , SpItem[] items )
	{
		boolean canAdd = true ;
		String[] eyes = { "jiggle" } ;
		Class<?>[] tabooClass = new Class[]{ SpInstSCUBA2.class } ;
		for( int index = 0 ; index < eyes.length && canAdd ; index++ )
		{
			String eye = eyes[ index ] ;
			if( !canAddEye( target , items , eye + "Obs" , tabooClass ) )
			{
				String firstLetter = eye.substring( 0 , 1 ) ;
				eye = firstLetter.toUpperCase() + eye.substring( 1 , eye.length() ) ;
				DialogUtil.error( this , "Can not add a " + eye + " to a SCUBA-2 observation" ) ;
				canAdd = false ;
			}
		}
		return canAdd ;
	}
	
	// A badly named method, private though, so can be changed easily
	private boolean canAddEyeToNonACSIS( SpItem target , SpItem[] items )
	{
		boolean canAdd = true ;
		String[] eyes = { "dream" , "arrayTest" , "noise", "flat" , "FTS2" , "setup" } ;
		Class<?>[] tabooClass = new Class[]{ SpInstHeterodyne.class } ;
		for( int index = 0 ; index < eyes.length && canAdd ; index++ )
		{
			String eye = eyes[ index ] ;
			if( !canAddEye( target , items , eye + "Obs" , tabooClass ) )
			{
				String firstLetter = eye.substring( 0 , 1 ) ;
				eye = firstLetter.toUpperCase() + eye.substring( 1 , eye.length() ) ;
				DialogUtil.error( this , "Can not add a " + eye + " to an ACSIS heterodyne observation" ) ;
				canAdd = false ;
			}
		}
		return canAdd ;
	}

	private boolean canAddEye( SpItem target , SpItem[] items , String eye , Class<?>[] tabooClasses )
	{
		boolean canAdd = true ;
		SpInstObsComp inst = SpTreeMan.findInstrument( target ) ;
		if( inst != null )
		{
			for( int j = 0 ; j < tabooClasses.length && canAdd ; j++ )
			{
				Class<?> tabooClass = tabooClasses[ j ] ;
				if( tabooClass.isInstance( inst ) )
				{
					for( int i = 0 ; i < items.length && canAdd ; i++ )
					{
						String subType = items[ i ].subtypeStr() ;
						if( subType.equals( eye ) )
							canAdd = false ;
					}
				}
			}
		}
		return canAdd ;
	}
	
	/**
	 * Convenience method for resolving a OtTreeNodeWidget from an SpItem.
	 * @param item to be resolved.
	 * @return corresponding OtTreeNodeWidget or null.
	 */
	private OtTreeNodeWidget getTreeNodeWidgetForSpItem( SpItem item )
	{
		OtTreeNodeWidget tnw = null ;
		Object clientData = item.getClientData() ;
		if( clientData != null && clientData instanceof OtClientData )
		{
			OtClientData otClientData = ( OtClientData )clientData ;
			tnw = otClientData.tnw ;
		}
		return tnw ;
	}
}
