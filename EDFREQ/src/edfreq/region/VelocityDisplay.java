package edfreq.region;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JSlider;
import java.awt.GridLayout;
import edfreq.EmissionLines;
import edfreq.GraphScale;
import edfreq.EdFreq;

public class VelocityDisplay extends JPanel
{

	private GraphScale _localScale; // Rest frame velocity scale
	private GraphScale _targetScale; // Redshifted velocity scale
	private EmissionLines _el; // Emission lines panel
	private double _redshift; // Target velocity in redhift
	private double _lRangeLimit; // Lower range in frequency (Hz)
	private double _uRangeLimit; // Upper frequency limit (Hz)
	private double _feIF; // FE IF frequency (Hz)
	private double _feBandWidth; // FE bandwidth (Hz)
	private double _mainLineFreq; // main line frequency(Hz)
	private int _currentSideband = EdFreq.SIDE_BAND_USB; // The current sideband
	private boolean _isDSB = true;
	private static final int DEFAULT_DISPLAY_WIDTH = 800;

	public void updateDisplay( double mainLine , double feIF , double feMaxBandWidth , int primarySideband , boolean isDSB , double redshift )
	{
		_mainLineFreq = mainLine;
		_redshift = redshift;
		_uRangeLimit = _mainLineFreq + ( 0.5 * feMaxBandWidth );
		_lRangeLimit = _mainLineFreq - ( 0.5 * feMaxBandWidth );
		_feIF = feIF;
		_feBandWidth = feMaxBandWidth;
		_isDSB = isDSB;

		if( primarySideband == EdFreq.SIDE_BAND_LSB )
			_currentSideband = EdFreq.SIDE_BAND_LSB;
		else
			_currentSideband = EdFreq.SIDE_BAND_USB;

		updateDisplay();
	}

	public void updateDisplay()
	{
		removeAll();

		// Convert this range to velocity
		// If the main-line frequency isn't set, or is outside
		// the range, assume the midpoint of low and high
		double mainLine = _mainLineFreq;

		double loVel = EdFreq.LIGHTSPEED * ( ( _lRangeLimit - mainLine ) / mainLine );
		double hiVel = EdFreq.LIGHTSPEED * ( ( _uRangeLimit - mainLine ) / mainLine );

		_targetScale = new GraphScale( loVel , hiVel , 100.0 , 10.0 , _redshift , 0 , DEFAULT_DISPLAY_WIDTH , JSlider.HORIZONTAL , true );
		_localScale = new GraphScale( loVel , hiVel , 100.0 , 10.0 , 0.0 , 0 , DEFAULT_DISPLAY_WIDTH , JSlider.HORIZONTAL , true );

		_el = new EmissionLines( _lRangeLimit , _uRangeLimit , 0.0 , DEFAULT_DISPLAY_WIDTH , 20 , 1 );
		_el.setDisplayMode( EmissionLines.VELOCITY_DISPLAY );
		if( _isDSB )
			_el.showAlternateSideband( _currentSideband , _feIF );

		setLayout( new GridLayout( 3 , 1 ) );
		add( _el );
		add( _targetScale );
		add( _localScale );
	}

	public void setMainLine( double freq )
	{
		_mainLineFreq = freq;
		if( _el != null )
			_el.setMainLine( freq );
		updateDisplay();
	}

	public void setRedshift( double redshift )
	{
		_redshift = redshift;
		// Ignore updating the emission lines for now

		// Update the targetScale
		if( _targetScale != null )
			_targetScale.setRedshift( redshift );
	}

	public int getDisplayWidth()
	{
		return DEFAULT_DISPLAY_WIDTH;
	}

	public double getPixelsPerValue()
	{
		return 1.0E9 * ( ( double )DEFAULT_DISPLAY_WIDTH ) / ( _uRangeLimit - _lRangeLimit );
	}

	public double getLO1()
	{
		double lo1;
		if( _currentSideband == EdFreq.SIDE_BAND_USB )
			lo1 = ( ( _uRangeLimit + _lRangeLimit ) * 0.5 ) - _feIF;
		else
			lo1 = ( ( _uRangeLimit + _lRangeLimit ) * 0.5 ) + _feIF;
		return lo1;
	}

	public static void main( String[] args )
	{
		VelocityDisplay vd = new VelocityDisplay();
		vd.updateDisplay( 230.538E+09 , 4.0E9 , 1.8E9 , EdFreq.SIDE_BAND_USB , true , 0.1 );
		vd.setMainLine( 230.538E+09 );

		JFrame frame = new JFrame( "Velocity Display" );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.add( vd );
		frame.setLocation( 100 , 100 );
		frame.pack();
		frame.show();
	}
}
