
/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot.editor;

import java.awt.*;
import javax.swing.*;
import jsky.app.ot.gui.*;

public class NoteGUI extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    JLabel jLabel1 = new JLabel();
    TextBoxWidgetExt title = new TextBoxWidgetExt();
    JLabel imageLabel = new JLabel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JLabel jLabel3 = new JLabel();
    JScrollPane jScrollPane1 = new JScrollPane();
    RichTextBoxWidgetExt note = new RichTextBoxWidgetExt();
    CheckBoxWidgetExt observeInstruction = new CheckBoxWidgetExt();

    public NoteGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.setPreferredSize(new Dimension(280, 250));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Title");
        jPanel1.setLayout(gridBagLayout1);
        jPanel2.setLayout(gridBagLayout2);
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setForeground(Color.black);
        jLabel3.setText("Note");
        note.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
	observeInstruction.setFont(new java.awt.Font("Dialog", 0, 12));
	observeInstruction.setText("Show to the Observer");
        this.add(jPanel1, BorderLayout.NORTH);
        jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 20, 5, 5), 0, 0));
        jPanel1.add(title, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
        jPanel1.add(imageLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 5, 20), 0, 0));
	jPanel1.add(observeInstruction, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 5, 20), 0, 0));
        this.add(jPanel2, BorderLayout.CENTER);
        jPanel2.add(jLabel3, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 5, 3, 0), 0, 0));
        jPanel2.add(jScrollPane1, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        jScrollPane1.getViewport().add(note, null);
    }
}
