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

import java.util.StringTokenizer;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
 * There are 22 indeces in all. See below.
 * </pre>
 * The name of the attribute is always the same as the name of the tag.
 * Therefore, each tag must be unique.
 *
 * <h3>Target Tags</h3> <!-- Added by MFO (9 January 2002) -->
 * Note that internally the OT only allowed {@link #BASE_TAG}, {@link #USER_TAG} and
 * {@link #GUIDE_TAGS}.
 * This is because the OT was originally developed for Optical/IR telescopes (Gemini/UKIRT)
 * But the tags can be changed to other telescope specific names.
 * Example: For JCMT BASE_TAG is set to "SCIENCE".
 */
public final class SpTelescopePos extends TelescopePos implements java.io.Serializable
{
   /** Year of origin of MJD: 1858. */
   public static final int MJD_0_YEAR  = 1858;

   /** Year of origin of MJD: java.util.Calendar.NOVEMBER. */
   public static final int MJD_0_MONTH = Calendar.NOVEMBER;

   /** Day of origin of MJD: 17. */
   public static final int MJD_0_DAY   = 17;

   private static final String [] MONTH_NAMES = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                                                  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

   /**
    * System based on two coordinates.
    *
    * RA/Dec, Az/El etc.
    * See JAC OCS TCS.
    */
   public static final int SYSTEM_SPHERICAL     = 0;

   /**
    * Conic System.
    *
    * Represented by its Oribtal Elements.
    * See JAC OCS TCS.
    */
   public static final int SYSTEM_CONIC         = 1;

   /**
    * Named System.
    *
    * A target is specified by its name. Used for planets, sun, moon etc.
    * See JAC OCS TCS.
    */
   public static final int SYSTEM_NAMED         = 2;

   /** Conic/Named System type Major. */
   public static final int TYPE_MAJOR = 0;
   
   /** Conic/Named System type Minor. */
   public static final int TYPE_MINOR = 1;
   
   /** Conic System type Comet. */
   public static final int TYPE_COMET = 2;

   /** Named System type Planetary Satellite. */
   public static final int TYPE_PLANETARY_SATELLITE = TYPE_COMET;



   /**
    * Attributes for types of conic systems.
    *
    * "major", "minor", "planetarySatellite". <P>
    *
    * @see #NAMED_SYSTEM_TYPES_DESCRIPTION 
    */
   public static final String [] NAMED_SYSTEM_TYPES         = { "major", "minor", "planetarySatellite"  };

   /**
    * Readable attributes for types of conic systems.
    *
    * "Major", "Minor", "Planetary Satellite". <P>
    *
    * @see #NAMED_SYSTEM_TYPES
    */
   public static final String [] NAMED_SYSTEM_TYPES_DESCRIPTION = { "Major", "Minor", "Planetary Satellite" };


   /**
    * Attributes for types of conic systems.
    *
    * "major", "minor", "comet".<P>
    *
    * @see #CONIC_SYSTEM_TYPES_DESCRIPTION 
    */
   public static final String [] CONIC_SYSTEM_TYPES         = { "major", "minor", "comet" };

   /**
    * Readable attributes for types of conic systems.
    *
    * "Major", "Minor", "Comet".<P>
    *
    * @see #CONIC_SYSTEM_TYPES
    */
   public static final String [] CONIC_SYSTEM_TYPES_DESCRIPTION = { "Major", "Minor", "Comet" };


   /** @see #isOffsetPosition() */
   public static final String ATTR_OFFSET_POSITION          = "isOffset";

   //
   // A position can have one of the following tags.  "Guide" and "User"
   // tags have a numeric suffix appended to them to make them unique.
   //
   public static       String   BASE_TAG  = "Base";
   public static final String   USER_TAG  = "User";
   public static String[] GUIDE_TAGS = {"PWFS1", "PWFS2", "OIWFS"};

   // Indices of the the fields of a position
   /** Index for SpAvTable value (position in value Vector). */
   public static final int TAG_INDEX		=  0;
   /** Index for SpAvTable value (position in value Vector). */
   public static final int NAME_INDEX		=  1;
   /** Index for SpAvTable value (position in value Vector). */
   public static final int XAXIS_INDEX		=  2;
   /** Index for SpAvTable value (position in value Vector). */
   public static final int YAXIS_INDEX		=  3;
   /** Index for SpAvTable value (position in value Vector). */
   public static final int COORD_SYS_INDEX	=  4;

   /** Index for SpAvTable value (position in value Vector). */
   public static final int PROP_MOTION_RA	=  5;
   /** Index for SpAvTable value (position in value Vector). */
   public static final int PROP_MOTION_DEC	=  6;

   /** Index for SpAvTable value (position in value Vector). */
   public static final int TRACKING_SYSTEM	=  7;
   /** Index for SpAvTable value (position in value Vector). */
   public static final int TRACKING_EPOCH	=  8;
   /** Index for SpAvTable value (position in value Vector). */
   public static final int TRACKING_PARALLAX	=  9;
   /** Index for SpAvTable value (position in value Vector). */
   public static final int TRACKING_RADIAL_VEL  = 10;
   /** Index for SpAvTable value (position in value Vector). */
   public static final int TRACKING_EFF_WAVELENGTH = 11;


   /**
    * Index for SpAvTable value (position in value Vector).
    *
    * @see #getSystemType()
    */
   public static final int SYSTEM_TYPE              = 12;

   /**
    * Index for SpAvTable value (position in value Vector).
    *
    * @see #isOffsetPosition()
    */
   public static final int OFFSET_POSITION          = 13;

   /**
    * Index for SpAvTable value (position in value Vector).
    *
    * @see #getConicOrNamedType()
    */
   public static final int CONIC_OR_NAMED_TYPE      = 14;

   /**
    * Index for SpAvTable value (position in value Vector).
    *
    * @see #getConicSystemEpoch()
    */
   public static final int CONIC_SYSTEM_EPOCH       = 15;

   /**
    * Index for SpAvTable value (position in value Vector).
    *
    * @see #getConicSystemInclination()
    */
   public static final int CONIC_SYSTEM_INCLINATION = 16;

   /**
    * Index for SpAvTable value (position in value Vector).
    *
    * @see #getConicSystemAnode()
    */
   public static final int CONIC_SYSTEM_ANODE       = 17;

   /**
    * Index for SpAvTable value (position in value Vector).
    *
    * @see #getConicSystemPerihelion()
    */
   public static final int CONIC_SYSTEM_PERIHELION  = 18;

   /**
    * Index for SpAvTable value (position in value Vector).
    *
    * @see #getConicSystemAorQ()
    */
   public static final int CONIC_SYSTEM_AORQ        = 19;

   /**
    * Index for SpAvTable value (position in value Vector).
    *
    * @see #getConicSystemE()
    */
   public static final int CONIC_SYSTEM_E           = 20;

   /**
    * Index for SpAvTable value (position in value Vector).
    *
    * @see #getConicSystemLorM()
    */
   public static final int CONIC_SYSTEM_LORM        = 21;

   /**
    * Index for SpAvTable value (position in value Vector).
    *
    * @see #getConicSystemDailyMotion()
    */
   public static final int CONIC_SYSTEM_DM          = 22;



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
 * Get the system type: spherical, conic, named.
 *
 * Do not confuse with the coord system.
 *
 * @return One of {@link #SYSTEM_SPHERICAL}, {@link #SYSTEM_CONIC}, {@link #SYSTEM_NAMED}
 */
public int getSystemType() {
   return _avTab.getInt(_tag, SYSTEM_TYPE, SYSTEM_SPHERICAL);
}

/**
 * Set the system type: spherical, conic, named.
 *
 * Do not confuse with the coord system.
 *
 * @param systemType One of {@link #SYSTEM_SPHERICAL}, {@link #SYSTEM_CONIC}, {@link #SYSTEM_NAMED}
 *                     If another int is supplied then the system is set to {@link #SYSTEM_SPHERICAL}.
 */
public void setSystemType(int systemType) {

   // Make sure only valid a systemType int is stored in the table.
   switch(systemType) {
      case SYSTEM_CONIC:
         _avTab.set(_tag, SYSTEM_CONIC, SYSTEM_TYPE);
         setConicOrNamedType(CONIC_SYSTEM_TYPES[TYPE_COMET]);
         break;

      case SYSTEM_NAMED:
         _avTab.set(_tag, SYSTEM_NAMED, SYSTEM_TYPE);
         setConicOrNamedType(NAMED_SYSTEM_TYPES[TYPE_MAJOR]);
         break;

      default:
         _avTab.set(_tag, SYSTEM_SPHERICAL, SYSTEM_TYPE);
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
         if(isOffsetPosition() || ((getCoordSys() != CoordSys.FK5) && (getCoordSys() != CoordSys.FK4))) {
	    pos = new String[]{ "" + _xaxis, "" + _yaxis };
	 }
	 else {
            pos = RADecMath.degrees2String(_xaxis, _yaxis, _coordSys);
	 }
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
   if(isOffsetPosition() || ((getCoordSys() != CoordSys.FK5) && (getCoordSys() != CoordSys.FK4))) {
     _avTab.set(_tag, xaxis, XAXIS_INDEX);
     _avTab.set(_tag, yaxis, YAXIS_INDEX);
   }
   else {
     String[] pos = RADecMath.degrees2String(xaxis, yaxis, _coordSys);
     _avTab.set(_tag, pos[0], XAXIS_INDEX);
     _avTab.set(_tag, pos[1], YAXIS_INDEX);
   }

   if (_tag.equals(BASE_TAG)) {
      SpObsData od = _spItem.getObsData();
      if (od != null) od.setBasePos(xaxis, yaxis, _coordSys);
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
   try {
      double[] pos = null;
    
      if(isOffsetPosition() || ((getCoordSys() != CoordSys.FK5) && (getCoordSys() != CoordSys.FK4))) {
         pos = new double[] { 0.0, 0.0 };
	 
	 try {
	    pos[0] = Double.parseDouble(xaxisStr);
	 }   
	 catch(Exception e) {
            pos[0] = 0.0;
	 }

	 try {
	    pos[1] = Double.parseDouble(yaxisStr);
	 }   
	 catch(Exception e) {
            pos[1] = 0.0;
	 }
      }
      else {
         pos = RADecMath.string2Degrees(xaxisStr, yaxisStr, _coordSys);

         if (pos == null) {
            _xaxis   = 0.0;
            _yaxis   = 0.0;
            _isValid = false;
         }
         else {
           _xaxis   = pos[0];
           _yaxis   = pos[1];
           _isValid = true;
         }
      }  
   }
   catch (IllegalArgumentException e) { System.out.println("IllegalArumentException: xaxisStr = \"" + xaxisStr + "\", yaxisStr = \"" + yaxisStr + "\""); }

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
      if (od != null) od.setBasePos(_xaxis, _yaxis, _coordSys);
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

   if (_tag.equals(BASE_TAG)) {
      SpObsData od = _spItem.getObsData();
      if (od != null) od.setBasePos(_xaxis, _yaxis, _coordSys);
   }

   _notifyOfGenericUpdate();

   // Changing the coordinate system while maintaining the
   // coordinate values (as done in the OT) results in
   // change of location. (MFO, April 09, 2002)
   _notifyOfLocationUpdate();
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

   if (_tag.equals(BASE_TAG)) {
      SpObsData od = _spItem.getObsData();
      if (od != null) od.setBasePos(_xaxis, _yaxis, _coordSys);
   }

   _notifyOfGenericUpdate();

   // Changing the coordinate system while maintaining the
   // coordinate values (as done in the OT) results in
   // change of location. (MFO, April 09, 2002)
   _notifyOfLocationUpdate();
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
   return _avTab.getDouble(_tag, CONIC_SYSTEM_EPOCH, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Get the epoch of the orbital elements or epoch of perihelion (t0, T).
 */
public String
getConicSystemEpochAsString()
{
   return convertMJD(getConicSystemEpoch());
}

/**
 * Conic System (Orbital Elements).
 * 
 * Set the epoch of the orbital elements or epoch of perihelion (t0, T).
 */
public void
setConicSystemEpoch(String value)
{
   try {
      _avTab.set(_tag, Double.parseDouble(value), CONIC_SYSTEM_EPOCH);
   }
   catch(NumberFormatException e) {
      _avTab.set(_tag, convertMJD(value), CONIC_SYSTEM_EPOCH);
   }
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the inclination of the orbit (i).
 */
public double
getConicSystemInclination()
{
   return _avTab.getDouble(_tag, CONIC_SYSTEM_INCLINATION, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the inclination of the orbit (i).
 */
public void
setConicSystemInclination(String value)
{
   _avTab.set(_tag, Format.toDouble(value), CONIC_SYSTEM_INCLINATION);
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the longitude of the ascending node (&Omega;).
 */
public double
getConicSystemAnode()
{
   return _avTab.getDouble(_tag, CONIC_SYSTEM_ANODE, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the longitude of the ascending node (&Omega;).
 */
public void
setConicSystemAnode(String value)
{
   _avTab.set(_tag, Format.toDouble(value), CONIC_SYSTEM_ANODE);
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the argument of perihelion (&omega;).
 */
public double
getConicSystemPerihelion()
{
   return _avTab.getDouble(_tag, CONIC_SYSTEM_PERIHELION, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the argument of perihelion (&omega;).
 */
public void
setConicSystemPerihelion(String value)
{
   _avTab.set(_tag, Format.toDouble(value), CONIC_SYSTEM_PERIHELION);
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the mean distance (a) or perihelion distance (q).
 */
public double
getConicSystemAorQ()
{
   return _avTab.getDouble(_tag, CONIC_SYSTEM_AORQ, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the mean distance (a) or perihelion distance (q).
 */
public void
setConicSystemAorQ(String value)
{
   _avTab.set(_tag, Format.toDouble(value), CONIC_SYSTEM_AORQ);
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the orbital eccentricity (e).
 */
public double
getConicSystemE()
{
   return _avTab.getDouble(_tag, CONIC_SYSTEM_E, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the orbital eccentricity (e).
 */
public void
setConicSystemE(String value)
{
   _avTab.set(_tag, Format.toDouble(value), CONIC_SYSTEM_E);
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the longitude (L) or Mean Distance (M).
 */
public double
getConicSystemLorM()
{
   return _avTab.getDouble(_tag, CONIC_SYSTEM_LORM, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the longitude (L) or mean distance (M).
 */
public void
setConicSystemLorM(String value)
{
   _avTab.set(_tag, Format.toDouble(value), CONIC_SYSTEM_LORM);
}


/**
 * Conic System (Oribital Elements).
 *
 * Get the daily motion (n).
 */
public double
getConicSystemDailyMotion()
{
   return _avTab.getDouble(_tag, CONIC_SYSTEM_DM, 0.0);
}

/**
 * Conic System (Oribital Elements).
 *
 * Set the daily motion (n).
 */
public void
setConicSystemDailyMotion(String value)
{
   _avTab.set(_tag, Format.toDouble(value), CONIC_SYSTEM_DM);
}


/**
 * Get type of conic or named system.
 *
 * Types are Major, Minor, Comet, Planetary Satelite.
 *
 * @return One of {@link #CONIC_SYSTEM_TYPES} or {@link #NAMED_SYSTEM_TYPE}
 *
 * @see #CONIC_SYSTEM_TYPES
 * @see #NAMED_SYSTEM_TYPES
 */
public String
getConicOrNamedType()
{
   return _avTab.get(_tag, CONIC_OR_NAMED_TYPE);
}

/**
 * Get type of conic or named system as readable string.
 *
 * Types are Major, Minor, Comet, Planetary Satelite.
 *
 * @return One of {@link #CONIC_SYSTEM_TYPES_DESCRIPTION} or {@link #NAMED_SYSTEM_TYPES_DESCRIPTION}
 *
 * @see #NAMED_SYSTEM_TYPES
 * @see #CONIC_SYSTEM_TYPES
 */
public String
getConicOrNamedTypeDescription()
{
   String conicOrNamedType = getConicOrNamedType();

   if(conicOrNamedType == null) {
     return null;
   }

   if(getSystemType() == SYSTEM_CONIC) {
      for(int i = 0; i < CONIC_SYSTEM_TYPES.length; i++) {
         if(conicOrNamedType.equals(CONIC_SYSTEM_TYPES[i])) {
            return CONIC_SYSTEM_TYPES_DESCRIPTION[i];
         }
      }
   }
   
   if(getSystemType() == SYSTEM_NAMED) {
      for(int i = 0; i < NAMED_SYSTEM_TYPES.length; i++) {
         if(conicOrNamedType.equals(NAMED_SYSTEM_TYPES[i])) {
            return NAMED_SYSTEM_TYPES_DESCRIPTION[i];
         }
      }
   }

   // If the system type is spherical or if no type is stored in _avTab
   // then return null;
   return null;
}

/**
 * Set type of conic or named system.
 *
 * This method has no effect if the system is spherical.
 *
 * @param systemType Must be a valid system type string (One of {@link #CONIC_SYSTEM_TYPES} or
                     {@link #NAMED_SYSTEM_TYPE}. Otherwise the method call has no effect.
 *
 * @see #getConicOrNamedType()
 */
public void
setConicOrNamedType(String systemType)
{
   // Make sure only valid system types are stored.

   if(getSystemType() == SYSTEM_CONIC) {
      if(systemType.equals(CONIC_SYSTEM_TYPES[TYPE_MAJOR]) ||
         systemType.equals(CONIC_SYSTEM_TYPES[TYPE_MINOR]) ||
         systemType.equals(CONIC_SYSTEM_TYPES[TYPE_COMET])) {

         _avTab.set(_tag, systemType, CONIC_OR_NAMED_TYPE);
      }
   }
   
   if(getSystemType() == SYSTEM_NAMED) {
      if(systemType.equals(NAMED_SYSTEM_TYPES[TYPE_MAJOR]) ||
         systemType.equals(NAMED_SYSTEM_TYPES[TYPE_MINOR]) ||
         systemType.equals(NAMED_SYSTEM_TYPES[TYPE_PLANETARY_SATELLITE])) {
	
         _avTab.set(_tag, systemType, CONIC_OR_NAMED_TYPE);
      }
   }
}

/**
 * Set a base position.
 * 
 * This will cause the x axis and y axis of position to be interpreted
 * as offsets
 */
public void
setOffsetPosition(boolean offset)
{
   _avTab.set(_tag, offset, OFFSET_POSITION);
}


/**
 * Returns true if this position is an offset position.
 *
 * This position is an offset position iff a base position has been set for this position.
 */
public boolean
isOffsetPosition()
{
   return _avTab.getBool(_tag, OFFSET_POSITION);
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


  /**
   * Converts TT in MJD to YYYY MM DD.ddd format.
   *
   * @param  mjdDays TT in MJD (days).
   * @return TT in YYYY MM DD.ddd format.
   */
  public static String convertMJD(double mjdDays) {
    double fullDays  = Math.floor(mjdDays);
    double remainder = mjdDays - fullDays;

    remainder = Math.rint(remainder * 1E7) / 1E7;

    GregorianCalendar calendar = new GregorianCalendar(MJD_0_YEAR, MJD_0_MONTH, MJD_0_DAY);
    calendar.add(Calendar.DAY_OF_MONTH, (int)fullDays);

    return calendar.get(Calendar.YEAR) + " " +
           MONTH_NAMES[calendar.get(Calendar.MONTH)] + " " +
	   calendar.get(Calendar.DAY_OF_MONTH) + ("" + remainder).substring(1);
  }

  /**
   * Converts TT in YYYY MM DD.ddd format to MJD (days).
   *
   * @param  yyyymmdd_ddd TT in YYYY MM DD.ddd format.
   * @return TT in MJD (days).
   */
  public static double convertMJD(String yyyymmdd_ddd) {
    StringTokenizer stringTokenizer = new StringTokenizer(yyyymmdd_ddd, " ,:;/");
    GregorianCalendar calendar = new GregorianCalendar();

    int year        = 0;

    // monthIndex for January is 0, for December is 11
    int monthIndex  = 0;
    double day      = 0;

    String monthString = "";

    double resultInDays = 0.0;
   
    if(stringTokenizer.hasMoreTokens()) {
      year = Format.toInt(stringTokenizer.nextToken());
    }

    if(stringTokenizer.hasMoreTokens()) {
      monthString = stringTokenizer.nextToken().toLowerCase();

      for(int i = 0; i < MONTH_NAMES.length; i++) {
        if(monthString.startsWith(MONTH_NAMES[i].toLowerCase())) {
          monthIndex = i;
	  break;
	}
      }
    }

    if(stringTokenizer.hasMoreTokens()) {
      day = Format.toDouble(stringTokenizer.nextToken());
    }

    // Add the remaining 44 days of 1858 (November 18 - December 31 inclusive)
    resultInDays += 44;

    if(year > MJD_0_YEAR) {
      for(int i = MJD_0_YEAR + 1; i < year; i++) {
        resultInDays += 365;

        if(calendar.isLeapYear(i)) {
          resultInDays++;
        }
      }
    }

    if(year <= MJD_0_YEAR) {
      for(int i = MJD_0_YEAR; i >= year; i--) {
        resultInDays -= 365;

        if(calendar.isLeapYear(i)) {
          resultInDays--;
        }
      }
    }

    for(int i = 1; i <= monthIndex; i++) {
      switch(i) {
        case  1: resultInDays += 31; break;
        case  2: resultInDays += 28; break;
        case  3: resultInDays += 31; break;
        case  4: resultInDays += 30; break;
        case  5: resultInDays += 31; break;
        case  6: resultInDays += 30; break;
        case  7: resultInDays += 31; break;
        case  8: resultInDays += 31; break;
        case  9: resultInDays += 30; break;
        case 10: resultInDays += 31; break;
        case 11: resultInDays += 30; break;
        case 12: resultInDays += 31; break;
      }
    }

    if((calendar.isLeapYear(year)) && (monthIndex > 1)) {
      resultInDays++;
    }

    resultInDays += day;

    resultInDays = Math.rint(resultInDays * 1E7) / 1E7;

    return resultInDays;
  }
}
