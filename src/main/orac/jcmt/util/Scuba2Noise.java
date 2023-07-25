/*
 * Copyright (C) 2009 Science and Technology Facilities Council.
 * All Rights Reserved.
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

package orac.jcmt.util;

import gemini.util.MathUtil;
import orac.util.OrderedMap;
import orac.jcmt.SpJCMTConstants;
import java.util.Vector;

public class Scuba2Noise {
    private static Scuba2Noise self;
    private OrderedMap<Double, Double> fourFifty;
    private OrderedMap<Double, Double> eightFifty;

    private static class TATB {
        public final double tA;
        public final double tB;

        public TATB(double tA, double tB) {
            this.tA = tA;
            this.tB = tB;
        }
    }

    public final static double[] scuba2PongSizes = {
            900,
            1800,
            2700,
            3600,
            7200,
    };

    // Time parameters for standard map sizes
    // taken from propscuba2itc.pl

    private enum Scuba2Wavelength {
        WL850("850",
                189, -48,                                      // point
                763.2, -64.0,                                  // point pol
                new double[]{407, 795, 1174, 1675, 3354},      // pong tA
                new double[]{-104, -203, -300, -428, -857},    // pong tB
                2.640625, 9.0),                                // binning factors (non-pol, pol)

        WL450("450",
                689, -118,                                     // point
                4626.8, -411.54,                               // point pol
                new double[]{1483, 2904, 4143, 6317, 12837},   // pong tA
                new double[]{-254, -497, -709, -1082, -2200},  // pong tB
                4.0, 36.0);                                    // binning factors (non-pol, pol)

        public final String id;
        public final double pointTA;
        public final double pointTB;
        public final double pointPolTA;
        public final double pointPolTB;
        public final double[] pongTA;
        public final double[] pongTB;
        public final double bin;
        public final double binPol;

        Scuba2Wavelength(String id, double pointTA, double pointTB,
                double pointPolTA, double pointPolTB,
                double[] pongTA, double[] pongTB,
                double bin, double binPol) {
            this.id = id;

            assert (pongTA.length == scuba2PongSizes.length);
            assert (pongTB.length == scuba2PongSizes.length);

            this.pointTA = pointTA;
            this.pointTB = pointTB;
            this.pointPolTA = pointPolTA;
            this.pointPolTB = pointPolTB;
            this.pongTA = pongTA;
            this.pongTB = pongTB;
            this.bin = bin;
            this.binPol = binPol;
        }

        public static Scuba2Wavelength fromId(String id)
                throws IllegalArgumentException {
            for (Scuba2Wavelength wl : values()) {
                if (wl.id.equals(id)) {
                    return wl;
                }
            }
            throw new IllegalArgumentException("SCUBA-2 wavelength " + id
                    + " not known");
        }

        public double getBin(boolean pol) {
            return pol ? binPol : bin;
        }

        public TATB getTATB(double mapSize, String scanStrategy, boolean pol) {
            if (SpJCMTConstants.SCAN_PATTERN_POINT.equals(scanStrategy)) {
                if (pol) {
                    return new TATB(pointPolTA, pointPolTB);
                }
                else {
                    return new TATB(pointTA, pointTB);
                }

            } else if (SpJCMTConstants.SCAN_PATTERN_PONG.equals(scanStrategy)) {
                return new TATB(interpolateParameter(scuba2PongSizes, pongTA,
                        mapSize, "Map size"), interpolateParameter(
                        scuba2PongSizes, pongTB, mapSize, "Map size"));

            } else {
                throw new IllegalArgumentException(
                        "Can not calculate noise for strategy " + scanStrategy);
            }
        }

        private double interpolateParameter(double[] xs, double[] ys, double x,
                String controlName) {
            assert (xs.length == ys.length);
            if (x < xs[0]) {
                throw new IllegalArgumentException(controlName + " " + x
                        + " too small to calculate noise");
            }

            if (x > xs[xs.length - 1]) {
                throw new IllegalArgumentException(controlName + " " + x
                        + " too large to calculate noise");
            }

            double x0 = 0;
            double x1 = 0;
            double y0 = 0;
            double y1 = 0;

            for (int i = 0; i < xs.length; i++) {
                if (xs[i] == x) {
                    return ys[i];
                } else if (xs[i] > x) {
                    x1 = xs[i];
                    y1 = ys[i];
                    break;
                } else {
                    x0 = xs[i];
                    y0 = ys[i];
                }
            }

            return MathUtil.linterp(x0, y0, x1, y1, x);
        }
    };

    public static final String four50 = Scuba2Wavelength.WL450.id;
    public static final String eight50 = Scuba2Wavelength.WL850.id;

    private static final double fourFiftyTauMultiplicand = 23.3;
    private static final double fourFiftyTauCorrection = -0.018;
    private static final double fourFiftyTauCorrectionSq = 0.05;
    private static final double eightFiftyTauMultiplicand = 3.71;
    private static final double eightFiftyTauCorrection = -0.040;
    private static final double eightFiftyTauCorrectionSq = 0.202;

    /**
     * Singleton constructor, private for obvious reasons.
     */
    private Scuba2Noise() {
        // Parameters for NEFD estimation
        fourFifty = new OrderedMap<Double, Double>();
        fourFifty.add(0.040, 100.0);
        fourFifty.add(0.065, 220.0);
        fourFifty.add(0.100, 550.0);
        fourFifty.add(0.150, 5500.0);

        // Parameters for NEFD estimation
        eightFifty = new OrderedMap<Double, Double>();
        eightFifty.add(0.040, 50.0);
        eightFifty.add(0.065, 55.0);
        eightFifty.add(0.100, 70.0);
        eightFifty.add(0.150, 90.0);
    }

    /**
     * Get singleton instance
     * @return the singleton instance of Scuba2Noise
     */
    public static synchronized Scuba2Noise getInstance() {
        if (self == null) {
            self = new Scuba2Noise();
        }

        return self;
    }

    /**
     * Calculate the noise equivalent flux density in milijanskys at
     * 450 microns with relation to time.
     *
     * @param timeSeconds
     * @param tau
     * @param airmass
     *
     * @return noise equivalent flux density in milijanskys
     */
    public double calculateNEFD450ForTime(double timeSeconds, double tau,
            double airmass) {
        double mJy = -1.0;
        mJy = calculateNEFDForTime(four50, timeSeconds, tau, airmass);
        return mJy;
    }

    /**
     * Calculate the noise equivalent flux density in milijanskys at
     * 850 microns with relation to time.
     *
     * @param timeSeconds
     * @param tau
     * @param airmass
     *
     * @return noise equivalent flux density in milijanskys
     */
    public double calculateNEFD850ForTime(double timeSeconds, double tau,
            double airmass) {
        double mJy = -1.0;
        mJy = calculateNEFDForTime(eight50, timeSeconds, tau, airmass);
        return mJy;
    }

    /**
     * Generic method to calculate the noise equivalent flux density
     * in milijanskys for 450 and 850 microns with relation to time.
     *
     * @param timeSeconds
     * @param tau
     * @param airmass
     * @param wavelength
     *
     * @return noise equivalent flux density in milijanskys
     */
    public double calculateNEFDForTime(String wavelength, Double timeSeconds,
            Double tau, double airmass) {
        double mJy = -1.0;

        mJy = calculateNEFD(wavelength, tau, airmass);

        if (mJy != -1.0) {
            mJy /= StrictMath.sqrt(timeSeconds);
        }

        return mJy;
    }

    /**
     * Generic method to calculate the noise equivalent flux density
     * in milijanskys for 450 and 850 microns
     *
     * @param tau
     * @param airmass
     * @param wavelength
     *
     * @return noise equivalent flux density in milijanskys
     */
    public double calculateNEFD(String waveLength, Double csoTau, double airmass)
            throws IllegalArgumentException {
        double mJy = -1.0;

        OrderedMap<Double, Double> wavelengthLookup = null;

        if (four50.equals(waveLength)) {
            wavelengthLookup = fourFifty;
        } else if (eight50.equals(waveLength)) {
            wavelengthLookup = eightFifty;
        } else {
            throw new IllegalArgumentException("Wave length " + waveLength
                    + " unknown at this time.");
        }

        Vector<Double> csoTaus = wavelengthLookup.keys();
        if (csoTaus.contains(csoTau)) {
            mJy = wavelengthLookup.find(csoTau);

        } else if (csoTau > 0 && csoTau < csoTaus.lastElement()) {
            double before = 0.0;
            double after = 0.0;

            for (int index = 0; index < csoTaus.size(); index++) {
                double current = csoTaus.elementAt(index);

                if (current > csoTau) {
                    after = current;
                    if (index > 0) {
                        before = csoTaus.elementAt(index - 1);
                    }
                    break;
                }
            }

            double beforeNoise = 0.0;
            if (before > 0.0) {
                beforeNoise = wavelengthLookup.find(before);
            }

            double afterNoise = wavelengthLookup.find(after);
            mJy = MathUtil.linterp(before, beforeNoise, after, afterNoise,
                    csoTau);
        }

        if (mJy != -1) {
            mJy = correctForAirmass(waveLength, mJy, csoTau, airmass);
        }

        return mJy;
    }

    /**
     * Correct input value in milijanskys based on given CSO Tau and
     * airmass for a given wavelength.
     *
     * @param waveLength
     * @param mJy
     * @param csoTau
     * @param airmass
     *
     * @return corrected input value in milijanskys.
     */
    private double correctForAirmass(String waveLength, double mJy,
            double csoTau, double airmass) {
        double ts2 = calculateTau(waveLength, csoTau);

        double transmission = StrictMath.exp((airmass - 1) * ts2);
        mJy *= transmission;

        return mJy;
    }

    /**
     * Calculate tau value for a particular wavelength.
     *
     * @param waveLength
     * @param csoTau
     *
     * @return sky opacity
     */
    private double calculateTau(String waveLength, double csoTau)
            throws IllegalArgumentException {
        double tauMultiplicand = 0.0;
        double tauCorrection = 0.0;
        double tauCorrectionSq = 0.0;

        if (four50.equals(waveLength)) {
            tauMultiplicand = fourFiftyTauMultiplicand;
            tauCorrection = fourFiftyTauCorrection;
            tauCorrectionSq = fourFiftyTauCorrectionSq;

        } else if (eight50.equals(waveLength)) {
            tauMultiplicand = eightFiftyTauMultiplicand;
            tauCorrection = eightFiftyTauCorrection;
            tauCorrectionSq = eightFiftyTauCorrectionSq;

        } else {
            throw new IllegalArgumentException("Wave length " + waveLength
                    + " unknown at this time.");
        }

        return tauMultiplicand * (csoTau + tauCorrection + tauCorrectionSq * Math.sqrt(csoTau));
    }

    /**
     * Calculate the noise achieved for a given map
     *
     * @param waveLength
     * @param time
     * @param csoTau
     * @param airmass
     * @param widthArcSeconds
     * @param heightArcSeconds
     *
     * @return desired noise in milijanskys
     */
    public double noiseForMapTotalIntegrationTime(String waveLength,
            double time, double csoTau, double airmass, double widthArcSeconds,
            double heightArcSeconds, String scanStrategy, boolean pol)
            throws IllegalArgumentException {
        double mJy = -1;

        double mapSize = (widthArcSeconds == heightArcSeconds)
                ? widthArcSeconds
                : Math.sqrt(widthArcSeconds * heightArcSeconds);

        Scuba2Wavelength wl = Scuba2Wavelength.fromId(waveLength);

        TATB p = wl.getTATB(mapSize, scanStrategy, pol);

        // binning factor
        final double factor = wl.getBin(pol);

        final double opacity = calculateTau(waveLength, csoTau);

        final double trans = StrictMath.exp(-airmass * opacity);

        mJy = (p.tA / trans + p.tB) / StrictMath.sqrt(factor * time);

        return mJy;
    }

    /**
     * Method for testing.
     *
     * @param args
     */
    public static void main(String[] args) {
        Scuba2Noise s2n = getInstance();

        double timeSeconds = 1.0;
        double csoTau = 0.040;
        double airmass = 0.0;
        String waveLength = null;
        Integer programType = null;
        double result = -1.0;

        if (args.length == 0) {
            System.out.println("I have no argument with that.");
            System.exit(-1);
        }

        if (args.length > 0) {
            programType = new Integer(args[0]);
        }

        if (args.length > 1) {
            waveLength = args[1];
        }

        if (!four50.equals(waveLength) && !eight50.equals(waveLength)) {
            throw new RuntimeException("Wave length " + waveLength
                    + " unknown at this time.");
        }

        System.out.println("Desired wave length = " + waveLength);

        try {
            switch (programType) {
                case 0:
                    System.out.println("Calculating NEFD. Requires arguments"
                            + " for CSO Tau and airmass.");
                    csoTau = new Double(args[2]);
                    airmass = new Double(args[3]);
                    result = s2n.calculateNEFD(waveLength, csoTau, airmass);
                    System.out.println("NEFD using CSO Tau " + csoTau
                            + ", airmass " + airmass
                            + " = " + result + " mJy.");
                    break;

                case 1:
                    System.out.println("Calculating NEFD for time. Requires"
                            + " arguments for time, CSO Tau and airmass.");
                    timeSeconds = new Double(args[2]);
                    csoTau = new Double(args[3]);
                    airmass = new Double(args[4]);
                    result = s2n.calculateNEFDForTime(waveLength, timeSeconds,
                            csoTau, airmass);
                    System.out.println("NEFD using time " + timeSeconds
                            + " seconds, CSO Tau " + csoTau
                            + ", airmass " + airmass
                            + " = " + result + " mJy.");
                    break;

                case 2:
                    System.out.println("Calculating noise for total"
                            + " integration time for map. Requires arguments"
                            + " for CSO Tau, airmass, time,"
                            + " width and height (arcminutes).");
                    csoTau = new Double(args[2]);
                    airmass = new Double(args[3]);
                    timeSeconds = new Double(args[4]);
                    double widthArcMinutes = new Double(args[5]);
                    double heightArcMinutes = new Double(args[6]);

                    String strategy = (widthArcMinutes == 0)
                            ? SpJCMTConstants.SCAN_PATTERN_POINT
                            : SpJCMTConstants.SCAN_PATTERN_PONG;

                    result = s2n.noiseForMapTotalIntegrationTime(waveLength,
                            timeSeconds, csoTau, airmass,
                            (widthArcMinutes * 60), (heightArcMinutes * 60),
                            strategy, false);

                    System.out.println("Time for map using CSO Tau " + csoTau
                            + ", airmass " + airmass
                            + ", time " + timeSeconds
                            + ", width " + widthArcMinutes
                            + ", height " + heightArcMinutes
                            + ", strategy " + strategy
                            + " = " + result + " mJy.");
                    break;

                default:
                    System.out.println("Unknown program type.");
                    System.out.println("\t0 : Calculate NEFD.");
                    System.out.println("\t1 : Calculate NEFD for time.");
                    System.out.println("\t2 : Calculate noise for total"
                            + " integration time of map.");
                    break;
            }
        } catch (IndexOutOfBoundsException ioobe) {
            System.out.println("Not enough arguments for program type.");
        } catch (NumberFormatException nfe) {
            System.out.println("Argument should be a double.");
        } catch (Exception e) {
            System.out.println("Unhandled exception " + e);
        }
    }
}
