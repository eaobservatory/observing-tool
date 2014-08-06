/*
 * Copyright (C) 2007-2008 Science and Technology Facilities Council.
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot.jcmt.inst.editor ;

import gemini.sp.SpItem ;
import orac.jcmt.inst.SpInstSCUBA2 ;
import jsky.app.ot.editor.OtItemEditor ;

public final class EdCompInstSCUBA2 extends OtItemEditor
{
	private SpInstSCUBA2 _inst ;

	private Scuba2GUI _w ;

	public EdCompInstSCUBA2()
	{
		_title = "JCMT SCUBA-2" ;
		_presSource = _w = new Scuba2GUI() ;
		_description = "The SCUBA-2 instrument is configured with this component." ;
	}

	/**
	 * Override setup to store away a reference to the SpInstSCUBA2 item.
	 */
	public void setup( SpItem spItem )
	{
		_inst = ( SpInstSCUBA2 )spItem ;
		super.setup( spItem ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets(){}
}
