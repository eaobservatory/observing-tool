/* ============================================================== */
/*                                                              */
/* UK Astronomy Technology Centre */
/* Royal Observatory, Edinburgh */
/* Joint Astronomy Centre, Hilo */
/* Copyright (c) PPARC 2003 */
/*                                                              */
/* ============================================================== */
// $Id$
package edfreq.region;

import edfreq.EdFreq;
import edfreq.SideBand;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * A GUI widget that is uses to specify ranges (intervals).
 * 
 * A RangeBar looks similar to a JScrollBar but it cannot be dragged as a whole.
 * Instead its left and write edge can be dragged independently of one another
 * thus allowing the interval which is covered by the bar to be changed.
 * <p>
 * 
 * <img src="doc-files/rangeBar.gif">
 * 
 * @author Martin Folger
 */
public class RangeBar extends JPanel implements MouseMotionListener , MouseListener
{

	protected static final Dimension DEFAULT_PREFERRED_SIZE = new Dimension( 100 , 20 );
	public static final Color DEFAULT_BAR_COLOR = Color.blue;
	public static final Color DEFAULT_RANGE_COLOR = SideBand.getSideBandColor();
	private Color _barColor = DEFAULT_BAR_COLOR;
	private Color _rangeColor = DEFAULT_RANGE_COLOR;
	private int _barX;
	private int _barY = 2;
	private int _barW;
	private int _barH;
	private int _minX;
	private int _maxX;
	private Vector<Integer> _xRanges = new Vector<Integer>( 2 );
	private Vector<Integer> _barXs = new Vector<Integer>();
	private Vector<Integer> _barWs = new Vector<Integer>();
	private int _editingRegion = -1;
	private boolean _barChanged = false;

	/**
     * Indicates the sideband that is represented by this RangeBar.
     * 
     * Whether or not this is the sideband with the main line is irrelevant for
     * this class. The meaning of _associatedSideBand differs from that of
     * {@link edfreq.region.SpectralRegionEditor#_sideBand}.
     */
	private int _associatedSideBand = EdFreq.SIDE_BAND_USB;

	private Observable _observable = new Observable()
	{

		public void notifyObservers( Object arg )
		{
			setChanged();
			super.notifyObservers( arg );
		}

		public void notifyObservers()
		{
			setChanged();
			super.notifyObservers();
		}
	};

	public RangeBar()
	{
		this( EdFreq.SIDE_BAND_USB );
		setBorder( new EtchedBorder( EtchedBorder.RAISED ) );

		_minX = 0;
		_maxX = getWidth();
		try
		{
			_xRanges.setElementAt( new Integer( 0 ) , 0 );
			_xRanges.setElementAt( new Integer( getWidth() ) , 1 );
		}
		catch( Exception e )
		{
			_xRanges.add( new Integer( 0 ) );
			_xRanges.add( new Integer( getWidth() ) );
		}
	}

	public RangeBar( int associatedSideBand )
	{
		_associatedSideBand = associatedSideBand;
		setBorder( new EtchedBorder( EtchedBorder.RAISED ) );

		addMouseMotionListener( this );
		addMouseListener( this );

		setPreferredSize( DEFAULT_PREFERRED_SIZE );
		setBorder( BasicBorders.getButtonBorder() );

		setBar( ( int )( DEFAULT_PREFERRED_SIZE.width / 3.0 ) , ( int )( DEFAULT_PREFERRED_SIZE.width / 3.0 ) );
		setToolTipText( "RangeBar" );
	}

	public RangeBar( int barX , int barWidth )
	{
		this( barX , barWidth , EdFreq.SIDE_BAND_USB );
	}

	public RangeBar( int barX , int barWidth , int associatedSideBand )
	{
		_associatedSideBand = associatedSideBand;
		setBorder( new EtchedBorder( EtchedBorder.RAISED ) );

		setBar( barX , barWidth );
		setToolTipText( "RangeBar" );
	}

	public void setRange( int minX , int maxX )
	{
		_minX = minX;
		_maxX = maxX;
		setBorder( new EtchedBorder( EtchedBorder.RAISED ) );
		_xRanges.setElementAt( new Integer( 0 ) , 0 );
		_xRanges.setElementAt( new Integer( getWidth() ) , 1 );

		_validateBarInRange();
	}

	public void setRange( int minX , int maxX , int region )
	{
		_minX = minX;
		_maxX = maxX;

		if( region == 0 )
			_xRanges.clear();

		try
		{
			_xRanges.setElementAt( new Integer( minX ) , 2 * region );
			_xRanges.setElementAt( new Integer( maxX ) , 2 * region + 1 );
		}
		catch( ArrayIndexOutOfBoundsException ex )
		{
			_xRanges.add( new Integer( minX ) );
			_xRanges.add( new Integer( maxX ) );
		}

		_validateBarInRange();
	}

	public void _validateBarInRange()
	{
		if( _barX < _minX )
			_barX = _minX;

		if( ( _barX + _barW ) > _maxX )
			_barW = _maxX - _barX;

		for( int i = 0 ; i < _xRanges.size() ; i += 2 )
		{
			for( int j = 0 ; j < _barXs.size() ; j++ )
			{
				int myBarX = _barXs.get( j ).intValue();
				int myBarW = _barWs.get( j ).intValue();
				if( myBarX < _xRanges.get( i ).intValue() )
					_barXs.setElementAt( _xRanges.get( j ) , j );

				if( ( myBarX + myBarW ) > _xRanges.get( i + 1 ).intValue() )
				{
					int newW = _xRanges.get( i + 1 ).intValue() - myBarX;
					_barWs.setElementAt( new Integer( newW ) , j );
				}
			}
		}
		repaint();
	}

	public void setBar( int x , int width )
	{
		setBar( x , width , 0 );
	}

	public void setBar( int x , int width , int region )
	{
		_barX = x;
		_barW = width;
		if( region == 0 )
		{
			_barXs.clear();
			_barWs.clear();
		}
		try
		{
			_barXs.setElementAt( new Integer( x ) , region );
			_barWs.setElementAt( new Integer( width ) , region );
		}
		catch( ArrayIndexOutOfBoundsException e )
		{
			_barXs.add( new Integer( x ) );
			_barWs.add( new Integer( width ) );
		}
		repaint();
	}

	public int getBarX()
	{
		return getBarX( 0 );
	}

	public int getBarX( int bar )
	{
		return _barXs.get( bar ).intValue();
	}

	public int getBarWidth()
	{
		return getBarWidth( 0 );
	}

	public int getBarWidth( int bar )
	{
		return _barWs.get( bar ).intValue();
	}

	public int getNumBars()
	{
		return _barXs.size();
	}

	public int getMinX()
	{
		return getMinX( 0 );
	}

	public int getMinX( int range )
	{
		return _xRanges.get( 2 * range ).intValue();
	}

	public int getMaxX()
	{
		return getMaxX( 0 );
	}

	public int getMaxX( int range )
	{
		return _xRanges.get( 2 * range + 1 ).intValue();
	}

	public int getNumRanges()
	{
		return _xRanges.size() / 2;
	}

	public int getAssociatedSideBand()
	{
		return _associatedSideBand;
	}

	public void addObserver( Observer o )
	{
		_observable.addObserver( o );
	}

	public void deleteObserver( Observer o )
	{
		_observable.deleteObserver( o );
	}

	public void deleteObservers()
	{
		_observable.deleteObservers();
	}

	public void paint( Graphics g )
	{
		super.paint( g );

		if( !isEnabled() )
			return;

		g.setColor( _rangeColor );
		for( int i = 0 ; i < _xRanges.size() ; i += 2 )
		{
			int minX = _xRanges.get( i ).intValue();
			int maxX = _xRanges.get( i + 1 ).intValue();
			g.fillRect( minX , _barY , ( maxX - minX ) , _barH );
		}

		g.setColor( _barColor );
		for( int i = 0 ; i < _barXs.size() ; i++ )
		{
			int barX = _barXs.get( i ).intValue();
			int barW = _barWs.get( i ).intValue();
			g.fillRect( barX , _barY , barW , _barH );
		}
	}

	public void setPreferredSize( Dimension d )
	{
		super.setPreferredSize( d );

		_barH = d.height - 4;
	}

	public void setForeground( Color fg )
	{
		super.setForeground( fg );

		_barColor = fg;
	}

	public void setRangeColor( Color rangeColor )
	{
		_rangeColor = rangeColor;
	}

	public boolean hasChanged()
	{
		return _barChanged;
	}

	public void setBarChanged( boolean b )
	{
		_barChanged = b;
	}

	public void mouseDragged( MouseEvent e )
	{
		if( !isEnabled() )
			return;

		if( e.getModifiers() != MouseEvent.BUTTON1_MASK )
			return;

		if( _editingRegion == -1 )
			return;

		_barChanged = true;
		int barX = _barXs.get( _editingRegion ).intValue();
		int barW = _barWs.get( _editingRegion ).intValue();

		if( getCursor().getType() == Cursor.W_RESIZE_CURSOR )
		{

			// West edge of bar reaches east edge of bar
			if( e.getX() > ( barX + barW ) )
			{
				barX = ( barX + barW ) - 1;
				barW = 1;

				_validateBarInRange();
				e.translatePoint( barX - e.getX() , e.getY() );
			}
			else
			{
				// West edge of bar reaches _minX.
				int minX = _xRanges.get( 2 * _editingRegion ).intValue();
				if( e.getX() <= minX )
				{
					barW = ( barX + barW ) - minX;
					barX = minX;

					_validateBarInRange();
					e.translatePoint( barX - e.getX() , e.getY() );
				}
				else
				{
					barW = ( barX + barW ) - e.getX();
					barX = e.getX();
				}
			}

			_barXs.setElementAt( new Integer( barX ) , _editingRegion );
			_barWs.setElementAt( new Integer( barW ) , _editingRegion );

			if( e.getX() >= 0 )
			{
				// Note that notifyObservers has been overriden such that
				// Observable.setChanged() is called from within it.
				_observable.notifyObservers( e );
			}

			repaint();
		}

		if( getCursor().getType() == Cursor.E_RESIZE_CURSOR )
		{
			// East edge of bar reaches west edge of bar
			if( ( e.getX() - barX ) < 1 )
			{
				barW = 1;

				_validateBarInRange();
				e.translatePoint( ( barX - e.getX() ) + barW , e.getY() );
			}
			else
			{
				// East edge of bar reaches east edge of bounds
				int maxX = _xRanges.get( 2 * _editingRegion + 1 ).intValue();
				if( e.getX() >= maxX )
				{
					barW = maxX - barX;

					_validateBarInRange();
					e.translatePoint( ( barX - e.getX() ) + barW , e.getY() );
				}
				else
				{
					barW = e.getX() - barX;
				}
			}

			_barXs.setElementAt( new Integer( barX ) , _editingRegion );
			_barWs.setElementAt( new Integer( barW ) , _editingRegion );

			// Note that notifyObservers has been overriden such that
			// Observable.setChanged() is called from within it.
			_observable.notifyObservers( e );

			repaint();
		}
	}

	public void mouseMoved( MouseEvent e )
	{

		for( int i = 0 ; i < _barXs.size() ; i++ )
		{
			int barX = _barXs.get( i ).intValue();
			int barW = _barWs.get( i ).intValue();
			if( Math.abs( e.getX() - barX ) < 4 )
			{
				setCursor( Cursor.getPredefinedCursor( Cursor.W_RESIZE_CURSOR ) );
				_editingRegion = i;
				return;
			}

			if( Math.abs( e.getX() - ( barX + barW ) ) < 4 )
			{
				setCursor( Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR ) );
				_editingRegion = i;
				return;
			}
		}

		setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
		_editingRegion = -1;
	}

	public void mouseClicked( MouseEvent e ){}

	public void mouseEntered( MouseEvent e ){}

	public void mouseExited( MouseEvent e ){}

	public void mousePressed( MouseEvent e ){}

	public void mouseReleased( MouseEvent e )
	{
		// Note that notifyObservers has been overriden such that
		// Observable.setChanged() is called from within it.
		_observable.notifyObservers();
	}

	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append( "barX=" + _barX );
		buf.append( ", barY=" + _barY );
		buf.append( ", barW=" + _barW );
		buf.append( ", barH=" + _barH );
		buf.append( ", minX=" + _minX );
		buf.append( ", maxX=" + _maxX + "\n" );
		for( int i = 0 ; i < _xRanges.size() ; i += 2 )
			buf.append( ", xrange[" + i + "]=(" + _xRanges.get( i ) + ", " + _xRanges.get( i + 1 ) + ")\n" );

		for( int i = 0 ; i < _barXs.size() ; i++ )
			buf.append( "bar[" + i + "] = (" + _barXs.get( i ) + ", " + _barWs.get( i ) + ")\n" );

		return buf.toString();
	}

}
