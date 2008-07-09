/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package orac.jcmt.iter ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;

/**
 * Focus Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterFlatObs extends SpIterJCMTObs
{

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "flatObs" , "Flat Field" ) ;
	private String[] FLAT_SOURCES = null ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterFlatObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterFlatObs()
	{
		super( SP_TYPE ) ;
	}

	public double getElapsedTime()
	{
		return 0. ;
	}
	
	/** Get the flat source. */
	public String getFlatSource()
	{
		return _avTable.get( ATTR_FLAT_SOURCE ) ;
	}

	/**
	 * Set noise source to one of FLAT_SOURCES.
	 *
	 * @param noiseSource if this is not one of the FLAT_SOURCES then it will be ignored.
	 *
	 * @see orac.jcmt.SpJCMTConstants#FLAT_SOURCES
	 */
	public void setFlatSource( String flatSource )
	{
		if( FLAT_SOURCES != null )
		{
			for( int i = 0 ; i < FLAT_SOURCES.length ; i++ )
			{
				if( FLAT_SOURCES[ i ].equals( flatSource ) )
					_avTable.set( ATTR_FLAT_SOURCE , FLAT_SOURCES[ i ] ) ;
			}
		}
	}
	
	public void setupForHeterodyne()
	{
		_avTable.noNotifyRm( ATTR_FLAT_SOURCE ) ;
	}

	public void setupForSCUBA2()
	{
		checkSources( SCUBA2_FLAT_SOURCES ) ;
	}
	
	private void checkSources( String[] sources )
	{
		if( sources != null && FLAT_SOURCES != sources )
		{
			_avTable.noNotifyRm( ATTR_FLAT_SOURCE ) ;
			FLAT_SOURCES = sources ;
			_avTable.noNotifySet( ATTR_FLAT_SOURCE , FLAT_SOURCES[ 0 ] , 0 ) ;
		}
	}
	
	public String[] getFlatSources()
	{
		return FLAT_SOURCES ;
	}
}
