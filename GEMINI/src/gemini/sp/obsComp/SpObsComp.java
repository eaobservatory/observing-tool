// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.obsComp;
 
import gemini.sp.SpItem;
import gemini.sp.SpType;


/**
 * The base class for observation component items.
 */
public class SpObsComp extends SpItem
{

/**
 * Construct with a subtype.
 */
public SpObsComp(SpType spType)
{
   super(spType);

   // Most observation components must be unique in their scope.
   // If this isn't the case, set ".unique" to false in the subclass
   // constructor.
   _avTable.noNotifySet(".unique", "true", 0);
}

/**
 * Override getTitle to return the subtype instead of the type.
 */
public String
getTitle()
{
   String title     = type().getReadable();
   String titleAttr = getTitleAttr();
   if ((titleAttr != null) && !(titleAttr.equals(""))) {
      title = title + ": " + titleAttr;
   }
   return title;
}

/**
 * Does this observation component have to be unique in its scope?
 * Most components must be unique within their scope to avoid ambiguity.
 * For instance, it would not do to have two instruments defined in the
 * same scope.
 */
public boolean
mustBeUnique()
{
   return _avTable.getBool(".unique");
}

/**
 * Set the "uniqueness" property.
 * @see #mustBeUnique
 */
public void
setMustBeUnique(boolean unique)
{
   _avTable.set(".unique", true);
}

}

