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

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger ( M.Folger@roe.ac.uk )
 */
public class BandSpec
{
   public String name;
   public int numBands;
   public double [] bandWidths;
   public int [] channels;
   public int [] numHybridSubBands;

   // Added by MFO (September 5, 2002)
   private void initBandSpec ( String name, int numBands, double [] bandWidths, int [] channels )
   {
      this.name = name;
      this.numBands = numBands;
      this.bandWidths = bandWidths;
      this.channels = channels;
   }


   public BandSpec ( String name, int numBands, double [] bandWidths, int [] channels ) {
      initBandSpec(name, numBands, bandWidths, channels);

      this.numHybridSubBands = new int[bandWidths.length];

      for(int i = 0; i < numHybridSubBands.length; i++) {
         numHybridSubBands[i] = 1;
      }
   }

   // Added by MFO (September 5, 2002)
   /**
    * @param numHybridSubBands 1 for non-hybrid, &gt;1 for hybrid.
    */
   public BandSpec ( String name, int numBands, double [] bandWidths, int [] channels, int [] numHybridSubBands)
   {   
      initBandSpec(name, numBands, bandWidths, channels);

      this.numHybridSubBands = numHybridSubBands; 
   }

   public String toString()
   {
      return name;
   }

   // Added by MFO (September 4, 2002)
   public double [] getBandWidths(double overlap) {
      double [] result = new double[bandWidths.length];

      for(int i = 0; i < bandWidths.length; i++) {
	 result[i] = bandWidths[i] - ((numHybridSubBands[i] - 1) * overlap);
      }

      return result;
   }

   // Added by MFO (September 4, 2002)
   public int [] getChannels(double overlap) {
      int [] result = new int[channels.length];
      int channelOverlap;

      for(int i = 0; i < channels.length; i++) {
	 channelOverlap = (int)(channels[i] * (overlap / bandWidths[i]));
	 result[i] = channels[i] - ((numHybridSubBands[i] - 1) * channelOverlap);
      }

      return result;
   }
}
