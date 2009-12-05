package jsky.app.ot ;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

abstract class MenuHandler implements MenuListener , MouseListener
{
	protected Vector<String> menuStack = new Vector<String>() ;
	protected Vector<String> selectionStack = new Vector<String>() ;

	/**
	 * Method to override.
	 * Sub classes have access to menuStack and selectionStack vectors.
	 * menuStack *should* have all the menus and submenus in order.
	 * selectionStack *should* contain only one item.
	 */
	public void actionToPerform(){}

	@Override
    public void menuCanceled( MenuEvent e )
    {
		Object source = e.getSource() ;
		if( source instanceof JMenu )
		{
			JMenu menu = ( JMenu )source ;
			String menuName = menu.getText() ;
			if( menuStack.contains( menuName ) )
			{
				int index = menuStack.indexOf( menuName ) ;
				if( index != menuStack.size() - 1 )
					System.out.println( menuName + " not at top of stack" ) ;
				menuStack.remove( menuName ) ;
			}
			else
			{
				System.out.println( menuName + " does not exist" ) ;
			}
		}
    }

	@Override
    public void menuDeselected( MenuEvent e )
    {
		Object source = e.getSource() ;
		if( source instanceof JMenu )
		{
			JMenu menu = ( JMenu )source ;
			String menuName = menu.getText() ;
			if( menuStack.contains( menuName ) )
			{
				int index = menuStack.indexOf( menuName ) ;
				if( index != menuStack.size() - 1 )
					System.out.println( menuName + " not at top of stack" ) ;
				menuStack.remove( menuName ) ;
			}
			else
			{
				System.out.println( menuName + " does not exist" ) ;
			}
			
			if( selectionStack.size() == 1 )
			{
				String item = selectionStack.elementAt( 0 ) ;
				actionToPerform() ;
			}
			else if( selectionStack.size() > 1 )
			{
				System.out.println( "More items selected ( " + selectionStack.size() + " ) than expected." ) ;
				for( String item : selectionStack )
					System.out.println( item ) ;
			}
		} 	        
    }

	@Override
    public void menuSelected( MenuEvent e )
    {
		Object source = e.getSource() ;
		if( source instanceof JMenu )
		{
			JMenu menu = ( JMenu )source ;
			String menuName = menu.getText() ;
			if( !menuStack.contains( menuName ) )
				menuStack.add( menuName ) ;
			else
				System.out.println( menuName + " already exists" ) ;
		}   
    }

	@Override
    public void mouseClicked( MouseEvent e )
    {
		Object source = e.getSource() ;
		if( source instanceof JMenuItem )
			actionToPerform() ;			
    }

	@Override
    public void mouseEntered( MouseEvent e )
    {
		Object source = e.getSource() ;
		if( source instanceof JMenuItem )
		{
			JMenuItem menuitem = ( JMenuItem )source ;
			String menuitemName = menuitem.getText() ;
			if( !selectionStack.contains( menuitemName ) )
				selectionStack.add( menuitemName ) ;
			else
				System.out.println( menuitemName + " already exists" ) ;
		} 	        
    }

	@Override
    public void mouseExited( MouseEvent e )
    {
		Object source = e.getSource() ;
		if( source instanceof JMenuItem )
		{
			JMenuItem menuitem = ( JMenuItem )source ;
			String menuitemName = menuitem.getText() ;
			if( selectionStack.contains( menuitemName ) )
			{
				int index = selectionStack.indexOf( menuitemName ) ;
				if( index != selectionStack.size() - 1 )
					System.out.println( menuitemName + " not at top of stack" ) ;
				selectionStack.remove( menuitemName ) ;
			}
			else
			{
				System.out.println( menuitemName + " does not exist" ) ;
			}
		} 	        
    }

	@Override
    public void mousePressed( MouseEvent e )
    {
		Object source = e.getSource() ;
		if( source instanceof JMenuItem )
			actionToPerform() ; 	        
    }

	@Override
    public void mouseReleased( MouseEvent e )
    {
		Object source = e.getSource() ;
		if( source instanceof JMenuItem )
		{
			JMenuItem menuitem = ( JMenuItem )source ;
			String menuitemName = menuitem.getText() ;
			if( selectionStack.contains( menuitemName ) )
			{
				int index = selectionStack.indexOf( menuitemName ) ;
				if( index != selectionStack.size() - 1 )
					System.out.println( menuitemName + " not at top of stack" ) ;
				selectionStack.remove( menuitemName ) ;
			}
		} 	        
    }		
}
