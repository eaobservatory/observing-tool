// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.obsComp;
 
import gemini.sp.SpAvTable;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTelescopePosList;
import gemini.sp.SpType;


/**
 * A class for telescope observation component items.  Maintains a
 * position list and keeps up-to-date the base position element of
 * the observation data for the observation context.
 *
 * @see gemini.sp.SpTelescopePosList
 * @see gemini.sp.SpObsData
 * @see gemini.sp.SpObsContextItem
 */
public class SpTelescopeObsComp extends SpObsComp
{
   protected SpTelescopePosList _posList;

public SpTelescopeObsComp()
{
   super(SpType.OBSERVATION_COMPONENT_TARGET_LIST);
   _init();
}

/**
 * Initialize the position list.
 */
protected void
_init()
{
   _posList = null;
   SpTelescopePos.createDefaultBasePosition(_avTable);
}

/**
 * Override clone to make sure the position list is correctly
 * initialized.
 */
public Object
clone()
{
   SpTelescopeObsComp toc = (SpTelescopeObsComp) super.clone();
   //toc._init();
   toc._posList = null;
   return toc;
}

/**
 * Override getTitle to return the name of the base position if set.
 */
public String
getTitle()
{
   // By default, append the name of the base position.  If a title
   // has been directly set though, use that instead.
   String titleAttr = getTitleAttr();
   if ((titleAttr == null) || titleAttr.equals("")) {
      SpTelescopePosList tpl = getPosList();
      SpTelescopePos     tp  = tpl.getBasePosition();
      if (tp == null) {
         return super.getTitle();
      }
      String name = tp.getName();
      if ((name == null) || name.equals("")) {
         return super.getTitle();
      }
      return type().getReadable() + ": " + name;
   } else {
      return super.getTitle();
   }
}

/**
 * Get a position list data structure for the telescope positions
 * in the attribute table.
 */
public SpTelescopePosList
getPosList()
{
   if (_posList == null) {
      _posList = new SpTelescopePosList(this);
   }

   return _posList;
}

/**
 * Override setTable to update the position list, and to change the
 * base position associated with the context this item is in.
 *
 * @see gemini.sp.SpObsData
 * @see gemini.sp.SpObsContextItem
 */
protected void
setTable(SpAvTable avTab)
{
   super.setTable(avTab);
   if (_posList != null) {
      // NOTE this will reset the base position in the obs data.
      _posList.setTable(avTab);
   }
}

/**
 * Override setTable to update the position list, and to change the
 * base position associated with the context this item is in.
 *
 * @see gemini.sp.SpObsData
 * @see gemini.sp.SpObsContextItem
 */
protected void
replaceTable(SpAvTable avTab)
{
   super.replaceTable(avTab);
   if (_posList != null) {
      // NOTE this will reset the base position in the obs data.
      _posList.setTable(avTab);
   }
}

}

