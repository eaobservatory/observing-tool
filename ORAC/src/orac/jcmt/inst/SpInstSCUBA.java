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

import java.io.*;
import java.util.*;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.util.Angle;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpStareCapability;

/**
 * The SCUBA instrument Observation Component
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public final class SpInstSCUBA extends SpJCMTInstObsComp {
   public static final SpType SP_TYPE =
     SpType.create( SpType.OBSERVATION_COMPONENT_TYPE, "inst.SCUBA", "SCUBA" );

// Register the prototype.
   static {
      SpFactory.registerPrototype( new SpInstSCUBA() );
   }

   public SpInstSCUBA() {
      super( SP_TYPE );
   }
}
