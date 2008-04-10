/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot.tpe;

import jsky.navigator.NavigatorImageDisplayFrame;
import jsky.image.gui.DivaMainImageDisplay;
import jsky.image.gui.ImageDisplayMenuBar;
import jsky.image.gui.ImageDisplayControl;
import jsky.image.gui.ImageDisplayToolBar;

/**
 * Extends NavigatorImageDisplayFrame to add OT/TPE specific features.
 *
 * @version $Revision$
 * @author Allan Brighton
 */
public class TpeImageDisplayFrame extends NavigatorImageDisplayFrame
{
	/** Tool bar with Tpe specific commands */
	TelescopePosEditorToolBar tpeToolBar;

	/**
	 * Create a top level window containing an ImageDisplayControl panel.
	 *
	 * @param size   the size (width, height) to use for the pan and zoom windows.
	 */
	public TpeImageDisplayFrame( int size )
	{
		super( size );
	}

	/**
	 * Create a top level window containing an ImageDisplayControl panel
	 * with the default settings.
	 */
	public TpeImageDisplayFrame()
	{
		super();
	}

	/**
	 * Create a top level window containing an ImageDisplayControl panel.
	 *
	 * @param size   the size (width, height) to use for the pan and zoom windows.
	 * @param fileOrUrl The file name or URL of an image to display.
	 */
	public TpeImageDisplayFrame( int size , String fileOrUrl )
	{
		super( size , fileOrUrl );
	}

	/**
	 * Create a top level window containing an ImageDisplayControl panel.
	 *
	 * @param fileOrUrl The file name or URL of an image to display.
	 */
	public TpeImageDisplayFrame( String fileOrUrl )
	{
		super( fileOrUrl );
	}

	/** Make and return the menubar */
	protected ImageDisplayMenuBar makeMenuBar( DivaMainImageDisplay main , ImageDisplayToolBar toolBar )
	{
		return new TpeImageDisplayMenuBar( main , toolBar );
	}

	/**
	 * Make and return the image display control frame.
	 *
	 * @param size the size (width, height) to use for the pan and zoom windows.
	 */
	protected ImageDisplayControl makeImageDisplayControl( int size )
	{
		return new TpeImageDisplayControl( this , /*statusPanel,*/size );
	}

	/** Make and return the toolbar */
	protected ImageDisplayToolBar makeToolBar( DivaMainImageDisplay mainImageDisplay )
	{
		// add the Tpe tool bar while we are at it...
		addTpeToolBar();

		// Dragging can cause problems with two tool bars...
		ImageDisplayToolBar toolBar = super.makeToolBar( mainImageDisplay );
		toolBar.setFloatable( false );
		return toolBar;
	}

	/** Add a tool bar for OT/TPE specific operations. */
	protected void addTpeToolBar()
	{
		TpeImageWidget imageDisplay = ( TpeImageWidget )imageDisplayControl.getImageDisplay();
		tpeToolBar = new TelescopePosEditorToolBar( imageDisplay );
		add( "West" , tpeToolBar );
	}

	/** Return the Tool bar with OT/TPE specific commands */
	TelescopePosEditorToolBar getTpeToolBar()
	{
		return tpeToolBar;
	}
}
