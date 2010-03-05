// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//

package jsky.app.ot ;

import java.awt.Font ;
import java.util.Observable ;
import java.util.Observer ;
import jsky.app.ot.gui.MultiSelTreeNodeWidget ;
import gemini.sp.SpAvEditState ;
import gemini.sp.SpItem ;
import gemini.util.Assert ;

/**
 * A MultiSelTreeNodeWidget that contains an SpItem.  OtTreeNodeWidget
 * not only maintains a reference to an associated SpItem, but also provides
 * support for dragging and dropping items in the Science Program tree.
 */
@SuppressWarnings( "serial" )
public abstract class OtTreeNodeWidget extends MultiSelTreeNodeWidget implements Observer , OtGuiAttributes
{
	/** The font for unedited items.  */
	public static final Font DEFAULT_FONT = new Font( "Dialog" , Font.PLAIN , 12 ) ;

	/** The font for edited items.  */
	public static final Font EDITED_FONT = new Font( "Dialog" , Font.ITALIC , 12 ) ;

	/** The SpItem associated one-to-one with this tree node widget.  */
	protected SpItem _spItem = null ;

	/** Constructor */
	public OtTreeNodeWidget( OtTreeWidget tree )
	{
		super( tree ) ;
	}

	/** Constructor */
	public OtTreeNodeWidget(){}

	/**
	 * Copy the tree node widget.  Each subclass must implement this routine.
	 * Maybe someday org.freebongo.gui.Widget.clone() will work and this won't be
	 * needed anymore...
	 */
	public abstract OtTreeNodeWidget copy() ;

	/**
	 * Set the Science Program item to edit.  This method should be
	 * called once, before the object is used.
	 */
	public void setItem( SpItem spItem )
	{
		Assert.notFalse( _spItem == null ) ;

		_spItem = spItem ;
		_spItem.getAvEditFSM().addObserver( this ) ;
		update( _spItem.getAvEditFSM() , null ) ;
	}

	/**
	 * Get the item represented by this tree node.
	 */
	public SpItem getItem()
	{
		return _spItem ;
	}

	/**
	 * Override collapse(boolean) to store the "collapsed" state in the
	 * associated SpItem.  This information is used when a program is loaded
	 * from disk or fetched from the database in order to display the tree
	 * as it was when last saved.
	 */
	public void setCollapsed( boolean collapsed )
	{
		super.setCollapsed( collapsed ) ;

		if( _spItem != null )
			_spItem.getTable().set( GUI_COLLAPSED , collapsed ) ;
	}

	/**
	 * Has the associated SpItem been edited?
	 */
	public boolean isEdited()
	{
		return( _spItem.getAvEditState() == SpAvEditState.EDITED ) ;
	}

	/**
	 * Implements the update method from the Observer interface in order to
	 * show whether the associated SpItem has been edited.
	 */
	public void update( Observable o , Object arg )
	{
		SpAvEditState fsm = ( SpAvEditState )o ;
		if( fsm.getState() == SpAvEditState.EDITED )
			setFont( EDITED_FONT ) ;
		else
			setFont( DEFAULT_FONT ) ;

		// See if there's a new title
		String title = _spItem.getTitle() ;
		if( getText() != title )
			setText( title ) ;
	}
}
