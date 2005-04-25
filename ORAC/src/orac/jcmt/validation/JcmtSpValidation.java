package orac.jcmt.validation;

import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import gemini.sp.*;
import orac.jcmt.util.*;
import orac.jcmt.inst.*;
import orac.jcmt.iter.*;
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
import orac.jcmt.SpJCMTConstants;
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

    public void checkObservation( SpObs spObs, Vector report ) {
        if ( report == null ) report = new Vector();
        
        SpInstObsComp obsComp     = SpTreeMan.findInstrument(spObs);
        SpTelescopeObsComp target = SpTreeMan.findTargetList(spObs);
        Vector observes           = SpTreeMan.findAllInstances(spObs, "orac.jcmt.iter.SpIterJCMTObs");
        // Give a warning for heterodyne if integration times > 40 seconds
        // and frequency > 400 GHz
        for ( int count = 0; count < observes.size(); count++ ) {
            SpIterJCMTObs thisObs = (SpIterJCMTObs)observes.elementAt(count);
            if ( obsComp != null && obsComp instanceof SpInstHeterodyne ) {
                if ( ((SpInstHeterodyne)obsComp).getSkyFrequency() > 4.0E11 &&
                      thisObs.getSecsPerCycle() > 40.0 ) {
                    report.add( new ErrorMessage( 
                                ErrorMessage.WARNING,
                                spObs.getTitle(),
                                "Observations > 4GHz should be done with < 40 secs/cycle")
                            );
                }
            }
            // Also check the switching mode.  If we are in beam switch, we need a chop iterator,
            // in position we need a reference in the target
            if ( thisObs.getSwitchingMode().equals(SpJCMTConstants.SWITCHING_MODE_BEAM) ) {
                Vector chops = SpTreeMan.findAllInstances(spObs, "gemini.sp.iter.SpIterChop");
                if ( chops == null || chops.size() == 0 ) {
                    report.add( new ErrorMessage(
                                ErrorMessage.ERROR,
                                spObs.getTitle(),
                                "Chop iterator required for beam switch mode")
                            );
                }
            }
            else if ( thisObs.getSwitchingMode().equals(SpJCMTConstants.SWITCHING_MODE_POSITION) ) {
                if (!(target.getPosList().exists("REFERENCE")) ) {
                    report.add( new ErrorMessage(
                                ErrorMessage.ERROR,
                                spObs.getTitle(),
                                "Position switched observation requires a REFERENCE target")
                            );
                }
            }
        }

        super.checkObservation(spObs, report);
    }
}

