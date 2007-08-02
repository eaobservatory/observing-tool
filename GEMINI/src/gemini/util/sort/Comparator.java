package gemini.util.sort;

/**
 * An interface used by Sorter classes to compare two arbitrary objects.
 * 
 * @author Shane Walker
 */
public interface Comparator
{

	/**
     * Compare two objects.
     * 
     * @return -1 if <tt>o1</tt> is less than <tt>o2</tt>, 0 if they are
     *         equal, and 1 if <tt>o1</tt> is greater than <tt>o2</tt>.
     */
	int compare( Object o1 , Object o2 );
}
