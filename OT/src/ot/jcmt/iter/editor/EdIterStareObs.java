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
import orac.jcmt.iter.SpIterStareObs;
import orac.jcmt.util.ScubaNoise;
import orac.jcmt.util.HeterodyneNoise;
import ot.util.DialogUtil;

/**
 * This is the editor for the Stare Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterStareObs extends EdIterJCMTGeneric implements ActionListener {

  private IterStareObsGUI _w;       // the GUI layout panel

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterStareObs() {
    super(new IterStareObsGUI());

    _title       ="Photometry/Sample";
    _presSource  = _w = (IterStareObsGUI)super._w;
    _description ="Photometry/Sampling Observation Mode";
    _w.widePhotom.addActionListener(this);

  }

    protected void _updateWidgets () {
	if ( _iterObs != null && ((SpIterStareObs)_iterObs).getWidePhotom() ) {
	    _w.widePhotom.setSelected(true);
	}
	else {
	    _w.widePhotom.setSelected(false);
	}
	super._updateWidgets();
    }
    

//  public void textBoxKeyPress(TextBoxWidgetExt e) {
//    super.textBoxKeyPress(tbwe);
//  }


  public void setInstrument(SpInstObsComp spInstObsComp) {
    if((spInstObsComp != null) && (spInstObsComp instanceof SpInstHeterodyne)) {
      _w.acsisPanel.setVisible(true);
      _w.widePhotom.setVisible(false);
      _w.widePhotom.setSelected(false);
    }
    else {
      _w.acsisPanel.setVisible(false);
      _w.widePhotom.setVisible(true);
    }

    super.setInstrument(spInstObsComp);
  }

  protected double calculateNoise(int integrations, double wavelength, double nefd, int [] status) {

    return ScubaNoise.noise_level(integrations, wavelength, "PHOT", nefd, status);
  }

    protected double calculateNoise(SpInstHeterodyne inst, double airmass, double tau) {
	double tSys = HeterodyneNoise.getTsys(inst.getFrontEnd(),
					      tau,
					      airmass,
					      inst.getRestFrequency(0)/1.0e9,
					      inst.getMode().equalsIgnoreCase("ssb"));

	_noiseToolTip = "airmass = "      + (Math.rint(airmass  * 10) / 10) +
	    ", Tsys = " + (Math.rint(tSys  * 10) / 10);
	return HeterodyneNoise.getHeterodyneNoise((SpIterStareObs)_iterObs, inst, tau, airmass);
    }

    public void actionPerformed (ActionEvent e) {
	((SpIterStareObs)_iterObs).setWidePhotom ( _w.widePhotom.isSelected() );
    }
}

