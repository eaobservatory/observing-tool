
/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package ot.ukirt.iter.editor;

import java.awt.*;
import javax.swing.*;
import jsky.app.ot.gui.*;

public class IterMichelleTargetAcqGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel5 = new JLabel();
    JLabel jLabel6 = new JLabel();
    TextBoxWidgetExt exposureTime = new TextBoxWidgetExt();
    TextBoxWidgetExt observationTime = new TextBoxWidgetExt();
    CommandButtonWidgetExt defaultAcquisition = new CommandButtonWidgetExt();

    public IterMichelleTargetAcqGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        jLabel1.setText("Obs. Time");
        jLabel1.setForeground(Color.black);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("(sec)");
        jLabel2.setForeground(Color.black);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 10));
        this.setMinimumSize(new Dimension(280, 278));
        this.setPreferredSize(new Dimension(280, 278));
        this.setLayout(gridBagLayout1);
        jLabel5.setText("(sec)");
        jLabel5.setForeground(Color.black);
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel6.setText("Exp. Time");
        jLabel6.setForeground(Color.black);
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    
	exposureTime.setBackground(new Color(220, 220, 220));
	exposureTime.setBorder(BorderFactory.createLoweredBevelBorder());
	exposureTime.setEditable(false);

	observationTime.setBackground(new Color(220, 220, 220));
	observationTime.setBorder(BorderFactory.createLoweredBevelBorder());
	observationTime.setEditable(false);

        defaultAcquisition.setText("Reset to Default");
	this.add(jLabel2, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel5, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        this.add(exposureTime, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.add(observationTime, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(defaultAcquisition, new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
    }
}
