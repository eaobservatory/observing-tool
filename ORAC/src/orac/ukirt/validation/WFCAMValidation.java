package orac.ukirt.validation;

import gemini.sp.obsComp.SpInstObsComp;
import orac.validation.InstrumentValidation;
import orac.validation.ErrorMessage;
import java.util.Vector;

/**
 * Implements the validation of WFCAM.
 *
 * @author A.Pickup@roe.ac.uk UKATC
 */
public class WFCAMValidation implements InstrumentValidation {
    
    public void checkInstrument(SpInstObsComp instObsComp, Vector report) {
      if(report == null) {
        report = new Vector();
      }

      //report.add(new ErrorMessage(ErrorMessage.WARNING,
      //                            instObsComp,
      //	                    "WARNING: WFCAMValidation.checkInstrument not implemented."));
    }

}
