// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
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
