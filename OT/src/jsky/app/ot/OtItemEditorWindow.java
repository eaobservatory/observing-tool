// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot ;

import java.awt.Frame ;
import java.awt.event.MouseAdapter ;
import java.awt.event.MouseEvent ;
import java.util.Observable ;
import java.util.Observer ;
import javax.swing.JFrame ;
import javax.swing.JPanel ;
import javax.swing.JButton ;
import jsky.app.ot.editor.OtItemEditor ;
import jsky.app.ot.editor.OtItemEditorFactory ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetWatcher ;
import gemini.sp.SpAvEditState ;
import gemini.sp.SpItem ;
import gemini.util.Assert ;

/**
 * This class presents the window in which specific item editors are
 * embedded.  Its GUI contains a PresentationWidget in which the form
 * used to edit the attributes of items is displayed.  It delegates
 * behavior to an associated ot.editor.OtItemEditor class that uses
 * the embeded _content presentation and converts between the screen
 * representation of the item's attributes and values and the real
 * SpItem attributes and values.
 *
 * <p>
 * The editor window is an Observer of SpAvEditState.  When an item's
 * attributes are modified, the "edit pencil" is displayed and the
 * "Undo" button is enabled.
 *
 * @see OtItemEditor
 */
@SuppressWarnings( "serial" )
public class OtItemEditorWindow extends ItemEditorGUI implements Observer
{
	/** The top level parent frame. */
	private JFrame _parent;

	private OtWindow _otWindow ;
	private SpItem _curItem ;
	private ProgramInfo _progInfo ;
	private JPanel _content ;
	private OtItemEditor _editor ;

	/**
	 * Default constructor.
	 */
	public OtItemEditorWindow()
	{
		_undoButton.addWatcher( new CommandButtonWidgetWatcher()
		{
			public void commandButtonAction( CommandButtonWidgetExt cbwe )
			{
				undo() ;
			}
		} ) ;

		_closeButton.addWatcher( new CommandButtonWidgetWatcher()
		{
			public void commandButtonAction( CommandButtonWidgetExt cbwe )
			{
				close() ;
			}
		} ) ;

		_showEditPencil.addMouseListener( new MouseAdapter()
		{
			public void mousePressed( MouseEvent e )
			{
				// If pencil is showing, we must be in the process of editing something.
				Assert.notNull( _curItem ) ;
				// Show the widgets as they were before editing
				undo() ;
				_showEditPencil.setIcon( _pencilDownIcon ) ;
			}

			public void mouseReleased( MouseEvent e )
			{
				// If pencil is showing, we must be in the process of editing something.
				Assert.notNull( _curItem ) ;
				// Show the widgets as they are now
				undo() ;
				_showEditPencil.setIcon( _pencilIcon ) ;
			}
		} ) ;
	}

	/** Called when the window is closed */
	public void close()
	{
		if( _progInfo.online )
			save( _curItem ) ; // Save any changes

		if( _otWindow != null )
			_otWindow.close() ;
	}

	/**
	 * Initialize the title, description, and embedded presentation from
	 * information taken from the given OtItemEditor.
	 */
	public void reinit( OtItemEditor ed )
	{
		if( ed == null )
			return ;

		// Set the title displayed in the large, italic font at the top of the window.
		setTitle( ed.getTitle() ) ;

		// Set the description of the item that is displayed
		_descriptionBox.setText( ed.getDescription() ) ;

		// Get the content presentation
		_content = ed.getPresSource() ;

		// Set the content
		_contentPresentation.removeAll() ;
		_contentPresentation.add( "Center" , _content ) ;
		_contentPresentation.revalidate() ;
		if( _parent != null )
			_parent.repaint() ;

		_editor = ed ;
		_editor.setPresentation( _content ) ;
		_editor.setOtItemEditorWindow( this ) ;
		_editor.setDescriptionWidget( _descriptionBox ) ;
	}

	/** Set the title (tab label) for the given component */
	public void setTitle( String title )
	{
		_border.setTitle( title ) ;
	}

	/**
	 * Initialize the buttons based upon the state of the AV table being
	 * edited.
	 */
	private void _reinitState( int state )
	{
		switch( state )
		{
			case SpAvEditState.UNEDITED :
				_undoButton.setText( "Undo" ) ;
				_undoButton.setEnabled( false ) ;
				_showEditPencil.setVisible( false ) ;
				break ;

			case SpAvEditState.EDITED :
				_undoButton.setText( "Undo" ) ;
				_undoButton.setEnabled( true ) ;
				_showEditPencil.setVisible( true ) ;
				break ;

			case SpAvEditState.EDIT_UNDONE :
				_undoButton.setText( "Redo" ) ;
				_undoButton.setEnabled( true ) ;
				_showEditPencil.setVisible( true ) ;
				break ;
		}
	}

	/**
	 * Set the ProgramInfo variable.  This structure contains global information
	 * about the program as a whole and is needed to send changes to the ODB.
	 */
	public void setInfo( ProgramInfo pi )
	{
		_progInfo = pi ;
	}

	/**
	 * Get the item being edited.
	 */
	public SpItem getItem()
	{
		return _curItem ;
	}

	/**
	 * Set the item being edited.  The OtItemEditor that is associated with
	 * the class of item represented by the spItem is discovered from the
	 * spItem's client data and used to reinitialize the editor window.
	 * In addition, the editor is told to initialize its widgets to reflect
	 * the values of the attributes in the spItem.
	 */
	public void setItem( SpItem spItem )
	{
		if( _curItem != null )
		{
			if( _progInfo.online )
				save( _curItem ) ; // Save changes to the item before editing the new one

			if( ( _editor != null ) && ( _curItem != spItem ) )
				_editor.cleanup() ;

			_curItem.getAvEditFSM().deleteObserver( this ) ;
		}

		// Get the appropriate editor for the spItem
		OtClientData cd = ( OtClientData )spItem.getClientData() ;
		Assert.notNull( cd ) ;
		Assert.notNull( cd.itemEditorClass ) ;
		OtItemEditor ed = OtItemEditorFactory.getEditor( cd.itemEditorClass , spItem ) ;

		_curItem = spItem ;

		// Will only add if not already an observer.
		_curItem.getAvEditFSM().addObserver( this ) ;

		// Show the right presentation, title and description.
		reinit( ed ) ;

		// Reset the "edit pencil" and undo buttons depending upon whether the
		// item has been edited.
		_reinitState( _curItem.getAvEditState() ) ;

		// Tell the editor to show the values of this item.
		if( _editor != null )
			_editor.setup( spItem ) ;
	}

	/**
	 * Receive an update from either an SpAvEditState machine or the SpItem
	 * itself.  This method implements the <tt>update</tt> method from the
	 * Observer interface.  Updates are sent when the state machine changes
	 * state or when the SpItem's table is replaced.
	 *
	 * @param o   the SpAvEditState of the item being edited
	 * @param arg ignored
	 */
	public void update( Observable o , Object arg )
	{
		if( o instanceof SpAvEditState )
		{
			SpAvEditState fsm = ( SpAvEditState )o ;
			if( fsm == _curItem.getAvEditFSM() )
				_reinitState( fsm.getState() ) ;
		}
	}

	/**
	 * Forget any edits that have been made.
	 */
	public void undo()
	{
		_curItem.getAvEditFSM().undo() ;
		_editor.update() ;
	}

	//
	// See whether the current item has been edited.
	//
	private boolean _isEdited( SpItem spItem )
	{
		if( spItem == null )
			return false ;

		return !( spItem.getAvEditState() == SpAvEditState.UNEDITED ) ;
	}

	/**
	 * Save any changes that have been made to a given item.
	 */
	public void save( SpItem spItem )
	{
		// Nothing to do
		if( !_isEdited( spItem ) )
			return ;

		spItem.getAvEditFSM().save() ;
	}

	/**
	 * Hide the window.
	 */
	public void setVisible( boolean visible )
	{
		_parent.setVisible( visible ) ;
		if( !visible && _editor != null )
				_editor.cleanup() ;
	}

	public boolean isVisible()
	{
		return _parent != null && _parent.isVisible() ;
	}

	public void toFront() {
		_parent.setState(Frame.NORMAL);
	}

	/** Return the top level parent frame used to close the window */
	public JFrame getParentFrame() {
		return _parent;
	}

	/** Set the top level parent frame used to close the window */
	public void setParentFrame(JFrame p) {
		_parent = p;
	}

	/** Set a reference to the parent panel. */
	public void setOtWindow( OtWindow w )
	{
		_otWindow = w ;
	}

	/**
	 * Get the "resizable" property.  If the editor subclass is resizable,
	 * then it should reset _resizable to true.  Otherwise, the default (false)
	 * is returned.
	 */
	public boolean isResizable()
	{
		// XXX allan return _resizable ; 
		return true ;
	}

	/**
	 * Set the "resizable" property.  
	 */
	public void setResizable( boolean b ){}

	// Needed for survey component editor, added by MFO, January 08, 2003
	public JButton getUndoButton()
	{
		return _undoButton ;
	}

	// Needed for survey component editor, added by MFO, January 08, 2003
	public JButton getShowEditPencilButton()
	{
		return _showEditPencil ;
	}
}
