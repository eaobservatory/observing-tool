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

package jsky.app.ot.gui.image ;

import java.awt.Component ;
import javax.swing.event.MouseInputListener ;
import java.awt.geom.Rectangle2D ;
import java.awt.geom.Point2D ;
import java.util.Vector ;
import java.awt.event.MouseEvent ;
import jsky.navigator.NavigatorImageDisplay ;

/**
 * An ImageWidget that maintains a concept of a viewport on a base image.
 * The image that the viewport displays is taken to be a view of or window
 * on the real base image.
 * <p>
 * The ViewportImageWidget supports two types of observers: mouse observers
 * and view change observers.  Mouse observers are kept informed of where
 * the mouse is at all times and when it is pressed and released.   View
 * change observers are notified whenever the view on the base image changes.
 */
@SuppressWarnings( "serial" )
public class ViewportImageWidget extends NavigatorImageDisplay implements MouseInputListener
{
	private Vector<ViewportMouseObserver> _mouseObs = new Vector<ViewportMouseObserver>() ;
	private Vector<ViewportViewObserver> _viewObs = new Vector<ViewportViewObserver>() ;

	public ViewportImageWidget( Component parent )
	{
		super() ;
		addMouseListener( this ) ;
		addMouseMotionListener( this ) ;
	}

	/**
	 * Free any resources used by this widget.
	 */
	public void free(){}

	/**
	 * Translate an (x,y) position on the image view (which is relative to the
	 * the base image being viewed) to the ImageWidget itself.  Since the scale
	 * of the viewport may be greater than 1, double pixel values are required.
	 */
	public synchronized Point2D.Double imageViewToImageWidget( double x , double y )
	{
		Point2D.Double p = new Point2D.Double( x , y ) ;
		getCoordinateConverter().userToScreenCoords( p , false ) ;
		return p ;
	}

	/**
	 * Translate an (x,y) position on the ImageWidget to the image view.  Since
	 * the view may be scaled, a Point2D.Double is returned.
	 */
	public synchronized Point2D.Double imageWidgetToImageView( int x , int y )
	{
		Point2D.Double p = new Point2D.Double( x , y ) ;
		getCoordinateConverter().screenToUserCoords( p , false ) ;
		return p ;
	}

	protected ViewportMouseEvent _createMouseEvent()
	{
		return new ViewportMouseEvent() ;
	}

	protected synchronized boolean _initMouseEvent( MouseEvent evt , ViewportMouseEvent vme )
	{
		Point2D.Double p = new Point2D.Double( evt.getX() , evt.getY() ) ;
		getCoordinateConverter().screenToUserCoords( p , false ) ;

		vme.id = evt.getID() ;
		vme.source = this ;
		vme.xView = p.x ;
		vme.yView = p.y ;
		vme.xWidget = evt.getX() ;
		vme.yWidget = evt.getY() ;

		return true ;
	}

	public synchronized void addMouseObserver( ViewportMouseObserver obs )
	{
		if( !_mouseObs.contains( obs ) )
			_mouseObs.addElement( obs ) ;
	}

	public synchronized void deleteMouseObserver( ViewportMouseObserver obs )
	{
		_mouseObs.removeElement( obs ) ;
	}

	/**
	 * Tell all the mouse observers about the new mouse event.
	 */
	protected void _notifyMouseObs( MouseEvent e )
	{
		ViewportMouseEvent vme = _createMouseEvent() ;
		_initMouseEvent( e , vme ) ;
		for( int i = 0 ; i < _mouseObs.size() ; ++i )
		{
			ViewportMouseObserver vmo = _mouseObs.elementAt( i ) ;
			vmo.viewportMouseEvent( this , vme ) ;
		}
	}

	/**
	 * Tell all the mouse observers about the new ViewportMouseEvent event.
	 */
	protected void _notifyMouseObs( ViewportMouseEvent vme )
	{
		for( int i = 0 ; i < _mouseObs.size() ; ++i )
		{
			ViewportMouseObserver vmo = _mouseObs.elementAt( i ) ;
			vmo.viewportMouseEvent( this , vme ) ;
		}
	}

	/** called when the image has changed to update the display */
	public synchronized void updateImage()
	{
		super.updateImage() ;

		Rectangle2D.Double r = getVisibleArea() ;
		try
		{
			// might throw an exception if changing images
			_notifyViewObs( new ImageView( ( int )r.x , ( int )r.y , ( int )( r.x + r.width ) , ( int )( r.y + r.height ) , 1.0 ) ) ;
			/* last arg was scale which no longer exists in current jsky*/
		}
		catch( Exception e ){}
	}

	/**
	 * Tell all the view observers that the view has changed.
	 */
	protected void _notifyViewObs( ImageView iv )
	{
		for( int i = 0 ; i < _viewObs.size() ; ++i )
		{
			ViewportViewObserver vvo = _viewObs.elementAt( i ) ;
			vvo.viewportViewChange( this , iv ) ;
		}
	}

	public synchronized void addViewObserver( ViewportViewObserver obs )
	{
		if( !_viewObs.contains( obs ) )
			_viewObs.addElement( obs ) ;
	}

	public synchronized void deleteViewObserver( ViewportViewObserver obs )
	{
		_viewObs.removeElement( obs ) ;
	}

	// -- These implement the MouseInputListener interface --

	public void mousePressed( MouseEvent e )
	{
		_notifyMouseObs( e ) ;
	}

	public void mouseDragged( MouseEvent e )
	{
		_notifyMouseObs( e ) ;
	}

	public void mouseReleased( MouseEvent e )
	{
		_notifyMouseObs( e ) ;
	}

	public void mouseMoved( MouseEvent e )
	{
		_notifyMouseObs( e ) ;
	}

	public void mouseClicked( MouseEvent e )
	{
		_notifyMouseObs( e ) ;
	}

	public void mouseEntered( MouseEvent e )
	{
		_notifyMouseObs( e ) ;
	}

	public void mouseExited( MouseEvent e )
	{
		_notifyMouseObs( e ) ;
	}
}
