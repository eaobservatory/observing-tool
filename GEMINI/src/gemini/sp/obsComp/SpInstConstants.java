// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.obsComp;

/**
 * Attribute names shared in common with many instruments.
 */
public interface SpInstConstants
{
   String ATTR_EXPOSURE_TIME			= "exposureTime";
   String ATTR_POS_ANGLE			= "posAngle";

   String ATTR_EXPOSURES_PER_CHOP_POSITION	= "exposuresPerChopPosition";
   int    DEF_EXPOSURES_PER_CHOP_POSITION     	= 1;

   String ATTR_CHOP_CYCLES_PER_NOD		= "chopCyclesPerNod";
   int    DEF_CHOP_CYCLES_PER_NOD		= 1;

   String ATTR_CYCLES_PER_OBSERVE		= "cyclesPerObserve";
   int    DEF_CYCLES_PER_OBSERVE		= 0;

   String  ATTR_NODDING				= "nodding";
   boolean DEF_NODDING				= false;

   String ATTR_COADDS				= "coadds";
}

 
