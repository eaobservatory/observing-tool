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
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

import java.util.Vector;


/**
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class FrequencyTable extends JPanel implements ActionListener
{

   private double lLowLimit;
   private double lHighLimit;
   private double uLowLimit;
   private double uHighLimit;
   private Sampler[] samplers;
   private Object[][] data;

   private JButton [] lineButtons;

   private SideBandDisplay sideBandDisplay;
   private HeterodyneEditor hetEditor;
   private EmissionLines emissionLines;

   private boolean _notifyHeterodyneEditor = true;

   public FrequencyTable ( double feIF, double feBandWidth,
     double [] bandWidths, int [] channels, 
     int samplerCount, int displayWidth,
     SideBandDisplay sideBandDisplay,
     HeterodyneEditor hetEditor,
     EmissionLines emissionLines)
   {

      super ( );

      this.sideBandDisplay = sideBandDisplay;
      this.hetEditor         = hetEditor;
      this.emissionLines   = emissionLines;

      JPanel[] columns = new JPanel[6];

      int j;
      int i;
      JScrollBar lowBar;
      JScrollBar highBar;
      JComboBox widthChoice;
      SamplerDisplay samplerDisplay;
      ResolutionDisplay resolutionDisplay;
      double gigToPix;


      lLowLimit = -feIF - ( feBandWidth * 0.5 );
      lHighLimit = -feIF + ( feBandWidth * 0.5 );
      uLowLimit = feIF - ( feBandWidth * 0.5 );
      uHighLimit = feIF + ( feBandWidth * 0.5 );

/* Create basic pattern of columns */

      setLayout ( new BoxLayout ( this, BoxLayout.X_AXIS ) );

      JLabel lsbTitle = new JLabel ( "LSB", SwingConstants.CENTER );
      JLabel lineTitle = new JLabel(" Line", SwingConstants.CENTER);
      JLabel loTitle = new JLabel ( "IF", SwingConstants.CENTER );
      JLabel bwTitle = new JLabel ( "BW", SwingConstants.CENTER );
      JLabel resTitle = new JLabel ( "Res (KHz)", SwingConstants.CENTER );
      JLabel usbTitle = new JLabel ( "USB", SwingConstants.CENTER );

      for ( i=0; i<6; i++ )
      {
         columns[i] = new JPanel();
         columns[i].setLayout ( new GridLayout ( samplerCount+1, 1 ) );
         add ( columns[i] );
      }

      columns[0].add ( lsbTitle );
      columns[1].add ( lineTitle );
      columns[2].add ( loTitle );
      columns[3].add ( bwTitle );
      columns[4].add ( resTitle );
      columns[5].add ( usbTitle );

/* Calculate scale factors such that lLowLimit to uHighLimit maps to 
   displayWidth graphics pixels */

      gigToPix = ((double)displayWidth) / ( uHighLimit - lLowLimit );
      int lWidth = (int) ( gigToPix * ( lHighLimit - lLowLimit ) );
      int uWidth = (int) ( gigToPix * ( uHighLimit - uLowLimit ) );
      int sWidth = 80;
      int tWidth = 80;
      int sp1Width = ( displayWidth - lWidth - uWidth - sWidth - tWidth )
        / 2;
      int sp2Width = displayWidth - lWidth - uWidth - sWidth - tWidth
        - sp1Width;
      int h = 20 * ( 1 + samplerCount );

/* Set column sizes */

      columns[0].setPreferredSize ( new Dimension ( lWidth, h ) );
      columns[0].setMaximumSize ( new Dimension ( lWidth, h ) );
      columns[1].setPreferredSize ( new Dimension ( sp1Width, h ) );
      columns[1].setMaximumSize ( new Dimension ( sp1Width, h ) );
      columns[2].setPreferredSize ( new Dimension ( tWidth, h ) );
      columns[2].setMaximumSize ( new Dimension ( tWidth, h ) );
      columns[3].setPreferredSize ( new Dimension ( sWidth, h ) );
      columns[3].setMaximumSize ( new Dimension ( sWidth, h ) );
      columns[4].setPreferredSize ( new Dimension ( sp2Width, h ) );
      columns[4].setMaximumSize ( new Dimension ( sp2Width, h ) );
      columns[5].setPreferredSize ( new Dimension ( uWidth, h ) );
      columns[5].setMaximumSize ( new Dimension ( uWidth, h ) );

/* Create the samplers and sidebands and their associated displays */

      data = new Object [samplerCount][3];
      samplers = new Sampler [samplerCount];

      lineButtons = new JButton[samplerCount];

      for ( j=0; j<samplerCount; j++ )
      {
         lowBar = new JScrollBar ( JScrollBar.HORIZONTAL, 
           (int)( gigToPix * (-feIF-0.5*bandWidths[0]) ), 
           (int)( gigToPix * bandWidths[0] ), 
           (int)( gigToPix * lLowLimit ) + 20, 
           (int)( gigToPix * lHighLimit ) - 20 );
         lowBar.setUnitIncrement ( 1 );

         highBar = new JScrollBar ( JScrollBar.HORIZONTAL,
           (int)( gigToPix * (feIF-0.5*bandWidths[0]) ), 
           (int)( gigToPix * bandWidths[0]), 
           (int)( gigToPix * uLowLimit ) + 20, 
           (int)( gigToPix * uHighLimit ) - 20 );
         highBar.setUnitIncrement ( 1 );

         // Line display added by MFO (October 16, 2002)
         if(j == 0) {
            lineButtons[j] = new JButton("See Heterodyne Editor");
            lineButtons[j].setEnabled(false);
	 }
	 else {
            lineButtons[j] = new JButton(HeterodyneEditor.NO_LINE);
	 }

	 lineButtons[j].setForeground(Color.black);
         lineButtons[j].setFont(new java.awt.Font("Dialog", 0, 10));
	 lineButtons[j].setActionCommand("" + j);
	 lineButtons[j].addActionListener(this);


         samplerDisplay = new SamplerDisplay ( String.valueOf ( feIF ) );
         resolutionDisplay = new ResolutionDisplay ( channels[0],
           bandWidths[0] );
	 Vector bandWidthItems = new Vector();
	 for(int k = 0; k < bandWidths.length; k++) {
            bandWidthItems.add("" + (Math.rint(bandWidths[k] * 1.0E-6) / 1000.0));
	 }
         widthChoice = new JComboBox ( bandWidthItems );
	 widthChoice.addItemListener(new NumberedBandWidthListener(j));
         samplers[j] = new Sampler ( feIF, bandWidths, channels, widthChoice );

         data[j][0] = new SideBand ( lLowLimit, lHighLimit, 
           bandWidths[0], -feIF, (Sampler)samplers[j], 
           lowBar, gigToPix, emissionLines );
         data[j][1] = samplers[j];
         data[j][2] = new SideBand ( uLowLimit, uHighLimit, 
           bandWidths[0], feIF, (Sampler)samplers[j],
            highBar, gigToPix, emissionLines );
         ((Sampler)samplers[j]).addSamplerWatcher ( 
           (SamplerWatcher)data[j][0] );


         ((Sampler)samplers[j]).addSamplerWatcher ( 
           (SamplerWatcher)samplerDisplay );
         ((Sampler)samplers[j]).addSamplerWatcher ( 
           (SamplerWatcher)resolutionDisplay );
         ((Sampler)samplers[j]).addSamplerWatcher ( 
           (SamplerWatcher)data[j][2] );

         NumberedSideBandListener numberedSideBandListener = new NumberedSideBandListener(j);
	 lowBar.addMouseListener(numberedSideBandListener);
	 highBar.addMouseListener(numberedSideBandListener);
	 ((SideBand)data[j][0]).addAdjustmentListener(numberedSideBandListener);
	 

         columns[0].add ( lowBar );
         columns[1].add ( lineButtons[j] );
         columns[2].add ( samplerDisplay );
         columns[3].add ( widthChoice );
         columns[4].add ( resolutionDisplay );
         columns[5].add ( highBar );
      }


      // Added by MFO (8 January 2002)
      // Only the top pair of SideBands need to have references of
      // SideBandDisplay and HeterodyneEditor because when one of them is changed
      // the LO1 needs adjusting, depending on whether "usb" or "lsb"
      // has been selected.
      ((SideBand)data[0][0]).connectTopSideBand(sideBandDisplay, hetEditor);
      ((SideBand)data[0][2]).connectTopSideBand(sideBandDisplay, hetEditor);
   }


   public Sampler [] getSamplers()
   {
      return samplers;   
   }

   public void setLineText(String lineText, int subsystem) {
      lineButtons[subsystem].setText(lineText);
   }


   public void actionPerformed(ActionEvent e)
   {
      int i = Integer.parseInt(e.getActionCommand());

      // emissionLines.getSelectedLine() can be null if no line has been selected.
      LineDetails lineDetails = emissionLines.getSelectedLine();

      if(lineDetails != null) {

         double obsFrequency = (lineDetails.frequency * 1.0E6) / 
              ( 1.0 + hetEditor.getRedshift() );

         double centreFrequency = Math.abs(obsFrequency - sideBandDisplay.getLO1());

         samplers[i].setCentreFrequency(centreFrequency);

         hetEditor.updateLineDetails(lineDetails, i);
         hetEditor.updateCentreFrequency(centreFrequency, i);


         if(lineDetails != null) {
            lineButtons[i].setText( lineDetails.name       + "  " +
                                    lineDetails.transition + "  " +
                                    lineDetails.frequency);

            lineButtons[i].setToolTipText(lineButtons[i].getText());
         }
         else {
            lineButtons[i].setText(HeterodyneEditor.NO_LINE);
         }
      }
   }


   protected void lo1Changed() {
      // Skip first button
      for(int i = 1; i < lineButtons.length; i++) {
         lineButtons[i].setText(HeterodyneEditor.NO_LINE);
      }
   }

   class NumberedBandWidthListener implements ItemListener {
      private int _number;

      NumberedBandWidthListener(int number) {
         _number = number;
      }

      public void itemStateChanged(ItemEvent e) {
         hetEditor.updateBandWidth(samplers[_number].getBandWidth(), _number);
         hetEditor.updateChannels(samplers[_number].getChannels(), _number);
      }
   }

   class NumberedSideBandListener implements MouseListener, AdjustmentListener {
      private int _number;
      private boolean _mousePressed = false;

      NumberedSideBandListener(int number) {
         _number = number;
      }

      private void _processAdjustment(boolean isRightMouseButton) {
         hetEditor.updateCentreFrequency(samplers[_number].getCentreFrequency(), _number);

         boolean lineClamped = (_number == 0) && (!isRightMouseButton);

         if(_number == 0) {
	    if(lineClamped) {
               // Skip top subsystem because it is clamped and keeps its line.
               // All other subsystem loose their lines.
               for(int i = 1; i < samplers.length; i++) {
                  hetEditor.updateLineDetails(null, i);
                  lineButtons[i].setText(HeterodyneEditor.NO_LINE);
               }
	    }
	    else {
               hetEditor.updateLineDetails(null, _number);
            }
         }
         else {
            hetEditor.updateLineDetails(null, _number);
	    lineButtons[_number].setText(HeterodyneEditor.NO_LINE);
	 }
      }

      public void mouseClicked(MouseEvent e)  { }
      public void mouseEntered(MouseEvent e)  { }
      public void mouseExited(MouseEvent e)   { }

      public void mousePressed(MouseEvent e)  {
         _mousePressed = true;
      }

      public void mouseReleased(MouseEvent e) {
         _processAdjustment(SwingUtilities.isRightMouseButton(e));

	 _mousePressed = false;
      }

      public void adjustmentValueChanged(AdjustmentEvent e) {
         if(!_mousePressed) {
            _processAdjustment(false);
	 }
      }
   }
}
