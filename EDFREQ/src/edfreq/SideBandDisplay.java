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


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class SideBandDisplay extends JFrame implements ChangeListener
{

   private double subBandWidth;
   private int displayWidth = 800;
   private JSlider slider;
   private EmissionLines el;
   private GraphScale localScale;
   private GraphScale targetScale;
   private Box targetPanel;
   private Box dataPanel;
   private Box titlePanel;
   private Box contentPanel;
   private double redshift;
   private FrequencyTable jt;
   private HeterodyneEditor hetEditor;
   private Container contentPane;
   private JPanel area1;
   private JPanel area2;
   private JPanel area3;
   private JPanel area4;
   
   private double _lo1;
   private double _lRangeLimit;
   private double _uRangeLimit;

   private boolean _ignoreEvents = false;

   public SideBandDisplay (HeterodyneEditor hetEditor) {   
      super ( "Frequency editor" );
      setResizable ( false );

      setDefaultCloseOperation(HIDE_ON_CLOSE);
      contentPane = getContentPane();

      this.hetEditor = hetEditor;
   }

   public void updateDisplay ( String feName, 
     double lRangeLimit, double uRangeLimit, 
     double feIF, double feBandWidth,
     double redshift, 
     double [] bandWidths, int [] channels, 
     int samplerCount )
   {

      setTitle ( "Frequency editor: front end = " + feName );

      int j;

      this.redshift = redshift;

      _uRangeLimit = uRangeLimit;
      _lRangeLimit = lRangeLimit;

      contentPanel = Box.createHorizontalBox();
      contentPanel.add ( Box.createHorizontalGlue() );
      dataPanel = Box.createVerticalBox();
      titlePanel = Box.createVerticalBox();

      this.subBandWidth = bandWidths[0];

      double mid = 0.5 * ( lRangeLimit + uRangeLimit );
      double lowIF = mid - feIF - ( feBandWidth * 0.5 );
      double highIF = mid + feIF + ( feBandWidth * 0.5 );

      el = new EmissionLines ( lowIF, highIF, redshift, 
        displayWidth, 20, samplerCount );

      jt = new FrequencyTable ( feIF, feBandWidth,
        bandWidths, channels,
        samplerCount, displayWidth, this, hetEditor, el);

      dataPanel.add ( jt, BorderLayout.CENTER );

      SkyTransmission st = new SkyTransmission ( lowIF, highIF,  
        displayWidth, 80 );
      
      double pixelsPerValue = 1.0E9 * 800.0 / ( uRangeLimit - lRangeLimit );

      targetScale = new GraphScale ( lowIF, highIF, 
        1.0E9, 0.1E9, redshift, 9, 800, JSlider.HORIZONTAL );
      localScale = new GraphScale ( lowIF, highIF, 
        1.0E9, 0.1E9, 0.0, 9, 800, JSlider.HORIZONTAL );

/* Create slider for Front-end local oscillator  */

      int centre = (int) ( mid / EdFreq.SLIDERSCALE );
      int lslide = (int) ( lRangeLimit / EdFreq.SLIDERSCALE );
      int uslide = (int) ( uRangeLimit / EdFreq.SLIDERSCALE );
      slider = new JSlider ( JSlider.HORIZONTAL, lslide, uslide, 
        centre );
      slider.setMinorTickSpacing ( (int)( 1.0E9 / EdFreq.SLIDERSCALE ) );
      slider.setMajorTickSpacing ( (int)( 10.0E9 / EdFreq.SLIDERSCALE ) );
      slider.setPaintTicks ( true );
      slider.setPaintLabels ( true );

/* Create labels for slider at 10GHz intervals */

      Hashtable labels = new Hashtable();
      for ( j=lslide; j<=uslide; j+=(int)( 10.0E9 / EdFreq.SLIDERSCALE ) ) 
      {
         labels.put ( new Integer ( j ), 
           new JLabel ( "" + j / ( (int)( 1.0E9 / EdFreq.SLIDERSCALE ) ), 
           SwingConstants.CENTER ) );
      }

      slider.setLabelTable ( labels );

/* Make the graphics follow the slider */

      slider.addChangeListener ( (ChangeListener)el );
      slider.addChangeListener ( (ChangeListener)st );
      slider.addChangeListener ( (ChangeListener)targetScale );
      slider.addChangeListener ( (ChangeListener)localScale );
      slider.addChangeListener   ( this );

      targetPanel = Box.createVerticalBox();
      targetPanel.add ( Box.createVerticalGlue() );
      targetPanel.add ( el );
      targetPanel.add ( targetScale );

      Box scales = Box.createVerticalBox();
      scales.add ( Box.createVerticalGlue() );
      scales.add ( localScale );
      scales.add ( slider );

      Box plot = Box.createVerticalBox();
      plot.add ( targetPanel );
      plot.add ( st );
      plot.add ( scales );


      dataPanel.add ( plot );

      area1 = new JPanel();
      area2 = new JPanel();
      area3 = new JPanel ( new BorderLayout() );
      area4 = new JPanel ( new BorderLayout() );


      JLabel label1 = new JLabel ( "Subsystems" );
      area1.add ( label1 );

      area1.setPreferredSize ( 
        new Dimension ( 100, (jt.getPreferredSize()).height ) );
      area1.setSize ( 
        new Dimension ( 100, (jt.getPreferredSize()).height ) );
      titlePanel.add ( area1 );

      JLabel label2 = new JLabel ( "Emission lines" );
      area2.add ( label2 );
      area2.setPreferredSize ( 
        new Dimension ( 100, (targetPanel.getPreferredSize()).height ) );
      area2.setSize ( 
        new Dimension ( 100, (targetPanel.getPreferredSize()).height ) );
      titlePanel.add ( area2 );

      JLabel label3 = new JLabel ( "Atm. Transm.",
        SwingConstants.CENTER );
      area3.add ( label3, BorderLayout.CENTER );

      GraphScale gs = new GraphScale ( 0.0, 1.1, 0.5, 0.1, 0.0, 0, 
        (st.getPreferredSize()).height, JSlider.VERTICAL );
      area3.add ( gs, BorderLayout.EAST );

      area3.setPreferredSize ( 
        new Dimension ( 100, (st.getPreferredSize()).height ) );
      area3.setSize ( 
        new Dimension ( 100, (st.getPreferredSize()).height ) );
      titlePanel.add ( area3 );

      JLabel label4 = new JLabel ( "FE Freq",
       SwingConstants.CENTER );
      JLabel label5 = new JLabel ( "LO1", SwingConstants.CENTER  );
      area4.add ( label4, BorderLayout.NORTH );
      area4.add ( label5, BorderLayout.CENTER );
      area4.setPreferredSize ( 
        new Dimension ( 100, (scales.getPreferredSize()).height ) );
      area4.setSize ( 
        new Dimension ( 100, (scales.getPreferredSize()).height ) );
      titlePanel.add ( area4 );

      contentPanel.add ( titlePanel );
      contentPanel.add ( dataPanel );
      contentPane.removeAll();
      contentPane.add ( contentPanel, BorderLayout.CENTER );

      pack();


   }


   public void setLO1 ( double lo1 )
   {
      _lo1 = lo1;

      if(_lo1 < _lRangeLimit) {
         _lo1 = _lRangeLimit;
      }

      if(_lo1 > _uRangeLimit) {
         _lo1 = _uRangeLimit;
      }

      _ignoreEvents = true;

      if(slider != null) {
         int centre = Math.round ( (float)(_lo1 / EdFreq.SLIDERSCALE) );
         slider.setValue ( centre );
      }

      _ignoreEvents = false;
   }

   public double getLO1 ( )
   {
      return _lo1; //(double)slider.getValue ( ) * EdFreq.SLIDERSCALE;
   }

   public void setMainLine ( double frequency )
   {
      if(el != null) {
         el.setMainLine ( frequency );
      }
   }

   public void setSideLine ( double frequency )
   {
      if(el != null) {
         el.setSideLine ( frequency );
      }
   }

   public void setRedshift ( double redshift )
   {
      this.redshift = redshift;

      if(el != null) {
         el.setRedshift ( redshift );
      }

      if(targetScale != null) {
         targetScale.setRedshift ( redshift );
      }
   }


   public double getTopSubSystemCentreFrequency() {
      return ((Sampler)jt.getSamplers()[0]).getCentreFrequency();
   }

   public int getNumSubSystems() {
      return jt.getSamplers().length;
   }

   public void setCentreFrequency(double centre, int subsystem) {
      jt.getSamplers()[subsystem].setCentreFrequency(centre);
   }

   public void setBandWidth(double width, int subsystem) {
      jt.getSamplers()[subsystem].setBandWidth(width);
   }

   public void setLineText(String lineText, int subsystem) {
      jt.setLineText(lineText, subsystem);
   }

   public void resetModeAndBand(String mode, String band)
   {
      jt.resetModeAndBand(mode, band);
   }

   public void initModeAndBand(String mode, String band)
   {
      jt.initModeAndBand(mode, band);
   }

   public static void main(String args[]) 
   {
      // Create SideBandDisplay with anonymous HeterodyneEditor implementation
      // that does not do anything.
      SideBandDisplay sbt = new SideBandDisplay ( new HeterodyneEditor() {
         public String getFeBand() { return "usb"; }
         public String getMode() { return "dsb"; }
         public double getRedshift() { return 0.0; }
         public double getRestFrequency(int subsystem) { return 0.0; }
         public double getObsFrequency(int subsystem) { return 0.0; }
         public void updateCentreFrequency(double centre, int subsystem) { }
         public void updateBandWidth(double width, int subsystem) { }
         public void updateChannels(double channels, int subsystem) { }
         public void updateLineDetails(LineDetails lineDetails, int subsystem) { }
         public void updateLO1(double lo1) { }
      } );

      sbt.updateDisplay( "Frequency editor test",
        365.0E+9, 375.0E+9, 4.0E9, 1.8E9, 0.0,
        new double[] { 0.25E9, 1.0E9 }, new int [] { 8192, 2048 }, 8 );
      sbt.setVisible(true);
   }


   public void stateChanged(ChangeEvent e) {
      if(_ignoreEvents) {
         return;
      }

      _lo1 = (double)slider.getValue ( ) * EdFreq.SLIDERSCALE;

      hetEditor.updateLO1(getLO1());
   }
}
