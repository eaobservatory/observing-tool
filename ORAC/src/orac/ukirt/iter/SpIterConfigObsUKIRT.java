// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter ;

import gemini.sp.SpType ;
import gemini.sp.SpItem ;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpInstObsComp ;
import gemini.sp.iter.SpIterConfigObs ;
import gemini.sp.iter.IterConfigItem ;
import orac.ukirt.inst.SpUKIRTInstObsComp ;
import java.io.File ;
import java.util.Vector ;

/**
 * This class extends the SpIterConfigObs class and adds some UKIRT
 * specific items and overrides.
 */
@SuppressWarnings( "serial" )
public abstract class SpIterConfigObsUKIRT extends SpIterConfigObs implements SpTranslatable
{

	public SpIterConfigObsUKIRT( SpType spType )
	{
		super( spType ) ;
	}

	/**
	 * Get the instrument item in the scope of the base item.
	 */
	public SpInstObsComp getInstrumentItem()
	{
		SpItem _baseItem = parent() ;

		// Added by RDK
		if( _baseItem == null )
			return null ;
		else
			return SpTreeMan.findInstrument( _baseItem ) ;
		//End of Added by RDK
	}

	/**
	 * Insert a new (blank) step at the given index.
	 */
	public void insertConfigStep( int index )
	{
		super.insertConfigStep( index ) ;
		Vector<String> v = getConfigAttribs() ;
		for( int i = 0 ; i < v.size() ; ++i )
		{
			String a = v.elementAt( i ) ;
			String defval = "" ;
			if( index > 0 )
				defval = getConfigStep( a , index - 1 ) ;
			setConfigStep( a , defval , index ) ;
		}
	}

	/**
	 * Overriding adding a configuration item to the set to default values
	 */
	public void addConfigItem( IterConfigItem ici , int size )
	{

		super.addConfigItem( ici , size ) ;

		// Get default from the associated instrument
		SpInstObsComp _inst = getInstrumentItem() ;
		String defval = "" ;
		int l = ici.attribute.length() ;
		defval = _inst.getTable().get( ici.attribute.substring( 0 , l - 4 ) ) ;

		if( size == 0 )
		{
			setConfigStep( ici.attribute , defval , 0 ) ;
		}
		else
		{
			for( int i = 0 ; i < size ; i++ )
				setConfigStep( ici.attribute , defval , i ) ;
		}

	}

	/**
	 * Adding an item with no default
	 */
	public void addConfigItemNoDef( IterConfigItem ici , int size )
	{
		super.addConfigItem( ici , size ) ;
	}

	public void translateProlog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translateEpilog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		String confDir = System.getProperty( "CONF_PATH" ) ;
		if( confDir == null || confDir.equals( "" ) || !( new File( confDir ).exists() ) )
			confDir = System.getProperty( "user.home" ) ;

		// Get default values from the instrument
		SpInstObsComp inst = SpTreeMan.findInstrument( this ) ;
		if( inst == null || !( inst instanceof SpUKIRTInstObsComp ) )
			throw new SpTranslationNotSupportedException( "No instrument, or not a UKIRT instrument" ) ;

		getAvailableItems() ;
	}
}
