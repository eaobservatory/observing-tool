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
 * The HROS instrument.
 */
public class SpInstHROS extends SpInstObsComp
{
	public static final SpType SP_TYPE = SpType.create( SpType.OBSERVATION_COMPONENT_TYPE , "inst.HROS" , "HROS" );

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpInstHROS() );
	}

	public SpInstHROS()
	{
		super( SP_TYPE );
	}

	/**
	 * Return the slit size.
	 */
	public double[] getScienceArea()
	{
		return new double[]{ 1. , 60. };
	}
}
