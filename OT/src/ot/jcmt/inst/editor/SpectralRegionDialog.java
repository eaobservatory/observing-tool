/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2003                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.inst.editor ;

import orac.jcmt.inst.SpDRRecipe ;
import orac.jcmt.inst.SpInstHeterodyne ;
import edfreq.EdFreq ;
import edfreq.FrequencyEditorCfg ;
import edfreq.region.SpectralRegionEditor ;
import ot.util.DialogUtil ;

import gemini.sp.obsComp.SpTelescopeObsComp ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpTreeMan ;

import java.awt.BorderLayout ;
import java.awt.Graphics ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import javax.swing.JDialog ;
import javax.swing.JPanel ;
import javax.swing.JButton ;

/**
 * This class provides a frame for the SpectralRegionEditor.
 *
 * An "OK" button is provided to store the settings to
 * a JCMT SpDRRecipe item of the OT.
 *
 * Note that this frame is a modal JDialog (like {@link ot.jcmt.inst.editor.ScubaArraysFrame}).
 *
 * @see edfreq.FrontEnd
 * @see edfreq.FrontEndFrame
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class SpectralRegionDialog extends JDialog implements ActionListener
{
	private JButton okButton = new JButton( "OK" ) ;
	private JButton cancelButton = new JButton( "Cancel" ) ;
	private JPanel buttonPanel = new JPanel() ;
	private EdDRRecipe _drRecipeEditor ;
	protected static FrequencyEditorCfg _cfg = FrequencyEditorCfg.getConfiguration() ;
	private SpectralRegionEditor _spectralRegionEditor = new SpectralRegionEditor( this ) ;

	public SpectralRegionDialog()
	{
		setTitle( "Baseline Fit and Line Regions" ) ;
		setModal( true ) ;

		buttonPanel.add( okButton ) ;
		buttonPanel.add( cancelButton ) ;

		add( _spectralRegionEditor , BorderLayout.CENTER ) ;
		add( buttonPanel , BorderLayout.SOUTH ) ;
		pack() ;
		setLocation( 100 , 100 ) ;

		setDefaultCloseOperation( HIDE_ON_CLOSE ) ;

		okButton.addActionListener( this ) ;
		cancelButton.addActionListener( this ) ;
	}

	public void show( SpDRRecipe drRecipe , SpInstHeterodyne instHeterodyne , EdDRRecipe drRecipeEditor )
	{
		if( instHeterodyne.getBand() == null )
		{
			DialogUtil.error( this , "Heterodyne component has not been edited." ) ;
			return ;
		}

		_drRecipeEditor = drRecipeEditor ;

		double redshift ;
		SpTelescopeObsComp tgt = SpTreeMan.findTargetList( instHeterodyne ) ;
		if( tgt != null )
		{
			SpTelescopePos tp = tgt.getPosList().getBasePosition();
			redshift = tp.getRedshift() ;
		}
		else
		{
			redshift = 0. ;
		}

		double feIF = instHeterodyne.getFeIF() ;
		double feBandWidth = instHeterodyne.getFeBandWidth() ;
		double restFrequency = instHeterodyne.getRestFrequency( 0 ) ;
		double obsFrequency = EdFreq.getObsFrequency( restFrequency , redshift ) ;
		double lo1Hz = EdFreq.getLO1( obsFrequency , instHeterodyne.getCentreFrequency( 0 ) , instHeterodyne.getBand() ) ;

		_spectralRegionEditor.setModeAndBand( instHeterodyne.getMode() , instHeterodyne.getBand() ) ;

		_spectralRegionEditor.updateLineDisplay( lo1Hz - ( feIF + ( 0.5 * feBandWidth ) ) , lo1Hz + ( feIF + ( 0.5 * feBandWidth ) ) , feIF , feBandWidth , redshift ) ;

		for( int i = 0 ; i < Integer.parseInt( instHeterodyne.getBandMode() ) ; i++ )
			_spectralRegionEditor.updateBackendValues( instHeterodyne.getCentreFrequency( i ) , instHeterodyne.getBandWidth( i ) , i ) ;

		_spectralRegionEditor.setMainLine( instHeterodyne.getRestFrequency( 0 ) ) ;

		_spectralRegionEditor.removeAllRegions( false ) ;

		_spectralRegionEditor.createCombinedRegions( false ) ;

		_spectralRegionEditor.resetLayout() ;

		setVisible( true ) ;
	}

	public void applyAndHide()
	{
		apply() ;
		setVisible( false ) ;
	}

	public void apply()
	{
		_drRecipeEditor.refresh() ;
	}

	public void cancel()
	{
		setVisible( false ) ;
	}

	public void update( Graphics g )
	{
		super.update( g ) ;
	}

	public void actionPerformed( ActionEvent e )
	{
		if( e.getSource() == okButton )
			applyAndHide() ;
		else if( e.getSource() == cancelButton )
			setVisible( false ) ;
	}
}
