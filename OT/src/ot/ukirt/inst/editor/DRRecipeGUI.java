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

package ot.ukirt.inst.editor ;


import java.awt.GridBagLayout ;
import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.BorderLayout ;
import java.awt.CardLayout ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;

import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TableWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;

@SuppressWarnings( "serial" )
public class DRRecipeGUI extends JPanel
{
	CardLayout cardLayout1 = new CardLayout() ;
	BorderLayout borderLayoutE1 = new BorderLayout() ;
	JPanel emptyPanel = new JPanel() ;
	JLabel jLabelE1 = new JLabel() ;

	// Initialise GUI components by instrument.

	// CGS4
	JPanel cgs4Panel = new JPanel() ;
	JPanel jPanelC1 = new JPanel() ;
	JLabel jLabelC1 = new JLabel() ;
	JLabel jLabelC2 = new JLabel() ;
	JLabel jLabelC3 = new JLabel() ;
	JLabel jLabelC4 = new JLabel() ;
	JLabel jLabelC5 = new JLabel() ;
	JLabel jLabelC6 = new JLabel() ;
	JLabel jLabelC7 = new JLabel() ;
	JLabel jLabelC8 = new JLabel() ;
	JLabel jLabelC9 = new JLabel() ;
	GridBagLayout gridBagLayoutC1 = new GridBagLayout() ;
	BorderLayout borderLayoutC1 = new BorderLayout() ;
	TextBoxWidgetExt cgs4_arcRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt cgs4_biasRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt cgs4_darkRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt cgs4_flatRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt cgs4_objectRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt cgs4_skyRecipe = new TextBoxWidgetExt() ;
	CheckBoxWidgetExt cgs4_arcInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt cgs4_biasInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt cgs4_darkInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt cgs4_flatInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt cgs4_objectInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt cgs4_skyInGroup = new CheckBoxWidgetExt() ;
	CommandButtonWidgetExt cgs4_arcSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt cgs4_biasSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt cgs4_darkSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt cgs4_flatSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt cgs4_objectSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt cgs4_skySet = new CommandButtonWidgetExt() ;

	// IRCAM3
	JPanel ircam3Panel = new JPanel() ;
	JPanel jPanelI1 = new JPanel() ;
	JLabel jLabelI1 = new JLabel() ;
	JLabel jLabelI2 = new JLabel() ;
	JLabel jLabelI3 = new JLabel() ;
	JLabel jLabelI4 = new JLabel() ;
	JLabel jLabelI5 = new JLabel() ;
	JLabel jLabelI6 = new JLabel() ;
	JLabel jLabelI7 = new JLabel() ;
	GridBagLayout gridBagLayoutI1 = new GridBagLayout() ;
	BorderLayout borderLayoutI1 = new BorderLayout() ;
	TextBoxWidgetExt ircam3_darkRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt ircam3_biasRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt ircam3_objectRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt ircam3_skyRecipe = new TextBoxWidgetExt() ;
	CheckBoxWidgetExt ircam3_biasInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt ircam3_darkInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt ircam3_objectInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt ircam3_skyInGroup = new CheckBoxWidgetExt() ;
	CommandButtonWidgetExt ircam3_biasSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt ircam3_darkSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt ircam3_objectSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt ircam3_skySet = new CommandButtonWidgetExt() ;

	// Michelle
	JPanel michellePanel = new JPanel() ;
	JPanel jPanelM1 = new JPanel() ;
	JLabel jLabelM1 = new JLabel() ;
	JLabel jLabelM2 = new JLabel() ;
	JLabel jLabelM3 = new JLabel() ;
	JLabel jLabelM4 = new JLabel() ;
	JLabel jLabelM5 = new JLabel() ;
	JLabel jLabelM6 = new JLabel() ;
	JLabel jLabelM7 = new JLabel() ;
	JLabel jLabelM8 = new JLabel() ;
	JLabel jLabelM9 = new JLabel() ;
	GridBagLayout gridBagLayoutM1 = new GridBagLayout() ;
	BorderLayout borderLayoutM1 = new BorderLayout() ;
	TextBoxWidgetExt michelle_arcRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt michelle_biasRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt michelle_darkRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt michelle_flatRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt michelle_objectRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt michelle_skyRecipe = new TextBoxWidgetExt() ;
	CheckBoxWidgetExt michelle_arcInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt michelle_biasInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt michelle_darkInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt michelle_flatInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt michelle_objectInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt michelle_skyInGroup = new CheckBoxWidgetExt() ;
	CommandButtonWidgetExt michelle_arcSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt michelle_biasSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt michelle_darkSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt michelle_flatSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt michelle_objectSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt michelle_skySet = new CommandButtonWidgetExt() ;

	// UFTI
	JPanel uftiPanel = new JPanel() ;
	JLabel jLabelF1 = new JLabel() ;
	JPanel jPanelF1 = new JPanel() ;
	JLabel jLabelF2 = new JLabel() ;
	JLabel jLabelF3 = new JLabel() ;
	JLabel jLabelF4 = new JLabel() ;
	JLabel jLabelF5 = new JLabel() ;
	JLabel jLabelF6 = new JLabel() ;
	GridBagLayout gridBagLayoutF1 = new GridBagLayout() ;
	BorderLayout borderLayoutF1 = new BorderLayout() ;
	TextBoxWidgetExt ufti_darkRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt ufti_objectRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt ufti_skyRecipe = new TextBoxWidgetExt() ;
	CheckBoxWidgetExt ufti_darkInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt ufti_skyInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt ufti_objectInGroup = new CheckBoxWidgetExt() ;
	CommandButtonWidgetExt ufti_darkSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt ufti_objectSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt ufti_skySet = new CommandButtonWidgetExt() ;

	// UIST
	JPanel uistPanel = new JPanel() ;
	JPanel jPanelU1 = new JPanel() ;
	JLabel jLabelU1 = new JLabel() ;
	JLabel jLabelU2 = new JLabel() ;
	JLabel jLabelU3 = new JLabel() ;
	JLabel jLabelU4 = new JLabel() ;
	JLabel jLabelU5 = new JLabel() ;
	JLabel jLabelU6 = new JLabel() ;
	JLabel jLabelU7 = new JLabel() ;
	JLabel jLabelU8 = new JLabel() ;
	JLabel jLabelU9 = new JLabel() ;
	GridBagLayout gridBagLayoutU1 = new GridBagLayout() ;
	BorderLayout borderLayoutU1 = new BorderLayout() ;
	TextBoxWidgetExt uist_arcRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt uist_biasRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt uist_darkRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt uist_flatRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt uist_objectRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt uist_skyRecipe = new TextBoxWidgetExt() ;
	CheckBoxWidgetExt uist_arcInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt uist_biasInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt uist_darkInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt uist_flatInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt uist_objectInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt uist_skyInGroup = new CheckBoxWidgetExt() ;
	CommandButtonWidgetExt uist_arcSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt uist_biasSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt uist_darkSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt uist_flatSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt uist_objectSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt uist_skySet = new CommandButtonWidgetExt() ;

	// WFCAM
	JPanel wfcamPanel = new JPanel() ;
	JPanel jPanelW1 = new JPanel() ;
	JLabel jLabelW1 = new JLabel() ;
	JLabel jLabelW2 = new JLabel() ;
	JLabel jLabelW3 = new JLabel() ;
	JLabel jLabelW4 = new JLabel() ;
	JLabel jLabelW5 = new JLabel() ;
	JLabel jLabelW6 = new JLabel() ;
	JLabel jLabelW7 = new JLabel() ;
	JLabel jLabelW8 = new JLabel() ;
	JLabel jLabelW9 = new JLabel() ;
	GridBagLayout gridBagLayoutW1 = new GridBagLayout() ;
	BorderLayout borderLayoutW1 = new BorderLayout() ;
	TextBoxWidgetExt wfcam_focusRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt wfcam_biasRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt wfcam_darkRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt wfcam_flatRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt wfcam_objectRecipe = new TextBoxWidgetExt() ;
	TextBoxWidgetExt wfcam_skyRecipe = new TextBoxWidgetExt() ;
	CheckBoxWidgetExt wfcam_focusInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt wfcam_biasInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt wfcam_darkInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt wfcam_flatInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt wfcam_objectInGroup = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt wfcam_skyInGroup = new CheckBoxWidgetExt() ;
	CommandButtonWidgetExt wfcam_focusSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt wfcam_biasSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt wfcam_darkSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt wfcam_flatSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt wfcam_objectSet = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt wfcam_skySet = new CommandButtonWidgetExt() ;

	// Headings
	JLabel jLabelC11 = new JLabel() ;
	JLabel jLabelI8 = new JLabel() ;
	JLabel jLabelM10 = new JLabel() ;
	JLabel jLabelF7 = new JLabel() ;
	JLabel jLabelU10 = new JLabel() ;
	JLabel jLabelW10 = new JLabel() ;

	// CGS4 recipe selection
	JPanel jPanelC2 = new JPanel() ;
	JPanel jPanelC3 = new JPanel() ;
	BorderLayout borderLayoutC2 = new BorderLayout() ;
	CommandButtonWidgetExt cgs4_defaultName = new CommandButtonWidgetExt() ;
	JScrollPane jScrollPane_cgs4 = new JScrollPane() ;
	TableWidgetExt cgs4_recipeTable = new TableWidgetExt() ;

	// IRCAM3 recipe selection
	JPanel jPanelI2 = new JPanel() ;
	JPanel jPanelI3 = new JPanel() ;
	BorderLayout borderLayoutI2 = new BorderLayout() ;
	CommandButtonWidgetExt ircam3_defaultName = new CommandButtonWidgetExt() ;
	JScrollPane jScrollPane_ircam3 = new JScrollPane() ;
	TableWidgetExt ircam3_recipeTable = new TableWidgetExt() ;

	// Michelle recipe selection
	JPanel jPanelM2 = new JPanel() ;
	JPanel jPanelM3 = new JPanel() ;
	BorderLayout borderLayoutM2 = new BorderLayout() ;
	CommandButtonWidgetExt michelle_defaultName = new CommandButtonWidgetExt() ;
	JScrollPane jScrollPane_michelle = new JScrollPane() ;
	TableWidgetExt michelle_recipeTable = new TableWidgetExt() ;

	// UFTI recipe selection
	JPanel jPanelF2 = new JPanel() ;
	JPanel jPanelF3 = new JPanel() ;
	BorderLayout borderLayoutF2 = new BorderLayout() ;
	CommandButtonWidgetExt ufti_defaultName = new CommandButtonWidgetExt() ;
	JScrollPane jScrollPane_ufti = new JScrollPane() ;
	TableWidgetExt ufti_recipeTable = new TableWidgetExt() ;

	// UIST recipe selection
	JPanel jPanelU2 = new JPanel() ;
	JPanel jPanelU3 = new JPanel() ;

	BorderLayout borderLayoutU2 = new BorderLayout() ;
	CommandButtonWidgetExt uist_defaultName = new CommandButtonWidgetExt() ;
	JScrollPane jScrollPane_uist = new JScrollPane() ;
	TableWidgetExt uist_recipeTable = new TableWidgetExt() ;

	// WFCAM recipe selection
	JPanel jPanelW2 = new JPanel() ;
	JPanel jPanelW3 = new JPanel() ;
	BorderLayout borderLayoutW2 = new BorderLayout() ;
	CommandButtonWidgetExt wfcam_defaultName = new CommandButtonWidgetExt() ;
	JScrollPane jScrollPane_wfcam = new JScrollPane() ;
	TableWidgetExt wfcam_recipeTable = new TableWidgetExt() ;

	// For defaults and selecting user recipe.
	GridBagLayout gridBagLayoutC2 = new GridBagLayout() ;
	GridBagLayout gridBagLayoutI2 = new GridBagLayout() ;
	GridBagLayout gridBagLayoutM2 = new GridBagLayout() ;
	GridBagLayout gridBagLayoutF2 = new GridBagLayout() ;
	GridBagLayout gridBagLayoutU2 = new GridBagLayout() ;
	GridBagLayout gridBagLayoutW2 = new GridBagLayout() ;

	public DRRecipeGUI()
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
		this.setLayout( cardLayout1 ) ;

		emptyPanel.setLayout( borderLayoutE1 ) ;
		jLabelE1.setText( "This panel is deliberately left empty" ) ;
		jLabelE1.setForeground( Color.black ) ;

		// Define headings by instrument.

		// CGS4
		cgs4_objectRecipe.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				cgs4_objectRecipe_actionPerformed( e ) ;
			}
		} ) ;
		cgs4Panel.setLayout( borderLayoutC1 ) ;
		jPanelC1.setLayout( gridBagLayoutC1 ) ;
		jLabelC1.setText( "Recipe Name" ) ;
		jLabelC1.setForeground( Color.black ) ;
		jLabelC1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelC2.setText( "Include in Group?" ) ;
		jLabelC2.setForeground( Color.black ) ;
		jLabelC2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelC3.setText( "OBJECT" ) ;
		jLabelC3.setForeground( Color.black ) ;
		jLabelC3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelC4.setText( "SKY" ) ;
		jLabelC4.setForeground( Color.black ) ;
		jLabelC4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelC5.setText( "DARK" ) ;
		jLabelC5.setForeground( Color.black ) ;
		jLabelC5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelC6.setText( "BIAS" ) ;
		jLabelC6.setForeground( Color.black ) ;
		jLabelC6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelC7.setText( "Observation Type" ) ;
		jLabelC7.setForeground( Color.black ) ;
		jLabelC7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		cgs4_biasRecipe.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				cgs4_biasRecipe_actionPerformed( e ) ;
			}
		} ) ;
		jLabelC8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelC8.setForeground( Color.black ) ;
		jLabelC8.setText( "FLAT" ) ;
		jLabelC9.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelC9.setForeground( Color.black ) ;
		jLabelC9.setText( "ARC" ) ;

		cgs4_skySet.setText( "Set" ) ;
		cgs4_flatSet.setText( "Set" ) ;
		cgs4_darkSet.setText( "Set" ) ;
		cgs4_arcSet.setText( "Set" ) ;
		cgs4_biasSet.setText( "Set" ) ;
		cgs4_objectSet.setText( "Set" ) ;

		// IRCAM3
		ircam3_objectRecipe.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				ircam3_objectRecipe_actionPerformed( e ) ;
			}
		} ) ;
		ircam3Panel.setLayout( borderLayoutI1 ) ;
		jPanelI1.setLayout( gridBagLayoutI1 ) ;
		jLabelI1.setText( "Recipe Name" ) ;
		jLabelI1.setForeground( Color.black ) ;
		jLabelI1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelI2.setText( "Include in Group?" ) ;
		jLabelI2.setForeground( Color.black ) ;
		jLabelI2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelI3.setText( "OBJECT" ) ;
		jLabelI3.setForeground( Color.black ) ;
		jLabelI3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelI4.setText( "SKY" ) ;
		jLabelI4.setForeground( Color.black ) ;
		jLabelI4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelI5.setText( "DARK" ) ;
		jLabelI5.setForeground( Color.black ) ;
		jLabelI5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelI6.setText( "BIAS" ) ;
		jLabelI6.setForeground( Color.black ) ;
		jLabelI6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelI7.setText( "Observation Type" ) ;
		jLabelI7.setForeground( Color.black ) ;
		jLabelI7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		ircam3_biasRecipe.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				ircam3_biasRecipe_actionPerformed( e ) ;
			}
		} ) ;
		ircam3_skySet.setText( "Set" ) ;
		ircam3_biasSet.setText( "Set" ) ;
		ircam3_darkSet.setText( "Set" ) ;
		ircam3_objectSet.setText( "Set" ) ;

		// Michelle
		michelle_objectRecipe.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				michelle_objectRecipe_actionPerformed( e ) ;
			}
		} ) ;
		michellePanel.setLayout( borderLayoutM1 ) ;
		jLabelM1.setText( "Observation Type" ) ;
		jLabelM1.setForeground( Color.black ) ;
		jLabelM1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelM2.setText( "BIAS" ) ;
		jLabelM2.setForeground( Color.black ) ;
		jLabelM2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelM3.setText( "DARK" ) ;
		jLabelM3.setForeground( Color.black ) ;
		jLabelM3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelM4.setText( "SKY" ) ;
		jLabelM4.setForeground( Color.black ) ;
		jLabelM4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelM5.setText( "OBJECT" ) ;
		jLabelM5.setForeground( Color.black ) ;
		jLabelM5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelM6.setText( "Include in Group?" ) ;
		jLabelM6.setForeground( Color.black ) ;
		jLabelM6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		michelle_biasRecipe.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				michelle_biasRecipe_actionPerformed( e ) ;
			}
		} ) ;
		jPanelM1.setLayout( gridBagLayoutM1 ) ;
		jLabelM7.setText( "Recipe Name" ) ;
		jLabelM7.setForeground( Color.black ) ;
		jLabelM7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelM8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelM8.setForeground( Color.black ) ;
		jLabelM8.setText( "ARC" ) ;
		jLabelM9.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelM9.setForeground( Color.black ) ;
		jLabelM9.setText( "FLAT" ) ;

		michelle_darkSet.setText( "Set" ) ;
		michelle_arcSet.setText( "Set" ) ;
		michelle_flatSet.setText( "Set" ) ;
		michelle_skySet.setText( "Set" ) ;
		michelle_objectSet.setText( "Set" ) ;
		michelle_biasSet.setText( "Set" ) ;

		// UFTI
		ufti_objectRecipe.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				ufti_objectRecipe_actionPerformed( e ) ;
			}
		} ) ;
		uftiPanel.setLayout( borderLayoutF1 ) ;
		jLabelF1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelF1.setForeground( Color.black ) ;
		jLabelF1.setText( "Include in Group?" ) ;
		jPanelF1.setLayout( gridBagLayoutF1 ) ;
		jLabelF2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelF2.setForeground( Color.black ) ;
		jLabelF2.setText( "OBJECT" ) ;
		jLabelF3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelF3.setForeground( Color.black ) ;
		jLabelF3.setText( "SKY" ) ;
		jLabelF4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelF4.setForeground( Color.black ) ;
		jLabelF4.setText( "DARK" ) ;
		jLabelF5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelF5.setForeground( Color.black ) ;
		jLabelF5.setText( "Observation Type" ) ;
		ufti_darkSet.setText( "Set" ) ;
		ufti_objectSet.setText( "Set" ) ;
		ufti_skySet.setText( "Set" ) ;
		jLabelF6.setText( "Recipe Name" ) ;
		jLabelF6.setForeground( Color.black ) ;
		jLabelF6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;

		// UIST
		uist_objectRecipe.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				uist_objectRecipe_actionPerformed( e ) ;
			}
		} ) ;
		uistPanel.setLayout( borderLayoutU1 ) ;
		jLabelU1.setText( "Observation Type" ) ;
		jLabelU1.setForeground( Color.black ) ;
		jLabelU1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelU2.setText( "BIAS" ) ;
		jLabelU2.setForeground( Color.black ) ;
		jLabelU2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelU3.setText( "DARK" ) ;
		jLabelU3.setForeground( Color.black ) ;
		jLabelU3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelU4.setText( "SKY" ) ;
		jLabelU4.setForeground( Color.black ) ;
		jLabelU4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelU5.setText( "OBJECT" ) ;
		jLabelU5.setForeground( Color.black ) ;
		jLabelU5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelU6.setText( "Include in Group?" ) ;
		jLabelU6.setForeground( Color.black ) ;
		jLabelU6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		uist_biasRecipe.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				uist_biasRecipe_actionPerformed( e ) ;
			}
		} ) ;
		jPanelU1.setLayout( gridBagLayoutU1 ) ;
		jLabelU7.setText( "Recipe Name" ) ;
		jLabelU7.setForeground( Color.black ) ;
		jLabelU7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelU8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelU8.setForeground( Color.black ) ;
		jLabelU8.setText( "ARC" ) ;
		jLabelU9.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelU9.setForeground( Color.black ) ;
		jLabelU9.setText( "FLAT" ) ;

		uist_darkSet.setText( "Set" ) ;
		uist_arcSet.setText( "Set" ) ;
		uist_flatSet.setText( "Set" ) ;
		uist_skySet.setText( "Set" ) ;
		uist_objectSet.setText( "Set" ) ;
		uist_biasSet.setText( "Set" ) ;

		// WFCAM
		wfcam_objectRecipe.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				wfcam_objectRecipe_actionPerformed( e ) ;
			}
		} ) ;
		wfcamPanel.setLayout( borderLayoutW1 ) ;
		jLabelW1.setText( "Observation Type" ) ;
		jLabelW1.setForeground( Color.black ) ;
		jLabelW1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelW2.setText( "BIAS" ) ;
		jLabelW2.setForeground( Color.black ) ;
		jLabelW2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelW3.setText( "DARK" ) ;
		jLabelW3.setForeground( Color.black ) ;
		jLabelW3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelW4.setText( "SKY" ) ;
		jLabelW4.setForeground( Color.black ) ;
		jLabelW4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelW5.setText( "OBJECT" ) ;
		jLabelW5.setForeground( Color.black ) ;
		jLabelW5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelW6.setText( "Include in Group?" ) ;
		jLabelW6.setForeground( Color.black ) ;
		jLabelW6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		wfcam_biasRecipe.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				wfcam_biasRecipe_actionPerformed( e ) ;
			}
		} ) ;
		jPanelW1.setLayout( gridBagLayoutW1 ) ;
		jLabelW7.setText( "Recipe Name" ) ;
		jLabelW7.setForeground( Color.black ) ;
		jLabelW7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelW8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelW8.setForeground( Color.black ) ;
		jLabelW8.setText( "FOCUS" ) ;
		jLabelW9.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabelW9.setForeground( Color.black ) ;
		jLabelW9.setText( "FLAT" ) ;

		wfcam_darkSet.setText( "Set" ) ;
		wfcam_focusSet.setText( "Set" ) ;
		wfcam_flatSet.setText( "Set" ) ;
		wfcam_skySet.setText( "Set" ) ;
		wfcam_objectSet.setText( "Set" ) ;
		wfcam_biasSet.setText( "Set" ) ;

		// Define the titles
		jLabelC11.setFont( new java.awt.Font( "Dialog" , 1 , 14 ) ) ;
		jLabelC11.setForeground( Color.black ) ;
		jLabelC11.setText( "CGS4 DR Recipe" ) ;
		jLabelI8.setFont( new java.awt.Font( "Dialog" , 1 , 14 ) ) ;
		jLabelI8.setForeground( Color.black ) ;
		jLabelI8.setText( "IRCAM3 DR Recipe" ) ;
		jLabelM10.setFont( new java.awt.Font( "Dialog" , 1 , 14 ) ) ;
		jLabelM10.setForeground( Color.black ) ;
		jLabelM10.setText( "Michelle DR Recipe" ) ;
		jLabelF7.setFont( new java.awt.Font( "Dialog" , 1 , 14 ) ) ;
		jLabelF7.setForeground( Color.black ) ;
		jLabelF7.setText( "UFTI DR Recipe" ) ;
		jLabelU10.setFont( new java.awt.Font( "Dialog" , 1 , 14 ) ) ;
		jLabelU10.setForeground( Color.black ) ;
		jLabelU10.setText( "UIST DR Recipe" ) ;
		jLabelW10.setFont( new java.awt.Font( "Dialog" , 1 , 14 ) ) ;
		jLabelW10.setForeground( Color.black ) ;
		jLabelW10.setText( "WFCAM DR Recipe" ) ;

		// Defaults and user-defined recipe by instrument. 
		jPanelC2.setLayout( borderLayoutC2 ) ;
		cgs4_defaultName.setText( "Reset Defaults" ) ;
		jPanelC3.setLayout( gridBagLayoutC2 ) ;
		jPanelC3.setMinimumSize( new Dimension( 260 , 60 ) ) ;
		jScrollPane_cgs4.setPreferredSize( new Dimension( 100 , 120 ) ) ;

		jPanelI2.setLayout( borderLayoutI2 ) ;
		ircam3_defaultName.setText( "Reset Defaults" ) ;
		jPanelI3.setLayout( gridBagLayoutI2 ) ;
		jPanelI3.setMinimumSize( new Dimension( 260 , 60 ) ) ;

		jPanelM2.setLayout( borderLayoutM2 ) ;
		michelle_defaultName.setText( "Reset Defaults" ) ;
		jPanelM3.setLayout( gridBagLayoutM2 ) ;
		jPanelM3.setMinimumSize( new Dimension( 260 , 60 ) ) ;

		jPanelF2.setLayout( borderLayoutF2 ) ;
		ufti_defaultName.setText( "Reset Defaults" ) ;
		jPanelF3.setLayout( gridBagLayoutF2 ) ;
		jPanelF3.setMinimumSize( new Dimension( 260 , 60 ) ) ;

		jPanelU2.setLayout( borderLayoutU2 ) ;
		uist_defaultName.setText( "Reset Defaults" ) ;
		jPanelU3.setLayout( gridBagLayoutU2 ) ;
		jPanelU3.setMinimumSize( new Dimension( 260 , 60 ) ) ;

		jPanelW2.setLayout( borderLayoutW2 ) ;
		wfcam_defaultName.setText( "Reset Defaults" ) ;
		jPanelW3.setLayout( gridBagLayoutW2 ) ;
		jPanelW3.setMinimumSize( new Dimension( 260 , 60 ) ) ;

		this.add( emptyPanel , "empty" ) ;
		emptyPanel.add( jLabelE1 , BorderLayout.SOUTH ) ;

		// For each instrument.  Fill the GUI array of recipes, set button, presence in the group
		// tick boxes.  Create scrolling menu of recipes.

		// CGS4
		this.add( cgs4Panel , "cgs4" ) ;
		cgs4Panel.add( jPanelC1 , BorderLayout.NORTH ) ;
		jPanelC1.add( jLabelC3 , new GridBagConstraints( 0 , 8 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 15 , 0 ) ) ;
		jPanelC1.add( cgs4_biasRecipe , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 52 , 0 ) ) ;
		jPanelC1.add( cgs4_darkRecipe , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 42 , 0 ) ) ;
		jPanelC1.add( cgs4_skyRecipe , new GridBagConstraints( 2 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_objectRecipe , new GridBagConstraints( 2 , 8 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_darkSet , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_skySet , new GridBagConstraints( 1 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_objectSet , new GridBagConstraints( 1 , 8 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_biasInGroup , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_darkInGroup , new GridBagConstraints( 3 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_skyInGroup , new GridBagConstraints( 3 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_objectInGroup , new GridBagConstraints( 3 , 8 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( jLabelC2 , new GridBagConstraints( 3 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( jLabelC1 , new GridBagConstraints( 2 , 2 , 1 , 1 , 1. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , -2 , 0 , 27 ) , 0 , 0 ) ) ;
		jPanelC1.add( jLabelC7 , new GridBagConstraints( 0 , 2 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( jLabelC6 , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( jLabelC5 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( jLabelC4 , new GridBagConstraints( 0 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( jLabelC8 , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_flatSet , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_flatRecipe , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_flatInGroup , new GridBagConstraints( 3 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( jLabelC9 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_arcSet , new GridBagConstraints( 1 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_arcRecipe , new GridBagConstraints( 2 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_arcInGroup , new GridBagConstraints( 3 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( cgs4_biasSet , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelC1.add( jLabelC11 , new GridBagConstraints( 0 , 1 , 3 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 0 , 10 , 0 ) , 0 , 0 ) ) ;
		cgs4Panel.add( jPanelC2 , BorderLayout.CENTER ) ;
		jPanelC2.add( jPanelC3 , BorderLayout.NORTH ) ;
		jPanelC3.add( cgs4_defaultName , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 0 ) , 0 , 0 ) ) ;
		jPanelC2.add( jScrollPane_cgs4 , BorderLayout.CENTER ) ;
		jScrollPane_cgs4.getViewport().add( cgs4_recipeTable , null ) ;

		// IRCAM3
		this.add( ircam3Panel , "ircam3" ) ;
		ircam3Panel.add( jPanelI1 , BorderLayout.NORTH ) ;
		jPanelI1.add( jLabelI3 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 15 , 0 ) ) ;
		jPanelI1.add( ircam3_biasSet , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 17 ) , 0 , 0 ) ) ;
		jPanelI1.add( ircam3_biasRecipe , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 52 , 0 ) ) ;
		jPanelI1.add( ircam3_darkRecipe , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 42 , 0 ) ) ;
		jPanelI1.add( ircam3_skyRecipe , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( ircam3_objectRecipe , new GridBagConstraints( 2 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( ircam3_darkSet , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( ircam3_skySet , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( ircam3_objectSet , new GridBagConstraints( 1 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( ircam3_biasInGroup , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( ircam3_darkInGroup , new GridBagConstraints( 3 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( ircam3_skyInGroup , new GridBagConstraints( 3 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( ircam3_objectInGroup , new GridBagConstraints( 3 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( jLabelI2 , new GridBagConstraints( 3 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( jLabelI1 , new GridBagConstraints( 2 , 2 , 1 , 1 , 1. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , -2 , 0 , 27 ) , 0 , 0 ) ) ;
		jPanelI1.add( jLabelI7 , new GridBagConstraints( 0 , 2 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( jLabelI6 , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( jLabelI5 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( jLabelI4 , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelI1.add( jLabelI8 , new GridBagConstraints( 0 , 1 , 3 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 0 , 10 , 0 ) , 0 , 0 ) ) ;
		ircam3Panel.add( jPanelI2 , BorderLayout.CENTER ) ;
		jPanelI2.add( jPanelI3 , BorderLayout.NORTH ) ;
		jPanelI3.add( ircam3_defaultName , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 0 ) , 0 , 0 ) ) ;
		jPanelI2.add( jScrollPane_ircam3 , BorderLayout.CENTER ) ;
		jScrollPane_ircam3.getViewport().add( ircam3_recipeTable , null ) ;

		// Michelle
		this.add( michellePanel , "michelle" ) ;
		michellePanel.add( jPanelM1 , BorderLayout.NORTH ) ;
		jPanelM1.add( jLabelM5 , new GridBagConstraints( 0 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 15 , 0 ) ) ;
		jPanelM1.add( michelle_biasRecipe , new GridBagConstraints( 2 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 52 , 0 ) ) ;
		jPanelM1.add( michelle_darkRecipe , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 42 , 0 ) ) ;
		jPanelM1.add( michelle_skyRecipe , new GridBagConstraints( 2 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_objectRecipe , new GridBagConstraints( 2 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_darkSet , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_skySet , new GridBagConstraints( 1 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_objectSet , new GridBagConstraints( 1 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_biasInGroup , new GridBagConstraints( 3 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_darkInGroup , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_skyInGroup , new GridBagConstraints( 3 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_objectInGroup , new GridBagConstraints( 3 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( jLabelM6 , new GridBagConstraints( 3 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( jLabelM7 , new GridBagConstraints( 2 , 1 , 1 , 1 , 1. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , -2 , 0 , 27 ) , 0 , 0 ) ) ;
		jPanelM1.add( jLabelM1 , new GridBagConstraints( 0 , 1 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( jLabelM2 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( jLabelM3 , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( jLabelM4 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( jLabelM9 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_flatSet , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_flatRecipe , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_flatInGroup , new GridBagConstraints( 3 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( jLabelM8 , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_arcSet , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_arcRecipe , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_arcInGroup , new GridBagConstraints( 3 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( michelle_biasSet , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelM1.add( jLabelM10 , new GridBagConstraints( 0 , 0 , 3 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 0 , 10 , 0 ) , 0 , 0 ) ) ;
		michellePanel.add( jPanelM2 , BorderLayout.CENTER ) ;
		jPanelM2.add( jPanelM3 , BorderLayout.NORTH ) ;
		jPanelM3.add( michelle_defaultName , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 0 ) , 0 , 0 ) ) ;
		jPanelM2.add( jScrollPane_michelle , BorderLayout.CENTER ) ;
		jScrollPane_michelle.getViewport().add( michelle_recipeTable , null ) ;

		// UFTI
		this.add( uftiPanel , "ufti" ) ;
		uftiPanel.add( jPanelF1 , BorderLayout.NORTH ) ;
		jPanelF1.add( jLabelF2 , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 15 , 0 ) ) ;
		jPanelF1.add( ufti_darkRecipe , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 42 , 0 ) ) ;
		jPanelF1.add( ufti_skyRecipe , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( ufti_objectRecipe , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( ufti_darkSet , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( ufti_skySet , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( ufti_objectSet , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( ufti_darkInGroup , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( ufti_skyInGroup , new GridBagConstraints( 3 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( ufti_objectInGroup , new GridBagConstraints( 3 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( jLabelF1 , new GridBagConstraints( 3 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( jLabelF6 , new GridBagConstraints( 2 , 1 , 1 , 1 , 1. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , -2 , 0 , 27 ) , 0 , 0 ) ) ;
		jPanelF1.add( jLabelF5 , new GridBagConstraints( 0 , 1 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( jLabelF4 , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( jLabelF3 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelF1.add( jLabelF7 , new GridBagConstraints( 0 , 0 , 3 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 0 , 10 , 0 ) , 0 , 0 ) ) ;
		uftiPanel.add( jPanelF2 , BorderLayout.CENTER ) ;
		jPanelF2.add( jPanelF3 , BorderLayout.NORTH ) ;
		jPanelF3.add( ufti_defaultName , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 0 ) , 0 , 0 ) ) ;
		jPanelF2.add( jScrollPane_ufti , BorderLayout.CENTER ) ;
		jScrollPane_ufti.getViewport().add( ufti_recipeTable , null ) ;

		// UIST
		this.add( uistPanel , "uist" ) ;
		uistPanel.add( jPanelU1 , BorderLayout.NORTH ) ;
		jPanelU1.add( jLabelU5 , new GridBagConstraints( 0 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 15 , 0 ) ) ;
		jPanelU1.add( uist_biasRecipe , new GridBagConstraints( 2 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 52 , 0 ) ) ;
		jPanelU1.add( uist_darkRecipe , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 42 , 0 ) ) ;
		jPanelU1.add( uist_skyRecipe , new GridBagConstraints( 2 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_objectRecipe , new GridBagConstraints( 2 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_darkSet , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_skySet , new GridBagConstraints( 1 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_objectSet , new GridBagConstraints( 1 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_biasInGroup , new GridBagConstraints( 3 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_darkInGroup , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_skyInGroup , new GridBagConstraints( 3 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_objectInGroup , new GridBagConstraints( 3 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( jLabelU6 , new GridBagConstraints( 3 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( jLabelU7 , new GridBagConstraints( 2 , 1 , 1 , 1 , 1. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , -2 , 0 , 27 ) , 0 , 0 ) ) ;
		jPanelU1.add( jLabelU1 , new GridBagConstraints( 0 , 1 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( jLabelU2 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( jLabelU3 , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( jLabelU4 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( jLabelU9 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_flatSet , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_flatRecipe , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_flatInGroup , new GridBagConstraints( 3 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( jLabelU8 , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_arcSet , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_arcRecipe , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_arcInGroup , new GridBagConstraints( 3 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( uist_biasSet , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelU1.add( jLabelU10 , new GridBagConstraints( 0 , 0 , 3 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 0 , 10 , 0 ) , 0 , 0 ) ) ;
		uistPanel.add( jPanelU2 , BorderLayout.CENTER ) ;
		jPanelU2.add( jPanelU3 , BorderLayout.NORTH ) ;
		jPanelU3.add( uist_defaultName , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 0 ) , 0 , 0 ) ) ;
		jPanelU2.add( jScrollPane_uist , BorderLayout.CENTER ) ;
		jScrollPane_uist.getViewport().add( uist_recipeTable , null ) ;

		// WFCAM
		this.add( wfcamPanel , "wfcam" ) ;
		wfcamPanel.add( jPanelW1 , BorderLayout.NORTH ) ;
		jPanelW1.add( jLabelW5 , new GridBagConstraints( 0 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 15 , 0 ) ) ;
		jPanelW1.add( wfcam_biasRecipe , new GridBagConstraints( 2 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 52 , 0 ) ) ;
		jPanelW1.add( wfcam_darkRecipe , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 42 , 0 ) ) ;
		jPanelW1.add( wfcam_skyRecipe , new GridBagConstraints( 2 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_objectRecipe , new GridBagConstraints( 2 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_darkSet , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_skySet , new GridBagConstraints( 1 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_objectSet , new GridBagConstraints( 1 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_biasInGroup , new GridBagConstraints( 3 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_darkInGroup , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_skyInGroup , new GridBagConstraints( 3 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_objectInGroup , new GridBagConstraints( 3 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( jLabelW6 , new GridBagConstraints( 3 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( jLabelW7 , new GridBagConstraints( 2 , 1 , 1 , 1 , 1. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , -2 , 0 , 27 ) , 0 , 0 ) ) ;
		jPanelW1.add( jLabelW1 , new GridBagConstraints( 0 , 1 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( jLabelW2 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( jLabelW3 , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( jLabelW4 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( jLabelW9 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_flatSet , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_flatRecipe , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_flatInGroup , new GridBagConstraints( 3 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( jLabelW8 , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_focusSet , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_focusRecipe , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_focusInGroup , new GridBagConstraints( 3 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( wfcam_biasSet , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanelW1.add( jLabelW10 , new GridBagConstraints( 0 , 0 , 3 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 0 , 10 , 0 ) , 0 , 0 ) ) ;
		wfcamPanel.add( jPanelW2 , BorderLayout.CENTER ) ;
		jPanelW2.add( jPanelW3 , BorderLayout.NORTH ) ;
		jPanelW3.add( wfcam_defaultName , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 0 ) , 0 , 0 ) ) ;
		jPanelW2.add( jScrollPane_wfcam , BorderLayout.CENTER ) ;
		jScrollPane_wfcam.getViewport().add( wfcam_recipeTable , null ) ;
	}

	void cgs4_objectRecipe_actionPerformed( ActionEvent e ){}

	void cgs4_biasRecipe_actionPerformed( ActionEvent e ){}

	void ircam3_objectRecipe_actionPerformed( ActionEvent e ){}

	void ircam3_biasRecipe_actionPerformed( ActionEvent e ){}

	void michelle_objectRecipe_actionPerformed( ActionEvent e ){}

	void michelle_biasRecipe_actionPerformed( ActionEvent e ){}

	void ufti_objectRecipe_actionPerformed( ActionEvent e ){}

	void uist_objectRecipe_actionPerformed( ActionEvent e ){}

	void uist_biasRecipe_actionPerformed( ActionEvent e ){}

	void wfcam_objectRecipe_actionPerformed( ActionEvent e ){}

	void wfcam_biasRecipe_actionPerformed( ActionEvent e ){}
}
