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

/**
 * Changes for OMP (MFO, August 2001).
 */
public class ObsGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel1 = new JLabel();
    TextBoxWidgetExt obsTitle = new TextBoxWidgetExt();
    JLabel jLabel4 = new JLabel();
    JToggleButton priorityHigh = new JToggleButton();
    JToggleButton priorityMedium = new JToggleButton();
    JToggleButton priorityLow = new JToggleButton();
    CheckBoxWidgetExt chained = new CheckBoxWidgetExt();
    JLabel jLabel5 = new JLabel();
    JTextArea historyBox = new JTextArea();
  JPanel msbPanel = new JPanel();
  JLabel obsStateLabel = new JLabel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JLabel estimatedTimeLabel = new JLabel();
  TextBoxWidgetExt estimatedTime = new TextBoxWidgetExt();
  JLabel remainingLabel = new JLabel();
  DropDownListBoxWidgetExt remaining = new DropDownListBoxWidgetExt();
  JLabel xLabel = new JLabel();
  JLabel obsState = new JLabel();

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
        obsStateLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    obsStateLabel.setForeground(Color.black);
    obsStateLabel.setText("Status");
    msbPanel.setLayout(gridBagLayout2);
    estimatedTimeLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    estimatedTimeLabel.setForeground(Color.black);
    estimatedTimeLabel.setText("Estimated Time");
    estimatedTime.setEditable(false);
    remainingLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    remainingLabel.setForeground(Color.black);
    remainingLabel.setText("Observe");
    xLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    xLabel.setForeground(Color.black);
    xLabel.setText("X");
    obsState.setFont(new java.awt.Font("Dialog", 0, 12));
    obsState.setForeground(Color.black);
    obsState.setText("Not in Active Database");
    this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    this.add(obsTitle, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(chained, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(jLabel5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
    this.add(historyBox, new GridBagConstraints(0, 5, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.add(msbPanel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    msbPanel.add(jLabel4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    msbPanel.add(priorityHigh, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    msbPanel.add(priorityMedium, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    msbPanel.add(priorityLow, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    msbPanel.add(estimatedTimeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
    msbPanel.add(estimatedTime, new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(15, 5, 5, 5), 0, 0));
    msbPanel.add(remainingLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    msbPanel.add(remaining, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    msbPanel.add(xLabel, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(obsStateLabel, new GridBagConstraints(0, 2, 1, 1, -1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 10, 0), 0, 0));
    this.add(obsState, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));

    }
}
