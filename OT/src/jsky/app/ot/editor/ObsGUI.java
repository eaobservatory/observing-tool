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

public class ObsGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel1 = new JLabel();
    TextBoxWidgetExt obsTitle = new TextBoxWidgetExt();

    // Added for OMP (MFO, 1 August 2001)
    CheckBoxWidgetExt obsDone = new CheckBoxWidgetExt();

    JLabel jLabel4 = new JLabel();
    JToggleButton priorityHigh = new JToggleButton();
    JToggleButton priorityMedium = new JToggleButton();
    JToggleButton priorityLow = new JToggleButton();
    CheckBoxWidgetExt chained = new CheckBoxWidgetExt();
    JLabel jLabel5 = new JLabel();
    JTextArea historyBox = new JTextArea();
  JPanel msbPanel = new JPanel();
  JLabel jLabel2 = new JLabel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JLabel obsState = new JLabel();
  JLabel jLabel3 = new JLabel();
  TextBoxWidgetExt estimatedTime = new TextBoxWidgetExt();

    public ObsGUI() {
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
        jLabel1.setText("Obs Name");
        this.setLayout(gridBagLayout1);

	// Added for OMP (MFO, 1 August 2001)
        obsDone.setFont(new java.awt.Font("Dialog", 0, 12));
        obsDone.setForeground(Color.black);
        obsDone.setText("Done");

	jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setForeground(Color.black);
        jLabel4.setText("Priority");
        priorityHigh.setText("High");
        priorityHigh.setFont(new java.awt.Font("Dialog", 0, 12));
        priorityMedium.setText("Medium");
        priorityMedium.setFont(new java.awt.Font("Dialog", 0, 12));
        priorityLow.setText("Low");
        priorityLow.setFont(new java.awt.Font("Dialog", 0, 12));
        chained.setText("Chained To Next Obs");
        chained.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setForeground(Color.black);
        jLabel5.setText("History");
        this.setPreferredSize(new Dimension(280, 280));
        historyBox.setBorder(BorderFactory.createLoweredBevelBorder());
        historyBox.setBackground(Color.lightGray);
	historyBox.setEditable(false);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("Status");
    msbPanel.setLayout(gridBagLayout2);
    obsState.setFont(new java.awt.Font("Dialog", 0, 12));
    obsState.setForeground(Color.black);
    obsState.setText("Not in Active Database");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("Estimated Time");
    estimatedTime.setEditable(false);
    this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(obsTitle, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        // Modified for OMP (MFO, 1 August 2001)
	if(System.getProperty("OMP") != null) {
          msbPanel.add(obsDone, new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}
	else {
          msbPanel.add(obsState, new GridBagConstraints(2, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}

        this.add(chained, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel5, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
        this.add(historyBox, new GridBagConstraints(0, 4, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.add(msbPanel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    msbPanel.add(jLabel4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 10), 0, 0));
    msbPanel.add(priorityHigh, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    msbPanel.add(priorityMedium, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    msbPanel.add(priorityLow, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    msbPanel.add(jLabel2, new GridBagConstraints(0, 0, 2, 1, -1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 10, 10), 0, 0));
    msbPanel.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 5), 0, 0));
    msbPanel.add(estimatedTime, new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(15, 0, 5, 5), 0, 0));
    }
}
