/*
 * Copyright (C) 2007-2010 Science and Technology Facilities Council.
 * All Rights Reserved.
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

import java.awt.BorderLayout ;
import java.awt.Color ;
import java.awt.GridLayout ;

import javax.swing.BorderFactory ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.border.BevelBorder ;
import javax.swing.border.Border ;

import jsky.app.ot.gui.TextBoxWidgetExt ;

@SuppressWarnings( "serial" )
public class IterDREAMObsGUI extends IterJCMTGenericGUI
{
	TextBoxWidgetExt secsPerObservation = new TextBoxWidgetExt() ;
	JPanel mainPanel = new JPanel() ;
	JPanel subPanel = new JPanel() ;
	JLabel integrationTimeLabel = new JLabel() ;
	JLabel secondsLabel = new JLabel() ;

	public IterDREAMObsGUI()
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
		integrationTimeLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		integrationTimeLabel.setForeground( Color.black ) ;
		integrationTimeLabel.setText( "Integration time : " ) ;

		secondsLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		secondsLabel.setForeground( Color.black ) ;
		secondsLabel.setText( " seconds / pixel" ) ;

		Border bevelBorder = BorderFactory.createBevelBorder( BevelBorder.LOWERED ) ;
		Border titleBorder = BorderFactory.createTitledBorder( bevelBorder , "DREAM setup" ) ;

		subPanel.setBorder( titleBorder ) ;
		subPanel.setLayout( new GridLayout( 1 , 3 ) ) ;
		subPanel.add( integrationTimeLabel , BorderLayout.WEST ) ;
		subPanel.add( secsPerObservation , BorderLayout.CENTER ) ;
		subPanel.add( secondsLabel , BorderLayout.EAST ) ;
		mainPanel.add( subPanel ) ;
		this.add( mainPanel , BorderLayout.CENTER ) ;
	}
}
