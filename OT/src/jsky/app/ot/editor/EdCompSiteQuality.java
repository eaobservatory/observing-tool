// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;

import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import gemini.sp.obsComp.SpSiteQualityObsComp;


/**
 * This is the editor for Site Quality component.
 */
public final class EdCompSiteQuality extends OtItemEditor 
    implements ActionListener, TextBoxWidgetWatcher {

    private SiteQualityGUI _w;       // the GUI layout panel


    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdCompSiteQuality() {
	_title       ="Site Quality";
	_presSource  = _w = new SiteQualityGUI();
	_description ="Observing constraints set here are used to schedule the telescope.";

	// add action listeners and group the buttons
	ButtonGroup grp;

// 	_w.seeingExcellent.addActionListener(this);
// 	_w.seeingGood.addActionListener(this);
// 	_w.seeingPoor.addActionListener(this);
// 	_w.seeingAny.addActionListener(this);
	_w.seeingAllocated.addActionListener(this);
	_w.seeingUserDefined.addActionListener(this);
	_w.minSeeing.addWatcher(this);
	_w.maxSeeing.addWatcher(this);

	grp = new ButtonGroup();
	grp.add(_w.seeingAllocated);
	grp.add(_w.seeingUserDefined);


// 	_w.csoVeryDry.addActionListener(this);
// 	_w.csoAny.addActionListener(this);
	_w.tauBandAllocated.addActionListener(this);
	_w.tauBandUserDefined.addActionListener(this);
	_w.minTau.addWatcher(this);
	_w.maxTau.addWatcher(this);

	grp = new ButtonGroup();
	grp.add(_w.tauBandAllocated);
	grp.add(_w.tauBandUserDefined);


	_w.moonDark.addActionListener(this);
	_w.moonGrey.addActionListener(this);
	_w.moonAny.addActionListener(this);

	grp = new ButtonGroup();
	grp.add(_w.moonDark);
	grp.add(_w.moonGrey);
	grp.add(_w.moonAny);


	_w.cloudPhotometric.addActionListener(this);
	_w.cloudThinCirrus.addActionListener(this);
	_w.cloudAny.addActionListener(this);

	grp = new ButtonGroup();
	grp.add(_w.cloudPhotometric);
	grp.add(_w.cloudThinCirrus);
	grp.add(_w.cloudAny);
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
	SpSiteQualityObsComp sq = (SpSiteQualityObsComp) _spItem;
	OptionWidgetExt ow;
	int i;

	// Image Quality
// 	i = sq.getSeeing();
// 	switch (i) {
// 	case SpSiteQualityObsComp.SEEING_EXCELLENT:
// 	    ow = _w.seeingExcellent; break;
// 	case SpSiteQualityObsComp.SEEING_GOOD:
// 	    ow = _w.seeingGood; break;
// 	case SpSiteQualityObsComp.SEEING_POOR:
// 	    ow = _w.seeingPoor; break;
// 	default:
// 	    ow = _w.seeingAny; break;
// 	}
	if (sq.tauBandAllocated()) {
	    _w.tauBandAllocated.setValue(true);
	    _w.minTau.setValue("");
	    _w.maxTau.setValue("");
	    _w.minTau.setEnabled(false);
	    _w.maxTau.setEnabled(false);
	}
	else {
	    _w.tauBandUserDefined.setValue(true);
	    _w.minTau.setValue(sq.getMinTau());
	    _w.maxTau.setValue(sq.getMaxTau());
	    _w.minTau.setEnabled(true);
	    _w.maxTau.setEnabled(true);
	}
// 	ow.setValue(true);

	// IR Background
// 	i = sq.getCsoTau();
// 	switch (i) {
// 	case SpSiteQualityObsComp.CSO_TAU_VERY_DRY:
// 	    ow = _w.csoVeryDry; break;
// 	default:
// 	    ow = _w.csoAny; break;
// 	}
// 	ow.setValue(true);
	if (sq.seeingAllocated()) {
	    _w.seeingAllocated.setValue(true);
	    _w.minSeeing.setValue("");
	    _w.maxSeeing.setValue("");
	    _w.minSeeing.setEnabled(false);
	    _w.maxSeeing.setEnabled(false);
	}
	else {
	    _w.seeingUserDefined.setValue(true);
	    _w.minSeeing.setValue(sq.getMinSeeing());
	    _w.maxSeeing.setValue(sq.getMaxSeeing());
	    _w.minSeeing.setEnabled(true);
	    _w.maxSeeing.setEnabled(true);
	}

	// Moon
	i = sq.getMoon();
	switch (i) {
	case SpSiteQualityObsComp.MOON_DARK:
	    ow = _w.moonDark; break;
	case SpSiteQualityObsComp.MOON_GREY:
	    ow = _w.moonGrey; break;
	default:
	    ow = _w.moonAny; break;
	}
	ow.setValue(true);

	// Sky
	i = sq.getCloud();
	switch (i) {
	case SpSiteQualityObsComp.CLOUD_PHOTOMETRIC:
	    ow = _w.cloudPhotometric; break;
	case SpSiteQualityObsComp.CLOUD_THIN_CIRRUS:
	    ow = _w.cloudThinCirrus; break;
	default:
	    ow = _w.cloudAny; break;
	}
	ow.setValue(true);
    }


    /**
     * Called when a button is pressed
     */
    public void actionPerformed(ActionEvent evt) {

	Object w = evt.getSource();
	SpSiteQualityObsComp sq = (SpSiteQualityObsComp) _spItem;

	// Seeing
// 	if (w == _w.seeingExcellent) {
// 	    sq.setSeeing(SpSiteQualityObsComp.SEEING_EXCELLENT);
// 	    return;
// 	}
// 	if (w == _w.seeingGood) {
// 	    sq.setSeeing(SpSiteQualityObsComp.SEEING_GOOD);
// 	    return;
// 	}
// 	if (w == _w.seeingPoor) {
// 	    sq.setSeeing(SpSiteQualityObsComp.SEEING_POOR);
// 	    return;
// 	}
// 	if (w == _w.seeingAny) {
// 	    sq.setSeeing(SpSiteQualityObsComp.SEEING_ANY);
// 	    return;
// 	}
	if (w == _w.seeingAllocated) {
	    sq.setSeeingAllocated(true);
	    _w.minSeeing.setValue("");
	    _w.maxSeeing.setValue("");
	    _w.minSeeing.setEnabled(false);
	    _w.maxSeeing.setEnabled(false);
	}
	
	if (w == _w.seeingUserDefined) {
	    sq.setSeeingAllocated(false);
	    _w.minSeeing.setValue(sq.getMinSeeing());
	    _w.maxSeeing.setValue(sq.getMaxSeeing());
	    _w.minSeeing.setEnabled(true);
	    _w.maxSeeing.setEnabled(true);
	}

	// CSO Tau
// 	if (w == _w.csoVeryDry) {
// 	    sq.setCsoTau(SpSiteQualityObsComp.CSO_TAU_VERY_DRY);
// 	    return;
// 	}
// 	if (w == _w.csoAny) {
// 	    sq.setCsoTau(SpSiteQualityObsComp.CSO_TAU_ANY);
// 	    return;
// 	}
	if (w == _w.tauBandAllocated) {
	    sq.setTauBandAllocated(true);
	    _w.minTau.setValue("");
	    _w.maxTau.setValue("");
	    _w.minTau.setEnabled(false);
	    _w.maxTau.setEnabled(false);

	}

	if (w == _w.tauBandUserDefined) {
	    sq.setTauBandAllocated(false);
	    _w.minTau.setValue(sq.getMinTau());
	    _w.maxTau.setValue(sq.getMaxTau());
	    _w.minTau.setEnabled(true);
	    _w.maxTau.setEnabled(true);
	}

	// Moon
	if (w == _w.moonDark) {
	    sq.setMoon(SpSiteQualityObsComp.MOON_DARK);
	    return;
	}
	if (w == _w.moonGrey) {
	    sq.setMoon(SpSiteQualityObsComp.MOON_GREY);
	    return;
	}
	if (w == _w.moonAny) {
	    sq.setMoon(SpSiteQualityObsComp.MOON_ANY);
	    return;
	}

	// Cloud
	if (w == _w.cloudPhotometric) {
	    sq.setCloud(SpSiteQualityObsComp.CLOUD_PHOTOMETRIC);
	    return;
	}
	if (w == _w.cloudThinCirrus) {
	    sq.setCloud(SpSiteQualityObsComp.CLOUD_THIN_CIRRUS);
	    return;
	}
	if (w == _w.cloudAny) {
	    sq.setCloud(SpSiteQualityObsComp.CLOUD_ANY);
	    return;
	}
    }

    /**
     * Watch changes to min text boxes.
     */
    public void	textBoxKeyPress(TextBoxWidgetExt tbwe) {
	if (tbwe == _w.minTau) {
	    ((SpSiteQualityObsComp)_spItem).setMinTau(_w.minTau.getValue());
	    return;
	}

	if (tbwe == _w.maxTau) {
	    ((SpSiteQualityObsComp)_spItem).setMaxTau(_w.maxTau.getValue());
	    return;
	}

	if (tbwe == _w.minSeeing) {
	    System.out.println("Updating min seeing to "+_w.minSeeing.getValue());
	    ((SpSiteQualityObsComp)_spItem).setMinSeeing(_w.minSeeing.getValue());
	    return;
	}

	if (tbwe == _w.maxSeeing) {
	    System.out.println("Updating max seeing to "+_w.maxSeeing.getValue());
	    ((SpSiteQualityObsComp)_spItem).setMaxSeeing(_w.maxSeeing.getValue());
	    return;
	}
    }

    /**
     * Text box action, ignore.
     */
    public void	textBoxAction(TextBoxWidgetExt tbwe) { }
}
