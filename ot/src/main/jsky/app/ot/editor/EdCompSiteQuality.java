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

package jsky.app.ot.editor ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

import javax.swing.ButtonGroup ;

import jsky.app.ot.gui.OptionWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

import gemini.sp.obsComp.SpSiteQualityObsComp ;

/**
 * This is the editor for Site Quality component.
 */
public final class EdCompSiteQuality extends OtItemEditor implements ActionListener , TextBoxWidgetWatcher
{
	private SiteQualityGUI _w ; // the GUI layout panel

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdCompSiteQuality()
	{
		_title = "Site Quality" ;
		_presSource = _w = new SiteQualityGUI() ;
		_description = "Looser constraints than those allocated by the TAG are ignored at the telescope." ;

		// add action listeners and group the buttons
		ButtonGroup grp ;

		_w.seeingAllocated.addActionListener( this ) ;
		_w.seeingUserDefined.addActionListener( this ) ;
		_w.minSeeing.addWatcher( this ) ;
		_w.maxSeeing.addWatcher( this ) ;

		grp = new ButtonGroup() ;
		grp.add( _w.seeingAllocated ) ;
		grp.add( _w.seeingUserDefined ) ;

		_w.tauBandAllocated.addActionListener( this ) ;
		_w.tauBandUserDefined.addActionListener( this ) ;
		_w.minTau.addWatcher( this ) ;
		_w.maxTau.addWatcher( this ) ;

		grp = new ButtonGroup() ;
		grp.add( _w.tauBandAllocated ) ;
		grp.add( _w.tauBandUserDefined ) ;

		_w.skyAllocated.addActionListener( this ) ;
		_w.skyUserDefined.addActionListener( this ) ;
		_w.minSky.addWatcher( this ) ;
		_w.maxSky.addWatcher( this ) ;

		grp = new ButtonGroup() ;
		grp.add( _w.skyAllocated ) ;
		grp.add( _w.skyUserDefined ) ;

		_w.moonDark.addActionListener( this ) ;
		_w.moonGrey.addActionListener( this ) ;
		_w.moonAny.addActionListener( this ) ;

		grp = new ButtonGroup() ;
		grp.add( _w.moonDark ) ;
		grp.add( _w.moonGrey ) ;
		grp.add( _w.moonAny ) ;

		_w.cloudPhotometric.addActionListener( this ) ;
		_w.cloudThinCirrus.addActionListener( this ) ;
		_w.cloudAny.addActionListener( this ) ;

		grp = new ButtonGroup() ;
		grp.add( _w.cloudPhotometric ) ;
		grp.add( _w.cloudThinCirrus ) ;
		grp.add( _w.cloudAny ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		SpSiteQualityObsComp sq = ( SpSiteQualityObsComp )_spItem ;
		OptionWidgetExt ow ;
		int i ;

		// Image Quality
		if( sq.tauBandAllocated() )
		{
			_w.tauBandAllocated.setValue( true ) ;
			_w.minTau.setValue( "" ) ;
			_w.maxTau.setValue( "" ) ;
			_w.minTau.setEnabled( false ) ;
			_w.maxTau.setEnabled( false ) ;
		}
		else
		{
			_w.tauBandUserDefined.setValue( true ) ;
			_w.minTau.setValue( sq.getMinTau() ) ;
			_w.maxTau.setValue( sq.getMaxTau() ) ;
			_w.minTau.setEnabled( true ) ;
			_w.maxTau.setEnabled( true ) ;
		}

		// IR Background
		if( sq.seeingAllocated() )
		{
			_w.seeingAllocated.setValue( true ) ;
			_w.minSeeing.setValue( "" ) ;
			_w.maxSeeing.setValue( "" ) ;
			_w.minSeeing.setEnabled( false ) ;
			_w.maxSeeing.setEnabled( false ) ;
		}
		else
		{
			_w.seeingUserDefined.setValue( true ) ;
			_w.minSeeing.setValue( sq.getMinSeeing() ) ;
			_w.maxSeeing.setValue( sq.getMaxSeeing() ) ;
			_w.minSeeing.setEnabled( true ) ;
			_w.maxSeeing.setEnabled( true ) ;
		}

		// J-Band Sky brightness
		if( sq.skyAllocated() )
		{
			_w.skyAllocated.setValue( true ) ;
			_w.minSky.setValue( "" ) ;
			_w.maxSky.setValue( "" ) ;
			_w.minSky.setEnabled( false ) ;
			_w.maxSky.setEnabled( false ) ;
		}
		else
		{
			_w.skyUserDefined.setValue( true ) ;
			_w.minSky.setValue( sq.getMinSky() ) ;
			_w.maxSky.setValue( sq.getMaxSky() ) ;
			_w.minSky.setEnabled( true ) ;
			_w.maxSky.setEnabled( true ) ;
		}

		// Moon
		i = sq.getMoon() ;
		switch( i )
		{
			case SpSiteQualityObsComp.MOON_DARK :
				ow = _w.moonDark ;
				break ;
			case SpSiteQualityObsComp.MOON_GREY :
				ow = _w.moonGrey ;
				break ;
			default :
				ow = _w.moonAny ;
				break ;
		}
		ow.setValue( true ) ;

		// Sky
		i = sq.getCloud() ;
		switch( i )
		{
			case SpSiteQualityObsComp.CLOUD_PHOTOMETRIC :
				ow = _w.cloudPhotometric ;
				break ;
			case SpSiteQualityObsComp.CLOUD_THIN_CIRRUS :
				ow = _w.cloudThinCirrus ;
				break ;
			default :
				ow = _w.cloudAny ;
				break ;
		}
		ow.setValue( true ) ;
	}

	/**
	 * Called when a button is pressed
	 */
	public void actionPerformed( ActionEvent evt )
	{

		Object w = evt.getSource() ;
		SpSiteQualityObsComp sq = ( SpSiteQualityObsComp )_spItem ;

		// Seeing
		if( w == _w.seeingAllocated )
		{
			sq.setSeeingAllocated( true ) ;
			_w.minSeeing.setValue( "" ) ;
			_w.maxSeeing.setValue( "" ) ;
			_w.minSeeing.setEnabled( false ) ;
			_w.maxSeeing.setEnabled( false ) ;
		}
		else if( w == _w.seeingUserDefined )
		{
			sq.setSeeingAllocated( false ) ;
			_w.minSeeing.setValue( sq.getMinSeeing() ) ;
			_w.maxSeeing.setValue( sq.getMaxSeeing() ) ;
			_w.minSeeing.setEnabled( true ) ;
			_w.maxSeeing.setEnabled( true ) ;
		}
		else if( w == _w.tauBandAllocated )
		{
			sq.setTauBandAllocated( true ) ;
			_w.minTau.setValue( "" ) ;
			_w.maxTau.setValue( "" ) ;
			_w.minTau.setEnabled( false ) ;
			_w.maxTau.setEnabled( false ) ;
		}
		else if( w == _w.tauBandUserDefined )
		{
			sq.setTauBandAllocated( false ) ;
			_w.minTau.setValue( sq.getMinTau() ) ;
			_w.maxTau.setValue( sq.getMaxTau() ) ;
			_w.minTau.setEnabled( true ) ;
			_w.maxTau.setEnabled( true ) ;
		}
		else if( w == _w.skyAllocated )
		{
			sq.setSkyAllocated( true ) ;
			_w.minSky.setValue( "" ) ;
			_w.maxSky.setValue( "" ) ;
			_w.minSky.setEnabled( false ) ;
			_w.maxSky.setEnabled( false ) ;
		}
		else if( w == _w.skyUserDefined )
		{
			sq.setSkyAllocated( false ) ;
			_w.minSky.setValue( sq.getMinSky() ) ;
			_w.maxSky.setValue( sq.getMaxSky() ) ;
			_w.minSky.setEnabled( true ) ;
			_w.maxSky.setEnabled( true ) ;
		}
		else if( w == _w.moonDark )
		{
			sq.setMoon( SpSiteQualityObsComp.MOON_DARK ) ;
		}
		else if( w == _w.moonGrey )
		{
			sq.setMoon( SpSiteQualityObsComp.MOON_GREY ) ;
		}
		else if( w == _w.moonAny )
		{
			sq.setMoon( SpSiteQualityObsComp.MOON_ANY ) ;
		}
		else if( w == _w.cloudPhotometric )
		{
			sq.setCloud( SpSiteQualityObsComp.CLOUD_PHOTOMETRIC ) ;
		}
		else if( w == _w.cloudThinCirrus )
		{
			sq.setCloud( SpSiteQualityObsComp.CLOUD_THIN_CIRRUS ) ;
		}
		else if( w == _w.cloudAny )
		{
			sq.setCloud( SpSiteQualityObsComp.CLOUD_ANY ) ;
		}
	}

	/**
	 * Watch changes to min text boxes.
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		if( tbwe == _w.minTau )
			( ( SpSiteQualityObsComp )_spItem ).setMinTau( _w.minTau.getValue() ) ;
		else if( tbwe == _w.maxTau )
			( ( SpSiteQualityObsComp )_spItem ).setMaxTau( _w.maxTau.getValue() ) ;
		else if( tbwe == _w.minSeeing )
			( ( SpSiteQualityObsComp )_spItem ).setMinSeeing( _w.minSeeing.getValue() ) ;
		else if( tbwe == _w.maxSeeing )
			( ( SpSiteQualityObsComp )_spItem ).setMaxSeeing( _w.maxSeeing.getValue() ) ;
		else if( tbwe == _w.minSky )
			( ( SpSiteQualityObsComp )_spItem ).setMinSky( _w.minSky.getValue() ) ;
		else if( tbwe == _w.maxSky )
			( ( SpSiteQualityObsComp )_spItem ).setMaxSky( _w.maxSky.getValue() ) ;
	}

	/**
	 * Text box action, ignore.
	 */
	public void textBoxAction( TextBoxWidgetExt tbwe ){}
}
