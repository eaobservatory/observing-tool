// $Id$
/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on Allan Brighton (jsky/app/ot/editor/TitleEditorGUI.java)
 * @version 1.0
 */
package ot.editor;

import java.awt.*;
import javax.swing.*;
import jsky.app.ot.gui.*;

/**
 * Modified version of jsky.app.ot.editor.TitleEditorGUI.
 *
 * @see jsky.app.ot.editor.TitleEditorGUI
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on Allan Brighton (jsky/app/ot/editor/TitleEditorGUI.java)
 */
public class MsbEditorGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel1 = new JLabel();
    TextBoxWidgetExt nameBox = new TextBoxWidgetExt();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  CheckBoxWidgetExt obsDone = new CheckBoxWidgetExt();
  JToggleButton priorityHigh = new JToggleButton();
  JToggleButton priorityMedium = new JToggleButton();
  JToggleButton priorityLow = new JToggleButton();
  JLabel jLabel4 = new JLabel();
  TextBoxWidgetExt estimatedTime = new TextBoxWidgetExt();

    public MsbEditorGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Name");
        this.setPreferredSize(new Dimension(279, 271));
        this.setLayout(gridBagLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("Status");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("Priority");
    obsDone.setText("Done");
    obsDone.setFont(new java.awt.Font("Dialog", 0, 12));
    priorityHigh.setText("High");
    priorityHigh.setFont(new java.awt.Font("Dialog", 0, 12));
    priorityMedium.setText("Medium");
    priorityMedium.setFont(new java.awt.Font("Dialog", 0, 12));
    priorityLow.setText("Low");
    priorityLow.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setForeground(Color.black);
    jLabel4.setText("Estimated Time");
    estimatedTime.setEditable(false);
    this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        this.add(nameBox, new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(obsDone, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(priorityHigh, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    this.add(priorityMedium, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(priorityLow, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
    this.add(estimatedTime, new GridBagConstraints(1, 3, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(15, 5, 5, 5), 0, 0));
    }
}
