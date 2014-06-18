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

import java.util.Observer ;
import java.util.Observable ;

import javax.swing.AbstractButton ;
import javax.swing.JLabel ;
import javax.swing.JOptionPane ;
import javax.swing.JComboBox ;

import jsky.app.ot.OtCfg ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetWatcher ;

import gemini.sp.SpAvEditState ;
import gemini.sp.SpItem ;
import gemini.sp.SpObs ;
import orac.util.OracUtilities ;
import orac.util.TelescopeUtil ;

import ot.editor.EdMsbObsCommon;

/**
 * This is the editor for the Observation item.
 */
public final class EdObservation extends EdMsbObsCommon implements TextBoxWidgetWatcher, CheckBoxWidgetWatcher, Observer, ActionListener
{
	private TextBoxWidgetExt _obsTitle ;
	private JLabel _obsState ;
	private ObsGUI _w ; // the GUI layout panel

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdObservation()
	{
		_title = "Observation" ;
		_presSource = _w = new ObsGUI() ;
		_description = "The observation is the smallest entity that can be scheduled." ;

		_obsTitle = _w.nameBox;
		_obsTitle.addWatcher( this ) ;

		_obsState = _w.obsState ;

		_w.jComboBox1.addActionListener( this ) ;

		_w.optional.addWatcher( this ) ;
		_w.standard.addWatcher( this ) ;

		// Added by MFO (22 February 2002)
		if( !OtCfg.telescopeUtil.supports( TelescopeUtil.FEATURE_FLAG_AS_STANDARD ) )
		{
			_w.standard.setText( "Flag as Calibration" ) ;
			_w.optional.setVisible( false ) ;
			_w.optional.deleteWatcher( this ) ;
		}

		_w.addRemainingCountListener(this);

		_w.obsStateLabel.setVisible( false ) ;
		_w.obsState.setVisible( false ) ;
	}

	/**
	 * Do any (one time) initialization.
	 */
	protected void _init(){}

	/**
	 * The method is called when a new item is edited.  Reset the item being
	 * observed.
	 */
	public void setup( SpItem spItem )
	{
		if( _spItem != null )
			_spItem.getAvEditFSM().deleteObserver( this ) ;

		super.setup( spItem ) ;
		_spItem.getAvEditFSM().addObserver( this ) ;
	}

	/**
	 * The method is called when a new item is no longer being edited.
	 * Remove ourselves as an observer.
	 */
	public void cleanup( SpItem spItem )
	{
		if( _spItem != null )
			_spItem.getAvEditFSM().deleteObserver( this ) ;

		super.cleanup() ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		// Show the title
		String title = _spItem.getTitleAttr() ;
		if( title != null )
			_obsTitle.setText( title ) ;
		else
			_obsTitle.setText( "" ) ;

		String state = _avTab.get( "state" ) ;
		if( state == null )
			_obsState.setText( "Not in Active Database" ) ;
		else
			_obsState.setText( state ) ;

		ignoreActions = true ; // MFO

		SpObs obs = ( SpObs )_spItem ;
		
		// Set the priority
		int pri = obs.getPriority() ;
		_w.jComboBox1.setSelectedIndex( pri - 1 ) ;

		// Set whether or not this is a standard
		_w.standard.setSelected( obs.getIsStandard() ) ;

		// Added for OMP (MFO, 7 August 2001)
		_w.optional.setValue( obs.isOptional() ) ;

		int numberRemaining = obs.getNumberRemaining();
		_w.setRemainingCount(numberRemaining);

		_w.unSuspendCB.addActionListener( this ) ;

		ignoreActions = false ;

		_w.estimatedTime.setText( OracUtilities.secsToHHMMSS( obs.getElapsedTime() , 1 ) ) ;

		_updateMsbDisplay() ;
	}

	/**
	 * Checks whether SpObs is an MSB and updates display
	 * accordingly.
	 *
	 * Added for OMP. (MFO, 5 March 2002)
	 *
	 */
	protected void _updateMsbDisplay()
	{
		ignoreActions = true ;
		SpObs obs = ( SpObs )_spItem ;
		if( obs.isMSB() )
		{
			_w.msbPanel.setVisible( true ) ;
			_w.optional.setVisible( false ) ;
			_w.unSuspendCB.setVisible( obs.isSuspended() ) ;
		}
		else
		{
			_w.msbPanel.setVisible( false ) ;

			if( OtCfg.telescopeUtil.supports( TelescopeUtil.FEATURE_FLAG_AS_STANDARD ) )
				_w.optional.setVisible( true ) ;
		}
		ignoreActions = false ;
	}

	/**
	 * Watch changes to the title text box.
	 * @see TextBoxWidgetWatcher
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		if( tbwe == _obsTitle )
			_spItem.setTitleAttr( _obsTitle.getText().trim() ) ;
	}

	/**
	 * Text box action, ignore.
	 * @see TextBoxWidgetWatcher
	 */
	public void textBoxAction( TextBoxWidgetExt tbwe ){}

	/**
	 * Observer of attribute/value table changes.  Observing to notice
	 * changes to the "chainedToNext" state.
	 */
	public void update( Observable o , Object arg )
	{
		if( o instanceof SpAvEditState )
		{
			// Set whether or not this is a standard
			_w.standard.setSelected( (( SpObs )_spItem).getIsStandard() ) ;
	
			_updateMsbDisplay() ; // MFO
		}
	}

	/**
	 * Handle standard action events.
	 */
	public void actionPerformed( ActionEvent evt )
	{
		if( ignoreActions )
			return ;

		Object w = evt.getSource() ;
		SpObs spObs = ( SpObs )_spItem ;

		if( w instanceof JComboBox )
			spObs.setPriority( ( Integer )_w.jComboBox1.getSelectedItem() ) ;
		else if( ( w instanceof AbstractButton ) && !(( AbstractButton )w).isSelected() )
			return ;

		if( w == _w.unSuspendCB )
		{
			String message = "This is an Irreversible Operation" + "\n" + "Are you sure you want to proceed?" ;
			int option = JOptionPane.showConfirmDialog( _w , message , "Irreversible Operation" , JOptionPane.YES_NO_OPTION , JOptionPane.WARNING_MESSAGE ) ;
			if( option == JOptionPane.NO_OPTION )
				return ;

			spObs.unSuspend() ;
			_w.unSuspendCB.setVisible( false ) ;
		}
	}

	public void checkBoxAction( CheckBoxWidgetExt checkBoxWidgetExt )
	{
		if( checkBoxWidgetExt == _w.optional )
		{
			(( SpObs )_spItem).setOptional( _w.optional.isSelected() ) ;
		}
		else if( checkBoxWidgetExt == _w.standard )
		{
			// We can onlt do this if the observation is inside an MSB
			SpItem parent = _spItem.parent() ;
			while( parent != null )
			{
				if( parent instanceof gemini.sp.SpMSB )
				{
					_spItem.getAvEditFSM().deleteObserver( this ) ;
					( ( SpObs )_spItem ).setIsStandard( _w.standard.getBooleanValue() ) ;
					_spItem.getAvEditFSM().addObserver( this ) ;

					_w.standard.setValue( ( ( SpObs )_spItem ).getIsStandard() ) ;

					if( !OtCfg.telescopeUtil.supports( TelescopeUtil.FEATURE_FLAG_AS_STANDARD ) )
					{
						_w.optional.setValue( _w.standard.getBooleanValue() ) ;
						( ( SpObs )_spItem ).setOptional( _w.standard.getBooleanValue() ) ;
					}
					return ;
				}
				else
				{
					parent = parent.parent() ;
				}
			}
			// If we get to here, then this is not inside an MSB so we can't do this
			JOptionPane.showMessageDialog( null , "This can only be flagged as a calibration if it is inside an MSB." , "Operation not valid here" , JOptionPane.ERROR_MESSAGE ) ;
		}
	}
}
