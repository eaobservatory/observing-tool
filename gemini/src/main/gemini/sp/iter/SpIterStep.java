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

package gemini.sp.iter ;

/**
 * A "data" holder that describes a single iteration step of an iterator. An
 * iteration step can contain more than one value. For instance, when iterating
 * over a configuration, a single step could change both the filter and the
 * exposure time. In this case, the SpIterStep would contain 2 SpIterValues in
 * its <tt>values</tt> array.
 * 
 * @see SpIterEnumeration
 */
@SuppressWarnings( "serial" )
public class SpIterStep implements java.io.Serializable
{
	/** The title of the iteration step. */
	public String title ;

	/** The iteration step number this step represents. */
	public int stepCount ;

	/**
     * The SpIterComp from which this step comes. Each step is associated with a
     * single SpIterComp.
     */
	public SpIterComp item ;

	/** Attribute/value set from this step. */
	public SpIterValue[] values ;

	private SpIterStep( String title , int stepCount , SpIterComp item )
	{
		this.title = title ;
		this.stepCount = stepCount ;
		this.item = item ;
	}

	public SpIterStep( String title , int stepCount , SpIterComp item , SpIterValue[] values )
	{
		this( title , stepCount , item ) ;
		this.values = values ;
	}

	public SpIterStep( String title , int stepCount , SpIterComp item , SpIterValue value )
	{
		this( title , stepCount , item ) ;
		if( value != null )
		{
			this.values = new SpIterValue[ 1 ] ;
			this.values[ 0 ] = value ;
		}
		else
		{
			this.values = new SpIterValue[ 0 ] ;
		}
	}
}
