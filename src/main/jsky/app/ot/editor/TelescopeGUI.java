/*
 * Copyright: Copyright (c) 2000 Association of Universities for Research in
 *            Astronomy, Inc. (AURA)
 * Company:   Gemini
 * @author Allan Brighton
 * @version 1.0
 *
 * License:
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jsky.app.ot.editor;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import jsky.app.ot.OtCfg;
import jsky.app.ot.editor.TelescopePosTableWidget;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;

import orac.util.TelescopeUtil;
import ot.gui.GuiUtil;

// MFO, June 06, 2002:
// At the moment the only supported type is MAJOR. So the
// DropDownListBoxWidgetExt namedSystemType has been commented out for now.
// I have not removed it completely from the code in case it is
// needed in the future.
// A DropDownListBoxWidgetExt with the available named target choices
// has been added.
@SuppressWarnings("serial")
public class TelescopeGUI extends JPanel {
    public static final String TAB_PROPER_MOTION = "Proper Motion";
    public static final String TAB_RADIAL_VELOCITY = "Radial Vel/Tracking";
    public static final String TAB_CHOP_SETTINGS = "Chop Settings";

    JTabbedPane targetSystemsTabbedPane = new JTabbedPane();
    JPanel nameTagPanel = new JPanel();
    JPanel objectGBW = new JPanel();
    JPanel conicSystemPanel = new JPanel();
    JPanel namedSystemPanel = new JPanel();
    JPanel tleSystemPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    TextBoxWidgetExt nameTBW = new TextBoxWidgetExt();
    DropDownListBoxWidgetExt nameResolversDDLBW =
            new DropDownListBoxWidgetExt(); // MFO
    CommandButtonWidgetExt resolveButton =
            new CommandButtonWidgetExt(); // MFO
    TextBoxWidgetExt xaxisTBW = new TextBoxWidgetExt();
    JLabel Dec_El_STW = new JLabel();
    JLabel RA_Az_STW = new JLabel();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel1 = new JLabel();
    DropDownListBoxWidgetExt systemDDLBW = new DropDownListBoxWidgetExt();
    TextBoxWidgetExt yaxisTBW = new TextBoxWidgetExt();
    Border border1;
    CommandButtonWidgetExt setBaseButton = new CommandButtonWidgetExt();
    JLabel newLabel = new JLabel("Add");
    JComboBox newButton = new JComboBox();
    CommandButtonWidgetExt plotButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt removeButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt broadcastButton = new CommandButtonWidgetExt();
    GridBagLayout gridBagLayout4 = new GridBagLayout();
    TextBoxWidgetExt detailsEpochTBW = new TextBoxWidgetExt();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JLabel jLabel16 = new JLabel();
    JLabel jLabel15 = new JLabel();
    JLabel jLabel14 = new JLabel();
    JPanel detailsPW = new JPanel();
    JLabel jLabel13 = new JLabel();
    JLabel jLabel12 = new JLabel();
    TextBoxWidgetExt propMotionDecTBW = new TextBoxWidgetExt();
    JTabbedPane extrasFolder = new JTabbedPane();
    TextBoxWidgetExt propMotionRATBW = new TextBoxWidgetExt();
    JLabel jLabel9 = new JLabel();
    JLabel jLabel8 = new JLabel();
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
    GridLayout gridLayout1 = new GridLayout();
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
    DropDownListBoxWidgetExt dropDownListBoxWidgetExt1 =
            new DropDownListBoxWidgetExt();
    CheckBoxWidgetExt offsetCheckBox = new CheckBoxWidgetExt();
    JLabel xUnitsLabel = new JLabel();
    JLabel yUnitsLabel = new JLabel();
    JLabel targetType = new JLabel();
    DropDownListBoxWidgetExt targetTypeDDList = new DropDownListBoxWidgetExt();
    JLabel resolvedName = new JLabel();

    // Widgets for the TLE panel
    TextBoxWidgetExt tleEpochYear = new TextBoxWidgetExt();
    TextBoxWidgetExt tleEpochDay = new TextBoxWidgetExt();
    TextBoxWidgetExt tleInclination = new TextBoxWidgetExt();
    TextBoxWidgetExt tleRaANode = new TextBoxWidgetExt();
    TextBoxWidgetExt tlePerigee = new TextBoxWidgetExt();
    TextBoxWidgetExt tleE = new TextBoxWidgetExt();
    TextBoxWidgetExt tleMeanAnomaly = new TextBoxWidgetExt();
    TextBoxWidgetExt tleMeanMotion = new TextBoxWidgetExt();
    TextBoxWidgetExt tleBStar = new TextBoxWidgetExt();

    // Widgets for radial velocity frame
    JLabel velLabel = new JLabel();

    DropDownListBoxWidgetExt velDefn = new DropDownListBoxWidgetExt();
    TextBoxWidgetExt velValue = new TextBoxWidgetExt();
    JLabel velFrameLabel = new JLabel();
    DropDownListBoxWidgetExt velFrame = new DropDownListBoxWidgetExt();
    JLabel baseXOffLabel = new JLabel();
    JLabel baseYOffLabel = new JLabel();
    TextBoxWidgetExt baseXOff = new TextBoxWidgetExt();
    TextBoxWidgetExt baseYOff = new TextBoxWidgetExt();
    JLabel baseXOffUnits = new JLabel();
    JLabel baseYOffUnits = new JLabel();
    CommandButtonWidgetExt resolveOrbitalElementButton =
            new CommandButtonWidgetExt();
    JLabel orbitalElementResolvedNameLabel = new JLabel();
    // Make this panel public so that ot.editor.SurveyGUI can see it.
    public JPanel XYOffsetPanel = new JPanel();

    public TelescopeGUI() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        border1 = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Object",
                TitledBorder.LEFT, TitledBorder.CENTER,
                new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
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
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Name ");
        jLabel1.setLabelFor(nameTBW);
        targetType.setFont(new java.awt.Font("Dialog", 0, 12));
        targetType.setForeground(Color.black);
        targetType.setText("    TargetType ");
        targetType.setLabelFor(targetTypeDDList);
        nameResolversDDLBW.setFont(new java.awt.Font("Dialog", 0, 10)); // MFO
        resolveButton.setText("Resolve Name"); // MFO
        resolvedName.setFont(new java.awt.Font("Dialog", 0, 12)); // SDW
        resolvedName.setText("Resolved Name: ");
        resolvedName.setForeground(Color.gray);
        setBaseButton.setMargin(new Insets(2, 2, 2, 2));
        setBaseButton.setText("Set Base From Image");
        newLabel.setForeground(Color.black);
        newLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        newLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        newButton.setForeground(Color.black);
        newButton.setFont(new java.awt.Font("Dialog", 0, 12));
        plotButton.setMargin(new Insets(2, 2, 2, 2));
        plotButton.setText("Plot...");
        removeButton.setMargin(new Insets(2, 2, 2, 2));
        removeButton.setText("Remove");
        broadcastButton.setMargin(new Insets(2, 2, 2, 2));
        broadcastButton.setText("Broadcast");
        this.setPreferredSize(new Dimension(360, 400));
        targetTypeDDList.setFont(new java.awt.Font("Dialog", 0, 12));
        systemDDLBW.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel16.setText("Dec");
        jLabel16.setForeground(Color.black);
        jLabel16.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel15.setText("<html>(\u03bc<sub>\u03b4</sub>, milli-arcsec/year)</html>");
        jLabel15.setForeground(Color.black);
        jLabel15.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel14.setForeground(Color.black);
        jLabel14.setText("RA");
        detailsPW.setLayout(gridBagLayout3);
        jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel13.setForeground(Color.black);
        jLabel13.setText("<html>(\u03bc<sub>\u03b1</sub>, milli-arcsec/year)</html>");
        jLabel12.setText("(milli-arcsec)");
        jLabel12.setForeground(Color.black);
        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel9.setText("Epoch");
        jLabel9.setForeground(Color.black);
        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setText("Parallax");
        jLabel8.setForeground(Color.black);
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
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
        namedSystemPanel.setLayout(gridBagLayout6);
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
        nameTBW.setColumns(15);
        nameTagPanel.setLayout(new BoxLayout(nameTagPanel, BoxLayout.X_AXIS));
        conicSystemType.setFont(new java.awt.Font("Dialog", 0, 12));
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
        detailsEpochTBW.setColumns(8);
        l_or_m.setColumns(6);
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

        baseXOffLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        baseXOffLabel.setForeground(Color.black);
        baseXOffLabel.setText("xOff");

        baseYOffLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        baseYOffLabel.setForeground(Color.black);
        baseYOffLabel.setText("yOff");

        baseXOff.setColumns(5);
        baseYOff.setColumns(5);

        baseXOffUnits.setFont(new java.awt.Font("Dialog", 2, 10));
        baseXOffUnits.setForeground(Color.black);
        baseXOffUnits.setText("arcsecs");

        baseYOffUnits.setFont(new java.awt.Font("Dialog", 2, 10));
        baseYOffUnits.setForeground(Color.black);
        baseYOffUnits.setText("arcsecs");

        XYOffsetPanel.setLayout(new GridLayout(2, 4));
        XYOffsetPanel.add(new JPanel());
        XYOffsetPanel.add(baseXOffLabel);
        XYOffsetPanel.add(baseXOff);
        XYOffsetPanel.add(baseXOffUnits);
        XYOffsetPanel.add(new JPanel());
        XYOffsetPanel.add(baseYOffLabel);
        XYOffsetPanel.add(baseYOff);
        XYOffsetPanel.add(baseYOffUnits);

        epochPerih.setVisible(false);
        epochPerihLabel.setVisible(false);
        epochPerihUnitsLabel.setVisible(false);

        orbitalElementResolvedNameLabel.setFont(new java.awt.Font("Dialog",
                Font.PLAIN, 10));
        orbitalElementResolvedNameLabel.setForeground(Color.black);
        orbitalElementResolvedNameLabel.setText("");
        orbitalElementResolvedNameLabel.setVisible(true);
        resolveOrbitalElementButton.setText("Resolve Name");
        resolveOrbitalElementButton.setVisible(true);

        this.add(nameTagPanel, new GridBagConstraints(
                0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        nameTagPanel.add(jLabel1);
        nameTagPanel.add(nameTBW);
        nameTagPanel.add(targetType);
        nameTagPanel.add(targetTypeDDList);
        nameTagPanel.validate();

        this.add(targetSystemsTabbedPane, new GridBagConstraints(
                0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        objectGBW.add(nameResolversDDLBW, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 0), 0, 0)); // MFO
        objectGBW.add(resolveButton, new GridBagConstraints(
                2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 5, 0), 0, 0)); // MFO
        objectGBW.add(resolvedName, new GridBagConstraints(
                3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 5, 0), 0, 0)); // SDW

        objectGBW.add(xaxisTBW, new GridBagConstraints(
                3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 0), 0, 0));

        this.add(XYOffsetPanel, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 5, 5, 0), 0, 0));

        objectGBW.add(Dec_El_STW, new GridBagConstraints(
                2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        objectGBW.add(RA_Az_STW, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        objectGBW.add(jLabel3, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        objectGBW.add(systemDDLBW, new GridBagConstraints(
                0, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 0), 0, 0));

        objectGBW.add(yaxisTBW, new GridBagConstraints(
                3, 2, 1, 1, 1.5, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 0), 0, 0));

        objectGBW.add(offsetCheckBox, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        objectGBW.add(xUnitsLabel, new GridBagConstraints(
                4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        objectGBW.add(yUnitsLabel, new GridBagConstraints(
                4, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(extrasFolder, new GridBagConstraints(
                0, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        // Target Types Tabbed Pane, added by MFO (22 February 2002)
        targetSystemsTabbedPane.add(objectGBW, "RA/Dec");
        targetSystemsTabbedPane.add(conicSystemPanel, "Orbital Elements");

        conicSystemPanel.add(orbitalElementResolvedNameLabel,
                new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(resolveOrbitalElementButton,
                new GridBagConstraints(7, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.SOUTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5), 0, 0));

        conicSystemPanel.add(epoch, new GridBagConstraints(
                4, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 0), 0, 0));
        conicSystemPanel.add(orbinc, new GridBagConstraints(
                4, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 0), 0, 0));
        conicSystemPanel.add(anode, new GridBagConstraints(
                4, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 0), 0, 0));
        conicSystemPanel.add(epochLabel, new GridBagConstraints(
                3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(orbincLabel, new GridBagConstraints(
                3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(anodeLabel, new GridBagConstraints(
                3, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(perihLabel, new GridBagConstraints(
                6, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(aorqLabel, new GridBagConstraints(
                6, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(eLabel, new GridBagConstraints(
                6, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(perih, new GridBagConstraints(
                7, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 0), 0, 0));
        conicSystemPanel.add(aorq, new GridBagConstraints(
                7, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 0), 0, 0));
        conicSystemPanel.add(e, new GridBagConstraints(
                7, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 0), 0, 0));
        conicSystemPanel.add(jLabel20, new GridBagConstraints(
                5, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 15), 0, 0));
        conicSystemPanel.add(jLabel22, new GridBagConstraints(
                5, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(jLabel23, new GridBagConstraints(
                5, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(jLabel24, new GridBagConstraints(
                8, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(jLabel25, new GridBagConstraints(
                8, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(conicSystemType, new GridBagConstraints(
                0, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 5), 0, 0));
        conicSystemPanel.add(l_or_m, new GridBagConstraints(
                1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 0), 0, 0));
        conicSystemPanel.add(dm, new GridBagConstraints(
                1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 0), 0, 0));
        conicSystemPanel.add(l_or_mLabel, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(dmLabel, new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(l_or_mUnitsLabel, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 15), 0, 0));
        conicSystemPanel.add(dmUnitsLabel, new GridBagConstraints(
                2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        conicSystemPanel.add(epochPerih, new GridBagConstraints(
                1, 3, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 0), 0, 0));
        conicSystemPanel.add(epochPerihLabel, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        conicSystemPanel.add(epochPerihUnitsLabel, new GridBagConstraints(
                2, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 15), 0, 0));

        targetSystemsTabbedPane.add(namedSystemPanel, "Named Planets");
        namedSystemPanel.add(namedTarget, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));

        // TLE System Panel
        tleSystemPanel.setLayout(new GridBagLayout());

        tleSystemPanel.add(GuiUtil.createLabel("Epoch year"),
                new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(tleEpochYear, new GridBagConstraints(
                1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel("Epoch day"),
                new GridBagConstraints(
                        3, 0, 1, 1, 0.0, 1.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(tleEpochDay, new GridBagConstraints(
                4, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel("BSTAR"),new GridBagConstraints(
                6, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(tleBStar, new GridBagConstraints(
                7, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel(
                        "<html>(R<sub>&oplus;</sub><sup>-1</sup>)</html>"),
                new GridBagConstraints(8, 0, 1, 1, 0.0, 1.0,
                        GridBagConstraints.WEST,
                        GridBagConstraints.NONE,
                        new Insets(5, 0, 5, 5), 0, 0));

        tleSystemPanel.add(GuiUtil.createLabel("Inclination"),
                new GridBagConstraints(
                        0, 1, 1, 1, 0.0, 1.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(tleInclination, new GridBagConstraints(
                1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel("(deg)"),
                new GridBagConstraints(
                        2, 1, 1, 1, 0.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 0, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel("RA A. Node"),
                new GridBagConstraints(
                        3, 1, 1, 1, 0.0, 1.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(tleRaANode, new GridBagConstraints(
                4, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel("(deg)"),
                new GridBagConstraints(
                        5, 1, 1, 1, 0.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 0, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel("e"), new GridBagConstraints(
                6, 1, 1, 1, 0.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(tleE, new GridBagConstraints(
                7, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        tleSystemPanel.add(GuiUtil.createLabel("Perigee"),
                new GridBagConstraints(
                        0, 2, 1, 1, 0.0, 1.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(tlePerigee, new GridBagConstraints(
                1, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel("(deg)"),
                new GridBagConstraints(
                        2, 2, 1, 1, 0.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 0, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel("Mn. anom."),
                new GridBagConstraints(
                        3, 2, 1, 1, 0.0, 1.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(tleMeanAnomaly, new GridBagConstraints(
                4, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel("(deg)"),
                new GridBagConstraints(
                        5, 2, 1, 1, 0.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 0, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel("Mn. mot."),
                new GridBagConstraints(
                        6, 2, 1, 1, 0.0, 1.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(tleMeanMotion, new GridBagConstraints(
                7, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        tleSystemPanel.add(GuiUtil.createLabel("(rv/d)"),
                new GridBagConstraints(
                        8, 2, 1, 1, 0.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 0, 5, 5), 0, 0));

        targetSystemsTabbedPane.add(tleSystemPanel, "TLE");

        for (int i = 0; i < targetSystemsTabbedPane.getTabCount(); i++) {
            targetSystemsTabbedPane.setEnabledAt(i, false);
            Component[] component = ((JPanel)
                    targetSystemsTabbedPane.getComponentAt(i)).getComponents();

            for (int j = 0; j < component.length; j++) {
                component[j].setEnabled(false);
            }
        }

        // chop mode added by MFO (3 August 2001)
        chopPW.add(jLabel5, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 5), 0, 0));
        chopPW.add(chopThrow, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        chopPW.add(jLabel18, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        chopPW.add(chopping, new GridBagConstraints(
                0, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        chopPW.add(jLabel4, new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        chopPW.add(jLabel19, new GridBagConstraints(
                2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        chopPW.add(chopAngle, new GridBagConstraints(
                1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        chopPW.add(chopSystem, new GridBagConstraints(
                1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 0, 0));
        chopPW.add(chopSystemLabel, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        // Proper Motion tab
        propMotionPW.add(jLabel14, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 5), 0, 0));
        propMotionPW.add(propMotionRATBW, new GridBagConstraints(
                1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 10, 5), 80, 0));
        propMotionPW.add(jLabel16, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 5), 0, 0));
        propMotionPW.add(propMotionDecTBW, new GridBagConstraints(
                1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 10, 5), 80, 0));
        propMotionPW.add(jLabel13, new GridBagConstraints(
                2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 5, 10, 20), 60, 0));
        propMotionPW.add(jLabel15, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 5, 10, 20), 60, 0));
        propMotionPW.add(jLabel9, new GridBagConstraints(
                3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 20, 10, 5), 0, 0));
        propMotionPW.add(detailsEpochTBW, new GridBagConstraints(
                4, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 10, 5), 80, 0));
        propMotionPW.add(jLabel8, new GridBagConstraints(
                3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(10, 20, 10, 5), 0, 0));
        propMotionPW.add(detailsParallaxTBW, new GridBagConstraints(
                4, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 10, 5), 80, 0));
        propMotionPW.add(jLabel12, new GridBagConstraints(
                5, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 5, 10, 10), 0, 0));

        // Radial Velocity tab
        // This looks like what the velocity panel in the het compt.
        velLabel.setText("Velocity (km/s or redshift)");
        velLabel.setForeground(Color.black);
        velLabel.setFont(new java.awt.Font("Dialog", 0, 12));

        velDefn.setForeground(Color.black);
        velDefn.setFont(new java.awt.Font("Dialog", 0, 12));

        velFrameLabel.setText("Frame");
        velFrameLabel.setForeground(Color.black);
        velFrameLabel.setFont(new java.awt.Font("Dialog", 0, 12));

        velValue.setColumns(20);

        velFrame.setForeground(Color.black);
        velFrame.setFont(new java.awt.Font("Dialog", 0, 12));

        detailsPW.add(velLabel, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 5, 10, 5), 0, 0));
        detailsPW.add(velDefn, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 5, 10, 5), 0, 0));
        detailsPW.add(velValue, new GridBagConstraints(
                2, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 10, 50), 0, 0));
        detailsPW.add(velFrameLabel, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 5, 10, 5), 0, 0));
        detailsPW.add(velFrame, new GridBagConstraints(
                1, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 5, 10, 20), 0, 0));

        if (OtCfg.telescopeUtil.supports(
                TelescopeUtil.FEATURE_TARGET_INFO_TRACKING)) {
            extrasFolder.add(detailsPW, TAB_RADIAL_VELOCITY);
            extrasFolder.add(propMotionPW, TAB_PROPER_MOTION);

        } else {
            extrasFolder.add(propMotionPW, TAB_PROPER_MOTION);
            extrasFolder.add(detailsPW, TAB_RADIAL_VELOCITY);
        }

        extrasFolder.add(chopPW, TAB_CHOP_SETTINGS);

        this.add(jScrollPane1, new GridBagConstraints(
                0, 3, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 0, 5, 0), 0, 0));

        jScrollPane1.getViewport().add(positionTable, null);
        gridLayout1.setHgap(25);
        buttonPanel.setLayout(gridLayout1);
        setBaseButton.setToolTipText(setBaseButton.getText());

        this.add(buttonPanel, new GridBagConstraints(
                0, 4, 2, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        buttonPanel.add(plotButton, null);
        buttonPanel.add(setBaseButton, null);
        buttonPanel.add(removeButton, null);
        buttonPanel.add(broadcastButton, null);
        buttonPanel.add(newLabel, null);
        buttonPanel.add(newButton, null);
    }
}
