/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 * License:
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
 */
package jsky.app.ot.editor ;

import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Dimension ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.JButton ;
import javax.swing.BorderFactory ;

@SuppressWarnings( "serial" )
public class IterFolderGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JScrollPane jScrollPane1 = new JScrollPane() ;
	JButton compileButton = new JButton() ;
	IterEnumTableWidget table = new IterEnumTableWidget() ;

	public IterFolderGUI()
	{
		try
		{
			jbInit() ;
		}
		catch( Exception ex )
		{
			ex.printStackTrace() ;
		}
	}

	void jbInit() throws Exception
	{
		this.setLayout( gridBagLayout1 ) ;
		compileButton.setText( "Show" ) ;
		jScrollPane1.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		this.setPreferredSize( new Dimension( 300 , 300 ) ) ;
		this.add( jScrollPane1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		jScrollPane1.getViewport().add( table , null ) ;
		this.add( compileButton , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
	}
}
