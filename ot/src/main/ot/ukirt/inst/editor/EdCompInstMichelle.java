// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * author: Alan Pickup = dap@roe.ac.uk         2001 Feb
 */

package ot.ukirt.inst.editor ;

import orac.ukirt.inst.SpInstMichelle ;

import gemini.sp.SpItem ;
import gemini.sp.obsComp.SpInstObsComp ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetWatcher ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetWatcher ;

import jsky.app.ot.tpe.TelescopePosEditor ;
import jsky.app.ot.tpe.TpeManager ;

import java.awt.CardLayout ;

/**
 * This is the editor for the Michelle instrument
 */
public final class EdCompInstMichelle extends EdCompInstBase
{
	private SpInstMichelle _instMichelle ;
	private boolean haveInitialised = false ;
	private MichelleGUI _w ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdCompInstMichelle()
	{
		_title = "Michelle" ;
		_presSource = _w = new MichelleGUI() ;
		_description = "The Michelle instrument is configured with this component." ;

		_w.camera.addItem( "imaging" ) ;
		_w.camera.addItem( "spectroscopy" ) ;

		//
		// Camera
		//
		_w.camera.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_updateCamera( val ) ;
				_updateWidgets() ;

				TelescopePosEditor tpe = TpeManager.get( _instMichelle ) ;
				if( tpe != null )
					tpe.repaint() ;
			}
		} ) ;

		//
		// Polarimetry
		//
		_w.polarimetry.addWatcher( new CheckBoxWidgetWatcher()
		{
			public void checkBoxAction( CheckBoxWidgetExt cb )
			{
				if( cb.getBooleanValue() )
					_instMichelle.setPolarimetry( "yes" ) ;
				else
					_instMichelle.setPolarimetry( "no" ) ;
				
				_instMichelle.useDefaultAcquisition() ;
				_updateWidgets() ;
			}
		} ) ;

		//
		// GUIs in imaging group
		//

		//
		// Filter category
		//
		_w.imaging_filterCategory.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instMichelle.setFilterCategory( val ) ;
				_updateFilterChoices() ;
				_instMichelle.useDefaultAcquisition() ;
				_updateWidgets() ;
			}
		} ) ;

		//
		// Filter
		//
		_w.imaging_filter.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instMichelle.setFilterOT( val ) ;
				_instMichelle.useDefaultCentralWavelength() ;
				_instMichelle.useDefaultAcquisition() ;
				_updateWidgets() ;
			}
		} ) ;

		//
		// GUIs in spectroscopy group
		//

		//
		// Disperser
		//
		_w.spectroscopy_grating.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instMichelle.setDisperser( val ) ;
				_updateCentralWavelength() ;
				_updateOrder() ;
				_instMichelle.useDefaultAcquisition() ;
				_updateWidgets() ;

				TelescopePosEditor tpe = TpeManager.get( _instMichelle ) ;
				if( tpe != null )
					tpe.repaint() ;
			}
		} ) ;

		//
		// Wavelength
		//
		_w.spectroscopy_wavelength.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				try
				{
					_instMichelle.setCentralWavelength( tbw.getText() ) ;
					_instMichelle.useDefaultAcquisition() ;
					_updateWidgets() ;
				}
				catch( Exception ex )
				{
					// ignore
				}
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){} // ignore
		} ) ;

		//
		// Order
		//
		_w.spectroscopy_order.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				try
				{
					_instMichelle.setOrder( Integer.parseInt( tbw.getText() ) ) ;
					_instMichelle.useDefaultAcquisition() ;
					_updateWidgets() ;
				}
				catch( Exception ex )
				{
					// ignore
				}
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){} // ignore
		} ) ;

		//
		// Default order
		//
		_w.spectroscopy_default.addWatcher( new CommandButtonWidgetWatcher()
		{
			public void commandButtonAction( CommandButtonWidgetExt cbw )
			{
				_instMichelle.useDefaultOrder() ;
				_updateOrder() ;
				_instMichelle.useDefaultAcquisition() ;
				_updateWidgets() ;
			}
		} ) ;

		//
		// Mask
		//
		/*
		 _w.spectroscopy_mask.setChoices(_instMichelle.getMaskList()) ;
		 */

		_w.spectroscopy_mask.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instMichelle.setMask( val ) ;
				_instMichelle.useDefaultAcquisition() ;
				_updateWidgets() ;

				TelescopePosEditor tpe = TpeManager.get( _instMichelle ) ;
				if( tpe != null )
					tpe.repaint() ;
			}
		} ) ;

		//
		// Position angle
		//
		_w.spectroscopy_posAngle.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				System.out.println( "In text box watcher..." ) ;
				_instMichelle.getAvEditFSM().setEachEditNotifies( false ) ;
				_instMichelle.getAvEditFSM().deleteObserver( EdCompInstMichelle.this ) ;
				_instMichelle.setPosAngleDegrees( Double.parseDouble( tbw.getText() ) ) ;
				_instMichelle.getAvEditFSM().addObserver( EdCompInstMichelle.this ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){} // ignore
		} ) ;

		//
		// Pixel sampling
		//
		_w.spectroscopy_sampling.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instMichelle.setPixelSampling( val ) ;
				_updateWidgets() ;
			}
		} ) ;

		//
		// Exposure time
		//
		_w.dataAcq_exposureTime.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				try
				{
					String ets = tbw.getText() ;
					double et = Double.parseDouble( ets ) ;
					if( et > 0.00001 )
					{
						_instMichelle.setExpTime( ets ) ;
						_updateWidgets( _w.dataAcq_exposureTime ) ;
					}
				}
				catch( Exception ex )
				{
					// ignore
				}
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){} // ignore
		} ) ;

		//
		// Default exposure time
		//
		_w.dataAcq_defaultExpTime.addWatcher( new CommandButtonWidgetWatcher()
		{
			public void commandButtonAction( CommandButtonWidgetExt cbw )
			{
				_instMichelle.useDefaultExposureTime() ;
				_updateWidgets() ;
			}
		} ) ;

		//
		// Observation time
		//
		_w.dataAcq_observationTime.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				try
				{
					String ots = tbw.getText() ;
					double ot = Double.parseDouble( ots ) ;
					if( ot > 0.00001 )
					{
						_instMichelle.setObservationTime( ot ) ;
						_instMichelle.setCoadds( 0 ) ;
						_updateWidgets( _w.dataAcq_observationTime ) ;
					}
				}
				catch( Exception ex )
				{
					// ignore
				}
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){} // ignore
		} ) ;

		//
		// Default observation time
		//
		_w.dataAcq_defaultObsTime.addWatcher( new CommandButtonWidgetWatcher()
		{
			public void commandButtonAction( CommandButtonWidgetExt cbw )
			{
				_instMichelle.useDefaultObservationTime() ;
				_updateObsTime() ;
				_updateWidgets() ;
			}
		} ) ;
	}

	/**
	 * Override method in super class to avoid exposure time and position angle text box watchers
	 * being added twice.
	 */
	protected void _init(){}

	/**
	 * Override setup to store away a reference to the SpInstMichelle item.
	 */
	public void setup( SpItem spItem )
	{
		_instMichelle = ( SpInstMichelle )spItem ;
		haveInitialised = false ;
		super.setup( spItem ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		_updateWidgets( null ) ;
	}

	protected void _updateWidgets( Object source )
	{
		if( !haveInitialised )
		{
			// Load drop down lists only first time in
			_showCamera() ;
			_updateFilterCategoryChoices() ;
			_updateFilterCategory() ;
			_updateFilterChoices() ;
			_updateGratingChoices() ;
			_updateMaskChoices() ;
			_updateSamplingChoices() ;
			_updatePolarimetry() ;
			_updateCentralWavelength() ;
			_updateFilter() ;
			_updateObsTime() ;
			haveInitialised = true ;
		}
		_updateScienceFOV() ;
		if( !_instMichelle.isImaging() )
		{
			_updateResolvingPower() ;
			_updateWavelengthCoverage() ;
			_updateSpecFilter() ;
		}
		_updateAcquisition( source ) ;
		_updateChopFreq() ;
		_updateDutyCycle() ;

		//super._updateWidgets() ;
		TextBoxWidgetExt tbwe ;
		tbwe = getPosAngleTextBox() ;
		tbwe.setText( ( ( SpInstObsComp )_spItem ).getPosAngleDegreesStr() ) ;

		if( _w.dataAcq_exposureTime != source )
		{
			tbwe = getExposureTimeTextBox() ;
			tbwe.setText( ( ( SpInstObsComp )_spItem ).getExposureTimeAsString() ) ;
		}
	}

	//
	// Update the observation time
	//
	private void _updateObsTime()
	{
		String obts = _instMichelle.getObservationTimeString() ;
		_w.dataAcq_observationTime.setText( obts ) ;
	}

	//
	// Update the polarimetry check box
	//
	private void _updatePolarimetry()
	{

		String p = _instMichelle.getPolarimetry() ;
		_w.polarimetry.setValue( p.equalsIgnoreCase( "yes" ) ) ;
	}

	//
	// Update the central wavelength
	//
	private void _updateCentralWavelength()
	{

		_w.spectroscopy_wavelength.setText( _instMichelle.getCentralWavelengthString() ) ;
		_updateOrder() ;
	}

	//
	// Update the order
	//
	private void _updateOrder()
	{
		_w.spectroscopy_order.setText( _instMichelle.getOrderString() ) ;
	}

	//
	// Update the spectral resolving power
	//
	private void _updateResolvingPower()
	{
		_w.spectroscopy_resolvingPower.setText( _instMichelle.getResolvingPowerString() ) ;
	}

	//
	// Update the science field of view displays on both the 
	// imaging and spectroscopy panes
	//
	private void _updateScienceFOV()
	{
		String scienceArea = _instMichelle.getScienceAreaString() ;
		_w.imaging_fieldOfView.setText( scienceArea ) ;
		_w.spectroscopy_fieldOfView.setText( scienceArea ) ;
	}

	//
	// Update the wavelength coverage
	//
	private void _updateWavelengthCoverage()
	{
		_w.spectroscopy_coverage.setText( _instMichelle.getSpectralCoverageString() ) ;
	}

	//
	// Update the imaging filter
	//
	private void _updateFilter()
	{
		_w.imaging_filter.setValue( _instMichelle.getFilterOT() ) ;
	}

	//
	// Update the filter category
	//
	private void _updateFilterCategory()
	{
		_w.imaging_filterCategory.setValue( _instMichelle.getFilterCategory() ) ;
	}

	//
	// Update the spectroscopy filter
	//
	private void _updateSpecFilter()
	{
		_w.spectroscopy_filter.setValue( _instMichelle.getFilter() ) ;
	}

	//
	// Update the list of available filter categories
	//
	private void _updateFilterCategoryChoices()
	{
		String choices[] = new String[ _instMichelle.getFilterCategoryList().length ] ;
		choices = _instMichelle.getFilterCategoryList() ;
		_w.imaging_filterCategory.setChoices( choices ) ;
	}

	//
	// Update the list of available filters
	//
	private void _updateFilterChoices()
	{
		_w.imaging_filter.setChoices( _instMichelle.getFilterList() ) ;
		_updateFilter() ;
	}

	//
	// Update the list of available gratings/dispersers
	//
	private void _updateGratingChoices()
	{
		_w.spectroscopy_grating.setChoices( _instMichelle.getDisperserList() ) ;
		_w.spectroscopy_grating.setValue( _instMichelle.getDisperser() ) ;
	}

	//
	// Update the list of available masks
	//
	private void _updateMaskChoices()
	{
		_w.spectroscopy_mask.setChoices( _instMichelle.getMaskList() ) ;
		_w.spectroscopy_mask.setValue( _instMichelle.getMask() ) ;
	}

	//
	// Update the list of available samplings
	//
	private void _updateSamplingChoices()
	{
		_w.spectroscopy_sampling.setChoices( SpInstMichelle.SAMPLINGS ) ;
		_w.spectroscopy_sampling.setValue( _instMichelle.getPixelSampling() ) ;
	}

	//
	// Update the chop frequency
	//
	private void _updateChopFreq()
	{
		_w.dataAcq_chopFrequency.setText( _instMichelle.getChopFreqRound() ) ;
	}

	//
	// Update the duty cycle
	//
	private void _updateDutyCycle()
	{
		_w.dataAcq_dutyCycle.setText( _instMichelle.getDutyCycleRound() ) ;
	}

	//
	// Update the acquisition
	//
	private void _updateAcquisition( Object source )
	{
		_instMichelle.setAcquisition() ;
		if( _w.dataAcq_exposureTime != source )
			_w.dataAcq_exposureTime.setText( _instMichelle.getExpTimeString() ) ;

		if( _w.dataAcq_observationTime != source )
			_w.dataAcq_observationTime.setText( _instMichelle.getObservationTimeString() ) ;
	}

	//
	// Update camera
	//
	private void _updateCamera( String camera )
	{
		_instMichelle.setCamera( camera ) ;
		_instMichelle.useDefaultFilterCategory() ;
		_updateFilterCategory() ;
		_updateFilterChoices() ;
		_instMichelle.useDefaultCentralWavelength() ;
		_updateCentralWavelength() ;
		_instMichelle.useDefaultAcquisition() ;
		_showCamera() ;
	}

	//
	// Show camera
	//
	private void _showCamera()
	{

		String camera = _instMichelle.getCamera() ;
		_w.camera.setValue( camera ) ;
		// Make the appropriate imaging or spectroscopy config area visible
		if( camera.equals( "imaging" ) )
			( ( CardLayout )( _w.modePanel.getLayout() ) ).show( _w.modePanel , "imaging" ) ;
		else
			( ( CardLayout )( _w.modePanel.getLayout() ) ).show( _w.modePanel , "spectroscopy" ) ;
	}

	/** Return the position angle text box */
	public TextBoxWidgetExt getPosAngleTextBox()
	{
		return _w.spectroscopy_posAngle ;
	}

	/** Return the exposure time text box */
	public TextBoxWidgetExt getExposureTimeTextBox()
	{
		return _w.dataAcq_exposureTime ;
	}

	/** Return the coadds text box, or null if not available. */
	public TextBoxWidgetExt getCoaddsTextBox()
	{
		// Michelle does not have a coadds text box.
		return new TextBoxWidgetExt() ;
	}
}
