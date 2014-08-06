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
package jsky.app.ot.editor ;

import java.util.Enumeration ;
import java.util.Hashtable ;
import java.util.Vector ;

import jsky.app.ot.gui.TableWidgetExt ;
import jsky.app.ot.OtCfg ;

import gemini.sp.SpTelescopePos ;
import gemini.sp.SpTelescopePosList ;

import gemini.util.TelescopePos ;
import gemini.util.TelescopePosList ;
import gemini.util.TelescopePosListWatcher ;
import gemini.util.TelescopePosSelWatcher ;
import gemini.util.TelescopePosWatcher ;

/**
 * An extension of the TableWidget to support telescope target lists.
 */
@SuppressWarnings( "serial" )
public class TelescopePosTableWidget extends TableWidgetExt implements TelescopePosWatcher , TelescopePosListWatcher , TelescopePosSelWatcher
{
	private Hashtable<String,TelescopePos> _posTable ;
	private SpTelescopePosList _tpl ;
	private boolean _coordSysInTable = false ; // MFO (April 12, 2002)

	/**
	 * Default constructor.
	 */
	public TelescopePosTableWidget()
	{
		_posTable = new Hashtable<String,TelescopePos>() ;

		// If there are more then 2 systems (FK5/FK4) then display the system of a target in a separate column in the target list table.
		if( ( OtCfg.telescopeUtil.getCoordSys() != null ) && ( OtCfg.telescopeUtil.getCoordSys().length > 2 ) )
			_coordSysInTable = true ;
	}

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
		if( !( tp instanceof SpTelescopePos ) )
		{
			// This shouldn't happen ...
			System.out.println( getClass().getName() + ": received a position " + " update for a non SpTelescopePos position: " + tp ) ;
			return ;
		}
		_updatePos( ( SpTelescopePos )tp ) ;
	}

	/**
	 *
	 */
	public void reinit( SpTelescopePosList tpl )
	{
		if( ( _tpl != null ) && ( _tpl != tpl ) )
		{
			// Quit watching previous positions
			_tpl.deleteWatcher( this ) ;
			_tpl.deleteSelWatcher( this ) ;

			Enumeration<TelescopePos> e = _posTable.elements() ;
			while( e.hasMoreElements() )
			{
				SpTelescopePos tp = ( SpTelescopePos )e.nextElement() ;
				tp.deleteWatcher( this ) ;
			}
		}

		// Watch the new list, and add all of its positions at once
		_tpl = tpl ;
		_tpl.addWatcher( this ) ;
		_tpl.addSelWatcher( this ) ;
		_insertAllPos( _tpl.getAllPositions() ) ;

		selectRowAt( 0 ) ;
	}

	/**
	 * A position has been selected so select the corresponding table row.
	 *
	 * @see TelescopePosSelWatcher
	 */
	public void telescopePosSelected( TelescopePosList tpl , TelescopePos tp )
	{
		if( tpl != _tpl )
			return ;

		int index = _tpl.getPositionIndex( tp ) ;
		if( index == -1 )
			return ;

		_selectRowAt( index , false ) ;
	}

	/**
	 * @see TelescopePosListWatcher
	 */
	public void posListReset( TelescopePosList tpl , TelescopePos[] newList )
	{
		if( tpl != _tpl )
			return ;

		// Quit watching the old positions
		Enumeration<TelescopePos> e = _posTable.elements() ;
		while( e.hasMoreElements() )
		{
			SpTelescopePos tp = ( SpTelescopePos )e.nextElement() ;
			tp.deleteWatcher( this ) ;
		}

		_insertAllPos( newList ) ;
		selectRowAt( 0 ) ;
	}

	/**
	 * @see TelescopePosListWatcher
	 */
	public void posListReordered( TelescopePosList tpl , TelescopePos[] newList , TelescopePos tp )
	{
		if( tpl != _tpl )
		{
			System.out.println( "XXX TelescopePosTableWidget.posListReordered: tpl != _tpl" ) ;
			return ;
		}
		_insertAllPos( newList ) ;
		selectPos( tp ) ;
	}

	/**
	 * @see TelescopePosListWatcher
	 */
	public void posListAddedPosition( TelescopePosList tpl , TelescopePos tp )
	{
		if( tpl != _tpl )
			return ;

		// Add the row to the table
		int i = tpl.getPositionIndex( tp ) ;
		Vector<String> v = _createPosRow( ( SpTelescopePos )tp ) ;
		absInsertRowAt( v , i ) ;

		// Add the position to the posTable and observe it.
		_posTable.put( tp.getTag() , tp ) ;
		tp.addWatcher( this ) ;

		// Select the new position
		selectPos( tp ) ;
	}

	/**
	 * @see TelescopePosListWatcher
	 */
	public void posListRemovedPosition( TelescopePosList tpl , TelescopePos tp )
	{
		if( tpl != _tpl )
			return ;

		// Find the position in the table
		int index = -1 ;
		for( int i = 0 ; i < getRowCount() ; ++i )
		{
			String tag = ( String )getCell( 0 , i ) ;
			if( tp.getTag().equals( tag ) )
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

		// Remove the row
		removeRowAt( index ) ;
		tp.deleteWatcher( this ) ;
		_posTable.remove( tp.getTag() ) ;

		// Select the correct position
		int rc = getRowCount() ;
		if( rc <= 0 )
			return ;

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

	/**
	 */
	private Vector<String> _createPosRow( SpTelescopePos tp )
	{
		Vector<String> v = new Vector<String>() ;
		v.addElement( tp.getTag() ) ;
		v.addElement( tp.getName() ) ;

		if( tp.getSystemType() == SpTelescopePos.SYSTEM_SPHERICAL || tp.getTag().equals( "REFERENCE" ) )
		{
			if( tp.isOffsetPosition() )
			{
				v.addElement( "" + tp.getXaxisAsString() + " (\u2206)" ) ;
				v.addElement( "" + tp.getYaxisAsString() + " (\u2206)" ) ;
			}
			else
			{
				v.addElement( tp.getXaxisAsString() ) ;
				v.addElement( tp.getYaxisAsString() ) ;
			}
		}
		else
		{
			v.addElement( "  - - -" ) ;
			v.addElement( "  - - -" ) ;
		}

		if( tp.getTag().equals( "REFERENCE" ) )
		{
			v.addElement( tp.getCoordSysAsString() ) ;
		}
		else if( _coordSysInTable )
		{
			switch( tp.getSystemType() )
			{
				case SpTelescopePos.SYSTEM_CONIC :
					v.addElement( "Orb. Elem." ) ;
					break ;
				case SpTelescopePos.SYSTEM_NAMED :
					v.addElement( "Planets etc." ) ;
					break ;
                                case SpTelescopePos.SYSTEM_TLE:
                                        v.addElement("TLE");
                                        break;
				case SpTelescopePos.SYSTEM_SPHERICAL :
					v.addElement( tp.getCoordSysAsString() ) ;
					break ;
			}
		}
		return v ;
	}

	/**
	 */
	@SuppressWarnings( "unchecked" )
    private void _insertAllPos( TelescopePos[] tpA )
	{
		Vector<String>[] dataV = new Vector[ tpA.length ] ;

		_posTable.clear() ;
		for( int i = 0 ; i < tpA.length ; ++i )
		{
			SpTelescopePos tp = ( SpTelescopePos )tpA[ i ] ;
			tp.addWatcher( this ) ;
			_posTable.put( tp.getTag() , tp ) ;
			dataV[ i ] = _createPosRow( tp ) ;
		}
		setRows( dataV ) ;
	}

	/**
	 */
	private void _updatePos( SpTelescopePos tp )
	{
		int index = findPosIndex( tp.getTag() ) ;
		if( index == -1 )
			return ;

		Vector<String> v = _createPosRow( tp ) ;

		setCell( v.elementAt( 0 ) , 0 , index ) ;
		setCell( v.elementAt( 1 ) , 1 , index ) ;
		setCell( v.elementAt( 2 ) , 2 , index ) ;
		setCell( v.elementAt( 3 ) , 3 , index ) ;

		if( _coordSysInTable )
			setCell( v.elementAt( 4 ) , 4 , index ) ;
	}

	/**
	 * Get the position that is currently selected.
	 */
	public SpTelescopePos getSelectedPos()
	{
		int[] rows = getSelectedRowIndexes() ;
		if( rows.length == 0 )
			return null ;

		String tag = ( String )getCell( 0 , rows[ 0 ] ) ;
		return ( SpTelescopePos )_posTable.get( tag ) ;
	}

	/**
	 * Get the index of the position with the given tag.
	 */
	public int findPosIndex( String tag )
	{
		int index = -1 ;
		for( int i = 0 ; i < getRowCount() ; ++i )
		{
			String s = ( String )getCell( 0 , i ) ;
			if( s.equals( tag ) )
			{
				index = i ;
				break ;
			}
		}
		return index ;
	}

	/**
	 * Find the position with the given tag.
	 */
	public SpTelescopePos getPos( String tag )
	{
		return ( SpTelescopePos )_posTable.get( tag ) ;
	}

	private void _selectRowAt( int index , boolean selectPos )
	{
		if( ( index >= 0 ) && ( index < getRowCount() ) )
		{
			// Select the TelescopePos at this row
			if( selectPos && ( _tpl != null ) )
			{
				TelescopePos tp = _tpl.getPositionAt( index ) ;
				if( tp != null )
				{
					_tpl.deleteSelWatcher( this ) ;
					tp.select() ;
					_tpl.addSelWatcher( this ) ;
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
	 * Select the position with the given tag.
	 */
	public boolean selectPos( String tag )
	{
		int index = findPosIndex( tag ) ;
		if( index == -1 )
			return false ;

		selectRowAt( index ) ;
		return true ;
	}

	/**
	 * Select a given position.
	 */
	public boolean selectPos( TelescopePos tp )
	{
		return selectPos( tp.getTag() ) ;
	}

	/**
	 */
	public String toString()
	{
		String head = getClass().getName() + "[\n" ;
		String body = "" ;
		for( int row = 0 ; row < getRowCount() ; ++row )
		{
			for( int col = 0 ; col < getColumnCount() ; ++col )
				body += "\t" + ( String )getCell( col , row ) ;

			body += "\n" ;
		}
		body += "]" ;
		return head + body ;
	}
}
