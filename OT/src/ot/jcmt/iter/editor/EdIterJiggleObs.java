/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.iter.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.CardLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import jsky.app.ot.editor.OtItemEditor;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstSCUBA;

/**
 * This is the editor for Jiggle Observe Mode iterator component.
 * 
 * @author modified by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterJiggleObs extends EdIterJCMTGeneric
    implements TextBoxWidgetWatcher, ActionListener {

    private IterJiggleObsGUI _w;       // the GUI layout panel

    // If true, ignore action events
    private boolean ignoreActions = false;

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdIterJiggleObs() {
        super(new IterJiggleObsGUI());

	_title       ="Jiggle Iterator";
	_presSource  = _w = (IterJiggleObsGUI)super._w;
	_description ="Jiggle Observation Mode";

/*
        System.out.println("_w = " + _w);
        System.out.println("super._w = " + super._w);
        System.out.println("_w.getClass() = " + _w.getClass());
        System.out.println("super._w.getClass() = " + super._w.getClass());
        System.out.println("_w.jigglesPerCycle.getText() = " + _w.jigglesPerCycle.getText());

	//initGenericWidgets();
/*
	for(int i = 0; i < 100; i++) {
	  _w.noOfIntegrations.addItem("" + (i + 1));
	}

	for(int i = 0; i < SWITCHING_MODES.length; i++) {
	  _w.switchingMode.addItem(SWITCHING_MODES[i]);
	}

	_w.noOfIntegrations.addActionListener(this);
	_w.switchingMode.addActionListener(this);
*/
    }


    public void actionPerformed(ActionEvent e) { }

    public void textBoxKeyPress(TextBoxWidgetExt e) { }
    public void textBoxAction(TextBoxWidgetExt e) { }

    public void setInstrument(SpInstObsComp spInstObsComp) {
      super.setInstrument(spInstObsComp);

      if((spInstObsComp != null) && (spInstObsComp instanceof SpInstSCUBA)) {
        //_w.switchingMode.setValue(SWITCHING_MODES[SWITCHING_MODE_CHOP]);
	//((CardLayout)_w.switchingModePanel.getLayout()).show(_w.switchingModePanel, SWITCHING_MODES[SWITCHING_MODE_CHOP]);
	//_w.switchingMode.setEnabled(false);
        _w.acsisPanel.setVisible(false);
      }
      else {
        //_w.switchingMode.setEnabled(true);
        _w.acsisPanel.setVisible(true);
      }
    }
}

