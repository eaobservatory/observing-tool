
/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot;

import java.awt.*;
import jsky.app.ot.gui.*;
import javax.swing.*;

public class OdbAccessGUI extends JPanel {
    StopActionWidget stopAction = new StopActionWidget();
    JLabel jLabel1 = new JLabel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();

    public OdbAccessGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        this.setMinimumSize(new Dimension(239, 69));
        this.setPreferredSize(new Dimension(239, 69));
        this.setLayout(gridBagLayout1);
        jLabel1.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Accessing the Observing Database");
        this.add(stopAction, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    }
}
