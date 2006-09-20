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

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import javax.swing.border.Border;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import orac.util.JThermometer;

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */

public class IterRasterObsGUI extends IterJCMTGenericGUI
{
	JPanel scubaAcsisPanel = new JPanel();
	JPanel areaPanel = new JPanel();
	JPanel scanPanel = new JPanel();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	GridBagLayout gridBagLayout3 = new GridBagLayout();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel scanSystemLabel = new JLabel();
	DropDownListBoxWidgetExt scanSystem = new DropDownListBoxWidgetExt();
	TextBoxWidgetExt dx = new TextBoxWidgetExt();
	TextBoxWidgetExt dy = new TextBoxWidgetExt();
	TextBoxWidgetExt width = new TextBoxWidgetExt();
	JLabel arcSecsLabel1 = new JLabel();
	JLabel arcSecsLabel2 = new JLabel();
	JLabel arcSecsLabel3 = new JLabel();
	TextBoxWidgetExt height = new TextBoxWidgetExt();
	JLabel arcSecsLabel4 = new JLabel();
	JLabel jLabel21 = new JLabel();
	TextBoxWidgetExt posAngle = new TextBoxWidgetExt();
	JLabel jLabel23 = new JLabel();
	Border border1;
	JLabel jLabel14 = new JLabel();
	DropDownListBoxWidgetExt sampleTime = new DropDownListBoxWidgetExt();
	TextBoxWidgetExt acsisSampleTime = new TextBoxWidgetExt();
	TextBoxWidgetExt secsPerObservation = new TextBoxWidgetExt();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	TextBoxWidgetExt secsPerRow = new TextBoxWidgetExt();
	JLabel jLabel13 = new JLabel();
	JLabel jLabel10 = new JLabel();
	JPanel heterodynePanel = new JPanel();
	OptionWidgetExt alongRow = new OptionWidgetExt();
	CommandButtonWidgetExt defaultButton = new CommandButtonWidgetExt();
	OptionWidgetExt interleaved = new OptionWidgetExt();
	TextBoxWidgetExt rowsPerCal = new TextBoxWidgetExt();
	CheckBoxWidgetExt rowReversal = new CheckBoxWidgetExt();
	JLabel jLabel9 = new JLabel();
	JLabel jLabel8 = new JLabel();
	TextBoxWidgetExt rowsPerRef = new TextBoxWidgetExt();
	JLabel jLabel16 = new JLabel();
	DropDownListBoxWidgetExt scanAngle = new DropDownListBoxWidgetExt();
	JLabel jLabel17 = new JLabel() ;
	JThermometer thermometer = new JThermometer();
	JLabel estimationLabel = new JLabel();
	
	TextBoxWidgetExt sizeOfXPixel = new TextBoxWidgetExt() ;
	TextBoxWidgetExt sizeOfYPixel = new TextBoxWidgetExt() ;
	JLabel sizeOfXPixelLabel = new JLabel() ;
	JLabel sizeOfYPixelLabel = new JLabel() ;
	JLabel dimensionWarningTextTop = new JLabel() ;
	JLabel dimensionWarningTextBottom = new JLabel() ;
	JLabel spacingLabel = new JLabel() ;

	public IterRasterObsGUI()
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
		border1 = BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 );
		JPanel rasterPanel = new JPanel();
		Border bevelBorder = BorderFactory.createBevelBorder( BevelBorder.LOWERED );
		Border titleBorder = BorderFactory.createTitledBorder( bevelBorder , "Raster setup" );
		rasterPanel.setBorder( titleBorder );
		rasterPanel.setLayout( new BorderLayout() );
		scubaAcsisPanel.setLayout( new BorderLayout() );
		areaPanel.setLayout( gridBagLayout2 );
		scanPanel.setLayout( gridBagLayout3 );
		areaPanel.setBorder( new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Area" ) );
		scanPanel.setBorder( new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Scan" ) );
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel1.setForeground( Color.black );
		jLabel1.setText( "Sample Spacing" );
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel2.setForeground( Color.black );
		jLabel2.setText( "Scan Spacing" );
		jLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel3.setForeground( Color.black );
		jLabel3.setText( "Width" );
		scanSystemLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		scanSystemLabel.setForeground( Color.black );
		scanSystemLabel.setText( "System" );
		arcSecsLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		arcSecsLabel1.setForeground( Color.black );
		arcSecsLabel1.setText( "(arcsecs)" );
		scanSystem.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		sampleTime.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		arcSecsLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		arcSecsLabel2.setForeground( Color.black );
		arcSecsLabel2.setText( "(arcsecs)" );
		arcSecsLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		arcSecsLabel3.setForeground( Color.black );
		arcSecsLabel3.setText( "(arcsecs)" );
		arcSecsLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		arcSecsLabel4.setForeground( Color.black );
		arcSecsLabel4.setText( "(arcsecs)" );
		jLabel21.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel21.setForeground( Color.black );
		jLabel21.setText( "Height" );
		posAngle.setColumns( 5 );
		jLabel23.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel23.setForeground( Color.black );
		jLabel23.setText( "(degrees)" );
		jLabel14.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel14.setForeground( Color.black );
		jLabel14.setText( "PA" );
		secsPerObservation.setEditable( false );
		secsPerRow.setEditable( false );
		jLabel13.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel13.setForeground( Color.black );
		jLabel13.setText( "Secs/Observation" );
		jLabel10.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel10.setForeground( Color.black );
		jLabel10.setText( "Secs/Row" );
		estimationLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		estimationLabel.setForeground( Color.black );
		estimationLabel.setText( "(estimated)" );
		heterodynePanel.setLayout( gridBagLayout1 );
		heterodynePanel.setBorder( new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Heterodyne Details" ) );
		alongRow.setText( "Along Row" );
		alongRow.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		defaultButton.setText( "Default" );
		interleaved.setText( "Interleafed" );
		interleaved.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		rowReversal.setText( "Row Reversal" );
		rowReversal.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel9.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel9.setForeground( Color.black );
		jLabel9.setText( "(sec)" );
		jLabel8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel8.setForeground( Color.black );
		jLabel8.setText( "Sample Time" );
		jLabel16.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel16.setForeground( Color.black );
		jLabel16.setText( "PA" );
		jLabel17.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel17.setForeground( Color.black );
		jLabel17.setText( "(degrees)" );
		scanAngle.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );

		spacingLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		spacingLabel.setForeground( Color.black ) ;
		spacingLabel.setText( " " ) ;
		
		sizeOfXPixel.setEnabled( false ) ;
		sizeOfYPixel.setEnabled( false ) ;

		sizeOfXPixelLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		sizeOfXPixelLabel.setForeground( Color.black ) ;
		sizeOfXPixelLabel.setText( "Nr of samples/pixels" ) ;
		sizeOfYPixelLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		sizeOfYPixelLabel.setForeground( Color.black ) ;
		sizeOfYPixelLabel.setText( "Nr of scans/pixels" ) ;
		
		dimensionWarningTextTop.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		dimensionWarningTextBottom.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		dimensionWarningTextTop.setForeground( Color.red ) ;
		dimensionWarningTextBottom.setForeground( Color.red ) ;
		dimensionWarningTextTop.setText( "Dimension must be equally" ) ;
		dimensionWarningTextBottom.setText( "divisible by spacing" ) ;
		dimensionWarningTextTop.setVisible( false ) ;		
		dimensionWarningTextBottom.setVisible( false ) ;
		
		this.add( rasterPanel , BorderLayout.CENTER );
		rasterPanel.add( scubaAcsisPanel , BorderLayout.WEST );
		scubaAcsisPanel.add( areaPanel , BorderLayout.CENTER );
		scubaAcsisPanel.add( scanPanel , BorderLayout.SOUTH );
		scanPanel.add( scanSystemLabel , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		scanPanel.add( scanSystem , new GridBagConstraints( 1 , 1 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 2 , 0 , 0 ) , 0 , 0 ) );
		scanPanel.add( jLabel16 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 1.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 5 , 0 ) , 0 , 0 ) );
		scanPanel.add( scanAngle , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.SOUTH , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		scanPanel.add( jLabel17 , new GridBagConstraints( 2 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );

		areaPanel.add( jLabel1 , new GridBagConstraints( 0 , 2 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 5 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( jLabel2 , new GridBagConstraints( 0 , 4 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 5 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( jLabel3 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( dx , new GridBagConstraints( 1 , 3 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		areaPanel.add( dy , new GridBagConstraints( 1 , 5 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		areaPanel.add( width , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		areaPanel.add( arcSecsLabel1 , new GridBagConstraints( 2 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( arcSecsLabel2 , new GridBagConstraints( 2 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( arcSecsLabel3 , new GridBagConstraints( 2 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( height , new GridBagConstraints( 1 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		areaPanel.add( arcSecsLabel4 , new GridBagConstraints( 2 , 5 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( jLabel21 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( posAngle , new GridBagConstraints( 1 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		areaPanel.add( jLabel23 , new GridBagConstraints( 2 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( jLabel14 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );

		areaPanel.add( spacingLabel , new GridBagConstraints( 0 , 7 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		areaPanel.add( sizeOfXPixelLabel , new GridBagConstraints( 0 , 8 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		areaPanel.add( sizeOfXPixel, new GridBagConstraints( 1 , 9 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		areaPanel.add( sizeOfYPixelLabel , new GridBagConstraints( 0 , 10 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		areaPanel.add( sizeOfYPixel, new GridBagConstraints( 1 , 11 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		areaPanel.add( dimensionWarningTextTop , new GridBagConstraints( 0 , 12 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		areaPanel.add( dimensionWarningTextBottom , new GridBagConstraints( 0 , 13 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		
		rasterPanel.add( heterodynePanel , BorderLayout.CENTER );
		heterodynePanel.add( jLabel8 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		heterodynePanel.add( sampleTime , new GridBagConstraints( 1 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		heterodynePanel.add( acsisSampleTime , new GridBagConstraints( 1 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		heterodynePanel.add( jLabel9 , new GridBagConstraints( 2 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 5 ) , 0 , 0 ) );
		heterodynePanel.add( defaultButton , new GridBagConstraints( 1 , 5 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		heterodynePanel.add( jLabel10 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		heterodynePanel.add( estimationLabel , new GridBagConstraints( 0 , 7 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		heterodynePanel.add( jLabel13 , new GridBagConstraints( 0 , 9 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		heterodynePanel.add( secsPerRow , new GridBagConstraints( 1 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		heterodynePanel.add( secsPerObservation , new GridBagConstraints( 1 , 9 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		heterodynePanel.add( thermometer , new GridBagConstraints( 1 , 11 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
	}
}
