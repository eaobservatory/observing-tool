// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Observer;
import java.util.Observable;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import jsky.app.ot.OtCfg;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;

import gemini.sp.SpAvEditState;
import gemini.sp.SpItem;
import gemini.sp.SpObs;
import orac.util.OracUtilities;

/**
 * This is the editor for the Observation item.
 */
public final class EdObservation extends OtItemEditor
    implements TextBoxWidgetWatcher, CheckBoxWidgetWatcher, Observer, ActionListener {

    private TextBoxWidgetExt _obsTitle;
    private JLabel _obsState;
    private JToggleButton _priHigh, _priMedium, _priLow;

    private ObsGUI         _w;       // the GUI layout panel

    /**
     * If true, ignore action events.
     */
    private boolean ignoreActions = false;

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdObservation() {
	_title       ="Observation";
	_presSource  = _w = new ObsGUI();
	_description ="The observation is the smallest entity that can be scheduled.";

	_obsTitle  = _w.obsTitle;
	_obsTitle.addWatcher(this);

	_obsState  =  _w.obsState;

	_priHigh   = _w.priorityHigh;
	_priMedium = _w.priorityMedium;
	_priLow    = _w.priorityLow;

	ButtonGroup grp = new ButtonGroup();
	grp.add(_priHigh);
	grp.add(_priMedium);
	grp.add(_priLow);

	_w.priorityHigh.addActionListener(this);
	_w.priorityMedium.addActionListener(this);
	_w.priorityLow.addActionListener(this);
	_w.optional.addWatcher(this);
	_w.standard.addWatcher(this);
//	_w.chained.addActionListener(this);
//	_w.chained.setVisible(false); // XXX allan: remove it?

        // Added by MFO (22 February 2002)
	if(!OtCfg.telescopeUtil.supports(OtCfg.telescopeUtil.FEATURE_FLAG_AS_STANDARD)) {
          _w.standard.setText("Flag as Calibration");
	  _w.optional.setVisible(false);
	  _w.optional.deleteWatcher(this);
	}

	_w.remaining.addItem(SpObs.REMOVED_STRING);

	for(int i = 0; i <= 100; i++) {
	  _w.remaining.addItem("" + i);
	}

	_w.remaining.addActionListener(this);

        if(System.getProperty("OMP") != null) {
          _w.obsStateLabel.setVisible(false);
          _w.obsState.setVisible(false);
        }
        else {
          _w.remaining.setVisible(false);
          _w.remainingLabel.setVisible(false);
          _w.xLabel.setVisible(false);
          _w.estimatedTime.setVisible(false);
          _w.estimatedTimeLabel.setVisible(false);
        }
    }

    /**
     * Do any (one time) initialization.
     */
    protected void _init()  {
    }

    /**
     * The method is called when a new item is edited.  Reset the item being
     * observed.
     */
    public void	setup(SpItem spItem) {
	if (_spItem != null) {
	    _spItem.getAvEditFSM().deleteObserver(this);
	}
	super.setup(spItem);
	_spItem.getAvEditFSM().addObserver(this);
    }
 
    /**
     * The method is called when a new item is no longer being edited.
     * Remove ourselves as an observer.
     */
    public void cleanup(SpItem spItem) {
	if (_spItem != null) {
	    _spItem.getAvEditFSM().deleteObserver(this);
	}
	super.cleanup();
    }
 

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
	// Show the title
	String title = _spItem.getTitleAttr();
	if (title != null) {
	    _obsTitle.setText( title );
	} else {
	    _obsTitle.setText( "" );
	}

	String state = _avTab.get("state");
	if (state == null) {
	    _obsState.setText("Not in Active Database");
	} else {
	    _obsState.setText(state);
	}


	ignoreActions = true; // MFO

	// Set the priority
	int pri = ((SpObs) _spItem).getPriority();
	switch (pri) {
	case SpObs.PRIORITY_HIGH:   _priHigh.setSelected(true);   break;
	case SpObs.PRIORITY_MEDIUM: _priMedium.setSelected(true); break;
	default:                    _priLow.setSelected(true);
	}

	// Set the chained state
//	CheckBoxWidgetExt cbw = _w.chained;
//	cbw.setValue( ((SpObs) _spItem).getChainedToNext() );

	// Set whether or not this is a standard
	_w.standard.setSelected( ((SpObs) _spItem).getIsStandard() );

        // Added for OMP (MFO, 7 August 2001)
	_w.optional.setValue(((SpObs)_spItem).isOptional()); 

	int numberRemaining = ((SpObs)_spItem).getNumberRemaining();

	if(numberRemaining == SpObs.REMOVED_CODE) {
	  _w.remaining.setValue(SpObs.REMOVED_STRING);
	}
	else {
	  _w.remaining.setSelectedIndex(numberRemaining + 1);
	}

	ignoreActions = false;

	_w.estimatedTime.setText(OracUtilities.secsToHHMMSS(((SpObs)_spItem).getElapsedTime(), 1));
    
        _updateMsbDisplay();
    }


    /**
     * Checks whether SpObs is an MSB and calls _setMsbDisplay(true/false)
     * accordingly.
     *
     * Added for OMP. (MFO, 5 March 2002)
     *
     * @see #_setMsbDisplay(boolean)
     */
    protected void _updateMsbDisplay() {

      if(((SpObs)_spItem).isMSB()) {
        _w.msbPanel.setVisible(true);
	_w.optional.setVisible(false);
      }
      else {
        _w.msbPanel.setVisible(false);
	
	if((System.getProperty("OMP") != null) &&
	   (OtCfg.telescopeUtil.supports(OtCfg.telescopeUtil.FEATURE_FLAG_AS_STANDARD))) {

	  _w.optional.setVisible(true);
	}  
      }
    }


    /**
     * Watch changes to the title text box.
     * @see TextBoxWidgetWatcher
     */
    public void	textBoxKeyPress(TextBoxWidgetExt tbwe) {
	if (tbwe == _obsTitle) {
	    _spItem.setTitleAttr(_obsTitle.getText().trim());
	}
    }
 
    /**
     * Text box action, ignore.
     * @see TextBoxWidgetWatcher
     */
    public void	textBoxAction(TextBoxWidgetExt tbwe) {}
 
    /**
     * Observer of attribute/value table changes.  Observing to notice
     * changes to the "chainedToNext" state.
     */
    public void	update(Observable o, Object arg) {
	if (!(o instanceof SpAvEditState)) {
	    return;
	}

//	CheckBoxWidgetExt cbw = _w.chained;
//	cbw.setValue( ((SpObs) _spItem).getChainedToNext() );

	// Set whether or not this is a standard
	_w.standard.setSelected( ((SpObs) _spItem).getIsStandard() );


        _updateMsbDisplay(); // MFO
    }


    /**
     * Handle standard action events.
     */
    public void actionPerformed(ActionEvent evt) {
	if(ignoreActions)
	  return;

	Object w  = evt.getSource();
	SpObs spObs = (SpObs) _spItem;

	// Added for OMP (MFO, 7 August 2001)
	if(w == _w.remaining) {
	    if(_w.remaining.getSelectedItem().equals(SpObs.REMOVED_STRING)) {
	        spObs.setNumberRemaining(SpObs.REMOVED_CODE);
	    }
	    else {
                spObs.setNumberRemaining(_w.remaining.getSelectedIndex() - 1);
	    }
	}
	 
	if ((w instanceof AbstractButton) && ! ((AbstractButton)w).isSelected())
	    return;
	
	if (w == _priHigh) {
	    spObs.setPriority(SpObs.PRIORITY_HIGH);
	    return;
	}
	if (w == _priMedium) {
	    spObs.setPriority(SpObs.PRIORITY_MEDIUM);
	    return;
	}
	if (w == _priLow) {
	    spObs.setPriority(SpObs.PRIORITY_LOW);
	    return;
	}

	// Change the chained state
//	if (w == _w.chained) {
//	    CheckBoxWidgetExt cbw = (CheckBoxWidgetExt)w;
//	    
//	    _spItem.getAvEditFSM().deleteObserver(this);
//	    spObs.chainToNext( cbw.getBooleanValue() );
//	    _spItem.getAvEditFSM().addObserver(this);
//
//	    cbw.setValue( spObs.getChainedToNext() );
//	    return;
//	}

    }

    public void checkBoxAction(CheckBoxWidgetExt checkBoxWidgetExt) {
	if(checkBoxWidgetExt == _w.optional) {
	    ((SpObs)_spItem).setOptional(_w.optional.isSelected());
	    return;
	}

        if (checkBoxWidgetExt == _w.standard) {
	    // We can onlt do this if the observation is inside an MSB
	    SpItem parent = _spItem.parent();
	    while (parent != null) {
		if (parent instanceof gemini.sp.SpMSB) {
		    _spItem.getAvEditFSM().deleteObserver(this);
		    ((SpObs)_spItem).setIsStandard( _w.standard.getBooleanValue() );
		    _spItem.getAvEditFSM().addObserver(this);

		    _w.standard.setValue( ((SpObs)_spItem).getIsStandard() );

		    if(!OtCfg.telescopeUtil.supports(OtCfg.telescopeUtil.FEATURE_FLAG_AS_STANDARD)) {
			_w.optional.setValue(_w.standard.getBooleanValue());
			((SpObs)_spItem).setOptional(_w.standard.getBooleanValue());
		    }
		    return;
		}
		else {
		    parent = parent.parent();
		}
	    }
	    // If we get to here, then this is not inside an MSB so we can't do this
	    JOptionPane.showMessageDialog(null,
					  "This can only be flagged as a calibration if it is inside an MSB.",
					  "Operation not valid here",
					  JOptionPane.ERROR_MESSAGE);
           return;
        }
    }
}

