// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

import java.util.Vector ;
import javax.swing.JFrame ;
import javax.swing.JScrollPane ;
import javax.swing.ListSelectionModel ;
import javax.swing.event.ChangeEvent ;
import javax.swing.event.ListSelectionEvent ;
import javax.swing.event.ListSelectionListener ;
import javax.swing.event.TableColumnModelEvent ;
import javax.swing.event.TableColumnModelListener ;
import javax.swing.table.DefaultTableModel ;
import jsky.util.gui.BasicWindowMonitor ;

/**
 * A TableWidget subclass that supports selection of individual cells
 * (as opposed to just entire rows as int org.freebongo.gui.TableWidget).
 */
@SuppressWarnings( "serial" )
public class CellSelectTableWidget extends RowManipulateTableWidget implements DescriptiveWidget
{
	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.  (There's really no need for this to be private ...)
	 *
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description ;
	private Vector<CellSelectTableWatcher> _watchers = new Vector<CellSelectTableWatcher>() ;

	/**
	 * Construct the CellSelectTableWidget, setting the selectMode to NONE.
	 */
	public CellSelectTableWidget()
	{
		setRowSelectionAllowed( true ) ;
		setColumnSelectionAllowed( true ) ;
		setCellSelectionEnabled( true ) ;
		setSelectionMode( ListSelectionModel.SINGLE_SELECTION ) ;
		setShowHorizontalLines( true ) ;

		// track row selections
		getSelectionModel().addListSelectionListener( new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent e )
			{
				if( !e.getValueIsAdjusting() )
				{
					int col = getSelectedColumn() ;
					int row = getSelectionModel().getLeadSelectionIndex() ;
					cellSelected( col , row ) ;
				}
			}
		} ) ;

		// track column selections
		getColumnModel().addColumnModelListener( new TableColumnModelListener()
		{
			public void columnAdded( TableColumnModelEvent e )
			{
				selectCell( e.getToIndex() , getSelectionModel().getLeadSelectionIndex() ) ;
			}

			public void columnRemoved( TableColumnModelEvent e ){}

			public void columnMoved( TableColumnModelEvent e ){}

			public void columnMarginChanged( ChangeEvent e ){}

			public void columnSelectionChanged( ListSelectionEvent e )
			{
				if( !e.getValueIsAdjusting() )
				{
					int col = getColumnModel().getSelectionModel().getLeadSelectionIndex() ;
					int row = getSelectedRow() ;
					cellSelected( col , row ) ;
				}
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
	 * Check whether the rows are separated by a line.
	 */
	public boolean getRowLine()
	{
		return getShowHorizontalLines() ;
	}

	/**
	 * Show/hide the lines between rows.
	 */
	public void setRowLine( boolean rowLine )
	{
		setShowHorizontalLines( rowLine ) ;
	}

	/**
	 * Check whether the rows are highlighted as well.
	 */
	public boolean getRowHighlight()
	{
		return getRowSelectionAllowed() ;
	}

	/**
	 * Show/hide the row highlight.
	 */
	public void setRowHighlight( boolean rowHighlight )
	{
		setRowSelectionAllowed( rowHighlight ) ;
	}

	/**
	 * Focus at a particular cell.
	 */
	public void focusAtCell( int colIndex , int rowIndex )
	{
		selectCell( colIndex , rowIndex ) ;
	}

	/**
	 * Select a particular cell.
	 */
	public void selectCell( int colIndex , int rowIndex )
	{
		if( ( colIndex < 0 ) || ( colIndex >= getModel().getColumnCount() ) )
			return ;
		if( ( rowIndex < 0 ) || ( rowIndex >= getModel().getRowCount() ) )
			return ;
		changeSelection( rowIndex , colIndex , false , false ) ;
	}

	/**
	 * Get the column and row indices (in that order) of the selected cell.
	 * Returns { -1, -1 } if no column or row is selected.
	 */
	public int[] getSelectedCoordinates()
	{
		int colIndex = getSelectedColumn() ;
		int rowIndex = getSelectedRow() ;
		int[] c = { colIndex , rowIndex } ;
		return c ;
	}

	/**
	 * Get the currently selected cell.
	 */
	public Object getSelectedCell()
	{
		return getValueAt( getSelectedRow() , getSelectedColumn() ) ;
	}

	/**
	 * Set the currently selected cell.
	 */
	public void setSelectedCell( Object item )
	{
		int selectedCol = getSelectedColumn() ;
		int selectedRow = getSelectedRow() ;
		if( ( selectedCol != -1 ) && ( selectedRow != -1 ) )
			getModel().setValueAt( item , selectedRow , selectedCol ) ;
	}

	/** Add an empty row to the table */
	public void addRow()
	{
		DefaultTableModel model = getModel() ;
		Vector<Object> v = new Vector<Object>( model.getColumnCount() ) ;
		model.addRow( v ) ;
	}

	/** Append an empty column to this table. */
	public void addColumn( String header , int width )
	{
		getModel().addColumn( header ) ;
		getColumn( header ).setPreferredWidth( width ) ;
	}

	/**
	 * The given cell was selected.
	 */
	@SuppressWarnings( "unchecked" )
    public void cellSelected( int colIndex , int rowIndex )
	{
		if( ( colIndex < 0 ) || ( colIndex >= getModel().getColumnCount() ) )
			return ;
		if( ( rowIndex < 0 ) || ( rowIndex >= getModel().getRowCount() ) )
			return ;

		Vector<CellSelectTableWatcher> v ;
		synchronized( this )
		{
			v = ( Vector<CellSelectTableWatcher> )_watchers.clone() ;
		}

		int cnt = v.size() ;
		for( int i = 0 ; i < cnt ; ++i )
		{
			CellSelectTableWatcher cstw ;
			cstw = v.elementAt( i ) ;
			cstw.cellSelected( this , colIndex , rowIndex ) ;
		}
	}

	/**
	 * Add a CellSelectTableWatcher.
	 */
	public synchronized final void addWatcher( CellSelectTableWatcher cstw )
	{
		if( !_watchers.contains( cstw ) )
			_watchers.addElement( cstw ) ;
	}

	/**
	 * Delete a CellSelectTableWatcher.
	 */
	public synchronized final void deleteWatcher( CellSelectTableWatcher cstw )
	{
		_watchers.removeElement( cstw ) ;
	}

	/**
	 * Delete all watchers.
	 */
	public synchronized final void deleteWatchers()
	{
		_watchers.removeAllElements() ;
	}

	/**
	 * test main
	 */
	@SuppressWarnings( "unchecked" )
    public static void main( String[] args )
	{
		JFrame frame = new JFrame( "CellSelectTableWidget" ) ;

		CellSelectTableWidget table = new CellSelectTableWidget() ;
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
		table.addWatcher( new CellSelectTableWatcher()
		{
			public void cellSelected( CellSelectTableWidget w , int colIndex , int rowIndex )
			{
				System.out.println( "tableCellSelected: " + rowIndex + ", " + colIndex ) ;
			}
		} ) ;

		frame.add( "Center" , new JScrollPane( table ) ) ;
		frame.pack() ;
		frame.setVisible( true ) ;
		frame.addWindowListener( new BasicWindowMonitor() ) ;
	}
}
