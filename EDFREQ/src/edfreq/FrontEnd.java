/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package edfreq;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.BoxLayout;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.Point;
import java.util.*;
import java.io.*;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;


/**
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class FrontEnd extends JPanel implements ActionListener, FrequencyEditorConstants,
                                                DocumentListener
{

   private JComboBox feChoice;
   private JComboBox feBandModeChoice;
   private String currentFE = "";
   private JPanel fePanel;
   private JPanel displayPanel;
   private JPanel rangePanel;
   private Box linePanel;
   private JPanel mol1Panel;
   private JPanel mol2Panel;
   private JPanel northPanel;
   private JLabel lowFreq;
   private JLabel highFreq;
   private JTextField velocity;
   private JTextField overlap;
   private SideBandDisplay sideBandDisplay = new SideBandDisplay(this);
   private LineCatalog lineCatalog = new LineCatalog();
   private JComboBox feBand;
   private JComboBox feMode;
   private JComboBox moleculeChoice;
   private JComboBox moleculeChoice2;
   private JTextField moleculeFrequency;
   private JTextField moleculeFrequency2;
   private JComboBox transitionChoice;
   private JComboBox transitionChoice2;
   private JScrollPane scrollPanel;
   private double redshift = 0.0;
   private double subBandWidth = 0.25E9;
   private ReceiverList receiverList = new ReceiverList();
   private double loMin;
   private double loMax;
   private double feIF;
   private double feBandWidth;
   private double feOverlap = 0.0;
   private String defaultStoreFile = "hetsetup.txt";
   private FlowLayout flowLayoutLeft  = new FlowLayout(FlowLayout.LEFT);
   private FlowLayout flowLayoutRight = new FlowLayout(FlowLayout.RIGHT);
   // Changed to avoid security exception in applet.
   private String defaultStoreDirectory = ".;"; //System.getProperty("user.dir");
   private boolean editFlag = false;
   // Commented out to avoid security exception in applet.
   // private JFileChooser fileChooser = new JFileChooser ( );

   //private JButton sideBandButton = new JButton("Show Side Band Display");

   /** Parser for XML input/output. (MFO, 29 November 2001) */
   private XMLReader _xmlReader = null;

   protected static FrequencyEditorCfg cfg = null;

   public FrontEnd ( )
   {
      this(null);
   }

   public FrontEnd ( InputStream inputStream )
   {
      if(inputStream != null) {
         cfg = FrequencyEditorCfg.getCfg(inputStream);
      }
      else {
         // Use default.
	 cfg = new FrequencyEditorCfg();
      }

      setLayout(new BorderLayout());
/* Create the choice of frontends */

      fePanel = new JPanel(flowLayoutLeft);
      fePanel.add ( new JLabel ( "Choose Front End" ) );

      feChoice = new JComboBox ( cfg.frontEnds );
      lowFreq = new JLabel ( "215" );
      lowFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
      highFreq = new JLabel ( "272" );
      highFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );

      feChoice.addActionListener ( this );

      feMode = new JComboBox ( cfg.frontEndModes );
      feBandModeChoice = new JComboBox ( );
      feBandModeChoice.addActionListener ( this );

      overlap = new JTextField ( 10 );
      overlap.setText ( "0.0" );
      overlap.addActionListener ( this );

      fePanel.add ( feChoice );
      fePanel.add ( feMode );
      fePanel.add ( feBandModeChoice );
      fePanel.add ( new JLabel ( "Overlap (MHz)" ) );
      fePanel.add ( overlap );

/* Create the display */

      displayPanel = new JPanel(flowLayoutLeft);
      displayPanel.add ( new JLabel ( "Low Limit (GHz)" ) );
      displayPanel.add ( lowFreq );
      displayPanel.add ( new JLabel ( "High Limit (GHz)" ) );
      displayPanel.add ( highFreq );
      velocity = new JTextField ( 10 );
      velocity.setText ( "0.0" );
      velocity.addActionListener ( this );

      displayPanel.add ( new JLabel ( "Velocity (Km/s)" ) );
      displayPanel.add ( velocity );

      rangePanel = new JPanel();
      feBand = new JComboBox ( new String[] { "usb", "lsb", "optimum" } );
      feBand.addActionListener ( this );

/* Main molecular line choice - used to set front-end LO1 to put the line
   in the nominated sideband */

      moleculeChoice = new JComboBox();
      moleculeChoice.setForeground ( Color.red );
      moleculeChoice.addActionListener ( this );
      transitionChoice = new JComboBox();
      transitionChoice.setForeground ( Color.red );
      transitionChoice.addActionListener ( this );
      moleculeFrequency = new JTextField();
      moleculeFrequency.setColumns(12);
      moleculeFrequency.setText ( "0.0000" );
      moleculeFrequency.setForeground ( Color.red );
      moleculeFrequency.getDocument().addDocumentListener(this);
      mol1Panel = new JPanel(flowLayoutRight);
      mol1Panel.add ( feBand );
      mol1Panel.add ( moleculeChoice );
      mol1Panel.add ( transitionChoice );
      mol1Panel.add ( moleculeFrequency );
      mol1Panel.add ( new JLabel("MHz") );

/* Secondary moleculare line choice - displayed just for convenience of
   astronomer */

      moleculeChoice2 = new JComboBox();
      moleculeChoice2.setForeground ( Color.magenta );
      moleculeChoice2.addActionListener ( this );
      transitionChoice2 = new JComboBox();
      transitionChoice2.setForeground ( Color.magenta );
      transitionChoice2.addActionListener ( this );
      moleculeFrequency2 = new JTextField();
      moleculeFrequency2.setColumns(12);
      moleculeFrequency2.setText ( "0.0000" );
      moleculeFrequency2.setForeground ( Color.magenta );
      moleculeFrequency2.getDocument().addDocumentListener(this);
      mol2Panel = new JPanel(flowLayoutRight);
      mol2Panel.add ( moleculeChoice2 );
      mol2Panel.add ( transitionChoice2 );
      mol2Panel.add ( moleculeFrequency2 );
      mol2Panel.add ( new JLabel("MHz") );

      linePanel = Box.createVerticalBox();
      linePanel.add ( mol1Panel );

      // MFO (May 03, 2002): mol2Panel is currently not used.
      //linePanel.add ( mol2Panel );

      //linePanel.add ( sideBandButton );

      //sideBandButton.addActionListener(this);

/* Assemble the display */

      rangePanel.add ( linePanel );

      northPanel = new JPanel();
      northPanel.setLayout ( new BoxLayout ( northPanel, 
        BoxLayout.Y_AXIS ) );

      northPanel.add ( fePanel );
      northPanel.add ( displayPanel );
      northPanel.add ( rangePanel );
      add ( northPanel, BorderLayout.NORTH );

      scrollPanel = new JScrollPane();
      //scrollPanel.setPreferredSize ( new Dimension ( 600, 150 ) );

      add ( scrollPanel, BorderLayout.CENTER );
   
      // MFO trigger additional initialising.
      feChoiceAction(null);
      updateSideBandDisplay();
      feMoleculeAction(null);
      feMolecule2Action(null);
      feTransition2Action(null);
      feTransitionAction(null);
   
      // Initialize parser (MFO, 29 November 2001)
      try {
         _xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
	 
	 _xmlReader.setContentHandler(new DefaultHandler() {

            public void startElement(String namespaceURI,
                                     String localName,
                                     String qName,
                                     Attributes atts) throws SAXException {

               if(qName.equals(XML_ELEMENT_HETERODYNE)) {

                  feChoice.setSelectedItem(atts.getValue(XML_ATTRIBUTE_FE_NAME));
                  feMode.setSelectedItem(atts.getValue(XML_ATTRIBUTE_MODE));
                  feBandModeChoice.setSelectedItem(getObject(feBandModeChoice, atts.getValue(XML_ATTRIBUTE_BAND_MODE)));
                  double overlapGuiValue = Double.parseDouble(atts.getValue(XML_ATTRIBUTE_OVERLAP)) / 1.0E6;
	          overlap.setText("" + overlapGuiValue);
                  velocity.setText(atts.getValue(XML_ATTRIBUTE_VELOCITY));
                  feBand.setSelectedItem(atts.getValue(XML_ATTRIBUTE_BAND));
                  moleculeChoice.setSelectedItem(getObject(moleculeChoice, atts.getValue(XML_ATTRIBUTE_MOLECULE)));
                  transitionChoice.setSelectedItem(getObject(transitionChoice, atts.getValue(XML_ATTRIBUTE_TRANSITION)));
                  moleculeChoice2.setSelectedItem(getObject(moleculeChoice2, atts.getValue(XML_ATTRIBUTE_MOLECULE2)));
                  transitionChoice2.setSelectedItem(getObject(transitionChoice2, atts.getValue(XML_ATTRIBUTE_TRANSITION2)));
	       }
            }
         });
      }
      //catch(SAXException e) {
      catch(Exception e) {
          JOptionPane.showMessageDialog(this,
	                                "Could not initialize XMLReader: " + e,
					"XMLReader problem.", JOptionPane.ERROR_MESSAGE);

      }
   }

   public void actionPerformed ( ActionEvent ae )
   {

      if ( ae.getSource() == feBand )
      {
         feBandAction ( ae );
      }
      else if ( ae.getSource() == moleculeChoice )
      {
         feMoleculeAction ( ae );
      }
      else if ( ae.getSource() == transitionChoice )
      {
         feTransitionAction ( ae );
      }
      else if ( ae.getSource() == moleculeChoice2 )
      {
         feMolecule2Action ( ae );
      }
      else if ( ae.getSource() == transitionChoice2 )
      {
         feTransition2Action ( ae );
      }
      else if ( ae.getSource() == feChoice )
      {
         feChoiceAction ( ae );
      }
      else if ( ae.getSource() == feBandModeChoice )
      {
         feBandModeChoiceAction ( ae );
      }
      else if ( ae.getSource() == velocity )
      {
         feVelocityAction ( ae );
      }
      else if ( ae.getSource() == overlap )
      {
         feOverlapAction ( ae );
      }
   }

   public void changedUpdate(DocumentEvent e) { }

   public void insertUpdate(DocumentEvent e) {
      moleculeFrequencyChanged();
      moleculeFrequency2Changed();
   }
  
   public void removeUpdate(DocumentEvent e) {
      moleculeFrequencyChanged();
      moleculeFrequency2Changed();
   }



   public void feBandAction ( ActionEvent ae )
   {
      feTransitionAction ( ae );
   }


   public void feMoleculeAction ( ActionEvent ae )
   {
      SelectionList species = (SelectionList)moleculeChoice.getSelectedItem();

      transitionChoice.setModel ( 
        new DefaultComboBoxModel ( species.objectList ) );

      moleculeFrequency.setText ( "0.0000" );
   }


   public void feMolecule2Action ( ActionEvent ae )
   {
      SelectionList species = (SelectionList)moleculeChoice2.getSelectedItem();

      transitionChoice2.setModel ( 
        new DefaultComboBoxModel ( species.objectList ) );

      moleculeFrequency2.setText ( "0.0000" );
   }


   public void feTransitionAction ( ActionEvent ae )
   {
      Transition transition = (Transition)transitionChoice.getSelectedItem();

      if ( transition != null )
      {

         moleculeFrequency.setText ( "" + transition.frequency/1.0E6 );
         String band = (String)feBand.getSelectedItem();

         if ( sideBandDisplay != null )
         {

            sideBandDisplay.setMainLine ( transition.frequency );

            double obsFrequency = transition.frequency / 
              ( 1.0 + redshift );
            if ( band.equals ( "usb" ) )
            {
               sideBandDisplay.setLO1 ( obsFrequency - feIF );
            }
            else
            {
               sideBandDisplay.setLO1 ( obsFrequency + feIF );
            }
         }

      }
   }


   public void feTransition2Action ( ActionEvent ae )
   {
      Transition transition = (Transition)transitionChoice2.getSelectedItem();

      if ( transition != null )
      {

         moleculeFrequency2.setText ( "" + transition.frequency/1.0E6 );

         if ( sideBandDisplay != null )
         {

            sideBandDisplay.setSideLine ( transition.frequency );

         }

      }
   }


   public void moleculeFrequencyChanged()
   {
      try {
         double frequency = Double.parseDouble(moleculeFrequency.getText()) * 1.0E6;
         
	 String band = (String)feBand.getSelectedItem();

         if ( sideBandDisplay != null )
         {

            sideBandDisplay.setMainLine ( frequency );

            double obsFrequency = frequency / 
              ( 1.0 + redshift );
            if ( band.equals ( "usb" ) )
            {
               sideBandDisplay.setLO1 ( obsFrequency - feIF );
            }
            else
            {
               sideBandDisplay.setLO1 ( obsFrequency + feIF );
            }
         }

      }
      catch(NumberFormatException e) {
        // ignore
      }
   }


   public void moleculeFrequency2Changed()
   {
      try {
         double frequency = Double.parseDouble(moleculeFrequency2.getText()) * 1.0E6;
      
         if ( sideBandDisplay != null )
         {
            sideBandDisplay.setSideLine ( frequency );

         }

      }
      catch(NumberFormatException e) {
        // ignore
      }
   }


   public void feBandModeChoiceAction ( ActionEvent ae ) {
      updateSideBandDisplay();
   }

   public void updateSideBandDisplay() {
      double loRange[];
      double mid;
      int subBandCount;

      BandSpec currentBandSpec = (BandSpec)feBandModeChoice.getSelectedItem();

      if(currentBandSpec == null) {
         feBandModeChoice.setSelectedIndex(0);
	 currentBandSpec = (BandSpec)feBandModeChoice.getSelectedItem();
      }


/* Update display of sidebands and subbands */
      Point sideBandDisplayLocation = new Point(100, 100);

      mid = 0.5 * ( loMin + loMax );

      subBandCount = currentBandSpec.numBands;
      subBandWidth = currentBandSpec.loBandWidth;

      sideBandDisplay.updateDisplay ( currentFE, loMin, loMax,
        feIF, feBandWidth,
        redshift,
        currentBandSpec.loBandWidth, currentBandSpec.loChannels,
        currentBandSpec.hiBandWidth, currentBandSpec.hiChannels,
        subBandCount );
   }


   public void feChoiceAction ( ActionEvent ae )
   {

      double loRange[];
      double mid;
      int subBandCount;
      Receiver r;
      double obsmin;
      double obsmax;

      String newFE = (String)feChoice.getSelectedItem();
      currentFE = newFE;
      r = (Receiver)cfg.receivers.get ( currentFE );

      loMin = r.loMin;
      loMax = r.loMax;
      feIF = r.feIF;
      feBandWidth = r.bandWidth;

      lowFreq.setText ( "" + (int)(loMin*1.0E-9) );
      highFreq.setText ( "" + (int)(loMax*1.0E-9) );

/* Update choice of sub-band configurations */

      feBandModeChoice.setModel ( 
        new DefaultComboBoxModel ( r.bandspecs ) );


      obsmin = loMin - feIF - ( feBandWidth * 0.5 );
      obsmax = loMax + feIF + ( feBandWidth * 0.5 );

/* Update choice of molecules */

      moleculeChoice.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
          obsmax*(1.0+redshift) ) ) );
      moleculeChoice2.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
        obsmax*(1.0+redshift) ) ) );

/* Reset line frequency report */

      moleculeFrequency.setText ( "0.0000" );
      moleculeFrequency2.setText ( "0.0000" );


/* Update display of sidebands and subbands */

      updateSideBandDisplay();
      //if ( sideBandDisplay != null )
      //{
      //   sideBandDisplay.dispose ( );
      //}

   }


   public void feVelocityAction ( ActionEvent ae )
   {
      String svalue;
      double dvalue;
      double obsmin;
      double obsmax;

      svalue = velocity.getText();
      dvalue = (Double.valueOf(svalue)).doubleValue();
      redshift = dvalue / EdFreq.LIGHTSPEED;

      obsmin = loMin - feIF - ( feBandWidth * 0.5 );
      obsmax = loMax + feIF + ( feBandWidth * 0.5 );

/* Update choice of molecules */

      moleculeChoice.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
          obsmax*(1.0+redshift) ) ) );
      moleculeChoice2.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
        obsmax*(1.0+redshift) ) ) );

/* Update display of sidebands */

      if ( sideBandDisplay != null )
      {
         sideBandDisplay.setRedshift ( redshift );
      }
   }


   public void feOverlapAction ( ActionEvent ae )
   {
      String svalue;
      double dvalue;

      svalue = overlap.getText();
      dvalue = (Double.valueOf(svalue)).doubleValue();
      feOverlap = 1.0E6 * dvalue;
   }

   /**
    * Not used anymore.
    *
    * @deprecated replaced by {@link #toXML()}.
    */
   public void saveAsASCII ( PrintWriter out )
   {
      out.println ( "<heterodyne" );

      out.println ( "  feName=" + (String)feChoice.getSelectedItem() );
      out.println ( "  mode=" + (String)feMode.getSelectedItem() );
      out.println ( "  bandMode=" + 
        (feBandModeChoice.getSelectedItem()).toString() );
      out.println ( "  velocity=" + velocity.getText() );
      out.println ( "  band=" + (String)feBand.getSelectedItem() );

      String svalue = overlap.getText();
      double dvalue = (Double.valueOf(svalue)).doubleValue();
      feOverlap = 1.0E6 * dvalue;

      out.println ( "  overlap=" + feOverlap );

      out.println ( "  LO1=" + sideBandDisplay.getLO1() );

      out.println ( ">" );

      sideBandDisplay.saveAsASCII ( out );

      out.println ( "</heterodyne>" );

   }

   /**
    * Creates XML representation of the frequency editor settings.
    *
    * (MFO, November 2001)
    */
   public String toXML() {
      StringBuffer stringBuffer = new StringBuffer();
      String value = "";

      stringBuffer.append ("<" + XML_ELEMENT_HETERODYNE + "\n" );

      stringBuffer.append ("    " + XML_ATTRIBUTE_FE_NAME + "=\"" + (String)feChoice.getSelectedItem() + "\"\n");
      stringBuffer.append ("    " + XML_ATTRIBUTE_MODE + "=\"" + (String)feMode.getSelectedItem() + "\"\n");
      try {
        value = feBandModeChoice.getSelectedItem().toString();
      }
      catch(NullPointerException e) {
        value = "";
        // ignore for now.
	// Probable reason for NullPointerException: toXML is called in _updateWidgets of another class
	// and many things in the frequency editor are currently only initialised after the user has made
	// some choices/settings.
      }
      stringBuffer.append("    " + XML_ATTRIBUTE_BAND_MODE + "=\"" + value + "\"\n");

      stringBuffer.append("    " + XML_ATTRIBUTE_VELOCITY + "=\"" + velocity.getText() + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_BAND + "=\"" + (String)feBand.getSelectedItem() + "\"\n");

      String svalue = overlap.getText();
      double dvalue = (Double.valueOf(svalue)).doubleValue();
      feOverlap = 1.0E6 * dvalue;

      stringBuffer.append("    " + XML_ATTRIBUTE_OVERLAP + "=\"" + feOverlap + "\"\n");

      try {
	value = "" + sideBandDisplay.getLO1();
      }
      catch(NullPointerException e) {
        value = "";
        // ignore for now.
	// Probable reason for NullPointerException: toXML is called in _updateWidgets of another class
	// and many things in the frequency editor are currently only initialised after the user has made
	// some choices/settings.
      }
      stringBuffer.append("    " + XML_ATTRIBUTE_LO1 + "=\"" + value + "\"\n");

      stringBuffer.append("    " + XML_ATTRIBUTE_MOLECULE    + "=\"" + moleculeChoice.getSelectedItem()     + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_TRANSITION  + "=\"" + transitionChoice.getSelectedItem()   + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_MOLECULE_FREQUENCY  + "=\"" + moleculeFrequency.getText()  + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_MOLECULE2 + "=\"" + moleculeChoice2.getSelectedItem()      + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_TRANSITION2 + "=\"" + transitionChoice2.getSelectedItem()  + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_MOLECULE_FREQUENCY2 + "=\"" + moleculeFrequency2.getText() + "\"");
      stringBuffer.append(">\n");

      stringBuffer.append(sideBandDisplay.toXML() + "\n");

      stringBuffer.append ( "</" + XML_ELEMENT_HETERODYNE + ">\n" );

      return stringBuffer.toString();
   }

   /**
    * Set FrequencyTable from XML.
    *
    * Added by Martin Folger (14 November 2001)
    */
   public void update(String xml) throws Exception
   {
      // If there is no xml then trigger reset.
      if((xml == null) || xml.trim().equals("")) {
         feChoice.setSelectedIndex(0);
	 return;
      }
 
      //try {
        _xmlReader.parse(new InputSource(new StringReader(xml)));
      //}
      //catch(Exception e) {
         // There seems to be a class cast exception at the first attempt to parse which
	 // disappears at the second attempt.
	 // This problem will disappear with jdk1.4
	// System.out.println("FrontEnd.update(): SAXParser.parse bug in jdk1.3. Ignore.");
	 //_parser.parse(new InputSource(new StringReader(xml)));
      //}

      updateSideBandDisplay();
      sideBandDisplay.update(xml);
   }

   public static void main ( String args[] )
   {
      new FrontEndFrame(new FrontEnd());
   }


   private Object getObject(JComboBox comboBox, String name) {
     for(int i = 0; i < comboBox.getItemCount(); i++) {
       if(comboBox.getItemAt(i).toString().equals(name)) {
         return comboBox.getItemAt(i);
       }
     }

     return null;
   }

   public void setSideBandDisplayVisible(boolean visible) {
      sideBandDisplay.setVisible(visible);
   }

   public void setSideBandDisplayLocation(int x, int y) {
      sideBandDisplay.setLocation(x, y);
   }

   // Added by MFO (8 January 2002)
   /**
    * Returns "usb" (Upper Side Band) or "lsb" (Lower Side Band).
    *
    * Needed by {@link edfreq.SideBand} to shift LO1 when top SideBands are changed.
    */
   protected String getFeBand() {
      return (String)feBand.getSelectedItem();
   }
}
