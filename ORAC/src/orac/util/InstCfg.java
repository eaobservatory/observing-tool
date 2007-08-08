/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.util;

import java.util.Vector ;
import java.util.StringTokenizer ;
import orac.util.LookUpTable;

/**
 * This implements the parsing of instrument config information
 * @author Alan Bridger, UKATC
 * @version 0.9
 */
public class InstCfg
{
	private String keyword;

	private String value;

	// Helper shorthand method to match attributes
	public static boolean matchAttr( InstCfg info , String attr )
	{
		return info.getKeyword().equalsIgnoreCase( attr );
	}

	public static boolean likeAttr( InstCfg info , String attr )
	{
		return ( info.getKeyword().toLowerCase().indexOf( attr.toLowerCase() ) == -1 ) ? false : true;
	}

	/**
	 * The constructor splits the given String block into a keyword and
	 * "value string". The value string may be a single value or arrays.
	 */
	public InstCfg( String infoBlock )
	{
		String[] split = infoBlock.split( "=" ) ;
		keyword = split[ 0 ].trim() ;
		value = split[ 1 ].trim() ;
	}

	/**
	 * Get the keyword for this config block
	 */
	public String getKeyword()
	{
		return keyword;
	}

	/**
	 * Get the value for this block
	 */
	public String getValue()
	{
		return _clean( value );
	}

	/**
	 * Return this block's value as an  array
	 */
	public String[] getValueAsArray()
	{
		String[] split = value.split( "," ) ;
		String[] result = new String[ split.length ];
		int i = 0;
		while( i < split.length )
			result[ i ] = _clean( split[ i++ ] ) ;

		return result;
	}

	/**
	 * Return this block's values as a 2D array
	 */
	public String[][] getValueAs2DArray()
	{
		String temp;

		// try to tokenize with "{" or "}" as delimiters. This should give us the "rows"
		StringTokenizer st = new StringTokenizer( value , "{}" );
		int size = st.countTokens();
		String[] rows = new String[ size ];
		int i = 0;
		while( st.hasMoreTokens() )
		{
			temp = st.nextToken().trim();
			if( temp.length() != 0 )
				rows[ i++ ] = temp;
		}
		int numRows = i;

		// now split each "row". Encode as strings.
		st = new StringTokenizer( rows[ 0 ] , "," );
		int numCols;
		String[][] result = new String[ numRows ][];

		for( i = 0 ; i < numRows ; i++ )
		{
			st = new StringTokenizer( rows[ i ] , "," );
			numCols = st.countTokens();
			result[ i ] = new String[ numCols ];
			int j = 0;
			while( st.hasMoreTokens() )
			{
				temp = st.nextToken().trim();
				try
				{
					result[ i ][ j++ ] = _clean( temp );
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					System.out.println( "    ArrayIndexOutOfBoundsException occured with i = " + i + " and j = " + j );
				}
			}
		}
		return result;
	}

	/**
	 * Return this block's value as a Vector 
	 */
	public Vector getValueAsVector()
	{
		String[] split = value.split( "," ) ;
		Vector result = new Vector();
		int i = 0;
		while( i < split.length )
			result.addElement( _clean( split[ i ] ) );

		return result;
	}

	/**
	 * Return this block's value as a LookUpTable
	 */
	public LookUpTable getValueAsLUT()
	{
		String temp;

		// try to tokenize with "{" or "}" as delimiters. This should give us the "rows"
		StringTokenizer st = new StringTokenizer( value , "{}" );
		Vector rows = new Vector();
		while( st.hasMoreTokens() )
		{
			temp = st.nextToken().trim();
			if( temp.length() != 0 )
				rows.addElement( temp );
		}

		int numRows = rows.size();

		LookUpTable result = new LookUpTable();

		for( int i = 0 ; i < numRows ; i++ )
		{
			st = new StringTokenizer( ( String )rows.elementAt( i ) , "," );
			Vector row = new Vector();
			while( st.hasMoreTokens() )
				row.addElement( _clean( st.nextToken() ) );

			result.addRow( row );
		}
		return result;
	}

	// "Clean" a string, i.e. remove {}" characters and trim it
	private String _clean( String string )
	{
		String retstr;
		retstr = string.replace( '{' , ' ' );
		retstr = retstr.replace( '}' , ' ' );
		retstr = retstr.replace( '\"' , ' ' );
		retstr = retstr.trim();
		return retstr;
	}
}
