// (c)2008 STFC

package gemini.util ;

public class XXMMSS
{
	protected static final String rapattern =  "\\d{1,2}([ ]{1,2}\\d{1,2}([ ]{1,2}\\d{1,2}(\\.\\d*)?)?)?" ;
	protected static final String decpattern = "^(\\+|-)?" + rapattern ;

	protected static final String rapatterncolon =  "\\d{1,2}(:\\d{1,2}(:\\d{1,2}(\\.\\d*)?)?)?" ;
	protected static final String decpatterncolon = "^(\\+|-)?" + rapatterncolon ;
	
	public static double[] stringTodoubleTriplet( String hhmmss )
	{
		double[] values = { 0. , 0. , 0. } ;

		String[] split = hhmmss.split( "[: ]" ) ;
		for( int index = 0 ; index < split.length && index < values.length ; index++ )
		{
			String current = split[ index ].trim() ;
			values[ index ] = new Double( current ) ;
		}

		return values ;
	}
}
