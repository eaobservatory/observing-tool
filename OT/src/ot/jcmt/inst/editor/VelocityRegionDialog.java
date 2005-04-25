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
import edfreq.region.VelocityRegionEditor;
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
 * This class provides a frame for the VelocityRegionEditor.
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
public class VelocityRegionDialog extends JDialog implements ActionListener {

    private JButton okButton = new JButton("OK");
    private JButton cancelButton = new JButton("Cancel");
    private JPanel buttonPanel  = new JPanel();

    private EdDRRecipe _drRecipeEditor;
    private SpDRRecipe _drRecipe;

    protected static FrequencyEditorCfg _cfg = FrequencyEditorCfg.getConfiguration();  

    private VelocityRegionEditor _vre = new VelocityRegionEditor(this);


    public VelocityRegionDialog() {
        setTitle("Baseline Fit Regions (km.s-1)");
        setModal(true);

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        getContentPane().add(_vre, BorderLayout.CENTER);
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

//        System.out.println("Calling setModeAndBand(" + instHeterodyne.getMode() + ", " + instHeterodyne.getBand() + ")");
        _vre.setModeAndBand(instHeterodyne.getMode(), instHeterodyne.getBand());

//         System.out.println("Calling updateDisplay with args");
//         System.out.println("\tmainLineFrequency="+instHeterodyne.getRestFrequency(0));
//         System.out.println("\tIFFreq="+feIF);
//         System.out.println("\tfeBandwidth=" + feBandWidth);
//         System.out.println("\tredshift=" + redshift);
        _vre.updateDisplay(
                instHeterodyne.getRestFrequency(0),
                feIF, feBandWidth,
                redshift);

        for ( int i=0; i<Integer.parseInt(instHeterodyne.getBandMode()); i++ ) {
//             System.out.println("Calling updateBackendValues with arguments:");
//             System.out.println("\tbeIF=" + instHeterodyne.getCentreFrequency(i));
//             System.out.println("\tbeBW=" + instHeterodyne.getBandWidth(i));
//             System.out.println("\tregion=" + i);
            _vre.updateBackendValues(instHeterodyne.getCentreFrequency(i),
                    instHeterodyne.getBandWidth(i),
                    i);
        }

        _vre.removeAllRegions(false);

        double [] min = new double [drRecipe.getNumBaselineRegions()];
        double [] max = new double [drRecipe.getNumBaselineRegions()];
        for(int i = 0; i < drRecipe.getNumBaselineRegions(); i++) {
            // drRecipe.getBaselineRegionMin(i) and drRecipe.getBaselineRegionMin(i) are in km/s.
            // Need to convert this to Hz.  Use the formula f = f0(1+v/c)
            min[i] = (instHeterodyne.getRestFrequency(0) * ( 1 + drRecipe.getBaselineRegionMin(i)/EdFreq.LIGHTSPEED)) / 1.0E9;
            max[i] = (instHeterodyne.getRestFrequency(0) * ( 1 + drRecipe.getBaselineRegionMax(i)/EdFreq.LIGHTSPEED)) / 1.0E9;
        }
        // If there are no Baseline regions, add a default one
        if ( drRecipe.getNumBaselineRegions() == 0 ) {
            _vre.addBaselineFitRegion(true);
        }
        else {
            _vre.addBaselineFitRegions(min, max, lo1Hz/1.0E9, false);
        }

        _vre.resetLayout();

        show();
    }

    public void applyAndHide() {

        apply();
        hide();
    }

    public void apply() {

        _drRecipe.removeAllBaselineRegions();
        _drRecipe.removeAllLineRegions();

        double [][] baselineFitRegions = _vre.getBaselineFitRegions();

        for(int i = 0; i < baselineFitRegions.length; i++) {
            // Return values are in Hz - we need to convert this to velocity
            double min = (baselineFitRegions[i][0] - _vre.getMainLine()) / _vre.getMainLine();
            min *= EdFreq.LIGHTSPEED;
            double max = (baselineFitRegions[i][1] - _vre.getMainLine()) / _vre.getMainLine();
            max *= EdFreq.LIGHTSPEED;
            _drRecipe.setBaselineRegionMin(min, i);
            _drRecipe.setBaselineRegionMax(max, i);
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

