// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import gemini.sp.SpItem;

/**
 * This is a temporary editor for a few of the more important items that
 * don't have custom editors yet.  For instance, the Observation Folder
 * currently uses this editor.
 */
public final class EdTitle extends OtItemEditor implements TextBoxWidgetWatcher {

    private TitleEditorGUI _w;   // the GUI layout

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdTitle() {
	_title       = "Title Editor";
	_presSource  = _w = new TitleEditorGUI();
	_description = "Change the title of the item here.";
    }

    /**
     * Do any (one time) initialization.
     */
    protected void _init() {
	TextBoxWidgetExt tbw = _w.itemTitle;
	tbw.addWatcher(this);
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
	// Show the title
	TextBoxWidgetExt tbw = _w.itemTitle;
	String title = _spItem.getTitleAttr();
	if (title != null) {
	    tbw.setText( title );
	} else {
	    tbw.setText( "" );
	}
    }

    /**
     * Watch changes to the title text box.
     * @see TextBoxWidgetWatcher
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbw) {
	_spItem.setTitleAttr(tbw.getText().trim());
    }
 
    /**
     * Text box action, ignored.
     * @see TextBoxWidgetWatcher
     */
    public void	textBoxAction(TextBoxWidgetExt tbw) {}
}

