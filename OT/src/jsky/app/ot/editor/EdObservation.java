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

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;

import gemini.sp.SpAvEditState;
import gemini.sp.SpItem;
import gemini.sp.SpObs;

/**
 * This is the editor for the Observation item.
 */
public final class EdObservation extends OtItemEditor
    implements TextBoxWidgetWatcher, Observer, ActionListener {

    private TextBoxWidgetExt _obsTitle;
    private JLabel _obsState;
    private JToggleButton _priHigh, _priMedium, _priLow;

    private ObsGUI         _w;       // the GUI layout panel

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
	_w.chained.addActionListener(this);
	_w.chained.setVisible(false); // XXX allan: remove it?
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

	// Set the priority
	int pri = ((SpObs) _spItem).getPriority();
	switch (pri) {
	case SpObs.PRIORITY_HIGH:   _priHigh.setSelected(true);   break;
	case SpObs.PRIORITY_MEDIUM: _priMedium.setSelected(true); break;
	default:                    _priLow.setSelected(true);
	}

	// Set the chained state
	CheckBoxWidgetExt cbw = _w.chained;
	cbw.setValue( ((SpObs) _spItem).getChainedToNext() );
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

	CheckBoxWidgetExt cbw = _w.chained;
	cbw.setValue( ((SpObs) _spItem).getChainedToNext() );
    }


    /**
     * Handle standard action events.
     */
    public void actionPerformed(ActionEvent evt) {
	Object w  = evt.getSource();
	SpObs spObs = (SpObs) _spItem;
	
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
	if (w == _w.chained) {
	    CheckBoxWidgetExt cbw = (CheckBoxWidgetExt)w;
	    
	    _spItem.getAvEditFSM().deleteObserver(this);
	    spObs.chainToNext( cbw.getBooleanValue() );
	    _spItem.getAvEditFSM().addObserver(this);

	    cbw.setValue( spObs.getChainedToNext() );
	    return;
	}
    }
}

