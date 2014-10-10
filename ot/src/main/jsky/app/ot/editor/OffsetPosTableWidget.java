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

package jsky.app.ot.editor ;

import java.util.Vector ;

import jsky.app.ot.gui.TableWidgetExt ;

import gemini.sp.SpOffsetPos ;
import gemini.sp.SpOffsetPosList ;

import gemini.util.TelescopePos ;
import gemini.util.TelescopePosList ;
import gemini.util.TelescopePosListWatcher ;
import gemini.util.TelescopePosSelWatcher ;
import gemini.util.TelescopePosWatcher ;

/**
 * An extension of the TableWidget to support telescope offset lists.
 */
@SuppressWarnings( "serial" )
public class OffsetPosTableWidget extends TableWidgetExt implements TelescopePosWatcher , TelescopePosListWatcher , TelescopePosSelWatcher
{
	private SpOffsetPosList _opl ;
	private TelescopePos[] _tpArray ;

	/**
	 */
	public OffsetPosTableWidget(){}

	/**
	 * The current position location has changed.
	 * 
	 * @see TelescopePosWatcher
	 */
	public void telescopePosLocationUpdate( TelescopePos tp )
	{
		telescopePosGenericUpdate( tp ) ;
	}

	/**
	 * The current position has changed in some way.
	 * 
	 * @see TelescopePosWatcher
	 */
	public void telescopePosGenericUpdate( TelescopePos tp )
	{
		if( !( tp instanceof SpOffsetPos ) )
		{
			// This shouldn't happen ...
			System.out.println( getClass().getName() + ": received a position " + " update for a non-offset position: " + tp ) ;
			return ;
		}
		_updatePos( ( SpOffsetPos )tp ) ;
	}

	/**
	 *
	 */
	public void reinit( SpOffsetPosList opl )
	{
		if( ( _opl != opl ) && ( _opl != null ) )
		{
			// Quit watching previous positions
			_opl.deleteWatcher( this ) ;
			_opl.deleteSelWatcher( this ) ;

			TelescopePos[] tpA = _opl.getAllPositions() ;
			for( int i = 0 ; i < tpA.length ; ++i )
			{
				SpOffsetPos op = ( SpOffsetPos )tpA[ i ] ;
				op.deleteWatcher( this ) ;
			}
		}

		// Watch the new list, and add all of its positions at once
		_opl = opl ;
		_opl.addWatcher( this ) ;
		_opl.addSelWatcher( this ) ;
		_tpArray = _opl.getAllPositions() ;
		_insertAllPos( _tpArray ) ;
	}

	/**
	 * A position has been selected so select the corresponding table row.
	 *
	 * @see TelescopePosSelWatcher
	 */
	public void telescopePosSelected( TelescopePosList tpl , TelescopePos tp )
	{
		if( tpl != _opl )
			return ;

		int index = _opl.getPositionIndex( tp ) ;
		if( index == -1 )
			return ;

		_selectRowAt( index , false ) ;
	}

	/**
	 * @see TelescopePosListWatcher
	 */
	public void posListReset( TelescopePosList tpl , TelescopePos[] newList )
	{
		if( tpl != _opl )
			return ;
		_tpArray = newList ;
		_insertAllPos( newList ) ;
		selectRowAt( 0 ) ;
	}

	/**
	 * @see TelescopePosListWatcher
	 */
	public void posListReordered( TelescopePosList tpl , TelescopePos[] newList , TelescopePos tp )
	{
		if( tpl != _opl )
			return ;
		_tpArray = newList ;
		_insertAllPos( newList ) ;
		selectPos( tp ) ;
	}

	/**
	 * @see TelescopePosListWatcher
	 */
	public void posListAddedPosition( TelescopePosList tpl , TelescopePos tp )
	{
		if( tpl != _opl )
			return ;
		_tpArray = tpl.getAllPositions() ;
		_insertAllPos( _tpArray ) ;
		selectPos( tp ) ;
	}

	/**
	 * @see TelescopePosListWatcher
	 */
	public void posListRemovedPosition( TelescopePosList tpl , TelescopePos tp )
	{
		if( tpl != _opl )
			return ;

		// Get the index of the position before it was removed.
		int index = -1 ;
		for( int i = 0 ; i < _tpArray.length ; ++i )
		{
			if( _tpArray[ i ] == tp )
			{
				index = i ;
				break ;
			}
		}
		
		if( index == -1 )
			return ;

		// Get the currently selected row
		int[] selA = getSelectedRowIndexes() ;
		int sel = -1 ;
		if( selA.length != 0 )
			sel = selA[ 0 ] ;

		_tpArray = tpl.getAllPositions() ;
		_insertAllPos( _tpArray ) ;

		// Select the correct position
		int rc = getRowCount() ;
		if( rc > 0 )
		{
			if( sel == -1 )
			{
				selectRowAt( 0 ) ;
			}
			else
			{
				if( ( sel > index ) || ( sel == rc ) )
					--sel ;
	
				if( sel >= 0 )
					selectRowAt( sel ) ;
			}
		}
	}

	/**
	 */
	private Vector<String> _createPosRow( SpOffsetPos op , int index )
	{
		Vector<String> v = new Vector<String>( 4 ) ;
		v.addElement( String.valueOf( index ) ) ;
		v.addElement( op.getXaxisAsString() ) ;
		v.addElement( op.getYaxisAsString() ) ;
		return v ;
	}

	/**
	 */
	@SuppressWarnings( "unchecked" )
    private void _insertAllPos( TelescopePos[] tpA )
	{
		Vector<String>[] dataV = new Vector[ tpA.length ] ;

		for( int i = 0 ; i < tpA.length ; ++i )
		{
			SpOffsetPos op = ( SpOffsetPos )tpA[ i ] ;
			op.addWatcher( this ) ;
			dataV[ i ] = _createPosRow( op , i ) ;
		}
		setRows( dataV ) ;
	}

	/**
	 */
	private void _updatePos( SpOffsetPos op )
	{
		int index = _opl.getPositionIndex( op ) ;
		if( index != -1 )
		{
			Vector<String> v = _createPosRow( op , index ) ;
	
			setCell( v.elementAt( 0 ) , 0 , index ) ;
			setCell( v.elementAt( 1 ) , 1 , index ) ;
			setCell( v.elementAt( 2 ) , 2 , index ) ;
		}
	}

	/**
	 * Get the position that is currently selected.
	 */
	public SpOffsetPos getSelectedPos()
	{
		int[] rows = getSelectedRowIndexes() ;
		if( rows.length == 0 )
			return null ;

		return ( SpOffsetPos )_opl.getPositionAt( rows[ 0 ] ) ;
	}

	/**
	 * Get the position at the given index.
	 */
	public SpOffsetPos getPos( int index )
	{
		return ( SpOffsetPos )_opl.getPositionAt( index ) ;
	}

	private void _selectRowAt( int index , boolean selectPos )
	{
		if( ( index >= 0 ) && ( index < getRowCount() ) )
		{
			// Select the TelescopePos at this row.
			if( selectPos && ( _opl != null ) )
			{
				TelescopePos tp = _opl.getPositionAt( index ) ;
				if( tp != null )
				{
					_opl.deleteSelWatcher( this ) ;
					tp.select() ;
					_opl.addSelWatcher( this ) ;
				}
			}
	
			super.selectRowAt( index ) ;
			focusAtRow( index ) ;
		}
	}

	/**
	 * Override selectRowAt to also set the focus.
	 */
	public void selectRowAt( int index )
	{
		_selectRowAt( index , true ) ;
	}

	/**
	 * Select the position at the given index.
	 */
	public boolean selectPos( int index )
	{
		if( ( index < 0 ) || ( index >= getRowCount() ) )
			return false ;

		selectRowAt( index ) ;
		return true ;
	}

	/**
	 * Select a given position.
	 */
	public boolean selectPos( TelescopePos tp )
	{
		int index = _opl.getPositionIndex( tp ) ;
		if( index == -1 )
			return false ;

		return selectPos( index ) ;
	}

	/**
	 * Select a given position.
	 */
	public boolean selectPos( String tag )
	{
		TelescopePos tp = _opl.getPosition( tag ) ;
		int index = _opl.getPositionIndex( tp ) ;
		if( index == -1 )
			return false ;

		return selectPos( index ) ;
	}
}
