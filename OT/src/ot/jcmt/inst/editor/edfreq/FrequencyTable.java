/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.inst.editor.edfreq;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class FrequencyTable extends JPanel
{

   private double lLowLimit;
   private double lHighLimit;
   private double uLowLimit;
   private double uHighLimit;
   Object[] samplers;
   Object[][] data;


   public FrequencyTable ( double feIF, double feBandWidth,
     double loBandWidth, int loChannels, 
     double hiBandWidth, int hiChannels, 
     int samplerCount, int displayWidth ) 
   {

      super ( );


      JPanel[] columns = new JPanel[6];

      int j;
      int i;
      JScrollBar lowBar;
      JScrollBar highBar;
      JToggleButton widthButton;
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
      samplers = new Object [samplerCount];

      for ( j=0; j<samplerCount; j++ )
      {
         lowBar = new JScrollBar ( JScrollBar.HORIZONTAL, 
           (int)( gigToPix * (-feIF-0.5*loBandWidth) ), 
           (int)( gigToPix * loBandWidth ), 
           (int)( gigToPix * lLowLimit ) + 20, 
           (int)( gigToPix * lHighLimit ) - 20 );
         lowBar.setUnitIncrement ( 1 );
         highBar = new JScrollBar ( JScrollBar.HORIZONTAL,
           (int)( gigToPix * (feIF-0.5*loBandWidth) ), 
           (int)( gigToPix * loBandWidth), 
           (int)( gigToPix * uLowLimit ) + 20, 
           (int)( gigToPix * uHighLimit ) - 20 );
         highBar.setUnitIncrement ( 1 );

         samplerDisplay = new SamplerDisplay ( String.valueOf ( feIF ) );
         resolutionDisplay = new ResolutionDisplay ( loChannels,
           loBandWidth );
         widthButton = new JToggleButton ( "" + ( loBandWidth * 1.0E-9 ) );
         samplers[j] = new Sampler ( feIF, loBandWidth, hiBandWidth, 
           loChannels, hiChannels, widthButton );

         data[j][0] = new SideBand ( lLowLimit, lHighLimit, 
           loBandWidth, -feIF, (Sampler)samplers[j], 
           lowBar, gigToPix );
         data[j][1] = samplers[j];
         data[j][2] = new SideBand ( uLowLimit, uHighLimit, 
           loBandWidth, feIF, (Sampler)samplers[j],
            highBar, gigToPix );

         ((Sampler)samplers[j]).addSamplerWatcher ( 
           (SamplerWatcher)data[j][0] );
         ((Sampler)samplers[j]).addSamplerWatcher ( 
           (SamplerWatcher)samplerDisplay );
         ((Sampler)samplers[j]).addSamplerWatcher ( 
           (SamplerWatcher)resolutionDisplay );
         ((Sampler)samplers[j]).addSamplerWatcher ( 
           (SamplerWatcher)data[j][2] );

         columns[0].add ( lowBar );
         columns[2].add ( samplerDisplay );
         columns[3].add ( widthButton );
         columns[4].add ( resolutionDisplay );
         columns[5].add ( highBar );
      }

  }

   public void saveAsASCII ( PrintWriter out )
   {
      int j;

      out.println ( "  <bandSystem" );

      out.println ( "    count=" + samplers.length );
      out.println ( "  >" );

      for ( j=0; j<samplers.length; j++ )
      {
         out.println ( "    <subSystem" );

         out.println ( "      bandNum=" + j );
         out.println ( "      centreFrequency=" +
           ((Sampler)samplers[j]).getCentreFrequency() );
         out.println ( "      bandWidth=" + 
           ((Sampler)samplers[j]).getBandWidth() );
         out.println ( "      channels=" + 
           ((Sampler)samplers[j]).getChannels() );

         out.println ( "    />" );
      }
      out.println ( "  </bandSystem>" );
   }


}
