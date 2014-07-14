/*
 * Copyright (C) 2009 Science and Technology Facilities Council.
 * All Rights Reserved.
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

package orac.util ;

import uk.ac.starlink.pal.Pal ;
import uk.ac.starlink.pal.AngleDR ;

import gemini.util.CoordSys ;
import gemini.util.RADec ;

import gemini.util.HHMMSS ;
import gemini.util.DDMMSS ;

public class MapArea
{
	public static final double DAS2R = Math.PI / ( 180. * 3600. ) ;
	public static final double DD2R = Math.PI / 180. ;
	private static final Pal pal = new Pal() ;

	public static void main( String[] args )
	{
		for( RADec value : createNewMapArea( 96.33727 , -60.18855 , 0 , 0 , 180 , 180 , 0 ) )
			System.out.println( HHMMSS.valStr( value.ra ) + " " + DDMMSS.valStr( value.dec ) ) ;
	}

	/**
	 * Returns the four FK5 positions for a map defined in Galactic coordinates.
	 * @param longitude
	 * @param lattitude
	 * @param mapX - offset iterator x
	 * @param mapY - offset iterator y
	 * @param mapWidth
	 * @param mapHeight
	 * @param mapPA
	 * @return Four RADec objects in an array.
	 */
	public static RADec[] createNewMapArea( double longitude , double lattitude , double mapX , double mapY , double mapWidth , double mapHeight , double mapPA )
	{
		double rotationAngle = -1.0 * mapPA * DD2R ;

		longitude = Math.toRadians( longitude ) ;
		lattitude = Math.toRadians( lattitude ) ; 

		double mx1 = mapWidth / 2. ;
		double my1 = mapHeight / 2. ;
		double mx2 = mapWidth / 2. ;
		double my2 = -1. * mapHeight / 2. ;
		double mx3 = -1. * mapWidth / 2. ;
		double my3 = -1. * mapHeight / 2. ;
		double mx4 = -1. * mapWidth / 2. ;
		double my4 = mapHeight / 2. ;

		RADec rotated = _rotate( mx1 , my1 , rotationAngle ) ;
		double mrx1 = rotated.ra ;
		double mry1 = rotated.dec ;

		rotated = _rotate( mx2 , my2 , rotationAngle ) ;
		double mrx2 = rotated.ra ;
		double mry2 = rotated.dec ;

		rotated = _rotate( mx3 , my3 , rotationAngle ) ;
		double mrx3 = rotated.ra ;
		double mry3 = rotated.dec ;

		rotated = _rotate( mx4 , my4 , rotationAngle ) ;
		double mrx4 = rotated.ra ;
		double mry4 = rotated.dec ;

		rotated = _rotate( mapX , mapY , rotationAngle ) ;
		double rx = rotated.ra ;
		double ry = rotated.dec ;

		RADec topLeft = Dpt2s( rx + mrx1 , ry + mry1 , longitude , lattitude ) ;
		RADec topRight = Dpt2s( rx + mrx2 , ry + mry2 , longitude , lattitude ) ;
		RADec bottomLeft = Dpt2s( rx + mrx3 , ry + mry3 , longitude , lattitude ) ;
		RADec bottomRight = Dpt2s( rx + mrx4 , ry + mry4 , longitude , lattitude ) ;

		return new RADec[]{ topLeft , topRight , bottomLeft , bottomRight } ;
	}

	private static RADec Dpt2s( double longitude , double lattitude , double baselong , double baselat )
	{
		AngleDR adr = new AngleDR( longitude * DAS2R , lattitude * DAS2R ) ;
		AngleDR base = new AngleDR( baselong , baselat ) ;
		AngleDR result = pal.Dtp2s( adr , base ) ;
		double newLong = pal.Dranrm( result.getAlpha() ) ;
		double newLatt = pal.Dranrm( result.getDelta() ) ;
		return CoordConvert.gal2fk5( Math.toDegrees( newLong ) , Math.toDegrees( newLatt ) ) ;
	}

	private static RADec _rotate( double x , double y , double angle )
	{
		double sinAngle = Math.sin( angle ) ;
		double cosAngle = Math.cos( angle ) ;
		double xPrime = x * cosAngle - y * sinAngle ;
		double yPrime = x * sinAngle + y * cosAngle ;
		return new RADec( CoordSys.GAL , xPrime , yPrime ) ;
	}
}
