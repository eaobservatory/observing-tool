package orac.validation;

import java.util.Vector;
import gemini.sp.SpProg;
import gemini.sp.SpObs;

/**
 * Validation Tool.
 * 
 * This class is used for checking whether the values and settings in a Science Program or
 * Observation are sensible.
 *
 * Errors and warnings are issued otherwise.
 *
 * The class contains a main method and can be used as a stand alone command line tool to validate science programs.
 *
 * @author M.Folger@roe.ac.uk UKATC
 */
public interface SpValidation {
  public void checkSciProgram(SpProg spProg, Vector report);
  public void checkObservation(SpObs spObs,  Vector report);
}

