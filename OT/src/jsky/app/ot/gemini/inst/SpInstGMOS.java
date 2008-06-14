// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.inst ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;

import gemini.sp.obsComp.SpInstObsComp ;

/**
 * The GMOS instrument.  GMOS is an imager and a spectrograph.  We are
 * only concerned with the imager aspect.  Slitlets have to be cut
 * based upon the image obtained and can't really be specified in the
 * Science Program.
 */
public final class SpInstGMOS extends SpInstObsComp
{
	public static final SpType SP_TYPE = SpType.create( SpType.OBSERVATION_COMPONENT_TYPE , "inst.GMOS" , "GMOS" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpInstGMOS() ) ;
	}

	public SpInstGMOS()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Return the detector size.
	 */
	public double[] getScienceArea()
	{
		return new double[]{ 422. , 332. } ;
	}
}
