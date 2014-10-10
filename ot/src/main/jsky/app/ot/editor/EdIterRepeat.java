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

import javax.swing.DefaultComboBoxModel ;
import javax.swing.JComboBox ;

import gemini.sp.iter.SpIterRepeat ;

/**
 * This is the editor for Repeat iterator component.
 *
 * <p>
 * <em>Note</em> there is a bug in this class in that typing a repeat
 * count directly will not trigger an update to the attribute.
 */
public final class EdIterRepeat extends OtItemEditor implements ActionListener
{
	private IterRepeatGUI _w ; // the GUI layout panel

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterRepeat()
	{
		_title = "Repeat Iterator" ;
		_presSource = _w = new IterRepeatGUI() ;
		_description = "Repeat exposures or other iterators." ;

		// Note: The original bongo code used a SpinBoxWidget, but since Swing doesn't have one, try using a JComboBox instead...
		Object[] ar = new Object[ 99 ] ;
		for( int i = 0 ; i < 99 ; i++ )
			ar[ i ] = new Integer( i + 1 ) ;
		_w.repeatComboBox.setModel( new DefaultComboBoxModel( ar ) ) ;
		_w.repeatComboBox.addActionListener( this ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		JComboBox sbw ;

		SpIterRepeat iterRepeat = ( SpIterRepeat )_spItem ;

		// Repetitions
		sbw = _w.repeatComboBox ;
		_w.repeatComboBox.removeActionListener( this ) ;
		sbw.setSelectedItem( new Integer( iterRepeat.getCount() ) ) ;
		_w.repeatComboBox.addActionListener( this ) ;
	}

	/**
	 * Called when the value in the combo box is changed.
	 */
	public void actionPerformed( ActionEvent evt )
	{
		SpIterRepeat iterRepeat = ( SpIterRepeat )_spItem ;

		JComboBox sbw = _w.repeatComboBox ;
		int i = ( Integer )sbw.getSelectedItem() ;
		iterRepeat.setCount( i ) ;
		_spItem.setTitleAttr( "Repeat " + i + "X" ) ;
	}
}
