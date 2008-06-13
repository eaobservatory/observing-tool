// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp ;

/**
 * An interface that client data classes can support if they are cloneable.
 * "Client data" is an arbitrary object that may be stored with an SpItem and
 * later retrieved by the client. If the client wishes this data to be cloned
 * when the SpItem is cloned, it's client data must implement this interface.
 */
public interface SpCloneableClientData extends Cloneable
{
	/**
     * Clone the client data. The SpItem argument is the item that should be
     * associated with the new clone.
     */
	public Object clone( SpItem spItem ) ;
}
