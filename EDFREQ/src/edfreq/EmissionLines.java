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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class EmissionLines extends JPanel implements MouseListener,
ChangeListener
{

   int xSize;
   int ySize;
   JPopupMenu popup = null;
   JMenuItem item;
   private double lowLimit;
   private double highLimit;
   private double halfrange;
   private double restHalfrange;
   private LineDetails [] lineStore;
   private LineCatalog lineCatalog;
   private Image buffer = null;
   private Graphics ig;
   private double mainLineFreq = 0;
   private int mainLinePos = -1;
   private double sideLineFreq = 0;
   private int sideLinePos = -1;
   private double redshift;
   private double restLowLimit;
   private double restHighLimit;


   public EmissionLines ( double lowLimit, 
     double highLimit, double redshift, 
     int xSize, int ySize )
   {
      super();

      int j;

      this.xSize = xSize;
      this.ySize = ySize;
      restLowLimit = lowLimit;
      restHighLimit = highLimit;

      this.lowLimit = restLowLimit * ( 1.0 + redshift );
      this.highLimit = restHighLimit * ( 1.0 + redshift );
      this.redshift = redshift;

      restHalfrange = 0.5 * ( highLimit - lowLimit );
      halfrange = 0.5 * ( this.highLimit - this.lowLimit );

/* Select the emission lines within the frequency range */

      lineStore = new LineDetails[xSize];
      lineCatalog = new LineCatalog();

      for ( j=0; j<xSize; j++ )
      {
         lineStore[j] = null;
      }

      lineCatalog.returnLines ( this.lowLimit, this.highLimit, xSize, 
        lineStore );

/* Set up the graphics */

      setPreferredSize ( new Dimension ( xSize, ySize ) );
      setSize ( xSize, ySize );
      addMouseListener ( this );
   }


   public void setRedshift ( double redshift )
   {
      this.redshift = redshift;
      lowLimit = restLowLimit * ( 1.0 + redshift );
      highLimit = restHighLimit * ( 1.0 + redshift );
      halfrange = 0.5 * ( highLimit - lowLimit );
      updateLines();
      repaint();
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

      for ( j=0; j<xSize; j++ )
      {
         if ( lineStore[j] != null )
         {
            ig.drawLine ( j, 0, j, ySize );
         }
      }
      if ( mainLinePos >= 0 )
      {
         ig.setColor ( Color.red );
         ig.drawLine ( mainLinePos, 0, mainLinePos, ySize );
      }
      if ( sideLinePos >= 0 )
      {
         ig.setColor ( Color.magenta );
         ig.drawLine ( sideLinePos, 0, sideLinePos, ySize );
      }

      g.drawImage ( buffer, 0, 0, null );
   }

   public void mousePressed ( MouseEvent e )
   {
      int j;
      int xPos;

      j = e.getX();
      xPos = j;
      if ( xPos > xSize/2 )
      {
         xPos = xPos - 200;
      }

      if ( lineStore[j] != null )
      {
         item = new JMenuItem ( lineStore[j].name + "  " +
           lineStore[j].transition );
         popup = new JPopupMenu ( );
         popup.add ( item );
         popup.show ( this, xPos, e.getY() );
      }
      else if ( j > 0 )
      {
         j--;
         if ( lineStore[j] != null )
         {
            item = new JMenuItem ( lineStore[j].name + "  " +
              lineStore[j].transition );
            popup = new JPopupMenu ( );
            popup.add ( item );
            popup.show ( this, xPos, e.getY() );
         }
      }
      else if ( j+1 < lineStore.length )
      {
         j++;
         if ( lineStore[j] != null )
         {
            item = new JMenuItem ( lineStore[j].name + "  " +
              lineStore[j].transition );
            popup = new JPopupMenu ( );
            popup.add ( item );
            popup.show ( this, xPos, e.getY() );
         }
      }
   }

   public void mouseClicked ( MouseEvent e )
   {
   }
   public void mouseEntered ( MouseEvent e )
   {
   }
   public void mouseExited ( MouseEvent e )
   {
   }
   public void mouseReleased ( MouseEvent e )
   {
      if ( popup != null )
      {
         popup.setVisible ( false );
      }
   }

   public void stateChanged ( ChangeEvent e )
   {
      double value;
      int j;

/* Find lines using allowing for velocity offset */

      value = EdFreq.SLIDERSCALE * 
        (double) ((JSlider)e.getSource()).getValue();


      restLowLimit = value - restHalfrange;
      restHighLimit = value + restHalfrange;
      lowLimit = restLowLimit * ( 1.0 + redshift );
      highLimit = restHighLimit * ( 1.0 + redshift );
      halfrange = 0.5 * ( highLimit - lowLimit );

      updateLines ( );

      repaint();
   }


   public void updateLines ( )
   {
      int j;

      for ( j=0; j<xSize; j++ )
      {
         lineStore[j] = null;
      }

      lineCatalog.returnLines ( lowLimit, highLimit, xSize, lineStore );

      if ( ( mainLineFreq > lowLimit ) && ( mainLineFreq < highLimit ) )
      {
         mainLinePos = (int) ( ((double)xSize) * 
                 ( mainLineFreq - lowLimit ) / ( highLimit - lowLimit ) );
      }
      else
      {
         mainLinePos = -1;
      }

      if ( ( sideLineFreq > lowLimit ) && ( sideLineFreq < highLimit ) )
      {
         sideLinePos = (int) ( ((double)xSize) * 
           ( sideLineFreq - lowLimit ) / ( highLimit - lowLimit ) );
      }
      else
      {
         sideLinePos = -1;
      }

   }


   public void setMainLine ( double frequency )
   {
      mainLineFreq = frequency;

      if ( ( mainLineFreq > lowLimit ) && ( mainLineFreq < highLimit ) )
      {
         mainLinePos = (int) ( ((double)xSize) * 
                 ( mainLineFreq - lowLimit ) / ( highLimit - lowLimit ) );
      }
      else
      {
         mainLinePos = -1;
      }

      repaint();
   }

   public void setSideLine ( double frequency )
   {
      sideLineFreq = frequency;

      if ( ( sideLineFreq > lowLimit ) && ( sideLineFreq < highLimit ) )
      {
         sideLinePos = (int) ( ((double)xSize) * 
           ( sideLineFreq - lowLimit ) / ( highLimit - lowLimit ) );
      }
      else
      {
         sideLinePos = -1;
      }

      repaint();

   }


}
