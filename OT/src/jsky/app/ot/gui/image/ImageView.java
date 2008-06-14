// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui.image ;

/**
 * This is a data object that describes an image view.  ImageViews are
 * relative to a base image and describe a "view" or window on the base
 * image.
 */
public class ImageView
{
	/** Where the view starts relative to the left edge of the base image. */
	public int left ;

	/** Where the view starts relative to the top edge of the base image. */
	public int top ;

	/** Where the view starts relative to the right edge of the base image. */
	public int right ;

	/** Where the view starts relative to the bottom edge of the base image. */
	public int bottom ;

	/** The scale of the view relative to the base image. */
	public double scale ;

	/** Construct with all the instance fields. */
	public ImageView( int left , int top , int right , int bottom , double scale )
	{
		this.left = left ;
		this.top = top ;
		this.right = right ;
		this.bottom = bottom ;
		this.scale = scale ;
	}

	/** Debugging string. */
	public String toString()
	{
		return "ImageView[left=" + left + ", top=" + top + ", right=" + right + ", bottom=" + bottom + ", scale=" + scale + "]" ;
	}
}
