// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe ;

import java.awt.Cursor ;
import java.util.Vector ;
import java.util.Hashtable ;
import java.util.Enumeration ;
import jsky.app.ot.gui.ToggleButtonWidget ;
import jsky.app.ot.gui.ToggleButtonWidgetWatcher ;

/**
 * A helper object to keep up with which tool has been selected and to
 * simplify selecting tools.  This logically belongs with the
 * TelescopePosEditor code but because of its length, its been moved
 * to a separate file.
 */
final class TpeEditorTools
{
	static final int MODE_BROWSE = 0 ;
	static final int MODE_DRAG = 1 ;
	static final int MODE_ERASE = 2 ;
	static final int MODE_CREATE = 3 ;
	private int _curMode = MODE_BROWSE ;
	private TpeImageFeature _curFeature = null ;
	private TelescopePosEditor _tpe ;
	private TelescopePosEditorToolBar _tpeToolBar ;
	private ToggleButtonWidget _browseButton ;
	private Hashtable<String,ToggleButtonWidget> _buttonMap = new Hashtable<String,ToggleButtonWidget>() ;

	/** Create with the Presentation that contains the tool buttons. */
	TpeEditorTools( TelescopePosEditor tpe )
	{
		_tpe = tpe ;
		_tpeToolBar = _tpe.getTpeToolBar() ;

		ToggleButtonWidget tbw ;

		// Browse Tool
		tbw = _tpeToolBar.getModeToggleButton("Browse");
		tbw.addWatcher( new ToggleButtonWidgetWatcher()
		{
			public void toggleButtonAction( ToggleButtonWidget tbw )
			{
				_tpe.setCurrentMouseCursor( java.awt.Cursor.getPredefinedCursor( java.awt.Cursor.DEFAULT_CURSOR ) ) ;
				_curMode = MODE_BROWSE ;
				_curFeature = null ;
			}
		} ) ;
		_browseButton = tbw ;

		// Drag Tool
		tbw = _tpeToolBar.getModeToggleButton("Drag");
		tbw.addWatcher( new ToggleButtonWidgetWatcher()
		{
			public void toggleButtonAction( ToggleButtonWidget tbw )
			{
				_tpe.setCurrentMouseCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) ) ;
				_curMode = MODE_DRAG ;
				_curFeature = null ;
			}
		} ) ;

		// Erase Tool
		tbw = _tpeToolBar.getModeToggleButton("Erase");
		tbw.addWatcher( new ToggleButtonWidgetWatcher()
		{
			public void toggleButtonAction( ToggleButtonWidget tbw )
			{
				_tpe.setCurrentMouseCursor( Cursor.getPredefinedCursor( Cursor.TEXT_CURSOR ) ) ;
				_curMode = MODE_ERASE ;
				_curFeature = null ;
			}
		} ) ;

		// Hide all the create tools.
		Enumeration<ToggleButtonWidget> e = _enumerateCreateTools() ;
		while( e.hasMoreElements() )
		{
			tbw = e.nextElement() ;
			tbw.setVisible( false ) ;
		}
	}

	//
	// Get an enumeration of the create tool buttons.
	//
	private Enumeration<ToggleButtonWidget> _enumerateCreateTools()
	{
		return new Enumeration<ToggleButtonWidget>()
		{ // XXX allan: should make 2 separate ToggleButtonPanels
			private int i = 4 ; // skip first 3 buttons (Browse, Drag, Erase) and empty label

			public boolean hasMoreElements()
			{
				try
				{
					return( _tpeToolBar.getModeToggleButton( i ) != null ) ;
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					return false ;
				}
			}

			public ToggleButtonWidget nextElement()
			{
				return _tpeToolBar.getModeToggleButton( i++ ) ;
			}
		} ;
	}

	//
	// Get an unoccupied tool button.
	//
	private ToggleButtonWidget _allocateToolButton()
	{
		// Find an open tool button
		ToggleButtonWidget tbw = null ;
		Enumeration<ToggleButtonWidget> e = _enumerateCreateTools() ;
		while( e.hasMoreElements() )
		{
			ToggleButtonWidget tmp = e.nextElement() ;
			if( !( tmp.isVisible() ) )
			{
				tbw = tmp ; // This one was occupied
				break ;
			}
		}
		return tbw ;
	}

	//
	// Make a tool button available for allocation again.
	//
	private void _freeToolButton( ToggleButtonWidget tbw )
	{
		tbw.setVisible( false ) ;
		tbw.deleteWatchers() ;
	}

	//
	// Add a creational tool.
	//
	private boolean _addCreateTool( String label , final TpeImageFeature tif )
	{
		// See if this tool is already present.
		if( _buttonMap.get( label ) != null )
			return true ;

		ToggleButtonWidget tbw = _allocateToolButton() ;
		if( tbw == null )
			return false ; // Never found a valid one

		_buttonMap.put( label , tbw ) ;

		tbw.addWatcher( new ToggleButtonWidgetWatcher()
		{
			public void toggleButtonAction( ToggleButtonWidget tbw )
			{
				_tpe.setCurrentMouseCursor( Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR ) ) ;
				_curMode = MODE_CREATE ;
				_curFeature = tif ;
			}
		} ) ;

		tbw.setText( label ) ;
		tbw.setVisible( true ) ;
		tbw.setEnabled( true ) ;
		return true ;
	}

	/**
	 * Add the create tools for the given feature.
	 */
	public void addCreateTools( TpeImageFeature tif )
	{
		if( !( tif instanceof TpeCreateableFeature ) )
			return ; // nothing to add

		String[] labels = ( ( TpeCreateableFeature )tif ).getCreateButtonLabels() ;
		for( int i = 0 ; i < labels.length ; ++i )
		{
			if( !_addCreateTool( labels[ i ] , tif ) )
				throw new RuntimeException( "No more create tool buttons available!" ) ;
		}
	}

	//
	// Delete a creational tool.
	//
	private void _deleteCreateTool( String label )
	{
		ToggleButtonWidget tbw ;
		tbw = _buttonMap.get( label ) ;
		if( tbw != null )
		{
			_freeToolButton( tbw ) ;
			_buttonMap.remove( label ) ;
		}
	}

	/**
	 * Delete the create tools for the given feature.
	 */
	public void deleteCreateTools( TpeImageFeature tif )
	{
		if( !( tif instanceof TpeCreateableFeature ) )
			return ; // nothing to delete

		String[] labels = ( ( TpeCreateableFeature )tif ).getCreateButtonLabels() ;
		for( int i = 0 ; i < labels.length ; ++i )
			_deleteCreateTool( labels[ i ] ) ;
	}

	//
	// Disable/enable the given creational tool.
	//
	private void _setDisabled( String label , boolean disabled )
	{
		ToggleButtonWidget tbw ;
		tbw = _buttonMap.get( label ) ;
		if( tbw != null )
			tbw.setEnabled( !disabled ) ;
	}

	/**
	 * Disable or enable the creational tools associated with the given
	 * image feature.
	 */
	public void setCreateToolsDisabled( TpeImageFeature tif , boolean disabled )
	{
		if( !( tif instanceof TpeCreateableFeature ) )
			return ; // nothing to disable

		String[] labels = ( ( TpeCreateableFeature )tif ).getCreateButtonLabels() ;
		for( int i = 0 ; i < labels.length ; ++i )
			_setDisabled( labels[ i ] , disabled ) ;
	}

	/**
	 * Disable or enable the set of creational tools associated with the
	 * given image features.
	 */
	public void setCreateToolsDisabled( Vector<TpeImageFeature> tifV , boolean disabled )
	{
		for( int i = 0 ; i < tifV.size() ; ++i )
		{
			TpeImageFeature tif = tifV.elementAt( i ) ;
			setCreateToolsDisabled( tif , disabled ) ;
		}
	}

	/**
	 * Get the current mode.  One of MODE_BROWSE, MODE_DRAG, MODE_ERASE, or
	 * MODE_CREATE.
	 */
	public int getMode()
	{
		return _curMode ;
	}

	/**
	 * Get the image feature.  This will be null if the mode is not MODE_CREATE.
	 * If MODE_CREATE, the image feature will be the one assoicated with the
	 * create button.
	 */
	public TpeImageFeature getImageFeature()
	{
		return _curFeature ;
	}

	/**
	 * Get the label of the button that is currently selected.
	 */
	public String getCurrentButtonLabel()
	{
		Enumeration<ToggleButtonWidget> e = _enumerateCreateTools() ;
		while( e.hasMoreElements() )
		{
			ToggleButtonWidget tbw = e.nextElement() ;
			if( tbw.getBooleanValue() )
				return tbw.getText() ;
		}
		return null ;
	}

	/**
	 * Go to browse mode.
	 */
	public void gotoBrowseMode()
	{
		_browseButton.press() ;
	}

	/**
	 * Reset the cursor (mouse indicator) for the current mode.
	 */
	public void resetCursor()
	{
		switch( _curMode )
		{
			case MODE_BROWSE :
				_tpe.setCurrentMouseCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) ) ;
				break ;
			case MODE_DRAG :
				_tpe.setCurrentMouseCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) ) ;
				break ;
			case MODE_ERASE :
				_tpe.setCurrentMouseCursor( Cursor.getPredefinedCursor( Cursor.TEXT_CURSOR ) ) ;
				break ;
			case MODE_CREATE :
				_tpe.setCurrentMouseCursor( Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR ) ) ;
				break ;
		}
	}

	/** Standard dubug string. */
	public String toString()
	{
		return getClass().getName() + "[tool=" + getCurrentButtonLabel() + "]" ;
	}
}
