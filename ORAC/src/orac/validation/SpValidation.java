package orac.validation;

import java.util.Vector;
import java.util.Enumeration;
import gemini.sp.SpProg;
import gemini.sp.SpObs;
import gemini.sp.SpMSB;
import gemini.sp.SpItem;
import gemini.sp.SpNote;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpSiteQualityObsComp;
import gemini.sp.obsComp.SpSchedConstObsComp;
import gemini.sp.obsComp.SpInstObsComp;
import orac.util.SpItemUtilities;

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

    checkSciProgramRecursively(spProg, report);
  }

  protected void checkSciProgramRecursively(SpItem spItem, Vector report) {
    Enumeration children = spItem.children();
    SpItem child;

    while(children.hasMoreElements()) {
      child = (SpItem)children.nextElement();

      if(child instanceof SpMSB) {
        checkMSB((SpMSB)child, report);
      }
      else {
        checkSciProgramRecursively(child, report);
      }
    }
  }

  public void checkMSB(SpMSB spMSB,  Vector report) {

    // Check for observation components (target information, instrument,
    // site quality, scheduling constraints

    if(SpTreeMan.findTargetList(spMSB) == null) {
      report.add(new ErrorMessage(ErrorMessage.ERROR,
                                  "MSB \"" + spMSB.getTitle() + "\"",
                                  "Target information is missing.")); 
    }

    checkMSBgeneric(spMSB, report);
  }

  protected void checkMSBgeneric(SpMSB spMSB,  Vector report) {
/*MFO DEBUG*/System.out.println("in SpValidation.checkMSBgeneric().");

    report.add("------------------------------------------------------\n");

    if(report == null) {
      report = new Vector();
    }

    if(SpTreeMan.findInstrument(spMSB) == null) {
      report.add(new ErrorMessage(ErrorMessage.ERROR,
                                  "MSB \"" + spMSB.getTitle() + "\"",
                                  "Instrument component is missing."));    
    }

    if(SpItemUtilities.findSiteQuality(spMSB) == null) {
      report.add(new ErrorMessage(ErrorMessage.ERROR,
                                  "MSB \"" + spMSB.getTitle() + "\"",
                                  "Site quality component list is missing."));
    }


    // Check whether there are more than one observe instruction notes.
    Vector notes = SpTreeMan.findAllItems(spMSB, SpNote.class.getName());

    if(notes.size() > 1) {
      int numberOfObserveInstructions = 0;

      for(int i = 0; i < notes.size(); i++) {
        if(((SpNote)notes.get(i)).isObserveInstruction()) {
          numberOfObserveInstructions++;

          if(numberOfObserveInstructions > 1) {
            report.add(new ErrorMessage(ErrorMessage.ERROR,
                                        "Note \"" + ((SpNote)notes.get(i)).getTitle() + "\" in MSB \"" + spMSB.getTitle() + "\"",
                                        "Each MSB can only contain one note that has \"Show to the Observer\" ticked."));
          }
        }
      }
    }  


    if(spMSB instanceof SpObs) {
      checkObservation((SpObs)spMSB, report);
    }
    else {

      Enumeration children = spMSB.children();
      SpItem child;

      while(children.hasMoreElements()) {
        child = (SpItem)children.nextElement();

        if(child instanceof SpObs) {
          checkObservation((SpObs)child, report);
        }  
      }
    }
  }

  public void checkObservation(SpObs spObs,  Vector report) {
    if(report == null) {
      report = new Vector();
    }

    Enumeration children;
    SpItem child;

    if(!spObs.isMSB()) {
      children = spObs.children();

      while(children.hasMoreElements()) {
        child = (SpItem)children.nextElement();
        if(child instanceof SpSiteQualityObsComp) {
          report.add(new ErrorMessage(ErrorMessage.ERROR,
                                      "Observation \"" + spObs.getTitle() + "\"",
                                      "Observations inside MSBs must use the site quality component of the parent MSB."));
	}

        if(child instanceof SpSchedConstObsComp) {
          report.add(new ErrorMessage(ErrorMessage.ERROR,
                                      "Observation \"" + spObs.getTitle() + "\"",
                                      "Observations inside MSBs must use the scheduling constraints component of the parent MSB."));
	}
      }
    }
  }
}

