// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import gemini.sp.SpItem;
import gemini.sp.SpAvTable;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterObserve;

/**
 * This is the editor for Observe iterator component.
 */
public final class EdIterObserve extends OtItemEditor 
    implements ActionListener {
    
    private IterObserveGUI  _w;       // the GUI layout panel

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdIterObserve() {
	_title       ="Observe Iterator";
	_presSource  = _w = new IterObserveGUI();
	_description ="Observe the specified number of times.";
	
	// Note: The original bongo code used a SpinBoxWidget, but since Swing
	// doesn't have one, try using a JComboBox instead...
	Object[] ar = new Object[99];
	for (int i = 0; i < 99; i++)
	    ar[i] = new Integer(i+1);
	_w.repeatComboBox.setModel(new DefaultComboBoxModel(ar));
	_w.repeatComboBox.addActionListener(this);
    }

    /**
     * Override setup to set the title and description correctly.
     */
    public void	setup(SpItem spItem) {
	super.setup(spItem);
	setEditorWindowTitle("Observe Iterator");
	setEditorWindowDescription("Take the specified number of exposures.");
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
	JComboBox sbw;

	SpIterObserveBase iterObserve = (SpIterObserveBase) _spItem;

	// Repetitions
	sbw = _w.repeatComboBox;
	_w.repeatComboBox.removeActionListener(this);
	sbw.setSelectedItem(new Integer(iterObserve.getCount()));
	_w.repeatComboBox.addActionListener(this);
    }

    /**
     * Called when the value in the combo box is changed.
     */
    public void actionPerformed(ActionEvent evt) {
	SpIterObserveBase iterObserve = (SpIterObserveBase) _spItem;

	JComboBox sbw = _w.repeatComboBox;
	int i = ((Integer)(sbw.getSelectedItem())).intValue();
	

	iterObserve.setCount(i);
    }
}

