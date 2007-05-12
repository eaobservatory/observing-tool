
package ot.jcmt.iter.editor ;


import javax.swing.JLabel ;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import jsky.app.ot.gui.CheckBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;

public class MiniConfigIterGUI extends jsky.app.ot.editor.MiniConfigIterGUI 
{
	public CheckBoxWidgetExt continuousSpinCheckBox ;
	private JLabel continuousSpinLabel ; 
	public TextBoxWidgetExt continuousSpinTextBox ;
	
	public MiniConfigIterGUI()
	{
		super();
		try
		{
			jbInit();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception 
	{
		continuousSpinLabel = new JLabel() ;
		continuousSpinLabel.setFont(new java.awt.Font( "Dialog" , 3 , 12 ) ) ;
		continuousSpinLabel.setForeground( Color.black ) ;
		continuousSpinLabel.setText( "Continuous Spin" ) ;
		
		continuousSpinCheckBox = new CheckBoxWidgetExt() ;
		
		continuousSpinTextBox = new TextBoxWidgetExt() ;
		
		this.add( continuousSpinLabel , new GridBagConstraints( 0 , 10 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 1 , 1 , 1 , 1 ) , 0 , 0 ) ) ;
		this.add( continuousSpinCheckBox , new GridBagConstraints( 0 , 10 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 1 , 1 , 1 , 1 ) , 0 , 0 ) ) ;
		this.add( continuousSpinTextBox , new GridBagConstraints( 1 , 10 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 1 , 1 , 1 , 1 ) , 0 , 0 ) ) ;
	}

}
