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
package jsky.app.ot.editor ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

import java.util.Hashtable ;
import java.util.List ;
import java.util.Vector ;

import javax.swing.ImageIcon ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;

import jsky.app.ot.gui.CellSelectTableWatcher ;
import jsky.app.ot.gui.CellSelectTableWidget ;
import jsky.app.ot.gui.ListBoxWidgetExt ;
import jsky.app.ot.gui.ListBoxWidgetWatcher ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

import gemini.sp.SpItem ;
import gemini.sp.iter.IterConfigItem ;
import gemini.sp.iter.SpIterConfigBase ;
import gemini.util.ObservingToolUtilities;

//
// Helper class for EdIterGenericConfig.  It is the base class for editing
// various attribute types (currently "list of choices" and "text box").
// The ICValueEditor is concerned with displaying the control needed to
// edit a particular attribute, as described by a
// jsky.app.ot.sp.iter.IterConfigItem.  When the user changes the value in the
// editor, the EdIterGenericConfig instance is informed of the new value.
// Each ICValueEditor subclass appears in its own JPanel.
//
abstract class ICValueEditor
{
	protected EdIterGenericConfig _ci ;
	protected JPanel _container ;
	protected JLabel _title ;

	ICValueEditor( EdIterGenericConfig ci , JPanel gw , JLabel title )
	{
		_ci = ci ;
		_container = gw ;
		_title = title ;
	}

	// Show the JPanel that contains the editor's widgets.
	void setVisible( boolean visible )
	{
		if( visible )
			_ci.showGroup( _container ) ;
	}

	// Update the editor's widgets to reflect the given IterConfigItem.
	abstract void editValue( IterConfigItem configItem , String curValue ) ;

	// Update the editor's widgets to show no value.
	abstract void clear() ;
}

//
// An ICValueEditor for ListBoxes.
//
class ICListBoxValueEditor extends ICValueEditor implements ListBoxWidgetWatcher
{
	ListBoxWidgetExt _choicesLBW ;
	IterConfigItem _lastICI ;

	ICListBoxValueEditor( EdIterGenericConfig ci , JPanel gw , JLabel stw , ListBoxWidgetExt lbw )
	{
		super( ci , gw , stw ) ;
		_choicesLBW = lbw ;
		_choicesLBW.addWatcher( this ) ;
	}

	// Show the list of choices defined in the IterConfigItem, and select the curValue.
	void editValue( IterConfigItem configItem , String curValue )
	{
		_title.setText( configItem.title + " Choices" ) ;
		_choicesLBW.deleteWatcher( this ) ;

		if( configItem != _lastICI )
		{
			// Have to avoid resetting the choices unless the item has changed.
			// There's a bug in Bongo1.1 that breaks the application if you remove
			// the item that caused an event in the event callback ...

			_choicesLBW.setChoices( configItem.choices ) ;
			_lastICI = configItem ;
		}

		if( ( curValue == null ) || curValue.equals( "" ) )
		{
			_choicesLBW.setValue( -1 ) ;
		}
		else
		{
			_choicesLBW.setValue( curValue ) ;
			_choicesLBW.focusAtSelectedItem() ;
		}
		_choicesLBW.addWatcher( this ) ;
	}

	void clear()
	{
		_title.setText( "Nothing Selected" ) ;
		_choicesLBW.deleteWatcher( this ) ;
		_choicesLBW.clear() ;
		_choicesLBW.addWatcher( this ) ;
		_lastICI = null ;
	}

	// Called when the user selects an option from the list box.  The EdIterGenericConfig instance is informed of the new value.
	public void listBoxSelect( ListBoxWidgetExt w , int index , String val )
	{
		// There is a new value for the current attribute, but the user may not be finished editing.
		_ci.cellValueChanged( val , false ) ;
	}

	// Called when the user double-clicks an option from the list box.
	// The EdIterGenericConfig instance is informed of the new value.
	public void listBoxAction( ListBoxWidgetExt w , int index , String val )
	{
		// There is a new value for the current attribute, and the user is finished editing.
		_ci.cellValueChanged( val , true ) ;
	}

}

//
// An ICValueEditor for TextBoxes.
//
class ICTextBoxValueEditor extends ICValueEditor implements TextBoxWidgetWatcher
{

	TextBoxWidgetExt _textBox ;

	ICTextBoxValueEditor( EdIterGenericConfig ci , JPanel gw , JLabel stw , TextBoxWidgetExt tbw )
	{
		super( ci , gw , stw ) ;
		_textBox = tbw ;
		_textBox.addWatcher( this ) ;
	}

	//
	// Put the curValue in the text box.
	//
	void editValue( IterConfigItem configItem , String curValue )
	{
		_title.setText( "Enter " + configItem.title + ":" ) ;
		_textBox.setText( curValue ) ;
	}

	void clear()
	{
		_title.setText( "Nothing Selected" ) ;
		_textBox.setText( "" ) ;
	}

	// Called when the user types a key in the text box.  The
	// EdIterGenericConfig instance is informed of the new value.
	public void textBoxKeyPress( TextBoxWidgetExt tbw )
	{
		_ci.cellValueChanged( tbw.getText() , false ) ;
	}

	// Called when the user types a return key in the text box.  The
	// EdIterGenericConfig instance is informed of the new value.
	public void textBoxAction( TextBoxWidgetExt tbw )
	{
		_ci.cellValueChanged( tbw.getText() , true ) ;
	}
}

//----------------------------------------------------------------------------

/**
 * This class implements an editor for "configuration editor" subclasses.
 * The configuration is edited in a table whose rows are iteration steps,
 * and whose columns are the attributes being iterated over.  In a single
 * step, all the attributes in the table columns are set with the values
 * in the table row when the iterator is executed.
 *
 * <p> The attributes available for iteration (i.e., potential columns in
 * the table) are displayed in a list box in the upper right hand corner.
 * Selecting an item in the list box makes it a column in the table.  When
 * a cell in the table is selected, an editor for its value is displayed
 * in the upper left hand corner.
 *
 * <p>
 * For subclasses of SpIterConfigBase, the items being
 * iterated over (i.e., the table columns) are stored in an attribute
 * called "iterConfigList".  For example:
 * <pre>
 *     &lt;av name=iterConfigList descr="No Description"&gt;
 *        &lt;val value="filterIter"&gt;
 *        &lt;val value="diffuserIter"&gt;
 *     &lt;/av&gt;
 * </pre>
 * shows that this configuration iterator is iterating over two items,
 * the "filters" and "diffusers".
 *
 * <p>
 * For each item being iterated over, there is a attribute with a value
 * for each of its steps.  For instance:
 * <pre>
 *     &lt;av name=filterIter descr="No Description"&gt;
 *        &lt;val value="x300 NDF + CBF"&gt;
 *        &lt;val value="CBF"&gt;
 *     &lt;/av&gt;
 * </pre>
 *
 * <p>
 * shows that the values for the two steps of the filter iteration are
 * "x300 NDF + CBF" and "CBF".  The "diffuserIter" would also have two
 * values for the two steps.
 *
 * @see SpIterConfigBase
 */
public class EdIterGenericConfig extends OtItemEditor implements CellSelectTableWatcher , ListBoxWidgetWatcher , ActionListener
{
	// The iteration table widget.
	protected CellSelectTableWidget _iterTab ;

	// Maps attribute names to IterConfigItems.
	protected Hashtable<String,IterConfigItem> _iterItems ;

	// The array of available items.
	private IterConfigItem[] _iciA ;

	// A ref to either _listBoxVE or _textBoxVE, depending upon the type of
	// attribute represented by the selected cell.
	protected ICValueEditor _valueEditor ;

	protected ICListBoxValueEditor _listBoxVE ;
	protected ICTextBoxValueEditor _textBoxVE ;

	// The list box that contains the available items.
	protected ListBoxWidgetExt _itemsLBW ;

	// The GUI layout
	protected MiniConfigIterGUI _w ;
	
	protected static ClassLoader cl = EdIterGenericConfig.class.getClassLoader() ;

	/** Default constructor */
	public EdIterGenericConfig()
	{
		_title = "Configuration Iterator" ;
		_presSource = _w = new MiniConfigIterGUI() ;
		_description = "Iterate over a configuration with this component." ;

		// add button action listeners
		_w.deleteTest.addActionListener( this ) ;
		_w.addStep.addActionListener( this ) ;
		_w.deleteStep.addActionListener( this ) ;
		_w.top.addActionListener( this ) ;
		_w.up.addActionListener( this ) ;
		_w.down.addActionListener( this ) ;
		_w.bottom.addActionListener( this ) ;

		// JBuilder has some problems with image buttons...
		_w.top.setIcon( new ImageIcon( ObservingToolUtilities.resourceURL( "jsky/app/ot/images/top.gif" ) ) ) ;
		_w.up.setIcon( new ImageIcon( ObservingToolUtilities.resourceURL( "jsky/app/ot/images/up.gif" ) ) ) ;
		_w.bottom.setIcon( new ImageIcon( ObservingToolUtilities.resourceURL( "jsky/app/ot/images/bottom.gif" ) ) ) ;
		_w.down.setIcon( new ImageIcon( ObservingToolUtilities.resourceURL( "jsky/app/ot/images/down.gif" ) ) ) ;

		// --- XXX the code below was in _init() ---

		// Watch for selection of cells in the iterator table.
		_iterTab = _w.iterStepsTable ;
		_iterTab.addWatcher( this ) ;

		// Watch for selection of available items.
		_itemsLBW = _w.availableItems ;
		_itemsLBW.addWatcher( this ) ;

		JPanel gw ;
		JLabel stw ;

		// Initialize the ListBox value editor
		gw = _w.listBoxGroup ;
		stw = _w.listBoxTitle ;
		ListBoxWidgetExt lbw ;
		lbw = _w.availableChoices ;
		_listBoxVE = createICListBoxValueEditor( this , gw , stw , lbw ) ;

		// Initialize the TextBox value editor
		gw = _w.textBoxGroup ;
		stw = _w.textBoxTitle ;
		TextBoxWidgetExt tbw ;
		tbw = _w.textBox ;
		_textBoxVE = createICTextBoxValueEditor( this , gw , stw , tbw ) ;

		_valueEditor = _listBoxVE ;

		_iterItems = new Hashtable<String,IterConfigItem>() ;
	}

	// helper methods for inheriting classes
	protected ICListBoxValueEditor createICListBoxValueEditor( EdIterGenericConfig ci , JPanel gw , JLabel stw , ListBoxWidgetExt lbw )
	{
		return new ICListBoxValueEditor( ci , gw , stw , lbw ) ;
	}

	// helper methods for inheriting classes
	protected ICTextBoxValueEditor createICTextBoxValueEditor( EdIterGenericConfig ci , JPanel gw , JLabel stw , TextBoxWidgetExt tbw )
	{
		return new ICTextBoxValueEditor( ci , gw , stw , tbw ) ;
	}

	/**
	 * Perform any required one-time initialization.
	 */
	protected void _init(){}

	/**
	 * Override setup() to initialize the array of available items and show
	 * the name of the SpItem in the OtItemEditorWindow banner title.
	 */
	public void setup( SpItem spItem )
	{
		// Oops, this is a bug so throw an exception
		if( !( spItem instanceof SpIterConfigBase ) )
			throw new RuntimeException( "Tried to setup an configuration iterator with the wrong kind of SpItem!" ) ;

		// Initialize the array of available items.
		_iciA = ( ( SpIterConfigBase )spItem ).getAvailableItems() ;

		// Show the name of the SpItem in the main window, overriding the value of _title.
		String name = ( ( SpIterConfigBase )spItem ).getItemName() ;
		setEditorWindowTitle( name + " Configuration Iterator" ) ;

		super.setup( spItem ) ;
	}

	/**
	 * Update the widgets to display the values of the attributes in the
	 * current SpItem.
	 */
	protected void _updateWidgets()
	{
		// Clear everything
		_iterItems.clear() ;
		_iterTab.removeAllRows() ;
		_iterTab.removeAllColumns() ;

		_listBoxVE.setVisible( true ) ;
		_textBoxVE.setVisible( false ) ;

		_valueEditor = _listBoxVE ;
		_valueEditor.clear() ;

		SpIterConfigBase icb = ( SpIterConfigBase )_spItem ;

		//
		// Add all the items from the table.
		//

		// Each element in the array is the name of an attribute being iterated over.
		List<String> l = icb.getConfigAttribs() ;

		if( ( l == null ) || ( l.size() == 0 ) )
		{
			// Nothing is being iterated over
			_initAvailableItems() ;
			_updateTableInfo() ;
			return ;
		}

		// For each attribute, add a column in the table, and enter each of
		// the attribute's steps in subsequent table rows.
		int index = 0 ;
		for( int i = 0 ; i < l.size() ; ++i )
		{
			String attrib = l.get(i);

			if( isUserEditable( attrib ) )
			{
				// Add a column for the item
				IterConfigItem ici = _getIterConfigItem( attrib ) ;
				if( ici == null )
					continue ;

				_addConfigItem( ici ) ;

				// Make sure there are enough rows to hold the item's values.
				List<String> vals = icb.getConfigSteps( attrib ) ;
				for( int j = _iterTab.getRowCount() ; j < vals.size() ; ++j )
					_iterTab.addRow() ;

				for( int j = 0 ; j < vals.size() ; ++j )
					_iterTab.setCell( vals.get( j ) , index , j ) ;

				++index ;
			}
		}

		// Select the upper left hand cell, causing its editor to be displayed.
		if( ( _iterTab.getColumnCount() > 0 ) && ( _iterTab.getRowCount() > 0 ) )
			_iterTab.selectCell( 0 , 0 ) ;

		_initAvailableItems() ;
		_updateTableInfo() ;
	}

	//
	// Update table info text box that shows the number of items and steps
	// in the table.
	//
	private void _updateTableInfo()
	{
		JLabel stw = _w.tableInfo ;
		int items = _iterTab.getColumnCount() ;
		int steps = _iterTab.getRowCount() ;

		String message = "(" + items ;
		if( items == 1 )
			message += " Item, " ;
		else
			message += " Items, " ;

		message += steps ;
		if( steps == 1 )
			message += " Step)" ;
		else
			message += " Steps)" ;

		stw.setText( message ) ;
	}

	//
	// Get the IterConfigItem, given the attribute name associated with it.
	//
	private IterConfigItem _getIterConfigItem( String attrib )
	{
		// Go through the array of available IterConfigItems, and return the one
		// whose attribute name matches the attrib parameter.

		for( int i = 0 ; i < _iciA.length ; ++i )
		{
			if( _iciA[ i ].attribute.equals( attrib ) )
				return _iciA[ i ] ;
		}
		return null ;
	}

	//
	// Get the IterConfigItem associated with the selected column.  The
	// IterConfigItem is used to fill in the editor for the attribute.
	//
	private IterConfigItem _getCurIterConfigItem()
	{
		// Get the column index of the selected cell.
		int[] coord = _iterTab.getSelectedCoordinates() ;
		int colIndex = coord[ 0 ] ;
		if( colIndex == -1 )
			return null ;

		// Figure out the title of the column of the selected cell.
		String title = _iterTab.getColumnName( colIndex ) ;

		// Go through the array of available IterConfigItems, and return the one
		// whose title matches the table column's title.
		for( int i = 0 ; i < _iciA.length ; ++i )
		{
			if( _iciA[ i ].title.equals( title ) )
				return _iciA[ i ] ;
		}
		return null ;
	}

	//
	// Given a column index, get the IterConfigItem associated with it.
	//
	protected IterConfigItem _getConfigItem( int colIndex )
	{
		return _iterItems.get( _iterTab.getColumnName( colIndex ) ) ;
	}

	//
	// Get the column index associated with the given IterConfigItem.
	//
	private int _getColIndex( IterConfigItem ici )
	{
		// Find the index of the column that should be deleted
		int col = -1 ;
		for( int i = 0 ; i < _iterTab.getColumnCount() ; ++i )
		{
			if( _iterTab.getColumnName( i ).equals( ici.title ) )
			{
				col = i ;
				break ;
			}
		}
		return col ;
	}

	/**
	 * Select the cell on the current row in the given column.
	 */
	public void selectColumnCell( int colIndex )
	{
		int[] coord = _iterTab.getSelectedCoordinates() ;
		int rowIndex = coord[ 1 ] ;
		_iterTab.selectCell( colIndex , rowIndex ) ;
		_iterTab.focusAtCell( colIndex , rowIndex ) ;
	}

	//
	// Is the given title the title of an IterConfigItem being iterated over?
	//
	final boolean isCurrentIterItem( String title )
	{
		return _iterItems.containsKey( title ) ;
	}

	//
	// Add a configuration item.  Does the work for the public addConfigItem
	// method.  Also called in _updateWidgets.
	//
	private void _addConfigItem( IterConfigItem ici )
	{
		_iterItems.put( ici.title , ici ) ;
		_iterTab.addColumn( ici.title , 105 ) ;
	}

	/**
	 * Add a new attribute to iterate over.
	 */
	public void addConfigItem( IterConfigItem ici )
	{
		_addConfigItem( ici ) ;

		( ( SpIterConfigBase )_spItem ).addConfigItem( ici , _iterTab.getRowCount() ) ;

		_updateWidgets() ;
		selectColumnCell( _iterTab.getColumnCount() - 1 ) ;
	}

	/**
	 * Remove an attribute from the set being iterated over.
	 */
	public void deleteConfigItem( IterConfigItem ici )
	{
		if( _iterTab.getColumnCount() == 0 )
			return ;

		// Find the index of the column that should be deleted
		int deleteCol = _getColIndex( ici ) ;
		if( deleteCol == -1 )
			return ;

		_deleteColumn( ici , deleteCol ) ;
	}

	/**
	 * Delete the column of the currently selected cell from the table.  This
	 * will result in removing the associated attribute from the set of
	 * attributes being iterated over.
	 */
	public void deleteSelectedColumn()
	{
		IterConfigItem ici = _getCurIterConfigItem() ;
		int col = _getColIndex( ici ) ;
		_deleteColumn( ici , col ) ;
	}

	//
	// Delete the given attribute at the given column index.
	//
	private void _deleteColumn( IterConfigItem ici , int deleteCol )
	{
		if( _iterTab.getColumnCount() == 0 )
			return ;

		// Remember which cell was selected.
		int[] coord = _iterTab.getSelectedCoordinates() ;
		int colIndex = coord[ 0 ] ;
		int rowIndex = coord[ 1 ] ;

		// Remove the item from the hashtable and from the table widget
		_iterItems.remove( ici.title ) ;
		_iterTab.removeColumnAt( deleteCol ) ;

		// Make sure the colIndex is still valid
		if( _iterTab.getColumnCount() <= colIndex )
			colIndex = _iterTab.getColumnCount() - 1 ;

		// Reselect the old col,row if there's anything left in the table, otherwise, remove all the rows and all the available choices.
		if( colIndex >= 0 )
		{
			_iterTab.selectCell( colIndex , rowIndex ) ;
			_iterTab.focusAtRow( rowIndex ) ;
		}
		else
		{
			_iterTab.removeAllRows() ;
			_valueEditor.clear() ;
		}

		// Remove the attribute from the table.
		( ( SpIterConfigBase )_spItem ).deleteConfigItem( ici.attribute ) ;

		_unselectAvailableItems() ;

		_updateTableInfo() ;
	}

	/**
	 * Add an iteration step.
	 */
	public void addStep()
	{
		if( _iterTab.getColumnCount() == 0 )
			return ;

		// Figure out the coordinates of the currently selected cell, if any
		int[] coord = _iterTab.getSelectedCoordinates() ;
		int colIndex = coord[ 0 ] ;
		int rowIndex = coord[ 1 ] ;
		if( colIndex == -1 )
			colIndex = 0 ;

		_iterTab.absInsertRowAt( null , ++rowIndex ) ;

		// Insert a blank element into all the iterator attributes.
		( ( SpIterConfigBase )_spItem ).insertConfigStep( rowIndex ) ;

		_updateWidgets() ;

		// Select the cell in the current column, newly added row
		_iterTab.selectCell( colIndex , rowIndex ) ;
		_iterTab.focusAtRow( rowIndex ) ;
	}

	/**
	 * Delete an iteration step.
	 */
	public void deleteStep()
	{
		if( ( _iterTab.getColumnCount() == 0 ) || ( _iterTab.getRowCount() == 0 ) )
			return ;

		int[] coord = _iterTab.getSelectedCoordinates() ;
		if( ( coord[ 0 ] == -1 ) || ( coord[ 1 ] == -1 ) )
			return ;

		int rowIndex = coord[ 1 ] ;
		_iterTab.removeRowAt( rowIndex ) ;

		// Delete the element from all the iterator attributes.
		( ( SpIterConfigBase )_spItem ).deleteConfigStep( rowIndex ) ;

		// Select the next cell in the next step (or previous step if this was the last element.)
		if( _iterTab.getRowCount() <= rowIndex )
			rowIndex = _iterTab.getRowCount() - 1 ;

		if( rowIndex >= 0 )
		{
			_iterTab.selectCell( coord[ 0 ] , rowIndex ) ;
			_iterTab.focusAtRow( rowIndex ) ;
			_updateTableInfo() ;
		}
		else
		{
			addStep() ; // Leave a blank step
		}
	}

	/**
	 * Move the current step to be the first step.
	 */
	public void stepToFirst()
	{
		int[] coord = _iterTab.getSelectedCoordinates() ;
		if( ( coord[ 0 ] == -1 ) || ( coord[ 1 ] == -1 ) )
			return ;

		int rowIndex = coord[ 1 ] ;
		_iterTab.absMoveToFirstRowAt( rowIndex ) ;

		// Move the attribute elements in the avTable
		( ( SpIterConfigBase )_spItem ).configStepToFirst( rowIndex ) ;

		// Select the cell that was just moved
		_iterTab.selectCell( coord[ 0 ] , 0 ) ;
		_iterTab.focusAtRow( 0 ) ;
	}

	/**
	 * Move the current step up one.
	 */
	public void decrementStep()
	{
		int[] coord = _iterTab.getSelectedCoordinates() ;
		if( ( coord[ 0 ] != -1 ) && ( coord[ 1 ] != -1 ) )
		{
			int rowIndex = coord[ 1 ] ;
			_iterTab.absDecrementRowAt( rowIndex ) ;
	
			// Move the attribute elements in the avTable
			( ( SpIterConfigBase )_spItem ).configStepDecrement( rowIndex ) ;
	
			// Select the cell that was just moved
			if( --rowIndex >= 0 )
			{
				_iterTab.selectCell( coord[ 0 ] , rowIndex ) ;
				_iterTab.focusAtRow( rowIndex ) ;
			}
		}
	}

	/**
	 * Move the current step down one.
	 */
	public void incrementStep()
	{
		int[] coord = _iterTab.getSelectedCoordinates() ;
		if( ( coord[ 0 ] != -1 ) && ( coord[ 1 ] != -1 ) )
		{
			int rowIndex = coord[ 1 ] ;
			_iterTab.absIncrementRowAt( rowIndex ) ;
	
			// Move the attribute elements in the avTable
			( ( SpIterConfigBase )_spItem ).configStepIncrement( rowIndex ) ;
	
			// Select the cell that was just moved
			if( ++rowIndex <= ( _iterTab.getRowCount() - 1 ) )
			{
				_iterTab.selectCell( coord[ 0 ] , rowIndex ) ;
				_iterTab.focusAtRow( rowIndex ) ;
			}
		}
	}

	/**
	 * Move the current step to the end.
	 */
	public void stepToLast()
	{
		int[] coord = _iterTab.getSelectedCoordinates() ;
		if( ( coord[ 0 ] != -1 ) && ( coord[ 1 ] != -1 ) )
		{
			int rowIndex = coord[ 1 ] ;
			_iterTab.absMoveToLastRowAt( rowIndex ) ;
	
			// Move the attribute elements in the avTable
			( ( SpIterConfigBase )_spItem ).configStepToLast( rowIndex ) ;
	
			// Select the cell that was just moved
			rowIndex = _iterTab.getRowCount() - 1 ;
			_iterTab.selectCell( coord[ 0 ] , rowIndex ) ;
			_iterTab.focusAtRow( rowIndex ) ;
		}
	}

	//
	// Change the _valueEditor reference to the given value editor.  The
	// old value editor will be hidden, and the new one shown.
	//
	private void _setEditor( ICValueEditor ve )
	{
		if( _valueEditor == ve )
			return ;

		if( _valueEditor != null )
			_valueEditor.setVisible( false ) ;

		_valueEditor = ve ;
		_valueEditor.setVisible( true ) ;
	}

	/**
	 * Called when a table cell is selected.  The value editor is reconfigured
	 * to display the appropriate editor for the attribute in the selected
	 * cell.
	 *
	 * @see CellSelectTableWidget
	 */
	public void cellSelected( CellSelectTableWidget w , int colIndex , int rowIndex )
	{
		String cellValue = ( String )w.getCell( colIndex , rowIndex ) ;
		IterConfigItem ici = _iterItems.get( _iterTab.getColumnName( colIndex ) ) ;

		if( ici.choices == null )
			_setEditor( _textBoxVE ) ;
		else
			_setEditor( _listBoxVE ) ;

		_valueEditor.editValue( ici , cellValue ) ;
	}

	/**
	 * Ignore cell actions.
	 * @see CellSelectTableWidget
	 */
	public void cellAction( CellSelectTableWidget w , int colIndex , int rowIndex ){}

	//
	// Turn off the selection in the list box containing the available
	// items.  
	//
	private void _unselectAvailableItems()
	{
		_itemsLBW.deleteWatcher( this ) ;
		_itemsLBW.setValue( -1 ) ;
		_itemsLBW.addWatcher( this ) ;
	}

	//
	// Initialize the list box containing the available items.
	//
	private void _initAvailableItems()
	{
		Vector<String> v = new Vector<String>( _iciA.length ) ;

		for( int i = 0 ; i < _iciA.length ; ++i )
		{
			// In Bongo1.0, used to only add the items that weren't already in
			// the table.  Used to only show the items that weren't already in
			// the table.  But you can't dynamically remove an item in Bongo1.1
			// in the callback for its selection.  So now I just show everything
			// and never remove items from the list ...

			IterConfigItem ici = _iciA[ i ] ;

			if( isUserEditable( ici.title ) )
				v.addElement( ici.title ) ;
		}

		_itemsLBW.deleteWatcher( this ) ;
		_itemsLBW.setValue( -1 ) ;
		_itemsLBW.setChoices( v ) ;
		_itemsLBW.addWatcher( this ) ;
	}

	/**
	 * Called when an item is selected from the list of available items.  The
	 * corresponding attribute is added as a table column and the selection
	 * is moved to the new column.
	 *
	 * @see ListBoxWidgetWatcher
	 */
	public void listBoxSelect( ListBoxWidgetExt w , int index , String val )
	{
		if( w != _itemsLBW )
			throw new RuntimeException( "weird listBoxSelect error: " + w ) ;

		IterConfigItem ici = null ;
		for( int i = 0 ; i < _iciA.length ; ++i )
		{
			if( val.equals( _iciA[ i ].title ) )
				ici = _iciA[ i ] ;
		}

		if( ici == null )
			return ;

		// Find the index of the column, if it is already in the table.  If not,
		// add it.  If so, select it.
		int colIndex = _getColIndex( ici ) ;
		if( colIndex == -1 )
			addConfigItem( ici ) ;
		else
			selectColumnCell( colIndex ) ;

		_updateTableInfo() ;
	}

	/**
	 * Ignore list box actions.
	 * @see ListBoxWidgetWatcher
	 */
	public void listBoxAction( ListBoxWidgetExt w , int index , String val ){}

	/**
	 * This method is called when the value of the selected step and attribute
	 * is changed.
	 */
	public void cellValueChanged( String newVal , boolean finishedEditing )
	{
		// Get the selected cell's coordinates
		int[] coord = _iterTab.getSelectedCoordinates() ;
		if( ( coord[ 0 ] == -1 ) || ( coord[ 1 ] == -1 ) )
			return ;

		int colIndex = coord[ 0 ] ;
		int rowIndex = coord[ 1 ] ;

		// Figure out the IterConfigItem that goes with the selected cell
		IterConfigItem ici = _getConfigItem( colIndex ) ;
		if( ici == null )
			throw new RuntimeException( "couldn't find the IterConfigItem associated with column: " + colIndex ) ;

		// Set the value in the selected cell
		_iterTab.setSelectedCell( newVal ) ;

		( ( SpIterConfigBase )_spItem ).setConfigStep( ici.attribute , newVal , rowIndex ) ;

		++rowIndex ;

		if( ( finishedEditing ) && ( rowIndex < _iterTab.getRowCount() ) )
		{
			// Move to the next cell down in the column
			_iterTab.selectCell( colIndex , rowIndex ) ;
			_iterTab.focusAtCell( colIndex , rowIndex ) ;
		}
	}

	//
	// Handle action events on the buttons in the editor. 
	//
	public void actionPerformed( ActionEvent e )
	{
		Object w = e.getSource() ;

		if( w == _w.deleteTest )
			deleteSelectedColumn() ;
		else if( w == _w.addStep )
			addStep() ;
		else if( w == _w.deleteStep )
			deleteStep() ;
		else if( w == _w.top )
			stepToFirst() ;
		else if( w == _w.up )
			decrementStep() ;
		else if( w == _w.down )
			incrementStep() ;
		else if( w == _w.bottom )
			stepToLast() ;
	}

	/** Show the given group of widgets (in upper left choice panel) */
	public void showGroup( JPanel panel )
	{
		if( panel == _w.listBoxGroup )
			_w.choicePanel.remove( _w.textBoxGroup ) ;
		else if( panel == _w.textBoxGroup )
			_w.choicePanel.remove( _w.listBoxGroup ) ;

		_w.choicePanel.add( "Center" , panel ) ;
		_w.choicePanel.revalidate() ;
		_w.getParent().repaint() ;
	}

	/**
	 * Subclasses can implement this method to prevent certain attributes
	 * to be displayed in the editor.
	 *
	 * This is useful if an iterator contains config items that depend only on
	 * other config items and should not be set by the user directly.
	 */
	protected boolean isUserEditable( String attribute )
	{
		return true ;
	}
}
