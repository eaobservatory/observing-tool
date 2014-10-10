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

package jsky.app.ot.editor ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

import java.util.List ;
import java.util.Vector ;

import gemini.sp.iter.SpIterFolder ;
import gemini.sp.iter.SpIterStep ;

/**
 * This is the editor for the "iterator folder" or "Sequence" item.
 * It simply displays a table into which the iteration sequence of contained
 * iterators is written.
 */
public final class EdIteratorFolder extends OtItemEditor implements ActionListener
{
	private IterEnumTableWidget _table ;
	private IterFolderGUI _w ; // the GUI layout panel

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIteratorFolder()
	{
		_title = "Sequence" ;
		_presSource = _w = new IterFolderGUI() ;
		_description = "Press the \"Show\" button to obtain a text representation of the dynamic sequence." ;
		_w.compileButton.addActionListener( this ) ;
	}

	/**
	 * Perform any necessary (one time) initialization.
	 */
	protected void _init()
	{
		_table = _w.table ;
		_table.setColumnHeaders( new String[] { "Action" , "Details" } ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		_table.removeAllRows() ;
	}

	private void _updateFolder()
	{
		SpIterFolder iterFolder = ( SpIterFolder )_spItem ;
		List<Vector<SpIterStep>> code = iterFolder.compile() ;

		_table.removeAllRows() ;
		for( int i = 0 ; i < code.size() ; ++i )
		{
			Vector<SpIterStep> o = code.get( i ) ;
			_table.addSteps( o ) ;
		}
	}

	/**
	 * Handle action event on the show button.
	 */
	public void actionPerformed( ActionEvent evt )
	{
		_updateFolder() ;
	}
}
