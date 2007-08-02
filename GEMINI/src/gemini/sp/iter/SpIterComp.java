// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter;

import gemini.sp.SpItem;
import gemini.sp.SpType;

/**
 * This is the base class for all iterator Science Program components.
 */
public abstract class SpIterComp extends SpItem
{

	/**
     * Construct with a subtype.
     */
	public SpIterComp( SpType type )
	{
		super( type );
	}

	/**
     * Override getTitle to return the subtype instead of the type.
     */
	public String getTitle()
	{
		String title = type().getReadable();
		String titleAttr = getTitleAttr();
		if( ( titleAttr != null ) && !( titleAttr.equals( "" ) ) )
			title = title + ": " + titleAttr;
		return title;
	}

	/**
     * Get an enumeration of the elements of this iterator. The client code uses
     * this enumeration to step through all the values that this iterator can
     * produce.
     */
	public abstract SpIterEnumeration elements();

}
