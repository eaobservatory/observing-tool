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
package gemini.sp.iter ;

import gemini.sp.SpType ;
import java.util.Vector ;

//
// This class defines the Enumeration for all SpIterConfigBase subclasses.
// "Configuration" iterators like those for instruments simply provide
// the set of IterConfigItems that they offer for iteration. The subclasses
// do not need to provide their own SpIterEnumeration subclass.
//
@SuppressWarnings( "serial" )
class SpIterConfigEnumeration extends SpIterEnumeration
{
	private SpIterConfigBase _iterConfig ;

	private Vector<String> _configItemAttr ;

	private int _totalSteps ;

	private int _curStep = 0 ;

	SpIterConfigEnumeration( SpIterConfigBase iterConfig )
	{
		super( iterConfig ) ;

		_iterConfig = iterConfig ;
		_configItemAttr = iterConfig.getConfigAttribs() ;
		_totalSteps = iterConfig.getConfigStepCount() ;
	}

	protected boolean _thisHasMoreElements()
	{
		return ( _configItemAttr != null ) && ( _configItemAttr.size() > 0 ) && ( _curStep < _totalSteps ) ;
	}

	// Trim the "Iter" off the end of an iteration item attribute name.
	private String _trimIter( String attribute )
	{
		if( attribute.endsWith( "Iter" ) )
		{
			int endIndex = attribute.length() - 4 ;
			return attribute.substring( 0 , endIndex ) ;
		}
		return attribute ;
	}

	// Get the configuration step corresponding with the given step.
	private SpIterValue[] _getConfigValues( int stepIndex )
	{
		String attr , val ;

		SpIterValue[] values = new SpIterValue[ _configItemAttr.size() ] ;

		for( int i = 0 ; i < _configItemAttr.size() ; ++i )
		{
			attr = _configItemAttr.elementAt( i ) ;
			val = _iterConfig.getConfigStep( attr , stepIndex ) ;
			values[ i ] = new SpIterValue( _trimIter( attr ) , val ) ;
		}

		return values ;
	}

	protected SpIterStep _thisFirstElement()
	{
		return _thisNextElement() ;
	}

	protected SpIterStep _thisNextElement()
	{
		SpIterValue[] values = _getConfigValues( _curStep ) ;
		return new SpIterStep( "config" , _curStep++ , _iterComp , values ) ;
	}

}

/**
 * The base class for iterators that iterate over configurations. For example,
 * the SpIterNIRI iterator iterates over filters, grisms, etc.
 */
@SuppressWarnings( "serial" )
public abstract class SpIterConfigBase extends SpIterComp
{

	/**
     * The name of the attribute that lists the set of attribtes being iterated
     * over. For example, an iterator set up to iterate over filters and
     * integration time would have its ATTR_ITER_ATTRIBUTES set to
     * 
     * <pre>
     * { filterIter , integrationTimeIter }
     * </pre>.
     */
	public static final String ATTR_ITER_ATTRIBUTES = "iterConfigList" ;

	/**
     * The array of configuration items offered by this iterator. This must be
     * initialized by the subclass (and is pretty much the only thing the
     * subclass has to do besides set the SpIterCompSubtype in the constructor).
     * 
     * @see #getAvailableItems()
     */
	protected IterConfigItem[] _iterConfigItems ;

	/**
     * Constructor.
     */
	public SpIterConfigBase( SpType spType )
	{
		super( spType ) ;
		_iterConfigItems = getAvailableItems() ;
	}

	/**
     * Get the name of the item being iterated over. Subclasses must define.
     */
	public abstract String getItemName() ;

	/**
     * Get an array containing the IterConfigItems offered by this iterator.
     */
	public abstract IterConfigItem[] getAvailableItems() ;

	/**
     * Get the IterConfigItem that corresponds with the given attribute.
     */
	public IterConfigItem getItemForAttribute( String attribute )
	{
		for( int i = 0 ; i < _iterConfigItems.length ; ++i )
		{
			if( _iterConfigItems[ i ].attribute.equals( attribute ) )
				return _iterConfigItems[ i ] ;
		}
		return null ;
	}

	/**
     * Get the set of attributes being iterated over.
     */
	public Vector<String> getConfigAttribs()
	{
		return _avTable.getAll( ATTR_ITER_ATTRIBUTES ) ;
	}

	/**
     * Get the steps of the item iterator of the given attribute.
     */
	public Vector<String> getConfigSteps( String attribute )
	{
		return _avTable.getAll( attribute ) ;
	}

	/**
     * Get the steps of the item iterator of the given attribute.
     */
	public String getConfigStep( String attribute , int index )
	{
		return _avTable.get( attribute , index ) ;
	}

	/**
     * Set the steps of the item iterator of the given attribute.
     */
	public void setConfigStep( String attribute , String value , int index )
	{
		_avTable.set( attribute , value , index ) ;
	}

	/**
     * Get the number of steps of the item iterator of the given attribute.
     */
	public int getConfigStepCount()
	{
		/*
		 * Should be able to get the number of elements in any iterator
		 * attribute, they should all have the same number of steps.
		 * So just use the first attribute in the list.
		 */
		
		String firstAttrib = _avTable.get( ATTR_ITER_ATTRIBUTES , 0 ) ;
		if( firstAttrib == null )
			return 0 ;

		return _avTable.size( firstAttrib ) ;
	}

	/**
     * Add a configuration item to the set.
     */
	public void addConfigItem( IterConfigItem ici , int size )
	{
		_avTable.add( ATTR_ITER_ATTRIBUTES , ici.attribute ) ;
		_avTable.setAll( ici.attribute , new Vector<String>() ) ;
		_avTable.setSize( ici.attribute , size ) ;
	}

	/**
     * Remove a configuration item from the set.
     */
	public void deleteConfigItem( String attribute )
	{
		Vector<String> v = _avTable.getAll( ATTR_ITER_ATTRIBUTES ) ;
		for( int i = 0 ; i < v.size() ; ++i )
		{
			String a = v.elementAt( i ) ;
			if( a.equals( attribute ) )
			{
				_avTable.rm( ATTR_ITER_ATTRIBUTES , i ) ;
				break ;
			}
		}
		_avTable.rm( attribute ) ;
	}

	/**
     * Insert a new step at the given index. MODIFIED by AB to get a default
     * from the previous step (for index >0)
     */
	public void insertConfigStep( int index )
	{
		Vector<String> v = _avTable.getAll( ATTR_ITER_ATTRIBUTES ) ;
		for( int i = 0 ; i < v.size() ; ++i )
		{
			String a = v.elementAt( i ) ;
			_avTable.insertAt( a , "" , index ) ;
		}
	}

	/**
     * Delete the step at the given index.
     */
	public void deleteConfigStep( int index )
	{
		Vector<String> v = _avTable.getAll( ATTR_ITER_ATTRIBUTES ) ;
		for( int i = 0 ; i < v.size() ; ++i )
		{
			String a = v.elementAt( i ) ;
			_avTable.rm( a , index ) ;
		}
	}

	/**
     * Move the step at the given index to the front.
     */
	public void configStepToFirst( int index )
	{
		Vector<String> v = _avTable.getAll( ATTR_ITER_ATTRIBUTES ) ;
		for( int i = 0 ; i < v.size() ; ++i )
		{
			String a = v.elementAt( i ) ;
			_avTable.indexToFirst( a , index ) ;
		}
	}

	/**
     * Move the step at the given index ahead one, toward the front.
     */
	public void configStepDecrement( int index )
	{
		Vector<String> v = _avTable.getAll( ATTR_ITER_ATTRIBUTES ) ;
		for( int i = 0 ; i < v.size() ; ++i )
		{
			String a = v.elementAt( i ) ;
			_avTable.decrementIndex( a , index ) ;
		}
	}

	/**
     * Move the step at the given index back one, toward the end.
     */
	public void configStepIncrement( int index )
	{
		Vector<String> v = _avTable.getAll( ATTR_ITER_ATTRIBUTES ) ;
		for( int i = 0 ; i < v.size() ; ++i )
		{
			String a = v.elementAt( i ) ;
			_avTable.incrementIndex( a , index ) ;
		}
	}

	/**
     * Move the step at the given index to the back.
     */
	public void configStepToLast( int index )
	{
		Vector<String> v = _avTable.getAll( ATTR_ITER_ATTRIBUTES ) ;
		for( int i = 0 ; i < v.size() ; ++i )
		{
			String a = v.elementAt( i ) ;
			_avTable.indexToLast( a , index ) ;
		}
	}

	/**
     * Get an Enumeration of all the iterator steps.
     */
	public SpIterEnumeration elements()
	{
		return new SpIterConfigEnumeration( this ) ;
	}
}
