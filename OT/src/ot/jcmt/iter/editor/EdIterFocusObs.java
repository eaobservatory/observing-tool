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
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.iter.SpIterFocusObs;
import orac.jcmt.inst.SpInstSCUBA;

/**
 * This is the editor for Focus Observe Mode iterator component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterFocusObs extends EdIterJCMTGeneric
    implements TextBoxWidgetWatcher, DropDownListBoxWidgetWatcher {

  private IterFocusObsGUI _w;       // the GUI layout panel

  // If true, ignore action events
  //private boolean ignoreActions = false;

  private SpIterFocusObs _iterObs;


  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterFocusObs() {
    super(new IterFocusObsGUI());

    _title       ="Focus Iterator";
    _presSource  = _w = (IterFocusObsGUI)super._w;
    _description ="Focus Observation Mode";

    _w.axis.setChoices(SpIterFocusObs.AXES);

    _w.axis.addWatcher(this);
    _w.steps.addWatcher(this);
    _w.focusPoints.addWatcher(this);
  }

  /**
   * Override setup to store away a reference to the Focus Iterator.
   */
  public void setup(SpItem spItem) {
    _iterObs = (SpIterFocusObs) spItem;
    super.setup(spItem);
  }

  protected void _updateWidgets() {
    _w.axis.setValue(_iterObs.getAxis());
    _w.steps.setValue(_iterObs.getSteps());
    _w.focusPoints.setValue(_iterObs.getFocusPoints());

    super._updateWidgets();
  }

  public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
    if (tbwe == _w.steps)       { _iterObs.setSteps(_w.steps.getValue()); }
    if (tbwe == _w.focusPoints) { _iterObs.setFocusPoints(_w.focusPoints.getValue()); }
  }

  public void textBoxAction(TextBoxWidgetExt tbwe) { }

  public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int index, String val) {
    if (ddlbwe == _w.axis) { _iterObs.setAxis(SpIterFocusObs.AXES[index]); }
  }
    
  public void dropDownListBoxSelect(DropDownListBoxWidgetExt ddlbwe, int index, String val) { }

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

