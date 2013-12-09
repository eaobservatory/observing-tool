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

import gemini.sp.SpItem ;

import java.util.Vector ;
import java.util.Enumeration ;

//
// This is a helper class for SpIterEnumeration. It is constructed with
// and SpIterComp parent and it will enumerate the SpIterEnumeration for
// each child of the parent. Each SpIterEnumeration is used to get all
// the iteration steps from the children of the parent.
//
@SuppressWarnings( "serial" )
class SpIterChildEnumeration implements Enumeration<SpIterEnumeration> , java.io.Serializable
{

	private SpIterComp _iterParent ;

	private SpIterComp _curChild ;

	private Enumeration<SpItem> _children ;

	//
	// Construct with the parent class whose children's SpIterEnumerations
	// should be enumerated.
	//
	SpIterChildEnumeration( SpIterComp iterParent )
	{
		_iterParent = iterParent ;
		reinit() ;
	}

	//
	// Get the next child that is an SpIterComp.
	//
	private SpIterComp _getNextChild()
	{
		SpIterComp icChild = null ;
		while( _children.hasMoreElements() )
		{
			SpItem child = _children.nextElement() ;
			if( child instanceof SpIterComp )
			{
				icChild = ( SpIterComp )child ;
				break ;
			}
		}
		return icChild ;
	}

	//
	// For each step of the parent, all the values of the children will have
	// to be enumerated again. This method is called after a step of the
	// parent has been completed to get a fresh set of values for the children.
	//
	void reinit()
	{
		// Get a new enumeration of the children items of the parent.
		_children = _iterParent.children() ;

		// Reset the _curChild to be the first child of the parent.
		_curChild = _getNextChild() ;
	}

	//
	// Returns the whether there are any more children of the parent.
	//
	public boolean hasMoreElements()
	{
		// reinit() sets the _curChild in the constructor, and after each element
		// returned by nextElement, _curChild is moved to the next child (if any).
		// This means that there are more elements so long as _curChild isn't null.

		return( _curChild != null ) ;
	}

	//
	// Returns the SpIterEnumeration from the current child (and sets _curChild
	// to refer to the next child of the parent, if any). The SpIterEnumeration
	// is used to get all the steps from the children of the parent.
	//
	public SpIterEnumeration nextElement()
	{
		SpIterEnumeration sie = _curChild.elements() ;
		_curChild = _getNextChild() ;
		return sie ;
	}

}

/**
 * This is the base class for enumerating the iteration steps of an iterator. It
 * takes care of the details of stepping through nested child iterators.
 * Subclasses should concentrate on providing the implementation of the three
 * protected abstract methods:
 * 
 * <pre>
 * _thisHasMoreElements()
 * _thisFirstElement()
 * _thisNextElement()
 * </pre>
 * 
 * Each element returned by this Enumeration (i.e., the return value of the
 * nextElement() method) is a Vector of <b>SpIterStep</b>. The SpIterStep
 * describes one step of the set of steps produced by an iterator. For instance,
 * an instrument iterator might iterate over filters and exposure time. A single
 * step of the iterator would set an exposure time and a filter concurrently.
 * 
 * <p>
 * For each step of a parent of a nested iterator, the child iterators must
 * cycle through all of their values. The Vector of SpIterSteps returned by
 * nextElement() is ordered from the outermost iterator to the most deeply
 * nested iterator.
 * 
 * <h3>Example</h3>
 * Consider a Science Program fragment containing the following nested
 * iterators:
 * 
 * <pre>
 * Instrument Iterator (steps through filters J, H, K)
 * 	Offset Iterator (steps through positions 1 and 2)
 * 		Observe Iterator (observes 2x)
 * </pre>
 * 
 * <p>
 * The first value returned from nextElement() would be:
 * 
 * <pre>
 * Vector of SpIterStep
 * 1) filter=J
 * 2) offset to position 1
 * 3) observe
 * </pre>
 * 
 * <p>
 * The value returned from the next call to nextElement() would simply be:
 * 
 * <pre>
 * Vector of SpIterStep
 * 1) observe
 * </pre>
 * 
 * <p>
 * since the filter and offset have already been applied prior to the first
 * observe. The next call produces:
 * 
 * <pre>
 * Vector of SpIterStep
 * 1) offset to position 2
 * 2) observe
 * </pre>
 * 
 * <p>
 * Then in the next call, the filter is changed to H, the telescope is offset to
 * position 1, and an observe is applied. This pattern continues until all the
 * values that can be produced are exhausted.
 * 
 * <h3>Subclassing</h3>
 * Again, subclasses should concentrate on implementing only the three protected
 * abstract methods and not worry about nesting. The combined task of these
 * three methods is to iterate over the steps contained in a single iterator.
 * For instance, an Offset iterator should return an SpIterStep describing the
 * first offset position with the call to _thisFirstElement(). Each subsequent
 * call to _thisNextElement() should return an SpIterStep the next offset
 * position. So long as there are offset positions available,
 * _thisHasMoreElements() should return true.
 */
@SuppressWarnings( "serial" )
public abstract class SpIterEnumeration implements Enumeration<Vector<SpIterStep>> , java.io.Serializable
{

	protected SpIterComp _iterComp ;
	private SpIterChildEnumeration _children ;
	private SpIterEnumeration _childEnum ;
	private SpIterStep _curElement ;
	private boolean _firstTime = true ;

	/**
     * Construct the SpIterEnumeration with the iterator component that it will
     * be stepping through.
     */
	public SpIterEnumeration( SpIterComp iterComp )
	{
		_iterComp = iterComp ;
		_children = new SpIterChildEnumeration( iterComp ) ;
	}

	/**
     * Return true if there are more steps in the SpIterComp.
     */
	protected abstract boolean _thisHasMoreElements() ;

	/**
     * Return the first step in the iterator.
     */
	protected abstract SpIterStep _thisFirstElement() ;

	/**
     * Return the next step in the iterator.
     */
	protected abstract SpIterStep _thisNextElement() ;

	/**
     * This method has not come to its fruition. No subclasses currently
     * override this method.
     */
	protected boolean _thisHasCleanup()
	{
		return false ;
	}

	/**
     * This method has not come to its fruition. No subclasses currently
     * override this method.
     */
	protected SpIterStep _thisCleanup()
	{
		return null ;
	}

	/**
     * Returns true if there are more steps before the enumeration is completely
     * exhausted. This method is used by clients of this class and need not be
     * overriden. See the discussion in the description of this class.
     */
	public boolean hasMoreElements()
	{
		if( _firstTime )
			return _thisHasMoreElements() ;

		if( _childEnum != null )
		{
			if( _childEnum.hasMoreElements() || _childEnum.hasCleanup() )
			{
				return true ;
			}
			else if( _children.hasMoreElements() )
			{
				_childEnum = _children.nextElement() ;
				return hasMoreElements() ;
			}
		}
		else
		{
			if( _children.hasMoreElements() )
			{
				_childEnum = _children.nextElement() ;
				return hasMoreElements() ;
			}
		}

		return _thisHasMoreElements() ;
	}

	/**
     * This method is not currently used.
     */
	public boolean hasCleanup()
	{
		return _thisHasCleanup() ;
	}

	/**
     * Return a Vector of SpIterStep. Each call produces the next change
     * prescribed by this iterator and any iterators nested inside of it. Please
     * see the discussion of this class for more information.
     */
	public Vector<SpIterStep> nextElement()
	{
		Vector<SpIterStep> v ;

		if( _firstTime )
		{
			_firstTime = false ;
			_curElement = _thisFirstElement() ;
		}

		// Base case: an iterComp without children
		if( _childEnum == null )
		{
			if( _curElement == null )
				_curElement = _thisNextElement() ;
			v = new Vector<SpIterStep>() ;
			v.insertElementAt( _curElement , 0 ) ;
			_curElement = null ;

			return v ;
		}

		// The iterComp had children

		if( _childEnum.hasMoreElements() )
		{
			v = _childEnum.nextElement() ;

			if( _curElement != null )
			{
				// This is the first time through, so include my step in the vector of steps.
				v.insertElementAt( _curElement , 0 ) ;

				/*
                 * Set _curElement to null so that the next time through my step
                 * won't be added again (it will have already been executed).
                 */
				_curElement = null ;
			}
			return v ;

		}
		else if( _childEnum.hasCleanup() )
		{
			v = new Vector<SpIterStep>() ;
			v.addElement( _childEnum.cleanup() ) ;
			return v ;
		}

		// Reached the end of the child's values, move to the next element
		_children.reinit() ;
		_childEnum = _children.nextElement() ;

		_curElement = _thisNextElement() ;
		return nextElement() ;
	}

	/**
     * This method is not currently used.
     */
	public SpIterStep cleanup()
	{
		return _thisCleanup() ;
	}
}
