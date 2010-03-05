// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp ;

import java.util.Enumeration ;

/**
 * The science program item.
 */
@SuppressWarnings( "serial" )
public class SpProg extends SpRootItem
{
	/** The PI (principal investigator) attribute. */
	public static final String ATTR_PI = "pi" ;

	/** The country attribute. */
	public static final String ATTR_COUNTRY = "country" ;

	/** The project ID. */
	public static final String ATTR_PROJECT_ID = "projectID" ;

	/** The timestamp. */
	public static final String ATTR_TIMESTAMP = ":timestamp" ;

	/**
     * Default constructor.
     */
	protected SpProg()
	{
		super( SpType.SCIENCE_PROGRAM ) ;
		setTelescope() ;
		setOTVersion() ;
	}

	/**
     * Get PI (principle investigator) attribute.
     * 
     * Added for OMP (MFO, 7 August 2001)
     * 
     * @return PI or "" if attribute hasn't been set.
     */
	public String getPI()
	{
		if( _avTable.get( ATTR_PI ) != null )
			return _avTable.get( ATTR_PI ) ;
		else
			return "" ;
	}

	/**
     * Set PI (principle investigator) attribute.
     * 
     * Added for OMP (MFO, 7 August 2001)
     */
	public void setPI( String pi )
	{
		_avTable.set( ATTR_PI , pi ) ;
	}

	/**
     * Get country attribute.
     * 
     * Added for OMP (MFO, 7 August 2001)
     * 
     * @return country or "" if attribute hasn't been set.
     */
	public String getCountry()
	{
		if( _avTable.get( ATTR_COUNTRY ) != null )
			return _avTable.get( ATTR_COUNTRY ) ;
		else
			return "" ;
	}

	/**
     * Set country attribute.
     * 
     * Added for OMP (MFO, 7 August 2001)
     */
	public void setCountry( String country )
	{
		_avTable.set( ATTR_COUNTRY , country ) ;
	}

	/**
     * Get country attribute.
     * 
     * Added for OMP (MFO, 7 August 2001)
     * 
     * @return country or "" if attribute hasn't been set.
     */
	public String getProjectID()
	{
		if( _avTable.get( ATTR_PROJECT_ID ) != null )
			return _avTable.get( ATTR_PROJECT_ID ) ;
		else
			return "" ;
	}

	/**
     * Set project ID.
     * 
     * Added for OMP (MFO, 7 August 2001)
     */
	public void setProjectID( String projectID )
	{
		_avTable.set( ATTR_PROJECT_ID , projectID ) ;
	}

	/**
     * Set timestamp.
     * 
     * Added for OMP (MFO, 12 November 2001)
     */
	public void setTimestamp( int timestamp )
	{
		_avTable.set( ATTR_TIMESTAMP , timestamp ) ;
	}

	/**
     * Calculates the duration of this Science Program.
     */
	public double getTotalTime()
	{
		double elapsedTime = 0. ;
		Enumeration<SpItem> children = children() ;
		SpItem spItem = null ;

		while( children.hasMoreElements() )
		{
			spItem = children.nextElement() ;

			if( spItem instanceof SpMSB )
			{
				SpMSB msb = ( SpMSB )spItem ;
				if( msb.getNumberRemaining() >= 0 )
					elapsedTime += msb.getTotalTime() * msb.getNumberRemaining() ;
			}
			else if( spItem instanceof SpAND )
			{
				elapsedTime += (( SpAND )spItem).getTotalTime() ;
			}
			else if( spItem instanceof SpOR )
			{
				elapsedTime += (( SpOR )spItem).getTotalTime() ;
			}
			else if( spItem instanceof SpSurveyContainer )
			{
				elapsedTime += (( SpSurveyContainer )spItem).getTotalTime() ;
			}
		}
		return elapsedTime ;
	}

	/**
     * Calculates the duration of this Science Program.
     */
	public double getElapsedTime()
	{
		double elapsedTime = 0. ;
		Enumeration<SpItem> children = children() ;
		SpItem spItem = null ;

		while( children.hasMoreElements() )
		{
			spItem = children.nextElement() ;

			if( spItem instanceof SpMSB )
			{
				SpMSB msb = ( SpMSB )spItem ;
				if( msb.getNumberRemaining() >= 0 )
					elapsedTime += msb.getElapsedTime() * msb.getNumberRemaining() ;
			}
			else if( spItem instanceof SpAND )
			{
				elapsedTime += (( SpAND )spItem).getElapsedTime() ;
			}
			else if( spItem instanceof SpOR )
			{
				elapsedTime += (( SpOR )spItem).getElapsedTime() ;
			}
			else if( spItem instanceof SpSurveyContainer )
			{
				elapsedTime += (( SpSurveyContainer )spItem).getElapsedTime() ;
			}
		}

		return elapsedTime ;
	}

	public void processXmlElementContent( String element , String value )
	{
		super.processXmlElementContent( element , value ) ;
	}
}
