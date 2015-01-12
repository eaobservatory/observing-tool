/*
 * Copyright (c) 2000 Association of Universities for Research in Astronomy,
 * Inc. (AURA)
 *
 * @author Allan Brighton (modified for CGS4/UKIRT by M.Folger@roe.ac.uk)
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
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;

@SuppressWarnings("serial")
public class IterCGS4CalObsGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JComboBox repeatComboBox = new JComboBox();
    TextBoxWidgetExt coadds = new TextBoxWidgetExt();
    TextBoxWidgetExt cvfWavelength = new TextBoxWidgetExt();
    TextBoxWidgetExt flatCVFWavelength = new TextBoxWidgetExt();
    TextBoxWidgetExt flatCVFOffset = new TextBoxWidgetExt();
    TextBoxWidgetExt exposureTime = new TextBoxWidgetExt();
    JLabel jLabel1 = new JLabel(); // Coadds label
    JLabel jLabel2 = new JLabel(); // exp/obs label
    JLabel jLabel3 = new JLabel(); // multiplier label
    JLabel jLabel4 = new JLabel(); // Observe label
    JLabel jLabel5 = new JLabel(); // sec label
    JLabel jLabel6 = new JLabel(); // Exp. Time label
    JLabel jLabel7 = new JLabel(); // Acq. mode label
    JLabel jLabel8 = new JLabel(); // Filter label
    JLabel jLabel9 = new JLabel(); // Lamp label
    JLabel jLabel10 = new JLabel(); // Cal type label
    JLabel jLabel11 = new JLabel(); // Flat Sampling label
    JLabel jLabel12 = new JLabel(); // microns label
    JLabel jLabel13 = new JLabel(); // CVF wavelength label
    JLabel jLabel14 = new JLabel(); // CVF offset for flats
    JLabel jLabel15 = new JLabel(); // Clone of 13
    DropDownListBoxWidgetExt mode = new DropDownListBoxWidgetExt();
    DropDownListBoxWidgetExt filter = new DropDownListBoxWidgetExt();
    DropDownListBoxWidgetExt lamp = new DropDownListBoxWidgetExt();
    DropDownListBoxWidgetExt calType = new DropDownListBoxWidgetExt();
    DropDownListBoxWidgetExt flatSampling = new DropDownListBoxWidgetExt();
    JPanel calTypesPanel = new JPanel();
    JPanel flatPanel = new JPanel();
    JPanel arcPanel = new JPanel();
    JPanel emptyPanel = new JPanel();
    CardLayout cardLayout1 = new CardLayout();
    CommandButtonWidgetExt defaultValues = new CommandButtonWidgetExt();

    public IterCGS4CalObsGUI() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        jLabel1.setText("Coadds");
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
        coadds.setBorder(BorderFactory.createLoweredBevelBorder());
        flatCVFWavelength.setBorder(BorderFactory.createLoweredBevelBorder());
        flatCVFWavelength.setEditable(false);
        flatCVFOffset.setBorder(BorderFactory.createLoweredBevelBorder());
        flatCVFOffset.setEditable(false);
        jLabel6.setText("Exp. Time");
        jLabel6.setForeground(Color.black);
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        repeatComboBox.setPreferredSize(new Dimension(50, 26));
        repeatComboBox.setAutoscrolls(true);
        this.setMinimumSize(new Dimension(280, 206));
        this.setPreferredSize(new Dimension(280, 206));
        this.setLayout(gridBagLayout1);
        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel7.setForeground(Color.black);
        jLabel7.setText("Aquisition Mode");
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setForeground(Color.black);
        jLabel8.setText("Filter");
        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel9.setForeground(Color.black);
        jLabel9.setText("Lamp");
        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel10.setForeground(Color.black);
        jLabel10.setText("Calibration Type");
        defaultValues.setText("Default");
        calTypesPanel.setLayout(cardLayout1);
        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel11.setForeground(Color.black);
        jLabel11.setText("Flat Sampling");
        jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel13.setForeground(Color.black);
        jLabel13.setText("CVF Wavelength");
        jLabel15.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel15.setForeground(Color.black);
        jLabel15.setText("CVF Wavelength");
        cvfWavelength.setColumns(12);
        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel12.setForeground(Color.black);
        jLabel12.setText("(microns)");
        jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel14.setForeground(Color.black);
        jLabel14.setText("CVF Offset");

        this.add(repeatComboBox, new GridBagConstraints(
                2, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(coadds, new GridBagConstraints(
                2, 9, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel6, new GridBagConstraints(
                0, 8, 2, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(exposureTime, new GridBagConstraints(
                2, 8, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel5, new GridBagConstraints(
                3, 8, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel4, new GridBagConstraints(
                0, 7, 2, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel3, new GridBagConstraints(
                3, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel2, new GridBagConstraints(
                3, 9, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel1, new GridBagConstraints(
                0, 9, 2, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel7, new GridBagConstraints(
                0, 3, 2, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel8, new GridBagConstraints(
                0, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel9, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 0, 5, 5), 0, 0));
        this.add(mode, new GridBagConstraints(
                2, 3, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(filter, new GridBagConstraints(
                2, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(lamp, new GridBagConstraints(
                2, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel10, new GridBagConstraints(
                0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 5, 5, 5), 0, 0));
        this.add(calType, new GridBagConstraints(
                2, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(defaultValues, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 30), 0, 0));
        this.add(calTypesPanel, new GridBagConstraints(
                0, 10, 4, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        calTypesPanel.add(flatPanel, "FLAT");
        flatPanel.setLayout(new GridLayout(-1, 2));
        flatPanel.add(jLabel11);
        flatPanel.add(flatSampling);
        flatPanel.add(jLabel15);
        flatPanel.add(flatCVFWavelength);
        flatPanel.add(jLabel14);
        flatPanel.add(flatCVFOffset);
        calTypesPanel.add(arcPanel, "ARC");
        arcPanel.add(jLabel13, null);
        arcPanel.add(cvfWavelength, null);
        arcPanel.add(jLabel12, null);
        calTypesPanel.add(emptyPanel, "EMPTY");
    }
}
