// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

import java.util.List ;

import gemini.sp.iter.SpIterFolder ;

/**
 * This is the editor for the "iterator folder" or "Sequence" item.
 * It simply displays a table into which the iteration sequence of contained
 * iterators is written.
 */
public final class EdIteratorFolder extends OtItemEditor implements ActionListener
{
	private IterEnumTableWidget _table ;
	private IterFolderGUI _w ; // the GUI layout panel

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIteratorFolder()
	{
		_title = "Sequence" ;
		_presSource = _w = new IterFolderGUI() ;
		_description = "Press the \"Show\" button to obtain a text representation of the dynamic sequence." ;
		_w.compileButton.addActionListener( this ) ;
	}

	/**
	 * Perform any necessary (one time) initialization.
	 */
	protected void _init()
	{
		_table = _w.table ;
		_table.setColumnHeaders( new String[] { "Action" , "Details" } ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		_table.removeAllRows() ;
	}

	private void _updateFolder()
	{
		SpIterFolder iterFolder = ( SpIterFolder )_spItem ;
		List code = iterFolder.compile() ;

		_table.removeAllRows() ;
		for( int i = 0 ; i < code.size() ; ++i )
		{
			Object o = code.get( i ) ;
			if( o instanceof List )
				_table.addSteps( ( List )o ) ;
			else
				System.out.println( "UNKNOWN iter element: " + o ) ;
		}
	}

	/**
	 * Handle action event on the show button.
	 */
	public void actionPerformed( ActionEvent evt )
	{
		_updateFolder() ;
	}
}
