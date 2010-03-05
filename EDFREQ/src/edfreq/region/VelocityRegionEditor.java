/*                                                              */
/* UK Astronomy Technology Centre */
/* Royal Observatory, Edinburgh */
/* Joint Astronomy Centre, Hilo */
/* Copyright (c) PPARC 2003 */
/*                                                              */
/* ============================================================== */
// $Id$
package edfreq.region ;

import javax.swing.JButton ;
import javax.swing.JPopupMenu ;
import javax.swing.JMenuItem ;
import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.JScrollPane ;
import javax.swing.border.TitledBorder ;
import javax.swing.border.EtchedBorder ;
import java.awt.Dialog ;
import java.awt.Frame ;
import java.awt.Window ;
import java.awt.BorderLayout ;
import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.GridLayout ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import java.awt.event.MouseAdapter ;
import java.awt.event.MouseEvent ;
import java.util.Vector ;
import java.util.HashMap ;

import edfreq.EdFreq ;

/**
 * Tool for selecting baseline fit regions and line regions in a spectrum.
 * 
 * Terminology: "Baseline region", "Baseline fit region" and "Fit region" all
 * mean the same in the context of this package. They are different from the
 * "Line Region".
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class VelocityRegionEditor extends JPanel implements ActionListener
{

	private Color FIT_REGION_BAR_COLOR = Color.blue ;
	private JButton _addFitRegionButton = new JButton( "Add" ) ;

	/**
     * Contains the Baseline Fit Region SingleSidebandRangeBar bars.
     * 
     * Every SingleSidebandRangeBar that is in _fitRegionBars is also in
     * {@link #_allRegionBars}.
     */
	private Vector<SingleSidebandRangeBar> _fitRegionBars = new Vector<SingleSidebandRangeBar>() ;
	private Window _parent = null ;
	private JPanel _rangeBarPanel = new JPanel() ;
	private JPanel _rangeBarLineDisplayPanel = new JPanel( new BorderLayout() ) ;
	private JScrollPane _scrollPane ;
	private JPanel _allButtonPanels = new JPanel( new GridLayout( 3 , 1 ) ) ;
	private VelocityDisplay _vDisp = new VelocityDisplay() ;
	private int _feBandWidthPixels ;
	private double _feBandWidth ;
	private double _feIF ;
	private Vector<Double> _beBandWidth = new Vector<Double>( 4 ) ;
	private Vector<Double> _beIF = new Vector<Double>( 4 ) ;
	private double _mainLine ;

	/**
     * Indicates the sideband in which the main (first, uppermost) transition
     * line is located.
     * 
     * The meaning of _sideBand differs from that of
     * {@link edfreq.region.RangeBar._associatedSideBand}.
     */
	private int _sideBand ;

	/** Double sideband versus single sideband. */
	private boolean _dsb ;

	/**
     * Used to create alternating baseline fit regions below and above the line
     * region when the user adds baseline regions repeatedly.
     * 
     * "Inside" means the baseline fit regions is between the LO1 and the center
     * of the sideband.
     */
	private boolean _fitRegionInside = false ;

	/**
     * Used to indicate that the baseline regions have been moved. This is to
     * help stop rounding errors when swapping from frequency to pixels and
     * back.
     */
	private boolean _baselinesChanged = false ;
	private HashMap<Integer,Object[]> _inputValues = new HashMap<Integer,Object[]>() ;

	public VelocityRegionEditor( Window parent )
	{
		setLayout( new BorderLayout() ) ;

		JPanel legendPanel = new JPanel( new GridLayout( 4 , 0 ) ) ;
		JLabel redLabel = new JLabel( "Main Line Frequency" ) ;
		redLabel.setForeground( Color.red ) ;
		JLabel blueLabel = new JLabel( "Lines in alternate sideband" ) ;
		blueLabel.setForeground( Color.blue ) ;
		JLabel normLabel = new JLabel( "Lines in primary sideband" ) ;
		legendPanel.setBorder( new TitledBorder( new EtchedBorder( EtchedBorder.RAISED ) , "Legend" ) ) ;
		legendPanel.add( redLabel ) ;
		legendPanel.add( blueLabel ) ;
		legendPanel.add( normLabel ) ;

		JPanel buttonPanel = new JPanel() ;
		JLabel label = new JLabel( "Baseline Fit Region: " ) ;
		label.setForeground( FIT_REGION_BAR_COLOR ) ;
		label.setPreferredSize( new Dimension( 160 , label.getPreferredSize().height ) ) ;
		buttonPanel.add( label ) ;
		buttonPanel.add( _addFitRegionButton ) ;
		_allButtonPanels.add( buttonPanel ) ;
		_rangeBarLineDisplayPanel.add( _rangeBarPanel , BorderLayout.CENTER ) ;
		_rangeBarLineDisplayPanel.add( _vDisp , BorderLayout.SOUTH ) ;

		_scrollPane = new JScrollPane( _rangeBarLineDisplayPanel ) ;
		_scrollPane.setPreferredSize( new Dimension( 820 , 100 + 20 ) ) ;

		JPanel bottomPanel = new JPanel( new BorderLayout() ) ;
		bottomPanel.add( _allButtonPanels , BorderLayout.CENTER ) ;
		bottomPanel.add( legendPanel , BorderLayout.WEST ) ;

		add( _scrollPane , BorderLayout.CENTER ) ;
		add( bottomPanel , BorderLayout.SOUTH ) ;

		_addFitRegionButton.addActionListener( this ) ;

		_parent = parent ;

		if( _parent instanceof Dialog )
			(( Dialog )_parent).setResizable( false ) ;
		else if( _parent instanceof Frame )
			(( Frame )_parent).setResizable( false ) ;
	}

	public void setSideBand( int sideBand )
	{
		_sideBand = sideBand ;
	}

	/**
     * Switches between single/double mode and lower/upper sideband.
     * 
     * @param modeStr
     *            Allowed values: "ssb" (single side band), "dsb" (double side
     *            band) {@link orac.jcmt.inst.SpInstHeterodyne.setMode(String)}
     * @param bandStr
     *            Allowed values: "usb" (upper side band), "lsb" (lower side
     *            band), "best" (whichever sideband is best)
     *            {@link orac.jcmt.inst.SpInstHeterodyne.setBand(String)}
     */
	public void setModeAndBand( String modeStr , String bandStr )
	{
		_dsb = !modeStr.equalsIgnoreCase( "ssb" ) ;

		if( bandStr.equalsIgnoreCase( "lsb" ) )
			_sideBand = EdFreq.SIDE_BAND_LSB ;
		else
			_sideBand = EdFreq.SIDE_BAND_USB ;
	}

	public void addBaselineFitRegions( double[] xMin , double[] xMax , double lo1 , boolean resetLayout )
	{
		_inputValues.clear() ;
		SingleSidebandRangeBar temp = new SingleSidebandRangeBar( _sideBand ) ;
		int[] beUsbPixels = getBeUpperSideBandPixels() ;
		temp.resetRangeBar( _feBandWidthPixels , _vDisp.getDisplayWidth() , beUsbPixels , _sideBand , _dsb ) ;

		// Find out how many ranges we are expecting
		int nRanges = temp.getNumRanges() ;
		// We can now delete temp
		temp = null ;

		// Call addBaselineFitRegion based on the input
		double[] minArray = new double[ nRanges ] ;
		double[] maxArray = new double[ nRanges ] ;
		for( int i = 0 ; i < ( xMin.length / nRanges ) ; i++ )
		{
			for( int j = 0 ; j < nRanges ; j++ )
			{
				minArray[ j ] = xMin[ i * nRanges + j ] ;
				maxArray[ j ] = xMax[ i * nRanges + j ] ;
			}
			double[] minCopy = new double[ minArray.length ] ;
			double[] maxCopy = new double[ maxArray.length ] ;
			try
			{
				System.arraycopy( minArray , 0 , minCopy , 0 , minCopy.length ) ;
				System.arraycopy( maxArray , 0 , maxCopy , 0 , maxCopy.length ) ;
			}
			catch( Exception e )
			{
				System.out.println( "Error copying arrays" ) ;
				e.printStackTrace() ;
			}
			Object[] vals = { minCopy , maxCopy } ;
			_inputValues.put( new Integer( i ) , vals ) ;
			addBaselineFitRegion( minArray , maxArray , lo1 , resetLayout ) ;
		}

		// Set the changed flag to false since we read in some existing values
		_baselinesChanged = false ;

	}

	/**
     * Adds a SingleSidebandRangeBar representing a baseline fit region.
     * 
     * @param xMin
     *            lower bound of baseline fit region in GHz.
     * @param xMax
     *            upper bound of baseline fit region in GHz.
     * @param lo1
     *            LO1 in GHz.
     * @param resetLayout
     *            The changes will only become visible if resetLayout == true.
     *            However, if several calls to
     *            {@link #addLineRegion(int,int,boolean)}, and
     *            addBaselineFitRegion(int,int,boolean) or
     *            {@link removeAllRegions(boolean)} are made in a row then
     *            resetLayout should only be set to true in the last of these
     *            calls.
     */
	public void addBaselineFitRegion( double[] xMin , double[] xMax , double lo1 , boolean resetLayout )
	{
		int barWidth ;
		int barX ;

		final SingleSidebandRangeBar fitRegionBar = new SingleSidebandRangeBar( _sideBand ) ;

		int[] beUsbPixels = getBeUpperSideBandPixels() ;

		fitRegionBar.resetRangeBar( _feBandWidthPixels , _vDisp.getDisplayWidth() , beUsbPixels , _sideBand , _dsb ) ;

		double pixelsPerValue = _vDisp.getPixelsPerValue() ;

		for( int i = 0 ; i < fitRegionBar.getNumRanges() ; i++ )
		{
			if( xMin != null && xMax != null )
			{
				barWidth = ( int )Math.round( ( xMax[ i ] - xMin[ i ] ) * pixelsPerValue ) ;
				if( xMin[ i ] > lo1 )
				{
					System.out.println( "xMin[i] > lo1" ) ;
					barX = ( ( int )Math.round( ( xMin[ i ] - ( _mainLine / 1.E9 - _feBandWidth * 1.E-9 / 2 ) ) * pixelsPerValue ) ) ;
				}
				else
				{
					System.out.println( "xMin[i] <= lo1" ) ;
					barX = ( _vDisp.getDisplayWidth() / 2 ) - ( ( int )Math.round( ( lo1 - xMin[ i ] ) * pixelsPerValue ) ) ;
				}
			}
			else
			{
				barWidth = ( fitRegionBar.getMaxX( i ) - fitRegionBar.getMinX( i ) ) / 3 ;
				if( _fitRegionInside )
					barX = fitRegionBar.getMinX( i ) ;
				else
					barX = fitRegionBar.getMaxX( i ) - barWidth ;
			}
			fitRegionBar.setBars( barX , barWidth , i ) ;
		}

		// Add a popup menu to allow removal
		final JPopupMenu menu = new JPopupMenu() ;
		JMenuItem remove = new JMenuItem( "Remove" ) ;
		remove.addActionListener( new ActionListener()
		{

			public void actionPerformed( ActionEvent e )
			{
				_fitRegionBars.remove( fitRegionBar ) ;
				resetLayout() ;
			}
		} ) ;
		menu.add( remove ) ;

		fitRegionBar.addMouseListener( new MouseAdapter()
		{

			public void mousePressed( MouseEvent e )
			{
				if( e.getButton() == MouseEvent.BUTTON3 )
					menu.show( e.getComponent() , e.getX() , e.getY() ) ;
			}
		} ) ;

		_fitRegionBars.add( fitRegionBar ) ;

		if( resetLayout )
			resetLayout() ;
	}

	/**
     * Creates default baseline fit region above or below the center of the
     * frontend sideband.
     * 
     * @see addBaselineFitRegion(double,double,double,boolean)
     */
	public void addBaselineFitRegion( boolean reset )
	{

		// Since this is only called when no baselines already exist, we can set
        // the
		// chnaged flah to true
		_baselinesChanged = true ;

		// Assume xMin and xMax to be in the upper sideband. In this context it
        // does not matter
		// whether the mode is single or double sideband and whether we are in
        // the upper or lower
		// sideband. Internally the SingleSidebandRangeBar has RangeBars for
        // both sidebands and
		// the method addLineRegion() accepts xMin and xMax values in both
        // sidebands and
		// sets both RangeBars appropriatedly.
		double[] xMinHz = new double[ _beIF.size() ] ;
		double[] xMaxHz = new double[ _beIF.size() ] ;

		for( int i = 0 ; i < _beIF.size() ; i++ )
		{
			double beIF = _beIF.get( i ) ;
			double beBandWidth = _beBandWidth.get( i ) ;
			if( _fitRegionInside )
			{
				xMinHz[ i ] = ( _vDisp.getLO1() + ( beIF - ( beBandWidth / 2. ) ) ) / 1.E9 ;
				xMaxHz[ i ] = ( _vDisp.getLO1() + ( beIF - ( beBandWidth / 6. ) ) ) / 1.E9 ;
			}
			else
			{
				xMinHz[ i ] = ( _vDisp.getLO1() + ( beIF + ( beBandWidth / 6.0 ) ) ) / 1.0E9 ;
				xMaxHz[ i ] = ( _vDisp.getLO1() + ( beIF + ( beBandWidth / 2.0 ) ) ) / 1.0E9 ;
			}

			_fitRegionInside = !_fitRegionInside ;

		}
		_fitRegionInside = ( ( double )_fitRegionBars.size() % 2.0 == 0 ) ;
		addBaselineFitRegion( null , null , _vDisp.getLO1() / 1.0E9 , reset ) ;
	}

	/**
     * Removes all baseline fit and line regions.
     * 
     * @param resetLayout
     *            The changes will only become visible if resetLayout == true.
     *            However, if several calls to
     *            {@link addLineRegion(int,int,boolean)},
     *            {@link #addBaselineFitRegion(int,int,boolean)} or
     *            removeAllRegions(boolean) are made in a row then resetLayout
     *            should only be set to true in the last of these calls.
     */
	public void removeAllRegions( boolean resetLayout )
	{
		_fitRegionBars.clear() ;

		if( resetLayout )
			resetLayout() ;
	}

	public void updateDisplay( double mainLineFreq , double feIF , double feBandWidth , double redshift )
	{
		_mainLine = mainLineFreq ;
		_feBandWidth = feBandWidth ;
		_feIF = feIF ;

		_vDisp.updateDisplay( _mainLine , feIF , feBandWidth , _sideBand , _dsb , redshift ) ;

		// Make sure _vDisp.getPixelsPerValue() is called after
        // _vDisp.updateDisplay() ;
		_feBandWidthPixels = ( int )( ( feBandWidth / 1.E9 ) * _vDisp.getPixelsPerValue() ) ;

		_parent.pack() ;
	}

	public void updateBackendValues( double beIF , double beBandWidth )
	{
		updateBackendValues( beIF , beBandWidth , 0 ) ;
	}

	public void updateBackendValues( double beIF , double beBandWidth , int region )
	{
		// Reset everything if the region is 0
		if( region == 0 )
		{
			_beIF.clear() ;
			_beBandWidth.clear() ;
		}

		try
		{
			_beIF.setElementAt( new Double( beIF ) , region ) ;
			_beBandWidth.setElementAt( new Double( beBandWidth ) , region ) ;
		}
		catch( ArrayIndexOutOfBoundsException ex )
		{
			// Just add the elements
			_beIF.add( region , new Double( beIF ) ) ;
			_beBandWidth.add( region , new Double( beBandWidth ) ) ;
		}
		System.out.println( "In VelocityRegionEditor::updateBackendValues - updated to:" ) ;
		System.out.println( "\tbeIF = " + _beIF ) ;
		System.out.println( "\tbeBW = " + _beBandWidth ) ;
	}

	/**
     * Calculates the offsets of the backend sidband range relative to the
     * frontend sidband in pixels.
     * 
     * {@link _feIF}, {@link _beIF} and {@link _beBandWidth} must have been set
     * correctly for this method to work.
     * 
     * @see #updateLineDisplay(double,double,double,double,double)
     * @see #updateBackendValues(double,double)
     * 
     * @param feIF
     *            Frontend IF in Hz.
     * @param beIF
     *            Backend IF (sampler centre frequency) in Hz.
     * @param beBandWidth
     *            Backend bandwidth (sampler bandwidth) in Hz.
     */
	public int[] getBeUpperSideBandPixels()
	{

		int[] result = new int[ _beIF.size() * 2 ] ;
		for( int i = 0 ; i < _beIF.size() ; i++ )
		{
			double beIF = _beIF.get( i ) ;
			double beBandWidth = _beBandWidth.get( i ) ;
			// Min, max frequencies in range of upper backend sideband.
			double beMinUSB = _vDisp.getLO1() + ( beIF - ( .5 * beBandWidth ) ) ;
			double beMaxUSB = _vDisp.getLO1() + ( beIF + ( .5 * beBandWidth ) ) ;

			// Min frequency in range of upper frontend sideband.
			double feMinUSB = _vDisp.getLO1() + ( _feIF - ( .5 * _feBandWidth ) ) ;

			double pixelsPerValue = _vDisp.getPixelsPerValue() ;

			result[ i * 2 ] = ( int )( ( ( beMinUSB - feMinUSB ) / 1.E9 ) * pixelsPerValue ) ;
			result[ i * 2 + 1 ] = ( int )( ( ( beMaxUSB - feMinUSB ) / 1.E9 ) * pixelsPerValue ) ;
		}

		return result ;
	}

	public void setMainLine( double mainLine )
	{
		_mainLine = mainLine ;

		resetMainLine() ;
	}

	/**
     * Resets pixel value of main line in EmissionLines display.
     */
	public void resetMainLine()
	{
		_vDisp.setMainLine( _mainLine ) ;
	}

	public double getMainLine()
	{
		return _mainLine ;
	}

	public void resetLayout()
	{
		_rangeBarPanel.removeAll() ;

		_rangeBarPanel.removeAll() ;
		_rangeBarPanel.setLayout( new GridLayout( _fitRegionBars.size() , 1 ) ) ;

		for( int i = 0 ; i < _fitRegionBars.size() ; i++ )
			_rangeBarPanel.add( _fitRegionBars.get( i ) ) ;

		_scrollPane.setPreferredSize( new Dimension( 820 , _rangeBarLineDisplayPanel.getPreferredSize().height + 20 ) ) ;

		_parent.pack() ;
	}

	public double[][] getBaselineFitRegions()
	{
		int numRegions = 0 ;
		for( int i = 0 ; i < _fitRegionBars.size() ; i++ )
			numRegions += _fitRegionBars.get( i ).getNumRanges() ;
		double[][] result = new double[ numRegions ][] ;

		int barX ;
		int barWidth ;

		double pixelsPerValue = _vDisp.getPixelsPerValue() ;

		int k = 0 ;
		for( int i = 0 ; i < _fitRegionBars.size() ; i++ )
		{
			for( int n = 0 ; n < _fitRegionBars.get( i ).getNumBars() ; n++ )
			{
				barX = _fitRegionBars.get( i ).getBarX( n ) ;
				barWidth = _fitRegionBars.get( i ).getBarWidth( n ) ;
				if( _fitRegionBars.get( i ).hasBaselineChanged() || _baselinesChanged )
				{
					result[ k++ ] = getRegion( barX , barWidth , pixelsPerValue , _vDisp.getLO1() ) ;
				}
				else
				{
					Object[] o = _inputValues.get( new Integer( i ) ) ;
					double[] d1 = ( double[] )o[ 0 ] ;
					double[] d2 = ( double[] )o[ 1 ] ;
					for( int l = 0 ; l < d1.length ; l++ )
					{
						double[] d = { d1[ l ] * 1.E9 , d2[ l ] * 1.E9 } ;
						result[ k++ ] = d ;
					}
				}
			}
		}
		_baselinesChanged = false ;

		return result ;
	}

	public double[] getRegion( int barX , int barWidth , double pixelsPerValue , double lo1 )
	{
		double[] result = new double[ 2 ] ;

		result[ 0 ] = ( ( barX / pixelsPerValue ) * 1.E9 ) + ( _feIF - ( .5 * _feBandWidth ) ) + lo1 ;
		result[ 1 ] = result[ 0 ] + ( ( barWidth / pixelsPerValue ) * 1.E9 ) ;

		// If the region should appear in the lower sideband than reflect the
        // values on LO1.
		if( _sideBand == EdFreq.SIDE_BAND_LSB )
		{
			result[ 0 ] -= 2. * ( result[ 0 ] - lo1 ) ;
			result[ 1 ] -= 2. * ( result[ 1 ] - lo1 ) ;

			// Now swap the values
			double result0 = result[ 0 ] ;
			result[ 0 ] = result[ 1 ] ;
			result[ 1 ] = result0 ;
		}

		return result ;
	}

	public void actionPerformed( ActionEvent e )
	{
		Object source = e.getSource() ;

		if( source == _addFitRegionButton )
			addBaselineFitRegion( false ) ;

		resetLayout() ;
	}

	public int getNumRegions()
	{
		int numRegions = 0 ;
		if( _fitRegionBars != null )
			numRegions = _fitRegionBars.size() ;
		return numRegions ;
	}

	public boolean changedBaseline()
	{
		boolean flag = false ;
		if( _baselinesChanged )
			flag = true ;
		if( !flag )
		{
			for( int i = 0 ; i < _fitRegionBars.size() ; i++ )
			{
				SingleSidebandRangeBar bar = _fitRegionBars.get( i ) ;
				if( bar.hasBaselineChanged() )
				{
					flag = true ;
					break ;
				}
			}
		}

		return flag ;
	}
}
