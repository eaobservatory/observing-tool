// $Id$

package orac.jcmt.iter ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;

@SuppressWarnings( "serial" )
public class SpIterDREAMObs extends SpIterJCMTObs
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "dreamObs" , "DREAM" ) ;

	static
	{
		SpFactory.registerPrototype( new SpIterDREAMObs() ) ;
	}

	public SpIterDREAMObs()
	{
		super( SP_TYPE ) ;
	}

	public double getElapsedTime()
	{
		return 0. ;
	}
}
