// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import java.util.Observable;

/**
 * This class stores "file" data associated with a program.  This includes
 * the filename and directory path, and whether or not the program has
 * ever been saved under the given filename.
 */
public class FileInfo extends Observable
{
   public String  dir;
   public String  filename;
   public boolean hasBeenSaved;

public FileInfo()
{
   filename     = "Untitled";
   hasBeenSaved = false;
}

public FileInfo(String dir, String filename, boolean hasBeenSaved)
{
   this.dir          = dir;
   this.filename     = filename;
   this.hasBeenSaved = hasBeenSaved;
}

public void
setDir(String newDir)
{
   dir = newDir;
}

public void
setFilename(String newFilename)
{
   filename = newFilename;
   setChanged();
   notifyObservers();
}

public void
setHasBeenSaved(boolean newHasBeenSaved)
{
   hasBeenSaved = newHasBeenSaved;
}

public String
toString()
{
   return "dir = " + dir + ", file = " + filename + ", hasBeenSaved = " + hasBeenSaved;
}

}
