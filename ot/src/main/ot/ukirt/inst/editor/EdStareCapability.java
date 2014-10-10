// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package ot.ukirt.inst.editor ;

import gemini.sp.obsComp.SpInstObsComp ;
import gemini.sp.obsComp.SpStareCapability ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;
import jsky.app.ot.editor.OtItemEditor ;

/**
 * Support for coadds.
 */
public class EdStareCapability
{
	/**
	 * Get the SpStareCapability from an item editor.
	 */
	private SpStareCapability _getStareCap( OtItemEditor itemEditor )
	{
		SpInstObsComp spInst = ( SpInstObsComp )itemEditor.getCurrentSpItem() ;
		String name = SpStareCapability.CAPABILITY_NAME ;
		return ( SpStareCapability )spInst.getCapability( name ) ;
	}

	/**
	 * This method initializes the widgets in the presentation to reflect the
	 * current values of the items attributes.
	 */
	protected void _init( final EdCompInstBase gw )
	{
		TextBoxWidgetExt tbwe ;
		tbwe = gw.getCoaddsTextBox() ;
		tbwe.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbwe )
			{
				_getStareCap( gw ).setCoadds( tbwe.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbwe ){} // ignore
		} ) ;
	}

	/**
	 * Override _updateWidgets to show the value of the "coadds" attribute.
	 */
	protected void _updateWidgets( EdCompInstBase gw , SpStareCapability stareCap )
	{
		TextBoxWidgetExt tbwe ;
		tbwe = gw.getCoaddsTextBox() ;
		tbwe.setText( stareCap.getCoaddsAsString() ) ;
	}
}
