// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot ;

/**
 * A TreeNodeWidget for items that may contain of observations.  A special
 * subclass of OtTreeNodeWidget is used in order to draw the chains between
 * observations correctly.
 */
@SuppressWarnings( "serial" )
public class OtObsContainerTreeNodeWidget extends OtTreeNodeWidget
{
	public OtObsContainerTreeNodeWidget( OtTreeWidget tree )
	{
		super( tree ) ;
	}

	public OtObsContainerTreeNodeWidget(){}

	/**
	 * Implement copy() to make this class concrete.
	 */
	public OtTreeNodeWidget copy()
	{
		OtObsContainerTreeNodeWidget newTNW = new OtObsContainerTreeNodeWidget( ( OtTreeWidget )tree ) ;
		super.copyInto( newTNW ) ;
		return newTNW ;
	}
}
