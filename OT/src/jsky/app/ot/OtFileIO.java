// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

//import jsky.app.ot.gui.FileBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
//import jsky.app.ot.gui.util.AlertBox;
//import jsky.app.ot.gui.util.ErrorBox;

import java.io.PrintStream;

import gemini.sp.SpInputSGML;
import gemini.sp.SpItem;
//import gemini.sp.SpOutputSGML;
import gemini.sp.SpPhase1;
import gemini.sp.SpRootItem;

import java.awt.FileDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

import jsky.app.ot.OtTreeWidget;
import jsky.app.ot.OtWindow;

import orac.util.SpItemUtilities;
import orac.util.SpInputXML;
import orac.util.FileFilterSGML;
import orac.util.FileFilterXML;


/**
 * This is a utility class used for reading and writing science programs.
 * It handles the GUI end of the problem, classes in gemini.sp do the
 * actual translation from the file format into gemini.sp structures.
 * All methods are static since this is just a collection of helper
 * routines.
 */
public class OtFileIO
{
   private static String _lastDir = OT.getOtUserDir();
   
   /** Determines whether to save as SGML (*.ot, *.sp, *.sgml) or XML (*.xml). MFO, 2001 */
   private static boolean _io_xml;

   /** XML file filter (*.xml). MFO 2001 */
   protected static FileFilterXML   xmlFilter = new FileFilterXML();

   /** SGML file filter (*.ot, *.sp, *.sgml). MFO 2001 */
   protected static FileFilterSGML sgmlFilter = new FileFilterSGML();


/**
 * Store the Science Program rooted at the given SpItem into the file
 * given by the directory and filename arguments.
 */
public static boolean
storeSp(SpRootItem spItem, String dir, String filename)
{
   // Just create a File and pass it to the other storeSp method.
   File f = new File(dir, filename);
   return storeSp(spItem, f);
}

/**
 * Store the Science Program rooted at the give SpItem into the given
 * java.io.File.
 */
public static boolean
storeSp(SpRootItem spItem, File f)
{
   // Get a FileOutputStream pointing to the given File.
    if (f.exists()) {
	File backup = new File (f.getPath()+".BAK");
	f.renameTo(backup);
    }
   try {
      FileOutputStream fis = new FileOutputStream(f);
      OutputStream os = new BufferedOutputStream(fis);

      // MFO (store to XML)
      if(_io_xml) {
         if(System.getProperty("DEBUG") != null) { 
            System.out.println("xml = " + isXML());
         }
        try {
          if(System.getProperty("DEBUG") != null) { 
            System.out.println("Before removing id/idref\n");
            spItem.print();
          }

          SpItemUtilities.removeReferenceIDs(spItem);

          if(System.getProperty("DEBUG") != null) { 
            System.out.println("After removing id/idref\n");
            spItem.print();
          } 

	  (new SpItemUtilities()).setReferenceIDs(spItem);

	  // Set the ATTR_ELAPSED_TIME attributes in SpMSB components and
	  // SpObs components that are MSBs.
	  SpItemUtilities.saveElapsedTimes(spItem);

          // Make sure the msb attributes are set correctly.
          SpItemUtilities.updateMsbAttributes(spItem);

          (new PrintStream(os)).print(spItem.toXML());
	}
	catch(Exception e) {
	    fis.close();
	    os.close();
	  e.printStackTrace();
          JOptionPane.showMessageDialog(null, e.getMessage(), "Problem storing Science Program", JOptionPane.ERROR_MESSAGE);
	  return false;
	}
      }
      else {
         if(System.getProperty("DEBUG") != null) { 
            System.out.println("xml = " + isXML());
         }

         spItem.printDocument(os);
      }

      os.flush();
      fis.close();
      os.close();
   } catch (SecurityException sex) {
      JOptionPane.showMessageDialog(null, "The Observing Tool does not have access to '" +
                   f.getAbsolutePath() + "'.",
		   "Error", JOptionPane.ERROR_MESSAGE);
      
      return false;
   } catch (IOException ioex) {
      JOptionPane.showMessageDialog(null, ioex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
      return false;
   }

   // Pass the output stream to the Science Program SGML writer.
   //SpOutputSGML outSGML = new SpOutputSGML(os);
   //outSGML.print(spItem);
   
   return true;
}

/**
 * Read a Science Program from the file indicated by the given directory
 * and filename arguments.
 *
 * @return an SpItem root containing the Science Program if successful,
 * null otherwise
 */
public static SpRootItem
fetchSp(String dir, String filename)
{
   // Get a File object from the directory and filename;
   File f = new File(dir, filename);

   FileReader fr;
   try {
      fr = new FileReader(f);

   } catch (SecurityException sex) {
      JOptionPane.showMessageDialog(null, "The Observing Tool does not have access to the '" + dir +
                   "' directory.",
		   "Error", JOptionPane.ERROR_MESSAGE);
      return null;

   } catch (FileNotFoundException fnfex) {
      String path = dir;
      if (! (dir.endsWith(File.separator)) ) {
         path += File.separator;
      }
      path += filename;
      JOptionPane.showMessageDialog(null, "The file '" + path + "' was not found.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
      return null;

   } catch (IOException ioex) {
      JOptionPane.showMessageDialog(null, ioex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
      return null;
   }

   return fetchSp(fr);
}

/**
 * Read a Science Program from the given reader.
 *
 * @return an SpItem root containing the Science Program if successful,
 * null otherwise
 */
public static SpRootItem
fetchSp(Reader rdr)
{
  // MFO (next 18 lines)
  if(_io_xml) {
    if(System.getProperty("DEBUG") != null) { 
      System.out.println("xml = " + isXML());
    }

    try {
      return (SpRootItem)(new SpInputXML()).xmlToSpItem(rdr);
    }
    catch(Exception e) {
      JOptionPane.showMessageDialog(null, "Could not load Science Programme: " + e.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);        
      e.printStackTrace();
      return null;
    }
  }
  else {
    if(System.getProperty("DEBUG") != null) { 
      System.out.println("xml = " + isXML());
    }

    SpInputSGML inSGML = new SpInputSGML( rdr );

    // Read the science program
    SpRootItem newItem = inSGML.parseDocument();
    if (newItem == null) {
      JOptionPane.showMessageDialog(null, inSGML.getProblemDescr(), "Error", JOptionPane.ERROR_MESSAGE);
      return null;
    }

    // If this is a Phase1 document, generate a science program
    if (newItem instanceof SpPhase1) {
      newItem = ((SpPhase1) newItem).createProgram();
    }

    // Old science programs and libraries won't have their the MSB attributes set.
    // Set them now. (MFO, 04 March 2002)
    SpItemUtilities.updateMsbAttributes(newItem);

    return newItem;
  }
}

/**
 * Open a program on disk.
 */
public static void
open()
{
   JFileChooser fd = new JFileChooser(_lastDir); //FileBox.getFileDialog(FileDialog.LOAD);
   fd.addChoosableFileFilter(xmlFilter);
   fd.addChoosableFileFilter(sgmlFilter);
   //if (_lastDir != null) {
   //   fd.setCurrentDirectory(_lastDir);
   //}

   if(System.getProperty("OMP") != null) {
     fd.setFileFilter(xmlFilter);
   }
   else {
     fd.setFileFilter(sgmlFilter);
   }

   fd.showOpenDialog(null);
   if(fd.getSelectedFile() == null) {
     return;
   }

   String dir      = fd.getSelectedFile().getParent();
   String filename = fd.getSelectedFile().getName();
   if (filename == null) {
      return;
   }

   if(fd.getFileFilter() instanceof FileFilterXML) {
     _io_xml = true;
   }
   else {
     _io_xml = false;
   }

   if (dir != null) _lastDir = dir;

   SpRootItem spItem = fetchSp( dir, filename );
   if (spItem != null) {
      FileInfo fi = new FileInfo(dir, filename, true);
      OtWindow.create(spItem, fi);
   }
}

/**
 * Open the given program file.
 */
public static void
open(String filename)
{
   File file = new File(filename);
   String dir = file.getParent();
   String name = file.getName();
   SpRootItem spItem = fetchSp(dir, name);
   if (spItem != null) {
      FileInfo fi = new FileInfo(dir, name, true);
      OtWindow.create(spItem, fi);
   }
}




/**
 * Save a program to disk.
 */
public static boolean
save(SpRootItem spItem, FileInfo fi)
{
   if (fi.hasBeenSaved) {
      if (!storeSp(spItem, fi.dir, fi.filename)) {
         fi.hasBeenSaved = false;
         return save(spItem, fi);
      }
      spItem.getEditFSM().reset();
      return true;
   }

   //System.out.println("*** WRITE TO FILE ***");

   JFileChooser fd = new JFileChooser(fi.dir); //FileBox.getFileDialog(FileDialog.SAVE);
   fd.addChoosableFileFilter(xmlFilter);
   fd.addChoosableFileFilter(sgmlFilter);

   if(System.getProperty("OMP") != null) {
     fd.setFileFilter(xmlFilter);
   }
   else {
     fd.setFileFilter(sgmlFilter);
   }


   int rtn = fd.showSaveDialog(null);
   if (rtn == JFileChooser.CANCEL_OPTION) {
       return false;
   }

   //System.out.println("*** Default Dir.: " + fi.dir);
   //System.out.println("*** Default File: " + fi.filename);

   // Seems to do nothing ...
   //if ((fi.dir != null) && (fi.filename != null)) {
   //   fd.setDirectory(fi.dir);
   //   fd.setFile(fi.filename);
   //}
   //Set with a custom *.sp filter
   //fd.setFilenameFilter( new FilenameFilter( *.sp) );

//   fd.show();
//   String dir      = fd.getDirectory();
//   String filename = fd.getFile();
//
//   if (filename == null) {
//      return false;
//   }
//
//   // For some weird reason, on windows platforms a ".*.*" is appended
//   // to the filename!   Strip it off.
//   if (filename.endsWith(".*.*")) {
//      filename = filename.substring(0, filename.length() - 4);
//   }
//
//   File f = new File(dir, filename);

   File f = fd.getSelectedFile(); // FileBox.chooseFile(fd);

   if(fd.getFileFilter() instanceof FileFilterXML) {
     _io_xml = true;
   }
   else {
     _io_xml = false;
   }
   /*MFO DEBUG*/System.out.println("_io_xml = " + _io_xml);

   // Check whether this file is supposed to be an XML file but does not have any suffix.
   // If so, append ".xml" to its name.
   if(_io_xml && (f.getName().indexOf('.') < 0)) {
      f = new File(f.getAbsolutePath() + ".xml");
   }

   if (f == null) return false;
   String      dir = f.getParent();
   String filename = f.getName();

   if (dir != null) _lastDir = dir;

   // Check whether user is about to confuse file formats.
   if(!_io_xml && filename.endsWith(".xml")) {
      if (JOptionPane.showConfirmDialog(null,
                                        "Are you sure you want to save in SGML format\n" +
					"to a file with *.xml suffix?", "Incorrect file suffix",
					JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
         return false;
      }     
   }

   if(_io_xml && !filename.endsWith(".xml")) {
      if (JOptionPane.showConfirmDialog(null,
                                        "Are you sure you want to save in XML format\n" +
					"to a file without *.xml suffix?", "Incorrect file suffix",
					JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
         return false;
      }     
   }
   
   // Make sure we aren't going to over-write an existing file.
   if (f.exists()) {
      if (!f.canWrite()) {
         JOptionPane.showMessageDialog(null, "Can't write to `" + dir + filename + "'",
	                               "Error", JOptionPane.ERROR_MESSAGE);
         return false;
      }
 
      if (!f.isFile()) {
         JOptionPane.showMessageDialog(null, "`" + dir + filename + "' isn't an ordinary file.",
	                               "Error", JOptionPane.ERROR_MESSAGE);
         return false;
      }

      String m = "'" + dir + File.separator + filename + "' already exists.  Replace it?";
      //AlertBox ab = new AlertBox(m, "Replace", "Cancel");
      //if (ab.getChoice().equals("Cancel")) {
      if (JOptionPane.showConfirmDialog(null, m, "Replace file?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
         return false;
      }
   }

   String oldFileName = spItem.getTable().get(".gui.filename");
   spItem.getTable().set(".gui.filename", filename);

   SpRootItem spRoot = (SpRootItem)spItem.getRootItem();
   spRoot.setOTVersion();

   boolean hasBeenSaved = storeSp(spItem, f);

   if (hasBeenSaved) {
      fi.setDir(dir);
      fi.setFilename(filename);
      fi.setHasBeenSaved(true);
      spItem.getEditFSM().reset();
      OtProps.setSaveShouldPrompt(false);
   } else {
      spItem.getTable().set(".gui.filename", oldFileName);
   }

   return hasBeenSaved;
}

/**
 * Save the program under a new name.
 */
public static SpRootItem
saveAs(SpRootItem spItem, FileInfo fi)
{
   // Remember the "previously saved" state, then set it to false to that
   // the save method will prompt for a file name.  If the save fails or
   // is cancelled, reset hasBeenSaved.
   boolean hasBeenSaved = fi.hasBeenSaved;
   fi.hasBeenSaved = false;

   SpRootItem spCopy = (SpRootItem) spItem.deepCopy();
   spCopy.setOTVersion();

   if (!save(spCopy, fi)) {
      fi.hasBeenSaved = hasBeenSaved;
      return null;
   }

   return spCopy;
}

/**
 * Revert to the saved version of the program.
 */
public static SpRootItem
revertToSaved(FileInfo fi)
{
   if (!fi.hasBeenSaved) {
      JOptionPane.showMessageDialog(null, "This program has never been saved.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
      return null;
   }

   String m = "Revert to the saved version of '" + fi.filename + "'?";
   //AlertBox ab = new AlertBox(m, "Revert", "Cancel");
   //if (ab.getChoice().equals("Cancel")) {
   if (JOptionPane.showConfirmDialog(null, m, "Revert?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
      return null;
   }
   
   return fetchSp(fi.dir, fi.filename);
}

public static void setXML(boolean xml) {
   _io_xml = xml;
}

public static boolean isXML() {
   return _io_xml;
}

}

