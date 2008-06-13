// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp ;

/**
 * The observation group item. Observation groups are a lot like folders (but
 * may not contain folders or other groups). They are used to indicate to the
 * scheduler that any contained observations are to be scheduled as a block,
 * without intervening observations.
 */
public class SpObsGroup extends SpObsContextItem
{
	/**
     * Default constructor.
     */
	protected SpObsGroup()
	{
		super( SpType.OBSERVATION_GROUP ) ;
	}
}
