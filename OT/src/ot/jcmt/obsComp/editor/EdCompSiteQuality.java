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

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.editor.OtItemEditor;

import orac.jcmt.obsComp.SpSiteQualityObsComp;


/**
 * This is the editor for Site Quality component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
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

	_w.minTau.addWatcher(this);
	_w.maxTau.addWatcher(this);
	_w.tauBandAllocated.addActionListener(this);
	_w.tauBandUserDefined.addActionListener(this);

	grp = new ButtonGroup();
	grp.add(_w.tauBandAllocated);
	grp.add(_w.tauBandUserDefined);


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
	if(sq.tauBandAllocated()) {
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
	if (w == _w.tauBandAllocated) {
	    sq.setTauBandAllocated(true);
	    //sq.setMinTau(0.0);
	    //sq.setMaxTau(0.0);

	    _w.minTau.setValue("");
	    _w.maxTau.setValue("");
	    _w.minTau.setEnabled(false);
	    _w.maxTau.setEnabled(false);

	}

	if (w == _w.tauBandUserDefined) {
	    sq.setTauBandAllocated(false);
	    //sq.setMinTau(_w.minTau.getDoubleValue(0.0));
	    //sq.setMaxTau(_w.maxTau.getDoubleValue(Double.POSITIVE_INFINITY));

	    _w.minTau.setValue(sq.getMinTau());
	    _w.maxTau.setValue(sq.getMaxTau());
	    _w.minTau.setEnabled(true);
	    _w.maxTau.setEnabled(true);
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
    }
 
    /**
     * Text box action, ignore.
     */
    public void	textBoxAction(TextBoxWidgetExt tbwe) { }

}
