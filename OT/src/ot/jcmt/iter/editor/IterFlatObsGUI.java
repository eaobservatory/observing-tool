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

import java.awt.BorderLayout;
import java.awt.FlowLayout ;

import javax.swing.BorderFactory;
import javax.swing.JPanel ;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author$
 * @version$
 */

public class IterFlatObsGUI extends IterJCMTGenericGUI
{
	JPanel flatPanel = new JPanel() ;
	FlowLayout flowLayout = new FlowLayout() ;
	DropDownListBoxWidgetExt flatSourceComboBox = new DropDownListBoxWidgetExt() ;
	
	public IterFlatObsGUI()
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
		Border bevelBorder = BorderFactory.createBevelBorder( BevelBorder.LOWERED ) ;
		Border titleBorder = BorderFactory.createTitledBorder( bevelBorder , "Flat setup" ) ;
		flatPanel.setBorder( titleBorder ) ;
		flatPanel.setLayout( flowLayout ) ;
		secsPerCycle.setColumns( 8 ) ;
		flatSourceComboBox.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		this.add( flatPanel , BorderLayout.CENTER ) ;
		flatPanel.add( flatSourceComboBox , null ) ;
		this.setVisible( false ) ;
	}
}
