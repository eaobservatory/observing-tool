/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.iter.editor ;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

import gemini.sp.SpItem ;
import gemini.sp.obsComp.SpInstObsComp ;
import orac.jcmt.iter.SpIterFocusObs ;
import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.inst.SpInstSCUBA2 ;

/**
 * This is the editor for Focus Observe Mode iterator component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterFocusObs extends EdIterJCMTGeneric implements ItemListener
{
	private IterFocusObsGUI _w ; // the GUI layout panel
	private SpIterFocusObs _iterObs ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterFocusObs()
	{
		super( new IterFocusObsGUI() ) ;

		_title = "Focus" ;
		_presSource = _w = ( IterFocusObsGUI )super._w ;
		_description = "Focus Observation Mode" ;

		_w.axis.setChoices( SpIterFocusObs.AXES ) ;

		_w.axis.addWatcher( this ) ;
		_w.steps.addWatcher( this ) ;
		_w.focusPoints.addWatcher( this ) ;

		_w.automaticTarget.setToolTipText( "Automatically determine focus/align target at time of observation" ) ;

		_w.switchingMode.setVisible( false ) ;
		_w.switchingModeLabel.setVisible( false ) ;
		_w.frequencyPanel.setVisible( false ) ;

                _w.pol2_in_beam.addItemListener(this);
                _w.fts2_in_beam.addItemListener(this);
	}

	/**
	 * Override setup to store away a reference to the Focus Iterator.
	 */
	public void setup( SpItem spItem )
	{
		_iterObs = ( SpIterFocusObs )spItem ;
		super.setup( spItem ) ;
	}

	protected void _updateWidgets()
	{
		_w.axis.setValue( _iterObs.getAxis() ) ;
		_w.steps.setValue( _iterObs.getSteps() ) ;
		_w.focusPoints.setValue( _iterObs.getFocusPoints() ) ;

                _w.pol2_in_beam.setSelected(_iterObs.isPol2InBeam());
                _w.fts2_in_beam.setSelected(_iterObs.isFts2InBeam());

		super._updateWidgets() ;
	}

	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{

		if( tbwe == _w.steps )
			_iterObs.setSteps( _w.steps.getValue() ) ;
		else if( tbwe == _w.focusPoints )
			_iterObs.setFocusPoints( _w.focusPoints.getValue() ) ;
		else
			super.textBoxKeyPress( tbwe ) ;
	}

	public void dropDownListBoxAction( DropDownListBoxWidgetExt ddlbwe , int index , String val )
	{
		if( ddlbwe == _w.axis )
		{
			_iterObs.setAxis( SpIterFocusObs.AXES[ index ] ) ;
			_iterObs.setSteps( SpIterFocusObs.getDefaultSteps( SpIterFocusObs.AXES[ index ] ) ) ;
			_w.steps.setValue( _iterObs.getSteps() ) ;
		}
		else
		{
			super.dropDownListBoxAction( ddlbwe , index , val ) ;
		}
	}

	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		super.setInstrument( spInstObsComp ) ;

		if( spInstObsComp != null )
		{
			if( spInstObsComp instanceof SpInstHeterodyne )
			{
				_w.acsisPanel.setVisible( true ) ;
				_w.switchingMode.setVisible( false ) ;
				_w.switchingModeLabel.setVisible( false ) ;

                                _w.fts2_in_beam.setVisible(false);
                                _w.pol2_in_beam.setVisible(false);
			}
			else if( spInstObsComp instanceof SpInstSCUBA2 )
			{
				_w.acsisPanel.setVisible( false ) ;
				_iterObs.rmSwitchingMode() ;

                                _w.fts2_in_beam.setVisible(true);
                                _w.pol2_in_beam.setVisible(true);
			}
		}
		else
		{
			_w.acsisPanel.setVisible( false ) ;
			_iterObs.rmSwitchingMode() ;

			_w.stepsLabel.setVisible( false ) ;
			_w.steps.setVisible( false ) ;
			_w.mmLabel.setVisible( false ) ;

                        _w.fts2_in_beam.setVisible(false);
                        _w.pol2_in_beam.setVisible(false);
		}
	}

        public void itemStateChanged(ItemEvent e) {
                if (e.getSource() == _w.pol2_in_beam) {
                        _iterObs.setPol2InBeam(_w.pol2_in_beam.isSelected());
                }
                else if (e.getSource() == _w.fts2_in_beam) {
                        _iterObs.setFts2InBeam(_w.fts2_in_beam.isSelected());
                }
        }
}
