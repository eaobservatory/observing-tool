package orac.ukirt.validation;

import gemini.sp.obsComp.SpInstObsComp;
import orac.ukirt.inst.SpInstUFTI;
import orac.validation.InstrumentValidation;
import orac.validation.ErrorMessage;
import java.util.Vector;

/**
 * Implements the validation of UFTI.
 * 
 * @author M.Folger@roe.ac.uk
 */
public class UFTIValidation implements InstrumentValidation
{

	/**
	 * readoutAreas, acqModes and minExpTimes are used as follows:
	 * Given readoutAreas[i] and acqModes[i] check whether the exposure time is at least minExpTimes[i].
	 *
	 * @see acqModes
	 * @see minExpTimes
	 */
	static String[] readoutAreas = 
	{ 
		"1024x1024" , 
		"1024x1024" , 
		"1024x1024" , 
		"512x512" , 
		"512x512" , 
		"512x512" , 
		"256x256" , 
		"256x256" , 
		"256x256" 
	};

	/**
	 * @see readoutAreas
	 */
	static String[] acqModes = 
	{ 
		"Normal+NDSTARE" , 
		"Normal+10_NDSTARE" , 
		"Fast+NDSTARE" , 
		"Normal+NDSTARE" , 
		"Normal+10_NDSTARE" , 
		"Fast+NDSTARE" , 
		"Normal+NDSTARE" , 
		"Normal+10_NDSTARE" , 
		"Fast+NDSTARE" 
	};

	/**
	 * @see readoutAreas
	 */
	static double[] minExpTimes = { 4 , 20 , 2 , 1 , 5 , 1.3 , 0.3 , 1.3 , 0.15 };

	public void checkInstrument( SpInstObsComp instObsComp , Vector report )
	{
		SpInstUFTI spInstUFTI = ( SpInstUFTI )instObsComp;
		String readoutArea = spInstUFTI.getReadoutArea();
		String acqMode = spInstUFTI.getAcqMode();
		double expTime = spInstUFTI.getExpTime();

		if( report == null )
			report = new Vector();

		double[] expLimits = spInstUFTI.getExpTimeLimits();
		if( expTime < expLimits[ 0 ] || expTime > expLimits[ 1 ] )
		{
			String message = "Exposure time of (" + expTime + ") out of limits for current combination " + "of readout area (" + readoutArea + ") and mode (" + acqMode + ").  Valid limits are " + "(" + expLimits[ 0 ] + ", " + expLimits[ 1 ] + ").";
			report.add( new ErrorMessage( ErrorMessage.ERROR , "UFTI" , message ) );
		}
	}
}
