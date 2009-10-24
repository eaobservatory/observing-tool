/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.iter.editor ;

import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.BorderFactory ;
import java.awt.GridBagLayout ;
import java.awt.GridLayout ;
import java.awt.BorderLayout ;
import java.awt.Color ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Font ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;
import javax.swing.border.Border ;
import javax.swing.border.BevelBorder ;
import javax.swing.border.TitledBorder ;
import orac.util.JThermometer ;
import ot.gui.GuiUtil;

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */
@SuppressWarnings( "serial" )
public class IterRasterObsGUI extends IterJCMTGenericGUI
{
	JPanel rasterPanel = new JPanel() ;
	
	// area panel
	private JPanel areaPanel = new JPanel() ;
	public TextBoxWidgetExt width = new TextBoxWidgetExt() ;
	public TextBoxWidgetExt height = new TextBoxWidgetExt() ;
	public TextBoxWidgetExt posAngle = new TextBoxWidgetExt() ;
	public TextBoxWidgetExt dx = new TextBoxWidgetExt() ;
	public TextBoxWidgetExt dy = new TextBoxWidgetExt() ;
	public JPanel subAreaPanel = new JPanel() ;
	
	// scan panel
	public JPanel scanPanel = new JPanel() ;
	public DropDownListBoxWidgetExt scanSystem = new DropDownListBoxWidgetExt() ;
	public DropDownListBoxWidgetExt scanAngle = new DropDownListBoxWidgetExt() ;
	
	// heterodyne
	public JPanel heterodynePanel = new JPanel() ;
	public TextBoxWidgetExt acsisSampleTime = new TextBoxWidgetExt() ;
	public CommandButtonWidgetExt defaultButton = new CommandButtonWidgetExt() ;
	public TextBoxWidgetExt secsPerRow = new TextBoxWidgetExt() ;
	public TextBoxWidgetExt secsPerObservation = new TextBoxWidgetExt() ;
	
	// non harp panel
	public JPanel nonHarpPanel = new JPanel() ;
	public TextBoxWidgetExt sizeOfXPixel = new TextBoxWidgetExt() ;
	public TextBoxWidgetExt sizeOfYPixel = new TextBoxWidgetExt() ;
	private JLabel dimensionWarningTop = GuiUtil.createLabel( "Dimension must be equally" , Color.red ) ;
	private JLabel dimensionWarningBottom = GuiUtil.createLabel( "divisible by spacing" , Color.red ) ;
	
	// harp
	private JPanel harpPanel = new JPanel() ;
	DropDownListBoxWidgetExt harpRasters = new DropDownListBoxWidgetExt() ;
	
	// scuba2
	private JPanel scuba2Panel = new JPanel() ;
	public JPanel mapCyclesPanel = new JPanel() ;
	public TextBoxWidgetExt numberOfMapCycles = new TextBoxWidgetExt() ;
	public JPanel pointSourcePanel = new JPanel() ;
	public TextBoxWidgetExt pointSourceTime = new TextBoxWidgetExt() ;
	private JPanel scanSpeedPanel = new JPanel() ;
	public TextBoxWidgetExt scanSpeed = new TextBoxWidgetExt() ;
	
	// harp and scuba2
	private JLabel scanStrategyLabel = createLabel( "Scan Strategy" ) ;
	public DropDownListBoxWidgetExt scanningStrategies = new DropDownListBoxWidgetExt() ;

	// misc
	public JThermometer thermometer = new JThermometer() ; // ?
	public CheckBoxWidgetExt rowReversal = new CheckBoxWidgetExt() ; // ?
	
	// convenience variables ( rather than static imports )
	private int GBWEST = GridBagConstraints.WEST ;
	private int GBEAST = GridBagConstraints.EAST ;
	private int GBHORIZONTAL = GridBagConstraints.HORIZONTAL ;
	private int GBNONE = GridBagConstraints.NONE ;
	private int GBCENTER = GridBagConstraints.CENTER ;
	private int GBSOUTHWEST = GridBagConstraints.SOUTHWEST ;
	private int GBSOUTH = GridBagConstraints.SOUTH ;
	private int GBBOTH = GridBagConstraints.BOTH ;

	public IterRasterObsGUI()
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
		// misc setup, probably not required
		rowReversal.setText( "Row Reversal" ) ;
		rowReversal.setFont( new Font( "Dialog" , 0 , 12 ) ) ;
		
		// initial setup of the main raster panel
		Border bevelBorder = BorderFactory.createBevelBorder( BevelBorder.LOWERED ) ;
		Border titleBorder = BorderFactory.createTitledBorder( bevelBorder , "Scan setup" ) ;
		rasterPanel.setBorder( titleBorder ) ;
		rasterPanel.setLayout( new GridLayout( 1 , 2 ) ) ;
		
		// initial setup of the SCUBA and ACSIS panel
		JPanel scubaAcsisPanel = new JPanel() ;
		scubaAcsisPanel.setLayout( new BorderLayout() ) ;
		
		// initial setup of the area panel
		areaPanel.setLayout( new GridBagLayout() ) ;
		areaPanel.setBorder( new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Area" ) ) ;
		
		// fill in the contents of the area panel
		// width		
		areaPanel.add( GuiUtil.createLabel( "Width" ) , new GridBagConstraints( 0 , 0 , 2 , 1 , 0. , 0. , GBWEST , GBNONE , insetsL( 5 ) , 0 , 0 ) ) ;
		areaPanel.add( width , new GridBagConstraints( 2 , 0 , 1 , 1 , 0. , 0. , GBCENTER , GBHORIZONTAL , allInsets( 2 ) , 0 , 0 ) ) ;
		areaPanel.add( GuiUtil.createLabel( "(arcsecs)" ) , new GridBagConstraints( 3 , 0 , 1 , 1 , 0. , 0. , GBEAST , GBNONE , allInsets( 0 ) , 0 , 0 ) ) ;

		// height
		areaPanel.add( GuiUtil.createLabel( "Height" ) , new GridBagConstraints( 0 , 1 , 2 , 1 , 0. , 0. , GBWEST , GBNONE , insetsL( 5 ) , 0 , 0 ) ) ;
		areaPanel.add( height , new GridBagConstraints( 2 , 1 , 1 , 1 , 0. , 0. , GBCENTER , GBHORIZONTAL , allInsets( 2 ) , 0 , 0 ) ) ;
		areaPanel.add( GuiUtil.createLabel( "(arcsecs)" ) , new GridBagConstraints( 3 , 1 , 1 , 1 , 0. , 0. , GBEAST , GBNONE , allInsets( 0 ) , 0 , 0 ) ) ;
		
		// PA
		posAngle.setColumns( 5 ) ;
		areaPanel.add( GuiUtil.createLabel( "PA" ) , new GridBagConstraints( 0 , 3 , 2 , 1 , 0. , 0. , GBWEST , GBNONE , insetsL( 5 ) , 0 , 0 ) ) ;
		areaPanel.add( posAngle , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GBCENTER , GBHORIZONTAL , allInsets( 2 ) , 0 , 0 ) ) ;
		areaPanel.add( GuiUtil.createLabel( "(degrees)" ) , new GridBagConstraints( 3 , 3 , 1 , 1 , 0. , 0. , GBEAST , GBNONE , allInsets( 0 ) , 0 , 0 ) ) ;
		
		// dx
		areaPanel.add( GuiUtil.createLabel( "Sample Spacing" ) , new GridBagConstraints( 0 , 5 , 2 , 1 , 0. , 0. , GBWEST , GBHORIZONTAL , insetsTL( 10 , 5 ) , 0 , 0 ) ) ;
		areaPanel.add( dx , new GridBagConstraints( 2 , 5 , 1 , 1 , 1. , 0. , GBCENTER , GBHORIZONTAL , allInsets( 2 ) , 0 , 0 ) ) ;
		areaPanel.add( GuiUtil.createLabel( "(arcsecs)" ) , new GridBagConstraints( 3 , 5 , 1 , 1 , 0. , 0. , GBEAST , GBNONE , allInsets( 0 ) , 0 , 0 ) ) ;
		
		// dy
		subAreaPanel.setLayout( new GridLayout( 1 , 3 ) ) ;
		subAreaPanel.add( GuiUtil.createLabel( "Scan Spacing" ) ) ;
		subAreaPanel.add( dy ) ;
		subAreaPanel.add( GuiUtil.createLabel( "(arcsecs)" ) ) ;
		areaPanel.add( subAreaPanel , new GridBagConstraints( 0 , 6 , 4 , 1 , 0. , 0. , GBCENTER , GBHORIZONTAL , insetsTL( 10 , 5 ) , 0 , 0 ) ) ;
		
		// scan panel
		scanPanel.setLayout( new GridBagLayout() ) ;
		scanPanel.setBorder( new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Scan" ) ) ;

		scanPanel.add( GuiUtil.createLabel( "System" ) , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GBWEST , GBNONE , insetsL( 5 ) , 0 , 0 ) ) ;
		scanPanel.add( scanSystem , new GridBagConstraints( 1 , 1 , 2 , 1 , 0. , 0. , GBSOUTHWEST , GBNONE , insetsL( 2 ) , 0 , 0 ) ) ;
		scanPanel.add( GuiUtil.createLabel( "PA" ) , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 1. , GBWEST , GBNONE , insetsLB( 5 , 5 ) , 0 , 0 ) ) ;
		scanPanel.add( scanAngle , new GridBagConstraints( 1 , 0 , 1 , 1 , 0. , 0. , GBSOUTH , GBHORIZONTAL , allInsets( 2 ) , 0 , 0 ) ) ;
		scanPanel.add( GuiUtil.createLabel( "(degrees)" ) , new GridBagConstraints( 2 , 0 , 1 , 1 , 0. , 0. , GBEAST , GBNONE , allInsets( 0 ) , 0 , 0 ) ) ;

		scubaAcsisPanel.add( areaPanel , BorderLayout.CENTER ) ;
		scubaAcsisPanel.add( scanPanel , BorderLayout.SOUTH ) ;
		
		// Heterodyne
		heterodynePanel.setBorder( new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "Heterodyne Details" ) ) ;
		heterodynePanel.setLayout( new GridBagLayout() ) ;
		heterodynePanel.add( GuiUtil.createLabel( "Sample Time" ) , new GridBagConstraints( 0 , 4 , 1 , 1 , 0. , 0. , GBWEST , GBNONE , insetsL( 5 ) , 0 , 0 ) ) ;
		heterodynePanel.add( acsisSampleTime , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GBCENTER , GBHORIZONTAL , allInsets( 5 ) , 0 , 0 ) ) ;
		heterodynePanel.add( GuiUtil.createLabel( "(sec)" ) , new GridBagConstraints( 2 , 4 , 1 , 1 , 0. , 0. , GBCENTER , GBNONE , insetsR( 5 ) , 0 , 0 ) ) ;
		defaultButton.setText( "Default" ) ;
		heterodynePanel.add( defaultButton , new GridBagConstraints( 1 , 5 , 2 , 1 , 0. , 0. , GBWEST , GBNONE , allInsets( 5 ) , 0 , 0 ) ) ;
		heterodynePanel.add( GuiUtil.createLabel( "Secs/Row" ) , new GridBagConstraints( 0 , 6 , 1 , 1 , 0. , 0. , GBWEST , GBNONE , insetsL( 5 ) , 0 , 0 ) ) ;
		heterodynePanel.add( GuiUtil.createLabel( "(estimated)" ) , new GridBagConstraints( 0 , 7 , 1 , 1 , 0. , 0. , GBWEST , GBNONE , insetsL( 5 ) , 0 , 0 ) ) ;
		heterodynePanel.add( GuiUtil.createLabel( "Secs/Observation" ) , new GridBagConstraints( 0 , 9 , 1 , 1 , 0. , 0. , GBWEST , GBNONE , insetsL( 5 ) , 0 , 0 ) ) ;
		heterodynePanel.add( secsPerRow , new GridBagConstraints( 1 , 6 , 1 , 1 , 0. , 0. , GBCENTER , GBHORIZONTAL , allInsets( 5 ) , 0 , 0 ) ) ;
		heterodynePanel.add( secsPerObservation , new GridBagConstraints( 1 , 9 , 2 , 1 , 0. , 0. , GBCENTER , GBHORIZONTAL , allInsets( 5 ) , 0 , 0 ) ) ;
		secsPerObservation.setEditable( false ) ;
		secsPerRow.setEditable( false ) ;
		
		// non harp
		nonHarpPanel.setLayout( new GridBagLayout() ) ;
		nonHarpPanel.add( GuiUtil.createLabel( " " ) , new GridBagConstraints( 0 , 0 , 3 , 1 , 0. , 0. , GBCENTER , GBBOTH , allInsets( 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( GuiUtil.createLabel( "Nr of samples/pixels" ) , new GridBagConstraints( 0 , 1 , 2 , 1 , 0. , 0. , GBCENTER , GBBOTH , allInsets( 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( sizeOfXPixel , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GBCENTER , GBBOTH , allInsets( 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( GuiUtil.createLabel( "Nr of scans/pixels" ) , new GridBagConstraints( 0 , 3 , 2 , 1 , 0. , 0. , GBCENTER , GBBOTH , allInsets( 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( sizeOfYPixel , new GridBagConstraints( 1 , 4 , 1 , 1 , 0. , 0. , GBCENTER , GBBOTH , allInsets( 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( dimensionWarningTop , new GridBagConstraints( 0 , 5 , 3 , 1 , 0. , 0. , GBCENTER , GBNONE , allInsets( 0 ) , 0 , 0 ) ) ;
		nonHarpPanel.add( dimensionWarningBottom , new GridBagConstraints( 0 , 6 , 3 , 1 , 0. , 0. , GBCENTER , GBNONE , allInsets( 0 ) , 0 , 0 ) ) ;
		sizeOfXPixel.setEnabled( false ) ;
		sizeOfYPixel.setEnabled( false ) ;
		showDimensionWarning( false ) ;
		
		// harp
		harpPanel.setLayout( new GridBagLayout() ) ;
		harpPanel.add( GuiUtil.createLabel( "Scan Spacing" ) , new GridBagConstraints( 0 , 3 , 2 , 1 , 0. , 0. , GBWEST , GBNONE , insetsL( 5 ) , 0 , 0 ) ) ;
		harpPanel.add( harpRasters , new GridBagConstraints( 2 , 3 , 2 , 1 , 0. , 0. , GBEAST , GBHORIZONTAL , insetsL( 5 ) , 0 , 0 ) ) ;

		// scuba2
		scuba2Panel.setBorder( new TitledBorder( BorderFactory.createLineBorder( new Color( 153 , 153 , 153 ) , 2 ) , "SCUBA-2 Details" ) ) ;
		scuba2Panel.setLayout( new GridLayout( 10 , 1 ) ) ;
		
		mapCyclesPanel.setLayout( new GridLayout( 1 , 2 ) ) ;
		mapCyclesPanel.add( GuiUtil.createLabel( "Times round map" ) ) ;
		mapCyclesPanel.add( numberOfMapCycles ) ;
		scuba2Panel.add( mapCyclesPanel ) ;
		
		pointSourcePanel.setLayout( new GridLayout( 1 , 3 ) ) ;
		pointSourcePanel.add( GuiUtil.createLabel( "Integration time" ) ) ;
		pointSourcePanel.add( pointSourceTime ) ;
		pointSourcePanel.add( GuiUtil.createLabel( "Seconds" ) ) ;
		scuba2Panel.add( pointSourcePanel ) ;
		
		scanSpeedPanel.setLayout( new GridLayout( 1 , 3 ) ) ;
		scanSpeedPanel.add( GuiUtil.createLabel( "Scan Speed" ) ) ;
		scanSpeedPanel.add( scanSpeed ) ;
		scanSpeed.setEnabled( false ) ;
		scanSpeedPanel.add( GuiUtil.createLabel( "ArcSec/Sec" ) ) ;
		scuba2Panel.add( scanSpeedPanel ) ;
		
		rasterPanel.add( scubaAcsisPanel ) ;
		rasterPanel.add( heterodynePanel ) ;
		this.add( rasterPanel , BorderLayout.CENTER ) ;
		
		addNonHarpPanel() ;
	}

	private boolean harped = false ;

	public boolean isHarped()
	{
		return harped ;
	}

	public void addHarpPanel()
	{
		areaPanel.remove( nonHarpPanel ) ;
		areaPanel.add( harpPanel , new GridBagConstraints( 0 , 7 , 3 , 1 , 0. , 0. , GBCENTER , GBNONE , allInsets( 0 ) , 0 , 0 ) ) ;

		heterodynePanel.add( scanStrategyLabel , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GBWEST , GBNONE , insetsL( 5 ) , 0 , 0 ) ) ;
		heterodynePanel.add( scanningStrategies , new GridBagConstraints( 1 , 2 , 1 , 1 , 0. , 0. , GBWEST , GBNONE , insetsL( 5 ) , 0 , 0 ) ) ;
		scanStrategyLabel.setVisible( true ) ;
		scanningStrategies.setVisible( true ) ;

		harped = true ;
	}

	public void addNonHarpPanel()
	{
		areaPanel.remove( harpPanel ) ;
		areaPanel.add( nonHarpPanel , new GridBagConstraints( 0 , 7 , 3 , 1 , 0. , 0. , GBCENTER , GBNONE , allInsets( 0 ) , 0 , 0 ) ) ;

		//scanStrategyLabel.setVisible( false ) ;
		//scanningStrategies.setVisible( false ) ;

		harped = false ;
	}

	private boolean scuba2d = false ;

	public boolean isScuba2d()
	{
		return scuba2d ;
	}

	public void addScuba2Panel()
	{
		rasterPanel.remove( heterodynePanel ) ;

		scuba2Panel.add( scanStrategyLabel ) ;
		scuba2Panel.add( scanningStrategies ) ;
		scanStrategyLabel.setVisible( true ) ;
		scanningStrategies.setVisible( true ) ;

		rasterPanel.add( scuba2Panel ) ;
		scuba2d = true ;
	}

	public void addNonScuba2Panel()
	{
		rasterPanel.remove( scuba2Panel ) ;

		rasterPanel.add( heterodynePanel ) ;
		scuba2d = false ;
	}
	
	public void showDimensionWarning( boolean show )
	{
		dimensionWarningTop.setVisible( show ) ;
		dimensionWarningBottom.setVisible( show ) ;
	}	
	
	private Insets insetsL( int left )
	{
		return new Insets( 0 , left , 0 , 0 ) ;
	}
	
	private Insets insetsTL( int top , int left )
	{
		return new Insets( top , left , 0 , 0 ) ;
	}
	
	private Insets insetsLB( int left , int bottom )
	{
		return new Insets( 0 , left , bottom , 0 ) ;
	}
	
	private Insets insetsR( int right )
	{
		return new Insets( 0 , 0 , 0 , right ) ;
	}
	
	private Insets allInsets( int value )
	{
		return new Insets( value , value , value , value ) ;
	}
}
