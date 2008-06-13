/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.ukirt.iter ;

import java.util.Hashtable ;
import java.util.Vector ;
import java.util.List ;
import java.util.Enumeration ;

import orac.ukirt.inst.SpInstCGS4 ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpTreeMan ;
import gemini.sp.iter.IterConfigItem ;

import gemini.util.ConfigWriter ;
import gemini.util.TranslationUtils ;

/**
 * The CGS4 configuration iterator.
 */
public class SpIterCGS4 extends SpIterConfigObsUKIRT implements SpTranslatable
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "instCGS4" , "CGS4" ) ;
	private IterConfigItem iciInstAperL ;
	private Hashtable _myTable = null ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterCGS4() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterCGS4()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Get the name of the item being iterated over.  Subclasses must
	 * define.
	 */
	public String getItemName()
	{
		return "CGS4" ;
	}

	/**
	 * Override adding a configuration item to the set - to add instrument
	 * aperture items.
	 */
	public void addConfigItem( IterConfigItem ici , int size )
	{
		if( ici.attribute.equals( SpInstCGS4.ATTR_CENTRAL_WAVELENGTH + "Iter" ) )
			super.addConfigItemNoDef( iciInstAperL , size ) ;

		super.addConfigItem( ici , size ) ;
	}

	/**
	 * Override deleting  a configuration item to the set to remove the
	 * inst aper attributes   too.
	 */
	public void deleteConfigItem( String attribute )
	{
		super.deleteConfigItem( attribute ) ;

		if( attribute.equals( SpInstCGS4.ATTR_CENTRAL_WAVELENGTH + "Iter" ) )
			super.deleteConfigItem( iciInstAperL.attribute ) ;
	}

	/**
	 * Set the steps of the item iterator of the given attribute.
	 * Overriding the method from SpIterConfigBase to add set of the
	 * inst aper info when it changes.
	 */
	public void setConfigStep( String attribute , String value , int index )
	{
		_avTable.set( attribute , value , index ) ;

		// If central wavelength then set L inst aper to same.
		if( attribute.equals( SpInstCGS4.ATTR_CENTRAL_WAVELENGTH + "Iter" ) )
			_avTable.set( SpInstCGS4.ATTR_INSTRUMENT_APER + "LIter" , value , index ) ;
	}

	/**
	 * Get the array containing the IterConfigItems offered by CGS4.
	 */
	public IterConfigItem[] getAvailableItems()
	{
		IterConfigItem iciMode = new IterConfigItem( "Mode" , SpInstCGS4.ATTR_MODE + "Iter" , SpInstCGS4.MODES ) ;
		iciInstAperL = new IterConfigItem( "InstAperL" , SpInstCGS4.ATTR_INSTRUMENT_APER + "LIter" , null ) ;
		IterConfigItem[] iciA = { iciMode , getExposureTimeConfigItem() , getCoaddsConfigItem() , iciInstAperL } ;

		return iciA ;
	}

	public void translateProlog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translateEpilog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		// Make sure we have a valid instrument
		SpInstCGS4 inst ;
		try
		{
			inst = ( SpInstCGS4 )SpTreeMan.findInstrument( this ) ;
		}
		catch( Exception e )
		{
			throw new SpTranslationNotSupportedException( "No CGS4 instrument in scope" ) ;
		}

		List iterList = getConfigAttribs() ;
		int nConfigs = getConfigSteps( ( String )iterList.get( 0 ) ).size() ;
		for( int i = 0 ; i < nConfigs ; i++ )
		{
			_myTable = inst.getConfigItems() ;
			for( int j = 0 ; j < iterList.size() ; j++ )
			{
				if( iterList.contains( "exposureTimeIter" ) )
					_myTable.put( "expTime" , ( String )getConfigSteps( "exposureTimeIter" ).get( i ) ) ;
				if( iterList.contains( "coaddsIter" ) )
					_myTable.put( "objNumExp" , ( String )getConfigSteps( "coaddsIter" ).get( i ) ) ;
				if( iterList.contains( "acqModeIter" ) )
					_myTable.put( "readMode" , ( String )getConfigSteps( "acqModeIter" ).get( i ) ) ;
				if( iterList.contains( "instAperXIter" ) )
					_myTable.put( "instAperX" , ( String )getConfigSteps( "instAperXIter" ).get( i ) ) ;
				if( iterList.contains( "instAperYIter" ) )
					_myTable.put( "instAperY" , ( String )getConfigSteps( "instAperYIter" ).get( i ) ) ;
				if( iterList.contains( "instAperZIter" ) )
					_myTable.put( "instAperZ" , ( String )getConfigSteps( "instAperZIter" ).get( i ) ) ;
				if( iterList.contains( "instAperLIter" ) )
				{
					_myTable.put( "instAperL" , ( String )getConfigSteps( "instAperLIter" ).get( i ) ) ;
					_myTable.put( "centralWavelength" , ( String )getConfigSteps( "instAperLIter" ).get( i ) ) ;
				}

				try
				{
					ConfigWriter.getCurrentInstance().write( _myTable ) ;
				}
				catch( Exception e )
				{
					throw new SpTranslationNotSupportedException( "Unable to write config file for CGS4 iterator:" + e.getMessage() ) ;
				}
				
				v.add( "loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName() ) ;

				// translate all the cildren...
				Enumeration e = this.children() ;
				TranslationUtils.recurse( e , v ) ;
			}
		}
	}

	/**
	 * Gets the hashtable created by this iterator
	 */
	public Hashtable getIterTable()
	{
		Hashtable clone = null ;
		if( _myTable != null )
			clone = new Hashtable( _myTable ) ;

		return clone ;
	}
}
