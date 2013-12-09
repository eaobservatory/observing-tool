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
package jsky.app.ot.gui ;

import java.awt.Dimension ;
import java.awt.Font ;
import java.awt.FontMetrics ;
import java.awt.event.ItemListener ;
import java.awt.event.ItemEvent ;
import java.util.Enumeration ;
import java.util.Vector ;
import javax.swing.ImageIcon ;
import javax.swing.JToggleButton ;
import javax.swing.border.BevelBorder ;

/**
 * A "description" property is added.  It serves the same purpose as
 * "tip" but is shown on demand by clients of this widget rather than
 * anytime the mouse rests over the button.  A "resizeToContent" method
 * causes the button to resize itself to exactly enclose its contents.
 *
 * @author	Shane Walker, Dayle Kotturi, Allan Brighton (port to Swing)
 */
@SuppressWarnings( "serial" )
public class ToggleButtonWidget extends JToggleButton implements DescriptiveWidget , ItemListener
{
	private final static int _PADX = 2 ;
	private final static int _PADY = 2 ;

	/** Set to true if the button is depressed */
	protected boolean booleanValue = false ;

	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description ;

	// The list of watchers.
	private Vector<ToggleButtonWidgetWatcher> _watchers = new Vector<ToggleButtonWidgetWatcher>() ;

	/** If true, multiple buttons may be selected, otherwise only one */
	private boolean enableMultipleSelection ;

	/** The default constructor. */
	public ToggleButtonWidget( boolean enableMultipleSelection )
	{
		super() ;
		init( enableMultipleSelection ) ;
	}

	/** Constructor with label. */
	public ToggleButtonWidget( String label , boolean enableMultipleSelection )
	{
		super( label ) ;
		init( enableMultipleSelection ) ;
	}

	/** Constructor with icon. */
	public ToggleButtonWidget( ImageIcon icon , boolean enableMultipleSelection )
	{
		super( icon ) ;
		init( enableMultipleSelection ) ;
	}

	/** Constructor with label and mode (mode ignored here). */
	public ToggleButtonWidget( String label , int mode , boolean enableMultipleSelection )
	{
		super( label ) ;
		init( enableMultipleSelection ) ;
	}

	/** Initialize the button */
	protected void init( boolean enableMultipleSelection )
	{
		this.enableMultipleSelection = enableMultipleSelection ;
		addItemListener( this ) ;
		setFocusPainted( false ) ;
		setFont( getFont().deriveFont( Font.PLAIN ) ) ;
		setBorder( new BevelBorder( BevelBorder.RAISED ) ) ;
	}

	public void itemStateChanged( ItemEvent e )
	{
		ToggleButtonWidget selectedButton = ( ToggleButtonWidget )e.getItem() ;
		if( e.getStateChange() == ItemEvent.SELECTED )
		{
			selectedButton.setBorder( new BevelBorder( BevelBorder.LOWERED ) ) ;
			booleanValue = true ;
			action() ;
		}
		else
		{
			selectedButton.setBorder( new BevelBorder( BevelBorder.RAISED ) ) ;
			booleanValue = false ;
			if( ToggleButtonWidget.this.enableMultipleSelection )
				action() ;
		}
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

	/** Return true if the button is selected */
	public boolean getBooleanValue()
	{
		return booleanValue ;
	}

	/**
	 * Add a watcher.  Watchers are notified when an item is selected.
	 */
	public synchronized final void addWatcher( ToggleButtonWidgetWatcher watcher )
	{
		if( !_watchers.contains( watcher ) )
			_watchers.addElement( watcher ) ;
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized final void deleteWatcher( ToggleButtonWidgetWatcher watcher )
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

	/**
	 * Notify watchers of an action event.
	 */
	public synchronized void action()
	{
		Enumeration<ToggleButtonWidgetWatcher> e = _watchers.elements() ;
		while( e.hasMoreElements() )
		{
			ToggleButtonWidgetWatcher watcher = e.nextElement() ;
			watcher.toggleButtonAction( this ) ;
		}
	}

	/**
	 * Programatically press the button.  The button will repaint itself
	 * and notify its watchers that it has been pressed.  However, no
	 * ACTION or other events are fired.
	 *
	 */
	public void press()
	{
		if( isEnabled() )
			doClick() ;
	}

	/**
	 * Resize the button to fit its contents.
	 */
	public void resizeToContent()
	{
		int imgW = 0 , imgH = 0 , labW = 0 , labH = 0 ;
		boolean havePic = false , haveText = false ;

		if( getIcon() != null )
		{
			imgW = Math.max( getIcon().getIconWidth() , 0 ) ;
			imgH = Math.max( getIcon().getIconHeight() , 0 ) ;
			havePic = true ;
		}

		if( ( getText() != null ) && ( !getText().equals( "" ) ) )
		{
			FontMetrics fm = getFontMetrics( getFont() ) ;
			labW = fm.stringWidth( getText() ) ;
			labH = fm.getHeight() ;
			haveText = true ;
		}

		int w = 0 , h = 0 ;

		if( havePic && !haveText )
		{
			w = imgW ;
			h = imgH ;
		}
		else if( havePic && haveText )
		{
			w = Math.max( imgW , labW ) ;
			if( imgH > 0 )
				h = imgH + _PADY + labH ;
			else
				h = labH ;
		}
		else
		{
			w = labW ;
			h = labH ;
		}

		w += _PADX + _PADX ;
		h += _PADY + _PADY ;

		setPreferredSize( new Dimension( w , h ) ) ;
	}
}
