
/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton, M.Folger@roe.ac.uk
 * @version 1.0
 */
package ot.editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

/**
 * This is the GUI of the editor for a Sky iterator component.
 *
 * This class is a modified version of jsky.app.ot.editor.IterSkyGUI.
 *
 * @see ot.editor.EdIterSky
 *
 * @author M.Folger@roe.ac.uk (modified copy of jsky.app.ot.editor.IterSkyGUI by Allan Brighton)
 */
public class IterSkyGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();
    JComboBox repeatComboBox = new JComboBox();

    public IterSkyGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Sky");
        this.setLayout(gridBagLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel2.setForeground(Color.black);
        jLabel2.setText("X");
        repeatComboBox.setAutoscrolls(true);
        repeatComboBox.setPreferredSize(new Dimension(50, 26));
        this.setPreferredSize(new Dimension(280, 282));
        this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        this.add(jLabel2, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        this.add(repeatComboBox, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }
}
