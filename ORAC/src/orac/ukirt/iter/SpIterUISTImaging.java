/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.ukirt.iter ;

import orac.ukirt.inst.SpInstUIST ;

import gemini.sp.SpFactory ;
import gemini.sp.SpItem;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpType ;
import gemini.sp.iter.IterConfigItem ;

import gemini.util.ConfigWriter ;
import gemini.util.TranslationUtils ;

import java.util.Vector ;
import java.util.Hashtable ;
import java.util.List ;
import java.util.Enumeration ;

/**
 * The UIST configuration iterator (Imaging).
 */
@SuppressWarnings( "serial" )
public class SpIterUISTImaging extends SpIterConfigObsUKIRT implements SpTranslatable
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "instUISTImaging" , "UIST Imaging" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterUISTImaging() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterUISTImaging()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Try overriding the method from SpIterConfigBase to add set
	 * of the instrument-aperture information when it changes.
	 * Set the steps of the item iterator of the given attribute.
	 */
	public void setConfigStep( String attribute , String value , int index )
	{
		_avTable.set( attribute , value , index ) ;
	}

	/**
	 * Get the name of the item being iterated over.
	 */
	public String getItemName()
	{
		return "UIST Imaging" ;
	}

	/**
	 * Get the array containing the IterConfigItems offered by UIST.
	 */
	public IterConfigItem[] getAvailableItems()
	{
		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		boolean instAvailable = inst != null ;

		IterConfigItem iciFilter ;
		if( instAvailable )
		{
			String filtersBroadband[] = inst.getFilterList( true ) ;
			String filtersNarrowband[] = inst.getFilterList( false ) ;
			String filters[] = new String[ filtersBroadband.length + filtersNarrowband.length ] ;
			for( int i = 0 ; i < filtersBroadband.length ; i++ )
				filters[ i ] = filtersBroadband[ i ] ;
			
			for( int i = 0 ; i < filtersNarrowband.length ; i++ )
				filters[ filtersBroadband.length + i ] = filtersNarrowband[ i ] ;

			// Filters.
			iciFilter = new IterConfigItem( "Filter" , SpInstUIST.ATTR_FILTER + "Iter" , filters ) ;
		}
		else
		{
			Vector<String> vFilters = SpInstUIST.FILTERS.getColumn( 0 ) ;
			int n = vFilters.size() ;
			String[] filters = new String[ n ] ;
			for( int i = 0 ; i < n ; i++ )
				filters[i] = vFilters.elementAt(i);

			// Filters.
			iciFilter = new IterConfigItem( "Filter" , SpInstUIST.ATTR_FILTER + "Iter" , filters ) ;
		}

		// Specify configuration items which can be iterated. 
		IterConfigItem[] iciA = { iciFilter , getExposureTimeConfigItem() , getCoaddsConfigItem() } ;
		return iciA ;
	}

	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		SpInstUIST inst ;
		try
		{
			inst = ( SpInstUIST )SpTreeMan.findInstrument( this ) ;
		}
		catch( Exception e )
		{
			throw new SpTranslationNotSupportedException( "No UIST instrument in scope" ) ;
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
				if( iterList.contains( "filterIter" ) )
					defaultsTable.put( "filter" , getConfigSteps( "filterIter" ).get( i ) ) ;

				if( iterList.contains( "exposureTimeIter" ) )
					defaultsTable.put( "exposureTime" , getConfigSteps( "exposureTimeIter" ).get( i ) ) ;

				if( iterList.contains( "coaddsIter" ) )
					defaultsTable.put( "coadds" , getConfigSteps( "coaddsIter" ).get( i ) ) ;

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
				throw new SpTranslationNotSupportedException( "Unable to write config file for UISTImaging iterator:" + e.getMessage() ) ;
			}
			
			v.add( "loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName() ) ;
			v.add( "define_inst UIST " + xAper + yAper + zAper + lAper ) ;
			Enumeration<SpItem> e = this.children() ;
			
			TranslationUtils.recurse( e , v ) ;
		}
	}
}
