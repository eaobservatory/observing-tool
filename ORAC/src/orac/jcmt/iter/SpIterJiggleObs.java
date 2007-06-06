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
import orac.jcmt.inst.SpInstSCUBA;


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

		// Actual integration time on source
		double totalIntegrationTime = 0. ;
		if( instrument != null )
		{
			String jigglePattern = getJigglePattern() ;
			if( jigglePattern != null )
			{
				jigglePattern = jigglePattern.toLowerCase() ;
		
				int steps = 0 ;
				if( jigglePattern.matches( "\\d+x\\d+" ) )
				{
					String[] split = jigglePattern.split( "x" ) ;
					steps = Integer.parseInt( split[ 0 ] ) ;
				}
				else if( jigglePattern.matches( "harp\\d" ) )
				{
					String[] split = jigglePattern.split( "harp" ) ;
					steps = Integer.parseInt( split[ 1 ] ) ;					
				}
				// number of points
				int npts = 0 ;
		
				switch( steps )
				{
					// 3X3 jigle map
					case 3 :
						npts = 9 ;
						break;
					// 4x4 jiggle
					case 4 : 
						npts = 16 ;
						break ;
					// 5X5 jigle map
					case 5 :
						npts = 25 ;
						break;
					// 7X7 jigle map
					case 7 :
						npts = 49 ;
						break;
					// 9X9 jigle map
					case 9 :
						npts = 81 ;
						break;
					case 11 :
						npts = 121 ;
						break ;
					// default
					default :
						break ;
				}					
				totalIntegrationTime = 2.12 * ( npts * getSecsPerCycle() ) ;
				if( isContinuum() )
					totalIntegrationTime *= 1.7 ;
			}
		}
		return totalIntegrationTime ;
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

    public void setupForHeterodyne()
	{
		String value;

		// Scale Factor
		value = _avTable.get( ATTR_SCALE_FACTOR );
		if( value == null || value.equals( "" ) )
			_avTable.noNotifySet( ATTR_SCALE_FACTOR , JIGGLE_SCALE_DEFAULT , 0 );

		// Jiggles/Cyle
		value = _avTable.get( ATTR_JIGGLES_PER_CYCLE );
		if( value == null || value.equals( "" ) )
			_avTable.noNotifySet( ATTR_JIGGLES_PER_CYCLE , JIGGLES_PER_CYCLE_DEFAULT , 0 );

		// continuum mode
		value = _avTable.get( ATTR_CONTINUUM_MODE );
		if( value == null || value.equals( "" ) )
			_avTable.noNotifySet( ATTR_CONTINUUM_MODE , "false" , 0 );

		// Seconds per cycle
		value = _avTable.get( ATTR_SECS_PER_CYCLE );
		if( value == null || value.equals( "" ) )
			_avTable.noNotifySet( ATTR_SECS_PER_CYCLE , SECS_PER_CYCLE_DEFAULT , 0 );

		// Jiggle PA
		value = _avTable.get( ATTR_JIGGLE_PA );
		if( value == null || value.equals( "" ) )
			_avTable.noNotifySet( ATTR_JIGGLE_PA , "0.0" , 0 );

		super.setupForHeterodyne() ;
	}

    public void setupForSCUBA()
	{
		_avTable.noNotifyRm( ATTR_SCALE_FACTOR );
		_avTable.noNotifyRm( ATTR_JIGGLES_PER_CYCLE );
		_avTable.noNotifyRm( ATTR_CONTINUUM_MODE );
		_avTable.noNotifyRm( ATTR_SECS_PER_CYCLE );
		_avTable.noNotifyRm( ATTR_JIGGLE_PA );
		super.setupForSCUBA() ;
	}

    public void setupForSCUBA2()
	{
		_avTable.noNotifyRm( ATTR_SCALE_FACTOR );
		_avTable.noNotifyRm( ATTR_JIGGLES_PER_CYCLE );
		_avTable.noNotifyRm( ATTR_CONTINUUM_MODE );
		_avTable.noNotifyRm( ATTR_SECS_PER_CYCLE );
		_avTable.noNotifyRm( ATTR_JIGGLE_PA );
		super.setupForSCUBA2();
	}
    
    public String[] getSwitchingModeOptions()
	{
		return new String[]
		{ 
			SWITCHING_MODE_POSITION ,
			SWITCHING_MODE_BEAM , 
			SWITCHING_MODE_FREQUENCY_S , 
			SWITCHING_MODE_FREQUENCY_F 
		} ;
	}
    
    public void setSeperateOffs( boolean enable )
    {
    	_avTable.set( SEPERATE_OFFS , enable ) ;
    }
    
    public boolean hasSeperateOffs()
    {
    	return _avTable.getBool( SEPERATE_OFFS ) ;
    }
    
    public void rmSeperateOffs()
    {
    	_avTable.noNotifyRm( SEPERATE_OFFS ) ;
    }
    
}
