// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot ;

import java.util.Vector ;
import javax.swing.JPopupMenu ;
import javax.swing.JMenuItem ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;

import jsky.util.QuickSort ;

//
// A popup menu used to create iterator folders and components
// (except for the "observe" iterators--cal unit flats and arcs,
// bias, darks, and generic observes).
//
@SuppressWarnings( "serial" )
class OtIterCompPopupMenu extends JPopupMenu
{
	/**
	 * Create the popup menu, adding entries for the folder and for
	 * each iterator subtype.
	 */
	public OtIterCompPopupMenu( OtTreeWidget treeWidget )
	{
		// Add the iter folder and a separator
		_add( treeWidget , SpType.SEQUENCE ) ;
		addSeparator() ;

		// Sort the iterator types
		Vector<SpType> spTypeV = OtCfg.iteratorTypes ;
		OtSortableSpType[] sst = new OtSortableSpType[ spTypeV.size() ] ;
		for( int i = 0 ; i < sst.length ; ++i )
			sst[ i ] = new OtSortableSpType( spTypeV.elementAt( i ) ) ;

		QuickSort.sort( sst , 0 , sst.length , null ) ;

		// Add each type
		for( int i = 0 ; i < sst.length ; ++i )
			_add( treeWidget , sst[ i ].spType ) ;
	}

	//
	// Add a new menu item based on the given SpType.  The label displayed
	// on the menu is taken from the SpType, and the command that will be
	// executed addes a new item of the given SpType to the given treeWidget.
	//
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
