/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.jcmt.util;

import orac.util.TelescopeUtil;
import orac.util.PreTranslator;
import orac.util.SpItemDOM;
import orac.validation.SpValidation;

/**
 * Used for JCMT specific features.
 *
 * @author Martin Folger
 */
public class JcmtUtil implements TelescopeUtil {

  // There is no proper JCMT validation class yet.
  // Use the Generic class instead.
  private SpValidation _spValidation =  new SpValidation();

  public SpValidation getValidationTool() {
    return _spValidation;
  }

  public void installPreTranslator() throws Exception {
    SpItemDOM.setPreTranslator(new JcmtPreTranslator());
  }
}

