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

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class Sampler implements ItemListener
{
   double centreFrequency;
   double bandWidth;
   double lBandWidth;
   double hBandWidth;
   int channels = 0;
   int lChannels;
   int hChannels;
   int row;
   Vector swArray = new Vector();
   JToggleButton jtb;

   public Sampler ( double centreFrequency, double lBandWidth, 
     double hBandWidth, int lChannels, int hChannels, JToggleButton jtb )
   {
      int j;

      this.centreFrequency = centreFrequency;
      this.bandWidth = lBandWidth;
      this.lBandWidth = lBandWidth;
      this.hBandWidth = hBandWidth;
      this.channels = lChannels;
      this.lChannels = lChannels;
      this.hChannels = hChannels;
      this.jtb = jtb;
      jtb.addItemListener ( this );
   }

   public void addSamplerWatcher ( SamplerWatcher sw )
   {
      swArray.addElement ( sw );
   }

   public void setCentreFrequency ( double centreFrequency )
   {
      int j;

      this.centreFrequency = centreFrequency;
      if ( !swArray.isEmpty() )
      {
         for ( j=0; j<swArray.size(); j++ )
         {
            ((SamplerWatcher)swArray.elementAt(j)).updateSamplerValues ( 
              centreFrequency, bandWidth, channels );
         }
      }
   }

   public double getCentreFrequency ( )
   {
      return centreFrequency;
   }

   public void setBandWidth ( double bandWidth )
   {
      this.bandWidth = bandWidth;
   }

   public double getBandWidth ( )
   {
      return bandWidth;
   }

   public int getChannels ( )
   {
      return channels;
   }

   public void itemStateChanged ( ItemEvent ev )
   {
      int j;

      if ( bandWidth == lBandWidth )
      {
         bandWidth = hBandWidth;
         channels = hChannels;
         jtb.setText ( "" + ( bandWidth * 1.0E-9 ) );
      }
      else
      {
         bandWidth = lBandWidth;
         channels = lChannels;
         jtb.setText ( "" + ( bandWidth * 1.0E-9 ) );
      }

      if ( !swArray.isEmpty() )
      {
         for ( j=0; j<swArray.size(); j++ )
         {
            ((SamplerWatcher)swArray.elementAt(j)).updateSamplerValues ( 
              centreFrequency, bandWidth, channels );
         }
      }
   }
}
