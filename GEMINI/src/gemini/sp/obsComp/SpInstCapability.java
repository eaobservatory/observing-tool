package gemini.sp.obsComp ;

import gemini.sp.SpAvTable ;

/**
 * A base class for instrument capabilities. A "capability" is a class that
 * simplifies attribute/value manipulation for an instrument. For instance, the
 * "chop" capability contains methods to get and set number of exposures per
 * chop position, etc. These methods read and set an associated attribute/value
 * table and serve as a convenience and type-safe way of manipulating these
 * attributes.
 * 
 * <p>
 * The base class mainly implements the code required to copy a capability.
 */
@SuppressWarnings( "serial" )
public class SpInstCapability implements Cloneable , java.io.Serializable
{
	protected SpAvTable _avTable ;

	private String _name ;

	/**
     * Construct with the AvTable to use.
     */
	public SpInstCapability( String name )
	{
		_name = name ;
	}

	/**
     * Get the name of the capability, for instance, "chop" or "stare".
     */
	public String getName()
	{
		return _name ;
	}

	/**
     * Copy this capability, associating the given attribute value table with
     * the copy.
     */
	public SpInstCapability copy( SpAvTable avTable )
	{
		SpInstCapability sic ;
		try
		{
			sic = ( SpInstCapability )super.clone() ;
		}
		catch( CloneNotSupportedException ex )
		{
			return null ;
		}
		sic.setAvTable( avTable ) ;
		return sic ;
	}

	void setAvTable( SpAvTable avTable )
	{
		_avTable = avTable ;
	}
}
