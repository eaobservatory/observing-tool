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
   public double [] bandWidths;
   public int [] channels;

   public BandSpec ( String name, int numBands, double [] bandWidths, int [] channels )
   {
      this.name = name;
      this.numBands = numBands;
      this.bandWidths = bandWidths;
      this.channels = channels;
   }

   public String toString()
   {
      return name;
   }

}
