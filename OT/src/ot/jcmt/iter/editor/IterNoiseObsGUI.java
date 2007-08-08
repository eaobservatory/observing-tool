/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.iter.editor;

import javax.swing.JPanel ;
import javax.swing.BorderFactory ;
import javax.swing.border.BevelBorder ;
import javax.swing.border.Border ;
import java.awt.FlowLayout ;
import java.awt.BorderLayout ;

import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */

public class IterNoiseObsGUI extends IterJCMTGenericGUI
{
	JPanel noisePanel = new JPanel();
	FlowLayout flowLayout1 = new FlowLayout();
	DropDownListBoxWidgetExt noiseSourceComboBox = new DropDownListBoxWidgetExt();

	public IterNoiseObsGUI()
	{
		try
		{
			jbInit();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		Border bevelBorder = BorderFactory.createBevelBorder( BevelBorder.LOWERED );
		Border titleBorder = BorderFactory.createTitledBorder( bevelBorder , "Noise setup" );
		noisePanel.setBorder( titleBorder );
		noisePanel.setLayout( flowLayout1 );
		secsPerCycle.setColumns( 8 );
		noiseSourceComboBox.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		this.add( noisePanel , BorderLayout.CENTER );
		noisePanel.add( noiseSourceComboBox , null );
	}
}
