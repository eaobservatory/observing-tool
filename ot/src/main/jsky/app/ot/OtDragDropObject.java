// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
