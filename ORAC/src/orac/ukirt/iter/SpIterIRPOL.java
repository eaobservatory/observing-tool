/*
 * Copyright 2000 United Kingdom Astronomy Technology Centre, an
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

package orac.ukirt.iter ;

import gemini.sp.SpFactory ;
import gemini.sp.SpItem;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpType ;
import gemini.sp.iter.IterConfigItem ;

import gemini.util.TranslationUtils ;

import java.util.Vector ;
import java.util.Enumeration ;
import java.util.List ;

/**
 * The FP configuration iterator.
 */
@SuppressWarnings( "serial" )
public class SpIterIRPOL extends SpIterConfigObsUKIRT implements SpTranslatable
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "instIRPOL" , "IRPOL" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterIRPOL() ) ;
	}

	// Hardwire the allowed angles, so that they are accessible to all instruments.
	static final String ALLOWED_ANGLES[] = 
	{ 
		"0" , 
		"22.5" , 
		"45" , 
		"67.5" , 
		"90" , 
		"112.5" , 
		"135" , 
		"157.5" , 
		"180.0" , 
		"202.5" , 
		"225.0" , 
		"247.5" , 
		"270.0" , 
		"292.5" , 
		"315.0" , 
		"337.5" 
	} ;

	/**
	 * Default constructor.
	 */
	public SpIterIRPOL()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Get the name of the item being iterated over.  Subclasses must
	 * define.
	 */
	public String getItemName()
	{
		return "IRPOL" ;
	}

	/**
	 * Override adding a configuration item to use the no default version.
	 */
	public void addConfigItem( IterConfigItem ici , int size )
	{
		super.addConfigItemNoDef( ici , size ) ;

		// Then set a default value
		setConfigStep( ici.attribute , ALLOWED_ANGLES[ 0 ] , 0 ) ;
	}

	/**
	 * Get the array containing the IterConfigItems offered by IRPOL.
	 */
	public IterConfigItem[] getAvailableItems()
	{
		// Hardwire the allowed angles.
		IterConfigItem iciWPLAngle = new IterConfigItem( "Waveplate Angle" , "IRPOLIter" , ALLOWED_ANGLES ) ;
		IterConfigItem[] iciA = { iciWPLAngle } ;

		return iciA ;
	}

	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		List<String> iterList = getConfigAttribs() ;
		int nConfigs = getConfigSteps( iterList.get( 0 ) ).size() ;
		for( int i = 0 ; i < nConfigs ; i++ )
		{
			v.add( "polAngle " + getConfigSteps( "IRPOLIter" ).get( i ) ) ;
			Enumeration<SpItem> e = this.children() ;
			TranslationUtils.recurse( e , v ) ;
		}
	}
}
