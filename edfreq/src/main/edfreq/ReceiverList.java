/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package edfreq ;

import java.util.Hashtable ;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (
 *         M.Folger@roe.ac.uk )
 */
public class ReceiverList
{

	public static Hashtable<String,Receiver> getReceiverTable()
	{
		Hashtable<String,Receiver> receivers = new Hashtable<String,Receiver>() ;

		Receiver r ;

		/*
         * Create details of all known receivers and record them in the
         * hashtable
         */

		r = new Receiver( "HARP-B" , 325.0E9 , 375.0E9 , 5.0E9 , 2.0E9 ) ;

		r.bandspecs.add( new BandSpec( "1-system" , 1 , new double[] { 0.25E9 , 0.5E9 , 1.0E9 , 2.0E9 } , // band
                                                                                                            // widths
		new double[] { 0.25E8 , 0.5E8 , 1.0E8 , 2.0E8 } , // default overlaps
		new int[] { 8192 , 8192 , 2048 , 2048 } , // channels
		new int[] { 1 , 2 , 1 , 2 } ) ) ; // hybrid sub bands
		r.bandspecs.add( new BandSpec( "2-system" , 2 , new double[] { 0.25E9 , 1.0E9 } , new int[] { 4096 , 1024 } ) ) ;

		receivers.put( "HARP-B" , r ) ;

		r = new Receiver( "A3M" , 211.0E9 , 255.0E9 , 5.7E9 , 4.0E9 ) ;

		r.bandspecs.add( new BandSpec( "1-system" , 1 , new double[] { 1.0E9 , 4.0E9 } , new double[] { 1.0E8 , 4.0E8 } , new int[] { 32768 , 8192 } , new int[] { 4 , 4 } ) ) ;
		r.bandspecs.add( new BandSpec( "4-system" , 4 , new double[] { 0.25E9 , 1.0E9 } , new int[] { 8192 , 2048 } ) ) ;
		r.bandspecs.add( new BandSpec( "8-system" , 8 , new double[] { 0.25E9 , 1.0E9 } , new int[] { 4096 , 1024 } ) ) ;

		receivers.put( "A3M" , r ) ;

		r = new Receiver( "A3" , 215.0E9 , 272.0E9 , 4.0E9 , 1.8E9 ) ;

		r.bandspecs.add( new BandSpec( "1-system" , 1 , new double[] { 1.0E9 , 4.0E9 } , new double[] { 1.0E8 , 4.0E8 } , new int[] { 32768 , 8192 } , new int[] { 4 , 4 } ) ) ;
		r.bandspecs.add( new BandSpec( "4-system" , 4 , new double[] { 0.25E9 , 1.0E9 } , new int[] { 8192 , 2048 } ) ) ;
		r.bandspecs.add( new BandSpec( "8-system" , 8 , new double[] { 0.25E9 , 1.0E9 } , new int[] { 4096 , 1024 } ) ) ;

		receivers.put( "A3" , r ) ;

		r = new Receiver( "WB" , 322.0E9 , 373.0E9 , 5.0E9 , 1.8E9 ) ;

		r.bandspecs.add( new BandSpec( "1-system" , 1 , new double[] { 1.0E9 , 4.0E9 } , new double[] { 1.0E8 , 4.0E8 } , new int[] { 32768 , 8192 } , new int[] { 4 , 4 } ) ) ;
		r.bandspecs.add( new BandSpec( "4-system" , 4 , new double[] { 0.25E9 , 1.0E9 } , new int[] { 8192 , 2048 } ) ) ;
		r.bandspecs.add( new BandSpec( "8-system" , 8 , new double[] { 0.25E9 , 1.0E9 } , new int[] { 4096 , 1024 } ) ) ;

		receivers.put( "WB" , r ) ;

		r = new Receiver( "WC" , 430.0E9 , 510.0E9 , 4.0E9 , 1.8E9 ) ;

		r.bandspecs.add( new BandSpec( "1-system" , 1 , new double[] { 1.0E9 , 4.0E9 } , new double[] { 1.0E8 , 4.0E8 } , new int[] { 32768 , 8192 } , new int[] { 4 , 4 } ) ) ;
		r.bandspecs.add( new BandSpec( "4-system" , 4 , new double[] { 0.25E9 , 1.0E9 } , new int[] { 8192 , 2048 } ) ) ;
		r.bandspecs.add( new BandSpec( "8-system" , 8 , new double[] { 0.25E9 , 1.0E9 } , new int[] { 4096 , 1024 } ) ) ;

		receivers.put( "WC" , r ) ;

		r = new Receiver( "WD" , 630.0E9 , 710.0E9 , 4.0E9 , 1.8E9 ) ;

		r.bandspecs.add( new BandSpec( "1-system" , 1 , new double[] { 1.0E9 , 4.0E9 } , new double[] { 1.0E8 , 4.0E8 } , new int[] { 32768 , 8192 } , new int[] { 4 , 4 } ) ) ;
		r.bandspecs.add( new BandSpec( "4-system" , 4 , new double[] { 0.25E9 , 1.0E9 } , new int[] { 8192 , 2048 } ) ) ;
		r.bandspecs.add( new BandSpec( "8-system" , 8 , new double[] { 0.25E9 , 1.0E9 } , new int[] { 4096 , 1024 } ) ) ;

		receivers.put( "WD" , r ) ;

		return receivers ;
	}

}
