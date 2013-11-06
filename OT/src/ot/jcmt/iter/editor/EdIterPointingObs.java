/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot.jcmt.iter.editor ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JMenuItem ;

import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetWatcher ;
import jsky.app.ot.gui.OptionWidgetExt ;
import jsky.app.ot.gui.OptionWidgetWatcher ;

import gemini.sp.SpItem ;
import gemini.sp.obsComp.SpInstObsComp ;
import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.iter.SpIterPointingObs ;

/**
 * This is the editor for Pointing Observe Mode iterator component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterPointingObs extends EdIterJCMTGeneric implements CommandButtonWidgetWatcher , OptionWidgetWatcher , ActionListener, ItemListener
{
	private IterPointingObsGUI _w ; // the GUI layout panel
	private SpIterPointingObs _iterObs ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterPointingObs()
	{
		super( new IterPointingObsGUI() ) ;

		_title = "Pointing" ;
		_presSource = _w = ( IterPointingObsGUI )super._w ;
		_description = "Pointing Observation Mode" ;

		_w.pointingPixelButton.addWatcher( this ) ;

		_w.automaticTarget.setToolTipText( "Automatically determine pointing target at time of observation" ) ;

		_w.frequencyPanel.setVisible( false ) ;
		_w.switchingModeLabel.setVisible( false ) ;
		_w.switchingMode.setVisible( false ) ;

                _w.fts2_in_beam.addItemListener(this);
                _w.pol2_in_beam.addItemListener(this);
	}

	/**
	 * Override setup to store away a reference to the Pointing Iterator.
	 */
	public void setup( SpItem spItem )
	{
		_iterObs = ( SpIterPointingObs )spItem ;
		super.setup( spItem ) ;
	}

	protected void _updateWidgets()
	{
		super._updateWidgets() ;

                _w.fts2_in_beam.setSelected(_iterObs.isFts2InBeam());
                _w.pol2_in_beam.setSelected(_iterObs.isPol2InBeam());
	}

	public void commandButtonAction( CommandButtonWidgetExt cbwe )
	{
		_w.pointingPixelPopupMenu.show( _w.pointingPixelButton , 0 , 0 ) ;
	}

	public void actionPerformed( ActionEvent e )
	{
		if( e.getSource() instanceof JMenuItem )
		{
			_w.pointingPixelButton.setText( ( ( JMenuItem )( e.getSource() ) ).getText() ) ;
			_iterObs.setPointingPixel( ( ( JMenuItem )( e.getSource() ) ).getText() ) ;
		}
	}

	public void optionAction( OptionWidgetExt owe )
	{
		_iterObs.setSpectralMode( owe.getActionCommand() ) ;
	}

	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		super.setInstrument( spInstObsComp ) ;

		if( ( spInstObsComp != null ) && ( spInstObsComp instanceof SpInstHeterodyne ) )
		{
			_w.switchingMode.setVisible( false ) ;
			_w.switchingModeLabel.setVisible( false ) ;
			_w.acsisPanel.setVisible( false ) ;

                        _w.fts2_in_beam.setVisible(false);
                        _w.pol2_in_beam.setVisible(false);
		}
		else
		{
			_w.acsisPanel.setVisible( false ) ;
                        _w.fts2_in_beam.setVisible(true);
                        _w.pol2_in_beam.setVisible(true);

		}
	}

        public void itemStateChanged(ItemEvent e) {
                if (e.getSource() == _w.pol2_in_beam) {
                        _iterObs.setPol2InBeam(_w.pol2_in_beam.isSelected());
                }
                else if (e.getSource() == _w.fts2_in_beam) {
                        _iterObs.setFts2InBeam(_w.fts2_in_beam.isSelected());
                }
        }
}
