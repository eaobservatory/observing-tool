/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 * License:
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package ot.ukirt.inst.editor ;

import java.awt.GridBagLayout ;
import java.awt.Color ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.BorderLayout ;
import java.awt.CardLayout ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.SwingConstants ;
import javax.swing.BorderFactory ;
import javax.swing.border.TitledBorder ;

import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;

@SuppressWarnings( "serial" )
public class MichelleGUI extends JPanel
{
	JPanel jPanel1 = new JPanel() ;
	BorderLayout borderLayout1 = new BorderLayout() ;
	JLabel jLabel1 = new JLabel() ;
	DropDownListBoxWidgetExt camera = new DropDownListBoxWidgetExt() ;
	CheckBoxWidgetExt polarimetry = new CheckBoxWidgetExt() ;
	JPanel jPanel2 = new JPanel() ;
	TitledBorder titledBorder1 ;
	JPanel modePanel = new JPanel() ;
	CardLayout cardLayout1 = new CardLayout() ;
	JPanel imagingPanel = new JPanel() ;
	TitledBorder titledBorder2 ;
	JPanel spectroscopyPanel = new JPanel() ;
	TitledBorder titledBorder3 ;
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JLabel jLabel2 = new JLabel() ;
	DropDownListBoxWidgetExt imaging_filterCategory = new DropDownListBoxWidgetExt() ;
	JLabel jLabel3 = new JLabel() ;
	DropDownListBoxWidgetExt imaging_filter = new DropDownListBoxWidgetExt() ;
	JLabel jLabel4 = new JLabel() ;
	JLabel jLabel5 = new JLabel() ;
	TextBoxWidgetExt imaging_fieldOfView = new TextBoxWidgetExt() ;
	GridBagLayout gridBagLayout2 = new GridBagLayout() ;
	TextBoxWidgetExt dataAcq_dutyCycle = new TextBoxWidgetExt() ;
	TextBoxWidgetExt dataAcq_chopFrequency = new TextBoxWidgetExt() ;
	JLabel jLabel6 = new JLabel() ;
	JLabel jLabel7 = new JLabel() ;
	JLabel jLabel8 = new JLabel() ;
	JLabel jLabel9 = new JLabel() ;
	JLabel jLabel10 = new JLabel() ;
	JLabel jLabel11 = new JLabel() ;
	TextBoxWidgetExt dataAcq_exposureTime = new TextBoxWidgetExt() ;
	TextBoxWidgetExt dataAcq_observationTime = new TextBoxWidgetExt() ;
	CommandButtonWidgetExt dataAcq_defaultExpTime = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt dataAcq_defaultObsTime = new CommandButtonWidgetExt() ;
	GridBagLayout gridBagLayout3 = new GridBagLayout() ;
	JLabel jLabel12b = new JLabel() ;
	DropDownListBoxWidgetExt spectroscopy_grating = new DropDownListBoxWidgetExt() ;
	JLabel jLabel13 = new JLabel() ;
	DropDownListBoxWidgetExt spectroscopy_mask = new DropDownListBoxWidgetExt() ;
	JLabel jLabel14 = new JLabel() ;
	DropDownListBoxWidgetExt spectroscopy_sampling = new DropDownListBoxWidgetExt() ;
	JLabel jLabel12 = new JLabel() ;
	JLabel jLabel15 = new JLabel() ;
	JLabel jLabel16 = new JLabel() ;
	TextBoxWidgetExt spectroscopy_wavelength = new TextBoxWidgetExt() ;
	TextBoxWidgetExt spectroscopy_posAngle = new TextBoxWidgetExt() ;
	TextBoxWidgetExt spectroscopy_resolvingPower = new TextBoxWidgetExt() ;
	JLabel jLabel17 = new JLabel() ;
	JLabel jLabel18 = new JLabel() ;
	TextBoxWidgetExt spectroscopy_fieldOfView = new TextBoxWidgetExt() ;
	TextBoxWidgetExt spectroscopy_coverage = new TextBoxWidgetExt() ;
	JLabel jLabel19 = new JLabel() ;
	TextBoxWidgetExt spectroscopy_filter = new TextBoxWidgetExt() ;
	JLabel jLabel20 = new JLabel() ;
	TextBoxWidgetExt spectroscopy_order = new TextBoxWidgetExt() ;
	CommandButtonWidgetExt spectroscopy_default = new CommandButtonWidgetExt() ;
	JLabel jLabel21 = new JLabel() ;

	public MichelleGUI()
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
		titledBorder1 = new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Data Acquisition" ) ;
		titledBorder2 = new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Imaging Configuration" ) ;
		titledBorder3 = new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Spectroscopy Configuration" ) ;
		this.setLayout( borderLayout1 ) ;
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel1.setForeground( Color.black ) ;
		jLabel1.setText( "Camera" ) ;
		polarimetry.setText( "Polarimetry" ) ;
		polarimetry.setHorizontalAlignment( SwingConstants.RIGHT ) ;
		polarimetry.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jPanel2.setBorder( titledBorder1 ) ;
		jPanel2.setLayout( gridBagLayout2 ) ;
		modePanel.setLayout( cardLayout1 ) ;
		imagingPanel.setBorder( titledBorder2 ) ;
		imagingPanel.setLayout( gridBagLayout1 ) ;
		spectroscopyPanel.setBorder( titledBorder3 ) ;
		spectroscopyPanel.setLayout( gridBagLayout3 ) ;
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel2.setForeground( Color.black ) ;
		jLabel2.setText( "Filter Category" ) ;
		jLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel3.setForeground( Color.black ) ;
		jLabel3.setText( "Filter" ) ;
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel4.setForeground( Color.black ) ;
		jLabel4.setText( "Field of View" ) ;
		jLabel5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel5.setForeground( Color.black ) ;
		jLabel5.setText( "(arcsec)" ) ;
		imaging_fieldOfView.setBackground( new Color( 204 , 204 , 204 ) ) ;
		imaging_fieldOfView.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		imaging_fieldOfView.setEditable( false ) ;
		dataAcq_dutyCycle.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		dataAcq_dutyCycle.setEditable( false ) ;
		dataAcq_dutyCycle.setBackground( new Color( 204 , 204 , 204 ) ) ;
		dataAcq_chopFrequency.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		dataAcq_chopFrequency.setEditable( false ) ;
		dataAcq_chopFrequency.setBackground( new Color( 204 , 204 , 204 ) ) ;
		jLabel6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel6.setForeground( Color.black ) ;
		jLabel6.setText( "Chop Frequency" ) ;
		jLabel7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel7.setForeground( Color.black ) ;
		jLabel7.setText( "Detector Duty Cycle" ) ;
		jLabel8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel8.setForeground( Color.black ) ;
		jLabel8.setText( "Hz" ) ;
		jLabel9.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel9.setForeground( Color.black ) ;
		jLabel9.setText( "%" ) ;
		jLabel10.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel10.setForeground( Color.black ) ;
		jLabel10.setText( "Exposure Time (sec)" ) ;
		jLabel11.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel11.setForeground( Color.black ) ;
		jLabel11.setText( "Observation Time (sec)" ) ;
		dataAcq_defaultExpTime.setText( "Default" ) ;
		dataAcq_defaultObsTime.setText( "Default" ) ;
		jLabel12b.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel12b.setForeground( Color.black ) ;
		jLabel12b.setText( "Grating" ) ;
		jLabel13.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel13.setForeground( Color.black ) ;
		jLabel13.setText( "Focal Plane Mask" ) ;
		jLabel14.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel14.setForeground( Color.black ) ;
		jLabel14.setText( "Pixel Sampling" ) ;
		jLabel12.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel12.setForeground( Color.black ) ;
		jLabel12.setText( "Wavelength" ) ;
		jLabel15.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel15.setForeground( Color.black ) ;
		jLabel15.setText( "Position Angle" ) ;
		jLabel16.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel16.setForeground( Color.black ) ;
		jLabel16.setText( "Resolving Power" ) ;
		spectroscopy_resolvingPower.setBackground( new Color( 204 , 204 , 204 ) ) ;
		spectroscopy_resolvingPower.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		spectroscopy_resolvingPower.setEditable( false ) ;
		jLabel17.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel17.setForeground( Color.black ) ;
		jLabel17.setText( "Field of View" ) ;
		jLabel18.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel18.setForeground( Color.black ) ;
		jLabel18.setText( "Spectral Coverage" ) ;
		spectroscopy_coverage.setBackground( new Color( 204 , 204 , 204 ) ) ;
		spectroscopy_coverage.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		spectroscopy_coverage.setEditable( false ) ;
		spectroscopy_fieldOfView.setBackground( new Color( 204 , 204 , 204 ) ) ;
		spectroscopy_fieldOfView.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		spectroscopy_fieldOfView.setEditable( false ) ;
		jLabel19.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel19.setForeground( Color.black ) ;
		jLabel19.setText( "Filter" ) ;
		spectroscopy_filter.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				spectroscopy_filter_actionPerformed( e ) ;
			}
		} ) ;
		jLabel20.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel20.setForeground( Color.black ) ;
		jLabel20.setText( "Order" ) ;
		spectroscopy_filter.setBackground( new Color( 204 , 204 , 204 ) ) ;
		spectroscopy_filter.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		spectroscopy_filter.setEditable( false ) ;
		spectroscopy_default.setText( "Default" ) ;
		jLabel21.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel21.setForeground( Color.black ) ;
		jLabel21.setText( "(deg E of N)" ) ;
		imaging_filterCategory.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		imaging_filter.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		camera.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		spectroscopy_grating.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		spectroscopy_mask.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		spectroscopy_sampling.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		this.add( jPanel1 , BorderLayout.NORTH ) ;
		jPanel1.add( jLabel1 , null ) ;
		jPanel1.add( camera , null ) ;
		jPanel1.add( polarimetry , null ) ;
		this.add( jPanel2 , BorderLayout.SOUTH ) ;
		jPanel2.add( dataAcq_dutyCycle , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 72 , 0 ) ) ;
		jPanel2.add( dataAcq_chopFrequency , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 72 , 0 ) ) ;
		jPanel2.add( jLabel6 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( jLabel7 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 9 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( jLabel8 , new GridBagConstraints( 1 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 36 ) , 0 , 0 ) ) ;
		jPanel2.add( jLabel9 , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( jLabel10 , new GridBagConstraints( 2 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( jLabel11 , new GridBagConstraints( 2 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_exposureTime , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_observationTime , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_defaultExpTime , new GridBagConstraints( 3 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_defaultObsTime , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( modePanel , BorderLayout.CENTER ) ;
		modePanel.add( imagingPanel , "imaging" ) ;
		imagingPanel.add( jLabel2 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( imaging_filterCategory , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 28 ) , 0 , 0 ) ) ;
		imagingPanel.add( jLabel3 , new GridBagConstraints( 1 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( imaging_filter , new GridBagConstraints( 1 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( jLabel4 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 40 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( jLabel5 , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( imaging_fieldOfView , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 103 , 0 ) ) ;
		modePanel.add( spectroscopyPanel , "spectroscopy" ) ;
		spectroscopyPanel.add( jLabel12b , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_grating , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 18 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel13 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 9 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_mask , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel14 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 8 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_sampling , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel12 , new GridBagConstraints( 1 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel15 , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel16 , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_wavelength , new GridBagConstraints( 1 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 90 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_posAngle , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 90 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_resolvingPower , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 90 , 0 ) ) ;
		spectroscopyPanel.add( jLabel17 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 9 , 0 , 0 , 49 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel18 , new GridBagConstraints( 1 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 15 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_fieldOfView , new GridBagConstraints( 0 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 90 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_coverage , new GridBagConstraints( 1 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 90 , 0 ) ) ;
		spectroscopyPanel.add( jLabel19 , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 26 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_filter , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel20 , new GridBagConstraints( 2 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_order , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_default , new GridBagConstraints( 3 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel21 , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
	}

	void spectroscopy_filter_actionPerformed( ActionEvent e ){}
}
