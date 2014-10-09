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

import java.awt.event.MouseAdapter ;
import java.awt.event.MouseEvent ;
import java.util.Enumeration ;
import java.util.Vector ;
import javax.swing.DefaultListModel ;
import javax.swing.JFrame ;
import javax.swing.JList ;
import javax.swing.JScrollPane ;
import javax.swing.event.ListSelectionEvent ;
import javax.swing.event.ListSelectionListener ;
import jsky.util.gui.BasicWindowMonitor ;

/**
 * An extension of the Marimba ListBoxWidget to selection observers.
 *
 * @author	Shane Walker
 */
@SuppressWarnings( "serial" )
public class ListBoxWidgetExt extends JList implements DescriptiveWidget
{
	// Observers
	private Vector<ListBoxWidgetWatcher> _watchers = new Vector<ListBoxWidgetWatcher>() ;

	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description ;

	/** Default Constructor */
	public ListBoxWidgetExt()
	{
		getSelectionModel().addListSelectionListener( new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent e )
			{
				if( !e.getValueIsAdjusting() )
					_notifySelect( getSelectedIndex() ) ;
			}
		} ) ;

		addMouseListener( new MouseAdapter()
		{
			public void mouseClicked( MouseEvent e )
			{
				if( e.getClickCount() == 2 )
					_notifyAction( locationToIndex( e.getPoint() ) ) ;
			}
		} ) ;
		setModel( new DefaultListModel() ) ;
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
	public synchronized final void addWatcher( ListBoxWidgetWatcher watcher )
	{
		if( !_watchers.contains( watcher ) )
			_watchers.addElement( watcher ) ;
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized final void deleteWatcher( ListBoxWidgetWatcher watcher )
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
	// Notify watchers that an item has been selected.
	//
	private synchronized void _notifySelect( int index )
	{
		Enumeration<ListBoxWidgetWatcher> e = _watchers.elements() ;
		while( e.hasMoreElements() )
		{
			ListBoxWidgetWatcher watcher = e.nextElement() ;
			watcher.listBoxSelect( this , index , getStringValue() ) ;
		}
	}

	//
	// Notify watchers that an item has been double-clicked.
	//
	private synchronized void _notifyAction( int index )
	{
		Enumeration<ListBoxWidgetWatcher> e = _watchers.elements() ;
		while( e.hasMoreElements() )
		{
			ListBoxWidgetWatcher watcher = e.nextElement() ;
			watcher.listBoxAction( this , index , getStringValue() ) ;
		}
	}

	/** Return the String value of teh selected row */
	public String getStringValue()
	{
		return ( String )getSelectedValue() ;
	}

	/**
	 * Focus at the selected item.  I couldn't find an easy way to do this.
	 */
	public void focusAtSelectedItem(){}

	/** Set the contents of the list */
	public void setRows(){}

	/**
	 * test main
	 */
	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "ListBoxWidgetExt" ) ;

		ListBoxWidgetExt list = new ListBoxWidgetExt() ;
		DefaultListModel model = new DefaultListModel() ;
		for( int i = 0 ; i < 50 ; i++ )
			model.addElement( "row " + i ) ;

		list.setModel( model ) ;
		list.addWatcher( new ListBoxWidgetWatcher()
		{
			public void listBoxSelect( ListBoxWidgetExt lbwe , int index , String val )
			{
				System.out.println( "listBoxSelect: " + index ) ;
			}

			public void listBoxAction( ListBoxWidgetExt lbwe , int index , String val )
			{
				System.out.println( "listBoxAction: " + index ) ;
			}
		} ) ;

		frame.add( "Center" , new JScrollPane( list ) ) ;
		frame.pack() ;
		frame.setVisible( true ) ;
		frame.addWindowListener( new BasicWindowMonitor() ) ;
	}

	/** Select the given value */
	public void setValue( int i )
	{
		getSelectionModel().clearSelection() ;
		if( i >= 0 )
			getSelectionModel().addSelectionInterval( i , i ) ;
	}

	/** Select the given value */
	public void setValue( String s )
	{
		setValue( ( ( DefaultListModel )getModel() ).indexOf( s ) ) ;
	}

	/** Set the contents of the list */
	public void setChoices( Vector<String> v )
	{
		DefaultListModel model = new DefaultListModel() ;
		int n = v.size() ;
		for( int i = 0 ; i < n ; i++ )
			model.addElement( v.get( i ) ) ;
		setModel( model ) ;
	}

	/** Set the contents of the list */
	public void setChoices( Object[] ar )
	{
		DefaultListModel model = new DefaultListModel() ;
		for( int i = 0 ; i < ar.length ; i++ )
			model.addElement( ar[ i ] ) ;
		setModel( model ) ;
	}

	/** Clear  out the list */
	public void clear()
	{
		( ( DefaultListModel )getModel() ).clear() ;
	}
}
