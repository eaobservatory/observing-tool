// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.inst.editor;

import javax.swing.*;
import jsky.app.ot.editor.OtItemEditor;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import gemini.sp.obsComp.SpChopCapability;
import gemini.sp.obsComp.SpInstObsComp;

/**
 * Support for exposures/chop pos, chop cycles/nod, and nod cycles/obs.
 */
public class EdChopCapability {

    /**
     * Get the SpChopCapability from an item editor.
     */
    private SpChopCapability _getChopCap(OtItemEditor itemEditor) {
	SpInstObsComp spInst = (SpInstObsComp) itemEditor.getCurrentSpItem();
	String name = SpChopCapability.CAPABILITY_NAME;
	return (SpChopCapability) spInst.getCapability(name);
    }

    /**
     * This method initializes the widgets in the presentation to reflect the
     * current values of the chopping attributes.
     */
    protected void _init(final MichelleGUI gw, final OtItemEditor itemEditor) {
	CheckBoxWidgetExt cbwe;
	cbwe = gw.nodding;
	cbwe.addWatcher( new CheckBoxWidgetWatcher() {
		public void checkBoxAction(CheckBoxWidgetExt cbwe) {
		    boolean nodding = cbwe.getBooleanValue();
		    SpChopCapability chopCap = _getChopCap(itemEditor);
		    chopCap.setNodding(nodding);
		    _updateWidgets(gw, chopCap);
		}
	    });

	TextBoxWidgetExt tbwe;

	tbwe = gw.expPerChopPos;
	tbwe.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _getChopCap(itemEditor).setExposuresPerChopPosition(tbwe.getText());
		}

		public void textBoxAction(TextBoxWidgetExt tbwe) {} // ignore
	    });

	tbwe = gw.chopCyclesPerNod;
	tbwe.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _getChopCap(itemEditor).setChopCyclesPerNod(tbwe.getText());
		}

		public void textBoxAction(TextBoxWidgetExt tbwe) {} // ignore
	    });

	tbwe = gw.cyclesPerObs;
	tbwe.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _getChopCap(itemEditor).setCyclesPerObserve(tbwe.getText());
		}

		public void textBoxAction(TextBoxWidgetExt tbwe) {} // ignore
	    });
    }

    /**
     * Update the chop control widgets that depend upon the value of the
     * nodding? attribute.
     */
    protected void _updateNoddingWidgets(MichelleGUI gw, boolean nodding) {
	TextBoxWidgetExt tbwe;
	tbwe = gw.chopCyclesPerNod;
	tbwe.setEnabled(nodding);

	JLabel stw;
	stw = gw.cyclesPerObsLabel;
	if (nodding) {
	    stw.setText("(nod cycles/obs)");
	} else {
	    stw.setText("(chop cycles/obs)");
	}
    }

    /**
     * Override _updateWidgets to show the value of the chopping attributes.
     */
    protected void _updateWidgets(MichelleGUI gw, SpChopCapability chopCap) {
	boolean nodding = chopCap.getNodding();

	CheckBoxWidgetExt cbwe;
	cbwe = gw.nodding;
	cbwe.setValue( nodding );
	_updateNoddingWidgets(gw, nodding);

	TextBoxWidgetExt tbwe;
	tbwe = gw.expPerChopPos;
	tbwe.setText( chopCap.getExposuresPerChopPositionAsString() );

	tbwe = gw.chopCyclesPerNod;
	if (nodding) {
	    tbwe.setText( chopCap.getChopCyclesPerNodAsString() );
	} else {
	    tbwe.setText( "" );
	}

	tbwe = gw.cyclesPerObs;
	tbwe.setText( chopCap.getCyclesPerObserveAsString() );
    }

}

