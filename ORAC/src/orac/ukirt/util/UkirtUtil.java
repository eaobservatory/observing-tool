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

import gemini.sp.SpTelescopePos;
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

  public String [] getTargetTags() {
    if(SpTelescopePos.GUIDE_TAGS == null) {
      return new String[] { SpTelescopePos.BASE_TAG };
    }

    String [] result = new String[SpTelescopePos.GUIDE_TAGS.length + 1];

    result[0] = SpTelescopePos.BASE_TAG;

    for(int i = 0 ; i < SpTelescopePos.GUIDE_TAGS.length; i++) {
      result[i + 1] = SpTelescopePos.GUIDE_TAGS[i];
    }

    return result;
  }

  public String getBaseTag() {
    return SpTelescopePos.BASE_TAG;
  }

  public String getAdditionalTarget() {
    return SpTelescopePos.GUIDE_TAGS[0];
  }

  public boolean supports(int feature) {
    switch(feature) {
      case FEATURE_TARGET_INFO_CHOP:        return true;
      case FEATURE_TARGET_INFO_PROP_MOTION: return false;
      case FEATURE_TARGET_INFO_TRACKING:    return false;
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
    SpItemDOM.setPreTranslator(new UkirtPreTranslator(getBaseTag(), getAdditionalTarget()));
  }
}

