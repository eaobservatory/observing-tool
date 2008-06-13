package gemini.util.sort ;

/**
 * A simple Comparator implementation to compare two strings.
 * 
 * @author Shane Walker
 */
public class StringComparator implements Comparator
{
	/**
     * @see Comparator#compare
     */
	public int compare( Object o1 , Object o2 )
	{
		return ( ( String )o1 ).compareTo( ( String )o2 ) ;
	}
}
