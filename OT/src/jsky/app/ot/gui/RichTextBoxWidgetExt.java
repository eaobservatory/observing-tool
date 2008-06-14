// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

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
	private void _notifyKeyPress( KeyEvent evt )
	{
		Vector v ;
		synchronized( this )
		{
			v = ( Vector )_watchers.clone() ;
		}

		for( int i = 0 ; i < v.size() ; ++i )
		{
			KeyPressWatcher kpw = ( KeyPressWatcher )v.elementAt( i ) ;
			kpw.keyPressed( evt ) ;
		}
	}
}
