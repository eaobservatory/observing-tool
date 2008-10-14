// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor ;

import gemini.sp.SpItem ;
import jsky.util.gui.DialogUtil ;
import java.util.Hashtable ;

/**
 * This class is used to obtain the editor for SpItems.  Each program or plan
 * has 0 or 1 editor of a given type.  An editor is created, if it doesn't
 * already exist, when a node of the corresponding science program type is
 * double-clicked or selected with the component editor already open.
 * <p>
 * Editors are stored in hashtables using the program or plan item as a key.
 * This avoids creating a new editor every time the user clicks on a node
 * in the program.  Each program or plan has to have its own editors though,
 * because more than one program can be open and editing the same type of
 * item at once.
 */
public final class OtItemEditorFactory
{
	private static Hashtable<String,Hashtable<SpItem,OtItemEditor>> _editorMap = new Hashtable<String,Hashtable<SpItem,OtItemEditor>>() ;

	/**
	 * Get an item editor given an associated SpItem from the program and
	 * the name of the class for the item.
	 */
	public static OtItemEditor getEditor( String editorClassName , SpItem spItem )
	{
		Hashtable<SpItem,OtItemEditor> ht = _editorMap.get( editorClassName ) ;
		if( ht == null )
		{
			ht = new Hashtable<SpItem,OtItemEditor>() ;
			_editorMap.put( editorClassName , ht ) ;
		}

		OtItemEditor ed = ht.get( spItem.getRootItem() ) ;
		if( ed == null )
		{
			try
			{
				Class c = Class.forName( editorClassName ) ;
				ed = ( OtItemEditor )c.newInstance() ;
				ht.put( spItem.getRootItem() , ed ) ;
			}
			catch( Exception ex )
			{
				// It is important to print this stack trace. The error dialog below does not
				// give any detailed information about what went wrong. And this exception occurs frequenty
				// when new components are added to the OT. (MFO, November 27, 2002)
				ex.printStackTrace() ;

				DialogUtil.error( null , "Problem instantiating: " + editorClassName + ", " + ex ) ;
				return null ;
			}
		}
		return ed ;
	}
}
