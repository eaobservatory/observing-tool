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

package jsky.app.ot.editor ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

import java.util.Vector ;
import java.util.Observer ;
import java.util.Observable ;

import javax.swing.ImageIcon ;
import javax.swing.JLabel ;
import javax.swing.event.TableModelListener ;
import javax.swing.event.TableModelEvent ;

import jsky.app.ot.OtCfg ;
import jsky.app.ot.gui.TableWidgetExt ;
import jsky.app.ot.gui.TableWidgetWatcher ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher ;
import jsky.app.ot.tpe.TpeManager ;

import gemini.sp.SpItem ;
import gemini.sp.SpAvEditState ;
import gemini.sp.iter.SpIterChop ;
import gemini.util.ObservingToolUtilities;

import orac.util.TelescopeUtil ;

/**
 * Editor for Chop iterator.
 *
 * @author Based on EdIterGenericConfig (Gemini OT), modified by Martin Folger (M.Folger@roe.ac.uk).
 */
public class EdIterChop extends OtItemEditor implements DropDownListBoxWidgetWatcher , TextBoxWidgetWatcher , TableWidgetWatcher , ActionListener , TableModelListener , Observer
{
	// The iteration table widget.
	private TableWidgetExt _iterTab ;
	
	private SpIterChop _iterChop ;

	// The GUI layout
	IterChopGUI _w ;

	/** If true, ignore table model events. */
	private boolean _ignoreGuiEvents = false ;

	/** Default constructor */
	public EdIterChop()
	{
		_title = "Chop Iterator" ;
		_presSource = _w = new IterChopGUI() ;
		_description = "Iterate over a chop configurations." ;

		// add button action listeners
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

		// MFO: This is probaly JCMT specific. Might need modification when the Chop iterator is used with UKIRT.
		_w.coordFrameListBox.setChoices( OtCfg.telescopeUtil.getCoordSysFor( TelescopeUtil.CHOP ) ) ;
		if( _w.coordFrameListBox.getItemCount() < 2 )
			_w.coordFrameListBox.setEnabled( false ) ;

		_w.throwTextBox.addWatcher( this ) ;
		_w.angleTextBox.addWatcher( this ) ;
		_w.coordFrameListBox.addWatcher( this ) ;

		// --- XXX the code below was in _init() ---

		_iterTab = _w.iterStepsTable ;
		_iterTab.setColumnHeaders( new String[] { "Throw" , "Angle" , "Coord Frame" } ) ;
		_iterTab.sizeColumnsToFit( TableWidgetExt.AUTO_RESIZE_ALL_COLUMNS ) ;
		_iterTab.addWatcher( this ) ;
		_iterTab.getModel().addTableModelListener( this ) ;
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
		_iterChop = ( SpIterChop )spItem ;

		if( _iterChop != null )
			_iterChop.getAvEditFSM().deleteObserver( this ) ;
		super.setup( spItem ) ;
		_iterChop.getAvEditFSM().addObserver( this ) ;
	}

	/**
	 * Update the widgets to display the values of the attributes in the
	 * current SpItem.
	 */
	protected void _updateWidgets()
	{
		_ignoreGuiEvents = true ;

		if( _iterChop.getStepCount() < 1 )
			_iterChop.addInitialStep() ;

		_iterTab.setRows( _iterChop.getAllSteps() ) ;

		if( _iterTab.getRowCount() > 0 )
			_iterTab.selectRowAt( 0 ) ;

		_updateTableInfo() ;

		_ignoreGuiEvents = false ;
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

		StringBuffer buffer = new StringBuffer() ;

		buffer.append( "(" ) ;
		buffer.append( items ) ;
		buffer.append( " " ) ;
		buffer.append( "Item" ) ;
		if( items > 1 )
			buffer.append( "s" ) ;
		buffer.append( "," ) ;
		buffer.append( " " ) ;
		buffer.append( steps ) ;
		buffer.append( " " ) ;
		buffer.append( "Step" ) ;
		if( items > 1 )
			buffer.append( "s" ) ;
		buffer.append( ")" ) ;

		String message = buffer.toString() ;
		buffer = null ;

		stw.setText( message ) ;
	}

	/**
	 * Observes the associated SpItem attribute table for changes to the
	 * chop parameters.
	 */
	public void update( Observable o , Object arg )
	{
		// Was it the spItem attributes?
		if( o instanceof SpAvEditState )
			_updateWidgets() ;
	}

	/**
	 * Add an iteration step.
	 */
	public void addStep()
	{
		if( _iterTab.getColumnCount() != 0 )
		{
			int rowIndex = _iterTab.getSelectedRow() ;

			Vector<String> rowVector = new Vector<String>() ;
			rowVector.add( SpIterChop.getDefaultThrow() ) ;
			rowVector.add( SpIterChop.getDefaultAngle() ) ;
			rowVector.add( SpIterChop.getDefaultCoordFrame() ) ;

			_iterTab.absInsertRowAt( rowVector , ++rowIndex ) ;

			// Select the newly added row
			_iterTab.selectRowAt( rowIndex ) ;
			_iterTab.focusAtRow( rowIndex ) ;

			_updateTableInfo() ;
		}
	}

	/**
	 * Delete an iteration step.
	 */
	public void deleteStep()
	{
		if( ( _iterTab.getColumnCount() != 0 ) && ( _iterTab.getRowCount() != 0 ) )
		{
			int rowIndex = _iterTab.getSelectedRow() ;
			_iterTab.removeRowAt( rowIndex ) ;

			if( _iterTab.getRowCount() <= rowIndex )
				rowIndex = _iterTab.getRowCount() - 1 ;

			if( rowIndex >= 0 )
			{
				_iterTab.selectRowAt( rowIndex ) ;
				_iterTab.focusAtRow( rowIndex ) ;
				_updateTableInfo() ;
			}
			else
			{
				addStep() ; // Leave a blank step
			}
		}
	}

	/**
	 * Move the current step to be the first step.
	 */
	public void stepToFirst()
	{
		int rowIndex = _iterTab.getSelectedRow() ;
		_iterTab.absMoveToFirstRowAt( rowIndex ) ;

		_iterTab.selectRowAt( 0 ) ;
		_iterTab.focusAtRow( 0 ) ;
	}

	/**
	 * Move the current step up one.
	 */
	public void decrementStep()
	{
		int rowIndex = _iterTab.getSelectedRow() ;
		_iterTab.absDecrementRowAt( rowIndex ) ;

		if( --rowIndex >= 0 )
		{
			_iterTab.selectRowAt( rowIndex ) ;
			_iterTab.focusAtRow( rowIndex ) ;
		}
	}

	/**
	 * Move the current step down one.
	 */
	public void incrementStep()
	{
		int rowIndex = _iterTab.getSelectedRow() ;
		_iterTab.absIncrementRowAt( rowIndex ) ;

		if( ++rowIndex <= ( _iterTab.getRowCount() - 1 ) )
		{
			_iterTab.selectRowAt( rowIndex ) ;
			_iterTab.focusAtRow( rowIndex ) ;
		}
	}

	/**
	 * Move the current step to the end.
	 */
	public void stepToLast()
	{
		int rowIndex = _iterTab.getSelectedRow() ;
		_iterTab.absMoveToLastRowAt( rowIndex ) ;

		rowIndex = _iterTab.getRowCount() - 1 ;
		_iterTab.selectRowAt( rowIndex ) ;
		_iterTab.focusAtRow( rowIndex ) ;
	}

	public void tableRowSelected( TableWidgetExt w , int rowIndex )
	{
		_ignoreGuiEvents = true ;

		_w.throwTextBox.setValue( ( String )_iterTab.getValueAt( rowIndex , 0 ) ) ;
		_w.angleTextBox.setValue( ( String )_iterTab.getValueAt( rowIndex , 1 ) ) ;
		_w.coordFrameListBox.setValue( _iterTab.getValueAt( rowIndex , 2 ) ) ;

		_iterChop.setSelectedIndex( rowIndex ) ;

		// MFO
		// I think this is implemented in a different way in Gemini ot-2000B.12.
		try
		{
			TpeManager.get( _iterChop ).reset( _iterChop ) ;
		}
		catch( NullPointerException e )
		{
			// ignore
		}

		_ignoreGuiEvents = false ;
	}

	public void tableAction( TableWidgetExt w , int colIndex , int rowIndex ){}

	/*
	 * Handle action events on the buttons in the editor. 
	 */
	public void actionPerformed( ActionEvent e )
	{
		Object w = e.getSource() ;

		if( w == _w.addStep )
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

	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		if( !_ignoreGuiEvents )
		{
			if( tbwe == _w.throwTextBox )
				_iterTab.setValueAt( _w.throwTextBox.getValue() , _iterTab.getSelectedRow() , 0 ) ;
			else if( tbwe == _w.angleTextBox )
				_iterTab.setValueAt( _w.angleTextBox.getValue() , _iterTab.getSelectedRow() , 1 ) ;
		}
	}

	public void textBoxAction( TextBoxWidgetExt tbwe ){}

	public void dropDownListBoxAction( DropDownListBoxWidgetExt ddlbwe , int index , String val )
	{
		if( !_ignoreGuiEvents )
		{
			if( ddlbwe == _w.coordFrameListBox )
				_iterTab.setValueAt( val , _iterTab.getSelectedRow() , 2 ) ;
		}
	}

	public void tableChanged( TableModelEvent e )
	{
		if( !_ignoreGuiEvents )
		{
			_iterChop.getAvEditFSM().deleteObserver( this ) ;

			_iterChop.getTable().rmAll() ;

			for( int i = 0 ; i < _iterTab.getRowCount() ; i++ )
			{
				_iterChop.setThrow( ( String )_iterTab.getValueAt( i , 0 ) , i ) ;
				_iterChop.setAngle( ( String )_iterTab.getValueAt( i , 1 ) , i ) ;
				_iterChop.setCoordFrame( ( String )_iterTab.getValueAt( i , 2 ) , i ) ;
			}

			_iterChop.getAvEditFSM().addObserver( this ) ;

			// MFO
			// I think this is implemented in a different way in Gemini ot-2000B.12.
			try
			{
				TpeManager.get( _iterChop ).reset( _iterChop ) ;
			}
			catch( NullPointerException exception )
			{
				// ignore
			}
		}
	}
}
