// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import gemini.util.TelescopePos;

/**
 * A data object that describes an offset position and includes methods for
 * extracting positions from A/V tables.
 */
public final class SpOffsetPos extends TelescopePos
{

	// The prefix of all SpOffsetPos tags
	public static final String OFFSET_TAG = "Offset";

	// Prefix for all sky offset tags
	public static final String SKY_TAG = "SkyOffset";

	// Prefix for all guide offset tags
	public static final String GUIDE_TAG = "GuideOffset";

	// Indices of the the fields of a position
	public static final int XAXIS_INDEX = 0;

	public static final int YAXIS_INDEX = 1;

	private SpAvTable _avTab; // The table that holds this position

	/**
     * Create a SpOffsetPos object, bound to an attribute with the same name as
     * its tag. SpTelescopePos objects are created by the SpTelescopePosList.
     */
	protected SpOffsetPos( SpAvTable avTab , String tag , SpOffsetPosList list )
	{
		super( tag , list );
		_avTab = avTab;

		if( avTab.exists( tag ) )
		{
			_xaxis = avTab.getDouble( tag , XAXIS_INDEX , 0.0 );
			_yaxis = avTab.getDouble( tag , YAXIS_INDEX , 0.0 );
		}
		else
		{
			// Create a new (blank) position and a new attribute
			_xaxis = 0.0;
			_yaxis = 0.0;

			avTab.set( tag , 0.0 , XAXIS_INDEX );
			avTab.set( tag , 0.0 , YAXIS_INDEX );
		}
	}

	/**
     * Is this an offset position (always true). This method is part of the
     * TelescopePos and has to be overridden here to always return true.
     */
	public boolean isOffsetPosition()
	{
		return true;
	}

	/**
     * Allow setting x and y axes without notifying observers. This can come in
     * handy when you want to do a bunch of updates in a row and don't want to
     * waste time notifying observers of each change.
     */
	public synchronized void noNotifySetXY( double xaxis , double yaxis )
	{
		_xaxis = xaxis;
		_yaxis = yaxis;
		_avTab.set( _tag , xaxis , XAXIS_INDEX );
		_avTab.set( _tag , yaxis , YAXIS_INDEX );
	}

	/**
     * Set the xaxis and the yaxis.
     */
	public void setXY( double xaxis , double yaxis )
	{
		synchronized( this )
		{
			_avTab.set( _tag , xaxis , XAXIS_INDEX );
			_avTab.set( _tag , yaxis , YAXIS_INDEX );
		}
		super.setXY( xaxis , yaxis );
	}

	/**
     * Set the xaxis and the yaxis as Strings.
     */
	public void setXY( String xaxis , String yaxis )
	{
		double x = 0.0;
		double y = 0.0;
		try
		{
			x = Double.valueOf( xaxis ).doubleValue();
			y = Double.valueOf( yaxis ).doubleValue();
		}
		catch( Exception ex ){}

		setXY( x , y );
	}

	/**
     * Return a string representation of this object for debugging.
     */
	public synchronized String toString()
	{
		return getClass().getName() + "[tag=" + _tag + ", xaxis=" + _xaxis + ", yaxis=" + _yaxis + "]";
	}
}
