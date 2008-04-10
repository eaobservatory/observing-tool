// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

import java.util.Vector ;
import javax.swing.ListSelectionModel ;
import javax.swing.JScrollPane ;
import javax.swing.JFrame ;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import jsky.util.gui.BasicWindowMonitor;

/**
 * Was an extension of the Marimba TableWidget to support row selection
 * and action observers. Now this class is derived from JTable.
 */
public class TableWidgetExt extends RowManipulateTableWidget implements DescriptiveWidget
{
	// Observers
	private Vector _watchers = new Vector();

	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.
	 *
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description;

	/** Default constructor */
	public TableWidgetExt()
	{
		getSelectionModel().addListSelectionListener( new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent e )
			{
				int i = getSelectionModel().getMinSelectionIndex();
				if( i >= 0 && !e.getValueIsAdjusting() )
					_notifySelect( i );
			}
		} );
		setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		setShowHorizontalLines( false );
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
	 * Add a watcher.  Watchers are notified when an item is selected.
	 */
	public synchronized final void addWatcher( TableWidgetWatcher tww )
	{
		if( !_watchers.contains( tww ) )
			_watchers.addElement( tww );
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized final void deleteWatcher( TableWidgetWatcher tww )
	{
		_watchers.removeElement( tww );
	}

	/**
	 * Delete all watchers.
	 */
	public synchronized final void deleteWatchers()
	{
		_watchers.removeAllElements();
	}

	/**
	 * Notify observers when a row is selected.
	 */
	public void selectRowAt( int rowIndex )
	{
		getSelectionModel().addSelectionInterval( rowIndex , rowIndex );
		// Notify watchers
		_notifySelect( rowIndex );
	}

	/**
	 * Notify observers when a row is selected.
	 */
	protected void _notifySelect( int rowIndex )
	{
		Vector v;
		synchronized( this )
		{
			v = ( Vector )_watchers.clone();
		}

		int cnt = v.size();
		for( int i = 0 ; i < cnt ; ++i )
		{
			TableWidgetWatcher tww = ( TableWidgetWatcher )v.elementAt( i );
			tww.tableRowSelected( this , rowIndex );
		}
	}

	/**
	 * Extends action method of TableWidget to notify observers.
	 */
	public void action( int colIndex , int rowIndex )
	{
		// XXX allan: this method is not called, from anywhere now, but there are no watchers using it either...

		System.out.println( "Action on table: col(" + colIndex + "), row(" + rowIndex + ")" );

		// Notify watchers
		Vector v;
		synchronized( this )
		{
			v = ( Vector )_watchers.clone();
		}

		int cnt = v.size();
		for( int i = 0 ; i < cnt ; ++i )
		{
			TableWidgetWatcher tww = ( TableWidgetWatcher )v.elementAt( i );
			tww.tableAction( this , colIndex , rowIndex );
		}
	}

	/**
	 * test main
	 */
	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "TableWidgetExt" );

		TableWidgetExt table = new TableWidgetExt();
		String[] headers = new String[] { "One" , "Two" , "Three" , "Four" };
		table.setColumnHeaders( headers );
		Vector[] v = new Vector[ 5 ];
		for( int i = 0 ; i < v.length ; i++ )
		{
			v[ i ] = new Vector( 4 );
			for( int j = 0 ; j < headers.length ; j++ )
				v[ i ].add( "cell " + i + ", " + j );
		}
		table.setRows( v );
		table.addWatcher( new TableWidgetWatcher()
		{
			public void tableRowSelected( TableWidgetExt twe , int rowIndex )
			{
				System.out.println( "tableRowSelected: " + rowIndex );
			}

			public void tableAction( TableWidgetExt twe , int colIndex , int rowIndex )
			{
				System.out.println( "tableAction: " + rowIndex );
			}
		} );

		frame.add( "Center" , new JScrollPane( table ) );
		frame.pack();
		frame.setVisible( true );
		frame.addWindowListener( new BasicWindowMonitor() );
	}
}
