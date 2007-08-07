// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe.feat;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Enumeration;

import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.fits.gui.FitsPosMapEntry;
import jsky.app.ot.fits.gui.FitsMouseEvent;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTelescopePosList;
import jsky.app.ot.util.BasicPropertyList;
import jsky.app.ot.util.PropertyWatcher;

import jsky.app.ot.tpe.TpeImageWidget;
import jsky.app.ot.tpe.TpePositionMap;
import jsky.app.ot.tpe.TpeCreateableFeature;
import java.awt.geom.Point2D;

public class TpeTargetPosFeature extends TpePositionFeature implements TpeCreateableFeature , PropertyWatcher
{
	private static final String PROP_SHOW_TAGS = "Show Tags";
	private static BasicPropertyList _props;

	static
	{
		// Initialize the properties supported by the TpeGuidePosFeature.

		_props = new BasicPropertyList();
		_props.setBoolean( PROP_SHOW_TAGS , true );
	}

	/**
	 * Construct the feature with its name and description. 
	 */
	public TpeTargetPosFeature()
	{
		super( "Target" , "Location of target positions." );
	}

	public void reinit( TpeImageWidget iw , FitsImageInfo fii )
	{
		super.reinit( iw , fii );

		_props.addWatcher( this );

		// Tell the position map that the target positions are visible.
		TpePositionMap pm = TpePositionMap.getMap( iw );
		pm.setFindUserTarget( true );
	}

	public void unloaded()
	{
		// Tell the position map that the target positions are not visible.
		TpePositionMap pm = TpePositionMap.getExistingMap( _iw );
		if( pm != null )
			pm.setFindUserTarget( false );

		_props.deleteWatcher( this );

		super.unloaded();
	}

	/**
	 * A property has changed.
	 *
	 * @see PropertyWatcher
	 */
	public void propertyChange( String propName )
	{
		_iw.repaint( this );
	}

	/**
	 * Override getProperties to return the properties supported by this
	 * feature.
	 */
	public BasicPropertyList getProperties()
	{
		return _props;
	}

	/**
	 * Turn on/off the drawing of position tags.
	 */
	public void setDrawTags( boolean drawTags )
	{
		_props.setBoolean( PROP_SHOW_TAGS , drawTags );
	}

	/**
	 * Get the "draw position tags" property.
	 */
	public boolean getDrawTags()
	{
		return _props.getBoolean( PROP_SHOW_TAGS , true );
	}

	/**
	 */
	public String[] getCreateButtonLabels()
	{
		String[] s = { "Target" };
		return s;
	}

	/**
	 */
	public boolean create( FitsMouseEvent fme , FitsImageInfo fii , String label )
	{
		SpTelescopePosList tpl = getSpTelescopePosList();
		if( tpl == null )
			return false;

		tpl.createPosition( fme.ra , fme.dec );
		return true;
	}

	/**
	 */
	public boolean erase( FitsMouseEvent fme )
	{
		SpTelescopePosList tpl = getSpTelescopePosList();
		if( tpl == null )
			return false;

		TpePositionMap pm = TpePositionMap.getMap( _iw );

		Enumeration e = pm.getAllPositionMapEntries();
		while( e.hasMoreElements() )
		{
			FitsPosMapEntry pme = ( FitsPosMapEntry )e.nextElement();
			SpTelescopePos tp = ( SpTelescopePos )pme.telescopePos;

			if( positionIsClose( pme , fme.xWidget , fme.yWidget ) && tp.isUserPosition() )
			{
				tpl.removePosition( tp );
				return true;
			}
		}
		return false;
	}

	/**
	 * @see jsky.app.ot.tpe.TpeSelectableFeature
	 */
	public Object select( FitsMouseEvent fme )
	{
		TpePositionMap pm = TpePositionMap.getMap( _iw );
		SpTelescopePos tp = ( SpTelescopePos )pm.locatePos( fme.xWidget , fme.yWidget );
		if( ( tp != null ) && tp.isUserPosition() )
		{
			tp.select();
			return tp;
		}
		return null;
	}

	/**
	 */
	public void draw( Graphics g , FitsImageInfo fii )
	{
		TpePositionMap pm = TpePositionMap.getMap( _iw );

		g.setColor( Color.yellow );

		if( getDrawTags() )
			g.setFont( FONT );

		Enumeration e = pm.getAllPositionMapEntries();
		while( e.hasMoreElements() )
		{
			FitsPosMapEntry pme = ( FitsPosMapEntry )e.nextElement();
			SpTelescopePos tp = ( SpTelescopePos )pme.telescopePos;
			if( tp.isUserPosition() )
			{
				Point2D.Double p = pme.screenPos;
				if( p == null )
					continue;

				if( tp.isOffsetPosition() )
				{
					int r = MARKER_SIZE * 2;
					g.drawOval( ( int )( p.x - MARKER_SIZE ) , ( int )( p.y - MARKER_SIZE ) , r , r );
				}
				else
				{
					g.drawLine( ( int )p.x , ( int )( p.y - MARKER_SIZE ) , ( int )p.x , ( int )( p.y + MARKER_SIZE ) );
					g.drawLine( ( int )( p.x - MARKER_SIZE ) , ( int )p.y , ( int )( p.x + MARKER_SIZE ) , ( int )p.y );
				}

				if( getDrawTags() && ( tp.getTag() != null ) )
				{
					// Draw the tag--should use font metrics to position the tag
					g.drawString( tp.getTag() , ( int )( p.x + MARKER_SIZE + 2 ) , ( int )( p.y + MARKER_SIZE * 2 ) );
				}
			}
		}
	}

	/**
	 */
	public boolean dragStart( FitsMouseEvent fme , FitsImageInfo fii )
	{
		TpePositionMap pm = TpePositionMap.getMap( _iw );

		Enumeration e = pm.getAllPositionMapEntries();
		while( e.hasMoreElements() )
		{
			FitsPosMapEntry pme = ( FitsPosMapEntry )e.nextElement();

			if( positionIsClose( pme , fme.xWidget , fme.yWidget ) && ( ( SpTelescopePos )pme.telescopePos ).isUserPosition() )
			{
				_dragObject = pme;
				return true;
			}
		}
		return false;
	}
}
