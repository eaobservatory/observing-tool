/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.obsComp.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;

import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.editor.OtItemEditor;

import orac.jcmt.obsComp.SpSiteQualityObsComp;


/**
 * This is the editor for Site Quality component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
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

	_w.weatherBand1.addActionListener(this);
	_w.weatherBand2.addActionListener(this);
	_w.weatherBand3.addActionListener(this);
	_w.weatherBand4.addActionListener(this);
	_w.weatherBand5.addActionListener(this);

	grp = new ButtonGroup();
	grp.add(_w.weatherBand1);
	grp.add(_w.weatherBand2);
	grp.add(_w.weatherBand3);
	grp.add(_w.weatherBand4);
	grp.add(_w.weatherBand5);


	_w.seeing1.addActionListener(this);
	_w.seeing2.addActionListener(this);
	_w.seeing3.addActionListener(this);
	_w.seeing4.addActionListener(this);

	grp = new ButtonGroup();
	grp.add(_w.seeing1);
	grp.add(_w.seeing2);
	grp.add(_w.seeing3);
	grp.add(_w.seeing4);

/*
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
*/	
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
	SpSiteQualityObsComp sq = (SpSiteQualityObsComp) _spItem;
	OptionWidgetExt ow;
	int i;
/* MFO: TO BE IMPLEMENTED
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
*/	
    }


    /**
     * Called when a button is pressed
     */
    public void actionPerformed(ActionEvent evt) {

/* MFO: TO BE IMPLEMENTED
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
*/
    }
}
