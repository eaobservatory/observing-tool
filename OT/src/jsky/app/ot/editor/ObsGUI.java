/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot.editor ;

// Why are there two packages each containing a random selection of GUI classes?
import ot.editor.MsbObsCommonGUI;

import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.awt.GridBagConstraints ;
import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.Font ;
import javax.swing.JLabel ;
import javax.swing.JComboBox ;
import javax.swing.JPanel ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;

import ot.gui.GuiUtil;

/**
 * Changes for OMP (MFO, August 2001).
 */
@SuppressWarnings( "serial" )
public class ObsGUI extends MsbObsCommonGUI
{
	JPanel msbPanel = new JPanel() ;
	JLabel obsStateLabel;
	JLabel obsState; 
	CheckBoxWidgetExt optional = new CheckBoxWidgetExt() ;
	CheckBoxWidgetExt standard = new CheckBoxWidgetExt() ;

	public ObsGUI()
	{
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
		GridBagLayout gridBagLayout1 = new GridBagLayout() ;
		GridBagLayout gridBagLayout2 = new GridBagLayout() ;
		this.setLayout(gridBagLayout1);
		this.setPreferredSize(new Dimension(280, 280));

		JLabel jLabel1 = GuiUtil.createLabel("Obs Name");

		// Added for OMP (MFO, 1 August 2001)
		JLabel jLabel4 = GuiUtil.createLabel("Priority");

		obsStateLabel = GuiUtil.createLabel("Status");

		msbPanel.setLayout(gridBagLayout2);

		JLabel estimatedTimeLabel = GuiUtil.createLabel("Estimated Time (w/o optionals)");
		estimatedTime.setEditable(false);

		JLabel remainingLabel = GuiUtil.createLabel("Observe");

		JLabel xLabel = GuiUtil.createLabel("X");

		unSuspendCB.setFont(new java.awt.Font("Dialog", Font.BOLD, 12));
		unSuspendCB.setForeground(Color.black);
		unSuspendCB.setText("Un-Suspend") ;

		obsState = GuiUtil.createLabel("Not in Active Database");

		optional.setText("Optional");
		optional.setFont(new java.awt.Font("Dialog",0, 12));

		standard.setText("Flag as Standard");
		standard.setFont(new java.awt.Font("Dialog", 0, 12));

		JLabel jLabel6 = GuiUtil.createLabel("(1-highest, 99-lowest)");

		this.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( nameBox, new GridBagConstraints( 1 , 0 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( msbPanel , new GridBagConstraints( 0 , 1 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		msbPanel.add( jLabel4 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 30 , 0 , 0 ) , 0 , 0 ) ) ;
		msbPanel.add( jComboBox1 , new GridBagConstraints( 1 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 0 ) , 0 , 0 ) ) ;
		msbPanel.add( jLabel6 , new GridBagConstraints( 2 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 2 , 5 , 0 ) , 0 , 0 ) ) ;
		msbPanel.add( remainingLabel , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 30 , 0 , 0 ) , 0 , 0 ) ) ;

		JPanel panel = new JPanel();
		panel.add(remaining);
		panel.add(xLabel);
		panel.add(unRemoveButton);
		msbPanel.add(panel, new GridBagConstraints( 1 , 0 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST, GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 0 ) , 0 , 0 ) ) ;

		msbPanel.add( unSuspendCB , new GridBagConstraints( 3 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 15 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( obsStateLabel , new GridBagConstraints( 0 , 2 , 1 , 1 , -1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( obsState , new GridBagConstraints( 1 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( optional , new GridBagConstraints( 0 , 6 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( standard , new GridBagConstraints( 0 , 5 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( estimatedTimeLabel , new GridBagConstraints( 0 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 5 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( estimatedTime , new GridBagConstraints( 1 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 15 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
	}
}
