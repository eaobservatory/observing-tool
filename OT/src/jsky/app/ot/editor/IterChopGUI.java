/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright 2000 Association for Universities for Research in Astronomy, Inc.<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot.editor ;

import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Dimension ;
import java.awt.Color ;
import java.awt.FlowLayout ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.JButton ;
import javax.swing.JLabel ;
import jsky.app.ot.gui.TableWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

@SuppressWarnings( "serial" )
public class IterChopGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JLabel jLabel3 = new JLabel() ;
	JLabel tableInfo = new JLabel() ;
	JScrollPane configScrollPane = new JScrollPane() ;
	JLabel jLabel5 = new JLabel() ;
	JPanel jPanel1 = new JPanel() ;
	JButton deleteStep = new JButton() ;
	JButton addStep = new JButton() ;
	JButton top = new JButton() ;
	JButton up = new JButton() ;
	JButton down = new JButton() ;
	JButton bottom = new JButton() ;
	TableWidgetExt iterStepsTable = new TableWidgetExt() ;
	GridBagLayout gridBagLayout4 = new GridBagLayout() ;
	JPanel textBoxGroup = new JPanel() ;
	JLabel textBoxTitle = new JLabel() ;
	TextBoxWidgetExt textBox = new TextBoxWidgetExt() ;
	JLabel jLabel1 = new JLabel() ;
	JLabel jLabel4 = new JLabel() ;
	JLabel jLabel7 = new JLabel() ;
	TextBoxWidgetExt throwTextBox = new TextBoxWidgetExt() ;
	TextBoxWidgetExt angleTextBox = new TextBoxWidgetExt() ;
	FlowLayout flowLayout1 = new FlowLayout() ;
	DropDownListBoxWidgetExt coordFrameListBox = new DropDownListBoxWidgetExt() ;

	public IterChopGUI()
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
		this.setPreferredSize( new Dimension( 394 , 356 ) ) ;
		this.setLayout( gridBagLayout1 ) ;
		jLabel3.setFont( new java.awt.Font( "Dialog" , 3 , 12 ) ) ;
		jLabel3.setForeground( Color.black ) ;
		jLabel3.setText( "Iteration Config" ) ;
		tableInfo.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		tableInfo.setForeground( Color.black ) ;
		tableInfo.setText( "(0 Items, 0 Steps)" ) ;
		jLabel5.setFont( new java.awt.Font( "Dialog" , 3 , 12 ) ) ;
		jLabel5.setForeground( Color.black ) ;
		jLabel5.setText( "Step" ) ;
		deleteStep.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		deleteStep.setText( "Delete" ) ;
		addStep.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		addStep.setText( "Add" ) ;
		down.setToolTipText( "" ) ;
		iterStepsTable.setBackground( Color.lightGray ) ;
		textBoxGroup.setLayout( gridBagLayout4 ) ;
		textBoxTitle.setForeground( Color.black ) ;
		textBoxTitle.setText( "Value" ) ;
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel1.setForeground( Color.black ) ;
		jLabel1.setText( "Throw" ) ;
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel4.setForeground( Color.black ) ;
		jLabel4.setText( "Angle" ) ;
		jLabel7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel7.setForeground( Color.black ) ;
		jLabel7.setText( "Coord Frame" ) ;
		jPanel1.setLayout( flowLayout1 ) ;
		flowLayout1.setAlignment( FlowLayout.LEFT ) ;
		coordFrameListBox.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		this.add( jLabel3 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 20 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( tableInfo , new GridBagConstraints( 2 , 2 , 1 , 3 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( configScrollPane , new GridBagConstraints( 0 , 5 , 3 , 4 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		configScrollPane.getViewport().add( iterStepsTable , null ) ;
		this.add( jLabel5 , new GridBagConstraints( 0 , 9 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 5 , 0 ) , 0 , 0 ) ) ;
		this.add( jPanel1 , new GridBagConstraints( 0 , 10 , 3 , 1 , 1.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel1.add( addStep , null ) ;
		jPanel1.add( deleteStep , null ) ;
		this.add( top , new GridBagConstraints( 3 , 5 , 1 , 1 , 0.0 , 0.25 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( up , new GridBagConstraints( 3 , 6 , 1 , 1 , 0.0 , 0.25 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( down , new GridBagConstraints( 3 , 7 , 1 , 1 , 0.0 , 0.25 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( bottom , new GridBagConstraints( 3 , 8 , 1 , 1 , 0.0 , 0.25 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel4 , new GridBagConstraints( 0 , 1 , 1 , 2 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel7 , new GridBagConstraints( 0 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( throwTextBox , new GridBagConstraints( 1 , 0 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( angleTextBox , new GridBagConstraints( 1 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( coordFrameListBox , new GridBagConstraints( 1 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		textBoxGroup.add( textBoxTitle , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.NORTHWEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		textBoxGroup.add( textBox , new GridBagConstraints( 0 , 1 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
	}
}
