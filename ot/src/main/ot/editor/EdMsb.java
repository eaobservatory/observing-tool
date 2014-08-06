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
package ot.editor ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

import javax.swing.JComboBox ;
import javax.swing.JOptionPane ;

import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;
import gemini.sp.SpMSB ;
import gemini.sp.SpSurveyContainer ;
import orac.util.OracUtilities ;

import ot.editor.MsbObsCommonGUI;
import ot.editor.EdMsbObsCommon;

/**
 * MSB folder editor.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on jsky/app/ot/editor/EdTitle.java
 */
public final class EdMsb extends EdMsbObsCommon implements TextBoxWidgetWatcher, ActionListener, MsbObsCommonGUI.RemainingCountListener
{
	private MsbEditorGUI _w ; // the GUI layout

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdMsb()
	{
		_title = "MSB Editor" ;
		_presSource = _w = new MsbEditorGUI() ;
		_description = "MSB information." ;

		_w.jComboBox1.addActionListener( this ) ;

		_w.addRemainingCountListener(this);
		_w.unSuspendCB.addActionListener( this ) ;
	}

	/**
	 * Do any (one time) initialization.
	 */
	protected void _init() {
		TextBoxWidgetExt tbw = _w.nameBox;
		tbw.addWatcher(this);

		if (_spItem != null) {
			if (_spItem.parent() instanceof SpSurveyContainer) {
				_w.setRemainingCountEnabled(false);
				_w.jComboBox1.setEnabled(false);
			}
		}
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		if( _spItem != null )
		{
			boolean enable = !( _spItem.parent() instanceof SpSurveyContainer ) ;
			_w.setRemainingCountEnabled(enable);
			_w.jComboBox1.setEnabled( enable ) ;
		}
		// Show the title
		TextBoxWidgetExt tbw = _w.nameBox ;
		String title = _spItem.getTitleAttr() ;
		if( title != null )
			tbw.setText( title ) ;
		else
			tbw.setText( "" ) ;

		// Set the priority
		int pri = ( ( SpMSB )_spItem ).getPriority() ;
		_w.jComboBox1.setSelectedIndex( pri - 1 ) ;

		ignoreActions = true ;

		int numberRemaining = ( ( SpMSB )_spItem ).getNumberRemaining() ;
		_w.setRemainingCount(numberRemaining);

		_w.unSuspendCB.setVisible( ( ( SpMSB )_spItem ).isSuspended() ) ;

		ignoreActions = false ;

		// Note that the elapsed time is re-calculated every time _updateWidgets is called.
		// It will not be written to or read from the SpMSB item until the Science Program
		// is saved to disk or stored to database.
		// Then OracUtilities.set
		_w.estimatedTime.setText( OracUtilities.secsToHHMMSS( ( ( SpMSB )_spItem ).getElapsedTime() , 1 ) ) ;
		_w.totalTime.setText( OracUtilities.secsToHHMMSS( ( ( SpMSB )_spItem ).getTotalTime() , 1 ) ) ;
	}

	/**
	 * Watch changes to the title text box.
	 * @see TextBoxWidgetWatcher
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbw )
	{
		_spItem.setTitleAttr( tbw.getText().trim() ) ;
	}

	/**
	 * Text box action, ignored.
	 * @see TextBoxWidgetWatcher
	 */
	public void textBoxAction( TextBoxWidgetExt tbw ){}

	public void actionPerformed(ActionEvent evt) {
		if (ignoreActions) {
			return;
		}

		Object w = evt.getSource();
		SpMSB spMSB = ( SpMSB )_spItem;

		if (w instanceof JComboBox) {
			Object value = _w.jComboBox1.getSelectedItem();

			if (value != null && value instanceof Integer) {
				spMSB.setPriority((Integer) value);
			}
			else {
				spMSB.setPriority(SpMSB.PRIORITY_LOW);
			}
		}

		if (w == _w.unSuspendCB) {
			int option = JOptionPane.showConfirmDialog(_w, 
				"This is an Irreversible Operation" 
				+ "\n" + "Are you sure you want to proceed?",
				"Irreversible Operation", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);

			if (option == JOptionPane.NO_OPTION) {
				return;
			}

			spMSB.unSuspend();
			_w.unSuspendCB.setVisible(false);
			return;
		}
	}
}
