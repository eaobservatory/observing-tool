// $Id$

package ot.jcmt.iter.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import jsky.app.ot.gui.TextBoxWidgetExt;

public class IterDREAMObsGUI extends IterJCMTGenericGUI
{
	TextBoxWidgetExt secsPerObservation = new TextBoxWidgetExt();
	JPanel mainPanel = new JPanel();
	JLabel integrationTimeLabel = new JLabel();
	JLabel secondsLabel = new JLabel();

	public IterDREAMObsGUI()
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
		integrationTimeLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		integrationTimeLabel.setForeground( Color.black );
		integrationTimeLabel.setText( "Integration time : " );

		secondsLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		secondsLabel.setForeground( Color.black );
		secondsLabel.setText( " seconds" );

		Border bevelBorder = BorderFactory.createBevelBorder( BevelBorder.LOWERED );
		Border titleBorder = BorderFactory.createTitledBorder( bevelBorder , "DREAM setup" );

		mainPanel.setBorder( titleBorder );
		mainPanel.setLayout( new GridLayout( 1 , 3 ) );
		mainPanel.add( integrationTimeLabel , BorderLayout.WEST );
		mainPanel.add( secsPerObservation , BorderLayout.CENTER );
		mainPanel.add( secondsLabel , BorderLayout.EAST );
		this.setLayout( new GridLayout( 10 , 1 ) );
		this.add( mainPanel , BorderLayout.CENTER );
	}
}
