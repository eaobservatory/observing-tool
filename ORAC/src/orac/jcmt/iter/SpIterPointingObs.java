/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package orac.jcmt.iter ;

import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.inst.SpInstSCUBA2 ;
import orac.jcmt.util.Scuba2Time ;
import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.sp.SpTreeMan ;

import gemini.sp.obsComp.SpInstObsComp ;

/**
 * Pointing Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class SpIterPointingObs extends SpIterJCMTObsInBeamChoice
{
	/**
	 * Pointing pixel choices.
	 *
	 * To be decided.
	 */
	public static String[] POINTING_PIXEL_MANUAL_CHOICES = { "1" , "..." } ;
	public static String[] POINTING_METHODS = { "9 Position" , "16 Position" , "25 Position" } ;
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "pointingObs" , "Pointing" ) ;
	private Scuba2Time s2time = null ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterPointingObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterPointingObs()
	{
		super( SP_TYPE ) ;
		_avTable.noNotifySet( ATTR_AUTOMATIC_TARGET , "true" , 0 ) ;
	}

	public String getSpectralMode()
	{
		return _avTable.get( ATTR_SPECTRAL_MODE ) ;
	}

	public void setSpectralMode( String value )
	{
		_avTable.set( ATTR_SPECTRAL_MODE , value ) ;
	}

	public String getPointingPixel()
	{
		return _avTable.get( ATTR_POINTING_PIXEL ) ;
	}

	public void setPointingPixel( String value )
	{
		_avTable.set( ATTR_POINTING_PIXEL , value ) ;
	}

	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this ) ;
		double overhead = 0. ;
		double totalIntegrationTime = 0. ;

		if( instrument instanceof SpInstHeterodyne )
		{
			totalIntegrationTime = 120. ;
		}
		else if( instrument instanceof SpInstSCUBA2 )
		{
			totalIntegrationTime = SCUBA2_STARTUP_TIME ;

			if( s2time == null )
				s2time = new Scuba2Time() ;

			totalIntegrationTime = s2time.totalIntegrationTime( this ) ;
		}
		return( overhead + totalIntegrationTime ) ;
	}

	public void setupForSCUBA2()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE ) ;
		_avTable.noNotifyRm( ATTR_POINTING_METHOD ) ;
		_avTable.noNotifyRm( ATTR_SECS_PER_CYCLE ) ;
		_avTable.noNotifyRm( ATTR_SPECTRAL_MODE ) ;
	}

	public String[] getSwitchingModeOptions()
	{
		return new String[]{ SWITCHING_MODE_BEAM } ;
	}
}
