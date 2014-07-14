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

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import java.util.Vector ;
import javax.swing.JPopupMenu ;
import javax.swing.JMenuItem ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;

import jsky.util.QuickSort ;

//
// A popup menu used to create "observe" iterators (cal unit flats
// and arcs, bias, darks, and generic observes).
//
@SuppressWarnings( "serial" )
class OtIterObsPopupMenu extends JPopupMenu
{
	/**
	 * Create the popup menu, adding entries for the folder and for
	 * each iterator subtype.
	 */
	public OtIterObsPopupMenu( OtTreeWidget treeWidget )
	{
		// Sort the iterator types
		Vector<SpType> spTypeV = OtCfg.obsIteratorTypes ;
		OtSortableSpType[] sst = new OtSortableSpType[ spTypeV.size() ] ;
		for( int i = 0 ; i < sst.length ; ++i )
			sst[ i ] = new OtSortableSpType( spTypeV.elementAt( i ) ) ;

		QuickSort.sort( sst , 0 , sst.length , null ) ;

		// Add each type
		for( int i = 0 ; i < sst.length ; ++i )
			_add( treeWidget , sst[ i ].spType ) ;
	}

	/*
	 * Add a new menu item based on the given SpType.  The label displayed
	 * on the menu is taken from the SpType, and the command that will be
	 * executed addes a new item of the given SpType to the given treeWidget.
	 */
	private void _add( final OtTreeWidget treeWidget , final SpType spType )
	{
		JMenuItem menuItem = new JMenuItem( spType.getReadable() ) ;
		menuItem.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent ae )
			{
				treeWidget.addItem( SpFactory.create( spType ) ) ;
			}
		} ) ;
		add( menuItem ) ;
	}
}
