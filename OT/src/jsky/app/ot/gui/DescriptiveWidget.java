// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

/**
 * An interface that can be supported by widgets to provide a description
 * of themselves.
 */
public interface DescriptiveWidget
{
	/**
	 * Set the widget's description.
	 */
	public void setDescription( String description );

	/**
	 * Get the widget's description.
	 */
	public String getDescription();
}
