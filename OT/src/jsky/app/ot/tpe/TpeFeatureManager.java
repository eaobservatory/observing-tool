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
package jsky.app.ot.tpe ;

import java.util.Enumeration ;
import java.util.Hashtable ;
import java.util.Vector ;

import jsky.app.ot.gui.ToggleButtonWidget ;
import jsky.app.ot.gui.ToggleButtonWidgetWatcher ;

/**
 * This is a helper object used to manage the various TpeImageFeatures.
 * This logically belongs with the TelescopePosEditor code but has been
 * moved out to simplify.
 */
final class TpeFeatureManager
{
	private class TpeFeatureData
	{
		TpeImageFeature tif ; // The image feature itself
		ToggleButtonWidget tbw ; // The button used to toggle it

		public TpeFeatureData( TpeImageFeature tif , ToggleButtonWidget tbw )
		{
			this.tif = tif ;
			this.tbw = tbw ;
		}
	}

	private TelescopePosEditor _tpe ;
	private TelescopePosEditorToolBar _tpeToolBar ;
	private TpeImageWidget _iw ;
	private Hashtable<String,TpeFeatureData> _featureMap = new Hashtable<String,TpeFeatureData>() ;

	TpeFeatureManager( TelescopePosEditor tpe , TpeImageWidget iw )
	{
		_tpe = tpe ;
		_tpeToolBar = _tpe.getTpeToolBar() ;
		_iw = iw ;

		// Hide all the toggle buttons
		Enumeration<ToggleButtonWidget> e = _enumerateToggleButtons() ;
		while( e.hasMoreElements() )
		{
			ToggleButtonWidget tbw = e.nextElement() ;
			tbw.setVisible( false ) ;
		}
	}

	//
	// Get an enumeration of the toggle view buttons.
	//
	private Enumeration<ToggleButtonWidget> _enumerateToggleButtons()
	{
		return new Enumeration<ToggleButtonWidget>()
		{
			private int i = 0 ;

			public boolean hasMoreElements()
			{
				try
				{
					return( _tpeToolBar.getViewToggleButton( i ) != null ) ;
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					return false ;
				}
			}

			public ToggleButtonWidget nextElement()
			{
				return _tpeToolBar.getViewToggleButton( i++ ) ;
			}
		} ;
	}

	//
	// Get an unoccupied toggle button.
	//
	private ToggleButtonWidget _allocateToggleButton()
	{
		// Find an open toggle button
		ToggleButtonWidget tbw = null ;
		Enumeration<ToggleButtonWidget> e = _enumerateToggleButtons() ;
		while( e.hasMoreElements() )
		{
			ToggleButtonWidget tmp = e.nextElement() ;
			if( !( tmp.isVisible() ) )
			{
				tbw = tmp ;
				break ;
			}
		}
		return tbw ;
	}

	//
	// Make a tool button available for allocation again.
	//
	private void _freeToggleButton( ToggleButtonWidget tbw )
	{
		tbw.setVisible( false ) ;
		tbw.deleteWatchers() ;
	}

	/**
	 * Add a feature.
	 */
	public boolean addFeature( final TpeImageFeature tif )
	{
		// See if this feature is already present.
		if( _featureMap.get( tif.getName() ) != null )
			return true ;

		ToggleButtonWidget tbw = _allocateToggleButton() ;
		if( tbw == null )
			return false ;

		_featureMap.put( tif.getName() , new TpeFeatureData( tif , tbw ) ) ;

		tbw.addWatcher( new ToggleButtonWidgetWatcher()
		{
			public void toggleButtonAction( ToggleButtonWidget tbw )
			{
				if( tbw.getBooleanValue() )
					_iw.addFeature( tif ) ;
				else
					_iw.deleteFeature( tif ) ;
			}
		} ) ;

		tbw.setText( tif.getName() ) ;
		tbw.setToolTipText( tif.getDescription() ) ;
		tbw.setVisible( true ) ;
		tbw.setEnabled( true ) ;

		// hack, but need to enable this somewhere, otherwise catalog plotting wouldn't (shouldn't) be displayed
		if( tif.getName().equals( "Catalog" ) )
			tbw.setSelected( true ) ;

		return true ;
	}

	/**
	 * Delete a feature.
	 */
	public void deleteFeature( TpeImageFeature tif )
	{
		setVisible( tif , false ) ;

		TpeFeatureData tfd = _featureMap.get( tif.getName() ) ;
		if( tfd == null )
			return ;

		ToggleButtonWidget tbw = tfd.tbw ;
		_freeToggleButton( tbw ) ;
		_featureMap.remove( tif.getName() ) ;
	}

	/**
	 * Get the named feature.
	 */
	public TpeImageFeature getFeature( String name )
	{
		TpeFeatureData tfd = _featureMap.get( name ) ;
		if( tfd == null )
			return null ;

		return tfd.tif ;
	}

	/**
	 * Is the given feature already added?
	 */
	public boolean isFeaturePresent( TpeImageFeature tif )
	{
		return( getFeature( tif.getName() ) != null ) ;
	}

	/**
	 * Toggle viewing of the given image feature.
	 */
	public void toggleFeature( TpeImageFeature tif )
	{
		TpeFeatureData tfd = _featureMap.get( tif.getName() ) ;
		if( tfd == null )
			return ;

		tfd.tbw.press() ;
	}

	/**
	 * Make sure the given feature is displayed/hidden.
	 */
	public void setVisible( TpeImageFeature tif , boolean visible )
	{
		if( visible != tif.isVisible() )
			toggleFeature( tif ) ;
	}

	/**
	 * Disable/enable the given feature.
	 */
	public void setDisabled( TpeImageFeature tif , boolean disabled )
	{
		TpeFeatureData tfd = _featureMap.get( tif.getName() ) ;
		if( tfd == null )
			return ;

		setVisible( tif , !disabled ) ;
		tfd.tbw.setEnabled( !disabled ) ;
	}

	/**
	 * Disable/enable the given features.
	 */
	public void setDisabled( Vector<TpeImageFeature> tifV , boolean disabled )
	{
		for( int i = 0 ; i < tifV.size() ; ++i )
		{
			TpeImageFeature tif = tifV.elementAt( i ) ;
			setDisabled( tif , disabled ) ;
		}
	}
}
