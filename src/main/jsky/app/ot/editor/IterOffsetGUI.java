/*
 * Copyright: Copyright (c) 2000 Association of Universities for Research in
 *            Astronomy, Inc. (AURA)
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
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;

@SuppressWarnings("serial")
public class IterOffsetGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel pqItem = new JLabel();
    JLabel jLabel1 = new JLabel();
    TextBoxWidgetExt titleTBW = new TextBoxWidgetExt();
    JLabel offSTW = new JLabel();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel4 = new JLabel();
    TextBoxWidgetExt xOffset = new TextBoxWidgetExt();
    JLabel jLabel5 = new JLabel();
    TextBoxWidgetExt yOffset = new TextBoxWidgetExt();
    CommandButtonWidgetExt newButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt removeAllButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt removeButton = new CommandButtonWidgetExt();
    JScrollPane jScrollPane1 = new JScrollPane();
    OffsetPosTableWidget offsetTable = new OffsetPosTableWidget();
    CommandButtonWidgetExt topButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt upButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt downButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt bottomButton = new CommandButtonWidgetExt();
    JPanel gridGBW = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    TitledBorder titledBorder1;
    JLabel jLabel6 = new JLabel();
    JLabel jLabel7 = new JLabel();
    JLabel jLabel8 = new JLabel();
    JLabel jLabel9 = new JLabel();
    JLabel jLabel10 = new JLabel();
    JLabel jLabel11 = new JLabel();
    JLabel jLabel12 = new JLabel();
    TextBoxWidgetExt gridXOffset = new TextBoxWidgetExt();
    TextBoxWidgetExt gridYOffset = new TextBoxWidgetExt();
    JLabel jLabel13 = new JLabel();
    JLabel jLabel14 = new JLabel();
    TextBoxWidgetExt gridXSpacing = new TextBoxWidgetExt();
    TextBoxWidgetExt gridYSpacing = new TextBoxWidgetExt();
    JLabel jLabel15 = new JLabel();
    JLabel jLabel16 = new JLabel();
    TextBoxWidgetExt gridRows = new TextBoxWidgetExt();
    TextBoxWidgetExt gridCols = new TextBoxWidgetExt();
    CommandButtonWidgetExt createGridButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt centreOnBaseButton = new CommandButtonWidgetExt();
    TextBoxWidgetExt paTextBox = new TextBoxWidgetExt();
    JLabel paLabel = new JLabel();
    DropDownListBoxWidgetExt coordSys = new DropDownListBoxWidgetExt();
    JLabel sysLabel = new JLabel();
    CommandButtonWidgetExt displayRotatedOffsets = new CommandButtonWidgetExt();
    CommandButtonWidgetExt setSpacingButton = new CommandButtonWidgetExt();
    JRadioButton overwrite = new JRadioButton("Overwrite");
    JRadioButton append = new JRadioButton("Append");
    ButtonGroup buttonGroup = new ButtonGroup();

    public IterOffsetGUI() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,
                Color.white, new Color(142, 142, 142)), "Grid Pattern");
        this.setLayout(gridBagLayout1);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Title");
        offSTW.setFont(new java.awt.Font("Dialog", 3, 12));
        offSTW.setForeground(Color.black);
        offSTW.setText("Offset");
        jLabel3.setFont(new java.awt.Font("Dialog", 2, 10));
        jLabel3.setForeground(Color.black);
        jLabel3.setText("(arcsec)");
        jLabel4.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel4.setForeground(Color.black);
        jLabel4.setText("p");
        jLabel5.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel5.setForeground(Color.black);
        jLabel5.setText("q");
        newButton.setFont(new java.awt.Font("Dialog", 0, 12));
        newButton.setText("New");
        removeAllButton.setFont(new java.awt.Font("Dialog", 0, 12));
        removeAllButton.setText("Rm. All");
        removeButton.setFont(new java.awt.Font("Dialog", 0, 12));
        removeButton.setText("Remove");
        gridGBW.setLayout(gridBagLayout2);
        gridGBW.setBorder(titledBorder1);
        jLabel6.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel6.setForeground(Color.black);
        jLabel6.setText("Dimensions");
        jLabel7.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel7.setForeground(Color.black);
        jLabel7.setText("Spacing");
        jLabel8.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel8.setForeground(Color.black);
        jLabel8.setText("Initial Offset");
        jLabel9.setText("(arcsec)");
        jLabel9.setForeground(Color.black);
        jLabel9.setFont(new java.awt.Font("Dialog", 2, 10));
        jLabel10.setText("(arcsec)");
        jLabel10.setForeground(Color.black);
        jLabel10.setFont(new java.awt.Font("Dialog", 2, 10));
        jLabel11.setText("p");
        jLabel11.setForeground(Color.black);
        jLabel11.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel12.setText("q");
        jLabel12.setForeground(Color.black);
        jLabel12.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel13.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel13.setForeground(Color.black);
        jLabel13.setText("p");
        jLabel14.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel14.setForeground(Color.black);
        jLabel14.setText("q");
        jLabel15.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel15.setForeground(Color.black);
        jLabel15.setText("Rows");
        jLabel16.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel16.setForeground(Color.black);
        jLabel16.setText("Cols");
        createGridButton.setFont(new java.awt.Font("Dialog", 0, 12));
        createGridButton.setText("Create");
        this.setPreferredSize(new Dimension(400, 415));
        gridXOffset.setHorizontalAlignment(SwingConstants.RIGHT);
        gridXOffset.setText("0");
        gridYOffset.setHorizontalAlignment(SwingConstants.RIGHT);
        gridYOffset.setText("0");
        gridXSpacing.setHorizontalAlignment(SwingConstants.RIGHT);
        gridXSpacing.setText("60");
        gridYSpacing.setHorizontalAlignment(SwingConstants.RIGHT);
        gridYSpacing.setText("60");
        gridRows.setHorizontalAlignment(SwingConstants.RIGHT);
        gridRows.setText("2");
        gridCols.setHorizontalAlignment(SwingConstants.RIGHT);
        gridCols.setText("2");
        jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
        topButton.setPreferredSize(new Dimension(20, 20));
        upButton.setPreferredSize(new Dimension(20, 20));
        downButton.setPreferredSize(new Dimension(20, 20));
        bottomButton.setPreferredSize(new Dimension(20, 20));
        centreOnBaseButton.setText("Create/Centre On Base");
        paLabel.setFont(new java.awt.Font("Dialog", 2, 12));
        paLabel.setForeground(Color.black);
        paLabel.setText("PA");
        coordSys.setFont(new java.awt.Font("Dialog", 0, 12));
        coordSys.setForeground(Color.black);
        sysLabel.setFont(new java.awt.Font("Dialog", 2, 12));
        sysLabel.setForeground(Color.black);
        sysLabel.setText("System");
        displayRotatedOffsets.setText("Display Derotated Offsets");
        setSpacingButton.setText("Set Spacing from Scan Area");

        buttonGroup.add(overwrite);
        buttonGroup.add(append);
        overwrite.setSelected(true);

        this.add(pqItem, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel1, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 10, 5, 5, 5), 0, 0));
        this.add(titleTBW, new GridBagConstraints(
                2, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 0, 15), 0, 0));
        this.add(offSTW, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 5), 0, 0));
        this.add(jLabel3, new GridBagConstraints(
                1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel4, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(xOffset, new GridBagConstraints(
                1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel5, new GridBagConstraints(
                0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(yOffset, new GridBagConstraints(
                1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(newButton, new GridBagConstraints(
                1, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 0, 2, 0), 0, 0));
        this.add(removeAllButton, new GridBagConstraints(
                1, 6, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 0, 2, 0), 0, 0));
        this.add(removeButton, new GridBagConstraints(
                1, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 0, 2, 0), 0, 0));
        this.add(jScrollPane1, new GridBagConstraints(
                2, 2, 1, 7, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(topButton, new GridBagConstraints(
                3, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(upButton, new GridBagConstraints(
                3, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(downButton, new GridBagConstraints(
                3, 6, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(gridGBW, new GridBagConstraints(
                0, 10, 4, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        gridGBW.add(jLabel7, new GridBagConstraints(
                3, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 15), 0, 0));
        gridGBW.add(jLabel6, new GridBagConstraints(
                5, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 0), 0, 0));
        gridGBW.add(jLabel8, new GridBagConstraints(
                1, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 20), 0, 0));
        gridGBW.add(jLabel9, new GridBagConstraints(
                4, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        gridGBW.add(jLabel10, new GridBagConstraints(
                2, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        gridGBW.add(jLabel11, new GridBagConstraints(
                1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 25, 5, 5), 0, 0));
        gridGBW.add(jLabel12, new GridBagConstraints(
                1, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        gridGBW.add(gridXOffset, new GridBagConstraints(
                2, 4, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        gridGBW.add(gridYOffset, new GridBagConstraints(
                2, 5, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        gridGBW.add(jLabel13, new GridBagConstraints(
                3, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 25, 5, 5), 0, 0));
        gridGBW.add(jLabel14, new GridBagConstraints(
                3, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        gridGBW.add(gridXSpacing, new GridBagConstraints(
                4, 4, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        gridGBW.add(gridYSpacing, new GridBagConstraints(
                4, 5, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        gridGBW.add(jLabel15, new GridBagConstraints(
                5, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 25, 5, 5), 0, 0));
        gridGBW.add(jLabel16, new GridBagConstraints(
                5, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        gridGBW.add(gridRows, new GridBagConstraints(
                6, 4, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        gridGBW.add(gridCols, new GridBagConstraints(
                6, 5, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        gridGBW.add(createGridButton, new GridBagConstraints(
                2, 7, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 0, 5, 5), 0, 0));
        gridGBW.add(centreOnBaseButton, new GridBagConstraints(
                2, 6, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        gridGBW.add(setSpacingButton, new GridBagConstraints(
                5, 6, 5, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 0, 5, 5), 0, 0));

        gridGBW.add(append, new GridBagConstraints(
                3, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        gridGBW.add(overwrite, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        this.add(bottomButton, new GridBagConstraints(
                3, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(paTextBox, new GridBagConstraints(
                1, 8, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(paLabel, new GridBagConstraints(
                0, 8, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(coordSys, new GridBagConstraints(
                1, 9, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(sysLabel, new GridBagConstraints(
                0, 9, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(displayRotatedOffsets, new GridBagConstraints(
                2, 9, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jScrollPane1.getViewport().add(offsetTable, null);
    }
}
