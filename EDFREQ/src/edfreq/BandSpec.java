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
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class BandSpec
{
   public String name;
   public int numBands;
   public double loBandWidth;
   public int loChannels;
   public double hiBandWidth;
   public int hiChannels;

   public BandSpec ( String name, int numBands, double loBandWidth, 
     int loChannels, double hiBandWidth, int hiChannels )
   {
      this.name = name;
      this.numBands = numBands;
      this.loBandWidth = loBandWidth;
      this.loChannels = loChannels;
      this.hiBandWidth = hiBandWidth;
      this.hiChannels = hiChannels;
   }

   public String toString()
   {
      return name;
   }

}
