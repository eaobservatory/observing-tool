// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.inst.editor;

import java.awt.Event;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import orac.ukirt.inst.SpInstMichelle;
import orac.ukirt.inst.SpInstMichelleConstants;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import gemini.sp.SpItem;
import jsky.app.ot.tpe.TelescopePosEditor;
import jsky.app.ot.tpe.TpeManager;
import jsky.app.ot.util.MathUtil;


/**
 * This is the editor for Scheduling Info component.
 */
public final class EdCompInstMichelle extends EdCompInstBase {
    private EdChopCapability  _edChopCapability;
    private EdStareCapability _edStareCapability;
    private SpInstMichelle   _instMichelle;

    private MichelleGUI _w;	// the GUI layout

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdCompInstMichelle() {
	_title       ="Michelle";
	_presSource  = _w = new MichelleGUI();
	_description ="The Michelle instrument is configured with this component.";

	_edChopCapability  = new EdChopCapability();
	_edStareCapability = new EdStareCapability();
    }

    /**
     * This method initializes the widgets in the presentation to reflect the
     * current values of the items attributes.
     */
    protected void _init() {
	CommandButtonWidgetExt   cbw;
	DropDownListBoxWidgetExt ddlbw;
	TextBoxWidgetExt         tbw;

	//
	// Camera
	//
	ddlbw = _w.camera;
	ddlbw.setChoices(SpInstMichelleConstants.CAMERAS);

	ddlbw.addWatcher( new DropDownListBoxWidgetWatcher() {
		public void
		    dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

		public void
		    dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
		    _instMichelle.setCamera( val );
		    _updateWidgets();

		    TelescopePosEditor tpe = TpeManager.get(_instMichelle);
		    if (tpe != null) tpe.repaint();
		}
	    });

	//
	// Disperser
	//
	ddlbw = _w.disperser;
	ddlbw.setChoices(SpInstMichelleConstants.DISPERSERS);

	ddlbw.addWatcher( new DropDownListBoxWidgetWatcher() {
		public void
		    dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

		public void
		    dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
		    _instMichelle.setDisperser( val );
		    _updateWidgets();
		}
	    });


	//
	// Mask
	//
	ddlbw = _w.mask;
	ddlbw.setChoices(SpInstMichelleConstants.MASKS);

	ddlbw.addWatcher( new DropDownListBoxWidgetWatcher() {
		public void
		    dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

		public void
		    dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
		    _instMichelle.setMask( val );
		    _updateWidgets();

		    TelescopePosEditor tpe = TpeManager.get(_instMichelle);
		    if (tpe != null) tpe.repaint();
		}
	    });


	//
	// Filter
	//
	ddlbw = _w.filter;
	String[][] filts = SpInstMichelleConstants.FILTERS;
	Vector         v = new Vector();
	for (int i=0; i<filts.length; ++i) {
	    v.addElement(filts[i][0] + " (" + filts[i][1] + ")");
	}
	ddlbw.setChoices(v);

	ddlbw.addWatcher( new DropDownListBoxWidgetWatcher() {
		public void
		    dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

		public void
		    dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
		    _instMichelle.setFilter( _filtTrimWavelength(val) );
		}
	    });

	//
	// Mode
	//
	CheckBoxWidgetExt cbwe;
	cbwe = _w.mode;

	cbwe.addWatcher( new CheckBoxWidgetWatcher() {
		public void checkBoxAction(CheckBoxWidgetExt cb) {
		    if (cb.getBooleanValue()) {
			_instMichelle.setMode( SpInstMichelleConstants.CHOP_MODE );
		    } else {
			_instMichelle.setMode( SpInstMichelleConstants.STARE_MODE );
		    }
		    _updateWidgets();
		}
	    });

	//
	// Central Wavelength
	//
	tbw = _w.centralWavelength;
	tbw.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbw) {
		    _instMichelle.setCentralWavelength( tbw.getText() );
		    _updateWavelengthCoverage();
		    _updateDefaultCentralWavelengthButton();
		}

		public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
	    });

	cbw = _w.defaultCentralWavelength;
	cbw.addWatcher( new CommandButtonWidgetWatcher() {
		public void commandButtonAction(CommandButtonWidgetExt cbw) {
		    _instMichelle.useDefaultCentralWavelength();
		    _updateWidgets();
		}
	    });

	super._init();

	_edChopCapability._init(_w, this);
	_edStareCapability._init(this);
    }

    //
    // Trim the wavelength off a filter selection
    //
    private String _filtTrimWavelength(String filter) {
	return filter.substring(0, filter.lastIndexOf('(')).trim();
    }

    //
    // Add the wavelength to a filter selection
    //
    private int _filtGetIndex(String filter) {
	String[][] filts = SpInstMichelleConstants.FILTERS;
	for (int i=0; i<filts.length; ++i) {
	    if (filts[i][0].equals(filter)) {
		return i;
	    }
	}
	return 0;
    }

    /**
     * Override setup to store away a reference to the SpInstMichelle item.
     */
    public void setup(SpItem spItem) {
	_instMichelle = (SpInstMichelle) spItem;
	super.setup(spItem);
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
	DropDownListBoxWidgetExt ddlbw;
	TextBoxWidgetExt         tbw;

	ddlbw = _w.camera;
	ddlbw.setValue( _instMichelle.getCamera() );

	boolean disabled = false;
	if (_instMichelle.isImaging()) {
	    disabled = true;
	}

	ddlbw        = _w.disperser;
	ddlbw.setValue( _instMichelle.getDisperser() );
	ddlbw.setEnabled(!disabled);

	ddlbw         = _w.mask;
	ddlbw.setValue( _instMichelle.getMask() );
	ddlbw.setEnabled(!disabled);

	ddlbw         = _w.filter;
	ddlbw.setValue( _filtGetIndex(_instMichelle.getFilter()) );

	String mode = _instMichelle.getMode();

	CheckBoxWidgetExt cbwe;
	cbwe = _w.mode;

	((TitledBorder)_w.chopPanel.getBorder()).setTitle(mode + " Control");

	JPanel chopGW  = _w.chopControlGroup;
	JPanel stareGW = _w.stareControlGroup;
	// MFO JSKY // if (mode.equals(_instMichelle.STARE_MODE)) {
	// MFO JSKY //    _w.chopPanel.remove(chopGW);
	// MFO JSKY //     _w.chopPanel.add("Center", stareGW);
	// MFO JSKY //     _edStareCapability._updateWidgets(this, _instMichelle.getStareCapability());
	// MFO JSKY //     cbwe.setValue(false);
	// MFO JSKY // } else {
	    _w.chopPanel.remove(stareGW);
	    _w.chopPanel.add("Center", chopGW);
	    _edChopCapability._updateWidgets(_w, _instMichelle.getChopCapability());
	    cbwe.setValue(true);
	// MFO JSKY // }
	_w.chopPanel.revalidate();
	_w.getParent().repaint();

	tbw = (TextBoxWidgetExt) _w.centralWavelength;
	double centralWavelength = _instMichelle.getCentralWavelength();
	if (centralWavelength == 0.0) {
	    tbw.setText("");
	    tbw.setEnabled(false);
	} else {
	    tbw.setText( Double.toString(centralWavelength) );
	    tbw.setEnabled(true);
	}

	_updateScienceFOV();
	_updateWavelengthCoverage();
	_updateDefaultCentralWavelengthButton();

	super._updateWidgets();

    }

    //
    // Update the science field of view based upon the camera and mask
    // settings.
    //
    private void _updateScienceFOV() {
	TextBoxWidgetExt tbw = _w.scienceFOV;   
	double[] scienceArea = _instMichelle.getScienceArea();

	double w = MathUtil.round(scienceArea[0], 2);
	double h = MathUtil.round(scienceArea[1], 2);

	tbw.setText(w + " x " + h);
    }

    //
    // Update the wavelength coverage based upon the grating and central
    // wavelength.
    //
    private void _updateWavelengthCoverage() {
	TextBoxWidgetExt tbwLow, tbwHigh;

	tbwLow  = _w.wavelengthCoverageLow;
	tbwHigh = _w.wavelengthCoverageHigh;

	/* MFO JSKY
	double[] range = _instMichelle.getWavelengthCoverage();
	if ((range[0] == 0.0) || (range[1] == 0.0)) {
	    tbwLow.setText("");
	    tbwHigh.setText("");
	} else {
	    range[0] = MathUtil.round(range[0], 2);
	    range[1] = MathUtil.round(range[1], 2);
	    tbwLow.setText(Double.toString(range[0]));
	    tbwHigh.setText(Double.toString(range[1]));
	}
	MFO JSKY */
    }

    //
    // Update the "Use Default" button based upon the current instrument
    // mode and central wavelength.  If imaging, the button should be
    // disabled.  If the current central wavelength is the default one,
    // it should also be disabled.
    //
    private void _updateDefaultCentralWavelengthButton() {
	CommandButtonWidgetExt cbw;
	cbw = _w.defaultCentralWavelength;

	boolean disabled = false;
	if (_instMichelle.isImaging()) {
	    disabled = true;
	} else if (_instMichelle.getCentralWavelength() ==
		   _instMichelle.getDefaultCentralWavelength()) {
	    disabled = true;
	}
	cbw.setEnabled(!disabled);
    }

    /** Return the coadds text box. */
    public TextBoxWidgetExt getCoaddsTextBox() {
	return _w.coadds;
    }

    /** Return the position angle text box */
    public TextBoxWidgetExt getPosAngleTextBox() {
	return _w.posAngle;
    }

    /** Return the exposure time text box */
    public TextBoxWidgetExt getExposureTimeTextBox() {
	return _w.exposureTime;
    }
}
