
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

// MFO, June 06, 2002:
//   At the moment the only supported type is MAJOR. So the DropDownListBoxWidgetExt namedSystemType
//   has been commented out for now. I have not removed it completely from the code in case it is
//   needed in the future.
//   A DropDownListBoxWidgetExt with the available named target choices has been added.

public class TelescopeGUI extends JPanel {
    JTabbedPane targetSystemsTabbedPane = new JTabbedPane();
    JPanel nameTagPanel = new JPanel();
    JPanel objectGBW = new JPanel();
    JPanel conicSystemPanel = new JPanel();
    JPanel namedSystemPanel = new JPanel();
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
  JPanel chopPW = new JPanel();
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  CheckBoxWidgetExt chopping = new CheckBoxWidgetExt();
  JLabel jLabel5 = new JLabel();
  TextBoxWidgetExt chopThrow = new TextBoxWidgetExt();
  JLabel jLabel18 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel19 = new JLabel();
  TextBoxWidgetExt chopAngle = new TextBoxWidgetExt();
  DropDownListBoxWidgetExt chopSystem = new DropDownListBoxWidgetExt();
  JLabel chopSystemLabel = new JLabel();
  GridBagLayout gridBagLayout6 = new GridBagLayout();
  //JLabel jLabel21 = new JLabel();
  //DropDownListBoxWidgetExt namedSystemType = new DropDownListBoxWidgetExt();
  DropDownListBoxWidgetExt namedTarget = new DropDownListBoxWidgetExt();
  GridBagLayout gridBagLayout7 = new GridBagLayout();
  TextBoxWidgetExt epoch = new TextBoxWidgetExt();
  TextBoxWidgetExt epochPerih = new TextBoxWidgetExt();
  TextBoxWidgetExt orbinc = new TextBoxWidgetExt();
  TextBoxWidgetExt anode = new TextBoxWidgetExt();
  JLabel epochLabel = new JLabel();
  JLabel epochPerihLabel = new JLabel();
  JLabel epochPerihUnitsLabel = new JLabel();
  JLabel orbincLabel = new JLabel();
  JLabel anodeLabel = new JLabel();
  JLabel perihLabel = new JLabel();
  JLabel aorqLabel = new JLabel();
  JLabel eLabel = new JLabel();
  TextBoxWidgetExt perih = new TextBoxWidgetExt();
  TextBoxWidgetExt aorq = new TextBoxWidgetExt();
  TextBoxWidgetExt e = new TextBoxWidgetExt();
  DropDownListBoxWidgetExt conicSystemType = new DropDownListBoxWidgetExt();
  FlowLayout flowLayout1 = new FlowLayout();
  TextBoxWidgetExt l_or_m = new TextBoxWidgetExt();
  TextBoxWidgetExt dm = new TextBoxWidgetExt();
  JLabel l_or_mLabel = new JLabel();
  JLabel dmLabel = new JLabel();
  JLabel jLabel20 = new JLabel();
  JLabel jLabel22 = new JLabel();
  JLabel jLabel23 = new JLabel();
  JLabel jLabel24 = new JLabel();
  JLabel jLabel25 = new JLabel();
  JLabel l_or_mUnitsLabel = new JLabel();
  JLabel dmUnitsLabel = new JLabel();
  DropDownListBoxWidgetExt dropDownListBoxWidgetExt1 = new DropDownListBoxWidgetExt();
  CheckBoxWidgetExt offsetCheckBox = new CheckBoxWidgetExt();
  JLabel xUnitsLabel = new JLabel();
  JLabel yUnitsLabel = new JLabel();

  JLabel targetType = new JLabel();
  DropDownListBoxWidgetExt targetTypeDDList = new DropDownListBoxWidgetExt();

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
        jLabel2.setText("        Tag");
        jLabel2.setForeground(Color.black);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Name");
        targetType.setFont(new java.awt.Font("Dialog", 0, 12));
        targetType.setForeground(Color.black);
        targetType.setText("TargetType");
	nameResolversDDLBW.setFont(new java.awt.Font("Dialog", 0, 10)); // MFO
	resolveButton.setText("Resolve Name"); //MFO
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
	targetTypeDDList.setFont(new java.awt.Font("Dialog", 0, 12));
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
        chopPW.setLayout(gridBagLayout5);
    chopping.setText("Chopping");
    chopping.setFont(new java.awt.Font("Dialog", 0, 12));
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
    chopSystemLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    chopSystemLabel.setForeground(Color.black);
    chopSystemLabel.setText("System");
    chopSystem.setFont(new java.awt.Font("Dialog", 0, 12));
    targetSystemsTabbedPane.setFont(new java.awt.Font("Dialog", 0, 12));
    targetSystemsTabbedPane.setMinimumSize(new Dimension(273, 140));
    targetSystemsTabbedPane.setPreferredSize(new Dimension(274, 130));
    namedSystemPanel.setLayout(gridBagLayout6);
    //jLabel21.setFont(new java.awt.Font("Dialog", 0, 12));
    //jLabel21.setForeground(Color.black);
    //jLabel21.setText("Type");
    conicSystemPanel.setLayout(gridBagLayout7);
    epochLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    epochLabel.setForeground(Color.black);
    epochLabel.setText("t0");
    epochPerihLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    epochPerihLabel.setForeground(Color.black);
    epochPerihLabel.setText("TP");    
    orbincLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    orbincLabel.setForeground(Color.black);
    orbincLabel.setText("i");
    anodeLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    anodeLabel.setForeground(Color.black);
    anodeLabel.setText("\u03A9");
    perihLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    perihLabel.setForeground(Color.black);
    perihLabel.setText("\u03C9");
    aorqLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    aorqLabel.setForeground(Color.black);
    aorqLabel.setText("q");
    eLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    eLabel.setForeground(Color.black);
    eLabel.setText("e");
    perih.setColumns(6);
    nameTBW.setColumns(10);
    nameTagPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    conicSystemType.setFont(new java.awt.Font("Dialog", 0, 12));
    //namedSystemType.setFont(new java.awt.Font("Dialog", 0, 12));
    namedTarget.setFont(new java.awt.Font("Dialog", 0, 12));
    l_or_mLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    l_or_mLabel.setForeground(Color.black);
    l_or_mLabel.setText("L");
    dmLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    dmLabel.setForeground(Color.black);
    dmLabel.setText("n");
    jLabel20.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel20.setForeground(Color.black);
    jLabel20.setText("(TT)");
    jLabel22.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel22.setForeground(Color.black);
    jLabel22.setText("(deg)");
    jLabel23.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel23.setForeground(Color.black);
    jLabel23.setText("(deg)");
    jLabel24.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel24.setForeground(Color.black);
    jLabel24.setText("(deg)");
    jLabel25.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel25.setForeground(Color.black);
    jLabel25.setText("(AU)");
    l_or_mUnitsLabel.setFont(new java.awt.Font("Dialog", 0, 10));
    l_or_mUnitsLabel.setForeground(Color.black);
    l_or_mUnitsLabel.setText("(deg)");
    epochPerihUnitsLabel.setFont(new java.awt.Font("Dialog", 0, 10));
    epochPerihUnitsLabel.setForeground(Color.black);
    epochPerihUnitsLabel.setText("(TT)");
    dmUnitsLabel.setFont(new java.awt.Font("Dialog", 0, 10));
    dmUnitsLabel.setForeground(Color.black);
    dmUnitsLabel.setText("(deg)");
    epoch.setColumns(10);
    epochPerih.setColumns(10);
    l_or_m.setColumns(6);
    detailsRadVelTBW.setColumns(10);
    offsetCheckBox.setVerticalTextPosition(SwingConstants.BOTTOM);
    offsetCheckBox.setText("Offset");
    offsetCheckBox.setFont(new java.awt.Font("Dialog", 0, 12));
    offsetCheckBox.setVerticalAlignment(SwingConstants.BOTTOM);
    xUnitsLabel.setFont(new java.awt.Font("Dialog", 0, 10));
    xUnitsLabel.setForeground(Color.black);
    xUnitsLabel.setText("(degrees)");
    yUnitsLabel.setFont(new java.awt.Font("Dialog", 0, 10));
    yUnitsLabel.setForeground(Color.black);
    yUnitsLabel.setText("(degrees)");

    epochPerih.setVisible(false);
    epochPerihLabel.setVisible(false);
    epochPerihUnitsLabel.setVisible(false);
    
    this.add(nameTagPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    nameTagPanel.add(jLabel1, null);
    nameTagPanel.add(nameTBW, null);
    nameTagPanel.add(jLabel2, null);
    nameTagPanel.add(tagDDLBW, null);
    nameTagPanel.add(targetType, null);
    nameTagPanel.add(targetTypeDDList, null);
    this.add(targetSystemsTabbedPane, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        objectGBW.add(nameResolversDDLBW, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0)); // MFO
        objectGBW.add(resolveButton, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 0, 0)); // MFO

        objectGBW.add(xaxisTBW, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));
        objectGBW.add(Dec_El_STW, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        objectGBW.add(RA_Az_STW, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        objectGBW.add(jLabel3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        objectGBW.add(systemDDLBW, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        objectGBW.add(yaxisTBW, new GridBagConstraints(3, 2, 1, 1, 1.5, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
    objectGBW.add(offsetCheckBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    objectGBW.add(xUnitsLabel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    objectGBW.add(yUnitsLabel, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(extrasFolder, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    // Target Types Tabbed Pane, added by MFO (22 February 2002)
    targetSystemsTabbedPane.add(objectGBW,            "RA/Dec");
    targetSystemsTabbedPane.add(conicSystemPanel, "Orbital Elements");
    conicSystemPanel.add(epoch, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
    conicSystemPanel.add(orbinc, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
    conicSystemPanel.add(anode, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
    conicSystemPanel.add(epochLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(orbincLabel, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(anodeLabel, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(perihLabel, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(aorqLabel, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(eLabel, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(perih, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
    conicSystemPanel.add(aorq, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
    conicSystemPanel.add(e, new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
    conicSystemPanel.add(jLabel20, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 15), 0, 0));
    conicSystemPanel.add(jLabel22, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(jLabel23, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(jLabel24, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(jLabel25, new GridBagConstraints(8, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(conicSystemType, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    conicSystemPanel.add(l_or_m, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
    conicSystemPanel.add(dm, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
    conicSystemPanel.add(l_or_mLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(dmLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(l_or_mUnitsLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 15), 0, 0));
    conicSystemPanel.add(dmUnitsLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    conicSystemPanel.add(epochPerih, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
    conicSystemPanel.add(epochPerihLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    conicSystemPanel.add(epochPerihUnitsLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 15), 0, 0));

    targetSystemsTabbedPane.add(namedSystemPanel,     "Planets, Sun, Moon");
    //namedSystemPanel.add(jLabel21, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
    //        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    //namedSystemPanel.add(namedSystemType, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
    //        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    namedSystemPanel.add(namedTarget, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    for (int i=0; i<targetSystemsTabbedPane.getTabCount(); i++) {
	targetSystemsTabbedPane.setEnabledAt(i, false);
	Component [] component = ((JPanel)targetSystemsTabbedPane.getComponentAt(i)).getComponents();
	for (int j=0; j<component.length; j++) {
	    component[j].setEnabled(false);
	}
    }

    // chop mode added by MFO (3 August 2001)
    chopPW.add(jLabel5, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    chopPW.add(chopThrow, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    chopPW.add(jLabel18, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    chopPW.add(chopping, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    chopPW.add(jLabel4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    chopPW.add(jLabel19, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    chopPW.add(chopAngle, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    chopPW.add(chopSystem, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    chopPW.add(chopSystemLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
        detailsPW.add(jLabel10, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        detailsPW.add(detailsRadVelTBW, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4, 5, 0, 0), 0, 0));
        detailsPW.add(jLabel7, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(13, 3, 0, 0), 0, 0));
        detailsPW.add(detailsEffWavelengthTBW, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 2, 0), 47, 0));
        detailsPW.add(jLabel11, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
        detailsPW.add(jLabel8, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
        detailsPW.add(detailsParallaxTBW, new GridBagConstraints(5, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 69, 0));
        detailsPW.add(jLabel6, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        detailsPW.add(detailsSystemDDLBW, new GridBagConstraints(5, 3, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 68, 0));
        detailsPW.add(jLabel9, new GridBagConstraints(4, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 30, 6, 0), 0, 0));
        detailsPW.add(detailsEpochTBW, new GridBagConstraints(5, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 68, 0));
        detailsPW.add(jLabel12, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 0, 10, 20), 0, 0));
        detailsPW.add(detailsAutoCBW, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 14, 2, 6), 0, 0));
        detailsPW.add(jLabel17, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        extrasFolder.add(propMotionPW, "Proper Motion");
        extrasFolder.add(detailsPW, "Radial Vel/Tracking");
        extrasFolder.add(chopPW, "Chop Settings");
        this.add(jScrollPane1, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
        jScrollPane1.getViewport().add(positionTable, null);
        this.add(buttonPanel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(plotButton, null);
        buttonPanel.add(setBaseButton, null);
        buttonPanel.add(removeButton, null);
        buttonPanel.add(newButton, null);
    }
}

