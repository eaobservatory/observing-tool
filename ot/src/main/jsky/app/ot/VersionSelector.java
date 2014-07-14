/*
 * Copyright (C) 2009-2010 Science and Technology Facilities Council.
 * All Rights Reserved.
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

package jsky.app.ot ;

import gemini.util.ObservingToolUtilities ;

import java.io.BufferedReader ;
import java.io.IOException ;
import java.io.InputStreamReader ;
import java.net.URL ;
import java.util.TreeMap ;
import java.util.Collection ;

import javax.swing.JRadioButton ;
import javax.swing.JPanel ;
import javax.swing.JFrame ;
import javax.swing.ButtonGroup ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import java.awt.Dimension ;
import java.awt.GridLayout ;
import java.awt.Toolkit ;

@SuppressWarnings( "serial" )
public class VersionSelector extends JPanel implements ActionListener
{
	private static final VersionSelector selector = new VersionSelector() ;
	private TreeMap<String,TelescopeConfig> configs = new TreeMap<String,TelescopeConfig>() ;
	public static final String TELESCOPE_KEY = "telescope" ;
	public static final String RES_KEY = "ot.resource.cfgdir" ;
	public static final String CFG_KEY = "ot.cfgdir" ;
	public static final String JSKY_KEY = "jsky.catalog.skycat.config" ;
	private JFrame frame = new JFrame( "Select a configuration" ) ;

	private VersionSelector()
	{
		super() ;
	}
	
	public static boolean checkVersions()
	{
		if( selector.checkTelescopeConfig() )
		{
			selector.readConfig() ;
			selector.makeMenu() ;
		}
		while( selector.frame.isVisible() )
		{
			Thread.yield() ;
		}
		return true ;
	}
	
	private boolean checkTelescopeConfig()
	{
		boolean requiresMenu = false ;
		String telescope = System.getProperty( TELESCOPE_KEY ) ;
		String resDir = System.getProperty( RES_KEY ) ;
		String cfgDir = System.getProperty( CFG_KEY ) ;
		String jsky = System.getProperty( JSKY_KEY ) ;
		 
		if( ( telescope == null || "".equals( telescope ) ) && 
				( resDir == null || "".equals( resDir ) ) &&
					( cfgDir == null || "".equals( cfgDir ) ) &&
						( jsky == null || "".equals( jsky ) ) )
		{
			requiresMenu = true ;
		}
		
		return requiresMenu ;
	}
	
	private void readConfig()
	{
		URL url = ObservingToolUtilities.resourceURL( "ot/cfg.cfg" ) ;
		BufferedReader br = null ;
		try
		{
			br = new BufferedReader( new InputStreamReader( url.openStream() ) ) ;
			String line ;
			TelescopeConfig tc = null ;
			while( ( line = br.readLine() ) != null )
			{
				line = line.trim() ;
				
				String[] split = line.split( "=" ) ;
				String key = split[ 0 ].trim().toLowerCase() ;
				if( "name".equals( key ) && tc == null )
				{
					tc = new TelescopeConfig() ;
					tc.name = split[ 1 ].trim() ;
					configs.put( tc.name , tc ) ;
				}
				else if( TELESCOPE_KEY.equals( key ) && tc != null )
				{
					tc.telescope = split[ 1 ].trim() ;
				}
				else if( CFG_KEY.equals( key ) && tc != null )
				{
					tc.cfgDir = split[ 1 ].trim() ;
				}
				else if( RES_KEY.equals( key ) && tc != null )
				{
					tc.resDir = split[ 1 ].trim() ;
				}
				else if( JSKY_KEY.equals( key ) && tc != null )
				{
					tc.jsky = split[ 1 ].trim() ;
				}
				else
				{
					tc = null ;
				}
			}
		}
		catch( IOException ex )
		{
			System.out.println( ex ) ;
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
	
	private void makeMenu()
	{
		ButtonGroup group = new ButtonGroup() ;
		Collection<TelescopeConfig> values = configs.values() ;
		if( values.size() > 0 )
		{
		setLayout( new GridLayout( values.size() , 1 ) ) ;
    		for( TelescopeConfig config : values )
    		{
    			JRadioButton rb = new JRadioButton( config.name ) ;
    			rb.setActionCommand( config.name ) ;
    			rb.addActionListener( this ) ;
    			group.add( rb ) ;
    			add( rb ) ;
    		}
    		
    		frame.setContentPane( selector ) ;
    		Dimension dim = getPreferredSize() ;
		dim.width = dim.width * 2 ;
		dim.height = dim.height * 2 ;
    		frame.setPreferredSize( dim ) ;
    		frame.pack() ;
    		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize() ;
    		frame.setLocation( screen.width / 2 - dim.width / 2 , screen.height / 2 - dim.height / 2 ) ;
    		frame.setVisible( true ) ;
		}
	}
	
	public void actionPerformed( ActionEvent e )
	{
		String name = e.getActionCommand() ;
		TelescopeConfig config = configs.get( name ) ;
		System.setProperty( TELESCOPE_KEY , config.telescope ) ;
		System.setProperty( CFG_KEY , config.cfgDir ) ;
		System.setProperty( RES_KEY , config.resDir ) ;
		System.setProperty( JSKY_KEY , config.jsky ) ;
		frame.setVisible( false ) ;
		frame.dispose() ;
	}

	public static void main( String[] args )
	{
		VersionSelector.checkVersions() ;
	}
	
	class TelescopeConfig
	{
		String name ;
		String telescope ;
		String cfgDir ;
		String resDir ;
		String jsky ;
	}
}
