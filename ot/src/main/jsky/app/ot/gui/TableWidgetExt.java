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
import javax.swing.ListSelectionModel ;
import javax.swing.JScrollPane ;
import javax.swing.JFrame ;
import javax.swing.event.ListSelectionEvent ;
import javax.swing.event.ListSelectionListener ;
import jsky.util.gui.BasicWindowMonitor ;

/**
 * Was an extension of the Marimba TableWidget to support row selection
 * and action observers. Now this class is derived from JTable.
 */
@SuppressWarnings( "serial" )
public class TableWidgetExt extends RowManipulateTableWidget implements DescriptiveWidget
{
	// Observers
	private Vector<TableWidgetWatcher> _watchers = new Vector<TableWidgetWatcher>() ;

	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.
	 *
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description ;

	/** Default constructor */
	public TableWidgetExt()
	{
		getSelectionModel().addListSelectionListener( new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent e )
			{
				int i = getSelectionModel().getMinSelectionIndex() ;
				if( i >= 0 && !e.getValueIsAdjusting() )
					_notifySelect( i ) ;
			}
		} ) ;
		setSelectionMode( ListSelectionModel.SINGLE_SELECTION ) ;
		setShowHorizontalLines( false ) ;
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
	public synchronized final void addWatcher( TableWidgetWatcher tww )
	{
		if( !_watchers.contains( tww ) )
			_watchers.addElement( tww ) ;
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized final void deleteWatcher( TableWidgetWatcher tww )
	{
		_watchers.removeElement( tww ) ;
	}

	/**
	 * Delete all watchers.
	 */
	public synchronized final void deleteWatchers()
	{
		_watchers.removeAllElements() ;
	}

	/**
	 * Notify observers when a row is selected.
	 */
	public void selectRowAt( int rowIndex )
	{
		getSelectionModel().addSelectionInterval( rowIndex , rowIndex ) ;
		// Notify watchers
		_notifySelect( rowIndex ) ;
	}

	/**
	 * Notify observers when a row is selected.
	 */
    protected synchronized void _notifySelect( int rowIndex )
	{
		Enumeration<TableWidgetWatcher> e = _watchers.elements() ;
		while( e.hasMoreElements() )
		{
			TableWidgetWatcher tww = e.nextElement() ;
			tww.tableRowSelected( this , rowIndex ) ;
		}
	}

	/**
	 * test main
	 */
	@SuppressWarnings( "unchecked" )
    public static void main( String[] args )
	{
		JFrame frame = new JFrame( "TableWidgetExt" ) ;

		TableWidgetExt table = new TableWidgetExt() ;
		String[] headers = new String[] { "One" , "Two" , "Three" , "Four" } ;
		table.setColumnHeaders( headers ) ;
		Vector<String>[] v = new Vector[ 5 ] ;
		for( int i = 0 ; i < v.length ; i++ )
		{
			v[ i ] = new Vector<String>( 4 ) ;
			for( int j = 0 ; j < headers.length ; j++ )
				v[ i ].add( "cell " + i + ", " + j ) ;
		}
		table.setRows( v ) ;
		table.addWatcher( new TableWidgetWatcher()
		{
			public void tableRowSelected( TableWidgetExt twe , int rowIndex )
			{
				System.out.println( "tableRowSelected: " + rowIndex ) ;
			}

			public void tableAction( TableWidgetExt twe , int colIndex , int rowIndex )
			{
				System.out.println( "tableAction: " + rowIndex ) ;
			}
		} ) ;

		frame.add( "Center" , new JScrollPane( table ) ) ;
		frame.pack() ;
		frame.setVisible( true ) ;
		frame.addWindowListener( new BasicWindowMonitor() ) ;
	}
}
