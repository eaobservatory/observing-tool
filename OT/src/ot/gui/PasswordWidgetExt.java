// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.gui;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import javax.swing.JPasswordField ;
import javax.swing.JFrame ;
import javax.swing.event.DocumentListener ;
import javax.swing.event.DocumentEvent ;
import jsky.util.gui.BasicWindowMonitor;
import jsky.app.ot.gui.DescriptiveWidget;

/**
 * A TextBoxWidget that permits clients to register as key press watchers.
 *
 * @author	Shane Walker, Allan Brighton (Swing port)
 */
public class PasswordWidgetExt extends JPasswordField implements DescriptiveWidget , DocumentListener , ActionListener
{
	// if true, ignore changes in the text box content
	private boolean _ignoreChanges = false;

	/**
	 * Like the "tip" but not shown automatically when the mouse rests on
	 * the widget.
	 * @see #getDescription
	 * @see #setDescription
	 */
	public String description;

	/** Default constructor */
	public PasswordWidgetExt()
	{
		getDocument().addDocumentListener( this );
		addActionListener( this );
	}

	// -- For the DocumentListener interface --

	/** 
	 * Gives notification that there was an insert into the
	 * document. The range given by the DocumentEvent bounds the
	 * freshly inserted region. 
	 */
	public void insertUpdate( DocumentEvent e ){}

	/**
	 * Gives notification that a portion of the document has been
	 * removed. The range is given in terms of what the view last saw
	 * (that is, before updating sticky positions).  
	 */
	public void removeUpdate( DocumentEvent e ){}

	/** Gives notification that an attribute or set of attributes changed. */
	public void changedUpdate( DocumentEvent e ){}

	// -- For the ActionListener interface --

	/** Invoked when an action occurs. */
	public void actionPerformed( ActionEvent e ){}

	/**
	 * Set the description.
	 * @see #description
	 */
	public void setDescription( String newDescription )
	{
		description = newDescription;
	}

	/**
	 * Get the description.
	 * @see #description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Get the current value as a double.
	 */
	public double getDoubleValue( double def )
	{
		try
		{
			return ( Double.valueOf( ( String )getValue() ) ).doubleValue();
		}
		catch( Exception ex ){}
		return def;
	}

	/**
	 * Set the current value as a double.
	 */
	public void setValue( double d )
	{
		setText( String.valueOf( d ) );
	}

	/**
	 * Get the current value as an int.
	 */
	public int getIntegerValue( int def )
	{
		try
		{
			return Integer.parseInt( ( String )getValue() );
		}
		catch( Exception ex ){}

		return def;
	}

	/**
	 * Set the current value
	 */
	public void setText( String s )
	{
		_ignoreChanges = true;
		super.setText( s );
		_ignoreChanges = false;
	}

	/**
	 * Set the current value as an int.
	 */
	public void setValue( int i )
	{
		setText( String.valueOf( i ) );
	}

	/**
	 * Set the current value.
	 */
	public void setValue( String s )
	{
		setText( s );
	}

	/**
	 * Return the current value.
	 */
	public String getValue()
	{
		return new String( getPassword() );
	}

	/**
	 * test main
	 */
	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "PasswordWidgetExt" );

		PasswordWidgetExt tbw = new PasswordWidgetExt();

		frame.getContentPane().add( "Center" , tbw );
		frame.pack();
		frame.setVisible( true );
		frame.addWindowListener( new BasicWindowMonitor() );
	}
}
