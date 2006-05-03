/*==============================================================*/
/*                                                              */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2006                   */
/*                                                              */
/*==============================================================*/

/* Designed to be used in conjunction with ot.util.Horizons */
 
package ot.util ;

import java.util.Vector ;
import java.util.TreeMap ;
import java.util.regex.Pattern ;
import java.util.regex.Matcher ;

import gemini.util.MJDUtils ;

public class QuickMatch
{

	Matcher matcher ;
	static Pattern pattern ;
	static final String word = "[a-zA-Z]" ;
	static final String keyValueRegex = "\\s*\\w+(\\s|\\-)?\\w*=\\s*" ;
	static final String epochRegex = "\\s*\\d+\\.??\\d*?\\s*!{1}=?\\s*\\d{4}-" + word + "*-\\d*\\.?\\d*?\\s*\\(" + word + "*\\)\\s*" ;
	static final String tpRegex = "\\s*\\d{4}-" + word + "*-\\d*\\.?\\d*" ;
	static final String nameDateRegex = "\\d{4}-" + word + "{3}-\\d{1,2}" ;
	static final String nameTimeRegex = "\\d{2}:\\d{2}:\\d{2}" ;
	static final String nameRegex = "^\\s*JPL/HORIZONS\\s+.+\\s+" + nameDateRegex + "\\s+" + nameTimeRegex + "\\s*$" ;
	private static QuickMatch quickmatch ;

	static
	{
		pattern = Pattern.compile( keyValueRegex ) ;
	}

	public static void main( String[] args )
	{
		String input = "  EPOCH=  2453800.5 ! 2006-Mar-06.00 (CT)         RMSW= n.a.  " ;
		if( args.length != 0 )
			input = args[ 0 ] ;
		QuickMatch match = getInstance() ;
		TreeMap merged = match.parseLine( input ) ;
		printMap( merged ) ;
	}

	public boolean isName( String line )
	{
		if( line == null || line.trim().equals( "" ) )
			return false ;
		return line.matches( nameRegex ) ;
	}

	public TreeMap parseName( String line )
	{
		TreeMap treeMap = new TreeMap() ;
		if( line == null || line.trim().equals( "" ) )
			return treeMap ;
		String[] split = line.split( " " ) ;
		String current ;
		String output = "" ;
		for( int index = 0 ; index < split.length ; index++ )
		{
			current = split[ index ] ;
			if( current.equals( "JPL/HORIZONS" ) || current.trim().equals( "" ) )
				continue ;
			if( current.matches( nameDateRegex ) || current.matches( nameTimeRegex ) )
				continue ;
			output += ( current + " " ) ;
		}
		if( output.equals( "" ) )
			return treeMap ;
		treeMap.put( "NAME" , output.trim() ) ;
		return treeMap ;
	}

	// Ripped from Horizons for debugging
	public static void printMap( TreeMap map )
	{
		if( map == null )
			return ;
		String key , value ;
		Object tmp ;
		while( map.size() != 0 )
		{
			tmp = map.lastKey() ;
			if( !( tmp instanceof String ) )
			{
				System.out.println( tmp + " not a String - something *really* wrong" ) ;
				System.exit( -8 ) ;
			}
			key = ( String )tmp ;
			tmp = map.remove( key ) ;
			value = tmp.toString() ;
			System.out.println( key + " == " + value ) ;
		}
		
	}

	private QuickMatch(){}

	public static synchronized QuickMatch getInstance()
	{
		if( quickmatch == null )
			quickmatch = new QuickMatch() ;
		return quickmatch ;
	}

	public TreeMap parseLine( String line )
	{
		if( line == null || line.trim().equals( "" ) )
			return null ;
		if( isName( line ) )
			return parseName( line ) ;
		Vector keys = keys( line ) ;
		Vector values = values( line ) ;
		TreeMap merged = merge( keys , values ) ;
		return merged ;
	}

	private Vector values( String input )
	{
		if( input == null )
			return new Vector() ;
		input = input.trim() ;
		if( input.equals( "" ) )
			return new Vector() ;
		String[] values = input.split( keyValueRegex ) ;
		int index ;
		Vector vector = new Vector() ;
                for( index = 0 ; index < values.length ; index++ )
		{
			String current = values[ index ] ;
			current = current.trim() ;
			if( current.equals( "" ) ) 
				continue ;
			Object object = parseValue( current ) ;
			vector.add( object ) ;
		}
		return vector ;
	}

	private Vector keys( String input )
	{
		if( input == null || input.trim().equals( "" ) )
			return new Vector() ;
		matcher = pattern.matcher( input ) ;
		Vector vector = new Vector() ;
		String group ;
		while( matcher.find() )
		{
			group = matcher.group() ;
			group = group.replace( '=' , ' ' ) ;
			group = group.trim() ;
			vector.add( group ) ;
		}
		return vector ;
	}

	private TreeMap merge( Vector keys , Vector values )
	{
		if( keys == null || values == null )
			return new TreeMap() ;

		if( keys.size() != values.size() )
			return new TreeMap() ;

		TreeMap treemap = new TreeMap() ;
		int size = keys.size() ;
		Object tmp ;
		String key ;
		Object value ;
		for( int index = 0 ; index < size ; index++ )
		{
			tmp = keys.get( index ) ;
			if( !( tmp instanceof String ) )
				return new TreeMap() ;
			key = ( String )tmp ;
			value = values.get( index ) ;
			if( value == null )
				return new TreeMap() ;
			tmp = treemap.put( key , value ) ;
			if( tmp != null )
				System.out.println( key + " replaced " ) ;
		}
		return treemap ;
	}

	private Object parseValue( String value )
	{
		if( value == null )
			return "" ;
		if( value.matches( epochRegex ) )
		{
			String[] split = value.split( "!" ) ;
			value = split[ 0 ] ;
			value = value.trim() ;
			try
			{
				Double object = new Double( value ) ;
				double converted = object.doubleValue() ;
				converted = MJDUtils.makeMJD( converted ) ;
				object = new Double( converted ) ;
				return object ;
			}
			catch( NumberFormatException nfe ){}
		}
		if( value.matches( tpRegex ) )
		{
			value = value.trim() ;
			value = value.replace( '-' , ' ' ) ;
			double converted = MJDUtils.convertMJD( value ) ;
			Double object = new Double( converted ) ;
			return object ;
		}		
		try
		{
			Double object = new Double( value ) ;
			return object ;
		}
		catch( NumberFormatException nfe ){}
		// See if it's fortranified
		// maybe should add a regex ...
		try
		{
			value = value.replace( 'D' , 'E' ) ;
			Double object = new Double( value ) ;
			return object ;
		}
		catch( NumberFormatException nfe ){}
		return "" ;
	}

}
