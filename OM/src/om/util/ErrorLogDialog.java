
/**
 * Title:        Ukirt GUIs<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */
package om.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ErrorLogDialog extends JDialog {
  JPanel jPanel1 = new JPanel();
  JButton clear = new JButton();
  JButton close = new JButton();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextPane errorMessages = new JTextPane();

  public ErrorLogDialog() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    clear.setText("Clear");
    clear.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        clear_actionPerformed(e);
      }
    });
    close.setText("Close");
    close.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        close_actionPerformed(e);
      }
    });
    this.setTitle("Error Message Log");
    errorMessages.setBackground(Color.lightGray);
    errorMessages.setEditable(false);
    this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(clear, null);
    jPanel1.add(close, null);
    this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(errorMessages, null);

    setBounds(100, 100, 400, 400);
  }

  public void addMessage(String message) {
    errorMessages.setText(errorMessages.getText() + "\n" + message);
    show();
  }

  void clear_actionPerformed(ActionEvent e) {
    errorMessages.setText("");
  }

  void close_actionPerformed(ActionEvent e) {
    hide();
  }
}
