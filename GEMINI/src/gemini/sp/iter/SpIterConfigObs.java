// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter;

import gemini.sp.SpType;
import gemini.sp.obsComp.SpInstConstants;
import java.util.Vector;

/**
 * A configuration iterator base class for iterators that include exposure time
 * and coadd attributes.
 */
public abstract class SpIterConfigObs extends SpIterConfigBase
{

	/**
     * Default constructor.
     */
	public SpIterConfigObs( SpType spType )
	{
		super( spType );
	}

	/**
     * Get the exposure time attribute.
     */
	protected IterConfigItem getExposureTimeConfigItem()
	{
		return new IterConfigItem( "Exp. Time" , SpInstConstants.ATTR_EXPOSURE_TIME + "Iter" );
	}

	/**
     * Get the exposure time attribute.
     */
	protected IterConfigItem getCoaddsConfigItem()
	{
		return new IterConfigItem( "Coadds" , SpInstConstants.ATTR_COADDS + "Iter" , null );
	}

	/**
     * Get the exposure times being iterated over (if any). Null is returned if
     * the exposure times are not being iterated over.
     */
	public Vector getExposureTimes()
	{
		return _avTable.getAll( SpInstConstants.ATTR_EXPOSURE_TIME + "Iter" );
	}

	/**
     * Get the coadds being iterated over (if any). Null is returned if the
     * coadds are not being iterated over.
     */
	public Vector getCoadds()
	{
		return _avTable.getAll( SpInstConstants.ATTR_COADDS + "Iter" );
	}

}
