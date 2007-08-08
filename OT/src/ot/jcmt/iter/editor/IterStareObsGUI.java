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

import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.JCheckBox ;
import javax.swing.BorderFactory ;
import javax.swing.SwingConstants ;

import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.BorderLayout ;
import java.awt.Color ;
import java.awt.GridLayout ;

import javax.swing.border.Border ;
import javax.swing.border.BevelBorder ;

import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;

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
	JLabel informationLabelTop = new JLabel();
	JLabel informationLabelMiddle = new JLabel();
	JLabel informationLabelBottom = new JLabel();
	JPanel informationPanel = new JPanel();
	JPanel scuba2Panel = new JPanel();
	JLabel integrationTimeLabel = new JLabel();
	TextBoxWidgetExt integrationTime = new TextBoxWidgetExt();
	JLabel integrationTimeSeconds = new JLabel();

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

		secsPerCycle.setColumns( 5 );

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

		informationLabelTop.setText( "Warning: Using continuum mode will significantly increase the duration of the observation." );
		informationLabelTop.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		informationLabelTop.setForeground( Color.black );
		informationLabelMiddle.setText( "Continuum mode should only be used if an accurate measure of the continuum emission" );
		informationLabelMiddle.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		informationLabelMiddle.setForeground( Color.black );
		informationLabelBottom.setText( "from the source is a requirement." );
		informationLabelBottom.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		informationLabelBottom.setForeground( Color.black );

		informationPanel.setLayout( new GridLayout( 3 , 1 ) );

		this.add( starePanel , BorderLayout.CENTER );
		starePanel.add( widePhotom , BorderLayout.NORTH );

		starePanel.add( acsisPanel , BorderLayout.CENTER );
		acsisPanel.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		acsisPanel.add( secsPerCycle , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );

		// Add a coninuum mode checkbox
		acsisPanel.add( contModeCB , new GridBagConstraints( 0 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );

		this.add( informationPanel , BorderLayout.SOUTH );
		informationPanel.add( informationLabelTop );
		informationPanel.add( informationLabelMiddle );
		informationPanel.add( informationLabelBottom );

		scuba2Panel.setLayout( new GridLayout( 1 , 3 ) );

		integrationTimeLabel.setText( "Integration Time" );
		integrationTimeLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		integrationTimeLabel.setForeground( Color.black );
		scuba2Panel.add( integrationTimeLabel );

		scuba2Panel.add( integrationTime );

		integrationTimeSeconds.setText( "Seconds" );
		integrationTimeSeconds.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		integrationTimeSeconds.setForeground( Color.black );
		scuba2Panel.add( integrationTimeSeconds );

		starePanel.add( scuba2Panel , BorderLayout.NORTH );
	}
}
