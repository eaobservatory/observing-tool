
package ot.jcmt.iter.editor ;


import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import java.awt.Container ;
import java.awt.Component ;
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
		continuousSpinLabel.setText( "Continuous Spin ( Hz )" ) ;
		
		continuousSpinCheckBox = new CheckBoxWidgetExt() ;
		
		continuousSpinTextBox = new TextBoxWidgetExt() ;
		
		this.add( continuousSpinLabel , new GridBagConstraints( 0 , 10 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 1 , 1 , 1 , 1 ) , 0 , 0 ) ) ;
		this.add( continuousSpinCheckBox , new GridBagConstraints( 0 , 10 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 1 , 1 , 1 , 1 ) , 0 , 0 ) ) ;
		this.add( continuousSpinTextBox , new GridBagConstraints( 1 , 10 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 1 , 1 , 1 , 1 ) , 0 , 0 ) ) ;
	}

	public void enableParent( boolean enabled )
	{
		Container container = ( Container )getParent() ;
		enableComponent( container , enabled ) ;
	}
	
	public void enableComponent( Container container , boolean enabled )
	{
		int componentCount = container.getComponentCount() ;
		for( int index = 0 ; index < componentCount ; index++ )
		{
			Component component = container.getComponent( index ) ;
			if( continuousSpinCheckBox == component || continuousSpinTextBox == component )
				continue ;
			else if( component instanceof JPanel || component instanceof JScrollPane )
				enableComponent( ( Container )component , enabled ) ;
			component.setEnabled( enabled ) ;
		}
	}
}
