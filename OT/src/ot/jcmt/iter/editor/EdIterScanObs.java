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

import jsky.app.ot.util.CoordSys;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.iter.SpIterScanObs;
import ot.util.DialogUtil;


/**
 * This is the editor for the Scan Observe Mode iterator component (SCUBA).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterScanObs extends EdIterJCMTGeneric {

  private IterScanObsGUI _w;       // the GUI layout panel

  private SpIterScanObs _iterObs;

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterScanObs() {
    super(new IterScanObsGUI());

    _title       ="Scan Iterator (SCUBA)";
    _presSource  = _w = (IterScanObsGUI)super._w;
    _description ="Scan Observation Mode (SCUBA)";

    _w.system.setChoices(CoordSys.COORD_SYS);
    _w.coordFrame.setChoices(CoordSys.COORD_SYS);

    _w.x.addWatcher(this);
    _w.y.addWatcher(this);
    _w.theta.addWatcher(this);
    _w.system.addWatcher(this);
    _w.deltaX.addWatcher(this);
    _w.deltaY.addWatcher(this);
    _w.coordFrame.addWatcher(this);
    _w.posAngle.addWatcher(this);
  }

  /**
   * Override setup to store away a reference to the Scan Iterator.
   */
  public void setup(SpItem spItem) {
    _iterObs = (SpIterScanObs) spItem;
    super.setup(spItem);
  }

  protected void _updateWidgets() {
    _w.x.setValue(_iterObs.getX());
    _w.y.setValue(_iterObs.getY());
    _w.theta.setValue(_iterObs.getTheta());
    _w.system.setValue(_iterObs.getSystem());
    _w.deltaX.setValue(_iterObs.getDeltaX());
    _w.deltaY.setValue(_iterObs.getDeltaY());
    _w.coordFrame.setValue(_iterObs.getCoordFrame());
    _w.posAngle.setValue(_iterObs.getPosAngle());

    super._updateWidgets();
  }

  public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
    if(tbwe == _w.x) {
      _iterObs.setX(_w.x.getValue());
      return;
    }

    if(tbwe == _w.y) {
      _iterObs.setY(_w.y.getValue());
    }

    if(tbwe == _w.theta) {
      _iterObs.setTheta(_w.theta.getValue());
      return;
    }

    if(tbwe == _w.deltaX) {
      _iterObs.setDeltaX(_w.deltaX.getValue());
      return;
    }

    if(tbwe == _w.deltaY) {
      _iterObs.setDeltaY(_w.deltaY.getValue());
      return;
    }

    if(tbwe == _w.posAngle) {
      _iterObs.setPosAngle(_w.posAngle.getValue());
      return;
    }

    super.textBoxKeyPress(tbwe);
  }

  public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int index, String val) {
    if(ddlbwe == _w.system) {
      _iterObs.setSystem(CoordSys.COORD_SYS[index]);
      return;  
    }

    if(ddlbwe == _w.coordFrame) {
      _iterObs.setCoordFrame(CoordSys.COORD_SYS[index]);
      return;
    }

    super.dropDownListBoxAction(ddlbwe, index, val);
  }

  public void setInstrument(SpInstObsComp spInstObsComp) {
    if((spInstObsComp != null) && (spInstObsComp instanceof SpInstHeterodyne)) {
      DialogUtil.error(_w, "Scan Iterator cannot be used with Heterodyne.\nUse Raster Iterator instead.");
    }
      
    _w.switchingMode.setVisible(false);
    _w.switchingModeLabel.setVisible(false);
    _w.switchingModePanel.setVisible(false);
  }
}

