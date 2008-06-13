package gemini.sp ;

/*
 * Added so that we can use the compiler to check strings
 */

public interface SpTranslationConstants
{
	String darkString = "set DARK" ;

	String biasString = "set BIAS" ;

	String focusString = "set FOCUS" ;

	String domeString = "set DOMEFLAT" ;

	String objectString = "set OBJECT" ;

	String skyString = "set SKY" ;

	String skyflatString = "set SKYFLAT" ;

	String breakString = "break" ;

	String[] sets = { darkString , biasString , focusString , domeString , objectString , skyString , skyflatString } ;
}