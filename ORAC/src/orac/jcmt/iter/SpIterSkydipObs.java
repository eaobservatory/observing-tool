/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package orac.jcmt.iter;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;

import gemini.sp.obsComp.SpInstObsComp;

import gemini.util.Format;

/**
 * Skydip Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterSkydipObs extends SpIterJCMTObs
{

	public static String[] START_POSITIONS = { "Zenith" , "Horizon" , "Automatic" };

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "skydipObs" , "Skydip" );

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterSkydipObs() );
	}

	/**
	 * Default constructor.
	 */
	public SpIterSkydipObs()
	{
		super( SP_TYPE );

		_avTable.noNotifySet( ATTR_POSITIONS , "1" , 0 );
		_avTable.noNotifySet( ATTR_START_POSITION , START_POSITIONS[ 0 ] , 0 );
		_avTable.noNotifySet( ATTR_DO_AT_CURRENT_AZ , "false" , 0 );
	}

	public int getPositions()
	{
		return _avTable.getInt( ATTR_POSITIONS , 0 );
	}

	public void setPositions( int positions )
	{
		_avTable.set( ATTR_POSITIONS , positions );
	}

	public void setPositions( String positionsStr )
	{
		_avTable.set( ATTR_POSITIONS , Format.toInt( positionsStr ) );
	}

	public String getStartPosition()
	{
		return _avTable.get( ATTR_START_POSITION , 0 );
	}

	public void setStartPosition( String startPositionStr )
	{
		_avTable.set( ATTR_START_POSITION , startPositionStr );
	}

	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this );
		double time = 0. ;
		if( instrument instanceof orac.jcmt.inst.SpInstSCUBA )
			time = 227. + SCUBA_STARTUP_TIME;

		return time;
	}

	public void setupForHeterodyne()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE );
		_avTable.noNotifySet( ATTR_POSITIONS , "0" , 0 );
		_avTable.noNotifySet( ATTR_START_POSITION , START_POSITIONS[ 0 ] , 0 );
	}

	public void setupForSCUBA()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE );
		_avTable.noNotifyRm( ATTR_POSITIONS );
		_avTable.noNotifyRm( ATTR_START_POSITION );
	}

	// mimicks SCUBA
	public void setupForSCUBA2()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE );
		_avTable.noNotifyRm( ATTR_POSITIONS );
		_avTable.noNotifyRm( ATTR_START_POSITION );
	}
}
