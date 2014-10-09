// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package gemini.sp ;

import gemini.util.TelescopePos ;

/**
 * A data object that describes an offset position and includes methods for
 * extracting positions from A/V tables.
 */
@SuppressWarnings( "serial" )
public final class SpOffsetPos extends TelescopePos
{

	// The prefix of all SpOffsetPos tags
	public static final String OFFSET_TAG = "Offset" ;

	// Prefix for all sky offset tags
	public static final String SKY_TAG = "SkyOffset" ;

	// Prefix for all guide offset tags
	public static final String GUIDE_TAG = "GuideOffset" ;

	// Indices of the the fields of a position
	public static final int XAXIS_INDEX = 0 ;

	public static final int YAXIS_INDEX = 1 ;

	private SpAvTable _avTab ; // The table that holds this position

	/**
     * Create a SpOffsetPos object, bound to an attribute with the same name as
     * its tag. SpTelescopePos objects are created by the SpTelescopePosList.
     */
	protected SpOffsetPos( SpAvTable avTab , String tag , SpOffsetPosList list )
	{
		super( tag , list ) ;
		_avTab = avTab ;

		if( avTab.exists( tag ) )
		{
			_xaxis = avTab.getDouble( tag , XAXIS_INDEX , 0.0 ) ;
			_yaxis = avTab.getDouble( tag , YAXIS_INDEX , 0.0 ) ;
		}
		else
		{
			// Create a new (blank) position and a new attribute
			_xaxis = 0.0 ;
			_yaxis = 0.0 ;

			avTab.set( tag , 0.0 , XAXIS_INDEX ) ;
			avTab.set( tag , 0.0 , YAXIS_INDEX ) ;
		}
	}

	/**
     * Is this an offset position (always true). This method is part of the
     * TelescopePos and has to be overridden here to always return true.
     */
	public boolean isOffsetPosition()
	{
		return true ;
	}

	/**
     * Allow setting x and y axes without notifying observers. This can come in
     * handy when you want to do a bunch of updates in a row and don't want to
     * waste time notifying observers of each change.
     */
	public synchronized void noNotifySetXY( double xaxis , double yaxis )
	{
		_xaxis = xaxis ;
		_yaxis = yaxis ;
		_avTab.set( _tag , xaxis , XAXIS_INDEX ) ;
		_avTab.set( _tag , yaxis , YAXIS_INDEX ) ;
	}

	/**
     * Set the xaxis and the yaxis.
     */
	public void setXY( double xaxis , double yaxis )
	{
		synchronized( this )
		{
			_avTab.set( _tag , xaxis , XAXIS_INDEX ) ;
			_avTab.set( _tag , yaxis , YAXIS_INDEX ) ;
		}
		super.setXY( xaxis , yaxis ) ;
	}

	/**
     * Set the xaxis and the yaxis as Strings.
     */
	public void setXY( String xaxis , String yaxis )
	{
		double x = 0. ;
		double y = 0. ;
		try
		{
			x = Double.valueOf( xaxis ) ;
			y = Double.valueOf( yaxis ) ;
		}
		catch( Exception ex ){}

		setXY( x , y ) ;
	}

	/**
     * Return a string representation of this object for debugging.
     */
	public synchronized String toString()
	{
		return getClass().getName() + "[tag=" + _tag + ", xaxis=" + _xaxis + ", yaxis=" + _yaxis + "]" ;
	}
}
