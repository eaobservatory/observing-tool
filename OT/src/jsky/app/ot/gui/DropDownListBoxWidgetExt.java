// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

import java.util.Enumeration ;
import java.util.Vector ;
import javax.swing.JComboBox ;
import javax.swing.JFrame ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import jsky.util.gui.BasicWindowMonitor ;

/**
 * A non-editable combo box with watchers.
 * 
 *
 * @author	Shane Walker, Allan Brighton (Swing port)
 */
@SuppressWarnings( "serial" )
public class DropDownListBoxWidgetExt extends JComboBox implements DescriptiveWidget
{
	// Observers
	private Vector<DropDownListBoxWidgetWatcher> _watchers = new Vector<DropDownListBoxWidgetWatcher>() ;

	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description ;

	/** If true, don't fire any action events */
	protected boolean actionsEnabled = true ;

	/** Default Constructor */
	public DropDownListBoxWidgetExt()
	{
		addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				_notifyAction( getIntegerValue() ) ;
			}
		} ) ;
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
	 * Add a watcher.  Watchers are notified when an item is selected.
	 */
	public synchronized final void addWatcher( DropDownListBoxWidgetWatcher watcher )
	{
		if( !_watchers.contains( watcher ) )
			_watchers.addElement( watcher ) ;
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized final void deleteWatcher( DropDownListBoxWidgetWatcher watcher )
	{
		_watchers.removeElement( watcher ) ;
	}

	/**
	 * Delete all watchers.
	 */
	public synchronized final void deleteWatchers()
	{
		_watchers.removeAllElements() ;
	}

	//
	// Notify watchers that an item has been double-clicked.
	//
	private synchronized void _notifyAction( int index )
	{
		if( actionsEnabled )
		{
			Enumeration<DropDownListBoxWidgetWatcher> e = _watchers.elements() ;
			while( e.hasMoreElements() )
			{
				DropDownListBoxWidgetWatcher watcher = e.nextElement() ;
				watcher.dropDownListBoxAction( this , index , getStringValue() ) ;
			}
		}
	}

	/**
	 * Set the index of the selected value.
	 */
	public void setValue( int index )
	{
		actionsEnabled = false ;
		setSelectedIndex( index ) ;
		actionsEnabled = true ;
	}

	/**
	 * Set the selected value.
	 */
	public void setValue( Object o )
	{
		actionsEnabled = false ;
		setSelectedItem( o ) ;
		actionsEnabled = true ;
	}

	/**
	 * Return the (String) value of the selected item.
	 */
	public Object getValue()
	{
		return getSelectedItem() ;
	}

	/** Return the index of the selected value. */
	public int getIntegerValue()
	{
		return getSelectedIndex() ;
	}

	/** Return the index of the selected value. */
	public String getStringValue()
	{
		return getSelectedItem().toString() ;
	}

	/** Add the given object to the list of choices */
	public void addChoice( Object o )
	{
		addItem( o ) ;
	}

	/** Set the choices by specifying a Vector containing the strings that represent the choices. */
	public void setChoices( Vector<String> choices )
	{
		actionsEnabled = false ;
		removeAllItems() ;
		int n = choices.size() ;
		for( int i = 0 ; i < n ; i++ )
			addItem( choices.get( i ) ) ;
		actionsEnabled = true ;
	}

	/** Set the choices by specifying the strings that appear on screen. */
	public void setChoices( String[] choices )
	{
		actionsEnabled = false ;
		removeAllItems() ;
		for( int i = 0 ; i < choices.length ; i++ )
			addItem( choices[ i ] ) ;
		actionsEnabled = true ;
	}

	/** Clear the list of choices. */
	public void clear()
	{
		actionsEnabled = false ;
		removeAllItems() ;
		actionsEnabled = true ;
	}

	/**
	 * test main
	 */
	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "DropDownListBoxWidgetExt" ) ;

		DropDownListBoxWidgetExt ddlbwe = new DropDownListBoxWidgetExt() ;
		ddlbwe.setChoices( new String[] { "One" , "Two" , "Three" , "Four" , "Five" , "Six" } ) ;
		ddlbwe.setChoices( new String[] { "XOne" , "XTwo" , "XThree" , "XFour" , "XFive" , "XSix" } ) ;

		ddlbwe.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt ddlbwe , int index , String val )
			{
				System.out.println( "dropDownListBoxAction: " + ddlbwe.getValue() ) ;
			}
		} ) ;

		frame.add( "Center" , ddlbwe ) ;
		frame.pack() ;
		frame.setVisible( true ) ;
		frame.addWindowListener( new BasicWindowMonitor() ) ;
	}
}
