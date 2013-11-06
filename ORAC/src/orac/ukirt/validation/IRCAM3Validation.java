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
import orac.ukirt.inst.SpInstIRCAM3 ;
import orac.validation.InstrumentValidation ;
import orac.validation.ErrorMessage ;
import java.util.Vector ;

/**
 * Implements validation of IRCAM3.
 *
 * @author M.Folger@roe.ac.uk UKATC
 */
public class IRCAM3Validation implements InstrumentValidation
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
		"Standard+NDSTARE" , 
		"Standard+STARE" , 
		"Standard+CHOP" , 
		"Fast+NDSTARE" , 
		"Fast+STARE" , 
		"Fast+CHOP" , 
		"Deepwell+NDSTARE" , 
		"Deepwell+STARE" , 
		"Deepwell+CHOP" 
	} ;

	/**
	 * @see readoutAreas
	 */
	static double[] minExpTimes = { 4 , 20 , 2 , 1 , 5 , 1.3 , 0.3 , 1.3 , 0.15 } ;

	public void checkInstrument( SpInstObsComp instObsComp , Vector<ErrorMessage> report )
	{
		SpInstIRCAM3 spInstIRCAM3 = ( SpInstIRCAM3 )instObsComp ;
		String readoutArea = spInstIRCAM3.getReadoutArea() ;
		String acqMode = spInstIRCAM3.getAcqMode() ;
		double expTime = spInstIRCAM3.getExpTime() ;

		if( report == null )
			report = new Vector<ErrorMessage>() ;

		if( readoutArea.equals( "256x256" ) )
		{
			if( acqMode.startsWith( "Standard+" ) )
			{
				if( expTime < 0.12 )
					report.add( new ErrorMessage( ErrorMessage.ERROR , "IRCAM3" , "Exposure time (with readout area: \"" + readoutArea + "\", acquisition mode: \"" + acqMode + "\")" , ">= 0.12 sec" , expTime + " sec" ) ) ;
			}
			// acqMode starts with "Fast+" or "Deepwell+"
			else
			{
				if( expTime < 0.072 )
					report.add( new ErrorMessage( ErrorMessage.ERROR , "IRCAM3" , "Exposure time (with readout area: \"" + readoutArea + "\", acquisition mode: \"" + acqMode + "\")" , ">= 0.072 sec" , expTime + " sec" ) ) ;
			}
		}

		if( readoutArea.equals( "128x128" ) )
		{
			if( acqMode.startsWith( "Standard+" ) )
			{
				if( expTime < 0.036 )
					report.add( new ErrorMessage( ErrorMessage.ERROR , "IRCAM3" , "Exposure time (with readout area: \"" + readoutArea + "\", acquisition mode: \"" + acqMode + "\")" , ">= 0.036 sec" , expTime + " sec" ) ) ;
			}
			// acqMode starts with "Fast+" or "Deepwell+"
			else
			{
				if( expTime < 0.021 )
					report.add( new ErrorMessage( ErrorMessage.ERROR , "IRCAM3" , "Exposure time (with readout area: \"" + readoutArea + "\", acquisition mode: \"" + acqMode + "\")" , ">= 0.021 sec" , expTime + " sec" ) ) ;
			}
		}
	}
}
