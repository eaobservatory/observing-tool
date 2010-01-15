// $Id$

package ot.jcmt.iter.editor ;

import java.awt.BorderLayout ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;

import javax.swing.BorderFactory ;
import javax.swing.ButtonGroup ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.JRadioButton ;
import javax.swing.border.Border ;

import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

public class IterFTS2ObsGUI extends JPanel
{
	JPanel portSetupPanel = new JPanel() ;
	JRadioButton dual = new JRadioButton() ;
	JRadioButton single = new JRadioButton() ;
	JRadioButton port1 = new JRadioButton() ;
	JRadioButton port2 = new JRadioButton() ;
	ButtonGroup dualSingleGroup = new ButtonGroup() ;
	ButtonGroup portsGroup = new ButtonGroup() ;
	JLabel trackingPortLabel = new JLabel() ;
	JPanel modesPanel = new JPanel() ;
	BorderLayout portsSetupBorderLayout = new BorderLayout() ;
	GridBagLayout gridBagLayout = new GridBagLayout() ;
	JLabel specialModesLabel = new JLabel() ;
	DropDownListBoxWidgetExt specialModes = new DropDownListBoxWidgetExt() ;
	BorderLayout specialModesBorderLayout = new BorderLayout() ;
	JPanel leftPanel = new JPanel() ;
	JPanel rightPanel = new JPanel() ;
	
	public IterFTS2ObsGUI()
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
		this.setLayout( portsSetupBorderLayout ) ;
		
		portSetupPanel.setLayout( gridBagLayout ) ;
		Border portsBorder = BorderFactory.createTitledBorder( "Ports Setup" ) ;
		portSetupPanel.setBorder( portsBorder ) ;
		
		dual.setText( "Dual" ) ;
		single.setText( "Single" ) ;
		dualSingleGroup.add( dual ) ;
		dualSingleGroup.add( single ) ;
		portSetupPanel.add( dual , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		portSetupPanel.add( single , new GridBagConstraints( 1 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;

		trackingPortLabel.setText( "Tracking Port : " ) ;	
		portSetupPanel.add( trackingPortLabel , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;

		port1.setText( "Port 1" ) ;
		port2.setText( "Port 2" ) ;
		portsGroup.add( port1 ) ;
		portsGroup.add( port2 ) ;
		portSetupPanel.add( port1 , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		portSetupPanel.add( port2 , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;		

		specialModesLabel.setText( "Special Modes " ) ;
		modesPanel.setLayout( specialModesBorderLayout ) ;
		modesPanel.add( specialModesLabel , BorderLayout.WEST ) ;
		modesPanel.add( specialModes , BorderLayout.CENTER ) ;
		
		leftPanel.add( portSetupPanel ) ;
		rightPanel.add( modesPanel ) ;
		this.add( leftPanel , BorderLayout.WEST ) ;
		this.add( rightPanel , BorderLayout.CENTER ) ;
	}
}
