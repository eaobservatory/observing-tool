/*
 * Copyright 2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package orac.jcmt.validation;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import gemini.sp.SpItem;
import gemini.sp.SpObsContextItem;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTelescopePosList;
import gemini.sp.SpTreeMan;
import gemini.sp.SpObs;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.inst.SpInstSCUBA2;
import orac.jcmt.iter.SpIterJCMTObs;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.obsComp.SpSchedConstObsComp;
import gemini.sp.iter.SpIterChop;
import gemini.util.Format;
import gemini.util.CoordSys;
import gemini.util.DDMMSS;
import gemini.util.HHMMSS;
import gemini.util.RADec;
import gemini.util.RADecMath;
import gemini.util.TelescopePos;
import orac.jcmt.SpJCMTConstants;
import orac.jcmt.inst.SpDRRecipe;
import orac.util.CoordConvert;
import orac.validation.SpValidation;
import orac.validation.ErrorMessage;

import orac.jcmt.iter.SpIterJiggleObs;
import orac.jcmt.iter.SpIterNoiseObs;
import orac.jcmt.iter.SpIterRasterObs;
import orac.jcmt.iter.SpIterPOL;
import ot.jcmt.iter.editor.EdIterRasterObs;

import edfreq.BandSpec;
import edfreq.FrequencyEditorCfg;
import edfreq.Receiver;

/**
 * Validation Tool for JCMT.
 *
 * This class is used for checking whether the values and settings in a
 * Science Program or Observation are sensible.
 *
 * Errors and warnings are issued otherwise.
 *
 * @author M.Folger@roe.ac.uk (M.Folger@roe.ac.uk)
 */
public class JcmtSpValidation extends SpValidation {
    public void checkObservation(SpObs spObs, Vector<ErrorMessage> report) {
        if (report == null) {
            report = new Vector<ErrorMessage>();
        }

        String titleString = titleString(spObs);

        SpInstObsComp obsComp = SpTreeMan.findInstrument(spObs);
        SpTelescopeObsComp target = SpTreeMan.findTargetList(spObs);
        Vector<SpItem> observes = SpTreeMan.findAllInstances(spObs,
                SpIterJCMTObs.class.getName());

        // Check we don't have proper motions which the JCMT translator
        // does not support.
        if (target != null) {
            SpTelescopePos pos = target.getPosList().getBasePosition();
            if (pos.getSystemType() == SpTelescopePos.SYSTEM_SPHERICAL) {
                int coordSystem = pos.getCoordSys();
                if ((coordSystem != CoordSys.FK4) && (coordSystem != CoordSys.FK5)) {
                    double pmRa = Format.toDouble(pos.getPropMotionRA());
                    double pmDec = Format.toDouble(pos.getPropMotionDec());
                    if ((pmRa != 0.0) || (pmDec != 0.0)) {
                        report.add(new ErrorMessage(
                                ErrorMessage.WARNING,
                                titleString,
                                "Proper motion values are specified for the"
                                + " target coordinates, but proper motion is"
                                + " not supported for coordinates of this type."));

                    }
                }
            }
        }

        for (int count = 0; count < observes.size(); count++) {
            SpIterJCMTObs thisObs = (SpIterJCMTObs) observes.elementAt(count);

            if (obsComp != null && obsComp instanceof SpInstHeterodyne) {
                SpInstHeterodyne spInstHeterodyne = (SpInstHeterodyne) obsComp;
                String feName = spInstHeterodyne.getFrontEnd();

                /* Check for retired receivers which are still present to allow
                   programmes to be loaded successfully. */
                if (feName != null) {
                    if (feName.equals("A3")) {
                        report.add(new ErrorMessage(
                                ErrorMessage.WARNING,
                                titleString,
                                "Receiver RxA3 was removed for upgrade in"
                                + " November 2015.  Please update your program"
                                + " to use Uu and check your observing"
                                + " frequencies carefully."));
                    }
                    else if (feName.equals("A3m")) {
                        report.add(new ErrorMessage(
                                ErrorMessage.WARNING,
                                titleString,
                                "Receiver RxA3m was decommissioned in"
                                + " June 2018.  Please update your program"
                                + " to use Uu and check your observing"
                                + " frequencies carefully."));

                    }
                    else if (feName.equals("WB")) {
                        report.add(new ErrorMessage(
                                ErrorMessage.WARNING,
                                titleString,
                                "Receiver RxWB was removed from service in"
                                + " April 2014.  Please update your program"
                                + " to use HARP or Aweoweo and check your observing"
                                + " frequencies carefully."));

                    }
                    else if (feName.equals("WD")) {
                        report.add(new ErrorMessage(
                                ErrorMessage.WARNING,
                                titleString,
                                "Receiver RxWD is no longer available."));
                    }
                }

                double loMin = 0.0;
                double loMax = 0.0;
                double[] defaultOverlaps = null;
                double[] bandwidths = null;
                int systems = new Integer(spInstHeterodyne.getBandMode());

                FrequencyEditorCfg requencyEditorCfg =
                        FrequencyEditorCfg.getConfiguration();

                Receiver receiver = requencyEditorCfg.receivers.get(feName);

                loMin = receiver.loMin;
                loMax = receiver.loMax;

                BandSpec bandSpec = receiver.bandspecs.get(systems - 1);

                defaultOverlaps = bandSpec.defaultOverlaps;

                bandwidths = bandSpec.getDefaultOverlapBandWidths();

                // Use a newly-calculated sky frequency (but don't store it to
                // avoid unnecessary changes to the science program) in order
                // to take into account any subsequent changes to velocities
                // in associated target components.
                double skyFrequency = spInstHeterodyne.calculateSkyFrequency();
                if (loMin != 0.0
                        && (loMin - spInstHeterodyne.getFeIF())
                                > skyFrequency) {
                    report.add(new ErrorMessage(
                            ErrorMessage.WARNING, titleString,
                            "Sky frequency of "
                            + String.format("%.3f", skyFrequency / 1.0E9)
                            + " GHz is lower than receiver minimum "
                            + String.format("%.3f", (loMin - spInstHeterodyne.getFeIF()) / 1.0E9)
                            + " GHz"));
                }

                if (loMax != 0.0
                        && (loMax + spInstHeterodyne.getFeIF())
                                < skyFrequency) {
                    report.add(new ErrorMessage(
                            ErrorMessage.WARNING, titleString,
                            "Sky frequency of "
                            + String.format("%.3f", skyFrequency / 1.0E9)
                            + " GHz is greater than receiver maximum "
                            + String.format("%.3f", (loMax  + spInstHeterodyne.getFeIF())/ 1.0E9)
                            + " GHz"));
                }

                int available = new Integer(spInstHeterodyne.getBandMode());
                String sideBand = spInstHeterodyne.getBand();
                String sidebandMode = spInstHeterodyne.getMode();

                List<String> availableModes = Arrays.asList(
                        requencyEditorCfg.frontEndTable.get(feName));

                if (! availableModes.contains(sidebandMode)) {
                    report.add(new ErrorMessage(
                            ErrorMessage.ERROR, titleString,
                            "Sideband mode " + sidebandMode
                            + " is not suitable for this receiver."));
                }

                double loFreq;
                if ("lsb".equals(sideBand)) {
                    loFreq = skyFrequency + spInstHeterodyne.getCentreFrequency(0);
                } else {
                    loFreq = skyFrequency - spInstHeterodyne.getCentreFrequency(0);
                }

                for (int index = 0; index < available; index++) {
                    double centre = spInstHeterodyne.getCentreFrequency(index);
                    double rest = spInstHeterodyne.getRestFrequency(index);
                    double skyFreq = spInstHeterodyne.calculateSkyFrequency(index);

                    if ("lsb".equals(sideBand) && (skyFreq + centre) > loMax) {
                        report.add(new ErrorMessage(ErrorMessage.WARNING,
                                titleString,
                                "Need to use upper or best sideband"
                                + " to reach the line " + rest
                                + " (at sky frequency " + skyFreq + ")"));

                    } else if (!"lsb".equals(sideBand)
                            && (skyFreq - centre) < loMin) {
                        report.add(new ErrorMessage(ErrorMessage.WARNING,
                                titleString,
                                "Need to use lower sideband"
                                + " to reach the line " + rest
                                + " (at sky frequency " + skyFreq + ")"));
                    }

                    if ("best".equals(sideBand)
                            && (index > 0)
                            && (skyFreq < loFreq)) {
                        report.add(new ErrorMessage(ErrorMessage.WARNING,
                                titleString,
                                "Configuration uses 'best' to determine"
                                + " sideband automatically, but subsystem "
                                + (index + 1) + " at " + rest
                                + " (sky frequency " + skyFreq + ")"
                                + " appears to need to be in LSB, so the"
                                + " observation sideband can not be changed."
                                + " Please select USB for this configuration."));
                    }

                    if ("ssb".equals(sidebandMode) && ("lsb".equals(sideBand)
                            ^ (skyFreq < loFreq))) {
                        report.add(new ErrorMessage(ErrorMessage.WARNING,
                                titleString,
                                "For single sideband (SSB) mode, all subsystems"
                                + " should be in the same sideband, but subsystem "
                                + (index + 1) +" at " + rest
                                + " (sky frequency " + skyFreq
                                + ") appears not to be in the selected sideband ("
                                + sideBand + ")."));
                    }
                }

                if (defaultOverlaps != null && bandwidths != null) {
                    for (int system = 0; system < systems; system++) {
                        double overLap = spInstHeterodyne.getOverlap(system);
                        double bandwidth =
                                spInstHeterodyne.getBandWidth(system);

                        for (int index = 0; index < bandwidths.length;
                                index++) {
                            if (bandwidth == bandwidths[index]) {
                                double defaultOverlap = defaultOverlaps[index];

                                if (overLap != defaultOverlap) {
                                    report.add(new ErrorMessage(
                                            ErrorMessage.ERROR, titleString,
                                            "Overlap invalid for system "
                                                    + (system + 1)
                                                    + ", should be "
                                                    + defaultOverlap
                                                    + " and not " + overLap
                                                    + "."));
                                }

                                break;
                            }
                        }
                    }
                }

                if (thisObs instanceof SpIterJiggleObs) {
                    SpIterJiggleObs spIterJiggleObs = (SpIterJiggleObs) thisObs;
                    String jigglePattern = spIterJiggleObs.getJigglePattern();

                    if (feName == null) {
                        report.add(new ErrorMessage(ErrorMessage.ERROR,
                                titleString, "Front end is null"));

                    } else if (jigglePattern == null) {
                        report.add(new ErrorMessage(ErrorMessage.ERROR,
                                titleString, "Jiggle pattern not initialised"));

                    } else if (jigglePattern.startsWith("HARP")
                            && !feName.startsWith("HARP")) {
                        report.add(new ErrorMessage(
                                ErrorMessage.ERROR,
                                titleString,
                                "Cannot use " + jigglePattern
                                + " jiggle pattern without HARP-B frontend"));
                    }
                }

                if (thisObs instanceof SpIterNoiseObs) {
                    report.add(new ErrorMessage(ErrorMessage.ERROR,
                            spObs.getTitle(),
                            "Cannot use Noise observations with Hetrodyne"));
                }

                if (thisObs instanceof SpIterRasterObs) {
                    SpIterRasterObs spIterRasterObs = (SpIterRasterObs) thisObs;

                    // Do not allow rasters with ACSIS to have a sample time
                    // in excess of 10 seconds.  (See fault 20150410.005.)
                    if (thisObs.getSampleTime() > 10.0) {
                        report.add(new ErrorMessage(ErrorMessage.ERROR,
                            spObs.getTitle(),
                            "Sample time exceeds 10 seconds"));
                    }

                    // Check for non-square pixels if the reciever is not
                    // HARP.  (See fault 20200128.001.)
                    try {
                        double dx = spIterRasterObs.getScanDx();
                        double dy = spIterRasterObs.getScanDy();

                        if (feName == null) {
                            report.add(new ErrorMessage(ErrorMessage.ERROR,
                                    titleString, "Front end is null"));
                        }
                        else if (! feName.startsWith("HARP")) {
                            if (Math.abs(dx - dy) > 0.0001) {
                                report.add(new ErrorMessage(
                                        ErrorMessage.WARNING,
                                        titleString,
                                        "Raster map has non-square pixels (dx = " +
                                            dx + "\", dy = " + dy + "\")."));
                            }
                        }
                        else {
                            if (Math.abs(dx - SpIterRasterObs.HARP_SAMPLE) > 0.0001) {
                                report.add(new ErrorMessage(
                                        ErrorMessage.WARNING,
                                        titleString,
                                        "Raster map has unusual sample spacing " +
                                            "for HARP (dx = " + dx +  "\")."));
                            }

                            boolean found = false;
                            for (double rasterValue : EdIterRasterObs.HARP_RASTER_VALUES) {
                                if (Math.abs(dy - rasterValue) < 0.0001) {
                                    found = true;
                                }
                            }
                            if (! found) {
                                report.add(new ErrorMessage(
                                        ErrorMessage.WARNING,
                                        titleString,
                                        "Raster map has unusual scan spacing " +
                                            "for HARP (dy = " + dy +  "\")."));
                            }
                        }
                    }
                    catch (UnsupportedOperationException e) {
                        report.add(new ErrorMessage(ErrorMessage.ERROR,
                                titleString, "Unable to check pixel size."));
                    }
                }

                // Warn if the velocity/redshift has been left at zero.
                double velocity = 0.0;
                String velocitySource = null;
                if (spInstHeterodyne.hasVelocityInformation()) {
                    velocity = spInstHeterodyne.getVelocity();
                    velocitySource = "Het Setup";
                } else if (target != null) {
                    velocity = Double.parseDouble(
                        target.getPosList().getBasePosition().getTrackingRadialVelocity());
                    velocitySource = "Target Information";
                }

                if ((velocity == 0.0) && (velocitySource != null)) {
                    report.add(new ErrorMessage(ErrorMessage.WARNING,
                            titleString,
                            "The radial velocity / redshift (specified in the "
                            + velocitySource
                            + " component) is zero."
                            + " Please check if this is really appropriate"
                            + " for your source and adjust if not."));
                }

                for (int system = 0; system < systems; system ++) {
                    for (int prev_system = 0; prev_system < system; prev_system ++) {
                        // Perform same comparison as translator (not sure why
                        // it compares overlap when channels and bandwidth are included).
                        if ((spInstHeterodyne.getCentreFrequency(system)
                                    == spInstHeterodyne.getCentreFrequency(prev_system)) &&
                                (spInstHeterodyne.getOverlap(system)
                                    == spInstHeterodyne.getOverlap(prev_system)) &&
                                (spInstHeterodyne.getChannels(system)
                                    == spInstHeterodyne.getChannels(prev_system)) &&
                                (spInstHeterodyne.getBandWidth(system)
                                    == spInstHeterodyne.getBandWidth(prev_system))) {
                            report.add(new ErrorMessage(ErrorMessage.ERROR,
                                titleString,
                                "Subsystems " + (prev_system + 1)
                                + " and " + (system + 1)
                                + " appear to be identical."));
                            }
                    }
                }

            } else if (obsComp != null && obsComp instanceof SpInstSCUBA2) {
                // Check for POL-2 observation with inappropriate mode or
                // scan pattern.
                SpItem pol_iter = SpTreeMan.findParentOfType(
                    thisObs, SpIterPOL.class);

                if (pol_iter != null) {
                    if (thisObs instanceof SpIterRasterObs) {
                        if (! "Point Source".equals(
                                ((SpIterRasterObs) thisObs).getScanStrategy())) {
                            report.add(new ErrorMessage(ErrorMessage.ERROR,
                                    titleString,
                                    "POL-2 scan observations should use " +
                                    "the \"Point Source\" scan strategy."));
                        }
                    } else {
                        report.add(new ErrorMessage(ErrorMessage.WARNING,
                                titleString,
                                "POL-2 should normally be used in " +
                                "scan observations."));
                    }
                }
            }

            // Also check the switching mode.  If we are in beam switch,
            // we need a chop iterator, in position or freq we need
            // a reference in the target
            String switchingMode = thisObs.getSwitchingMode();

            if (switchingMode != null) {
                if (switchingMode.equals(SpJCMTConstants.SWITCHING_MODE_BEAM)) {
                    if (SpTreeMan.findParentOfType(
                            thisObs, SpIterChop.class) == null) {
                        report.add(new ErrorMessage(ErrorMessage.ERROR,
                                titleString,
                                "Chop iterator required for beam switch mode"));
                    }
                } else if (switchingMode.equals(
                                SpJCMTConstants.SWITCHING_MODE_POSITION)) {
                    if (target != null) {
                        SpTelescopePos refPos = (SpTelescopePos)
                            target.getPosList().getPosition("REFERENCE");
                        if (refPos == null) {
                            report.add(new ErrorMessage(ErrorMessage.ERROR,
                                    titleString,
                                    "Position-switched observation"
                                    + " requires a REFERENCE target."));
                        } else {
                            double offRA = 0.0;
                            double offDec = 0.0;

                            try {
                                if (refPos.isOffsetPosition()) {
                                    offRA = refPos.getXaxis();
                                    offDec = refPos.getYaxis();
                                } else {
                                    SpTelescopePos basePos = target.getPosList().getBasePosition();
                                    RADec refConverted = CoordConvert.convert(
                                        refPos.getXaxis(), refPos.getYaxis(),
                                        refPos.getCoordSys(), basePos.getCoordSys());

                                    double baseRA = basePos.getXaxis();
                                    double baseDec = basePos.getYaxis();
                                    double[] offsets = RADecMath.getOffset(
                                        refConverted.ra, refConverted.dec,
                                        baseRA, baseDec);

                                    offRA = offsets[0] * Math.cos(Math.toRadians(baseDec));
                                    offDec = offsets[1];
                                }

                                double offDistance = Math.sqrt(offRA * offRA + offDec * offDec);

                                if (offDistance < 120.0) {
                                    report.add(new ErrorMessage(
                                        ErrorMessage.WARNING,
                                        titleString,
                                        "Position-switched observation REFERENCE"
                                        + " is within 120 arcseconds from target."));
                                }
                                else if (offDistance > 3600.0) {
                                    report.add(new ErrorMessage(
                                        ErrorMessage.WARNING,
                                        titleString,
                                        "Position-switched observation REFERENCE"
                                        + " is more than one degree from target "
                                        + String.format("(%.3f degrees).", offDistance / 3600.0)));
                                }
                            } catch (UnsupportedOperationException uoe) {
                                // Can be thrown by CoordConvert.convert.
                                report.add(new ErrorMessage(
                                    ErrorMessage.WARNING,
                                    titleString,
                                    "Could not check REFERENCE distance for"
                                    + " this coordinate type."));
                            }
                        }
                    }

                } else if (switchingMode.equals(
                                SpJCMTConstants.SWITCHING_MODE_FREQUENCY_F)) {
                    if (target != null
                            && !(target.getPosList().exists("REFERENCE"))) {
                        report.add(new ErrorMessage(ErrorMessage.ERROR,
                                titleString,
                                "Frequency-switched observation"
                                + " requires a REFERENCE target."));
                    }

                    double frequencyThrow = thisObs.getFrequencyOffsetThrow();

                    if (frequencyThrow == 0.0) {
                        report.add(new ErrorMessage(ErrorMessage.ERROR,
                                titleString,
                                "Frequency-switched observation has a"
                                + " frequency throw of 0.0 MHz."));
                    } else if (frequencyThrow < 8.0) {
                        report.add(new ErrorMessage(ErrorMessage.WARNING,
                                titleString,
                                "Frequency-switched observation has a"
                                + " frequency throw of less than 8 MHz."));
                    } else if (frequencyThrow > 32.0) {
                        report.add(new ErrorMessage(ErrorMessage.WARNING,
                                titleString,
                                "Frequency-switched observation has a"
                                + " frequency throw of greater than 32 MHz."));
                    }
                } else if (switchingMode.equals(
                                SpJCMTConstants.SWITCHING_MODE_FREQUENCY_S)) {
                    report.add(new ErrorMessage(
                        ErrorMessage.ERROR,
                        titleString,
                        "Slow frequency switching should no longer be used."));
                }
            } else if (obsComp != null && !(obsComp instanceof SpInstSCUBA2)) {
                report.add(new ErrorMessage(ErrorMessage.ERROR, titleString,
                        "No switching mode set"));
            }

            // Check whether the observation a DR recipe
            // (as its own child OR in its context).
            SpDRRecipe recipe = findRecipe(spObs);

            if (recipe == null
                    && (obsComp != null
                            && !(obsComp instanceof SpInstSCUBA2))) {
                report.add(new ErrorMessage(ErrorMessage.WARNING, titleString,
                        "No DR-recipe component."));

            } else if (recipe != null) {
                checkDRRecipe(recipe, report, titleString, thisObs, obsComp);
            }

            // Check whether the observe specifies rotator angles.  If so
            // see if there's a scheduling constraint.
            double[] angle = thisObs.getRotatorAngles();
            if ((angle != null) && (angle.length > 0)) {
                SpSchedConstObsComp schedConst
                    = (SpSchedConstObsComp) SpTreeMan.findSpItemOfType(
                        thisObs, SpSchedConstObsComp.class);

                if (schedConst == null) {
                    report.add(new ErrorMessage(
                        ErrorMessage.ERROR, titleString,
                        "Observation has a rotator angle restriction but no"
                        + " scheduling constraint.  Please add a constraint"
                        + " to indicate at which elevations it is possible"
                        + " to perform this observation."));
                } else {
                    report.add(new ErrorMessage(
                        ErrorMessage.WARNING, titleString,
                        "Restricting the rotator angle is an experimental"
                        + " feature.  Please check that you really want to"
                        + " apply this restriction and ensure that your"
                        + " scheduling constraint indicates at which"
                        + " elevations it is possible to perform this"
                        + " observation.  Otherwise consider removing"
                        + " the restriction (by unselecting all angles)."));
                }
            }
        }

        // Check POL-2 iterators.
        for (SpItem item : SpTreeMan.findAllInstances(spObs,
                SpIterPOL.class.getName())) {
            SpIterPOL pol = (SpIterPOL) item;

            // Currently SpIterPOL returns 1 step count for continuous spin
            // mode, but testing it explicitly here anyway in case that
            // behaviour changes.
            if (!(pol.hasContinuousSpin() || pol.getConfigStepCount() != 0)) {
                report.add(new ErrorMessage(
                        ErrorMessage.ERROR,
                        titleString,
                        "POL iterator is not continuous spin but does"
                        + " not have waveplate angles selected."));
            }
        }

        super.checkObservation(spObs, report);
    }

    /*
     * Check the DR Recipe for validity.
     *
     * Takes the observation's instrument as a parameter so that we can check
     * the recipes are correct for that instrument.
     */
    public void checkDRRecipe(SpDRRecipe recipe, Vector<ErrorMessage> report,
            String obsTitle, SpIterJCMTObs thisObs, SpInstObsComp obsInst) {
        // Check if there is an instrument of the wrong type in this DR
        // recipe's scope.  (Because if there is, it's probably selecting
        // recipes for that other instrument.)
        SpInstObsComp _inst = SpTreeMan.findInstrument(recipe);
        if (_inst != null && obsInst != null) {
            if (!_inst.getClass().equals(obsInst.getClass())) {
                report.add(new ErrorMessage(
                        ErrorMessage.ERROR,
                        obsTitle,
                        "This observation uses a data reduction recipe"
                        + " component for the wrong type of instrument."));
            }
        }

        // Determine the type of instrument, as referred to by the
        // observation which references this DR recipe.
        // (Altered 2013/06/03 -- was previously the instrumet
        // found in the tree above this DR recipe, which could
        // be the wrong one.)
        String instrument = null;
        Vector<String> recipeList = null;
        if (obsInst instanceof SpInstHeterodyne) {
            instrument = "heterodyne";
            recipeList = SpDRRecipe.HETERODYNE.getColumn(0);
        } else if (obsInst instanceof SpInstSCUBA2) {
            instrument = "scuba2";
            recipeList = SpDRRecipe.SCUBA2.getColumn(0);
        }

        // Check that the recipes are set correctly.
        String[] types = recipe.getAvailableTypes(instrument);
        if (types != null) {
            for (String type : types) {
                String shortType = "";
                if (type.toLowerCase().endsWith("recipe")) {
                    shortType = type.substring(0,
                            type.length() - "recipe".length());
                }

                if (thisObs.getClass().getName().toLowerCase()
                        .indexOf(shortType) == -1) {
                    continue;
                }

                String recipeForType = recipe.getRecipeForType(type);
                if (recipeForType == null && !"scuba2".equals(instrument)) {
                    report.add(new ErrorMessage(ErrorMessage.WARNING, obsTitle,
                            "No data reduction recipe set for " + instrument
                            + " " + shortType));

                } else if (recipeForType != null
                        && !recipeList.contains(recipeForType)) {
                    report.add(new ErrorMessage(
                            ErrorMessage.ERROR,
                            obsTitle,
                            recipeForType
                            + " does not appear to be a valid recipe for "
                            + instrument + " " + shortType));
                }
            }
        }
    }

    /**
     * Find a data-reduction recipe component associated with the
     * scope of the given item.
     *
     * This traverses the tree.
     *
     * @param spItem the SpItem defining the scope to search
     */
    public static SpDRRecipe findRecipe(SpItem spItem) {
        SpItem child;                 // Child of spItem
        Enumeration<SpItem> children; // Children of the sequence
        SpItem parent;                // Parent of spItem
        SpItem searchItem;            // The sequence item to search

        if (spItem instanceof SpDRRecipe) {
            return (SpDRRecipe) spItem;
        }

        // Get the parent.
        parent = spItem.parent();

        // Either the item is an observation context, which is what we want,
        // or continue the search one level higher in the hierarchy.
        if (!(spItem instanceof SpObsContextItem)) {
            searchItem = parent;

            if (parent == null) {
                return null;
            }
        } else {
            searchItem = spItem;
        }

        // Search the observation context for the data-reduction recipe.
        children = searchItem.children();
        while (children.hasMoreElements()) {
            child = children.nextElement();

            if (child instanceof SpDRRecipe) {
                return (SpDRRecipe) child;
            }
        }

        if (parent != null) {
            return findRecipe(parent);
        }

        return null;
    }
}
