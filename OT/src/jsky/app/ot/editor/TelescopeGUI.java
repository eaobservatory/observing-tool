
/**
 * Title:        OT2<p>
 * Description:  Version of OT using JBuilder<p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      Gemini<p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot.editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import jsky.app.ot.editor.TelescopePosTableWidget;
import jsky.app.ot.gui.*;

public class TelescopeGUI extends JPanel {
    JPanel objectGBW = new JPanel();
    JPanel buttonPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    TextBoxWidgetExt nameTBW = new TextBoxWidgetExt();
    DropDownListBoxWidgetExt nameResolversDDLBW = new DropDownListBoxWidgetExt(); // MFO
    CommandButtonWidgetExt   resolveButton = new CommandButtonWidgetExt();  // MFO
    TextBoxWidgetExt xaxisTBW = new TextBoxWidgetExt();
    JLabel Dec_El_STW = new JLabel();
    DropDownListBoxWidgetExt tagDDLBW = new DropDownListBoxWidgetExt();
    JLabel RA_Az_STW = new JLabel();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel1 = new JLabel();
    DropDownListBoxWidgetExt systemDDLBW = new DropDownListBoxWidgetExt();
    TextBoxWidgetExt yaxisTBW = new TextBoxWidgetExt();
    Border border1;
    CommandButtonWidgetExt setBaseButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt newButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt plotButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt removeButton = new CommandButtonWidgetExt();
    GridBagLayout gridBagLayout4 = new GridBagLayout();
    TextBoxWidgetExt detailsEpochTBW = new TextBoxWidgetExt();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    TextBoxWidgetExt detailsEffWavelengthTBW = new TextBoxWidgetExt();
    JLabel jLabel17 = new JLabel();
    JLabel jLabel16 = new JLabel();
    JLabel jLabel15 = new JLabel();
    JLabel jLabel14 = new JLabel();
    JPanel detailsPW = new JPanel();
    JLabel jLabel13 = new JLabel();
    JLabel jLabel12 = new JLabel();
    TextBoxWidgetExt propMotionDecTBW = new TextBoxWidgetExt();
    JLabel jLabel11 = new JLabel();
    JLabel jLabel10 = new JLabel();
    JTabbedPane extrasFolder = new JTabbedPane();
    CommandButtonWidgetExt detailsAutoCBW = new CommandButtonWidgetExt();
    TextBoxWidgetExt detailsRadVelTBW = new TextBoxWidgetExt();
    TextBoxWidgetExt propMotionRATBW = new TextBoxWidgetExt();
    JLabel jLabel9 = new JLabel();
    JLabel jLabel8 = new JLabel();
    DropDownListBoxWidgetExt detailsSystemDDLBW = new DropDownListBoxWidgetExt();
    JLabel jLabel7 = new JLabel();
    JLabel jLabel6 = new JLabel();
    TextBoxWidgetExt detailsParallaxTBW = new TextBoxWidgetExt();
    JPanel propMotionPW = new JPanel();
    JScrollPane jScrollPane1 = new JScrollPane();
    TelescopePosTableWidget positionTable = new TelescopePosTableWidget();

  // chop mode added by MFO (3 August 2001)
  JPanel chopModePW = new JPanel();
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  CheckBoxWidgetExt chopMode = new CheckBoxWidgetExt();
  JLabel jLabel5 = new JLabel();
  TextBoxWidgetExt chopThrow = new TextBoxWidgetExt();
  JLabel jLabel18 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel19 = new JLabel();
  TextBoxWidgetExt chopAngle = new TextBoxWidgetExt();

    public TelescopeGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
	border1 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
							 "Object",
							 TitledBorder.LEFT,
							 TitledBorder.CENTER,
							 new Font("Dialog", Font.BOLD|Font.ITALIC, 12));
        this.setLayout(gridBagLayout4);
        objectGBW.setLayout(gridBagLayout1);
        objectGBW.setBorder(border1);
        Dec_El_STW.setText("Dec");
        Dec_El_STW.setForeground(Color.black);
        Dec_El_STW.setFont(new java.awt.Font("Dialog", 0, 12));
        RA_Az_STW.setText("RA");
        RA_Az_STW.setForeground(Color.black);
        RA_Az_STW.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setText("System");
        jLabel3.setForeground(Color.black);
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("Tag");
        jLabel2.setForeground(Color.black);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Name");
	nameResolversDDLBW.setFont(new java.awt.Font("Dialog", 0, 10)); // MFO
	resolveButton.setText("Resolve"); //MFO
        setBaseButton.setMargin(new Insets(2, 2, 2, 2));
        setBaseButton.setText("Set Base From Image");
        newButton.setMargin(new Insets(2, 10, 2, 10));
        newButton.setText("New");
        plotButton.setMargin(new Insets(2, 10, 2, 10));
        plotButton.setText("Plot...");
        removeButton.setMargin(new Insets(2, 2, 2, 2));
        removeButton.setText("Remove");
        this.setPreferredSize(new Dimension(360, 400));
        tagDDLBW.setFont(new java.awt.Font("Dialog", 0, 12));
        systemDDLBW.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel17.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel17.setForeground(Color.black);
        jLabel17.setText("(km/s)");
        jLabel16.setText("Dec");
        jLabel16.setForeground(Color.black);
        jLabel16.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel15.setText("(milli-arcsec/year)");
        jLabel15.setForeground(Color.black);
        jLabel15.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel14.setForeground(Color.black);
        jLabel14.setText("RA");
        detailsPW.setLayout(gridBagLayout3);
        detailsPW.setMinimumSize(new Dimension(400, 83));
        detailsPW.setPreferredSize(new Dimension(400, 89));
        jLabel13.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel13.setForeground(Color.black);
        jLabel13.setText("(milli-arcsec/year)");
        jLabel12.setText("(arcsec)");
        jLabel12.setForeground(Color.black);
        jLabel12.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel11.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel11.setForeground(Color.black);
        jLabel11.setText("(microns)");
        jLabel10.setText("Radial Vel");
        jLabel10.setForeground(Color.black);
        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
        detailsAutoCBW.setMargin(new Insets(2, 2, 2, 2));
        detailsAutoCBW.setText("auto");
        jLabel9.setText("Epoch");
        jLabel9.setForeground(Color.black);
        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setText("Parallax");
        jLabel8.setForeground(Color.black);
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        detailsSystemDDLBW.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel7.setText("Eff. Wavelength");
        jLabel7.setForeground(Color.black);
        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel6.setText("System");
        jLabel6.setForeground(Color.black);
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        propMotionPW.setLayout(gridBagLayout2);
        propMotionPW.setAlignmentX((float) 0.0);
        propMotionPW.setAlignmentY((float) 0.0);
        positionTable.setBackground(Color.lightGray);
        positionTable.setShowHorizontalLines(false);

	// chop mode added by MFO (3 August 2001)
        chopModePW.setLayout(gridBagLayout5);
    chopMode.setText("Chopping");
    chopMode.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel5.setForeground(Color.black);
    jLabel5.setText("Chop Throw");
    jLabel18.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel18.setForeground(Color.black);
    jLabel18.setText("(arc secs)");
    chopThrow.setPreferredSize(new Dimension(80, 21));
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setForeground(Color.black);
    jLabel4.setText("Chop PA");
    jLabel19.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel19.setForeground(Color.black);
    jLabel19.setText("(deg. E of N)");
    extrasFolder.setFont(new java.awt.Font("Dialog", 0, 12));
    this.add(objectGBW, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        objectGBW.add(nameTBW, new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0                           // MFO
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));

        objectGBW.add(nameResolversDDLBW, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0                // MFO
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0)); // MFO
        objectGBW.add(resolveButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0                     // MFO
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0)); // MFO

        objectGBW.add(xaxisTBW, new GridBagConstraints(3, 1, 3, 1, 0.0, 0.0                          // MFO
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
        objectGBW.add(Dec_El_STW, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        objectGBW.add(tagDDLBW, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
        objectGBW.add(RA_Az_STW, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        objectGBW.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        objectGBW.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        objectGBW.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
        objectGBW.add(systemDDLBW, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
        objectGBW.add(yaxisTBW, new GridBagConstraints(3, 2, 3, 1, 1.5, 0.0                          //MFO
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
        this.add(extrasFolder, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    // chop mode added by MFO (3 August 2001)
    extrasFolder.add(chopModePW, "Chop Mode");
    chopModePW.add(jLabel5, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    chopModePW.add(chopThrow, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    chopModePW.add(jLabel18, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    chopModePW.add(chopMode, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    chopModePW.add(jLabel4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    chopModePW.add(jLabel19, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    chopModePW.add(chopAngle, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        extrasFolder.add(propMotionPW, "Proper Motion");
        propMotionPW.add(jLabel14, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(16, 49, 0, 0), 0, 0));
        propMotionPW.add(propMotionRATBW, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(16, 8, 0, 0), 92, 0));
        propMotionPW.add(jLabel16, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(16, 49, 14, 0), 0, 0));
        propMotionPW.add(propMotionDecTBW, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(14, 9, 14, 0), 90, 0));
        propMotionPW.add(jLabel13, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(16, 0, 0, 134), 0, 0));
        propMotionPW.add(jLabel15, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(18, 0, 14, 134), 0, 0));
        extrasFolder.add(detailsPW, "Tracking Details");
        detailsPW.add(jLabel6, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        detailsPW.add(detailsSystemDDLBW, new GridBagConstraints(1, 0, 3, 2, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4, 5, 0, 0), 0, 0));
        detailsPW.add(jLabel7, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(13, 3, 0, 0), 0, 0));
        detailsPW.add(detailsEffWavelengthTBW, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 2, 0), 47, 0));
        detailsPW.add(jLabel11, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
        detailsPW.add(jLabel8, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
        detailsPW.add(detailsParallaxTBW, new GridBagConstraints(5, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 69, 0));
        detailsPW.add(jLabel10, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 8, 0, 0), 0, 0));
        detailsPW.add(detailsRadVelTBW, new GridBagConstraints(5, 3, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 68, 0));
        detailsPW.add(jLabel9, new GridBagConstraints(4, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 30, 6, 0), 0, 0));
        detailsPW.add(detailsEpochTBW, new GridBagConstraints(5, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 68, 0));
        detailsPW.add(jLabel12, new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 0, 10, 20), 0, 0));
        detailsPW.add(detailsAutoCBW, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 14, 2, 6), 0, 0));
        detailsPW.add(jLabel17, new GridBagConstraints(7, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jScrollPane1, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
        jScrollPane1.getViewport().add(positionTable, null);
        this.add(buttonPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(plotButton, null);
        buttonPanel.add(setBaseButton, null);
        buttonPanel.add(removeButton, null);
        buttonPanel.add(newButton, null);
    }

    void tagDDLBW_actionPerformed(ActionEvent e) {

    }

    void plotButton_actionPerformed(ActionEvent e) {

    }
}

