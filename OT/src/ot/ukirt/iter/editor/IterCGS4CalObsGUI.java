
/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton (modified for CGS4/UKIRT by M.Folger@roe.ac.uk)
 * @version 1.0
 */
package ot.ukirt.iter.editor;

import java.awt.*;
import javax.swing.*;
import jsky.app.ot.gui.*;

public class IterCGS4CalObsGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JComboBox repeatComboBox = new JComboBox();
    TextBoxWidgetExt coadds = new TextBoxWidgetExt();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JLabel jLabel6 = new JLabel();
    TextBoxWidgetExt exposureTime = new TextBoxWidgetExt();
    JLabel jLabel5 = new JLabel();
    JLabel jLabel4 = new JLabel();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel7 = new JLabel();
    JLabel jLabel8 = new JLabel();
    JLabel jLabel9 = new JLabel();
    DropDownListBoxWidgetExt mode = new DropDownListBoxWidgetExt();
    DropDownListBoxWidgetExt filter = new DropDownListBoxWidgetExt();
    DropDownListBoxWidgetExt lamp = new DropDownListBoxWidgetExt();
  JLabel jLabel10 = new JLabel();
  DropDownListBoxWidgetExt calType = new DropDownListBoxWidgetExt();
  CommandButtonWidgetExt defaultValues = new CommandButtonWidgetExt();
  JPanel calTypesPanel = new JPanel();
  CardLayout cardLayout1 = new CardLayout();
  JPanel flatPanel = new JPanel();
  JPanel arcPanel = new JPanel();
  JLabel jLabel11 = new JLabel();
  DropDownListBoxWidgetExt flatSampling = new DropDownListBoxWidgetExt();
  JLabel jLabel13 = new JLabel();
  TextBoxWidgetExt cvfWavelength = new TextBoxWidgetExt();
  JLabel jLabel12 = new JLabel();
  JPanel emptyPanel = new JPanel();

    public IterCGS4CalObsGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        jLabel1.setText("Coadds");
        jLabel1.setForeground(Color.black);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("(exp / obs)");
        jLabel2.setForeground(Color.black);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel3.setText("X");
        jLabel3.setForeground(Color.black);
        jLabel3.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel4.setText("Observe");
        jLabel4.setForeground(Color.black);
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setText("(sec)");
        jLabel5.setForeground(Color.black);
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 10));
        exposureTime.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel6.setText("Exp. Time");
        jLabel6.setForeground(Color.black);
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        coadds.setBorder(BorderFactory.createLoweredBevelBorder());
        repeatComboBox.setPreferredSize(new Dimension(50, 26));
        repeatComboBox.setAutoscrolls(true);
        this.setMinimumSize(new Dimension(280, 206));
        this.setPreferredSize(new Dimension(280, 206));
        this.setLayout(gridBagLayout1);
        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel7.setForeground(Color.black);
        jLabel7.setText("Aquisition Mode");
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setForeground(Color.black);
        jLabel8.setText("Filter");
        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel9.setForeground(Color.black);
        jLabel9.setText("Lamp");
        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setForeground(Color.black);
    jLabel10.setText("Calibration Type");
    defaultValues.setText("Default");
    calTypesPanel.setLayout(cardLayout1);
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setForeground(Color.black);
    jLabel11.setText("Flat Sampling");
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel13.setForeground(Color.black);
    jLabel13.setText("CVF Wavelength");
    cvfWavelength.setColumns(12);
    jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setForeground(Color.black);
    jLabel12.setText("(microns)");
    this.add(repeatComboBox, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(coadds, new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel6, new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(exposureTime, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel5, new GridBagConstraints(3, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel4, new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel3, new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel2, new GridBagConstraints(3, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel1, new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel7, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel8, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel9, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
        this.add(mode, new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(filter, new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(lamp, new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(jLabel10, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    this.add(calType, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(defaultValues, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 30), 0, 0));
    this.add(calTypesPanel, new GridBagConstraints(0, 10, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    calTypesPanel.add(flatPanel, "FLAT");
    flatPanel.add(jLabel11, null);
    flatPanel.add(flatSampling, null);
    calTypesPanel.add(arcPanel, "ARC");
    arcPanel.add(jLabel13, null);
    arcPanel.add(cvfWavelength, null);
    arcPanel.add(jLabel12, null);
    calTypesPanel.add(emptyPanel, "EMPTY");
    }
}
