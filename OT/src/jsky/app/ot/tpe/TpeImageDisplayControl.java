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
 *
 * $Id$
 */

package jsky.app.ot.tpe ;

import javax.swing.JFrame;
import java.net.URL ;

import jsky.navigator.NavigatorImageDisplayControl ;
import jsky.image.gui.DivaMainImageDisplay ;

/**
 * Extends the NavigatorImageDisplayControl class by adding Gemini specific features.
 *
 * @version $Revision$
 * @author Allan Brighton
 */
@SuppressWarnings( "serial" )
public class TpeImageDisplayControl extends NavigatorImageDisplayControl
{
	private JFrame parent ;
	/**
	 * Construct a TpeImageDisplayControl widget.
	 *
	 * @param parent the top level parent frame used to close the window
	 * @param size   the size (width, height) to use for the pan and zoom windows.
	 */
	public TpeImageDisplayControl(JFrame parent, int size) {
		super(size);
		this.parent = parent;
	}

	/** 
	 * Make a TpeImageDisplayControl widget with the default settings.
	 *
	 * @param parent The top level parent frame used to close the window
	 */
	public TpeImageDisplayControl(JFrame parent) {
		super();
		this.parent = parent;
	}

	/** 
	 * Make a TpeImageDisplayControl widget with the default settings and display the contents
	 * of the image file pointed to by the URL.
	 *
	 * @param parent The top level parent frame used to close the window
	 * @param url The URL for the image to load
	 */
	public TpeImageDisplayControl(JFrame parent, URL url) {
		super(url);
		this.parent = parent;
	}

	/** 
	 * Make a TpeImageDisplayControl widget with the default settings and display the contents
	 * of the image file.
	 *
	 * @param parent The top level parent frame used to close the window
	 * @param filename The image file to load
	 */
	public TpeImageDisplayControl(JFrame parent, String filename) {
		super(filename);
		this.parent = parent;
	}

	/** Make and return the image display window */
	protected DivaMainImageDisplay makeImageDisplay() {
		return new TpeImageWidget(parent);
	}
}
