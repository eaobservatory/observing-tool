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
import orac.jcmt.inst.SpInstHeterodyne;
import ot.util.DialogUtil;

/**
 * This is the editor for the Photom Observe Mode iterator component for SCUBA.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterPhotomObs extends EdIterJCMTGeneric
    implements TextBoxWidgetWatcher, ActionListener {

    private IterPhotomObsGUI _w;       // the GUI layout panel

    // If true, ignore action events
    private boolean ignoreActions = false;

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdIterPhotomObs() {
	super(new IterPhotomObsGUI());

	_title       ="Photom Iterator (SCUBA)";
	_presSource  = _w = (IterPhotomObsGUI)super._w;
	_description ="Photom Observation Mode (SCUBA)";
    }

    public void actionPerformed(ActionEvent e) { }
    public void textBoxKeyPress(TextBoxWidgetExt e) { }
    public void textBoxAction(TextBoxWidgetExt e) { }

    public void setInstrument(SpInstObsComp spInstObsComp) {
      if((spInstObsComp != null) && (spInstObsComp instanceof SpInstHeterodyne)) {
	DialogUtil.error(_w, "Photom Iterator cannot be used with Heterodyne.\nUse Stare Iterator instead.");
      }
      
      _w.switchingMode.setVisible(false);
      _w.switchingModeLabel.setVisible(false);
      _w.switchingModePanel.setVisible(false);
    }
}

