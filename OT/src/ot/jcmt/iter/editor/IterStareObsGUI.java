/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.iter.editor;

import javax.swing.*;

import java.awt.*;

import javax.swing.border.*;

import jsky.app.ot.gui.CheckBoxWidgetExt;

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */

public class IterStareObsGUI extends IterJCMTGenericGUI
{
	JPanel acsisPanel = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JCheckBox widePhotom = new JCheckBox();
	CheckBoxWidgetExt contModeCB = new CheckBoxWidgetExt();
	
	public IterStareObsGUI()
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
		JPanel starePanel = new JPanel();
		Border bevelBorder = BorderFactory.createBevelBorder( BevelBorder.LOWERED );
		Border titleBorder = BorderFactory.createTitledBorder( bevelBorder , "Photom Setup" );
		starePanel.setBorder( titleBorder );
		starePanel.setLayout( new BorderLayout() );

		acsisPanel.setLayout( gridBagLayout1 );
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel1.setForeground( Color.black );
		jLabel1.setText( "Secs per offset sample" );
		
		secsPerCycle.setColumns( 5 ) ;
		
		cycleReversal.setText( "Cycle Reversal" );
		cycleReversal.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		continuousCal.setText( "Continuous Cal" );
		continuousCal.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		
		contModeCB.setText( "Continuum Mode" );
		contModeCB.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		contModeCB.setForeground( Color.black );
		contModeCB.setHorizontalTextPosition( SwingConstants.LEFT );

		widePhotom.setText( "Wide Photometry" );
		widePhotom.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		widePhotom.setForeground( Color.black );
		
		this.add( starePanel , BorderLayout.CENTER );
		starePanel.add( widePhotom , BorderLayout.NORTH );

		starePanel.add( acsisPanel , BorderLayout.CENTER );
		acsisPanel.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		acsisPanel.add( secsPerCycle , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );

		// Add a coninuum mode checkbox
		acsisPanel.add( contModeCB , new GridBagConstraints( 0 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
	}

}
