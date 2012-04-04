/* ============================================================== */
/*                                                              */
/* UK Astronomy Technology Centre */
/* Royal Observatory, Edinburgh */
/* Joint Astronomy Centre, Hilo */
/* Copyright (c) PPARC 2001 */
/*                                                              */
/* ============================================================== */
// $Id$
package gemini.sp ;

import java.util.Enumeration ;

/**
 * OMP class.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class SpOR extends SpObsContextItem
{

	/** This attribute records the number of items in the OR folder. */
	public static final String ATTR_NUMBER_OF_ITEMS = ":numberOfItems" ;

	/**
     * Default constructor.
     */
	protected SpOR()
	{
		super( SpType.OR_FOLDER ) ;
		_avTable.noNotifySet( ATTR_NUMBER_OF_ITEMS , "1" , 0 ) ;
	}


	/**
	 * Create a display title including the number of items to include.
	 */
	public String getTitle() {
		return super.getTitle()
			+ " (" + getNumberOfItems()
			// + " of " + childCount() // does not update...
			+ ")";
	}


	/**
     * Set number of items in OR folder (such as MSBs, AND folder) that are to
     * be selected.
     */
	public void setNumberOfItems( int numberOfItems )
	{
		_avTable.set( ATTR_NUMBER_OF_ITEMS , numberOfItems ) ;
	}

	/**
     * Get number of items in OR folder (such as MSBs, AND folder) that are to
     * be selected.
     * 
     * @return number of items (default 1)
     */
	public int getNumberOfItems()
	{
		return _avTable.getInt( ATTR_NUMBER_OF_ITEMS , 1 ) ;
	}

	/**
     * Calculates the duration of this OR folder.
     * 
     * Returns the mean of the elapsed time per item in the OR folder multiplied
     * by the number of items that are to be selected.
     * 
     * @see #getNumberOfItems()
     */
	public double getTotalTime()
	{
		double elapsedTime = 0. ;

		// Records the number of children that have a duration, i.e. SpAND and
		// SpMSB (and its subclass SpObs).
		int n = 0 ;

		Enumeration<SpItem> children = children() ;
		SpItem spItem = null ;

		while( children.hasMoreElements() )
		{
			spItem = children.nextElement() ;

			if( spItem instanceof SpMSB )
			{
				SpMSB msb = ( SpMSB )spItem ;
				if( msb.getNumberRemaining() > 0 )
					elapsedTime += msb.getTotalTime() * msb.getNumberRemaining() ;
				n++ ;
			}
			else if( spItem instanceof SpAND )
			{
				elapsedTime += (( SpAND )spItem).getTotalTime() ;
				n++ ;
			}
			else if( spItem instanceof SpSurveyContainer )
			{
				elapsedTime += (( SpSurveyContainer )spItem).getTotalTime() ;
				n++ ;
			}
		}

		if( elapsedTime != 0. )
			elapsedTime = ( elapsedTime / n ) * getNumberOfItems() ;
		return elapsedTime ;
	}

	/**
     * Calculates the duration of this OR folder.
     * 
     * Returns the mean of the elapsed time per item in the OR folder multiplied
     * by the number of items that are to be selected.
     * 
     * @see #getNumberOfItems()
     */
	public double getElapsedTime()
	{
		double elapsedTime = 0. ;

		// Records the number of children that have a duration, i.e. SpAND and
		// SpMSB (and its subclass SpObs).
		int n = 0 ;

		Enumeration<SpItem> children = children() ;
		SpItem spItem = null ;

		while( children.hasMoreElements() )
		{
			spItem = children.nextElement() ;

			if( spItem instanceof SpMSB )
			{
				if( ( ( SpMSB )spItem ).getNumberRemaining() > 0 )
				{
					SpMSB msb = ( SpMSB )spItem ;
					elapsedTime += ( msb.getElapsedTime() * msb.getNumberRemaining() ) ;
					n++ ;
				}
			}
			else if( spItem instanceof SpAND )
			{
				elapsedTime += (( SpAND )spItem).getElapsedTime() ;
				n++ ;
			}
			else if( spItem instanceof SpSurveyContainer )
			{
				elapsedTime += (( SpSurveyContainer )spItem).getElapsedTime() ;
				n++ ;
			}
		}

		if( elapsedTime != 0. )
			elapsedTime = ( elapsedTime / n ) * getNumberOfItems() ;
		return elapsedTime ;
	}

	/**
	 * Disabled if set to observe 0 items. 
	 */
	public boolean isDisabled() {
		return getNumberOfItems() == 0; 
	}
}
