// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util ;

import java.util.Vector ;

/**
 * Base class for telescope positions.
 */
@SuppressWarnings( "serial" )
public abstract class TelescopePos implements java.io.Serializable
{
	/** The position's <i>unique</i> string tag. */
	protected String _tag ;

	/** The position's xaxis (for example, its RA). */
	protected double _xaxis ;

	/** The position's yaxis (for example, its Dec). */
	protected double _yaxis ;

	protected double _xoff ;

	protected double _yoff ;

	/** The list that the position belongs to, if any. */
	protected TelescopePosList _list ;

	// TelescopePos change watchers.
	private Vector<TelescopePosWatcher> _watchers ;

	/**
     * Construct with a <i>unique</i> tag.
     */
	protected TelescopePos( String tag )
	{
		_tag = tag ;
	}

	/**
     * Construct with a <i>unique</i> tag, and the list to which the position
     * belongs.
     */
	protected TelescopePos( String tag , TelescopePosList list )
	{
		this( tag ) ;
		_list = list ;
	}

	/**
     * Is this position and offset position?
     */
	public boolean isOffsetPosition()
	{
		return false ;
	}

	/**
     * Is this position valid?
     */
	public boolean isValid()
	{
		return true ;
	}

	/**
     * Get the tag.
     */
	public final String getTag()
	{
		return _tag ;
	}

	/**
     * Get the xaxis.
     * 
     * @return x axis in degrees or x offset in arcsecs if isOffsetPosition() ==
     *         true.
     */
	public final synchronized double getXaxis()
	{
		double xaxis ;
		if( getTag().equalsIgnoreCase( "base" ) )
			xaxis = RADecMath.getAbsolute( _xaxis , _yaxis , _xoff , _yoff )[ 0 ] ;
		else
			xaxis = _xaxis ;

		return xaxis ;
	}

	/**
     * Get the real BASE position without any offset applied.
     */
	public final synchronized double getRealXaxis()
	{
		return _xaxis ;
	}

	/**
     * Get the yaxis.
     * 
     * @return y axis in degrees or y offset in arcsecs if isOffsetPosition() ==
     *         true.
     */
	public final synchronized double getYaxis()
	{
		double yaxis ;
		if( getTag().equalsIgnoreCase( "base" ) )
			yaxis = RADecMath.getAbsolute( _xaxis , _yaxis , _xoff , _yoff )[ 1 ] ;
		else
			yaxis = _yaxis ;
			
		return yaxis ;
	}

	/**
     * Get the real BASE position without any offset applied.
     */
	public final synchronized double getRealYaxis()
	{
		return _yaxis ;
	}

	/**
     * Get the xaxis as a String. If this is a base position, any base offsets
     * are not applied.
     */
	public synchronized String getRealXaxisAsString()
	{
		return String.valueOf( _xaxis ) ;
	}

	/**
     * Get the xaxis as a String. If this is a base position, any base offsets
     * are applied.
     */
	public synchronized String getXaxisAsString()
	{
		double xaxis ;
		if( getTag().equalsIgnoreCase( "base" ) )
			xaxis = RADecMath.getAbsolute( _xaxis , _yaxis , _xoff , _yoff )[ 0 ] ;
		else
			xaxis = _xaxis ;

		return String.valueOf( xaxis ) ;
	}

	/**
     * Get the yaxis as a String. If this is a base position, any base offsets
     * are applied.
     */
	public synchronized String getYaxisAsString()
	{
		double yaxis ;
		if( getTag().equalsIgnoreCase( "base" ) )
			yaxis = RADecMath.getAbsolute( _xaxis , _yaxis , _xoff , _yoff )[ 1 ] ;
		else
			yaxis = _yaxis ;

		return String.valueOf( yaxis ) ;
	}

	/**
     * Get the yaxis as a String. If this is a base position, any base offsets
     * are not applied.
     */
	public synchronized String getRealYaxisAsString()
	{
		return String.valueOf( _yaxis ) ;
	}

	/**
     * Set the x/y position and notify observers
     */
	public void setXY( double x , double y )
	{
		synchronized( this )
		{
			_xaxis = x ;
			_yaxis = y ;
		}
		_notifyOfLocationUpdate() ;
	}

	/**
     * Add a watcher.
     */
	public synchronized void addWatcher( TelescopePosWatcher tpw )
	{
		if( _watchers == null )
			_watchers = new Vector<TelescopePosWatcher>() ;
		else if( _watchers.contains( tpw ) )
			return ;

		_watchers.addElement( tpw ) ;
	}

	/**
     * Remove a watcher.
     */
	public synchronized void deleteWatcher( TelescopePosWatcher tpw )
	{
		if( _watchers != null )
			_watchers.removeElement( tpw ) ;
	}

	/**
     * Remove all watchers.
     */
	public synchronized void deleteWatchers()
	{
		if( _watchers != null )
			_watchers.removeAllElements() ;
	}

	/**
     * Copy the watchers list.
     */
	protected final synchronized Vector<TelescopePosWatcher> _getWatchers()
	{
		if( _watchers != null )
			return ( Vector<TelescopePosWatcher> )_watchers.clone() ;
		else
			return null ;
	}

	/**
     * Notify of a location update.
     */
	protected void _notifyOfLocationUpdate()
	{
		Vector<TelescopePosWatcher> v = _getWatchers() ;
		if( v != null )
		{
			for( int i = 0 ; i < v.size() ; ++i )
			{
				TelescopePosWatcher tpw ;
				tpw = v.elementAt( i ) ;
				tpw.telescopePosLocationUpdate( this ) ;
			}
		}
	}

	/**
     * Notify of a generic/non-location update.
     */
	protected void _notifyOfGenericUpdate()
	{
		Vector<TelescopePosWatcher> v = _getWatchers() ;
		if( v != null )
		{
			for( int i = 0 ; i < v.size() ; ++i )
			{
				TelescopePosWatcher tpw ;
				tpw = v.elementAt( i ) ;
				tpw.telescopePosGenericUpdate( this ) ;
			}
		}
	}

	/**
     * Select this position.
     */
	public void select()
	{
		if( _list != null )
			_list.setSelectedPos( this ) ;
	}

	/**
     * Debugging method.
     */
	public synchronized String toString()
	{
		return getClass().getName() + "[tag=" + getTag() + ", xaxis=" + HHMMSS.valStr( getXaxis() ) + ", yaxis=" + DDMMSS.valStr( getYaxis() ) + ", xoff=" + _xoff + ", yoff=" + _yoff + "]" ;
	}
}
