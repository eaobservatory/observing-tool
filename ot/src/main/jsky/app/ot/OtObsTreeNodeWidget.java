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
//
package jsky.app.ot ;

import gemini.sp.SpItem ;
import java.util.Observable ;

/**
 * A TreeNodeWidget for SpObs items.  A special OtTreeNodeWidget is
 * required for two reasons:
 * <ul>
 * <li> to handle CTRL-click events that toggle the "chained-to-next"
 *      state of the observation, and
 * <li> to handle the creation of SpObsLinks when CTRL-dragging an
 *      observation.
 * </ul>
 */
@SuppressWarnings( "serial" )
public class OtObsTreeNodeWidget extends OtTreeNodeWidget
{
	public OtObsTreeNodeWidget(){}

	public OtObsTreeNodeWidget( OtTreeWidget tree )
	{
		super( tree ) ;
	}

	/**
	 * Implement copy() to make this class concrete.
	 */
	public OtTreeNodeWidget copy()
	{
		OtObsTreeNodeWidget newTNW = new OtObsTreeNodeWidget( ( OtTreeWidget )tree ) ;
		super.copyInto( newTNW ) ;
		return newTNW ;
	}

	/**
	 * Set the Science Program item to edit.  This method should be
	 * called once, before the object is used.
	 */
	public void setItem( SpItem spItem )
	{
		super.setItem( spItem ) ;
	}

	/**
	 * Get the chained state of the observation associated with this node.
	 */
	public boolean getObsChained()
	{
		return false ;
	}

	/**
	 * Set the chained state of the observation associated with this node.
	 */
	public void setObsChained( boolean chained ){}

	/**
	 * Observer of chain state changes.  Note that the superclass,
	 * OtTreeNodeWidget implements Observable, we are simply overriding
	 * its definition.
	 */
	public void update( Observable o , Object arg )
	{
		super.update( o , arg ) ;
	}
}
