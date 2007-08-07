// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

/**
 * This is a simple concrete OtTreeNodeWidget extension that defines
 * the OtTreeNodeWidget.copy() method.  OtTreeNodeWidget itself is
 * abstract, to make the copy() operation possible.
 * <p>
 * All of this is necessitated by the fact that Bongo widgets can't
 * be cloned.
 */
public class OtSimpleTreeNodeWidget extends OtTreeNodeWidget
{

	public OtSimpleTreeNodeWidget( OtTreeWidget tree )
	{
		super( tree );
	}

	public OtSimpleTreeNodeWidget(){}

	/**
	 * Implement copy() to make this class concrete.
	 */
	public OtTreeNodeWidget copy()
	{
		OtTreeNodeWidget newTNW = new OtSimpleTreeNodeWidget( ( OtTreeWidget )tree );
		super.copyInto( newTNW );
		return newTNW;
	}
}
