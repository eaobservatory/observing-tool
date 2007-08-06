package orac.util;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.PrintJob;
import java.util.StringTokenizer;
import java.io.LineNumberReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.awt.Frame;

public class TextPrint
{
	protected Font _font = new Font( "Times New Roman" , Font.PLAIN , 12 );
	protected int _lineSpacing = 18;
	protected String _printJobTitle = "ot ukirt print job";
	protected int _y_offset = 20;
	protected int _x_offset = 20;
	protected PrintJob printJob = null;
	protected Graphics printGraphics = null;

	public TextPrint(){}

	public TextPrint( String printJobTitle )
	{
		this();
		_printJobTitle = printJobTitle;
	}

	public TextPrint( String printJobTitle , Font font , int lineSpacing )
	{
		_printJobTitle = printJobTitle;
		_font = font;
		_lineSpacing = lineSpacing;
	}

	public void printMultiLine( String string )
	{
		StringTokenizer st = new StringTokenizer( string , "\n" );
		while( st.hasMoreTokens() )
			printLine( st.nextToken() );
	}

	public void printLine( String string )
	{
		printGraphics.drawString( string , _x_offset , _y_offset += _lineSpacing );
	}

	public boolean init( Frame frame )
	{
		printJob = frame.getToolkit().getPrintJob( frame , _printJobTitle , null );

		if( printJob == null )
			return false;

		printGraphics = printJob.getGraphics();

		if( printGraphics == null )
			throw new RuntimeException( "Could not initialize printing routine." );

		printGraphics.setFont( _font );

		return true;
	}

	public void finish()
	{
		printGraphics.dispose();
		printJob.end();
	}

	public void setPrintJobTitle( String printJobTitle )
	{
		_printJobTitle = printJobTitle;
	}

	public String getPrintJobTitle()
	{
		return _printJobTitle;
	}

	public void setFont( Font font )
	{
		_font = font;
	}

	public Font getFont()
	{
		return _font;
	}

	public void setLineSpacing( int lineSpacing )
	{
		_lineSpacing = lineSpacing;
	}

	public int getLineSpacing()
	{
		return _lineSpacing;
	}

	public void set_y_offet( int y )
	{
		_y_offset = y;
	}

	public int get_y_offet()
	{
		return _y_offset;
	}

	public void set_x_offet( int x )
	{
		_x_offset = x;
	}

	public int get_x_offet()
	{
		return _x_offset;
	}

	public static void main( String[] args )
	{
		Frame frame = new Frame();
		TextPrint textPrint = null;

		if( args.length > 0 )
			textPrint = new TextPrint( args[ 0 ] );
		else
			textPrint = new TextPrint();

		if( textPrint.init( frame ) == false )
			System.exit( 0 );

		try
		{
			LineNumberReader reader = new LineNumberReader( new InputStreamReader( System.in ) );
			String line = reader.readLine();

			while( line != null )
			{
				textPrint.printLine( line );
				line = reader.readLine();
			}
		}
		catch( IOException e )
		{
			System.err.println( "Problem reading from stdin: " + e );
		}

		textPrint.finish();

		System.exit( 0 );
	}
}
