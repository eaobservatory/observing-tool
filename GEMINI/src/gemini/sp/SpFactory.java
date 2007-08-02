// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import gemini.sp.iter.SpIterFolder; // will be removed ...

import java.util.Hashtable;

/**
 * The SpFactory should be used by clients to constructs Science Program items.
 * A prototype of each type of item is stored with the SpFactory. When a new
 * item is created, a prototype for its type is located and cloned. This will
 * also clone its "client data", if the client data supports the
 * SpCloneableClientData interface.
 * 
 * AB Added SKY observe iterator 24-Mar-00
 * 
 * @see SpItem#clone
 */
public final class SpFactory
{

	// Holds the prototype SpItems
	private static final Hashtable _prototypes = new Hashtable();
	public static final SpItem SCIENCE_PROGRAM = new SpProg();
	public static final SpItem SCIENCE_PLAN = new SpPlan();
	public static final SpItem PHASE_1 = new SpPhase1();
	public static final SpItem LIBRARY_FOLDER = new SpLibraryFolder();
	public static final SpItem LIBRARY = new SpLibrary( ( SpLibraryFolder )LIBRARY_FOLDER );
	public static final SpItem SEQUENCE = new SpIterFolder();
	public static final SpItem OBSERVATION = new SpObs( ( SpIterFolder )SEQUENCE );
	public static final SpItem OBSERVATION_FOLDER = new SpObsFolder();
	public static final SpItem OBSERVATION_GROUP = new SpObsGroup();
	public static final SpItem OBSERVATION_LINK = new SpObsLink();
	public static final SpItem NOTE = new SpNote();

	/**
     * MSB folder for OMP.
     * 
     * Added by MFO (09 July 2001)
     */
	public static final SpItem MSB_FOLDER = new SpMSB();

	/**
     * AND folder for OMP.
     * 
     * Added by MFO (09 July 2001)
     */
	public static final SpItem AND_FOLDER = new SpAND();

	/**
     * OR folder for OMP.
     * 
     * Added by MFO (09 July 2001)
     */
	public static final SpItem OR_FOLDER = new SpOR();

	/**
     * Survey folder for OMP.
     * 
     * Added by SDW (Oct 2004)
     */
	public static final SpItem SURVEY_CONTAINER = new SpSurveyContainer();

	// MFO: JCMT/ACSIS and UKIRT use different site quality components
	// so they have to be specified in the config file.

	public static final SpItem OBSERVATION_COMPONENT_TARGET_LIST = new gemini.sp.obsComp.SpTelescopeObsComp();

	// MFO: "Observe" and "Sky" are not needed in JCMT so they are
	// specified in the config file if needed.

	public static final SpItem ITERATOR_COMPONENT_OFFSET = new gemini.sp.iter.SpIterOffset();

	public static final SpItem ITERATOR_COMPONENT_REPEAT = new gemini.sp.iter.SpIterRepeat();

	static
	{
		registerPrototype( SCIENCE_PROGRAM );
		registerPrototype( SCIENCE_PLAN );
		registerPrototype( PHASE_1 );
		registerPrototype( LIBRARY );
		registerPrototype( LIBRARY_FOLDER );
		registerPrototype( OBSERVATION );
		registerPrototype( OBSERVATION_FOLDER );
		registerPrototype( OBSERVATION_GROUP );
		registerPrototype( OBSERVATION_LINK );
		registerPrototype( SEQUENCE );
		registerPrototype( NOTE );

		// Added by MFO (09 July 2001)
		registerPrototype( MSB_FOLDER );
		registerPrototype( AND_FOLDER );
		registerPrototype( OR_FOLDER );

		// Added by SDW (OCT 2004)
		registerPrototype( SURVEY_CONTAINER );

		// MFO: Changed because UKIRT and JCMT use different site quality
        // components.
		registerPrototype( OBSERVATION_COMPONENT_TARGET_LIST );

		// MFO: "Observe" and "Sky" are not needed in JCMT so they are
		// specified in the config file if needed.
		registerPrototype( ITERATOR_COMPONENT_OFFSET );
		registerPrototype( ITERATOR_COMPONENT_REPEAT );
	}

	/**
     * Register a new prototype. Replacing an existing prototype is now allowed
     * (MFO, June 10, 2002).
     */
	public static void registerPrototype( SpItem protoItem )
	{
		_prototypes.put( protoItem.type() , protoItem );
	}

	/**
     * Get the prototype of the given SpType.
     */
	public static SpItem getPrototype( SpType spType )
	{
		return ( SpItem )_prototypes.get( spType );
	}

	/**
     * Create an item with the given SpType. This method makes a shallow copy of
     * the prototype, so any children that the prototype item has are
     * <em>not</em> copied to the new item.
     */
	public static SpItem createShallow( SpType spType )
	{
		SpItem spItem = getPrototype( spType );
		// Make a copy of the prototype
		if( spItem != null )
			spItem = spItem.shallowCopy();

		return spItem;
	}

	/**
     * Duplicate the prototype with the given SpType. This just makes a deep
     * copy of the associated prototype.
     */
	public static SpItem create( SpType spType )
	{
		SpItem spItem = getPrototype( spType );

		// Make a deep copy of the prototype
		if( spItem != null )
			spItem = spItem.deepCopy();

		return spItem;
	}
}
