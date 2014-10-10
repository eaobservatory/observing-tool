/*
 * Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jsky.app.ot.tpe ;

import jsky.navigator.NavigatorImageDisplayFrame ;
import jsky.image.gui.DivaMainImageDisplay ;
import jsky.image.gui.ImageDisplayMenuBar ;
import jsky.image.gui.ImageDisplayControl ;
import jsky.image.gui.ImageDisplayToolBar ;

/**
 * Extends NavigatorImageDisplayFrame to add OT/TPE specific features.
 *
 * @author Allan Brighton
 */
@SuppressWarnings( "serial" )
public class TpeImageDisplayFrame extends NavigatorImageDisplayFrame
{
	/** Tool bar with Tpe specific commands */
	TelescopePosEditorToolBar tpeToolBar ;

	/**
	 * Create a top level window containing an ImageDisplayControl panel.
	 *
	 * @param size   the size (width, height) to use for the pan and zoom windows.
	 */
	public TpeImageDisplayFrame( int size )
	{
		super( size ) ;
	}

	/**
	 * Create a top level window containing an ImageDisplayControl panel
	 * with the default settings.
	 */
	public TpeImageDisplayFrame()
	{
		super() ;
	}

	/**
	 * Create a top level window containing an ImageDisplayControl panel.
	 *
	 * @param size   the size (width, height) to use for the pan and zoom windows.
	 * @param fileOrUrl The file name or URL of an image to display.
	 */
	public TpeImageDisplayFrame( int size , String fileOrUrl )
	{
		super( size , fileOrUrl ) ;
	}

	/**
	 * Create a top level window containing an ImageDisplayControl panel.
	 *
	 * @param fileOrUrl The file name or URL of an image to display.
	 */
	public TpeImageDisplayFrame( String fileOrUrl )
	{
		super( fileOrUrl ) ;
	}

	/** Make and return the menubar */
	protected ImageDisplayMenuBar makeMenuBar( DivaMainImageDisplay main , ImageDisplayToolBar toolBar )
	{
		return new TpeImageDisplayMenuBar( main , toolBar ) ;
	}

	/**
	 * Make and return the image display control frame.
	 *
	 * @param size the size (width, height) to use for the pan and zoom windows.
	 */
	protected ImageDisplayControl makeImageDisplayControl( int size )
	{
		return new TpeImageDisplayControl( this , /*statusPanel,*/size ) ;
	}

	/** Make and return the toolbar */
	protected ImageDisplayToolBar makeToolBar( DivaMainImageDisplay mainImageDisplay )
	{
		// add the Tpe tool bar while we are at it...
		addTpeToolBar() ;

		// Dragging can cause problems with two tool bars...
		ImageDisplayToolBar toolBar = super.makeToolBar( mainImageDisplay ) ;
		toolBar.setFloatable( false ) ;
		return toolBar ;
	}

	/** Add a tool bar for OT/TPE specific operations. */
	protected void addTpeToolBar()
	{
		TpeImageWidget imageDisplay = ( TpeImageWidget )imageDisplayControl.getImageDisplay() ;
		tpeToolBar = new TelescopePosEditorToolBar( imageDisplay ) ;
		add( "West" , tpeToolBar ) ;
	}

	/** Return the Tool bar with OT/TPE specific commands */
	TelescopePosEditorToolBar getTpeToolBar()
	{
		return tpeToolBar ;
	}
}
