/*
 * Copyright (c) 2000 Association of Universities for Research in Astronomy,
 * Inc. (AURA)
 *
 * @author Allan Brighton (modified for ukirt: Martin Folger)
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

package ot.ukirt.inst.editor;

import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;

@SuppressWarnings("serial")
public class Cgs4GUI extends JPanel {
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();
    DropDownListBoxWidgetExt mask = new DropDownListBoxWidgetExt();
    TextBoxWidgetExt posAngle = new TextBoxWidgetExt();
    JLabel jLabel4 = new JLabel();
    DropDownListBoxWidgetExt sourceMag = new DropDownListBoxWidgetExt();
    JLabel jLabel5 = new JLabel();
    DropDownListBoxWidgetExt acqMode = new DropDownListBoxWidgetExt();
    JLabel jLabel6 = new JLabel();
    TextBoxWidgetExt coadds = new TextBoxWidgetExt();
    JLabel jLabel10 = new JLabel();
    TextBoxWidgetExt scienceFOV = new TextBoxWidgetExt();
    JLabel jLabel11 = new JLabel();
    TitledBorder titledBorder1;
    JLabel jLabel13 = new JLabel();
    DropDownListBoxWidgetExt polariser = new DropDownListBoxWidgetExt();
    CommandButtonWidgetExt defaultAcquisition = new CommandButtonWidgetExt();
    JPanel jPanel2 = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    CheckBoxWidgetExt useND = new CheckBoxWidgetExt();
    TextBoxWidgetExt exposureTime = new TextBoxWidgetExt();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel7 = new JLabel();
    JLabel jLabel8 = new JLabel();
    JLabel jLabel9 = new JLabel();
    DropDownListBoxWidgetExt sampling = new DropDownListBoxWidgetExt();
    JLabel jLabel12 = new JLabel();
    TextBoxWidgetExt cvfOffset = new TextBoxWidgetExt();
    JLabel jLabel14 = new JLabel();
    BorderLayout borderLayout1 = new BorderLayout();
    TextBoxWidgetExt centralWavelength = new TextBoxWidgetExt();
    JLabel jLabel15 = new JLabel();
    JLabel jLabel16 = new JLabel();
    DropDownListBoxWidgetExt disperser = new DropDownListBoxWidgetExt();
    JLabel jLabel17 = new JLabel();
    TextBoxWidgetExt order = new TextBoxWidgetExt();
    CommandButtonWidgetExt defaultOrder = new CommandButtonWidgetExt();
    JLabel jLabel18 = new JLabel();
    TextBoxWidgetExt wavelengthCoverage = new TextBoxWidgetExt();
    JLabel jLabel19 = new JLabel();
    JLabel jLabel20 = new JLabel();
    TextBoxWidgetExt resolution = new TextBoxWidgetExt();
    JPanel jPanel4 = new JPanel();
    GridBagLayout gridBagLayout4 = new GridBagLayout();
    JPanel jPanel1 = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JLabel jLabel21 = new JLabel();
    TextBoxWidgetExt filter = new TextBoxWidgetExt();

    public Cgs4GUI() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,
                Color.white, new Color(142, 142, 142)), "Filter");
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Slit");
        this.setMinimumSize(new Dimension(350, 378));
        this.setPreferredSize(new Dimension(350, 378));
        this.setLayout(borderLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setForeground(Color.black);
        jLabel2.setText("Position Angle");
        jLabel2.setVerticalAlignment(SwingConstants.BOTTOM);
        posAngle.setBorder(BorderFactory.createLoweredBevelBorder());
        posAngle.setToolTipText("");
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setForeground(Color.black);
        jLabel4.setText("Source Magnitude");
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setForeground(Color.black);
        jLabel5.setText("Acquisition Mode");
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel6.setForeground(Color.black);
        jLabel6.setText("Coadds");
        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel10.setForeground(Color.black);
        jLabel10.setText("Science FOV");
        scienceFOV.setBackground(new Color(204, 204, 204));
        scienceFOV.setBorder(BorderFactory.createLoweredBevelBorder());
        scienceFOV.setEditable(false);
        jLabel11.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel11.setForeground(Color.black);
        jLabel11.setText("(arcsec)");
        mask.setFont(new java.awt.Font("Dialog", 0, 12));
        sourceMag.setFont(new java.awt.Font("Dialog", 0, 12));
        acqMode.setFont(new java.awt.Font("Dialog", 0, 12));
        coadds.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel13.setForeground(Color.black);
        jLabel13.setText("Polariser");
        jLabel13.setVerticalAlignment(SwingConstants.BOTTOM);
        defaultAcquisition.setText("Default");
        jPanel2.setLayout(gridBagLayout1);
        useND.setText("use ND filfer?");
        useND.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setForeground(Color.black);
        jLabel3.setText("Exposure Time");
        jLabel7.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel7.setForeground(Color.black);
        jLabel7.setText("(sec)");
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel8.setForeground(Color.black);
        jLabel8.setText("(exp/integration)");
        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel9.setForeground(Color.black);
        jLabel9.setText("Sampling");
        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel12.setForeground(Color.black);
        jLabel12.setText("CVF Wavelength");
        jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel14.setForeground(Color.black);
        jLabel14.setText("Central Wavelength");
        centralWavelength.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                centralWavelength_actionPerformed(e);
            }
        });
        jLabel15.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel15.setForeground(Color.black);
        jLabel15.setText("(um)");
        jLabel16.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel16.setForeground(Color.black);
        jLabel16.setText("Grating");
        jLabel17.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel17.setForeground(Color.black);
        jLabel17.setText("Order");
        order.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                order_actionPerformed(e);
            }
        });
        defaultOrder.setText("Default");
        jLabel18.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel18.setForeground(Color.black);
        jLabel18.setText("Wavelength Coverage");
        wavelengthCoverage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                wavelengthCoverage_actionPerformed(e);
            }
        });
        wavelengthCoverage.setBorder(BorderFactory.createLoweredBevelBorder());
        wavelengthCoverage.setEditable(false);
        jLabel19.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel19.setForeground(Color.black);
        jLabel19.setText("(um)");
        jLabel20.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel20.setForeground(Color.black);
        jLabel20.setText("Resolution");
        resolution.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resolution_actionPerformed(e);
            }
        });
        jPanel4.setLayout(gridBagLayout4);
        jPanel1.setLayout(gridBagLayout2);
        jPanel2.setBorder(BorderFactory.createEtchedBorder());
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel4.setBorder(BorderFactory.createEtchedBorder());
        disperser.setFont(new java.awt.Font("Dialog", 0, 10));
        sampling.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel21.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel21.setForeground(Color.black);
        jLabel21.setText("Filter");
        jLabel21.setVisible(false);
        filter.setVisible(false);
        resolution.setBackground(new Color(204, 204, 204));
        resolution.setBorder(BorderFactory.createLoweredBevelBorder());
        resolution.setEditable(false);
        polariser.setFont(new java.awt.Font("Dialog", 0, 12));
        exposureTime.setColumns(6);
        this.add(jPanel2, BorderLayout.CENTER);
        jPanel2.add(jLabel1, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0), 0, 0));
        jPanel2.add(mask, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, -15, 0, 15), 0, 0));
        jPanel2.add(posAngle, new GridBagConstraints(
                4, 1, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, -3, 0, 7), 112, 0));
        jPanel2.add(jLabel13, new GridBagConstraints(
                1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(-1, -15, 1, 15), 0, 10));
        jPanel2.add(polariser, new GridBagConstraints(
                1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, -16, 0, 16), 0, 0));
        jPanel2.add(jLabel11, new GridBagConstraints(
                5, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 13), 0, 0));
        jPanel2.add(scienceFOV, new GridBagConstraints(
                4, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 40, 0));
        jPanel2.add(jLabel10, new GridBagConstraints(
                4, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, -3, 0, 3), 0, 0));
        jPanel2.add(jLabel4, new GridBagConstraints(
                2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, -2, 0, 2), 0, 0));
        jPanel2.add(sourceMag, new GridBagConstraints(
                2, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 3), 0, 0));
        jPanel2.add(useND, new GridBagConstraints(
                2, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel2.add(jLabel2, new GridBagConstraints(
                4, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, -1, 0, 41), 0, 0));
        this.add(jPanel4, BorderLayout.SOUTH);
        jPanel4.add(acqMode, new GridBagConstraints(
                0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(sampling, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(jLabel8, new GridBagConstraints(
                4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(jLabel6, new GridBagConstraints(
                3, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(coadds, new GridBagConstraints(
                3, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(cvfOffset, new GridBagConstraints(
                1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(jLabel12, new GridBagConstraints(
                1, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(5, 0, 0, 0), 0, 0));
        jPanel4.add(jLabel9, new GridBagConstraints(
                0, 2, 5, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(5, 0, 0, 0), 0, 0));
        jPanel4.add(exposureTime, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(jLabel7, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 10), 13, 0));
        jPanel4.add(jLabel5, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(5, 0, 0, 0), 0, 0));
        jPanel4.add(jLabel3, new GridBagConstraints(
                1, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(defaultAcquisition, new GridBagConstraints(
                4, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(0, 20, 0, 0), 0, 0));
        this.add(jPanel1, BorderLayout.NORTH);
        jPanel1.add(centralWavelength, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 74, 0));
        jPanel1.add(jLabel15, new GridBagConstraints(
                1, 1, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 6), 13, 0));
        jPanel1.add(jLabel14, new GridBagConstraints(
                0, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets(10, 0, 0, 0), 0, 0));
        jPanel1.add(order, new GridBagConstraints(
                5, 1, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 42, 4));
        jPanel1.add(jLabel18, new GridBagConstraints(
                0, 3, 4, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(15, 0, 1, 29), 0, 0));
        jPanel1.add(wavelengthCoverage, new GridBagConstraints(
                0, 4, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 107, 0));
        jPanel1.add(jLabel17, new GridBagConstraints(
                5, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 14, 0));
        jPanel1.add(defaultOrder, new GridBagConstraints(
                6, 1, 1, 3, 0.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
                new Insets(0, 21, 0, 13), 0, 0));
        jPanel1.add(disperser, new GridBagConstraints(
                4, 1, 1, 2, 1.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, -35, 0, 18), 70, 0));
        jPanel1.add(jLabel16, new GridBagConstraints(
                3, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, 7, 0, 6), 22, 0));
        jPanel1.add(jLabel19, new GridBagConstraints(
                2, 4, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 2), 0, 0));
        jPanel1.add(resolution, new GridBagConstraints(
                4, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 13), 56, 0));
        jPanel1.add(jLabel20, new GridBagConstraints(
                4, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 12), 0, 0));
        jPanel1.add(jLabel21, new GridBagConstraints(
                5, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(filter, new GridBagConstraints(
                5, 4, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 75, 0));
    }

    void centralWavelength_actionPerformed(ActionEvent e) {
    }

    void order_actionPerformed(ActionEvent e) {
    }

    void wavelengthCoverage_actionPerformed(ActionEvent e) {
    }

    void resolution_actionPerformed(ActionEvent e) {
    }
}
