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

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;

@SuppressWarnings("serial")
public class ProgramGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel1 = new JLabel();
    TextBoxWidgetExt titleBox = new TextBoxWidgetExt();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel4 = new JLabel();
    JLabel propKindLabel = new JLabel();
    OptionWidgetExt queueOption = new OptionWidgetExt();
    OptionWidgetExt classicalOption = new OptionWidgetExt();
    JEditorPane infoBox = new JEditorPane();
    TextBoxWidgetExt piBox = new TextBoxWidgetExt();
    TextBoxWidgetExt countryBox = new TextBoxWidgetExt();
    JLabel jLabel3 = new JLabel();
    TextBoxWidgetExt projectIdBox = new TextBoxWidgetExt();
    JLabel jLabel5 = new JLabel();
    TextBoxWidgetExt estimatedTime = new TextBoxWidgetExt();
    JLabel jLabel6 = new JLabel();
    TextBoxWidgetExt totalTime = new TextBoxWidgetExt();

    public ProgramGUI() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Title");
        this.setLayout(gridBagLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setForeground(Color.black);
        jLabel2.setText("PI");
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setForeground(Color.black);
        jLabel4.setText("Country");
        propKindLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        propKindLabel.setForeground(Color.black);
        propKindLabel.setText("Prop. Kind");
        queueOption.setSelected(true);
        queueOption.setText("Queue");
        queueOption.setFont(new java.awt.Font("Dialog", 0, 12));
        classicalOption.setText("Classical");
        classicalOption.setFont(new java.awt.Font("Dialog", 0, 12));
        infoBox.setText(
                "In the future all the proposal information will be"
                + " accessible. This form merely indicates the type"
                + " of information that will be available.\n");
        infoBox.setBorder(BorderFactory.createLoweredBevelBorder());
        infoBox.setToolTipText("");
        infoBox.setBackground(Color.lightGray);
        infoBox.setEditable(false);
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setForeground(Color.black);
        jLabel3.setText("Project ID");
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setForeground(Color.black);
        jLabel5.setText("Estimated Time (w/o optionals)");
        estimatedTime.setEditable(false);
        jLabel6.setText("Estimated Total Time");
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        totalTime.setEditable(false);
        this.add(jLabel1, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(10, 0, 0, 5), 0, 0));
        this.add(titleBox, new GridBagConstraints(
                1, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 0, 0, 0), 0, 0));
        this.add(jLabel2, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(10, 0, 0, 5), 0, 0));
        this.add(jLabel4, new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(10, 0, 0, 5), 0, 0));
        this.add(propKindLabel, new GridBagConstraints(
                0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 10, 0, 0), 0, 0));
        this.add(classicalOption, new GridBagConstraints(
                2, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 0, 0), 0, 0));
        this.add(queueOption, new GridBagConstraints(
                1, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 0, 0), 0, 0));
        this.add(infoBox, new GridBagConstraints(
                0, 6, 3, 2, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 9, 5), 0, 0));
        this.add(piBox, new GridBagConstraints(
                1, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(countryBox, new GridBagConstraints(
                1, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel3, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(10, 0, 0, 5), 0, 0));
        this.add(projectIdBox, new GridBagConstraints(
                1, 3, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel5, new GridBagConstraints(
                0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 5, 0, 5), 0, 0));
        this.add(estimatedTime, new GridBagConstraints(
                1, 4, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel6, new GridBagConstraints(
                0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 5, 0, 5), 0, 0));
        this.add(totalTime, new GridBagConstraints(
                1, 5, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.setPreferredSize(new Dimension(282, 260));
    }
}
