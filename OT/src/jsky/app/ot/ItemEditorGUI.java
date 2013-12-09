/*
 * Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
