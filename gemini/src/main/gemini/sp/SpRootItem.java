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

import gemini.util.Version ;

/**
 * A root of the program hierarchy.
 * 
 * @see SpProg
 * @see SpPlan
 * @see SpLibrary
 */
@SuppressWarnings( "serial" )
public class SpRootItem extends SpObsContextItem
{
	/** The version of the OT used to generate this file */
	public static final String ATTR_OT_VERSION = "ot_version" ;

	/** The telescope name */
	public static final String ATTR_TELESCOPE = "telescope" ;

	/**
     * Default constructor.
     */
	protected SpRootItem( SpType spType )
	{
		super( spType ) ;
		setTelescope() ;
		setEditFSM( new SpEditState( this ) ) ;
	}

	/**
     * Override clone to set the SpEditState.
     */
	protected Object clone()
	{
		SpItem spClone = ( SpItem )super.clone() ;
		spClone.setEditFSM( new SpEditState( spClone ) ) ;
		return spClone ;
	}

	/**
     * Override getTitle to display the program name.
     */
	public String getTitle()
	{
		String title = getTitleAttr() ;
		if( title == null )
			title = type().getReadable() ;

		String nameStr = "" ;
		if( hasBeenNamed() )
			nameStr = " (" + name() + ")" ;

		return title + nameStr ;
	}

	/**
     * Set the telescope name - based on input TELESCOPE
     */

	public void setTelescope()
	{
		String name = "" ;
		name = System.getProperty( "TELESCOPE" ) ;
		if( name == null || name.equals( "" ) )
			name = System.getProperty( "telescope" ) ;
		_avTable.set( ATTR_TELESCOPE , name ) ;
	}

	public void setOTVersion()
	{
		String version = Version.getInstance().getVersion() ;
		_avTable.set( ATTR_OT_VERSION , version ) ;
	}

	public String getOTVersion()
	{
		String version = "unknown" ;
		if( _avTable.get( ATTR_OT_VERSION ) != null )
			version = _avTable.get( ATTR_OT_VERSION ) ;
		return version ;
	}
}
