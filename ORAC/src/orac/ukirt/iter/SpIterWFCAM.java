/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                 Copyright (c) PPARC 1999-2003                */
/*                                                              */
/*==============================================================*/

package orac.ukirt.iter ;

import orac.ukirt.inst.SpInstWFCAM ;

import gemini.util.ConfigWriter ;

import gemini.sp.SpFactory ;
import gemini.sp.SpItem;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpType ;
import gemini.sp.iter.IterConfigItem ;

import java.util.List ;
import java.util.Enumeration ;
import java.util.Hashtable ;
import java.util.Vector ;

import gemini.util.TranslationUtils ;

/**
 * The WFCAM configuration iterator.
 */
@SuppressWarnings( "serial" )
public class SpIterWFCAM extends SpIterConfigObsUKIRT implements SpTranslatable
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "instWFCAM" , "WFCAM" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterWFCAM() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterWFCAM()
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
		return v ;
	}

	/**
	 * Override adding a configuration item to the set to add the instrument
	 * aperture items.
	 */
	public void addConfigItem( IterConfigItem ici , int size )
	{
		super.addConfigItem( ici , size ) ;
	}

	/**
	 * Override deleting a configuration item to the set in order
	 * to remove the instrument-aperture attributes too.
	 */
	public void deleteConfigItem( String attribute )
	{
		super.deleteConfigItem( attribute ) ;
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
		return "WFCAM" ;
	}

	/**
	 * Get the array containing the IterConfigItems offered by WFCAM.
	 */
	public IterConfigItem[] getAvailableItems()
	{
		SpInstWFCAM inst = ( SpInstWFCAM )getInstrumentItem() ;
		boolean instAvailable = inst != null ;

		IterConfigItem iciFilter ;
		if( instAvailable )
		{
			String[] filters = inst.getFilterList() ;
			// Filters.
			iciFilter = new IterConfigItem( "Filter" , SpInstWFCAM.ATTR_FILTER + "Iter" , filters ) ;
		}
		else
		{
			Vector<String> vFilters = SpInstWFCAM.FILTERS.getColumn( 0 ) ;
			int n = vFilters.size() ;
			String[] filters = new String[ n ] ;
			for( int i = 0 ; i < n ; i++ )
				filters[i] = vFilters.elementAt(i);

			// Filters.
			iciFilter = new IterConfigItem( "Filter" , SpInstWFCAM.ATTR_FILTER + "Iter" , filters ) ;
		}

		IterConfigItem iciReadMode ;
		if( instAvailable )
		{
			String[] readmodes = inst.getReadModeList() ;
			iciReadMode = new IterConfigItem( "ReadMode" , SpInstWFCAM.ATTR_READMODE + "Iter" , readmodes ) ;
		}
		else
		{
			String[] readmodes = SpInstWFCAM.READMODES ;
			iciReadMode = new IterConfigItem( "ReadMode" , SpInstWFCAM.ATTR_READMODE + "Iter" , readmodes ) ;
		}

		// Specify configuration items which can be iterated. 
		IterConfigItem[] iciA = { iciReadMode , iciFilter , getExposureTimeConfigItem() , getCoaddsConfigItem() } ;
		return iciA ;
	}

	public Hashtable<String,String> getIterTable() throws SpTranslationNotSupportedException
	{
		SpInstWFCAM inst ;
		Hashtable<String,String> configTable = new Hashtable<String,String>() ;
		try
		{
			inst = ( SpInstWFCAM )SpTreeMan.findInstrument( this ) ;
		}
		catch( Exception e )
		{
			throw new SpTranslationNotSupportedException( "No WFCAM instrument is scope" ) ;
		}

		List<String> iterList = getConfigAttribs() ;
		configTable = inst.getConfigItems() ;
		for( int j = 0 ; j < iterList.size() ; j++ )
		{
			if( iterList.contains( "filterIter" ) )
				configTable.put( "filter" , getConfigStep( "filterIter" , step ) ) ;

			if( iterList.contains( "readModeIter" ) )
				configTable.put( "readMode" , getConfigStep( "readModeIter" , step ) ) ;

			if( iterList.contains( "exposureTimeIter" ) )
				configTable.put( "exposureTime" , getConfigStep( "exposureTimeIter" , step ) ) ;

			if( iterList.contains( "coaddsIter" ) )
				configTable.put( "coadds" , getConfigStep( "coaddsIter" , step ) ) ;

			if( iterList.contains( "instAperXIter" ) )
				configTable.put( "instAperX" , getConfigStep( "instAperXIter" , step ) ) ;

			if( iterList.contains( "instAperYIter" ) )
				configTable.put( "instAperY" , getConfigStep( "instAperYIter" , step ) ) ;

			if( iterList.contains( "instAperZIter" ) )
				configTable.put( "instAperZ" , getConfigStep( "instAperZIter" , step ) ) ;

			if( iterList.contains( "instAperLIter" ) )
				configTable.put( "instAperL" , getConfigStep( "instAperLIter" , step ) ) ;
		}
		return configTable ;
	}

	private int step = 0 ;
	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		SpInstWFCAM inst ;
		try
		{
			inst = ( SpInstWFCAM )SpTreeMan.findInstrument( this ) ;
		}
		catch( Exception e )
		{
			throw new SpTranslationNotSupportedException( "No WFCAM instrument is scope" ) ;
		}

		List<String> iterList = getConfigAttribs() ;
		int nConfigs = getConfigSteps( iterList.get( 0 ) ).size() ;
		for( step = 0 ; step < nConfigs ; step++ )
		{
			Hashtable<String,String> configTable = inst.getConfigItems() ;
			Hashtable<String,String> localConfigs = getIterTable() ;
			configTable.putAll( localConfigs ) ;

			String xAper = " " + configTable.get( "instAperX" ) ;
			String yAper = " " + configTable.get( "instAperY" ) ;
			String zAper = " " + configTable.get( "instAperZ" ) ;
			String lAper = " " + configTable.get( "instAperL" ) ;

			try
			{
				ConfigWriter.getCurrentInstance().write( configTable ) ;
			}
			catch( Exception e )
			{
				throw new SpTranslationNotSupportedException( "Unable to write config file for WFCAM iterator:" + e.getMessage() ) ;
			}
			v.add( "loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName() ) ;
			v.add( "define_inst " + getItemName() + xAper + yAper + zAper + lAper ) ;

			// translate all the children...
			Enumeration<SpItem> e = this.children() ;
			TranslationUtils.recurse( e , v ) ;
		}
	}
}
