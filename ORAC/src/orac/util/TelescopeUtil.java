/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.util;

import orac.validation.SpValidation;

/**
 * Used for telescope specific features.
 *
 * The config file (ot.cfg) is used to specify plug-in like classes (such as instruments etc)
 * and other things.
 *
 * TelescopeUtil classes are used to make telescope specific settings and initialize telescope
 * specific classes such as the validation tool and pre-translator. This means that users do not
 * have be responsible for maintaining the correct full class names of all these telescope specific
 * in the ot.cfg file.
 *
 * @author Martin Folger
 */
public interface TelescopeUtil {

  public SpValidation getValidationTool();

  public void installPreTranslator() throws Exception;
}

