// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;
 
import gemini.util.CoordSys;
import gemini.util.HHMMSS;
import gemini.util.DDMMSS;
import gemini.util.RADecMath;
import gemini.util.TelescopePos;

/**
 * A data object that describes a telescope position and includes methods
 * for extracting positions from A/V tables.
 *
 * <h3>Fields of a SpTelescopePos Value</h3>
 * Each SpTelescopePos is an attribute that consists of a set of 5 values
 * at the given indices:
 * <pre>
 *    Index  Field
 *        0  tag   - one of "Base", "User", or a site-specific guide star 
 *                   tag name like "PWFS1"
 *        1  name  - arbitrary name of the position (e.g., a tag from a
 *                   position in a guide star catalog)
 *        2  xaxis - the x axis of the position (e.g., the RA)
 *        3  yaxis - the y axis of the position (e.g., the Dec)
 *        4  coordinate system - (currently only FK5/J2000 is supported)
 * </pre>
 * The name of the attribute is always the same as the name of the tag.
 * Therefore, each tag must be unique.
 */
public final class SpTelescopePos extends TelescopePos implements java.io.Serializable
{
   //
   // A position can have one of the following tags.  "Guide" and "User"
   // tags have a numeric suffix appended to them to make them unique.
   //
   public static final String   BASE_TAG  = "Base";
   public static final String   USER_TAG  = "User";
   public static String[] GUIDE_TAGS = {"PWFS1", "PWFS2", "OIWFS"};

   // CHOP MODE parameters (added by MFO, 2 August 2001)
   public static final String ATTR_CHOP_MODE	= "chopMode";
   public static final String ATTR_CHOP_THROW	= "chopThrow";
   public static final String ATTR_CHOP_ANGLE	= "chopAngle";

   // Indices of the the fields of a position
   public static final int TAG_INDEX		=  0;
   public static final int NAME_INDEX		=  1;
   public static final int XAXIS_INDEX		=  2;
   public static final int YAXIS_INDEX		=  3;
   public static final int COORD_SYS_INDEX	=  4;

   public static final int PROP_MOTION_RA	=  5;
   public static final int PROP_MOTION_DEC	=  6;

   public static final int TRACKING_SYSTEM	=  7;
   public static final int TRACKING_EPOCH	=  8;
   public static final int TRACKING_PARALLAX	=  9;
   public static final int TRACKING_RADIAL_VEL  = 10;
   public static final int TRACKING_EFF_WAVELENGTH = 11;

   private SpItem    _spItem;
   private SpAvTable _avTab;	// The table that holds this position

   // Additional data that makes up a telescope position
  
   // Name of the position, if any.
   String _name;

   // Coordinate system being used (see gemini.util.CoordSys)
   int _coordSys;

   // Position is valid.
   boolean _isValid = false;

/**
 * Set the list of tags that represent guide stars.  For instance,
 * for Gemini, the set of tags are "PWFS1", "PWFS2", "OIWFS".
 */
public static void
setGuideStarTags(String[] tags)
{
   GUIDE_TAGS = tags;
}

/**
 * Get the list of tags that represent guide stars.
 */
public static String[]
getGuideStarTags()
{
   return GUIDE_TAGS;
}

/**
 * Create a SpTelescopePos object, bound to an attribute with the same name
 * as its tag.  SpTelescopePos objects are created by the SpTelescopePosList.
 */
protected SpTelescopePos(SpItem spItem, SpAvTable avTab,
                         String tag,    SpTelescopePosList list)
{
   super(tag, list);
   _spItem = spItem;
   _avTab  = avTab;

   if (avTab.exists(tag)) {
      _name = avTab.get(tag, NAME_INDEX);

      String system = avTab.get(tag, COORD_SYS_INDEX);
      if (system == null) {
         system = CoordSys.COORD_SYS[CoordSys.FK5];
      }
      _coordSys = CoordSys.getSystem(system);

      String xaxisStr = avTab.get(tag, XAXIS_INDEX);
      String yaxisStr = avTab.get(tag, YAXIS_INDEX);
      _updateXYFromString(xaxisStr, yaxisStr);

   } else {
      // Create a new (blank) position and a new attribute
      _name     = "";
      _xaxis    = 0.0;
      _yaxis    = 0.0;
      _coordSys = CoordSys.FK5;
      _isValid  = false;

      avTab.set(tag, tag, TAG_INDEX);
      avTab.set(tag, "",  NAME_INDEX);
      avTab.set(tag, "",  XAXIS_INDEX);
      avTab.set(tag, "",  YAXIS_INDEX);
      avTab.set(tag, CoordSys.COORD_SYS[_coordSys], COORD_SYS_INDEX);
   }
}

/**
 * Hack for the SpTelescopeObsComp.  It needs to always be created
 * with a base position, so it calls this method in the constructor.
 */
public static void
createDefaultBasePosition(SpAvTable avTab)
{
   avTab.noNotifySet(BASE_TAG, BASE_TAG, 	TAG_INDEX);
   avTab.noNotifySet(BASE_TAG, "",	 	NAME_INDEX);
   avTab.noNotifySet(BASE_TAG, "0:00:00",	XAXIS_INDEX);
   avTab.noNotifySet(BASE_TAG, "0:00:00",	YAXIS_INDEX);
   avTab.noNotifySet(BASE_TAG, CoordSys.COORD_SYS[CoordSys.FK5],COORD_SYS_INDEX);
}


//
// Set the tag, thus changing the attribute name.  This method should only
// be available to SpTelescopePosList, not general clients.
//
void
setTag(String newTag)
{
   synchronized (this) {
      String propMotionRA  = _avTab.get(_tag, PROP_MOTION_RA);
      String propMotionDec = _avTab.get(_tag, PROP_MOTION_DEC);
      String trackSystem   = _avTab.get(_tag, TRACKING_SYSTEM);
      String trackEpoch    = _avTab.get(_tag, TRACKING_EPOCH);
      String trackParallax = _avTab.get(_tag, TRACKING_PARALLAX);
      String trackRadialVel= _avTab.get(_tag, TRACKING_RADIAL_VEL);

      _avTab.rm(_tag);

      // Convert from degrees to the current coordinate system
      String[] pos;
      if (_isValid) {
         pos = RADecMath.degrees2String(_xaxis, _yaxis, _coordSys);
      } else {
         pos = new String[2];
         pos[0] = "";
         pos[1] = "";
      }

      _tag = newTag;
      _avTab.set(newTag, newTag,    TAG_INDEX);
      _avTab.set(newTag, _name,     NAME_INDEX);
      _avTab.set(newTag, pos[0],    XAXIS_INDEX);
      _avTab.set(newTag, pos[1],    YAXIS_INDEX);
      _avTab.set(newTag, CoordSys.getSystem(_coordSys), COORD_SYS_INDEX);

      if (propMotionRA != null) {
         _avTab.set(newTag, propMotionRA, PROP_MOTION_RA);
      }
      if (propMotionDec != null) {
         _avTab.set(newTag, propMotionDec, PROP_MOTION_DEC);
      }
      if (trackSystem != null) {
         _avTab.set(newTag, trackSystem, TRACKING_SYSTEM);
      }
      if (trackEpoch != null) {
         _avTab.set(newTag, trackEpoch, TRACKING_EPOCH);
      }
      if (trackParallax != null) {
         _avTab.set(newTag, trackParallax, TRACKING_PARALLAX);
      }
      if (trackRadialVel!= null) {
         _avTab.set(newTag, trackRadialVel, TRACKING_RADIAL_VEL);
      }
   }
   _notifyOfGenericUpdate();
}

/**
 * Set the name.
 */
public void
setName(String name)
{
   synchronized (this) {
      _name = name;
      _avTab.set(_tag, name, NAME_INDEX);
   }
   _notifyOfGenericUpdate();
}


/**
 * Get the name.
 */
public String
getName()
{
   return _name;
}


/**
 * Allow setting x and y axes without notifying observers.
 */
public synchronized void
noNotifySetXY(double xaxis, double yaxis)
{
   _xaxis   = xaxis;
   _yaxis   = yaxis;
   _isValid = true;

   // Convert from degrees to the current coordinate system
   String[] pos = RADecMath.degrees2String(xaxis, yaxis, _coordSys);
   _avTab.set(_tag, pos[0], XAXIS_INDEX);
   _avTab.set(_tag, pos[1], YAXIS_INDEX);

   if (_tag.equals(BASE_TAG)) {
      SpObsData od = _spItem.getObsData();
      if (od != null) od.setBasePos(xaxis, yaxis);
   }
}

//
// Set the x and y axes from a string, without notifying observers or
// modifying any attributes.
//
private synchronized void
_updateXYFromString(String xaxisStr, String yaxisStr)
{
   // Convert from whatever coordinate system to degrees
   double[] pos = RADecMath.string2Degrees(xaxisStr, yaxisStr, _coordSys);
   if (pos == null) {
      _xaxis   = 0.0;
      _yaxis   = 0.0;
      _isValid = false;
   } else {
      _xaxis   = pos[0];
      _yaxis   = pos[1];
      _isValid = true;
   }
}

/**
 * Allow setting x and y axes without notifying observers.
 */
public synchronized void
noNotifySetXYFromString(String xaxisStr, String yaxisStr)
{
   _updateXYFromString(xaxisStr, yaxisStr);

   _avTab.set(_tag, xaxisStr, XAXIS_INDEX);
   _avTab.set(_tag, yaxisStr, YAXIS_INDEX);

   if (_tag.equals(BASE_TAG)) {
      SpObsData od = _spItem.getObsData();
      if (od != null) od.setBasePos(_xaxis, _yaxis);
   }
}

/**
 * Set the xaxis and the yaxis.
 */
public void
setXY(double xaxis, double yaxis)
{
   noNotifySetXY(xaxis, yaxis);
   _notifyOfLocationUpdate();
}

/**
 * Set the xaxis and the yaxis.
 */
public void
setXYFromString(String xaxisStr, String yaxisStr)
{
   noNotifySetXYFromString(xaxisStr, yaxisStr);
   _notifyOfLocationUpdate();
}

/**
 * Override getXaxisAsString to directly return the appropriate
  * attribute value.
 */
public synchronized String
getXaxisAsString()
{
   return _avTab.get(_tag, XAXIS_INDEX);
}

/**
 * Override getYaxisAsString to directly return the appropriate
 * attribute value.
 */
public synchronized String
getYaxisAsString()
{
   return _avTab.get(_tag, YAXIS_INDEX);
}

/**
 * Is this position valid?
 */
public boolean
isValid()
{
   return _isValid;
}

/**
 * Set the Coordinate System with an int (presumably from the coordinate
 * system static constants).
 */
public void
setCoordSys(int i)
{
   String sysString = CoordSys.getSystem(i);
   if (sysString == null) {
      return;
   }

   // Eventually, will have to change the String stored in the _avTable
   // and update the values of this.xaxis and this.yaxis

   synchronized (this) {
      _coordSys = i;
      _avTab.set(_tag, sysString, COORD_SYS_INDEX);
   }
   _notifyOfGenericUpdate();
}

/**
 * Set the Coordinate System with a string.
 */
public void
setCoordSys(String coordSysString)
{
   int sysInt = CoordSys.getSystem(coordSysString);
   if (sysInt == -1) {
      return;
   }

   // Eventually, will have to change the String stored in the _avTable
   // and update the values of this.xaxis and this.yaxis

   synchronized (this) {
      _coordSys = sysInt;
      _avTab.set(_tag, coordSysString, COORD_SYS_INDEX);
   }
   _notifyOfGenericUpdate();
}

/**
 * Get coordinate system used by this position.
 */
public int
getCoordSys()
{
   return _coordSys;
}

/**
 * Get coordinate system used by this position as a String.
 */
public String
getCoordSysAsString()
{
   return CoordSys.getSystem(_coordSys);
}

/**
 * Is this the "base" position?
 */
public synchronized boolean
isBasePosition()
{
   return _tag.equals(BASE_TAG);
}

/**
 * Is this the "base" position?
 */
public static boolean
isBasePositionTag(String tag)
{
   return tag.equals(BASE_TAG);
}

/**
 * Is this a guide star?
 */
public synchronized boolean
isGuidePosition()
{
   return isGuidePositionTag(_tag);
}

/**
 * Is this tag a guide star tag?
 */
public static boolean
isGuidePositionTag(String tag)
{
   for (int i=0; i<GUIDE_TAGS.length; ++i) {
      if (tag.equals(GUIDE_TAGS[i])) return true;
   }
   return false;
}

/**
 * Is this a "user" position?
 */
public synchronized boolean
isUserPosition()
{
   return _tag.startsWith(USER_TAG);
}

/**
 * Is this a "user" position?
 */
public static boolean
isUserPositionTag(String tag)
{
   return tag.startsWith(USER_TAG);
}

/**
 * Get the index of the User tag, its unique number suffix.
 */
public static int
getUserPositionIndex(String tag)
{
   String index = tag.substring(USER_TAG.length());
   return Integer.parseInt(index);
}


/**
 * Get the proper motion RA.
 */
public String
getPropMotionRA()
{
   String res = _avTab.get(_tag, PROP_MOTION_RA);
   if (res == null) {
      res = "0";
   }
   return res;
}

/**
 * Set the proper motion ra as a string.
 */
public void
setPropMotionRA(String raStr)
{
   _avTab.set(_tag, raStr, PROP_MOTION_RA);
   _notifyOfGenericUpdate();
}

/**
 * Get the proper motion Dec.
 */
public String
getPropMotionDec()
{
   String res = _avTab.get(_tag, PROP_MOTION_DEC);
   if (res == null) {
      res = "0";
   }
   return res;
}

/**
 * Set the proper motion Dec as a string.
 */
public void
setPropMotionDec(String decStr)
{
   _avTab.set(_tag, decStr, PROP_MOTION_DEC);
   _notifyOfGenericUpdate();
}

/**
 * Get the tracking system.
 */
public String
getTrackingSystem()
{
   String res = _avTab.get(_tag, TRACKING_SYSTEM);
   if (res == null) {
      res = CoordSys.COORD_SYS[CoordSys.FK5];
   }
   return res;
}

/**
 * Set the tracking system as a string.
 */
public void
setTrackingSystem(String trackSys)
{
   _avTab.set(_tag, trackSys, TRACKING_SYSTEM);
   _notifyOfGenericUpdate();
}

/**
 * Get the tracking epoch.
 */
public String
getTrackingEpoch()
{
   String res = _avTab.get(_tag, TRACKING_EPOCH);
   if (res == null) {
      res = "2000";
   }
   return res;
}

/**
 * Set the tracking epoch as a string.
 */
public void
setTrackingEpoch(String trackEpoch)
{
   _avTab.set(_tag, trackEpoch, TRACKING_EPOCH);
   _notifyOfGenericUpdate();
}

/**
 * Get the tracking parallax.
 */
public String
getTrackingParallax()
{
   String res = _avTab.get(_tag, TRACKING_PARALLAX);
   if (res == null) {
      res = "0";
   }
   return res;
}

/**
 * Set the tracking parallax as a string.
 */
public void
setTrackingParallax(String trackParallax)
{
   _avTab.set(_tag, trackParallax, TRACKING_PARALLAX);
   _notifyOfGenericUpdate();
}

/**
 * Get the tracking radial velocity.
 */
public String
getTrackingRadialVelocity()
{
   String res = _avTab.get(_tag, TRACKING_RADIAL_VEL);
   if (res == null) {
      res = "0";
   }
   return res;
}

/**
 * Set the tracking radial velocity as a string.
 */
public void
setTrackingRadialVelocity(String trackRadialVel)
{
   _avTab.set(_tag, trackRadialVel, TRACKING_RADIAL_VEL);
   _notifyOfGenericUpdate();
}

/**
 * Get the tracking effective wavelength.
 */
public String
getTrackingEffectiveWavelength()
{
   String res = _avTab.get(_tag, TRACKING_EFF_WAVELENGTH);
   if (res == null) {
      res = "auto";
   }
   return res;
}

/**
 * Set the tracking effective wavelength as a string.
 */
public void
setTrackingEffectiveWavelength(String trackEffWave)
{
   _avTab.set(_tag, trackEffWave, TRACKING_EFF_WAVELENGTH);
   _notifyOfGenericUpdate();
}

/**
 * Get chop mode.
 *
 * Values: CHOP_MODE_ON, CHOP_MODE_OFF
 *
 * Added by MFO (2 August 2001)
 */
public boolean
getChopMode()
{
   return _avTab.getBool(ATTR_CHOP_MODE);
}

/**
 * Set chop mode as a string.
 *
 * Values: CHOP_MODE_ON, CHOP_MODE_OFF
 *
 * Added by MFO (2 August 2001)
 */
public void
setChopMode(boolean chopModeOn)
{
   _avTab.set(ATTR_CHOP_MODE, chopModeOn);
   _notifyOfGenericUpdate();
}

/**
 * Get the chop throw as String.
 *
 * Added by MFO (2 August 2001)
 */
public String
getChopThrowAsString()
{
   String res = _avTab.get(ATTR_CHOP_THROW);
   if (res == null) {
      res = "0.0";
   }
   return res;
}

/**
 * Get the chop throw.
 *
 * Added by MFO (2 August 2001)
 */
public double
getChopThrow()
{
   return Double.parseDouble(getChopThrowAsString());
}

/**
 * Set the chop throw as a string.
 *
 * Added by MFO (2 August 2001)
 */
public void
setChopThrow(String chopThrowStr)
{
   _avTab.set(ATTR_CHOP_THROW, chopThrowStr);
   _notifyOfGenericUpdate();
}

/**
 * Get the chop angle as string.
 *
 * Added by MFO (2 August 2001)
 */
public String
getChopAngleAsString()
{
   String res = _avTab.get(ATTR_CHOP_ANGLE);
   if (res == null) {
      res = "0.0";
   }
   return res;
}

/**
 * Get the chop angle.
 *
 * Added by MFO (2 August 2001)
 */
public double
getChopAngle()
{
   return Double.parseDouble(getChopAngleAsString());
}

/**
 * Set the chop angle as a string.
 *
 * Added by MFO (2 August 2001)
 */
public void
setChopAngle(String chopAngleStr)
{
   _avTab.set(ATTR_CHOP_ANGLE, chopAngleStr);
   _notifyOfGenericUpdate();
}


/**
 * Standard debugging method.
 */
public synchronized String
toString()
{
   return getClass().getName() + "[" +
	"name=" + getName() +
	", tag=" + getTag() +
	", xaxis=" + getXaxisAsString() + " (" + getXaxis() + ")" +
	", yaxis=" + getYaxisAsString() + " (" + getYaxis() + ")" +
	", coordSystem=" + getCoordSysAsString() + "]";
}

}
