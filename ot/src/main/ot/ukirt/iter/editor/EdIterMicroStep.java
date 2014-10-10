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

package ot.ukirt.iter.editor ;

import gemini.sp.iter.SpIterMicroStep ;

import gemini.sp.SpOffsetPosList ;
import gemini.sp.obsComp.SpInstObsComp ;
import gemini.sp.obsComp.SpMicroStepUser ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import java.util.TreeSet ;
import java.util.Vector ;

import jsky.app.ot.editor.OtItemEditor ;

/**
 * This is the editor for MicroStep iterator component.
 *
 * @author converted to MicroStep Iterator component by Martin Folger (M.Folger@roe.ac.uk)
 */
public final class EdIterMicroStep extends OtItemEditor implements ActionListener
{
	private IterMicroStepGUI _w ;

	/**
	 * If true, ignore action events.
	 */
	private boolean ignoreActions = false ;

	/** For internal use only. */
	private Vector<String> _patternVector = new Vector<String>() ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterMicroStep()
	{
		_title = "MicroStep Iterator" ;
		_presSource = _w = new IterMicroStepGUI() ;
		_description = "Use this editor to specify a microstep pattern." ;

		_w.microStepPattern.addActionListener( this ) ;
	}

	/**
	 */
	protected void _init()
	{
		super._init() ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		ignoreActions = true ;

		SpIterMicroStep microStepIter = ( SpIterMicroStep )_spItem ;

		SpInstObsComp inst = microStepIter.getInstrumentItem() ;

		_patternVector.clear() ;

		if( ( inst != null ) && ( inst instanceof SpMicroStepUser ) )
		{
			TreeSet<String> ts = new TreeSet<String>( ( ( SpMicroStepUser )inst ).getMicroStepPatterns().keySet() ) ;
			if( !ts.contains( SpIterMicroStep.NO_PATTERN ) )
				_patternVector.add( SpIterMicroStep.NO_PATTERN ) ;
			
			_patternVector.addAll( ts ) ;
		}

		_w.microStepPattern.setChoices( _patternVector ) ;
		_w.microStepPattern.setValue( microStepIter.getPattern() ) ;

		ignoreActions = false ;
	}

	/**
	 *
	 */
	public void actionPerformed( ActionEvent evt )
	{
		if( !ignoreActions )
		{
			SpIterMicroStep microStepIter = ( SpIterMicroStep )_spItem ;
	
			SpInstObsComp inst = microStepIter.getInstrumentItem() ;
	
			if( ( inst != null ) && ( inst instanceof SpMicroStepUser ) )
				_updateSpIterMicroStep( ( String )_w.microStepPattern.getSelectedItem() ) ;
		}
	}

	private void _updateSpIterMicroStep( String pattern )
	{
		SpIterMicroStep microStepIter = ( SpIterMicroStep )_spItem ;
		SpInstObsComp inst = microStepIter.getInstrumentItem() ;

		microStepIter.setPattern( pattern ) ;

		SpOffsetPosList opl = microStepIter.getPosList() ;

		opl.removeAllPositions() ;

		if( ( inst != null ) && ( inst instanceof SpMicroStepUser ) && ( !pattern.equals( SpIterMicroStep.NO_PATTERN ) ) )
		{
			double[][] offsets = ((SpMicroStepUser) inst).getMicroStepPatterns().get(_w.microStepPattern.getSelectedItem());

			for( int i = 0 ; i < offsets.length ; i++ )
				opl.createPosition( offsets[ i ][ 0 ] , offsets[ i ][ 1 ] ) ;
		}
		else
		{
			// If the microstep pattern is set to SpIterMicroStep.NO_PATTERN then a single microstep postion is added at 0.0. 
			// This means that there will be one "microstep" at each of the offset positions if the microstep iterator is inside 
			// an offset iterator or one "microstep" at the telescope base position if the microstep iterator is not inside an offset iterator. 
			// In either case the microstep iterator has no effect.
			// The microstep iterator should be used with an instruments classes that implement the SpMicroStepUser interface and provide a list of meaningful instrument specific microstep patterns.
			// For instruments that do not implement the SpMicroStepUser interface SpIterMicroStep.NO_PATTERN is the only selectable choice.
			opl.createPosition( 0. , 0. ) ;
		}
	}
}
