package orac.util ;

import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.Font ;
import java.awt.Graphics ;
import java.awt.Graphics2D ;
import java.awt.Insets ;
import java.awt.geom.RoundRectangle2D ;

import java.text.DecimalFormat ;

import javax.swing.BoundedRangeModel ;
import javax.swing.DefaultBoundedRangeModel ;
import javax.swing.JComponent ;
import javax.swing.JFrame ;

public class JThermometer extends JComponent
{
	protected Dimension _preferredSize = new Dimension( 200 , 50 ) ;
	private Insets _defaultInsets = new Insets( 10 , 20 , 10 , 10 ) ;
	protected int _defaultNumTicks = 4 ;
	protected int _defaultHeight = 10 ;
	protected int _nTicks = _defaultNumTicks ;
	protected DefaultBoundedRangeModel _brm = new DefaultBoundedRangeModel( 0 , 0 , 0 , 100 ) ;
	private Color[] _colorArray = { Color.green , Color.yellow , Color.red } ;

	public JThermometer(){} ;

	public JThermometer( BoundedRangeModel brm )
	{
		this._brm = ( DefaultBoundedRangeModel )brm ;
	}

	public JThermometer( int min , int max )
	{
		_brm.setMinimum( min ) ;
		_brm.setMaximum( max ) ;
	}

	public Dimension getMinimumSize()
	{
		return _preferredSize ;
	}

	public Dimension getPreferredSize()
	{
		return _preferredSize ;
	}

	public Insets getInsets()
	{
		return _defaultInsets ;
	}

	public Insets getInsets( Insets insets )
	{
		insets = _defaultInsets ;
		return insets ;
	}

	public void setExtent( int extent )
	{
		_brm.setExtent( extent ) ;
		repaint() ;
	}

	public void setMaximum( int max )
	{
		_brm.setMaximum( max ) ;
	}

	public int getNumTicks()
	{
		return _nTicks ;
	}

	public void setNumTicks( int ticks )
	{
		_nTicks = ticks ;
	}

	public void setRangeModel( BoundedRangeModel brm )
	{
		this._brm = ( DefaultBoundedRangeModel )brm ;
	}

	protected void paintComponent( Graphics g )
	{
		Graphics2D g2 = ( Graphics2D )g.create() ;
		paintThermometer( g2 ) ;
		paintTicks( g2 ) ;
		g2.dispose() ;
	}

	private void paintThermometer( Graphics2D g2 )
	{
		Insets insets = getInsets() ;
		int firstX = insets.left ;
		int firstY = insets.top ;
		int lastX = getWidth() - insets.right - firstX ;

		RoundRectangle2D.Double outer = new RoundRectangle2D.Double( firstX , firstY , lastX , _defaultHeight , _defaultHeight , _defaultHeight ) ;

		// Work out the inner thermometer
		int endMercury = ( int )( ( double )_brm.getExtent() / _brm.getMaximum() * ( lastX ) ) ;
		RoundRectangle2D.Double inner = new RoundRectangle2D.Double( firstX + 1 , firstY + 1 , endMercury , _defaultHeight - 2 , _defaultHeight - 2 , _defaultHeight - 2 ) ;

		g2.draw( outer ) ;

		double fraction = ( double )_brm.getExtent() / _brm.getMaximum() ;
		if( fraction < 0.7 )
			g2.setColor( _colorArray[ 0 ] ) ;
		else if( fraction < 0.9 )
			g2.setColor( _colorArray[ 1 ] ) ;
		else
			g2.setColor( _colorArray[ 2 ] ) ;

		g2.fill( inner ) ;
	}

	private void paintTicks( Graphics2D g2 )
	{
		DecimalFormat df = new DecimalFormat() ;
		df.setMaximumFractionDigits( 2 ) ;
		Font font = new Font( "dialog" , Font.PLAIN , 10 ) ;
		g2.setFont( font ) ;
		java.awt.FontMetrics fm = g2.getFontMetrics() ;

		double lastValue = ( double )_brm.getMaximum() ;
		String formattedLastValue = df.format( lastValue ) ;

		Insets insets = getInsets() ;
		int firstX = insets.left ;
		int lastX = getWidth() - insets.right - firstX - fm.stringWidth( formattedLastValue ) / 2 ;

		// Translate the graphics position to each point under the thermometer
		int trans = firstX + _defaultHeight ;
		g2.translate( firstX , trans ) ;
		g2.setColor( Color.black ) ;
		g2.drawLine( 0 , 0 , lastX , 0 ) ;
		for( int i = 0 ; i <= _nTicks ; i++ )
		{
			// Translate the X position
			double pos = i * ( ( double )lastX ) / _nTicks ;
			double labelValue = i * ( double )_brm.getMaximum() / _nTicks ;
			String label = df.format( labelValue ) ;
			g2.translate( pos , 0 ) ;
			g2.drawLine( 0 , 5 , 0 , -5 ) ;
			g2.drawString( label , -10 , 15 ) ;
			g2.translate( -pos , 0 ) ;
		}
		g2.drawString( "MB" , lastX + 2 , 5 ) ;
	}

	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "Demo" ) ;
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
		JThermometer therm = new JThermometer() ;
		therm.setExtent( 75 ) ;
		frame.add( therm ) ;
		frame.pack() ;
		frame.setVisible( true ) ;
	}
}
