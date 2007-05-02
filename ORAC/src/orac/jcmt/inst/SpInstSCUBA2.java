// $Id$

package orac.jcmt.inst ;

import java.io.IOException;
import java.util.Hashtable;

import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.sp.SpFactory;
import gemini.sp.SpType ;
import gemini.sp.obsComp.SpMicroStepUser;

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
		String baseDir = System.getProperty( "ot.cfgdir" );
		String cfgFile = baseDir + "scuba2.cfg";
		_readCfgFile( cfgFile );
	}

    private void _readCfgFile( String filename )
	{
		InstCfgReader instCfg = null;
		InstCfg instInfo = null;
		String block = null;
		int i;
		instCfg = new InstCfgReader( filename );
		try
		{
			while (( block = instCfg.readBlock() ) != null)
			{
				instInfo = new InstCfg( block );
				if( InstCfg.matchAttr( instInfo , "microstep_patterns" ) )
					MICROSTEP_PATTERNS = instInfo.getValueAs2DArray();
				else
					System.out.println( "Unmatched keyword:" + instInfo.getKeyword() );
			}
		}
		catch( IOException e )
		{
			System.out.println( "Error reading WFCAM inst. cfg file" );
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
		return 600. ;
	}

	/**
	 * Returns instrument specific default value for scan dx.
	 */
	public double getDefaultScanDy()
	{
		return 240. ;
	}
	
    public Hashtable getMicroStepPatterns()
	{
		Hashtable result = new Hashtable();

		double[][] offsets;

		for( int i = 0 ; i < MICROSTEP_PATTERNS.length ; i++ )
		{
			offsets = new double[ ( MICROSTEP_PATTERNS[ i ].length - 1 ) / 2 ][ 2 ];

			int k = 1;
			for( int j = 0 ; j < offsets.length ; j++ )
			{
				offsets[ j ][ 0 ] = Double.parseDouble( MICROSTEP_PATTERNS[ i ][ k++ ] );
				offsets[ j ][ 1 ] = Double.parseDouble( MICROSTEP_PATTERNS[ i ][ k++ ] );
			}

			result.put( ( String )MICROSTEP_PATTERNS[ i ][ 0 ] , offsets );
		}

		return result;
	}
}
