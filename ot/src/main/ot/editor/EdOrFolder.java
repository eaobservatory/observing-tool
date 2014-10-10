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
