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

import orac.util.TcsPreTranslator;

/**
 * XML PreTranslator for UKIRT.
 *
 * Applies/removes UKIRT specific changes to {@link orac.util.SpItemDOM} xml.
 *
 * @author Martin Folger
 */
public class UkirtPreTranslator extends TcsPreTranslator {

  private static final String [] TCS_TARGET_TYPES      = { "science", "guide" };

  public UkirtPreTranslator() throws Exception { }

  /**
   * Target types used by the JAC OCS TCS for JCMT XML.
   *
   * The target types are
   * <pre>
   *   "science" (Base position)
   *   "guide"   (Guide star position)
   * </pre>
   *
   * Note that the tag which is defined as "Base" in ot.cfg and also displayed as
   * "Base" in the OT is changed to "science" in the TCS XML output to avoid confusion
   * with the existing &lt;base&gt; element in the TCS XML.
   */
  protected String [] getTcsTargetTypes() {
    return TCS_TARGET_TYPES;
  }
}

