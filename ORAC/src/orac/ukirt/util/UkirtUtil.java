/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.ukirt.util;

import orac.util.TelescopeUtil;
import orac.util.PreTranslator;
import orac.util.SpItemDOM;
import orac.validation.SpValidation;
import orac.ukirt.validation.UkirtSpValidation;

/**
 * Used for UKIRT specific features.
 *
 * @author Martin Folger
 */
public class UkirtUtil implements TelescopeUtil {

  private UkirtSpValidation _ukirtSpValidation = new UkirtSpValidation();

  public SpValidation getValidationTool() {
    return _ukirtSpValidation;
  }

  public void installPreTranslator() throws Exception {
    SpItemDOM.setPreTranslator(new UkirtPreTranslator());
  }
}

