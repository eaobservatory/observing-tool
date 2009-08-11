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

import java.text.NumberFormat ;

import jsky.app.ot.OtCfg ;
import jsky.app.ot.editor.OtItemEditor ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetWatcher ;

import gemini.sp.SpItem ;
import gemini.sp.obsComp.SpInstObsComp ;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpTelescopeObsComp ;
import gemini.util.DDMMSS ;
import gemini.util.CoordSys ;
import gemini.sp.SpTelescopePos ;

import orac.jcmt.inst.SpJCMTInstObsComp ;
import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.inst.SpInstSCUBA ;
import orac.jcmt.iter.SpIterJCMTObs ;
import orac.jcmt.obsComp.SpSiteQualityObsComp ;
import orac.jcmt.util.ScubaNoise ;
import orac.jcmt.SpJCMTConstants ;
import orac.util.DrUtil ;
import orac.util.SpItemUtilities ;

/**
 * This is the generic editor for JCMT iterator components.
 * 
 * @author modified by Martin Folger ( M.Folger@roe.ac.uk )
 */
public class EdIterJCMTGeneric extends OtItemEditor implements DropDownListBoxWidgetWatcher , TextBoxWidgetWatcher , CheckBoxWidgetWatcher , SpJCMTConstants
{
	/**
	 * Error code for observe modes whose noise calculation is not implemented yet.
	 *
	 * This constant should remain distinct from the STATUS constants used in
	 * {@link orac.util.DrUtil}.
	 */
	protected static int NOISE_CALCULATION_STATUS_NOT_IMPLEMENTED = -5 ;

	protected IterJCMTGenericGUI _w ; // the GUI layout panel
	protected SpIterJCMTObs _iterObs ;
	protected String _noiseToolTip = "" ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterJCMTGeneric( IterJCMTGenericGUI w )
	{
		_title = "JCMT Observe" ;
		_presSource = _w = w ;
		_description = "Iterator Component for JCMT" ;
		_w.switchingMode.addWatcher( this ) ;
		_w.frequencyOffset_throw.addWatcher( this ) ;
		_w.frequencyOffset_rate.addWatcher( this ) ;
		_w.secsPerCycle.addWatcher( this ) ;
		_w.cycleReversal.addWatcher( this ) ;
		_w.continuousCal.addWatcher( this ) ;
		_w.stepSize.addWatcher( this ) ;
		_w.jiggleAtReference.addWatcher( this ) ;
		_w.jigglesPerCycle.addWatcher( this ) ;
		_w.sampleTime.addWatcher( this ) ;
		_w.automaticTarget.addWatcher( this ) ;
	}

	/**
	 * Override setup to store away a reference to the Scan Iterator.
	 */
	public void setup( SpItem spItem )
	{
		_iterObs = ( SpIterJCMTObs )spItem ;
		super.setup( spItem ) ;

		_w.switchingMode.deleteWatcher( this ) ;
		_w.switchingMode.setChoices( _iterObs.getSwitchingModeOptions() ) ;

		_w.switchingMode.setEnabled( _w.switchingMode.getItemCount() > 1 ) ;

		_w.switchingMode.setValue( _iterObs.getSwitchingMode() ) ;

		_w.switchingMode.addWatcher( this ) ;
	}

	public void dropDownListBoxAction( DropDownListBoxWidgetExt ddlbwe , int index , String val )
	{
		if( ddlbwe == _w.switchingMode )
		{
			if( val.equals( SWITCHING_MODE_FREQUENCY_S ) )
			{
				_w.frequencyPanel.setVisible( true ) ;
				_w.frequencyOffset_rate.setEnabled( false ) ;
				_w.switchRateVisible( true ) ;
				if( _iterObs.getSecsPerCycle() != 0 )
				{
					NumberFormat nf = NumberFormat.getInstance() ;
					nf.setMaximumFractionDigits( 5 ) ;
					_w.frequencyOffset_rate.setValue( nf.format( 1. / _iterObs.getSecsPerCycle() ) ) ;
				}
				else
				{
					_w.frequencyOffset_rate.setValue( 0. ) ;
				}
				_iterObs.setFrequencyOffsetRate( _w.frequencyOffset_rate.getValue() ) ;
				_iterObs.setFrequencyOffsetThrow( _w.frequencyOffset_throw.getValue() ) ;
			}
			else if( val.equals( SWITCHING_MODE_FREQUENCY_F ) )
			{
				_w.frequencyPanel.setVisible( true ) ;
				_w.frequencyOffset_rate.setEnabled( false ) ;
				_w.switchRateVisible( false ) ;
				_w.frequencyPanel.setVisible( false ) ;
				_iterObs.rmFrequencyOffsetRate() ;
				_iterObs.setFrequencyOffsetThrow( _w.frequencyOffset_throw.getValue() ) ;
			}
			_iterObs.setSwitchingMode( val ) ;
			_updateWidgets() ;
		}
	}

	public void dropDownListBoxSelect( DropDownListBoxWidgetExt ddlbwe , int index , String val ){}

	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		if( tbwe == _w.frequencyOffset_throw )
		{
			_iterObs.setFrequencyOffsetThrow( tbwe.getValue() ) ;
		}
		else if( tbwe == _w.frequencyOffset_rate )
		{
			_iterObs.setFrequencyOffsetRate( tbwe.getValue() ) ;
		}
		else if( tbwe == _w.secsPerCycle )
		{
			_iterObs.setSecsPerCycle( _w.secsPerCycle.getValue() ) ;
			if( !_w.frequencyOffset_rate.isEnabled() && _w.frequencyOffset_rate.isVisible() )
			{
				NumberFormat nf = NumberFormat.getInstance() ;
				nf.setMaximumFractionDigits( 5 ) ;
				_w.frequencyOffset_rate.setValue( nf.format( 1. / _iterObs.getSecsPerCycle() ) ) ;
				_iterObs.setFrequencyOffsetRate( _w.frequencyOffset_rate.getValue() ) ;
			}
		}
		else if( tbwe == _w.jigglesPerCycle )
		{
			_iterObs.setJigglesPerCycle( _w.jigglesPerCycle.getValue() ) ;
		}
		else if( tbwe == _w.stepSize )
		{
			_iterObs.setStepSize( _w.stepSize.getValue() ) ;
		}
		else if( tbwe == _w.sampleTime )
		{
			_iterObs.setSampleTime( _w.sampleTime.getValue() ) ;
		}
	}

	public void textBoxAction( TextBoxWidgetExt tbwe ){}

	public void checkBoxAction( CheckBoxWidgetExt cbwe )
	{
		if( cbwe == _w.cycleReversal )
			_iterObs.setCycleReversal( _w.cycleReversal.getBooleanValue() ) ;
		else if( cbwe == _w.jiggleAtReference )
			_iterObs.setJiggleAtReference( _w.jiggleAtReference.getBooleanValue() ) ;
		else if( cbwe == _w.automaticTarget )
			_iterObs.setAutomaticTarget( _w.automaticTarget.getBooleanValue() ) ;
		else if( cbwe == _w.continuousCal )
			_iterObs.setContinuousCal( _w.continuousCal.getBooleanValue() ) ;
	}

	protected void _updateWidgets()
	{
		setInstrument( SpTreeMan.findInstrument( _spItem ) ) ;

		String switchingMode = _iterObs.getSwitchingMode() ;
		_w.switchingMode.setValue( switchingMode ) ;
		if( ( switchingMode != null ) && ( SWITCHING_MODE_FREQUENCY_S.equals( switchingMode ) || SWITCHING_MODE_FREQUENCY_F.equals( switchingMode ) ) )
		{
			_w.frequencyPanel.setVisible( true ) ;
			_w.frequencyOffset_rate.setEnabled( false ) ;
			_w.switchRateVisible( !SWITCHING_MODE_FREQUENCY_F.equals( switchingMode ) ) ;
		}
		else
		{
			_w.frequencyPanel.setVisible( false ) ;
		}
		_w.frequencyOffset_throw.setValue( _iterObs.getFrequencyOffsetThrow() ) ;
		_w.frequencyOffset_rate.setValue( _iterObs.getFrequencyOffsetRate() ) ;
		_w.secsPerCycle.setValue( _iterObs.getSecsPerCycle() ) ;
		_w.cycleReversal.setValue( _iterObs.getCycleReversal() ) ;
		_w.stepSize.setValue( _iterObs.getStepSize() ) ;
		_w.jiggleAtReference.setValue( _iterObs.getJiggleAtReference() ) ;
		_w.jigglesPerCycle.setValue( _iterObs.getJigglesPerCycle() ) ;
		_w.sampleTime.setValue( _iterObs.getSampleTime() ) ;
		_w.automaticTarget.setValue( _iterObs.getAutomaticTarget() ) ;
		_w.noiseTextBox.setValue( calculateNoise() ) ;
		_w.noiseTextBox.setToolTipText( _noiseToolTip ) ;
		_w.continuousCal.setValue( _iterObs.getContinuousCal() ) ;
	}

	/**
	 * This method should be overridden by subclasses representing iterators whose appearance
	 * is different for different instruments.
	 */
	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		if( ( spInstObsComp != null ) && ( spInstObsComp instanceof SpInstHeterodyne ) )
		{
			_w.switchingMode.setVisible( true ) ;
			_w.switchingModeLabel.setVisible( true ) ;
			_w.jLabel2.setText( "K" ) ;

			_w.frequencyPanel.setVisible( _w.switchingMode.getValue() != null && ( _w.switchingMode.getValue().equals( SWITCHING_MODE_FREQUENCY_S ) || _w.switchingMode.getValue().equals( SWITCHING_MODE_FREQUENCY_F ) ) ) ;
		}
		else
		{
			_w.jLabel2.setText( "mJy" ) ;
			_w.switchingMode.setVisible( false ) ;
			_w.switchingModeLabel.setVisible( false ) ;
			_w.frequencyPanel.setVisible( false ) ;
		}
	}

	/**
	 * Returns noise information.
	 */
	protected String calculateNoise()
	{
		SpTelescopeObsComp telescopeObsComp = ( SpTelescopeObsComp )SpTreeMan.findTargetList( _iterObs ) ;
		if( telescopeObsComp == null )
		{
			_noiseToolTip = "No target" ;
			return "No target" ;
		}

		SpJCMTInstObsComp instObsComp = ( SpJCMTInstObsComp )SpTreeMan.findInstrument( _iterObs ) ;
		if( instObsComp == null )
		{
			_noiseToolTip = "No instruments" ;
			return "No instrument" ;
		}

		SpSiteQualityObsComp siteQualityObsComp = ( SpSiteQualityObsComp )SpItemUtilities.findSiteQuality( _iterObs ) ;
		if( siteQualityObsComp == null )
		{
			_noiseToolTip = "No site quality" ;
			return "No site quality" ;
		}

		double airmass = 0. ;
		
		SpTelescopePos base = telescopeObsComp.getPosList().getBasePosition() ;
		if( base.getCoordSys() == CoordSys.FK5 )
			airmass = DrUtil.airmass( base.getYaxis() , DDMMSS.valueOf( OtCfg.getTelescopeLatitude() ) ) ;
		else if( base.getCoordSys() == CoordSys.AZ_EL )
			airmass = DrUtil.airmass( base.getYaxis() ) ;
		
		double csoTau = siteQualityObsComp.getNoiseCalculationTau() ;

		if( instObsComp instanceof SpInstSCUBA )
		{
			int[] status = { 0 } ;
			double noise = 0. ;
			double wavelength ;
			double nefd ;

			if( ( (( SpInstSCUBA )instObsComp).getFilter() != null ) && (( SpInstSCUBA )instObsComp).getFilter().toUpperCase().endsWith( "PHOT" ) )
			{

				if( ( ( SpInstSCUBA )instObsComp ).getPrimaryBolometer() == null )
				{
					_noiseToolTip = "No wavelength" ;
					return "No wavelength" ;
				}

				wavelength = Double.parseDouble( ( ( SpInstSCUBA )instObsComp ).getPrimaryBolometer().substring( 1 ) ) ;
				nefd = ScubaNoise.scunefd( wavelength , airmass , csoTau , status ) ;
				noise = calculateNoise( wavelength , nefd , status ) ;

				if( status[ 0 ] == 0 )
				{
					_noiseToolTip = "airmass = " + ( Math.rint( airmass * 10 ) / 10 ) + ", nefd = " + ( Math.rint( nefd * 10 ) / 10 ) + ", noise = " + ( Math.rint( noise * 10 ) / 10 ) ;
					return "" + ( Math.rint( noise * 10 ) / 10 ) ;
				}
			}
			else
			{
				String noise450Str ;

				wavelength = 450. ;
				double nefd450 = ScubaNoise.scunefd( wavelength , airmass , csoTau , status ) ;
				double noise450 = calculateNoise( wavelength , nefd450 , status ) ;

				if( status[ 0 ] == NOISE_CALCULATION_STATUS_NOT_IMPLEMENTED )
				{
					_noiseToolTip = "Not implemented" ;
					return "Not implemented." ;
				}

				if( status[ 0 ] == 0 )
					noise450Str = "" + ( Math.rint( noise450 * 10 ) / 10 ) ;
				else
					noise450Str = "error " + status[ 0 ] ;

				String noise850Str ;

				wavelength = 850. ;
				double nefd850 = ScubaNoise.scunefd( wavelength , airmass , csoTau , status ) ;
				double noise850 = calculateNoise( wavelength , nefd850 , status ) ;

				if( status[ 0 ] == 0 )
					noise850Str = "" + ( Math.rint( noise850 * 10 ) / 10 ) ;
				else
					noise850Str = "error " + status[ 0 ] ;

				_noiseToolTip = "airmass = " + ( Math.rint( airmass * 10 ) / 10 ) + ", nefd(450) = " + ( Math.rint( nefd450 * 10 ) / 10 ) + ", noise(450) = " + ( Math.rint( noise450 * 10 ) / 10 ) + ", nefd(850) = " + ( Math.rint( nefd850 * 10 ) / 10 ) + ", noise(850) = " + ( Math.rint( noise850 * 10 ) / 10 ) ;

				return "" + noise450Str + " (450), " + noise850Str + " (850)" ;
			}
		}
		else if( instObsComp instanceof SpInstHeterodyne )
		{
			return "" + calculateNoise( ( SpInstHeterodyne )instObsComp , airmass , csoTau ) ;
		}

		_noiseToolTip = "Not available" ;
		return "Not available" ;
	}

	/**
	 * Returns noise.
	 * 
	 * This method should be implemented by subclasses taking into accound the observe mode and whether length and width are needed (SCAN more only).
	 */
	protected double calculateNoise( double wavelength , double nefd , int[] status )
	{
		status[ 0 ] = NOISE_CALCULATION_STATUS_NOT_IMPLEMENTED ;
		return 0. ;
	}

	protected double calculateNoise( SpInstHeterodyne inst , double airmass , double tau )
	{
		return 0. ;
	}
}
