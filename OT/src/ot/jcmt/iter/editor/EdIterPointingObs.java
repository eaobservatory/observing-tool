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
 * This is the editor for Pointing Observe Mode iterator component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterPointingObs extends EdIterJCMTGeneric
    implements TextBoxWidgetWatcher, ActionListener {

    private IterPointingObsGUI _w;       // the GUI layout panel

    // If true, ignore action events
    private boolean ignoreActions = false;

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdIterPointingObs() {
        super(new IterPointingObsGUI());

	_title       ="Pointing Iterator";
	_presSource  = _w = (IterPointingObsGUI)super._w;
	_description ="Pointing Observation Mode";
    }

    public void actionPerformed(ActionEvent e) { }
    public void textBoxKeyPress(TextBoxWidgetExt e) { }
    public void textBoxAction(TextBoxWidgetExt e) { }

    public void setInstrument(SpInstObsComp spInstObsComp) {
      if((spInstObsComp != null) && (spInstObsComp instanceof SpInstSCUBA)) {
        _w.switchingMode.setValue(SWITCHING_MODES[SWITCHING_MODE_CHOP]);
	((CardLayout)_w.switchingModePanel.getLayout()).show(_w.switchingModePanel, SWITCHING_MODES[SWITCHING_MODE_CHOP]);
	_w.switchingMode.setEnabled(false);
        _w.acsisPanel.setVisible(false);
      }
      else {
        _w.switchingMode.setEnabled(true);
        _w.acsisPanel.setVisible(true);
      }
    }
}

