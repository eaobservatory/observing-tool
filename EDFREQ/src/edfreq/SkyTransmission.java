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
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.beans.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class SkyTransmission extends JPanel implements ChangeListener
{

   int xSize;
   int ySize;
   private double lowLimit;
   private double highLimit;
   private double halfrange;
   private double [][] skyTrans;
   private SkyData skyData;
   private int [][] skyPlot;
   private Image buffer = null;
   private Graphics ig;


   public SkyTransmission ( double lowLimit, 
     double highLimit, int xSize, int ySize )
   {
      super();

      int j;

      this.xSize = xSize;
      this.ySize = ySize;
      this.lowLimit = lowLimit;
      this.highLimit = highLimit;
      halfrange = 0.5 * ( highLimit - lowLimit );

/* Get relevant section of sky transmission and scale it ready for
   plotting */

      skyData = new SkyData();

      skyTrans = skyData.getTransmission ( lowLimit, highLimit );
      skyPlot = new int [skyTrans.length][2];
      for ( j=0; j<skyTrans.length; j++ )
      {
         skyPlot[j][0] = (int) ( 
           (double)xSize * ( skyTrans[j][0] - lowLimit ) / 
           ( highLimit - lowLimit ) );
         skyPlot[j][1] = Math.min ( ySize-1,
           (int) ( (double)ySize * ( 1.0 - skyTrans[j][1] ) ) );
      }

/* Set up display */

      setPreferredSize ( new Dimension ( xSize, ySize ) );
      setSize ( xSize, ySize );
   }

   public void paintComponent ( Graphics g )
   {
      int j;


      if ( buffer == null )
      {
         buffer = createImage ( xSize, ySize );
         
	 // added by MFO, 16 November 2001
	 if(buffer == null) {
            return;
	 }
	 
	 ig = buffer.getGraphics();
      }

      ig.setColor ( getBackground() );
      ig.fillRect ( 0, 0, xSize, ySize );
      ig.setColor ( getForeground() );


      for ( j=0; j<skyPlot.length-1; j++ )
      {
         ig.drawLine ( skyPlot[j][0], skyPlot[j][1], 
           skyPlot[j+1][0], skyPlot[j+1][1]  );
      }
      g.drawImage ( buffer, 0, 0, null );

   }

   public void stateChanged ( ChangeEvent e )
   {
      double value;
      int j;

      value = EdFreq.SLIDERSCALE * (double) ((JSlider)e.getSource()).getValue();
      lowLimit = value - halfrange;
      highLimit = value + halfrange;

      skyTrans = skyData.getTransmission ( lowLimit, highLimit );
      skyPlot = new int [skyTrans.length][2];
      for ( j=0; j<skyTrans.length; j++ )
      {
         skyPlot[j][0] = (int) ( 
           (double)xSize * ( skyTrans[j][0] - lowLimit ) / 
           ( highLimit - lowLimit ) );
         skyPlot[j][1] = Math.min ( ySize-1,
           (int) ( (double)ySize * ( 1.0 - skyTrans[j][1] ) ) );
      }

      repaint();
   }

}
