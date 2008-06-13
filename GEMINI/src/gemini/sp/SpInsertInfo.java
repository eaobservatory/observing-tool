// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp ;

final class SpInsertInfo implements SpInsertConstants
{

	/**
     * What type of insertion?
     * 
     * @see SpInsertConstants
     */
	public int result ;

	/** Insert relative to this item. */
	public SpItem referant ;

	/**
     * The item that will be replaced by this insertion (if any). Items are
     * replaced when a newly inserted item must be unique in its scope, but is
     * being inserted into a scope that already contains an item of its type.
     * For instance, an instrument must be unique in its scope.
     */
	public SpItem replaceItem ;

	SpInsertInfo( int result , SpItem referant )
	{
		this.result = result ;
		this.referant = referant ;
	}

	SpInsertInfo( int result , SpItem referant , SpItem replaceItem )
	{
		this.result = result ;
		this.referant = referant ;
		this.replaceItem = replaceItem ;
	}

	/**
     * For debugging.
     */
	public String toString()
	{
		String posn ;
		switch( result )
		{
			case INS_INSIDE :
				posn = "INS_INSIDE" ;
				break ;
			case INS_AFTER :
				posn = "INS_AFTER" ;
				break ;
			default :
				posn = "UNKNOWN (" + result + ")" ;
		}
		return getClass().getName() + "[" + posn + ", " + "(" + referant.name() + ", " + referant.typeStr() + ", " + referant.subtypeStr() + ")" + ", replace=" + replaceItem + "]" ;
	}
}
