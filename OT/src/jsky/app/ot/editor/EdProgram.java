// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

import javax.swing.ButtonGroup ;

import jsky.app.ot.gui.OptionWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

import orac.util.OracUtilities ;

import gemini.sp.SpProg ;

/**
 * This is the editor for the Science Program component.  At this point,
 * this editor is just a simple placeholder.
 */
public final class EdProgram extends OtItemEditor implements TextBoxWidgetWatcher , ActionListener
{

	// Attributes edited by this editor
	// changed for OMP by MFO, 7 August 2001
	private static final String KIND = "kind" ;
	private ProgramGUI _w ; // the GUI layout panel

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdProgram()
	{
		_title = "Program" ;
		_presSource = _w = new ProgramGUI() ;
		_description = "General program information taken from the proposal." ;
		_resizable = true ;

		// group the option buttons (radio buttons)
		ButtonGroup grp = new ButtonGroup() ;
		grp.add( _w.queueOption ) ;
		grp.add( _w.classicalOption ) ;
		_w.queueOption.addActionListener( this ) ;
		_w.classicalOption.addActionListener( this ) ;
		_w.infoBox.setVisible( false ) ;

		_w.propKindLabel.setVisible( false ) ;
		_w.queueOption.setVisible( false ) ;
		_w.classicalOption.setVisible( false ) ;
	}

	/**
	 * Do any (one time) initialization.
	 */
	protected void _init()
	{
		TextBoxWidgetExt tbwe = _w.titleBox ;
		tbwe.addWatcher( this ) ;

		// added for OMP by MFO, 7 August 2001
		_w.piBox.addWatcher( this ) ;
		_w.countryBox.addWatcher( this ) ;
		_w.projectIdBox.addWatcher( this ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		String val ;

		// Title
		TextBoxWidgetExt tbwe = _w.titleBox ;
		val = _spItem.getTitleAttr() ;
		if( val == null )
			tbwe.setText( "" ) ;
		else
			tbwe.setText( val ) ;

		// PI (changed for OMP by MFO, 7 August 2001)
		_w.piBox.setText( ( ( SpProg )_spItem ).getPI() ) ;

		// Country (changed for OMP by MFO, 7 August 2001)
		_w.countryBox.setText( ( ( SpProg )_spItem ).getCountry() ) ;

		// Project ID (added for OMP by MFO, 7 August 2001)
		_w.projectIdBox.setText( ( ( SpProg )_spItem ).getProjectID() ) ;

		_showPropKind( _avTab.get( KIND ) ) ;

		double time = (( SpProg )_spItem).getElapsedTime() ;
		_w.estimatedTime.setText( OracUtilities.secsToHHMMSS( time , 1 ) ) ;
		time = (( SpProg )_spItem).getTotalTime() ;
		_w.totalTime.setText( OracUtilities.secsToHHMMSS( time , 1 ) ) ;
	}

	/**
	 * Set the Prop. Kind option widget.
	 */
	void _showPropKind( String kind )
	{
		OptionWidgetExt ow ;

		// Proposal Kind
		if( ( kind == null ) || kind.equals( "queue" ) )
			ow = _w.queueOption ;
		else
			ow = _w.classicalOption ;

		ow.setValue( true ) ;
	}

	/**
	 * Watch changes to the title text box.
	 * @see TextBoxWidgetWatcher
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		if( tbwe == _w.titleBox )
			_spItem.setTitleAttr( tbwe.getText().trim() ) ;
		else if( tbwe == _w.piBox )
			( ( SpProg )_spItem ).setPI( tbwe.getText().trim() ) ;
		else if( tbwe == _w.countryBox )
			( ( SpProg )_spItem ).setCountry( tbwe.getText().trim() ) ;
		else if( tbwe == _w.projectIdBox )
			( ( SpProg )_spItem ).setProjectID( tbwe.getText().trim() ) ;
	}

	/**
	 * Text box action, ignore.
	 * @see TextBoxWidgetWatcher
	 */
	public void textBoxAction( TextBoxWidgetExt tbwe ){}

	/**
	 * Override the <code>action()</code> method to handle user events.
	 */
	public void actionPerformed( ActionEvent evt )
	{
		Object w = evt.getSource() ;

		if( w == _w.queueOption )
			_showPropKind( "queue" ) ;
		else if( w == _w.classicalOption )
			_showPropKind( "classical" ) ;
	}
}
