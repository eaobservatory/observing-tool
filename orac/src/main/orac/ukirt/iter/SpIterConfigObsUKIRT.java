// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
