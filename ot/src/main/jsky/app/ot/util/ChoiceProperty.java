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
package jsky.app.ot.util ;

/**
 * This class represents an object property that consists of a set of
 * possible string choices, once of which is the "current" choice.
 */
public class ChoiceProperty
{
	// Choices
	private String[] _choices ;

	// The current choice index.
	private int _curValue = -1 ;

	/**
	 * Construct with the set of choices.  The initial choice is the
	 * 1'st element (index 0).
	 */
	public ChoiceProperty( String[] choices )
	{
		_choices = choices ;
		if( choices.length > 0 )
			_curValue = 0 ;
	}

	/**
	 * Get the set of choices that are available.
	 */
	public String[] getChoices()
	{
		return _choices ;
	}

	/**
	 * Get the index of the current choice.
	 */
	public int getCurValue()
	{
		return _curValue ;
	}

	/**
	 * Set the current choice (index).
	 */
	public void setCurValue( int value )
	{
		if( value >= _choices.length )
			_curValue = _choices.length - 1 ;
		else if( value < 0 )
			_curValue = 0 ;
		else
			_curValue = value ;
	}

}
