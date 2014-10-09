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

package orac.ukirt.iter ;

import gemini.sp.SpItem ;
import gemini.sp.SpFactory ;
import gemini.sp.SpMSB ;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpType ;

import gemini.sp.iter.SpIterEnumeration ;
import gemini.sp.iter.SpIterObserveBase ;
import gemini.sp.iter.SpIterOffset ;
import gemini.sp.iter.SpIterStep ;
import gemini.sp.iter.SpIterValue ;

import orac.ukirt.inst.SpDRRecipe ;

import java.util.Vector ;

//
// Enumerater for the elements of the Observe iterator.
//
@SuppressWarnings( "serial" )
class SpIterObserveEnumeration extends SpIterEnumeration
{
	private int _curCount = 0 ;
	private int _maxCount ;

	SpIterObserveEnumeration( SpIterObserve iterObserve )
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
		return _thisNextElement() ;
	}

	protected SpIterStep _thisNextElement()
	{
		return new SpIterStep( "observe" , _curCount++ , _iterComp , ( SpIterValue )null ) ;
	}

}

/**
 * A simple "Observe" iterator.
 */
@SuppressWarnings( "serial" )
public class SpIterObserve extends SpIterObserveBase implements SpTranslatable
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "observe" , "Observe" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterObserve() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterObserve()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Override getTitle to return the observe count.
	 */
	public String getTitle()
	{
		if( getTitleAttr() != null )
			return super.getTitle() ;

		return "Observe (" + getCount() + "X)" ;
	}

	/**
	 * Get the Enumation of the iteration steps.
	 */
	public SpIterEnumeration elements()
	{
		return new SpIterObserveEnumeration( this ) ;
	}

	public void translateProlog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translateEpilog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translate( Vector<String> v )
	{
		// Get the DR recipe component so we can add the header information
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
			v.add( "setHeader GRPMEM " + ( recipe.getObjectInGroup() ? "T" : "F" ) ) ;
			v.add( "setHeader RECIPE " + recipe.getObjectRecipeName() ) ;
		}
		else
		{
			logger.error( "No DRRecipe Component found in observe" ) ;
		}

		// If we are not inside an offset, we need to tell the system there is an offset here
		parent = parent() ;
		boolean inOffset = false ;
		while( parent != null )
		{
			if( parent instanceof SpIterOffset )
			{
				inOffset = true ;
				break ;
			}
			parent = parent.parent() ;
		}
		v.add( gemini.sp.SpTranslationConstants.objectString ) ;
		String observe = "do " + getCount() + " _observe" ;
		v.add( observe ) ;
		if( !inOffset )
			v.add( "ADDOFFSET" ) ;
	}
}
