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
package jsky.app.ot.gui ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import java.util.Enumeration ;
import java.util.Vector ;
import javax.swing.JTextField ;
import javax.swing.JFrame ;
import javax.swing.event.DocumentListener ;
import javax.swing.event.DocumentEvent ;
import jsky.util.gui.BasicWindowMonitor ;

/**
 * A TextBoxWidget that permits clients to register as key press watchers.
 *
 * @author	Shane Walker, Allan Brighton (Swing port)
 */
@SuppressWarnings( "serial" )
public class TextBoxWidgetExt extends JTextField implements DescriptiveWidget , DocumentListener , ActionListener
{
	// Observers
	private Vector<TextBoxWidgetWatcher> _watchers = new Vector<TextBoxWidgetWatcher>() ;

	// if true, ignore changes in the text box content
	private boolean _ignoreChanges = false ;

	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description ;

	/** Default constructor */
	public TextBoxWidgetExt()
	{
		getDocument().addDocumentListener( this ) ;
		addActionListener( this ) ;
	}

	// -- For the DocumentListener interface --

	/** 
	 * Gives notification that there was an insert into the
	 * document. The range given by the DocumentEvent bounds the
	 * freshly inserted region. 
	 */
	public void insertUpdate( DocumentEvent e )
	{
		if( !_ignoreChanges )
			_notifyKeyPress() ;
	}

	/**
	 * Gives notification that a portion of the document has been
	 * removed. The range is given in terms of what the view last saw
	 * (that is, before updating sticky positions).  
	 */
	public void removeUpdate( DocumentEvent e )
	{
		if( !_ignoreChanges )
			_notifyKeyPress() ;
	}

	/** Gives notification that an attribute or set of attributes changed. */
	public void changedUpdate( DocumentEvent e ){}

	// -- For the ActionListener interface --

	/** Invoked when an action occurs. */
	public void actionPerformed( ActionEvent e )
	{
		_notifyAction() ;
	}

	/**
	 * Set the description.
	 * @see #description
	 */
	public void setDescription( String newDescription )
	{
		description = newDescription ;
	}

	/**
	 * Get the description.
	 * @see #description
	 */
	public String getDescription()
	{
		return description ;
	}

	/**
	 * Add a watcher.  Watchers are notified when a key is pressed in the
	 * text box.
	 */
	public synchronized final void addWatcher( TextBoxWidgetWatcher watcher )
	{
		if( !_watchers.contains( watcher ) )
			_watchers.addElement( watcher ) ;
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized final void deleteWatcher( TextBoxWidgetWatcher watcher )
	{
		_watchers.removeElement( watcher ) ;
	}

	/**
	 * Delegate this method from the Observable interface.
	 */
	public synchronized final void deleteWatchers()
	{
		_watchers.removeAllElements() ;
	}

	//
	// Notify watchers that a key has been pressed.
	// 
	private synchronized void _notifyKeyPress()
	{
		Enumeration<TextBoxWidgetWatcher> e = _watchers.elements() ;
		while( e.hasMoreElements() )
		{
			TextBoxWidgetWatcher watcher = e.nextElement() ;
			try
			{
				watcher.textBoxKeyPress( this ) ;
			}
			catch( Exception ex ){}
		}
	}

	//
	// Notify watchers that a return key has been pressed in the text box.
	//
	private synchronized void _notifyAction()
	{
		Enumeration<TextBoxWidgetWatcher> e = _watchers.elements() ;
		while( e.hasMoreElements() )
		{
			TextBoxWidgetWatcher watcher = e.nextElement() ;
			watcher.textBoxAction( this ) ;
		}
	}

	/**
	 * Get the current value as a double.
	 */
	public double getDoubleValue( double def )
	{
		try
		{
			return Double.valueOf( getValue() ) ;
		}
		catch( Exception ex ){}
		return def ;
	}

	/**
	 * Set the current value as a double.
	 */
	public void setValue( double d )
	{
		setText( String.valueOf( d ) ) ;
	}

	/**
	 * Get the current value as an int.
	 */
	public int getIntegerValue( int def )
	{
		try
		{
			return Integer.parseInt( getValue() ) ;
		}
		catch( Exception ex ){}

		return def ;
	}

	/**
	 * Set the current value
	 */
	public void setText( String s )
	{
		_ignoreChanges = true ;

		// added by MFO (14 August 2001)
		try
		{
			super.setText( s ) ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}

		_ignoreChanges = false ;
	}

	/**
	 * Set the current value as an int.
	 */
	public void setValue( int i )
	{
		setText( String.valueOf( i ) ) ;
	}

	/**
	 * Set the current value.
	 */
	public void setValue( String s )
	{
		setText( s ) ;
	}

	/**
	 * Return the current value.
	 */
	public String getValue()
	{
		return getText() ;
	}

	/**
	 * test main
	 */
	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "TextBoxWidgetExt" ) ;

		TextBoxWidgetExt tbw = new TextBoxWidgetExt() ;
		tbw.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbwe )
			{
				System.out.println( "textBoxKeyPress: " + tbwe.getValue() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbwe )
			{
				System.out.println( "textBoxAction: " + tbwe.getValue() ) ;
			}
		} ) ;

		frame.add( "Center" , tbw ) ;
		frame.pack() ;
		frame.setVisible( true ) ;
		frame.addWindowListener( new BasicWindowMonitor() ) ;
	}
}
