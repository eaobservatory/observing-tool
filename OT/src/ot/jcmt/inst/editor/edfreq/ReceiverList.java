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

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class ReceiverList
{
   public Hashtable receivers = new Hashtable();

   public ReceiverList ( )
   {
      Receiver r;

/* Create details of all known receivers and record them in the 
   hashtable */

      r = new Receiver ( "HARP-B", 325.0E9, 375.0E9, 5.0E9, 2.0E9 );

      r.bandspecs.add ( new BandSpec ( "1-system", 1, 0.25E9, 8192, 
        1.0E9, 2048 ) );
      r.bandspecs.add ( new BandSpec ( "2-system", 2, 0.25E9, 4096,
        1.0E9, 1024 ) );

      r.bandspecs.add ( new BandSpec ( "synth", 1, 0.5E9, 8192,
        2.0E9, 2048 ) );

      receivers.put ( "HARP-B", r );


      r = new Receiver ( "A3", 215.0E9, 272.0E9, 4.0E9, 1.8E9 );

      r.bandspecs.add ( new BandSpec ( "4-system", 4, 0.25E9, 8192,
        1.0E9, 2048 ) );
      r.bandspecs.add ( new BandSpec ( "8-system", 8, 0.25E9, 4096,
        1.0E9, 1024 ) );
      r.bandspecs.add ( new BandSpec ( "synth", 1, 1.0E9, 32768,
        4.0E9, 8192 ) );

      receivers.put ( "A3", r );


      r = new Receiver ( "B3", 322.0E9, 373.0E9, 4.0E9, 1.8E9 );

      r.bandspecs.add ( new BandSpec ( "4-system", 4, 0.25E9, 8192,
        1.0E9, 2048 ) );
      r.bandspecs.add ( new BandSpec ( "8-system", 8, 0.25E9, 4096,
        1.0E9, 1024 ) );
      r.bandspecs.add ( new BandSpec ( "synth", 1, 1.0E9, 32768,
        4.0E9, 8192 ) );

      receivers.put ( "B3", r );


      r = new Receiver ( "WC", 430.0E9, 510.0E9, 4.0E9, 1.8E9 );

      r.bandspecs.add ( new BandSpec ( "4-system", 4, 0.25E9, 8192,
        1.0E9, 2048 ) );
      r.bandspecs.add ( new BandSpec ( "8-system", 8, 0.25E9, 4096,
        1.0E9, 1024 ) );
      r.bandspecs.add ( new BandSpec ( "synth", 1, 1.0E9, 32768,
        4.0E9, 8192 ) );

      receivers.put ( "WC", r );


      r = new Receiver ( "WD", 630.0E9, 710.0E9, 4.0E9, 1.8E9 );

      r.bandspecs.add ( new BandSpec ( "4-system", 4, 0.25E9, 8192,
        1.0E9, 2048 ) );
      r.bandspecs.add ( new BandSpec ( "8-system", 8, 0.25E9, 4096,
        1.0E9, 1024 ) );
      r.bandspecs.add ( new BandSpec ( "synth", 1, 1.0E9, 32768,
        4.0E9, 8192 ) );

      receivers.put ( "WD", r );


   }


}
