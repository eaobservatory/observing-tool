// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;

import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import gemini.sp.SpItem;

/**
 * This is the editor for the Science Program component.  At this point,
 * this editor is just a simple placeholder.
 */
public final class EdProgram extends OtItemEditor 
    implements TextBoxWidgetWatcher, ActionListener {

    // Attributes edited by this editor
    private static final String PI	= "piFull";
    private static final String COUNTRY	= "country";
    private static final String KIND	= "kind";

    private ProgramGUI         _w;       // the GUI layout panel

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdProgram() {
	_title       ="Program";
	_presSource  = _w = new ProgramGUI();
	_description ="General program information taken from the proposal.";
	_resizable   = true;

	// group the option buttons (radio buttons)
	ButtonGroup grp = new ButtonGroup();
	grp.add(_w.queueOption);
	grp.add(_w.classicalOption);
	_w.queueOption.addActionListener(this);
	_w.classicalOption.addActionListener(this);
    }

    /**
     * Do any (one time) initialization.
     */
    protected void _init() {
	TextBoxWidgetExt tbwe = _w.titleBox;
	tbwe.addWatcher(this);
    }
 


    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
	String val;

	// Title
	TextBoxWidgetExt tbwe = _w.titleBox;
	val  = _spItem.getTitleAttr();
	if (val == null) {
	    tbwe.setText("");
	} else {
	    tbwe.setText(val);
	}

	JLabel stbw;

	// PI 
	stbw = _w.piBox;
	val  = _avTab.get(PI);
	if (val == null) {
	    stbw.setText("");
	} else {
	    stbw.setText(val);
	}

	// Country
	stbw = _w.countryBox;
	val  = _avTab.get(COUNTRY);
	if (val == null) {
	    stbw.setText("");
	} else {
	    stbw.setText(val);
	}

	_showPropKind(_avTab.get(KIND));
    }

    /**
     * Set the Prop. Kind option widget.
     */
    void _showPropKind(String kind) {
	OptionWidgetExt ow;

	// Proposal Kind
	if ((kind == null) || kind.equals("queue")) {
	    ow = _w.queueOption;
	} else {
	    ow = _w.classicalOption;
	}
	ow.setValue(true);
    }

    /**
     * Watch changes to the title text box.
     * @see TextBoxWidgetWatcher
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbwe){
	if (tbwe == _w.titleBox) {
	    _spItem.setTitleAttr(tbwe.getText().trim());
	}
    }
 
    /**
     * Text box action, ignore.
     * @see TextBoxWidgetWatcher
     */
    public void textBoxAction(TextBoxWidgetExt tbwe) {}
 
    /**
     * Override the <code>action()</code> method to handle user events.
     */
    public void actionPerformed(ActionEvent evt) {
	Object w  = evt.getSource();

	// _showPropKind(_avTab.get(KIND));

	if (w == _w.queueOption) {
	    _showPropKind("queue");
	}
	else if (w == _w.classicalOption) {
	    _showPropKind("classical");
	}
    }
}

