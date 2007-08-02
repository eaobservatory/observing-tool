// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util;

/**
 * This is a very simple class that can be used to associate two objects, a task
 * that is frequently encountered.
 */
public class Pair
{

	public Object first = null;
	public Object second = null;

	/**
     * Construct a Pair from two objects.
     */
	public Pair( Object o1 , Object o2 )
	{
		first = o1;
		second = o2;
	}
}
