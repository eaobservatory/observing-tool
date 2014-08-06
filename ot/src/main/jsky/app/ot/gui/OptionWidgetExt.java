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
/**
 * This class watches a OptionWidgetExt object to know which node is selected.
 *
 * @author      Dayle Kotturi, Shane Walker, Allan Brighton (Swing port)
 * @version     $Version$
 */

package jsky.app.ot.gui ;

import java.util.Vector ;
import javax.swing.JRadioButton ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

/**
 * An OptionWidget that permits clients to register as button press watchers.
 */
@SuppressWarnings( "serial" )
public class OptionWidgetExt extends JRadioButton implements DescriptiveWidget , ActionListener
{
	// Observers
	private Vector<OptionWidgetWatcher> _watchers = new Vector<OptionWidgetWatcher>() ;

	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description ;

	/** Default constructor */
	public OptionWidgetExt()
	{
		addActionListener( this ) ;
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
	 * Add a watcher.  Watchers are notified when a button is pressed in the
	 * option widget.
	 */
	public synchronized final void addWatcher( OptionWidgetWatcher ow )
	{
		if( !_watchers.contains( ow ) )
			_watchers.addElement( ow ) ;
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized final void deleteWatcher( OptionWidgetWatcher ow )
	{
		_watchers.removeElement( ow ) ;
	}

	/**
	 * Delegate this method from the Observable interface.
	 */
	public synchronized final void deleteWidgetWatchers()
	{
		_watchers.removeAllElements() ;
	}

	//
	// Notify watchers that a button has been pressed in the option widget.
	//
	private void _notifyAction()
	{
		for( int i = 0 ; i < _watchers.size() ; ++i )
		{
			OptionWidgetWatcher ow = _watchers.elementAt( i ) ;
			ow.optionAction( this ) ;
		}
	}

	/** Called when the button is pressed */
	public void actionPerformed( ActionEvent ae )
	{
		_notifyAction() ;
	}

	public void setValue( boolean value )
	{
		setSelected( value ) ;
	}

	public boolean getValue()
	{
		return isSelected() ;
	}
}
