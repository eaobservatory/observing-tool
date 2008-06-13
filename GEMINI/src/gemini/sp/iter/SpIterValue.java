// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter ;

/**
 * The SpIterValue is a part of an SpIterStep. It describes the configuration of
 * one particular attribute in the step. Because attributes can have multiple
 * values, the SpIterValue allows an array of values to be supplied.
 */
public class SpIterValue implements java.io.Serializable
{
	public String attribute ;
	public String[] values ;

	public SpIterValue( String a , String v )
	{
		attribute = a ;
		values = new String[ 1 ] ;
		values[ 0 ] = v ;
	}

	public SpIterValue( String a , String[] v )
	{
		attribute = a ;
		values = v ;
	}
}
