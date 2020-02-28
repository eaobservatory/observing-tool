/*
 * Copyright (C) 2020 East Asian Observatory
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful,but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc.,51 Franklin
 * Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

package ot.jcmt.iter.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import gemini.sp.SpItem;
import orac.jcmt.iter.SpIterRawXmlObs;

public final class EdIterRawXmlObs extends EdIterJCMTGeneric {
    private IterRawXmlObsGUI _w;

    private SpIterRawXmlObs _iterObs;

    public EdIterRawXmlObs() {
        super(new IterRawXmlObsGUI());

        _title = "Raw XML Observation";
        _presSource = _w = (IterRawXmlObsGUI) super._w;
        _description = "Raw XML Observation Mode";

        _w.jPanel1.setVisible(false);

        _w.timeField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                _iterObs.setElapsedTime(Double.parseDouble(_w.timeField.getText()));
            }
        });

        _w.ocsCfgArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                _iterObs.setOcsConfig(_w.ocsCfgArea.getText());
            }
        });

        _w.loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(_w);
                File file = chooser.getSelectedFile();
                if (file != null) {
                    try {
                        StringBuffer buff = new StringBuffer();
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        char[] cbuf = new char[1024];
                        int len;
                        while ((len = reader.read(cbuf, 0, 1024)) > 0) {
                            buff.append(cbuf, 0, len);
                        }
                        _iterObs.setOcsConfig(buff.toString());
                        _updateWidgets();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(_w, e,
                                "Could not load OCS config. XML",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
    }

    public void setup(SpItem spItem) {
        _iterObs = (SpIterRawXmlObs) spItem;

        super.setup(spItem);
    }

    protected void _updateWidgets() {
        super._updateWidgets();

        _w.timeField.setText(Double.toString(_iterObs.getElapsedTime()));

        Caret caret = _w.ocsCfgArea.getCaret();
        if (caret instanceof DefaultCaret) {
            ((DefaultCaret) caret).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        }

        _w.ocsCfgArea.setText(_iterObs.getOcsConfig());

        if (caret instanceof DefaultCaret) {
            ((DefaultCaret) caret).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }
    }
}
