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
package jsky.app.ot.editor ;

import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

/**
 * This is a temporary editor for a few of the more important items that
 * don't have custom editors yet.  For instance, the Observation Folder
 * currently uses this editor.
 */
public final class EdTitle extends OtItemEditor implements TextBoxWidgetWatcher
{
	private TitleEditorGUI _w ; // the GUI layout

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdTitle()
	{
		_title = "Title Editor" ;
		_presSource = _w = new TitleEditorGUI() ;
		_description = "Change the title of the item here." ;
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
}
