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

	_w.tauBand1.addActionListener(this);
	_w.tauBand2.addActionListener(this);
	_w.tauBand3.addActionListener(this);
	_w.tauBand4.addActionListener(this);
	_w.tauBand5.addActionListener(this);

	grp = new ButtonGroup();
	grp.add(_w.tauBand1);
	grp.add(_w.tauBand2);
	grp.add(_w.tauBand3);
	grp.add(_w.tauBand4);
	grp.add(_w.tauBand5);


	_w.seeing1.addActionListener(this);
	_w.seeing2.addActionListener(this);
	_w.seeing3.addActionListener(this);
	_w.seeing4.addActionListener(this);
	_w.seeingAny.addActionListener(this);

	grp = new ButtonGroup();
	grp.add(_w.seeing1);
	grp.add(_w.seeing2);
	grp.add(_w.seeing3);
	grp.add(_w.seeing4);
	grp.add(_w.seeingAny);
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
	SpSiteQualityObsComp sq = (SpSiteQualityObsComp) _spItem;
	OptionWidgetExt ow;
	int i;

	// Tau Band
	i = sq.getTauBand();
	switch (i) {
	case 0:
	    _w.tauBand1.setValue(true); break;
	case 1:
	    _w.tauBand2.setValue(true); break;
	case 2:
	    _w.tauBand3.setValue(true); break;
	case 3:
	    _w.tauBand4.setValue(true); break;
	case 4:
	    _w.tauBand5.setValue(true); break;
	default:
	    _w.tauBand1.setValue(false);
	    _w.tauBand2.setValue(false);
	    _w.tauBand3.setValue(false);
	    _w.tauBand4.setValue(false);
	    _w.tauBand5.setValue(false); break;
	}

	// Seeing
	i = sq.getSeeing();
	switch (i) {
	case 0:
	    _w.seeing1.setValue(true); break;
	case 1:
	    _w.seeing2.setValue(true); break;
	case 2:
	    _w.seeing3.setValue(true); break;
	case 3:
	    _w.seeing4.setValue(true); break;	    
	case 4:
	    _w.seeingAny.setValue(true); break;
	default:
	    _w.seeing1.setValue(false);
	    _w.seeing2.setValue(false);
	    _w.seeing3.setValue(false);
	    _w.seeing4.setValue(false);
	    _w.seeingAny.setValue(false);
	    break;
	}

    }


    /**
     * Called when a button is pressed
     */
    public void actionPerformed(ActionEvent evt) {

	Object w = evt.getSource();
	SpSiteQualityObsComp sq = (SpSiteQualityObsComp) _spItem;

	// Tau band
	if (w == _w.tauBand1) {
	    sq.setTauBand(0);
	}

	if (w == _w.tauBand2) {
	    sq.setTauBand(1);
	}

	if (w == _w.tauBand3) {
	    sq.setTauBand(2);
	}

	if (w == _w.tauBand4) {
	    sq.setTauBand(3);
	}

	if (w == _w.tauBand5) {
	    sq.setTauBand(4);
	}


	// Seeing
	if (w == _w.seeing1) {
	    sq.setSeeing(0);
	}

	if (w == _w.seeing2) {
	    sq.setSeeing(1);
	}

	if (w == _w.seeing3) {
	    sq.setSeeing(2);
	}

	if (w == _w.seeing4) {
	    sq.setSeeing(3);
	}

	if (w == _w.seeingAny) {
	    sq.setSeeing(4);
	}
    }
}
