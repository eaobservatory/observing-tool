/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package ot.jcmt.inst.editor;

import gemini.sp.*;
import gemini.sp.obsComp.*;
import jsky.app.ot.gui.KeyPressWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.TableWidgetExt;
import jsky.app.ot.gui.TableWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;
//import jsky.app.ot.gui.util.ErrorBox;

//import ot_ukirt.util.*;
import jsky.app.ot.editor.OtItemEditor;
import orac.ukirt.inst.SpDRRecipe;
import ot.jcmt.inst.editor.DataReductionScreen;

import java.awt.Event;
import java.util.Vector;

// MFO: Preliminary. Get rid of GUI stuff inside this class.
import javax.swing.border.TitledBorder;
import javax.swing.JPanel;
import java.awt.BorderLayout;



/**
 * This is the editor for the DRRecipe item.
 *
 * This class should be re-written. There should be a DRRecipeGUI class that contains the orac DR GUI
 * (currently DRRecipeGUI) and the DataReductionScreen.
 */
public final class EdDRRecipe extends OtItemEditor {
// MFO SWING empty shell so far

    //private DRRecipeGUI _w;		// the GUI layout
    private javax.swing.JPanel _w;		// the GUI layout


    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdDRRecipe() {
	_title       ="DR Recipe";

        // MFO: Preliminary. Get rid of GUI stuff inside this class.
	DRRecipeGUI         drRecipeGUI         = new DRRecipeGUI();
	DataReductionScreen dataReductionScreen = new DataReductionScreen();

        drRecipeGUI.setBorder(new TitledBorder("ORAC DR"));
        dataReductionScreen.setBorder(new TitledBorder("ACSIS DR"));

	javax.swing.JPanel panel = new javax.swing.JPanel();
	panel.setLayout(new java.awt.BorderLayout());
	panel.add(drRecipeGUI, java.awt.BorderLayout.NORTH);
	panel.add(dataReductionScreen, java.awt.BorderLayout.CENTER);
	
	_presSource  = _w = panel; //new DRRecipeGUI();
	_description ="The DR Recipe is configured with this component.";
    }


  protected void _updateWidgets() { }
}
