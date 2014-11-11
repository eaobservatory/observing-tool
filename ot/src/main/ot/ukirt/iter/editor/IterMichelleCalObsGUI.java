/*
 * Copyright (c) 2000 Association of Universities for Research in Astronomy,
 * Inc. (AURA)
 *
 * @author Allan Brighton (modified for Michelle/UKIRT by M.Folger@roe.ac.uk)
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
import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;

@SuppressWarnings("serial")
public class IterMichelleCalObsGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    DropDownListBoxWidgetExt repeatComboBox = new DropDownListBoxWidgetExt();
    TextBoxWidgetExt observationTime = new TextBoxWidgetExt();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JLabel jLabel6 = new JLabel();
    TextBoxWidgetExt exposureTime = new TextBoxWidgetExt();
    JLabel jLabel5 = new JLabel();
    JLabel jLabel4 = new JLabel();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel9 = new JLabel();
    DropDownListBoxWidgetExt calType = new DropDownListBoxWidgetExt();
    JLabel jLabel13 = new JLabel();
    JLabel jLabel12 = new JLabel();
    JLabel jLabel11 = new JLabel();
    JPanel arcPanel = new JPanel();
    JPanel emptyPanel = new JPanel();
    JPanel calTypesPanel = new JPanel();
    CardLayout cardLayout1 = new CardLayout();
    DropDownListBoxWidgetExt flat_sampling = new DropDownListBoxWidgetExt();
    TextBoxWidgetExt cvfWavelength = new TextBoxWidgetExt();
    JPanel flatPanel = new JPanel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    DropDownListBoxWidgetExt flat_source = new DropDownListBoxWidgetExt();
    JLabel jLabel7 = new JLabel();
    CommandButtonWidgetExt useDefaults = new CommandButtonWidgetExt();

    public IterMichelleCalObsGUI() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        jLabel1.setText("Observation Time");
        jLabel1.setForeground(Color.black);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("(exp / obs)");
        jLabel2.setForeground(Color.black);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel3.setText("X");
        jLabel3.setForeground(Color.black);
        jLabel3.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel4.setText("Observe");
        jLabel4.setForeground(Color.black);
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setText("(sec)");
        jLabel5.setForeground(Color.black);
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 10));
        exposureTime.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel6.setText("Exposure Time");
        jLabel6.setForeground(Color.black);
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        observationTime.setBorder(BorderFactory.createLoweredBevelBorder());
        repeatComboBox.setPreferredSize(new Dimension(50, 26));
        repeatComboBox.setAutoscrolls(true);
        this.setMinimumSize(new Dimension(280, 206));
        this.setPreferredSize(new Dimension(280, 206));
        this.setLayout(gridBagLayout1);
        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel9.setForeground(Color.black);
        jLabel9.setText("Calibration Type");
        jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel13.setForeground(Color.black);
        jLabel13.setText("CVF Wavelength");
        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel12.setForeground(Color.black);
        jLabel12.setText("(microns)");
        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel11.setForeground(Color.black);
        jLabel11.setText("Flat Sampling");
        calTypesPanel.setLayout(cardLayout1);
        cvfWavelength.setColumns(12);
        calTypesPanel.setBorder(BorderFactory.createEtchedBorder());
        flatPanel.setLayout(gridBagLayout3);
        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel7.setForeground(Color.black);
        jLabel7.setText("Flat Source");
        useDefaults.setText("Use Defaults");
        this.add(repeatComboBox, new GridBagConstraints(
                1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(observationTime, new GridBagConstraints(
                1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 5), 0, 0));
        this.add(jLabel6, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(exposureTime, new GridBagConstraints(
                1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel5, new GridBagConstraints(
                2, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel4, new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel3, new GridBagConstraints(
                2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel2, new GridBagConstraints(
                2, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel1, new GridBagConstraints(
                0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel9, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(calType, new GridBagConstraints(
                1, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(calTypesPanel, new GridBagConstraints(
                0, 1, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        calTypesPanel.add(flatPanel, "FLAT");
        flatPanel.add(jLabel11, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 5), 0, 0));
        flatPanel.add(flat_sampling, new GridBagConstraints(
                1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 0), 0, 0));
        flatPanel.add(flat_source, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 0), 0, 0));
        flatPanel.add(jLabel7, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 5), 0, 0));
        calTypesPanel.add(arcPanel, "ARC");
        arcPanel.add(jLabel13, null);
        arcPanel.add(cvfWavelength, null);
        arcPanel.add(jLabel12, null);
        calTypesPanel.add(emptyPanel, "EMPTY");
        this.add(useDefaults, new GridBagConstraints(
                0, 5, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 5), 0, 0));
    }
}
