
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */
package ot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyleConstants;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.filechooser.FileFilter;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.StringTokenizer;

//import orac.util.TextPrint;

/**
 * ReportBox has been rewritten to allow displaying different fonts and using java print routines.
 * TODO: The code is in an experimental stage and needs tidying up.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class ReportBox extends JFrame {
  protected JPanel jPanel1 = new JPanel();
  protected JButton _dismissButton = new JButton();
  protected JButton _printButton = new JButton();
  protected JButton _saveButton = new JButton();
  protected JScrollPane jScrollPane1 = new JScrollPane();
  protected JTextPane _textPane = new JTextPane();
  protected JFileChooser _fileChooser = new JFileChooser();

  protected String _asciiDescription          = "Text Only (*.txt)";
//  protected String _asciiLineBreakDescription = "Text Only with Line Breaks (*.txt)";
  protected String _richTextFormatDescription = "Rich Text Format (*.rtf)";
//  protected JPanel jPanel2 = new JPanel();
//  protected JLabel _label = new JLabel();

  public ReportBox() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    setBounds(100, 100, 480, 640);
    setVisible(true);

    _fileChooser.addChoosableFileFilter(new FileFilter() {
      public boolean accept(File file) {
        if(file.getName().endsWith(".txt")) {
          return true;
	}
	else {
          return false;
	}
      }
      
      public String getDescription() {
        return _asciiDescription;
      }
    });
/*
   _fileChooser.addChoosableFileFilter(new FileFilter() {
      public boolean accept(File file) {
        if(file.getName().endsWith(".txt")) {
          return true;
	}
	else {
          return false;
	}
      }
      
      public String getDescription() {
        return _asciiLineBreakDescription;
      }
    });
*/
    _fileChooser.addChoosableFileFilter(new FileFilter() {
      public boolean accept(File file) {
        if(file.getName().endsWith(".rtf")) {
          return true;
	}
	else {
          return false;
	}
      }
      public String getDescription() {
        return _richTextFormatDescription;
      }
    });
  }

  public ReportBox(String message) {
    this();

    initStylesForTextPane(_textPane);

    Document doc = _textPane.getDocument();
    try {
      doc.insertString(doc.getLength(), message + "\n", _textPane.getStyle("regular"));


      // The capability of using different fonts is not used yet.
      //doc.insertString(doc.getLength(), message + "\n", _textPane.getStyle("bold"));
      //doc.insertString(doc.getLength(), message + "\n", _textPane.getStyle("large"));
      //doc.insertString(doc.getLength(), message + "\n", _textPane.getStyle("small"));
      //doc.insertString(doc.getLength(), message, null);
    }
    catch(BadLocationException e) {
      e.printStackTrace();
    }
    
    //_textPane.setText(message);
    //_textPane.setLineWrap(true);
    _textPane.setEditable(false);
  }

  public ReportBox(String message, String title) {
    this(message);
    
    setTitle(title);
  }


  private void jbInit() throws Exception {
    _dismissButton.setText("Dismiss");
    _dismissButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        dismissButton_actionPerformed(e);
      }
    });
    _printButton.setText("Print");
    _printButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        printButton_actionPerformed(e);
      }
    });
    _saveButton.setText("Save");
    _saveButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        saveButton_actionPerformed(e);
      }
    });
    //_label.setText("Validation Report");
    this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(_dismissButton, null);
    jPanel1.add(_printButton, null);
    jPanel1.add(_saveButton, null);
    this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
//    this.getContentPane().add(jPanel2, BorderLayout.WEST);
    //jPanel2.add(_label, null);
    jScrollPane1.getViewport().add(_textPane, null);
  }

//  public void setLabel(JLabel label) {
//    _label = label;
//  }
  
  public void dismissButton_actionPerformed(ActionEvent e) {
    dispose();
  }

  public void printButton_actionPerformed(ActionEvent e) {
	    PrintJob pj = getToolkit().getPrintJob(ReportBox.this, "mfo print test", null);
	                                           //new JobAttributes(),
						   //new PageAttributes());
	    Graphics pg = pj.getGraphics();
	    //pg.setFont(fnt);
	    
	    
	    _textPane.printAll(pg); 
            pg.dispose(); 
            pj.end();

// ---
/*  
    TextPrint textPrint = new TextPrint();

    if(textPrint.init(this)) {
      textPrint.printMultiLine(_textPane.getText());
      textPrint.finish();
    }
*/    
  }

  public void saveButton_actionPerformed(ActionEvent e) {
  
    //_fileChooser.setDialogTitle("Save Error/Warning Report");
    
    if(_fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    
    String fileName    = _fileChooser.getSelectedFile().getPath();
    String description = _fileChooser.getFileFilter().getDescription();
    if(fileName != null) {
      
      try {
        if(description.equals(_richTextFormatDescription)) {
          (new RTFEditorKit()).write(new FileOutputStream(fileName),  _textPane.getDocument(), 0,  _textPane.getDocument().getLength());
	}
	else { //if(description.equals(_asciiDescription)) {
          PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
          StringTokenizer st = new StringTokenizer(_textPane.getText(), "\n");
          while (st.hasMoreTokens()) {
            printWriter.println(st.nextToken());
          }
          printWriter.close();
	}
/*	else {
          PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
          StringTokenizer st = new StringTokenizer(_textPane.getText(), "\n");
          while (st.hasMoreTokens()) {
            printWriter.println(st.nextToken());
          }
          printWriter.close();          
	} */
      }
      catch(IOException exception) {
        JOptionPane.showMessageDialog(this, "Problems writing to file \"" + fileName + "\": " + exception,
	                                   "Save Error", JOptionPane.ERROR_MESSAGE);
      }
      catch(BadLocationException exception) {
        JOptionPane.showMessageDialog(this, "Problems writing to file \"" + fileName + "\": " + exception,
	                                   "Save Error", JOptionPane.ERROR_MESSAGE);        
      }
    }
    //else {
    //  System.out.println("No file name.");
    //}
    
  }

  /**
   * Copied from java.sun.com example TextSamplerDemo.
   */
  protected void initStylesForTextPane(JTextPane textPane) {
    //Initialize some styles.
    Style def = StyleContext.getDefaultStyleContext().
                                    getStyle(StyleContext.DEFAULT_STYLE);

    Style regular = textPane.addStyle("regular", def);
    StyleConstants.setFontFamily(def, "SansSerif");

    Style s = textPane.addStyle("italic", regular);
    StyleConstants.setItalic(s, true);

    s = textPane.addStyle("bold", regular);
    StyleConstants.setBold(s, true);

    s = textPane.addStyle("small", regular);
    StyleConstants.setFontSize(s, 10);

    s = textPane.addStyle("large", regular);
    StyleConstants.setFontSize(s, 16);
  }

  public static void main(String [] args) {
    new ReportBox(args[0]);
  }
}
