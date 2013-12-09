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

import java.awt.BorderLayout ;
import java.awt.CardLayout ;
import java.awt.GridBagLayout ;
import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.BorderFactory ;
import javax.swing.SwingConstants ;
import javax.swing.border.TitledBorder ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.OptionWidgetExt ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

@SuppressWarnings( "serial" )
public class UistGUI extends JPanel
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
	TitledBorder titledBorder3 ;
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JLabel jLabel2 = new JLabel() ;
	DropDownListBoxWidgetExt imaging_imagerList = new DropDownListBoxWidgetExt() ;
	JLabel jLabel3 = new JLabel() ;
	DropDownListBoxWidgetExt imaging_filter = new DropDownListBoxWidgetExt() ;
	JLabel jLabel4 = new JLabel() ;
	TextBoxWidgetExt imaging_fieldOfView = new TextBoxWidgetExt() ;
	GridBagLayout gridBagLayout2 = new GridBagLayout() ;
	TextBoxWidgetExt dataAcq_dutyCycle = new TextBoxWidgetExt() ;
	TextBoxWidgetExt dataAcq_chopFrequency = new TextBoxWidgetExt() ;
	JLabel jLabel6 = new JLabel() ;
	JLabel jLabel7 = new JLabel() ;
	JLabel jLabel10 = new JLabel() ;
	JLabel jLabel11 = new JLabel() ;
	JLabel jLabel24 = new JLabel() ;
	JLabel jLabel25 = new JLabel() ;

	// Added jLabel26, jLabel27, and jLabel28 for Readout, Mode, and Area labels for testing by RDK 30 Dec 2002
	JLabel jLabel26 = new JLabel() ;
	JLabel jLabel27 = new JLabel() ;
	JLabel jLabel28 = new JLabel() ;

	TextBoxWidgetExt dataAcq_exposureTime = new TextBoxWidgetExt() ;

	// Commented for testing by RDK 30 Dec 2002
	TextBoxWidgetExt dataAcq_actualExposureTime = new TextBoxWidgetExt() ;
	TextBoxWidgetExt dataAcq_actualObservationTime = new TextBoxWidgetExt() ;
	CommandButtonWidgetExt dataAcq_defaultExpTime = new CommandButtonWidgetExt() ;
	JLabel jLabel12 = new JLabel() ;
	TextBoxWidgetExt dataAcq_coadds = new TextBoxWidgetExt() ;
	OptionWidgetExt filterBroadBand = new OptionWidgetExt() ;
	OptionWidgetExt filterNarrowBand = new OptionWidgetExt() ;
	JLabel jLabel5 = new JLabel() ;
	TextBoxWidgetExt imaging_bandpass = new TextBoxWidgetExt() ;
	TextBoxWidgetExt spectroscopy_order = new TextBoxWidgetExt() ;

	// Added imaging_pupilCamera for testing by RDK 30 Dec 2002
	CheckBoxWidgetExt imaging_pupilCamera = new CheckBoxWidgetExt() ;
	GridBagLayout gridBagLayout3 = new GridBagLayout() ;
	JLabel jLabel19 = new JLabel() ;
	JLabel jLabel12b = new JLabel() ;
	JLabel jLabel18 = new JLabel() ;
	JLabel jLabel17 = new JLabel() ;
	JLabel jLabel15 = new JLabel() ;
	JLabel maskLabel = new JLabel() ;
	DropDownListBoxWidgetExt spectroscopy_mask = new DropDownListBoxWidgetExt() ;
	TextBoxWidgetExt spectroscopy_posAngle = new TextBoxWidgetExt() ;
	TextBoxWidgetExt spectroscopy_filter = new TextBoxWidgetExt() ;
	TextBoxWidgetExt spectroscopy_coverage = new TextBoxWidgetExt() ;
	JLabel jLabel21 = new JLabel() ;
	JLabel jLabel20 = new JLabel() ;
	TextBoxWidgetExt spectroscopy_resolution = new TextBoxWidgetExt() ;
	DropDownListBoxWidgetExt spectroscopy_sourceMag = new DropDownListBoxWidgetExt() ;
	DropDownListBoxWidgetExt spectroscopy_grism = new DropDownListBoxWidgetExt() ;
	JPanel spectroscopyPanel = new JPanel() ;
	TextBoxWidgetExt spectroscopy_fieldOfView = new TextBoxWidgetExt() ;
	JLabel jLabel22 = new JLabel() ;
	DropDownListBoxWidgetExt imaging_sourceMag = new DropDownListBoxWidgetExt() ;
	JLabel jLabel8 = new JLabel() ;
	DropDownListBoxWidgetExt dataAcq_readMode = new DropDownListBoxWidgetExt() ;
	DropDownListBoxWidgetExt dataAcq_readArea = new DropDownListBoxWidgetExt() ;
	DropDownListBoxWidgetExt imaging_and_polarimetry_mask = new DropDownListBoxWidgetExt() ;
	TextBoxWidgetExt imaging_and_polarimetry_posAngle = new TextBoxWidgetExt() ;
	JLabel imaging_and_polarimetry_posAngleLabel = new JLabel() ;
	JLabel imaging_and_polarimetry_maskLabel = new JLabel() ;

	public UistGUI()
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
		jLabel1.setText( "Configuration" ) ;
		polarimetry.setText( "Polarimetry" ) ;
		polarimetry.setHorizontalAlignment( SwingConstants.RIGHT ) ;
		polarimetry.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jPanel2.setBorder( titledBorder1 ) ;
		jPanel2.setMinimumSize( new Dimension( 369 , 180 ) ) ;
		jPanel2.setPreferredSize( new Dimension( 369 , 180 ) ) ;
		jPanel2.setActionMap( null ) ;
		jPanel2.setLayout( gridBagLayout2 ) ;
		modePanel.setLayout( cardLayout1 ) ;
		imagingPanel.setBorder( titledBorder2 ) ;
		imagingPanel.setMinimumSize( new Dimension( 369 , 210 ) ) ;
		imagingPanel.setPreferredSize( new Dimension( 369 , 210 ) ) ;
		imagingPanel.setToolTipText( "" ) ;
		imagingPanel.setLayout( gridBagLayout1 ) ;
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel2.setForeground( Color.black ) ;
		jLabel2.setToolTipText( "" ) ;
		// Changed text in jLabel2 from Camera/platescale to Plate scale for testing by RDK 30 Dec 2002
		jLabel2.setText( "Plate scale" ) ;
		jLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel3.setForeground( Color.black ) ;
		jLabel3.setHorizontalAlignment( SwingConstants.CENTER ) ;
		jLabel3.setText( "Filter selection" ) ;
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel4.setForeground( Color.black ) ;
		jLabel4.setText( "Field of view" ) ;
		imaging_fieldOfView.setBackground( new Color( 220 , 220 , 220 ) ) ;
		imaging_fieldOfView.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		imaging_fieldOfView.setEditable( false ) ;
		// Added imaging_pupilCamera for testing by RDK 30 Dec 2002
		imaging_pupilCamera.setText( "Pupil imaging mode" ) ;
		imaging_pupilCamera.setHorizontalAlignment( SwingConstants.RIGHT ) ;
		imaging_pupilCamera.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		dataAcq_actualExposureTime.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		dataAcq_actualExposureTime.setEditable( false ) ;
		dataAcq_actualExposureTime.setBackground( new Color( 220 , 220 , 220 ) ) ;
		dataAcq_actualObservationTime.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		dataAcq_actualObservationTime.setEditable( false ) ;
		dataAcq_actualObservationTime.setBackground( new Color( 220 , 220 , 220 ) ) ;
		dataAcq_dutyCycle.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		dataAcq_dutyCycle.setEditable( false ) ;
		dataAcq_dutyCycle.setBackground( new Color( 220 , 220 , 220 ) ) ;
		dataAcq_chopFrequency.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		dataAcq_chopFrequency.setEditable( false ) ;
		dataAcq_chopFrequency.setBackground( new Color( 220 , 220 , 220 ) ) ;
		jLabel6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel6.setForeground( Color.black ) ;
		jLabel6.setToolTipText( "" ) ;
		jLabel6.setText( "Chop freq (Hz)" ) ;
		jLabel7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel7.setForeground( Color.black ) ;
		jLabel7.setToolTipText( "" ) ;
		jLabel7.setText( "Duty cycle (%)" ) ;
		jLabel10.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel10.setForeground( Color.black ) ;
		jLabel10.setText( "Exposure (sec)" ) ;
		jLabel11.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel11.setForeground( Color.black ) ;
		jLabel11.setToolTipText( "" ) ;
		jLabel11.setText( "Observation (sec)" ) ;
		jLabel24.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel24.setForeground( Color.black ) ;
		jLabel24.setText( "Demand:" ) ;
		jLabel25.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel25.setForeground( Color.black ) ;
		jLabel25.setText( "Actual:" ) ;
		jLabel26.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel26.setForeground( Color.black ) ;
		jLabel26.setText( "Readout:" ) ;
		jLabel27.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel27.setForeground( Color.black ) ;
		jLabel27.setText( "Mode" ) ;
		jLabel28.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel28.setForeground( Color.black ) ;
		jLabel28.setText( "Area" ) ;
		dataAcq_defaultExpTime.setBorder( BorderFactory.createRaisedBevelBorder() ) ;
		dataAcq_defaultExpTime.setText( "Default" ) ;
		imaging_imagerList.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		imaging_imagerList.setBackground( Color.white ) ;
		imaging_filter.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		imaging_filter.setBackground( Color.white ) ;
		camera.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		camera.setBackground( Color.white ) ;
		titledBorder2.setTitle( "Imaging" ) ;
		titledBorder3.setTitle( "Spectroscopy" ) ;
		this.setMinimumSize( new Dimension( 369 , 450 ) ) ;
		this.setPreferredSize( new Dimension( 369 , 450 ) ) ;
		jLabel12.setText( "Coadds" ) ;
		jLabel12.setForeground( Color.black ) ;
		jLabel12.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		filterBroadBand.setText( "Broad-band" ) ;
		filterBroadBand.setDescription( "" ) ;
		filterBroadBand.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		filterBroadBand.setActionCommand( "Broad" ) ;
		filterNarrowBand.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		filterNarrowBand.setActionCommand( "Narrow" ) ;
		filterNarrowBand.setText( "Narrow-band" ) ;
		jLabel5.setText( "Bandpass" ) ;
		jLabel5.setForeground( Color.black ) ;
		jLabel5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		imaging_bandpass.setEditable( false ) ;
		imaging_bandpass.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		imaging_bandpass.setBackground( new Color( 220 , 220 , 220 ) ) ;
		spectroscopy_order.setBackground( new Color( 220 , 220 , 220 ) ) ;
		spectroscopy_order.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		spectroscopy_order.setEditable( false ) ;
		spectroscopy_order.setHorizontalAlignment( SwingConstants.CENTER ) ;
		jLabel19.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel19.setForeground( Color.black ) ;
		jLabel19.setText( "Filter" ) ;
		jLabel12b.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel12b.setForeground( Color.black ) ;
		jLabel12b.setText( "Grism" ) ;
		jLabel18.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel18.setForeground( Color.black ) ;
		jLabel18.setText( "Spectral coverage" ) ;
		jLabel17.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel17.setForeground( Color.black ) ;
		jLabel17.setToolTipText( "" ) ;
		jLabel17.setText( "Field of view" ) ;
		jLabel15.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel15.setForeground( Color.black ) ;
		jLabel15.setToolTipText( "" ) ;
		jLabel15.setText( "Pos angle (deg E of N)" ) ;
		maskLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		maskLabel.setForeground( Color.black ) ;
		maskLabel.setText( "Slit mask" ) ;
		spectroscopy_mask.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		spectroscopy_mask.setBackground( Color.white ) ;
		spectroscopy_filter.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				spectroscopy_filter_actionPerformed( e ) ;
			}
		} ) ;
		spectroscopy_filter.setBackground( new Color( 220 , 220 , 220 ) ) ;
		spectroscopy_filter.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		spectroscopy_filter.setEditable( false ) ;
		spectroscopy_coverage.setBackground( new Color( 220 , 220 , 220 ) ) ;
		spectroscopy_coverage.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		spectroscopy_coverage.setEditable( false ) ;
		jLabel21.setText( "Resolution" ) ;
		jLabel21.setForeground( Color.black ) ;
		jLabel21.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel20.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel20.setForeground( Color.black ) ;
		jLabel20.setText( "Order" ) ;
		spectroscopy_resolution.setHorizontalAlignment( SwingConstants.CENTER ) ;
		spectroscopy_resolution.setEditable( false ) ;
		spectroscopy_resolution.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		spectroscopy_resolution.setBackground( new Color( 220 , 220 , 220 ) ) ;
		spectroscopy_sourceMag.setPreferredSize( new Dimension( 50 , 26 ) ) ;
		spectroscopy_sourceMag.setAutoscrolls( true ) ;
		spectroscopy_sourceMag.setBackground( Color.white ) ;
		spectroscopy_sourceMag.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		spectroscopy_sourceMag.setAlignmentX( ( float )0. ) ;
		spectroscopy_grism.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		spectroscopy_grism.setBackground( Color.white ) ;
		spectroscopyPanel.setBorder( titledBorder3 ) ;
		spectroscopyPanel.setMinimumSize( new Dimension( 369 , 180 ) ) ;
		spectroscopyPanel.setPreferredSize( new Dimension( 369 , 180 ) ) ;
		spectroscopyPanel.setToolTipText( "" ) ;
		spectroscopyPanel.setActionMap( null ) ;
		spectroscopyPanel.setLayout( gridBagLayout3 ) ;
		spectroscopy_fieldOfView.setBackground( new Color( 220 , 220 , 220 ) ) ;
		spectroscopy_fieldOfView.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		spectroscopy_fieldOfView.setEditable( false ) ;
		modePanel.setMinimumSize( new Dimension( 400 , 150 ) ) ;
		modePanel.setPreferredSize( new Dimension( 400 , 150 ) ) ;
		jLabel22.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel22.setForeground( Color.black ) ;
		jLabel22.setText( "Source mag" ) ;
		imaging_sourceMag.setAlignmentX( ( float )0. ) ;
		imaging_sourceMag.setBackground( Color.white ) ;
		imaging_sourceMag.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		imaging_sourceMag.setAutoscrolls( true ) ;
		imaging_sourceMag.setPreferredSize( new Dimension( 50 , 26 ) ) ;
		jLabel8.setText( "Source mag" ) ;
		jLabel8.setHorizontalAlignment( SwingConstants.CENTER ) ;
		jLabel8.setForeground( Color.black ) ;
		jLabel8.setToolTipText( "" ) ;
		jLabel8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		dataAcq_readMode.setBackground( Color.white ) ;
		dataAcq_readMode.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		dataAcq_readArea.setBackground( Color.white ) ;
		dataAcq_readArea.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;

		imaging_and_polarimetry_posAngleLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		imaging_and_polarimetry_posAngleLabel.setForeground( Color.black ) ;
		imaging_and_polarimetry_posAngleLabel.setToolTipText( "" ) ;
		imaging_and_polarimetry_posAngleLabel.setText( "Pos angle (deg E of N)" ) ;

		imaging_and_polarimetry_maskLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		imaging_and_polarimetry_maskLabel.setForeground( Color.black ) ;
		imaging_and_polarimetry_maskLabel.setText( "Slit mask" ) ;

		// Incremented by 1 row numbers of all dataAcqPanel items to make room for Readout Mode and Readout Area labels for testing by RDK 30 Dec 2002
		// Then rearranged to put exposure time stuff at top of panel and read mode/area below that
		jPanel2.add( dataAcq_defaultExpTime , new GridBagConstraints( 4 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 10 , 0 ) ) ;
		jPanel2.add( jLabel10 , new GridBagConstraints( 1 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( jLabel12 , new GridBagConstraints( 2 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( jLabel11 , new GridBagConstraints( 3 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( jLabel24 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_exposureTime , new GridBagConstraints( 1 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_coadds , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 10 , 0 , 10 ) , 50 , 0 ) ) ;
		jPanel2.add( jLabel25 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_actualExposureTime , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_actualObservationTime , new GridBagConstraints( 3 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;

		jPanel2.add( jLabel26 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( jLabel27 , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( jLabel28 , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_readMode , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_readArea , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;

		jPanel2.add( jLabel6 , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( jLabel7 , new GridBagConstraints( 3 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 9 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_dutyCycle , new GridBagConstraints( 3 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( dataAcq_chopFrequency , new GridBagConstraints( 1 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jPanel1 , BorderLayout.NORTH ) ;
		jPanel1.add( jLabel1 , null ) ;
		jPanel1.add( camera , null ) ;
		jPanel1.add( polarimetry , null ) ;
		this.add( modePanel , BorderLayout.CENTER ) ;
		// Added imaging_pupilCamera for testing by RDK 30 Dec 2002
		imagingPanel.add( imaging_pupilCamera , new GridBagConstraints( 0 , 0 , 2 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		// Incremented by 1 row numbers of all imagingPanel items to make room for imaging_pupilCamera for testing by RDK 30 Dec 2002
		imagingPanel.add( jLabel2 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( imaging_imagerList , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( jLabel3 , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 50 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( imaging_filter , new GridBagConstraints( 1 , 4 , 2 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 50 , 0 , 80 ) , 0 , 0 ) ) ;
		imagingPanel.add( jLabel4 , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 10 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( imaging_fieldOfView , new GridBagConstraints( 0 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , -40 ) , 0 , 0 ) ) ;
		imagingPanel.add( filterBroadBand , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 30 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( filterNarrowBand , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 30 ) , 0 , 0 ) ) ;
		imagingPanel.add( jLabel5 , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , -40 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( imaging_bandpass , new GridBagConstraints( 2 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , -40 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( imaging_sourceMag , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 30 , 0 ) ) ;
		imagingPanel.add( jLabel8 , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;

		imagingPanel.add( imaging_and_polarimetry_posAngleLabel , new GridBagConstraints( 2 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		imagingPanel.add( imaging_and_polarimetry_posAngle , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 20 , 0 , 40 ) , 0 , 0 ) ) ;
		imagingPanel.add( imaging_and_polarimetry_mask , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 18 ) , 0 , 0 ) ) ;
		imagingPanel.add( imaging_and_polarimetry_maskLabel , new GridBagConstraints( 3 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;

		this.add( jPanel2 , BorderLayout.SOUTH ) ;
		modePanel.add( imagingPanel , "imagingPanel" ) ;

		// Incremented by 1 row numbers of all spectroscopyPanel items to make room for spectroscopy_targetAcqMode for testing by RDK 30 Dec 2002
		spectroscopyPanel.add( jLabel12b , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_grism , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 18 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel17 , new GridBagConstraints( 2 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_fieldOfView , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel20 , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTH , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_coverage , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel18 , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel19 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_order , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_mask , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 18 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel15 , new GridBagConstraints( 2 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_posAngle , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 20 , 0 , 40 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( maskLabel , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_filter , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 20 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_resolution , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( jLabel21 , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTH , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		spectroscopyPanel.add( spectroscopy_sourceMag , new GridBagConstraints( 1 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 20 , 0 ) ) ;
		spectroscopyPanel.add( jLabel22 , new GridBagConstraints( 1 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		modePanel.add( spectroscopyPanel , "spectroscopyPanel" ) ;
	}

	void spectroscopy_filter_actionPerformed( ActionEvent e ){}
}
