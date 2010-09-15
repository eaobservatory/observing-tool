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

import jsky.app.ot.gui.CheckBoxWidgetExt ;
import java.awt.GridLayout ;

/**
 * Cloned from Skydip
 */
@SuppressWarnings( "serial" )
public class IterSetupObsGUI extends IterJCMTGenericGUI
{
	CheckBoxWidgetExt currentAzimuth = new CheckBoxWidgetExt() ;
	GridLayout layout = new GridLayout( 1 , 1 ) ;

	public IterSetupObsGUI()
	{
		try
		{
			jbInit() ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}
	}

	private void jbInit() throws Exception
	{
		currentAzimuth.setText( "Do setup at current telescope location ?" ) ;
		currentAzimuth.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		this.setLayout( layout ) ;
		this.add( currentAzimuth ) ;
	}
}
