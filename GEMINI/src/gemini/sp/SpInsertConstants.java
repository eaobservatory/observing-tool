// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp ;

/**
 * This interface is used in conjunction with the SpTreeMan class to define how
 * items should be inserted relative to the reference item.
 * 
 * @see SpTreeMan
 */
public interface SpInsertConstants
{
	/** Insert as the first child of the referant. */
	public static final int INS_INSIDE = 0 ;

	/** Insert after the referant as a sibling. */
	public static final int INS_AFTER = 1 ;
}
