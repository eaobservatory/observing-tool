
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
import java.text.DecimalFormat;

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

    JLabel selectLabel = new JLabel();
    JSpinner skySpinner = new JSpinner();

    JRadioButton jrb1 = new JRadioButton("Follow Base Offset");
    JRadioButton jrb2 = new JRadioButton("Random Offset");
    JRadioButton jrb3 = new JRadioButton("No Offset");
    JLabel scaleLabel = new JLabel("Scaling factor");
    JTextField scaleText  = new JTextField(6);
    JLabel boxLabel = new JLabel("Box size(arcsecs)");
    JTextField boxText = new JTextField(6);
    JPanel jp = new JPanel();
    ButtonGroup bg = new ButtonGroup();
    

    public IterSkyGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        jrb1.setHorizontalTextPosition(SwingConstants.LEADING);
        jrb1.setFont(new java.awt.Font("Dialog", 0, 12));
        jrb2.setHorizontalTextPosition(SwingConstants.LEADING);
        jrb2.setFont(new java.awt.Font("Dialog", 0, 12));
        jrb3.setHorizontalTextPosition(SwingConstants.LEADING);
        jrb3.setFont(new java.awt.Font("Dialog", 0, 12));
        scaleLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        scaleLabel.setForeground(Color.black);
        boxLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        boxLabel.setForeground(Color.black);
        selectLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        selectLabel.setForeground(Color.black);
        selectLabel.setText("Select Sky:");
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Repeat");
        this.setLayout(gridBagLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel2.setForeground(Color.black);
        jLabel2.setText("X");
        repeatComboBox.setAutoscrolls(true);
        repeatComboBox.setPreferredSize(new Dimension(50, 26));
        jp.setLayout(new GridBagLayout());
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.anchor = GridBagConstraints.EAST;
        gbc1.weightx=1.0;
        gbc1.weighty=1.0;
        bg.add(jrb1);
        bg.add(jrb2);
        bg.add(jrb3);
        gbc1.gridx=0;
        gbc1.gridy=0;
        jp.add(jrb1, gbc1);
        gbc1.gridx=1;
        jp.add(scaleLabel, gbc1);
        gbc1.gridx=2;
        jp.add(scaleText, gbc1);
        gbc1.gridx=0;
        gbc1.gridy=1;
        jp.add(jrb2, gbc1);
        gbc1.gridx=1;
        jp.add(boxLabel, gbc1);
        gbc1.gridx=2;
        jp.add(boxText, gbc1);
        gbc1.gridx=0;
        gbc1.gridy=2;
        jp.add(jrb3, gbc1);


        
        this.setPreferredSize(new Dimension(280, 282));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth=6;
        gbc.gridheight = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5,2,5,2);
        this.add (jp);
        gbc.gridwidth=1;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(selectLabel, gbc);
        gbc.gridx = 1;
        this.add(skySpinner, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(jLabel1, gbc);
        gbc.gridx=1;
        this.add(repeatComboBox, gbc);
        gbc.gridx = 3;
        this.add(jLabel2, gbc);
        

        /*
        this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        this.add(jLabel2, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        this.add(repeatComboBox, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            */
    }
}
