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
import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

//import org.xml.sax.InputSource;
//import org.apache.xerces.dom.ElementImpl;
//import org.apache.xerces.parsers.DOMParser;
//import java.io.StringReader;

import gemini.sp.SpItem;
//import orac.util.SpItemDOM;
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

  private Document _freqEditorSummaryDoc;

  private SpInstHeterodyne _instHeterodyne;

  private FrequencyEditorFrame _freqEditorFrame;

  private HeterodyneGUI _w;		// the GUI layout


  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdCompInstHeterodyne() {
    _title       ="JCMT Heterodyne";
    _presSource  = _w = new HeterodyneGUI();
    _description ="The Heterodyne instrument is configured with this component.";

    _freqEditorFrame = new FrequencyEditorFrame(this);
//    _freqEditorFrame.setVisible(false);

    _nameFontAttribues = new SimpleAttributeSet();
    StyleConstants.setFontFamily(_nameFontAttribues, "TimesNewRoman");
    //StyleConstants.setBold(_nameFontAttribues, true);

    _valueFontAttribues = new SimpleAttributeSet();
    StyleConstants.setFontFamily(_valueFontAttribues, "Monospaced");
    StyleConstants.setBold(_valueFontAttribues, true);

    _freqEditorSummaryDoc = _w.summaryTextPane.getDocument();

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
    //try {
      _freqEditorFrame.update(_instHeterodyne);      
      updateFreqEditorSummaryDoc();
      //_freqEditorSummaryDoc.insertString(0, _instHeterodyne.toString(), _valueFontAttribues);
      //_freqEditorSummaryDoc.insertString(_freqEditorSummaryDoc.getLength(), "test name" + ":\n", _nameFontAttribues);
      //_freqEditorSummaryDoc.insertString(_freqEditorSummaryDoc.getLength(), "test value" + "\n", _valueFontAttribues);
    //}
    //catch(BadLocationException e) {
    //  e.printStackTrace();
    //}
  }

  /**
   * Updates the Heterodyne SpItem (SpInstHeterodyne).
   */
//  public void updateSpItem(String xml) {
//    try {
//      DOMParser parser = new DOMParser();
//      parser.parse(new InputSource(new StringReader(xml)));
//      ElementImpl element = (ElementImpl)parser.getDocument().getDocumentElement();
//
//      _instHeterodyne.getTable().rmAll();
//      SpItemDOM.fillAvTable("", element, _instHeterodyne.getTable());
//    }
//    catch(Exception e) {
//      e.printStackTrace();
//      DialogUtil.error(_w, "Could not read data from frequency editor: " + e);
//    }
//  }


  public void actionPerformed(ActionEvent evt) {
    if(evt.getSource() == _w.editButton) {
      //try {
        _freqEditorFrame.update(_instHeterodyne);
        _freqEditorFrame.setFrequencyEditorVisible(true);
      //}
      //catch(Exception e) {
      //  DialogUtil.error(_w, "Could not update frequency editor: " + e);
      //  e.printStackTrace();
      //}
    }
  }

  /**
   * Converts SpItem to Document.
   *
   * Resets {@link #_freqEditorSummaryDoc} according to current settings stored in
   * {@link _instHeterodyne}.
   */
  protected void updateFreqEditorSummaryDoc() {
  
    try {
      _freqEditorSummaryDoc.remove(0, _freqEditorSummaryDoc.getLength());

      _freqEditorSummaryDoc.insertString(_freqEditorSummaryDoc.getLength(), "Summary for Heterodyne component in ",
                                         _nameFontAttribues);
      _freqEditorSummaryDoc.insertString(_freqEditorSummaryDoc.getLength(), _instHeterodyne.parent().getTitle() + "\n\n",
                                         _valueFontAttribues);
    }
    catch(BadLocationException e) {
      DialogUtil.error("Could not create summary for Heterodyne settings: " + e);
      e.printStackTrace();
    }

    _appendToFreqEditorSummaryDoc("Front End \t: ", XML_ELEMENT_HETERODYNE + ":" + XML_ATTRIBUTE_FE_NAME,    "\n");
    _appendToFreqEditorSummaryDoc("Mode      \t: ", XML_ELEMENT_HETERODYNE + ":" + XML_ATTRIBUTE_MODE,       "\n");
    _appendToFreqEditorSummaryDoc("Band Mode\t: ",  XML_ELEMENT_HETERODYNE + ":" + XML_ATTRIBUTE_BAND_MODE,  "\n");
    _appendToFreqEditorSummaryDoc("Velocity  \t: ", XML_ELEMENT_HETERODYNE + ":" + XML_ATTRIBUTE_VELOCITY,   "\n");
    _appendToFreqEditorSummaryDoc("Band      \t: ", XML_ELEMENT_HETERODYNE + ":" + XML_ATTRIBUTE_BAND,       "\n");
    _appendToFreqEditorSummaryDoc("Molecule  \t: ", XML_ELEMENT_HETERODYNE + ":" + XML_ATTRIBUTE_MOLECULE,   "\n");
    _appendToFreqEditorSummaryDoc("Transition\t: ", XML_ELEMENT_HETERODYNE + ":" + XML_ATTRIBUTE_TRANSITION, "\n");
    _appendToFreqEditorSummaryDoc("Overlap   \t: ", XML_ELEMENT_HETERODYNE + ":" + XML_ATTRIBUTE_OVERLAP,    "\n");
    _appendToFreqEditorSummaryDoc("LO1       \t: ", XML_ELEMENT_HETERODYNE + ":" + XML_ATTRIBUTE_LO1,        "\n");
//    _appendToFreqEditorSummaryDoc("Frequency\t:", );

    int subsystemCount = _instHeterodyne.getTable().getInt(XML_ELEMENT_HETERODYNE + "." +
                                                           XML_ELEMENT_BANDSYSTEM + ":" +
							   XML_ATTRIBUTE_COUNT,
							   1);

    if((subsystemCount < 1) || (subsystemCount > 8)) {
      try {
        _freqEditorSummaryDoc.insertString(_freqEditorSummaryDoc.getLength(),
                                           "\n\nInvalid number of subsystems found: " + subsystemCount,
                                           _nameFontAttribues);
      }
      catch(BadLocationException e) {
        DialogUtil.error("Invalid number of subsystems found: " + subsystemCount);
      }

      return;
    }

    for(int i = 0; i < subsystemCount; i++) {
      _appendToFreqEditorSummaryDoc("  " + (i + 1) + ": IF = ",
                                    XML_ELEMENT_HETERODYNE + "." +
                                    XML_ELEMENT_BANDSYSTEM + "." +
                                    XML_ELEMENT_SUBSYSTEM  + "#" +
                                    i                      + ":" +
                                    XML_ATTRIBUTE_CENTRE_FREQUENCY,
				    ", ");

      _appendToFreqEditorSummaryDoc("bandWidth = ",
                                    XML_ELEMENT_HETERODYNE + "." +
                                    XML_ELEMENT_BANDSYSTEM + "." +
                                    XML_ELEMENT_SUBSYSTEM  + "#" +
                                    i                      + ":" +
                                    XML_ATTRIBUTE_BANDWIDTH,
				    ", ");


      _appendToFreqEditorSummaryDoc("",
                                    XML_ELEMENT_HETERODYNE + "." +
                                    XML_ELEMENT_BANDSYSTEM + "." +
                                    XML_ELEMENT_SUBSYSTEM  + "#" +
                                    i                      + ":" +
                                    XML_ATTRIBUTE_CHANNELS,
				    " channels\n");

    }

//      _freqEditorSummaryDoc.insertString(_freqEditorSummaryDoc.getLength(), "Frequency \t: ",               _nameFontAttribues);
//      _freqEditorSummaryDoc.insertString(_freqEditorSummaryDoc.getLength(),
//                                         _instHeterodyne.getTable().get(XML_ELEMENT_HETERODYNE + ":" + XML_ATTRIBUTE_FE_NAME) + "\n",
//					 _valueFontAttribues);
  }

  private void _appendToFreqEditorSummaryDoc(String prefix, String avTableAttr, String suffix) {
    try {
      _freqEditorSummaryDoc.insertString(_freqEditorSummaryDoc.getLength(), prefix,
                                         _nameFontAttribues);
      _freqEditorSummaryDoc.insertString(_freqEditorSummaryDoc.getLength(), _instHeterodyne.getTable().get(avTableAttr),
					 _valueFontAttribues);
      _freqEditorSummaryDoc.insertString(_freqEditorSummaryDoc.getLength(), suffix,
                                         _nameFontAttribues);
    }
    catch(BadLocationException e) {
      DialogUtil.error("Could not create summary for " + prefix +
                       " in Heterodyne settings (" + _instHeterodyne.parent().getTitle() + "): " + e);
      e.printStackTrace();
    }
  }

//  private void _appendToFreqEditorSummaryDocLn(String name, String avTableAttr) {
//    _appendToFreqEditorSummaryDoc(name, avTableAttr);
//
//    try {
//      _freqEditorSummaryDoc.insertString(_freqEditorSummaryDoc.getLength(), "\n", _valueFontAttribues);
//    }
//    catch(BadLocationException e) {
//      DialogUtil.error("Could not create summary for " + name +
//                       " in Heterodyne settings (" + _instHeterodyne.parent().getTitle() + "): " + e);
//    }    
//  }
}
