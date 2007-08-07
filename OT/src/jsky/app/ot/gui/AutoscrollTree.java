// Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.Autoscroll;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeModel;

/** Simple wrapper for JTree to add auto-scrolling support for use with drag&drop */
public class AutoscrollTree extends JTree implements Autoscroll
{
	public static final Insets defaultScrollInsets = new Insets( 8 , 8 , 8 , 8 );

	protected Insets scrollInsets = defaultScrollInsets;

	public AutoscrollTree(){}

	public AutoscrollTree( TreeModel model )
	{
		super( model );
	}

	public void setScrollInsets( Insets insets )
	{
		this.scrollInsets = insets;
	}

	public Insets getScrollInsets()
	{
		return scrollInsets;
	}

	public Insets getAutoscrollInsets()
	{
		Rectangle r = getVisibleRect();
		Dimension size = getSize();
		Insets i = new Insets( r.y + scrollInsets.top , r.x + scrollInsets.left , size.height - r.y - r.height + scrollInsets.bottom , size.width - r.x - r.width + scrollInsets.right );
		return i;
	}

	public void autoscroll( Point location )
	{
		JScrollPane scroller = ( JScrollPane )SwingUtilities.getAncestorOfClass( JScrollPane.class , this );
		if( scroller != null )
		{
			JScrollBar hBar = scroller.getHorizontalScrollBar();
			JScrollBar vBar = scroller.getVerticalScrollBar();
			Rectangle r = getVisibleRect();
			if( location.x <= r.x + scrollInsets.left )
				hBar.setValue( hBar.getValue() - hBar.getUnitIncrement( -1 ) );
			if( location.y <= r.y + scrollInsets.top )
				vBar.setValue( vBar.getValue() - vBar.getUnitIncrement( -1 ) );
			if( location.x >= r.x + r.width - scrollInsets.right )
				hBar.setValue( hBar.getValue() + hBar.getUnitIncrement( 1 ) );
			if( location.y >= r.y + r.height - scrollInsets.bottom )
				vBar.setValue( vBar.getValue() + vBar.getUnitIncrement( 1 ) );
		}
	}
}
