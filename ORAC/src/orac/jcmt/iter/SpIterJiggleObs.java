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

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.sp.obsComp.SpInstConstants;
import gemini.sp.obsComp.SpInstObsComp;

import gemini.util.Format;
import orac.jcmt.inst.SpInstSCUBA;

import java.util.StringTokenizer;


/**
 * Jiggle Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterJiggleObs extends SpIterJCMTObs {

  private final String JIGGLE_SCALE_DEFAULT = "10.0";
  private final String JIGGLES_PER_CYCLE_DEFAULT = "1";
  private final String SECS_PER_CYCLE_DEFAULT = "10";


  public static final SpType SP_TYPE =
    SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "jiggleObs", "Jiggle");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterJiggleObs());
  }


  /**
   * Default constructor.
   */
  public SpIterJiggleObs() {
    super(SP_TYPE);

    _avTable.noNotifySet(ATTR_JIGGLE_PA,     "0.0",             0);
    _avTable.noNotifySet(ATTR_JIGGLE_SYSTEM, JIGGLE_SYSTEMS[0], 0);
  }

	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this );

		if( instrument == null )
			return 0.0;

		if( instrument instanceof SpInstSCUBA )
		{
			String jigglePattern = getJigglePattern();
			int steps = 0;
			double overhead = 0.0;

			// Actual integration time on source
			double totalIntegrationTime = 0.0;

			if( ( jigglePattern != null ) && ( jigglePattern.toLowerCase().indexOf( 'x' ) > -1 ) )
			{
				StringTokenizer st = new StringTokenizer( jigglePattern.toLowerCase() , "x " );
				steps = Integer.parseInt( st.nextToken() );
			}

			// Calculate overheads
			switch( steps )
			{
				// 3X3 jigle map
				case 3 :
					overhead = 9;
					break;

				// 5X5 jigle map
				case 5 :
					overhead = 18;
					break;

				// 7X7 jigle map
				case 7 :
					overhead = 27;
					break;

				// 9X9 jigle map
				case 9 :
					overhead = 36;
					break;

				// JIG16 (LONG or SHORT) or JIG64 (LONG and SHORT)
				default :
					if( isJIG64( ( SpInstSCUBA ) instrument ) )
					{
						overhead = 36;
					}
					else
					{
						overhead = 9;
					}
			}

			// Add SCUBA startup time.
			overhead += SCUBA_STARTUP_TIME;

			// Calculate total integration time
			if( steps > 0 )
			{
				totalIntegrationTime = steps * steps * 2;
			}
			else
			{
				if( isJIG64( ( SpInstSCUBA ) instrument ) )
				{
					totalIntegrationTime = 64 * 2;
				}
				else
				{
					totalIntegrationTime = 16 * 2;
				}
			}

			return totalIntegrationTime + overhead;
		}

		return 0.0;
	}

  public String getJigglePattern() {
    return _avTable.get(ATTR_JIGGLE_PATTERN);
  }

  public void setJigglePattern(String value) {
    _avTable.set(ATTR_JIGGLE_PATTERN, value);
  }


  /**
   * Get area position angle (map position angle).
   */
  public double getPosAngle() {
    return _avTable.getDouble(ATTR_JIGGLE_PA, 0.0);
  }

  /**
   * Set area postition angle (map postition angle).
   */
  public void setPosAngle(double theta) {
    _avTable.set(ATTR_JIGGLE_PA, Math.rint(theta * 10.0) / 10.0);
  }

  /**
   * Set area position angle (map position angle).
   */
  public void setPosAngle(String thetaStr) {
    setPosAngle(Format.toDouble(thetaStr));
  }

  /**
   * Get map coordinate system.
   */
  public String getCoordSys() {
    return _avTable.get(ATTR_JIGGLE_SYSTEM);
  }

  /**
   * Set map coordinate system.
   */
  public void setCoordSys(String coordSys) {
    _avTable.set(ATTR_JIGGLE_SYSTEM, coordSys);
  }

  public static boolean isJIG64(SpInstSCUBA instSCUBA) {
    return instSCUBA.getBolometers() != null &&
           instSCUBA.getBolometers().contains("LONG") &&
           instSCUBA.getBolometers().contains("SHORT");
  }

  public void setScaleFactor (double value) {
      _avTable.set( ATTR_SCALE_FACTOR, value );
  }

  private void setScaleFactor (String value) {
      double dVal = 1.0;
      try {
	  dVal = Double.parseDouble( value );
      }
      catch (NumberFormatException nfe) {};

      setScaleFactor(dVal);
  }

  public double getScaleFactor () {
      return _avTable.getDouble( ATTR_SCALE_FACTOR, 1.0 );
  }

  public void setJigglePA (double value) {
      _avTable.set( ATTR_JIGGLE_PA, value );
  }

  public double getJigglePA () {
      return _avTable.getDouble( ATTR_JIGGLE_PA, 0.0 );
  }

  public void setAcsisDefaults() {
      setContinuumMode(false);
      setScaleFactor( JIGGLE_SCALE_DEFAULT );
      setJigglesPerCycle( JIGGLES_PER_CYCLE_DEFAULT );
  }

    public void setupForHeterodyne() {
	String value;

	// Scale Factor
	value = _avTable.get( ATTR_SCALE_FACTOR );
	if ( value == null || value.equals("") ) {
	    _avTable.noNotifySet( ATTR_SCALE_FACTOR, JIGGLE_SCALE_DEFAULT, 0);
	}

	// Jiggles/Cyle
	value = _avTable.get( ATTR_JIGGLES_PER_CYCLE );
	if ( value == null || value.equals("") ) {
	    _avTable.noNotifySet ( ATTR_JIGGLES_PER_CYCLE, JIGGLES_PER_CYCLE_DEFAULT, 0 );
	}

        // continuum mode
	value = _avTable.get( ATTR_CONTINUUM_MODE );
	if ( value ==  null || value.equals("") ) {
	    _avTable.noNotifySet(ATTR_CONTINUUM_MODE, "false", 0);
	}

	// Seconds per cycle
	value = _avTable.get( ATTR_SECS_PER_CYCLE );
	if ( value == null || value.equals("") ) {
	     _avTable.noNotifySet(ATTR_SECS_PER_CYCLE, SECS_PER_CYCLE_DEFAULT, 0);
	}

	// Jiggle PA
	value = _avTable.get( ATTR_JIGGLE_PA );
	if ( value == null || value.equals("") ) {
	     _avTable.noNotifySet(ATTR_JIGGLE_PA, "0.0", 0);
	}

    }

    public void setupForSCUBA() {
	_avTable.noNotifyRm ( ATTR_SCALE_FACTOR );
	_avTable.noNotifyRm ( ATTR_JIGGLES_PER_CYCLE );
	_avTable.noNotifyRm ( ATTR_CONTINUUM_MODE );
	_avTable.noNotifyRm ( ATTR_SECS_PER_CYCLE );
	_avTable.noNotifyRm ( ATTR_JIGGLE_PA );
	
    }

    public String [] getSwitchingModeOptions() {
        return new String [] {
            SWITCHING_MODE_BEAM,
            //SWITCHING_MODE_NOD,       // Not for day 1 version.
            SWITCHING_MODE_FREQUENCY_S,
            SWITCHING_MODE_FREQUENCY_F
            //SWITCHING_MODE_NONE       // Not for day 1 version.
        };
    }
}
