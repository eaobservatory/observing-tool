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

  /**
   * Target Information Component, Tab "Chop Settings".
   *
   * Do <i>not</i> confuse with Telescope Position Editor features.
   */
  public static final int FEATURE_TARGET_INFO_CHOP        = 0;

  /**
   * Target Information Component, Tab "Proper Motion".
   *
   * Do <i>not</i> confuse with Telescope Position Editor features.
   */
  public static final int FEATURE_TARGET_INFO_PROP_MOTION = 1;

  /**
   * Target Information Component, Tab "Tracking Details".
   *
   * Do <i>not</i> confuse with Telescope Position Editor features.
   */
  public static final int FEATURE_TARGET_INFO_TRACKING    = 2;

  public SpValidation getValidationTool();

  public String [] getTargetTags();

  /**
   * Get telescope specific base tag.
   *
   * For example "Base" for UKIRT and "Science" for JCMT.
   */
  public String    getBaseTag();

  /**
   * Returns the tag for the target that can be added to the Target List
   * in addition to Base/Science.
   *
   * For example "GUIDE" for UKIRT and "Reference" for JCMT.
   */
  public String getAdditionalTarget();

  /**
   * Gets full class name of GUIDE/Reference feature.
   *
   * <pre>
     Example:

       <b>Telescope	Additional Target	Feature class</b>
       
       UKIRT		GUIDE			{@link jsky.app.ot.tpe.feat.TpeGuidePosFeature}
       JCMT		Reference		{@link ot.jcmt.tpe.TpeReferencePosFeature}
   * </pre> 
   */
  public String getAdditionalTargetFeatClass();

  public boolean supports(int feature);

  public void installPreTranslator() throws Exception;
}

