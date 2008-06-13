// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util ;

/**
 * This is a very simple class that can be used to associate three objects, a
 * task that is somewhat frequently encountered.
 */
public class Triple
{
	public Object first = null ;;
	public Object second = null ;
	public Object third = null ;

	/**
     * Construct a Triple from three objects.
     */
	public Triple( Object o1 , Object o2 , Object o3 )
	{
		first = o1 ;
		second = o2 ;
		third = o3 ;
	}
}
