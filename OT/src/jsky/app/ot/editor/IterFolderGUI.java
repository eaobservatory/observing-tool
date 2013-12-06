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
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.JButton ;
import javax.swing.BorderFactory ;

@SuppressWarnings( "serial" )
public class IterFolderGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JScrollPane jScrollPane1 = new JScrollPane() ;
	JButton compileButton = new JButton() ;
	IterEnumTableWidget table = new IterEnumTableWidget() ;

	public IterFolderGUI()
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
		this.setLayout( gridBagLayout1 ) ;
		compileButton.setText( "Show" ) ;
		jScrollPane1.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		this.setPreferredSize( new Dimension( 300 , 300 ) ) ;
		this.add( jScrollPane1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		jScrollPane1.getViewport().add( table , null ) ;
		this.add( compileButton , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
	}
}
