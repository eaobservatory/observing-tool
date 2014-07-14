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
package gemini.sp ;

final class SpInsertInfo implements SpInsertConstants
{

	/**
     * What type of insertion?
     * 
     * @see SpInsertConstants
     */
	public int result ;

	/** Insert relative to this item. */
	public SpItem referant ;

	/**
     * The item that will be replaced by this insertion (if any). Items are
     * replaced when a newly inserted item must be unique in its scope, but is
     * being inserted into a scope that already contains an item of its type.
     * For instance, an instrument must be unique in its scope.
     */
	public SpItem replaceItem ;

	SpInsertInfo( int result , SpItem referant )
	{
		this.result = result ;
		this.referant = referant ;
	}

	SpInsertInfo( int result , SpItem referant , SpItem replaceItem )
	{
		this.result = result ;
		this.referant = referant ;
		this.replaceItem = replaceItem ;
	}

	/**
     * For debugging.
     */
	public String toString()
	{
		String posn ;
		switch( result )
		{
			case INS_INSIDE :
				posn = "INS_INSIDE" ;
				break ;
			case INS_AFTER :
				posn = "INS_AFTER" ;
				break ;
			default :
				posn = "UNKNOWN (" + result + ")" ;
		}
		return getClass().getName() + "[" + posn + ", " + "(" + referant.name() + ", " + referant.typeStr() + ", " + referant.subtypeStr() + ")" + ", replace=" + replaceItem + "]" ;
	}
}
