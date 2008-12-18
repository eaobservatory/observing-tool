// (c)2008 STFC

package gemini.util ;

public class XXMMSS
{
	protected static final String arcseconds = "[0-5]?[0-9]{1}" ;
	protected static final String degrees = "([0-1][0-7][0-9]|[0]?[0-9]?[0-9]{1})" ;
	protected static final String hours = "([0-2][0-3]|[0-1]?[0-9]{1})" ;
	protected static final String miliarcseconds = "\\.[0-9]?[0-9]?[0-9]{1}" ;
	
	protected static final String spacePattern = "([ ]{1,2}" + arcseconds + "([ ]{1,2}" + arcseconds + "(" + miliarcseconds + ")?)?)?" ;
	protected static final String rapattern =  hours + spacePattern ;
	protected static final String decpattern = "^(\\+|-)?" + degrees + spacePattern ;

	protected static final String colonPattern = "(:" + arcseconds + "(:" + arcseconds + "(" + miliarcseconds + ")?)?)?" ;
	protected static final String rapatterncolon =  hours + colonPattern ;
	protected static final String decpatterncolon = "^(\\+|-)?" + degrees + colonPattern ;
	
	public static double[] stringTodoubleTriplet( String hhmmss )
	{
		double[] values = { 0. , 0. , 0. } ;

		String[] split = hhmmss.split( "([:]|[ ]+)" ) ;
		for( int index = 0 ; index < split.length && index < values.length ; index++ )
		{
			String current = split[ index ].trim() ;
			values[ index ] = new Double( current ) ;
		}

		return values ;
	}
}
