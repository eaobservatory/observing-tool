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
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;
import java.util.Observer;
import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

import jsky.util.gui.DialogUtil;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.OptionWidgetWatcher;
import jsky.app.ot.tpe.TpeManager;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.SpJCMTConstants;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.iter.SpIterRasterObs;

/**
 * This is the editor for the Raster Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterRasterObs extends EdIterJCMTGeneric implements Observer, OptionWidgetWatcher,
									KeyListener {

  private IterRasterObsGUI _w;       // the GUI layout panel

  private SpIterRasterObs _iterObs;

  private final String [] SCAN_PA_CHOICES = { "automatic", "user def" };

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

    _w.scanAngle.setChoices(SCAN_PA_CHOICES);
//    _w.scanAngle.addItem(new javax.swing.JTextField("bla bla"));
    _w.scanSystem.setChoices(SpJCMTConstants.SCAN_SYSTEMS);

    _w.dx.addWatcher(this);
    _w.dy.addWatcher(this);
    _w.width.addWatcher(this);
    _w.height.addWatcher(this);
    _w.posAngle.addWatcher(this);
    _w.alongRow.addWatcher(this);
    _w.interleaved.addWatcher(this);
    _w.rowsPerCal.addWatcher(this);
    _w.rowsPerRef.addWatcher(this);
    _w.rowReversal.addWatcher(this);
    _w.scanSystem.addWatcher(this);
    _w.scanAngle.addWatcher(this);
    _w.scanAngle.getEditor().getEditorComponent().addKeyListener(this);
  }

  /**
   * Override setup to store away a reference to the Raster Iterator.
   */
  public void setup(SpItem spItem) {
    _iterObs = (SpIterRasterObs) spItem;

    super.setup(spItem);
    _iterObs.getAvEditFSM().addObserver(this);
  }

  protected void _updateWidgets() {
    try {
      _w.dx.setValue(_iterObs.getScanDx());
      _w.dx.setCaretPosition(0);
    }
    catch(UnsupportedOperationException e) {
      DialogUtil.message(_w, "Warning:\n" + e.getMessage());
    }

    _w.dy.setValue(_iterObs.getScanDy());
    _w.dy.setCaretPosition(0);
    _w.width.setValue(_iterObs.getWidth());
    _w.width.setCaretPosition(0);
    _w.height.setValue(_iterObs.getHeight());
    _w.height.setCaretPosition(0);
    _w.posAngle.setValue(_iterObs.getPosAngle());
    _w.posAngle.setCaretPosition(0);

/*MFO DEBUG*///try { throw new Exception("MFO DEBUG: _updateWidgets()"); } catch(Exception e) { e.printStackTrace(); }
    if((_iterObs.getScanAngles() == null) || (_iterObs.getScanAngles().size() == 0)) {
      _w.scanAngle.setEditable(false);
      _w.scanAngle.setValue(SCAN_PA_CHOICES[0]);
    }
    else {
      String scanAngleString = "";
      for(int i = 0; i < _iterObs.getScanAngles().size(); i++) {
        scanAngleString += ", " + _iterObs.getScanAngle(i);
      }

      _w.scanAngle.setEditable(true);
      _w.scanAngle.setValue(scanAngleString.substring(2));
    }

    _w.scanSystem.setValue(_iterObs.getScanSystem());
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
    _iterObs.getAvEditFSM().deleteObserver(this);

    if(tbwe == _w.dx) {
      _iterObs.setScanDx(_w.dx.getValue());
    }

    if(tbwe == _w.dy) {
      _iterObs.setScanDy(_w.dy.getValue());
    }

    if(tbwe == _w.width) {
      _iterObs.setWidth(_w.width.getValue());

      // Probably implemented in a different way in Gemini ot-2000B.12.
      try {
        TpeManager.get(_spItem).reset(_spItem);
      }
      catch(NullPointerException e) {
        // ignore
      }

    }

    if(tbwe == _w.height) {
      _iterObs.setHeight(_w.height.getValue());

      // Probably implemented in a different way in Gemini ot-2000B.12.
      try {
        TpeManager.get(_spItem).reset(_spItem);
      }
      catch(NullPointerException e) {
        // ignore
      }

    }

    if(tbwe == _w.posAngle) {
      _iterObs.setPosAngle(_w.posAngle.getValue());

      // Probably implemented in a different way in Gemini ot-2000B.12.
      try {
        TpeManager.get(_spItem).reset(_spItem);
      }
      catch(NullPointerException e) {
        // ignore
      }

    }

    if(tbwe == _w.rowsPerCal) {
      _iterObs.setRowsPerCal(_w.rowsPerCal.getValue());
    }

    if(tbwe == _w.rowsPerRef) {
      _iterObs.setRowsPerRef(_w.rowsPerRef.getValue());
    }

    super.textBoxKeyPress(tbwe);

    _iterObs.getAvEditFSM().addObserver(this);
  }

  public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int index, String val) {
    _iterObs.getAvEditFSM().deleteObserver(this);

    if(ddlbwe == _w.scanSystem) {
      _iterObs.setScanSystem(SpJCMTConstants.SCAN_SYSTEMS[index]);
      return;  
    }

    if(ddlbwe == _w.scanAngle) {
      if(_w.scanAngle.getValue().equals(SCAN_PA_CHOICES[0])) {
        _w.scanAngle.setEditable(false);

	_iterObs.setScanAngles(null);
      }
      
      if(_w.scanAngle.getValue().equals(SCAN_PA_CHOICES[1])) {
        _w.scanAngle.setEditable(true);
	_w.scanAngle.setValue("");

	_iterObs.setScanSystem(_w.scanSystem.getStringValue());
      }

      return;
    }

    if(ddlbwe == _w.scanSystem) {
      _iterObs.setScanAngles(_w.scanSystem.getStringValue());

      return;
    }

    super.dropDownListBoxAction(ddlbwe, index, val);

    _iterObs.getAvEditFSM().addObserver(this);
  }


  public void keyPressed(java.awt.event.KeyEvent e)  { }

  public void keyReleased(java.awt.event.KeyEvent e) {
    _iterObs.getAvEditFSM().deleteObserver(this);

     if(e.getSource() == _w.scanAngle.getEditor().getEditorComponent()) {
      _iterObs.setScanAngles(((JTextField)_w.scanAngle.getEditor().getEditorComponent()).getText());      
    }

    _iterObs.getAvEditFSM().addObserver(this);
  }

  public void keyTyped(java.awt.event.KeyEvent e) { }


  public void optionAction(OptionWidgetExt owe) {
    _iterObs.getAvEditFSM().deleteObserver(this);
    _iterObs.setRasterMode(owe.getActionCommand());
    _iterObs.getAvEditFSM().addObserver(this);
  }

  public void checkBoxAction(CheckBoxWidgetExt cbwe) {
    _iterObs.getAvEditFSM().deleteObserver(this);

     if (cbwe == _w.rowReversal) {
      _iterObs.setRowReversal(_w.rowReversal.getBooleanValue());
      return;
    }

    super.checkBoxAction(cbwe);

    _iterObs.getAvEditFSM().addObserver(this);
  }

  public void setInstrument(SpInstObsComp spInstObsComp) {
    if((spInstObsComp != null) && (spInstObsComp instanceof SpInstHeterodyne)) {
      _w.heterodynePanel.setVisible(true);
    }
    else {
      _w.heterodynePanel.setVisible(false);
    }

    super.setInstrument(spInstObsComp);
  }

  public void update(Observable o, Object arg) {
    _updateWidgets();
  }
}

