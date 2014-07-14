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

package ot ;

import javax.swing.JFrame ;
import javax.swing.JPanel ;
import javax.swing.JButton ;
import javax.swing.JScrollPane ;

import java.awt.BorderLayout ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.io.IOException ;

import java.net.URL ;

import jsky.app.ot.gui.RichTextBoxWidgetExt ;

/**
 * This class has been moved to the package ot and modified for use of swing instead of
 * freebongo widget.
 *
 * @author M.Folger (based on the class News in orac2/OT/ot/src, freebongo OT)
 */
@SuppressWarnings( "serial" )
public final class News extends JFrame
{
	private static News _news ;
	private RichTextBoxWidgetExt _rt = new RichTextBoxWidgetExt() ;
	private JButton _close = new JButton( "Close" ) ;

	public synchronized static void showNews( URL url )
	{
		if( _news == null )
		{
			_news = new News() ;
			_news._initTextBox() ;

			BufferedReader br = null ;
			try
			{
				br = new BufferedReader( new InputStreamReader( url.openStream() ) ) ;
				_news._readNews( br ) ;
			}
			catch( IOException ex )
			{
				_news._warning( "Couldn't read the news file!" ) ;
				System.out.println( "IO EXCEPTION: " + ex ) ;
			}
			finally
			{
				try
				{
					if( br != null )
						br.close() ;
				}
				catch( Exception ex ){}
			}
		}
		_news.setVisible( true ) ;
		_news.toFront() ;
		_news.setState( JFrame.NORMAL ) ;
	}

	public synchronized static void hideNews()
	{
		if( _news != null )
			_news.setVisible( false ) ;
	}

	private News()
	{
		super( "Observing Tool Release Notes" ) ;

		_rt.setEditable( false ) ;

		_close.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				hideNews() ;
			}
		} ) ;

		add( BorderLayout.CENTER , new JScrollPane( _rt ) ) ;

		JPanel bottomPanel = new JPanel() ;
		bottomPanel.add( _close ) ;
		add( BorderLayout.SOUTH , bottomPanel ) ;

		setBounds( 100 , 100 , 480 , 640 ) ;
	}

	private void _initTextBox()
	{
		_rt.setText( "" ) ;

		_rt.append( "JAC Observing Tool Release Notes" ) ;
		_rt.append( "\n\n" ) ;
		_rt.append( "This page will be updated frequently as new features are " + "incorporated and bugs are fixed." ) ;
		_rt.append( "\n\n" ) ;
	}

	private void _warning( String warning )
	{
		_rt.append( "WARNING: " ) ;
	}

	private void _readNews( BufferedReader br ) throws IOException
	{
		String line ;
		while( ( line = br.readLine() ) != null )
		{
			if( line.startsWith( "+++" ) )
			{
				_rt.append( "\n" ) ;
				_rt.append( line.substring( 4 ) ) ;
			}
			else
			{
				line = line.trim() ;
				if( line.equals( "" ) )
					_rt.append( "\n\n" ) ;
				else
					_rt.append( line + " " ) ;
			}
		}
	}
}
