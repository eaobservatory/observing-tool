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

import gemini.sp.SpItem ;
import gemini.sp.SpFactory ;
import gemini.sp.SpMSB ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpType ;

import gemini.sp.iter.SpIterEnumeration ;
import gemini.sp.iter.SpIterObserveBase ;
import gemini.sp.iter.SpIterOffset ;
import gemini.sp.iter.SpIterStep ;
import gemini.sp.iter.SpIterValue ;

import orac.ukirt.inst.SpDRRecipe ;

import gemini.util.MathUtil ;

import java.util.Random ;
import java.util.Vector ;

//
// Enumerater for the elements of the Sky iterator.
//
@SuppressWarnings( "serial" )
class SpIterSkyEnumeration extends SpIterEnumeration
{
	private int _curCount = 0 ;
	private int _maxCount ;

	SpIterSkyEnumeration( SpIterSky iterSky )
	{
		super( iterSky ) ;
		_maxCount = iterSky.getCount() ;
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
		return new SpIterStep( "sky" , _curCount++ , _iterComp , ( SpIterValue )null ) ;
	}
}

/**
 * A simple "Sky" iterator.
 */
@SuppressWarnings( "serial" )
public class SpIterSky extends SpIterObserveBase implements SpTranslatable
{
	public static final String ATTR_SKY_NAME = "sky" ;
	public static final String ATTR_SCALE_FACTOR = "scaleFactor" ;
	public static final String ATTR_BOX_SIZE = "randomBoxSize" ;
	public static final String ATTR_FOLLOW_OFFSET = "followOffset" ;
	public static final String ATTR_USE_RANDOM = "useRandom" ;
	private Random randomizer = new Random() ;

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "sky" , "Sky" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterSky() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterSky()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Override getTitle to return the sky count.
	 */
	public String getTitle()
	{
		if( getTitleAttr() != null )
			return super.getTitle() ;

		return "Sky (" + getCount() + "X)" ;
	}

	/**
	 * Get the Enumation of the iteration steps.
	 */
	public SpIterEnumeration elements()
	{
		return new SpIterSkyEnumeration( this ) ;
	}

	public void setSky( String name )
	{
		_avTable.set( ATTR_SKY_NAME , name ) ;
	}

	public String getSky()
	{
		if( _avTable.exists( ATTR_SKY_NAME ) )
			return _avTable.get( ATTR_SKY_NAME ) ;

		return "SKY" ;
	}

	public void rmSky()
	{
		_avTable.rm( ATTR_SKY_NAME ) ;
	}

	public void setFollowOffset( boolean flag )
	{
		if( flag == false && _avTable.exists( ATTR_SCALE_FACTOR ) )
			_avTable.noNotifyRm( ATTR_SCALE_FACTOR ) ;
		else if( flag == true && _avTable.exists( ATTR_BOX_SIZE ) )
			_avTable.noNotifyRm( ATTR_BOX_SIZE ) ;

		_avTable.set( ATTR_FOLLOW_OFFSET , flag ) ;
	}

	public boolean getFollowOffset()
	{
		return _avTable.getBool( ATTR_FOLLOW_OFFSET ) ;
	}

	public void setRandomPattern( boolean flag )
	{
		if( flag == false && _avTable.exists( ATTR_BOX_SIZE ) )
			_avTable.noNotifyRm( ATTR_BOX_SIZE ) ;
		else if( flag == true && _avTable.exists( ATTR_SCALE_FACTOR ) )
			_avTable.noNotifyRm( ATTR_SCALE_FACTOR ) ;

		_avTable.set( ATTR_USE_RANDOM , flag ) ;
	}

	public boolean getRandomPattern()
	{
		return _avTable.getBool( ATTR_USE_RANDOM ) ;
	}

	public void setScaleFactor( double scaleFactor )
	{
		_avTable.set( ATTR_SCALE_FACTOR , scaleFactor ) ;
	}

	public void setScaleFactor( String scaleFactor )
	{
		double dFactor = 1. ;
		try
		{
			dFactor = Double.parseDouble( scaleFactor ) ;
		}
		catch( NumberFormatException nfe ){}
		setScaleFactor( dFactor ) ;
	}

	public double getScaleFactor()
	{
		return _avTable.getDouble( ATTR_SCALE_FACTOR , 1. ) ;
	}

	public void setBoxSize( double size )
	{
		_avTable.set( ATTR_BOX_SIZE , size ) ;
	}

	public void setBoxSize( String size )
	{
		double dSize = 5. ;
		try
		{
			dSize = Double.parseDouble( size ) ;
		}
		catch( NumberFormatException nfe ){}
		setBoxSize( dSize ) ;
	}

	public double getBoxSize()
	{
		return _avTable.getDouble( ATTR_BOX_SIZE , 5. ) ;
	}

	public void translateProlog( Vector<String> v ) throws SpTranslationNotSupportedException
	{
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

		// Get the DR recipe component so we can add the header information
		if( recipes != null && recipes.size() != 0 )
		{
			SpDRRecipe recipe = ( SpDRRecipe )recipes.get( 0 ) ;
			v.add( "setHeader GRPMEM " + ( recipe.getSkyInGroup() ? "T" : "F" ) ) ;
			v.add( "setHeader RECIPE " + recipe.getSkyRecipeName() ) ;
		}
		else
		{
			logger.error( "No DRRecipe Component found in sky obs" ) ;
		}

		if( !"SKY".equals( getSky() ) )
		{
			int index = 0 ;
			String skyIndex = getSky().substring( 3 ) ;
			if( skyIndex.matches( "\\d+" ) )
				index = Integer.parseInt( skyIndex ) ;
			SpTelescopePos thisGuide = ( SpTelescopePos )SpTreeMan.findTargetList( this ).getPosList().getPosition( "SKYGUIDE" + index ) ;
	
			v.add( "slew MAIN " + getSky() ) ;
			
			if( thisGuide != null )
				v.add( "slew GUIDE SKYGUIDE" + index ) ;
			
			v.add( "-WAIT ALL" ) ;
		}
	}
	
	public void translateEpilog( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		if( !"SKY".equals( getSky() ) )
		{
			v.add( "do 1 _slew_all" ) ;
			v.add( "-WAIT ALL" ) ;
		}
	}
	
	public void translate( Vector<String> v )
	{
		/*
		 * If we are using unnamed skys, we can defer the offset tp the SpIterOffset class ;
		 * If not we need to add it here
		 */
		if( "".equals( getSky() ) || "SKY".equals( getSky() ) )
		{
			// defer offset
			v.add( gemini.sp.SpTranslationConstants.skyString ) ;
			v.add( "do " + getCount() + " _observe" ) ;

		}
		else
		{
			if( getFollowOffset() )
				translateFollowOffset( v ) ;
			else if( getRandomPattern() )
				translateRandom( v ) ;
			else
				translateStandard( v ) ;
		}
	}

	private void translateStandard( Vector<String> v )
	{
		SpTelescopePos thisSky = ( SpTelescopePos )SpTreeMan.findTargetList( this ).getPosList().getPosition( getSky() ) ;

		boolean inOffset = false ;
		SpItem parent = parent() ;
		while( parent != null )
		{
			if( parent instanceof SpIterOffset )
			{
				inOffset = true ;
				break ;
			}
			parent = parent.parent() ;
		}

		String lastOffset = null ;
		if( inOffset )
		{
			for( int i = v.size() - 1 ; i >= 0 ; i-- )
			{
				String candidate = v.get( i ) ;
				if( candidate.startsWith( "offset" ) )
				{
					lastOffset = candidate ;
					break ;
				}
			}
		}

		boolean isOffset = thisSky.isOffsetPosition() ;

		if( isOffset )
		{
			// Offset to the sky position, do the observe, then offset back
			v.add( "offset " + thisSky.getXaxis() + " " + thisSky.getYaxis() ) ;
			v.add( gemini.sp.SpTranslationConstants.skyString ) ;
			v.add( "do " + getCount() + " _observe" ) ;
			
			if( lastOffset != null )
				v.add( lastOffset ) ;
			else
				v.add( "offset 0.0 0.0" ) ;
		}
		else
		{
			v.add( gemini.sp.SpTranslationConstants.skyString ) ;
			v.add( "do " + getCount() + " _observe" ) ;
			
			if( lastOffset != null )
				v.add( lastOffset ) ;
		}
		
		if( !inOffset )
			v.add( "ADDOFFSET" ) ;
	}

	private void translateRandom( Vector<String> v )
	{
		SpTelescopePos thisSky = ( SpTelescopePos )SpTreeMan.findTargetList( this ).getPosList().getPosition( getSky() ) ;

		// See if we are inside an offset iterator
		boolean inOffset = false ;
		SpItem parent = parent() ;
		while( parent != null )
		{
			if( parent instanceof SpIterOffset )
			{
				inOffset = true ;
				break ;
			}
			parent = parent.parent() ;
		}
		String lastOffset = null ;
		if( inOffset )
		{
			for( int i = v.size() - 1 ; i >= 0 ; i-- )
			{
				String candidate = v.get( i ) ;
				if( candidate.startsWith( "offset" ) )
				{
					lastOffset = candidate ;
					break ;
				}
			}
		}

		boolean isOffset = thisSky.isOffsetPosition() ;

		double xAxis = thisSky.getXaxis() ;
		double yAxis = thisSky.getYaxis() ;
		double randX = ( randomizer.nextDouble() - 0.5 ) * getBoxSize() ;
		double randY = ( randomizer.nextDouble() - 0.5 ) * getBoxSize() ;

		if( isOffset )
		{
			// Offset to the sky position, do the observe, then offset back
			v.add( "offset " + MathUtil.round( xAxis + randX , 2 ) + " " + MathUtil.round( yAxis + randY , 2 ) ) ;
			
			v.add( gemini.sp.SpTranslationConstants.skyString ) ;
			v.add( "do " + getCount() + " _observe" ) ;
			
			if( lastOffset != null )
				v.add( lastOffset ) ;
			else
				v.add( "offset 0.0 0.0" ) ;
		}
		else
		{
			v.add( "offset " + MathUtil.round( randX , 2 ) + " " + MathUtil.round( randY , 2 ) ) ;
			v.add( gemini.sp.SpTranslationConstants.skyString ) ;
			v.add( "do " + getCount() + " _observe" ) ;
		
			if( lastOffset != null )
				v.add( lastOffset ) ;
		}
		if( !inOffset )
			v.add( "ADDOFFSET" ) ;
	}

	private void translateFollowOffset( Vector<String> v )
	{
		SpTelescopePos thisSky = ( SpTelescopePos )SpTreeMan.findTargetList( this ).getPosList().getPosition( getSky() ) ;

		// See if we are inside an offset iterator
		boolean inOffset = false ;
		SpItem parent = parent() ;
		while( parent != null )
		{
			if( parent instanceof SpIterOffset )
			{
				inOffset = true ;
				break ;
			}
			parent = parent.parent() ;
		}

		String lastOffset = null ;
		if( inOffset )
		{
			for( int i = v.size() - 1 ; i >= 0 ; i-- )
			{
				if( v.get( i ).startsWith( "offset" ) )
				{
					lastOffset = v.get( i ) ;
					break ;
				}
			}
		}

		boolean isOffset = thisSky.isOffsetPosition() ;

		double xAxis = thisSky.getXaxis() ;
		double yAxis = thisSky.getYaxis() ;
		double lastOffX = 0. ;
		double lastOffY = 0. ;
		if( inOffset )
		{
			lastOffX = Double.parseDouble( lastOffset.split( "\\s" )[ 1 ] ) ;
			lastOffY = Double.parseDouble( lastOffset.split( "\\s" )[ 2 ] ) ;
		}
		lastOffX *= getScaleFactor() ;
		lastOffY *= getScaleFactor() ;

		if( isOffset )
		{
			// Offset to the sky position, do the observe, then offset back
			v.add( "offset " + MathUtil.round( xAxis + lastOffX , 2 ) + " " + MathUtil.round( yAxis + lastOffY , 2 ) ) ;
			v.add( gemini.sp.SpTranslationConstants.skyString ) ;
			v.add( "do " + getCount() + " _observe" ) ;
			
			if( lastOffset != null )
				v.add( lastOffset ) ;
			else
				v.add( "offset 0.0 0.0" ) ;
		}
		else
		{
			v.add( "offset " + MathUtil.round( lastOffX , 2 ) + " " + MathUtil.round( lastOffY , 2 ) ) ;
			v.add( gemini.sp.SpTranslationConstants.skyString ) ;
			v.add( "do " + getCount() + " _observe" ) ;
			
			if( lastOffset != null )
				v.add( lastOffset ) ;
		}
		
		if( !inOffset )
			v.add( "ADDOFFSET" ) ;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer( "SpIterSky=[" ) ;
		sb = sb.append( "tag=" + getSky() + ", " ) ;
		sb = sb.append( "repeat=" + getCount() + ", " ) ;
		sb = sb.append( "followOffset=" + getFollowOffset() + ", " ) ;
		sb = sb.append( "useRandomPattern=" + getRandomPattern() + ", " ) ;
		
		if( getFollowOffset() )
			sb = sb.append( "scaleFactor=" + getScaleFactor() + "]" ) ;
		else if( getRandomPattern() )
			sb = sb.append( "boxSize=" + getBoxSize() + "]" ) ;
		else
			sb = sb.replace( sb.lastIndexOf( "," ) , sb.lastIndexOf( "," ) , "]" ) ;

		return sb.toString() ;
	}
}
