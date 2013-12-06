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

import orac.ukirt.inst.SpInstUFTI ;
import orac.util.LookUpTable ;

import gemini.util.ConfigWriter ;
import gemini.util.TranslationUtils ;

import gemini.sp.SpFactory ;
import gemini.sp.SpItem;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpType ;
import gemini.sp.iter.IterConfigItem ;

import java.util.Vector ;
import java.util.Hashtable ;
import java.util.Enumeration ;
import java.util.List ;

/**
 * The UFTI configuration iterator.
 */
@SuppressWarnings( "serial" )
public class SpIterUFTI extends SpIterConfigObsUKIRT implements SpTranslatable
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "instUFTI" , "UFTI" ) ;

	private IterConfigItem iciInstAperX ;
	private IterConfigItem iciInstAperY ;
	private IterConfigItem iciInstAperL ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterUFTI() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterUFTI()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Override "getConfigAttribs" to fix up old programs with the wrong
	 * attribute names.
	 */
	public Vector<String> getConfigAttribs()
	{
		Vector<String> v = super.getConfigAttribs() ;

		if( v == null )
			return null ;

		// Change the old attributes to the new ones.
		for( int i = 0 ; i < v.size() ; ++i )
		{
			String attr = v.elementAt( i ) ;
			boolean change = false ;

			String newAttr = null ;
			Vector<String> values = null ;
			if( attr.equals( "integrationTimeIter" ) )
			{
				change = true ;
				newAttr = SpInstUFTI.ATTR_EXPOSURE_TIME + "Iter" ;
				values = _avTable.getAll( attr ) ;
			}
			else if( attr.equals( "areaIter" ) )
			{
				change = true ;
				newAttr = SpInstUFTI.ATTR_READAREA + "Iter" ;
				values = _avTable.getAll( attr ) ;
			}

			if( change )
			{
				v.setElementAt( newAttr , i ) ;
				_avTable.rm( attr ) ;
				_avTable.setAll( newAttr , values ) ;
			}
		}

		return v ;
	}

	/**
	 * Override adding a configuration item to the set to add the instrument
	 * aperture items.
	 */
	public void addConfigItem( IterConfigItem ici , int size )
	{
		if( ici.attribute.equals( "readoutAreaIter" ) )
		{
			super.addConfigItemNoDef( iciInstAperX , size ) ;
			super.addConfigItemNoDef( iciInstAperY , size ) ;
		}
		else if( ici.attribute.equals( "filterIter" ) )
		{
			super.addConfigItemNoDef( iciInstAperL , size ) ;
		}

		super.addConfigItem( ici , size ) ;
	}

	/**
	 * Override deleting a configuration item to the set in order
	 * to remove the instrument-aperture attributes too.
	 */
	public void deleteConfigItem( String attribute )
	{
		super.deleteConfigItem( attribute ) ;
		if( attribute.equals( "readoutAreaIter" ) )
		{
			super.deleteConfigItem( iciInstAperX.attribute ) ;
			super.deleteConfigItem( iciInstAperY.attribute ) ;
		}
		else if( attribute.equals( "filterIter" ) )
		{
			super.deleteConfigItem( iciInstAperL.attribute ) ;
		}
	}

	/**
	 * Try overriding the method from SpIterConfigBase to add set
	 * of the instrument-aperture information when it changes.
	 * Set the steps of the item iterator of the given attribute.
	 */
	public void setConfigStep( String attribute , String value , int index )
	{

		_avTable.set( attribute , value , index ) ;

		if( attribute.equals( "readoutAreaIter" ) )
		{
			String[] split = value.split( "x" ) ;
			int xsize = Integer.parseInt( split[ 0 ] ) ;
			int ysize = Integer.parseInt( split[ 1 ] ) ;

			// I really hate doing this but...
			double xo = .998 ;
			double yo = -0.62 ;
			
			if( xsize == 512 )
				xo = -11.83 ;
			else if( xsize == 256 )
				xo = -4.22 ;
			
			if( ysize == 512 )
				yo = 16.83 ;
			else if( ysize == 256 )
				yo = 12.85 ;

			// These value are in millimeters - what will go to the telescope.
			_avTable.set( SpInstUFTI.ATTR_INSTRUMENT_APER + "XIter" , Double.toString( xo ) , index ) ;
			_avTable.set( SpInstUFTI.ATTR_INSTRUMENT_APER + "YIter" , Double.toString( yo ) , index ) ;
		}
		else if( attribute.equals( "filterIter" ) )
		{
			// Find the relevant filter table and extract the lambda info
			int filtind = -1 ;
			LookUpTable farray = null ;

			// Check for invalid filter value
			if( value == null || value.equals( "None" ) )
			{
				farray = SpInstUFTI.BROAD_BAND_FILTERS ;
				_avTable.set( SpInstUFTI.ATTR_INSTRUMENT_APER + "LIter" , "1.0" , index ) ;
			}
			else
			{
				// Value is ok. Find the relevant LUT
				farray = SpInstUFTI.BROAD_BAND_FILTERS ;
				try
				{
					filtind = farray.indexInColumn( value , 0 ) ;
				}
				catch( Exception ex1 )
				{
					farray = SpInstUFTI.NARROW_BAND_FILTERS ;
					try
					{
						filtind = farray.indexInColumn( value , 0 ) ;
					}
					catch( Exception ex2 )
					{
						farray = SpInstUFTI.SPECIAL_FILTERS ;
						try
						{
							filtind = farray.indexInColumn( value , 0 ) ;
						}
						catch( Exception ex3 )
						{
							System.out.println( "SpIterUFTI: Failed to find filter anywhere!" ) ;
							_avTable.set( SpInstUFTI.ATTR_INSTRUMENT_APER + "LIter" , "1.0" , index ) ;
							return ;
						}
					}
				}
				_avTable.set( SpInstUFTI.ATTR_INSTRUMENT_APER + "LIter" , farray.elementAt( filtind , 1 ) , index ) ;
			}
		}
	}

	/**
	 * Get the name of the item being iterated over.
	 */
	public String getItemName()
	{
		return "UFTI" ;
	}

	/**
	 * Get the array containing the IterConfigItems offered by UFTI.
	 */
	public IterConfigItem[] getAvailableItems()
	{

		// Acquistion Mode
		IterConfigItem iciAcqMode = new IterConfigItem( "Acqmode" , SpInstUFTI.ATTR_MODE + "Iter" , SpInstUFTI.MODES ) ;

		// Readout area.  This is now a lookup table, so extract the first column, and put the areas into an array.
		Vector<String> vRA = SpInstUFTI.READAREAS.getColumn( 0 ) ;

		String[] readoutAreas = new String[ vRA.size() ] ;
		for( int i = 0 ; i < vRA.size() ; ++i )
			readoutAreas[i] = vRA.elementAt(i);

		IterConfigItem iciReadoutArea = new IterConfigItem( "ReadoutArea" , SpInstUFTI.ATTR_READAREA + "Iter" , readoutAreas ) ;

		// Filters.
		Vector<String> vBB = SpInstUFTI.BROAD_BAND_FILTERS.getColumn( 0 ) ;
		Vector<String> vNB = SpInstUFTI.NARROW_BAND_FILTERS.getColumn( 0 ) ;
		Vector<String> vSP = SpInstUFTI.SPECIAL_FILTERS.getColumn( 0 ) ;

		// Concatenate filters to form a master array.
		int n = vBB.size() + vNB.size() + vSP.size() ;
		String[] filters = new String[ n ] ;
		for( int i = 0 ; i < vBB.size() ; ++i )
			filters[i] = vBB.elementAt(i);

		int offset = vBB.size() ;
		for( int i = 0 ; i < vNB.size() ; ++i )
			filters[offset + i] = vNB.elementAt(i);

		offset += vNB.size() ;
		for( int i = 0 ; i < vSP.size() ; ++i )
			filters[offset + i] = vSP.elementAt(i);

		IterConfigItem iciFilter = new IterConfigItem( "Filter" , SpInstUFTI.ATTR_FILTER + "Iter" , filters ) ;

		// Instrument aperture stuff.  Will need to hide...
		iciInstAperX = new IterConfigItem( "InstAperX" , SpInstUFTI.ATTR_INSTRUMENT_APER + "XIter" , null ) ;
		iciInstAperY = new IterConfigItem( "InstAperY" , SpInstUFTI.ATTR_INSTRUMENT_APER + "YIter" , null ) ;
		iciInstAperL = new IterConfigItem( "InstAperL" , SpInstUFTI.ATTR_INSTRUMENT_APER + "LIter" , null ) ;

		// Specify configuration items which can be iterated. 
		IterConfigItem[] iciA = { iciAcqMode , iciReadoutArea , iciFilter , getExposureTimeConfigItem() , getCoaddsConfigItem() , iciInstAperX , iciInstAperY , iciInstAperL } ;

		return iciA ;
	}

	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		// First get the available items and set the defaults after making sure an instrument of the right type is available
		SpInstUFTI inst ;
		boolean isPol = false ;
		try
		{
			inst = ( SpInstUFTI )SpTreeMan.findInstrument( this ) ;
			isPol = ( inst.getPolariser().equals( "prism" ) ) ;
		}
		catch( Exception npe )
		{
			throw new SpTranslationNotSupportedException( "No UFTI instrument in scope" ) ;
		}

		List<String> iterList = getConfigAttribs() ;
		int nConfigs = getConfigSteps( iterList.get( 0 ) ).size() ;
		for( int i = 0 ; i < nConfigs ; i++ )
		{
			Hashtable<String,String> defaultsTable = inst.getConfigItems() ;
			String xAper = " " + defaultsTable.get( "instAperX" ) ;
			String yAper = " " + defaultsTable.get( "instAperY" ) ;
			String zAper = " " + defaultsTable.get( "instAperZ" ) ;
			String lAper = " " + defaultsTable.get( "instAperL" ) ;
			for( int j = 0 ; j < iterList.size() ; j++ )
			{
				// Loop over each of there writing a new config file
				if( iterList.contains( "filterIter" ) )
				{
					String filter = getConfigSteps( "filterIter" ).get( i ) ;
					if( isPol )
						filter = filter + "+pol" ;
					defaultsTable.put( "filter" , filter ) ;
				}
				
				if( iterList.contains( "acqModeIter" ) )
					defaultsTable.put( "readMode" , getConfigSteps( "acqModeIter" ).get( i ) ) ;

				if( iterList.contains( "readoutAreaIter" ) )
					defaultsTable.put( "readArea" , getConfigSteps( "readoutAreaIter" ).get( i ) ) ;

				if( iterList.contains( "exposureTimeIter" ) )
					defaultsTable.put( "expTime" , getConfigSteps( "exposureTimeIter" ).get( i ) ) ;

				if( iterList.contains( "coaddsIter" ) )
					defaultsTable.put( "objNumExp" , getConfigSteps( "coaddsIter" ).get( i ) ) ;

				if( iterList.contains( "instAperXIter" ) )
					xAper = " " + getConfigSteps( "instAperXIter" ).get( i ) ;

				if( iterList.contains( "instAperYIter" ) )
					yAper = " " + getConfigSteps( "instAperYIter" ).get( i ) ;

				if( iterList.contains( "instAperZIter" ) )
					zAper = " " + getConfigSteps( "instAperZIter" ).get( i ) ;

				if( iterList.contains( "instAperLIter" ) )
					lAper = " " + getConfigSteps( "instAperLIter" ).get( i ) ;
			}
			try
			{
				ConfigWriter.getCurrentInstance().write( defaultsTable ) ;
			}
			catch( Exception e )
			{
				throw new SpTranslationNotSupportedException( "Unable to write config file for UFTI iterator:" + e.getMessage() ) ;
			}

			v.add( "loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName() ) ;
			v.add( "define_inst " + getItemName() + xAper + yAper + zAper + lAper ) ;

			Enumeration<SpItem> e = this.children() ;
			TranslationUtils.recurse( e , v ) ;
		}
	}
}
