// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.obsComp ;

/**
 * A capability for getting and setting chop attributes.
 */
public class SpChopCapability extends SpInstCapability implements SpInstConstants
{

	public static final String CAPABILITY_NAME = "chop" ;

	/**
	 */
	public SpChopCapability()
	{
		super( CAPABILITY_NAME ) ;
	}

	// ---- EXPOSURES PER CHOP POSITION ----

	/**
     * Get the exposures per chop position.
     */
	public int getExposuresPerChopPosition()
	{
		String attr = ATTR_EXPOSURES_PER_CHOP_POSITION ;
		int def = DEF_EXPOSURES_PER_CHOP_POSITION ;
		return _avTable.getInt( attr , def ) ;
	}

	/**
     * Get the exposures per chop position as a string.
     */
	public String getExposuresPerChopPositionAsString()
	{
		return String.valueOf( getExposuresPerChopPosition() ) ;
	}

	/**
     * Set the exposures per chop position.
     */
	public void setExposuresPerChopPosition( int exposuresPerChopPosition )
	{
		_avTable.set( ATTR_EXPOSURES_PER_CHOP_POSITION , exposuresPerChopPosition ) ;
	}

	/**
     * Set the exposures per chop position as a string.
     */
	public void setExposuresPerChopPosition( String exposuresPerChopPosition )
	{
		int i = DEF_EXPOSURES_PER_CHOP_POSITION ;
		try
		{
			i = Integer.parseInt( exposuresPerChopPosition ) ;
		}
		catch( Exception ex ){}

		_avTable.set( ATTR_EXPOSURES_PER_CHOP_POSITION , i ) ;
	}

	// ---- CHOP CYCLES PER NOD ----

	/**
     * Get the chop cycles per nod.
     */
	public int getChopCyclesPerNod()
	{
		String attr = ATTR_CHOP_CYCLES_PER_NOD ;
		int def = DEF_CHOP_CYCLES_PER_NOD ;
		return _avTable.getInt( attr , def ) ;
	}

	/**
     * Get the chop cycles per nod as a string.
     */
	public String getChopCyclesPerNodAsString()
	{
		return String.valueOf( getChopCyclesPerNod() ) ;
	}

	/**
     * Set the chop cycles per nod.
     */
	public void setChopCyclesPerNod( int chopCyclesPerNod )
	{
		_avTable.set( ATTR_CHOP_CYCLES_PER_NOD , chopCyclesPerNod ) ;
	}

	/**
     * Set the chop cycles per nod as a string.
     */
	public void setChopCyclesPerNod( String chopCyclesPerNod )
	{
		int i = DEF_CHOP_CYCLES_PER_NOD ;
		try
		{
			i = Integer.parseInt( chopCyclesPerNod ) ;
		}
		catch( Exception ex ){}

		_avTable.set( ATTR_CHOP_CYCLES_PER_NOD , i ) ;
	}

	// ---- (CHOP/NOD) CYCLES PER OBSERVE ----

	/**
     * Get the (chop/nod) cycles per observe.
     */
	public int getCyclesPerObserve()
	{
		String attr = ATTR_CYCLES_PER_OBSERVE ;
		int def = DEF_CYCLES_PER_OBSERVE ;
		return _avTable.getInt( attr , def ) ;
	}

	/**
     * Get the (chop/nod) cycles per observe as a string.
     */
	public String getCyclesPerObserveAsString()
	{
		return String.valueOf( getCyclesPerObserve() ) ;
	}

	/**
     * Set the (chop/nod) cycles per observe.
     */
	public void setCyclesPerObserve( int cyclesPerObserve )
	{
		_avTable.set( ATTR_CYCLES_PER_OBSERVE , cyclesPerObserve ) ;
	}

	/**
     * Set the (chop/nod) cycles per observe as a string.
     */
	public void setCyclesPerObserve( String cyclesPerObserve )
	{
		int i = DEF_CYCLES_PER_OBSERVE ;
		try
		{
			i = Integer.parseInt( cyclesPerObserve ) ;
		}
		catch( Exception ex ){}

		_avTable.set( ATTR_CYCLES_PER_OBSERVE , i ) ;
	}

	// ---- NODDING? ----

	/**
     * Get the nodding attribute.
     */
	public boolean getNodding()
	{
		String attr = ATTR_NODDING ;
		boolean res = DEF_NODDING ;
		if( _avTable.exists( attr ) )
			res = _avTable.getBool( attr ) ;
		return res ;
	}

	/**
     * Set the nodding attribute.
     */
	public void setNodding( boolean nodding )
	{
		String attr = ATTR_NODDING ;
		_avTable.set( attr , nodding ) ;
	}
}
