// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp ;

import java.util.Enumeration;

import gemini.sp.obsComp.SpTelescopeObsComp ;
import gemini.sp.obsComp.SpInstObsComp ;

/**
 * This is a base class for SpItems that define a observation context. In other
 * words, these are containers for observation components.
 * 
 * <h3>Observation Data</h3>
 * This class maintains a reference to "observation data" for the context. The
 * observation data consists of telescope base position and position angle. The
 * observation data structure gives clients one convenient place to find and
 * monitor this information and frees them from having to watch for target list
 * or instrument item insertions or deletions.
 * <p>
 * The information in the observation data in inherited from containing
 * contexts, if the given context is missing a target list or instrument
 * component.
 * 
 * @see SpObsData
 * @see gemini.sp.obsComp.SpTelescopeObsComp
 * @see gemini.sp.obsComp.SpInstObsComp
 * @see SpObs
 */
@SuppressWarnings( "serial" )
public class SpObsContextItem extends SpItem implements SpBasePosObserver , SpPosAngleObserver
{

	SpObsData _obsData ;

	/**
     * Construct with the specific SpType of the subclass.
     */
	protected SpObsContextItem( SpType spType )
	{
		super( spType ) ;
	}

	/**
     * Override clone to make sure obsData isn't set.
     */
	protected Object clone()
	{
		SpObsContextItem spClone = ( SpObsContextItem )super.clone() ;
		spClone._obsData = null ;
		return spClone ;
	}

	/**
     * Get the Observation Data associated with this item.
     */
	public SpObsData getObsData()
	{
		if( _obsData == null )
			_obsData = new SpObsData() ;
		return _obsData ;
	}

	/**
     * The base position has changed, so update the SpObsData maintained by this
     * class. Updating the SpObsData will notify interested clients.
     */
	public void basePosUpdate( double ra , double dec , double xoff , double yoff , int coordSys )
	{
		_obsData.setBasePos( ra , dec , xoff , yoff , coordSys ) ;
	}

	/**
     * The position angle has changed, so update the SpObsData maintained by
     * this class. Updating the SpObsData will notify interested clients.
     */
	public void posAngleUpdate( double posAngle )
	{
		_obsData.setPosAngle( posAngle ) ;
	}

	//
	// Overriden methods and helpers to keep observation data up-to-date
	//

	//
	// If the newly inserted item is a telescope, instrument, or obs context
	// component, then fix up the ObsData.
	//
	void _fixObsDataAfterInsert( SpItem newItem )
	{
		if( newItem instanceof SpTelescopeObsComp )
			SpObsData.completeSpTelescopeObsCompInsertion( ( SpTelescopeObsComp )newItem ) ;
		else if( newItem instanceof SpInstObsComp )
			SpObsData.completeSpInstObsCompInsertion( ( SpInstObsComp )newItem ) ;
		else if( newItem instanceof SpObsContextItem )
			SpObsData.completeSpObsContextItemInsertion( ( SpObsContextItem )newItem ) ;
	}

	//
	// Fix the obs data after the extraction of a telescope or instrument
	// component, or an obs context item.
	//
	void _fixObsDataBeforeExtract( SpItem spItem )
	{
		if( spItem instanceof SpTelescopeObsComp )
			SpObsData.prepareSpTelescopeObsCompExtract( spItem ) ;
		else if( spItem instanceof SpInstObsComp )
			SpObsData.prepareSpInstObsCompExtract( spItem ) ;

		else if( spItem instanceof SpObsContextItem )
			SpObsData.prepareSpObsContextItemExtract( spItem ) ;
	}

	/**
     * Override insert to check for the insertion of an item that would effect the SpObsData.
     */
	protected void doInsert( SpItem newChild , SpItem afterChild )
	{
		super.doInsert( newChild , afterChild ) ;
		_fixObsDataAfterInsert( newChild ) ;
	}

	/**
     * Override doExtract to fix up the observation data.
     */
	protected void doExtract( SpItem child )
	{
		_fixObsDataBeforeExtract( child ) ;
		super.doExtract( child ) ;
	}

	/**
	 * Loop over the specified SpItems and apply the given action.
	 *
	 * Recurses if it finds nested instances of SpObsContextItem,
	 * except that it does not recurse into survey containers
	 * because they may require special treatment.
	 */
	public static void applyAction(SpItemAction action,
			 Enumeration<SpItem> children) {
		while (children.hasMoreElements()) {
			SpItem child = children.nextElement();
			action.apply(child);

			if (child instanceof SpObsContextItem
				&& ! (child instanceof SpSurveyContainer)) {
				applyAction(action, child.children());
			}
		}
	}
}
