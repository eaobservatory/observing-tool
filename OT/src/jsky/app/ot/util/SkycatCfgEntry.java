// Copyright 1997 Asceociation for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.util;

import java.io.Serializable;

/**
 * This (immutable) class describes a server definition in a Skycat config
 * file.
 */
public final class SkycatCfgEntry implements Cloneable , Comparable , Serializable
{
	public static final String ARCHIVE_TYPE = "archive";
	public static final String CATALOG_TYPE = "catalog";
	public static final String IMAGE_TYPE = "imagesvr";
	public static final String NAME_TYPE = "namesvr";
	public static final String LONG_NAME_TAG = "long_name";
	public static final String SERV_TYPE_TAG = "serv_type";
	public static final String SHORT_NAME_TAG = "short_name";
	public static final String SYMBOL_TAG = "symbol";
	public static final String URL_TAG = "url";
	private String _servType;
	private String _longName;
	private String _shortName;
	private String _urlString;
	private String _symbol;

	/**
	 * Constructs with each of the fields.  Only symbol may be null.
	 */
	public SkycatCfgEntry( String type , String longName , String shortName , String url , String symbol )
	{
		if( ( type == null ) || ( longName == null ) || ( shortName == null ) || ( url == null ) )
			throw new IllegalArgumentException( "Cannot construct SkycatCfgEntry with null arguments." );

		_servType = type;
		_longName = longName;
		_shortName = shortName;
		_urlString = url;
		_symbol = symbol;
	}

	/**
	 * Gets the server type (should be one of {@link #CATALOG_TYPE} or
	 * {@link #IMAGE_TYPE}).
	 */
	public String getServType()
	{
		return _servType;
	}

	/**
	 * Returns true if the server type matches the given type.  This is
	 * equivalent to calling <code>getsServType().equals(type)</code>.
	 * server).
	 */
	public boolean isServType( String type )
	{
		return _servType.equals( type );
	}

	/**
	 * Gets the long name, suitable for use in menus to identify the server to the
	 * user.
	 */
	public String getLongName()
	{
		return _longName;
	}

	/**
	 * Gets a short identifying name (for instance, for use as a hash table key).
	 */
	public String getShortName()
	{
		return _shortName;
	}

	/**
	 * Gets the url.  This may be parsed to contact the server with the
	 * appropriate query string.
	 */
	public String getURLString()
	{
		return _urlString;
	}

	/**
	 * Gets the symbol string asceociated with the entry, if any.  If there is no
	 * such symbol, null is returned.
	 */
	public String getSymbol()
	{
		return _symbol;
	}

	/**
	 * Makes a copy of this object.  (Actually a reference to <code>this</code>
	 * is returned since this is an immutable object).
	 */
	public Object clone()
	{
		// This is an immutable object, no need to actually copy it.
		return this;
	}

	/**
	 * Implements the <code>compareTo()</code> method in Comparable based on
	 * the server type, followed by the long name, followed by the short name,
	 * followed by the URL string.  The ordering <em>is</em> consistent with
	 * <code>equals()</code>.
	 */
	public int compareTo( Object obj )
	{
		// Okay to throw a ClassCastException if obj isn't a SkycatCfgEntry
		SkycatCfgEntry sce = ( SkycatCfgEntry )obj;

		int res;

		res = _servType.compareTo( sce.getServType() );
		if( res != 0 )
			return res;
		
		res = _longName.compareTo( sce.getLongName() );
		if( res != 0 )
			return res;

		res = _shortName.compareTo( sce.getShortName() );
		if( res != 0 )
			return res;

		res = _urlString.compareTo( sce.getURLString() );
		if( res != 0 )
			return res;

		String sym = sce.getSymbol();
		if( _symbol == null )
		{
			if( sym == null )
				res = 0;
			else
				res = -1;
		}
		else
		{
			if( sym == null )
				res = 1;
			else
				res = _symbol.compareTo( sym );
		}
		return res;
	}

	/**
	 * Overrides equals to provide "semantic" equality, not the default value
	 * equality.
	 */
	public boolean equals( Object obj )
	{
		if( this == obj )
			return true;
		if( getClass() != obj.getClass() )
			return false;

		SkycatCfgEntry sce = ( SkycatCfgEntry )obj;

		if( !_servType.equals( sce.getServType() ) )
			return false;
		else if( !_longName.equals( sce.getLongName() ) )
			return false;
		else if( !_shortName.equals( sce.getShortName() ) )
			return false;
		else if( !_urlString.equals( sce.getURLString() ) )
			return false;

		String sym = sce.getSymbol();
		return ( _symbol == null ) ? ( sym == null ) : ( ( sym == null ) ? false : _symbol.equals( sym ) );
	}

	/**
	 * Overrides <code>hashCode()</code> to match <code>{@link #equals}</code>.
	 */
	public int hashCode()
	{
		int result = _servType.hashCode();
		result = 37 * result + _longName.hashCode();
		result = 37 * result + _shortName.hashCode();
		result = 37 * result + _urlString.hashCode();
		if( _symbol != null )
			result = 37 * result + _symbol.hashCode();

		return result;
	}

	/**
	 * Overrides <code>toString()</code> to show the value of the internal fields.
	 */
	public String toString()
	{
		return getClass().getName() + "[" + _servType + ", " + _longName + ", " + _shortName + ", " + _urlString + ", " + _symbol + "]";
	}
}
