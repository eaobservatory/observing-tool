package gemini.util ;

import java.io.File ;
import java.io.BufferedWriter ;
import java.io.FileWriter ;
import java.io.IOException ;

import java.text.DecimalFormat ;
import java.text.SimpleDateFormat ;

import java.util.Date ;
import java.util.Enumeration ;
import java.util.Hashtable ;

public class ConfigWriter
{
	private String _timeStamp ;
	private int _counter ;
	private static ConfigWriter _writer ;
	private String _instName = "none" ;
	private Hashtable<String,String> _lastConfig = null ;

	private ConfigWriter()
	{
		_timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmssSSS" ).format( new Date() ) ;
		_lastConfig = null ;
		_counter = 0 ;
	}

	public static synchronized ConfigWriter getNewInstance()
	{
		_writer = null ;
		_writer = new ConfigWriter() ;
		return _writer ;
	}

	public static ConfigWriter getCurrentInstance()
	{
		return _writer ;
	}

	public String getCurrentName()
	{
		return _instName + "_" + _timeStamp + "_" + _counter ;
	}

	public String getTelFile()
	{
		return _instName + "_" + _timeStamp + ".xml" ;
	}

	public String getExecName()
	{
		String execPath = System.getProperty( "EXEC_PATH" ) ;
		if( execPath == null || execPath.trim().equals( "" ) || !( new File( execPath ).exists() ) )
			execPath = System.getProperty( "user.home" ) ;

		return( execPath + File.separator + _instName + "_" + _timeStamp + ".exec" ) ;
	}

	public void writeTelFile( String xml ) throws IOException
	{
		synchronized( xml )
		{
			String confDir = System.getProperty( "CONF_PATH" ) ;
			if( confDir == null || confDir.equals( "" ) || !( new File( confDir ).exists() ) )
				confDir = System.getProperty( "user.home" ) ;

			String fileName = confDir + File.separator + getTelFile() ;
			BufferedWriter writer = new BufferedWriter( new FileWriter( fileName ) ) ;
			writer.write( xml ) ;
			writer.close() ;
		}
	}

	public void write( Hashtable<String,String> table ) throws IOException
	{
		boolean oldController = System.getProperty( "cgs4" ) == null ;
		synchronized( table )
		{
			// If the current table is the same as the last, don't bother writing a new one
			if( _lastConfig != null && _lastConfig.equals( table ) )
				return ;

			_lastConfig = table ;

			// First get the instrument from the hashtable
			_instName = table.get( "instrument" ) ;
			_counter++ ;

			String confDir = System.getProperty( "CONF_PATH" ) ;
			if( confDir == null || confDir.equals( "" ) || !( new File( confDir ).exists() ) )
				confDir = System.getProperty( "user.home" ) ;

			String fileName = confDir + File.separator + getCurrentName() + ".conf" ;

			if( "CGS4".equals( _instName ) && oldController )
			{
				fileName = fileName.replaceAll( "CGS4" , "cgs4" ) ;
				fileName = fileName.replaceAll( "\\.conf" , ".aim" ) ;
			}

			BufferedWriter writer = new BufferedWriter( new FileWriter( fileName ) ) ;

			// Now delegate the writing according to the instrument

			if( _instName.equalsIgnoreCase( "CGS4" ) && oldController )
				writeCGS4( writer , table ) ;
			else
				write( writer , table ) ;

			writer.close() ;
		}
	}

	private void write( BufferedWriter w , Hashtable<String,String> t ) throws IOException
	{
		Enumeration<String> e = t.keys() ;
		while( e.hasMoreElements() )
		{
			String key = e.nextElement() ;
			if( !( key.startsWith( "instAper" ) ) )
			{
				w.write( key + " = " + t.get( key ) ) ;
				w.newLine() ;
			}
		}
	}

	private void writeCGS4( BufferedWriter w , Hashtable<String,String> t ) throws IOException
	{
		w.write( formatLegacyConfigTitle( "ASTRONOMICAL" , getCurrentName() ) ) ;
		w.newLine() ;
		w.write( " Basic (object) configuration: " ) ;
		w.newLine() ;

		String readMode = t.get( "readMode" ) ;
		if( readMode.equals( "NDSTARE" ) )
			readMode = "ND_STARE" ;

		w.write( formatLegacyConfig( readMode , "acquisition configuration" ) ) ;
		w.newLine() ;

		w.write( formatFloatLegacyConfig( t.get( "expTime" ) , "exposure time" ) ) ;
		w.newLine() ;
		w.write( formatIntLegacyConfig( t.get( "objNumExp" ) , "exposures/integ" ) ) ;
		w.newLine() ;
		w.write( formatIntLegacyConfig( t.get( "savedInt" ) , "scans" ) ) ;
		w.newLine() ;

		String[] samplingValues = t.get( "sampling" ).split( "x" ) ;
		int sampling = Integer.parseInt( samplingValues[ 0 ] ) * Integer.parseInt( samplingValues[ 1 ] ) ;
		w.write( formatIntLegacyConfig( "" + sampling , "sampling" ) ) ;
		w.newLine() ;
		
		if( Integer.parseInt( samplingValues[ 1 ] ) == 1 )
			w.write( formatLegacyConfig( samplingValues[ 1 ] + "_pixel" , "sample range" ) ) ;
		else
			w.write( formatLegacyConfig( samplingValues[ 1 ] + "_pixels" , "sample range" ) ) ;

		w.newLine() ;

		w.write( formatFloatLegacyConfig( t.get( "posAngle" ) , "position angle" ) ) ;
		w.newLine() ;

		String filter = t.get( "filter" ).trim() ;
		boolean nd = Boolean.getBoolean( t.get( "neutralDensity" ) ) ;
		String pol = t.get( "polariser" ) ;
		if( nd )
			filter = filter + "+ND" ;
		else if( pol.equalsIgnoreCase( "none" ) )
			;
		else
			filter = filter + "+" + pol ;
		w.write( formatLegacyConfig( filter , "filter" ) ) ;
		w.newLine() ;

		StringBuffer slitWidth = new StringBuffer( t.get( "slitWidth" ) ) ;
		slitWidth = slitWidth.delete( 1 , slitWidth.length() ) ;
		slitWidth = slitWidth.append( "_pixel" ) ;
		if( slitWidth.charAt( 0 ) != '1' )
			slitWidth = slitWidth.append( "s" ) ;

		w.write( formatLegacyConfig( slitWidth.toString() , "slit width" ) ) ;
		w.newLine() ;

		String disperser = t.get( "disperser" ) ;
		if( disperser.startsWith( "echelle" ) )
			disperser = "echelle" ;
		else if( disperser.indexOf( "lpmm" ) != -1 && disperser.indexOf( "_lpmm" ) == -1 )
			disperser = disperser.substring( 0 , disperser.indexOf( "lpmm" ) ) + "_lpmm" ;

		w.write( formatLegacyConfig( disperser , "grating" ) ) ;
		w.newLine() ;

		w.write( formatFloatLegacyConfig( t.get( "centralWavelength" ) , "wavelength" ) ) ;
		w.newLine() ;
		w.write( formatLegacyConfig( t.get( "order" ) , "order" ) ) ;
		w.newLine() ;

		Double centalWavelength = new Double( t.get( "centralWavelength" ) ) ;
		Double cvfWavelength = new Double( t.get( "cvfWavelength" ) ) ;
		Double offset = new Double( cvfWavelength.doubleValue() - centalWavelength.doubleValue() ) ;
		offset = new Double( Math.rint( offset.doubleValue() * 1000. ) / 1000. ) ;
		w.write( formatLegacyConfig( offset.toString() , "cvf offset" ) ) ;
		w.newLine() ;

		w.write( formatLegacyConfig( t.get( "calibLamp" ) , "calibration lamp" ) ) ;
		w.newLine() ;
		w.write( formatLegacyConfig( t.get( "tunHalLevel" ) , "tungsten-halogen level" ) ) ;
		w.newLine() ;
		w.write( formatLegacyConfig( t.get( "lampEffAp" ) , "Lamp effective aperture" ) ) ;
		w.newLine() ;

		w.write( " Flat configuration variant (from object): " ) ;
		w.newLine() ;
		w.write( formatLegacyConfig( t.get( "flatSampling" ) , "Flat sampling" ) ) ;
		w.newLine() ;

		String calLamp = t.get( "flatCalLamp" ) ;
		if( calLamp.startsWith( "Black" ) )
			calLamp = calLamp.substring( 12 , 15 ) ;
		else if( calLamp.startsWith( "Tungsten-Halogen" ) )
			calLamp = "t_h" ;

		w.write( formatLegacyConfig( calLamp , "calibration lamp" ) ) ;
		w.newLine() ;

		filter = t.get( "flatFilter" ) ;
		nd = Boolean.valueOf( t.get( "flatNeutralDensity" ) ).booleanValue() ;
		if( nd )
			filter = filter + "+ND" ;
		else if( !( pol.equalsIgnoreCase( "none" ) ) )
			filter = filter + "+" + pol ;
		
		w.write( formatLegacyConfig( filter , "filter" ) ) ;
		w.newLine() ;

		readMode = t.get( "flatReadMode" ) ;
		if( readMode.equals( "NDSTARE" ) )
			readMode = "ND_STARE" ;

		w.write( formatLegacyConfig( readMode , "acquisition configuration" ) ) ;
		w.newLine() ;

		w.write( formatFloatLegacyConfig( t.get( "flatExpTime" ) , "flat exposure time" ) ) ;
		w.newLine() ;
		w.write( formatIntLegacyConfig( t.get( "flatNumExp" ) , "flat exposures/integ" ) ) ;
		w.newLine() ;
		w.write( formatIntLegacyConfig( t.get( "flatSavedInt" ) , "flat integrations" ) ) ;
		w.newLine() ;

		w.write( " Dark configuration variant (from object): " ) ;
		w.newLine() ;
		w.write( formatIntLegacyConfig( t.get( "darkNumExp" ) , "dark exposure/integ" ) ) ;
		w.newLine() ;
		w.write( formatIntLegacyConfig( t.get( "darkSavedInt" ) , "dark integrations" ) ) ;
		w.newLine() ;

		w.write( " Bias configuration variant (from object): " ) ;
		w.newLine() ;
		w.write( formatFloatLegacyConfig( t.get( "biasExpTime" ) , "bias exposure time" ) ) ;
		w.newLine() ;
		w.write( formatIntLegacyConfig( t.get( "biasNumExp" ) , "bias exposures/integ" ) ) ;
		w.newLine() ;
		w.write( formatIntLegacyConfig( t.get( "biasSavedInt" ) , "bias integrations" ) ) ;
		w.newLine() ;

		w.write( " Arc configuration variant (from object): " ) ;
		w.newLine() ;
		w.write( formatLegacyConfig( t.get( "arcCalLamp" ) , "calibration lamp" ) ) ;
		w.newLine() ;

		filter = t.get( "arcFilter" ) ;
		if( filter.equalsIgnoreCase( "blank" ) )
			filter = "Blanks" ;

		if( !( pol.equalsIgnoreCase( "none" ) ) )
			filter = filter + "+" + pol ;

		w.write( formatLegacyConfig( filter , "filter" ) ) ;
		w.newLine() ;
		w.write( formatLegacyConfig( t.get( "arcCvfWavelength" ) , "cvf wavelength" ) ) ;
		w.newLine() ;

		readMode = t.get( "arcReadMode" ) ;
		if( readMode.equals( "NDSTARE" ) )
			readMode = "ND_STARE" ;

		w.write( formatLegacyConfig( readMode , "acquisition configuration" ) ) ;
		w.newLine() ;
		w.write( formatFloatLegacyConfig( t.get( "arcExpTime" ) , "arc exposure time" ) ) ;
		w.newLine() ;
		w.write( formatIntLegacyConfig( t.get( "arcNumExp" ) , "arc exposures/integ" ) ) ;
		w.newLine() ;
		w.write( formatIntLegacyConfig( t.get( "arcSavedInt" ) , "arc integrations" ) ) ;
		w.newLine() ;
	}

	private String formatLegacyConfig( String value , String comment )
	{

		String blanks ; // Blanks to initialise StringBuffer
		StringBuffer line ; // Buffer for config line

		// Use a StringBuffer to insert the command from the beginning and
		// the comment from column 23. However, it appears that in must be
		// initialised first (or perhaps its length changed).
		line = new StringBuffer( 51 ) ;
		blanks = "                                                   " ;
		line.append( blanks ) ;
		line.insert( 1 , value.toString() ).insert( 22 , comment ) ;
		return line.toString() ;
	}

	private String formatLegacyConfigTitle( String type , String configName )
	{

		String blanks ; // Blanks to initialise StringBuffer
		StringBuffer line ; // Buffer for config line

		// Use a StringBuffer to insert the config name from the beginning and
		// the comment from column 23. However, it appears that in must be
		// initialised first (or perhaps its length changed).
		line = new StringBuffer( 79 ) ;
		blanks = "                                                                            " ;
		line.append( blanks ) ;
		line.insert( 1 , type ).insert( 18 , "configuration :" ) ;
		line.insert( 34 , configName ).insert( 74 , ": 2.1" ) ;
		return line.toString() ;
	}

	private String formatFloatLegacyConfig( Object o , String comment ) throws NumberFormatException
	{

		String blanks ; // Blanks to initialise StringBuffer
		DecimalFormat df ; // Decimal format
		int i ; // Loop counter
		StringBuffer line ; // Buffer for config line
		double number ; // Floating-point value
		boolean sign ; // Negative sign present?
		String value = o.toString() ;

		// Convert the value to a double. If it fails, print a contextual
		// error message but rethrow the exception for higher methods to catch.
		try
		{
			number = ( Double.valueOf( value ) ).doubleValue() ;

		}
		catch( NumberFormatException e )
		{
			System.out.println( "Unable to format config " + comment + " value '" + value + "' as a floating-point number." ) ;
			throw e ;
		}

		// Record the presence of a negative number. Temporarily change
		// value to positive to obtain the same alignment of the decimal point
		// for a negative number.
		sign = number < 0.0 ;
		number = Math.abs( number ) ;

		// Legacy config normally uses an F10.4 formatted number. Tried using
		// the negative format, but it seems to be ignored.
		df = new DecimalFormat( "00000.0000" ) ;

		// Use a StringBuffer to insert the command from the beginning and
		// the comment from column 23. However, it appears that in must be
		// initialised first (or perhaps its length changed).
		line = new StringBuffer( 51 ) ;
		blanks = "                                                   " ;
		line.append( blanks ) ;
		line.insert( 1 , df.format( number ) ).insert( 22 , comment ) ;

		// Replace the leading zeroes with spaces except before the decimal
		// point. It's safe to look beyond the cvurrent character, as there
		// must be a decimal point.
		for( i = 1 ; i < line.length() ; i++ )
		{
			if( line.charAt( i ) == '0' && !( line.charAt( i + 1 ) == '.' ) )
			{
				line.setCharAt( i , ' ' ) ;
			}
			else
			{
				// Restore a negative sign.
				if( sign )
					line.setCharAt( i - 1 , '-' ) ;
				break ;
			}
		}

		// Return the text as a String.
		return line.toString() ;
	}

	private String formatIntLegacyConfig( String value , String comment ) throws NumberFormatException
	{
		String blanks ; // Blanks to initialise StringBuffer
		DecimalFormat df ; // Decimal format
		int i ; // Loop counter
		StringBuffer line ; // Buffer for config line
		int number ; // Integer value

		// Convert the value to an integer. If it fails, print a contextual
		// error message but rethrow the exception for higher methods to catch.
		try
		{
			number = Integer.parseInt( value ) ;

		}
		catch( NumberFormatException e )
		{
			System.out.println( "Unable to format config " + comment + " value '" + value + "' as an integer." ) ;
			throw e ;
		}

		// Legacy config demands an I6 formatted number.
		df = new DecimalFormat( "000000" ) ;

		// Use a StringBuffer to insert the command from the beginning and
		// the comment from column 23. However, it appears that in must be
		// initialised first (or perhaps its length changed).
		line = new StringBuffer( 51 ) ;
		blanks = "                                                   " ;
		line.append( blanks ) ;
		line.insert( 1 , df.format( number ) ).insert( 22 , comment ) ;

		// Replace the leading zeroes with spaces.
		for( i = 1 ; i < line.length() ; i++ )
		{
			if( line.charAt( i ) == '0' )
				line.setCharAt( i , ' ' ) ;
			else
				break ;
		}

		// Return the text as a String.
		return line.toString() ;
	}
}
