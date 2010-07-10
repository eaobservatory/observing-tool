package ot.jcmt.iter.editor ;

import java.awt.BorderLayout ;
import java.awt.Component ;
import java.awt.Font ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.GridLayout ;
import java.awt.Insets ;

import javax.swing.BorderFactory ;
import javax.swing.ButtonGroup ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.JRadioButton ;
import javax.swing.border.Border ;
import javax.swing.JSlider ;
import javax.swing.JTextField ;

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

	JPanel resolutionFOVPanel = new JPanel() ;
	JSlider resolutionFOV = new JSlider( JSlider.HORIZONTAL ) ;
	JLabel hiResLoFOVLabel = new JLabel() ;
	JLabel loResHiFOVLabel = new JLabel() ;
	JLabel resolutionLabel = new JLabel() ;
	JTextField resolution = new JTextField() ;
	JLabel OneDividedByCM = new JLabel() ;
	JLabel FOVLabel = new JLabel() ;
	JTextField FOV = new JTextField() ;
	JLabel squareArcminutes = new JLabel() ;

	JPanel scanSpeedNyquistPanel = new JPanel() ;
	JSlider scanSpeedNyquist = new JSlider( JSlider.HORIZONTAL ) ;
	JLabel hiSpdLoNyqLabel = new JLabel() ;
	JLabel loSpdHiNyqLabel = new JLabel() ;
	JLabel scanSpeedLabel = new JLabel() ;
	JTextField scanSpeed = new JTextField() ;
	JLabel cmPerSecondLabel = new JLabel() ;
	JLabel nyquistLabel = new JLabel() ;
	JTextField nyquist = new JTextField() ;
	JLabel OneOverByCM = new JLabel() ;

	JPanel sensitivityTimePanel = new JPanel() ;
	JLabel sensitivityLabel = new JLabel() ;
	JTextField sensitivity = new JTextField() ;
	JLabel milijanskys = new JLabel() ;
	JLabel integrationTimeLabel = new JLabel() ;
	JTextField integrationTime = new JTextField() ;
	JLabel secondsLabel = new JLabel() ;

	JPanel southernPanel = new JPanel() ;

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

		hiResLoFOVLabel.setFont( new Font( "Arial" , Font.PLAIN , 10 ) ) ;
		hiResLoFOVLabel.setText( "Hi Res/Lo FOV" ) ;
		loResHiFOVLabel.setFont( new Font( "Arial" , Font.PLAIN , 10 ) ) ;
		loResHiFOVLabel.setText( "Lo Res/Hi FOV" ) ;
		resolutionLabel.setText(  "Resolution : " ) ;
		OneDividedByCM.setText( "1/CM" ) ;
		FOVLabel.setText( "FOV" ) ;
		squareArcminutes.setText( "arcmin^2" ) ;
		resolution.setEditable( false ) ;
		FOV.setEditable( false ) ;
		resolutionFOV.setPaintTicks( true ) ;
		resolutionFOV.setPaintTrack( true ) ;
		Border resolutionBorder = BorderFactory.createTitledBorder( "Resolution/FOV" ) ;
		resolutionFOVPanel.setBorder( resolutionBorder ) ;
		resolutionFOVPanel.setLayout( new GridBagLayout() ) ;
		resolutionFOVPanel.add( resolutionFOV , new GridBagConstraints( 0 , 0 , 4 , 1 , 10. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( hiResLoFOVLabel , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( loResHiFOVLabel , new GridBagConstraints( 3 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( resolutionLabel , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( resolution , new GridBagConstraints( 1 , 2 , 2 , 1 , 10. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( OneDividedByCM , new GridBagConstraints( 3 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( FOVLabel , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( FOV , new GridBagConstraints( 1 , 3 , 2 , 1 , 10. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( squareArcminutes , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;

		hiSpdLoNyqLabel.setFont( new Font( "Arial" , Font.PLAIN , 10 ) ) ;
		hiSpdLoNyqLabel.setText( "Hi spd/Lo Nyq" ) ;
		loSpdHiNyqLabel.setFont( new Font( "Arial" , Font.PLAIN , 10 ) ) ;
		loSpdHiNyqLabel.setText( "Lo spd/Hi Nyq" ) ;
		scanSpeedLabel.setText( "Optical scan speed : " ) ;
		cmPerSecondLabel.setText( "cm/s" ) ;
		nyquistLabel.setText( "Nyquist : " ) ;
		OneOverByCM.setText( "1/cm" ) ;
		scanSpeed.setEditable( false ) ;
		nyquist.setEditable( false ) ;
		scanSpeedNyquist.setPaintTicks( true ) ;
		scanSpeedNyquist.setPaintTrack( true ) ;
		Border scanspeedNyquistBorder = BorderFactory.createTitledBorder( "Optical scan speed/Nyquist" ) ;
		scanSpeedNyquistPanel.setBorder( scanspeedNyquistBorder ) ;
		scanSpeedNyquistPanel.setLayout( new GridBagLayout() ) ;
		scanSpeedNyquistPanel.add( scanSpeedNyquist , new GridBagConstraints( 0 , 0 , 3 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		scanSpeedNyquistPanel.add( loSpdHiNyqLabel , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		scanSpeedNyquistPanel.add( hiSpdLoNyqLabel , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		scanSpeedNyquistPanel.add( scanSpeedLabel , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		scanSpeedNyquistPanel.add( scanSpeed , new GridBagConstraints( 1 , 2 , 1 , 1 , 10. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets(0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		scanSpeedNyquistPanel.add( cmPerSecondLabel , new GridBagConstraints( 2 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		scanSpeedNyquistPanel.add( nyquistLabel , new GridBagConstraints( 0 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		scanSpeedNyquistPanel.add( nyquist , new GridBagConstraints( 1 , 3 , 1 , 1 , 10. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		scanSpeedNyquistPanel.add( OneOverByCM , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	

		sensitivityLabel.setText( "Sensitivity : " ) ;
		milijanskys.setText( "mJy" ) ;
		integrationTimeLabel.setText( "Integration time : " ) ;
		secondsLabel.setText( "s" ) ;
		Border sensitivityTimeBorder = BorderFactory.createTitledBorder( "Sensitivity/Times" ) ;
		sensitivityTimePanel.setBorder( sensitivityTimeBorder ) ;
		sensitivityTimePanel.setLayout( new GridBagLayout() ) ;
		sensitivityTimePanel.add( sensitivityLabel , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( sensitivity , new GridBagConstraints( 1 , 0 , 1 , 1 , 0. , 10. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( milijanskys , new GridBagConstraints( 2 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( integrationTimeLabel , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( integrationTime , new GridBagConstraints( 1 , 1 , 1 , 1 , 10. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( secondsLabel , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	

		southernPanel.setLayout( new GridLayout( 2 , 2 ) ) ;
		southernPanel.add( resolutionFOVPanel ) ;
		southernPanel.add( scanSpeedNyquistPanel ) ;
		southernPanel.add( sensitivityTimePanel ) ;

		this.add( southernPanel , BorderLayout.SOUTH ) ;
	}

	public void southernPanelEnabled( boolean enabled )
	{
		Component[] components = sensitivityTimePanel.getComponents() ;
		for( Component component : components )
			component.setEnabled( enabled ) ;

		components = scanSpeedNyquistPanel.getComponents() ;
		for( Component component : components )
			component.setEnabled( enabled ) ;

		components = resolutionFOVPanel.getComponents() ;
		for( Component component : components )
			component.setEnabled( enabled ) ;
	}
}
