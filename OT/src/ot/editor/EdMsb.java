// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.editor.OtItemEditor;

import gemini.sp.SpItem;
import gemini.sp.SpObs;
import gemini.sp.SpMSB;

/**
 * MSB folder editor.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on jsky/app/ot/editor/EdTitle.java
 */
public final class EdMsb extends OtItemEditor implements TextBoxWidgetWatcher, ActionListener {

    private MsbEditorGUI _w;   // the GUI layout

    /**
     * If true, ignore action events.
     */
    private boolean ignoreActions = false;

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdMsb() {
	_title       = "MSB Editor";
	_presSource  = _w = new MsbEditorGUI();
	_description = "MSB information.";

	ButtonGroup grp = new ButtonGroup();
	grp.add(_w.priorityHigh);
	grp.add(_w.priorityMedium);
	grp.add(_w.priorityLow);

	_w.priorityHigh.addActionListener(this);
	_w.priorityMedium.addActionListener(this);
	_w.priorityLow.addActionListener(this);

	for(int i = 0; i < 100; i++) {
	  _w.remaining.addItem("" + (i + 1));
	}

	_w.remaining.addActionListener(this);
    }

    /**
     * Do any (one time) initialization.
     */
    protected void _init() {
	TextBoxWidgetExt tbw = _w.nameBox;
	tbw.addWatcher(this);
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
	// Show the title
	TextBoxWidgetExt tbw = _w.nameBox;
	String title = _spItem.getTitleAttr();
	if (title != null) {
	    tbw.setText( title );
	} else {
	    tbw.setText( "" );
	}

	// Set the priority
	int pri = ((SpMSB) _spItem).getPriority();
	switch (pri) {
	case SpObs.PRIORITY_HIGH:   _w.priorityHigh.setSelected(true);   break;
	case SpObs.PRIORITY_MEDIUM: _w.priorityMedium.setSelected(true); break;
	default:                    _w.priorityLow.setSelected(true);
	}

	ignoreActions = true;
	_w.remaining.setSelectedIndex(((SpMSB)_spItem).getNumberRemaining() - 1);
	ignoreActions = false;
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


    public void actionPerformed(ActionEvent evt) {
	if(ignoreActions)
	  return;

	Object w  = evt.getSource();
	SpMSB spMSB = (SpMSB) _spItem;

	if(w == _w.remaining) {
            spMSB.setNumberRemaining(_w.remaining.getSelectedIndex() + 1);
	}

	if ((w instanceof AbstractButton) && ! ((AbstractButton)w).isSelected())
	    return;
	
	if (w == _w.priorityHigh) {
	    spMSB.setPriority(SpObs.PRIORITY_HIGH);
	    return;
	}
	if (w == _w.priorityMedium) {
	    spMSB.setPriority(SpObs.PRIORITY_MEDIUM);
	    return;
	}
	if (w == _w.priorityLow) {
	    spMSB.setPriority(SpObs.PRIORITY_LOW);
	    return;
	}
    }
}

