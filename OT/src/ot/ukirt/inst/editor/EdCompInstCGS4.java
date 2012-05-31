/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package ot.ukirt.inst.editor ;

import orac.util.LookUpTable ;
import orac.ukirt.inst.SpInstCGS4 ;

import jsky.app.ot.gui.CheckBoxWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetWatcher ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetWatcher ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

import gemini.sp.SpItem ;
import gemini.util.MathUtil ;

import java.util.Vector ;

import jsky.app.ot.tpe.TelescopePosEditor ;
import jsky.app.ot.tpe.TpeManager ;

/**
 * This is the editor for the CGS4 instrument Observation Component
 */
public final class EdCompInstCGS4 extends EdCompInstBase
{
	private SpInstCGS4 _instCGS4 ;
	private Cgs4GUI _w ; // the GUI layout

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdCompInstCGS4()
	{
		_title = "CGS4" ;
		_presSource = _w = new Cgs4GUI() ;
		_description = "The CGS4 instrument is configured with this component." ;

		CommandButtonWidgetExt cbw ;
		DropDownListBoxWidgetExt ddlbw ;
		TextBoxWidgetExt tbw ;

		//
		// Disperser
		//
		ddlbw = _w.disperser;
		LookUpTable disps = SpInstCGS4.DISPERSERS ;
		Vector<String> v = new Vector<String>() ;
		for( int i = 0 ; i < disps.getNumRows() ; ++i )
		{
			String res = disps.elementAt( i , 1 ) ;
			if( i == 2 )
				res = "40000" ;
			v.addElement( disps.elementAt( i , 0 ) + " (R\u007e" + res + ")" ) ;
		}
		ddlbw.setChoices( v ) ;

		ddlbw.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instCGS4.setDisperser( _dispTrim( val ) ) ;
				_updateOrder() ;
				_updateFilter() ;
				_updateMaskMenu() ;
				_instCGS4.useDefaultAcquisition() ;
				_updateWidgets() ;
			}
		} ) ;

		//
		// source magnitude
		//
		ddlbw = _w.sourceMag;
		ddlbw.setChoices( SpInstCGS4.SRCMAGS ) ;

		ddlbw.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instCGS4.setSourceMagnitude( val ) ;
				_instCGS4.useDefaultAcquisition() ;
				_updateWidgets() ;
			}
		} ) ;

		//
		// sampling
		//
		ddlbw = _w.sampling;
		ddlbw.setChoices( SpInstCGS4.SAMPLINGS ) ;

		ddlbw.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instCGS4.setSampling( val ) ;
				_updateWidgets() ;
			}
		} ) ;

		//
		// Mask
		//
		ddlbw = _w.mask;
		ddlbw.setChoices( SpInstCGS4.MASKS.getColumn( 0 ) ) ;

		ddlbw.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instCGS4.setMask( val ) ;
				_updateWidgets() ;

				TelescopePosEditor tpe = TpeManager.get( _instCGS4 ) ;
				if( tpe != null )
					tpe.repaint() ;
			}
		} ) ;

		//
		// Polariser
		//
		ddlbw = _w.polariser;
		ddlbw.setChoices( SpInstCGS4.POLARISERS ) ;

		ddlbw.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instCGS4.setPolariser( val ) ;
				_updateWidgets() ;
			}
		} ) ;

		//
		// Acq. Mode
		//
		ddlbw = _w.acqMode;
		ddlbw.setChoices( SpInstCGS4.MODES ) ;

		ddlbw.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instCGS4.setMode( val ) ;
				_updateSecondaryAcqMode() ;
				_updateWidgets() ;
			}
		} ) ;

		//
		// Central Wavelength
		//
		tbw = _w.centralWavelength;
		tbw.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				_instCGS4.setCentralWavelength( tbw.getText() ) ;
				_updateWavelengthCoverage() ;
				_updateResolution() ;
				_updateOrder() ;
				_updateFilter() ;
				_instCGS4.useDefaultAcquisition() ;
				_updateExpInfo() ;
				_updateSecondaryAcqMode() ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){} // ignore
		} ) ;

		//
		// CVF offset
		//
		tbw = _w.cvfOffset;
		tbw.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				_instCGS4.setCvfOffset( tbw.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){} // ignore
		} ) ;

		//
		// Order
		//
		tbw = _w.order;
		tbw.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				_instCGS4.setOrder( tbw.getText() ) ;
				_updateWavelengthCoverage() ;
				_updateResolution() ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){} // ignore
		} ) ;

		cbw = _w.defaultOrder;
		cbw.addWatcher( new CommandButtonWidgetWatcher()
		{
			public void commandButtonAction( CommandButtonWidgetExt cbw )
			{
				_instCGS4.useDefaultOrder() ;
				_updateWidgets() ;
			}
		} ) ;

		CheckBoxWidgetExt cbwe ;
		cbwe = _w.useND;
		cbwe.addWatcher( new CheckBoxWidgetWatcher()
		{
			public void checkBoxAction( CheckBoxWidgetExt cbw )
			{
				_instCGS4.setNdFilter( cbw.getBooleanValue() ) ;
				_updateWidgets() ;
			}
		} ) ;

		tbw = _w.exposureTime;
		tbw.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				_instCGS4.setExpTime( tbw.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){}
		} ) ;

		cbw = _w.defaultAcquisition;
		cbw.addWatcher( new CommandButtonWidgetWatcher()
		{
			public void commandButtonAction( CommandButtonWidgetExt cbw )
			{
				_instCGS4.useDefaultAcquisition() ;
				_instCGS4.setCvfOffset( _instCGS4.getCentralWavelength() ) ;
				_updateWidgets() ;
			}
		} ) ;

		_w.coadds.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				_instCGS4.setNoCoadds( tbw.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){}
		} ) ;

	}

	//
	// Trim a disperser selection
	//
	private String _dispTrim( String disperser )
	{
		return disperser.substring( 0 , disperser.lastIndexOf( '(' ) ).trim() ;
	}

	//
	// Add the resolution to a disperser selection
	//
	private String _fullDispName( String disperser )
	{
		LookUpTable d = SpInstCGS4.DISPERSERS ;
		try
		{
			int i = d.indexInColumn( disperser , 0 ) ;
			String res = d.elementAt( i , 1 ) ;
			if( i == 2 )
				res = "40000" ;
			return( d.elementAt( i , 0 ) + " (R\u007e" + res + ")" ) ;
		}
		catch( Exception ex )
		{
			return null ;
		}
	}

	/**
	 * Override setup to store away a reference to the SpInstCGS4 item.
	 */
	public void setup( SpItem spItem )
	{
		_instCGS4 = ( SpInstCGS4 )spItem ;
		super.setup( spItem ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		DropDownListBoxWidgetExt ddlbw ;
		CheckBoxWidgetExt cbwe ;

		ddlbw = _w.acqMode;
		ddlbw.setValue( _instCGS4.getMode() ) ;

		ddlbw = _w.disperser;
		ddlbw.setValue( _fullDispName( _instCGS4.getDisperser() ) ) ;

		ddlbw = _w.sourceMag;
		ddlbw.setValue( _instCGS4.getSourceMagnitude() ) ;

		ddlbw = _w.mask;
		ddlbw.setValue( _instCGS4.getMask() ) ;

		ddlbw = _w.polariser;
		ddlbw.setValue( _instCGS4.getPolariser() ) ;

		ddlbw = _w.sampling;
		ddlbw.setValue( _instCGS4.getSampling() ) ;

		double centralWavelength = _instCGS4.getCentralWavelength() ;
		_w.centralWavelength.setText( Double.toString( centralWavelength ) ) ;

		int order = _instCGS4.getOrder() ;
		_w.order.setText( Integer.toString( order ) ) ;

		_w.filter.setText( _instCGS4.getFilter() ) ;

		cbwe = _w.useND;
		cbwe.setValue( _instCGS4.getNdFilter() ) ;

		_w.cvfOffset.setValue( _instCGS4.getCvfOffset() ) ;

		_updateScienceFOV() ;
		_updateWavelengthCoverage() ;
		_updateResolution() ;

		super._updateWidgets() ;

		_updateExpInfo() ;
		_updateSecondaryAcqMode() ;
	}

	//
	// Update the science field of view based upon the camera and mask
	// settings.
	//
	private void _updateScienceFOV()
	{
		TextBoxWidgetExt tbw = _w.scienceFOV;
		double[] scienceArea = _instCGS4.getScienceArea() ;

		double w = MathUtil.round( scienceArea[ 0 ] , 2 ) ;
		double h = MathUtil.round( scienceArea[ 1 ] , 2 ) ;

		tbw.setText( w + " x " + h ) ;
	}

	//
	// Update the order based upon the wavelength and disperser
	//
	private void _updateOrder()
	{
		TextBoxWidgetExt tbw = _w.order;
		int o = _instCGS4.getDefaultOrder() ;
		_instCGS4.setOrder( o ) ;
		tbw.setText( Integer.toString( o ) ) ;
	}

	//
	// Update the menu of masks.
	//
	private void _updateMaskMenu()
	{
		DropDownListBoxWidgetExt ddlbw ;
		Vector<String> menu = _instCGS4.getMaskMenu() ;
		ddlbw = _w.mask;
		ddlbw.setChoices( menu ) ;
	}

	//
	// Update the filter
	//
	private void _updateFilter()
	{
		// The FreeBongo OT (OT2) seems to have an invisible filter text box widget which is never really used but nevertheless updated (?)
		// This Swing OT (OT3) does not have such a  filter text box widget. 
		// But _instCGS4 has a filter attribute that has to be set to a default filter when _updateFilter is called.

		String filter = _instCGS4.getDefaultFilter() ;
		_instCGS4.setFilter( filter ) ;
	}

	//
	// Update the exposure time and coadds
	//
	private void _updateExpInfo()
	{
		TextBoxWidgetExt tbw = _w.exposureTime;
		double d = _instCGS4.getExpTime() ;
		String e = Double.toString( d ) ;
		_instCGS4.setExpTime( e ) ;
		tbw.setText( e ) ;

		tbw = _w.coadds ;
		int coadds = _instCGS4.getNoCoadds() ;
		_instCGS4.setNoCoadds( coadds ) ;
		tbw.setText( Integer.toString( coadds ) ) ;
	}

	//
	// Update the secondary acqmode items based upon the acq Mode.
	// MFO: CHOP mode not supported anymore.
	//
	private void _updateSecondaryAcqMode(){}

	//
	// Update the wavelength coverage based upon the grating and central
	// wavelength.
	//
	private void _updateWavelengthCoverage()
	{
		TextBoxWidgetExt tbw ;
		tbw = _w.wavelengthCoverage;

		double coverage[] = _instCGS4.getWavelengthCoverage() ;
		//  If resolution > 10000  (arbitrary) display 4 dec. pl., otherwise 2.
		int dp = 2 ;
		if( _instCGS4.getResolution() > 10000 )
			dp = 4 ;
		double ll = MathUtil.round( coverage[ 0 ] , dp ) ;
		double ul = MathUtil.round( coverage[ 1 ] , dp ) ;
		tbw.setText( ll + "-" + ul ) ;
	}

	//
	// Update the resolution
	//
	private void _updateResolution()
	{
		TextBoxWidgetExt tbw ;
		tbw = _w.resolution;

		int res = ( new Double( _instCGS4.getResolution() ) ).intValue() ;
		tbw.setText( Integer.toString( res ) ) ;
	}

	public TextBoxWidgetExt getCoaddsTextBox()
	{
		return new TextBoxWidgetExt() ;
	}

	public TextBoxWidgetExt getExposureTimeTextBox()
	{
		return new TextBoxWidgetExt() ;
	}

	public TextBoxWidgetExt getPosAngleTextBox()
	{
		return _w.posAngle ;
	}
}
