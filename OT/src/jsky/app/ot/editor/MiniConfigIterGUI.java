
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
import jsky.app.ot.gui.CellSelectTableWidget;
import jsky.app.ot.gui.ListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;

public class MiniConfigIterGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel2 = new JLabel();
    JScrollPane itemsScrollPane = new JScrollPane();
    ListBoxWidgetExt availableItems = new ListBoxWidgetExt();
    JLabel jLabel3 = new JLabel();
    JLabel tableInfo = new JLabel();
    JScrollPane configScrollPane = new JScrollPane();
    JLabel jLabel5 = new JLabel();
    JLabel jLabel6 = new JLabel();
    JPanel jPanel1 = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JButton deleteTest = new JButton();
    JButton deleteStep = new JButton();
    JButton addStep = new JButton();
    JButton top = new JButton();
    JButton up = new JButton();
    JButton down = new JButton();
    JButton bottom = new JButton();
    CellSelectTableWidget iterStepsTable = new CellSelectTableWidget();
    JLabel listBoxTitle = new JLabel();
    JPanel listBoxGroup = new JPanel();
    GridBagLayout gridBagLayout4 = new GridBagLayout();
    JPanel choicePanel = new JPanel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    JPanel textBoxGroup = new JPanel();
    JScrollPane choicesScrollPane = new JScrollPane();
    BorderLayout borderLayout1 = new BorderLayout();
    ListBoxWidgetExt availableChoices = new ListBoxWidgetExt();
    JLabel textBoxTitle = new JLabel();
    TextBoxWidgetExt textBox = new TextBoxWidgetExt();

    public MiniConfigIterGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        this.setPreferredSize(new Dimension(394, 356));
        this.setLayout(gridBagLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel2.setForeground(Color.black);
        jLabel2.setText("Available Items");
        jLabel3.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel3.setForeground(Color.black);
        jLabel3.setText("Iteration Config");
        tableInfo.setFont(new java.awt.Font("Dialog", 0, 12));
        tableInfo.setForeground(Color.black);
        tableInfo.setText("(0 Items, 0 Steps)");
        jLabel5.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel5.setForeground(Color.black);
        jLabel5.setText("Step");
        jLabel6.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel6.setForeground(Color.black);
        jLabel6.setText("Item");
        jPanel1.setLayout(gridBagLayout2);
        deleteTest.setFont(new java.awt.Font("Dialog", 0, 12));
        deleteTest.setText("Delete");
        deleteStep.setFont(new java.awt.Font("Dialog", 0, 12));
        deleteStep.setText("Delete");
        addStep.setFont(new java.awt.Font("Dialog", 0, 12));
        addStep.setText("Add");
        down.setToolTipText("");
        availableItems.setBackground(Color.lightGray);
        iterStepsTable.setBackground(Color.lightGray);
        iterStepsTable.setShowHorizontalLines(false);
        listBoxTitle.setFont(new java.awt.Font("Dialog", 3, 12));
        listBoxTitle.setForeground(Color.black);
        listBoxTitle.setText("Available Choices");
        listBoxGroup.setLayout(gridBagLayout3);
        choicePanel.setLayout(borderLayout1);
        textBoxGroup.setLayout(gridBagLayout4);
        availableChoices.setBackground(Color.lightGray);
        choicesScrollPane.setPreferredSize(new Dimension(0, 0));
        textBoxTitle.setForeground(Color.black);
        textBoxTitle.setText("Value");
        this.add(jLabel2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
        this.add(itemsScrollPane, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
        this.add(jLabel3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
        this.add(tableInfo, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
        this.add(configScrollPane, new GridBagConstraints(0, 4, 2, 4, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
        configScrollPane.getViewport().add(iterStepsTable, null);
        this.add(jLabel5, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 0, 0));
        this.add(jLabel6, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
        this.add(jPanel1, new GridBagConstraints(0, 9, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(deleteTest, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(deleteStep, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        jPanel1.add(addStep, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        this.add(top, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.25
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(up, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.25
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(down, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.25
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(bottom, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.25
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(choicePanel, new GridBagConstraints(0, 0, 1, 2, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        choicePanel.add(listBoxGroup, BorderLayout.CENTER);
        listBoxGroup.add(choicesScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        listBoxGroup.add(listBoxTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        //choicePanel.add(textBoxGroup, BorderLayout.CENTER);
        textBoxGroup.add(textBoxTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        textBoxGroup.add(textBox, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        choicesScrollPane.getViewport().add(availableChoices, null);
        itemsScrollPane.getViewport().add(availableItems, null);
    }
}
