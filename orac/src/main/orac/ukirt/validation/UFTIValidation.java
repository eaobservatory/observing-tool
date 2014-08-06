/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package orac.ukirt.validation ;

import gemini.sp.obsComp.SpInstObsComp ;
import orac.ukirt.inst.SpInstUFTI ;
import orac.validation.InstrumentValidation ;
import orac.validation.ErrorMessage ;
import orac.validation.SpValidation;

import java.util.Vector ;

/**
 * Implements the validation of UFTI.
 * 
 * @author M.Folger@roe.ac.uk
 */
public class UFTIValidation implements InstrumentValidation
{

	/**
	 * readoutAreas, acqModes and minExpTimes are used as follows:
	 * Given readoutAreas[i] and acqModes[i] check whether the exposure time is at least minExpTimes[i].
	 *
	 * @see acqModes
	 * @see minExpTimes
	 */
	static String[] readoutAreas = 
	{ 
		"1024x1024" , 
		"1024x1024" , 
		"1024x1024" , 
		"512x512" , 
		"512x512" , 
		"512x512" , 
		"256x256" , 
		"256x256" , 
		"256x256" 
	} ;

	/**
	 * @see readoutAreas
	 */
	static String[] acqModes = 
	{ 
		"Normal+NDSTARE" , 
		"Normal+10_NDSTARE" , 
		"Fast+NDSTARE" , 
		"Normal+NDSTARE" , 
		"Normal+10_NDSTARE" , 
		"Fast+NDSTARE" , 
		"Normal+NDSTARE" , 
		"Normal+10_NDSTARE" , 
		"Fast+NDSTARE" 
	} ;

	/**
	 * @see readoutAreas
	 */
	static double[] minExpTimes = { 4 , 20 , 2 , 1 , 5 , 1.3 , 0.3 , 1.3 , 0.15 } ;

	public void checkInstrument( SpInstObsComp instObsComp , Vector<ErrorMessage> report )
	{
		String titleString = SpValidation.titleString( instObsComp ) ;
		if( "".equals( titleString ) )
			titleString = "UFTI" ;
		else
			titleString = "UFTI in " + titleString ;
		
		SpInstUFTI spInstUFTI = ( SpInstUFTI )instObsComp ;
		String readoutArea = spInstUFTI.getReadoutArea() ;
		String acqMode = spInstUFTI.getAcqMode() ;
		double expTime = spInstUFTI.getExpTime() ;

		if( report == null )
			report = new Vector<ErrorMessage>() ;

		double[] expLimits = spInstUFTI.getExpTimeLimits() ;
		if( expTime < expLimits[ 0 ] || expTime > expLimits[ 1 ] )
		{
			String message = "Exposure time of (" + expTime + ") out of limits for current combination " + "of readout area (" + readoutArea + ") and mode (" + acqMode + ").  Valid limits are " + "(" + expLimits[ 0 ] + ", " + expLimits[ 1 ] + ")." ;
			report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , message ) ) ;
		}
	}
}
