
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

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import orac.util.TextPrint;


public class ReportBox extends JFrame {
  protected JPanel jPanel1 = new JPanel();
  protected JButton _dismissButton = new JButton();
  protected JButton _printButton = new JButton();
  protected JButton _saveButton = new JButton();
  protected JScrollPane jScrollPane1 = new JScrollPane();
  protected JTextArea _textArea = new JTextArea();
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
  }

  public ReportBox(String message) {
    this();

    _textArea.setText(message);
    _textArea.setLineWrap(true);
    _textArea.setEditable(false);
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
    jScrollPane1.getViewport().add(_textArea, null);
  }

//  public void setLabel(JLabel label) {
//    _label = label;
//  }
  
  public void dismissButton_actionPerformed(ActionEvent e) {
    dispose();
  }

  public void printButton_actionPerformed(ActionEvent e) {
    TextPrint textPrint = new TextPrint();

    if(textPrint.init(this)) {
      textPrint.printMultiLine(_textArea.getText());
      textPrint.finish();
    }  
  }

  public void saveButton_actionPerformed(ActionEvent e) {
    JFileChooser fileDialog = new JFileChooser();
    //fileDialog.setDialogTitle("Save Error/Warning Report");
    
    if(fileDialog.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    
    String fileName = fileDialog.getSelectedFile().getPath();
    if(fileName != null) {
      try {
        PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
        StringTokenizer st = new StringTokenizer(_textArea.getText(), "\n");
        while (st.hasMoreTokens()) {
          printWriter.println(st.nextToken());
        }
        printWriter.close();
      }
      catch(IOException exception) {
        JOptionPane.showMessageDialog(this, "Problems writing to file \"" + fileName + "\": " + exception,
	                                   "Save Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    //else {
    //  System.out.println("No file name.");
    //}
  }
}
