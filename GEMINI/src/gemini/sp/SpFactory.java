// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import gemini.sp.iter.SpIterFolder;   // will be removed ...
import gemini.util.Assert;

import java.util.Hashtable;

/**
 * The SpFactory should be used by clients to constructs Science Program
 * items.  A prototype of each type of item is stored with the SpFactory.
 * When a new item is created, a prototype for its type is located and
 * cloned.  This will also clone its "client data", if the client data
 * supports the SpCloneableClientData interface.
 *
 * AB Added SKY observe iterator 24-Mar-00
 *
 * @see SpItem#clone
 */
public final class SpFactory
{
   // Holds the prototype SpItems
   private static final Hashtable _prototypes = new Hashtable();

   public static final SpItem SCIENCE_PROGRAM    = new SpProg();
   public static final SpItem SCIENCE_PLAN       = new SpPlan();
   public static final SpItem PHASE_1		 = new SpPhase1();
   public static final SpItem LIBRARY_FOLDER     = new SpLibraryFolder();
   public static final SpItem LIBRARY            =
	new SpLibrary((SpLibraryFolder) LIBRARY_FOLDER);
   public static final SpItem SEQUENCE           = new SpIterFolder();
   public static final SpItem OBSERVATION        =
	new SpObs((SpIterFolder) SEQUENCE);
   public static final SpItem OBSERVATION_FOLDER = new SpObsFolder();
   public static final SpItem OBSERVATION_GROUP  = new SpObsGroup();
   public static final SpItem OBSERVATION_LINK   = new SpObsLink();
   public static final SpItem NOTE               = new SpNote();

   public static final SpItem OBSERVATION_COMPONENT_SITE_QUALITY =
      new gemini.sp.obsComp.SpSiteQualityObsComp();

   public static final SpItem OBSERVATION_COMPONENT_TARGET_LIST =
      new gemini.sp.obsComp.SpTelescopeObsComp();

   public static final SpItem ITERATOR_COMPONENT_OBSERVE =
      new gemini.sp.iter.SpIterObserve();

   public static final SpItem ITERATOR_COMPONENT_SKY =
      new gemini.sp.iter.SpIterSky();

   public static final SpItem ITERATOR_COMPONENT_OFFSET =
      new gemini.sp.iter.SpIterOffset();

   public static final SpItem ITERATOR_COMPONENT_REPEAT =
      new gemini.sp.iter.SpIterRepeat();

static {
   registerPrototype(SCIENCE_PROGRAM);
   registerPrototype(SCIENCE_PLAN);
   registerPrototype(PHASE_1);
   registerPrototype(LIBRARY);
   registerPrototype(LIBRARY_FOLDER);
   registerPrototype(OBSERVATION);
   registerPrototype(OBSERVATION_FOLDER);
   registerPrototype(OBSERVATION_GROUP);
   registerPrototype(OBSERVATION_LINK);
   registerPrototype(SEQUENCE);
   registerPrototype(NOTE);

   registerPrototype(OBSERVATION_COMPONENT_SITE_QUALITY);
   registerPrototype(OBSERVATION_COMPONENT_TARGET_LIST);

   registerPrototype(ITERATOR_COMPONENT_OBSERVE);
   registerPrototype(ITERATOR_COMPONENT_SKY);
   registerPrototype(ITERATOR_COMPONENT_OFFSET);
   registerPrototype(ITERATOR_COMPONENT_REPEAT);
}

/**
 * Register a new prototype.  You cannot replace an existing prototype.
 */
public static void
registerPrototype(SpItem protoItem)
{
   Assert.notFalse(_prototypes.get(protoItem.type()) == null);
   _prototypes.put(protoItem.type(), protoItem);
}

/**
 * Get the prototype of the given SpType.
 */
public static SpItem
getPrototype(SpType spType)
{
   return (SpItem) _prototypes.get(spType);
}

/**
 * Create an item with the given SpType.  This method makes a shallow
 * copy of the prototype, so any children that the prototype item has
 * are <em>not</em> copied to the new item.
 */
public static SpItem
createShallow(SpType spType)
{
   SpItem spItem = getPrototype(spType);
   // Make a copy of the prototype
   if (spItem != null) {
      spItem = spItem.shallowCopy();
   }

   return spItem;
}

/**
 * Duplicate the prototype with the given SpType.  This just makes a
 * deep copy of the associated prototype.
 */
public static SpItem
create(SpType spType)
{
   SpItem spItem = getPrototype(spType);

   // Make a deep copy of the prototype
   if (spItem != null) {
      spItem = spItem.deepCopy();
   }

   return spItem;
}

}
