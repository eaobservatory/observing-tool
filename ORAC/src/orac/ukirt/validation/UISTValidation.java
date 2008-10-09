package orac.ukirt.validation ;

import gemini.sp.obsComp.SpInstObsComp ;
import orac.validation.ErrorMessage;
import orac.validation.InstrumentValidation ;
import java.util.Vector ;

/**
 * Implements the validation of UIST.
 *
 * @author A.Pickup@roe.ac.uk UKATC
 */
public class UISTValidation implements InstrumentValidation
{
	public void checkInstrument( SpInstObsComp instObsComp , Vector<ErrorMessage> report )
	{
		if( report == null )
			report = new Vector<ErrorMessage>() ;
	}
}
