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
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */
@SuppressWarnings( "serial" )
public class IterSkydipObsGUI extends IterJCMTGenericGUI
{
	CheckBoxWidgetExt currentAzimuth = new CheckBoxWidgetExt() ;
	GridLayout layout = new GridLayout( 1 , 1 ) ;

	public IterSkydipObsGUI()
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
		currentAzimuth.setText( "Do Skydip at Current Azimuth?" ) ;
		currentAzimuth.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		this.setLayout( layout ) ;
		this.add( currentAzimuth ) ;
	}
}
