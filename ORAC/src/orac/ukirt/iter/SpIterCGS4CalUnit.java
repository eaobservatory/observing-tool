// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.iter.IterConfigItem;
import gemini.sp.iter.SpIterConfigObs;

/**
 * The CGS4CalUnit configuration iterator.
 */
@SuppressWarnings( "serial" )
public class SpIterCGS4CalUnit extends SpIterConfigObs
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "CGS4CalUnit" , "CGS4 Cal Unit (Advanced)" );

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterCGS4CalUnit() );
	}

	/**
	 * Default constructor.
	 */
	public SpIterCGS4CalUnit()
	{
		super( SP_TYPE );
	}

	/**
	 * Get the name of the item being iterated over.  Subclasses must
	 * define.
	 */
	public String getItemName()
	{
		return "CGS4 Cal Unit";
	}

	/**
	 * Get the array containing the IterConfigItems offered by the Cal Unit.
	 */
	public IterConfigItem[] getAvailableItems()
	{
		String[] calTypes = { "Arc" , "Flat" };
		IterConfigItem iciCalType = new IterConfigItem( "Cal. Type" , SpCGS4CalUnitConstants.ATTR_CALTYPE + "Iter" , calTypes );
		IterConfigItem iciArcLamps = new IterConfigItem( "Arc Lamp" , SpCGS4CalUnitConstants.ATTR_LAMP + "Iter" , SpIterCGS4CalObs.ARC_LAMPS );
		IterConfigItem iciFlatLamps = new IterConfigItem( "Flat Lamp" , SpCGS4CalUnitConstants.ATTR_LAMP + "Iter" , SpIterCGS4CalObs.FLAT_LAMPS );
		IterConfigItem iciFilters = new IterConfigItem( "Filter" , SpCGS4CalUnitConstants.ATTR_FILTER + "Iter" , SpIterCGS4CalObs.FILTERS );
		IterConfigItem iciModes = new IterConfigItem( "Mode" , SpCGS4CalUnitConstants.ATTR_MODE + "Iter" , SpIterCGS4CalObs.MODES );
		IterConfigItem[] iciA = { iciCalType , iciArcLamps , iciFlatLamps , iciFilters , iciModes , getExposureTimeConfigItem() , getCoaddsConfigItem() };

		return iciA;
	}
}
