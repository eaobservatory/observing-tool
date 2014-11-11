/*
 * Copyright (c) 2000 Association of Universities for Research in Astronomy,
 * Inc. (AURA)
 *
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

package ot.ukirt.iter.editor;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;

@SuppressWarnings("serial")
public class IterWFCAMCalObsGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel cooaddsLabel = new JLabel();
    TextBoxWidgetExt Coadds = new TextBoxWidgetExt();
    TextBoxWidgetExt ExpTime = new TextBoxWidgetExt();
    JLabel expTimeLabel = new JLabel();
    JLabel typeLabel = new JLabel();
    JLabel filterLabel = new JLabel();
    DropDownListBoxWidgetExt ReadMode = new DropDownListBoxWidgetExt();
    DropDownListBoxWidgetExt Filter = new DropDownListBoxWidgetExt();
    CommandButtonWidgetExt useDefaults = new CommandButtonWidgetExt();
    JLabel observeLabel = new JLabel();
    DropDownListBoxWidgetExt repeatComboBox = new DropDownListBoxWidgetExt();
    JLabel XLabel = new JLabel();
    JLabel secLabel = new JLabel();
    DropDownListBoxWidgetExt CalType = new DropDownListBoxWidgetExt();
    JLabel focusLabel = new JLabel();
    TextBoxWidgetExt focusPos = new TextBoxWidgetExt();
    JLabel readModeLabel = new JLabel();
    JLabel focusTelStepsLabel = new JLabel();
    TextBoxWidgetExt focusTelSteps = new TextBoxWidgetExt();

    public IterWFCAMCalObsGUI() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
        this.setMinimumSize(new Dimension(369, 300));
        this.setPreferredSize(new Dimension(369, 300));

        cooaddsLabel.setText("Coadds");
        cooaddsLabel.setToolTipText("");
        cooaddsLabel.setForeground(Color.black);
        cooaddsLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        Coadds.setBorder(BorderFactory.createLoweredBevelBorder());
        Coadds.setHorizontalAlignment(SwingConstants.CENTER);
        Coadds.setBackground(Color.WHITE);
        ExpTime.setHorizontalAlignment(SwingConstants.CENTER);
        ExpTime.setBorder(BorderFactory.createLoweredBevelBorder());
        ExpTime.setEditable(true);
        ExpTime.setBackground(Color.WHITE);
        focusPos.setHorizontalAlignment(SwingConstants.CENTER);
        focusPos.setBorder(BorderFactory.createLoweredBevelBorder());
        focusPos.setEditable(true);
        focusPos.setBackground(Color.WHITE);
        expTimeLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        expTimeLabel.setForeground(Color.black);
        expTimeLabel.setRequestFocusEnabled(true);
        expTimeLabel.setText("Exp time");
        focusLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        focusLabel.setForeground(Color.black);
        focusLabel.setRequestFocusEnabled(true);
        focusLabel.setText("Focus Position");
        typeLabel.setText("Type");
        typeLabel.setForeground(Color.black);
        typeLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        filterLabel.setText("Filter");
        filterLabel.setToolTipText("");
        filterLabel.setForeground(Color.black);
        filterLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        ReadMode.setPreferredSize(new Dimension(50, 26));
        ReadMode.setBackground(Color.white);
        ReadMode.setFont(new java.awt.Font("Dialog", 0, 12));
        ReadMode.setAlignmentX((float) 0.0);
        ReadMode.setAutoscrolls(false);
        Filter.setAlignmentX((float) 0.0);
        Filter.setFont(new java.awt.Font("Dialog", 0, 12));
        Filter.setBackground(Color.white);
        Filter.setAutoscrolls(true);
        Filter.setPreferredSize(new Dimension(50, 26));
        useDefaults.setFont(new java.awt.Font("Dialog", 0, 14));
        useDefaults.setBorder(BorderFactory.createRaisedBevelBorder());
        useDefaults.setText("Use defaults");
        observeLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        observeLabel.setForeground(Color.black);
        observeLabel.setText("Observe");
        repeatComboBox.setPreferredSize(new Dimension(50, 26));
        repeatComboBox.setAutoscrolls(true);
        repeatComboBox.setBackground(Color.white);
        XLabel.setFont(new java.awt.Font("Dialog", 2, 12));
        XLabel.setForeground(Color.black);
        XLabel.setText("X");
        secLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        secLabel.setForeground(Color.black);
        secLabel.setText("sec");
        CalType.setAutoscrolls(false);
        CalType.setAlignmentX((float) 0.0);
        CalType.setFont(new java.awt.Font("Dialog", 0, 12));
        CalType.setBackground(Color.white);
        CalType.setPreferredSize(new Dimension(50, 26));
        readModeLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        readModeLabel.setForeground(Color.black);
        readModeLabel.setText("Read Mode");
        focusTelStepsLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        focusTelStepsLabel.setForeground(Color.black);
        focusTelStepsLabel.setText("Step Size");
        focusTelSteps.setBorder(BorderFactory.createLoweredBevelBorder());
        focusTelSteps.setHorizontalAlignment(SwingConstants.CENTER);
        focusTelSteps.setBackground(Color.WHITE);

        this.add(typeLabel, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 20, 0), 0, 0));
        this.add(CalType, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 20, 10), 30, 0));
        this.add(readModeLabel, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 10, 0), 0, 0));
        this.add(ReadMode, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 10, 0), 60, 0));
        this.add(filterLabel, new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 30, 10, 0), 0, 0));
        this.add(Filter, new GridBagConstraints(
                1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 10, 0), 60, 0));
        this.add(focusLabel, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 10, 10, 0), 0, 0));
        this.add(focusPos, new GridBagConstraints(
                1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 10, 10), 70, 0));
        this.add(focusTelStepsLabel, new GridBagConstraints(
                0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 10, 10, 0), 0, 0));
        this.add(focusTelSteps, new GridBagConstraints(
                1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 10, 10), 70, 0));
        this.add(expTimeLabel, new GridBagConstraints(
                0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(20, 10, 10, 0), 0, 0));
        this.add(ExpTime, new GridBagConstraints(
                1, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(20, 10, 10, 10), 70, 0));
        this.add(secLabel, new GridBagConstraints(
                2, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(20, 0, 0, 0), 0, 0));
        this.add(cooaddsLabel, new GridBagConstraints(
                0, 6, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 30, 0, 0), 0, 0));
        this.add(Coadds, new GridBagConstraints(
                1, 6, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 20, 10, 20), 0, 0));
        this.add(observeLabel, new GridBagConstraints(
                0, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(20, 0, 0, -20), 0, 0));
        this.add(repeatComboBox, new GridBagConstraints(
                1, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(20, 30, 0, 20), 0, 0));
        this.add(XLabel, new GridBagConstraints(
                2, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(20, -10, 0, 0), 20, 0));
        this.add(useDefaults, new GridBagConstraints(
                0, 8, 4, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(20, 0, 0, 0), 60, 0));
    }
}
