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

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import jsky.app.ot.util.CoordSys;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.OptionWidgetWatcher;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstSCUBA;
import orac.jcmt.iter.SpIterRasterObs;
import ot.util.DialogUtil;

/**
 * This is the editor for the Raster Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterRasterObs extends EdIterJCMTGeneric implements OptionWidgetWatcher {

  private IterRasterObsGUI _w;       // the GUI layout panel

  private SpIterRasterObs _iterObs;

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterRasterObs() {
    super(new IterRasterObsGUI());

    _title       ="Scan/Raster";
    _presSource  = _w = (IterRasterObsGUI)super._w;
    _description ="Scan/Raster Map";

    ButtonGroup grp = new ButtonGroup();
    grp.add(_w.alongRow);
    grp.add(_w.interleaved);

    _w.alongRow.setActionCommand(SpIterRasterObs.RASTER_MODE_ALONG_ROW);
    _w.interleaved.setActionCommand(SpIterRasterObs.RASTER_MODE_INTERLEAVED);

    _w.offSystem.setChoices(CoordSys.COORD_SYS);

    _w.xCenter.addWatcher(this);
    _w.yCenter.addWatcher(this);
    _w.width.addWatcher(this);
    _w.height.addWatcher(this);
    _w.rectanglePA.addWatcher(this);
    _w.offSystem.addWatcher(this);
    _w.alongRow.addWatcher(this);
    _w.interleaved.addWatcher(this);
    _w.rowsPerCal.addWatcher(this);
    _w.rowsPerRef.addWatcher(this);
    _w.rowReversal.addWatcher(this);
  }

  /**
   * Override setup to store away a reference to the Raster Iterator.
   */
  public void setup(SpItem spItem) {
    _iterObs = (SpIterRasterObs) spItem;
    super.setup(spItem);
  }

  protected void _updateWidgets() {
    _w.xCenter.setValue(_iterObs.getXCenter());
    _w.yCenter.setValue(_iterObs.getYCenter());
    _w.width.setValue(_iterObs.getWidth());
    _w.height.setValue(_iterObs.getHeight());
    _w.rectanglePA.setValue(_iterObs.getRectanglePA());
    _w.offSystem.setValue(_iterObs.getOffSystem());
    _w.rowsPerCal.setValue(_iterObs.getRowsPerCal());
    _w.rowsPerRef.setValue(_iterObs.getRowsPerRef());
    _w.rowReversal.setValue(_iterObs.getRowReversal());

    if(SpIterRasterObs.RASTER_MODE_ALONG_ROW.equals(_iterObs.getRasterMode())) {
      _w.alongRow.setSelected(true);
    }
    else {
      _w.interleaved.setSelected(true);
    }

    super._updateWidgets();
  }

  public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
    if(tbwe == _w.xCenter) {
      _iterObs.setXCenter(_w.xCenter.getValue());
      return;
    }

    if(tbwe == _w.yCenter) {
      _iterObs.setYCenter(_w.yCenter.getValue());
    }

    if(tbwe == _w.width) {
      _iterObs.setWidth(_w.width.getValue());
      return;
    }

    if(tbwe == _w.height) {
      _iterObs.setHeight(_w.height.getValue());
    }

    if(tbwe == _w.rectanglePA) {
      _iterObs.setRectanglePA(_w.rectanglePA.getValue());
      return;
    }

    if(tbwe == _w.rowsPerCal) {
      _iterObs.setRowsPerCal(_w.rowsPerCal.getValue());
      return;
    }

    if(tbwe == _w.rowsPerRef) {
      _iterObs.setRowsPerRef(_w.rowsPerRef.getValue());
      return;
    }

    super.textBoxKeyPress(tbwe);
  }

  public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int index, String val) {
    if(ddlbwe == _w.offSystem) {
      _iterObs.setOffSystem(CoordSys.COORD_SYS[index]);
      return;  
    }

    super.dropDownListBoxAction(ddlbwe, index, val);
  }

  public void optionAction(OptionWidgetExt owe) {
    _iterObs.setRasterMode(owe.getActionCommand());
  }

  public void checkBoxAction(CheckBoxWidgetExt cbwe) {
     if (cbwe == _w.rowReversal) {
      _iterObs.setRowReversal(_w.rowReversal.getBooleanValue());
      return;
    }

    super.checkBoxAction(cbwe);
  }

  public void setInstrument(SpInstObsComp spInstObsComp) {
    if((spInstObsComp != null) && (spInstObsComp instanceof SpInstSCUBA)) {
      _w.heterodynePanel.setVisible(false);
    }
    else {
      _w.heterodynePanel.setVisible(true);
    }
  }
}

