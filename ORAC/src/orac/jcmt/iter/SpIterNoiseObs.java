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

/**
 * Noise Iterator for JCMT (SCUBA).
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterNoiseObs extends SpIterJCMTObs
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "noiseObs" , "Noise" ) ;

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
		_avTable.noNotifySet( ATTR_NOISE_SOURCE , NOISE_SOURCES[ 0 ] , 0 ) ;
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
		for( int i = 0 ; i < NOISE_SOURCES.length ; i++ )
		{
			if( noiseSource.equals( NOISE_SOURCES[ i ] ) )
				_avTable.set( ATTR_NOISE_SOURCE , NOISE_SOURCES[ i ] ) ;
		}
	}

	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this ) ;
		double time = 0. ;
		if( instrument instanceof orac.jcmt.inst.SpInstSCUBA )
			time = 1.1 + SCUBA_STARTUP_TIME ;

		return time ;
	}

	public void setupForHeterodyne()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE ) ;
	}

	public void setupForSCUBA()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE ) ;
	}
}
