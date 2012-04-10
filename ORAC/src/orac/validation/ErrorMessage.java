package orac.validation ;

import gemini.sp.SpItem ;
import java.io.PrintStream ;
import java.util.Enumeration ;
import java.util.Hashtable ;

/**
 * Contains details about errors and warnings that occurred during the validation of a science program or observation.
 * 
 * @author M.Folger@roe.ac.uk UKATC
 */
public class ErrorMessage
{
	public final static int ERROR = 0 ;
	public final static int WARNING = 1 ;
	public final static int INFO = 2 ;
	private static int[] _errorWarningCount = { 0 , 0 , 0 } ;
	private static String[] _messageString = { "ERROR: " , "WARNING: " , "" } ;

	/**
	 * type is either ERROR or WARNING.
	 */
	private int _type = 0 ;

	/**
	 * Item where the invalid value has been found.
	 */
	private String _item = null ;

	/**
	 * Parameter whose value is invalid.
	 */
	private String _parameter = null ;

	/**
	 * Description of what is wrong with the parameter.
	 */
	private String _problem = null ;

	/**
	 * Expected value or range of values.
	 */
	private String _expected = null ;

	/**
	 * Value actually found.
	 */
	private String _found = null ;

	private static Hashtable<String,String> itemNames = new Hashtable<String,String>() ;

	static
	{
		itemNames.put( "pr" , "Science Programme" ) ;
		itemNames.put( "ob" , "Observation" ) ;
		itemNames.put( "if" , "Iteration Folder (?)" ) ;
		itemNames.put( "ic" , "Iterator" ) ;
		itemNames.put( "oc" , "Component" ) ;
		itemNames.put( "og" , "Observation Group" ) ;
		itemNames.put( "fo" , "Observation Folder" ) ;
		itemNames.put( "no" , "Note" ) ;
	}

	/**
	 * @param type      ERROR or WARNING.
	 * @param item      Description of item where the invalid value has been found.
	 * @param problem   Description of what is wrong with the parameter.
	 */
	public ErrorMessage( int type , String item , String problem )
	{
		_type = type ;
		_item = item ;
		_problem = problem ;

		_errorWarningCount[ _type ]++ ;
	}

	/**
	 * @param type      ERROR or WARNING.
	 * @param item      Item where the invalid value has been found.
	 * @param problem   Description of what is wrong with the parameter.
	 */
	public ErrorMessage( int type , SpItem item , String problem )
	{
		this( type , getDescription( item ) , problem ) ;
	}

	/**
	 * @param type      ERROR or WARNING.
	 * @param item      Description of item where the invalid value has been found.
	 * @param parameter Parameter whose value is invalid.
	 * @param expected  Expected value or range of values.
	 * @param found     Value actually found.
	 */
	public ErrorMessage( int type , String item , String parameter , String expected , String found )
	{
		_type = type ;
		_item = item ;
		_parameter = parameter ;
		_expected = expected ;
		_found = found ;

		_errorWarningCount[ _type ]++ ;
	}

	/**
	 * @param type      ERROR or WARNING.
	 * @param item      Item where the invalid value has been found.
	 * @param parameter Parameter whose value is invalid.
	 * @param expected  Expected value or range of values.
	 * @param found     Value actually found.
	 */
	public ErrorMessage( int type , SpItem item , String parameter , String expected , String found )
	{
		this( type , getDescription( item ) , parameter , expected , found ) ;
	}

	public String toString()
	{
		StringBuffer result = new StringBuffer() ;

		result.append( _messageString[ _type ] + _item + "\n" ) ;

		if( _problem != null )
			result.append( "" + _problem + "\n" ) ;

		if( _parameter != null )
			result.append( "Parameter: " + _parameter + "\n" ) ;

		if( _expected != null )
		{
			if( _type == ERROR )
				result.append( "Allowed: " + _expected + "\n" ) ;
			else
				result.append( "Expected " + _expected + "\n" ) ;
		}

		if( _found != null )
			result.append( "Found " + _found + "\n" ) ;

		result.append( "\n" ) ;

		return result.toString() ;
	}

	private static String getDescription( SpItem item )
	{
		return itemNames.get( item.typeStr() ) + " (" + item.getTitle() + ", " + item.subtypeStr() + ")" ;
	}

	public static void reset()
	{
		_errorWarningCount[ ERROR ] = 0 ;
		_errorWarningCount[ WARNING ] = 0 ;
	}

	public void setType( int type )
	{
		_type = type ;
	}

	public void setItem( String s )
	{
		_item = s ;
	}

	public void setParameter( String s )
	{
		_parameter = s ;
	}

	public void setProblem( String s )
	{
		_problem = s ;
	}

	public void setExpected( String s )
	{
		_expected = s ;
	}

	public void setFound( String s )
	{
		_found = s ;
	}

	public static void setErrorCount( int c )
	{
		_errorWarningCount[ ERROR ] = c ;
	}

	public static void setWarningCount( int c )
	{
		_errorWarningCount[ WARNING ] = c ;
	}

	public int getType()
	{
		return _type ;
	}

	public String getItem()
	{
		return _item ;
	}

	public String getParameter()
	{
		return _parameter ;
	}

	public String getProblem()
	{
		return _problem ;
	}

	public String getExpected()
	{
		return _expected ;
	}

	public String getFound()
	{
		return _found ;
	}

	public static int getErrorCount()
	{
		return _errorWarningCount[ ERROR ] ;
	}

	public static int getWarningCount()
	{
		return _errorWarningCount[ WARNING ] ;
	}

	public static String messagesToString(Enumeration<ErrorMessage> messages) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("If an error has been identified in an"
			+ " Observation you will be unable to take data with it.\n\n");
		buffer.append("If a warning is listed your observation"
			+ " or programme is probably non-standard, or is missing"
			+ " information which is not essential."
			+ " You should check it carefully to make sure it is what you need.\n\n");

		buffer.append(getErrorCount() + " errors, " + getWarningCount() + " warnings.\n\n");

		while (messages.hasMoreElements()) {
			ErrorMessage err = messages.nextElement();
			buffer.append(err.toString());
		}

		return buffer.toString();
	}

	public static void printMessages(Enumeration<ErrorMessage> messages, PrintStream out) {
		out.print(messagesToString(messages ));
	}
}
