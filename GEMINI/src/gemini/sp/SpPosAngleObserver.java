// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

/**
 * An interface that should be supported by clients wishing to be informed
 * of when a position angle changes.  SpPosAngleObservers are added to
 * SpObsData structures, which are always kept up-to-date with the current
 * position angle of the context with which they are associated.
 *
 * @see SpObsData
 */
public interface SpPosAngleObserver
{
   /**
    * Notification that the position angle has changed.
    *
    * @param posAngle The position angle in degrees.
    */
   public void posAngleUpdate(double posAngle);
}
