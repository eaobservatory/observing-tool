// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

/**
 * The science program item.
 */
public class SpProg extends SpRootItem
{
   // The Phase 1 proposal item.  It stores all the information entered
   // during the Phase 1 proposal definition.
   private SpPhase1 _phase1Item;

/**
 * Default constructor.
 */
protected SpProg()
{
   super(SpType.SCIENCE_PROGRAM);
}

//
// Set the Phase 1 item to associate with this program.
//
void
setPhase1Item(SpPhase1 p1)
{
   _phase1Item = p1;
}

/**
 * Get the Phase 1 item associated with this program (if any).  If there
 * is no Phase 1 item for this program, this method will return null.
 */
public SpPhase1
getPhase1Item()
{
   return _phase1Item;
}

}
