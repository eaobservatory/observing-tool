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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import jsky.app.ot.editor.OtItemEditor;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.iter.SpIterSkydipObs;
import orac.jcmt.inst.SpInstSCUBA;

/**
 * This is the editor for Skydip Observe Mode iterator component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterSkydipObs extends EdIterJCMTGeneric {

  private IterSkydipObsGUI _w;       // the GUI layout panel

  private SpIterSkydipObs _iterObs;

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterSkydipObs() {
    super(new IterSkydipObsGUI());

    _title       ="Skydip";
    _presSource  = _w = (IterSkydipObsGUI)super._w;
    _description ="Skydip Observation Mode";

    _w.startPosition.setChoices(SpIterSkydipObs.START_POSITIONS);

    _w.positions.addWatcher(this);
    _w.startPosition.addWatcher(this);
  }


  /**
   * Override setup to store away a reference to the Focus Iterator.
   */
  public void setup(SpItem spItem) {
    _iterObs = (SpIterSkydipObs) spItem;
    super.setup(spItem);
  }

  protected void _updateWidgets() {
    _w.positions.setValue(_iterObs.getPositions());
    _w.startPosition.setValue(_iterObs.getStartPosition());

    super._updateWidgets();
  }

  public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
    if (tbwe == _w.positions) {
      _iterObs.setPositions(_w.positions.getValue());
      return;
    }

    super.textBoxKeyPress(tbwe);
  }

  public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int index, String val) {
    if (ddlbwe == _w.startPosition) {
      _iterObs.setStartPosition(SpIterSkydipObs.START_POSITIONS[index]);
      return;
    }

    super.dropDownListBoxAction(ddlbwe, index, val);
  }
    
  public void setInstrument(SpInstObsComp spInstObsComp) {
    super.setInstrument(spInstObsComp);

    if((spInstObsComp != null) && (spInstObsComp instanceof SpInstSCUBA)) {
      _w.acsisPanel.setVisible(false);
    }
    else {
      _w.acsisPanel.setVisible(true);
    }
  }

}

