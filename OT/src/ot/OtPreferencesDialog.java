/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot;

import jsky.app.ot.OtProps;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;


/**
 * The general preferences page.
 *
 * This class uses code from ot.Palette of the FreeBongo OT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class OtPreferencesDialog implements ActionListener {
  /**
   * This is a subclass of JPanel so it can be used for internal as well as other frames.
   */
  private OtPreferencesGUI _w;

  /**
   * Is only used if the OT is started with internal frames.
   */
  private JInternalFrame _internalFrame;

  /**
   *
   */
  private String _title;

  public OtPreferencesDialog() {
    _title = "Preferences";
    /*_presSource  =*/
    _w = new OtPreferencesGUI();
    //_description ="The preferences are set with this component.";

    ButtonGroup grp = new ButtonGroup();
    grp.add(_w.closePromptOption);
    grp.add(_w.closeNoSaveOption);

    _w.okButton.addActionListener(this);
    _w.applyButton.addActionListener(this);
    _w.cancelButton.addActionListener(this);
  }


  /**
   * For ise with internal frames.
   */
  public void show(JDesktopPane desktop) {
    if(desktop != null) {
      boolean saveShouldPrompt = OtProps.isSaveShouldPrompt();
      _w.closePromptOption.setSelected(saveShouldPrompt);
      _w.closeNoSaveOption.setSelected(!saveShouldPrompt);
    
      _internalFrame = new JInternalFrame(_title);
      _internalFrame.getContentPane().add(_w);
      desktop.add(_internalFrame, JLayeredPane.MODAL_LAYER);
      _w.setVisible(true);
      _internalFrame.setVisible(true);
      _internalFrame.setLocation(100, 100);
      _internalFrame.pack();
    }

    _w.setVisible(true);
  }

  public void apply() {
    if(_w.closeNoSaveOption.isSelected()) {
      OtProps.setSaveShouldPrompt(false);
    }
    else {
      OtProps.setSaveShouldPrompt(true);
    }
  }

  public void hide() {
    _internalFrame.dispose();
  }


  /**
   * The standard actionPerformed method to handle the "ok", "apply", and "cancel"
   * buttons.
   */
  public void actionPerformed(ActionEvent evt) {
 
    Object w  = evt.getSource();

    if (w == _w.okButton) {
      apply();
      hide();
      return;
    }

    if (w == _w.applyButton) {
      apply();
      return;
    }

    if (w == _w.cancelButton) {
      hide();
      return;
    }
  }
}

