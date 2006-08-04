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

import jsky.app.ot.gui.TextBoxWidgetExt;

import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.iter.SpIterRoverObs;
import orac.jcmt.util.ScubaNoise;
import orac.jcmt.util.HeterodyneNoise;
import ot.util.DialogUtil;

import gemini.util.MathUtil ;

/**
 * This is the editor for the Rover Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterRoverObs extends EdIterJCMTGeneric { // implements ActionListener {

  private IterRoverObsGUI _w;       // the GUI layout panel

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterRoverObs() {
    super(new IterRoverObsGUI());

    _title       ="Rover";
    _presSource  = _w = (IterRoverObsGUI)super._w;
    _description ="Rover Observation Mode";
    _w.samplesPerRevolution.addWatcher(this);
  }

    protected void _updateWidgets () {
	super._updateWidgets();

        SpIterRoverObs iterRoverObs = (SpIterRoverObs)_iterObs;

        _w.switchingMode.deleteWatcher(this);
        _w.switchingMode.clear();
        _w.switchingMode.setChoices(iterRoverObs.getSwitchingModeOptions());
        _w.switchingMode.addWatcher(this);

        _w.samplesPerRevolution.deleteWatcher(this);
        _w.samplesPerRevolution.setValue(iterRoverObs.getSamplesPerRevolution());
        _w.samplesPerRevolution.addWatcher(this);

        _w.sampleTime.deleteWatcher(this);
        _w.sampleTime.setValue(iterRoverObs.getRoverSampleTime());
        _w.sampleTime.addWatcher(this);

        _updateSpinRate();

        SpInstObsComp spInstObsComp = SpTreeMan.findInstrument(_spItem);

        if((spInstObsComp == null) || (!(spInstObsComp instanceof SpInstHeterodyne))) {
          DialogUtil.message(_w, "Warning: The Rover iterator can only be used with a heterodyne instrument.");
        }
    }
    

  private void _updateSpinRate() {
    double spinRate = 1.0 / (((SpIterRoverObs)_iterObs).getSamplesPerRevolution() *
                             ((SpIterRoverObs)_iterObs).getRoverSampleTime());

    // Round to 5 decimal points.
    spinRate = Math.rint(spinRate * 1.0E5) / 1.0E5;

    _w.spinRate.setText("" + spinRate);
  }

  public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
    super.textBoxKeyPress(tbwe);

    if(tbwe == _w.sampleTime) {
      ((SpIterRoverObs)_iterObs).setRoverSampleTime(tbwe.getValue());
      _updateSpinRate();

      return;
    }

    if(tbwe == _w.samplesPerRevolution) {
      ((SpIterRoverObs)_iterObs).setSamplesPerRevolution(tbwe.getValue());
      _updateSpinRate();

      return;
    }
  }


  	protected double calculateNoise( int integrations , double wavelength , double nefd , int[] status )
	{
		return ScubaNoise.noise_level( integrations , wavelength , "PHOT" , nefd , status );
	}

	protected double calculateNoise( SpInstHeterodyne inst , double airmass , double tau )
	{
		double tSys = HeterodyneNoise.getTsys( inst.getFrontEnd() , tau , airmass , inst.getRestFrequency( 0 ) / 1.0e9 , inst.getMode().equalsIgnoreCase( "ssb" ) );

		_noiseToolTip = "airmass = " + ( Math.rint( airmass * 10 ) / 10 ) + ", Tsys = " + ( Math.rint( tSys * 10 ) / 10 );
		return MathUtil.round( HeterodyneNoise.getHeterodyneNoise( ( SpIterRoverObs ) _iterObs , inst , tau , airmass ) , 3 ) ;
	}
}

