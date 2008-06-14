// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

import java.awt.Component ;
import javax.swing.JLabel ;
import javax.swing.ImageIcon ;
import javax.swing.JTree ;
import javax.swing.tree.DefaultTreeCellRenderer ;

/**
 * Controls the appearance of OT tree nodes.
 */
public class TreeWidgetCellRenderer extends DefaultTreeCellRenderer
{
	public Component getTreeCellRendererComponent( JTree tree , Object value , boolean sel , boolean expanded , boolean leaf , int row , boolean hasFocus )
	{
		super.getTreeCellRendererComponent( tree , value , sel , expanded , leaf , row , hasFocus ) ;

		setBackgroundNonSelectionColor( tree.getBackground() ) ;
		if( this instanceof JLabel && value instanceof TreeNodeWidgetExt )
		{
			TreeNodeWidgetExt node = ( TreeNodeWidgetExt )value ;
			if( expanded )
			{
				ImageIcon imageIcon = node.getExpandIcon() ;
				if( imageIcon != null )
				{
					try
					{
						setIcon( imageIcon ) ;
					}
					catch( ClassCastException cce )
					{
						System.out.println( cce ) ;
					}
				}
			}
			else
			{
				ImageIcon imageIcon = node.getIcon() ;
				if( imageIcon != null )
				{
					try
					{
						setIcon( imageIcon ) ;
					}
					catch( ClassCastException cce )
					{
						System.out.println( cce ) ;
					}
				}
			}
			setFont( node.getFont() ) ;
			setText( node.getText() ) ;
		}
		else
		{
			System.out.println( "XXX TreeWidgetCellRenderer: ERROR" ) ;
		}

		return this ;
	}
}
