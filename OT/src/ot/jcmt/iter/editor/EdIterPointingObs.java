/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.iter.editor ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

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
public final class EdIterPointingObs extends EdIterJCMTGeneric implements CommandButtonWidgetWatcher , OptionWidgetWatcher , ActionListener
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
		}
		else
		{
			_w.acsisPanel.setVisible( false ) ;
		}
	}
}
