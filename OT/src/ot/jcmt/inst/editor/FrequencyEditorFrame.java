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

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import orac.util.SpItemDOM;
import orac.jcmt.inst.SpInstHeterodyne;
import edfreq.FrontEnd;
import edfreq.FrequencyEditorConstants;
import ot.util.DialogUtil;

import org.xml.sax.InputSource;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.parsers.DOMParser;
import java.io.StringReader;


/**
 * This class provides a frame for the FrontEnd panel of the Frequency Editor.
 *
 * Instead of a "Save" menu an "Apply" button is provided to store the settings to
 * a heterodyne instrument component of the OT.
 *
 * @see edfreq.FrontEnd
 * @see edfreq.FrontEndFrame
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class FrequencyEditorFrame extends JFrame implements ActionListener {

  private JButton applyButton = new JButton("Apply");
  private JButton closeButton = new JButton("Close");
  private JPanel buttonPanel  = new JPanel();

  private EdCompInstHeterodyne _heterodyneEditor;

  private FrontEnd _frontEnd = new FrontEnd();

  private String _currentXML = "";
  private SpInstHeterodyne _currentInstHeterodyne;

  public FrequencyEditorFrame(EdCompInstHeterodyne heterodyneEditor) {
    super("Frequency Editor");

    _heterodyneEditor = heterodyneEditor;

    buttonPanel.add(applyButton);
    buttonPanel.add(closeButton);

    getContentPane().add(_frontEnd, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    pack();
    setLocation(100, 100);
    _frontEnd.setSideBandDisplayLocation(200, 300);

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    applyButton.addActionListener(this);
    closeButton.addActionListener(this);

    _currentXML = _frontEnd.toXML();
  }

  public void update(SpInstHeterodyne newInstHeterodyne) {

    if((newInstHeterodyne != _currentInstHeterodyne) && (!_currentXML.equals(_frontEnd.toXML()))) {
      int userRequest = JOptionPane.showConfirmDialog(this,
                                       "Do you want to apply changes to previous Heterodyne component\n" +
				       "before starting to edit new Heterodyne component?\n" +
				       "Changes will be lost otherwise.",
                                       "Apply changes?",
				       JOptionPane.YES_NO_OPTION);
				       // CANCEL_OPTION would be difficult to support
				       // because selection would have to jump back in the
				       // Science Program tree.
				       //JOptionPane.YES_NO_CANCEL_OPTION);

      switch(userRequest) {
        case JOptionPane.YES_OPTION:
	  apply();
	  break;
	// CANCEL_OPTION: see comment above
	//case JOptionPane.CANCEL_OPTION:
	//  return;
      }
    }


    _currentInstHeterodyne = newInstHeterodyne;

    try {
      String xml = (new SpItemDOM(newInstHeterodyne)).toString();
      _frontEnd.update(xml);
      _currentXML = _frontEnd.toXML();
    }
    catch(Exception e) {
      e.printStackTrace();
      DialogUtil.error(this, "Could not update frequency editor: " + e);
    }
  }

  public void apply() {
    try {
      DOMParser parser = new DOMParser();
      parser.parse(new InputSource(new StringReader(_frontEnd.toXML())));
      ElementImpl element = (ElementImpl)parser.getDocument().getDocumentElement();

      _currentInstHeterodyne.getTable().noNotifyRmAll();
      SpItemDOM.fillAvTable("", element, _currentInstHeterodyne.getTable());

      _currentInstHeterodyne.setFrontEndName(_currentInstHeterodyne.getTable().get(
                                               FrequencyEditorConstants.XML_ELEMENT_HETERODYNE + ":" +
                                               FrequencyEditorConstants.XML_ATTRIBUTE_FE_NAME));
      _currentInstHeterodyne.getTable().edit();
    }
    catch(Exception e) {
      e.printStackTrace();
      DialogUtil.error(this, "Could not read data from frequency editor: " + e);
    }

  
    _heterodyneEditor.updateFreqEditorSummaryDoc();

    _currentXML = _frontEnd.toXML();
  }

  public void close() {
    setFrequencyEditorVisible(false);
  }

  public void setFrequencyEditorVisible(boolean visible) {
    setVisible(visible);
    _frontEnd.setSideBandDisplayVisible(visible);
  }


  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == applyButton) {
      apply();
    }

    if(e.getSource() == closeButton) {
      close();
    }
  }
}

