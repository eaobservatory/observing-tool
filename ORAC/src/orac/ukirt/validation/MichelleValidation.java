package orac.ukirt.validation;

import gemini.sp.obsComp.SpInstObsComp;
import orac.validation.InstrumentValidation;
import orac.validation.ErrorMessage;
import java.util.Vector;

/**
 * Implements the validation of Michelle.
 *
 * @author M.Folger@roe.ac.uk UKATC
 */
public class MichelleValidation implements InstrumentValidation {
    
    public void checkInstrument(SpInstObsComp instObsComp, Vector report) {
      if(report == null) {
        report = new Vector();
      }

      //report.add(new ErrorMessage(ErrorMessage.WARNING,
      //                            instObsComp,
      //	                    "WARNING: MichelleValidation.checkInstrument not implemented."));
    }

}
