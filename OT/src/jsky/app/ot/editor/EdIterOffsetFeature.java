// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor ;

import java.awt.Color ;
import java.awt.Graphics ;
import java.awt.Polygon ;
import java.awt.geom.Point2D ;

import java.util.Enumeration ;
import java.util.Vector ;

import jsky.app.ot.fits.gui.FitsImageInfo ;
import jsky.app.ot.fits.gui.FitsMouseEvent ;
import jsky.app.ot.fits.gui.FitsPosMapEntry ;

import gemini.sp.SpItem ;
import gemini.sp.SpOffsetPos ;
import gemini.sp.SpOffsetPosList ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpTelescopePosList ;
import gemini.sp.SpTreeMan ;
import gemini.sp.iter.SpIterOffset ;
import gemini.sp.obsComp.SpTelescopeObsComp;

import jsky.app.ot.tpe.TpeCreateableFeature ;
import jsky.app.ot.tpe.TpeDraggableFeature ;
import jsky.app.ot.tpe.TpeEraseableFeature ;
import jsky.app.ot.tpe.TpeImageFeature ;
import jsky.app.ot.tpe.TpeImageWidget ;
import jsky.app.ot.tpe.TpeSciArea ;
import jsky.app.ot.tpe.TpeSelectableFeature ;

import jsky.app.ot.util.BasicPropertyList ;
import jsky.app.ot.util.PropertyWatcher ;
import gemini.util.CoordSys ;
import gemini.util.PolygonD ;
import gemini.util.RADec ;
import gemini.util.TelescopePos ;
import gemini.util.TelescopePosList ;
import gemini.util.TelescopePosSelWatcher ;
import orac.jcmt.iter.SpIterRasterObs ;
import orac.util.MapArea ;
import orac.util.SpMapItem ;

/**
 * A TpeImageFeature extension to draw and manipulate offset positions on
 * the graphical position editor.  This feature will be displayed whenever
 * the SpIterOffset item is selected in the hierarchy view with the position
 * editor open.
 *
 * @author modified for JCMT (position angle, scan area display)
 *         by Martin Folger (M.Folger@roe.ac.uk)
 */
public class EdIterOffsetFeature extends TpeImageFeature implements TpeDraggableFeature , TpeEraseableFeature , TpeCreateableFeature , TpeSelectableFeature , PropertyWatcher
{
	/**
	 * The mode that indicates that the science area should not be drawn
	 * at any offset positions.
	 */
	public static final int SCI_AREA_NONE = 0 ;

	/**
	 * The mode that indicates that the science area should be drawn around
	 * the selected offset position.
	 */
	public static final int SCI_AREA_SELECTED = 1 ;

	/**
	 * The mode that indicates that the science area should be drawn around
	 * all the selected offset positions.
	 */
	public static final int SCI_AREA_ALL = 2 ;

	// Properties supported by this feature
	private static final String PROP_SHOW_TAGS = "Show Tags" ;

	private static final String PROP_SCI_AREA_DISPLAY = "Sci. Area Display" ;
	private static BasicPropertyList _props ;
	private SpIterOffset _iterOffset ;
	private TelescopePosSelWatcher _selWatcher ; // watch off. pos. selections
	private SpOffsetPosList _opl ;
	private OffsetPosMap _opm ;
	private FitsPosMapEntry _dragObject ;
	private Vector<SpMapItem> _mapItems = new Vector<SpMapItem>() ;

	static
	{
		// Initialize the properties supported by the Offset feature.
		_props = new BasicPropertyList() ;
		_props.setBoolean( PROP_SHOW_TAGS , true ) ;
		String[] choices = { "none" , "selected posn" , "all posn" } ;
		_props.setChoice( PROP_SCI_AREA_DISPLAY , choices , 0 ) ;
	}

	/**
	 * Construct the feature with its name and description.
	 */
	public EdIterOffsetFeature()
	{
		super( "Offset" , "Location of the offset positions." ) ;
	}

	/**
	 * Reinitialize the feature (after the base position moves for instance).
	 * Part of the TpeImageFeature interface and called by the TpeImageWidget.
	 */
	public void reinit( TpeImageWidget iw , FitsImageInfo fii )
	{
		// Quit watching for selections on the old position list.
		if( _opl != null )
			_opl.deleteSelWatcher( _selWatcher ) ;

		_iterOffset = null ;
		_opl = null ;

		// This should always be true ...
		if( iw.getBaseItem() instanceof SpIterOffset )
		{
			_iterOffset = ( SpIterOffset )iw.getBaseItem() ;
			_opl = _iterOffset.getCurrentPosList() ; // changed by MFO, 15 February 2002 and 1 May 2002
			_mapItems.clear() ;
			findMapItems( _iterOffset , _mapItems ) ;

			if( _selWatcher == null )
			{
				// Watch for selections of offset positions.  When one is selected,
				// determine whether there is a need to draw the science area at
				// the selected position.  If so, redraw the feature.
				_selWatcher = new TelescopePosSelWatcher()
				{
					public void telescopePosSelected( TelescopePosList tpl , TelescopePos tp )
					{
						if( getSciAreaMode() == SCI_AREA_SELECTED )
							redraw() ;
					}
				} ;
			}
			_opl.addSelWatcher( _selWatcher ) ;

			if( ( _iw != iw ) && ( _opm != null ) )
			{
				_opm.free() ;
				_opm = null ;
			}

			super.reinit( iw , fii ) ;

			// Watch for changes to the properties of this feature.
			_props.addWatcher( this ) ;

			if( _opm == null )
				_opm = new OffsetPosMap( iw ) ;

			if( _iterOffset != null )
				_opm.reset( _opl ) ;
		}
	}

	/**
	 * Unloaded, so free the position map.
	 */
	public void unloaded()
	{
		if( _opm != null )
		{
			_opm.free() ;
			_opm = null ;
		}
		_iw = null ;

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
	 * Turn on/off the drawing of the offset index.
	 */
	public void setDrawIndex( boolean drawIndex )
	{
		_props.setBoolean( PROP_SHOW_TAGS , drawIndex ) ;
	}

	/**
	 * Get the state of the drawing of the offset index.
	 */
	public final boolean getDrawIndex()
	{
		return _props.getBoolean( PROP_SHOW_TAGS , true ) ;
	}

	/**
	 * Set the science area draw mode.  Must be one of SCI_AREA_NONE,
	 * SCI_AREA_SELECTED, or SCI_AREA_ALL.
	 */
	public void setSciAreaMode( int mode )
	{
		_props.setChoice( PROP_SCI_AREA_DISPLAY , mode ) ;
	}

	/**
	 * Get the mode.  One of SCI_AREA_NONE, SCI_AREA_SELECTED, or SCI_AREA_ALL.
	 */
	public final int getSciAreaMode()
	{
		return _props.getChoice( PROP_SCI_AREA_DISPLAY , SCI_AREA_NONE ) ;
	}

	/**
	 * Redraw.
	 */
	public void redraw()
	{
		if( _iw != null )
			_iw.repaint() ;
	}

	/**
	 * Draw the offset positions.
	 */
	public void draw( Graphics g , FitsImageInfo fii )
	{
		g.setColor( Color.yellow ) ;
		g.setFont( FONT ) ;

		int r = MARKER_SIZE / 2 + 1 ;
		int d = MARKER_SIZE + 2 ;

		TpeSciArea tsa = null ;
		if( getSciAreaMode() != SCI_AREA_NONE )
			tsa = _iw.getSciArea() ;

		if( _opm != null )
		{
			Enumeration<FitsPosMapEntry> e = _opm.getAllPositionMapEntries() ;
			while( e.hasMoreElements() )
			{
				FitsPosMapEntry pme = e.nextElement() ;
				if( pme.telescopePos.getTag().startsWith( SpOffsetPos.GUIDE_TAG ) )
					g.setColor( Color.blue ) ;
				else
					g.setColor( Color.yellow ) ;

				Point2D.Double p = pme.screenPos ;
				// Rotate this point based on the current pos angle off the science area
				g.drawOval( ( int )( p.x - r ) , ( int )( p.y - r ) , d , d ) ;

				if( getDrawIndex() )
				{
					// Should probably use font metrics to position the tag ...
					// This is rather arbitrary ...
					SpOffsetPos op = ( SpOffsetPos )pme.telescopePos ;
					if( !( op.getTag().startsWith( SpOffsetPos.GUIDE_TAG ) ) )
						g.drawString( String.valueOf( _opl.getPositionIndex( op ) ) , ( int )p.x + d , ( int )p.y + d + r ) ;
				}

				switch( getSciAreaMode() )
				{
					case SCI_AREA_SELECTED :
						if( pme.telescopePos == _opl.getSelectedPos() )
							g.drawPolygon(tsa.getPolygonAt(p.x, p.y));
						break ;
					case SCI_AREA_ALL :
						g.drawPolygon(tsa.getPolygonAt(p.x, p.y));
						break ;
				}

				if( _mapItems != null )
				{
					for( int j = 0 ; j < _mapItems.size() ; j++ )
						g.drawPolygon(getPolygon(p.x, p.y, _mapItems.get(j), fii, pme));
				}
			}
		}
	}

	/**
	 * Start dragging an offset position.
	 *
	 * @see TpeDraggableFeature
	 */
	public boolean dragStart( FitsMouseEvent fme , FitsImageInfo fii )
	{
		if( _opm == null )
			return false ;

		_dragObject = _opm.locate( fme.xWidget , fme.yWidget ) ;
		return( _dragObject != null ) ;
	}

	/**
	 * Drag an offset position.
	 *
	 * @see TpeDraggableFeature
	 */
	public void drag( FitsMouseEvent fme )
	{
		if( _dragObject != null )
		{
			_dragObject.screenPos.x = fme.xWidget ;
			_dragObject.screenPos.y = fme.yWidget ;
			_iw.repaint() ;
		}
	}

	/**
	 * Stop dragging an offset position.
	 *
	 * @see TpeDraggableFeature
	 */
	public void dragStop( FitsMouseEvent fme )
	{
		if( _dragObject != null )
		{
			_opm.updatePosition( _dragObject , fme ) ;
			_dragObject = null ;
		}
	}

	/**
	 * If there is an offset position under the mouse, erase it and
	 * return true.  Return false otherwise.
	 *
	 * @see TpeEraseableFeature
	 */
	public boolean erase( FitsMouseEvent fme )
	{
		if( _opm == null )
			return false ;

		FitsPosMapEntry pme = _opm.locate( fme.xWidget , fme.yWidget ) ;
		if( pme == null )
			return false ;

		SpOffsetPos op = ( SpOffsetPos )pme.telescopePos ;
		_opl.removePosition( op ) ;
		return true ;
	}

	/**
	 * If there is an offset position under the mouse, return it.
	 *
	 * @see TpeCreateableFeature
	 */
	public Object select( FitsMouseEvent fme )
	{
		if( _opm == null )
			return null ;

		FitsPosMapEntry pme = _opm.locate( fme.xWidget , fme.yWidget ) ;
		if( pme == null )
			return null ;

		pme.telescopePos.select() ;
		return pme.telescopePos ;
	}

	/**
	 * Get the label that should be displayed on the create button.
	 */
	public String[] getCreateButtonLabels()
	{
		String[] s = { "Offset" } ;
		return s ;
	}

	/**
	 * Create an offset position at the given mouse position, if possible.
	 * Return true if anything is actually created.
	 *
	 * @see TpeCreateableFeature
	 */
	public boolean create( FitsMouseEvent fme , FitsImageInfo fii , String label )
	{
		_opl.createPosition( fme.xOffset , fme.yOffset ) ;
		return true ;
	}

	/**
	 *
	 */
	private static void findMapItems( SpItem spItem , Vector<SpMapItem> mapItemVector )
	{
		Enumeration<SpItem> children = spItem.children() ;
		SpItem child = null ;

		while( children.hasMoreElements() )
		{
			child = children.nextElement() ;

			if( child instanceof SpMapItem )
				mapItemVector.add( ( SpMapItem )child ) ;

			findMapItems( child , mapItemVector ) ;
		}
	}

	private Polygon getPolygon( double x , double y , SpMapItem spMapItem , FitsImageInfo fii , FitsPosMapEntry pme )
	{
		Polygon polygon = null ;
		if( spMapItem instanceof SpIterRasterObs )
			polygon = getPolygonAt( pme.telescopePos , _iw , ( SpIterRasterObs )spMapItem ) ;

		if( polygon == null )
		{
			polygon = new Polygon() ;
			double corner_x , corner_y , corner_x_rotated , corner_y_rotated ;
			double hw = ( spMapItem.getWidth() / 2 ) * fii.pixelsPerArcsec ;
			double hh = ( spMapItem.getHeight() / 2 ) * fii.pixelsPerArcsec ;
			double posAngle = ( spMapItem.getPosAngle() * Math.PI ) / 180. ;
			double cosAngle = Math.cos( posAngle ) ;
			double sinAngle = Math.sin( posAngle ) ;

			// Upper right corner
			corner_x = hw ;
			corner_y = hh ;
			corner_x_rotated = ( corner_x * cosAngle ) + ( corner_y * sinAngle ) ;
			corner_y_rotated = ( corner_x * -sinAngle ) + ( corner_y * cosAngle ) ;

			polygon.addPoint( ( int )( corner_x_rotated + x ) , ( int )( corner_y_rotated + y ) ) ;

			// Upper left corner
			corner_x = -hw ;
			corner_y = hh ;
			corner_x_rotated = ( corner_x * cosAngle ) + ( corner_y * Math.sin( posAngle ) ) ;
			corner_y_rotated = ( corner_x * -sinAngle ) + ( corner_y * cosAngle ) ;

			polygon.addPoint( ( int )( corner_x_rotated + x ) , ( int )( corner_y_rotated + y ) ) ;

			// Lower left corner
			corner_x = -hw ;
			corner_y = -hh ;
			corner_x_rotated = ( corner_x * cosAngle ) + ( corner_y * sinAngle ) ;
			corner_y_rotated = ( corner_x * -sinAngle ) + ( corner_y * cosAngle ) ;

			polygon.addPoint( ( int )( corner_x_rotated + x ) , ( int )( corner_y_rotated + y ) ) ;

			// Lower right corner
			corner_x = hw ;
			corner_y = -hh ;
			corner_x_rotated = ( corner_x * cosAngle ) + ( corner_y * sinAngle ) ;
			corner_y_rotated = ( corner_x * -sinAngle ) + ( corner_y * cosAngle ) ;

			polygon.addPoint( ( int )( corner_x_rotated + x ) , ( int )( corner_y_rotated + y ) ) ;
		}
		return polygon ;
	}

	public Polygon getPolygonAt( TelescopePos offset , TpeImageWidget _iw , SpIterRasterObs iterRaster )
	{
		if( offset != null && iterRaster != null && _iw != null )
		{
			SpTelescopeObsComp targetList = SpTreeMan.findTargetList( iterRaster ) ;
			SpTelescopePosList list = targetList.getPosList() ;
			SpTelescopePos position = list.getBasePosition() ;
			if( position.getCoordSys() == CoordSys.GAL )
			{
				RADec[] positions = MapArea.createNewMapArea( position.getXaxis() , position.getYaxis() , offset.getXaxis() , offset.getYaxis() , iterRaster.getWidth() , iterRaster.getHeight() , iterRaster.getPosAngle() ) ;

				PolygonD pd = new PolygonD() ;
				pd.xpoints = new double[ 5 ] ;
				pd.ypoints = new double[ 5 ] ;
				pd.npoints = 5 ;
				double[] xpoints = pd.xpoints ;
				double[] ypoints = pd.ypoints ;

				Point2D.Double point = _iw.raDecToImageWidget( positions[ 0 ].ra , positions[ 0 ].dec ) ;
				xpoints[ 0 ] = point.x ;
				ypoints[ 0 ] = point.y ;

				point = _iw.raDecToImageWidget( positions[ 1 ].ra , positions[ 1 ].dec ) ;
				xpoints[ 1 ] = point.x ;
				ypoints[ 1 ] = point.y ;

				point = _iw.raDecToImageWidget( positions[ 2 ].ra , positions[ 2 ].dec ) ;
				xpoints[ 2 ] = point.x ;
				ypoints[ 2 ] = point.y ;

				point = _iw.raDecToImageWidget( positions[ 3 ].ra , positions[ 3 ].dec ) ;
				xpoints[ 3 ] = point.x ;
				ypoints[ 3 ] = point.y ;

				xpoints[ 4 ] = xpoints[ 0 ] ;
				ypoints[ 4 ] = ypoints[ 0 ] ;

				return pd.getAWTPolygon() ;
			}
		}
		return null ;
	}
}
