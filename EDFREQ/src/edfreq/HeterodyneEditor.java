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
 * (which can be thought of as callback functions) are called. Additionally methods are provided to gibe the frequency
 * access to band and redshift information specified in the Heterodyne editor.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public interface HeterodyneEditor { //extends SamplerWatcher {

   public static final String NO_LINE = "No Line";

   public String getFeBand();
   public double getRedshift();
   public void updateCentreFrequency(double centre, int subsystem);
   public void updateBandWidth(double width, int subsystem);
   public void updateChannels(double channels, int subsystem);
   public void updateLineDetails(LineDetails lineDetails, int subsystem);
   public void updateLO1(double lo1);
}

