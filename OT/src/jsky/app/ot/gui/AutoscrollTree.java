// Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)
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
package jsky.app.ot.gui ;

import java.awt.Dimension ;
import java.awt.Insets ;
import java.awt.Point ;
import java.awt.Rectangle ;
import java.awt.dnd.Autoscroll ;
import javax.swing.JScrollBar ;
import javax.swing.JScrollPane ;
import javax.swing.JTree ;
import javax.swing.SwingUtilities ;
import javax.swing.tree.TreeModel ;

/** Simple wrapper for JTree to add auto-scrolling support for use with drag&drop */
@SuppressWarnings( "serial" )
public class AutoscrollTree extends JTree implements Autoscroll
{
	public static final Insets defaultScrollInsets = new Insets( 8 , 8 , 8 , 8 ) ;

	protected Insets scrollInsets = defaultScrollInsets ;

	public AutoscrollTree(){}

	public AutoscrollTree( TreeModel model )
	{
		super( model ) ;
	}

	public void setScrollInsets( Insets insets )
	{
		this.scrollInsets = insets ;
	}

	public Insets getScrollInsets()
	{
		return scrollInsets ;
	}

	public Insets getAutoscrollInsets()
	{
		Rectangle r = getVisibleRect() ;
		Dimension size = getSize() ;
		Insets i = new Insets( r.y + scrollInsets.top , r.x + scrollInsets.left , size.height - r.y - r.height + scrollInsets.bottom , size.width - r.x - r.width + scrollInsets.right ) ;
		return i ;
	}

	public void autoscroll( Point location )
	{
		JScrollPane scroller = ( JScrollPane )SwingUtilities.getAncestorOfClass( JScrollPane.class , this ) ;
		if( scroller != null )
		{
			JScrollBar hBar = scroller.getHorizontalScrollBar() ;
			JScrollBar vBar = scroller.getVerticalScrollBar() ;
			Rectangle r = getVisibleRect() ;
			if( location.x <= r.x + scrollInsets.left )
				hBar.setValue( hBar.getValue() - hBar.getUnitIncrement( -1 ) ) ;
			if( location.y <= r.y + scrollInsets.top )
				vBar.setValue( vBar.getValue() - vBar.getUnitIncrement( -1 ) ) ;
			if( location.x >= r.x + r.width - scrollInsets.right )
				hBar.setValue( hBar.getValue() + hBar.getUnitIncrement( 1 ) ) ;
			if( location.y >= r.y + r.height - scrollInsets.bottom )
				vBar.setValue( vBar.getValue() + vBar.getUnitIncrement( 1 ) ) ;
		}
	}
}
