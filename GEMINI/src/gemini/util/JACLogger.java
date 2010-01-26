package gemini.util ;

import java.io.IOException ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.util.logging.FileHandler ;
import java.util.logging.SimpleFormatter ;

public class JACLogger
{
	private static Logger logger = Logger.getAnonymousLogger() ;
	private static FileHandler handler = null ;
	private static JACLogger jacLogger = new JACLogger() ;

	private JACLogger()
	{
		String logDir = System.getProperty( "QT_LOG_DIR" ) ;
		if( logDir != null && !"".equals( logDir ) )
		{
			try
			{
				if( !logDir.endsWith( "/" ) )
					logDir += "/" ;
				handler = new FileHandler( logDir + "QT.log" ) ;
				handler.setFormatter( new SimpleFormatter() ) ;
				logger.addHandler( handler ) ;

				if( !"".equals( System.getProperty( "debug" ) ) )
				{
					info( "Debugging ON" ) ;
					logger.setLevel( Level.ALL ) ;
				}
				else
				{
					info( "Debugging OFF" ) ;
				}
			}
			catch( SecurityException e ){ e.printStackTrace() ; }
			catch( IOException e ){ e.printStackTrace() ; }
		}
	}
	
	public static JACLogger getLogger()
	{
		return jacLogger ;
	}
	
	public static JACLogger getLogger( Class<?> klass )
	{
		return getLogger() ;
	}
	
	public static JACLogger getRootLogger()
	{
		return getLogger() ;
	}
	
	public void error( String msg )
	{
		logger.logp( Level.SEVERE , "" , "" , msg ) ;
	}
	
	public void error( String msg , Throwable thrown )
	{
		logger.logp( Level.SEVERE , "" , "" , msg , thrown ) ;
	}
	
	public void debug( String msg )
	{
		logger.logp( Level.FINE , "" , "" , msg ) ;
	}
	
	public void debug( String msg , Throwable thrown )
	{
		logger.logp( Level.FINE , "" , "" , msg , thrown ) ;
	}
	
	public void info( String msg )
	{
		logger.logp( Level.INFO , "" , "" , msg ) ;
	}
	
	public void warn( String msg )
	{
		logger.logp( Level.WARNING , "" , "" , msg ) ;
	}
	
	public void warn( String msg , Throwable thrown )
	{
		logger.logp( Level.WARNING , "" , "" , msg , thrown ) ;
	}
	
	public void fatal( String msg )
	{
		logger.logp( Level.SEVERE , "" , "" , msg ) ;
	}
	
	public void fatal( String msg , Throwable thrown )
	{
		logger.logp( Level.SEVERE , "" , "" , msg , thrown ) ;
	}

	public void shutdown()
	{
		if( handler != null )
		{
			handler.flush() ;
			handler.close() ;
		}
	}
}
