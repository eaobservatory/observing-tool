/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.inst.editor ;

import java.awt.GridBagLayout ;
import java.awt.AWTEvent ;
import java.awt.Dimension ;
import javax.swing.JPanel ;

/**
 * ACSIS DR GUI.
 * 
 * @author Martin Folger ( M.Folger@roe.ac.uk )
 */
@SuppressWarnings( "serial" )
public class DataReductionScreen extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;

	// Construct the frame
	public DataReductionScreen()
	{
		enableEvents( AWTEvent.WINDOW_EVENT_MASK ) ;
		try
		{
			jbInit() ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}
	}

	// Component initialization
	private void jbInit() throws Exception
	{
		this.setLayout( gridBagLayout1 ) ;
		this.setSize( new Dimension( 501 , 564 ) ) ;
	}
}
