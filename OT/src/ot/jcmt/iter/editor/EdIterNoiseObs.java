/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.iter.editor;

import jsky.app.ot.editor.OtItemEditor;

import jsky.app.ot.gui.DropDownListBoxWidgetExt;

import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.SpJCMTConstants;
import orac.jcmt.iter.SpIterNoiseObs;
import orac.jcmt.inst.SpInstHeterodyne;

/**
 * This is the editor for Noise Observe Mode iterator component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterNoiseObs extends EdIterJCMTGeneric {

  private IterNoiseObsGUI _w;       // the GUI layout panel

  private SpIterNoiseObs _iterObs;

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterNoiseObs() {
    super(new IterNoiseObsGUI());

    _title       ="Noise";
    _presSource  = _w = (IterNoiseObsGUI)super._w;
    _description ="Noise Observation Mode (SCUBA)";

    _w.noiseSourceComboBox.setChoices(SpJCMTConstants.NOISE_SOURCES);

    _w.noiseSourceComboBox.addWatcher(this);
  }

  /**
   * Override setup to store away a reference to the Noise Iterator.
   */
  public void setup(SpItem spItem) {
    _iterObs = (SpIterNoiseObs) spItem;
    super.setup(spItem);
  }

  protected void _updateWidgets() {
    _w.noiseSourceComboBox.setValue(_iterObs.getNoiseSource());

    super._updateWidgets();
  }

  public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int index, String val) {
    if(ddlbwe == _w.noiseSourceComboBox) {
      _iterObs.setNoiseSource(SpJCMTConstants.NOISE_SOURCES[index]);
      return;
    }

    super.dropDownListBoxAction(ddlbwe, index, val);
  }

  public void setInstrument(SpInstObsComp spInstObsComp) {
    super.setInstrument(spInstObsComp);

    if((spInstObsComp != null) && (spInstObsComp instanceof SpInstHeterodyne)) {
      _w.noiseSourceComboBox.setEnabled(false);
    }
    else {
      _w.noiseSourceComboBox.setEnabled(true);
    }
  }
}

