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

import gemini.util.Assert ;

/**
 * The observation link item.
 */
@SuppressWarnings( "serial" )
public class SpObsLink extends SpItem
{

	/**
     * Default constructor.
     */
	protected SpObsLink()
	{
		super( SpType.OBSERVATION_LINK ) ;
	}

	/**
     * Construct with the observation that this link points to.
     */
	protected SpObsLink( SpObs spObs )
	{
		super( SpType.OBSERVATION_LINK ) ;
		linkTo( spObs ) ;
	}

	/**
     * Link to the given observation.
     */
	public void linkTo( SpObs spObs )
	{
		Assert.notFalse( spObs.hasBeenNamed() ) ;

		// Put a copy of the observation as this link's only child
		SpItem spItem = spObs.deepCopy() ;
		spItem.name( spObs.name() ) ;

		SpItem[] spItemA = { spItem } ;
		insert( spItemA , null ) ;

		// Add a ".target" attribute
		_avTable.set( ".target" , spObs.name() ) ;
	}
}
