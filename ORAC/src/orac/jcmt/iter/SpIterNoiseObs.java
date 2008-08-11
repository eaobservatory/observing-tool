/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// $Id$
package orac.jcmt.iter ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpInstObsComp ;
import orac.jcmt.inst.SpInstSCUBA ;
import orac.jcmt.inst.SpInstSCUBA2 ;
import orac.jcmt.inst.SpInstHeterodyne ;

/**
 * Noise Iterator for JCMT (SCUBA).
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterNoiseObs extends SpIterJCMTObs
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "noiseObs" , "Noise" ) ;
	private String[] NOISE_SOURCES = null ;

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
		if( instrument instanceof SpInstSCUBA )
			time = 1.1 + SCUBA_STARTUP_TIME ;

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
	
	public void setupForSCUBA()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE ) ;
		_avTable.noNotifyRm( ATTR_NOISE_SOURCE ) ;
		NOISE_SOURCES = null ;
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
