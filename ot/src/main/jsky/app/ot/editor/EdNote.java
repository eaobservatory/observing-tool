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
package jsky.app.ot.editor ;

import java.awt.event.KeyEvent ;
import java.awt.event.KeyAdapter ;
import java.awt.Component ;

import java.net.URL ;
import java.util.Vector ;
import javax.swing.ImageIcon ;
import javax.swing.JTextField ;

import jsky.app.ot.gui.KeyPressWatcher ;
import jsky.app.ot.gui.RichTextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetWatcher ;
import jsky.app.ot.OtCfg ;

import gemini.sp.SpNote ;
import gemini.sp.SpItem ;
import gemini.sp.SpRootItem ;
import gemini.sp.SpMSB ;
import gemini.sp.SpObs ;
import gemini.util.ObservingToolUtilities;

/**
 * This is the editor for Note item.
 */
public final class EdNote extends OtItemEditor implements KeyPressWatcher , TextBoxWidgetWatcher , CheckBoxWidgetWatcher
{
	private NoteGUI _w ; // the GUI layout panel

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdNote()
	{
		_title = "Note" ;
		_presSource = _w = new NoteGUI() ;
		_description = "Enter notes for the operator/astronomer here." ;
		_resizable = true ;

		URL url = ObservingToolUtilities.resourceURL( "jsky/app/ot/images/note.gif" ) ;
		_w.imageLabel.setIcon( new ImageIcon( url ) ) ;

		// The title
		TextBoxWidgetExt tbw = _w.title ;
		tbw.addWatcher( this ) ;

		// The note
		RichTextBoxWidgetExt rtbw ;
		rtbw = _w.note ;
		rtbw.addWatcher( this ) ;

		// The observe instruction check box.
		if( _spItem != null )
		{
			// Disable if within sequence
			SpItem parent = _spItem.parent() ;
			if( parent != null )
			{
				if( !( parent instanceof SpRootItem ) && !( parent instanceof SpMSB ) && !( parent instanceof SpObs ) )
				{
					_w.observeInstruction.setVisible( false ) ;
					if( _w.observeInstruction.isSelected() )
						_w.observeInstruction.doClick() ;
				}
				else
				{
					_w.observeInstruction.setVisible( true ) ;
				}
			}
		}
		_w.observeInstruction.addWatcher( this ) ;

		String[] observerTags = OtCfg.getNoteTags() ;
		if( observerTags != null )
		{
			Vector<String> tagsV = new Vector<String>() ;
			for( int i = 0 ; i < observerTags.length ; i++ )
				tagsV.add( observerTags[ i ] ) ;

			Component[] taggedComponents = _w.observerInputPanel.getComponents() ;
			for( int i = 0 ; i < taggedComponents.length ; i++ )
			{
				if( taggedComponents[ i ] instanceof JTextField && tagsV.indexOf( taggedComponents[ i ].getName() ) != -1 )
				{
					taggedComponents[ i ].addKeyListener( new KeyAdapter()
					{
						public void keyReleased( KeyEvent evt )
						{
							(( SpNote )_spItem).setAVPair( (( JTextField )evt.getSource()).getName() , (( JTextField )evt.getSource()).getText() ) ;
						}
					} ) ;
				}
			}
		}
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		// The title
		TextBoxWidgetExt tbw = _w.title ;
		String title = _spItem.getTitleAttr() ;
		if( title != null )
			tbw.setText( title ) ;
		else
			tbw.setText( "" ) ;

		// The Note
		RichTextBoxWidgetExt rtbw ;
		rtbw = _w.note ;

		SpNote note = ( SpNote )_spItem ;
		
		String noteText = note.getNote() ;

		if( noteText == null )
			rtbw.setText( "" ) ;
		else
			rtbw.setText( noteText ) ;
		
		rtbw.setCaretPosition( 0 ) ;

		Component[] taggedComponents = _w.observerInputPanel.getComponents() ;

		// The observe instruction check box.
		if( _spItem != null )
		{
			// Disable if within sequence
			SpItem parent = _spItem.parent() ;
			if( parent != null )
			{
				if( parent.getClass().getName().indexOf( "SpIter" ) != -1 )
				{
					_w.observeInstruction.setVisible( false ) ;
					if( _w.observeInstruction.isSelected() )
						_w.observeInstruction.doClick() ;
				}
				else
				{
					_w.observeInstruction.setVisible( true ) ;
				}
			}
		}
		_w.observeInstruction.setValue( note.isObserveInstruction() ) ;
		if( _w.observeInstruction.isSelected() )
		{
			_w.observerInputPanel.setVisible( true ) ;
			for( int i = 0 ; i < taggedComponents.length ; i++ )
			{
				if( taggedComponents[ i ] instanceof JTextField )
					( ( JTextField )taggedComponents[ i ] ).setText( note.getValueFor( taggedComponents[ i ].getName() ) ) ;
			}
		}
		else
		{
			_w.observerInputPanel.setVisible( false ) ;
			for( int i = 0 ; i < taggedComponents.length ; i++ )
			{
				if( taggedComponents[ i ] instanceof JTextField )
					note.rmAVPair( taggedComponents[ i ].getName() ) ;
			}
		}
	}

	/**
	 * A key has been pressed in the note rich text box widget.
	 * @see KeyPressWatcher
	 */
	public void keyPressed( KeyEvent evt )
	{
		RichTextBoxWidgetExt rtbw ;
		rtbw = _w.note ;

		(( SpNote )_spItem).setNote( rtbw.getText() ) ;
	}

	/**
	 * Watch changes to the title text box.
	 * @see TextBoxWidgetWatcher
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		_spItem.setTitleAttr( tbwe.getText().trim() ) ;
	}

	/**
	 * Text box action, ignore.
	 * @see TextBoxWidgetWatcher
	 */
	public void textBoxAction( TextBoxWidgetExt tbwe ){}

	public void checkBoxAction( CheckBoxWidgetExt checkBoxWidgetExt )
	{
		(( SpNote )_spItem).setObserveInstruction( _w.observeInstruction.getBooleanValue() ) ;
		_updateWidgets() ;
	}
}
