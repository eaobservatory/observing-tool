// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.iter.editor;

import orac.ukirt.iter.SpIterNod;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Enumeration;
import java.util.Vector;

import jsky.app.ot.editor.OtItemEditor;

/**
 * This is the editor for Nod iterator component.
 *
 * @author converted to Nod Iterator component by Martin Folger (M.Folger@roe.ac.uk)
 */
public final class EdIterNod extends OtItemEditor implements ActionListener
{
	private IterNodGUI _w;

	/**
	 * If true, ignore action events.
	 */
	private boolean ignoreActions = false;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterNod()
	{
		_title = "Nod Iterator";
		_presSource = _w = new IterNodGUI();
		_description = "Take the specified number of nod exposures.";

		// If the choices can change depending on other settings then the adding of
		// choice items will have to be done in _init or _updateWidgets (see other editor components)
		Enumeration nodPatterns = SpIterNod.patterns();
		while( nodPatterns.hasMoreElements() )
			_w.nodPattern.addItem( ( Vector )nodPatterns.nextElement() );

		_w.nodPattern.addActionListener( this );
	}

	/**
	 */
	protected void _init()
	{
		super._init();
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		ignoreActions = true;

		SpIterNod nodIter = ( SpIterNod )_spItem;

		// Nod Pattern
		_w.nodPattern.setValue( nodIter.getNodPatternVector() );

		ignoreActions = false;
	}

	/**
	 *
	 */
	public void actionPerformed( ActionEvent evt )
	{
		if( !ignoreActions )
		{
			Object w = evt.getSource();
	
			SpIterNod nodIter = ( SpIterNod )_spItem;
	
			if( w == _w.nodPattern )
				nodIter.setNodPattern( ( Vector )_w.nodPattern.getSelectedItem() );
		}
	}
}
