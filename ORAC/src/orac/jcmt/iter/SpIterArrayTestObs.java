package orac.jcmt.iter ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;

public class SpIterArrayTestObs extends SpIterJCMTObs
{

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "arrayTestObs" , "Array Test" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterArrayTestObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterArrayTestObs()
	{
		super( SP_TYPE ) ;
	}

	public double getElapsedTime()
	{
		return 0. ;
	}
}
