
/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot.editor;

import java.awt.*;
import javax.swing.*;
import jsky.app.ot.gui.*;

public class ProgramGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel1 = new JLabel();
    TextBoxWidgetExt titleBox = new TextBoxWidgetExt();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel4 = new JLabel();
    JLabel propKindLabel = new JLabel();
    OptionWidgetExt queueOption = new OptionWidgetExt();
    OptionWidgetExt classicalOption = new OptionWidgetExt();
    JEditorPane infoBox = new JEditorPane();
  TextBoxWidgetExt piBox = new TextBoxWidgetExt();
  TextBoxWidgetExt countryBox = new TextBoxWidgetExt();
  JLabel jLabel3 = new JLabel();
  TextBoxWidgetExt projectIdBox = new TextBoxWidgetExt();

    public ProgramGUI() {
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
        jLabel1.setText("Title");
        this.setLayout(gridBagLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setForeground(Color.black);
        jLabel2.setText("PI");
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setForeground(Color.black);
        jLabel4.setText("Country");
        propKindLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        propKindLabel.setForeground(Color.black);
        propKindLabel.setText("Prop. Kind");
        queueOption.setSelected(true);
        queueOption.setText("Queue");
        queueOption.setFont(new java.awt.Font("Dialog", 0, 12));
        classicalOption.setText("Classical");
        classicalOption.setFont(new java.awt.Font("Dialog", 0, 12));
        infoBox.setText("In the future all the proposal information will be accessible. This " +
    "form merely indicates the type of information that will be available.\n");
        infoBox.setBorder(BorderFactory.createLoweredBevelBorder());
        infoBox.setToolTipText("");
        infoBox.setBackground(Color.lightGray);
        infoBox.setEditable(false);
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("Project ID");
    this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 0, 0, 5), 0, 0));
        this.add(titleBox, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
        this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 0, 0, 5), 0, 0));
        this.add(jLabel4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 0, 0, 5), 0, 0));
        this.add(propKindLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 0, 0));
        this.add(classicalOption, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
        this.add(queueOption, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
        this.add(infoBox, new GridBagConstraints(0, 5, 3, 2, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 9, 5), 0, 0));
    this.add(piBox, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(countryBox, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 5), 0, 0));
    this.add(projectIdBox, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.setPreferredSize(new Dimension(282, 260));
    }
}
