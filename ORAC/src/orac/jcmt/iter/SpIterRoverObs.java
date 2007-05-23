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

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;

import gemini.sp.obsComp.SpInstObsComp;

import gemini.util.Format;


/**
 * Rover Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterRoverObs extends SpIterJCMTObs
{
   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "roverObs", "Rover");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterRoverObs());
}


/**
 * Default constructor.
 */
public SpIterRoverObs()
{
   super(SP_TYPE);

   _avTable.noNotifySet(ATTR_SWITCHING_MODE, getSwitchingModeOptions()[0], 0);
   _avTable.noNotifySet(ATTR_SAMPLES_PER_REVOLUTION, "64", 0);
   _avTable.noNotifySet(ATTR_SAMPLE_TIME, "0.05", 0);
}

    public void setSamplesPerRevolution(String samplesPerRevolution) {
        _avTable.set(ATTR_SAMPLES_PER_REVOLUTION, Format.toInt(samplesPerRevolution));
    }

    public int getSamplesPerRevolution() {
        return _avTable.getInt(ATTR_SAMPLES_PER_REVOLUTION, 1);
    }


    /**
     * This is the equivalent of getSampleTime() in the super class.
     *
     * It uses the same ATTR_SAMPLE_TIME and hence results in the same
     * XML output. But it is a double value.
     */
    public double getRoverSampleTime() {
      return _avTable.getDouble(ATTR_SAMPLE_TIME, 0.0);
    }

    /**
     * This is the equivalent of setSampleTime() in the super class.
     *
     * It uses the same ATTR_SAMPLE_TIME and hence results in the same
     * XML output. But it is a double value.
     */
    public void setRoverSampleTime(String value) {
      _avTable.set(ATTR_SAMPLE_TIME, value);
    }


    public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this );
		double overhead = 0.0;
		double totalIntegrationTime = 0.0;

		if( instrument instanceof orac.jcmt.inst.SpInstSCUBA )
		{
			// Go through all of this items parents to see if any are SpIterPOLs
			boolean polPhot = false;
			SpItem parent = parent();
			while( parent != null )
			{
				if( parent instanceof SpIterPOL )
				{
					polPhot = true;
					break;
				}
				parent = parent.parent();
			}
			overhead = SCUBA_STARTUP_TIME + 8 ;
			if( polPhot )
			{
				// 8 seconds per integration
				totalIntegrationTime = 8 ;
			}
		}
		else if( instrument instanceof orac.jcmt.inst.SpInstHeterodyne )
		{
			double overheadFactor = 1.2;
			totalIntegrationTime = getSecsPerCycle() * overheadFactor;
		}
		return ( overhead + totalIntegrationTime );
	}

	public void setupForHeterodyne()
	{
		if( _avTable.get( ATTR_SWITCHING_MODE ) == null || _avTable.get( ATTR_SWITCHING_MODE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SWITCHING_MODE , "Beam" , 0 );
		if( _avTable.get( ATTR_SECS_PER_CYCLE ) == null || _avTable.get( ATTR_SECS_PER_CYCLE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SECS_PER_CYCLE , "60" , 0 );
		if( _avTable.get( ATTR_CONT_CAL ) == null || _avTable.get( ATTR_CONT_CAL ).equals( "" ) )
			_avTable.set( ATTR_CONT_CAL , true );
	}

	public void setupForSCUBA()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE );
		_avTable.noNotifyRm( ATTR_SECS_PER_CYCLE );
		_avTable.noNotifyRm( ATTR_CONT_CAL );
	}

	public void setupForSCUBA2()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE );
		_avTable.noNotifyRm( ATTR_SECS_PER_CYCLE );
		_avTable.noNotifyRm( ATTR_CONT_CAL );
	}
	
    public String[] getSwitchingModeOptions()
	{
		return new String[]{ SWITCHING_MODE_BEAM , SWITCHING_MODE_POSITION };
	}
}


