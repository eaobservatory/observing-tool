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

  public static final SpType SP_TYPE =
    SpType.create( SpType.OBSERVATION_COMPONENT_TYPE, "inst.Heterodyne", "Heterodyne" );

//Register the prototype.
  static {
    SpFactory.registerPrototype( new SpInstHeterodyne() );
  }

  public SpInstHeterodyne() {
    super( SP_TYPE );
  }
}


