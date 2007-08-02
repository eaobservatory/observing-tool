// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

/**
 * An interface that should be supported by clients wishing to be informed of
 * when a base position changes. SpBasePosObservers are added to SpObsData
 * structures, which are always kept up-to-date with the current base position
 * of the context with which they are associated.
 * 
 * @see SpObsData
 */
public interface SpBasePosObserver
{

	// RA,Dec changed to x,y. coordSys added. (MFO, April 12, 2002)
	/**
     * Notification that the base position has changed. x and y are specified in
     * degrees.
     */
	public void basePosUpdate( double x , double y , double xoff , double yoff , int coordSys );
}
