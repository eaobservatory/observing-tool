// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import gemini.util.XmlUtil;

//  MFO (June 12, 2002):
//    isObserveInstruction(), setObserveInstruction(boolean)
//    and ATTR_OBSERVE_INSTRUCTION added for OMP.

/**
 * The Note item.  Notes are arbitrary text information that may be
 * entered at any level of the hierarchy.
 */
public class SpNote extends SpItem
{
   public static final String ATTR_NOTE  = "note";

   /**
    * This attribute records whether this note should be highlighted.
    *
    * @see @isObserveInstruction()
    */
   public static final String ATTR_OBSERVE_INSTRUCTION = ":observeInstruction";

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

/**
 * Set whether this note should be highlighted.
 *
 * @see #isObserveInstruction()
 */
public void
setObserveInstruction(boolean value)
{
   if(value) {
      _avTable.set(ATTR_OBSERVE_INSTRUCTION, value);
   }
   else {
      _avTable.rm(ATTR_OBSERVE_INSTRUCTION);
   }
}

/** 
 * Indicates whether this note should be highlighted.
 *
 * During obsering this attribute allows programs like the QT to
 * draw the attention of the observer to notes which contain
 * information which is critical for the observation.
 */
public boolean
isObserveInstruction()
{
   return _avTable.getBool(ATTR_OBSERVE_INSTRUCTION);
}

/**
 * Converts Note text using HTML style character references.
 *
 * After after this SpNote was turned into XML the Note text is
 * converted back to ascii again.
 */
protected void
toXML(String indent, StringBuffer xmlBuffer)
{
   // Note text must be converted 
   
   // Set character references.
   setNote(XmlUtil.asciiToXml(getNote()));

   // Call super method
   super.toXML(indent, xmlBuffer);

   // Remove character references.
   setNote(XmlUtil.xmlToAscii(getNote()));
}

}
