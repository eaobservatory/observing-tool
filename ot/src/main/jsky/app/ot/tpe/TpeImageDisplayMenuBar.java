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

import jsky.navigator.NavigatorImageDisplayMenuBar ;
import jsky.image.gui.DivaMainImageDisplay ;
import jsky.navigator.NavigatorImageDisplay ;
import jsky.image.gui.ImageDisplayToolBar ;
import jsky.navigator.NavigatorImageDisplayToolBar ;

/**
 * Extends the image display menubar by adding Gemini position editor features.
 *
 * @version $Revision$
 * @author Allan Brighton
 */
@SuppressWarnings( "serial" )
public class TpeImageDisplayMenuBar extends NavigatorImageDisplayMenuBar
{
	/**
	 * Create the menubar for the given main image display.
	 *
	 * @param imageDisplay the target image display
	 * @param toolBar the toolbar associated with this menubar (shares some actions)
	 */
	public TpeImageDisplayMenuBar( DivaMainImageDisplay imageDisplay , ImageDisplayToolBar toolBar )
	{
		super( ( NavigatorImageDisplay )imageDisplay , ( NavigatorImageDisplayToolBar )toolBar ) ;
	}
}
