// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter ;

import gemini.sp.SpItem ;
import gemini.sp.SpType ;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;

import java.util.Enumeration ;
import java.util.Vector ;

//
// Defines the attributes used by the repeat iterator.
//
interface SpIterRepeatConstants
{

	public static final String COUNT = "repeatCount" ;
	public static final int COUNT_DEF = 1 ;
}

//
// Enumeration of the repeat iterator's values.
//
@SuppressWarnings( "serial" )
class SpIterRepeatEnumeration extends SpIterEnumeration implements SpIterRepeatConstants
{
	private int _curCount = 0 ;
	private int _maxCount ;

	SpIterRepeatEnumeration( SpIterRepeat iterRepeat )
	{
		super( iterRepeat ) ;
		_maxCount = iterRepeat.getCount() ;
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
		SpIterValue siv = new SpIterValue( "loop" , String.valueOf( _curCount + 1 ) ) ;
		return new SpIterStep( "comment" , _curCount++ , _iterComp , siv ) ;
	}

}

/**
 * A simple iterator that repeats the steps of nested iterators the specified
 * number of times.
 */
@SuppressWarnings( "serial" )
public class SpIterRepeat extends SpIterComp implements SpIterRepeatConstants , SpTranslatable
{
	/**
     * Default constructor.
     */
	public SpIterRepeat()
	{
		super( SpType.ITERATOR_COMPONENT_REPEAT ) ;

		_avTable.noNotifySet( COUNT , String.valueOf( COUNT_DEF ) , 0 ) ;
	}

	/** Override getTitle to return the repeat count. */
	public String getTitle()
	{
		return "Repeat (" + getCount() + "X)" ;
	}

	/** Get the repeat count. */
	public int getCount()
	{
		return _avTable.getInt( COUNT , COUNT_DEF ) ;
	}

	/** Set the repeat count as an integer. */
	public void setCount( int i )
	{
		_avTable.set( COUNT , i ) ;
	}

	/** Set the repeat count as a String. */
	public void setCount( String s )
	{
		_avTable.set( COUNT , s ) ;
	}

	/** Enumerate the values of this iterator. */
	public SpIterEnumeration elements()
	{
		return new SpIterRepeatEnumeration( this ) ;
	}

	public void translateProlog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translateEpilog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		Enumeration<SpItem> e = this.children() ;
		Vector<String> childVector = new Vector<String>() ;
		SpTranslatable translatable = null ;
		SpTranslatable previous = null ;
		while( e.hasMoreElements() )
		{
			SpItem child = e.nextElement() ;
			if( child instanceof SpTranslatable )
			{
				translatable = ( SpTranslatable )child ;
				if( !translatable.equals( previous ) )
				{
					if( previous != null )
					{
						previous.translateEpilog( childVector ) ;
						previous = translatable ;
					}
					translatable.translateProlog( childVector ) ;
				}
				translatable.translate( childVector ) ;
			}
		}
		if( translatable != null  )
			translatable.translateEpilog( childVector ) ;
		
		childVector.add( "breakPoint" ) ;

		for( int i = 0 ; i < getCount() ; i++ )
			v.addAll( childVector ) ;
	}
}
