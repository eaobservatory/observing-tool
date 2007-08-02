// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import gemini.util.Assert;

/**
 * The observation link item.
 */
public class SpObsLink extends SpItem
{

	/**
     * Default constructor.
     */
	protected SpObsLink()
	{
		super( SpType.OBSERVATION_LINK );
	}

	/**
     * Construct with the observation that this link points to.
     */
	protected SpObsLink( SpObs spObs )
	{
		super( SpType.OBSERVATION_LINK );
		linkTo( spObs );
	}

	/**
     * Link to the given observation.
     */
	public void linkTo( SpObs spObs )
	{
		Assert.notFalse( spObs.hasBeenNamed() );

		// Put a copy of the observation as this link's only child
		SpItem spItem = spObs.deepCopy();
		spItem.name( spObs.name() );

		SpItem[] spItemA = { spItem };
		insert( spItemA , null );

		// Add a ".target" attribute
		_avTable.set( ".target" , spObs.name() );
	}

}
