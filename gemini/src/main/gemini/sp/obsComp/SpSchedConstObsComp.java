/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package gemini.sp.obsComp ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;

/**
 * Component for OMP scheduling contraints.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class SpSchedConstObsComp extends SpObsComp
{
	/** This attribute records the earliest scheduling date. */
	public static final String ATTR_EARLIEST = "earliest" ;

	/** This attribute records the latest scheduling date. */
	public static final String ATTR_LATEST = "latest" ;

	/** This attribute records the minimum elevation. */
	public static final String ATTR_MIN_ELEVATION = "minEl" ;

	/** This attribute records the maximum elevation. */
	public static final String ATTR_MAX_ELEVATION = "maxEl" ;

	/**
     * This attribute records the meridian approach (rising/setting).
     * 
     * Set to {@link #SOURCE_RISING} or {@link #SOURCE_SETTING}.
     */
	public static final String ATTR_MERIDIAN_APPROACH = "meridianApproach" ;

	/** This attribute records the monitoring period */
	public static final String ATTR_PERIOD = "period" ;

	/** Attribute value for {@link #ATTR_MERIDIAN_APPROACH}. */
	public static final String SOURCE_RISING = "rising" ;

	/** Attribute value for {@link #ATTR_MERIDIAN_APPROACH}. */
	public static final String SOURCE_SETTING = "setting" ;

	public static final String NO_VALUE = "none" ;

	public static final String SUBTYPE = "schedConstraints" ;

	public static final SpType SP_TYPE = SpType.create( SpType.OBSERVATION_COMPONENT_TYPE , SUBTYPE , "Sched. Constraints" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpSchedConstObsComp() ) ;
	}

	/**
     * Default constructor. Initialize the component type.
     */
	public SpSchedConstObsComp()
	{
		super( SP_TYPE ) ;

		_avTable.noNotifySet( ATTR_EARLIEST , NO_VALUE , 0 ) ;
		_avTable.noNotifySet( ATTR_LATEST , NO_VALUE , 0 ) ;
	}

	/**
     * Get earliest scheduling date.
     */
	public String getEarliest()
	{
		String earliest = _avTable.get( ATTR_EARLIEST ) ;

		if( earliest == null )
			earliest = NO_VALUE ;

		return earliest ;
	}

	/**
     * Set earliest scheduling date.
     */
	public void setEarliest( String earliest )
	{
		_avTable.set( ATTR_EARLIEST , earliest ) ;
	}

	/**
     * Set earliest scheduling date without notifying state machine.
     * 
     * Useful for initialising item.
     */
	public void initEarliest( String earliest )
	{
		_avTable.noNotifySet( ATTR_EARLIEST , earliest , 0 ) ;
	}

	/**
     * Get latest scheduling date.
     */
	public String getLatest()
	{
		String latest = _avTable.get( ATTR_LATEST ) ;

		if( latest == null )
			latest = NO_VALUE ;

		return latest ;
	}

	/**
     * Set latest scheduling date.
     */
	public void setLatest( String latest )
	{
		_avTable.set( ATTR_LATEST , latest ) ;
	}

	/**
     * Set latest scheduling date without notifying state machine.
     * 
     * Useful for initialising item.
     * 
     */
	public void initLatest( String latest )
	{
		_avTable.noNotifySet( ATTR_LATEST , latest , 0 ) ;
	}

	/**
     * Get min elevation
     */
	public String getMinElevation()
	{
		return _avTable.get( ATTR_MIN_ELEVATION ) ;
	}

	/**
     * Set min elevation.
     */
	public void setMinElevation( double minEl )
	{
		_avTable.set( ATTR_MIN_ELEVATION , minEl ) ;
	}

	/**
     * Set min elevation.
     */
	public void setMinElevation( String minEl )
	{
		try
		{
			_avTable.set( ATTR_MIN_ELEVATION , Double.parseDouble( minEl.trim() ) ) ;
		}
		catch( NumberFormatException e )
		{
			_avTable.rm( ATTR_MIN_ELEVATION ) ;
		}
		catch( NullPointerException e )
		{
			_avTable.rm( ATTR_MIN_ELEVATION ) ;
		}
	}

	/**
     * Get max elevation
     */
	public String getMaxElevation()
	{
		return _avTable.get( ATTR_MAX_ELEVATION ) ;
	}

	/**
     * Set max elevation.
     */
	public void setMaxElevation( double maxEl )
	{
		_avTable.set( ATTR_MAX_ELEVATION , maxEl ) ;
	}

	/**
     * Set max elevation.
     */
	public void setMaxElevation( String maxEl )
	{
		try
		{
			_avTable.set( ATTR_MAX_ELEVATION , Double.parseDouble( maxEl.trim() ) ) ;
		}
		catch( NumberFormatException e )
		{
			_avTable.rm( ATTR_MAX_ELEVATION ) ;
		}
		catch( NullPointerException e )
		{
			_avTable.rm( ATTR_MAX_ELEVATION ) ;
		}
	}

	/**
     * Get meridian approach.
     */
	public String getMeridianApproach()
	{
		return _avTable.get( ATTR_MERIDIAN_APPROACH ) ;
	}

	/**
     * Set meridian approach.
     */
	public void setMeridianApproach( String meridianApproach )
	{
		if( meridianApproach == null )
			_avTable.rm( ATTR_MERIDIAN_APPROACH ) ;
		else
			_avTable.set( ATTR_MERIDIAN_APPROACH , meridianApproach ) ;
	}

	/**
     * Get resheculing period
     */
	public String getPeriod()
	{
		return _avTable.get( ATTR_PERIOD ) ;
	}

	/**
     * Set rescheduling period.
     */
	public void setPeriod( double period )
	{
		_avTable.set( ATTR_PERIOD , period ) ;
	}

	/**
     * Set rescheduling period form String.
     */
	public void setPeriod( String period )
	{
		try
		{
			_avTable.set( ATTR_PERIOD , Double.parseDouble( period.trim() ) ) ;
		}
		catch( NumberFormatException e )
		{
			_avTable.rm( ATTR_PERIOD ) ;
		}
		catch( NullPointerException e )
		{
			_avTable.rm( ATTR_PERIOD ) ;
		}
	}

	/** Set whether to display as airmass or elevation */
	public void setDisplayAirmass( boolean flag )
	{
		_avTable.set( ".display.airmass" , flag ) ;
	}

	/** Get whether to display as airmass or elevation */
	public boolean getDisplayAirmass()
	{
		return( _avTable.getBool( ".display.airmass" ) ) ;
	}
}
