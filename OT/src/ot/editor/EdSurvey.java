// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2003                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.editor ;

import java.awt.Color ;
import java.awt.event.ActionEvent ;
import java.awt.event.InputEvent ;
import java.awt.event.KeyEvent ;
import java.awt.event.KeyListener ;
import java.awt.event.MouseAdapter ;
import java.awt.event.MouseEvent ;
import java.io.File ;
import java.util.Vector ;
import java.util.Observer ;
import java.util.Observable ;
import javax.swing.JFileChooser ;
import javax.swing.JOptionPane ;
import javax.swing.table.JTableHeader ;
import javax.swing.table.TableColumnModel ;
import javax.swing.table.TableColumn ;
import javax.swing.table.DefaultTableModel ;
import javax.swing.table.DefaultTableCellRenderer ;
import javax.swing.table.TableModel ;
import javax.swing.event.ChangeListener ;
import javax.swing.event.ChangeEvent ;
import javax.swing.event.ListSelectionListener ;
import javax.swing.event.ListSelectionEvent ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;
import gemini.sp.SpItem ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpSurveyContainer ;
import gemini.sp.obsComp.SpTelescopeObsComp ;
import gemini.util.DDMMSS ;
import jsky.app.ot.editor.EdCompTargetList ;
import jsky.app.ot.tpe.TelescopePosEditor ;
import jsky.app.ot.tpe.TpeManager ;

// MFO, June 06, 2002:
//   At the moment the only supported type is MAJOR. So the DropDownListBoxWidgetExt namedSystemType
//   has been commented out for now. I have not removed it completely from the code in case it is
//   needed in the future.
//   A DropDownListBoxWidgetExt with the available named target choices has been added.

// Conic System (Orbital Elements), Named System (named target such as planets, sun , moon etc)
// and Target specification using offsets added.
// Martin Folger (M.Folger@roe.ac.uk) February 27, 2002
/**
 * This is the editor for the target list component.
 */
@SuppressWarnings( "serial" )
public final class EdSurvey extends EdCompTargetList implements ListSelectionListener , KeyListener , Observer
{
	private static final String[] COLUMN_NAMES = 
	{ 
		"Name" , 
		"X Axis" , 
		"Y Axis" , 
		"Coord System" , 
		"Remaining" , 
		"Priority" 
	} ;

	private SurveyGUI _surveyGUI ; // the GUI layout panel
	private SpSurveyContainer _surveyObsComp = null ;
	private SpTelescopeObsComp _telescopeObsComp = null ;
	private boolean _ignoreEvents = false ;
	private static final String doubleRegex = "[+-]?\\d+\\.\\d+" ;

	private static final int LESS_THAN = -1 ;
	private static final int EQUAL = 0 ;
	private static final int GREATER_THAN = 1 ;

	/**
	 * Only used for GUIs that display list of survey targets and the
	 * target information editor side by side (rather than using a JTabbedPane).
	 *
	 * If the GUI is changed such that the list of survey targets and the
	 * target information editor are displayed side by side (rather than
	 * using a JTabbedPane) then all occurrances of _doNotUpdateSurveyWidgets
	 * should be uncommented in the code and all occurrances of _doNotUpdateWidgets
	 * should be commented out.
	 */

	/**
	 * Flag is used to delay _updateWidgets until the "Edit Target" tab has been selected.
	 *
	 * This flag is only needed due a java bug: When a target is selected on the
	 * "Survey Targets" tab then some of the "Edit Target" tab widgets become visible although
	 * "Edit Target" tab is not selected and should stay in the background.
	 *
	 * If the GUI is changed such that the list of survey targets and the
	 * target information editor are displayed side by side (rather than
	 * using a JTabbedPane) then all occurrances of _doNotUpdateSurveyWidgets
	 * should be uncommented in the code and all occurrances of _doNotUpdateWidgets
	 * should be commented out.
	 */
	private boolean _doNotUpdateWidgets = false ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdSurvey()
	{
		_title = "Survey Information" ;
		_presSource = _surveyGUI = new SurveyGUI( _w ) ;
		_description = "Use this editor to enter the survey information." ;

		// Initialise the remaining field like an MSB
		for( int i = 0 ; i < 100 ; i++ )
		{
			_surveyGUI.remaining.addItem( "" + i ) ;
			_surveyGUI.priority.addItem( "" + i ) ;
		}
		_surveyGUI.remaining.addItem( "(UN)REMOVE" ) ;

		_surveyGUI.fieldTable.getSelectionModel().addListSelectionListener( this ) ;
		_surveyGUI.addButton.addActionListener( this ) ;
		_surveyGUI.duplicateButton.addActionListener( this ) ;
		_surveyGUI.removeButton.addActionListener( this ) ;
		_surveyGUI.removeAllButton.addActionListener( this ) ;
		_surveyGUI.loadButton.addActionListener( this ) ;
		_surveyGUI.remaining.addActionListener( this ) ;
		_surveyGUI.priority.addActionListener( this ) ;
		_surveyGUI.chooseButton.addActionListener( this ) ;

		_surveyGUI.selectField.addKeyListener( this ) ;
		_surveyGUI.titleField.addKeyListener( this ) ;

		_surveyGUI.fieldTable.setModel( new DefaultTableModel()
		{
			public boolean isCellEditable( int row , int column )
			{
				return false ;
			}
		} ) ;
		_surveyGUI.fieldTable.setCellSelectionEnabled( false ) ;
		_surveyGUI.fieldTable.setColumnSelectionAllowed( false ) ;
		_surveyGUI.fieldTable.setRowSelectionAllowed( true ) ;

		TelescopePosWidgetWatcher telescopePosWidgetWatcher = new TelescopePosWidgetWatcher() ;

		_name.addWatcher( telescopePosWidgetWatcher ) ;
		_xaxis.addWatcher( telescopePosWidgetWatcher ) ;
		_yaxis.addWatcher( telescopePosWidgetWatcher ) ;
		_system.addWatcher( telescopePosWidgetWatcher ) ;

		_surveyGUI.tabbedPane.addChangeListener( new ChangeListener()
		{
			public void stateChanged( ChangeEvent e )
			{
				_surveyGUI.telescopeGUIVisible() ;
				_updateWidgets() ;
			}
		} ) ;

		// This should not be user editable for now (at least not for WFCAM).
		// If a the survey component is scheduled as part of an MBS then
		// remaining count and priority settings will probably be taken care of
		// by the MSB and the could removed from the survey component completely.
		_surveyGUI.remaining.setEnabled( true ) ;
		_surveyGUI.priority.setEnabled( true ) ;

		// Try adding a mouse listener to the column tables
		MouseAdapter columnListener = new MouseAdapter()
		{
			public void mouseClicked( MouseEvent e )
			{
				TableColumnModel columnModel = _surveyGUI.fieldTable.getColumnModel() ;
				int viewColumn = columnModel.getColumnIndexAtX( e.getX() ) ;
				int column = _surveyGUI.fieldTable.convertColumnIndexToModel( viewColumn ) ;
				if( e.getClickCount() == 1 && column != -1 )
				{
					// Sort based on selected column...
					boolean ascending = ( ( e.getModifiers() & InputEvent.SHIFT_MASK ) == 0 ) ;
					_sortByColumn( column , ascending , 0 , _surveyGUI.fieldTable.getRowCount() - 1 ) ;
					_updateFieldTable() ;
				}
			}
		} ;
		JTableHeader th = _surveyGUI.fieldTable.getTableHeader() ;
		th.addMouseListener( columnListener ) ;

	}

	public void setup( SpItem spItem )
	{
		_surveyObsComp = ( SpSurveyContainer )spItem ;

		_otItemEditorWindow.getUndoButton().addActionListener( this ) ;
		_otItemEditorWindow.getShowEditPencilButton().addMouseListener( new MouseAdapter()
		{
			public void mousePressed( MouseEvent e )
			{
				_undo() ;
			}

			public void mouseReleased( MouseEvent e )
			{
				_undo() ;
			}
		} ) ;

		if( _surveyObsComp.size() < 1 )
		{
			_surveyObsComp.addSpTelescopeObsComp() ;

			_surveyObsComp.setRemaining( 1 , 0 ) ;
			_surveyObsComp.setPriority( 1 , 0 ) ;
		}

		if( _surveyObsComp.hasMSBParent() )
		{
			_surveyGUI.chooseButton.setEnabled( false ) ;
			_surveyGUI.selectField.setEditable( false ) ;
			_surveyGUI.selectField.setText( "" ) ;
			_surveyObsComp.setChoose( 0 ) ;
			_surveyGUI.priority.setEnabled( false ) ;
		}
		else if( _surveyObsComp.isChoice() )
		{
			_surveyGUI.chooseButton.setSelected( true ) ;
			_surveyGUI.chooseButton.setEnabled( true ) ;
			_surveyGUI.selectField.setText( "" + _surveyObsComp.getChoose() ) ;
			_surveyGUI.selectField.setEnabled( true ) ;
			_surveyGUI.selectField.setEditable( true ) ;
		}
		else
		{
			_surveyGUI.chooseButton.setEnabled( true ) ;
			_surveyGUI.selectField.setEnabled( false ) ;
			_surveyGUI.selectField.setEditable( false ) ;
			_surveyGUI.priority.setEnabled( true ) ;
		}

		_surveyGUI.selectLabel.setText( "from " + _surveyObsComp.size() ) ;
		_surveyGUI.selectLabel.repaint() ;

		super.setup( _surveyObsComp.getSpTelescopeObsComp( _surveyObsComp.getSelectedTelObsComp() ) ) ;
	}

	protected void _updateWidgets()
	{
		if( !_doNotUpdateWidgets )
		{
			_ignoreEvents = true ;
	
			_updateFieldTable() ;

			_surveyGUI.fieldTable.setRowSelectionInterval( _surveyObsComp.getSelectedTelObsComp() , _surveyObsComp.getSelectedTelObsComp() ) ;
	
			if( _surveyObsComp.getRemaining( _surveyGUI.fieldTable.getSelectedRow() ) < 0 )
				_surveyGUI.remaining.setSelectedIndex( _surveyGUI.remaining.getItemCount() - 1 ) ;
			else
				_surveyGUI.remaining.setSelectedIndex( _surveyObsComp.getRemaining( _surveyGUI.fieldTable.getSelectedRow() ) ) ;
	
			_surveyGUI.priority.setSelectedIndex( _surveyObsComp.getPriority( _surveyGUI.fieldTable.getSelectedRow() ) ) ;
			_surveyGUI.titleField.setText( _surveyObsComp.getTitleAttr() ) ;
	
			if( _surveyObsComp.size() == 0 )
			{
				_w.setVisible( false ) ;
			}
			else
			{
				_w.setVisible( true ) ;
				super._updateWidgets() ;
			}
	
			_ignoreEvents = false ;
		}
	}

	private void _updateFieldTable()
	{
		_updateFieldTable( _surveyObsComp.getSelectedTelObsComp() ) ;
	}

	private void _updateFieldTable( int selectedRow )
	{
		_ignoreEvents = true ;

		String[][] data = new String[ _surveyObsComp.size() ][] ;

		for( int i = 0 ; i < data.length ; i++ )
			data[ i ] = _getRowData( _surveyObsComp.getSpTelescopeObsComp( i ).getPosList().getBasePosition() , i ) ;

		( ( DefaultTableModel )_surveyGUI.fieldTable.getModel() ).setDataVector( data , COLUMN_NAMES ) ;

		TableColumnModel tableColumnModel = _surveyGUI.fieldTable.getColumnModel() ;
		TableColumn columnFour = tableColumnModel.getColumn( 4 ) ;
		DefaultTableCellRenderer columnFourCellRenderer = ( DefaultTableCellRenderer )columnFour.getCellRenderer() ;
		if( columnFourCellRenderer == null )
		{
			columnFourCellRenderer = new DefaultTableCellRenderer() ;
			columnFour.setCellRenderer( columnFourCellRenderer ) ;
		}

		TableColumn columnFive = tableColumnModel.getColumn( 5 ) ;
		DefaultTableCellRenderer columnFiveCellRenderer = ( DefaultTableCellRenderer )columnFive.getCellRenderer() ;
		if( columnFive.getCellRenderer() == null )
		{
			columnFiveCellRenderer = new DefaultTableCellRenderer() ;
			columnFive.setCellRenderer( new DefaultTableCellRenderer() ) ;
		}

		columnFourCellRenderer.setBackground( _w.getBackground() ) ;
		columnFourCellRenderer.setForeground( Color.gray ) ;
		columnFiveCellRenderer.setBackground( _w.getBackground() ) ;
		columnFiveCellRenderer.setForeground( Color.gray ) ;

		if( selectedRow < 0 )
		{
			_surveyGUI.fieldTable.setRowSelectionInterval( 0 , 0 ) ;
		}
		else
		{
			if( selectedRow >= _surveyGUI.fieldTable.getRowCount() )
				_surveyGUI.fieldTable.setRowSelectionInterval( _surveyGUI.fieldTable.getRowCount() - 1 , _surveyGUI.fieldTable.getRowCount() - 1 ) ;
			else
				_surveyGUI.fieldTable.setRowSelectionInterval( selectedRow , selectedRow ) ;
		}

		_ignoreEvents = false ;
	}

	private void _sortByColumn( int column , boolean ascending , int left , int right )
	{
		int index = partition( left , right , column , ascending ) ;
		if( left < index - 1 )
			_sortByColumn( column , ascending , left , index - 1 ) ;
		if( index < right )
			_sortByColumn( column , ascending , index , right ) ;
	}

	private int partition( int left , int right , int column , boolean ascending )
	{
		int i = left ;
		int j = right ;

		TableModel data = _surveyGUI.fieldTable.getModel() ;

		Object pivot = data.getValueAt( ( i + j ) / 2 , column ) ;

		while( i <= j )
		{
			while( _compare( data.getValueAt( i , column ) , pivot , column , ascending ) == LESS_THAN )
				i++ ;

			while( _compare( data.getValueAt( j , column ) , pivot , column , ascending ) == GREATER_THAN )
				j-- ;

			if( i <= j )
				_swap( i++ , j-- ) ;
		}
		return i ;
	}

	private int _compare( Object first , Object second , int column , boolean ascending )
	{
		int result = EQUAL ;
		if( first == null || second == null )
		{
			if( first == null && second == null )
				result = EQUAL ;
			else if( first == null )
				result = LESS_THAN ;
			else if( second == null )
				result = GREATER_THAN ;
		}
		else
		{
			// Since these are quite hard to sort we will not try to do it in a generic way
			switch( column )
			{
				case 0 :
				case 1 :
					result = compareAsString( first , second ) ;
					break ;
				case 2 :
					result = compareYaxis( first , second ) ;
					break ;
				case 3 :
				case 4 :
					if( first.toString().equals( "REMOVED" ) )
						first = "100" ;
					if( second.toString().equals( "REMOVED" ) )
						second = "100" ;
					result = compareAsNumber( first , second ) ;
					break ;
				default :
					result = compareAsString( first , second ) ;
			}
		}
		return ascending ? result : -1 * result ;
	}

	private int compareAsString( Object o1 , Object o2 )
	{
		int returnable = EQUAL ;
		int result = o1.toString().compareTo( o2.toString() ) ;
		if( result > 0 )
			returnable = GREATER_THAN ;
		else if( result < 0 )
			returnable = LESS_THAN ;

		return returnable ;
	}

	private int compareAsNumber( Object o1 , Object o2 )
	{
		int returnable = EQUAL ;
		try
		{
			double d1 = new Double( o1.toString() ) ;
			double d2 = new Double( o2.toString() ) ;
			if( d1 < d2 )
				returnable = LESS_THAN ;
			else if( d1 > d2 )
				returnable = GREATER_THAN ;
		}
		catch( NumberFormatException nfe ){}

		return returnable ;
	}

	private int compareYaxis( Object o1 , Object o2 )
	{
		// Compares declination.  Need to convert current format to a number
		// Decide whether we already have decimal degree or not.
		// If not, this is indicated by fields separated by either
		// a colon or a space.  If neither exist, then we should already be in decimal degrees
		double n1 = 0. ;
		double n2 = 0. ;

		String s1 = o1.toString() ;
		String s2 = o2.toString() ;
		if( DDMMSS.validFormat( s1 ) )
			n1 = DDMMSS.valueOf( s1 ) ;
		else if( s1.matches( doubleRegex ) )
			n1 = Double.parseDouble( s1 ) ;

		if( DDMMSS.validFormat( s2 ) )
			n2 = DDMMSS.valueOf( s2 ) ;
		else if( s2.matches( doubleRegex ) )
			n2 = Double.parseDouble( s2 ) ;

		return compareAsNumber( n1 , n2 ) ;
	}

	public void _swap( int row1 , int row2 )
	{
		if( row1 != row2 )
		{
			int rem1 = _surveyObsComp.getRemaining( row1 ) ;
			int rem2 = _surveyObsComp.getRemaining( row2 ) ;

			int pri1 = _surveyObsComp.getPriority( row1 ) ;
			int pri2 = _surveyObsComp.getPriority( row2 ) ;

			// We also need to swap the TelescopePositions so when the table is redrawn, the new positions are read
			SpTelescopeObsComp tp2 = _surveyObsComp.getSpTelescopeObsComp(row2);
			SpTelescopeObsComp tp1 = _surveyObsComp.getSpTelescopeObsComp(row1);
			_surveyObsComp.replaceSpTelescopeObsComp( tp1 , row2 ) ;
			_surveyObsComp.replaceSpTelescopeObsComp( tp2 , row1 ) ;

			// Also swap the remaining and priority
			_surveyObsComp.setRemaining( rem1 , row2 ) ;
			_surveyObsComp.setRemaining( rem2 , row1 ) ;

			_surveyObsComp.setPriority( pri1 , row2 ) ;
			_surveyObsComp.setPriority( pri2 , row1 ) ;

			_updateFieldTable() ;
		}
	}

	private String[] _getRowData( SpTelescopePos tp )
	{
		Vector<String> v = new Vector<String>() ;
		v.addElement( tp.getName() ) ;

		if( tp.getSystemType() == SpTelescopePos.SYSTEM_SPHERICAL )
		{
			if( tp.isOffsetPosition() )
			{
				v.addElement( "" + tp.getXaxisAsString() + " (\u2206)" ) ;
				v.addElement( "" + tp.getYaxisAsString() + " (\u2206)" ) ;
			}
			else
			{
				v.addElement( tp.getXaxisAsString() ) ;
				v.addElement( tp.getYaxisAsString() ) ;
			}
		}
		else
		{
			v.addElement( "  - - -" ) ;
			v.addElement( "  - - -" ) ;
		}

		switch( tp.getSystemType() )
		{
			case SpTelescopePos.SYSTEM_CONIC :
				v.addElement( "Orb. Elem." ) ;
				break ;
			case SpTelescopePos.SYSTEM_NAMED :
				v.addElement( "Planets etc." ) ;
				break ;
			case SpTelescopePos.SYSTEM_SPHERICAL :
				v.addElement( tp.getCoordSysAsString() ) ;
				break ;
		}

		v.addElement( "0" ) ;
		v.addElement( "0" ) ;

		String[] result = new String[ COLUMN_NAMES.length ] ;
		v.toArray( result ) ;

		return result ;
	}

	private String[] _getRowData( SpTelescopePos tp , int index )
	{
		String[] result = _getRowData( tp ) ;
		if( _surveyObsComp.getRemaining( index ) < 0 )
			result[ 4 ] = "REMOVED" ;
		else
			result[ 4 ] += _surveyObsComp.getRemaining( index ) ;
		result[ 5 ] += _surveyObsComp.getPriority( index ) ;

		return result ;
	}

	public void valueChanged( ListSelectionEvent e )
	{
		if( !_ignoreEvents )
			_surveyTargetSelectionChanged() ;
	}

	private void _surveyTargetSelectionChanged()
	{
		_telescopeObsComp = _surveyObsComp.getSpTelescopeObsComp( _surveyGUI.fieldTable.getSelectedRow() ) ;
		_telescopeObsComp.getAvEditFSM().addObserver( this ) ;

		/*
		 * If the GUI is changed such that the list of survey targets and the
		 * target information editor are displayed side by side (rather than using a JTabbedPane) 
		 * then all occurrances of _doNotUpdateSurveyWidgets should be uncommented in the code 
		 * and all occurrances of _doNotUpdateWidgets should be commented out.
		 */
		_doNotUpdateWidgets = true ;
		super.setup( _telescopeObsComp ) ;

		TelescopePosEditor tpe = TpeManager.get( _telescopeObsComp ) ;
		if( tpe != null )
			tpe.reset( _telescopeObsComp ) ;

		_doNotUpdateWidgets = false ;

		int selectedRowIndex = _surveyGUI.fieldTable.getSelectedRow() ;
		_surveyObsComp.setSelectedTelObsComp( selectedRowIndex ) ;

		if( _surveyObsComp.getRemaining( selectedRowIndex ) < 0 )
			_surveyGUI.remaining.setSelectedIndex( _surveyGUI.remaining.getItemCount() - 1 ) ;
		else
			_surveyGUI.remaining.setSelectedIndex( _surveyObsComp.getRemaining( selectedRowIndex ) ) ;
		_surveyGUI.priority.setSelectedIndex( _surveyObsComp.getPriority( selectedRowIndex ) ) ;
	}

	public void actionPerformed( ActionEvent e )
	{
		if( _ignoreEvents )
			return ;

		Object source = e.getSource() ;
		if( source == _surveyGUI.addButton )
		{
			( ( DefaultTableModel )_surveyGUI.fieldTable.getModel() ).addRow( _getRowData( _surveyObsComp.addSpTelescopeObsComp().getPosList().getBasePosition() ) ) ;

			_surveyObsComp.setRemaining( 1 , _surveyGUI.fieldTable.getRowCount() - 1 ) ;
			_surveyObsComp.setPriority( 1 , _surveyGUI.fieldTable.getRowCount() - 1 ) ;

			_surveyGUI.selectLabel.setText( "from " + _surveyGUI.fieldTable.getRowCount() ) ;
			_surveyGUI.selectLabel.repaint() ;

			_updateFieldTable() ;

			return ;
		}
		else if (source == _surveyGUI.duplicateButton) {
			int currentRow = _surveyGUI.fieldTable.getSelectedRow();

			SpTelescopeObsComp target = _surveyObsComp.getSpTelescopeObsComp(currentRow).clone();
			_surveyObsComp.addSpTelescopeObsComp(target);

			((DefaultTableModel) _surveyGUI.fieldTable.getModel()).addRow(_getRowData(
				target.getPosList().getBasePosition()));

			_surveyObsComp.setRemaining(_surveyObsComp.getRemaining(currentRow),
					_surveyGUI.fieldTable.getRowCount() - 1);
			_surveyObsComp.setPriority(_surveyObsComp.getPriority(currentRow),
					_surveyGUI.fieldTable.getRowCount() - 1);

			_surveyGUI.selectLabel.setText("from " + _surveyGUI.fieldTable.getRowCount());
			_surveyGUI.selectLabel.repaint() ;

			_updateFieldTable() ;

			return;
		}
		else if( source == _surveyGUI.removeButton )
		{
			if( _surveyObsComp.isChoice() )
			{
				if( ( _surveyObsComp.getChoose() + 1 ) == _surveyObsComp.size() )
				{
					int cont = JOptionPane.showConfirmDialog( _surveyGUI , "Removing another item will disable the choose option.\n" + "Do you want to continue?" , "Disable Choice?" , JOptionPane.YES_NO_OPTION , JOptionPane.WARNING_MESSAGE ) ;
					if( cont == JOptionPane.NO_OPTION )
						return ;
					else
						_surveyGUI.chooseButton.doClick() ;
				}
			}
			_surveyObsComp.removeSpTelescopeObsComp( _surveyGUI.fieldTable.getSelectedRow() ) ;

			_updateFieldTable() ;

			_surveyTargetSelectionChanged() ;

			_surveyGUI.selectLabel.setText( "from " + _surveyGUI.fieldTable.getRowCount() ) ;
			_surveyGUI.selectLabel.repaint() ;

			return ;
		}
		else if( source == _surveyGUI.removeAllButton )
		{
			_surveyObsComp.removeAllSpTelescopeObsComponents() ;
			_updateFieldTable( 0 ) ;

			_surveyGUI.selectLabel.setText( "from " + _surveyGUI.fieldTable.getRowCount() ) ;
			_surveyGUI.selectLabel.repaint() ;

			return ;
		}
		else if( e.getSource() == _surveyGUI.loadButton )
		{
			_loadFields() ;
			return ;
		}
		else if( source == _surveyGUI.remaining )
		{
			if( _surveyGUI.remaining.getSelectedIndex() == _surveyGUI.remaining.getItemCount() - 1 )
			{
				int newRemaining = -1 * _surveyObsComp.getRemaining( _surveyGUI.fieldTable.getSelectedRow() ) ;
				_surveyObsComp.setRemaining( newRemaining , _surveyGUI.fieldTable.getSelectedRow() ) ;
				if( newRemaining < 0 )
				{
					( ( DefaultTableModel )_surveyGUI.fieldTable.getModel() ).setValueAt( "REMOVED" , _surveyGUI.fieldTable.getSelectedRow() , 4 ) ;
				}
				else
				{
					( ( DefaultTableModel )_surveyGUI.fieldTable.getModel() ).setValueAt( "" + newRemaining , _surveyGUI.fieldTable.getSelectedRow() , 4 ) ;
					_surveyGUI.remaining.setSelectedIndex( newRemaining ) ;
				}
			}
			else
			{
				_surveyObsComp.setRemaining( _surveyGUI.remaining.getSelectedIndex() , _surveyGUI.fieldTable.getSelectedRow() ) ;
				( ( DefaultTableModel )_surveyGUI.fieldTable.getModel() ).setValueAt( "" + _surveyGUI.remaining.getSelectedIndex() , _surveyGUI.fieldTable.getSelectedRow() , 4 ) ;
			}
			return ;
		}
		else if( source == _surveyGUI.priority )
		{
			_surveyObsComp.setPriority( _surveyGUI.priority.getSelectedIndex() , _surveyGUI.fieldTable.getSelectedRow() ) ;
			( ( DefaultTableModel )_surveyGUI.fieldTable.getModel() ).setValueAt( "" + _surveyGUI.priority.getSelectedIndex() , _surveyGUI.fieldTable.getSelectedRow() , 5 ) ;
			return ;
		}
		else if( source == _otItemEditorWindow.getUndoButton() )
		{
			_undo() ;
			return ;
		}
		else if( source == _otItemEditorWindow.getShowEditPencilButton() )
		{
			return ;
		}
		else if( source == _surveyGUI.chooseButton )
		{
			if( _surveyGUI.chooseButton.isSelected() )
			{
				_surveyGUI.selectField.setEditable( true ) ;
				_surveyGUI.selectField.setEnabled( true ) ;
				if( _surveyGUI.selectField.getText().trim().length() == 0 )
					_surveyGUI.selectField.setText( "" + _surveyGUI.fieldTable.getRowCount() ) ;

				_surveyObsComp.setChoose( _surveyGUI.selectField.getText().trim() ) ;
			}
			else
			{
				_surveyGUI.selectField.setEditable( false ) ;
				_surveyGUI.selectField.setEnabled( false ) ;
				_surveyObsComp.setChoose( 0 ) ;
			}
			_surveyGUI.selectField.repaint() ;
		}

		super.actionPerformed( e ) ;
	}

	public void keyPressed( KeyEvent evt ){}

	public void keyTyped( KeyEvent evt ){}

	public void keyReleased( KeyEvent evt )
	{
		if( evt.getSource() == _surveyGUI.selectField )
		{
			int value = 0 ;
			try
			{
				value = Integer.parseInt( _surveyGUI.selectField.getText() ) ;
			}
			catch( NumberFormatException nfe ){}
			if( value > _surveyGUI.fieldTable.getRowCount() )
			{
				JOptionPane.showMessageDialog( _surveyGUI , "Please enter a number less than the number of fields" , "Number too Big" , JOptionPane.WARNING_MESSAGE ) ;
				return ;
			}
			_surveyObsComp.setChoose( _surveyGUI.selectField.getText() ) ;
		}
		else if( evt.getSource() == _surveyGUI.titleField )
		{
			_surveyObsComp.setTitleAttr( _surveyGUI.titleField.getText() ) ;
		}
	}

	public void update( Observable o , Object arg )
	{
		_surveyObsComp.getTable().edit() ;
	}

	private void _loadFields()
	{
		JFileChooser fileChooser = new JFileChooser() ;
		fileChooser.showOpenDialog( _surveyGUI ) ;
		File file = fileChooser.getSelectedFile() ;

		if( file != null )
		{
			try
			{
				_surveyObsComp.load( file.getPath() ) ;
				_updateFieldTable() ;
			}
			catch( Exception e )
			{
				e.printStackTrace() ;
				JOptionPane.showMessageDialog( _surveyGUI , e , "Could not load survey targets" , JOptionPane.WARNING_MESSAGE ) ;
			}
		}
	}

	private void _undo()
	{
		if( _telescopeObsComp != null )
		{
			_telescopeObsComp.getAvEditFSM().undo() ;
			_updateWidgets() ;
		}
	}

	/** Watches name, xaxis, yaxis and coordinate system widgets and updates the list of survey fields accordingly. */
	class TelescopePosWidgetWatcher implements TextBoxWidgetWatcher , DropDownListBoxWidgetWatcher
	{
		public void textBoxKeyPress( TextBoxWidgetExt tbwe )
		{
			if( _curPos.isBasePosition() )
			{
				if( tbwe == _name )
					( ( DefaultTableModel )_surveyGUI.fieldTable.getModel() ).setValueAt( _name.getValue() , _surveyGUI.fieldTable.getSelectedRow() , 0 ) ;
				else if( tbwe == _xaxis )
					( ( DefaultTableModel )_surveyGUI.fieldTable.getModel() ).setValueAt( _xaxis.getValue() , _surveyGUI.fieldTable.getSelectedRow() , 1 ) ;
				else if( tbwe == _yaxis )
					( ( DefaultTableModel )_surveyGUI.fieldTable.getModel() ).setValueAt( _yaxis.getValue() , _surveyGUI.fieldTable.getSelectedRow() , 2 ) ;
			}
		}

		public void textBoxAction( TextBoxWidgetExt tbwe ){}

		public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
		{
			if( _curPos.isBasePosition() && dd == _system )
				( ( DefaultTableModel )_surveyGUI.fieldTable.getModel() ).setValueAt( val , _surveyGUI.fieldTable.getSelectedRow() , 3 ) ;
		}
	}
}
