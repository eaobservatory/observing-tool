/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * File filter for Science Programs in sgml format.
 * (*.sp. *.ot, *.sgml)
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class FileFilterSGML extends FileFilter {

  public static final String [] extension   = { ".sgml", ".ot", ".sp" };

  public static final String    description = "SP SGML (*.sgml, *.ot, *.sp)";

  public boolean accept(File file) {
    if(file.isDirectory()) {
      return true;
    }

    for(int i = 0; i < extension.length; i++) {
      if(file.getName().endsWith(extension[i])) {
        return true;
      }
    }
       
    return false;
  }
      
  public String getDescription() {
    return description;
  }
}
