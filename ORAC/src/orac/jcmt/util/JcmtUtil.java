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
  private String []    _targetTags = { "Science", "Reference" };

  public SpValidation getValidationTool() {
    return _spValidation;
  }

  public String [] getTargetTags() {
    return _targetTags;
  }

  public String getBaseTag() {
    return _targetTags[0];
  }

  public String getAdditionalTarget() {
    return _targetTags[1];
  }

  public boolean supports(int feature) {
    switch(feature) {
      case FEATURE_TARGET_INFO_CHOP:        return false;
      case FEATURE_TARGET_INFO_PROP_MOTION: return true;
      case FEATURE_TARGET_INFO_TRACKING:    return true;
      default:                              return false;
    }
  }

  /**
   * Sets PreTranslator in SpItemDOM.
   *
   * Make sure at the time this method is called {@link #getBaseTag()} and
   * {@link #getAdditionalTarget()} return correct values.
   */
  public void installPreTranslator() throws Exception {
    SpItemDOM.setPreTranslator(new JcmtPreTranslator(getBaseTag(), getAdditionalTarget()));
  }
}

