// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe;

/**
 * An interface supported by ClientData subclasses that support
 * storing TpeImageFeatures for drawing when their associated
 * item is selected.
 */
public interface TpeFeatureClientData
{
	/**
	 * Get the image feature.
	 */
	public TpeImageFeature getImageFeature();
}
