// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter;

/**
 * A data object that describes an attribute that may be iterated over.
 * Client code uses an IterConfigItem to present choices for iteration
 * to the user.
 */
public class IterConfigItem implements java.io.Serializable
{
   /**
    * The displayable title that is meaningful to a human user.
    */
   public String   title;  

   /**
    * The attribute name stored in the science program.  The convention
    * is to name the iterator attribute the same as the attribute being
    * iterated over, with "Iter" appended.  So "filter" becomes
    * "filterIter".
    */
   public String   attribute;

   /**
    * The list of choices that this attribute may take.  Numeric
    * attributes like exposureTime will have a null list.
    */
   public String[] choices;

/**
 * Constructor that should be used for numeric attributes.
 */
public IterConfigItem(String title, String attribute)
{
   this.title     = title;
   this.attribute = attribute;
}

/**
 * Constructor that should be used for attributes that have a finite
 * set of choices, for instance a filter setting.
 */
public IterConfigItem(String title, String attribute, String[] choices)
{
   this(title, attribute);
   this.choices   = choices;
}

}
