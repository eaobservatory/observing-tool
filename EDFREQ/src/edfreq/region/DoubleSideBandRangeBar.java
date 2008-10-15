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
import javax.swing.JLabel ;
import javax.swing.border.BevelBorder ;
import java.awt.Color ;
import java.awt.Font ;
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
public class DoubleSideBandRangeBar extends JPanel implements Observer
{

	private RangeBar _lowerRangeBar = new RangeBar( EdFreq.SIDE_BAND_LSB ) ;
	private RangeBar _upperRangeBar = new RangeBar( EdFreq.SIDE_BAND_USB ) ;
	private JLabel _baselineId = new JLabel() ;

	/**
     * Width of DoubleSideBandRangeBar in GUI pixels.
     * 
     * The total width represents the frequency range
     * <tt>feBandWidth + feIF</tt> where <tt>feBandWidth</tt> is the
     * frontend bandwidth and <tt>feIF</tt> is the frontend IF.
     */
	private int _totalWidth ;
	private int _previousZoom = 1 ;

	/** Frontend sideband width in pixels. */
	private int _feBandWidth ;

	/** Lower limit of backend sideband in pixels. */
	private int _beBandMin ;

	/** Upper limit of backend sideband in pixels. */
	private int _beBandMax ;
	Vector<Integer> _xRanges = new Vector<Integer>() ;

	public DoubleSideBandRangeBar( int currentZoom )
	{
		setLayout( null ) ;

		_baselineId.setForeground( Color.black ) ;
		_baselineId.setFont( new Font( _baselineId.getFont().getFontName() , Font.PLAIN , 10 ) ) ;

		add( _lowerRangeBar ) ;
		add( _baselineId ) ;
		add( _upperRangeBar ) ;

		// Event handling
		_lowerRangeBar.addObserver( this ) ;
		_upperRangeBar.addObserver( this ) ;

		setBorder( new BevelBorder( BevelBorder.RAISED ) ) ;

		_previousZoom = currentZoom ;
	}

	public void resetRangeBars( int feBandWidth , int totalWidth , int[] beBandRange )
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
					else if( beBandRange[ i ] > xRanges[ 0 ] && beBandRange[ i + 1 ] < xRanges[ 1 ] )
					{}
					else
					{
						_xRanges.add( new Integer( beBandRange[ i ] ) ) ;
						_xRanges.add( new Integer( beBandRange[ i + 1 ] ) ) ;
						break ; // Break out of the inner loop
					}
				}
			}
		}
		_feBandWidth = feBandWidth ;
		_totalWidth = totalWidth ;
		_beBandMin = xRanges[ 0 ] ;
		_beBandMax = xRanges[ 1 ] ;

		resetRangeBars() ;
	}

	public void resetRangeBars( int zoom )
	{
		double zoomChange = ( ( double )zoom / ( ( double )_previousZoom ) ) ;

		_totalWidth *= zoomChange ;
		_feBandWidth *= zoomChange ;
		_beBandMin *= zoomChange ;
		_beBandMax *= zoomChange ;

		for( int i = 0 ; i < _lowerRangeBar.getNumBars() ; i++ )
		{
			_lowerRangeBar.setBar( ( int )( _lowerRangeBar.getBarX( i ) * zoomChange ) , ( int )( _lowerRangeBar.getBarWidth( i ) * zoomChange ) , i ) ;
			_upperRangeBar.setBar( ( int )( _upperRangeBar.getBarX( i ) * zoomChange ) , ( int )( _upperRangeBar.getBarWidth( i ) * zoomChange ) , i ) ;
		}

		for( int i = 0 ; i < _lowerRangeBar.getNumRanges() ; i++ )
		{
			_lowerRangeBar.setRange( ( int )( _lowerRangeBar.getMinX( i ) * zoomChange ) , ( int )( _lowerRangeBar.getMaxX( i ) * zoomChange ) , i ) ;
			_upperRangeBar.setRange( ( int )( _upperRangeBar.getMinX( i ) * zoomChange ) , ( int )( _upperRangeBar.getMaxX( i ) * zoomChange ) , i ) ;
		}

		for( int i = 0 ; i < _xRanges.size() ; i++ )
		{
			int value = ( int )( _xRanges.elementAt( i ).intValue() * zoomChange ) ;
			_xRanges.setElementAt( new Integer( value ) , i ) ;
		}

		resetRangeBars() ;

		_previousZoom = zoom ;
	}

	public void resetRangeBars()
	{
		// Layout
		int height = RangeBar.DEFAULT_PREFERRED_SIZE.height ;

		setPreferredSize( new Dimension( _totalWidth , height ) ) ;

		// The RangeBar bounds refer to the frontend bandwidth.
		_lowerRangeBar.setBounds( 0 , 0 , _feBandWidth , height ) ;
		_upperRangeBar.setBounds( _totalWidth - _feBandWidth , 0 , _feBandWidth , height ) ;
		_baselineId.setBounds( getPreferredSize().width / 2 , 0 , _feBandWidth , height ) ;

		// The RangeBar range refers to the backend bandwidth.
		for( int i = 0 ; i < _xRanges.size() ; i += 2 )
		{
			_lowerRangeBar.setRange( _feBandWidth - _xRanges.get( i + 1 ).intValue() , _feBandWidth - _xRanges.get( i ).intValue() , i / 2 ) ;
			_upperRangeBar.setRange( _xRanges.get( i ).intValue() , _xRanges.get( i + 1 ).intValue() , i / 2 ) ;
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
	public void resetRangeBars( int feBandWidth , int totalWidth , int[] beBandRange , int sideBand , boolean dsb )
	{
		resetRangeBars( feBandWidth , totalWidth , beBandRange ) ;

		if( !dsb )
		{
			if( sideBand == EdFreq.SIDE_BAND_USB )
				_lowerRangeBar.setEnabled( false ) ;
			else
				_upperRangeBar.setEnabled( false ) ;
		}
	}

	public void setForeground( Color fg )
	{
		super.setForeground( fg ) ;

		if( ( _lowerRangeBar != null ) && ( _upperRangeBar != null ) )
		{
			_lowerRangeBar.setForeground( fg ) ;
			_upperRangeBar.setForeground( fg ) ;
		}
	}

	public void setRangeColor( Color bg )
	{
		if( ( _lowerRangeBar != null ) && ( _upperRangeBar != null ) )
		{
			_lowerRangeBar.setRangeColor( bg ) ;
			_upperRangeBar.setRangeColor( bg ) ;
		}
	}

	public void setId( int i )
	{
		_baselineId.setText( "Baseline Region " + i ) ;
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
		_upperRangeBar.setBar( barX , barWidth , bar ) ;
		_lowerRangeBar.setBar( _lowerRangeBar.getWidth() - ( barX + barWidth ) , barWidth , bar ) ;
	}

	public int getUpperBarX()
	{
		return _upperRangeBar.getBarX() ;
	}

	public int getUpperBarX( int bar )
	{
		return _upperRangeBar.getBarX( bar ) ;
	}

	public int getBarWidth()
	{
		return _upperRangeBar.getBarWidth() ;
	}

	public int getBarWidth( int bar )
	{
		return _upperRangeBar.getBarWidth( bar ) ;
	}

	public int getNumBars()
	{
		return _upperRangeBar.getNumBars() ;
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

		Object source = ( ( MouseEvent )arg ).getSource() ;

		if( source == _lowerRangeBar )
		{
			for( int i = 0 ; i < _lowerRangeBar.getNumBars() ; i++ )
				_upperRangeBar.setBar( _upperRangeBar.getWidth() - ( _lowerRangeBar.getBarX( i ) + _lowerRangeBar.getBarWidth( i ) ) , _lowerRangeBar.getBarWidth( i ) , i ) ;
		}

		if( source == _upperRangeBar )
		{
			for( int i = 0 ; i < _upperRangeBar.getNumBars() ; i++ )
				_lowerRangeBar.setBar( _lowerRangeBar.getWidth() - ( _upperRangeBar.getBarX( i ) + _upperRangeBar.getBarWidth( i ) ) , _upperRangeBar.getBarWidth( i ) , i ) ;
		}
	}

	public void addObserver( Observer o )
	{
		_lowerRangeBar.addObserver( o ) ;
		_upperRangeBar.addObserver( o ) ;
	}

	public int getNumRanges()
	{
		return _lowerRangeBar.getNumRanges() ;
	}

	public int getMaxX( int i )
	{
		return _upperRangeBar.getMaxX( i ) ;
	}

	public int getMinX( int i )
	{
		return _upperRangeBar.getMinX( i ) ;
	}

	public boolean hasBaselineChanged()
	{
		boolean flag = false ;
		if( _upperRangeBar.hasChanged() || _lowerRangeBar.hasChanged() )
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

			if( args[ 0 ].equalsIgnoreCase( "lsb" ) )
				sideBand = EdFreq.SIDE_BAND_LSB ;
		}

		DoubleSideBandRangeBar doubleSideBandRangeBar = new DoubleSideBandRangeBar( 1 ) ;
		int[] range = { 10 , 160 } ;
		doubleSideBandRangeBar.resetRangeBars( 180 , EdFreq.DISPLAY_WIDTH , range , sideBand , dsb ) ;

		JFrame frame = new JFrame( "DoubleSideBandRangeBar" ) ;
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
		frame.add( doubleSideBandRangeBar ) ;
		frame.setLocation( 100 , 100 ) ;
		frame.pack() ;
		frame.setVisible( true ) ;
	}
}
