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
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;
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
	JLabel sampleSpacingLabel = new JLabel();
	JLabel scanSpacingLabel = new JLabel();
	JLabel widthLabel = new JLabel();
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
	JLabel heightLabel = new JLabel();
	TextBoxWidgetExt posAngle = new TextBoxWidgetExt();
	JLabel degreesLabel1 = new JLabel();
	Border border1;
	JLabel posAngleLabel1 = new JLabel();
	DropDownListBoxWidgetExt sampleTime = new DropDownListBoxWidgetExt();
	TextBoxWidgetExt acsisSampleTime = new TextBoxWidgetExt();
	TextBoxWidgetExt secsPerObservation = new TextBoxWidgetExt();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	TextBoxWidgetExt secsPerRow = new TextBoxWidgetExt();
	JLabel secsPerObsLabel = new JLabel();
	JLabel secsPerRowLabel = new JLabel();
	JPanel heterodynePanel = new JPanel();
	CommandButtonWidgetExt defaultButton = new CommandButtonWidgetExt();
	TextBoxWidgetExt rowsPerCal = new TextBoxWidgetExt();
	CheckBoxWidgetExt rowReversal = new CheckBoxWidgetExt();
	JLabel secsLabel = new JLabel();
	JLabel sampleTimeLabel = new JLabel();
	TextBoxWidgetExt rowsPerRef = new TextBoxWidgetExt();
	JLabel posAngleLabel2 = new JLabel();
	DropDownListBoxWidgetExt scanAngle = new DropDownListBoxWidgetExt();
	JLabel degreesLabel2 = new JLabel() ;
	JThermometer thermometer = new JThermometer();
	JLabel estimationLabel = new JLabel();
	
	TextBoxWidgetExt sizeOfXPixel = new TextBoxWidgetExt() ;
	TextBoxWidgetExt sizeOfYPixel = new TextBoxWidgetExt() ;
	JLabel sizeOfXPixelLabel = new JLabel() ;
	JLabel sizeOfYPixelLabel = new JLabel() ;
	JLabel dimensionWarningTextTop = new JLabel() ;
	JLabel dimensionWarningTextBottom = new JLabel() ;
	JLabel spacingLabel = new JLabel() ;

	DropDownListBoxWidgetExt harpRasters = new DropDownListBoxWidgetExt() ;
	JPanel harpPanel = new JPanel() ;
	JPanel nonHarpPanel = new JPanel() ;
	JLabel harpScanSpacingLabel = new JLabel() ;
	
	DropDownListBoxWidgetExt scanningStrategies = new DropDownListBoxWidgetExt() ;
	JPanel scuba2Panel = new JPanel() ;
	
	JPanel rasterPanel = new JPanel() ;
	
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
		Border bevelBorder = BorderFactory.createBevelBorder( BevelBorder.LOWERED );
		Border titleBorder = BorderFactory.createTitledBorder( bevelBorder , "Raster setup" );
		rasterPanel.setBorder( titleBorder );
		rasterPanel.setLayout( new GridLayout( 1 , 2 ) ) ;
		scubaAcsisPanel.setLayout( new BorderLayout() );
		areaPanel.setLayout( gridBagLayout2 );
		scanPanel.setLayout( gridBagLayout3 );
		areaPanel.setBorder( new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Area" ) );
		scanPanel.setBorder( new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Scan" ) );
		sampleSpacingLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		sampleSpacingLabel.setForeground( Color.black );
		sampleSpacingLabel.setText( "Sample Spacing" );
		scanSpacingLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		scanSpacingLabel.setForeground( Color.black );
		scanSpacingLabel.setText( "Scan Spacing" );
		widthLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		widthLabel.setForeground( Color.black );
		widthLabel.setText( "Width" );
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
		heightLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		heightLabel.setForeground( Color.black );
		heightLabel.setText( "Height" );
		posAngle.setColumns( 5 );
		degreesLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		degreesLabel1.setForeground( Color.black );
		degreesLabel1.setText( "(degrees)" );
		posAngleLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		posAngleLabel1.setForeground( Color.black );
		posAngleLabel1.setText( "PA" );
		secsPerObservation.setEditable( false );
		secsPerRow.setEditable( false );
		secsPerObsLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		secsPerObsLabel.setForeground( Color.black );
		secsPerObsLabel.setText( "Secs/Observation" );
		secsPerRowLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		secsPerRowLabel.setForeground( Color.black );
		secsPerRowLabel.setText( "Secs/Row" );
		estimationLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		estimationLabel.setForeground( Color.black );
		estimationLabel.setText( "(estimated)" );
		heterodynePanel.setLayout( gridBagLayout1 );
		heterodynePanel.setBorder( new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Heterodyne Details" ) );
		defaultButton.setText( "Default" );
		rowReversal.setText( "Row Reversal" );
		rowReversal.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		secsLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		secsLabel.setForeground( Color.black );
		secsLabel.setText( "(sec)" );
		sampleTimeLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		sampleTimeLabel.setForeground( Color.black );
		sampleTimeLabel.setText( "Sample Time" );
		posAngleLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		posAngleLabel2.setForeground( Color.black );
		posAngleLabel2.setText( "PA" );
		degreesLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		degreesLabel2.setForeground( Color.black );
		degreesLabel2.setText( "(degrees)" );
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
		
		harpScanSpacingLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		harpScanSpacingLabel.setForeground( Color.black );
		harpScanSpacingLabel.setText( "Scan Spacing" );
		
		this.add( rasterPanel , BorderLayout.CENTER );
		scubaAcsisPanel.add( areaPanel , BorderLayout.CENTER );
		
		scuba2Panel.setBorder( new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "SCUBA-2 Details" ) );
		JLabel scanStrategyLabel = new JLabel() ;
		scanStrategyLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		scanStrategyLabel.setForeground( Color.black );
		scanStrategyLabel.setText( "Scan Strategy" );
		scuba2Panel.setLayout( new GridLayout( 10 , 1 ) ) ;
		scuba2Panel.add( scanStrategyLabel ) ;
		scuba2Panel.add( scanningStrategies ) ;
		
		rasterPanel.add( scubaAcsisPanel ) ;
		
		scubaAcsisPanel.add( scanPanel , BorderLayout.SOUTH );
		scanPanel.add( scanSystemLabel , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		scanPanel.add( scanSystem , new GridBagConstraints( 1 , 1 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 2 , 0 , 0 ) , 0 , 0 ) );
		scanPanel.add( posAngleLabel2 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 1.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 5 , 0 ) , 0 , 0 ) );
		scanPanel.add( scanAngle , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.SOUTH , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		scanPanel.add( degreesLabel1 , new GridBagConstraints( 2 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );

		areaPanel.add( widthLabel , new GridBagConstraints( 0 , 0 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( width , new GridBagConstraints( 2 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		areaPanel.add( arcSecsLabel1 , new GridBagConstraints( 3 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		
		areaPanel.add( heightLabel , new GridBagConstraints( 0 , 1 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( height , new GridBagConstraints( 2 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		areaPanel.add( arcSecsLabel2 , new GridBagConstraints( 3 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );

		areaPanel.add( posAngleLabel1 , new GridBagConstraints( 0 , 3 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( posAngle , new GridBagConstraints( 2 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		areaPanel.add( degreesLabel2 , new GridBagConstraints( 3 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		
		areaPanel.add( sampleSpacingLabel , new GridBagConstraints( 0 , 5 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 10 , 5 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( dx , new GridBagConstraints( 2 , 5 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		areaPanel.add( arcSecsLabel3 , new GridBagConstraints( 3 , 5 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );

		areaPanel.add( scanSpacingLabel , new GridBagConstraints( 0 , 6 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 10 , 5 , 0 , 0 ) , 0 , 0 ) );
		areaPanel.add( dy , new GridBagConstraints( 2 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 2 , 2 , 2 , 2 ) , 0 , 0 ) );
		areaPanel.add( arcSecsLabel4 , new GridBagConstraints( 3 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
				
		nonHarpPanel.setLayout( new GridBagLayout() ) ;
		nonHarpPanel.add( spacingLabel , new GridBagConstraints( 0 , 0 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( sizeOfXPixelLabel , new GridBagConstraints( 0 , 1 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( sizeOfXPixel, new GridBagConstraints( 1 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( sizeOfYPixelLabel , new GridBagConstraints( 0 , 3 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( sizeOfYPixel, new GridBagConstraints( 1 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( dimensionWarningTextTop , new GridBagConstraints( 0 , 5 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( dimensionWarningTextBottom , new GridBagConstraints( 0 , 6 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		
		rasterPanel.add( heterodynePanel ) ;
		heterodynePanel.add( sampleTimeLabel , new GridBagConstraints( 0 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		heterodynePanel.add( sampleTime , new GridBagConstraints( 1 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		heterodynePanel.add( acsisSampleTime , new GridBagConstraints( 1 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		heterodynePanel.add( secsLabel , new GridBagConstraints( 2 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 5 ) , 0 , 0 ) );
		heterodynePanel.add( defaultButton , new GridBagConstraints( 1 , 5 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		heterodynePanel.add( secsPerRowLabel , new GridBagConstraints( 0 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		heterodynePanel.add( estimationLabel , new GridBagConstraints( 0 , 7 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		heterodynePanel.add( secsPerObsLabel , new GridBagConstraints( 0 , 9 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) );
		heterodynePanel.add( secsPerRow , new GridBagConstraints( 1 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		heterodynePanel.add( secsPerObservation , new GridBagConstraints( 1 , 9 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );

		harpPanel.setLayout( new GridBagLayout() ) ;
		harpPanel.add( harpScanSpacingLabel , new GridBagConstraints( 0 , 3 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		harpPanel.add( harpRasters , new GridBagConstraints( 2 , 3 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;		
		addNonHarpPanel() ;
	}

	private boolean harped = false ;

	public boolean isHarped()
	{
		return harped ;
	}
	
	public void addHarpPanel()
	{
		areaPanel.remove( nonHarpPanel ) ;
		areaPanel.add( harpPanel , new GridBagConstraints( 0 , 7 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		harped = true ;
	}

	public void addNonHarpPanel()
	{
		areaPanel.remove( harpPanel ) ;
		areaPanel.add( nonHarpPanel , new GridBagConstraints( 0 , 7 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		harped = false ;
	}
	
	private boolean scuba2d = false ;

	public boolean isScuba2d()
	{
		return scuba2d ;
	}
	
	public void addScuba2Panel()
	{
		rasterPanel.remove( heterodynePanel ) ;
		rasterPanel.add( scuba2Panel ) ;
		scuba2d = true ;
	}

	public void addNonScuba2Panel()
	{
		rasterPanel.remove( scuba2Panel ) ;
		rasterPanel.add( heterodynePanel ) ;
		scuba2d = false ;
	}
	
}
