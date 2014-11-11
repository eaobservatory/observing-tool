/*
 * Copyright: Copyright (c) 2000 Association of Universities for Research in
 *            Astronomy, Inc. (AURA)
 *
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on Allan Brighton (jsky/app/ot/editor/TitleEditorGUI.java)
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

package ot.editor;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;

/**
 * Or folder GUI. Modified version of jsky.app.ot.editor.TitleEditorGUI.
 *
 * @see jsky.app.ot.editor.TitleEditorGUI
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on Allan Brighton (jsky/app/ot/editor/TitleEditorGUI.java)
 */
@SuppressWarnings("serial")
public class OrEditorGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel1 = new JLabel();
    TextBoxWidgetExt itemTitle = new TextBoxWidgetExt();
    JLabel jLabel2 = new JLabel();
    DropDownListBoxWidgetExt numberOfItems = new DropDownListBoxWidgetExt();
    JLabel jLabel3 = new JLabel();

    public OrEditorGUI() {
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
        this.setPreferredSize(new Dimension(279, 271));
        this.setLayout(gridBagLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setForeground(Color.black);
        jLabel2.setText("Select");
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setForeground(Color.black);
        jLabel3.setText("items");
        this.add(jLabel1, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 10, 0, 0), 0, 0));
        this.add(itemTitle, new GridBagConstraints(
                1, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.add(jLabel2, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 0, 0, 0), 0, 0));
        this.add(numberOfItems, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 5, 0, 5), 0, 0));
        this.add(jLabel3, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 0, 0, 0), 0, 0));
    }
}
