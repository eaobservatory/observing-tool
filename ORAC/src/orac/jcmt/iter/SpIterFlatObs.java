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

/**
 * Focus Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterFlatObs extends SpIterJCMTObs
{

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "flatObs" , "Flat Field" );

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterFlatObs() );
	}

	/**
	 * Default constructor.
	 */
	public SpIterFlatObs()
	{
		super( SP_TYPE );
	}

	public double getElapsedTime()
	{
		return 0. ;
	}
}
