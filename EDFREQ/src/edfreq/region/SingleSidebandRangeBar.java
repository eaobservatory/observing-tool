/* ============================================================== */
/*                                                              */
/* UK Astronomy Technology Centre */
/* Royal Observatory, Edinburgh */
/* Joint Astronomy Centre, Hilo */
/* Copyright (c) PPARC 2003 */
/*                                                              */
/* ============================================================== */
// $Id$
package edfreq.region ;

import javax.swing.JPanel ;
import javax.swing.JFrame ;
import javax.swing.border.BevelBorder ;
import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.event.MouseEvent ;
import java.util.Observable ;
import java.util.Observer ;
import java.util.Vector ;

import edfreq.EdFreq ;

/**
 * JPanel containing two RangeBars representing the two side bands.
 * 
 * @author Martin Folger
 */
@SuppressWarnings( "serial" )
public class SingleSidebandRangeBar extends JPanel implements Observer
{
	private RangeBar _bar ;

	/**
     * Width of SingleSidebandRangeBar in GUI pixels.
     * 
     * The total width represents the frequency range
     * <tt>feBandWidth + feIF</tt> where <tt>feBandWidth</tt> is the
     * frontend bandwidth and <tt>feIF</tt> is the frontend IF.
     */
	private int _totalWidth ;

	/** Frontend sideband width in pixels. */
	private int _feBandWidth ;

	/** Lower limit of backend sideband in pixels. */
	private int _beBandMin ;

	/** Upper limit of backend sideband in pixels. */
	private int _beBandMax ;

	private int _sideband = EdFreq.SIDE_BAND_USB ;

	private boolean _isDSB ;

	Vector<Integer> _xRanges = new Vector<Integer>() ;

	public SingleSidebandRangeBar( int sideband )
	{
		_sideband = sideband ;
		setLayout( null ) ;

		_bar = new RangeBar( _sideband ) ;

		add( _bar ) ;

		// Event handling
		_bar.addObserver( this ) ;

		setBorder( new BevelBorder( BevelBorder.RAISED ) ) ;

	}

	public SingleSidebandRangeBar()
	{
		this( EdFreq.SIDE_BAND_USB ) ;
	}

	public void resetRangeBar( int feBandWidth , int totalWidth , int[] beBandRange )
	{
		// Look for overlapping ranges
		_xRanges.add( new Integer( beBandRange[ 0 ] ) ) ;
		_xRanges.add( new Integer( beBandRange[ 1 ] ) ) ;
		int[] xRanges = { beBandRange[ 0 ] , beBandRange[ 1 ] } ;
		if( beBandRange.length > 2 )
		{
			// There is more than one range, so see if they overlap
			for( int i = 2 ; i < beBandRange.length ; i += 2 )
			{
				for( int j = 0 ; j < _xRanges.size() ; j += 2 )
				{
					if( beBandRange[ i ] >= xRanges[ 0 ] && beBandRange[ i ] <= xRanges[ 1 ] )
					{
						xRanges[ 1 ] = beBandRange[ i + 1 ] ;
						_xRanges.setElementAt( new Integer( beBandRange[ i + 1 ] ) , j + 1 ) ;
					}
					else if( beBandRange[ i + 1 ] >= xRanges[ 0 ] && beBandRange[ i + 1 ] <= xRanges[ 1 ] )
					{
						xRanges[ 0 ] = beBandRange[ i ] ;
						_xRanges.setElementAt( new Integer( beBandRange[ i ] ) , j ) ;
					}
					else if( beBandRange[ i ] < xRanges[ 0 ] && beBandRange[ i + 1 ] > xRanges[ 1 ] )
					{
						xRanges[ 1 ] = beBandRange[ i + 1 ] ;
						xRanges[ 0 ] = beBandRange[ i ] ;
						_xRanges.setElementAt( new Integer( beBandRange[ i ] ) , j ) ;
						_xRanges.setElementAt( new Integer( beBandRange[ i + 1 ] ) , j + 1 ) ;
					}
					else if( beBandRange[ i ] > xRanges[ 0 ] && beBandRange[ i + 1 ] < xRanges[ 1 ] ){}
					else
					{
						_xRanges.add( new Integer( beBandRange[ i ] ) ) ;
						_xRanges.add( new Integer( beBandRange[ i + 1 ] ) ) ;
						break ; // Break out of the inner loop
					}
				}
			}
		}
		System.out.println( "xRanges=" + _xRanges ) ;
		_feBandWidth = feBandWidth ;
		_totalWidth = totalWidth ;
		_beBandMin = xRanges[ 0 ] ;
		_beBandMax = xRanges[ 1 ] ;

		resetRangeBar() ;
	}

	public void resetRangeBar()
	{
		// Layout
		int height = RangeBar.DEFAULT_PREFERRED_SIZE.height ;

		setPreferredSize( new Dimension( _totalWidth , height ) ) ;

		// The RangeBar bounds refer to the frontend bandwidth.
		_bar.setBounds( 0 , 0 , _feBandWidth , height ) ;

		// The RangeBar range refers to the backend bandwidth.
		for( int i = 0 ; i < _xRanges.size() ; i += 2 )
		{
			if( _sideband == EdFreq.SIDE_BAND_LSB )
				_bar.setRange( _feBandWidth - _xRanges.get( i + 1 ) , _feBandWidth - _xRanges.get( i ) , i / 2 ) ;
			else
				_bar.setRange( _xRanges.get( i ) , _xRanges.get( i + 1 ) , i / 2 ) ;
		}
	}

	/**
     * @param feBandWidth
     *            Frontend bandwith in pixels
     * @param totalWidth
     *            2 * (frontend IF + (0.5 * frontend bandwith)) in pixels
     * @param sideBand
     *            EdFreq.SIDE_BAND_USB (upper sideband) or EdFreq.SIDE_BAND_LSB
     *            (lower sideband)
     * @param dsb
     *            If true: double sideband, else single sideband
     */
	public void resetRangeBar( int feBandWidth , int totalWidth , int[] beBandRange , int sideBand , boolean dsb )
	{
		resetRangeBar( feBandWidth , totalWidth , beBandRange ) ;

		_isDSB = dsb ;
		_sideband = sideBand ;
	}

	public void setForeground( Color fg )
	{
		super.setForeground( fg ) ;

		if( _bar != null )
			_bar.setForeground( fg ) ;
	}

	public void setRangeColor( Color bg )
	{
		if( _bar != null )
			_bar.setRangeColor( bg ) ;
	}

	/**
     * Set both bars from the values for the upper side band bar.
     */
	public void setBars( int barX , int barWidth )
	{
		setBars( barX , barWidth , 0 ) ;
	}

	public void setBars( int barX , int barWidth , int bar )
	{
		_bar.setBar( barX , barWidth , bar ) ;
	}

	public int getBarX( int bar )
	{
		return _bar.getBarX( bar ) ;
	}

	public int getBarWidth()
	{
		return _bar.getBarWidth() ;
	}

	public int getBarWidth( int bar )
	{
		return _bar.getBarWidth( bar ) ;
	}

	public int getNumBars()
	{
		return _bar.getNumBars() ;
	}

	/**
     * Note that the argument Observable o is not the RangeBar object.
     * 
     * Instead it is an Observable object which is a field of RangeBar.
     */
	public void update( Observable o , Object arg )
	{
		if( arg == null )
			return ;

		if( ( ( MouseEvent )arg ).getID() != MouseEvent.MOUSE_RELEASED )
			return ;

		Object source = ( ( MouseEvent )arg ).getSource() ;

		if( source == _bar )
		{
			for( int i = 0 ; i < _bar.getNumBars() ; i++ )
				_bar.setBar( _bar.getWidth() - ( _bar.getBarX( i ) + _bar.getBarWidth( i ) ) , _bar.getBarWidth( i ) , i ) ;
		}

	}

	public void addObserver( Observer o )
	{
		_bar.addObserver( o ) ;
	}

	public int getNumRanges()
	{
		return _bar.getNumRanges() ;
	}

	public int getMaxX( int i )
	{
		return _bar.getMaxX( i ) ;
	}

	public int getMinX( int i )
	{
		return _bar.getMinX( i ) ;
	}

	public boolean hasBaselineChanged()
	{
		boolean flag = false ;
		if( _bar.hasChanged() )
			flag = true ;
		return flag ;
	}

	/** Test method. */
	public static void main( String[] args )
	{
		boolean dsb = true ;
		int sideBand = EdFreq.SIDE_BAND_USB ;

		if( args.length == 1 )
		{
			dsb = false ;

			if( args[ 0 ].equalsIgnoreCase( "usb" ) )
				sideBand = EdFreq.SIDE_BAND_USB ;
			else if( args[ 0 ].equalsIgnoreCase( "lsb" ) )
				sideBand = EdFreq.SIDE_BAND_LSB ;
		}

		SingleSidebandRangeBar doubleSideBandRangeBar = new SingleSidebandRangeBar() ;
		int[] range = { 10 , 160 } ;
		doubleSideBandRangeBar.resetRangeBar( 180 , 800 , range , sideBand , dsb ) ;

		JFrame frame = new JFrame( "SingleSidebandRangeBar" ) ;
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
		frame.add( doubleSideBandRangeBar ) ;
		frame.setLocation( 100 , 100 ) ;
		frame.pack() ;
		frame.setVisible( true ) ;
	}
}
