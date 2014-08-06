// Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * @author Allan Brighton
 */

package ot.jcmt.obsComp.editor ;

import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Color ;
import java.awt.Dimension ;
import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.BorderFactory ;
import javax.swing.border.TitledBorder ;
import javax.swing.border.EtchedBorder ;
import javax.swing.border.Border ;
import jsky.app.ot.gui.OptionWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;

@SuppressWarnings( "serial" )
public class SiteQualityGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JPanel jPanel1 = new JPanel() ;
	JPanel jPanel2 = new JPanel() ;
	TitledBorder titledBorder1 ;
	Border border1 ;
	TitledBorder titledBorder2 ;
	TitledBorder titledBorder3 ;
	TitledBorder titledBorder4 ;
	GridBagLayout gridBagLayout2 = new GridBagLayout() ;
	GridBagLayout gridBagLayout3 = new GridBagLayout() ;
	OptionWidgetExt tauBandAllocated = new OptionWidgetExt() ;
	OptionWidgetExt tauBandUserDefined = new OptionWidgetExt() ;
	OptionWidgetExt seeing3 = new OptionWidgetExt() ;
	OptionWidgetExt seeing2 = new OptionWidgetExt() ;
	OptionWidgetExt seeing1 = new OptionWidgetExt() ;
	OptionWidgetExt seeingAny = new OptionWidgetExt() ;
	JLabel jLabel1 = new JLabel() ;
	JLabel jLabel2 = new JLabel() ;
	JLabel jLabel3 = new JLabel() ;
	TextBoxWidgetExt minTau = new TextBoxWidgetExt() ;
	TextBoxWidgetExt maxTau = new TextBoxWidgetExt() ;
	TextBoxWidgetExt noiseCalculationTau = new TextBoxWidgetExt() ;

	public SiteQualityGUI()
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
		titledBorder1 = new TitledBorder( new EtchedBorder( EtchedBorder.RAISED , Color.white , new Color( 142 , 142 , 142 ) ) , "Weather Band" ) ;
		border1 = new EtchedBorder( EtchedBorder.RAISED , Color.white , new Color( 142 , 142 , 142 ) ) ;
		titledBorder2 = new TitledBorder( border1 , "Seeing" ) ;
		titledBorder3 = new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Moon" ) ;
		titledBorder4 = new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Sky" ) ;
		this.setMinimumSize( new Dimension( 279 , 276 ) ) ;
		this.setPreferredSize( new Dimension( 279 , 276 ) ) ;
		this.setLayout( gridBagLayout1 ) ;
		jPanel1.setBorder( titledBorder1 ) ;
		jPanel1.setLayout( gridBagLayout2 ) ;
		jPanel2.setBorder( titledBorder2 ) ;
		jPanel2.setLayout( gridBagLayout3 ) ;
		tauBandAllocated.setText( "Allocated" ) ;
		tauBandAllocated.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		tauBandUserDefined.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		seeing3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		seeing3.setText( "<= 3.0" ) ;
		seeing2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		seeing2.setText( "<= 1.0" ) ;
		seeing1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		seeing1.setText( "<= 0.5" ) ;
		seeingAny.setText( "Don\'t care" ) ;
		seeingAny.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel1.setForeground( Color.black ) ;
		jLabel1.setText( "Min \u03C4" ) ;
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel2.setForeground( Color.black ) ;
		jLabel2.setText( "Max \u03C4" ) ;
		jLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel3.setForeground( Color.black ) ;
		jLabel3.setText( "\u03C4 for noise calculation" ) ;
		this.add( jPanel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		jPanel1.add( tauBandAllocated , new GridBagConstraints( 0 , 0 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel1.add( tauBandUserDefined , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel1.add( jLabel1 , new GridBagConstraints( 1 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel1.add( jLabel2 , new GridBagConstraints( 1 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel1.add( minTau , new GridBagConstraints( 2 , 1 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		jPanel1.add( maxTau , new GridBagConstraints( 2 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		jPanel1.add( jLabel3 , new GridBagConstraints( 0 , 3 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 30 , 5 , 1 , 5 ) , 0 , 0 ) ) ;
		jPanel1.add( noiseCalculationTau , new GridBagConstraints( 0 , 4 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 1 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jPanel2 , new GridBagConstraints( 1 , 0 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		jPanel2.add( seeing3 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( seeing2 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( seeing1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel2.add( seeingAny , new GridBagConstraints( 0 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
	}
}
