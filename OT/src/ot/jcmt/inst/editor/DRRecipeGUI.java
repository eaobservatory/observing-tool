
/**
 * Title:        Ukirt GUIs<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */
package ot.jcmt.inst.editor;

import java.awt.GridBagLayout ;
import java.awt.BorderLayout ;
import java.awt.CardLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Color ;
import java.awt.Insets ;
import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.JScrollPane ;
import javax.swing.JTabbedPane ;
import javax.swing.JTextArea ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TableWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import javax.swing.border.TitledBorder ;

public class DRRecipeGUI extends JPanel {
  JPanel scubaPanel = new JPanel();
  CommandButtonWidgetExt scuba_objectSet = new CommandButtonWidgetExt();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TextBoxWidgetExt scuba_objectRecipe = new TextBoxWidgetExt();
  JPanel jPanel1 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JPanel heterodynePanel = new JPanel();
  CardLayout cardLayout1 = new CardLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;
  JPanel jPanel4 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  TextBoxWidgetExt scuba_userRecipe = new TextBoxWidgetExt();
  JPanel jPanel2 = new JPanel();
  CommandButtonWidgetExt scuba_userSpec = new CommandButtonWidgetExt();
  CommandButtonWidgetExt scuba_defaultName = new CommandButtonWidgetExt();
  JScrollPane jScrollPane1 = new JScrollPane();
  TableWidgetExt scuba_recipeTable = new TableWidgetExt();
  JTabbedPane tabbedPaneHet = new JTabbedPane();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel5 = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel3 = new JLabel();
  TextBoxWidgetExt pixelSizeX = new TextBoxWidgetExt();
  TextBoxWidgetExt pixelSizeY = new TextBoxWidgetExt();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  TextBoxWidgetExt offsetX = new TextBoxWidgetExt();
  TextBoxWidgetExt offsetY = new TextBoxWidgetExt();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel10 = new JLabel();
  DropDownListBoxWidgetExt projection = new DropDownListBoxWidgetExt();
  DropDownListBoxWidgetExt gridFunction = new DropDownListBoxWidgetExt();
  TextBoxWidgetExt smoothingRad = new TextBoxWidgetExt();
  JLabel jLabel11 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JTextArea jTextArea1 = new JTextArea();
  JLabel jLabel13 = new JLabel();

  public DRRecipeGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
	{
		titledBorder1 = new TitledBorder( "" );
		this.setLayout( cardLayout1 );
		scubaPanel.setLayout( borderLayout3 );
		scuba_objectSet.setText( "Set" );
		jPanel1.setLayout( gridBagLayout1 );
		jLabel8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel8.setForeground( Color.black );
		jLabel8.setText( "Recipe Name" );
		jLabel6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel6.setForeground( Color.black );
		jLabel6.setText( "OBJECT" );
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel2.setForeground( Color.black );
		jLabel2.setText( "Observation Type" );
		heterodynePanel.setLayout( borderLayout1 );
		jPanel4.setLayout( borderLayout2 );
		scuba_userRecipe.setEditable( false );
		scuba_userRecipe.setText( "ENTER_YOUR_OWN_RECIPE_HERE" );
		jPanel2.setLayout( gridBagLayout2 );
		scuba_userSpec.setText( "User Specified:" );
		scuba_defaultName.setText( "Default" );
		jPanel3.setLayout( gridBagLayout3 );
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel1.setText( "x pixel size" );
		jLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel3.setText( "y pixel size" );
		pixelSizeX.setColumns( 10 );
		pixelSizeX.setText( "" );
		pixelSizeY.setText( "" );
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel4.setText( "x offset" );
		jLabel5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel5.setText( "y offset" );
		offsetX.setColumns( 10 );
		offsetX.setText( "" );
		offsetY.setText( "" );
		jLabel7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel7.setText( "Projection" );
		jLabel9.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel9.setRequestFocusEnabled( true );
		jLabel9.setText( "Grid Function" );
		jLabel10.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel10.setText( "Smoothing Radius" );
		smoothingRad.setText( "" );
		projection.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		gridFunction.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel11.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) );
		jLabel11.setText( "(arcsecs)" );
		jLabel12.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) );
		jLabel12.setText( "(arcsecs)" );
		jTextArea1.setBackground( jPanel3.getBackground() );
		jTextArea1.setEnabled( false );
		jTextArea1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jTextArea1.setDisabledTextColor( Color.red );
		jTextArea1.setEditable( false );
		jTextArea1.setText( "Note:\nThe group centre is set to the\nSCIENCE position in the\nTarget Information " + "component.\n\ntcs_coord is currently fixed to TRACKING." );
		jTextArea1.setLineWrap( true );
		jLabel13.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) );
		jLabel13.setText( "(arcsecs)" );
		this.add( scubaPanel , "scuba" );
		scubaPanel.add( jPanel1 , BorderLayout.NORTH );
		jPanel1.add( jLabel6 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 15 , 0 ) );
		jPanel1.add( scuba_objectRecipe , new GridBagConstraints( 2 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		jPanel1.add( scuba_objectSet , new GridBagConstraints( 1 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		jPanel1.add( jLabel8 , new GridBagConstraints( 2 , 0 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		jPanel1.add( jLabel2 , new GridBagConstraints( 0 , 0 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		scubaPanel.add( jPanel4 , BorderLayout.CENTER );
		jPanel4.add( jPanel2 , BorderLayout.NORTH );
		jPanel2.add( scuba_defaultName , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		jPanel2.add( scuba_userSpec , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		jPanel2.add( scuba_userRecipe , new GridBagConstraints( 2 , 0 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		jPanel4.add( jScrollPane1 , BorderLayout.CENTER );
		jScrollPane1.getViewport().add( scuba_recipeTable , null );
		this.add( heterodynePanel , "heterodyne" );
		heterodynePanel.add( tabbedPaneHet , BorderLayout.CENTER );
		jPanel3.add( jLabel1 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 2 ) , 0 , 0 ) );
		jPanel3.add( jLabel3 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 2 ) , 0 , 0 ) );
		jPanel3.add( pixelSizeX , new GridBagConstraints( 1 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		tabbedPaneHet.add( jPanel5 , "ORAC DR" );
		jPanel3.add( pixelSizeY , new GridBagConstraints( 1 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.SOUTH , GridBagConstraints.HORIZONTAL , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) );
		jPanel3.add( jLabel4 , new GridBagConstraints( 3 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 20 , 0 , 2 ) , 0 , 0 ) );
		jPanel3.add( jLabel5 , new GridBagConstraints( 3 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 2 ) , 0 , 0 ) );
		jPanel3.add( offsetX , new GridBagConstraints( 4 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		jPanel3.add( offsetY , new GridBagConstraints( 4 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) );
		jPanel3.add( jLabel7 , new GridBagConstraints( 0 , 3 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 20 , 0 , 0 , 2 ) , 0 , 0 ) );
		jPanel3.add( jLabel9 , new GridBagConstraints( 0 , 4 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 2 ) , 0 , 0 ) );
		jPanel3.add( jLabel10 , new GridBagConstraints( 0 , 5 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 2 ) , 0 , 0 ) );
		jPanel3.add( projection , new GridBagConstraints( 2 , 3 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.SOUTH , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		jPanel3.add( gridFunction , new GridBagConstraints( 2 , 4 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) );
		jPanel3.add( smoothingRad , new GridBagConstraints( 2 , 5 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) );
		jPanel3.add( jLabel11 , new GridBagConstraints( 2 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		jPanel3.add( jLabel12 , new GridBagConstraints( 2 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		jPanel3.add( jTextArea1 , new GridBagConstraints( 0 , 0 , 5 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 20 , 0 ) , 0 , 0 ) );
		jPanel3.add( jLabel13 , new GridBagConstraints( 4 , 5 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
	}
}

