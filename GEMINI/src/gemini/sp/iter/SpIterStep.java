// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter;

import java.util.Vector;

/**
 * A "data" holder that describes a single iteration step of an iterator.
 * An iteration step can contain more than one value.  For instance, when
 * iterating over a configuration, a single step could change both the
 * filter and the exposure time.  In this case, the SpIterStep would
 * contain 2 SpIterValues in its <tt>values</tt> array.
 *
 * @see SpIterEnumeration
 */
public class SpIterStep implements java.io.Serializable
{
   /** The title of the iteration step. */
   public String title;

   /** The iteration step number this step represents. */
   public int stepCount;

   /**
    * The SpIterComp from which this step comes. Each step is associated
    * with a single SpIterComp.
    */
   public SpIterComp item;

   /** Attribute/value set from this step. */
   public SpIterValue[] values;

private SpIterStep(String title, int stepCount, SpIterComp item)
{
   this.title     = title;
   this.stepCount = stepCount;
   this.item      = item;
}

public
SpIterStep(String title, int stepCount, SpIterComp item, SpIterValue[] values)
{
   this(title, stepCount, item);
   this.values = values;
}

public
SpIterStep(String title, int stepCount, SpIterComp item, SpIterValue value)
{
   this(title, stepCount, item);
   if (value != null) {
      this.values    = new SpIterValue[1];
      this.values[0] = value;
   } else {
      this.values    = new SpIterValue[0];
   }
}

}

