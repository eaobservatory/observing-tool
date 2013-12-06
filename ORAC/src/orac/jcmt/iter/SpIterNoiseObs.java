/*
 * Copyright 2002 United Kingdom Astronomy Technology Centre, an
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

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpInstObsComp ;
import orac.jcmt.inst.SpInstSCUBA2 ;
import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.util.Scuba2Time ;

/**
 * Noise Iterator for JCMT (SCUBA).
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class SpIterNoiseObs extends SpIterJCMTObs
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "noiseObs" , "Noise" ) ;
	private String[] NOISE_SOURCES = null ;
	private Scuba2Time s2time = null ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterNoiseObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterNoiseObs()
	{
		super( SP_TYPE ) ;
		
		_avTable.noNotifySet( ATTR_DO_AT_CURRENT_AZ , "true" , 0 ) ;
	}

	/** Get the noise source. */
	public String getNoiseSource()
	{
		return _avTable.get( ATTR_NOISE_SOURCE ) ;
	}

	/**
	 * Set noise source to one of NOISE_SOURCES.
	 *
	 * @param noiseSource if this is not one of the NOISE_SOURCES then it will be ignored.
	 *
	 * @see orac.jcmt.SpJCMTConstants#NOISE_SOURCES
	 */
	public void setNoiseSource( String noiseSource )
	{
		if( NOISE_SOURCES != null )
		{
			for( int i = 0 ; i < NOISE_SOURCES.length ; i++ )
			{
				if( NOISE_SOURCES[ i ].equals( noiseSource ) )
					_avTable.set( ATTR_NOISE_SOURCE , NOISE_SOURCES[ i ] ) ;
			}
		}
	}
	
	/** Get the selection of noise sources. */
	public String[] getNoiseSources()
	{
		if( NOISE_SOURCES == null )
		{
			SpInstObsComp instrument = SpTreeMan.findInstrument( this ) ;
			if( instrument instanceof SpInstSCUBA2 )
				NOISE_SOURCES = SCUBA2_NOISE_SOURCES ;
			else if( instrument instanceof SpInstHeterodyne )
				NOISE_SOURCES = HETERODYNE_NOISE_SOURCES ;
		}
		return NOISE_SOURCES ;
	}

	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this ) ;
		double time = 0. ;
		if( instrument instanceof SpInstSCUBA2 )
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
		checkSources( HETERODYNE_NOISE_SOURCES ) ;
	}

	public void setupForSCUBA2()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE ) ;
		checkSources( SCUBA2_NOISE_SOURCES ) ;
	}
	
	private void checkSources( String[] sources )
	{
		if( sources != null && NOISE_SOURCES != sources )
		{
			_avTable.noNotifyRm( ATTR_NOISE_SOURCE ) ;
			NOISE_SOURCES = sources ;
			_avTable.noNotifySet( ATTR_NOISE_SOURCE , NOISE_SOURCES[ 0 ] , 0 ) ;
		}
	}
}
