/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.editor;

import gemini.sp.SpItem;
import gemini.sp.obsComp.SpSchedConstObsComp;
import orac.util.OracUtilities;
import ot.util.DialogUtil;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.editor.OtItemEditor;

import java.util.Calendar;
import java.util.Date;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.ButtonGroup;

/**
 * This is the editor for Site Quality component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdCompSchedConstraints extends OtItemEditor implements TextBoxWidgetWatcher , ActionListener
{
	private SchedConstraintsGUI _w; // the GUI layout panel
	private SpSchedConstObsComp _schedConstObsComp;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdCompSchedConstraints()
	{
		_title = "Scheduling Constraints";
		_presSource = _w = new SchedConstraintsGUI();
		_description = "Observing constraints set here are used to schedule the observation.";

		ButtonGroup meridianApproachButtons = new ButtonGroup();
		meridianApproachButtons.add( _w.meridianApproachRising );
		meridianApproachButtons.add( _w.meridianApproachSetting );
		meridianApproachButtons.add( _w.meridianApproachAny );

		_w.earliest.addWatcher( this );
		_w.latest.addWatcher( this );
		_w.minElevation.addWatcher( this );
		_w.maxElevation.addWatcher( this );
		_w.period.addWatcher( this );
		_w.airmassCB.addActionListener( this );
		_w.meridianApproachRising.addActionListener( this );
		_w.meridianApproachSetting.addActionListener( this );
		_w.meridianApproachAny.addActionListener( this );
	}

	public void setup( SpItem spItem )
	{
		_schedConstObsComp = ( SpSchedConstObsComp )spItem;
		super.setup( spItem );

		if( _schedConstObsComp.getEarliest().equals( SpSchedConstObsComp.NO_VALUE ) )
		{
			_schedConstObsComp.initEarliest( OracUtilities.toISO8601( new Date() ) );
			_updateWidgets();
		}

		if( _schedConstObsComp.getLatest().equals( SpSchedConstObsComp.NO_VALUE ) )
		{
			Calendar calendar = Calendar.getInstance();
			calendar.set( Calendar.YEAR , calendar.get( Calendar.YEAR ) + 30 );
			_schedConstObsComp.initLatest( OracUtilities.toISO8601( calendar.getTime() ) );
			_updateWidgets();
		}
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		_w.earliest.setValue( _schedConstObsComp.getEarliest() );
		_w.latest.setValue( _schedConstObsComp.getLatest() );

		if( _schedConstObsComp.getDisplayAirmass() )
		{
			_w.airmassCB.removeActionListener( this );
			_w.airmassCB.setSelected( true );
			_w.airmassCB.addActionListener( this );
			_w.jLabel6.setVisible( false );
			_w.jLabel9.setText( "Max" );
			_w.jLabel10.setText( "Min" );

		}
		else
		{
			_w.jLabel6.setVisible( true );
			_w.jLabel9.setText( "Min" );
			_w.jLabel10.setText( "Max" );
		}

		if( _schedConstObsComp.getMinElevation() != null && !_schedConstObsComp.getMinElevation().equals( "" ) )
		{
			// Get the value as a double
			double elevation = Double.valueOf( _schedConstObsComp.getMinElevation() ).doubleValue();
			// See if we should display this as an airmass
			if( _w.airmassCB.isSelected() )
			{
				// Convert the elevation to an airmass
				double airmass = _elevationToAirmass( elevation );
				// Display airmass to 3 diecimal places
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits( 3 );
				nf.setGroupingUsed( false );
				_w.minElevation.setValue( nf.format( airmass ) );
			}
			else
			{
				// Just show the elevation
				_w.minElevation.setValue( _schedConstObsComp.getMinElevation() );
			}
		}
		else
		{
			_w.minElevation.setValue( _schedConstObsComp.getMinElevation() );
		}

		if( _schedConstObsComp.getMaxElevation() != null && !_schedConstObsComp.getMaxElevation().equals( "" ) )
		{
			// Get the value as a double
			double elevation = Double.valueOf( _schedConstObsComp.getMaxElevation() ).doubleValue();
			// See if we should display this as an airmass
			if( _w.airmassCB.isSelected() )
			{
				// Convert the elevation to an airmass
				double airmass = _elevationToAirmass( elevation );
				// Display airmass to 3 diecimal places
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits( 3 );
				nf.setGroupingUsed( false );
				_w.maxElevation.setValue( nf.format( airmass ) );
			}
			else
			{
				// Just show the elevation
				_w.maxElevation.setValue( _schedConstObsComp.getMaxElevation() );
			}
		}
		else
		{
			_w.maxElevation.setValue( _schedConstObsComp.getMaxElevation() );
		}

		_w.period.setValue( _schedConstObsComp.getPeriod() );

	    if( _schedConstObsComp.getMeridianApproach() != null )
		{
			if( _schedConstObsComp.getMeridianApproach().equals( SpSchedConstObsComp.SOURCE_RISING ) )
				_w.meridianApproachRising.setValue( true ) ;
			else
				_w.meridianApproachSetting.setValue( true ) ;
		}
		else
		{
			_w.meridianApproachAny.setValue( true ) ;
		}
	}

	/**
	 * Watch changes to the "Earliest" and "Latest" text boxes.
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbw )
	{
		// By converting the string to Date and back to a an ISO8601 format string
		// it gets tidied up in case the user made minor mistakes regarding the format.

		try
		{
			if( tbw == _w.earliest )
			{
				if( _w.earliest.getText().equals( "" ) )
				{
					_schedConstObsComp.setEarliest( "none" );
				}
				else
				{
					String earliest = OracUtilities.toISO8601( OracUtilities.parseISO8601( _w.earliest.getText() ) );
					_schedConstObsComp.setEarliest( earliest );
				}
			}
			else if( tbw == _w.latest )
			{
				if( _w.latest.getText().equals( "" ) )
				{
					_schedConstObsComp.setLatest( "none" );
				}
				else
				{
					String latest = OracUtilities.toISO8601( OracUtilities.parseISO8601( _w.latest.getText() ) );
					_schedConstObsComp.setLatest( latest );
				}
			}
			else if( tbw == _w.minElevation )
			{
				// If we aren't using airmass, just store the elevation
				if( !_w.airmassCB.isSelected() || "".equals( _w.minElevation.getValue() ) )
				{
					_schedConstObsComp.setMinElevation( _w.minElevation.getValue() );
				}
				else
				{
					// User is inputing airmass, so convert it to degrees elevation
					double airmass = Double.valueOf( _w.minElevation.getValue() ).doubleValue();
					double elevation = _airmassToElevation( airmass );
					// Just store the converted value to 3 decimal places
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits( 3 );
					nf.setGroupingUsed( false );
					_schedConstObsComp.setMinElevation( nf.format( elevation ) );
				}
			}
			else if( tbw == _w.maxElevation )
			{
				// If we aren't using airmass, just store the elevation
				if( !_w.airmassCB.isSelected() || _w.maxElevation.getValue().length() == 0 )
				{
					_schedConstObsComp.setMaxElevation( _w.maxElevation.getValue() );
				}
				else
				{
					// User is inputing airmass, so convert it to degrees elevation
					double airmass = Double.valueOf( _w.maxElevation.getValue() ).doubleValue();
					double elevation = _airmassToElevation( airmass );
					// Just store the converted value to 3 decimal places
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits( 3 );
					nf.setGroupingUsed( false );
					_schedConstObsComp.setMaxElevation( nf.format( elevation ) );
				}
			}
			else if( tbw == _w.period )
			{
				_schedConstObsComp.setPeriod( _w.period.getValue() );
			}
		}
		catch( Exception e )
		{
			DialogUtil.error( _w , e.getMessage() );
			e.printStackTrace();
		}
	}

	/**
	 * Text box action.
	 */
	public void textBoxAction( TextBoxWidgetExt tbw )
	{
		_updateWidgets();
	}

	/**
	 * Radio button action.
	 */
	public void actionPerformed( ActionEvent evt )
	{
		Object w = evt.getSource();

		if( w == _w.meridianApproachRising )
		{
			_schedConstObsComp.setMeridianApproach( SpSchedConstObsComp.SOURCE_RISING );
		}
		else if( w == _w.meridianApproachSetting )
		{
			_schedConstObsComp.setMeridianApproach( SpSchedConstObsComp.SOURCE_SETTING );
		}
		else if( w == _w.meridianApproachAny )
		{
			_schedConstObsComp.setMeridianApproach( null );
		}
		else if( w == _w.airmassCB )
		{
			_schedConstObsComp.setDisplayAirmass( _w.airmassCB.isSelected() );
			_updateWidgets();
		}
	}

	private double _elevationToAirmass( double elevation )
	{
		// If the elevation is < 5 degrees set the airmass to 12
		double airmass;
		if( elevation < 5. )
		{
			airmass = 12. ;
		}
		else
		{
			// Convert elevation to radians
			elevation *= ( Math.PI / 180. ) ;
			airmass = 1. / Math.sin( elevation );
		}
		return airmass;
	}

	private double _airmassToElevation( double airmass )
	{
		if( Math.abs( airmass ) < 1. )
			airmass = 1. ;
		double elevation = Math.asin( 1. / airmass );
		// Convert to degrees
		elevation = 180. * elevation / Math.PI;
		return elevation;
	}
}
