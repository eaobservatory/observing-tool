/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.jcmt.inst;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

/**
 * The Heterodyne instrument Observation Component.
 *
 * Note that ot.jcmt.inst.editor.EdCompInstHeterodyne manipulates the _avTable of the
 * SpInstHeterodyne item directly (via getTable()). That's why SpInstHeterodyne
 * does not have any proper functionality of its own at the moment. This might
 * change in the future.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public final class SpInstHeterodyne extends SpJCMTInstObsComp {

  public static String [] JIGGLE_PATTERNS = { "5 Point", "Jiggle", "Rotation" };

  /**
   * Front end name.
   *
   * @see #setFrontEndName(java.lang.String)
   */
  private String _frontEndName = "";

  public static final SpType SP_TYPE =
    SpType.create( SpType.OBSERVATION_COMPONENT_TYPE, "inst.Heterodyne", "Het Setup" );

//Register the prototype.
  static {
    SpFactory.registerPrototype( new SpInstHeterodyne() );
  }

  public SpInstHeterodyne() {
    super( SP_TYPE );
  }

  /**
   * Allows front end name to be appended to the title.
   *
   * This instrument component SpInstHeterodyne has the front end name stored to its _avTable of course.
   * But the attribute name is derived from the XML tag name specified in
   * the interface edfreq.FrequencyEditorConstants which is in orac3/EDFREQ. Since orac3/ORAC
   * (which contains SpInstHeterodyne) is supposed to be independent of orac3/EDFREQ (this might
   * change in the future) SpInstHeterodyne cannot actually access its information (such as the front
   * end name). This does not matter as long as the information inside SpInstHeterodyne is always given
   * to the outside world via XML. The only problem that arises from this is that this makes it difficult
   * to append the front end name to the title of SpInstHeterodyne. In order to fix this problem
   * _frontEndName and this method setFrontEndName(java.lang.String) have been introduced that allow
   * the method getTitle() to append _frontEndName to the title.
   *
   * @see #getTitle().
   */
  public void setFrontEndName(String frontEndName) {
    _frontEndName = frontEndName;
  }

  /**
   * Appends front end name to title.
   */
  public String getTitle() {
    if((_frontEndName != null) && (!_frontEndName.equals(""))) {
      return super.getTitle() + ": " + _frontEndName;
    }
    else {
      return super.getTitle();
    }
  }

  /**
   * Get jiggle pattern options.
   *
   * @return String array of jiggle pattern options.
   */
  public String [] getJigglePatterns() {
    return JIGGLE_PATTERNS;
  }

  /** Not properly implemented yet. Returns 0.0. */
  public double getDefaultScanVelocity() {
    return 0.0;
  }

  /** Not properly implemented yet. Returns 0.0. */
  public double getDefaultScanDy() {
    return 0.0;
  }
}


