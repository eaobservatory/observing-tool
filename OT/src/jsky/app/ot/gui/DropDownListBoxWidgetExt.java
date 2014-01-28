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
