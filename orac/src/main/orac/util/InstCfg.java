/*
 * Copyright 1999 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package orac.util ;

import java.util.Vector ;
import java.util.StringTokenizer ;
import orac.util.LookUpTable ;

/**
 * This implements the parsing of instrument config information
 * @author Alan Bridger, UKATC
 * @version 0.9
 */
public class InstCfg
{
	private String keyword ;

	private String value ;

	// Helper shorthand method to match attributes
	public static boolean matchAttr( InstCfg info , String attr )
	{
		return info.getKeyword().equalsIgnoreCase( attr ) ;
	}

	public static boolean likeAttr( InstCfg info , String attr )
	{
		return ( info.getKeyword().toLowerCase().indexOf( attr.toLowerCase() ) == -1 ) ? false : true ;
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
		return keyword ;
	}

	/**
	 * Get the value for this block
	 */
	public String getValue()
	{
		return _clean( value ) ;
	}

	/**
	 * Return this block's value as an  array
	 */
	public String[] getValueAsArray()
	{
		String[] split = value.split( "," ) ;
		String[] result = new String[ split.length ] ;
		int i = 0 ;
		while( i < split.length )
			result[ i ] = _clean( split[ i++ ] ) ;

		return result ;
	}

	/**
	 * Return this block's values as a 2D array
	 */
	public String[][] getValueAs2DArray()
	{
		String temp ;

		// try to tokenize with "{" or "}" as delimiters. This should give us the "rows"
		StringTokenizer st = new StringTokenizer( value , "{}" ) ;
		int size = st.countTokens() ;
		String[] rows = new String[ size ] ;
		int i = 0 ;
		while( st.hasMoreTokens() )
		{
			temp = st.nextToken().trim() ;
			if( temp.length() != 0 )
				rows[ i++ ] = temp ;
		}
		int numRows = i ;

		// now split each "row". Encode as strings.
		st = new StringTokenizer( rows[ 0 ] , "," ) ;
		int numCols ;
		String[][] result = new String[ numRows ][] ;

		for( i = 0 ; i < numRows ; i++ )
		{
			st = new StringTokenizer( rows[ i ] , "," ) ;
			numCols = st.countTokens() ;
			result[ i ] = new String[ numCols ] ;
			int j = 0 ;
			while( st.hasMoreTokens() )
			{
				temp = st.nextToken().trim() ;
				try
				{
					result[ i ][ j++ ] = _clean( temp ) ;
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					System.out.println( "    ArrayIndexOutOfBoundsException occured with i = " + i + " and j = " + j ) ;
				}
			}
		}
		return result ;
	}

	/**
	 * Return this block's value as a Vector 
	 */
	public Vector<String> getValueAsVector()
	{
		String[] split = value.split( "," ) ;
		Vector<String> result = new Vector<String>() ;
		int i = 0 ;
		while( i < split.length )
			result.addElement( _clean( split[ i ] ) ) ;

		return result ;
	}

	/**
	 * Return this block's value as a LookUpTable
	 */
	public LookUpTable getValueAsLUT()
	{
		String temp ;

		// try to tokenize with "{" or "}" as delimiters. This should give us the "rows"
		StringTokenizer st = new StringTokenizer( value , "{}" ) ;
		Vector<String> rows = new Vector<String>() ;
		while( st.hasMoreTokens() )
		{
			temp = st.nextToken().trim() ;
			if( temp.length() != 0 )
				rows.addElement( temp ) ;
		}

		int numRows = rows.size() ;

		LookUpTable result = new LookUpTable() ;

		for( int i = 0 ; i < numRows ; i++ )
		{
			Vector<String> row = new Vector<String>() ;
			String rowString = rows.elementAt( i ) ;
			String[] split = rowString.split( "," ) ;
			for( String element : split )
				row.addElement( _clean( element ) ) ;

			result.addRow( row ) ;
		}
		return result ;
	}

	// "Clean" a string, i.e. remove {}" characters and trim it
	private String _clean( String string )
	{
		String retstr ;
		retstr = string.replace( '{' , ' ' ) ;
		retstr = retstr.replace( '}' , ' ' ) ;
		retstr = retstr.replace( '\"' , ' ' ) ;
		retstr = retstr.trim() ;
		return retstr ;
	}
}
