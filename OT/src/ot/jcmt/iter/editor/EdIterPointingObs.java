/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.iter.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.CardLayout;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.OptionWidgetWatcher;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.iter.SpIterPointingObs;

/**
 * This is the editor for Pointing Observe Mode iterator component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterPointingObs extends EdIterJCMTGeneric
  implements CommandButtonWidgetWatcher, OptionWidgetWatcher, ActionListener {

  private IterPointingObsGUI _w;       // the GUI layout panel

  private SpIterPointingObs _iterObs;

    private static final String POINTING_METHOD = "5-point";

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterPointingObs() {
    super(new IterPointingObsGUI());

    _title       ="Pointing";
    _presSource  = _w = (IterPointingObsGUI)super._w;
    _description ="Pointing Observation Mode";

    ButtonGroup grp = new ButtonGroup();
    grp.add(_w.continuum);
    grp.add(_w.spectralLine);

    _w.continuum.setActionCommand(SpIterPointingObs.SPECTRAL_MODE_CONTINUUM);
    _w.spectralLine.setActionCommand(SpIterPointingObs.SPECTRAL_MODE_SPECTRAL_LINE);

//     _w.pointingMethod.setChoices(SpIterPointingObs.POINTING_METHODS);
    _w.pointingMethod.addItem(POINTING_METHOD);

    MenuElement [] menuElements = _w.pointingPixelPopupMenu.getSubElements();
    MenuElement [] subElements  = null;

    Font font = new Font("Dialog", Font.PLAIN, 12);
    JMenuItem menuItem = new JMenuItem(SpIterPointingObs.POINTING_PIXEL_AUTOMATIC);
    menuItem.setFont(font);
    menuItem.addActionListener(this);
    _w.pointingPixelPopupMenu.add(menuItem);

    JMenu menu = new JMenu(SpIterPointingObs.POINTING_PIXEL_MANUAL);
    menu.setFont(font);
    menu.addActionListener(this);
    _w.pointingPixelPopupMenu.add(menu);

    for(int i = 0; i < SpIterPointingObs.POINTING_PIXEL_MANUAL_CHOICES.length; i++) {
      menuItem = new JMenuItem(SpIterPointingObs.POINTING_PIXEL_MANUAL_CHOICES[i]);
      menuItem.setFont(font);
      menuItem.addActionListener(this);
      menu.add(menuItem);
    }

    _w.pointingPixelButton.addWatcher(this);
    _w.continuum.addWatcher(this);
    _w.spectralLine.addWatcher(this);

    _w.automaticTarget.setToolTipText("Automatically determine pointing target at time of observation");

    _w.frequencyPanel.setVisible(false);
  }

  /**
   * Override setup to store away a reference to the Pointing Iterator.
   */
  public void setup(SpItem spItem) {
    _iterObs = (SpIterPointingObs) spItem;
    super.setup(spItem);
  }

  protected void _updateWidgets() {
    _w.pointingPixelButton.setText(_iterObs.getPointingPixel());

    if(SpIterPointingObs.SPECTRAL_MODE_CONTINUUM.equals(_iterObs.getSpectralMode())) {
      _w.continuum.setSelected(true);
    }
    else {
      _w.spectralLine.setSelected(true);
    }

    _w.switchingMode.setValue(IterJCMTGenericGUI.BEAM);
    _w.switchingMode.setEnabled(false);

    super._updateWidgets();
  }


//  public void textBoxAction(TextBoxWidgetExt tbwe) {
//    super.textBoxAction(tbwe);
//  }

  public void commandButtonAction(CommandButtonWidgetExt cbwe) {
    _w.pointingPixelPopupMenu.show(_w.pointingPixelButton, 0, 0);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() instanceof JMenuItem) {
      _w.pointingPixelButton.setText(((JMenuItem)(e.getSource())).getText());
      _iterObs.setPointingPixel(((JMenuItem)(e.getSource())).getText());
      return;
    }
//    System.out.println("ChangeEvent.toString() = " + e.toString());
//    System.out.println("PopupMenuEvent.getSource() = " + e.getSource());
  }
 
  public void optionAction(OptionWidgetExt owe) {
    _iterObs.setSpectralMode(owe.getActionCommand());
  }

  public void setInstrument(SpInstObsComp spInstObsComp) {
    super.setInstrument(spInstObsComp);

    if((spInstObsComp != null) && (spInstObsComp instanceof SpInstHeterodyne)) {
      _w.acsisPanel.setVisible(true);
    }
    else {
      _w.acsisPanel.setVisible(false);
    }
  }
}

