/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.iter.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.iter.SpIterStareObs;
import orac.jcmt.util.ScubaNoise;
import orac.jcmt.util.HeterodyneNoise;

import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;

import gemini.util.MathUtil ;

/**
 * This is the editor for the Stare Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterStareObs extends EdIterJCMTGeneric implements ActionListener , CheckBoxWidgetWatcher 
{

  private IterStareObsGUI _w;       // the GUI layout panel

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterStareObs()
	{
		super( new IterStareObsGUI() );

		_title = "Photometry/Sample";
		_presSource = _w = ( IterStareObsGUI ) super._w;
		_description = "Photometry/Sampling Observation Mode";
		_w.widePhotom.addActionListener( this );
		_w.contModeCB.addWatcher( this ) ;
	}

    protected void _updateWidgets()
	{
		if( _iterObs != null && ( ( SpIterStareObs ) _iterObs ).getWidePhotom() )
		{
			_w.widePhotom.setSelected( true );
		}
		else
		{
			_w.widePhotom.setSelected( false );
		}
		if( SpTreeMan.findInstrument( _iterObs ) instanceof SpInstHeterodyne )
			_w.contModeCB.setSelected( _iterObs.isContinuum() );
		super._updateWidgets();
	}


  public void setInstrument(SpInstObsComp spInstObsComp) {
    if((spInstObsComp != null) && (spInstObsComp instanceof SpInstHeterodyne)) {
      _w.acsisPanel.setVisible(true);
      _w.widePhotom.setVisible(false);
      _w.widePhotom.setSelected(false);
    }
    else {
      _w.acsisPanel.setVisible(false);
      _w.widePhotom.setVisible(true);
    }

    super.setInstrument(spInstObsComp);
  }

  	protected double calculateNoise( int integrations , double wavelength , double nefd , int[] status )
	{
		return ScubaNoise.noise_level( integrations , wavelength , "PHOT" , nefd , status );
	}

    protected double calculateNoise( SpInstHeterodyne inst , double airmass , double tau )
	{
		double tSys = HeterodyneNoise.getTsys( inst.getFrontEnd() , tau , airmass , inst.getRestFrequency( 0 ) / 1.0e9 , inst.getMode().equalsIgnoreCase( "ssb" ) );

		_noiseToolTip = "airmass = " + ( Math.rint( airmass * 10 ) / 10 ) + ", Tsys = " + ( Math.rint( tSys * 10 ) / 10 );
		if( "acsis".equalsIgnoreCase( inst.getBackEnd() ) )
			return MathUtil.round( HeterodyneNoise.getHeterodyneNoise( ( SpIterStareObs ) _iterObs , inst , tau , airmass ) , 3 ) ;
		else
			return -999.9;
	}

    public void actionPerformed (ActionEvent e) {
	((SpIterStareObs)_iterObs).setWidePhotom ( _w.widePhotom.isSelected() );
    }
    
    public void checkBoxAction( CheckBoxWidgetExt cbwe )
	{
		if( cbwe == _w.contModeCB )
		{
			_iterObs.setContinuumMode( _w.contModeCB.isSelected() );
		}
		super.checkBoxAction( cbwe );
	}
}

