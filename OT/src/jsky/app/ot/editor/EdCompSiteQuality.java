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

import gemini.sp.obsComp.SpSiteQualityObsComp;


/**
 * This is the editor for Site Quality component.
 */
public final class EdCompSiteQuality extends OtItemEditor 
    implements ActionListener {

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

	_w.iq20.addActionListener(this);
	_w.iq50.addActionListener(this);
	_w.iqIgnore.addActionListener(this);

	grp = new ButtonGroup();
	grp.add(_w.iq20);
	grp.add(_w.iq50);
	grp.add(_w.iqIgnore);


	_w.ir20.addActionListener(this);
	_w.ir50.addActionListener(this);
	_w.irIgnore.addActionListener(this);

	grp = new ButtonGroup();
	grp.add(_w.ir20);
	grp.add(_w.ir50);
	grp.add(_w.irIgnore);


	_w.moonDark.addActionListener(this);
	_w.moonBright.addActionListener(this);
	_w.moonIgnore.addActionListener(this);

	grp = new ButtonGroup();
	grp.add(_w.moonDark);
	grp.add(_w.moonBright);
	grp.add(_w.moonIgnore);


	_w.skyPhotometric.addActionListener(this);
	_w.skySpectroscopic.addActionListener(this);
	_w.skyIgnore.addActionListener(this);

	grp = new ButtonGroup();
	grp.add(_w.skyPhotometric);
	grp.add(_w.skySpectroscopic);
	grp.add(_w.skyIgnore);
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
	i = sq.getImageQuality();
	switch (i) {
	case SpSiteQualityObsComp.IMAGE_QUALITY_20:
	    ow = _w.iq20; break;
	case SpSiteQualityObsComp.IMAGE_QUALITY_50:
	    ow = _w.iq50; break;
	default:
	    ow = _w.iqIgnore; break;
	}
	ow.setValue(true);

	// IR Background
	i = sq.getIRBackground();
	switch (i) {
	case SpSiteQualityObsComp.IR_BACKGROUND_20:
	    ow = _w.ir20; break;
	case SpSiteQualityObsComp.IR_BACKGROUND_50:
	    ow = _w.ir50; break;
	default:
	    ow = _w.irIgnore; break;
	}
	ow.setValue(true);

	// Moon
	i = sq.getMoon();
	switch (i) {
	case SpSiteQualityObsComp.MOON_DARK:
	    ow = _w.moonDark; break;
	case SpSiteQualityObsComp.MOON_BRIGHT:
	    ow = _w.moonBright; break;
	default:
	    ow = _w.moonIgnore; break;
	}
	ow.setValue(true);

	// Sky
	i = sq.getSky();
	switch (i) {
	case SpSiteQualityObsComp.SKY_PHOTOMETRIC:
	    ow = _w.skyPhotometric; break;
	case SpSiteQualityObsComp.SKY_SPECTROSCOPIC:
	    ow = _w.skySpectroscopic; break;
	default:
	    ow = _w.skyIgnore; break;
	}
	ow.setValue(true);
    }


    /**
     * Called when a button is pressed
     */
    public void actionPerformed(ActionEvent evt) {

	Object w = evt.getSource();
	SpSiteQualityObsComp sq = (SpSiteQualityObsComp) _spItem;

	// Image Quality
	if (w == _w.iq20) {
	    sq.setImageQuality(SpSiteQualityObsComp.IMAGE_QUALITY_20);
	    return;
	}
	if (w == _w.iq50) {
	    sq.setImageQuality(SpSiteQualityObsComp.IMAGE_QUALITY_50);
	    return;
	}
	if (w == _w.iqIgnore) {
	    sq.setImageQuality(SpSiteQualityObsComp.IMAGE_QUALITY_IGNORE);
	    return;
	}

	// IR Background
	if (w == _w.ir20) {
	    sq.setIRBackground(SpSiteQualityObsComp.IR_BACKGROUND_20);
	    return;
	}
	if (w == _w.ir50) {
	    sq.setIRBackground(SpSiteQualityObsComp.IR_BACKGROUND_50);
	    return;
	}
	if (w == _w.irIgnore) {
	    sq.setIRBackground(SpSiteQualityObsComp.IR_BACKGROUND_IGNORE);
	    return;
	}

	// Moon
	if (w == _w.moonDark) {
	    sq.setMoon(SpSiteQualityObsComp.MOON_DARK);
	    return;
	}
	if (w == _w.moonBright) {
	    sq.setMoon(SpSiteQualityObsComp.MOON_BRIGHT);
	    return;
	}
	if (w == _w.moonIgnore) {
	    sq.setMoon(SpSiteQualityObsComp.MOON_IGNORE);
	    return;
	}

	// Sky
	if (w == _w.skyPhotometric) {
	    sq.setSky(SpSiteQualityObsComp.SKY_PHOTOMETRIC);
	    return;
	}
	if (w == _w.skySpectroscopic) {
	    sq.setSky(SpSiteQualityObsComp.SKY_SPECTROSCOPIC);
	    return;
	}
	if (w == _w.skyIgnore) {
	    sq.setSky(SpSiteQualityObsComp.SKY_IGNORE);
	    return;
	}
    }
}
