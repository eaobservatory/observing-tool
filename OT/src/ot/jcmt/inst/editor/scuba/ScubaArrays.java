/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.inst.editor.scuba;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Panel for SCUBA Bolometer selection.
 *
 * This class is a panel that paints the SCUBA bolometers by calling the draw
 * method of the Bolometer objects. The Bolometers can be selected, deselcected and
 * selected as primary Bolometer.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class ScubaArrays extends JPanel implements ActionListener , MouseListener
{
	private Bolometer[] _bolometers = null;
	private BolometerArray _bolometerSHORT = new BolometerArray( Bolometer.BOLOMETER_SHORT , new int[]{ 201 , 365 , 365 , 207 , 55 , 55 } , new int[]{ 31 , 124 , 305 , 393 , 305 , 124 } , 6 );
	private BolometerArray _bolometerLONG = new BolometerArray( Bolometer.BOLOMETER_LONG , new int[]{ 713 , 920 , 920 , 717 , 520 , 520 } , new int[]{ 0 , 106 , 345 , 463 , 345 , 125 } , 6 );
	private JPanel _arrayControlPanel = new JPanel();
	private JPanel _display = null;
	private JButton _buttonShort = new JButton( "SHORT" );
	private JButton _buttonLong = new JButton( "LONG" );
	private JButton _buttonShortLong = new JButton( "SHORT, LONG" );
	private JButton _buttonLongShort = new JButton( "LONG, SHORT" );
	private JButton _buttonSingle = new JButton( "Only Single Bolometers" );

	/**
	 * @param file Name of file containing pixel information
	 */
	public ScubaArrays( String file )
	{
		try
		{
			_readPixelFile( file );
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		_buttonShort.addActionListener( _bolometerSHORT );
		_buttonShort.addActionListener( _bolometerLONG );

		_buttonLong.addActionListener( _bolometerSHORT );
		_buttonLong.addActionListener( _bolometerLONG );

		_buttonShortLong.addActionListener( _bolometerSHORT );
		_buttonShortLong.addActionListener( _bolometerLONG );

		_buttonLongShort.addActionListener( _bolometerSHORT );
		_buttonLongShort.addActionListener( _bolometerLONG );

		_buttonSingle.addActionListener( _bolometerSHORT );
		_buttonSingle.addActionListener( _bolometerLONG );

		addMouseListener( this );
	}

	private void _readPixelFile( String file ) throws IOException
	{
		Vector bolometerVector = new Vector();
		LineNumberReader lineNumberReader = new LineNumberReader( new FileReader( file ) );
		String line;
		StringTokenizer stringTokenizer;
		String[] data;
		String bolometerName;

		int type = Bolometer.BOLOMETER_NONE;

		while( true )
		{
			line = lineNumberReader.readLine();
			if( line == null )
			{
				break;
			}
			else
			{
				if( line.trim().startsWith( "SETBOL" ) )
				{
					data = new String[ 4 ];
					stringTokenizer = new StringTokenizer( line , " " );
					stringTokenizer.nextToken();

					data[ 0 ] = stringTokenizer.nextToken(); // Bolometer name
					data[ 1 ] = stringTokenizer.nextToken(); // Bolometer type
					data[ 2 ] = stringTokenizer.nextToken(); // Bolometer du3
					data[ 3 ] = stringTokenizer.nextToken(); // Bolometer du4

					type = Bolometer.toBolometerType( data[ 1 ] );

					if( type != Bolometer.BOLOMETER_NONE )
					{
						// Use P1100, P1300, P2000 (i.e. type stored in data[1]) for the bolometers outside the short wave and long wave arrays.
						if( ( type == Bolometer.BOLOMETER_SHORT ) || ( type == Bolometer.BOLOMETER_LONG ) )
							bolometerName = data[ 0 ];
						else
							bolometerName = data[ 1 ];

						bolometerVector.add( new Bolometer( bolometerName , type , Double.parseDouble( data[ 2 ] ) , Double.parseDouble( data[ 3 ] ) ) );
					}
				}
			}
		}

		bolometerVector.add( _bolometerSHORT );
		bolometerVector.add( _bolometerLONG );

		_bolometers = new Bolometer[ bolometerVector.size() ];
		bolometerVector.toArray( _bolometers );

		// Add a mouse listener to each individual bolometer,
		// but not to the arrays which have their own action listeners
		for( int i = 0 ; i < _bolometers.length - 2 ; i++ )
			addMouseListener( _bolometers[ i ] );
	}

	public void paint( Graphics g )
	{
		super.paint( g );

		(( Graphics2D )g).setStroke( new BasicStroke( 2 ) );

		if( _bolometers != null )
		{
			for( int i = 0 ; i < _bolometers.length ; i++ )
				_bolometers[ i ].draw( g );
		}
	}

	/**
	 * Determines which of the bolometers should be displayed as enabled.
	 *
	 * @param bolometerTypes  The type of bolometers to enable
	 * <pre>
	 * Boolean AND expression consisting of
	 *   {@link scuba.Bolometer.BOLOMETER_SHORT}
	 *   {@link scuba.Bolometer.BOLOMETER_LONG}
	 *   {@link scuba.Bolometer.BOLOMETER_P1100}
	 *   {@link scuba.Bolometer.BOLOMETER_P1350}
	 *   {@link scuba.Bolometer.BOLOMETER_P2000}
	 * </pre>
	 */
	public void enableBolometers( int bolometerTypes )
	{
		for( int i = 0 ; i < _bolometers.length ; i++ )
			_bolometers[ i ].setEnabled( ( _bolometers[ i ].getBolometerType() & bolometerTypes ) != 0 ) ;
	}

	private void setArraySelectionEnabled( boolean enabled )
	{
		_buttonShort.setEnabled( enabled );
		_buttonLong.setEnabled( enabled );
		_buttonShortLong.setEnabled( enabled );
		_buttonLongShort.setEnabled( enabled );
		_buttonSingle.setEnabled( enabled );
	}

	/**
	 * Returns the name of the primary bolometer.
	 */
	public String getPrimaryBolometer()
	{
		for( int i = 0 ; i < _bolometers.length ; i++ )
		{
			if( _bolometers[ i ].isPrimary() )
				return _bolometers[ i ].getBolometerName();
		}

		return null;
	}

	public Vector getBolometers()
	{
		Vector result = new Vector();

		for( int i = 0 ; i < _bolometers.length ; i++ )
		{
			if( _bolometers[ i ].isSelected() )
				result.add( _bolometers[ i ].getBolometerName() );
		}

		return result;
	}

	/**
	 * Update ScubaArrays display settings.
	 *
	 * Enable/disable, select/unselect, bolometers and primary bolometer.
	 *
	 * @param bolometerTypes     Bitmask for bolometer types (see {@link ot.jcmt.inst.editor.scuba.Bolometer})
	 * @param selectedBolometers Vector of names of selected bolometers (type String)
	 * @param primaryBolometer   Name of primaryBolometer
	 */
	public void update( int bolometerTypes , Vector selectedBolometers , String primaryBolometer )
	{
		enableBolometers( bolometerTypes );

		// If arrays are enabled then enables the array selection JRadioButtons.
		// Note that so far either both arrays are enabled or none (if P1100, P1350, P2000 are used).
		setArraySelectionEnabled( ( ( Bolometer.BOLOMETER_SHORT & bolometerTypes ) != 0 ) || ( ( Bolometer.BOLOMETER_LONG & bolometerTypes ) != 0 ) ) ;

		// If there are no bolometers then just reset ScubaArrays selections.
		if( selectedBolometers == null )
		{
			for( int i = 0 ; i < _bolometers.length ; i++ )
			{
				_bolometers[ i ].setSelected( false );
				_bolometers[ i ].setCoSelected( false );
			}
			return;
		}

		// Reset the coselection flag for each bolometer
		for( int i = 0 ; i < _bolometers.length ; i++ )
		{
			_bolometers[ i ].setCoSelected( false );
		}
		for( int i = 0 ; i < _bolometers.length ; i++ )
		{
			if( selectedBolometers.contains( _bolometers[ i ].getBolometerName() ) )
			{
				_bolometers[ i ].setSelected( true );
				// Find the corresponding bolometers in the other array and set these as well.
				Bolometer[] correspondingBolometers = getCorrespondingBolometers( _bolometers[ i ] );
				for( int j = 0 ; j < correspondingBolometers.length ; j++ )
				{
					correspondingBolometers[ j ].setSelected( false );
					correspondingBolometers[ j ].setCoSelected( true );
				}
			}
			else
			{
				_bolometers[ i ].setSelected( false );
			}

			if( _bolometers[ i ].getBolometerName().equals( primaryBolometer ) )
				_bolometers[ i ].setPrimary( true );
		}
	}

	public JPanel getDisplayPanel()
	{
		if( _display == null )
			_createDisplay();

		return _display;
	}

	private void _createDisplay()
	{
		setPreferredSize( new Dimension( 1000 , 600 ) );

		// Control Panel
		JLabel selectLabel = new JLabel( "Select: " );
		selectLabel.setForeground( Color.black );
		_arrayControlPanel.add( selectLabel );

		_buttonShort.setActionCommand( BolometerArray.ARRAY_SELECTION_SHORT );
		_buttonShort.addActionListener( this );
		_arrayControlPanel.add( _buttonShort );

		_buttonLong.setActionCommand( BolometerArray.ARRAY_SELECTION_LONG );
		_buttonLong.addActionListener( this );
		_arrayControlPanel.add( _buttonLong );

		_buttonShortLong.setActionCommand( BolometerArray.ARRAY_SELECTION_SHORT_LONG );
		_buttonShortLong.addActionListener( this );
		_arrayControlPanel.add( _buttonShortLong );

		_buttonLongShort.setActionCommand( BolometerArray.ARRAY_SELECTION_LONG_SHORT );
		_buttonLongShort.addActionListener( this );
		_arrayControlPanel.add( _buttonLongShort );

		_buttonSingle.setActionCommand( BolometerArray.ARRAY_SELECTION_NONE );
		_buttonSingle.addActionListener( this );
		_arrayControlPanel.add( _buttonSingle );

		// Add labels describing how to select bolometers.
		JPanel descriptionPanel = new JPanel( new GridLayout( 4 , 1 ) );

		JLabel label = new JLabel( "  Bolometer Disabled (Can not be selected)" );
		label.setForeground( Bolometer.COLOR_DISABLED );
		descriptionPanel.add( label );

		label = new JLabel( "  Bolometer Enabled (Can be selected: Left Mouse Button)" );
		label.setForeground( Bolometer.COLOR_ENABLED );
		descriptionPanel.add( label );

		label = new JLabel( "  Bolometer Selected (On/Off: Left Mouse Button) " );
		label.setForeground( Bolometer.COLOR_SELECTED );
		descriptionPanel.add( label );

		label = new JLabel( "  Reference Bolometer (Right Mouse Button)" );
		label.setForeground( Bolometer.COLOR_PRIMARY );
		descriptionPanel.add( label );

		label = new JLabel( "  Corresponding Bolometer (Automatically Selected)" );
		label.setForeground( Bolometer.COLOR_COSELECTED );
		descriptionPanel.add( label );

		// Add all panels to display panel.
		_display = new JPanel();
		_display.setLayout( new BorderLayout() );
		_display.add( _arrayControlPanel , BorderLayout.NORTH );
		_display.add( this , BorderLayout.CENTER );
		_display.add( descriptionPanel , BorderLayout.SOUTH );
	}

	Bolometer[] getCorrespondingBolometers( Bolometer b )
	{
		double maxSep = 2. ;

		// Get the location of the bolometer
		double du3 = b.getdU3();
		double du4 = b.getdU4();

		Vector bVector = new Vector();
		Bolometer[] bArray;

		if( b.getBolometerType() == Bolometer.BOLOMETER_SHORT || b.getBolometerType() == Bolometer.BOLOMETER_LONG )
		{
			// Loop through all the bolometers...
			for( int i = 0 ; i < _bolometers.length ; i++ )
			{
				// Ignore bolometers of the same type
				if( _bolometers[ i ].getBolometerType() == b.getBolometerType() || _bolometers[ i ] instanceof BolometerArray )
					continue;

				// Calculate the angular seperation between the reference and current loop bolometer
				double x = _bolometers[ i ].getdU3();
				double y = _bolometers[ i ].getdU4();
				double distance = Math.sqrt( Math.pow( ( x - du3 ) , 2 ) + Math.pow( ( y - du4 ) , 2 ) );
				if( distance < maxSep )
					bVector.add( _bolometers[ i ] );
			}
		}
		bArray = new Bolometer[ bVector.size() ];
		bVector.toArray( bArray );
		return bArray;
	}

	public void actionPerformed( ActionEvent e )
	{
		repaint();
	}

	public void mouseClicked( MouseEvent e )
	{
		for( int i = 0 ; i < _bolometers.length ; i++ )
		{
			_bolometers[ i ].setCoSelected( false );
			if( _bolometers[ i ].isPrimary() )
			{
				Bolometer[] b = getCorrespondingBolometers( _bolometers[ i ] );
				for( int j = 0 ; j < b.length ; j++ )
					b[ j ].setSelected( false );
			}
		}

		// Get all of the selected bolometers and find their co-selected partners
		for( int i = 0 ; i < _bolometers.length ; i++ )
		{
			if( _bolometers[ i ].isSelected() )
			{
				Bolometer[] b = getCorrespondingBolometers( _bolometers[ i ] );
				for( int j = 0 ; j < b.length ; j++ )
				{
					b[ j ].setSelected( false );
					b[ j ].setCoSelected( true );
				}
			}
		}

		repaint();
	}

	public void mouseEntered( MouseEvent e ){}

	public void mouseExited( MouseEvent e ){}

	public void mousePressed( MouseEvent e ){}

	public void mouseReleased( MouseEvent e ){}

	public static void main( String[] args )
	{
		if( ( args.length == 0 ) || ( args[ 0 ].equals( "-h" ) ) )
		{
			System.out.println( "Usage ScubaArrays file <enabledBolometers>" + "\n where <enabledBolometers> is a logical AND combination of" + "\n  NONE  =  0" + "\n  SHORT =  1" + "\n  LONG  =  2" + "\n  P1100 =  4" + "\n  P1350 =  8" + "\n  P2000 = 16" );
			return;
		}
		else
		{
			ScubaArrays pixelNames = new ScubaArrays( args[ 0 ] );

			if( args.length < 2 )
			{
				pixelNames.setArraySelectionEnabled( true );
				pixelNames.enableBolometers( Bolometer.BOLOMETER_SHORT + Bolometer.BOLOMETER_LONG );
			}
			else
			{
				pixelNames.setArraySelectionEnabled( false );
				pixelNames.enableBolometers( Bolometer.BOLOMETER_P1100 + Bolometer.BOLOMETER_P1350 + Bolometer.BOLOMETER_P2000 );
			}

			JFrame frame = new JFrame( "SCUBA bolometer selection" );
			frame.setLocation( 100 , 100 );
			frame.getContentPane().add( pixelNames.getDisplayPanel() , BorderLayout.CENTER );
		}
	}
}
