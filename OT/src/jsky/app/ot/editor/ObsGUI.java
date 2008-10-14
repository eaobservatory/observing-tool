/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot.editor ;

import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.awt.GridBagConstraints ;
import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.Font ;
import java.util.Vector ;
import javax.swing.JLabel ;
import javax.swing.JComboBox ;
import javax.swing.JPanel ;
import javax.swing.JCheckBox ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;

/**
 * Changes for OMP (MFO, August 2001).
 */
public class ObsGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JLabel jLabel1 = new JLabel() ;
	TextBoxWidgetExt obsTitle = new TextBoxWidgetExt() ;
	JLabel jLabel4 = new JLabel() ;
	JComboBox jComboBox1 ;
	Vector<Integer> priorities = new Vector<Integer>() ;
	final int nPriorities = 99 ;
	JPanel msbPanel = new JPanel() ;
	JLabel obsStateLabel = new JLabel() ;
	GridBagLayout gridBagLayout2 = new GridBagLayout() ;
	JLabel estimatedTimeLabel = new JLabel() ;
	TextBoxWidgetExt estimatedTime = new TextBoxWidgetExt() ;
	JLabel remainingLabel = new JLabel() ;
	DropDownListBoxWidgetExt remaining = new DropDownListBoxWidgetExt() ;
	JLabel xLabel = new JLabel() ;
	JLabel obsState = new JLabel() ;
	CheckBoxWidgetExt optional = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt standard = new CheckBoxWidgetExt() ;
	JCheckBox unSuspendCB = new JCheckBox() ;
	JLabel jLabel6 = new JLabel() ;

	public ObsGUI()
	{
		try
		{
			for( int i = 1 ; i <= nPriorities ; i++ )
				priorities.add( new Integer( i ) ) ;

			jComboBox1 = new JComboBox( priorities ) ;
			jbInit() ;
		}
		catch( Exception ex )
		{
			ex.printStackTrace() ;
		}
	}

	void jbInit() throws Exception
	{
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel1.setForeground( Color.black ) ;
		jLabel1.setText( "Obs Name" ) ;
		this.setLayout( gridBagLayout1 ) ;

		// Added for OMP (MFO, 1 August 2001)
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel4.setForeground( Color.black ) ;
		jLabel4.setText( "Priority" ) ;

		this.setPreferredSize( new Dimension( 280 , 280 ) ) ;

		obsStateLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		obsStateLabel.setForeground( Color.black ) ;
		obsStateLabel.setText( "Status" ) ;

		msbPanel.setLayout( gridBagLayout2 ) ;

		estimatedTimeLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		estimatedTimeLabel.setForeground( Color.black ) ;
		estimatedTimeLabel.setText( "Estimated Time (w/o optionals)" ) ;
		estimatedTime.setEditable( false ) ;

		remainingLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		remainingLabel.setForeground( Color.black ) ;
		remainingLabel.setText( "Observe" ) ;

		xLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		xLabel.setForeground( Color.black ) ;
		xLabel.setText( "X" ) ;

		unSuspendCB.setFont( new java.awt.Font( "Dialog" , Font.BOLD , 12 ) ) ;
		unSuspendCB.setForeground( Color.black ) ;
		unSuspendCB.setText( "Un-Suspend" ) ;

		obsState.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		obsState.setForeground( Color.black ) ;
		obsState.setText( "Not in Active Database" ) ;

		optional.setText( "Optional" ) ;
		optional.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;

		standard.setText( "Flag as Standard" ) ;
		standard.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;

		jLabel6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel6.setForeground( Color.black ) ;
		jLabel6.setText( "(1-highest, 99-lowest)" ) ;

		this.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( obsTitle , new GridBagConstraints( 1 , 0 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( msbPanel , new GridBagConstraints( 0 , 1 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		msbPanel.add( jLabel4 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 30 , 0 , 0 ) , 0 , 0 ) ) ;
		msbPanel.add( jComboBox1 , new GridBagConstraints( 1 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 0 ) , 0 , 0 ) ) ;
		msbPanel.add( jLabel6 , new GridBagConstraints( 2 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 2 , 5 , 0 ) , 0 , 0 ) ) ;
		msbPanel.add( remainingLabel , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 30 , 0 , 0 ) , 0 , 0 ) ) ;
		msbPanel.add( remaining , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 0 ) , 0 , 0 ) ) ;
		msbPanel.add( xLabel , new GridBagConstraints( 2 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		msbPanel.add( unSuspendCB , new GridBagConstraints( 3 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 15 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( obsStateLabel , new GridBagConstraints( 0 , 2 , 1 , 1 , -1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( obsState , new GridBagConstraints( 1 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( optional , new GridBagConstraints( 0 , 6 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( standard , new GridBagConstraints( 0 , 5 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( estimatedTimeLabel , new GridBagConstraints( 0 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( estimatedTime , new GridBagConstraints( 1 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 15 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
	}
}
