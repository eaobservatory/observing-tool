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
import ot.util.DialogUtil;

/**
 * This is the editor for the Stare Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterStareObs extends EdIterJCMTGeneric {

  private IterStareObsGUI _w;       // the GUI layout panel

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterStareObs() {
    super(new IterStareObsGUI());

    _title       ="Photom/Stare";
    _presSource  = _w = (IterStareObsGUI)super._w;
    _description ="Photom/Stare Observation Mode";
  }

//  public void textBoxKeyPress(TextBoxWidgetExt e) {
//    super.textBoxKeyPress(tbwe);
//  }

  public void setInstrument(SpInstObsComp spInstObsComp) {
    if((spInstObsComp != null) && (spInstObsComp instanceof SpInstSCUBA)) {
      DialogUtil.error(_w, "Stare Iterator cannot be used with SCUBA.\nUse Photom Iterator instead.");
    }
  }
}

