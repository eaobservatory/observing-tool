// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

/**
 * The Note item.  Notes are arbitrary text information that may be
 * entered at any level of the hierarchy.
 */
public class SpNote extends SpItem
{
   public static final String ATTR_NOTE  = "note";

/**
 * Default constructor.
 */
public SpNote()
{
   super(SpType.NOTE);

   _avTable.noNotifySet(ATTR_NOTE, "", 0);
}

/**
 * Override getTitle to return the title attribute.
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
 * Set the note text.
 */
public void
setNote(String text)
{
   _avTable.set(ATTR_NOTE, text);
}

/**
 * Get the note text.
 */
public String
getNote()
{
   return _avTable.get(ATTR_NOTE);
}

}
