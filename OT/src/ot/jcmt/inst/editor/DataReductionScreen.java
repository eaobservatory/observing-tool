/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.inst.editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import jsky.app.ot.gui.*;

/**
 * ACSIS DR GUI.
 * 
 * @author Martin Folger ( M.Folger@roe.ac.uk )
 */
public class DataReductionScreen extends JPanel
{

	GridBagLayout gridBagLayout1 = new GridBagLayout();

	JLabel jLabel11 = new JLabel();
	DropDownListBoxWidgetExt windowType = new DropDownListBoxWidgetExt();
	
	// Construct the frame
	public DataReductionScreen()
	{
		enableEvents( AWTEvent.WINDOW_EVENT_MASK );
		try
		{
			jbInit();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	// Component initialization
	private void jbInit() throws Exception
	{
		this.setLayout( gridBagLayout1 );
		this.setSize( new Dimension( 501 , 564 ) );
		
		jLabel11.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel11.setText( "Window Type" );

		windowType.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		windowType.setActionCommand( "comboBoxChanged" );

		this.add( jLabel11 , new GridBagConstraints( 3 , 2 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 10 , 0 , 2 ) , 0 , 0 ) );
		this.add( windowType , new GridBagConstraints( 5 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
	}

}
