/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2003                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.inst.editor;

import orac.jcmt.inst.SpDRRecipe;
import orac.jcmt.inst.SpInstHeterodyne;
import edfreq.*;
import edfreq.region.SpectralRegionEditor;
import ot.util.DialogUtil;

import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTreeMan;


import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;



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
public class SpectralRegionDialog extends JDialog implements ActionListener {

  private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Cancel");
  private JPanel buttonPanel  = new JPanel();

  private EdDRRecipe _drRecipeEditor;
  private SpDRRecipe _drRecipe;

  protected static FrequencyEditorCfg _cfg = FrequencyEditorCfg.getConfiguration();  

  private SpectralRegionEditor _spectralRegionEditor = new SpectralRegionEditor(this);


  public SpectralRegionDialog() {
    setTitle("Baseline Fit and Line Regions");
    setModal(true);

    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);

    getContentPane().add(_spectralRegionEditor, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    pack();
    setLocation(100, 100);

    setDefaultCloseOperation(HIDE_ON_CLOSE);

    okButton.addActionListener(this);
    cancelButton.addActionListener(this);
  }

  public void show(SpDRRecipe drRecipe, SpInstHeterodyne instHeterodyne, EdDRRecipe drRecipeEditor) {

    if(instHeterodyne.getBand() == null) {
      DialogUtil.error(this, "Heterodyne component has not been edited.");
      return;
    }

    _drRecipe       = drRecipe;
    _drRecipeEditor = drRecipeEditor;

    double redshift;
    SpTelescopeObsComp tgt = SpTreeMan.findTargetList(instHeterodyne);
    if ( tgt != null ) {
	SpTelescopePos tp = (SpTelescopePos)tgt.getPosList().getBasePosition();
	redshift = tp.getRedshift();
    }
    else {
	redshift = 0.0;
    }

    double feIF          = instHeterodyne.getFeIF();
    double feBandWidth   = instHeterodyne.getFeBandWidth();
    double restFrequency = instHeterodyne.getRestFrequency(0);
    double obsFrequency  = EdFreq.getObsFrequency(instHeterodyne.getRestFrequency(0),
                                                      redshift);
    double lo1Hz         = EdFreq.getLO1(obsFrequency,
                                         instHeterodyne.getCentreFrequency(0),
                                         instHeterodyne.getBand());

    _spectralRegionEditor.setModeAndBand(instHeterodyne.getMode(), instHeterodyne.getBand());

    Receiver r = (Receiver)_cfg.receivers.get(instHeterodyne.getFrontEnd());

    _spectralRegionEditor.updateLineDisplay(lo1Hz - (feIF + (0.5 * feBandWidth)),
                                            lo1Hz + (feIF + (0.5 * feBandWidth)),
                                            feIF, feBandWidth,
                                            redshift);

    for ( int i=0; i<Integer.parseInt(instHeterodyne.getBandMode()); i++ ) {
        _spectralRegionEditor.updateBackendValues(instHeterodyne.getCentreFrequency(i),
                                                  instHeterodyne.getBandWidth(i),
						  i);
    }

    _spectralRegionEditor.setMainLine(instHeterodyne.getRestFrequency(0));

    _spectralRegionEditor.removeAllRegions(false);

    double [] min = new double [drRecipe.getNumBaselineRegions()];
    double [] max = new double [drRecipe.getNumBaselineRegions()];
    for(int i = 0; i < drRecipe.getNumBaselineRegions(); i++) {
      // lo1Hz is in Hz.
      // drRecipe.getBaselineRegionMin(i) and drRecipe.getBaselineRegionMin(i) are in MHz.
      min[i] = drRecipe.getBaselineRegionMin(i) / 1.0E3;
      max[i] = drRecipe.getBaselineRegionMax(i) / 1.0E3;
    }
    // If there are no Baseline regions, add a default one
    if ( drRecipe.getNumBaselineRegions() == 0 ) {
	_spectralRegionEditor.addBaselineFitRegion(true);
    }
    else {
	_spectralRegionEditor.addBaselineFitRegions(min, max, lo1Hz / 1.0E9, false);
    }

    for(int i = 0; i < drRecipe.getNumLineRegions(); i++) {
      // lo1Hz is in Hz.
      // drRecipe.getBaselineRegionMin(i) and drRecipe.getBaselineRegionMin(i) are in MHz.
      _spectralRegionEditor.addLineRegion(drRecipe.getLineRegionMin(i) / 1.0E3,
                                          drRecipe.getLineRegionMax(i) / 1.0E3,
                                          lo1Hz / 1.0E9, false);
    }

    _spectralRegionEditor.createCombinedRegions(false);

    _spectralRegionEditor.resetLayout();

    show();
  }

  public void applyAndHide() {

    apply();
    hide();
  }

  public void apply() {
      /*
      if ( !(_spectralRegionEditor.changedBaseline()) ) {
	  return;
      }
      */

      _drRecipe.removeAllBaselineRegions();
      _drRecipe.removeAllLineRegions();
      
      double [][] baselineFitRegions = _spectralRegionEditor.getBaselineFitRegions();
      double [][] lineRegions        = _spectralRegionEditor.getLineRegions();

      for(int i = 0; i < baselineFitRegions.length; i++) {
	  _drRecipe.setBaselineRegionMin(baselineFitRegions[i][0] / 1.0E6, i);
	  _drRecipe.setBaselineRegionMax(baselineFitRegions[i][1] / 1.0E6, i);
      }

      for(int i = 0; i < lineRegions.length; i++) {
	  _drRecipe.setLineRegionMin(lineRegions[i][0] / 1.0E6, i);
	  _drRecipe.setLineRegionMax(lineRegions[i][1] / 1.0E6, i);
      }
      
      _drRecipeEditor.refresh();
  }

  public void cancel() {
    hide();
  }

  public void update(Graphics g) {
    super.update(g);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == okButton) {
      applyAndHide();
    }

    if(e.getSource() == cancelButton) {
      hide();
    }
  }
}

