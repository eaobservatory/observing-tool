package orac.validation;

import java.util.Vector;
import java.util.Enumeration;
import gemini.sp.SpProg;
import gemini.sp.SpObs;
import gemini.sp.SpMSB;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpSiteQualityObsComp;

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
public class SpValidation {
  public void checkSciProgram(SpProg spProg, Vector report) {
    if(report == null) {
      report = new Vector();
    }

    SpObs spObs = null;
    SpMSB spMSB = null;
    Vector itemVector = null;
    Enumeration children = null;
    boolean hasSiteQualityComponent = false;

    // Issue an Error message for each MSB without a Site Quality component.
    itemVector = SpTreeMan.findAllItems(spProg, SpMSB.class.getName());
    for(int i = 0; i < itemVector.size(); i++) {
      spMSB = (SpMSB)itemVector.get(i);
      children = spMSB.children();
      hasSiteQualityComponent = false;

      while(children.hasMoreElements()) {
        if(children.nextElement() instanceof SpSiteQualityObsComp) {
          hasSiteQualityComponent = true;
	  break;
	}
      }

      if(!hasSiteQualityComponent) {
        report.add(new ErrorMessage(ErrorMessage.ERROR,
	                            "MSB \"" + spMSB.getTitle() + "\"",
	                            "Site Quality component is missing."));

      }
    }

    // Issue a warning for each Observation that is not an MSB but has a Site Quality component.
    itemVector = SpTreeMan.findAllItems(spProg, SpObs.class.getName());

    for(int i = 0; i < itemVector.size(); i++) {
      spObs = (SpObs)itemVector.get(i);
      children = spObs.children();
      hasSiteQualityComponent = false;

      while(children.hasMoreElements()) {
        if(children.nextElement() instanceof SpSiteQualityObsComp) {
          hasSiteQualityComponent = true;
	  break;
	}
      }

      if(!spObs.isMSB() && hasSiteQualityComponent) {
        report.add(new ErrorMessage(ErrorMessage.ERROR,
	                            "Observation \"" + spObs.getTitle() + "\"",
	                            "Site Quality will be ignored because this Observation is not an MSB."));

      }
    }
    
  }
  
  public void checkObservation(SpObs spObs,  Vector report) { }
}

