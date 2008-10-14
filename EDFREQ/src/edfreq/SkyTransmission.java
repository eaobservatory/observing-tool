/* ============================================================== */
/*                                                              */
/* UK Astronomy Technology Centre */
/* Royal Observatory, Edinburgh */
/* Joint Astronomy Centre, Hilo */
/* Copyright (c) PPARC 2001 */
/*                                                              */
/* ============================================================== */
// $Id$
package edfreq;

import javax.swing.JPanel ;
import javax.swing.JSlider ;
import javax.swing.event.ChangeEvent ;
import javax.swing.event.ChangeListener ;
import java.io.BufferedReader ;
import java.io.FileReader ;
import java.io.File ;
import java.io.IOException ;
import java.util.TreeMap ;
import java.util.Iterator ;
import java.util.StringTokenizer ;
import java.util.Vector ;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class SkyTransmission extends JPanel implements ChangeListener
{

	int xSize;
	int ySize;
	String _feName = null;
	private double lowLimit;
	private double highLimit;
	private double halfrange;
	private double[][] skyTrans;
	private SkyData skyData;
	private int[][] skyPlot;
	private TreeMap<Double,Double> rxTemp = null;
	private int[][] rxPlot = null;
	private Image buffer = null;
	private Graphics ig;
	private TreeMap< Double , Vector<Double>> untunableBands;
	private int[][] untunablePlot = null;
	private double lowF = 0;
	private double highF = 0;

	public SkyTransmission( double lowLimit , double highLimit , int xSize , int ySize )
	{
		super();

		int j;

		this.xSize = xSize;
		this.ySize = ySize;
		this.lowLimit = lowLimit;
		this.highLimit = highLimit;
		halfrange = 0.5 * ( highLimit - lowLimit );

		/*
         * Get relevant section of sky transmission and scale it ready for
         * plotting
         */

		skyData = new SkyData();

		skyTrans = skyData.getTransmission( lowLimit , highLimit );
		skyPlot = new int[ skyTrans.length ][ 2 ];
		for( j = 0 ; j < skyTrans.length ; j++ )
		{
			skyPlot[ j ][ 0 ] = ( int )( ( double )xSize * ( skyTrans[ j ][ 0 ] - lowLimit ) / ( highLimit - lowLimit ) );
			skyPlot[ j ][ 1 ] = Math.min( ySize - 1 , ( int )( ( double )ySize * ( 1.0 - skyTrans[ j ][ 1 ] ) ) );
		}

		/*
         * Get the whole of the untunableBands structure
         */
		untunableBands = readUntunable();

		/* Set up display */

		setPreferredSize( new Dimension( xSize , ySize ) );
		setSize( xSize , ySize );
	}

	/**
     * Alternate constructor used when we wish to plot TRx as well as the
     * atmospheric transmission.
     */

	public SkyTransmission( String feName , double lowLimit , double highLimit , int xSize , int ySize )
	{
		this( lowLimit , highLimit , xSize , ySize );
		_feName = feName;

		rxTemp = getTRx( feName );

		// Find the max and min values of TRx so that we can scale
		// the temperatures between 1 and 0
		if( rxTemp != null )
		{
			int tMax = 0;
			int tMin = 0;
			boolean first = true;
			Iterator<Double> iter = rxTemp.values().iterator();
			while( iter.hasNext() )
			{
				if( first )
				{
					tMax = ( int )Math.rint( iter.next().doubleValue() );
					tMin = tMax;
					first = false;
				}
				else
				{
					int currentValue = ( int )Math.rint( iter.next().doubleValue() );
					if( currentValue > tMax )
						tMax = currentValue;
					if( currentValue < tMin )
						tMin = currentValue;
				}
			}

			rxPlot = new int[ rxTemp.size() + 2 ][ 2 ];
			iter = rxTemp.keySet().iterator();
			int index = 1;
			while( iter.hasNext() )
			{
				Double currentKey = iter.next();
				rxPlot[ index ][ 0 ] = ( int )( ( double )xSize * ( currentKey.doubleValue() - lowF ) / ( highF - lowF ) );
				int currValue = ( int )Math.rint( rxTemp.get( currentKey ).doubleValue() );
				int scaledValue = ( int )( ( double )ySize - ( double )ySize * ( currValue - tMin ) / ( tMax - tMin ) );
				rxPlot[ index ][ 1 ] = scaledValue;
				index++ ;
			}
			// Finally add a value of 1 at the start and end of the array
			rxPlot[ 0 ][ 0 ] = 0;
			rxPlot[ 0 ][ 1 ] = 0;
			rxPlot[ rxPlot.length - 1 ][ 0 ] = xSize;
			rxPlot[ rxPlot.length - 1 ][ 0 ] = 0;
		}
	}

	/**
     * Does the actual plotting. Overrides the paintComponent method in JPanel
     */
	public void paintComponent( Graphics g )
	{
		int j;

		if( buffer == null )
		{
			buffer = createImage( xSize , ySize );

			// added by MFO, 16 November 2001
			if( buffer == null )
				return;

			ig = buffer.getGraphics();
		}

		ig.setColor( getBackground() );
		ig.fillRect( 0 , 0 , xSize , ySize );
		ig.setColor( getForeground() );

		for( j = 0 ; j < skyPlot.length - 1 ; j++ )
			ig.drawLine( skyPlot[ j ][ 0 ] , skyPlot[ j ][ 1 ] , skyPlot[ j + 1 ][ 0 ] , skyPlot[ j + 1 ][ 1 ] );

		// Draw tRx
		if( rxPlot != null )
		{
			// Draw in Red
			ig.setColor( Color.red );
			for( j = 0 ; j < rxPlot.length - 1 ; j++ )
				ig.drawLine( rxPlot[ j ][ 0 ] , rxPlot[ j ][ 1 ] , rxPlot[ j + 1 ][ 0 ] , rxPlot[ j + 1 ][ 1 ] );
		}

		// Finally plot untunable bands...
		if( untunablePlot != null && untunablePlot.length > 0 )
		{
			for( j = 0 ; j < untunablePlot.length ; j++ )
				ig.fillRect( untunablePlot[ j ][ 0 ] , untunablePlot[ j ][ 1 ] , untunablePlot[ j ][ 2 ] , untunablePlot[ j ][ 3 ] );
		}

		g.drawImage( buffer , 0 , 0 , null );

	}

	/**
     * Implementation of ChangeListener interface.
     */
	public void stateChanged( ChangeEvent e )
	{
		double value;
		int j;

		value = EdFreq.SLIDERSCALE * ( double )( ( JSlider )e.getSource() ).getValue();
		lowLimit = value - halfrange;
		highLimit = value + halfrange;

		skyTrans = skyData.getTransmission( lowLimit , highLimit );
		skyPlot = new int[ skyTrans.length ][ 2 ];
		for( j = 0 ; j < skyTrans.length ; j++ )
		{
			skyPlot[ j ][ 0 ] = ( int )( ( double )xSize * ( skyTrans[ j ][ 0 ] - lowLimit ) / ( highLimit - lowLimit ) );
			skyPlot[ j ][ 1 ] = Math.min( ySize - 1 , ( int )( ( double )ySize * ( 1.0 - skyTrans[ j ][ 1 ] ) ) );
		}

		/*
         * Get the rxTemp plot values
         */
		if( rxTemp != null )
		{
			// First of all, get the max and min receiver temperature
			int tMax = 0;
			int tMin = 0;
			boolean first = true;
			Iterator<Double> iter = rxTemp.values().iterator();
			while( iter.hasNext() )
			{
				if( first )
				{
					tMax = ( int )Math.rint( iter.next().doubleValue() );
					tMin = tMax;
					first = false;
				}
				else
				{
					int currentValue = ( int )Math.rint( iter.next().doubleValue() );
					if( currentValue > tMax )
						tMax = currentValue;
					if( currentValue < tMin )
						tMin = currentValue;
				}
			}

			// Find out what range of data to show
			iter = rxTemp.keySet().iterator();
			Double key = null;
			while( iter.hasNext() )
			{
				key = iter.next();
				if( key.doubleValue() > lowLimit )
					break;
			}
			if( key == null )
			{
				rxPlot = null;
			}
			else
			{
				int xValue = ( int )( ( double )xSize * ( key.doubleValue() - lowLimit ) / ( highLimit - lowLimit ) );
				int yValue = ( int )Math.rint( rxTemp.get( key ).doubleValue() );
				yValue = ( int )( ( double )ySize - ( double )ySize * ( yValue - tMin ) / ( tMax - tMin ) );
				Vector<Integer> keys = new Vector<Integer>();
				Vector<Integer> values = new Vector<Integer>();
				keys.add( new Integer( xValue ) );
				values.add( new Integer( yValue ) );
				while( iter.hasNext() )
				{
					key = iter.next();
					if( key.doubleValue() > highLimit )
						break;
					xValue = ( int )( ( double )xSize * ( key.doubleValue() - lowLimit ) / ( highLimit - lowLimit ) );
					yValue = ( int )Math.rint( rxTemp.get( key ).doubleValue() );
					yValue = ( int )( ( double )ySize - ( double )ySize * ( yValue - tMin ) / ( tMax - tMin ) );
					keys.add( new Integer( xValue ) );
					values.add( new Integer( yValue ) );
				}
				// Now construct the plot array
				rxPlot = new int[ keys.size() ][ 2 ];
				for( int i = 0 ; i < rxPlot.length ; i++ )
				{
					rxPlot[ i ][ 0 ] = keys.elementAt( i ).intValue();
					rxPlot[ i ][ 1 ] = values.elementAt( i ).intValue();
				}
			}
		}

		/*
         * See if we need to plot any untunable bands.
         */

		if( untunableBands != null )
		{
			Iterator<Double> i = untunableBands.keySet().iterator();
			// Find out how many bands we need to plot...
			int nElements = 0;
			while( i.hasNext() )
			{
				Double f = i.next();
				if( f.doubleValue() < lowLimit )
					continue;
				else if( f.doubleValue() > highLimit )
					break;
				else
					nElements++ ;
			}
			untunablePlot = new int[ nElements ][ 4 ]; // (x,y,width,height)
			// Now go through again and actually get the value
			i = untunableBands.keySet().iterator();
			int index = 0;
			while( i.hasNext() )
			{
				Double f = i.next();
				if( f.doubleValue() < lowLimit )
				{
					continue;
				}
				else if( f.doubleValue() > highLimit )
				{
					break;
				}
				else
				{
					Vector<Double> v = untunableBands.get( f );
					int x = 0 , y = 0 , height = 0 , width = 0;
					switch( v.size() )
					{
						case 1 :
							x = ( int )( ( double )xSize * ( ( ( Double )v.firstElement() ).doubleValue() - lowLimit ) / ( highLimit - lowLimit ) );
							y = 0;
							width = 1;
							height = ySize;
							break;
						case 2 :
							double x1 = ( ( double )xSize * ( ( ( Double )v.firstElement() ).doubleValue() - lowLimit ) / ( highLimit - lowLimit ) );
							double x2 = ( ( double )xSize * ( ( ( Double )v.lastElement() ).doubleValue() - lowLimit ) / ( highLimit - lowLimit ) );
							x = ( int )Math.rint( x1 );
							y = 0;
							width = ( int )Math.rint( x2 - x1 );
							height = ySize;
							break;
						default :
							break;
					}
					untunablePlot[ index ][ 0 ] = x;
					untunablePlot[ index ][ 1 ] = y;
					untunablePlot[ index ][ 2 ] = width;
					untunablePlot[ index ][ 3 ] = height;
					index++ ;
				}
			}
		}
		repaint();
	}

	/**
     * Read the receiver temperature file and get the data for the specified
     * front end. The return is a TreeMap with frequency as the key and TRx as
     * the value.
     */
	public TreeMap<Double,Double> getTRx( String feName )
	{
		TreeMap<Double,Double> tRx = null;

		// Get the receiver temperature and open it
		File rxFile = new File( System.getProperty( "ot.cfgdir" ) + "receiver.info" );

		// Read in the data for the current front-end
		if( rxFile.exists() )
		{
			try
			{
				String inputLine;
				BufferedReader in = new BufferedReader( new FileReader( rxFile ) );
				// Skip over the header
				while( true )
				{
					while( ( inputLine = in.readLine() ) != null )
					{
						if( inputLine.equals( "" ) )
							break;
					}
					if( inputLine == null )
						break;
					// Now keep reading the file until we find the feName
					lowF = Double.valueOf( new String( in.readLine() ) ).doubleValue(); // Low
                                                                                        // frequency
                                                                                        // limit
					lowF = lowF * 1.0e9;
					highF = Double.valueOf( new String( in.readLine() ) ).doubleValue(); // High
                                                                                            // frequency
                                                                                            // limit
					highF = highF * 1.0e9;
					in.readLine(); // Number of sidebands
					if( in.readLine().equalsIgnoreCase( feName ) )
					{
						// We can start reading in the data
						int nLines = Integer.valueOf( in.readLine() ).intValue();
						tRx = new TreeMap<Double,Double>();
						for( int i = 0 ; i < nLines ; i++ )
						{
							String values = in.readLine();
							StringTokenizer st = new StringTokenizer( values );
							Double frequency = new Double( Double.parseDouble( st.nextToken() ) * 1.0e9 );
							Double trx = new Double( ( double )Integer.parseInt( st.nextToken() ) );
							tRx.put( frequency , trx );
						}
						break;
					}
					else
					{
						// Read up to the next blank line
						continue;
					}
				}
			}
			catch( IOException ex )
			{
				System.out.println( "Error reading receiver file: " + System.getProperty( "ot.cfgdir" ) + "receiver.info" );
			}
		}
		else
		{
			System.out.println( "Receiver info file does not exist: " + System.getProperty( "ot.cfgdir" ) + "receiver.info" );
		}
		return tRx;
	}

	private TreeMap<Double,Vector<Double>> readUntunable()
	{
		TreeMap<Double,Vector<Double>> rtn = null;
		// Get the file
		File f = new File( System.getProperty( "ot.cfgdir" ) + "untunable.dat" );
		if( !( f.exists() ) )
		{
			System.out.println( "Unable to find file " + f.getName() );
			return rtn;
		}

		// If we get here, we have the file so create a new TreeMap and
		// start reading the file
		rtn = new TreeMap<Double,Vector<Double>>();
		try
		{
			BufferedReader in = new BufferedReader( new FileReader( f ) );
			String inputLine;
			while( ( inputLine = in.readLine() ) != null )
			{
				if( inputLine.startsWith( "#" ) || inputLine.equals( "" ) )
					continue;
				StringTokenizer st = new StringTokenizer( inputLine );
				Vector<Double> values = new Vector<Double>();
				if( st.countTokens() == 1 )
				{
					values.add( new Double( Double.parseDouble( st.nextToken() ) * 1.0e9 ) );
				}
				else if( st.countTokens() == 2 )
				{
					values.add( new Double( Double.parseDouble( st.nextToken() ) * 1.0e9 ) );
					values.add( new Double( Double.parseDouble( st.nextToken() ) * 1.0e9 ) );
				}
				else
				{
					continue;
				}
				rtn.put( values.firstElement() , values );
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return null;
		}
		return rtn;

	}

	/**
     * Checks whether TRx values currently exist for this object
     */
	public boolean trxAvailable()
	{
		boolean available = false;
		if( rxTemp != null )
			available = true;
		return available;
	}

}
