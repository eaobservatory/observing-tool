// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

//import jsky.app.ot.gui.FileBox;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
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

import orac.util.SpItemDOM;


/**
 * This is a utility class used for reading and writing science programs.
 * It handles the GUI end of the problem, classes in gemini.sp do the
 * actual translation from the file format into gemini.sp structures.
 * All methods are static since this is just a collection of helper
 * routines.
 */
public class OtFileIO
{
   private static String _lastDir;

   public static final String []  xmlExtension = { ".xml" };
   public static final String [] sgmlExtension = { ".sgml", ".ot", ".sp" };

   public static final String  xmlDescription = "Science Program XML (*.xml)";
   public static final String sgmlDescription = "Science Program SGML (*.sgml, *.ot, *.sp)";

   private static boolean _io_xml = false;
   
   public static FileFilter xmlFilter = new FileFilter() {
     public boolean accept(File file) {
       if(file.isDirectory()) {
         //_io_xml = true;
	 
         return true;
       }

       for(int i = 0; i < xmlExtension.length; i++) {
         if(file.getName().endsWith(xmlExtension[i])) {
           _io_xml = true;
	 
           return true;
         }
       }
       
       //_io_xml = false;
       
       return false;
     }
      
     public String getDescription() {
       return xmlDescription;
     }
   };

   public static FileFilter sgmlFilter = new FileFilter() {
     public boolean accept(File file) {
       if(file.isDirectory()) {
         //_io_xml = false;
	 
         return true;
       }

       for(int i = 0; i < sgmlExtension.length; i++) {
         if(file.getName().endsWith(sgmlExtension[i])) {
           _io_xml = false;
	 
           return true;
         }
       }
       
       //_io_xml = true;
       
       return false;
     }
      
     public String getDescription() {
       return sgmlDescription;
     }
   };


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
   try {
      FileOutputStream fis = new FileOutputStream(f);
      OutputStream os = new BufferedOutputStream(fis);

      if(_io_xml) {
        (new PrintStream(os)).print((new SpItemDOM(spItem)).toString());
      }
      else {
        spItem.printDocument(os);
      }

      os.flush();
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
  if(_io_xml) {
    return (new SpItemDOM(rdr)).getSpItem();
  }
  else {
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
   //   fd.setDirectory(_lastDir);
   //}

   fd.showOpenDialog(null);
   String dir      = fd.getSelectedFile().getParent(); // getDirectory();
   String filename = fd.getSelectedFile().getName();   // getFile();
   if (filename == null) {
      return;
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
   fd.showOpenDialog(null);
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
   if (f == null) return false;
   String      dir = f.getParent();
   String filename = f.getName();

   if (dir != null) _lastDir = dir;
   
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

      String m = "'" + dir + filename + "' already exists.  Replace it?";
      //AlertBox ab = new AlertBox(m, "Replace", "Cancel");
      //if (ab.getChoice().equals("Cancel")) {
      if (JOptionPane.showConfirmDialog(null, m, "Replace file?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
         return false;
      }
   }

   String oldFileName = spItem.getTable().get(".gui.filename");
   spItem.getTable().set(".gui.filename", filename);

   boolean hasBeenSaved = storeSp(spItem, f);

   if (hasBeenSaved) {
      fi.setDir(dir);
      fi.setFilename(filename);
      fi.setHasBeenSaved(true);
      spItem.getEditFSM().reset();
   } else {
      spItem.getTable().set(".gui.filename.", oldFileName);
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


}

