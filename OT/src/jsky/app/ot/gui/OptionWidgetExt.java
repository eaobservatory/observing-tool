// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
/**
 * This class watches a OptionWidgetExt object to know which node is selected.
 *
 * @author      Dayle Kotturi, Shane Walker, Allan Brighton (Swing port)
 * @version     $Version$
 */

package jsky.app.ot.gui ;

import java.util.Vector ;
import javax.swing.JRadioButton ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

/**
 * An OptionWidget that permits clients to register as button press watchers.
 */
public class OptionWidgetExt extends JRadioButton implements DescriptiveWidget , ActionListener
{
	// Observers
	private Vector<OptionWidgetWatcher> _watchers = new Vector<OptionWidgetWatcher>() ;

	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description ;

	/** Default constructor */
	public OptionWidgetExt()
	{
		addActionListener( this ) ;
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
	 * Add a watcher.  Watchers are notified when a button is pressed in the
	 * option widget.
	 */
	public synchronized final void addWatcher( OptionWidgetWatcher ow )
	{
		if( !_watchers.contains( ow ) )
			_watchers.addElement( ow ) ;
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized final void deleteWatcher( OptionWidgetWatcher ow )
	{
		_watchers.removeElement( ow ) ;
	}

	/**
	 * Delegate this method from the Observable interface.
	 */
	public synchronized final void deleteWidgetWatchers()
	{
		_watchers.removeAllElements() ;
	}

	//
	// Notify watchers that a button has been pressed in the option widget.
	//
	private void _notifyAction()
	{
		for( int i = 0 ; i < _watchers.size() ; ++i )
		{
			OptionWidgetWatcher ow = _watchers.elementAt( i ) ;
			ow.optionAction( this ) ;
		}
	}

	/** Called when the button is pressed */
	public void actionPerformed( ActionEvent ae )
	{
		_notifyAction() ;
	}

	public void setValue( boolean value )
	{
		setSelected( value ) ;
	}

	public boolean getValue()
	{
		return isSelected() ;
	}
}
