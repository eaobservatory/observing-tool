/**
 * A class for parsing numbers which traps exceptions.
 * 
 * @author Martin Folger
 */
package gemini.util ;

public class Format
{
	// Added by Martin Folger (26 February 2002)
	/**
     * Helper method.
     * 
     * @return double value of doubleStr if it can be parsed to double, 0.0
     *         otherwise.
     */
	public static double toDouble( String doubleStr )
	{
		double result = 0. ;

		try
		{
			result = Double.valueOf( doubleStr ) ;
		}
		catch( Exception ex ){}

		return result ;
	}

	// Added by Martin Folger (26 February 2002)
	/**
     * Helper method.
     * 
     * @return int value of intStr if it can be parsed to int, 0 otherwise.
     */
	public static int toInt( String intStr )
	{
		int result = 0 ;

		try
		{
			result = Integer.valueOf( intStr ) ;
		}
		catch( Exception ex ){}

		return result ;
	}
}
