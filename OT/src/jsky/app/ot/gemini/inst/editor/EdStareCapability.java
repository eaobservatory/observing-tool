// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.inst.editor;

import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpStareCapability;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.editor.OtItemEditor;

/**
 * Support for coadds.
 */
public class EdStareCapability {

    /**
     * Get the SpStareCapability from an item editor.
     */
    private SpStareCapability _getStareCap(OtItemEditor itemEditor)  {
	SpInstObsComp spInst = (SpInstObsComp) itemEditor.getCurrentSpItem();
	String name = SpStareCapability.CAPABILITY_NAME;
	return (SpStareCapability) spInst.getCapability(name);
    }


    /**
     * This method initializes the widgets in the presentation to reflect the
     * current values of the items attributes.
     */
    protected void _init(final EdCompInstBase gw) {
	TextBoxWidgetExt tbwe;
	tbwe = gw.getCoaddsTextBox();
	tbwe.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _getStareCap(gw).setCoadds(tbwe.getText());
		}

		public void textBoxAction(TextBoxWidgetExt tbwe) {} // ignore
	    });
    }

    /**
     * Override _updateWidgets to show the value of the "coadds" attribute.
     */
    protected void _updateWidgets(EdCompInstBase gw, SpStareCapability stareCap) {
	TextBoxWidgetExt tbwe;
	tbwe = gw.getCoaddsTextBox();
	tbwe.setText( stareCap.getCoaddsAsString() );
    }

}

