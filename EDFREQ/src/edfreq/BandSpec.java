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

   /**
    * Array of bandwidths.
    *
    * The array contains the combined bandwidths based on 0 overlap,
    * i.e. a bandwidth in this array is the sum of the bandwidths of
    * one or more individual hybrid subbands (assuming no overlap).
    * The bandwidth of an individual hybrid subband is not stored in this class.
    * The methods {@link #getBandWidths(double)} and {@link #getDefaultOverlapBandWidths()}
    * are used to calculate combined bandwidths (i.e. sums one or more individual hybrid subbands)
    * with overlaps.
    */
   public double [] bandWidths;

   /** Default overlaps asscoiated with each bandwidth. */
   public double [] defaultOverlaps;

   /** Number of channels asscoiated with each bandwidth. */
   public int [] channels;

   /** Number of hybrid subbands asscoiated with each bandwidth. */
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

      defaultOverlaps   = new double[bandWidths.length];
      numHybridSubBands = new int[bandWidths.length];

      for(int i = 0; i < numHybridSubBands.length; i++) {
         defaultOverlaps[i]   = 0.0;
         numHybridSubBands[i] = 1;
      }
   }

   // Added by MFO (September 5, 2002)
   /**
    * @param numHybridSubBands 1 for non-hybrid, &gt;1 for hybrid.
    */
   public BandSpec ( String name, int numBands, double [] bandWidths, double [] defaultOverlaps,
                     int [] channels, int [] numHybridSubBands)
   {   
      initBandSpec(name, numBands, bandWidths, channels);

      this.defaultOverlaps   = defaultOverlaps;
      this.numHybridSubBands = numHybridSubBands; 
   }

   public String toString()
   {
      return name;
   }

   // Added by MFO (September 4, 2002)
   /**
    * Get the array of bandwidths narrowed according to the specified overlap.
    *
    * Each bandwidth has a number of hybrid subbands asscoiated with it. If this number is 1 then
    * this particular bandwidth will be uneffected by the overlap. Otherwise it will be narrowed due
    * to the overlap. The formula is
    * <blockquote><tt>
    *   combined_bandwidth<sub>overlap</sub> =
    *     combined_bandwidth<sub>no_overlap</sub> -
    *       ((number<sub>hybrid_subbands</sub> - 1) * overlap)
    * </tt></blockquote>
    *
    * Each bandwidth in the {@link #bandWidths} field of this class is used as a
    * <tt>combined_bandwidth<sub>no_overlap</sub></tt> in the above calculation.
    *
    * The calculation is done for each bandwidth and the results are returned as an array.
    *
    * @see #getDefaultOverlapBandWidths()
    */
   public double [] getBandWidths(double overlap) {
      double [] result = new double[bandWidths.length];

      for(int i = 0; i < bandWidths.length; i++) {
	 result[i] = bandWidths[i] - ((numHybridSubBands[i] - 1) * overlap);
      }

      return result;
   }

   /**
    * Get the array of bandwidths narrowed according to the default overlaps for the corresponding bandwidths.
    *
    * The default overlap bandwidths are calculated as in {@link #getBandWidths(double)}
    * except the each calculation is based on the default overlap for that band.
    *
    * @see #getBandWidths(double)
    */
   public double [] getDefaultOverlapBandWidths() {
      double [] result = new double[bandWidths.length];

      for(int i = 0; i < bandWidths.length; i++) {
	 result[i] = bandWidths[i] - (numHybridSubBands[i] * defaultOverlaps[i]);
      }

      return result;
   }

   // Added by MFO (September 4, 2002)
   /**
    * Get the array of numbers of channels taking into account the specified overlap.
    *
    * @see #getBandWidths(double)
    */
   public int [] getChannels(double overlap) {
      int [] result = new int[channels.length];
      int channelOverlap;

      for(int i = 0; i < channels.length; i++) {
	 channelOverlap = (int)(channels[i] * (overlap / bandWidths[i]));
	 result[i] = channels[i] - (numHybridSubBands[i] * channelOverlap);
      }

      return result;
   }

   /**
    * Get the array of numbers of channels taking into account the default overlaps for the corresponding bandwidths.
    *
    * @see #getDefaultOverlapBandWidths()
    */
   public int [] getDefaultOverlapChannels() {
      int [] result = new int[channels.length];
      int channelOverlap;

      for(int i = 0; i < channels.length; i++) {
	 channelOverlap = (int)(channels[i] * (defaultOverlaps[i] / bandWidths[i]));
	 result[i] = channels[i] - ((numHybridSubBands[i] - 1) * channelOverlap);
      }

      return result;
   }


   /**
    * Number of hybrid subbands for a given bandwidth choice.
    *
    * A BandSpec contains an array of bandwidths. Some of these
    * bandwidths are the result of joining a number of hybrid subbands
    * together. This method returns the number of hybrid subbands
    * of the bandwidth whose index in the bandwidth array is
    * given as the argument <i>bandWidthIndex</i>
    */
   public int getNumHybridSubBands(int bandWidthIndex) {
      return numHybridSubBands[bandWidthIndex];
   }
}
