// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor;

import java.awt.Event;
import java.awt.event.KeyEvent;

import java.net.URL;

import javax.swing.ImageIcon;

import jsky.app.ot.gui.KeyPressWatcher;
import jsky.app.ot.gui.RichTextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;

import orac.util.XmlUtil;
import gemini.sp.SpNote;

/**
 * This is the editor for Note item.
 */
public final class EdNote extends OtItemEditor
    implements KeyPressWatcher, TextBoxWidgetWatcher, CheckBoxWidgetWatcher {

    private NoteGUI             _w;         // the GUI layout panel

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdNote()  {
	_title       = "Note";
	_presSource  = _w = new NoteGUI();
	_description = "Enter notes for the operator/astronomer here.";
	_resizable   = true;

	URL url = ClassLoader.getSystemClassLoader().getResource("jsky/app/ot/images/note.gif");
        _w.imageLabel.setIcon(new ImageIcon(url));

	// The title
	TextBoxWidgetExt tbw = _w.title;
	tbw.addWatcher(this);

	// The note
	RichTextBoxWidgetExt rtbw;
	rtbw = _w.note;
	rtbw.addWatcher(this);

	// The observe instruction check box.
	_w.observeInstruction.addWatcher(this);
    }


    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
	// The title
	TextBoxWidgetExt tbw = _w.title;
	String         title = _spItem.getTitleAttr();
	if (title != null) {
	    tbw.setText( title );
	} else {
	    tbw.setText( "" );
	}

	// The Note
	RichTextBoxWidgetExt rtbw;
	rtbw = _w.note;

	String noteText = ((SpNote) _spItem).getNote();

	if (noteText == null) {
	    rtbw.setText("");
	} else {
	    rtbw.setText(noteText);
	}

	_w.observeInstruction.setValue(((SpNote)_spItem).isObserveInstruction());
    }


    /**
     * A key has been pressed in the note rich text box widget.
     * @see KeyPressWatcher
     */
    public void	keyPressed(KeyEvent evt) {
	RichTextBoxWidgetExt rtbw;
	rtbw = _w.note;

	((SpNote) _spItem).setNote(XmlUtil.asciiToXml(rtbw.getText()));
    }

    /**
     * Watch changes to the title text box.
     * @see TextBoxWidgetWatcher
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
	_spItem.setTitleAttr(tbwe.getText().trim());
    }
 
    /**
     * Text box action, ignore.
     * @see TextBoxWidgetWatcher
     */
    public void	textBoxAction(TextBoxWidgetExt tbwe) {}

    public void checkBoxAction(CheckBoxWidgetExt checkBoxWidgetExt) {
	((SpNote)_spItem).setObserveInstruction(_w.observeInstruction.getBooleanValue());
    }
}

