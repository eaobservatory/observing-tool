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
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.SpTreeMan;
import orac.jcmt.inst.SpInstSCUBA;
import orac.jcmt.iter.SpIterJCMTObs;


/**
 * This is the generic editor for JCMT iterator components.
 * 
 * @author modified by Martin Folger ( M.Folger@roe.ac.uk )
 */
public class EdIterJCMTGeneric extends OtItemEditor
    implements TextBoxWidgetWatcher {

    protected IterJCMTGenericGUI _w;       // the GUI layout panel

    // If true, ignore action events
    private boolean ignoreActions = false;

    protected static String [] SWITCHING_MODES = { IterJCMTGenericGUI.NOD,
                                                   IterJCMTGenericGUI.CHOP,
                                                   IterJCMTGenericGUI.FREQUENCY,
                                                   IterJCMTGenericGUI.NONE };

    protected static int SWITCHING_MODE_CHOP = 1;


    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdIterJCMTGeneric(IterJCMTGenericGUI w) {
	_title       ="JCMT Iterator";
	_presSource  = _w = w;
	_description ="Iterator Component for JCMT";

	for(int i = 0; i < 100; i++) {
	  _w.noOfIntegrations.addItem("" + (i + 1));
	}

	for(int i = 0; i < SWITCHING_MODES.length; i++) {
	  _w.switchingMode.addItem(SWITCHING_MODES[i]);
	}


	_w.switchingMode.addWatcher(new DropDownListBoxWidgetWatcher() {
	  public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int index, String val) {
            ((CardLayout)_w.switchingModePanel.getLayout()).show(_w.switchingModePanel, val);
	  }

	  public void dropDownListBoxSelect(DropDownListBoxWidgetExt ddlbwe, int index, String val) { }
	});


	_w.noOfIntegrations.addWatcher(new DropDownListBoxWidgetWatcher() {
	  public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int index, String val) {
            //try { 
	    ((SpIterJCMTObs)_spItem).setIntegrations(val);
	    //}
	    //catch(Exception e) {
	    //  ((SpIterObserveBase)_spItem).setCount(val);
	    //}
	  }

	  public void dropDownListBoxSelect(DropDownListBoxWidgetExt ddlbwe, int index, String val) { }
	});	
    }


    public void textBoxKeyPress(TextBoxWidgetExt tbwe) { }
    public void textBoxAction(TextBoxWidgetExt tbwe) { }

    protected void _updateWidgets() {
      setInstrument(SpTreeMan.findInstrument(_spItem));

      //try {
      _w.noOfIntegrations.setValue(((SpIterJCMTObs)_spItem).getIntegrations() - 1);
      //}
      //catch(Exception e) {
      //  _w.noOfIntegrations.setValue(((SpIterObserveBase)_spItem).getCount() - 1);
      //}
    }

    /**
     * This method should be overwritten by sub classes representing iterators whose appearance
     * is different for different instruments.
     */
    public void setInstrument(SpInstObsComp spInstObsComp) {
      if((spInstObsComp != null) && (spInstObsComp instanceof SpInstSCUBA)) {
	_w.switchingMode.setVisible(false);
        _w.switchingModeLabel.setVisible(false);
        _w.switchingModePanel.setVisible(false);
      }
      else {
	_w.switchingMode.setVisible(true);
        _w.switchingModeLabel.setVisible(true);      
        _w.switchingModePanel.setVisible(true);
      }
    }

}

