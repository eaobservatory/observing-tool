/*
 * Copyright (C) 2007-2012 Science and Technology Facilities Council.
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

package orac.jcmt.inst ;

import java.io.IOException ;
import java.util.Hashtable ;

import orac.util.InstCfg ;
import orac.util.InstCfgReader ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.sp.obsComp.SpMicroStepUser ;

@SuppressWarnings( "serial" )
public class SpInstSCUBA2 extends SpJCMTInstObsComp implements SpMicroStepUser
{
	public static final SpType SP_TYPE = SpType.create( SpType.OBSERVATION_COMPONENT_TYPE , "inst.SCUBA2" , "SCUBA-2" ) ;

	//	Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpInstSCUBA2() ) ;
	}

	public static String[] JIGGLE_PATTERNS = { "DREAM" } ;

	public static String[][] MICROSTEP_PATTERNS ;

	/**
	 * Constructor. Sets default values for attributes.
	 */
	public SpInstSCUBA2()
	{
		super( SP_TYPE ) ;

		// Read in the instrument config file
		String baseDir = System.getProperty( "ot.cfgdir" ) ;
		if( !baseDir.endsWith( "/" ) )
			baseDir += '/' ;
		String cfgFile = baseDir + "scuba2.cfg" ;
		_readCfgFile( cfgFile ) ;
	}

	private void _readCfgFile( String filename )
	{
		InstCfgReader instCfg = null ;
		InstCfg instInfo = null ;
		String block = null ;
		instCfg = new InstCfgReader( filename ) ;
		try
		{
			while( ( block = instCfg.readBlock() ) != null )
			{
				instInfo = new InstCfg( block ) ;
				if( InstCfg.matchAttr( instInfo , "microstep_patterns" ) )
					MICROSTEP_PATTERNS = instInfo.getValueAs2DArray() ;
				else
					System.out.println( "Unmatched keyword:" + instInfo.getKeyword() ) ;
			}
		}
		catch( IOException e )
		{
			System.out.println( "Error reading SCUBA2 inst. cfg file" ) ;
		}
	}

	/**
	 * Get jiggle pattern options for this instrument, given the current settings.
	 *
	 * @return String array of jiggle pattern options.
	 */
	public String[] getJigglePatterns()
	{
		return JIGGLE_PATTERNS ;
	}

	/**
	 * Returns instrument specific default value for scan dx.
	 */
	public double getDefaultScanVelocity()
	{
		return 120. ;
	}

	/**
	 * Returns instrument specific default value for scan dy.
	 */
	public double getDefaultScanDy()
	{
		return 60. ;
	}

	public Hashtable<String,double[][]> getMicroStepPatterns()
	{
		Hashtable<String,double[][]> result = new Hashtable<String,double[][]>() ;

		double[][] offsets ;

		for( int i = 0 ; i < MICROSTEP_PATTERNS.length ; i++ )
		{
			offsets = new double[ ( MICROSTEP_PATTERNS[ i ].length - 1 ) / 2 ][ 2 ] ;

			int k = 1 ;
			for( int j = 0 ; j < offsets.length ; j++ )
			{
				offsets[ j ][ 0 ] = Double.parseDouble( MICROSTEP_PATTERNS[ i ][ k++ ] ) ;
				offsets[ j ][ 1 ] = Double.parseDouble( MICROSTEP_PATTERNS[ i ][ k++ ] ) ;
			}
			result.put(MICROSTEP_PATTERNS[i][0], offsets);
		}

		return result ;
	}
	
	public double[] getScienceArea()
	{
		// 8 arcminute diameter circle
		double[] scienceArea = new double[]{ 240.0 } ;
		return scienceArea ;
	}
}
