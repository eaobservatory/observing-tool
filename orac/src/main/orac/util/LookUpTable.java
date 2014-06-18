/*
 * Copyright 1999 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package orac.util ;

import java.util.Vector ;
import java.util.NoSuchElementException ;

/**
 * This implements a look up table utility class. The table is implemented
 * as a Vector of Vectors. Objects of any type may be stored in the look up
 * table.
 * @author Alan Bridger, UKATC
 * @version 0.5
 */
@SuppressWarnings( "serial" )
public class LookUpTable extends Vector<String>
{
	private int numRows ;
	private int numColumns ;
	private Vector<Vector<String>> lut ;

	/*
	 * The constructor initializes the lut.
	 */

	/**
	 * Construct the lut with no arguments.
	 */
	public LookUpTable()
	{
		lut = new Vector<Vector<String>>() ;
	}

	/**
	 * Construct the lut with an initial size
	 */
	public LookUpTable( int numRows , int numColumns ) throws NegativeArraySizeException
	{
		if( numRows < 0 || numColumns < 0 )
			throw new NegativeArraySizeException() ;

		lut = new Vector<Vector<String>>( numRows ) ;
		for( int i = 0 ; i < numRows ; i++ )
			lut.addElement( new Vector<String>( numColumns ) ) ;
	}

	/**
	 * Return the number of rows in the lut
	 */
	public int getNumRows()
	{
		return numRows ;
	}

	/**
	 * Return the number of columns in the lut
	 */
	public int getNumColumns()
	{
		return numColumns ;
	}

	/**
	 * Add a row to the lut. The row is added to the end of the table.
	 */
	public void addRow( Vector<String> row )
	{
		lut.addElement( row ) ;
		numRows = lut.size() ;
		numColumns = Math.max( numColumns , row.size() ) ;
	}

	/**
	 * Add a column to the lut. The column is added to the end of the table.
	 * THIS METHOD IS NOT IMPLEMENTED.
	 */
	public void addColumn( Vector<String> column ){}

	/**
	 * Insert an object at a specific position.
	 * THIS METHOD IS NOT IMPLEMENTED
	 */
	public void insertElementAt( String obj , int rowPos , int colPos ){}

	/**
	 * Insert a row at a specific position.
	 * THIS METHOD IS NOT IMPLEMENTED
	 */
	public void insertRowAt( Vector<String> row , int rowPos ){}

	/**
	 * Insert a column at a specific position.
	 * THIS METHOD IS NOT IMPLEMENTED
	 */
	public void insertColumnAt( Vector<String> column , int colPos ){}

	/**
	 * Set the object at a specific position.
	 */
	public void setElementAt( String obj , int rowPos , int colPos ) throws ArrayIndexOutOfBoundsException
	{
		if( rowPos < 0 || rowPos >= numRows || colPos < 0 || colPos >= numColumns )
			throw new ArrayIndexOutOfBoundsException() ;

		Vector<String> vRow = new Vector<String>() ;
		vRow = this.getRow( rowPos ) ;
		vRow.setElementAt( obj , colPos ) ;
		this.setRowAt( vRow , rowPos ) ;
	}

	/**
	 * Set the row at a specific position
	 */
	public void setRowAt( Vector<String> row , int rowPos ) throws ArrayIndexOutOfBoundsException
	{
		if( rowPos < 0 || rowPos >= numRows )
			throw new ArrayIndexOutOfBoundsException() ;

		lut.setElementAt( row , rowPos ) ;
	}

	/**
	 * Set the column at a specific position
	 * THIS METHOD IS NOT IMPLEMENTED
	 */
	public void setColumnAt( Vector<String> column , int colPos ){}

	/**
	 * getAsVectorArray returns the whole LookUpTable as a Vector Array
	 */
	@SuppressWarnings( "unchecked" )
    public Vector<String>[] getAsVectorArray()
	{
		Vector<String>[] v = new Vector[ numRows ] ;
		for( int i = 0 ; i < numRows ; i++ )
			v[ i ] = this.getRow( i ) ;

		return v ;
	}

	/**
	 * getRow returns a Vector of the objects in the specified row
	 */
	public Vector<String> getRow( int row ) throws ArrayIndexOutOfBoundsException
	{
		Vector<String> vRow = new Vector<String>( numColumns ) ;

		if( row < 0 || row > numRows )
			throw new ArrayIndexOutOfBoundsException() ;

		vRow = lut.elementAt( row ) ;
		return vRow ;
	}

	/**
	 * getColumn returns a Vector of the objects in the specified column
	 */
	public Vector<String> getColumn( int column ) throws ArrayIndexOutOfBoundsException
	{
		Vector<String> vCol = new Vector<String>( numRows ) ;

		if( column < 0 || column > numColumns )
			throw new ArrayIndexOutOfBoundsException() ;

		for( int i = 0 ; i < numRows ; i++ )
		{
			Vector<String> vRow = this.getRow( i ) ;
			vCol.addElement( vRow.elementAt( column ) ) ;
		}
		return vCol ;
	}

	/**
	 * elementAt returns the Object at the given row/column position
	 */
	public String elementAt( int row , int column ) throws ArrayIndexOutOfBoundsException
	{
		if( row < 0 || row >= numRows || column < 0 || column >= numColumns )
			throw new ArrayIndexOutOfBoundsException() ;

		Vector<String> vRow = new Vector<String>( numColumns ) ;
		vRow = this.getRow( row ) ;
		if( vRow == null )
			return null ;
		return vRow.elementAt( column ) ;
	}

	/**
	 * indexInColumn returns the index (position) of a string in the given column
	 */
	public int indexInColumn( String str , int column ) throws ArrayIndexOutOfBoundsException , NoSuchElementException
	{
		if( column < 0 || column >= numColumns )
			throw new ArrayIndexOutOfBoundsException() ;

		Vector<String> vCol = new Vector<String>( numRows ) ;
		vCol = this.getColumn( column ) ;
		for( int i = 0 ; i < numRows ; i++ )
		{
			if( str.equals( vCol.elementAt( i ) ) )
				return i ;
		}
		throw new NoSuchElementException() ;
	}

	/**
	 * indexInRow returns the index (position) of a string in the given row
	 */
	public int indexInRow( String str , int row ) throws ArrayIndexOutOfBoundsException , NoSuchElementException
	{
		if( row < 0 || row >= numRows )
			throw new ArrayIndexOutOfBoundsException() ;

		Vector<String> vRow = new Vector<String>( numColumns ) ;
		vRow = this.getRow( row ) ;
		for( int i = 0 ; i < numColumns ; i++ )
		{
			if( str.equals( vRow.elementAt( i ) ) )
				return i ;
		}
		throw new NoSuchElementException() ;
	}

	/**
	 * rangeInColumn searches through a column of values and returns the row 
	 * position for which the column value, interpreted as a double, is the
	 * smallest one greater than the argument given
	 */
	public int rangeInColumn( double dval , int column ) throws ArrayIndexOutOfBoundsException
	{
		if( column < 0 || column >= numColumns )
			throw new ArrayIndexOutOfBoundsException() ;

		Vector<String> vCol = new Vector<String>( numRows ) ;
		vCol = this.getColumn( column ) ;
		for( int i = 0 ; i < numRows ; i++ )
		{
			try
			{
				if( new Double( vCol.elementAt( i ) ) < dval )
					continue ;
			}
			catch( NumberFormatException ex )
			{
				continue ;
			}
			return i ;
		}
		return -1 ;
	}

	/**
	 * rangeInRow searches through a row of values and returns the column 
	 * position for which the row value, interpreted as a double, is the
	 * smallest one greater than the argument given
	 */
	public int rangeInRow( double dval , int row ) throws ArrayIndexOutOfBoundsException
	{
		if( row < 0 || row >= numRows )
			throw new ArrayIndexOutOfBoundsException() ;

		Vector<String> vRow = new Vector<String>( numColumns ) ;
		vRow = this.getRow( row ) ;
		for( int i = 0 ; i < numColumns ; i++ )
		{
			try
			{
				if( new Double( vRow.elementAt( i ) ) < dval )
					continue ;
			}
			catch( NumberFormatException ex )
			{
				continue ;
			}
			return i ;
		}
		return -1 ;
	}
}
