package gemini.util.sort ;

import java.lang.reflect.InvocationTargetException ;
import java.lang.reflect.Method ;
import java.util.Random ;

/**
 * Test routines for any arbitrary Sorter implementation.
 * 
 * @author Shane Walker
 */
public class SortTester
{
	private static int NUM_STRINGS = 1000 ;

	private static String[] _generateStrings( int size )
	{
		String[] strA = new String[ size ] ;

		for( int i = 0 ; i < size ; ++i )
			strA[ i ] = "s" + i ;

		return strA ;
	}

	private static void _scrambleStrings( String[] strA )
	{
		Random r = new Random() ;

		int size = strA.length ;
		for( int i = 0 ; i < size ; ++i )
		{
			double d = r.nextDouble() ;
			int swp = ( int )Math.round( d * size ) ;
			if( swp >= size )
				swp = size - 1 ;

			String tmp = strA[ i ] ;
			strA[ i ] = strA[ swp ] ;
			strA[ swp ] = tmp ;
		}
	}

	private static boolean _checkStrings( String[] strA )
	{
		int size = strA.length ;
		for( int i = 0 ; i < ( size - 1 ) ; ++i )
		{
			if( strA[ i ].compareTo( strA[ i + 1 ] ) > 0 )
				return false ;
		}

		return true ;
	}

	/**
     * Basic test, sort an array of strings (of even length) in random order.
     */
	public static boolean test0( Sorter s )
	{
		String[] strA = _generateStrings( NUM_STRINGS ) ;
		_scrambleStrings( strA ) ;
		s.sort( strA , new StringComparator() ) ;
		return _checkStrings( strA ) ;
	}

	/**
     * Basic test, sort an array of strings (of odd length) in random order.
     */
	public static boolean test1( Sorter s )
	{
		String[] strA = _generateStrings( NUM_STRINGS + 1 ) ;
		_scrambleStrings( strA ) ;
		s.sort( strA , new StringComparator() ) ;
		return _checkStrings( strA ) ;
	}

	/**
     * Test whether the sorter works for an already sorted array.
     */
	public static boolean test2( Sorter s )
	{
		String[] strA = _generateStrings( NUM_STRINGS + 1 ) ;
		s.sort( strA , new StringComparator() ) ;
		return _checkStrings( strA ) ;
	}

	/**
     * Test whether the sorter works for a backwords sorted array.
     */
	public static boolean test3( Sorter s )
	{
		String[] nameA = { "Steve" , "Shane" , "Phil" , "Matt" , "Kim" , "Jim Oshman" , "Jim" , "Doug" , "Dayle" , "Bret" } ;
		s.sort( nameA , new StringComparator() ) ;
		return _checkStrings( nameA ) ;
	}

	/**
     * Test whether the sorter works for an array of 1 element.
     */
	public static boolean test4( Sorter s )
	{
		String[] nameA = { "Shane" } ;
		s.sort( nameA , new StringComparator() ) ;
		return nameA[ 0 ].equals( "Shane" ) ;
	}

	/**
     * Test whether the sorter works for an array of 2 elements.
     */
	public static boolean test5( Sorter s )
	{
		String[] nameA = { "Phil" , "Dayle" } ;
		s.sort( nameA , new StringComparator() ) ;
		return nameA[ 0 ].equals( "Dayle" ) && nameA[ 1 ].equals( "Phil" ) ;
	}

	/**
     * Test whether the sorter works for an array of all the same elements.
     */
	public static boolean test6( Sorter s )
	{
		String[] nameA = { "Kim" , "Kim" , "Kim" , "Kim" , "Kim" , "Kim" , "Kim" } ;
		s.sort( nameA , new StringComparator() ) ;

		// Actually if this test were to fail, I'd expect the sorter to
		// throw an exception ... but what the hey, test the sorted names.
		for( int i = 0 ; i < nameA.length ; ++i )
		{
			if( !nameA[ i ].equals( "Kim" ) )
				return false ;
		}
		return true ;
	}

	/**
     * Perform all the tests on the Sorter.
     */
	public static boolean testAll( Sorter s )
	{
		boolean allRes = true ;
		int i = 0 ;
		while( true )
		{
			Method m = null ;
			try
			{
				m = SortTester.class.getMethod( "test" + i , new Class[] { Sorter.class } ) ;
			}
			catch( NoSuchMethodException ex )
			{
				break ;
			}

			System.out.print( "Test " + i + " ... " ) ;
			System.out.flush() ;

			boolean testRes = false ;
			try
			{
				Boolean b = ( Boolean )m.invoke( null , new Object[] { s } ) ;
				testRes = b.booleanValue() ;
				System.out.println( ( testRes ) ? "passed" : "failed" ) ;
			}
			catch( InvocationTargetException itex )
			{
				System.err.println( "Exception invoking test #" + i + ": " + itex ) ;
				Throwable t = itex.getTargetException() ;
				System.err.println( "-->Target Exception: " + t ) ;
				t.printStackTrace() ;
			}
			catch( Exception ex )
			{
				System.err.println( "Exception invoking test #" + i + ": " + ex ) ;
				ex.printStackTrace() ;
			}
			if( !testRes )
				allRes = false ;
			++i ;
		}

		return allRes ;
	}
}
