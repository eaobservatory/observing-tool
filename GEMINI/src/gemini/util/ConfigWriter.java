package gemini.util ;

import java.io.File ;
import java.io.BufferedWriter ;
import java.io.FileWriter ;
import java.io.IOException ;

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
	private static int exec_counter = 0;

	private ConfigWriter()
	{
		_timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date()))
		           + String.format("%03d", ++ exec_counter) ;
		exec_counter %= 1000;
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
			writer.flush() ;
			writer.close() ;
		}
	}

	public void write( Hashtable<String,String> table ) throws IOException
	{
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

			BufferedWriter writer = new BufferedWriter( new FileWriter( fileName ) ) ;

			write( writer , table ) ;
			writer.flush();
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
}
