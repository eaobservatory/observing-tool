// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe.feat ;

import java.awt.Color ;
import java.awt.Graphics ;
import java.awt.geom.Point2D ;
import java.util.Arrays ;
import java.util.ArrayList ;
import jsky.app.ot.fits.gui.FitsImageInfo ;
import jsky.app.ot.fits.gui.FitsMouseEvent ;
import jsky.app.ot.fits.gui.FitsPosMapEntry ;
import gemini.util.CoordSys ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpTelescopePosList ;
import jsky.app.ot.tpe.TpeCreateableFeature ;
import jsky.app.ot.tpe.TpeImageWidget ;
import jsky.app.ot.tpe.TpePositionMap ;
import jsky.app.ot.util.BasicPropertyList ;
import jsky.app.ot.util.PropertyWatcher ;

public class TpeGuidePosFeature extends TpePositionFeature implements TpeCreateableFeature , PropertyWatcher
{
	private static String DEFAULT_NAME = "Guide" ;
	private static String _tpeViewGuideLabel = null ;
	private static final String PROP_SHOW_TAGS = "Show Tags" ;
	private static BasicPropertyList _props ;

	static
	{
		// Initialize the properties supported by the TpeGuidePosFeature.

		_props = new BasicPropertyList() ;
		_props.setBoolean( PROP_SHOW_TAGS , true ) ;
	}

	/**
	 * Construct the feature with its name and description. 
	 */
	public TpeGuidePosFeature()
	{
		super( DEFAULT_NAME , "Location(s) of the guide stars." ) ;
	}

	public void reinit( TpeImageWidget iw , FitsImageInfo fii )
	{
		super.reinit( iw , fii ) ;

		_props.addWatcher( this ) ;

		// Tell the position map that the guide star choices are visible.
		TpePositionMap pm = TpePositionMap.getMap( iw ) ;
		pm.setFindGuideStars( true ) ;
	}

	public void unloaded()
	{
		// Tell the position map that the guide star choices are no longer visible.
		TpePositionMap pm = TpePositionMap.getExistingMap( _iw ) ;
		if( pm != null )
			pm.setFindGuideStars( false ) ;

		_props.deleteWatcher( this ) ;

		super.unloaded() ;
	}

	/**
	 * A property has changed.
	 * 
	 * @see PropertyWatcher
	 */
	public void propertyChange( String propName )
	{
		_iw.repaint( this ) ;
	}

	/**
	 * Override getProperties to return the properties supported by this
	 * feature.
	 */
	public BasicPropertyList getProperties()
	{
		return _props ;
	}

	/**
	 * Turn on/off the drawing of position tags.
	 */
	public void setDrawTags( boolean drawTags )
	{
		_props.setBoolean( PROP_SHOW_TAGS , drawTags ) ;
	}

	/**
	 * Get the "draw position tags" property.
	 */
	public boolean getDrawTags()
	{
		return _props.getBoolean( PROP_SHOW_TAGS , true ) ;
	}

	/**
	 */
	public String[] getCreateButtonLabels()
	{
		return SpTelescopePos.getGuideStarTags() ;
	}

	/**
	 */
	public boolean create( FitsMouseEvent fme , FitsImageInfo fii , String label )
	{
		SpTelescopePosList tpl = getSpTelescopePosList() ;
		if( tpl == null )
			return false ;

		String tag = null ;
		String[] guideTags = SpTelescopePos.getGuideStarTags() ;
		for( int i = 0 ; i < guideTags.length ; ++i )
		{
			if( label.startsWith( guideTags[ i ] ) )
			{
				tag = label ;
				break ;
			}
		}
		if( tag == null )
			return false ;

		SpTelescopePos tp ;
		tp = ( SpTelescopePos )tpl.getPosition( tag ) ;

		// Try to find a catalog star near this one.
		if( tp != null )
		{
			tp.setOffsetPosition( false ) ;
			tp.setCoordSys( CoordSys.FK5 ) ;
			tp.setXY( fme.ra , fme.dec ) ;
			String name = tp.getName() ;
			if( ( name != null ) && !name.equals( "" ) )
				tp.setName( "" ) ;
		}
		else
		{
			if( "SKY".equals( tag ) || "SKYGUIDE".equals( tag ) )
			{
				int id = 0 ;
				while( tpl.exists( tag + id ) )
					id++ ;

				tag = tag + id ;
			}
			tp = tpl.createPosition( tag , fme.ra , fme.dec ) ;
		}
		return true ;
	}

	/**
	 */
	public boolean erase( FitsMouseEvent fme )
	{
		TpePositionMap pm = TpePositionMap.getMap( _iw ) ;
		SpTelescopePosList tpl = ( SpTelescopePosList )pm.getTelescopePosList() ;
		if( tpl == null )
			return false ;

		int x = fme.xWidget ;
		int y = fme.yWidget ;

		FitsPosMapEntry pme ;

		String[] guideTags = SpTelescopePos.getGuideStarTags() ;
		for( int i = 0 ; i < guideTags.length ; ++i )
		{
			pme = pm.getPositionMapEntry( guideTags[ i ] ) ;
			if( ( pme != null ) && ( positionIsClose( pme , x , y ) ) )
			{
				tpl.removePosition( ( SpTelescopePos )pme.telescopePos ) ;
				return true ;
			}
		}

		return false ;
	}

	/**
	 * @see jsky.app.ot.tpe.TpeSelectableFeature
	 */
	public Object select( FitsMouseEvent fme )
	{
		TpePositionMap pm = TpePositionMap.getMap( _iw ) ;

		int x = fme.xWidget ;
		int y = fme.yWidget ;

		FitsPosMapEntry pme ;
		String[] guideTags = SpTelescopePos.getGuideStarTags() ;
		for( int i = 0 ; i < guideTags.length ; ++i )
		{
			pme = pm.getPositionMapEntry( guideTags[ i ] ) ;
			if( ( pme != null ) && ( positionIsClose( pme , x , y ) ) )
			{
				pme.telescopePos.select() ;
				return pme.telescopePos ;
			}
		}
		return null ;
	}

	/**
	 */
	private final void _drawGuideStar( Graphics g , Point2D.Double p , int size , String tag )
	{
		g.setColor( Color.green ) ;
		g.drawRect( ( int )( p.x - size / 2 ) , ( int )( p.y - size / 2 ) , size , size ) ;

		if( getDrawTags() )
		{
			// Draw the tag--should use font metrics to position the tag
			g.setFont( FONT ) ;
			g.drawString( tag , ( int )( p.x + size ) , ( int )( p.y + size ) ) ;
		}
	}

	private final void _drawSkyBox( Graphics g , Point2D.Double p , int size , String tag )
	{
		g.setColor( Color.green.darker() ) ;
		g.drawRect( ( int )( p.x - size / 2 ) , ( int )( p.y - size / 2 ) , size , size ) ;

		if( getDrawTags() )
		{
			// Draw the tag--should use font metrics to position the tag
			g.setFont( FONT ) ;
			g.drawString( tag , ( int )( p.x + size / 2 ) , ( int )( p.y - size / 2 ) ) ;
		}
	}

	/**
	 * Draw Additional Target as Circle.
	 *
	 * Additional Targets can be added to the OT's Telescope Position Editor as "Guide" stars
	 * even if they are not Guide stars at all. This feature was originally used for Guide stars only,
	 * hence the name.
	 *
	 * Some additional targets (not Guide stars) can be specified as offsets in Az/El. If that is the case
	 * then have to be drawn as a circle.
	 *
	 * @see #_drawGuidePosition(java.awt.Graphics,java.awt.geom.Point2D.Double,int,java.lang.String)
	 */
	private final void _drawGuideStar( Graphics g , Point2D.Double p , int size , String tag , Point2D.Double base )
	{
		g.setColor( Color.green ) ;
		double radius = Math.sqrt( ( ( p.x - base.x ) * ( p.x - base.x ) ) + ( ( p.y - base.y ) * ( p.y - base.y ) ) ) ;

		g.drawArc( ( int )( base.x - radius ) , ( int )( base.y - radius ) , ( int )( 2.0 * radius ) , ( int )( 2.0 * radius ) , 0 , 360 ) ;

		if( getDrawTags() )
		{
			// Draw the tag--should use font metrics to position the tag
			g.setFont( FONT ) ;
			g.drawString( tag , ( int )( p.x + size ) , ( int )( p.y + size ) ) ;
		}
	}

	/**
	 */
	public void draw( Graphics g , FitsImageInfo fii )
	{
		TpePositionMap pm = TpePositionMap.getMap( _iw ) ;

		Point2D.Double base = pm.getLocationFromTag( SpTelescopePos.BASE_TAG ) ;
		if( base == null )
			return ;

		// How many pixels do 10 arcsecs take?
		int size = ( int )( 10 * fii.pixelsPerArcsec ) ;

		Point2D.Double p ;
		ArrayList<String> tags = new ArrayList<String>( Arrays.asList( SpTelescopePos.getGuideStarTags() ) ) ;
		tags.addAll( Arrays.asList( SpTelescopePos.getSkyTags() ) ) ;
		for( int i = 0 ; i < tags.size() ; ++i )
		{
			String currentTag = tags.get( i ) ;
			p = pm.getLocationFromTag( currentTag ) ;
			if( p != null )
			{
				SpTelescopePosList tpl = getSpTelescopePosList() ;
				SpTelescopePos tp = null ;

				if( tpl != null )
					tp = ( SpTelescopePos )tpl.getPosition( currentTag ) ;

				if( tp != null )
				{
					switch( tp.getCoordSys() )
					{
						case CoordSys.AZ_EL :
							_drawGuideStar( g , p , size , currentTag , fii.baseScreenPos ) ;
							break ;
						default :
							_drawGuideStar( g , p , size , currentTag ) ;
							if( tp.getBoxSize() > 0.0 )
							{
								int skySize = ( int )( tp.getBoxSize() * fii.pixelsPerArcsec ) ;
								_drawSkyBox( g , p , skySize , "Random Area" ) ;
							}
					}
				}
				else
				{
					_drawGuideStar( g , p , size , currentTag ) ;
				}
			}
		}
	}

	/**
	 */
	public boolean dragStart( FitsMouseEvent fme , FitsImageInfo fii )
	{
		TpePositionMap pm = TpePositionMap.getMap( _iw ) ;
		FitsPosMapEntry pme ;

		int x = fme.xWidget ;
		int y = fme.yWidget ;

		ArrayList<String> tags = new ArrayList<String>( Arrays.asList( SpTelescopePos.getGuideStarTags() ) ) ;
		tags.addAll( Arrays.asList( SpTelescopePos.getSkyTags() ) ) ;
		for( int i = 0 ; i < tags.size() ; ++i )
		{
			pme = pm.getPositionMapEntry( tags.get( i ) ) ;
			if( ( pme != null ) && ( positionIsClose( pme , x , y ) ) )
			{
				_dragObject = pme ;

				SpTelescopePos tp = ( SpTelescopePos )_dragObject.telescopePos ;

				tp.setOffsetPosition( false ) ;

				return true ;
			}
		}

		return false ;
	}

	/**
	 */
	public void dragStop( FitsMouseEvent fme )
	{
		if( _dragObject != null )
		{
			SpTelescopePos tp = ( SpTelescopePos )_dragObject.telescopePos ;
	
			// See if we can snap to a catalog star
			// Code removed in 7dab9164629e0d4acef1f359b10c2b0b3359ff24
			// but has been commented out for entire git history.
			boolean snappedToCatStar = false ;
	
			if( !snappedToCatStar )
			{
				super.dragStop( fme ) ;
				tp.setName( "" ) ;
			}
	
			_dragObject = null ;
		}
	}

	/**
	 * Get the feature's name.
	 */
	public String getName()
	{
		return getTpeViewGuideLabel() ;
	}

	/**
	 * Get the String used as button text for telescope position editor view button.
	 */
	public static String getTpeViewGuideLabel()
	{
		if( _tpeViewGuideLabel == null )
		{
			String[] guideTags = SpTelescopePos.getGuideStarTags() ;

			if( ( guideTags != null ) && ( guideTags.length > 0 ) )
			{
				if( guideTags.length == 1 )
					_tpeViewGuideLabel = guideTags[ 0 ] ;
				else
					_tpeViewGuideLabel = guideTags[ 0 ] + " etc" ;
			}
			else
			{
				_tpeViewGuideLabel = DEFAULT_NAME ;
			}
		}

		return _tpeViewGuideLabel ;
	}
}
