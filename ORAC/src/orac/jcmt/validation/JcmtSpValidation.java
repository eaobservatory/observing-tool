package orac.jcmt.validation;

import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import gemini.sp.*;
import orac.ukirt.util.*;
import orac.ukirt.inst.*;
import orac.ukirt.iter.*;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.iter.SpIterConfigBase;
import gemini.sp.iter.SpIterConfigObs;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterFolder;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTelescopePosList;
import gemini.sp.SpObsContextItem;
import gemini.util.TelescopePos;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.iter.SpIterObserve;
import gemini.sp.iter.SpIterSky;
import gemini.sp.iter.SpIterChop;
import gemini.sp.obsComp.SpSiteQualityObsComp;
import orac.validation.SpValidation;
import orac.validation.ErrorMessage;

/**
 * Validation Tool for JCMT.
 * 
 * This class is used for checking whether the values and settings in a Science Program or
 * Observation are sensible.
 *
 * Errors and warnings are issued otherwise.
 *
 * @author M.Folger@roe.ac.uk (M.Folger@roe.ac.uk)
 */
public class JcmtSpValidation extends SpValidation {
  public void checkMSB(SpMSB spMSB,  Vector report) {
    checkMSBgeneric(spMSB, report);


    // Check target information.
    boolean isCalibration = false;

    if(SpTreeMan.findTargetList(spMSB) == null) {
      // Check whether the MSB is a calibration observation.

      // Is spMSB itself a calibration observation?
      if((spMSB instanceof SpObs) && ((SpObs)spMSB).getIsStandard()) {
        isCalibration = true;
      }
      // Set isCalibration = true, then check the children of spMSB.
      // If any of them is not a calibration then set isCalibration = false.
      else {
        isCalibration = true;

        Enumeration children = spMSB.children();
        SpItem child;

        while(children.hasMoreElements()) {
          child = (SpItem)children.nextElement();

          if((child instanceof SpObs) && (!((SpObs)child).getIsStandard())) {
            isCalibration = false;
          }  
        }
      }
    }

    if(!isCalibration) {
      report.add(new ErrorMessage(ErrorMessage.ERROR,
                                  "MSB \"" + spMSB.getTitle() + "\"",
                                  "Target information is missing.")); 
    }
  }
}

