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

import gemini.util.Format ;

/**
 * Focus Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class SpIterFocusObs extends SpIterJCMTObs
{
	public static String[] AXES = { "x" , "y" , "z" } ;
	public static final String FTS2_IN_BEAM = "InBeamFTS2";
	public static final String POL2_IN_BEAM = "InBeamPol2";

	private Scuba2Time s2time = null ;

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "focusObs" , "Focus" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterFocusObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterFocusObs()
	{
		super( SP_TYPE ) ;

		_avTable.noNotifySet( ATTR_AXIS , AXES[ 2 ] , 0 ) ;
		_avTable.noNotifySet( ATTR_STEPS , "" + getDefaultSteps( AXES[ 2 ] ) , 0 ) ;
		_avTable.noNotifySet( ATTR_FOCUS_POINTS , "5" , 0 ) ;
		_avTable.noNotifySet( ATTR_AUTOMATIC_TARGET , "true" , 0 ) ;
	}

	public String getAxis()
	{
		return _avTable.get( ATTR_AXIS ) ;
	}

	public void setAxis( String axis )
	{
		_avTable.set( ATTR_AXIS , axis ) ;
	}

	public double getSteps()
	{
		return _avTable.getDouble( ATTR_STEPS , 0. ) ;
	}

	public void setSteps( double steps )
	{
		_avTable.set( ATTR_STEPS , steps ) ;
	}

	public void setSteps( String stepsStr )
	{
		_avTable.set( ATTR_STEPS , Format.toDouble( stepsStr ) ) ;
	}

	public int getFocusPoints()
	{
		return _avTable.getInt( ATTR_FOCUS_POINTS , 0 ) ;
	}

	public void setFocusPoints( int focusPoints )
	{
		_avTable.set( ATTR_FOCUS_POINTS , focusPoints ) ;
	}

	public void setFocusPoints( String focusPointsStr )
	{
		_avTable.set( ATTR_FOCUS_POINTS , Format.toInt( focusPointsStr ) ) ;
	}

	public static double getDefaultSteps( String axis )
	{
		if( axis.equalsIgnoreCase( "z" ) )
			return 0.3 ;
		else
			return 1. ;
	}

	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this ) ;
		double time = 0. ;
		if( instrument instanceof SpInstHeterodyne )
		{
			time = 150. ;
		}
		else if( instrument instanceof SpInstSCUBA2 )
		{
			time = SCUBA2_STARTUP_TIME ;

			if( s2time == null )
				s2time = new Scuba2Time() ;

			time = s2time.totalIntegrationTime( this ) ;
		}
		return time ;
	}

	public void setupForSCUBA()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE ) ;
		_avTable.noNotifyRm( ATTR_SECS_PER_CYCLE ) ;
	}

	public void setupForSCUBA2()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE ) ;
		_avTable.noNotifyRm( ATTR_SECS_PER_CYCLE ) ;
	}

	public String[] getSwitchingModeOptions()
	{
		return new String[] { SWITCHING_MODE_BEAM } ;
	}

        public boolean isPol2InBeam() {
                if (! _avTable.exists(POL2_IN_BEAM)) {
                        _avTable.set(POL2_IN_BEAM, false);
                }
                return _avTable.getBool(POL2_IN_BEAM);
        }

        public boolean isFts2InBeam() {
                if (! _avTable.exists(FTS2_IN_BEAM)) {
                        _avTable.set(FTS2_IN_BEAM, false);
                }
                return _avTable.getBool(FTS2_IN_BEAM);

        }

        public void setPol2InBeam(boolean in_beam) {
                _avTable.set(POL2_IN_BEAM, in_beam);
        }

        public void setFts2InBeam(boolean in_beam) {
                _avTable.set(FTS2_IN_BEAM, in_beam);
        }
}
