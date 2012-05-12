// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot ;

import gemini.sp.SpCloneableClientData ;
import gemini.sp.SpItem ;
import jsky.app.ot.tpe.TpeFeatureClientData ;
import jsky.app.ot.tpe.TpeImageFeature ;
import ot.util.DialogUtil ;

/**
 * This class groups information that should be stored with each SpItem
 * on behalf of the OT.  Since SpCloneableClientData is implemented,
 * when the associated SpItem is cloned, this client data will be cloned
 * as well (which causes the OtTreeNodeWidget to be cloned).
 */
public final class OtClientData implements SpCloneableClientData , TpeFeatureClientData
{
	/**
	 * The TreeNodeWidget representing the item in the structure/hierarchy
	 * editor display.
	 */
	public OtTreeNodeWidget tnw ;

	/**
	 * The full class name of the editor for this node.
	 */
	public String itemEditorClass ;

	/**
	 * The full class name of any TpeImageFeature subclasses that should
	 * be associated with this item (if any).  This may turn into an array
	 * in a future release.
	 */
	public String tpeImageFeatureClass ;

	//
	// A reference to an instantiated TpeImageFeature.
	//
	TpeImageFeature _feature ;

	/**
	 * Construct with an OtTreeNodeWidget and the name of the item editor
	 * for this class.
	 */
	public OtClientData( OtTreeNodeWidget tnw , String itemEditorClass )
	{
		this.tnw = tnw ;
		this.itemEditorClass = itemEditorClass ;
	}

	/**
	 * Construct with tree node widget, item editor class name, and an
	 * image feature class name.
	 */
	public OtClientData( OtTreeNodeWidget tnw , String itemEditorClass , String imageFeatureClass )
	{
		this( tnw , itemEditorClass ) ;
		tpeImageFeatureClass = imageFeatureClass ;
	}

	/**
	 * Create a new object of the same class as this object, with the same
	 * values.
	 */
	public Object clone( SpItem spItem )
	{
		OtClientData cd ;
		try
		{
			cd = ( OtClientData )super.clone() ;
		}
		catch( CloneNotSupportedException ex )
		{
			// This won't happen so long as SpCloneableClientData implements
			// Cloneable
			return null ;
		}

		cd.tnw = this.tnw.copy();
		cd.tnw.setItem( spItem ) ;
		cd.tnw.setText( spItem.getTitle() ) ;

		return cd ;
	}

	/**
	 * Get the TpeImageFeature, instantiating it if necessary.
	 */
	public TpeImageFeature getImageFeature()
	{
		if( ( _feature == null ) && ( tpeImageFeatureClass != null ) )
		{
			try
			{
				Class<?> c = Class.forName( tpeImageFeatureClass ) ;
				_feature = ( TpeImageFeature )c.newInstance() ;
			}
			catch( Exception ex )
			{
				DialogUtil.error( tnw.getTreeWidget() , "Problem instantiating: " + tpeImageFeatureClass , ex ) ;
				_feature = null ;
			}
		}
		return _feature ;
	}
}
