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

package ot.ukirt.inst.editor;

import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;

import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;

@SuppressWarnings("serial")
public class WfcamGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel4 = new JLabel();
    TextBoxWidgetExt Coadds = new TextBoxWidgetExt();
    TextBoxWidgetExt ExpTime = new TextBoxWidgetExt();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();
    DropDownListBoxWidgetExt ReadMode = new DropDownListBoxWidgetExt();
    DropDownListBoxWidgetExt Filter = new DropDownListBoxWidgetExt();

    public WfcamGUI() {
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

        jLabel4.setText("Coadds");
        jLabel4.setToolTipText("");
        jLabel4.setForeground(Color.black);
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        Coadds.setBorder(BorderFactory.createLoweredBevelBorder());
        Coadds.setHorizontalAlignment(SwingConstants.CENTER);
        ExpTime.setHorizontalAlignment(SwingConstants.CENTER);
        ExpTime.setBorder(BorderFactory.createLoweredBevelBorder());
        ExpTime.setEditable(true);
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setForeground(Color.black);
        jLabel3.setText("Exp time (sec)");
        jLabel1.setText("Read Mode");
        jLabel1.setForeground(Color.black);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("Filter");
        jLabel2.setToolTipText("");
        jLabel2.setForeground(Color.black);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
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
        this.add(jLabel4, new GridBagConstraints(
                1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(30, 30, 0, 0), 0, 0));
        this.add(Coadds, new GridBagConstraints(
                1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 30, 0, 0), 40, 0));
        this.add(ExpTime, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 70, 0));
        this.add(jLabel3, new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(30, 10, 0, 0), 0, 0));
        this.add(jLabel1, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel2, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 30, 0, 0), 0, 0));
        this.add(ReadMode, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 60, 0));
        this.add(Filter, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 30, 0, 0), 30, 0));
    }
}
