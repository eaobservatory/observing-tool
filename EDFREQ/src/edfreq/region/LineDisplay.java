/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2003                   */
/*                                                              */
/*==============================================================*/
// $Id$

package edfreq.region;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;

import edfreq.EdFreq;
import edfreq.EmissionLines;
import edfreq.GraphScale;
import edfreq.FrequencyEditorCfg;
import edfreq.Values;

/**
 * Like SideBandDisplay but without the sidebands themselves.
 *
 * Displays emission lines, target scale and local scale. The code is based on
 * edfreq.SideBandDisplay.
 *
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class LineDisplay extends JPanel implements Observer
{
   private EmissionLines el;
   private GraphScale localScale;
   private GraphScale targetScale;
   private double _redshift;
   
   private double _lRangeLimit;
   private double _uRangeLimit;
   private double _feIF;
   private double _feBandWidth;

   private boolean _ignoreEvents = false;

   private int _displayWidth = Values.DISPLAY_WIDTH ;

   public void updateDisplay (
     double lRangeLimit, double uRangeLimit, 
     double feIF, double feBandWidth,
     double redshift)
   {
      _redshift = redshift;
      _uRangeLimit = uRangeLimit;
      _lRangeLimit = lRangeLimit;
      _feIF        = feIF;
      _feBandWidth = feBandWidth;

      updateDisplay();
   }

   public void updateDisplay() {
      removeAll();

      // This number is probably arbitrary. The EmissionLines constructor
      // will probably not not need to know about the samplerCount.
      int samplerCount = 1;

      int j;

      double mid    = 0.5 * ( _lRangeLimit + _uRangeLimit );
      double lowIF  = mid - _feIF - ( _feBandWidth * 0.5 );
      double highIF = mid + _feIF + ( _feBandWidth * 0.5 );

      
//       System.out.println("LineDisplay: Getting emission lines with parameters:");
//       System.out.println("\tlRangeLimit="+_lRangeLimit);
//       System.out.println("\tuRangeLimit="+_uRangeLimit);
//       System.out.println("\tfeIF="+_feIF);
//       System.out.println("\tfeBandWidth="+_feBandWidth);
//       System.out.println("\tlowIF="+lowIF);
//       System.out.println("\thighIF="+highIF);
//       System.out.println("\tredshift="+_redshift);
//       System.out.println("\tdisplayWidth="+_displayWidth);
//       System.out.println("\tsamplerCount="+samplerCount);
      el = new EmissionLines ( lowIF, highIF, _redshift, 
        _displayWidth, 20, samplerCount );
      //System.out.println("el=" + el);

      targetScale = new GraphScale ( lowIF, highIF, 
        1.0E9, 0.1E9, _redshift, 9, _displayWidth, JSlider.HORIZONTAL );
      localScale = new GraphScale ( lowIF, highIF, 
        1.0E9, 0.1E9, 0.0, 9, _displayWidth, JSlider.HORIZONTAL );

      setLayout(new GridLayout(3, 1));
      add(el);
      add(targetScale);
      add(localScale);
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
      _redshift = redshift;

      if(el != null) {
         el.setRedshift ( redshift );
      }

      if(targetScale != null) {
         targetScale.setRedshift ( redshift );
      }
   }

   /**
    * Returns number of pixels per GHz.
    */
   public double getPixelsPerValue() {
      return 1.0E9 * ((double)_displayWidth) / ( _uRangeLimit - _lRangeLimit );
   }

   public double getLowerRangeLimit() {
     return _lRangeLimit;
   }

   public double getUpperRangeLimit() {
     return _lRangeLimit;
   }

   public double getLO1() {
     return (_lRangeLimit + _uRangeLimit) / 2.0;
   }

   public int getDisplayWidth() {
     return _displayWidth;
   }

   public void setDisplayWidth(int displayWidth) {
     _displayWidth = displayWidth;

     updateDisplay();
   }

   public void update(Observable o, Object arg) {
      if(arg == null) {
        setSideLine(-1);

        return;
      }

      int mouseX = ((MouseEvent)arg).getX();

      // Calculate for upper side band
      double mouseDragLine = getLO1() + (_feIF - (0.5 * _feBandWidth)) + ((mouseX / getPixelsPerValue()) * 1.0E9);

      // If the line is supposed to be in the lower sideband then shift it there.
      if(((RangeBar)((MouseEvent)arg).getSource()).getAssociatedSideBand() == EdFreq.SIDE_BAND_LSB) {
        mouseDragLine -= 2.0 * _feIF;
      }

      // Convert to rest frequency space
      mouseDragLine *= ( 1.0 + _redshift );

      setSideLine(mouseDragLine);
   }

   public static void main(String args[]) 
   {

      JFrame frame = new JFrame("Line Display");
      frame.setResizable(false);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


      // Create LineDisplay with anonymous HeterodyneEditor implementation
      // that does not do anything.
      LineDisplay lineDisplay = new LineDisplay ();

      lineDisplay.updateDisplay(365.0E+9, 375.0E+9, 4.0E9, 1.8E9, 0.0);
      lineDisplay.setMainLine(369.907439E+9);

      frame.getContentPane().add(lineDisplay);
      frame.setLocation(100, 100);
      frame.pack();
      frame.setVisible(true);
   }
}
