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

package ot.jcmt.inst.editor ;

import java.awt.GridBagLayout ;
import java.awt.BorderLayout ;
import java.awt.CardLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Color ;
import java.awt.Insets ;
import java.awt.Font ;
import java.lang.reflect.Field ;

import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.JScrollPane ;

import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TableWidgetExt ;

@SuppressWarnings( "serial" )
public class DRRecipeGUI extends JPanel
{
	Class<? extends DRRecipeGUI> whatami = this.getClass() ;
	
	JLabel recipeNameLabel = new JLabel() ;
	JLabel obeservationTypeLabel = new JLabel() ;

	// Beginning of types
		
	JLabel rasterLabel = new JLabel() ;	
	CommandButtonWidgetExt rasterRecipeSet = new CommandButtonWidgetExt() ;
	TextBoxWidgetExt rasterRecipe = new TextBoxWidgetExt() ;
	Object[] raster = { rasterRecipeSet , rasterRecipe } ;
	
	JLabel jiggleLabel = new JLabel() ;	
	CommandButtonWidgetExt jiggleRecipeSet = new CommandButtonWidgetExt() ;
	TextBoxWidgetExt jiggleRecipe = new TextBoxWidgetExt() ;
	Object[] jiggle = { jiggleRecipeSet , jiggleRecipe } ;
	
	JLabel stareLabel = new JLabel() ;	
	CommandButtonWidgetExt stareRecipeSet = new CommandButtonWidgetExt() ;
	TextBoxWidgetExt stareRecipe = new TextBoxWidgetExt() ;
	Object[] stare = { stareRecipeSet , stareRecipe } ;
	
	JLabel pointingLabel = new JLabel() ;	
	CommandButtonWidgetExt pointingRecipeSet = new CommandButtonWidgetExt() ;
	TextBoxWidgetExt pointingRecipe = new TextBoxWidgetExt() ;
	Object[] pointing = { pointingRecipeSet , pointingRecipe } ;
	
	JLabel focusLabel = new JLabel() ;	
	CommandButtonWidgetExt focusRecipeSet = new CommandButtonWidgetExt() ;
	TextBoxWidgetExt focusRecipe = new TextBoxWidgetExt() ;
	Object[] focus = { focusRecipeSet , focusRecipe } ;
	
	Object[] types = { raster , jiggle , stare , pointing , focus } ;
	
	// End of types
	
	TableWidgetExt recipeTable = new TableWidgetExt() ;
	
	CommandButtonWidgetExt defaultName = new CommandButtonWidgetExt() ;

	// the following names are not very helpful
	JPanel panel = new JPanel() ;
	JPanel bigPanel = new JPanel() ;
	JPanel thatPanel = new JPanel() ;
	JPanel anotherPanel = new JPanel() ;
	JScrollPane scrollPane = new JScrollPane() ;

	CardLayout cardLayout = new CardLayout() ;
	BorderLayout borderLayout = new BorderLayout() ;
	BorderLayout layout = new BorderLayout() ;
	GridBagLayout panelGridbag = new GridBagLayout() ;
	GridBagLayout anotherGridbag = new GridBagLayout() ;

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
		this.setLayout( cardLayout ) ;
		panel.setLayout( layout ) ;
		thatPanel.setLayout( panelGridbag ) ;
		bigPanel.setLayout( borderLayout ) ;
		anotherPanel.setLayout( anotherGridbag ) ;
		
		recipeNameLabel.setFont( new Font( "Dialog" , 0 , 12 ) ) ;
		recipeNameLabel.setForeground( Color.black ) ;
		recipeNameLabel.setText( "Recipe Name" ) ;
		
		obeservationTypeLabel.setFont( new Font( "Dialog" , 0 , 12 ) ) ;
		obeservationTypeLabel.setForeground( Color.black ) ;
		obeservationTypeLabel.setText( "Observation Type" ) ;
	
		defaultName.setText( "Default" ) ;
		
		// Beginning of types
		
		rasterLabel.setFont( new Font( "Dialog" , 0 , 12 ) ) ;
		rasterLabel.setForeground( Color.black ) ;
		rasterLabel.setText( "Scan" ) ;
		rasterRecipeSet.setText( "Set" ) ;
		
		jiggleLabel.setFont( new Font( "Dialog" , 0 , 12 ) ) ;
		jiggleLabel.setForeground( Color.black ) ;
		jiggleLabel.setText( "Jiggle" ) ;
		jiggleRecipeSet.setText( "Set" ) ;
		
		stareLabel.setFont( new Font( "Dialog" , 0 , 12 ) ) ;
		stareLabel.setForeground( Color.black ) ;
		stareLabel.setText( "Stare" ) ;
		stareRecipeSet.setText( "Set" ) ;
		
		pointingLabel.setFont( new Font( "Dialog" , 0 , 12 ) ) ;
		pointingLabel.setForeground( Color.black ) ;
		pointingLabel.setText( "Pointing" ) ;
		pointingRecipeSet.setText( "Set" ) ;
		
		focusLabel.setFont( new Font( "Dialog" , 0 , 12 ) ) ;
		focusLabel.setForeground( Color.black ) ;
		focusLabel.setText( "Focus" ) ;
		focusRecipeSet.setText( "Set" ) ;

		// End of types
		
		this.add( panel , "heterodyne" ) ;

		panel.add( thatPanel , BorderLayout.NORTH ) ;

		thatPanel.add( recipeNameLabel , new GridBagConstraints( 2 , 0 , 1 , 1 , 1. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		thatPanel.add( obeservationTypeLabel , new GridBagConstraints( 0 , 0 , 2 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;

		// Beginning of types
		
		thatPanel.add( rasterLabel , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 15 , 0 ) ) ;
		thatPanel.add( rasterRecipeSet , new GridBagConstraints( 1 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		thatPanel.add( rasterRecipe , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;

		thatPanel.add( jiggleLabel , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 15 , 0 ) ) ;
		thatPanel.add( jiggleRecipeSet , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		thatPanel.add( jiggleRecipe , new GridBagConstraints( 2 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;

		thatPanel.add( stareLabel , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 15 , 0 ) ) ;
		thatPanel.add( stareRecipeSet , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		thatPanel.add( stareRecipe , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;

		thatPanel.add( pointingLabel , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 15 , 0 ) ) ;
		thatPanel.add( pointingRecipeSet , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		thatPanel.add( pointingRecipe , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;

		thatPanel.add( focusLabel , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 15 , 0 ) ) ;
		thatPanel.add( focusRecipeSet , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		thatPanel.add( focusRecipe , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		
		// End of types
		
		panel.add( bigPanel , BorderLayout.CENTER ) ;
		
		bigPanel.add( anotherPanel , BorderLayout.NORTH ) ;
		anotherPanel.add( defaultName , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		bigPanel.add( scrollPane , BorderLayout.CENTER ) ;
		scrollPane.getViewport().add( recipeTable , null ) ;
		reset() ;
	}
	
	public void setEnabled( String type , boolean enabled )
	{
		Object[] typeFields = null ;
		if( type.endsWith( "Recipe" ) )
			type = type.substring( 0 , type.indexOf( "Recipe" ) ) ;
		try
		{
			Field field =  whatami.getDeclaredField( type ) ;
			if( field != null )
			{
				Class<?> klass = field.getType() ;
				if( klass == Object[].class )
					typeFields = ( Object[] )field.get( this ) ;
				
				setEnabled( typeFields , enabled ) ;
			}
		}
		catch( NoSuchFieldException nsfe )
		{
			System.out.println( type + " does not appear to exist for " + whatami.getName() + ". " + nsfe ) ;
		}
		catch( IllegalAccessException iae )
		{
			System.out.println( "You do not appear to have access to " + type + ". " + iae ) ;
		}
	}
	
	private void setEnabled( Object[] typeFields  , boolean enabled )
	{
		if( typeFields != null && typeFields.length == 2 )
		{
			if( typeFields[ 0 ] instanceof CommandButtonWidgetExt && typeFields[ 1 ] instanceof TextBoxWidgetExt )
			{
				CommandButtonWidgetExt button = ( CommandButtonWidgetExt )typeFields[ 0 ] ;
				button.setEnabled( enabled ) ;
				
				TextBoxWidgetExt textBox = ( TextBoxWidgetExt )typeFields[ 1 ] ;
				textBox.setText( "" ) ;
				textBox.setEnabled( enabled ) ;
			}
		}
	}
	
	public void reset()
	{
		for( int index = 0 ; index < types.length ; index++ )
		{
			Object[] typeFields = ( Object[] )types[ index ] ;
			setEnabled( typeFields , false ) ;
		}
	}
}
