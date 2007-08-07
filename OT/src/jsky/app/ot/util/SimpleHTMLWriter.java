package jsky.app.ot.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class SimpleHTMLWriter extends FilterWriter
{
	public SimpleHTMLWriter( Writer out )
	{
		super( out );
	}

	public void writeSpace() throws IOException
	{
		out.write( ' ' );
	}

	public void write( String str ) throws IOException
	{
		out.write( str , 0 , str.length() );
	}

	public void writeLine( String line ) throws IOException
	{
		line += "\n";
		out.write( line , 0 , line.length() );
	}

	public void writeBold( String text ) throws IOException
	{
		write( "<b>" + text + "</b>" );
	}

	public void writeParagraph() throws IOException
	{
		writeLine( "<p>" );
	}

	public void writeParagraph( String paragraph ) throws IOException
	{
		write( "<p>" );
		writeLine( paragraph );
	}

	public void writeHorizontalRule() throws IOException
	{
		writeHorizontalRule( 1 );
	}

	public void writeHorizontalRule( int size ) throws IOException
	{
		writeLine( "<hr size=" + size + ">" );
	}

	public void writeBreak() throws IOException
	{
		writeLine( "<br clear=margin>" );
	}

	public void writeHeader( String text , int size ) throws IOException
	{
		writeLine( "<h" + size + ">" + text + "</h" + size + ">" );
	}

	public void writeHeaderRefTarget( String anchor , String text , int size ) throws IOException
	{
		write( "<h" + size + ">" );
		writeRefTarget( anchor , text );
		writeLine( "</h" + size + ">" );
	}

	public void writeRefSource( String anchor , String text ) throws IOException
	{
		write( "<a href=\"#" + anchor + "\">" + text + "</a>" );
	}

	public void writeRefTarget( String anchor , String text ) throws IOException
	{
		write( "<a name=\"" + anchor + "\">" + text + "</a>" );
	}

	public void write( char[] buff , int off , int len ) throws IOException
	{
		out.write( buff , off , len );
	}

	public void flush() throws IOException
	{
		out.flush();
	}

	public void close() throws IOException
	{
		out.close();
	}
}
