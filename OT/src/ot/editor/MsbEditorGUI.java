// $Id$
/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on Allan Brighton (jsky/app/ot/editor/TitleEditorGUI.java)
 * @version 1.0
 */
package ot.editor ;

import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.Font ;
import javax.swing.JLabel ;
import javax.swing.JCheckBox ;
import javax.swing.JComboBox ;
import javax.swing.JToggleButton ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import ot.gui.GuiUtil;

/**
 * Modified version of jsky.app.ot.editor.TitleEditorGUI.
 *
 * @see jsky.app.ot.editor.TitleEditorGUI
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on Allan Brighton (jsky/app/ot/editor/TitleEditorGUI.java)
 */
@SuppressWarnings( "serial" )
public class MsbEditorGUI extends MsbObsCommonGUI
{
	TextBoxWidgetExt nameBox = new TextBoxWidgetExt() ;
	TextBoxWidgetExt estimatedTime = new TextBoxWidgetExt() ;
	TextBoxWidgetExt totalTime = new TextBoxWidgetExt() ;
	JCheckBox unSuspendCB = new JCheckBox() ;

	public MsbEditorGUI()
	{
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
		GridBagLayout gridBagLayout1 = new GridBagLayout() ;
		this.setPreferredSize(new Dimension(279, 271));
		this.setLayout(gridBagLayout1);

		JLabel jLabel1 = GuiUtil.createLabel("Name");
		JLabel jLabel2 = GuiUtil.createLabel("Observe");
		JLabel jLabel3 = GuiUtil.createLabel("Priority");
		JLabel jLabel4 = GuiUtil.createLabel("Estimated Time (w/o optionals)");
		estimatedTime.setEditable(false);
		JLabel jLabel7 = GuiUtil.createLabel("Estimated Total Time");
		totalTime.setEditable(false);
		JLabel jLabel5 = GuiUtil.createLabel("X");
		unSuspendCB.setFont(new java.awt.Font("Dialog", Font.BOLD, 12));
		unSuspendCB.setForeground(Color.black);
		unSuspendCB.setText("Un-Suspend");
		JLabel jLabel6 = GuiUtil.createLabel("(1-highest, 99-lowest)");

		this.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 10 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( nameBox , new GridBagConstraints( 1 , 0 , 3 , 1 , 1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jLabel2 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel3 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 30 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jComboBox1 , new GridBagConstraints( 1 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel6 , new GridBagConstraints( 2 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 2 , 5 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel4 , new GridBagConstraints( 0 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 10 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( estimatedTime , new GridBagConstraints( 1 , 3 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 15 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jLabel7 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 10 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( totalTime , new GridBagConstraints( 1 , 4 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 15 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( remaining , new GridBagConstraints( 1 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jLabel5 , new GridBagConstraints( 2 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;

		this.add( unSuspendCB , new GridBagConstraints( 3 , 1 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 20 , 0 , 0 ) , 0 , 0 ) ) ;
	}
}
