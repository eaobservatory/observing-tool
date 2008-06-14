/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot.gemini.inst.editor ;

import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.awt.BorderLayout ;
import javax.swing.BorderFactory ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.border.TitledBorder ;
import javax.swing.border.EtchedBorder ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;

public class MichelleGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JLabel jLabel1 = new JLabel() ;
	JLabel jLabel2 = new JLabel() ;
	DropDownListBoxWidgetExt camera = new DropDownListBoxWidgetExt() ;
	TextBoxWidgetExt exposureTime = new TextBoxWidgetExt() ;
	JLabel jLabel3 = new JLabel() ;
	JLabel jLabel4 = new JLabel() ;
	DropDownListBoxWidgetExt filter = new DropDownListBoxWidgetExt() ;
	JLabel jLabel7 = new JLabel() ;
	TextBoxWidgetExt posAngle = new TextBoxWidgetExt() ;
	JLabel jLabel9 = new JLabel() ;
	JLabel jLabel10 = new JLabel() ;
	TextBoxWidgetExt scienceFOV = new TextBoxWidgetExt() ;
	JLabel jLabel11 = new JLabel() ;
	TitledBorder titledBorder1 ;
	CheckBoxWidgetExt mode = new CheckBoxWidgetExt() ;
	JPanel spectroscopyPanel = new JPanel() ;
	DropDownListBoxWidgetExt disperser = new DropDownListBoxWidgetExt() ;
	DropDownListBoxWidgetExt mask = new DropDownListBoxWidgetExt() ;
	JLabel jLabel8 = new JLabel() ;
	JLabel jLabel12 = new JLabel() ;
	TextBoxWidgetExt centralWavelength = new TextBoxWidgetExt() ;
	TextBoxWidgetExt wavelengthCoverageLow = new TextBoxWidgetExt() ;
	CommandButtonWidgetExt defaultCentralWavelength = new CommandButtonWidgetExt() ;
	JLabel jLabel16 = new JLabel() ;
	TextBoxWidgetExt wavelengthCoverageHigh = new TextBoxWidgetExt() ;
	JPanel chopPanel = new JPanel() ;
	GridBagLayout gridBagLayout2 = new GridBagLayout() ;
	JLabel chopCyclesPerNodLabel = new JLabel() ;
	TextBoxWidgetExt cyclesPerObs = new TextBoxWidgetExt() ;
	JLabel nodCyclesPerObs = new JLabel() ;
	JPanel chopControlGroup = new JPanel() ;
	JLabel cyclesPerObsLabel = new JLabel() ;
	TextBoxWidgetExt coadds = new TextBoxWidgetExt() ;
	TextBoxWidgetExt chopCyclesPerNod = new TextBoxWidgetExt() ;
	CheckBoxWidgetExt nodding = new CheckBoxWidgetExt() ;
	BorderLayout borderLayout1 = new BorderLayout() ;
	TitledBorder titledBorder2 ;
	JPanel stareControlGroup = new JPanel() ;
	JLabel nodCyclesPerObs2 = new JLabel() ;
	TextBoxWidgetExt expPerChopPos = new TextBoxWidgetExt() ;
	GridBagLayout gridBagLayout3 = new GridBagLayout() ;
	JLabel jLabel5 = new JLabel() ;
	JLabel jLabel6 = new JLabel() ;
	JLabel jLabel13 = new JLabel() ;
	JLabel jLabel14 = new JLabel() ;
	GridBagLayout gridBagLayout4 = new GridBagLayout() ;

	public MichelleGUI()
	{
		try
		{
			jbInit() ;
		}
		catch( Exception ex )
		{
			ex.printStackTrace() ;
		}
	}

	void jbInit() throws Exception
	{
		titledBorder1 = new TitledBorder( new EtchedBorder( EtchedBorder.RAISED , Color.white , new Color( 142 , 142 , 142 ) ) , "Spectroscopy" ) ;
		titledBorder2 = new TitledBorder( new EtchedBorder( EtchedBorder.RAISED , Color.white , new Color( 142 , 142 , 142 ) ) , "Chop Control" ) ;
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel1.setForeground( Color.black ) ;
		jLabel1.setText( "Camera" ) ;
		this.setMinimumSize( new Dimension( 355 , 366 ) ) ;
		this.setPreferredSize( new Dimension( 355 , 366 ) ) ;
		this.setLayout( gridBagLayout1 ) ;
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel2.setForeground( Color.black ) ;
		jLabel2.setText( "Exposure Time" ) ;
		exposureTime.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		exposureTime.setToolTipText( "" ) ;
		jLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		jLabel3.setForeground( Color.black ) ;
		jLabel3.setText( "(sec)" ) ;
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel4.setForeground( Color.black ) ;
		jLabel4.setText( "Filter" ) ;
		jLabel7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel7.setForeground( Color.black ) ;
		jLabel7.setText( "Position Angle" ) ;
		jLabel9.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		jLabel9.setForeground( Color.black ) ;
		jLabel9.setText( "(degrees E of N)" ) ;
		jLabel10.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel10.setForeground( Color.black ) ;
		jLabel10.setText( "Science FOV" ) ;
		scienceFOV.setBackground( new Color( 204 , 204 , 204 ) ) ;
		scienceFOV.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		scienceFOV.setEditable( false ) ;
		jLabel11.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		jLabel11.setForeground( Color.black ) ;
		jLabel11.setText( "(arcsec)" ) ;
		camera.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		filter.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		mode.setText( "Chopping?" ) ;
		mode.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		spectroscopyPanel.setLayout( gridBagLayout3 ) ;
		spectroscopyPanel.setBorder( titledBorder1 ) ;
		spectroscopyPanel.setOpaque( false ) ;
		disperser.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		mask.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel8.setText( "Disperser" ) ;
		jLabel8.setForeground( Color.black ) ;
		jLabel8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel12.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel12.setForeground( Color.black ) ;
		jLabel12.setText( "Focal Plane Mask" ) ;
		defaultCentralWavelength.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		defaultCentralWavelength.setMargin( new Insets( 2 , 5 , 2 , 5 ) ) ;
		defaultCentralWavelength.setText( "Default" ) ;
		jLabel16.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		jLabel16.setForeground( Color.black ) ;
		jLabel16.setText( "to" ) ;
		wavelengthCoverageLow.setBackground( new Color( 204 , 204 , 204 ) ) ;
		wavelengthCoverageLow.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		wavelengthCoverageLow.setMinimumSize( new Dimension( 55 , 21 ) ) ;
		wavelengthCoverageLow.setPreferredSize( new Dimension( 55 , 21 ) ) ;
		wavelengthCoverageHigh.setBackground( new Color( 204 , 204 , 204 ) ) ;
		wavelengthCoverageHigh.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		wavelengthCoverageHigh.setMinimumSize( new Dimension( 55 , 21 ) ) ;
		wavelengthCoverageHigh.setPreferredSize( new Dimension( 55 , 21 ) ) ;
		chopCyclesPerNodLabel.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		chopCyclesPerNodLabel.setForeground( Color.black ) ;
		chopCyclesPerNodLabel.setText( "(chop cycles / nod)" ) ;
		cyclesPerObs.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		cyclesPerObs.setMinimumSize( new Dimension( 55 , 21 ) ) ;
		cyclesPerObs.setPreferredSize( new Dimension( 55 , 21 ) ) ;
		nodCyclesPerObs.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		nodCyclesPerObs.setForeground( Color.black ) ;
		nodCyclesPerObs.setText( "(exp / obs)" ) ;
		chopControlGroup.setLayout( gridBagLayout2 ) ;
		cyclesPerObsLabel.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		cyclesPerObsLabel.setForeground( Color.black ) ;
		cyclesPerObsLabel.setText( "(chop cycles/obs)" ) ;
		coadds.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		coadds.setMaximumSize( new Dimension( 999 , 999 ) ) ;
		coadds.setMinimumSize( new Dimension( 55 , 21 ) ) ;
		coadds.setPreferredSize( new Dimension( 55 , 21 ) ) ;
		nodding.setText( "Nodding?" ) ;
		nodding.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		chopPanel.setLayout( borderLayout1 ) ;
		centralWavelength.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		centralWavelength.setMinimumSize( new Dimension( 55 , 21 ) ) ;
		centralWavelength.setPreferredSize( new Dimension( 55 , 21 ) ) ;
		chopPanel.setBorder( titledBorder2 ) ;
		nodCyclesPerObs2.setText( "(exp / obs)" ) ;
		nodCyclesPerObs2.setForeground( Color.black ) ;
		nodCyclesPerObs2.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		expPerChopPos.setPreferredSize( new Dimension( 55 , 21 ) ) ;
		chopCyclesPerNod.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		chopCyclesPerNod.setMinimumSize( new Dimension( 55 , 21 ) ) ;
		chopCyclesPerNod.setPreferredSize( new Dimension( 55 , 21 ) ) ;
		posAngle.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		jLabel5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel5.setForeground( Color.black ) ;
		jLabel5.setText( "Central Wavelength" ) ;
		jLabel6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel6.setForeground( Color.black ) ;
		jLabel6.setToolTipText( "" ) ;
		jLabel6.setText( "Wavelength Coverage" ) ;
		jLabel13.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		jLabel13.setForeground( Color.black ) ;
		jLabel13.setText( "(um)" ) ;
		jLabel14.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		jLabel14.setForeground( Color.black ) ;
		jLabel14.setText( "(um)" ) ;
		stareControlGroup.setLayout( gridBagLayout4 ) ;
		this.add( jLabel1 , new GridBagConstraints( 0 , 0 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel2 , new GridBagConstraints( 3 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( camera , new GridBagConstraints( 0 , 1 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( exposureTime , new GridBagConstraints( 3 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel3 , new GridBagConstraints( 4 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 3 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel4 , new GridBagConstraints( 0 , 2 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( filter , new GridBagConstraints( 0 , 3 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel7 , new GridBagConstraints( 3 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( posAngle , new GridBagConstraints( 3 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 2 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel9 , new GridBagConstraints( 4 , 3 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 3 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel10 , new GridBagConstraints( 3 , 4 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( scienceFOV , new GridBagConstraints( 3 , 5 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 20 , 0 , 0 ) , 0 , 5 ) ) ;
		this.add( jLabel11 , new GridBagConstraints( 5 , 5 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( mode , new GridBagConstraints( 1 , 5 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( chopPanel , new GridBagConstraints( 0 , 6 , 6 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		chopPanel.add( chopControlGroup , BorderLayout.CENTER ) ;
		chopControlGroup.add( coadds , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 5 , 0 ) , -20 , 0 ) ) ;
		chopControlGroup.add( cyclesPerObs , new GridBagConstraints( 0 , 1 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 5 , 0 ) , 0 , 0 ) ) ;
		chopControlGroup.add( nodCyclesPerObs , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 3 , 5 , 0 ) , 0 , 0 ) ) ;
		chopControlGroup.add( cyclesPerObsLabel , new GridBagConstraints( 1 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 3 , 5 , 0 ) , 0 , 0 ) ) ;
		chopControlGroup.add( nodding , new GridBagConstraints( 2 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 10 , 5 , 0 ) , 0 , 0 ) ) ;
		chopControlGroup.add( chopCyclesPerNod , new GridBagConstraints( 2 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.BOTH , new Insets( 0 , 10 , 5 , 0 ) , 0 , 0 ) ) ;
		chopControlGroup.add( chopCyclesPerNodLabel , new GridBagConstraints( 3 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 3 , 5 , 0 ) , 0 , 0 ) ) ;
		this.add( spectroscopyPanel , new GridBagConstraints( 0 , 8 , 6 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 5 , 0 , 5 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( disperser , new GridBagConstraints( 0 , 1 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 1 ) ) ;
		spectroscopyPanel.add( jLabel12 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( mask , new GridBagConstraints( 0 , 3 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( centralWavelength , new GridBagConstraints( 1 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 20 , 0 , 0 ) , 0 , 6 ) ) ;
		spectroscopyPanel.add( wavelengthCoverageLow , new GridBagConstraints( 1 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.VERTICAL , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel8 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( wavelengthCoverageHigh , new GridBagConstraints( 3 , 3 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.VERTICAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( defaultCentralWavelength , new GridBagConstraints( 3 , 1 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel16 , new GridBagConstraints( 2 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 10 , 0 , 10 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel5 , new GridBagConstraints( 1 , 0 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel6 , new GridBagConstraints( 1 , 2 , 4 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel13 , new GridBagConstraints( 4 , 0 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 3 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel14 , new GridBagConstraints( 5 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 3 , 0 , 0 ) , 0 , 0 ) ) ;
		stareControlGroup.add( expPerChopPos , new GridBagConstraints( 0 , 0 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		stareControlGroup.add( nodCyclesPerObs2 , new GridBagConstraints( 1 , 0 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
	}
}
