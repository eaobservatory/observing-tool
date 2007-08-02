package gemini.util.sort;

/**
 * A Sorter implementation using the InsertionSort algorithm.
 */
public class InsertionSorter implements Sorter
{

	/**
     * Sort the given array of objects using the given Comparator.
     */
	public void sort( Object[] objA , Comparator comp )
	{
		sort( objA , 0 , objA.length , comp );
	}

	/**
     * Sort the given portion of the array of objects using the given
     * Comparator.
     */
	public void sort( Object[] objA , int offset , int length , Comparator comp )
	{
		_sort( objA , offset , length , comp );
	}

	/**
     * Implementation of the InsertionSort algorithm.
     */
	private void _sort( Object[] objA , int offset , int length , Comparator comp )
	{
		int maxIndex = offset + length;

		// First find the smallest value in the array.
		Object low = objA[ offset ];
		int lowIndex = 0;
		for( int i = offset + 1 ; i < maxIndex ; ++i )
		{
			if( comp.compare( low , objA[ i ] ) > 0 )
			{
				low = objA[ i ];
				lowIndex = i;
			}
		}

		// Now place the smallest value in the first slot.
		// This will speed up the remainder of the sort by removing a
		// test in the nested loop.
		Object tmp = objA[ offset ];
		objA[ offset ] = objA[ lowIndex ];
		objA[ lowIndex ] = tmp;

		for( int i = offset + 2 ; i < maxIndex ; ++i )
		{
			Object val = objA[ i ];
			int pos = i;
			while( comp.compare( objA[ pos - 1 ] , val ) > 0 )
			{
				objA[ pos ] = objA[ pos - 1 ];
				--pos;
			}
			objA[ pos ] = val;
		}
	}

	public static void main( String[] args )
	{
		Sorter s = new InsertionSorter();
		boolean res = SortTester.testAll( s );
		if( res )
			System.exit( 0 );

		System.exit( -1 );
	}
}
