/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.inst.editor;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import jsky.app.ot.gui.*;

/**
 * @author Martin Folger ( M.Folger@roe.ac.uk )
 */
public class ScubaGUI extends JPanel {
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  OptionWidgetExt longShortRadioButton = new OptionWidgetExt();
  CheckBoxWidgetExt longCheckBox = new CheckBoxWidgetExt();
  CheckBoxWidgetExt shortCheckBox = new CheckBoxWidgetExt();
  OptionWidgetExt pixelListRadioButton = new OptionWidgetExt();
  TextBoxWidgetExt textBoxWidgetExt1 = new TextBoxWidgetExt();
  CommandButtonWidgetExt pixelToolButton = new CommandButtonWidgetExt();

   public ScubaGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

   }

  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);
    longCheckBox.setText("Long");
    longCheckBox.setFont(new java.awt.Font("Dialog", 0, 12));
    shortCheckBox.setText("Short");
    shortCheckBox.setFont(new java.awt.Font("Dialog", 0, 12));
    pixelListRadioButton.setText("List of Pixels");
    pixelListRadioButton.setFont(new java.awt.Font("Dialog", 0, 12));
    pixelToolButton.setToolTipText("");
    pixelToolButton.setText("Pixel Tool");
    longShortRadioButton.setFont(new java.awt.Font("Dialog", 0, 12));
    this.add(longShortRadioButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(longCheckBox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
    this.add(shortCheckBox, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
    this.add(pixelListRadioButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(20, 0, 0, 0), 0, 0));
    this.add(textBoxWidgetExt1, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 20, 0, 0), 0, 0));
    this.add(pixelToolButton, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
  }
}
