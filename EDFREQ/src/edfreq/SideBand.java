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

import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class SideBand implements AdjustmentListener,  SamplerWatcher
{
   double lowLimit;
   double highLimit;
   double subBandWidth;
   double subBandCentre;
   Sampler sampler;
   JScrollBar sideBandGui;
   double pixratio;
   SideBandDisplay sideBandDisplay = null; // Added by MFO (8 January 2002)
   FrontEnd        frontEnd        = null; // Added by MFO (8 January 2002)

   private int _currentSideBandGuiValue;
   private int _currentSideBandGuiExtend;
   private int _currentSideBandGuiMinimum;
   private int _currentSideBandGuiMaximum;

   public SideBand ( double lowLimit, double highLimit, 
     double subBandWidth, double subBandCentre, 
     Sampler sampler, JScrollBar sideBandGui, double pixratio )
   { 
      this.lowLimit = lowLimit;
      this.highLimit = highLimit;
      this.subBandWidth = subBandWidth;
      this.subBandCentre = subBandCentre;
      this.sampler = sampler;
      this.sideBandGui = sideBandGui;
      this.pixratio = pixratio;
      sideBandGui.addAdjustmentListener ( this );
      sideBandGui.addMouseListener( this );
      if(!FrontEnd.cfg.centreFrequenciesAdjustable) {
         _currentSideBandGuiValue   = sideBandGui.getValue();
         _currentSideBandGuiExtend  = sideBandGui.getVisibleAmount();
         _currentSideBandGuiMinimum = sideBandGui.getMinimum();
         _currentSideBandGuiMaximum = sideBandGui.getMaximum();
      }
   }


   public void setSubBandCentre ( double subBandCentre ) 
   {
      this.subBandCentre = subBandCentre;
   }

   public double getSubBandCentre() 
   { 
      if ( highLimit < 0.0 )
      {
         subBandCentre = - sampler.getCentreFrequency();
      }
      else
      {
         subBandCentre = sampler.getCentreFrequency();
      }
      return subBandCentre;
   }

   public void setScaledCentre ( int v ) 
   { 
      setSubBandCentre ( (double)v/pixratio + 0.5 * subBandWidth );

      sampler.setCentreFrequency ( Math.abs ( subBandCentre ) );
   }

   public void updateCentreFrequency()
   {
      sampler.setCentreFrequency ( Math.abs ( subBandCentre ) );
   }


   public int getScaledCentre() 
   { 
      return (int)( ( getSubBandCentre() - 0.5 * subBandWidth ) * pixratio );
   }

   public int getScaledWidth() 
   { 
      return (int)( pixratio * subBandWidth );
   }

   public void setSubBandWidth ( double subBandWidth ) 
   {
      this.subBandWidth = subBandWidth;
   }

   public void adjustmentValueChanged ( AdjustmentEvent e) 
   {
      if(!FrontEnd.cfg.centreFrequenciesAdjustable) {
	 sideBandGui.setValues(_currentSideBandGuiValue,
                               _currentSideBandGuiExtend,
                               _currentSideBandGuiMinimum,
                               _currentSideBandGuiMaximum);
         return;
      }

      setScaledCentre ( sideBandGui.getValue() );
   }

   public void updateSamplerValues ( double centre, double width, 
   int channels )
   {
      // If the SideBand is one of the top SideBands then deal with the LO1.
      if(isTopSideBand()) {
         String band;
	 if(frontEnd != null) {
            band = frontEnd.getFeBand();
	 }
	 else {
            band = "usb";
	 }

         if ( highLimit < 0.0 )
	 { 
	    if ( band.equals("lsb") )
            {
               // old value: subBandCentre, new value: -centre
               sideBandDisplay.setLO1(sideBandDisplay.getLO1() + (subBandCentre + centre));
	    }   
         }
         else
	 {  
	    if( band.equals("usb") )
            {
               // old value: subBandCentre, new value: centre
               sideBandDisplay.setLO1(sideBandDisplay.getLO1() + (subBandCentre - centre));
            }
	 }   
      }	 
      
   
      int sc;
      int sw;

      if ( highLimit < 0.0 )
      {
         subBandCentre = - centre;
      }
      else
      {
         subBandCentre = centre;
      }

      subBandWidth = width;

      sideBandGui.removeAdjustmentListener ( this );

      sw = getScaledWidth();
      sc = getScaledCentre();
      sideBandGui.setValues ( sc, sw, (int)(pixratio*lowLimit)+20,
        (int)(pixratio*highLimit)-20 );

      if(!FrontEnd.cfg.centreFrequenciesAdjustable) {
         _currentSideBandGuiValue   = sc;
         _currentSideBandGuiExtend  = sw;
         _currentSideBandGuiMinimum = (int)(pixratio*lowLimit)+20;
         _currentSideBandGuiMaximum = (int)(pixratio*highLimit)-20;
      }

      sideBandGui.addAdjustmentListener ( this );
   }

   protected void connectTopSideBand(SideBandDisplay sideBandDisplay, FrontEnd frontEnd) {
      this.sideBandDisplay = sideBandDisplay;
      this.frontEnd        = frontEnd;
   }

   protected boolean isTopSideBand() {
      return (sideBandDisplay != null);
   }
}
