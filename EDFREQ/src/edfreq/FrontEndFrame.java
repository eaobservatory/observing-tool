/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package edfreq;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Frame for FrontEnd.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class FrontEndFrame extends JFrame implements ActionListener, WindowListener {

  private FrontEnd _frontEnd;

  private JFileChooser _fileChooser = new JFileChooser();

  private File _currentFile;

  private String _currentXML = "";

  public FrontEndFrame(FrontEnd frontEnd) {
    super("Frequency Editor");

    _frontEnd = frontEnd;

    setJMenuBar(new Edmenu(this));
    getContentPane().add(frontEnd);
    pack();
    setLocation(100, 100);
    setVisible(true);
    _frontEnd.setSideBandDisplayLocation(200, 300);
    _frontEnd.setSideBandDisplayVisible(true);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    addWindowListener(this);

    _fileChooser.setFileFilter(new FileFilter() {
      public boolean accept(File file) {
        if(file.isDirectory()) {
          return true;
        }

        if(file.getName().endsWith(".xml")) {
          return true;
        }

        return false;
      }
      
      public String getDescription() {
        return "XML file (*.xml)";
      }
    });

    _currentXML = _frontEnd.toXML();
  }

  public void open() {
    if(!_currentXML.equals(_frontEnd.toXML())) {
      int userRequest = JOptionPane.showConfirmDialog(this,
                                       "Do you want to save before loading other settings?",
                                       "Save?",
				       JOptionPane.YES_NO_CANCEL_OPTION);

      switch(userRequest) {
        case JOptionPane.YES_OPTION:
	  save();
	case JOptionPane.CANCEL_OPTION:
	  return;
      }
    }
  
    try {
      _fileChooser.showOpenDialog(this);
      if(_fileChooser.getSelectedFile() == null) {
        return;
      }

      _currentFile = _fileChooser.getSelectedFile();
      FileInputStream fileInputStream = new FileInputStream(_currentFile);
      byte [] xmlBytes = new byte[fileInputStream.available()];
      fileInputStream.read(xmlBytes);
      _frontEnd.update(new String(xmlBytes));
      _currentXML = _frontEnd.toXML();
      fileInputStream.close();
    }
    catch(Exception e) {
       e.printStackTrace();
    }
  }

  public void saveAs() {
    try {
      _fileChooser.showSaveDialog(this);

      File file = _fileChooser.getSelectedFile();
      
      if(file == null) {
        return;
      }


      // Make sure we aren't going to over-write an existing file.
      if (file.exists()) {
        if (!file.canWrite()) {
          JOptionPane.showMessageDialog(this, "Can't write to `" + file + "'",
	                               "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        if (!file.isFile()) {
          JOptionPane.showMessageDialog(this, "`" + file + "' isn't an ordinary file.",
	                               "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        String message = "'" + file + "' already exists.  Replace it?";
        if (JOptionPane.showConfirmDialog(this, message, "Replace file?",
	                                  JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
          return;
        }
      }

      FileOutputStream fileOutputStream = new FileOutputStream(file);
      PrintWriter printWriter = new PrintWriter(fileOutputStream);
      printWriter.println(_frontEnd.toXML());
      printWriter.close();
      fileOutputStream.close();

      _currentXML = _frontEnd.toXML();
      _currentFile = file;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void save() {

    if(_currentFile == null) {
      saveAs();

      return;
    }

    try {
      FileOutputStream fileOutputStream = new FileOutputStream(_currentFile);
      PrintWriter printWriter = new PrintWriter(fileOutputStream);
      printWriter.println(_frontEnd.toXML());
      printWriter.close();
      fileOutputStream.close();

      _currentXML = _frontEnd.toXML();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void exit() {
    if(!_currentXML.equals(_frontEnd.toXML())) {
      int userRequest = JOptionPane.showConfirmDialog(this,
                                       "Do you want to save before exiting?",
                                       "Save?",
				       JOptionPane.YES_NO_CANCEL_OPTION);

      switch(userRequest) {
        case JOptionPane.CANCEL_OPTION:
	  return;
        case JOptionPane.YES_OPTION:
	  save();
      }
    }

    System.exit(0);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getActionCommand() == "Open") {
      open();
    }

    if(e.getActionCommand() == "Save") {
      save();
    }

    if(e.getActionCommand() == "Save As ...") {
      saveAs();
    }

    if(e.getActionCommand() == "Exit") {
      exit();
    }
  }

  public void windowActivated(WindowEvent e)   { } 
  public void windowClosed(WindowEvent e)      { }

  public void windowClosing(WindowEvent e) {
    exit();
  }

  public void windowDeactivated(WindowEvent e) { }
  public void windowDeiconified(WindowEvent e) { }
  public void windowIconified(WindowEvent e)   { }
  public void windowOpened(WindowEvent e)      { }
}

