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
   double [] bandWidthsArray;
   int channels = 0;
   int [] channelsArray;
   int row;
   Vector swArray = new Vector();
   JComboBox bandWidthChoice;

   public Sampler ( double centreFrequency, double [] bandWidthsArray, int [] channelsArray, JComboBox bandWidthChoice )
   {
      int j;

      this.centreFrequency = centreFrequency;
      this.bandWidth = bandWidthsArray[0];
      this.bandWidthsArray = bandWidthsArray;
      this.channels = channelsArray[0];
      this.channelsArray = channelsArray;
      this.bandWidthChoice = bandWidthChoice;

      bandWidthChoice.addItemListener ( this );
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

   /**
    * Sets new band width and sets the band width choice box to bandWidthStr.
    *
    * This method allows the FrontEnd panel to set a band width which restes the
    * band widths of the individual Samplers.
    *
    * @see #setBandWidth(double)
    */
   public void setBandWidthAndGui( String bandWidthStr )
   {
      setBandWidth(Double.parseDouble(bandWidthStr) * 1.0E9);

      bandWidthChoice.removeItemListener(this);
      bandWidthChoice.setSelectedItem(bandWidthStr);
      bandWidthChoice.addItemListener(this);
   }

   /**
    * Sets band width, notifies SamplerWatchers but does <b>not</b> change the band width choice box.
    *
    * This method is called either when the band choice box of this Sampler has changed or from
    * when setBandWidthAndGui is called.
    *
    * @see #setBandWidthAndGui(java.lang.String)
    */
   public void setBandWidth(double value)
   {
      int j;

      bandWidth = value;

      if ( !swArray.isEmpty() )
      {
         for ( j=0; j<swArray.size(); j++ )
         {
            ((SamplerWatcher)swArray.elementAt(j)).updateSamplerValues ( 
              centreFrequency, bandWidth, channels );
         }
      }
   }

   /**
    * Returns the currently selected band width of this sampler.
    */
   public double getBandWidth ( )
   {
      return bandWidth;
   }

   /**
    * Returns numbers the number of channels corresponding to the
    * currently selected band width of this sampler.
    */
   public int getChannels ( )
   {
      return channels;
   }

   /**
    * Returns an array of the band width options of this sampler.
    */
   public double [] getBandWidthOptions ( )
   {
      return bandWidthsArray;
   }

   public void itemStateChanged ( ItemEvent ev )
   {
      setBandWidth(Double.parseDouble((String)bandWidthChoice.getSelectedItem()) * 1.0E9);
   }
}
