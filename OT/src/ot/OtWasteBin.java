/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot ;

import javax.swing.JLabel ;
import javax.swing.Icon ;
import javax.swing.ImageIcon ;
import java.awt.datatransfer.Transferable ;
import java.awt.datatransfer.UnsupportedFlavorException ;
import java.awt.dnd.DnDConstants ;
import java.awt.dnd.DropTarget ;
import java.awt.dnd.DropTargetDragEvent ;
import java.awt.dnd.DropTargetDropEvent ;
import java.awt.dnd.DropTargetEvent ;
import java.awt.dnd.DropTargetListener ;
import java.io.IOException ;

import jsky.app.ot.OtDragDropObject ;
import jsky.app.ot.OtTreeWidget ;
import ot.util.DialogUtil ;

import jsky.app.ot.gui.DrawUtil ;

/**
 * Waste bin drop target for deleting sp items / tree nodes.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class OtWasteBin extends JLabel implements DropTargetListener
{
	protected Icon wasteIcon = new ImageIcon( DrawUtil.getResourceURL( "ot/images/waste.gif" ) ) ;
	protected Icon wasteSelectedIcon = new ImageIcon( DrawUtil.getResourceURL( "ot/images/wasteSelected.gif" ) ) ;

	public OtWasteBin()
	{
		super() ;

		setIcon( wasteIcon ) ;

		new DropTarget( this , DnDConstants.ACTION_COPY_OR_MOVE , this , this.isEnabled() , null ) ;
	}

	protected boolean acceptOrRejectDrag( DropTargetDragEvent dtde )
	{
		if( dtde.isDataFlavorSupported( OtDragDropObject.dataFlavor ) )
		{
			dtde.acceptDrag( dtde.getDropAction() ) ;
			return true ;
		}
		else
		{
			dtde.rejectDrag() ;
			return false ;
		}
	}

	public void dragEnter( DropTargetDragEvent dtde )
	{
		acceptOrRejectDrag( dtde ) ;
		setIcon( wasteSelectedIcon ) ;
	}

	public void dragExit( DropTargetEvent dte )
	{
		setIcon( wasteIcon ) ;
	}

	public void dragOver( DropTargetDragEvent dtde )
	{
		acceptOrRejectDrag( dtde ) ;
	}

	public void drop( DropTargetDropEvent dtde )
	{
		try
		{
			Transferable transferable = dtde.getTransferable() ;
			OtDragDropObject ddo = ( OtDragDropObject )transferable.getTransferData( OtDragDropObject.dataFlavor ) ;
			OtTreeWidget ownerTW = ddo.getOwner() ;

			// If the remove methods of OtTreeWidget worked better with the new multi selection implementation in
			// MultiSelTreeWidget then one could also get the selected items from the OtDragDropObject
			// rather than from the current selection of the OtTreeWidget.
			ownerTW.rmAllSelectedItems() ;
			dtde.acceptDrop( dtde.getDropAction() ) ;
		}
		catch( UnsupportedFlavorException e )
		{
			dtde.rejectDrop() ;
			DialogUtil.error( this , "You cannot delete this item." ) ;
		}
		catch( IOException e )
		{
			dtde.rejectDrop() ;
			DialogUtil.error( this , "You cannot delete this item." ) ;
		}
		// IllegalArgumentException is thrown if user attempts to delete Science Program root node.
		catch( IllegalArgumentException e )
		{
			dtde.rejectDrop() ;
			DialogUtil.error( this , e.getMessage() ) ;
		}
	}

	public void dropActionChanged( DropTargetDragEvent dtde ){}
}
