// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

/**
 * The library item.
 */
public class SpLibrary extends SpRootItem
{

/**
 * Default constructor.  Initializes the SpLibrary with a default
 * library folder.
 */
protected SpLibrary()
{
   super(SpType.LIBRARY);
   setOTVersion();
}

/**
 * Construct a library, initializing it to contain the given library folder.
 */
protected SpLibrary(SpLibraryFolder lfPrototype)
{
   this();
   doInsert(lfPrototype, null);
}


}
