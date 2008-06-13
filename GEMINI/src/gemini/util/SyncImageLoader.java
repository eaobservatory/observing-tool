// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util ;

import java.awt.Toolkit ;
import java.awt.Image ;

import java.awt.image.ImageObserver ;
import java.awt.image.ImageProducer ;

/**
 * Class used to load an image synchronously. It should only be used by one
 * client at a time. Yes, I know about MediaTracker. But unfortunately it
 * requires an AWT component to work.
 */
public class SyncImageLoader implements ImageObserver
{

	private int _imageFlags ;

	public synchronized Image loadImage( ImageProducer ip )
	{
		_imageFlags = 0 ;

		Toolkit tk = Toolkit.getDefaultToolkit() ;
		Image im = tk.createImage( ip ) ;
		if( tk.prepareImage( im , -1 , -1 , this ) )
			return im ;

		while( _imageFlags == 0 )
		{
			try
			{
				wait() ;
			}
			catch( Exception ex ){}
		}

		if( _imageFlags == ImageObserver.ALLBITS )
		{
			return im ;
		}
		else
		{
			System.out.println( getClass().getName() + "loadImage returning null" ) ;
			return null ;
		}
	}

	public boolean imageUpdate( Image im , int flags , int x , int y , int w , int h )
	{
		if( ( flags & ImageObserver.ALLBITS ) != 0 )
		{
			synchronized( this )
			{
				_imageFlags = ImageObserver.ALLBITS ;
				notify() ;
			}
			return false ;
		}

		if( ( flags & ( ImageObserver.ERROR | ImageObserver.ABORT ) ) != 0 )
		{
			System.out.println( getClass().getName() + "imageUpdate ERROR!" ) ;
			synchronized( this )
			{
				_imageFlags = ImageObserver.ERROR ;
				notify() ;
			}
			return false ;
		}
		return true ;
	}
}
