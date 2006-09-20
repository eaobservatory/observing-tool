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

/**
 * Pointing Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterPointingObs extends SpIterJCMTObs {

  /**
   * Pointing pixel choices.
   *
   * To be decided.
   */
  public static String [] POINTING_PIXEL_MANUAL_CHOICES = { "1", "..." };

  public static String [] POINTING_METHODS = { "9 Position", "16 Position", "25 Position" };

  public static final SpType SP_TYPE =
    SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "pointingObs", "Pointing");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterPointingObs());
  }

  /**
	 * Default constructor.
	 */
	public SpIterPointingObs()
	{
		super( SP_TYPE );
		_avTable.noNotifySet( ATTR_AUTOMATIC_TARGET , "true" , 0 );
	}

  public String getSpectralMode() {
    return _avTable.get(ATTR_SPECTRAL_MODE);
  }

  public void setSpectralMode(String value) {
    _avTable.set(ATTR_SPECTRAL_MODE, value);
  }

  public String getPointingPixel() {
    return _avTable.get(ATTR_POINTING_PIXEL);
  }

  public void setPointingPixel(String value) {
    _avTable.set(ATTR_POINTING_PIXEL, value);
  }

    public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this );
		double overhead = 0.0;
		double totalIntegrationTime = 0.0;

		if( instrument instanceof orac.jcmt.inst.SpInstSCUBA )
		{
			overhead = SCUBA_STARTUP_TIME + 9.0 ;
			totalIntegrationTime = 16.0 * 2.0 ;
		}
		else if( instrument instanceof orac.jcmt.inst.SpInstHeterodyne )
		{
			totalIntegrationTime = calculateTotalPlusOverheadForElapsedTime( 120.0 ) ;
		}
		return ( overhead + totalIntegrationTime );
	}

//  dynamically allocated variables
    double T_startend ;
    double T_bcal ;
    double T_cal ;
    double T_ocal ;

    public double calculateTotalPlusOverheadForElapsedTime( double integrationTime )
    {
    	double n_cals = Math.max( 1. , Math.floor( integrationTime / T_bcal ) ) ;
    	double T_total = T_startend + integrationTime + ( ( T_cal + T_ocal ) * n_cals ) ;
    	return T_total ;	
    }
    
	public void setupForHeterodyne(){}

	public void setupForSCUBA()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE );
		_avTable.noNotifyRm( ATTR_POINTING_METHOD );
		_avTable.noNotifyRm( ATTR_SECS_PER_CYCLE );
		_avTable.noNotifyRm( ATTR_SPECTRAL_MODE );
	}

    public String [] getSwitchingModeOptions() {
        return new String [] {
            SWITCHING_MODE_BEAM
        };
    }
}


