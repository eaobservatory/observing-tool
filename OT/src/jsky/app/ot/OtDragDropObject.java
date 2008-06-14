// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot ;

import java.awt.datatransfer.DataFlavor ;
import java.awt.datatransfer.Transferable ;
import gemini.sp.SpItem ;

/**
 * This class ties an SpItem to the tree widget in which its associated
 * tree node lives.  This is used by drop targets when an item is being
 * moved or deleted in order to remove dragged objects from their tree.
 */
public final class OtDragDropObject implements Transferable
{
	/** Identifies the object being dragged and dropped */
	public final static DataFlavor dataFlavor = new DataFlavor( OtDragDropObject.class , "OtDragDropObject" ) ;

	// The tree node widget that owns the SpItem being dragged.  
	// Will be null if this item is new and is not in any tree.
	private OtTreeWidget _currentOwner ;

	// The item(s) being dragged.
	private SpItem[] _spItemA ;

	/**
	 * This constructor should be used when dragging a newly created object
	 * that hasn't been inserted in any tree.
	 */
	OtDragDropObject( SpItem spItem )
	{
		_spItemA = new SpItem[ 1 ] ;
		_spItemA[ 0 ] = spItem ;
	}

	/**
	 * This constructor should be used when dragging an object that currently
	 * exists in a tree.
	 */
	OtDragDropObject( SpItem spItem , OtTreeWidget treeWidget )
	{
		this( spItem ) ;
		_currentOwner = treeWidget ;
	}

	/**
	 * This constructor should be used when dragging a newly created set
	 * of items that haven't been inserted in any tree.
	 */
	OtDragDropObject( SpItem[] spItemA )
	{
		_spItemA = spItemA ;
	}

	/**
	 * This constructor should be used when dragging a set of objects
	 * that currently exists in a tree.
	 */
	OtDragDropObject( SpItem[] spItemA , OtTreeWidget treeWidget )
	{
		this( spItemA ) ;
		_currentOwner = treeWidget ;
	}

	/** Is more than one item being dragged? */
	boolean isMultiDrag()
	{
		return( _spItemA.length > 1 ) ;
	}

	/** Get the first SpItem. */
	public SpItem getSpItem()
	{
		return getSpItem( 0 ) ;
	}

	/** Get the nth SpItem. */
	SpItem getSpItem( int i )
	{
		return _spItemA[ i ] ;
	}

	/** Get the set of SpItems. */
	public SpItem[] getSpItems()
	{
		return _spItemA ;
	}

	/** Get the owner, the OtTreeWidget that contains the items being dragged. */
	public OtTreeWidget getOwner()
	{
		return _currentOwner ;
	}

	// Implementation of the Transferable interface

	public DataFlavor[] getTransferDataFlavors()
	{
		// MFO: DataFlavor.stringFlavor is only added because dropping nodes would not work under Windows (NT) otherwise.
		//      The same trick is used in the Gemini OT (from ot-0.6, in jsky.app.ot.viewer.SPDragDropObject)
		return new DataFlavor[] { dataFlavor , DataFlavor.stringFlavor } ;
	}

	public boolean isDataFlavorSupported( DataFlavor fl )
	{
		return fl.equals( dataFlavor ) ;
	}

	public Object getTransferData( DataFlavor fl )
	{
		if( !isDataFlavorSupported( fl ) )
			return null ;

		return this ;
	}
}
