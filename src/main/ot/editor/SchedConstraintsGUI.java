/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot.editor;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.OptionWidgetExt;

@SuppressWarnings("serial")
public class SchedConstraintsGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    TitledBorder titledBorder1;
    Border border1;
    TitledBorder titledBorder2;
    TitledBorder titledBorder3;
    TitledBorder titledBorder4;
    TextBoxWidgetExt earliest = new TextBoxWidgetExt();
    TextBoxWidgetExt latest = new TextBoxWidgetExt();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel4 = new JLabel();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel5 = new JLabel();
    TextBoxWidgetExt minElevation = new TextBoxWidgetExt();
    JLabel jLabel6 = new JLabel();
    JLabel jLabel7 = new JLabel();
    TextBoxWidgetExt period = new TextBoxWidgetExt();
    JLabel jLabel8 = new JLabel();
    JLabel jLabel9 = new JLabel();
    JLabel jLabel10 = new JLabel();
    TextBoxWidgetExt maxElevation = new TextBoxWidgetExt();
    JPanel jPanel1 = new JPanel();
    OptionWidgetExt meridianApproachAny = new OptionWidgetExt();
    OptionWidgetExt meridianApproachSetting = new OptionWidgetExt();
    OptionWidgetExt meridianApproachRising = new OptionWidgetExt();
    FlowLayout flowLayout1 = new FlowLayout();
    JLabel jLabel11 = new JLabel();
    JCheckBox airmassCB = new JCheckBox("As airmass");

    public SchedConstraintsGUI() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,
                Color.white, new Color(142, 142, 142)), "Weather Band");
        border1 = new EtchedBorder(EtchedBorder.RAISED,
                Color.white, new Color(142, 142, 142));
        titledBorder2 = new TitledBorder(border1, "Seeing");
        titledBorder3 = new TitledBorder(BorderFactory.createLineBorder(
                new Color(153, 153, 153), 2), "Moon");
        titledBorder4 = new TitledBorder(BorderFactory.createLineBorder(
                new Color(153, 153, 153), 2), "Sky");
        this.setMinimumSize(new Dimension(279, 276));
        this.setPreferredSize(new Dimension(279, 276));
        this.setLayout(gridBagLayout1);
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel3.setForeground(Color.black);
        jLabel3.setText("(YYYY-MM-DD[THH:MM:SS])");
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel4.setForeground(Color.black);
        jLabel4.setText("(YYYY-MM-DD[THH:MM:SS])");
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Earliest Schedule Date ( UT )");
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setForeground(Color.black);
        jLabel2.setText("Latest Schedule Date ( UT )");
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setForeground(Color.black);
        jLabel5.setText("Elevation Constraints");
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel6.setForeground(Color.black);
        jLabel6.setText("(degrees above horizon)");
        jLabel7.setForeground(Color.black);
        jLabel7.setText("Reschedule every");
        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setForeground(Color.black);
        jLabel8.setText("days");
        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel9.setForeground(Color.black);
        jLabel9.setText("Min");
        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel10.setForeground(Color.black);
        jLabel10.setText("Max");
        airmassCB.setFont(new java.awt.Font("Dialog", 0, 12));
        airmassCB.setForeground(Color.black);
        meridianApproachAny.setText("Don\'t Care");
        meridianApproachAny.setFont(new java.awt.Font("Dialog", 0, 12));
        meridianApproachSetting.setText("Setting    ");
        meridianApproachSetting.setFont(new java.awt.Font("Dialog", 0, 12));
        meridianApproachRising.setText("Rising    ");
        meridianApproachRising.setFont(new java.awt.Font("Dialog", 0, 12));
        jPanel1.setLayout(flowLayout1);
        flowLayout1.setAlignment(FlowLayout.LEFT);
        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel11.setForeground(Color.black);
        jLabel11.setText("Schedule when source is");
        this.add(earliest, new GridBagConstraints(
                0, 1, 4, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(latest, new GridBagConstraints(
                0, 3, 4, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel3, new GridBagConstraints(
                4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel4, new GridBagConstraints(
                4, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel1, new GridBagConstraints(
                0, 0, 4, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        this.add(jLabel2, new GridBagConstraints(
                0, 2, 4, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(20, 5, 0, 0), 0, 0));
        this.add(jLabel5, new GridBagConstraints(
                0, 6, 4, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(20, 5, 0, 0), 0, 0));
        this.add(airmassCB, new GridBagConstraints(
                3, 6, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(20, 5, 0, 0), 0, 0));
        this.add(minElevation, new GridBagConstraints(
                1, 7, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel6, new GridBagConstraints(
                4, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        // Periodicity [timj kluge]
        this.add(jLabel7, new GridBagConstraints(
                0, 4, 4, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(20, 5, 0, 0), 0, 0));
        this.add(period, new GridBagConstraints(
                0, 5, 4, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel8, new GridBagConstraints(
                4, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel9, new GridBagConstraints(
                0, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        this.add(jLabel10, new GridBagConstraints(
                2, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 10, 0, 0), 0, 0));
        this.add(maxElevation, new GridBagConstraints(
                3, 7, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.add(jPanel1, new GridBagConstraints(
                0, 9, 5, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(meridianApproachRising, null);
        jPanel1.add(meridianApproachSetting, null);
        jPanel1.add(meridianApproachAny, null);
        this.add(jLabel11, new GridBagConstraints(
                0, 8, 4, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(20, 5, 0, 0), 0, 0));
    }
}
