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
   /**
    * Using ATTR_NOTE constant String "PCDATA" instead of "note".
    *
    * change by M.Folger@roe.ac.uk.
    * 
    * Using the {@link keyword orac.util.AvTableToDom} "PCDATA" instead of "note" will cause
    * AvTableToDom to create XML output that looks like
    *
    * <!--
    * 
    * <SpNote>
    *   Some text.
    * </SpNote>
    *
    * rather than
    *
    * <SpNote>
    *   <note>
    *     Some text.
    *   </note>
    * </SpNote>
    * 
    * -->
    * 
    * <!-- for javadoc -->
    * <pre>
    * &lt;SpNote&gt;
    *   Some text.
    * &lt;/SpNote&gt;
    *
    * rather than
    *
    * &lt;SpNote&gt;
    *   &lt;note&gt;
    *     Some text.
    *   &lt;/note&gt;
    * &lt;/SpNote&gt;
    * </pre>
    * @see orac.util.AvTableToDom
    */
   public static final String ATTR_NOTE  = "PCDATA";

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
