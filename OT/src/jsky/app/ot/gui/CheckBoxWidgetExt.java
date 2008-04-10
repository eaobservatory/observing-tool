// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//

/**
 * This class watches a CheckBoxWidgetExt object to know which node is selected.
 *
 * @author      Dayle Kotturi, Allan Brighton (Swing Port)
 * @version     1.0, 8/8/97
 */

package jsky.app.ot.gui;

import java.util.Vector ;
import javax.swing.JCheckBox ;
import javax.swing.JFrame ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import jsky.util.gui.BasicWindowMonitor;

/**
 * An CheckBoxWidget that permits clients to register as button press watchers.
 */
public class CheckBoxWidgetExt extends JCheckBox implements DescriptiveWidget , ActionListener
{
	// Observers
	private Vector _watchers = new Vector();

	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description;

	/** Default constructor */
	public CheckBoxWidgetExt()
	{
		addActionListener( this );
	}

	/** Default constructor */
	public CheckBoxWidgetExt( String text )
	{
		this();
		setText( text );
	}

	/**
	 * Set the description.
	 * @see #description
	 */
	public void setDescription( String newDescription )
	{
		description = newDescription;
	}

	/**
	 * Get the description.
	 * @see #description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Add a watcher.  Watchers are notified when a button is pressed in the
	 * option widget.
	 */
	public synchronized final void addWatcher( CheckBoxWidgetWatcher cbw )
	{
		if( !_watchers.contains( cbw ) )
			_watchers.addElement( cbw );
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized final void deleteWatcher( CheckBoxWidgetWatcher cbw )
	{
		_watchers.removeElement( cbw );
	}

	/**
	 * Delegate this method from the Observable interface.
	 */
	public synchronized final void deleteWidgetWatchers()
	{
		_watchers.removeAllElements();
	}

	//
	// Notify watchers that a button has been pressed in the option widget.
	//
	private void _notifyAction()
	{
		for( int i = 0 ; i < _watchers.size() ; ++i )
		{
			CheckBoxWidgetWatcher cbw = ( CheckBoxWidgetWatcher )_watchers.elementAt( i );
			cbw.checkBoxAction( this );
		}
	}

	/** Called when the button is pressed. */
	public void actionPerformed( ActionEvent ae )
	{
		_notifyAction();
	}

	public void setValue( boolean value )
	{
		setSelected( value );
	}

	public boolean getBooleanValue()
	{
		return isSelected();
	}

	/**
	 * test main
	 */
	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "CheckBoxWidgetExt" );

		CheckBoxWidgetExt button = new CheckBoxWidgetExt( "Push Me" );
		button.addWatcher( new CheckBoxWidgetWatcher()
		{
			public void checkBoxAction( CheckBoxWidgetExt cbw )
			{
				System.out.println( "OK" );
			}
		} );

		frame.add( "Center" , button );
		frame.pack();
		frame.setVisible( true );
		frame.addWindowListener( new BasicWindowMonitor() );
	}
}
