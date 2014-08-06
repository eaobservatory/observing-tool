/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
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

package orac.util ;

import gemini.sp.SpItem ;
import gemini.sp.SpMSB ;
import gemini.sp.SpAND ;
import gemini.sp.SpOR ;
import gemini.sp.SpObsContextItem ;
import gemini.sp.SpRootItem ;
import gemini.sp.obsComp.SpSiteQualityObsComp ;
import gemini.sp.obsComp.SpSchedConstObsComp ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpObs ;
import gemini.sp.SpNote ;
import gemini.sp.SpHierarchyChangeObserver ;
import java.util.Vector ;
import java.util.Enumeration ;
import java.util.Iterator ;

/**
 * Utilities for SpItem trees.
 *
 * @see gemini.sp.SpTreeMan
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpItemUtilities
{
	/**
	 * Attribute for the SpAvTable of each SpObsContextItem
	 * that will be translated to the XML id attribute.
	 *
	 * @see #ID_REF_SUFFIX
	 */
	public static final String ATTR_ID = ":id" ;

	/**
	 */
	/**
	 * ID_REF_SUFFIX is going to be preceded by the class name.
	 *
	 * So the SpAvTable attribute name is going to look like this:
	 * For, say, SpTelescopeObsComp we get the SpAvTable attribute
	 * name "SpTelescopeObsCompRef:idref" with the XML representation:
	 * <!-- <SpTelescopeObsCompRef id="some_id"> -->
	 *      &lt;SpTelescopeObsCompRef id="some_id"&gt;
	 * inside the the SpMSB element.
	 *   
	 */
	public static final String ID_REF_SUFFIX = "Ref:idref" ;

	private static int _idCounter = 0 ;

	/**
	 * This method calls saveElapsedTime() on all MSBs and Observations that are MSBs.
	 *
	 * This utility method can be used to set all the elapsed times (time estimates),
	 * before saving to disk or storing to database.
	 * This means that this method sets the {@link gemini.sp.SpMSB#ATTR_ELAPSED_TIME}
	 * attributes of all SpMSB components and SpObs components that are MSBs
	 * based on the current setting.
	 * Unlike other attributes  the elapsed time is not directly typed in by the
	 * user but depends on the MSB/Observation as a whole. Therefore it has to be
	 * set in all SpMSB components and SpObs components that are MSBs immediately
	 * before saving/storing so that elapsed to written to XML is based on the latest
	 * settings of these  components.
	 */
	public static void saveElapsedTimes( SpItem spItem )
	{
		if( spItem instanceof SpMSB )
		{
			SpMSB spMSB = ( SpMSB )spItem ;
			spMSB.saveElapsedTime() ;
			spMSB.saveTotalTime() ;
		}

		if( spItem instanceof SpObsContextItem )
		{
			SpItem child = spItem.child() ;

			while( child != null )
			{
				saveElapsedTimes( child ) ;
				child = child.next() ;
			}
		}
	}

	/**
	 * Recursively updates the MSB attributes of all
	 * SpObs items in spItem.
	 */
	public static void updateMsbAttributes( SpItem spItem )
	{
		if( spItem instanceof SpObs )
			( ( SpObs )spItem ).updateMsbAttributes() ;

		if( spItem instanceof SpObsContextItem )
		{
			SpItem child = spItem.child() ;

			while( child != null )
			{
				updateMsbAttributes( child ) ;
				child = child.next() ;
			}
		}
	}

	/**
	 * Returns hierarchy observer object for SpItems.
	 *
	 * Returns an object that can be be added to an SpItem (especially SpRootItem, SpProg etc)
	 * to observe hierarchy changes and execute necessary code such as updating msb attributs.
	 *
	 * @see #updateMsbAttributes(SpItem)
	 */
	public static SpHierarchyChangeObserver getHierarchyChangeUtil()
	{
		return new SpHierarchyChangeObserver()
		{
			public void spItemsAdded( SpItem parent , SpItem[] children , SpItem afterChild )
			{
				updateMsbAttributes( parent ) ;
			}

			public void spItemsMoved( SpItem oldParent , SpItem[] children , SpItem newParent , SpItem afterChild )
			{
				updateMsbAttributes( oldParent ) ;
				updateMsbAttributes( newParent ) ;
			}

			public void spItemsRemoved( SpItem parent , SpItem[] children ){}
		} ;
	}

	/**
	 * Remove id and idref attributes from spItem subtree.
	 */
	public static void removeReferenceIDs( SpItem spItem )
	{
		spItem.getTable().noNotifyRm( ATTR_ID ) ;

		Iterator<String> attributes = spItem.getTable().getAttrIterator() ;
		String idrefAttribute = null ;

		while( attributes.hasNext() )
		{
			idrefAttribute = attributes.next() ;
			if( idrefAttribute.endsWith( ID_REF_SUFFIX ) )
				attributes.remove() ;
		}

		if( spItem instanceof SpObsContextItem )
		{
			SpItem child = spItem.child() ;

			while( child != null )
			{
				removeReferenceIDs( child ) ;
				child = child.next() ;
			}
		}
	}

	/**
	 * Find SpMSB's and SpObs's recursively and set references.
	 */
	public static void setReferenceIDs( SpItem spItem )
	{
		if( spItem instanceof SpRootItem )
			_idCounter = 0 ;
		if( spItem instanceof SpObsContextItem )
		{
			if( spItem instanceof SpMSB )
			{
				_insertReferenceIDsFor( SpTreeMan.findTargetList( spItem ) , spItem ) ;
				_insertReferenceIDsFor( SpTreeMan.findInstrument( spItem ) , spItem ) ;
				_insertReferenceIDsFor( findSiteQuality( spItem ) , spItem ) ;
				_insertReferenceIDsFor( findSchedConstraint( spItem ) , spItem ) ;
				_insertReferenceIDsFor( SpTreeMan.findDRRecipe( spItem ) , spItem ) ;
				_insertReferenceIDsFor( findProgramNotes( spItem.getRootItem() , new Vector<SpItem>() ) , spItem ) ;
				_insertReferenceIDsFor( findParentNotes( spItem , new Vector<SpItem>() ) , spItem ) ;
			}
			else
			{
				SpItem child = spItem.child() ;

				while( child != null )
				{
					setReferenceIDs( child ) ;
					child = child.next() ;
				}
			}
		}
	}

	/**
	 * @param spObsCompVector
	 *                  This vector used to contain only items of type SpObsComp
	 *                  but it can now contain any items of type SpItem
	 *                  in order to allow top level SpNotes to be inherited
	 *                  by MSBs that are further down in the tree. This is the
	 *                  same kind of inheritance that applies to SpObsComp items
	 *                  such as instrument components, site quality components etc.
	 */
	protected static void _insertReferenceIDsFor( Vector<SpItem> spObsCompVector , SpItem spItem )
	{
		if( spObsCompVector == null )
			return ;

		for( int i = 0 ; i < spObsCompVector.size() ; i++ )
			_insertReferenceIDsFor( spObsCompVector.get( i ) , spItem ) ;
	}

	/**
	 * @param spObsComp This argument used to be of type SpObsComp but it has been changed to
	 *                  SpItem in order to allow top level SpNotes to be inherited
	 *                  by MSBs that are further down in the tree. This is the
	 *                  same kind of inheritance that applies to SpObsComp items
	 *                  such as instrument components, site quality components etc.
	 */
	protected static void _insertReferenceIDsFor( SpItem spObsComp , SpItem spItem )
	{
		if( ( spObsComp == null ) || ( spItem == null ) )
			return ;

		// See whether the spObsComp (e.g. observation component or SpNote) has an id already. If so use it for idref of the SpMSB.
		String idString = spObsComp.getTable().get( ATTR_ID ) ;
		if( idString != null )
		{
			if( spObsComp.getClass().getName().indexOf( "SpNote" ) == -1 )
				spItem.getTable().noNotifySet( spObsComp.getClass().getName().substring( spObsComp.getClass().getName().lastIndexOf( "." ) + 1 ) + ID_REF_SUFFIX , idString , 0 ) ;
			else
				spItem.getTable().noNotifySet( spObsComp.getClass().getName().substring( spObsComp.getClass().getName().lastIndexOf( "." ) + 1 ) + ID_REF_SUFFIX + idString , idString , 0 ) ;
		}
		// No id yet. Use _idCounter for both spObsComp (e.g. observation component or SpNote) and spItem (e.g. SpMSB/SpObs). 
		else
		{
			spObsComp.getTable().noNotifySet( ATTR_ID , "" + _idCounter , 0 ) ;
			if( spObsComp.getClass().getName().indexOf( "SpNote" ) == -1 )
				spItem.getTable().noNotifySet( spObsComp.getClass().getName().substring( spObsComp.getClass().getName().lastIndexOf( "." ) + 1 ) + ID_REF_SUFFIX , "" + _idCounter , 0 ) ;
			else
				spItem.getTable().noNotifySet( spObsComp.getClass().getName().substring( spObsComp.getClass().getName().lastIndexOf( "." ) + 1 ) + ID_REF_SUFFIX + _idCounter , "" + _idCounter , 0 ) ;

			_idCounter++ ;
		}
	}

	/**
	 * Find the site quality component assoicated with this context, if any.  Only
	 * searches the given scope.  It does not navigate the tree hierarchy.
	 */
	public static SpSiteQualityObsComp findSiteQualityInContext( SpItem spItem )
	{
		SpSiteQualityObsComp sqc = null ;
		SpItem returned = SpTreeMan.findSpItemInContext( spItem , SpSiteQualityObsComp.class ) ;
		if( returned != null )
			sqc = ( SpSiteQualityObsComp )returned ;
		return sqc ;
	}

	/**
	 * Find the scheduling constraint component assoicated with this context, if any.  Only
	 * searches the given scope.  It does not navigate the tree hierarchy.
	 */
	public static SpSchedConstObsComp findSchedConstraintInContext( SpItem spItem )
	{
		SpSchedConstObsComp scc = null ;
		SpItem returned = SpTreeMan.findSpItemInContext( spItem , SpSchedConstObsComp.class ) ;
		if( returned != null )
			scc = ( SpSchedConstObsComp )returned ;
		return scc ;
	}

	/**
	 * Find the SpNote assoicated with this context, if any.  Only
	 * searches the given scope.  It does not navigate the tree hierarchy.
	 */
	public static SpNote findNoteInContext( SpItem spItem )
	{
		SpNote note = null ;
		Enumeration<SpItem> e = spItem.children() ;
		while( e.hasMoreElements() )
		{
			SpItem child = e.nextElement() ;
			if( child instanceof SpNote )
			{
				note = ( SpNote )child ;
				if( note.isObserveInstruction() )
				{
					break ;
				}
				else
				{
					note = null ;
					continue ;
				}
			}
		}
		return note ;
	}

	/**
	 * Find the site quality component associated with the given scope
	 * scope of the given item.
	 *
	 * @param spItem the SpItem defining the scope to search
	 */
	public static SpSiteQualityObsComp findSiteQuality( SpItem spItem )
	{
		SpSiteQualityObsComp siteQuality = null ;
		SpItem returned = SpTreeMan.findSpItemOfType( spItem , SpSiteQualityObsComp.class ) ;
		if( returned != null )
			siteQuality = ( SpSiteQualityObsComp )returned ;
		return siteQuality ;
	}

	/**
	 * Find the scheduling constraint component associated with the given scope
	 * scope of the given item.
	 *
	 * @param spItem the SpItem defining the scope to search
	 */
	public static SpSchedConstObsComp findSchedConstraint( SpItem spItem )
	{
		SpSchedConstObsComp schedConst = null ;
		SpItem returned = SpTreeMan.findSpItemOfType( spItem , SpSchedConstObsComp.class ) ;
		if( returned != null )
			schedConst = ( SpSchedConstObsComp )returned ;
		return schedConst ;
	}

	/**
	 * Find the SpNote associated with the scope of the given item.
	 *
	 * The idea of note covering a scope rather than just being
	 * associated with an item is new. It was introduced in connection
	 * with SpMSB folders. Having notes that cover a scope allows
	 * notes that are added to a top level folder (AND, OR, Science Program)
	 * to be accessible to all MSBs in the scope. This is useful
	 * when the Science Program tree is disassembled into MSBs (e.g. in the OMP)
	 *
	 * @param spItem the SpItem defining the scope to search
	 */
	public static SpNote findNote( SpItem spItem )
	{
		SpNote note = null ;
		SpItem returned = SpTreeMan.findSpItemOfType( spItem , SpNote.class ) ;
		if( returned != null )
			note = ( SpNote )returned ;
		return note ;
	}

	public static Vector<SpItem> findProgramNotes( SpItem spItem , Vector<SpItem> notes )
	{
		// Now get all the children
		Enumeration<SpItem> e = spItem.children() ;
		while( e.hasMoreElements() )
		{
			SpItem child = e.nextElement() ;
			if( child instanceof gemini.sp.SpNote )
			{
				if( ( ( SpNote )child ).isObserveInstruction() )
					notes.add( child ) ;
			}
		}
		return notes ;
	}

	public static Vector<SpItem> findParentNotes( SpItem spItem , Vector<SpItem> notes )
	{
		SpItem parent = spItem.parent() ;
		while( !( parent instanceof SpRootItem ) )
		{
			if( parent instanceof SpAND || parent instanceof SpOR )
			{
				Enumeration<SpItem> e = parent.children() ;
				while( e.hasMoreElements() )
				{
					SpItem child = e.nextElement() ;
					if( child instanceof SpNote && ( ( SpNote )child ).isObserveInstruction() )
						notes.add( child ) ;
				}
			}
			parent = parent.parent() ;
		}
		return notes ;
	}
}
