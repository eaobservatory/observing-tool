// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.inst.editor;

import gemini.sp.SpAvEditState;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import java.util.Observable;
import java.util.Observer;
import jsky.app.ot.editor.OtItemEditor;

/**
 * The base class for all instrument components.  It includes support for
 * a rotation angle text box, something all instruments support.
 */
public abstract class EdCompInstBase extends OtItemEditor implements Observer {

    private boolean _initialised = false;

    /**
	 * This method initializes the widgets in the presentation to reflect the
	 * current values of the items attributes.
	 */
	protected void _init()
	{
		if( _initialised )
			return;

		TextBoxWidgetExt tbwe;
		tbwe = getPosAngleTextBox();
		if( tbwe != null )
		{
			tbwe.addWatcher( new TextBoxWidgetWatcher()
			{
				public void textBoxKeyPress( TextBoxWidgetExt tbwe )
				{
					SpItem spItem = getCurrentSpItem();
					spItem.getAvEditFSM().deleteObserver( EdCompInstBase.this );
					( ( SpInstObsComp )spItem ).setPosAngleDegreesStr( tbwe.getText() );
					spItem.getAvEditFSM().addObserver( EdCompInstBase.this );
				}
	
				public void textBoxAction( TextBoxWidgetExt tbwe ){} // ignore
			} );
		}

		tbwe = getExposureTimeTextBox();
		if( tbwe != null )
		{
			tbwe.addWatcher( new TextBoxWidgetWatcher()
			{	
				public void textBoxKeyPress( TextBoxWidgetExt tbwe )
				{
					SpItem spItem = getCurrentSpItem();
					( ( SpInstObsComp )spItem ).setExposureTime( tbwe.getText() );
				}
	
				public void textBoxAction( TextBoxWidgetExt tbwe ){} // ignore
			} );
		}

		_initialised = true;
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
    public void	cleanup(SpItem spItem) {
	if (_spItem != null) {
	    _spItem.getAvEditFSM().deleteObserver(this);
	}
	super.cleanup();
    }


    /**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		TextBoxWidgetExt tbwe;
		tbwe = getPosAngleTextBox();
		if( tbwe != null )
			tbwe.setText( ( ( SpInstObsComp )_spItem ).getPosAngleDegreesStr() );

		tbwe = getExposureTimeTextBox();
		if( tbwe != null )
			tbwe.setText( ( ( SpInstObsComp )_spItem ).getExposureTimeAsString() );
	}

    /**
     * Observes the associated SpItem attribute table for changes to the
     * rotation angle.
     */
    public void update(Observable o, Object arg) {

	// Was it the spItem attributes?
	if (o instanceof SpAvEditState) {
	    TextBoxWidgetExt tbwe = getPosAngleTextBox();
	    String       newAngle = ((SpInstObsComp) _spItem).getPosAngleDegreesStr();
	    if (!newAngle.equals(tbwe.getText())) {
		tbwe.setText( newAngle );
	    }
	}
    }

    /** Return the position angle text box */
    public abstract TextBoxWidgetExt getPosAngleTextBox();

    /** Return the exposure time text box */
    public abstract TextBoxWidgetExt getExposureTimeTextBox();

    /** Return the coadds text box, or null if not available. */
    public abstract TextBoxWidgetExt getCoaddsTextBox();
}

