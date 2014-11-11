/*
 * Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package gemini.sp.iter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

import gemini.sp.SpItem;
import gemini.sp.SpType;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpInstObsComp.IterationTracker;

import gemini.util.TranslationUtils;

/**
 * The Iterator Folder (or "Sequence") item.
 *
 * The job of the folder is to hold iterators for an Observation Context.
 */
@SuppressWarnings("serial")
public class SpIterFolder extends SpItem implements SpTranslatable {
    /**
     * Time needed by the telescope to move to another offset position.
     *
     * 5 seconds.
     */
    private static final double OFFSET_TIME = 5.0;

    /**
     * Default constructor.
     */
    public SpIterFolder() {
        super(SpType.SEQUENCE);
    }

    /**
     * "Compiles" the iterators contained within the folder.
     *
     * This method steps through all the elements produced by its contained
     * iterators and appends them into a vector.
     *
     * @return A Vector of Vectors of SpIterStep
     *
     * @see SpIterEnumeration
     * @see SpIterStep
     */
    public Vector<Vector<SpIterStep>> compile() {
        Vector<Vector<SpIterStep>> code = new Vector<Vector<SpIterStep>>();

        Enumeration<SpItem> e = children();

        while (e.hasMoreElements()) {
            SpItem child = e.nextElement();

            if (!(child instanceof SpIterComp)) {
                continue;
            }

            SpIterComp sic = (SpIterComp) child;

            SpIterEnumeration sie = sic.elements();

            while (sie.hasMoreElements()) {
                code.addElement(sie.nextElement());
            }
        }

        return code;
    }

    /**
     * Debugging method.
     */
    public void printSummary() {
        Enumeration<SpItem> e = children();

        while (e.hasMoreElements()) {
            SpIterComp sic = (SpIterComp) e.nextElement();

            System.out.println("#########");
            SpIterEnumeration sie = sic.elements();

            while (sie.hasMoreElements()) {
                System.out.println("----------");
                Vector<SpIterStep> v = sie.nextElement();

                for (int i = 0; i < v.size(); ++i) {
                    SpIterStep sis = v.elementAt(i);
                    System.out.print(sis.title);

                    try {
                        if (sis.stepCount != 1) {
                            System.out.print(" (" + sis.stepCount + ")");
                        }
                    } finally {
                        System.out.println();
                    }

                    for (int j = 0; j < sis.values.length; ++j) {
                        SpIterValue siv = sis.values[j];
                        System.out.println('\t' + siv.attribute + " = "
                                + siv.values[0]);
                    }
                }
            }

            System.out.println("^^^^^^^^^");
        }
    }

    public double getElapsedTime() {
        SpInstObsComp instrument = SpTreeMan.findInstrument(this);

        if (instrument == null) {
            return 0.0;
        }

        boolean isHeterodyne =
                instrument.getClass().getName().indexOf("SpInstHeterodyne")
                > -1;

        Vector<Vector<SpIterStep>> iterStepVector = compile();
        Vector<SpIterStep> iterStepSubVector = null;
        SpIterStep spIterStep = null;
        IterationTracker iterationTracker = instrument.createIterationTracker();

        double elapsedTime = 0.0;

        int nPol = 0;

        int iterStepVectorSize = 0;
        if (iterStepVector != null) {
            iterStepVectorSize = iterStepVector.size();
        }

        int offsets = 0;
        int iterOffsets = 0;
        int iterRepeat = 0;

        Vector<SpIterOffset> oldIterOffsets = new Vector<SpIterOffset>();

        Vector<Object> stareObs = new Vector<Object>();

        for (int i = 0; i < iterStepVectorSize; i++) {
            iterStepSubVector = iterStepVector.get(i);

            int iterStepSubVectorSize = 0;

            if (iterStepSubVector != null) {
                iterStepSubVectorSize = iterStepSubVector.size();
            }

            for (int j = 0; j < iterStepSubVectorSize; j++) {
                spIterStep = iterStepSubVector.get(j);

                if (spIterStep.item.getClass().getName().endsWith(
                        "SpIterPOL")) {
                    nPol++;
                }

                if (isHeterodyne
                        && spIterStep.item.getClass().getName().endsWith(
                                "SpIterStareObs")) {
                    if (!stareObs.contains(spIterStep.item)) {
                        stareObs.add(spIterStep.item);
                    }

                    offsets++;

                    continue;
                }

                iterationTracker.update(spIterStep);

                if (spIterStep.item instanceof SpIterObserveBase) {
                    elapsedTime += iterationTracker.getObserveStepTime();
                }

                if (spIterStep.item instanceof SpIterRepeat) {
                    int count = ((SpIterRepeat) spIterStep.item).getCount();

                    if (count > 1) {
                        iterRepeat++;
                    }
                }

                if (spIterStep.item instanceof SpIterOffset) {
                    SpIterOffset spIterOffset = (SpIterOffset) spIterStep.item;

                    if (!oldIterOffsets.contains(spIterOffset)) {
                        int count = spIterOffset.getPosList().size();

                        if (count > 1) {
                            iterOffsets += count;
                        }

                        oldIterOffsets.add(spIterOffset);
                    }

                    if (instrument.getClass().getName().indexOf("WFCAM")
                            == -1) {
                        if ((OFFSET_TIME - instrument.getExposureOverhead())
                                > 0.0) {
                            // If for each OFFSET_TIME added an exposure
                            // overhead can be subtracted since this is done
                            // while the telescope moves.
                            elapsedTime += (OFFSET_TIME
                                    - instrument.getExposureOverhead());
                        }
                    }
                }
            }
        }

        if (iterRepeat == 0) {
            iterRepeat++;
        }

        // http://www.jach.hawaii.edu/software/jcmtot/het_obsmodes.html
        // 2007-07-12
        for (Object spIterStareObs : stareObs) {
            if (spIterStareObs != null && isHeterodyne) {
                double totalIntegrationTime = 0.0;
                try {
                    Class<?> spIterStareObsClass =
                            Class.forName("orac.jcmt.iter.SpIterStareObs");
                    Method getSwitchingMode = spIterStareObsClass.getMethod(
                            "getSwitchingMode", new Class[]{});
                    Method hasSeparateOffs = spIterStareObsClass.getMethod(
                            "hasSeparateOffs", new Class[]{});
                    Method getSecsPerCycle = spIterStareObsClass.getMethod(
                            "getSecsPerCycle", new Class[]{});

                    Object switchingMode = getSwitchingMode.invoke(
                            spIterStareObs, new Object[]{});

                    if (switchingMode != null) {
                        boolean isBeamSwitch = false;
                        boolean isPositionSwitch = false;
                        boolean isFastFrequencySwitch = false;

                        Field beamSwitchField =
                                spIterStareObsClass.getField(
                                        "SWITCHING_MODE_BEAM");
                        Object beamSwitch = beamSwitchField.get(spIterStareObs);
                        isBeamSwitch = switchingMode.equals(beamSwitch);

                        Field positionSwitchField =
                                spIterStareObsClass.getField(
                                        "SWITCHING_MODE_POSITION");
                        Object positionSwitch =
                                positionSwitchField.get(spIterStareObs);
                        isPositionSwitch = switchingMode.equals(positionSwitch);

                        Field fastFrequencySwitchField =
                                spIterStareObsClass.getField(
                                        "SWITCHING_MODE_FREQUENCY_F");
                        Object fastFrequencySwitch =
                                fastFrequencySwitchField.get(spIterStareObs);
                        isFastFrequencySwitch =
                                fastFrequencySwitch.equals(fastFrequencySwitch);

                        Object secsPerCycle = getSecsPerCycle.invoke(
                                spIterStareObs, new Object[]{});

                        int integrationTimePerPoint = 0;

                        if (secsPerCycle != null
                                && secsPerCycle instanceof Integer) {
                            integrationTimePerPoint = (Integer) secsPerCycle;
                        }

                        Method isContinuum = spIterStareObsClass.getMethod(
                                "isContinuum", new Class[]{});

                        if (isBeamSwitch) {
                            if (iterOffsets == 0) {
                                iterOffsets++;
                            }

                            totalIntegrationTime = iterRepeat
                                    * (2.3 * iterOffsets
                                            * integrationTimePerPoint + 100.0);

                        } else if (isPositionSwitch) {
                            Object separateOff = hasSeparateOffs.invoke(
                                    spIterStareObs, new Object[]{});

                            if (separateOff != null
                                    && separateOff instanceof Boolean) {
                                boolean sharedOff = !(Boolean) separateOff;

                                if (iterOffsets == 0) {
                                    // stare
                                    totalIntegrationTime = iterRepeat * (2.45
                                            * integrationTimePerPoint + 80.0);
                                } else if (sharedOff
                                        || integrationTimePerPoint >= 15) {
                                    // grid
                                    totalIntegrationTime = iterRepeat * (2.65
                                            * iterOffsets
                                            * integrationTimePerPoint + 80.0);
                                } else {
                                    totalIntegrationTime = iterRepeat * (2.0
                                            * iterOffsets
                                            * integrationTimePerPoint + 190.0);
                                }
                            }

                        } else if (isFastFrequencySwitch) {
                            if (iterOffsets == 0) {
                                iterOffsets++;
                            }

                            totalIntegrationTime = iterRepeat * (2.0
                                    * iterOffsets * integrationTimePerPoint
                                    + 120.0);
                        }

                        boolean addContinuum = false;
                        Object continuum = isContinuum.invoke(spIterStareObs,
                                new Object[]{});

                        if (continuum != null && continuum instanceof Boolean) {
                            addContinuum = (Boolean) continuum;
                        }

                        if (addContinuum) {
                            totalIntegrationTime *= 1.2;
                        }
                    }

                } catch (ClassNotFoundException cnfe) {
                    System.out.println("Could not find class " + cnfe);
                } catch (IllegalAccessException iae) {
                    System.out.println("Could not access " + iae);
                } catch (NoSuchMethodException nsme) {
                    System.out.println("Could not find method " + nsme);
                } catch (InvocationTargetException ite) {
                    System.out.println("Could not invoke method " + ite);
                } catch (NoSuchFieldException nsfe) {
                    System.out.println("Could not find field " + nsfe);
                }

                elapsedTime += totalIntegrationTime;
            }
        }

        // Overhead requested by Tim 1/13/10
        if (instrument.getClass().getName().endsWith("SpInstSCUBA2")) {
            elapsedTime += 60.0;
        }

        return elapsedTime;
    }

    public void translateProlog(Vector<String> sequence)
            throws SpTranslationNotSupportedException {
    }

    public void translateEpilog(Vector<String> sequence)
            throws SpTranslationNotSupportedException {
    }

    public void translate(Vector<String> v)
            throws SpTranslationNotSupportedException {
        Enumeration<SpItem> e = this.children();
        TranslationUtils.recurse(e, v);
    }
}
