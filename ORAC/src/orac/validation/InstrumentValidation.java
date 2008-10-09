package orac.validation ;

import gemini.sp.obsComp.SpInstObsComp ;
import java.util.Vector ;

/**
 * Interface for instrument component validation.
 * 
 * @author M.Folger@roe.ac.uk UKATC
 */
public interface InstrumentValidation
{
	public void checkInstrument( SpInstObsComp instObsComp , Vector<ErrorMessage> report ) ;
}
