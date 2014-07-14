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
//
// $Id$
//
package ot.ukirt.iter.editor ;

import orac.ukirt.iter.SpIterNod ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import java.util.Enumeration ;
import java.util.Vector ;

import jsky.app.ot.editor.OtItemEditor ;

/**
 * This is the editor for Nod iterator component.
 *
 * @author converted to Nod Iterator component by Martin Folger (M.Folger@roe.ac.uk)
 */
public final class EdIterNod extends OtItemEditor implements ActionListener
{
	private IterNodGUI _w ;

	/**
	 * If true, ignore action events.
	 */
	private boolean ignoreActions = false ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterNod()
	{
		_title = "Nod Iterator" ;
		_presSource = _w = new IterNodGUI() ;
		_description = "Take the specified number of nod exposures." ;

		// If the choices can change depending on other settings then the adding of
		// choice items will have to be done in _init or _updateWidgets (see other editor components)
		Enumeration<Vector<String>> nodPatterns = SpIterNod.patterns() ;
		while( nodPatterns.hasMoreElements() )
			_w.nodPattern.addItem( nodPatterns.nextElement() ) ;

		_w.nodPattern.addActionListener( this ) ;
	}

	/**
	 */
	protected void _init()
	{
		super._init() ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		ignoreActions = true ;

		SpIterNod nodIter = ( SpIterNod )_spItem ;

		// Nod Pattern
		_w.nodPattern.setValue( nodIter.getNodPatternVector() ) ;

		ignoreActions = false ;
	}

	/**
	 *
	 */
	@SuppressWarnings( "unchecked" )
    public void actionPerformed( ActionEvent evt )
	{
		if( !ignoreActions )
		{
			Object w = evt.getSource() ;
	
			SpIterNod nodIter = ( SpIterNod )_spItem ;
	
			if( w == _w.nodPattern )
				nodIter.setNodPattern( ( Vector<String> )_w.nodPattern.getSelectedItem() ) ;
		}
	}
}
