/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2003                   */
/*                                                              */
/*==============================================================*/
// $Id$

package gemini.sp.obsComp;

import gemini.sp.SpType;
import gemini.sp.obsComp.SpObsComp;

/**
 * This interface is used to provide a common super type for
 * orac.ukirt.inst.SpDRRecipe and orac.jcmt.inst.SpDRRecipe.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public abstract class SpDRObsComp extends SpObsComp {
  public SpDRObsComp(SpType spType) {
    super(spType);
  }
}

