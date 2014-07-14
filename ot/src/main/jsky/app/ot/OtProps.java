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
//
package jsky.app.ot ;

import jsky.app.ot.util.AppProperties ;
import jsky.app.ot.util.AppPropertyWatcher ;

import java.awt.Point ;
import java.awt.Dimension ;
import java.util.Enumeration ;
import java.util.Hashtable ;
import java.util.Properties ;
import java.util.Vector ;

/**
 * Global Observing Tool properties that are maintained on behalf of the
 * user.  Whatever properties are set are saved when the program exits
 * and then may be read the next time the program runs.
 */
public class OtProps
{
	public static final String BACKGROUND_PATTERN = "background.pattern" ;
	public static final String IMAGE_CACHE_SIZE = "image.cache.size" ;
	public static final String SAVE_PROMPT = "save.prompt" ;
	public static final String TOOLBAR_VISIBLE = "toolbar.visible" ;
	public static final String WINDOW_LOCATION = "window.location" ;
	public static final String WINDOW_SIZE = "window.size" ;
	private static Properties _props ;
	private static Vector<AppPropertyWatcher> _watchers = new Vector<AppPropertyWatcher>() ;
	private static Hashtable<String,Vector<AppPropertyWatcher>> _singlePropWatchers = new Hashtable<String,Vector<AppPropertyWatcher>>() ;

	/** Add a general property watcher. */
	public synchronized static void addWatcher( AppPropertyWatcher apw )
	{
		if( _watchers.contains( apw ) )
			return ;
		_watchers.addElement( apw ) ;
	}

	/** Delete a general property watcher. */
	public synchronized static void deleteWatcher( AppPropertyWatcher apw )
	{
		_watchers.removeElement( apw ) ;
	}

	/** Delete all general property watchers. */
	public synchronized static void deleteWatchers()
	{
		_watchers.removeAllElements() ;
	}

	/** Add a specific property watcher. */
	public synchronized static void addWatcher( String property , AppPropertyWatcher apw )
	{
		Vector<AppPropertyWatcher> v = _singlePropWatchers.get( property ) ;
		if( v == null )
		{
			v = new Vector<AppPropertyWatcher>() ;
			_singlePropWatchers.put( property , v ) ;
		}
		if( v.contains( apw ) )
			return ;
		v.addElement( apw ) ;
	}

	/** Delete a specific property watcher. */
	public synchronized static void deleteWatcher( String property , AppPropertyWatcher apw )
	{
		Vector<AppPropertyWatcher> v = _singlePropWatchers.get( property ) ;
		if( v == null )
			return ;

		v.removeElement( apw ) ;
	}

	/** Delete all specific property watchers. */
	public synchronized static void deleteWatchers( String property )
	{
		_singlePropWatchers.remove( property ) ;
	}

	//
	// Notify of a property change.
	//
    private synchronized static void _notifyPropertyChange( String property , String value )
	{
		Enumeration<AppPropertyWatcher> e = _watchers.elements() ;
		while( e.hasMoreElements() )
		{
			AppPropertyWatcher apw = e.nextElement() ;
			apw.propertyChange( property , value ) ;
		}

		Vector<AppPropertyWatcher> vSingle = _singlePropWatchers.get( property ) ;
		if( vSingle != null )
		{
			e = vSingle.elements() ;
			while( e.hasMoreElements() )
			{
				AppPropertyWatcher apw = e.nextElement() ;
				apw.propertyChange( property , value ) ;
			}
		}
	}

	//
	// Load or, failing that, create the Properties.
	//
	private static void _getProps()
	{
		if( _props != null )
			return ;

		_props = AppProperties.load( "ot.txt" ) ;
		if( _props == null )
		{
			System.out.println( "Using default properties." ) ;
			_props = new Properties() ;
		}
	}

	/**
	 * Save the current Properties.
	 */
	public static boolean save()
	{
		_getProps() ;
		return AppProperties.save( "ot.txt" , "Observing Tool Properties" , _props ) ;
	}

	//
	// Parse an array of two ints out of a size attribute in the form "int,int".
	//
	private static int[] _getIntPair( String propName )
	{
		_getProps() ;

		String dim = _props.getProperty( propName ) ;
		if( dim == null )
			return null ;

		// Parse the string: width,height
		int commaPos = dim.indexOf( ',' ) ;
		if( commaPos == -1 )
			return null ;

		int w , h ;
		try
		{
			String wStr = dim.substring( 0 , commaPos ) ;
			String hStr = dim.substring( commaPos + 1 ) ;
			w = Integer.parseInt( wStr ) ;
			h = Integer.parseInt( hStr ) ;
		}
		catch( Exception ex )
		{
			return null ;
		}

		return new int[] { w , h } ;
	}

	//
	// Store a width, height property so that it may later be retrieved by
	// _getIntPair.
	//
	private static void _setIntPair( String propName , int w , int h )
	{
		_getProps() ;
		_props.put( propName , w + "," + h ) ;
		_notifyPropertyChange( propName , w + "," + h ) ;
	}

	/**
	 * Get the window size property.
	 */
	public static Dimension getWindowSize()
	{
		int[] dim = _getIntPair( WINDOW_SIZE ) ;
		if( dim == null )
			return null ;
		return new Dimension( dim[ 0 ] , dim[ 1 ] ) ;
	}

	/**
	 * Store the window size property.
	 */
	public static void setWindowSize( int w , int h )
	{
		_setIntPair( WINDOW_SIZE , w , h ) ;
	}

	/**
	 * Get the window location property.
	 */
	public static Point getWindowLocation()
	{
		int[] siz = _getIntPair( WINDOW_LOCATION ) ;
		if( siz == null )
			return null ;
		return new Point( siz[ 0 ] , siz[ 1 ] ) ;
	}

	/**
	 * Store the window location property.
	 */
	public static void setWindowLocation( int x , int y )
	{
		_setIntPair( WINDOW_LOCATION , x , y ) ;
	}

	/**
	 * Get the size of a particular component editor region.
	 */
	public static Dimension getEditorSize( String classname )
	{
		int[] dim = _getIntPair( classname + ".size" ) ;
		if( dim == null )
			return null ;
		return new Dimension( dim[ 0 ] , dim[ 1 ] ) ;
	}

	/**
	 * Store the prefered size of a particular component editor region.
	 */
	public static void setEditorSize( String classname , int w , int h )
	{
		_setIntPair( classname + ".size" , w , h ) ;
	}

	/**
	 * Get the image cache size. 
	 *
	 * @return  The size of the image cache, or -1 on error.

	 */
	public static int getImageCacheSize()
	{
		_getProps() ;

		String s = _props.getProperty( IMAGE_CACHE_SIZE ) ;
		if( s == null )
			return -1 ;

		int size = -1 ;
		try
		{
			size = Integer.parseInt( s ) ;
			if( size <= 0 )
				size = -1 ;
		}
		catch( Exception ex ){}

		return size ;
	}

	/**
	 * Store the background pattern file name.
	 */
	public static void setBackgroundPattern( String patternFile )
	{
		String oldPattern = getBackgroundPattern() ;
		if( ( oldPattern != null ) && patternFile.equals( oldPattern ) )
			return ;

		_props.put( BACKGROUND_PATTERN , patternFile ) ;
		_notifyPropertyChange( BACKGROUND_PATTERN , patternFile ) ;
	}

	/**
	 * Get the background pattern file name.
	 */
	public static String getBackgroundPattern()
	{
		_getProps() ;
		return ( String )_props.get( BACKGROUND_PATTERN ) ;
	}

	/**
	 * Store the "save should prompt" state.
	 */
	public static void setSaveShouldPrompt( boolean save )
	{
		if( save == isSaveShouldPrompt() )
			return ;

		_props.put( SAVE_PROMPT , String.valueOf( save ) ) ;
		_notifyPropertyChange( SAVE_PROMPT , String.valueOf( save ) ) ;
	}

	/**
	 * Get the "save should prompt" state.
	 */
	public static boolean isSaveShouldPrompt()
	{
		_getProps() ;
		String tmp = ( String )_props.get( SAVE_PROMPT ) ;
		if( tmp == null )
			return true ;
		return Boolean.valueOf( tmp ) ;
	}

	/**
	 * Store the toolbar visibility state.
	 */
	public static void setToolBarVisible( boolean visible )
	{
		if( visible == isToolBarVisible() )
			return ;

		_props.put( TOOLBAR_VISIBLE , String.valueOf( visible ) ) ;
		_notifyPropertyChange( TOOLBAR_VISIBLE , String.valueOf( visible ) ) ;
	}

	/**
	 * Get the toolbar visibility state.
	 */
	public static boolean isToolBarVisible()
	{
		_getProps() ;
		String tmp = ( String )_props.get( TOOLBAR_VISIBLE ) ;
		if( tmp == null )
			return true ;
		return Boolean.valueOf( tmp ) ;
	}
}
