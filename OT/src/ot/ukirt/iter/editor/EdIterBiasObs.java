// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.iter.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import jsky.app.ot.editor.OtItemEditor;

import orac.ukirt.iter.SpIterBiasObs;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;

/**
 * This is the editor for Bias Observe iterator component.
 */
public final class EdIterBiasObs extends OtItemEditor
    implements TextBoxWidgetWatcher, ActionListener {

    private IterBiasObsGUI _w;       // the GUI layout panel

    // If true, ignore action events
    private boolean ignoreActions = false;

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdIterBiasObs() {
	_title       ="Bias Iterator";
	_presSource  = _w = new IterBiasObsGUI();
	_description ="Take the specified number of bias exposures.";

	// Note: The original bongo code used a SpinBoxWidget, but since Swing
	// doesn't have one, try using a JComboBox instead...
	Object[] ar = new Object[99];
	for (int i = 0; i < 99; i++)
	    ar[i] = new Integer(i+1);
	_w.repeatComboBox.setModel(new DefaultComboBoxModel(ar));
	_w.repeatComboBox.addActionListener(this);
    }

    /**
     */
    protected void _init() {
	TextBoxWidgetExt tbwe;
 
	// Coadds
	tbwe = _w.coadds;
	tbwe.addWatcher( this );
 
	super._init();
    }


    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
	ignoreActions = true;

	//try {
	    SpIterBiasObs iterObs = (SpIterBiasObs) _spItem;

	    // Repetitions
	    JComboBox sbw = _w.repeatComboBox;
	    sbw.getModel().setSelectedItem(new Integer(iterObs.getCount()));

	    TextBoxWidgetExt tbwe;
 
	    // Coadds
	    tbwe = _w.coadds;
	    tbwe.setValue( iterObs.getCoadds() );
	//}
	//catch(Exception e) {
	//    throw new RuntimeException(e.toString());
	//}

	ignoreActions = false;
    }

    /**
     * Watch changes to text boxes
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
	SpIterBiasObs iterObs = (SpIterBiasObs) _spItem;

	if (tbwe == _w.coadds) {
	    iterObs.setCoadds( tbwe.getText() );
	}
    }
 
    /**
     * Text box action.
     */
    public void textBoxAction(TextBoxWidgetExt tbwe) {}


    /**
     * Called when the value in the combo box is changed.
     */
    public void actionPerformed(ActionEvent evt) {
	if (ignoreActions)
	    return;

	SpIterBiasObs iterObs = (SpIterBiasObs) _spItem;

	JComboBox sbw = _w.repeatComboBox;
	int i = ((Integer)(sbw.getSelectedItem())).intValue();
	iterObs.setCount(i);
    }
}

