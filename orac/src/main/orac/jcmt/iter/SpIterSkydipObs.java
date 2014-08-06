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

package orac.jcmt.iter ;

import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.inst.SpInstSCUBA2 ;
import orac.jcmt.util.Scuba2Time ;
import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.sp.SpTreeMan ;

import gemini.sp.obsComp.SpInstObsComp ;

import gemini.util.Format ;

/**
 * Skydip Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class SpIterSkydipObs extends SpIterJCMTObs
{
	public static String[] START_POSITIONS = { "Zenith" , "Horizon" , "Automatic" } ;
	private Scuba2Time s2time = null ;
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "skydipObs" , "Skydip" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterSkydipObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterSkydipObs()
	{
		super( SP_TYPE ) ;

		_avTable.noNotifySet( ATTR_DO_AT_CURRENT_AZ , "false" , 0 ) ;
	}

	public int getPositions()
	{
		return _avTable.getInt( ATTR_POSITIONS , 0 ) ;
	}

	public void setPositions( int positions )
	{
		_avTable.set( ATTR_POSITIONS , positions ) ;
	}

	public void setPositions( String positionsStr )
	{
		_avTable.set( ATTR_POSITIONS , Format.toInt( positionsStr ) ) ;
	}

	public String getStartPosition()
	{
		return _avTable.get( ATTR_START_POSITION , 0 ) ;
	}

	public void setStartPosition( String startPositionStr )
	{
		_avTable.set( ATTR_START_POSITION , startPositionStr ) ;
	}

	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this ) ;
		double time = 0. ;
		if( instrument instanceof SpInstHeterodyne )
		{
			time = 295. ;
		}
		else if( instrument instanceof SpInstSCUBA2 )
		{
			time = SCUBA2_STARTUP_TIME ;

			if( s2time == null )
				s2time = new Scuba2Time() ;

			time = s2time.totalIntegrationTime( this ) ;

			return time ;
		}

		return time ;
	}

	public void setupForHeterodyne()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE ) ;
		_avTable.noNotifyRm( ATTR_POSITIONS ) ;
		_avTable.noNotifyRm( ATTR_START_POSITION ) ;
	}

	// mimicks SCUBA
	public void setupForSCUBA2()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE ) ;
		_avTable.noNotifyRm( ATTR_POSITIONS ) ;
		_avTable.noNotifyRm( ATTR_START_POSITION ) ;
	}
	
	public String[] getSwitchingModeOptions()
	{
		return new String[]{} ;
	}
}
