// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpMSB;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTreeMan;
import gemini.sp.SpTranslatable;
import gemini.sp.SpType;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import orac.ukirt.inst.SpDRRecipe;

import gemini.util.MathUtil;

import java.util.Random;
import java.util.Vector;

//
// Enumerater for the elements of the Sky iterator.
//
class SpIterSkyEnumeration extends SpIterEnumeration
{
	private int _curCount = 0;
	private int _maxCount;

	SpIterSkyEnumeration( SpIterSky iterSky )
	{
		super( iterSky );
		_maxCount = iterSky.getCount();
	}

	protected boolean _thisHasMoreElements()
	{
		return( _curCount < _maxCount );
	}

	protected SpIterStep _thisFirstElement()
	{
		return _thisNextElement();
	}

	protected SpIterStep _thisNextElement()
	{
		return new SpIterStep( "sky" , _curCount++ , _iterComp , ( SpIterValue )null );
	}
}

/**
 * A simple "Sky" iterator.
 */
public class SpIterSky extends SpIterObserveBase implements SpTranslatable
{
	public static final String ATTR_SKY_NAME = "sky";
	public static final String ATTR_SCALE_FACTOR = "scaleFactor";
	public static final String ATTR_BOX_SIZE = "randomBoxSize";
	public static final String ATTR_FOLLOW_OFFSET = "followOffset";
	public static final String ATTR_USE_RANDOM = "useRandom";
	private Random randomizer = new Random();

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "sky" , "Sky" );

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterSky() );
	}

	/**
	 * Default constructor.
	 */
	public SpIterSky()
	{
		super( SP_TYPE );
	}

	/**
	 * Override getTitle to return the sky count.
	 */
	public String getTitle()
	{
		if( getTitleAttr() != null )
			return super.getTitle();

		return "Sky (" + getCount() + "X)";
	}

	/**
	 * Get the Enumation of the iteration steps.
	 */
	public SpIterEnumeration elements()
	{
		return new SpIterSkyEnumeration( this );
	}

	public void setSky( String name )
	{
		_avTable.set( ATTR_SKY_NAME , name );
	}

	public String getSky()
	{
		if( _avTable.exists( ATTR_SKY_NAME ) )
			return _avTable.get( ATTR_SKY_NAME );

		return "SKY";
	}

	public void setFollowOffset( boolean flag )
	{
		if( flag == false && _avTable.exists( ATTR_SCALE_FACTOR ) )
			_avTable.noNotifyRm( ATTR_SCALE_FACTOR );
		else if( flag == true && _avTable.exists( ATTR_BOX_SIZE ) )
			_avTable.noNotifyRm( ATTR_BOX_SIZE );

		_avTable.set( ATTR_FOLLOW_OFFSET , flag );
	}

	public boolean getFollowOffset()
	{
		return _avTable.getBool( ATTR_FOLLOW_OFFSET );
	}

	public void setRandomPattern( boolean flag )
	{
		if( flag == false && _avTable.exists( ATTR_BOX_SIZE ) )
			_avTable.noNotifyRm( ATTR_BOX_SIZE );
		else if( flag == true && _avTable.exists( ATTR_SCALE_FACTOR ) )
			_avTable.noNotifyRm( ATTR_SCALE_FACTOR );

		_avTable.set( ATTR_USE_RANDOM , flag );
	}

	public boolean getRandomPattern()
	{
		return _avTable.getBool( ATTR_USE_RANDOM );
	}

	public void setScaleFactor( double scaleFactor )
	{
		_avTable.set( ATTR_SCALE_FACTOR , scaleFactor );
	}

	public void setScaleFactor( String scaleFactor )
	{
		double dFactor = 1. ;
		try
		{
			dFactor = Double.parseDouble( scaleFactor );
		}
		catch( NumberFormatException nfe ){}
		setScaleFactor( dFactor );
	}

	public double getScaleFactor()
	{
		return _avTable.getDouble( ATTR_SCALE_FACTOR , 1. );
	}

	public void setBoxSize( double size )
	{
		_avTable.set( ATTR_BOX_SIZE , size );
	}

	public void setBoxSize( String size )
	{
		double dSize = 5. ;
		try
		{
			dSize = Double.parseDouble( size );
		}
		catch( NumberFormatException nfe ){}
		setBoxSize( dSize );
	}

	public double getBoxSize()
	{
		return _avTable.getDouble( ATTR_BOX_SIZE , 5. );
	}

	public void translate( Vector v )
	{
		// Get the DR recipe component so we can add the header information
		SpItem parent = parent();
		Vector recipes = null;
		while( parent != null )
		{
			if( parent instanceof SpMSB )
			{
				recipes = SpTreeMan.findAllItems( parent , "orac.ukirt.inst.SpDRRecipe" );
				if( recipes != null && recipes.size() > 0 )
					break;
			}
			parent = parent.parent();
		}

		if( recipes != null && recipes.size() != 0 )
		{
			SpDRRecipe recipe = ( SpDRRecipe )recipes.get( 0 );
			v.add( "setHeader GRPMEM " + ( recipe.getSkyInGroup() ? "T" : "F" ) );
			v.add( "setHeader RECIPE " + recipe.getSkyRecipeName() );
		}

		// If we are using unnamed skys, we can defer the offset tp the SpIterOffset class;
		// If not we need to add it here
		if( "".equals( getSky() ) || "SKY".equals( getSky() ) )
		{
			// defer offset
			v.add( gemini.sp.SpTranslationConstants.skyString );
			v.add( "do " + getCount() + " _observe" );

		}
		else
		{
			if( getFollowOffset() )
				translateFollowOffset( v );
			else if( getRandomPattern() )
				translateRandom( v );
			else
				translateStandard( v );
		}
	}

	private void translateStandard( Vector v )
	{
		SpTelescopePos thisSky = ( SpTelescopePos )SpTreeMan.findTargetList( this ).getPosList().getPosition( getSky() );
		int index = Integer.parseInt( getSky().substring( 3 ) );
		SpTelescopePos thisGuide = ( SpTelescopePos )SpTreeMan.findTargetList( this ).getPosList().getPosition( "SKYGUIDE" + index );

		boolean inOffset = false;
		SpItem parent = parent();
		while( parent != null )
		{
			if( parent instanceof SpIterOffset )
			{
				inOffset = true;
				break;
			}
			parent = parent.parent();
		}

		String lastOffset = null;
		if( inOffset )
		{
			for( int i = v.size() - 1 ; i >= 0 ; i-- )
			{
				if( ( ( String )v.get( i ) ).startsWith( "offset" ) )
				{
					lastOffset = ( String )v.get( i );
					break;
				}
			}
		}

		boolean isOffset = thisSky.isOffsetPosition();

		if( isOffset )
		{
			// Offset to the sky position, do the observe, then offset back
			v.add( "offset " + thisSky.getXaxis() + " " + thisSky.getYaxis() );
			
			if( thisGuide != null )
				v.add( "slew GUIDE SKYGUIDE" + index );

			v.add( gemini.sp.SpTranslationConstants.skyString );
			v.add( "do " + getCount() + " _observe" );
			
			if( lastOffset != null )
				v.add( lastOffset );
			else
				v.add( "offset 0.0 0.0" );
			
			if( thisGuide != null )
				v.add( "slew GUIDE GUIDE" );
		}
		else
		{
			v.add( "slew MAIN " + getSky() );
			
			if( thisGuide != null )
				v.add( "slew GUIDE SKYGUIDE" + index );
			
			v.add( "-WAIT ALL" );
			v.add( gemini.sp.SpTranslationConstants.skyString );
			v.add( "do " + getCount() + " _observe" );
			v.add( "do 1 _slew_all" );
			v.add( "-WAIT ALL" );
			
			if( lastOffset != null )
				v.add( lastOffset );
		}
		
		if( !inOffset )
			v.add( "ADDOFFSET" );
	}

	private void translateRandom( Vector v )
	{
		SpTelescopePos thisSky = ( SpTelescopePos )SpTreeMan.findTargetList( this ).getPosList().getPosition( getSky() );
		int index = Integer.parseInt( getSky().substring( 3 ) );
		SpTelescopePos thisGuide = ( SpTelescopePos )SpTreeMan.findTargetList( this ).getPosList().getPosition( "SKYGUIDE" + index );

		// See if we are inside an offset iterator
		boolean inOffset = false;
		SpItem parent = parent();
		while( parent != null )
		{
			if( parent instanceof SpIterOffset )
			{
				inOffset = true;
				break;
			}
			parent = parent.parent();
		}
		String lastOffset = null;
		if( inOffset )
		{
			for( int i = v.size() - 1 ; i >= 0 ; i-- )
			{
				if( ( ( String )v.get( i ) ).startsWith( "offset" ) )
				{
					lastOffset = ( String )v.get( i );
					break;
				}
			}
		}

		boolean isOffset = thisSky.isOffsetPosition();

		double xAxis = thisSky.getXaxis();
		double yAxis = thisSky.getYaxis();
		double randX = ( randomizer.nextDouble() - 0.5 ) * getBoxSize();
		double randY = ( randomizer.nextDouble() - 0.5 ) * getBoxSize();

		if( isOffset )
		{
			// Offset to the sky position, do the observe, then offset back
			v.add( "offset " + MathUtil.round( xAxis + randX , 2 ) + " " + MathUtil.round( yAxis + randY , 2 ) );
			
			if( thisGuide != null )
				v.add( "slew GUIDE SKYGUIDE" + index );
				
			v.add( gemini.sp.SpTranslationConstants.skyString );
			v.add( "do " + getCount() + " _observe" );
			
			if( lastOffset != null )
				v.add( lastOffset );
			else
				v.add( "offset 0.0 0.0" );
			
			if( thisGuide != null )
				v.add( "slew GUIDE GUIDE" );
		}
		else
		{
			v.add( "slew MAIN " + getSky() );
			
			if( thisGuide != null )
				v.add( "slew GUIDE SKYGUIDE" + index );
			
			v.add( "-WAIT ALL" );
			v.add( "offset " + MathUtil.round( randX , 2 ) + " " + MathUtil.round( randY , 2 ) );
			v.add( gemini.sp.SpTranslationConstants.skyString );
			v.add( "do " + getCount() + " _observe" );
			v.add( "do 1 _slew_all" );
			v.add( "-WAIT ALL" );
			
			if( lastOffset != null )
				v.add( lastOffset );
		}
		if( !inOffset )
			v.add( "ADDOFFSET" );
	}

	private void translateFollowOffset( Vector v )
	{
		SpTelescopePos thisSky = ( SpTelescopePos )SpTreeMan.findTargetList( this ).getPosList().getPosition( getSky() );
		int index = Integer.parseInt( getSky().substring( 3 ) );
		SpTelescopePos thisGuide = ( SpTelescopePos )SpTreeMan.findTargetList( this ).getPosList().getPosition( "SKYGUIDE" + index );

		// See if we are inside an offset iterator
		boolean inOffset = false;
		SpItem parent = parent();
		while( parent != null )
		{
			if( parent instanceof SpIterOffset )
			{
				inOffset = true;
				break;
			}
			parent = parent.parent();
		}

		String lastOffset = null;
		if( inOffset )
		{
			for( int i = v.size() - 1 ; i >= 0 ; i-- )
			{
				if( ( ( String )v.get( i ) ).startsWith( "offset" ) )
				{
					lastOffset = ( String )v.get( i );
					break;
				}
			}
		}

		boolean isOffset = thisSky.isOffsetPosition();

		double xAxis = thisSky.getXaxis();
		double yAxis = thisSky.getYaxis();
		double lastOffX = 0. ;
		double lastOffY = 0. ;
		if( inOffset )
		{
			lastOffX = Double.parseDouble( lastOffset.split( "\\s" )[ 1 ] );
			lastOffY = Double.parseDouble( lastOffset.split( "\\s" )[ 2 ] );
		}
		lastOffX *= getScaleFactor();
		lastOffY *= getScaleFactor();

		if( isOffset )
		{
			// Offset to the sky position, do the observe, then offset back
			v.add( "offset " + MathUtil.round( xAxis + lastOffX , 2 ) + " " + MathUtil.round( yAxis + lastOffY , 2 ) );
			
			if( thisGuide != null )
				v.add( "slew GUIDE SKYGUIDE" + index );

			v.add( gemini.sp.SpTranslationConstants.skyString );
			v.add( "do " + getCount() + " _observe" );
			
			if( lastOffset != null )
				v.add( lastOffset );
			else
				v.add( "offset 0.0 0.0" );
			
			if( thisGuide != null )
				v.add( "slew GUIDE GUIDE" );
		}
		else
		{
			v.add( "slew MAIN " + getSky() );
			
			if( thisGuide != null )
				v.add( "slew GUIDE SKYGUIDE" + index );
			
			v.add( "-WAIT ALL" );
			v.add( "offset " + MathUtil.round( lastOffX , 2 ) + " " + MathUtil.round( lastOffY , 2 ) );
			v.add( gemini.sp.SpTranslationConstants.skyString );
			v.add( "do " + getCount() + " _observe" );
			v.add( "do 1 _slew_all" );
			v.add( "-WAIT ALL" );
			
			if( lastOffset != null )
				v.add( lastOffset );
		}
		
		if( !inOffset )
			v.add( "ADDOFFSET" );
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer( "SpIterSky=[" );
		sb = sb.append( "tag=" + getSky() + ", " );
		sb = sb.append( "repeat=" + getCount() + ", " );
		sb = sb.append( "followOffset=" + getFollowOffset() + ", " );
		sb = sb.append( "useRandomPattern=" + getRandomPattern() + ", " );
		
		if( getFollowOffset() )
			sb = sb.append( "scaleFactor=" + getScaleFactor() + "]" );
		else if( getRandomPattern() )
			sb = sb.append( "boxSize=" + getBoxSize() + "]" );
		else
			sb = sb.replace( sb.lastIndexOf( "," ) , sb.lastIndexOf( "," ) , "]" );

		return sb.toString();
	}
}
