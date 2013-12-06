/*
 * Copyright 1999 United Kingdom Astronomy Technology Centre, an
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
import java.util.List ;
import java.util.Enumeration ;

/**
 * The FP configuration iterator.
 */
@SuppressWarnings( "serial" )
public class SpIterFP extends SpIterConfigObsUKIRT implements SpTranslatable
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "instFP" , "FP" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterFP() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterFP()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Get the name of the item being iterated over.  Subclasses must
	 * define.
	 */
	public String getItemName()
	{
		return "FP" ;
	}

	/**
	 * Override adding a configuration item to use the no default version.
	 */
	public void addConfigItem( IterConfigItem ici , int size )
	{
		super.addConfigItemNoDef( ici , size ) ;

		// Then set a default value
		setConfigStep( ici.attribute , "300" , 0 ) ;
	}

	/**
	 * Get the array containing the IterConfigItems offered by FP.
	 */
	public IterConfigItem[] getAvailableItems()
	{
		IterConfigItem iciFPX = new IterConfigItem( "FPXPos" , "FPX" , null ) ;
		IterConfigItem iciFPY = new IterConfigItem( "FPYPos" , "FPY" , null ) ;
		IterConfigItem iciFPZ = new IterConfigItem( "FPZPos" , "FPZ" , null ) ;
		IterConfigItem[] iciA = { iciFPX , iciFPY , iciFPZ } ;

		return iciA ;
	}

	public void translateProlog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translateEpilog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		List<String> l = getConfigAttribs() ;
		if( l != null && l.size() != 0 )
		{
			List<String> vals = getConfigSteps( l.get( 0 ) ) ;
			for( int i = 0 ; i < vals.size() ; i++ )
			{
				for( int j = 0 ; j < l.size() ; j++ )
					v.add( l.get( j ) + " " + getConfigSteps( l.get( j ) ).get( i ) ) ;
				// Now loop through all the child elements
				Enumeration<SpItem> e = this.children() ;
				TranslationUtils.recurse( e , v ) ;
			}
		}
	}
}
