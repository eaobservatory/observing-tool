// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.ToggleButtonWidget;
import jsky.app.ot.gui.ToggleButtonWidgetWatcher;

/**
 * This is a helper object used to manage the various TpeImageFeatures.
 * This logically belongs with the TelescopePosEditor code but has been
 * moved out to simplify.
 */
final class TpeFeatureManager {

    private class TpeFeatureData {
	TpeImageFeature        tif;   // The image feature itself
	ToggleButtonWidget     tbw;   // The button used to toggle it

	public TpeFeatureData(TpeImageFeature tif, ToggleButtonWidget tbw) {
	    this.tif  = tif;
	    this.tbw = tbw;
	}
    }

    private static final String TOGGLE_BUTTON_ROOT = "toggleFeature";
    private TelescopePosEditor _tpe;
    private TelescopePosEditorToolBar _tpeToolBar;
    private TpeImageWidget _iw;
    private Hashtable      _featureMap = new Hashtable();

    TpeFeatureManager(TelescopePosEditor tpe, TpeImageWidget iw) {
	_tpe        = tpe;
	_tpeToolBar = _tpe.getTpeToolBar();
	_iw         = iw;

	// Hide all the toggle buttons
	Enumeration e = _enumerateToggleButtons();
	while (e.hasMoreElements()) {
	    ToggleButtonWidget tbw = (ToggleButtonWidget) e.nextElement();
	    tbw.setVisible(false);
	}
    }

    //
    // Get an enumeration of the toggle view buttons.
    //
    private Enumeration	_enumerateToggleButtons() {
	return new Enumeration() {
		private int i=0;

		public boolean hasMoreElements() {
		    try {
			return (_tpeToolBar.getViewToggleButton(i) != null);
		    }
		    catch(ArrayIndexOutOfBoundsException e) {
			return false;
		    }
		}
		public Object nextElement() {
		    return _tpeToolBar.getViewToggleButton(i++);
		}
	    };
    }

    //
    // Get an unoccupied toggle button.
    //
    private ToggleButtonWidget _allocateToggleButton() {
	// Find an open toggle button
	ToggleButtonWidget tbw = null;
	Enumeration               e = _enumerateToggleButtons();
	while (e.hasMoreElements()) {
	    ToggleButtonWidget tmp = (ToggleButtonWidget) e.nextElement();
	    if (!(tmp.isVisible())) {
		tbw = tmp;
		break;
	    }
	}
	return tbw;
    }

    //
    // Make a tool button available for allocation again.
    //
    private void _freeToggleButton(ToggleButtonWidget tbw) {
	tbw.setVisible(false);
	tbw.deleteWatchers();
    }


    /**
     * Add a feature.
     */
    public boolean addFeature(final TpeImageFeature tif) {
	// See if this feature is already present.
	if (_featureMap.get(tif.getName()) != null) {
	    return true;
	}

	ToggleButtonWidget tbw = _allocateToggleButton();
	if (tbw == null) {
	    return false;
	}

	_featureMap.put(tif.getName(), new TpeFeatureData(tif, tbw));

	tbw.addWatcher( new ToggleButtonWidgetWatcher() {
		public void toggleButtonAction(ToggleButtonWidget tbw) {
		    if (tbw.getBooleanValue()) {
			_iw.addFeature(tif);
		    } else {
			_iw.deleteFeature(tif);
		    }
		}
	    });

	tbw.setText(tif.getName());
	tbw.setToolTipText(tif.getDescription());
	tbw.setVisible(true);
	tbw.setEnabled(true);
	
	// hack, but need to enable this somewhere, otherwise catalog plotting 
	// wouldn't (shouldn't) be displayed
	if (tif.getName().equals("Catalog"))
	    tbw.setSelected(true);

	return true;
    }


    /**
     * Delete a feature.
     */
    public void deleteFeature(TpeImageFeature tif) {
	setVisible(tif, false);

	TpeFeatureData tfd = (TpeFeatureData) _featureMap.get(tif.getName());
	if (tfd == null) 
	    return;

	ToggleButtonWidget tbw = tfd.tbw;
	_freeToggleButton(tbw);
	_featureMap.remove(tif.getName());
    }


    /**
     * Get the named feature.
     */
    public TpeImageFeature getFeature(String name) {
	TpeFeatureData tfd = (TpeFeatureData) _featureMap.get(name);
	if (tfd == null) return null;

	return tfd.tif;
    }

    /**
     * Is the given feature already added?
     */
    public boolean isFeaturePresent(TpeImageFeature tif) {
	return (getFeature(tif.getName()) != null);
    }

    /**
     * Toggle viewing of the given image feature.
     */
    public void	toggleFeature(TpeImageFeature tif) {
	TpeFeatureData tfd = (TpeFeatureData) _featureMap.get(tif.getName());
	if (tfd == null) return;

	tfd.tbw.press();
    }

    /**
     * Make sure the given feature is displayed/hidden.
     */
    public void	setVisible(TpeImageFeature tif, boolean visible) {
	if (visible != tif.isVisible()) {
	    toggleFeature(tif);
	}
    }

    /**
     * Disable/enable the given feature.
     */
    public void	setDisabled(TpeImageFeature tif, boolean disabled) {
	TpeFeatureData tfd = (TpeFeatureData) _featureMap.get(tif.getName());
	if (tfd == null) return;

	setVisible(tif, !disabled);
	tfd.tbw.setEnabled(!disabled);
    }

    /**
     * Disable/enable the given features.
     */
    public void	setDisabled(Vector tifV, boolean disabled) {
	for (int i=0; i<tifV.size(); ++i) {
	    TpeImageFeature tif = (TpeImageFeature) tifV.elementAt(i);
	    setDisabled(tif, disabled);
	}
    }
}
