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
import gemini.util.Format;

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
 *
 * <h3>Target Tags</h3> <!-- Added by MFO (9 January 2002) -->
 * Note that internally the OT only allowed {@link #BASE_TAG}, {@link #USER_TAG} and
 * {@link #GUIDE_TAGS}.
 * This is because the OT was originally developed for Optical/IR telescopes (Gemini/UKIRT)
 * But the strings used in the user interfaces and in the XML output can be changed to
 * other telescope specific names. Example: For JCMT BASE_TAG is set to "Science".
 */
public final class SpTelescopePos extends TelescopePos implements java.io.Serializable
{
   /**
    * Target Name.
    *
    * _avTab.get(ATTR_TARGET_NAME is the same as _avTab.get(BASE_TAG, NAME_INDEX).
    * The target name is stored twice in the table. ATTR_TARGET_NAME is used for
    * the XML output. BASE_TAG, NAME_INDEX is used by the UKIRT translator.
    */
   public static final String ATTR_TARGET_NAME         = "target.targetName";

   /**
    * Target System based on two coordinates.
    *
    * RA/Dec, Az/El etc.
    * See JAC OCS TCS.
    */
   public static final int TARGET_SYSTEM_HMSDEG_DEGDEG = 0;

   /**
    * Conic Target System.
    *
    * Represented by its Oribtal Elements.
    * See JAC OCS TCS.
    */
   public static final int TARGET_SYSTEM_CONIC         = 1;

   /**
    * Named Target System.
    *
    * A target is specified by its name. Used for planets, sun, moon etc.
    * See JAC OCS TCS.
    */
   public static final int TARGET_SYSTEM_NAMED         = 2;

   /** Conic/Named System type Major. */
   public static final int TYPE_MAJOR = 0;
   
   /** Conic/Named System type Minor. */
   public static final int TYPE_MINOR = 1;
   
   /** Conic System type Comet. */
   public static final int TYPE_COMET = 2;

   /** Named System type Planetary Satellite. */
   public static final int TYPE_PLANETARY_SATELLITE = TYPE_COMET;



   /**
    * XML attributes for types of conic systems.
    *
    * "major", "minor", "planetarySatellite". <P>
    *
    * See JAC OCS TCS XML.
    *
    * @see #NAMED_SYSTEM_TYPES_DESCRIPITON 
    */
   public static final String [] NAMED_SYSTEM_TYPES_XML         = { "major", "minor", "planetarySatellite"  };

   /**
    * XML attributes for types of conic systems.
    *
    * "Major", "Minor", "Planetary Satellite". <P>
    *
    * See JAC OCS TCS XML.
    *
    * @see #NAMED_SYSTEM_TYPES_XML
    */
   public static final String [] NAMED_SYSTEM_TYPES_DESCRIPITON = { "Major", "Minor", "Planetary Satellite" };


   /**
    * XML attributes for types of conic systems.
    *
    * "major", "minor", "comet".<P>
    *
    * See JAC OCS TCS XML.
    *
    * @see #CONIC_SYSTEM_TYPES_DESCRIPITON 
    */
   public static final String [] CONIC_SYSTEM_TYPES_XML         = { "major", "minor", "comet" };

   /**
    * XML attributes for types of conic systems.
    *
    * "Major", "Minor", "Comet".<P>
    *
    * See JAC OCS TCS XML.
    *
    * @see #CONIC_SYSTEM_TYPES_XML
    */
   public static final String [] CONIC_SYSTEM_TYPES_DESCRIPITON = { "Major", "Minor", "Comet" };

   /** @see #getConicSystemEpoch() */
   public static final String ATTR_CONIC_SYSTEM_EPOCH       = "target.conicSystem.epoch";

   /** @see #getConicSystemInclination() */
   public static final String ATTR_CONIC_SYSTEM_INCLINATION = "target.conicSystem.inclination";

   /** @see #getConicSystemAnode() */
   public static final String ATTR_CONIC_SYSTEM_ANODE       = "target.conicSystem.anode";

   /** @see #getConicSystemPerihelion() */
   public static final String ATTR_CONIC_SYSTEM_PERIHELION  = "target.conicSystem.perihelion";

   /** @see #getConicSystemAorQ() */
   public static final String ATTR_CONIC_SYSTEM_AORQ        = "target.conicSystem.aorq";

   /** @see #getConicSystemE() */
   public static final String ATTR_CONIC_SYSTEM_E           = "target.conicSystem.e";

   /** @see #getConicSystemLorM() */
   public static final String ATTR_CONIC_SYSTEM_LORM        = "target.conicSystem.LorM";

   /** @see #getConicSystemDailyMotion() */
   public static final String ATTR_CONIC_SYSTEM_DM          = "target.conicSystem.n";

   /** @see #getConicSystemType() */
   public static final String ATTR_CONIC_SYSTEM_TYPE        = "target.conicSystem:type";


   /** @see #getNamedSystemType() */
   public static final String ATTR_NAMED_SYSTEM_TYPE        = "target.namedSystem:type";


   //
   // A position can have one of the following tags.  "Guide" and "User"
   // tags have a numeric suffix appended to them to make them unique.
   //
   public static       String   BASE_TAG  = "Base";
   public static final String   USER_TAG  = "User";
   public static String[] GUIDE_TAGS = {"PWFS1", "PWFS2", "OIWFS"};

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
 * Set BASE_TAG to another value.
 *
 * By default the base tag is "Base". If required this can be changed.
 * E.g. JCMT uses the tag "Science".
 *
 * Added by MFO, 19 December 2001.
 */
public static void
setBaseTag(String tag)
{
   BASE_TAG = tag;
}


// Added by MFO, 7 January 2002.
public static String
getBaseTag()
{
   return BASE_TAG;
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
 * Get the kind of target system: HMSDEG/DEGDEG, Conic, Named.
 *
 * Do not confuse with the coord system.
 *
 * @return One of {@link #TARGET_SYSTEM_HMSDEG_DEGDEG}, {@link #TARGET_SYSTEM_CONIC}, {@link #TARGET_SYSTEM_NAMED}
 */
public int getTargetSystem() {
   if(_avTab.exists(tagPrefix() + ATTR_CONIC_SYSTEM_TYPE)) {
      return TARGET_SYSTEM_CONIC;
   }

   if(_avTab.exists(tagPrefix() + ATTR_NAMED_SYSTEM_TYPE)) {
      return TARGET_SYSTEM_NAMED;
   }

   return TARGET_SYSTEM_HMSDEG_DEGDEG;
}

/**
 * Set the kind of target system: HMSDEG/DEGDEG, Conic, Named.
 *
 * Do not confuse with the coord system.
 *
 * @param targetSystem One of {@link #TARGET_SYSTEM_HMSDEG_DEGDEG}, {@link #TARGET_SYSTEM_CONIC}, {@link #TARGET_SYSTEM_NAMED}
 */
public void setTargetSystem(int targetSystem) {
   if(targetSystem == getTargetSystem()) {
      return;
   }

   switch(targetSystem) {
      case TARGET_SYSTEM_CONIC:
         _avTab.noNotifyRm(_tag);

         _avTab.noNotifySet(tagPrefix() + ATTR_CONIC_SYSTEM_EPOCH, "" + 0.0, 0);
         _avTab.noNotifySet(tagPrefix() + ATTR_CONIC_SYSTEM_INCLINATION, "" + 0.0, 0);
         _avTab.noNotifySet(tagPrefix() + ATTR_CONIC_SYSTEM_ANODE, "" + 0.0, 0);
         _avTab.noNotifySet(tagPrefix() + ATTR_CONIC_SYSTEM_PERIHELION, "" + 0.0, 0);
         _avTab.noNotifySet(tagPrefix() + ATTR_CONIC_SYSTEM_AORQ, "" + 0.0, 0);
         _avTab.noNotifySet(tagPrefix() + ATTR_CONIC_SYSTEM_E, "" + 0.0, 0);
         _avTab.noNotifySet(tagPrefix() + ATTR_CONIC_SYSTEM_TYPE, CONIC_SYSTEM_TYPES_XML[TYPE_COMET], 0);

         _avTab.noNotifyRm(tagPrefix() + ATTR_NAMED_SYSTEM_TYPE);

         // Last table change: notify
	 // Conic system uses table attribute tagPrefix() + ATTR_TARGET_NAME to store the name. 
	 _avTab.set(tagPrefix() + ATTR_TARGET_NAME, _name);

	 break;

      case TARGET_SYSTEM_NAMED:
         _avTab.noNotifyRm(_tag);

         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_EPOCH);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_INCLINATION);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_ANODE);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_PERIHELION);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_AORQ);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_E);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_LORM);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_DM);
	 _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_TYPE);

         _avTab.noNotifySet(tagPrefix() + ATTR_NAMED_SYSTEM_TYPE, NAMED_SYSTEM_TYPES_XML[TYPE_MAJOR], 0);
         
         // Last table change: notify
	 // Named system uses table attribute tagPrefix() + ATTR_TARGET_NAME to store the name. 
	 _avTab.set(tagPrefix() + ATTR_TARGET_NAME, _name);

	 break;

      //case TARGET_SYSTEM_HMSDEG_DEGDEG:
      default:
         _avTab.noNotifySet(_tag, _tag,      TAG_INDEX);
         _avTab.noNotifySet(_tag, _name,     NAME_INDEX);
         _avTab.noNotifySet(_tag, "0:00:00", XAXIS_INDEX);
         _avTab.noNotifySet(_tag, "0:00:00", YAXIS_INDEX);
         _avTab.noNotifySet(_tag, CoordSys.COORD_SYS[CoordSys.FK5], COORD_SYS_INDEX);

         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_EPOCH);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_INCLINATION);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_ANODE);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_PERIHELION);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_AORQ);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_E);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_LORM);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_DM);
         _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_TYPE);
	 
         _avTab.noNotifyRm(tagPrefix() + ATTR_NAMED_SYSTEM_TYPE);

         // Last table change: notify
	 // HMSDEG/DEGDEG system uses table attribute _tag with NAME_INDEX to store the name.
	 // So remove tagPrefix() + ATTR_TARGET_NAME.
         _avTab.rm(tagPrefix() + ATTR_TARGET_NAME);

	 break;
   }

   _notifyOfLocationUpdate();
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

      if(getTargetSystem() == TARGET_SYSTEM_HMSDEG_DEGDEG) {
         _avTab.set(_tag, name, NAME_INDEX);
      }
      else {
         // Equivalent to _avTab.set(tag, name,  NAME_INDEX); Needed for XML output. (MFO, 27 February 2002)
         _avTab.set(tagPrefix() + ATTR_TARGET_NAME, name);
      }	 
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

   // Eventually, will have to change the String stored in the _avTab
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

   // Eventually, will have to change the String stored in the _avTab
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
 * Conic System (Oribital Elements).
 *
 * Get the epoch of the orbital elements or epoch of perihelion (t0, T).
 */
public double
getConicSystemEpoch()
{
   return _avTab.getDouble(tagPrefix() + ATTR_CONIC_SYSTEM_EPOCH, 0.0);
}

/**
 * Conic System (Orbital Elements).
 * 
 * Set the epoch of the orbital elements or epoch of perihelion (t0, T).
 */
public void
setConicSystemEpoch(String value)
{
   _avTab.set(tagPrefix() + ATTR_CONIC_SYSTEM_EPOCH, Format.toDouble(value));
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the inclination of the orbit (i).
 */
public double
getConicSystemInclination()
{
   return _avTab.getDouble(tagPrefix() + ATTR_CONIC_SYSTEM_INCLINATION, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the inclination of the orbit (i).
 */
public void
setConicSystemInclination(String value)
{
   _avTab.set(tagPrefix() + ATTR_CONIC_SYSTEM_INCLINATION, Format.toDouble(value));
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the longitude of the ascending node (&Omega;).
 */
public double
getConicSystemAnode()
{
   return _avTab.getDouble(tagPrefix() + ATTR_CONIC_SYSTEM_ANODE, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the longitude of the ascending node (&Omega;).
 */
public void
setConicSystemAnode(String value)
{
   _avTab.set(tagPrefix() + ATTR_CONIC_SYSTEM_ANODE, Format.toDouble(value));
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the argument of perihelion (&omega;).
 */
public double
getConicSystemPerihelion()
{
   return _avTab.getDouble(tagPrefix() + ATTR_CONIC_SYSTEM_PERIHELION, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the argument of perihelion (&omega;).
 */
public void
setConicSystemPerihelion(String value)
{
   _avTab.set(tagPrefix() + ATTR_CONIC_SYSTEM_PERIHELION, Format.toDouble(value));
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the mean distance (a) or perihelion distance (q).
 */
public double
getConicSystemAorQ()
{
   return _avTab.getDouble(tagPrefix() + ATTR_CONIC_SYSTEM_AORQ, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the mean distance (a) or perihelion distance (q).
 */
public void
setConicSystemAorQ(String value)
{
   _avTab.set(tagPrefix() + ATTR_CONIC_SYSTEM_AORQ, Format.toDouble(value));
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the orbital eccentricity (e).
 */
public double
getConicSystemE()
{
   return _avTab.getDouble(tagPrefix() + ATTR_CONIC_SYSTEM_E, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the orbital eccentricity (e).
 */
public void
setConicSystemE(String value)
{
   _avTab.set(tagPrefix() + ATTR_CONIC_SYSTEM_E, Format.toDouble(value));
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the longitude (L) or Mean Distance (M).
 */
public double
getConicSystemLorM()
{
   return _avTab.getDouble(tagPrefix() + ATTR_CONIC_SYSTEM_LORM, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the longitude (L) or mean distance (M).
 */
public void
setConicSystemLorM(String value)
{
   _avTab.set(tagPrefix() + ATTR_CONIC_SYSTEM_LORM, Format.toDouble(value));
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the daily motion (n).
 */
public double
getConicSystemDailyMotion()
{
   return _avTab.getDouble(tagPrefix() + ATTR_CONIC_SYSTEM_DM, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the daily motion (n).
 */
public void
setConicSystemDailyMotion(String value)
{
   _avTab.set(tagPrefix() + ATTR_CONIC_SYSTEM_DM, Format.toDouble(value));
}


/**
 * Get type of Orbital Elements conic system.
 *
 * @return One of {@link #CONIC_SYSTEM_TYPES_XML}
 *
 * @see #CONIC_SYSTEM_TYPES_DESCRIPITON
 */
public String
getConicSystemType()
{
   return _avTab.get(tagPrefix() + ATTR_CONIC_SYSTEM_TYPE);
}

/**
 * Get type of Orbital Elements conic system as readable string.
 *
 * @return One of {@link #CONIC_SYSTEM_TYPES_DESCRIPITON}
 *
 * @see #CONIC_SYSTEM_TYPES_XML
 */
public String
getConicSystemTypeDescription()
{
   String conicSystemTypeXML = getConicSystemType();

   for(int i = 0; i < CONIC_SYSTEM_TYPES_XML.length; i++) {
      if(conicSystemTypeXML.equals(CONIC_SYSTEM_TYPES_XML[i])) {
         return CONIC_SYSTEM_TYPES_DESCRIPITON[i];
      }
   }
   
   return null;
}


/**
 * Set type of Orbital Elements conic system.
 *
 * @param conicSystemType {@link #CONIC_SYSTEM_TYPES_XML}
 *
 * @see #CONIC_SYSTEM_TYPES_DESCRIPITON
 */
public void
setConicSystemType(String conicSystemType)
{
   _avTab.set(tagPrefix() + ATTR_CONIC_SYSTEM_TYPE, conicSystemType);

   if(conicSystemType.equals(CONIC_SYSTEM_TYPES_XML[TYPE_MAJOR])) {
      if(!_avTab.exists(tagPrefix() + ATTR_CONIC_SYSTEM_LORM)) {
         _avTab.noNotifySet(tagPrefix() + ATTR_CONIC_SYSTEM_LORM, "" + 0.0, 0);
      }

     _avTab.noNotifySet(tagPrefix() + ATTR_CONIC_SYSTEM_DM,   "" + 0.0, 0);

     return;
   }

   if(conicSystemType.equals(CONIC_SYSTEM_TYPES_XML[TYPE_MINOR])) {
      if(!_avTab.exists(tagPrefix() + ATTR_CONIC_SYSTEM_LORM)) {
         _avTab.noNotifySet(tagPrefix() + ATTR_CONIC_SYSTEM_LORM, "" + 0.0, 0);
      }

      _avTab.noNotifyRm( tagPrefix() + ATTR_CONIC_SYSTEM_DM);

      return;
   }

   _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_LORM);
   _avTab.noNotifyRm(tagPrefix() + ATTR_CONIC_SYSTEM_DM);
}


/**
 * Get type of named system.
 *
 * A named target is used for planets, sun, moon etc.
 *
 * @return One of {@link #NAMED_SYSTEM_TYPES_XML}
 *
 * @see #NAMED_SYSTEM_TYPES_DESCRIPITON
 */
public String
getNamedSystemType()
{
   return _avTab.get(tagPrefix() + ATTR_NAMED_SYSTEM_TYPE);
}

/**
 * Get type of named system as readable string.
 *
 * A named target is used for planets, sun, moon etc.
 *
 * @return One of {@link #NAMED_SYSTEM_TYPES_DESCRIPITON}
 *
 * @see #NAMED_SYSTEM_TYPES_XML
 */
public String
getNamedSystemTypeDescription()
{
   String namedSystemTypeXML = getNamedSystemType();

   for(int i = 0; i < NAMED_SYSTEM_TYPES_XML.length; i++) {
      if(namedSystemTypeXML.equals(NAMED_SYSTEM_TYPES_XML[i])) {
         return NAMED_SYSTEM_TYPES_DESCRIPITON[i];
      }
   }
   
   return null;
}

/**
 * Get type of named system.
 *
 * A named target is used for planets, sun, moon etc.
 *
 * @param namedSystemType {@link #NAMED_SYSTEM_TYPES_XML}
 *
 * @see #NAMED_SYSTEM_TYPES_DESCRIPITON
 */
public void
setNamedSystemType(String namedSystemType)
{
   _avTab.set(tagPrefix() + ATTR_NAMED_SYSTEM_TYPE, namedSystemType);
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

protected String
tagPrefix()
{
   if(_tag == null) {
      return "";
   }

   if(_tag.equalsIgnoreCase("base")) {
      return "SCIENCE.";
   }

   return _tag + ".";
}
}
