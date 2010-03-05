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
public class SpAND extends SpObsContextItem
{
	/**
     * Default constructor.
     */
	protected SpAND()
	{
		super( SpType.AND_FOLDER ) ;
	}

	/**
     * Calculates the duration of this AND folder.
     */
	public double getTotalTime()
	{
		double elapsedTime = 0. ;
		Enumeration<SpItem> children = children() ;
		SpItem spItem = null ;

		while( children.hasMoreElements() )
		{
			spItem = children.nextElement() ;

			if( spItem instanceof SpMSB )
			{
				SpMSB msb = ( SpMSB )spItem ;
				if( msb.getNumberRemaining() > 0. )
					elapsedTime += msb.getTotalTime() * msb.getNumberRemaining() ;
			}
			else if( spItem instanceof SpSurveyContainer )
			{
				elapsedTime += ( ( SpSurveyContainer )spItem ).getTotalTime() ;
			}
		}

		return elapsedTime ;
	}

	/**
     * Calculates the duration of this AND folder.
     */
	public double getElapsedTime()
	{
		double elapsedTime = 0. ;
		Enumeration<SpItem> children = children() ;
		SpItem spItem = null ;

		while( children.hasMoreElements() )
		{
			spItem = children.nextElement() ;

			if( spItem instanceof SpMSB )
			{
				SpMSB msb = ( SpMSB )spItem ;
				if( msb.getNumberRemaining() > 0. )
					elapsedTime += ( msb.getElapsedTime() * msb.getNumberRemaining() ) ;
			}
			else if( spItem instanceof SpSurveyContainer )
			{
				elapsedTime += (( SpSurveyContainer )spItem).getElapsedTime() ;
			}
		}
		return elapsedTime ;
	}
}
