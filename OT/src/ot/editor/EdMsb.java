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
import javax.swing.JComboBox;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.editor.OtItemEditor;

import gemini.sp.SpItem;
import gemini.sp.SpObs;
import gemini.sp.SpMSB;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.util.OracUtilities;

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

// 	ButtonGroup grp = new ButtonGroup();
// 	grp.add(_w.priorityHigh);
// 	grp.add(_w.priorityMedium);
// 	grp.add(_w.priorityLow);

// 	_w.priorityHigh.addActionListener(this);
// 	_w.priorityMedium.addActionListener(this);
// 	_w.priorityLow.addActionListener(this);
	_w.jComboBox1.addActionListener(this);

	_w.remaining.addItem(SpMSB.REMOVED_STRING);

	for(int i = 0; i <= 100; i++) {
	  _w.remaining.addItem("" + i);
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
	_w.jComboBox1.setSelectedIndex(pri-1);
// 	switch (pri) {
// 	case SpObs.PRIORITY_HIGH:   _w.priorityHigh.setSelected(true);   break;
// 	case SpObs.PRIORITY_MEDIUM: _w.priorityMedium.setSelected(true); break;
// 	default:                    _w.priorityLow.setSelected(true);
// 	}

	ignoreActions = true;

	int numberRemaining = ((SpMSB)_spItem).getNumberRemaining();

	if(numberRemaining == SpMSB.REMOVED_CODE) {
	  _w.remaining.setValue(SpMSB.REMOVED_STRING);
	}
	else {
	  _w.remaining.setSelectedIndex(numberRemaining + 1);
	}

	ignoreActions = false;

	// Note that the elapsed time is re-calculated every time _updateWidgets is called.
	// It will not be written to or read from the SpMSB item until the Science Program
	// is saved to disk or stored to database.
	// Then OracUtilities.set
	// See what instrument we have...
	SpInstObsComp instrument = SpTreeMan.findInstrumentInContext(_spItem);
	if (instrument == null) {
	    instrument = SpTreeMan.findInstrument(_spItem.parent());
	}
	if (instrument == null || instrument instanceof SpInstHeterodyne) {
	    _w.estimatedTime.setText(OracUtilities.secsToHHMMSS(0, 1));
	}
	else {
	    _w.estimatedTime.setText(OracUtilities.secsToHHMMSS(((SpMSB)_spItem).getElapsedTime(), 1));
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


    public void actionPerformed(ActionEvent evt) {
	if(ignoreActions)
	  return;

	Object w  = evt.getSource();
	SpMSB spMSB = (SpMSB) _spItem;

	if(w == _w.remaining) {
	    if(_w.remaining.getSelectedItem().equals(SpMSB.REMOVED_STRING)) {
	        spMSB.setNumberRemaining(SpMSB.REMOVED_CODE);
	    }
	    else {
                spMSB.setNumberRemaining(_w.remaining.getSelectedIndex() - 1);
	    }
	}

	if ( w instanceof JComboBox ) {
	    spMSB.setPriority( ((Integer)_w.jComboBox1.getSelectedItem()).intValue() );
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

