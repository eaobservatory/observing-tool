// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.inst;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpInstObsComp;

/**
 * The MIRI instrument.
 */
public final class SpInstMIRI extends SpInstObsComp
{
	public static final SpType SP_TYPE = SpType.create( SpType.OBSERVATION_COMPONENT_TYPE , "inst.MIRI" , "MIRI" );

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpInstMIRI() );
	}

	public SpInstMIRI()
	{
		super( SP_TYPE );
	}

	/**
	 * Return the science area.
	 */
	public double[] getScienceArea()
	{
		return new double[] { 22. , 29. };
	}
}
