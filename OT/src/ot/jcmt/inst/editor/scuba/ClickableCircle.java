/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.inst.editor.scuba ;

import java.awt.Graphics ;
import java.awt.Graphics2D ;
import java.awt.geom.Arc2D ;
import java.awt.event.MouseListener ;
import java.awt.event.MouseEvent ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import java.util.Vector ;
import javax.swing.JPanel ;

/**
 * Labled circle GUI.
 *
 * This class is a labeled circle that can that can draw itself on a
 * component whose Graphics object is provided.
 * <p>
 * MouseListeners and ActionListeners can be added that will be notified if a
 * certain MouseEvents occur on a provided JPanel within the painted circle.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class ClickableCircle extends Arc2D.Double implements MouseListener
{
	protected String _label = "" ;
	Vector _mouseListeners = new Vector() ;
	Vector _actionListeners = new Vector() ;

	/**
	 * @param x x coordinate of centre.
	 * @param y x coordinate of centre.
	 * @param r radius.
	 */
	public ClickableCircle( double x , double y , double r )
	{
		super( x , y , r , r , 0.0 , 360.0 , OPEN ) ;
	}

	/**
	 * @param x x coordinate of centre.
	 * @param y x coordinate of centre.
	 * @param r radius.
	 * @param label label of the hexagon.
	 */
	public ClickableCircle( double x , double y , double r , String label )
	{
		this( x , y , r ) ;
		_label = label ;
	}

	public void draw( Graphics g )
	{
		( ( Graphics2D )g ).draw( this ) ;
		g.drawString( _label , ( int )getCenterX() - ( 3 * _label.length() ) , ( int )getCenterY() + 5 ) ;
	}

	public String getLabel()
	{
		return _label ;
	}

	public void addMouseListener( MouseListener mouseListener , JPanel panel )
	{
		if( !_mouseListeners.contains( mouseListener ) )
		{
			_mouseListeners.add( mouseListener ) ;
			panel.addMouseListener( this ) ;
		}
	}

	public void removeMouseListener( MouseListener mouseListener )
	{
		_mouseListeners.remove( mouseListener ) ;
	}

	public void addActionListener( ActionListener actionListener , JPanel panel )
	{
		if( !_actionListeners.contains( actionListener ) )
		{
			_actionListeners.add( actionListener ) ;
			panel.addMouseListener( this ) ;
		}
	}

	public void removeActionListener( ActionListener actionListener )
	{
		_actionListeners.remove( actionListener ) ;
	}

	public void mouseClicked( MouseEvent e )
	{
		if( contains( e.getPoint() ) )
		{
			for( int i = 0 ; i < _mouseListeners.size() ; i++ )
				( ( MouseListener )_mouseListeners.get( i ) ).mouseClicked( e ) ;
	
			for( int i = 0 ; i < _actionListeners.size() ; i++ )
				( ( ActionListener )_actionListeners.get( i ) ).actionPerformed( new ActionEvent( this , -1 , "Pixel \"" + getLabel() + "\" clicked." ) ) ;
		}
	}

	public void mouseEntered( MouseEvent e )
	{
		if( contains( e.getPoint() ) )
		{
			for( int i = 0 ; i < _mouseListeners.size() ; i++ )
				( ( MouseListener )_mouseListeners.get( i ) ).mouseEntered( e ) ;
		}
	}

	public void mouseExited( MouseEvent e )
	{
		if( contains( e.getPoint() ) )
		{
			for( int i = 0 ; i < _mouseListeners.size() ; i++ )
				( ( MouseListener )_mouseListeners.get( i ) ).mouseExited( e ) ;
		}
	}

	public void mousePressed( MouseEvent e )
	{
		if( contains( e.getPoint() ) )
		{
			for( int i = 0 ; i < _mouseListeners.size() ; i++ )
				( ( MouseListener )_mouseListeners.get( i ) ).mousePressed( e ) ;
		}
	}

	public void mouseReleased( MouseEvent e )
	{
		if( contains( e.getPoint() ) )
		{
			for( int i = 0 ; i < _mouseListeners.size() ; i++ )
				( ( MouseListener )_mouseListeners.get( i ) ).mouseReleased( e ) ;
		}
	}
}
