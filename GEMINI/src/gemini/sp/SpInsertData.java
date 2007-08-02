// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

/**
 * Data that describes the insertion of a set of SpItems into or after another
 * SpItem. The client uses the <code>result</code> together with the
 * <code>referant</code> item to know where the insertion would take place.
 * For instance a result of INS_AFTER would mean that the <code>items</code>
 * would be inserted after the <code>referant</code> item in the program.
 * 
 * <p>
 * The SpInsertData is needed because new items are not always inserted
 * immediately inside or after the existing item where the insertion was
 * evaluated. For instance, when inserting an observation inside of an
 * observation folder, it must be placed <em>after</em> any existing
 * observation components.
 * 
 * <pre>
 *    Observation Folder
 *       Site Quality (Observation Component)
 *   --&gt; where a new Observation would go if inserted inside of the folder
 * </pre>
 * 
 * @see SpTreeMan
 */
public final class SpInsertData implements SpInsertConstants
{

	/**
     * What type of insertion? This is one of:
     * 
     * <pre>
     * SpInsertConstants.INS_AFTER  - insert after the referant as the next
     *                                  siblings of the referant
     * SpInsertConstants.INS_INSIDE - insert inside the referant as the first
     *                                  children of the referant
     * </pre>
     */
	public int result;

	/** The set of items that will be inserted. */
	public SpItem[] items;

	/** Insert relative to this item. */
	public SpItem referant;

	/**
     * The set of items that would be replaced by the insertion (if any). Items
     * are replaced when a newly inserted item must be unique in its scope, but
     * is being inserted into a scope that already contains an item of its type.
     * For instance, an instrument must be unique in its scope.
     */
	public SpItem[] replaceItems;

	/** Construct the SpInsertData with all the fields except replaceItems. */
	SpInsertData( int result , SpItem[] items , SpItem referant )
	{
		this.result = result;
		this.items = items;
		this.referant = referant;
	}

	/** Construct the SpInsertData with all the fields. */
	SpInsertData( int result , SpItem[] items , SpItem referant , SpItem[] replaceItems )
	{
		this.result = result;
		this.items = items;
		this.referant = referant;
		this.replaceItems = replaceItems;
	}

	/** For debugging. */
	public String toString()
	{
		String posn;
		switch( result )
		{
			case INS_INSIDE :
				posn = "INS_INSIDE";
				break;
			case INS_AFTER :
				posn = "INS_AFTER";
				break;
			default :
				posn = "UNKNOWN (" + result + ")";
		}

		return getClass().getName() + "[" + posn + ", " + "(" + referant.getTitle() + ", " + referant.typeStr() + ", " + referant.subtypeStr() + "), " + ", replaceItems=" + replaceItems + "]";
	}

}
