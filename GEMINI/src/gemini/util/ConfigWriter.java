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
	private String _configName = "none" ;
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
		return _configName + "_" + _timeStamp + "_" + _counter ;
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

			// First get the config or instrument name from the hashtable
			_configName = table.remove( "config_name" ) ;
			_instName = table.get( "instrument" ) ;
			if( _configName == null )
				_configName = _instName ;
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
