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

   // CHOP MODE parameters (added by MFO, 6 August 2001)
   public static final String ATTR_CHOPPING	= "chopping";
   public static final String ATTR_CHOP_THROW	= "chopThrow";
   public static final String ATTR_CHOP_ANGLE	= "chopAngle";


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

/**
 * Get chopping on / off.
 *
 * Added by MFO (6 August 2001)
 */
public boolean
isChopping()
{
   return _avTable.getBool(ATTR_CHOPPING);
}

/**
 * Set chopping on / off.
 *
 * Added by MFO (6 August 2001)
 */
public void
setChopping(boolean choppingOn)
{
   _avTable.set(ATTR_CHOPPING, choppingOn);
   // MFO TODO: _notifyOfGenericUpdate or equivalent has to be implemented
   // (for TelescopesPosWatcher or TelescopePosListWatcher).
   //_notifyOfGenericUpdate();
}

/**
 * Get the chop throw as String.
 *
 * Added by MFO (6 August 2001)
 */
public String
getChopThrowAsString()
{
   String res = _avTable.get(ATTR_CHOP_THROW);
   if (res == null) {
      res = "0.0";
   }
   return res;
}

/**
 * Get the chop throw.
 *
 * Added by MFO (6 August 2001)
 */
public double
getChopThrow()
{
   return Double.valueOf(getChopThrowAsString()).doubleValue();
}

/**
 * Set the chop throw as a string.
 *
 * Added by MFO (6 August 2001)
 */
public void
setChopThrow(String chopThrowStr)
{
   _avTable.set(ATTR_CHOP_THROW, chopThrowStr);
   // MFO TODO: _notifyOfGenericUpdate or equivalent has to be implemented
   // (for TelescopesPosWatcher or TelescopePosListWatcher).
   //_notifyOfGenericUpdate();
}

/**
 * Get the chop angle as string.
 *
 * Added by MFO (6 August 2001)
 */
public String
getChopAngleAsString()
{
   String res = _avTable.get(ATTR_CHOP_ANGLE);
   if (res == null) {
      res = "0.0";
   }
   return res;
}

/**
 * Get the chop angle.
 *
 * Added by MFO (6 August 2001)
 */
public double
getChopAngle()
{
   return Double.valueOf(getChopAngleAsString()).doubleValue();
}

/**
 * Set the chop angle as a string.
 *
 * Added by MFO (6 August 2001)
 */
public void
setChopAngle(String chopAngleStr)
{
   _avTable.set(ATTR_CHOP_ANGLE, chopAngleStr);
   // MFO TODO: _notifyOfGenericUpdate or equivalent has to be implemented
   // (for TelescopesPosWatcher or TelescopePosListWatcher).
   //_notifyOfGenericUpdate();
}

}

