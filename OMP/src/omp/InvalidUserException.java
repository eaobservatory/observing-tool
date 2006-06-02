package omp;

/**
 * This exception is thrown when the user specifies an invalid user name on the MSB Done dialog box.
 *  
 */

public class InvalidUserException extends Exception
{

	InvalidUserException( String message )
	{
		super( message );
	}
}
