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

package gemini.sp.obsComp ;

/**
 * A base class for IR instrument observation component items.
 */
@SuppressWarnings( "serial" )
public class SpStareCapability extends SpInstCapability implements SpInstConstants
{
	public static final String CAPABILITY_NAME = "stare" ;

	/**
	 */
	public SpStareCapability()
	{
		super( CAPABILITY_NAME ) ;
	}

	/**
     * Get the coadds. This was modifed by AB for ORAC to retrun a default of 0
     * rather than 1 if no value is found.
     */
	public int getCoadds()
	{
		int coadds = _avTable.getInt( ATTR_COADDS , 0 ) ;
		return coadds ;
	}

	/**
     * Get the coadds as a string.
     */
	public String getCoaddsAsString()
	{
		return String.valueOf( getCoadds() ) ;
	}

	/**
     * Set the coadds.
     */
	public void setCoadds( int coadds )
	{
		_avTable.set( ATTR_COADDS , coadds ) ;
	}

	/**
     * Set the coadds as a string.
     */
	public void setCoadds( String coadds )
	{
		int i = 1 ;
		try
		{
			i = Integer.parseInt( coadds ) ;
		}
		catch( Exception ex ){}

		_avTable.set( ATTR_COADDS , i ) ;
	}
}
