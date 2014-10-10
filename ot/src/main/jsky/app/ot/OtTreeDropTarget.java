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
 */

package jsky.app.ot ;

import java.awt.Cursor ;
import java.awt.Point ;
import java.awt.datatransfer.Transferable ;
import java.awt.datatransfer.UnsupportedFlavorException ;
import java.awt.dnd.DnDConstants ;
import java.awt.dnd.DropTarget ;
import java.awt.dnd.DropTargetDragEvent ;
import java.awt.dnd.DropTargetDropEvent ;
import java.awt.dnd.DropTargetEvent ;
import java.awt.dnd.DropTargetListener ;
import java.beans.PropertyChangeEvent ;
import java.beans.PropertyChangeListener ;
import java.io.IOException ;
import javax.swing.JTree ;
import javax.swing.tree.TreePath ;

import gemini.sp.SpInsertData ;
import gemini.sp.SpItem ;
import gemini.sp.SpTreeMan ;

import jsky.app.ot.util.DnDUtils ;
import ot.util.DialogUtil ;

/**
 * Drag&Drop target for the OT tree widget.
 * Based on an example in the book: "Core Swing, Advanced Programming".
 *
 * MFO: This class has been updated by copying jsky.app.ot.viewer.SPTreeDropTarget from ot-0.9.0
 *      to this class and making modifications.
 *
 * @author Allan Brighton
 */
public class OtTreeDropTarget implements DropTargetListener , PropertyChangeListener
{
	/** Target SPTree widget */
	protected OtTreeWidget _spTree ;

	/** The internal JTree widget */
	protected JTree tree ;

	/** The drop target */
	protected DropTarget dropTarget ;

	/** Indicates whether data is acceptable */
	protected boolean acceptableType ;

	/** Initially selected rows */
	protected TreePath[] selections ;

	/** Initial lead selection */
	protected TreePath leadSelection ;

	/**
	 * Constructor
	 */
	public OtTreeDropTarget( OtTreeWidget _spTree )
	{
		this._spTree = _spTree ;
		tree = _spTree.getTree() ;

		// Listen for changes in the enabled property
		tree.addPropertyChangeListener( this ) ;

		// Create the DropTarget and register it with the SPTree.
		dropTarget = new DropTarget( tree , DnDConstants.ACTION_COPY_OR_MOVE , this , tree.isEnabled() , null ) ;
	}

	/** Implementation of the DropTargetListener interface */
	public void dragEnter( DropTargetDragEvent dtde )
	{
		DnDUtils.debugPrintln( "dragEnter, drop action = " + DnDUtils.showActions( dtde.getDropAction() ) ) ;

		// Save the list of selected items
		saveTreeSelection() ;

		// Get the type of object being transferred and determine whether it is appropriate.
		checkTransferType( dtde ) ;

		// Accept or reject the drag.
		boolean acceptedDrag = acceptOrRejectDrag( dtde ) ;

		// Do drag-under feedback
		dragUnderFeedback( dtde , acceptedDrag ) ;
	}

	/** Implementation of the DropTargetListener interface */
	public void dragExit( DropTargetEvent dte )
	{
		DnDUtils.debugPrintln( "DropTarget dragExit" ) ;

		// Do drag-under feedback
		dragUnderFeedback( null , false ) ;

		// Restore the original selections
		restoreTreeSelection() ;
	}

	/** Implementation of the DropTargetListener interface */
	public void dragOver( DropTargetDragEvent dtde )
	{
		DnDUtils.debugPrintln( "DropTarget dragOver, drop action = " + DnDUtils.showActions( dtde.getDropAction() ) ) ;

		// Accept or reject the drag
		boolean acceptedDrag = acceptOrRejectDrag( dtde ) ;

		// Do drag-under feedback
		dragUnderFeedback( dtde , acceptedDrag ) ;
	}

	/** Implementation of the DropTargetListener interface */
	public void dropActionChanged( DropTargetDragEvent dtde )
	{
		DnDUtils.debugPrintln( "DropTarget dropActionChanged, drop action = " + DnDUtils.showActions( dtde.getDropAction() ) ) ;

		// Accept or reject the drag
		boolean acceptedDrag = acceptOrRejectDrag( dtde ) ;

		// Do drag-under feedback
		dragUnderFeedback( dtde , acceptedDrag ) ;
	}

	/** Implementation of the DropTargetListener interface */
	public void drop( DropTargetDropEvent dtde )
	{
		DnDUtils.debugPrintln( "DropTarget drop, drop action = " + DnDUtils.showActions( dtde.getDropAction() ) ) ;

		// Check the drop action
		if( ( dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE ) != 0 )
		{
			// Accept the drop and get the transfer data
			dtde.acceptDrop( dtde.getDropAction() ) ;
			Transferable transferable = dtde.getTransferable() ;
			boolean dropSucceeded = false ;

			try
			{
				tree.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) ) ;

				// Save the user's selections
				saveTreeSelection() ;

				dropSucceeded = dropNodes( dtde.getDropAction() , transferable , dtde.getLocation() ) ;

				DnDUtils.debugPrintln( "Drop completed, success: " + dropSucceeded ) ;
			}
			catch( Exception e )
			{
				DnDUtils.debugPrintln( "Exception while handling drop " + e ) ;
			}
			finally
			{
				tree.setCursor( Cursor.getDefaultCursor() ) ;

				// Restore the user's selections
				restoreTreeSelection() ;
				dtde.dropComplete( dropSucceeded ) ;
			}
		}
		else
		{
			DnDUtils.debugPrintln( "Drop target rejected drop" ) ;
			dtde.dropComplete( false ) ;
		}
	}

	/** PropertyChangeListener interface */
	public void propertyChange( PropertyChangeEvent evt )
	{
		String propertyName = evt.getPropertyName() ;
		// Enable the drop target if the SPTree is enabled and vice versa.
		if( propertyName.equals( "enabled" ) )		
			dropTarget.setActive( tree.isEnabled() ) ;
	}

	// Internal methods start here
	protected boolean acceptOrRejectDrag( DropTargetDragEvent dtde )
	{
		int dropAction = dtde.getDropAction() ;
		int sourceActions = dtde.getSourceActions() ;
		boolean acceptedDrag = false ;

		DnDUtils.debugPrintln( "\tSource actions are " + DnDUtils.showActions( sourceActions ) + ", drop action is " + DnDUtils.showActions( dropAction ) ) ;

		boolean acceptableDropLocation = isAcceptableDropLocation( dtde ) ;

		// Reject if the object being transferred or the operations available are not acceptable.
		if( !acceptableType || ( sourceActions & DnDConstants.ACTION_COPY_OR_MOVE ) == 0 )
		{
			DnDUtils.debugPrintln( "Drop target rejecting drag: acceptableType = " + acceptableType ) ;
			dtde.rejectDrag() ;
		}
		else if( !acceptableDropLocation )
		{
			// Can only drag to writable directory
			DnDUtils.debugPrintln( "Drop target rejecting drag: no acceptable drop lLocation" ) ;
			dtde.rejectDrag() ;
		}
		else
		{
			// Offering an acceptable operation: accept
			DnDUtils.debugPrintln( "Drop target accepting drag" ) ;
			dtde.acceptDrag( dropAction ) ;
			acceptedDrag = true ;
		}

		return acceptedDrag ;
	}

	protected void dragUnderFeedback( DropTargetDragEvent dtde , boolean acceptedDrag )
	{
		if( dtde != null && acceptedDrag )
		{
			if( isAcceptableDropLocation( dtde ) )
			{
				Point location = dtde.getLocation() ;
				_spTree.setIgnoreSelection( true ) ;
				_spTree.selectNode( getNode( location ) ) ;
				_spTree.setIgnoreSelection( false ) ;
			}
			else
			{
				_spTree.setIgnoreSelection( true ) ;
				tree.clearSelection() ;
				_spTree.setIgnoreSelection( false ) ;
			}
		}
		else
		{
			_spTree.setIgnoreSelection( true ) ;
			tree.clearSelection() ;
			_spTree.setIgnoreSelection( false ) ;
		}
	}

	protected void checkTransferType( DropTargetDragEvent dtde )
	{
		// Accept a list of files
		acceptableType = false ;
		if( dtde.isDataFlavorSupported( OtDragDropObject.dataFlavor ) )
			acceptableType = true ;

		DnDUtils.debugPrintln( "Data type acceptable - " + acceptableType ) ;
	}

	// This method handles a drop for a list of files
	protected boolean dropNodes( int action , Transferable transferable , Point location ) throws IOException , UnsupportedFlavorException
	{
		OtDragDropObject ddo = ( OtDragDropObject )transferable.getTransferData( OtDragDropObject.dataFlavor ) ;

		// MFO: The following 7 lines are retained from ot-0.5.
		OtTreeNodeWidget node = getNode( location ) ;
		if( node == null )
			return false ;

		DnDUtils.debugPrintln( ( action == DnDConstants.ACTION_COPY ? "Copy" : "Move" ) + " item " + ddo.getSpItem() + " to targetNode " + node ) ;

		// Highlight the drop location while we perform the drop
		_spTree.setIgnoreSelection( true ) ;
		_spTree.selectNode( getNode( location ) ) ;
		_spTree.setIgnoreSelection( false ) ;

		// MFO: The following 32 lines are retained from ot-0.5.
		//      (except: try/catch and _spTree.setProgram(prog) ; from ot-0.9.0)

		// Copy source object to the target
		SpInsertData spID = getSpInsertData( ddo , node ) ;
		if( spID == null )
			return false ;

		OtTreeWidget ownerTW = ddo.getOwner() ;
		SpItem[] newItems = spID.items ;
		try
		{
			if( ownerTW == _spTree )
			{
				// The dragged item was owned by this tree, so just move it.
				_spTree.mvItems( spID ) ;
			}
			else
			{
				if( ownerTW != null )
				{
					// Make a copy, since these items are owned by another tree.
					for( int i = 0 ; i < newItems.length ; ++i )
						newItems[ i ] = newItems[ i ].deepCopy() ;
				}
				newItems = _spTree.addItems( spID ) ;
			}
			_spTree.updateNodeExpansions() ;
		}
		catch( Exception e )
		{
			DialogUtil.error( ownerTW , e ) ;
		}

		return ( newItems != null ) ;
	}

	/** Return true if its okay to drop the item(s) here */
	protected boolean isAcceptableDropLocation( DropTargetDragEvent dtde )
	{
		OtDragDropObject ddo = OtTreeDragSource._dragObject ;
		if( ddo == null )
			return false ;

		// MFO from ot-0.5
		// get the node under the mouse
		OtTreeNodeWidget node = getNode( dtde.getLocation() ) ;
		if( node == null )
			return false ;

		return( getSpInsertData( ddo , node ) != null ) ;
	}

	/** Save the current tree selection */
	protected void saveTreeSelection()
	{
		selections = tree.getSelectionPaths() ;
		leadSelection = tree.getLeadSelectionPath() ;
		_spTree.setIgnoreSelection( true ) ;
		tree.clearSelection() ;
		_spTree.setIgnoreSelection( false ) ;
	}

	/** Restore the current tree selection */
	protected void restoreTreeSelection()
	{
		_spTree.setIgnoreSelection( true ) ;
		tree.setSelectionPaths( selections ) ;

		// Restore the lead selection
		if( leadSelection != null )
		{
			tree.removeSelectionPath( leadSelection ) ;
			tree.addSelectionPath( leadSelection ) ;
		}
		_spTree.setIgnoreSelection( false ) ;
	}

	/**
	 * Return the SpInsertData object to use to insert the object(s) being dragged,
	 * or null, if it is not allowed to drop the item(s) at the current position.
	 *
	 * Return the tree node for the given location.
	 *
	 * Copied from ot-0.5. Note that most of this class has been copied from jsky.app.ot.viewer.SPTreeDropTarget in
	 * ot-0.9.0.
	 */
	protected SpInsertData getSpInsertData( OtDragDropObject ddo , OtTreeNodeWidget node )
	{
		// See whether we can insert the newItem.
		SpItem[] newItems = ddo.getSpItems() ;
		SpItem spItem = node.getItem() ;
		SpInsertData spID = SpTreeMan.evalInsertAfter( newItems , spItem ) ;
		if( spID == null )
			spID = SpTreeMan.evalInsertInside( newItems , spItem ) ;

		return spID ;
	}

	/**
	 * Return the tree node for the given location.
	 *
	 * Copied from ot-0.5. Note that most of this class has been copied from jsky.app.ot.viewer.SPTreeDropTarget in
	 * ot-0.9.0.
	 */
	protected OtTreeNodeWidget getNode( Point location )
	{
		TreePath treePath = tree.getPathForLocation( location.x , location.y ) ;
		if( treePath == null )
			return null ;

		return ( OtTreeNodeWidget )treePath.getLastPathComponent() ;
	}
}
