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

package gemini.sp ;

import gemini.sp.obsComp.SpTelescopeObsComp ;

import gemini.util.RADecMath ;
import gemini.util.TelescopePos ;
import gemini.util.TelescopePosList ;

import java.util.Enumeration ;
import java.util.Vector ;

/**
 * A data object that describes a list of telescope positions. The telescope
 * position list data is mapped into a set of attributes, one for each
 * SpTelescopePos and one for the ordered list of tags of the positions. Each
 * SpTelescopeObsComp component maintains a SpTelescopePosList.
 * 
 * @see gemini.sp.obsComp.SpTelescopeObsComp
 */
@SuppressWarnings( "serial" )
public final class SpTelescopePosList extends TelescopePosList implements java.io.Serializable
{

	/** The name of the attribute that holds the target tags. */
	public static final String USER_POS_LIST = "userPositions" ;

	// The order in which positions should be kept
	private static String[] _tagOrder ;
	private SpItem _spItem ;
	private SpAvTable _avTab ; // The table that holds the positions
	private Vector<SpTelescopePos> _posList ; // A vector of SpTelescopePos objects

	/**
     * Create with a an attribute/value table (from a telescope target list
     * comp).
     */
	public SpTelescopePosList( SpItem spItem )
	{
		_spItem = spItem ;
		if( _spItem instanceof SpTelescopeObsComp )
		{
			_avTab = _spItem.getTable() ;
			setTable( _avTab ) ;
		}
		else
		{
			_avTab = new SpAvTable() ;
			_posList = new Vector<SpTelescopePos>() ;
			createPosition( SpTelescopePos.BASE_TAG , 0. , 0. ) ;
		}
	}

	/**
     * Override <code>setTable()</code> to recreate the position list based
     * upon the attributes in the new table. Also, the base position is updated
     * in the Observation Data of this item's context.
     * 
     * @see SpObsData
     */
	public void setTable( SpAvTable avTab )
	{
		synchronized( this )
		{
			_avTab = avTab ;

			if( _posList != null )
			{
				for( int i = 0 ; i < _posList.size() ; ++i )
				{
					SpTelescopePos tp = _posList.elementAt( i ) ;
					tp.deleteWatchers() ;
				}
			}

			_posList = getAllPositions( _spItem , avTab , this ) ;

			// Update the base position in the observation data.
			SpTelescopePos tp = getBasePosition() ;
			SpObsData od = _spItem.getObsData() ;
			if( od != null )
				od.setBasePos( tp.getXaxis() , tp.getYaxis() , tp.getCoordSys() ) ;
		}

		_notifyOfReset() ;
	}

	//
	// Extract the position with the given tag.
	//
	static SpTelescopePos getPosition( SpItem spItem , SpAvTable avTab , String tag , SpTelescopePosList list )
	{
		if( !avTab.exists( tag ) )
			return null ;
		return new SpTelescopePos( spItem , avTab , tag , list ) ;
	}

	//
	// Return all the positions from the table.
	//
	static Vector<SpTelescopePos> getAllPositions( SpItem spItem , SpAvTable avTab , SpTelescopePosList list )
	{
		Vector<SpTelescopePos> v = new Vector<SpTelescopePos>() ;

		SpTelescopePos tp ;

		tp = getPosition( spItem , avTab , SpTelescopePos.BASE_TAG , list ) ;
		if( tp != null )
			v.addElement( tp ) ;

		String[] guideTags = SpTelescopePos.getGuideStarTags() ;
		if( guideTags != null )
		{
			for( int i = 0 ; i < guideTags.length ; ++i )
			{
				if( guideTags[ i ].startsWith( "SKY" ) )
				{
					int index = 0 ;
					while( ( tp = getPosition( spItem , avTab , guideTags[ i ] + index , list ) ) != null )
					{
						v.addElement( tp ) ;
						index++ ;
					}
				}
				else
				{
					tp = getPosition( spItem , avTab , guideTags[ i ] , list ) ;
					if( tp != null )
						v.addElement( tp ) ;
				}
			}
		}

		Vector<String> tpv = avTab.getAll( USER_POS_LIST ) ;
		if( tpv == null )
			return v ;

		Enumeration<String> e = tpv.elements() ;
		while( e.hasMoreElements() )
		{
			String tag = e.nextElement();
			tp = getPosition( spItem , avTab , tag , list ) ;
			if( tp != null )
				v.addElement( tp ) ;
		}

		return v ;
	}

	//
	// Get a unique "User" tag.
	//
	private synchronized String _getUniqueUserTag()
	{
		int i ;
		int size = _avTab.size( USER_POS_LIST ) ;
		for( i = 0 ; i < size ; ++i )
		{
			String tag = _avTab.get( SpTelescopePos.USER_TAG + i ) ;
			if( tag == null )
				break ;
		}

		return SpTelescopePos.USER_TAG + i ;
	}

	//
	// The implementation of the TelescopePosList interface.

	/**
     * Returns the number of positions that are in the list.
     * 
     * @see TelescopePosList
     */
	public synchronized int size()
	{
		return _posList.size() ;
	}

	/**
     * Does the named position exist?
     * 
     * @see TelescopePosList
     */
	public synchronized boolean exists( String tag )
	{
		return _avTab.exists( tag ) ;
	}

	/**
     * Retrieve the position at the given index.
     * 
     * @see TelescopePosList
     */
	public synchronized TelescopePos getPositionAt( int index )
	{
		if( ( index < 0 ) || ( index > _posList.size() ) )
			return null ;

		return _posList.elementAt( index ) ;
	}

	// End of TelescopePosList interface implementation
	//

	/**
     * Retrieve the base position from the position list.
     */
	public SpTelescopePos getBasePosition()
	{
		return ( SpTelescopePos )getPosition( SpTelescopePos.BASE_TAG ) ;
	}

	/**
     * Retrieve all the user positions from the position list.
     */
	public synchronized Vector<SpTelescopePos> getAllUserPositions()
	{
		Vector<SpTelescopePos> v = new Vector<SpTelescopePos>() ;
		for( int i = 0 ; i < _posList.size() ; ++i )
		{
			SpTelescopePos tp = _posList.elementAt( i ) ;
			if( tp.isUserPosition() )
				v.addElement( tp ) ;
		}
		return v ;
	}

	public synchronized Vector<String> getAllTags( String tagType )
	{
		Vector<String> v = new Vector<String>() ;
		for( int i = 0 ; i < _posList.size() ; i++ )
		{
			String tag = _posList.elementAt( i ).getTag() ;
			if( tag.matches( tagType + "[0-9]+" ) )
				v.add( tag ) ;
		}
		return v ;
	}

	//
	// Get (create if needed) the array of SpTelescopePos tags in order.
	//
	private static String[] _getTagOrder()
	{
		if( _tagOrder == null )
		{
			String[] guideTags = SpTelescopePos.getGuideStarTags() ;
			if( guideTags == null )
			{
				_tagOrder = new String[] { SpTelescopePos.BASE_TAG } ;
			}
			else
			{
				_tagOrder = new String[ guideTags.length + 1 ] ;
				_tagOrder[ 0 ] = SpTelescopePos.BASE_TAG ;
				for( int i = 0 ; i < guideTags.length ; ++i )
					_tagOrder[ i + 1 ] = guideTags[ i ] ;
			}
		}
		return _tagOrder ;
	}

	//
	// Place the given SpTelescopePos in the position list, keeping the
	// ordering of the elements correct. Positions are always ordered
	// such that the base position is first, followed by Guide positions
	// in numeric order of their indices, followed by any OIWFS star,
	// followed by user positions.
	//
	private synchronized void _placeInPosList( SpTelescopePos tp )
	{

		// If this is a user tag (or the first tp to be placed in the list), place the new tp at the end of the list
		if( ( _posList.size() == 0 ) || ( tp.getTag().startsWith( SpTelescopePos.USER_TAG ) ) )
		{
			_posList.addElement( tp ) ;
			return ;
		}

		// Get the order in which the tp should be kept
		String[] tagOrder = _getTagOrder() ;

		int pos = 0 ;
		for( int i = 0 ; i < tagOrder.length ; ++i )
		{
			// See what is next in the current position list.
			SpTelescopePos nextTP = _posList.elementAt( pos ) ;

			// If its a user position, insert the new tp before it
			if( nextTP.getTag().startsWith( SpTelescopePos.USER_TAG ) )
			{
				_posList.insertElementAt( tp , pos ) ;
				return ;
			}

			if( nextTP.getTag().equals( tagOrder[ i ] ) )
			{
				++pos ;
				if( pos == _posList.size() )
				{
					_posList.addElement( tp ) ;
					return ;
				}
			}
			else if( tp.getTag().startsWith( tagOrder[ i ] ) )
			{
				_posList.insertElementAt( tp , pos ) ;
				return ;
			}
		}
	}

	//
	// Add the given tag to the list of user positions.
	//
	private synchronized void _addUserPosition( String tag )
	{
		// Add the tag to the list of user tags
		int size = _avTab.size( USER_POS_LIST ) ;
		if( size == 0 )
			_avTab.set( USER_POS_LIST , tag ) ;
		else
			_avTab.set( USER_POS_LIST , tag , size ) ;
	}

	/**
     * Create a new blank user position.
     */
	public synchronized SpTelescopePos createBlankUserPosition()
	{
		String tag = _getUniqueUserTag() ;
		_addUserPosition( tag ) ;

		SpTelescopePos tp = new SpTelescopePos( _spItem , _avTab , tag , this ) ;
		_placeInPosList( tp ) ;
		_notifyOfAdd( tp ) ;

		return tp ;
	}

	/**
     * Create a user position and add it to the A/V table.
     */
	public SpTelescopePos createPosition( double xaxis , double yaxis )
	{
		return createPosition( SpTelescopePos.USER_TAG , xaxis , yaxis ) ;
	}

	/**
     * Create a position with the given tag and add it to the A/V table. If the
     * given tag is a USER_TAG, a unique tag will be generated and assigned to
     * the created position. Returns the new SpTelescopePos object.
     */
	public SpTelescopePos createPosition( String tag , double xaxis , double yaxis )
	{
		SpTelescopePos tp ;
		synchronized( this )
		{
			// If this is a user tag, generate a unique tag
			if( tag.equals( SpTelescopePos.USER_TAG ) )
			{
				tag = _getUniqueUserTag() ;
				_addUserPosition( tag ) ;
			}
			else
			{

				// See if this tag already exists in the position list
				tp = ( SpTelescopePos )getPosition( tag ) ;
				if( tp != null )
					return tp ;
			}

			// Create the position
			tp = new SpTelescopePos( _spItem , _avTab , tag , this ) ;
			tp.noNotifySetXY( xaxis , yaxis ) ;

			_placeInPosList( tp ) ;
		}
		_notifyOfAdd( tp ) ;
		return tp ;
	}

	/**
     * Create a position with the given tag and position as Strings and add it
     * to the A/V table. If the given tag is a USER_TAG, a unique tag will be
     * generated and assigned to the created position. Returns the new
     * SpTelescopePos object.
     */
	public SpTelescopePos createPosition( String tag , String xaxis , String yaxis , int coordSys )
	{
		SpTelescopePos tp ;
		synchronized( this )
		{
			// If this is a user tag, generate a unique tag
			if( tag.equals( SpTelescopePos.USER_TAG ) )
			{
				tag = _getUniqueUserTag() ;
				_addUserPosition( tag ) ;
			}
			else
			{
				// See if this tag already exists in the position list
				tp = ( SpTelescopePos )getPosition( tag ) ;
				if( tp != null )
					return tp ;
			}

			// Create the position
			tp = new SpTelescopePos( _spItem , _avTab , tag , this ) ;
			double[] xy = RADecMath.string2Degrees( xaxis , yaxis , coordSys ) ;
			tp.noNotifySetXY( xy[ 0 ] , xy[ 1 ] ) ;

			_placeInPosList( tp ) ;
		}
		_notifyOfAdd( tp ) ;
		return tp ;
	}

	//
	// Removes the position with the given tag from the position list.
	//
	private synchronized SpTelescopePos _removeFromPosList( String tag )
	{
		for( int i = 0 ; i < _posList.size() ; ++i )
		{
			SpTelescopePos tp = _posList.elementAt( i ) ;
			if( tp.getTag().equals( tag ) )
			{
				_posList.removeElementAt( i ) ;
				tp.deleteWatchers() ;
				return tp ;
			}
		}
		return null ; // Not Found
	}

	//
	// Remove the given position, without notifying observers.
	//
	private synchronized SpTelescopePos _removePosition( SpTelescopePos tp )
	{
		// Don't know anything about this position
		if( tp.getTag() == null )
			return null ;

		SpTelescopePos rmPos = _removeFromPosList( tp.getTag() ) ;
		if( rmPos == null )
			return null ;

		// If this is a user tag, remove it from the list of user positions
		if( tp.getTag().startsWith( SpTelescopePos.USER_TAG ) )
		{
			Vector<String> v = _avTab.getAll( USER_POS_LIST ) ;
			int i = v.indexOf( tp.getTag() ) ;
			if( i != -1 )
				_avTab.rm( USER_POS_LIST , i ) ;
		}

		_avTab.rm( tp.getTag() ) ;
		return rmPos ;
	}

	/**
     * Remove the given position from the given A/V table. This is a public
     * removePosition method. It calls the private version to do the work and
     * then notifies observers.
     */
	public void removePosition( TelescopePos tp )
	{
		if( !( tp instanceof SpTelescopePos ) )
			return ;
		SpTelescopePos rmPos = _removePosition( ( SpTelescopePos )tp ) ;
		if( rmPos != null )
			_notifyOfRemove( tp ) ;
	}

	//
	// Change the tag of a SpTelescopePos, thus potentially effecting its
	// position in the list. This is the private changeTag method. It
	// does all the real work, but doesn't notify observers. Returns
	// true if anything actually changed.
	//
	private synchronized boolean _changeTag( SpTelescopePos tp , String newTag )
	{
		String oldTag = tp.getTag() ;

		if( oldTag.startsWith( SpTelescopePos.USER_TAG ) )
			oldTag = SpTelescopePos.USER_TAG ;

		if( oldTag.equals( newTag ) )
			return false ;

		// Allow any change to a new USER_TAG
		if( newTag.equals( SpTelescopePos.USER_TAG ) )
		{
			// Remove the TP from its old place
			_removePosition( tp ) ;

			String tag = _getUniqueUserTag() ;
			_addUserPosition( tag ) ;

			// NOTE: this causes the tp to notifyObservers -- one of which is
            // the
			// TelescopePosTableWidget (tptw). tptw can't find the position
            // because
			// its tag has changed. That's okay since tptw will just have to
			// resync with the SpTelescopePosList anyway.
			tp.setTag( tag ) ;

			// Move the position to its new place
			_placeInPosList( tp ) ;
			return true ;
		}

		// Changing to one of the unique types. See if a position with the
		// tag already exists.
		SpTelescopePos existingTP = ( SpTelescopePos )getPosition( newTag ) ;
		// Move this one to an ordinary user tag
		if( existingTP != null )
			_changeTag( existingTP , SpTelescopePos.USER_TAG ) ;

		_removePosition( tp ) ;
		tp.setTag( newTag ) ;
		_placeInPosList( tp ) ;

		// If we've made tp the new base position, update the SpObsData
		if( tp.isBasePosition() )
			_spItem.getObsData().setBasePos( tp.getXaxis() , tp.getYaxis() , tp.getCoordSys() ) ;

		return true ;
	}

	/**
     * Change the tag of a SpTelescopePos, thus potentially effecting its
     * position in the list. This is the public changeTag method. It notifies
     * observers.
     */
	public void changeTag( SpTelescopePos tp , String newTag )
	{
		if( _changeTag( tp , newTag ) )
			_notifyOfReorder( tp ) ;
	}

	/**
     * The standard debugging method.
     */
	public synchronized String toString()
	{
		String out = getClass().getName() + "[" ;

		if( _posList.size() >= 1 )
			out += _posList.elementAt( 0 ).toString() ;
		for( int i = 1 ; i < _posList.size() ; ++i )
			out += "," + _posList.elementAt( i ).toString() ;

		return out + "]" ;
	}
}
