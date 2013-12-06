/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
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

package ot ;

import gemini.util.ObservingToolUtilities;

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

/**
 * Waste bin drop target for deleting sp items / tree nodes.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class OtWasteBin extends JLabel implements DropTargetListener
{
	protected String imgpath = "ot/images/" ;
	protected Icon wasteIcon = new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "waste.gif" ) ) ;
	protected Icon wasteSelectedIcon = new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "wasteSelected.gif" ) ) ;

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
