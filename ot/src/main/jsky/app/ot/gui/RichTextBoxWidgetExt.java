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

package jsky.app.ot.gui ;

import java.util.Enumeration ;
import java.util.Vector ;
import javax.swing.JTextArea ;
import java.awt.event.KeyEvent ;

/**
 * A RichTextBoxWidget extension that supports listeners on its
 * TextView.  Clients wishing to listen to key presses on the
 * contained TextView must call <code>getTextViewExt()</code> to
 * get the TextViewExt class.  Listeners are added to the TextViewExt.
 * <p>
 * Note: the Swing version is based on JTextArea, which is for plain text,
 * so the class name should probably be changed (allan).
 */
@SuppressWarnings( "serial" )
public class RichTextBoxWidgetExt extends JTextArea implements DescriptiveWidget
{
	// The observers
	private Vector<KeyPressWatcher> _watchers = new Vector<KeyPressWatcher>() ;

	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description ;

	/** Default Constructor */
	public RichTextBoxWidgetExt()
	{
		setLineWrap( true ) ;
		setWrapStyleWord( true ) ;
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
	 * text box.  Attempts to add a watcher that is already registered have
	 * no effect.
	 */
	public synchronized final void addWatcher( KeyPressWatcher kpw )
	{
		if( !_watchers.contains( kpw ) )
			_watchers.addElement( kpw ) ;
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized final void deleteWatcher( KeyPressWatcher kpw )
	{
		_watchers.removeElement( kpw ) ;
	}

	/** Override base class version to intercept key events and notify watchers */
	protected void processKeyEvent( KeyEvent e )
	{
		super.processKeyEvent( e ) ;
		_notifyKeyPress( e ) ;
	}

	/**
	 * Delete all the watchers.
	 */
	public synchronized final void deleteWatchers()
	{
		_watchers.removeAllElements() ;
	}

	//
	// Notify watchers that a key has been pressed.
	//
    private synchronized void _notifyKeyPress( KeyEvent evt )
	{
		Enumeration<KeyPressWatcher> e = _watchers.elements() ;
		while( e.hasMoreElements() )
		{
			KeyPressWatcher kpw = e.nextElement() ;
			kpw.keyPressed( evt ) ;
		}
	}
}
