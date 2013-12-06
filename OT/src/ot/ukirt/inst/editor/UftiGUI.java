/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright 2000 Association for Universities for Research in Astronomy, Inc.<p>
 * Company:      <p>
 * @author Allan Brighton (modified for ukirt: Martin Folger)
 * @version 1.0
 */
package ot.ukirt.inst.editor ;

import java.awt.GridBagLayout ;
import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;

import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.BorderFactory ;
import javax.swing.border.TitledBorder ;
import javax.swing.border.EtchedBorder ;

import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.OptionWidgetExt ;
import jsky.app.ot.gui.TableWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;

@SuppressWarnings( "serial" )
public class UftiGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JLabel jLabel1 = new JLabel() ;
	JLabel exposureTimeLabel = new JLabel() ;
	DropDownListBoxWidgetExt readoutArea = new DropDownListBoxWidgetExt() ;
	TextBoxWidgetExt exposureTime = new TextBoxWidgetExt() ;
	JLabel jLabel3 = new JLabel() ;
	JLabel jLabel4 = new JLabel() ;
	DropDownListBoxWidgetExt sourceMag = new DropDownListBoxWidgetExt() ;
	JLabel jLabel5 = new JLabel() ;
	DropDownListBoxWidgetExt acqMode = new DropDownListBoxWidgetExt() ;
	JLabel jLabel6 = new JLabel() ;
	TextBoxWidgetExt coadds = new TextBoxWidgetExt() ;
	JLabel jLabel8 = new JLabel() ;
	JLabel jLabel10 = new JLabel() ;
	TextBoxWidgetExt scienceFOV = new TextBoxWidgetExt() ;
	JLabel jLabel11 = new JLabel() ;
	JPanel jPanel1 = new JPanel() ;
	TitledBorder titledBorder1 ;
	GridBagLayout gridBagLayout2 = new GridBagLayout() ;
	JLabel jLabel12 = new JLabel() ;
	JLabel jLabel14 = new JLabel() ;
	OptionWidgetExt filterBroadBand = new OptionWidgetExt() ;
	OptionWidgetExt filterNarrowBand = new OptionWidgetExt() ;
	OptionWidgetExt filterSpecial = new OptionWidgetExt() ;
	JScrollPane jScrollPane1 = new JScrollPane() ;
	TableWidgetExt filterTable = new TableWidgetExt() ;
	JLabel jLabel13 = new JLabel() ;
	DropDownListBoxWidgetExt polariser = new DropDownListBoxWidgetExt() ;
	CommandButtonWidgetExt defaultAcquisition = new CommandButtonWidgetExt() ;
	TextBoxWidgetExt filter = new TextBoxWidgetExt() ;

	public UftiGUI()
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
		titledBorder1 = new TitledBorder( new EtchedBorder( EtchedBorder.RAISED , Color.white , new Color( 142 , 142 , 142 ) ) , "Filter" ) ;
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel1.setForeground( Color.black ) ;
		jLabel1.setText( "Readout Area" ) ;
		this.setMinimumSize( new Dimension( 350 , 378 ) ) ;
		this.setPreferredSize( new Dimension( 350 , 378 ) ) ;
		this.setLayout( gridBagLayout1 ) ;
		exposureTimeLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		exposureTimeLabel.setForeground( Color.black ) ;
		exposureTimeLabel.setText( "Exposure Time" ) ;
		exposureTime.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		exposureTime.setToolTipText( "" ) ;
		jLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		jLabel3.setForeground( Color.black ) ;
		jLabel3.setText( "(sec)" ) ;
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel4.setForeground( Color.black ) ;
		jLabel4.setText( "Source Magnitude" ) ;
		jLabel5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel5.setForeground( Color.black ) ;
		jLabel5.setText( "Acquisition Mode" ) ;
		jLabel6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel6.setForeground( Color.black ) ;
		jLabel6.setText( "Coadds" ) ;
		jLabel8.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		jLabel8.setForeground( Color.black ) ;
		jLabel8.setText( "(exp. / observe)" ) ;
		jLabel10.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel10.setForeground( Color.black ) ;
		jLabel10.setText( "Science FOV" ) ;
		scienceFOV.setBackground( new Color( 204 , 204 , 204 ) ) ;
		scienceFOV.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		scienceFOV.setPreferredSize( new Dimension( 4 , 40 ) ) ;
		scienceFOV.setEditable( false ) ;
		jLabel11.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		jLabel11.setForeground( Color.black ) ;
		jLabel11.setText( "(arcsec)" ) ;
		jPanel1.setBorder( titledBorder1 ) ;
		jPanel1.setLayout( gridBagLayout2 ) ;
		jLabel12.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel12.setForeground( Color.black ) ;
		jLabel12.setText( "Selected Filter" ) ;
		jLabel14.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel14.setForeground( Color.black ) ;
		jLabel14.setText( "Filter Category" ) ;
		filterBroadBand.setText( "Broad-Band" ) ;
		filterBroadBand.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		filterNarrowBand.setText( "Narrow-Band" ) ;
		filterNarrowBand.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		filterSpecial.setText( "Special" ) ;
		filterSpecial.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		filterTable.setBackground( Color.lightGray ) ;
		filterTable.setIntercellSpacing( new Dimension( 3 , 3 ) ) ;
		filterTable.setShowHorizontalLines( false ) ;
		readoutArea.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		sourceMag.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		acqMode.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		coadds.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		jLabel13.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel13.setForeground( Color.black ) ;
		jLabel13.setText( "Polariser/FP" ) ;
		defaultAcquisition.setText( "Default" ) ;
		filter.setBackground( new Color( 204 , 204 , 204 ) ) ;
		filter.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		polariser.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		this.add( jLabel1 , new GridBagConstraints( 0 , 0 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( exposureTimeLabel , new GridBagConstraints( 2 , 0 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( readoutArea , new GridBagConstraints( 0 , 1 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( exposureTime , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel3 , new GridBagConstraints( 3 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 3 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel4 , new GridBagConstraints( 0 , 2 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( sourceMag , new GridBagConstraints( 0 , 3 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel5 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( acqMode , new GridBagConstraints( 0 , 5 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel6 , new GridBagConstraints( 2 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( coadds , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel8 , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 3 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel10 , new GridBagConstraints( 0 , 6 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jPanel1 , new GridBagConstraints( 0 , 8 , 4 , 1 , 1. , 1. , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 10 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		jPanel1.add( jLabel12 , new GridBagConstraints( 0 , 0 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel1.add( jLabel14 , new GridBagConstraints( 0 , 2 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 7 , 0 , 0 , 0 ) , 0 , 5 ) ) ;
		jPanel1.add( filterBroadBand , new GridBagConstraints( 0 , 3 , 2 , 1 , 0. , 0. , GridBagConstraints.NORTHWEST , GridBagConstraints.NONE , new Insets( 0 , 16 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel1.add( filterNarrowBand , new GridBagConstraints( 0 , 4 , 2 , 1 , 0. , 0. , GridBagConstraints.NORTHWEST , GridBagConstraints.NONE , new Insets( 0 , 16 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel1.add( filterSpecial , new GridBagConstraints( 0 , 5 , 2 , 1 , 0. , 0. , GridBagConstraints.NORTHWEST , GridBagConstraints.NONE , new Insets( 0 , 16 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel1.add( jScrollPane1 , new GridBagConstraints( 2 , 0 , 1 , 8 , 1. , 1. , GridBagConstraints.NORTHEAST , GridBagConstraints.VERTICAL , new Insets( 0 , 0 , 0 , 0 ) , 211 , 0 ) ) ;
		jPanel1.add( jLabel13 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.NORTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 6 ) ) ;
		jPanel1.add( filter , new GridBagConstraints( 0 , 1 , 2 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 68 , 0 ) ) ;
		jPanel1.add( polariser , new GridBagConstraints( 1 , 6 , 1 , 2 , 0. , 0. , GridBagConstraints.NORTHEAST , GridBagConstraints.NONE , new Insets( -1 , 5 , 1 , 0 ) , 43 , 0 ) ) ;
		this.add( defaultAcquisition , new GridBagConstraints( 3 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( scienceFOV , new GridBagConstraints( 0 , 7 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 120 , 5 ) ) ;
		this.add( jLabel11 , new GridBagConstraints( 2 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 1 , 0 , 2 ) , 0 , 0 ) ) ;
		jScrollPane1.getViewport().add( filterTable , null ) ;
	}
}
