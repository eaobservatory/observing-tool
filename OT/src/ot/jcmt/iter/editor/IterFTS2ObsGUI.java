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

@SuppressWarnings( "serial" )
public class IterFTS2ObsGUI extends JPanel
{
	JPanel portSetupPanel = new JPanel() ;
	JRadioButton dual = new JRadioButton() ;
	JRadioButton single = new JRadioButton() ;
	JRadioButton port8d = new JRadioButton() ;
	JRadioButton port8c = new JRadioButton() ;
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

	JPanel resolutionFOVPanel = new JPanel() ;
	JSlider resolutionFOV = new JSlider( JSlider.HORIZONTAL ) ;
	JLabel hiResLoFOVLabel = new JLabel() ;
	JLabel loResHiFOVLabel = new JLabel() ;
	JLabel resolutionLabel = new JLabel() ;
	JTextField resolution = new JTextField() ;
	JLabel MHzLabel = new JLabel() ;
	JTextField resolutionMHz = new JTextField() ;
	JLabel OneDividedByCM = new JLabel() ;
	JLabel FOVLabel = new JLabel() ;
	JTextField FOV = new JTextField() ;
	JLabel squareArcminutes = new JLabel() ;
	JLabel physicalDistanceLabel = new JLabel("Physical dist.") ;
	JTextField physicalDistance = new JTextField() ;
	JLabel physicalDistanceUnit = new JLabel("mm") ;
	JLabel physicalSpeedLabel = new JLabel("Physical speed") ;
	JTextField physicalSpeed = new JTextField() ;
	JLabel physicalSpeedUnit = new JLabel("mm/s") ;


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
	JLabel sensitivity450Label = new JLabel() ;
	JTextField sensitivity450 = new JTextField() ;
	JLabel sensitivity850Label = new JLabel() ;
	JTextField sensitivity850 = new JTextField() ;
	JLabel milijanskys450 = new JLabel() ;
	JLabel milijanskys850 = new JLabel() ;
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

		port8d.setText( "Port 1 (S4A and S8D)" ) ;
		port8c.setText( "Port 2 (S4B and S8C)" ) ;
		portsGroup.add( port8d ) ;
		portsGroup.add( port8c ) ;
		portSetupPanel.add( port8d , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		portSetupPanel.add( port8c , new GridBagConstraints( 1 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;		

		specialModesLabel.setText( "Special Modes " ) ;
		modesPanel.setLayout( specialModesBorderLayout ) ;
		modesPanel.add( specialModesLabel , BorderLayout.WEST ) ;
		modesPanel.add( specialModes , BorderLayout.CENTER ) ;
		
		leftPanel.add( portSetupPanel ) ;
		this.add( leftPanel , BorderLayout.CENTER ) ;
		this.add( modesPanel , BorderLayout.NORTH ) ;

		hiResLoFOVLabel.setFont( new Font( "Arial" , Font.PLAIN , 10 ) ) ;
		hiResLoFOVLabel.setText( "Hi Res/Lo FOV" ) ;
		loResHiFOVLabel.setFont( new Font( "Arial" , Font.PLAIN , 10 ) ) ;
		loResHiFOVLabel.setText( "Lo Res/Hi FOV" ) ;
		resolutionLabel.setText(  "Resolution : " ) ;
		MHzLabel.setText( "MHz" ) ;
		OneDividedByCM.setText( "1/CM" ) ;
		FOVLabel.setText( "FOV" ) ;
		squareArcminutes.setText( "arcmin^2" ) ;
		resolution.setEditable( false ) ;
		resolutionMHz.setEditable( false ) ;
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
		resolutionFOVPanel.add( resolutionMHz , new GridBagConstraints( 1 , 3 , 2 , 1 , 10. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( MHzLabel , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( FOVLabel , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( FOV , new GridBagConstraints( 1 , 4 , 2 , 1 , 10. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( squareArcminutes , new GridBagConstraints( 3 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;

		physicalDistance.setEditable(false);
		resolutionFOVPanel.add( physicalDistanceLabel , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( physicalDistance , new GridBagConstraints( 1 , 5 , 2 , 1 , 10. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		resolutionFOVPanel.add( physicalDistanceUnit , new GridBagConstraints( 3 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;


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
		physicalSpeed.setEditable(false);
		scanSpeedNyquistPanel.add( physicalSpeedLabel, new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		scanSpeedNyquistPanel.add( physicalSpeed , new GridBagConstraints( 1 , 4 , 1 , 1 , 10. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		scanSpeedNyquistPanel.add( physicalSpeedUnit , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	


		sensitivity450Label.setText( "Sensitivity @ 450 : " ) ;
		sensitivity850Label.setText( "Sensitivity @ 850 : " ) ;
		milijanskys450.setText( "mJy" ) ;
		milijanskys850.setText( "mJy" ) ;
		integrationTimeLabel.setText( "Integration time : " ) ;
		secondsLabel.setText( "s" ) ;
		sensitivity450.setEnabled( false ) ;
		sensitivity850.setEnabled( false ) ;
		Border sensitivityTimeBorder = BorderFactory.createTitledBorder( "Sensitivity/Times" ) ;
		sensitivityTimePanel.setBorder( sensitivityTimeBorder ) ;
		sensitivityTimePanel.setLayout( new GridBagLayout() ) ;
		sensitivityTimePanel.add( sensitivity450Label , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( sensitivity450 , new GridBagConstraints( 1 , 0 , 1 , 1 , 0. , 10. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( milijanskys450 , new GridBagConstraints( 2 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( sensitivity850Label , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( sensitivity850 , new GridBagConstraints( 1 , 1 , 1 , 1 , 0. , 10. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( milijanskys850 , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( integrationTimeLabel , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( integrationTime , new GridBagConstraints( 1 , 2 , 1 , 1 , 10. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	
		sensitivityTimePanel.add( secondsLabel , new GridBagConstraints( 2 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;	

		southernPanel.setLayout( new GridLayout( 2 , 2 ) ) ;
		southernPanel.add( resolutionFOVPanel ) ;
		southernPanel.add( scanSpeedNyquistPanel ) ;
		southernPanel.add( sensitivityTimePanel ) ;

		this.add( southernPanel , BorderLayout.SOUTH ) ;
	}

	public void southernPanelEnabled( boolean enabled )
	{
		integrationTime.setEnabled( true ) ;

		Component[]components = scanSpeedNyquistPanel.getComponents() ;
		for( Component component : components )
			component.setEnabled( enabled ) ;

		components = resolutionFOVPanel.getComponents() ;
		for( Component component : components )
			component.setEnabled( enabled ) ;
	}
}
