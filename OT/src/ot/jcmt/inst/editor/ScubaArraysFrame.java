/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.inst.editor;

import orac.jcmt.inst.SpInstSCUBA;
import ot.jcmt.inst.editor.scuba.ScubaArrays;
import ot.jcmt.inst.editor.scuba.Bolometer;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;



/**
 * This class provides a frame for the ScubaArrays GUI.
 *
 * An "OK" button is provided to store the settings to
 * a SCUBA SpItem of the OT.
 *
 * Note that this frame is a modal JDialog.
 *
 * @see edfreq.FrontEnd
 * @see edfreq.FrontEndFrame
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class ScubaArraysFrame extends JDialog implements ActionListener {

  private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Cancel");
  private JPanel buttonPanel  = new JPanel();

  private EdCompInstSCUBA _scubaEditor;
  private SpInstSCUBA     _instSCUBA;

  private ScubaArrays _scubaArrays = new ScubaArrays(System.getProperty("ot.cfgdir") + "flat.dat");


  public ScubaArraysFrame() {
    setTitle("SCUBA Bolometer Selection");
    setModal(true);

    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);

    getContentPane().add(_scubaArrays.getDisplayPanel(), BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    pack();
    setLocation(100, 100);

    setDefaultCloseOperation(HIDE_ON_CLOSE);

    okButton.addActionListener(this);
    cancelButton.addActionListener(this);
  }

  public void show(EdCompInstSCUBA scubaEditor) {
    _scubaEditor = scubaEditor;
    _instSCUBA   = (SpInstSCUBA)scubaEditor.getCurrentSpItem();

    // Set appropriate bolometers enabled and reset all bolometers.
    String [] bolometerTypeStrings = SpInstSCUBA.getBolometersFor(_instSCUBA.getFilter());
    int bolometerTypeBitMask = Bolometer.BOLOMETER_NONE;

    for(int i = 0; i < bolometerTypeStrings.length; i++) {
      bolometerTypeBitMask += Bolometer.toBolometerType(bolometerTypeStrings[i]);
    }

    _scubaArrays.update(bolometerTypeBitMask, _instSCUBA.getBolometers(), _instSCUBA.getPrimaryBolometer());

    show();
  }

  public void applyAndHide() {
    _instSCUBA.setBolometers(_scubaArrays.getBolometers());
    _instSCUBA.setPrimaryBolometer(_scubaArrays.getPrimaryBolometer());
    _scubaEditor.refresh();
    hide();
  }

  public void cancel() {
    hide();
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == okButton) {
      applyAndHide();
    }

    if(e.getSource() == cancelButton) {
      hide();
    }
  }
}

