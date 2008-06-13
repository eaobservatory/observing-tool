/* ============================================================== */
/*                                                              */
/* UK Astronomy Technology Centre */
/* Royal Observatory, Edinburgh */
/* Joint Astronomy Centre, Hilo */
/* Copyright (c) PPARC 2003 */
/*                                                              */
/* ============================================================== */
// $Id$
package gemini.sp.obsComp ;

import java.util.Hashtable ;

/**
 * Implemented by instruments that use Micro stepping.
 * 
 * E.g. WFCAM.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public interface SpMicroStepUser
{
	public Hashtable getMicroStepPatterns() ;
}
