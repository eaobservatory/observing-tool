// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

/**
 * The library folder item.
 */
public class SpLibraryFolder extends SpObsContextItem
{

/**
 * Default constructor.
 */
protected SpLibraryFolder()
{
   super(SpType.LIBRARY_FOLDER);
}

/**
 * Override getTitle so that it simply returns the "title" attribute if
 * set.
 */
public String
getTitle()
{
   String title = getTitleAttr();
   if ((title == null) || title.equals("")) {
      title = type().getReadable();
   }
   return title;
}
 
}
