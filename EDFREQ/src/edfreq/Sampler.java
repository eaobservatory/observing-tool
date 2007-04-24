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
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class Sampler implements ItemListener
{
   double centreFrequency;
   double bandWidth;

   /** Front end IF. */
   double feIF;

   /** Instantaneous bandwidth of frontend. */
   double feBandWidth;

    /** Number of mixers selected on heterodyne panel */
    int nMixers = 1;

   /**
    * Bandwidth options for this sampler.
    *
    * The bandwidth options in this array are not necessarily the actual bandwidths of the instrument.
    * The bandwidths may have been reduced by a factor (derived form the overlap).
    * It is important that the number of channels in {@link #channelsArray} is reduced by the same factor in
    * order to get valid values for the resolution.
    */
   double [] bandWidthsArray;

   int channels = 0;

   /**
    * Channel options for this sampler.
    *
    * The channel options in this array are not necessarily the actual number of channels of the instrument.
    * The channels may have been reduced by a factor (derived form the overlap).
    * It is important that the number of channels is reduced by the same factor by which
    * the bandwidths in {@link #bandWidthsArray} have been reduced in order to get valid values
    * for the resolution.
    */
   int [] channelsArray;

   int row;
   Vector swArray = new Vector();
   JComboBox bandWidthChoice;

   public Sampler ( double centreFrequency, double feBandWidth,
                    double [] bandWidthsArray, int [] channelsArray,
                    JComboBox bandWidthChoice )
   {
      int j;

      this.centreFrequency = centreFrequency;
      this.feIF = centreFrequency;
      this.feBandWidth = feBandWidth;
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

   /**
	 * Set centre frequency to a value in the allowed range.
	 *
	 * The centre frequency is adjusted if necessary so that the sideband fits into the frontend bandwidth.
	 */
	public void setCentreFrequency( double centreFrequency )
	{
		int j;

		this.centreFrequency = centreFrequency;

		if( FrequencyEditorCfg.getConfiguration().centreFrequenciesAdjustable )
		{
			/*
			 * Check whether the centreFrequency has to be adjusted in order to
			 * make the new bandWidth fit into the frontend bandwidth.
			 */
			if( centreFrequency > ( feIF + ( 0.5 * ( feBandWidth - bandWidth ) ) ) )
				this.centreFrequency = ( feIF + ( 0.5 * ( feBandWidth - bandWidth ) ) ) ;
			else if( centreFrequency < ( feIF - ( 0.5 * ( feBandWidth - bandWidth ) ) ) )
				this.centreFrequency = ( feIF - ( 0.5 * ( feBandWidth - bandWidth ) ) ) ;
		}

		if( !swArray.isEmpty() )
		{
			for( j = 0 ; j < swArray.size() ; j++ )
				( ( SamplerWatcher )swArray.elementAt( j ) ).updateSamplerValues( this.centreFrequency , bandWidth , channels );
		}
	}

   public double getCentreFrequency ( )
   {
      return centreFrequency;
   }


   /**
    * Sets band width, notifies SamplerWatchers but does <b>not</b> change the band width choice box.
    */
   public void setBandWidth(double value)
   {
      int j;

      bandWidth = value;

      // Find index of new bandwidth and  set channels from channels array.
      for(int i = 0; i < bandWidthsArray.length; i++) {
         if(bandWidthsArray[i] == value) {
            channels = channelsArray[i];
         }
      }

      // Adjust centre frequency if necessary so that the sideband fits into the frontend bandwidth.
      setCentreFrequency(getCentreFrequency());

      // The above call to setCentreFrequency triggers a notification of the SamplerWatchers.
      // So the following can be commented out.

//      if ( !swArray.isEmpty() )
//      {
//         for ( j=0; j<swArray.size(); j++ )
//         {
//            ((SamplerWatcher)swArray.elementAt(j)).updateSamplerValues ( 
//              centreFrequency, bandWidth, channels );
//         }
//      }

      bandWidthChoice.removeItemListener(this);
      bandWidthChoice.setSelectedItem("" + (Math.rint(value * 1.0E-6) ));
      bandWidthChoice.addItemListener(this);
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

   public int getResolution() {
      return nMixers*((int) ( 1.0E-3 * bandWidth / (double)channels ));
   }

    public void setNumberOfMixers(int nMixers) {
	this.nMixers = nMixers;
    }

   public void itemStateChanged ( ItemEvent ev )
   {
      setBandWidth(Double.parseDouble((String)bandWidthChoice.getSelectedItem()) * 1.0E6);
   }
}
