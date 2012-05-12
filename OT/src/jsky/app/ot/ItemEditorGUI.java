/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot ;

import java.awt.BorderLayout ;
import java.awt.Font ;
import javax.swing.JTextArea ;
import javax.swing.JPanel ;
import javax.swing.ImageIcon ;
import javax.swing.JButton ;
import javax.swing.BorderFactory ;
import javax.swing.border.TitledBorder ;
import javax.swing.border.Border ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import java.net.URL ;

/** 
 * Implements the basic GUI layout for the OT item editor panel.
 */
@SuppressWarnings( "serial" )
public class ItemEditorGUI extends JPanel
{
	/** Text box describing the panel */
	protected JTextArea _descriptionBox ;

	/** An empty panel filled in by other classes */
	protected JPanel _contentPresentation ;
	
	protected ImageIcon _pencilIcon ;
	protected ImageIcon _pencilDownIcon ;
	protected JButton _showEditPencil ;
	protected CommandButtonWidgetExt _undoButton ;
	protected CommandButtonWidgetExt _closeButton ;

	/** Main panel border */
	protected TitledBorder _border ;

	/**
	 * Constructor
	 */
	public ItemEditorGUI()
	{
		setLayout( new BorderLayout() ) ;
		add( "Center" , makeMainPanel() ) ;
		add( "South" , makeButtonPanel() ) ;
	}

	protected JPanel makeMainPanel()
	{
		JPanel panel = new JPanel() ;
		panel.setLayout( new BorderLayout() ) ;

		_descriptionBox = new JTextArea() ;
		_descriptionBox.setBackground( getBackground() ) ;
		_descriptionBox.setLineWrap( true ) ;
		_descriptionBox.setWrapStyleWord( true ) ;
		_descriptionBox.setEditable( false ) ;

		_contentPresentation = new JPanel() ;
		_contentPresentation.setLayout( new BorderLayout() ) ;
		panel.add( "North" , _descriptionBox ) ;
		panel.add( "Center" , _contentPresentation ) ;
		Border b = BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder( 5 , 5 , 5 , 5 ) , BorderFactory.createEtchedBorder() ) ;
		_border = BorderFactory.createTitledBorder(b, "Component Editor", TitledBorder.LEFT, TitledBorder.CENTER, new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
		panel.setBorder( _border ) ;
		return panel ;
	}

	protected JPanel makeButtonPanel()
	{
		JPanel panel = new JPanel() ;
		panel.setLayout( new BorderLayout() ) ;

		JPanel panel2 = new JPanel() ;

		URL url = getClass().getResource( "images/pencil.gif" ) ;
		_pencilIcon = new ImageIcon( url ) ;
		url = getClass().getResource( "images/pencilDown.gif" ) ;
		_pencilDownIcon = new ImageIcon( url ) ;
		_showEditPencil = new JButton( _pencilIcon ) ;
		_showEditPencil.setBorder( null ) ;
		_showEditPencil.setVisible( false ) ;
		_showEditPencil.setFocusPainted( false ) ;

		_undoButton = new CommandButtonWidgetExt( "Undo" ) ;
		_closeButton = new CommandButtonWidgetExt( "Close" ) ;
		panel2.add( _showEditPencil ) ;
		panel2.add( _undoButton ) ;

		panel.add( "East" , panel2 ) ;
		return panel ;
	}
}
