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

import javax.swing.*;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class ResolutionDisplay extends JLabel implements SamplerWatcher
{
   private int channels;
   private int resolution;
   private double width;

   public ResolutionDisplay ( int channels, double width )
   {
      super ( );
      setHorizontalAlignment ( CENTER );
      this.channels = channels;
      this.width = width;
      resolution = (int) ( 1.0E-3 * width / (double)channels );
      setText ( String.valueOf ( resolution ) );
   }

   public void setChannels ( int channels )
   {
      this.channels = channels;
      resolution = (int) ( 1.0E-3 * width / (double)channels );
      setText ( String.valueOf ( resolution ) );
   }

   public void updateSamplerValues ( double centre, double width,
     int channels )
   {
      this.width = width;
      this.channels = channels;
      resolution = (int) ( 1.0E-3 * width / (double)channels );
      setText ( String.valueOf ( resolution ) );
   }
}
