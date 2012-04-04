// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.editor ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;
import jsky.app.ot.editor.OtItemEditor ;

import gemini.sp.SpOR ;

/**
 * OR folder editor.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on jsky/app/ot/editor/EdTitle.java
 */
public final class EdOrFolder extends OtItemEditor implements TextBoxWidgetWatcher , ActionListener
{
	private OrEditorGUI _w ; // the GUI layout

	/**
	 * If true, ignore action events.
	 */
	private boolean ignoreActions = false ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdOrFolder()
	{
		_title = "Or Folder Editor" ;
		_presSource = _w = new OrEditorGUI() ;
		_description = "The or folder can be configured here." ;

		for( int i = 0 ; i < 100 ; i++ )
			_w.numberOfItems.addItem( "" + i ) ;

		_w.numberOfItems.addActionListener( this ) ;
	}

	/**
	 * Do any (one time) initialization.
	 */
	protected void _init()
	{
		TextBoxWidgetExt tbw = _w.itemTitle ;
		tbw.addWatcher( this ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		// Show the title
		TextBoxWidgetExt tbw = _w.itemTitle ;
		String title = _spItem.getTitleAttr() ;
		if( title != null )
			tbw.setText( title ) ;
		else
			tbw.setText( "" ) ;

		ignoreActions = true ;
		_w.numberOfItems.setSelectedIndex( ( ( SpOR )_spItem ).getNumberOfItems() ) ;
		ignoreActions = false ;
	}

	/**
	 * Watch changes to the title text box.
	 * @see TextBoxWidgetWatcher
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbw )
	{
		_spItem.setTitleAttr( tbw.getText().trim() ) ;
	}

	/**
	 * Text box action, ignored.
	 * @see TextBoxWidgetWatcher
	 */
	public void textBoxAction( TextBoxWidgetExt tbw ){}

	public void actionPerformed( ActionEvent evt )
	{
		if( !ignoreActions )
			(( SpOR )_spItem).setNumberOfItems( _w.numberOfItems.getSelectedIndex() ) ;
	}
}
