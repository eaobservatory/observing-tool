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
 * This is the editor for the Raster Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterRasterObs extends EdIterJCMTGeneric
    implements TextBoxWidgetWatcher, ActionListener {

    private IterRasterObsGUI _w;       // the GUI layout panel

    // If true, ignore action events
    private boolean ignoreActions = false;

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdIterRasterObs() {
	super(new IterRasterObsGUI());

	_title       ="Raster Iterator (ACSIS)";
	_presSource  = _w = (IterRasterObsGUI)super._w;
	_description ="Raster Observation Mode (ACSIS)";
    }

    public void actionPerformed(ActionEvent e) { }
    public void textBoxKeyPress(TextBoxWidgetExt e) { }
    public void textBoxAction(TextBoxWidgetExt e) { }

    public void setInstrument(SpInstObsComp spInstObsComp) {
      if((spInstObsComp != null) && (spInstObsComp instanceof SpInstSCUBA)) {
	DialogUtil.error(_w, "Raster Iterator cannot be used with SCUBA.\nUse Scan Iterator instead.");
      }
    }
}

