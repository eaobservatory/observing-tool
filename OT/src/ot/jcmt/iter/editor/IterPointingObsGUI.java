/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot.jcmt.iter.editor ;

import javax.swing.JCheckBox;
import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.JPopupMenu ;
import javax.swing.BoxLayout ;
import javax.swing.BorderFactory ;
import javax.swing.SwingConstants ;
import javax.swing.border.Border ;
import javax.swing.border.BevelBorder ;
import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Color ;
import java.awt.Dimension ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.OptionWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;

@SuppressWarnings( "serial" )
public class IterPointingObsGUI extends IterJCMTGenericGUI
{
	JPanel acsisPanel = new JPanel() ;
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JLabel jLabel1 = new JLabel() ;
	JLabel jLabel2 = new JLabel() ;
	DropDownListBoxWidgetExt pointingMethod = new DropDownListBoxWidgetExt() ;
	JLabel jLabel3 = new JLabel() ;
	JLabel jLabel4 = new JLabel() ;
	JLabel jLabel5 = new JLabel() ;
	JLabel jLabel6 = new JLabel() ;
	JLabel jLabel7 = new JLabel() ;
	TextBoxWidgetExt secsPerObservation = new TextBoxWidgetExt() ;
	OptionWidgetExt continuum = new OptionWidgetExt() ;
	OptionWidgetExt spectralLine = new OptionWidgetExt() ;
	JLabel jLabel8 = new JLabel() ;
	CommandButtonWidgetExt pointingPixelButton = new CommandButtonWidgetExt() ;
	JPopupMenu pointingPixelPopupMenu = new JPopupMenu() ;
        JCheckBox fts2_in_beam = new JCheckBox(
                "FTS-2 in beam", false);
        JCheckBox pol2_in_beam = new JCheckBox(
                "POL-2 in beam", false);

	public IterPointingObsGUI()
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
		JPanel pointingPanel = new JPanel() ;
		Border bevelBorder = BorderFactory.createBevelBorder( BevelBorder.LOWERED ) ;
		Border titleBorder = BorderFactory.createTitledBorder( bevelBorder , "Pointing setup" ) ;
		pointingPanel.setBorder( titleBorder ) ;
		pointingPanel.setLayout( new BoxLayout(pointingPanel, BoxLayout.Y_AXIS) ) ;
		acsisPanel.setLayout( gridBagLayout1 ) ;
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel1.setForeground( Color.black ) ;
		jLabel1.setText( "Pointing Method" ) ;
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel2.setForeground( Color.black ) ;
		jLabel2.setText( "Step Size" ) ;
		jLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel3.setForeground( Color.black ) ;
		jLabel3.setText( "(arc sec)" ) ;
		stepSize.setColumns( 6 ) ;
		jiggleAtReference.setText( "Jiggle at Reference" ) ;
		jiggleAtReference.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel4.setForeground( Color.black ) ;
		jLabel4.setText( "Secs/Cycle" ) ;
		jLabel6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel6.setForeground( Color.black ) ;
		jLabel6.setText( "Secs/Cycle" ) ;
		jLabel7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel7.setForeground( Color.black ) ;
		jLabel7.setText( "Secs/Observation" ) ;
		continuum.setText( "Continuum" ) ;
		continuum.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		spectralLine.setText( "Spectral Line" ) ;
		spectralLine.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel8.setForeground( Color.black ) ;
		jLabel8.setText( "Pointing Pixel" ) ;
		pointingPixelButton.setText( "Automatic" ) ;
		pointingPixelButton.setSelected( true ) ;
		secsPerCycle.setEditable( true ) ;
		secsPerCycle.setColumns( 2 ) ;
		secsPerObservation.setEditable( false ) ;
		secsPerObservation.setColumns( 2 ) ;
		pointingMethod.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		automaticTarget.setPreferredSize( new Dimension( 190 , 50 ) ) ;
		automaticTarget.setText( "Automatic target" ) ;
		automaticTarget.setHorizontalAlignment( SwingConstants.CENTER ) ;
		automaticTarget.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		automaticTarget.setVerticalAlignment( SwingConstants.TOP ) ;
		this.add( pointingPanel ) ;
		acsisPanel.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		acsisPanel.add( pointingMethod , new GridBagConstraints( 1 , 0 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		acsisPanel.add( jLabel4 , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		acsisPanel.add( jLabel7 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		acsisPanel.add( secsPerCycle , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		acsisPanel.add( secsPerObservation , new GridBagConstraints( 1 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		acsisPanel.add( continuum , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 40 , 0 , 0 ) , 0 , 0 ) ) ;
		acsisPanel.add( spectralLine , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.NORTHWEST , GridBagConstraints.NONE , new Insets( 0 , 40 , 0 , 0 ) , 0 , 0 ) ) ;
		pointingPanel.add( automaticTarget ) ;
                pointingPanel.add(fts2_in_beam);
                pointingPanel.add(pol2_in_beam);
	}
}
