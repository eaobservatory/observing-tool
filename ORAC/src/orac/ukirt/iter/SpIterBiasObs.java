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

import gemini.util.ConfigWriter ;

import gemini.sp.SpItem ;
import gemini.sp.SpFactory ;
import gemini.sp.SpMSB ;
import gemini.sp.SpType ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;

import gemini.sp.iter.SpIterEnumeration ;
import gemini.sp.iter.SpIterObserveBase ;
import gemini.sp.iter.SpIterStep ;
import gemini.sp.iter.SpIterValue ;

import gemini.sp.obsComp.SpInstConstants ;
import gemini.sp.obsComp.SpInstObsComp ;

import java.io.IOException ;

import java.util.Hashtable ;
import java.util.Vector ;

import orac.ukirt.inst.SpInstCGS4 ;
import orac.ukirt.inst.SpInstWFCAM ;
import orac.ukirt.inst.SpDRRecipe ;
import orac.ukirt.inst.SpUKIRTInstObsComp ;

/**
 * Enumerater for the elements of the Observe iterator.
 */
@SuppressWarnings( "serial" )
class SpIterBiasObsEnumeration extends SpIterEnumeration
{
	private int _curCount = 0 ;
	private int _maxCount ;
	private SpIterValue[] _values ;

	SpIterBiasObsEnumeration( SpIterBiasObs iterObserve )
	{
		super( iterObserve ) ;
		_maxCount = iterObserve.getCount() ;
	}

	protected boolean _thisHasMoreElements()
	{
		return( _curCount < _maxCount ) ;
	}

	protected SpIterStep _thisFirstElement()
	{
		SpIterBiasObs ibo = ( SpIterBiasObs )_iterComp ;
		String expTimeValue = String.valueOf( ibo.getExposureTime() ) ;
		String coaddsValue = String.valueOf( ibo.getCoadds() ) ;

		_values = new SpIterValue[ 2 ] ;
		_values[ 0 ] = new SpIterValue( SpInstConstants.ATTR_EXPOSURE_TIME , expTimeValue ) ;
		_values[ 1 ] = new SpIterValue( SpInstConstants.ATTR_COADDS , coaddsValue ) ;

		return _thisNextElement() ;
	}

	protected SpIterStep _thisNextElement()
	{
		return new SpIterStep( "bias" , _curCount++ , _iterComp , _values ) ;
	}

}

@SuppressWarnings( "serial" )
public class SpIterBiasObs extends SpIterObserveBase implements SpTranslatable
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "biasObs" , "Bias" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterBiasObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterBiasObs()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Override getTitle to return the observe count.
	 */
	public String getTitle()
	{
		String title ;
		
		if( getTitleAttr() != null )
			title = super.getTitle() ;
		else
			title = "Bias (" + getCount() + "X)" ;
		
		return title ;
	}

	/**
	 */
	public SpIterEnumeration elements()
	{
		return new SpIterBiasObsEnumeration( this ) ;
	}

	/**
	 * Override getExposureTime. Get the value from the instrument in
	 * scope.
	 */
	public double getExposureTime()
	{
		double exposureTime = 0. ;
		SpItem _baseItem = parent() ;
		SpUKIRTInstObsComp spi = ( SpUKIRTInstObsComp )SpTreeMan.findInstrument( _baseItem ) ;
		if( spi != null )
			exposureTime = spi.getDefaultBiasExpTime() ;
		else
			throw new RuntimeException( "No instrument in scope" ) ;
		return exposureTime ;
	}

	/**
	 * Override setExposureTime to ignore what is passed in.
	 */
	public void setExposureTime( double expTime ){}

	/**
	 * Override setExposureTime to ignore what is passed in.
	 */
	public void setExposureTime( String expTime ){}

	/**
	 * Override setCoadds to ignore what is passed in.
	 */
	public void setCoadds( int coadds ){}

	/**
	 * Override setCoadds to ignore what is passed in.
	 */
	public void setCoadds( String coadds ){}

	/**
	 * Override getting the coadds.so as to not inherit unless necessary.
	 */
	public int getCoadds()
	{
		int coadds = 0 ;
		// If the coadds has been set, use it.
		if( _avTable.exists( ATTR_COADDS ) )
		{
			coadds = _avTable.getInt( ATTR_COADDS , 1 ) ;
		}
		else
		{
			SpItem _baseItem = parent() ;
			SpUKIRTInstObsComp spi = ( SpUKIRTInstObsComp )SpTreeMan.findInstrument( _baseItem ) ;
			if( spi != null )
				coadds = spi.getDefaultBiasCoadds() ;
			else
				throw new RuntimeException( "no instrument in scope" ) ;
		}

		return coadds ;
	}

	public void translateProlog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translateEpilog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		// First of all make sure we have a suitable instrument
		SpInstObsComp inst = SpTreeMan.findInstrument( this ) ;
		if( inst == null || ( !( inst instanceof SpInstCGS4 ) && !( inst instanceof SpInstWFCAM ) ) )
			throw new SpTranslationNotSupportedException( "No CGS4 instrument component in scope" ) ;

		// Now get the config items and update them for this bias observation
		Hashtable<String,String> t = inst.getConfigItems() ;

		if( "CGS4".equalsIgnoreCase( t.get( "instrument" ) ) )
		{
			// If we are inside a CGS4 iterator, we need to pick up it's hashtable
			SpItem parent = parent() ;
			while( parent != null )
			{
				if( parent instanceof SpIterCGS4 )
				{
					t = ( ( SpIterCGS4 )parent ).getIterTable() ;
					break ;
				}
				parent = parent.parent() ;
			}
		}
		else if( "WFCAM".equalsIgnoreCase( t.get( "instrument" ) ) )
		{
			// If we are inside a WFCAM iterator, we need to pick up it's hashtable
			SpItem parent = parent() ;
			while( parent != null )
			{
				if( parent instanceof SpInstWFCAM )
				{
					break ;
				}
				else if( parent instanceof SpIterWFCAM )
				{
					t = (( SpIterWFCAM )parent).getIterTable() ;
					break ;
				}
				parent = parent.parent() ;
			}
		}

		// CGS4 specific
		if( t.containsKey( "biasExpTime" ) )
			t.put( "biasExpTime" , "" + getExposureTime() ) ;
		if( t.containsKey( "biasNumExp" ) )
			t.put( "biasNumExp" , "" + getCoadds() ) ;

		// WFCAM specific
		if( t.containsKey( "type" ) )
			t.put( "type" , "bias" ) ;
		if( t.containsKey( "exposureTime" ) )
			t.put( "exposureTime" , "" + getExposureTime() ) ;
		if( t.containsKey( "coadds" ) )
			t.put( "coadds" , "" + getCoadds() ) ;

		// Delete redundant entries
		if( "UIST".equalsIgnoreCase( t.get( "instrument" ) ) )
		{
			t.remove( "instPort" ) ;
			t.remove( "camera" ) ;
			t.remove( "imager" ) ;
			t.remove( "filter" ) ;
			t.remove( "focus" ) ;
			t.remove( "polarimetry" ) ;
			t.remove( "mask" ) ;
			t.remove( "maskWidth" ) ;
			t.remove( "maskHeight" ) ;
			t.remove( "disperser" ) ;
			t.remove( "posAngle" ) ;
			t.remove( "centralWavelength" ) ;
			t.remove( "resolution" ) ;
			t.remove( "dispersion" ) ;
			t.remove( "scienceArea" ) ;
			t.remove( "pixelScale" ) ;
			t.remove( "nreads" ) ;
			t.remove( "mode" ) ;
			t.remove( "readInterval" ) ;
			t.remove( "chopFrequency" ) ;
			t.remove( "chopDelay" ) ;
			t.remove( "chopDelay" ) ;
			t.remove( "darkNumExp" ) ;
			t.remove( "pupil_imaging" ) ;
			t.remove( "pupil_imaging" ) ;
			t.remove( "DAConf" ) ;
			t.remove( "DAConfMinExpT" ) ;
			t.remove( "spectralCoverage" ) ;
		}

		// Now see if we have a DRRecipe component and write out it's headers if we do.
		SpItem parent = parent() ;
		Vector<SpItem> recipes = null ;
		while( parent != null )
		{
			if( parent instanceof SpMSB )
			{
				recipes = SpTreeMan.findAllItems( parent , SpDRRecipe.class.getName() ) ;
				if( recipes.size() > 0 )
					break ;
			}
			parent = parent.parent() ;
		}

		if( recipes != null && recipes.size() != 0 )
		{
			SpDRRecipe recipe = ( SpDRRecipe )recipes.get( 0 ) ;
			v.add( "setHeader GRPMEM " + ( recipe.getBiasInGroup() ? "T" : "F" ) ) ;
			v.add( "setHeader RECIPE " + recipe.getBiasRecipeName() ) ;
		}
		else
		{
			logger.error( "No DRRecipe Component found in bias obs." ) ;
		}

		try
		{
			ConfigWriter.getCurrentInstance().write( t ) ;
		}
		catch( IOException ioe )
		{
			throw new SpTranslationNotSupportedException( "Error writing bias config file" ) ;
		}

		v.add( "loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName() ) ;
		v.add( gemini.sp.SpTranslationConstants.biasString ) ;
		v.add( "do " + getCount() + " _observe" ) ;
	}
}
