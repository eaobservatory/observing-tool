// $Id$

package orac.jcmt.inst ;

import gemini.sp.SpFactory;
import gemini.sp.SpType ;

public class SpInstSCUBA2 extends SpJCMTInstObsComp
{
	
	public static final SpType SP_TYPE = SpType.create( SpType.OBSERVATION_COMPONENT_TYPE , "inst.SCUBA2" , "SCUBA-2" ) ;

	//	Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpInstSCUBA2() ) ;
	}
	
	public static String[] JIGGLE_PATTERNS = { "DREAM" } ;

	/**
	 * Constructor. Sets default values for attributes.
	 */
	public SpInstSCUBA2()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Get jiggle pattern options for this instrument, given the current settings.
	 *
	 * @return String array of jiggle pattern options.
	 */
	public String[] getJigglePatterns()
	{
		return JIGGLE_PATTERNS ;
	}

	/**
	 * Returns instrument specific default value for scan dx.
	 */
	public double getDefaultScanVelocity()
	{
		return 600. ;
	}

	/**
	 * Returns instrument specific default value for scan dx.
	 */
	public double getDefaultScanDy()
	{
		return 240. ;
	}
}
