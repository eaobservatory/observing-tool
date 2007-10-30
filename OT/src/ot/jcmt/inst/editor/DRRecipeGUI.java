/**
 * Title:        Ukirt GUIs<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */
package ot.jcmt.inst.editor;

import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Font ;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TableWidgetExt;

public class DRRecipeGUI extends JPanel
{
	JLabel recipeNameLabel = new JLabel();
	JLabel obeservationTypeLabel = new JLabel();

	// Beginning of types
	
	JLabel rasterLabel = new JLabel();	
	CommandButtonWidgetExt heterodyne_rasterRecipeSet = new CommandButtonWidgetExt();
	TextBoxWidgetExt heterodyne_rasterRecipe = new TextBoxWidgetExt();
	
	JLabel jiggleLabel = new JLabel();	
	CommandButtonWidgetExt heterodyne_jiggleRecipeSet = new CommandButtonWidgetExt();
	TextBoxWidgetExt heterodyne_jiggleRecipe = new TextBoxWidgetExt();
	
	JLabel stareLabel = new JLabel();	
	CommandButtonWidgetExt heterodyne_stareRecipeSet = new CommandButtonWidgetExt();
	TextBoxWidgetExt heterodyne_stareRecipe = new TextBoxWidgetExt();
	
	JLabel pointingLabel = new JLabel();	
	CommandButtonWidgetExt heterodyne_pointingRecipeSet = new CommandButtonWidgetExt();
	TextBoxWidgetExt heterodyne_pointingRecipe = new TextBoxWidgetExt();
	
	JLabel focusLabel = new JLabel();	
	CommandButtonWidgetExt heterodyne_focusRecipeSet = new CommandButtonWidgetExt();
	TextBoxWidgetExt heterodyne_focusRecipe = new TextBoxWidgetExt();
	
	// End of types
	
	TableWidgetExt heterodyne_recipeTable = new TableWidgetExt();
	
	CommandButtonWidgetExt heterodyne_defaultName = new CommandButtonWidgetExt();

	JPanel heterodynePanel = new JPanel();
	JPanel bigHetPanel = new JPanel() ;
	JPanel hetPanel = new JPanel();
	JPanel anotherHetPanel = new JPanel();
	JScrollPane hetScrollPane = new JScrollPane();

	CardLayout cardLayout1 = new CardLayout();
	BorderLayout hetBorderLayout = new BorderLayout();
	BorderLayout heterodyneLayout = new BorderLayout() ;
	GridBagLayout hetPanelGridbag = new GridBagLayout() ;
	GridBagLayout anotherHetGridbag = new GridBagLayout();

	public DRRecipeGUI()
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
		this.setLayout( cardLayout1 );
		heterodynePanel.setLayout( heterodyneLayout ) ;
		hetPanel.setLayout( hetPanelGridbag ) ;
		bigHetPanel.setLayout( hetBorderLayout ) ;
		anotherHetPanel.setLayout( anotherHetGridbag ) ;
		
		recipeNameLabel.setFont( new Font( "Dialog" , 0 , 12 ) );
		recipeNameLabel.setForeground( Color.black );
		recipeNameLabel.setText( "Recipe Name" );
		
		obeservationTypeLabel.setFont( new Font( "Dialog" , 0 , 12 ) );
		obeservationTypeLabel.setForeground( Color.black );
		obeservationTypeLabel.setText( "Observation Type" );
	
		heterodyne_defaultName.setText( "Default" );
		
		// Beginning of types
		
		rasterLabel.setFont( new Font( "Dialog" , 0 , 12 ) );
		rasterLabel.setForeground( Color.black );
		rasterLabel.setText( "Raster" );
		heterodyne_rasterRecipeSet.setText( "Set" );
		
		jiggleLabel.setFont( new Font( "Dialog" , 0 , 12 ) );
		jiggleLabel.setForeground( Color.black );
		jiggleLabel.setText( "Jiggle" );
		heterodyne_jiggleRecipeSet.setText( "Set" );
		
		stareLabel.setFont( new Font( "Dialog" , 0 , 12 ) );
		stareLabel.setForeground( Color.black );
		stareLabel.setText( "Stare" );
		heterodyne_stareRecipeSet.setText( "Set" );
		
		pointingLabel.setFont( new Font( "Dialog" , 0 , 12 ) );
		pointingLabel.setForeground( Color.black );
		pointingLabel.setText( "Pointing" );
		heterodyne_pointingRecipeSet.setText( "Set" );
		
		focusLabel.setFont( new Font( "Dialog" , 0 , 12 ) );
		focusLabel.setForeground( Color.black );
		focusLabel.setText( "Focus" );
		heterodyne_focusRecipeSet.setText( "Set" );

		// End of types
		
		this.add( heterodynePanel , "heterodyne" );

		heterodynePanel.add( hetPanel , BorderLayout.NORTH );

		hetPanel.add( recipeNameLabel , new GridBagConstraints( 2 , 0 , 1 , 1 , 1. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		hetPanel.add( obeservationTypeLabel , new GridBagConstraints( 0 , 0 , 2 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );

		// Beginning of types
		
		hetPanel.add( rasterLabel , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 15 , 0 ) );
		hetPanel.add( heterodyne_rasterRecipeSet , new GridBagConstraints( 1 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		hetPanel.add( heterodyne_rasterRecipe , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );

		hetPanel.add( jiggleLabel , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 15 , 0 ) );
		hetPanel.add( heterodyne_jiggleRecipeSet , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		hetPanel.add( heterodyne_jiggleRecipe , new GridBagConstraints( 2 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );

		hetPanel.add( stareLabel , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 15 , 0 ) );
		hetPanel.add( heterodyne_stareRecipeSet , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		hetPanel.add( heterodyne_stareRecipe , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );

		hetPanel.add( pointingLabel , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 15 , 0 ) );
		hetPanel.add( heterodyne_pointingRecipeSet , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		hetPanel.add( heterodyne_pointingRecipe , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );

		hetPanel.add( focusLabel , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 15 , 0 ) );
		hetPanel.add( heterodyne_focusRecipeSet , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		hetPanel.add( heterodyne_focusRecipe , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );

		// End of types
		
		heterodynePanel.add( bigHetPanel , BorderLayout.CENTER );
		
		bigHetPanel.add( anotherHetPanel , BorderLayout.NORTH ) ;
		anotherHetPanel.add( heterodyne_defaultName , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		bigHetPanel.add( hetScrollPane , BorderLayout.CENTER );
		hetScrollPane.getViewport().add( heterodyne_recipeTable , null );
	}
}
