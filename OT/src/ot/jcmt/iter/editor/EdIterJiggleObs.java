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

import jsky.app.ot.OtCfg;
import jsky.app.ot.editor.OtItemEditor;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.util.Angle;
import gemini.util.DDMMSS;
import orac.jcmt.inst.SpJCMTInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.inst.SpInstSCUBA;
import orac.jcmt.iter.SpIterJiggleObs;
import orac.jcmt.obsComp.SpSiteQualityObsComp;
import orac.jcmt.util.ScubaNoise;
import orac.util.SpItemUtilities;

/**
 * This is the editor for Jiggle Observe Mode iterator component.
 * 
 * @author modified by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterJiggleObs extends EdIterJCMTGeneric {

  private IterJiggleObsGUI _w;       // the GUI layout panel

  private SpIterJiggleObs _iterObs;

  private String [] _noJigglePatterns = { "No Instrument in scope." };

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterJiggleObs() {
    super(new IterJiggleObsGUI());

    _title       ="Jiggle";
    _presSource  = _w = (IterJiggleObsGUI)super._w;
    _description ="Jiggle Observation Mode";

    //_w.jigglePattern.setChoices(SpIterJiggleObs.JIGGLE_PATTERNS);

    _w.jigglePattern.addWatcher(this);
  }

  /**
   * Override setup to store away a reference to the Focus Iterator.
   */
  public void setup(SpItem spItem) {
    _iterObs = (SpIterJiggleObs) spItem;
    super.setup(spItem);
  }

  protected void _updateWidgets() {
    SpJCMTInstObsComp instObsComp = (SpJCMTInstObsComp)SpTreeMan.findInstrument(_iterObs);

    if(instObsComp != null) {
      _w.jigglePattern.setChoices(instObsComp.getJigglePatterns());

      // Select jiggle pattern.
      boolean jigglePatternSet = false;
      String jigglePattern = _iterObs.getJigglePattern();
      for(int i = 0; i < _w.jigglePattern.getItemCount(); i++) {
        if(jigglePattern == null) {
          break;
	}

        if(jigglePattern.equals(_w.jigglePattern.getItemAt(i))) {
          _w.jigglePattern.setValue(_w.jigglePattern.getItemAt(i));
	  jigglePatternSet = true;
	  break;
	}
      }

      if(!jigglePatternSet) {
        _iterObs.setJigglePattern((String)_w.jigglePattern.getValue());
      }
    }
    else {
      _w.jigglePattern.setChoices(_noJigglePatterns);
      _iterObs.setJigglePattern("");
    }

    super._updateWidgets();
  }

  public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int index, String val) {
    if(ddlbwe == _w.jigglePattern) {
      _iterObs.setJigglePattern(val);
      return;
    }

    super.dropDownListBoxAction(ddlbwe, index, val);
  }

  public void setInstrument(SpInstObsComp spInstObsComp) {
    super.setInstrument(spInstObsComp);

    if((spInstObsComp != null) && (spInstObsComp instanceof SpInstHeterodyne)) {
      //_w.switchingMode.setValue(SWITCHING_MODES[SWITCHING_MODE_CHOP]);
      //((CardLayout)_w.switchingModePanel.getLayout()).show(_w.switchingModePanel, SWITCHING_MODES[SWITCHING_MODE_CHOP]);
      //_w.switchingMode.setEnabled(false);
      _w.acsisPanel.setVisible(true);
    }
    else {
      //_w.switchingMode.setEnabled(true);
      _w.acsisPanel.setVisible(false);
    }
  }

  protected String calculateNoise() {
    SpTelescopeObsComp telescopeObsComp = (SpTelescopeObsComp)SpTreeMan.findTargetList(_iterObs);
    if(telescopeObsComp == null) {
      return "No target";
    }

    SpJCMTInstObsComp instObsComp       = (SpJCMTInstObsComp)SpTreeMan.findInstrument(_iterObs);
    if(instObsComp == null) {
      return "No instrument";
    }

    SpSiteQualityObsComp siteQualityObsComp = (SpSiteQualityObsComp)SpItemUtilities.findSiteQuality(_iterObs);
    if(siteQualityObsComp == null) {
      return "No site quality";
    }

    if(instObsComp instanceof SpInstSCUBA) {
      int [] status     = { 0 };
      double noise      = 0.0;
      int integrations  = _iterObs.getIntegrations();
      String mode       = "JIG16";
      double decRadians = Angle.degreesToRadians(telescopeObsComp.getPosList().getBasePosition().getXaxis());
      double latRadians = Angle.degreesToRadians(DDMMSS.valueOf(OtCfg.getTelescopeLatitude()));
      double tau        = siteQualityObsComp.getNoiseCalculationTau();
      double wavelength;

      if(_iterObs.isJIG64((SpInstSCUBA)instObsComp)) {
        mode = "JIG64";
      }

      if(((((SpInstSCUBA)instObsComp).getFilter() != null) &&
          (((SpInstSCUBA)instObsComp).getFilter().toUpperCase().endsWith("PHOT")))) {

	if(((SpInstSCUBA)instObsComp).getPrimaryBolometer() == null) {
	  return "No wavelength";
	}

	wavelength = Double.parseDouble(((SpInstSCUBA)instObsComp).getPrimaryBolometer().substring(1));
	noise = ScubaNoise.noise_level(integrations, wavelength, mode, decRadians, latRadians, tau, status);

	if(status[0] == 0) {
	  return "" + noise;
	}
      }
      else {
	String noise450Str;

	double noise450 =
	        ScubaNoise.noise_level(integrations, 450.0,  mode, decRadians, latRadians, tau, status);

	if(status[0] == 0) {
	  noise450Str = "" + (Math.rint(noise450 * 10) / 10);
	}
	else {
	  noise450Str = "error " + status[0];
	}

	String noise850Str;

	double noise850 =
	        ScubaNoise.noise_level(integrations, 850.0,  mode, decRadians, latRadians, tau, status);

	if(status[0] == 0) {
	  noise850Str = "" + (Math.rint(noise850 * 10) / 10);
	}
	else {
	  noise850Str = "error " + status[0];
	}

	return "" + noise450Str + " (450), " + noise850Str + " (850)";
      }
    }

    return "Not for Heterodyne";
  }
}

