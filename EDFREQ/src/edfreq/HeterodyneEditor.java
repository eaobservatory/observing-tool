/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// $Id$

package edfreq;

/**
 * Heterodyne editor interface.
 *
 * This interface defines the part of the interaction between a Heterodyne editor such as the EdCompInstHeterodyne class
 * of the OT and the frequency editor. If parameters are changed in the frequency editor then the update methods
 * (which can be thought of as callback functions) are called. Additionally methods are provided to give the frequency editor
 * access to band, redshift, source frequency, observe frequency etc as specified in the Heterodyne editor.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public interface HeterodyneEditor {

   public static final String NO_LINE = "No Line";

   public String getFeBand();

   /**
    * Get mode: single side band (ssb), double side band (dsb).
    */
   public String getMode();

   public double getRedshift();

   /**
    * Calculates the rest frequency corresponding to the current IF of a specified subsystem.
    *
    * The current IF of a subsystem is the IF at the centre of the sideband
    * (i.e. <tt>{@link orac.jcmt.inst.SpInstHeterodyne.getCentreFrequency(int)
    *  SpInstHeterodyne.getCentreFrequency(subsystem)}</tt>)<p>
    *
    * The calculation is based the current values for redshift, lo1
    * and the subsystem's centre frequency.
    *
    * @return rest frequency in GHz
    */
   public double getRestFrequency(int subsystem);

   /**
    * Calculates the observe frequency corresponding to the centre of the sideband of
    * a specified subsystem.
    *
    * The calculation is based the current values for redshift, lo1
    * and the subsystem's centre frequency.
    *
    * @return observe frequency in GHz 
    */
   public double getObsFrequency(int subsystem);

   public void updateCentreFrequency(double centre, int subsystem);
   public void updateBandWidth(double width, int subsystem);
   public void updateChannels(int channels, int subsystem);
   public void updateLineDetails(LineDetails lineDetails, int subsystem);
   public void updateLO1(double lo1);
}

