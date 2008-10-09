/* ============================================================== */
/*                                                              */
/* UK Astronomy Technology Centre */
/* Royal Observatory, Edinburgh */
/* Joint Astronomy Centre, Hilo */
/* Copyright (c) PPARC 2002 */
/*                                                              */
/* ============================================================== */
// $Id$
package gemini.util ;

import java.io.Reader ;
import java.io.FileReader ;
import java.io.StringReader ;
import java.io.LineNumberReader ;
import java.io.IOException ;
import java.util.Vector ;
import java.util.StringTokenizer ;

/**
 * Simple utility to convert special characters.
 * 
 * Converts
 * 
 * <pre>
 * '&lt;' to and from &quot;&amp;lt ;&quot;    &lt;!-- '&lt;' to and from &quot;&lt;&quot; --&gt;
 * '&gt;' to and from &quot;&amp;gt ;&quot;    &lt;!-- '&gt;' to and from &quot;&gt;&quot; --&gt;
 * '&amp;' to and from &quot;&amp;amp ;&quot;  &lt;!-- '&amp;' to and from &quot;&amp;&quot; --&gt;
 * </pre>
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class XmlUtil
{

	public static String asciiToXml( String ascii )
	{
		if( ascii == null )
			return "" ;

		StringBuffer result = new StringBuffer() ;

		for( int i = 0 ; i < ascii.length() ; i++ )
		{
			switch( ascii.charAt( i ) )
			{
				case '<' :
					result.append( "&lt;" ) ;
					break ;
				case '>' :
					result.append( "&gt;" ) ;
					break ;
				case '&' :
					result.append( "&amp;" ) ;
					break ;
				default :
					result.append( ascii.charAt( i ) ) ;
			}
		}

		return result.toString() ;
	}

	/**
     * This method is not needed when an XML parser is used.
     * 
     * The parser will perform the necessary conversion.
     */
	public static String xmlToAscii( String xml )
	{
		StringBuffer result = new StringBuffer() ;

		for( int i = 0 ; i < xml.length() ; i++ )
		{
			if( xml.charAt( i ) == '&' )
			{
				if( xml.substring( i ).startsWith( "&lt;" ) )
				{
					result.append( "<" ) ;
					i += 3 ;
				}
				else if( xml.substring( i ).startsWith( "&gt;" ) )
				{
					result.append( ">" ) ;
					i += 3 ;
				}
				else if( xml.substring( i ).startsWith( "&amp;" ) )
				{
					result.append( "&" ) ;
					i += 4 ;
				}
			}
			else
			{
				result.append( xml.charAt( i ) ) ;
			}
		}

		return result.toString() ;
	}

	/** Test and debug method. */
	public static void main2( String[] args )
	{
		if( args.length > 1 )
		{
			if( args[ 0 ].equals( "-toXml" ) )
			{
				System.out.println( asciiToXml( args[ 1 ] ) ) ;
				return ;
			}
			else if( args[ 0 ].equals( "-toAscii" ) )
			{
				System.out.println( xmlToAscii( args[ 1 ] ) ) ;
				return ;
			}
		}

		System.out.println( "Usage: orac.util.XmlUtil (-toXml | toAscii) \"string\"" ) ;
	}

	/**
     * Extracts occurances of the specified element.
     * 
     * Returns an array of XML string corresponding to the extracted elements.
     * Leading indentations are reduced by the same number of ' ' character such
     * that the outermost XML tag (the one representing the extracted element)
     * is not indented at all and all the relative indentations inside this tag
     * are maintained.
     * 
     * Note that this method does not perform any formal XML parsing at all. All
     * it does is extracting all the lines betwenn the occurrances of the start
     * and end tag of an element (including these lines themselves). Therefore
     * the method can <b>not</b> extract empty elements such as &lt;element
     * attr="value"/&gt;
     * 
     * @param element
     *            Name of the XML element to be extracted.
     * @param xml
     *            XML from which the element is extracted.
     */
	public static String[] extractElement( String element , String xml )
	{
		return extractElement( element , new StringReader( xml ) ) ;
	}

	/**
     * @see #extractElement(java.lang.String,java.lang.String)
     */
	public static String[] extractElement( String element , Reader reader )
	{
		LineNumberReader lineNumberReader = new LineNumberReader( reader ) ;

		String line = null ;
		boolean processingElement = false ;
		StringBuffer elementBuffer = new StringBuffer() ;
		Vector<String> elementVector = new Vector<String>() ;
		int offset = 0 ;

		do
		{
			try
			{
				line = lineNumberReader.readLine() ;
			}
			catch( IOException e )
			{
				e.printStackTrace() ;
			}

			if( line != null )
			{
				if( line.indexOf( "<" + element ) >= 0 )
				{
					processingElement = true ;
					offset = line.length() - line.trim().length() ;
				}

				if( processingElement == true )
				{
					elementBuffer.append( line.substring( offset ) + "\n" ) ;

					if( line.indexOf( "</" + element ) >= 0 )
					{
						processingElement = false ;
						elementVector.add( elementBuffer.toString() ) ;
						elementBuffer.setLength( 0 ) ;
					}
				}
			}
		}
		while( line != null ) ;

		String[] result = new String[ elementVector.size() ] ;

		for( int i = 0 ; i < elementVector.size() ; i++ )
			result[ i ] = elementVector.get( i ) ;

		return result ;
	}

	public static void main( String[] args )
	{
		if( args.length < 2 )
		{
			System.out.println( "Usage: XmlUtil \"element1 element2 ...\" file" ) ;
			return ;
		}

		try
		{
			StringTokenizer stringTokenizer = new StringTokenizer( args[ 0 ] , " " ) ;

			String[][] elements = new String[ stringTokenizer.countTokens() ][] ;

			for( int i = 0 ; i < elements.length ; i++ )
				elements[ i ] = extractElement( stringTokenizer.nextToken() , new FileReader( args[ 1 ] ) ) ;

			if( elements.length < 1 )
			{
				System.out.println( "No element found." ) ;
				return ;
			}

			for( int i = 0 ; i < elements[ 0 ].length ; i++ )
			{
				System.out.println( "\n--------------------------------\n" ) ;
				System.out.println( "------------- " + ( i + 1 ) + " ----------------\n" ) ;
				System.out.println( "--------------------------------\n" ) ;

				for( int j = 0 ; j < elements.length ; j++ )
					System.out.println( elements[ j ][ i ] ) ;
			}
		}
		catch( IOException e )
		{
			System.out.println( "Problems with file " + args[ 0 ] + ": " + e ) ;
		}
	}
}
