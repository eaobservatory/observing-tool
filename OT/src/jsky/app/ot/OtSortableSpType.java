// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot ;

import gemini.sp.SpType ;
import jsky.util.Sortable ;

//
// Helper used in sorting a set of SpTypes.
//
class OtSortableSpType implements Sortable
{
	SpType spType ;

	OtSortableSpType( SpType spType )
	{
		this.spType = spType ;
	}

	public int compareTo( Sortable other , Object rock )
	{
		if( !( other instanceof OtSortableSpType ) )
			return 0 ;

		String otherReadable = ( ( OtSortableSpType )other ).spType.getReadable() ;
		return spType.getReadable().compareTo( otherReadable ) ;
	}
}
