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

import orac.jcmt.inst.SpInstSCUBA2 ;
import orac.jcmt.util.Scuba2Time ;
import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.sp.SpTreeMan ;

import gemini.sp.obsComp.SpInstObsComp ;

import gemini.util.Format ;

/**
 * Cloned from SkyDip
 */
@SuppressWarnings( "serial" )
public class SpIterSetupObs extends SpIterJCMTObs
{
	public static String[] START_POSITIONS = { "Zenith" , "Horizon" , "Automatic" } ;
	private Scuba2Time s2time = null ;
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "setupObs" , "Setup" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterSetupObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterSetupObs()
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
