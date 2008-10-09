package orac.ukirt.validation ;

import gemini.sp.obsComp.SpInstObsComp ;
import orac.ukirt.inst.SpInstCGS4 ;
import orac.validation.InstrumentValidation ;
import orac.validation.ErrorMessage ;
import java.util.Vector ;

/**
 * Implements validation of CGS4.
 * 
 * @author M.Folger@roe.ac.uk UKATC
 */
public class CGS4Validation implements InstrumentValidation
{
	final double CENTRAL_WAVELENGTH_MIN = 0.88 ;
	final double CENTRAL_WAVELENGTH_MAX = 5.2 ;
	final double ORDER_FOR_40_OR_150_GRATING_MAX = 5 ;
	final double ORDER_FOR_ECHELLE_GRATING_MIN = 10 ;
	final double EXPOSURE_TIME_RECOMMENDED_MIN = 0.12 ;
	final double EXPOSURE_TIME_MIN = 0.012 ;
	private static Vector<String> _masks = new Vector<String>() ;
	private static Vector<String> _samplings = new Vector<String>() ;
	private static Vector<String> _posAngles = new Vector<String>() ;

	public void checkInstrument( SpInstObsComp instObsComp , Vector<ErrorMessage> report )
	{
		SpInstCGS4 spInstCGS4 = ( SpInstCGS4 )instObsComp ;
		double centralWavelength = spInstCGS4.getCentralWavelength() ;
		String grating = spInstCGS4.getDisperser() ;
		int order = spInstCGS4.getOrder() ;
		double expTime = spInstCGS4.getExpTime() ;
		boolean ndFilter = spInstCGS4.getNdFilter() ;

		if( report == null )
			report = new Vector<ErrorMessage>() ;

		// Checking central wavelength.
		if( ( centralWavelength < CENTRAL_WAVELENGTH_MIN ) || ( centralWavelength > CENTRAL_WAVELENGTH_MAX ) )
			report.add( new ErrorMessage( ErrorMessage.ERROR , "CGS4" , "central wavelength" , CENTRAL_WAVELENGTH_MIN + ".." + CENTRAL_WAVELENGTH_MAX , "" + centralWavelength ) ) ;

		// Checking grating.
		if( ( grating.equals( "40lpmm" ) || grating.equals( "150lpmm" ) ) && ( order > ORDER_FOR_40_OR_150_GRATING_MAX ) )
			report.add( new ErrorMessage( ErrorMessage.ERROR , "CGS4" , "order (with " + grating + " grating)" , "<= " + ORDER_FOR_40_OR_150_GRATING_MAX , "" + order ) ) ;
		else if( ( grating.equals( "echelle" ) ) && ( order < ORDER_FOR_ECHELLE_GRATING_MIN ) )
			report.add( new ErrorMessage( ErrorMessage.ERROR , "CGS4" , "order (with " + grating + " grating)" , ">= " + ORDER_FOR_ECHELLE_GRATING_MIN , "" + order ) ) ;

		// Checking eposure time.
		if( expTime < EXPOSURE_TIME_MIN )
			report.add( new ErrorMessage( ErrorMessage.ERROR , "CGS4" , "exposure time" , ">= " + EXPOSURE_TIME_MIN , "" + expTime ) ) ;
		else if( expTime < 0.12 )
			report.add( new ErrorMessage( ErrorMessage.WARNING , "CGS4" , "found exposure time " + expTime + ", subarray needs to be set by TSS." ) ) ;

		// Checking for invalid ND filter/grating combination.
		if( ndFilter && grating.equals( "echelle" ) )
			report.add( new ErrorMessage( ErrorMessage.ERROR , "CGS4" , "ND filter cannot be selected in combination with grating \"echelle\"" ) ) ;

		addMask( spInstCGS4.getMask() ) ;
		addSampling( spInstCGS4.getSampling() ) ;
		addPosAngle( spInstCGS4.getPosAngleDegreesStr() ) ;
	}

	public static void reset()
	{
		_masks = new Vector<String>() ;
		_samplings = new Vector<String>() ;
		_posAngles = new Vector<String>() ;
	}

	public static Vector<String> getMasks()
	{
		return _masks ;
	}

	public static Vector<String> getSamplings()
	{
		return _samplings ;
	}

	public static Vector<String> getPosAngles()
	{
		return _posAngles ;
	}

	/**
	 * Adds mask only if it is not an element of the _masks vector.
	 *
	 * @return false if the mask has not been added because it was in the _masks vector already
	 *         true if element has been added because it was not in the _masks vector yet.
	 */
	private static boolean addMask( String mask )
	{
		if( _masks.contains( mask ) )
		{
			return false ;
		}
		else
		{
			_masks.add( mask ) ;
			return true ;
		}
	}

	/**
	 * Adds sampling only if it is not an element of the _samplings vector.
	 *
	 * @return false if the sampling has not been added because it was in the _samplings vector already
	 *         true if element has been added because it was not in the _samplings vector yet.
	 */
	private static boolean addSampling( String sampling )
	{
		if( _samplings.contains( sampling ) )
		{
			return false ;
		}
		else
		{
			_samplings.add( sampling ) ;
			return true ;
		}
	}

	/**
	 * Adds posAngle only if it is not an element of the _posAngles vector.
	 *
	 * @return false if the posAngle has not been added because it was in the _posAngles vector already
	 *         true if element has been added because it was not in the _posAngles vector yet.
	 */
	private static boolean addPosAngle( String posAngle )
	{
		if( _posAngles.contains( posAngle ) )
		{
			return false ;
		}
		else
		{
			_posAngles.add( posAngle ) ;
			return true ;
		}
	}
}
