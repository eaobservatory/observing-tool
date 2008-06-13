// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.obsComp ;

/**
 * A base class for IR instrument observation component items.
 */
public class SpStareCapability extends SpInstCapability implements SpInstConstants
{

	public static final String CAPABILITY_NAME = "stare" ;

	/**
	 */
	public SpStareCapability()
	{
		super( CAPABILITY_NAME ) ;
	}

	/**
     * Get the coadds. This was modifed by AB for ORAC to retrun a default of 0
     * rather than 1 if no value is found.
     */
	public int getCoadds()
	{
		int coadds = _avTable.getInt( ATTR_COADDS , 0 ) ;
		return coadds ;
	}

	/**
     * Get the coadds as a string.
     */
	public String getCoaddsAsString()
	{
		return String.valueOf( getCoadds() ) ;
	}

	/**
     * Set the coadds.
     */
	public void setCoadds( int coadds )
	{
		_avTable.set( ATTR_COADDS , coadds ) ;
	}

	/**
     * Set the coadds as a string.
     */
	public void setCoadds( String coadds )
	{
		int i = 1 ;
		try
		{
			i = Integer.parseInt( coadds ) ;
		}
		catch( Exception ex ){}

		_avTable.set( ATTR_COADDS , i ) ;
	}
}
