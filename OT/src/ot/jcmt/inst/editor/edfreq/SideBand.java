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
      setScaledCentre ( sideBandGui.getValue() );
   }

   public void updateSamplerValues ( double centre, double width, 
   int channels )
   {
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

      sideBandGui.addAdjustmentListener ( this );

   }

}
