// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.util;

/**
 * An interface used by clients interested in knowing about property
 * changes.
 */
public interface AppPropertyWatcher
{
   /** The given property has the given new value. */
   public void propertyChange(String property, String value);
}
