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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import gemini.sp.SpItem;
import orac.jcmt.inst.SpInstHeterodyne;
import ot.util.DialogUtil;
import edfreq.FrequencyEditorConstants;
import jsky.app.ot.editor.OtItemEditor;

/**
 * Editor class for Heterodyne instrument component.
 */
public final class EdCompInstHeterodyne extends OtItemEditor implements FrequencyEditorConstants, ActionListener {

  /**
   * Font attributes for parameter names in JTextPane.
   *
   * Example:
   *
   * <b>bandMode</b><tt>8-system<tt>
   *
   * where bandMode is the parameter name.
   */
  private SimpleAttributeSet _nameFontAttribues;

  /**
   * Font attributes for parameter values in JTextPane.
   *
   * Example:
   *
   * <b>bandMode</b><tt>8-system<tt>
   *
   * where 8-systen is the parameter value.
   */
  private SimpleAttributeSet _valueFontAttribues;

  private SpInstHeterodyne _instHeterodyne;

  private FrequencyEditorFrame _freqEditorFrame;

  private HeterodyneGUI _w;		// the GUI layout

  private String _defaultFreqEditorXml = null;

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdCompInstHeterodyne() {
    _title       ="JCMT Heterodyne";
    _presSource  = _w = new HeterodyneGUI();
    _description ="The Heterodyne instrument is configured with this component.";

    _freqEditorFrame = new FrequencyEditorFrame(this);
//    _freqEditorFrame.setVisible(false);
    _w.summaryTextComponent.setEnabled(false);

    _w.editButton.addActionListener(this);
  }


  /**
   * Override setup to store away a reference to the SpInstCGS4 item.
   */
  public void setup(SpItem spItem) {
    _instHeterodyne = (SpInstHeterodyne)spItem;

    super.setup(spItem);
  }


  /**
   * Implements the _updateWidgets method from OtItemEditor in order to
   * setup the widgets to show the current values of the item.
   */
  protected void _updateWidgets() {
    _freqEditorFrame.update(_instHeterodyne);      
    updateFreqEditorSummaryDoc();
  }


  public void actionPerformed(ActionEvent evt) {
    if(evt.getSource() == _w.editButton) {
      _freqEditorFrame.update(_instHeterodyne);
      _freqEditorFrame.setFrequencyEditorVisible(true);
    }
  }

  /**
   * Converts SpItem to Document.
   *
   * Resets {@link #_freqEditorSummaryDoc} according to current settings stored in
   * {@link _instHeterodyne}.
   */
  protected void updateFreqEditorSummaryDoc() {
    String freqEditorXml = _instHeterodyne.getFreqEditorXml();

    if((freqEditorXml == null) || freqEditorXml.trim().equals("")) {
      _w.summaryTextComponent.setText("Use the Frequency Editor (\"Edit\" button)\n" +
                                      "to define the setup for Heterodyne observing.");
    }
    else {
      String summary = _instHeterodyne.getFreqEditorXml().replace('<', ' ')
							 .replace('>', ' ')
							 .replace('/', ' ')
							 .replace('"', ' ')
							 .replace('=', ':');

      _w.summaryTextComponent.setText(summary.substring(12, summary.length() - 12));
    }
  }
}
