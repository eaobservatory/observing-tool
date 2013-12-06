/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot ;

import javax.swing.JFrame ;
import javax.swing.JPanel ;
import javax.swing.JButton ;
import javax.swing.JScrollPane ;
import javax.swing.JTextPane ;
import javax.swing.JFileChooser ;
import javax.swing.JOptionPane ;
import java.awt.JobAttributes ;
import java.awt.PageAttributes ;
import java.awt.Graphics ;
import java.awt.BorderLayout ;
import java.awt.PrintJob ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import javax.swing.text.Document ;
import javax.swing.text.BadLocationException ;
import javax.swing.text.Style ;
import javax.swing.text.StyleContext ;
import javax.swing.text.StyleConstants ;
import javax.swing.text.rtf.RTFEditorKit ;
import javax.swing.filechooser.FileFilter ;

import java.io.PrintWriter ;
import java.io.File ;
import java.io.IOException ;
import java.io.FileOutputStream ;
import java.util.StringTokenizer ;

/**
 * ReportBox has been rewritten to allow displaying different fonts and using java print routines.
 * 
 * The code still contains some experimental bits which are mostly commented out.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class ReportBox extends JFrame
{
	protected JPanel jPanel1 = new JPanel() ;
	protected JButton _dismissButton = new JButton() ;
	protected JButton _printButton = new JButton() ;
	protected JButton _saveButton = new JButton() ;
	protected JScrollPane jScrollPane1 = new JScrollPane() ;
	protected JTextPane _textPane = new JTextPane() ;

	/**
	 * This is a referred to the text component whose text should be saved.
	 *
	 * It can be reset in sub classes.
	 */
	protected JFileChooser _fileChooser = new JFileChooser() ;

	protected FileFilter _asciiFileFilter = new FileFilter()
	{
		public boolean accept( File file )
		{
			return file.getName().endsWith( ".txt" ) ;
		}

		public String getDescription()
		{
			return _asciiDescription ;
		}
	} ;

	protected FileFilter _richTextFormatFileFilter = new FileFilter()
	{
		public boolean accept( File file )
		{
			return file.getName().endsWith( ".rtf" ) ;
		}

		public String getDescription()
		{
			return _richTextFormatDescription ;
		}
	} ;

	protected String _asciiDescription = "Text Only (*.txt)" ;

	protected String _richTextFormatDescription = "Rich Text Format (*.rtf)" ;

	protected ReportBox()
	{
		try
		{
			jbInit() ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}
	}

	public ReportBox( String message )
	{
		this() ;

		_fileChooser.addChoosableFileFilter( _asciiFileFilter ) ;
		_fileChooser.addChoosableFileFilter( _richTextFormatFileFilter ) ;

		initStylesForTextPane( _textPane ) ;

		Document doc = _textPane.getDocument() ;
		try
		{
			doc.insertString( doc.getLength() , message + "\n" , _textPane.getStyle( "regular" ) ) ;

		}
		catch( BadLocationException e )
		{
			e.printStackTrace() ;
		}

		_textPane.setEditable( false ) ;

		// Set frame bounds.
		// Choose frame height depending on the number of lines of the docuument given frame width 480.
		// Multiplying the number of lines by 16 and adding 100 gives roughly the right height.
		if( getNumberOfLines( _textPane.getText() ) < 34 )
			setBounds( 100 , 100 , 480 , 100 + ( getNumberOfLines( _textPane.getText() ) * 16 ) ) ;
		else
			setBounds( 100 , 100 , 480 , 640 ) ;

		_textPane.setCaretPosition( 0 ) ;
		
		setVisible( true ) ;
	}

	public ReportBox( String message , String title )
	{
		this( message ) ;
		setTitle( title ) ;
	}

	private void jbInit() throws Exception
	{
		_dismissButton.setText( "Dismiss" ) ;
		_dismissButton.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				dismissButton_actionPerformed( e ) ;
			}
		} ) ;
		_printButton.setText( "Print" ) ;
		_printButton.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				printButton_actionPerformed( e ) ;
			}
		} ) ;
		_saveButton.setText( "Save" ) ;
		_saveButton.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				saveButton_actionPerformed( e ) ;
			}
		} ) ;

		this.add( jPanel1 , BorderLayout.SOUTH ) ;
		jPanel1.add( _dismissButton , null ) ;
		jPanel1.add( _printButton , null ) ;
		jPanel1.add( _saveButton , null ) ;
		this.add( jScrollPane1 , BorderLayout.CENTER ) ;
		jScrollPane1.getViewport().add( _textPane , null ) ;
	}

	public void print()
	{
		JobAttributes jobAttributes = new JobAttributes() ;
		PageAttributes pageAttributes = new PageAttributes() ;
		pageAttributes.setOrigin( PageAttributes.OriginType.PRINTABLE ) ;
		PrintJob pj = getToolkit().getPrintJob( ReportBox.this , "OT Report (" + System.getProperty( "user.name" ) + ")" , jobAttributes , pageAttributes ) ;
		if( pj != null )
		{
			Graphics pg = pj.getGraphics() ;
			_textPane.printAll( pg ) ;
			pg.dispose() ;
			pj.end() ;
		}
	}

	public void save()
	{
		if( _fileChooser.showSaveDialog( this ) == JFileChooser.APPROVE_OPTION )
		{
			String fileName = _fileChooser.getSelectedFile().getPath() ;
			String description = _fileChooser.getFileFilter().getDescription() ;
			if( fileName != null )
			{
				try
				{
					if( description.equals( _richTextFormatDescription ) )
					{
						( new RTFEditorKit() ).write( new FileOutputStream( fileName ) , _textPane.getDocument() , 0 , _textPane.getDocument().getLength() ) ;
					}
					else
					{
						PrintWriter printWriter = new PrintWriter( fileName ) ;
						StringTokenizer st = new StringTokenizer( _textPane.getText() , "\n" ) ;
						while( st.hasMoreTokens() )
							printWriter.println( st.nextToken() ) ;

						printWriter.close() ;
					}
				}
				catch( IOException exception )
				{
					JOptionPane.showMessageDialog( this , "Problems writing to file \"" + fileName + "\": " + exception , "Save Error" , JOptionPane.ERROR_MESSAGE ) ;
				}
				catch( BadLocationException exception )
				{
					JOptionPane.showMessageDialog( this , "Problems writing to file \"" + fileName + "\": " + exception , "Save Error" , JOptionPane.ERROR_MESSAGE ) ;
				}
			}
		}
	}

	/**
	 * Copied from java.sun.com example TextSamplerDemo.
	 */
	protected void initStylesForTextPane( JTextPane textPane )
	{
		//Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle( StyleContext.DEFAULT_STYLE ) ;

		Style regular = textPane.addStyle( "regular" , def ) ;
		StyleConstants.setFontFamily( def , "SansSerif" ) ;

		Style s = textPane.addStyle( "italic" , regular ) ;
		StyleConstants.setItalic( s , true ) ;

		s = textPane.addStyle( "bold" , regular ) ;
		StyleConstants.setBold( s , true ) ;

		s = textPane.addStyle( "small" , regular ) ;
		StyleConstants.setFontSize( s , 10 ) ;

		s = textPane.addStyle( "large" , regular ) ;
		StyleConstants.setFontSize( s , 16 ) ;
	}

	public static void main( String[] args )
	{
		new ReportBox( args[ 0 ] ) ;
	}

	public static int getNumberOfLines( String string )
	{
		int result = 0 ;

		for( int i = 0 ; i < string.length() ; i++ )
		{
			if( string.charAt( i ) == '\n' )
				result++ ;
		}

		return result ;
	}

	public void printButton_actionPerformed( ActionEvent e )
	{
		print() ;
	}

	public void saveButton_actionPerformed( ActionEvent e )
	{
		save() ;
	}

	public void dismissButton_actionPerformed( ActionEvent e )
	{
		dispose() ;
	}
}
