package gemini.util.sort ;

/**
 * An interface implemented by various sorting algorithms.
 * 
 * @author Shane Walker
 */
public interface Sorter
{
	/**
     * Sort the given array of objects using the given Comparator.
     */
	void sort( Object[] objA , Comparator comp ) ;

	/**
     * Sort the given (subset of the) array of objects using the given
     * Comparator.
     */
	void sort( Object[] objA , int offset , int len , Comparator comp ) ;
}
