// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.obsComp;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.util.Format;

/**
 * Site Quality observation component.
 */
public class SpSiteQualityObsComp extends SpObsComp
{
   public static final String ATTR_TAU_BAND_ALLOCATED = "tauBandAllocated";
   public static final String ATTR_SEEING_ALLOCATED = "seeingAllocated";
   public static final String ATTR_SEEING_MIN   = "seeing.min";
   public static final String ATTR_SEEING_MAX   = "seeing.max";
   public static final String ATTR_CSO_TAU_MIN  = "csoTau.min";
   public static final String ATTR_CSO_TAU_MAX  = "csoTau.max";
   public static final String ATTR_MOON         = "moon";
   public static final String ATTR_CLOUD        = "cloud";

//    public static final int SEEING_EXCELLENT     = 0;
//    public static final int SEEING_GOOD          = 1;
//    public static final int SEEING_POOR          = 2;
//    public static final int SEEING_ANY           = 3;

   protected static final String XML_SEEING     = "seeing";
   protected static final String XML_CSO_TAU    = "csoTau";
   protected static final String XML_MOON       = "moon";
   protected static final String XML_CLOUD      = "cloud";
   protected static final String XML_MAX        = "max";
   protected static final String XML_MIN        = "min";

   private String _previousXmlElement = "";

//    public static final double [][] SEEING_RANGES = {
//       {0.0, 0.4},
//       {0.0, 0.6},
//       {0.0, 0.8}
//    };

//    public static final int CSO_TAU_VERY_DRY = 0;
//    public static final int CSO_TAU_ANY      = 1;

//    public static final double [][] CSO_TAU_RANGES = {
//       {0.0, 0.09}
//    };

   public static final int MOON_DARK = 0;
   public static final int MOON_GREY = 1;
   public static final int MOON_ANY  = 2;

   /** Number of Moon options. */
   public static final int MOON_OPTIONS_LENGTH = 3;

   public static final int CLOUD_PHOTOMETRIC = 0;
   public static final int CLOUD_THIN_CIRRUS = 1;
   public static final int CLOUD_ANY         = 2;

   /** Number of Cloud options. */
   public static final int CLOUD_OPTIONS_LENGTH = 3;

   public static final String SUBTYPE = "schedInfo";
   public static final int NO_VALUE              = 0;

   public static final SpType SP_TYPE =
   	SpType.create(SpType.OBSERVATION_COMPONENT_TYPE, SUBTYPE, "Site Quality");

   // Register the prototype.
   static {
     SpFactory.registerPrototype(new SpSiteQualityObsComp());
   }


/**
 * Default constructor.  Initialize the component type.
 */
public SpSiteQualityObsComp()
{
   // MFO: Changed because UKIRT and JCMT use different site quality components.
   super(SP_TYPE);
   _avTable.noNotifySet(ATTR_TAU_BAND_ALLOCATED, "true", 0);
   _avTable.noNotifySet(ATTR_SEEING_ALLOCATED, "true", 0);
   _avTable.noNotifySet(ATTR_MOON, "2", 0);
   _avTable.noNotifySet(ATTR_CLOUD, "2", 0);
   //super( SpType.OBSERVATION_COMPONENT_SITE_QUALITY );
}

/**
 * Set Seeing index.
 *
 * @param seeing One of {@link #SEEING_EXCELLENT}, {@link #SEEING_GOOD}, {@link #SEEING_POOR}, {@link #SEEING_ANY}.
 */
// public void
// setSeeing(int seeing)
// {
//    if ((seeing < 0) || (seeing >= (SEEING_RANGES.length) || (seeing == SEEING_ANY))) {
//       _avTable.rm(ATTR_SEEING_MIN);
//       _avTable.rm(ATTR_SEEING_MAX);
//    }
//    else {
//       _avTable.set(ATTR_SEEING_MIN, SEEING_RANGES[seeing][0]);
//       _avTable.set(ATTR_SEEING_MAX, SEEING_RANGES[seeing][1]);
//    }
// }

/**
 * Get Seeing index.
 *
 * @return One of {@link #SEEING_EXCELLENT}, {@link #SEEING_GOOD}, {@link #SEEING_POOR}, {@link #SEEING_ANY}.
 */
// public int
// getSeeing()
// {
//    if(_avTable.get(ATTR_SEEING_MAX) == null) {
//       return SEEING_ANY;
//    }
   
//    // Default -1.0 ensure that no range maximum matches and SEEING_ANY is returned.
//    double max = _avTable.getDouble(ATTR_SEEING_MAX, -1.0);

//    if(max == SEEING_RANGES[SEEING_EXCELLENT][1]) return SEEING_EXCELLENT;
//    if(max == SEEING_RANGES[SEEING_GOOD][1])      return SEEING_GOOD;
//    if(max == SEEING_RANGES[SEEING_POOR][1])      return SEEING_POOR;
   
//    return SEEING_ANY;
// }

/**
 * Get Seeing range as array of two doubles.
 */
// public double []
// getSeeingRange()
// {
//    double [] result = new double[]{
//       _avTable.getDouble(ATTR_SEEING_MIN, -1), _avTable.getDouble(ATTR_SEEING_MAX, -1)
//    };

//    if((result[0] == -1) || (result[1] == -1)) {
//       return null;
//    }
//    else {
//       return result;
//    }
// }
public void
setSeeingAllocated(boolean value)
{
   _avTable.set(ATTR_SEEING_ALLOCATED, value);
}

public boolean
seeingAllocated()
{
   return _avTable.getBool(ATTR_SEEING_ALLOCATED);
}

public void
setMinSeeing(double value)
{
   _avTable.set(ATTR_SEEING_MIN, value);
}

public void
setMinSeeing(String value)
{
   setMinSeeing(Format.toDouble(value));
}

public double
getMinSeeing()
{
   return _avTable.getDouble(ATTR_SEEING_MIN, NO_VALUE);
}

public void
setMaxSeeing(double value)
{
   _avTable.set(ATTR_SEEING_MAX, value);
}

public void
setMaxSeeing(String value)
{
   setMaxSeeing(Format.toDouble(value));
}

public double
getMaxSeeing()
{
   return _avTable.getDouble(ATTR_SEEING_MAX, NO_VALUE);
}

//--

/**
 * Set CSO Tau index.
 *
 * @param csoTau One of {@link #CSO_TAU_VERY_DRY}, {@link #CSO_TAU_ANY}.
 */
// public void
// setCsoTau(int csoTau)
// {
//    if ((csoTau < 0) || (csoTau >= (CSO_TAU_RANGES.length) || (csoTau == CSO_TAU_ANY))) {
//       _avTable.rm(ATTR_CSO_TAU_MIN);
//       _avTable.rm(ATTR_CSO_TAU_MAX);
//    }
//    else {
//       _avTable.set(ATTR_CSO_TAU_MIN, CSO_TAU_RANGES[csoTau][0]);
//       _avTable.set(ATTR_CSO_TAU_MAX, CSO_TAU_RANGES[csoTau][1]);
//    }
// }

/**
 * Get CSO Tau index.
 *
 * @return One of {@link #CSO_TAU_VERY_DRY}, {@link #CSO_TAU_ANY}.
 */
// public int
// getCsoTau()
// {
//    if(_avTable.get(ATTR_CSO_TAU_MIN) == null) {
//       return CSO_TAU_ANY;
//    }

//    // Default -1.0 ensure that no range minimum matches and CSO_TAU_ANY is returned.
//    double min = _avTable.getDouble(ATTR_SEEING_MIN, -1.0);

//    if(min == CSO_TAU_RANGES[CSO_TAU_VERY_DRY][0]) return CSO_TAU_VERY_DRY;
   
//    return CSO_TAU_ANY;
// }

/**
 * Get CSO Tau range as array of two doubles.
 */
// public double []
// getCsoTauRange()
// {
//    double [] result = new double[]{
//       _avTable.getDouble(ATTR_CSO_TAU_MIN, -1), _avTable.getDouble(ATTR_CSO_TAU_MAX, -1)
//    };

//    if((result[0] == -1) || (result[1] == -1)) {
//       return null;
//    }
//    else {
//       return result;
//    }
// }


public void
setTauBandAllocated(boolean value)
{
   _avTable.set(ATTR_TAU_BAND_ALLOCATED, value);
}

public boolean
tauBandAllocated()
{
   return _avTable.getBool(ATTR_TAU_BAND_ALLOCATED);
}

public void
setMinTau(double value)
{
   _avTable.set(ATTR_CSO_TAU_MIN, value);
}

public void
setMinTau(String value)
{
   setMinTau(Format.toDouble(value));
}

public double
getMinTau()
{
   return _avTable.getDouble(ATTR_CSO_TAU_MIN, NO_VALUE);
}

public void
setMaxTau(double value)
{
   _avTable.set(ATTR_CSO_TAU_MAX, value);
}

public void
setMaxTau(String value)
{
   setMaxTau(Format.toDouble(value));
}


public double
getMaxTau()
{
   return _avTable.getDouble(ATTR_CSO_TAU_MAX, NO_VALUE);
}


/**
 * Set Moon index.
 *
 * @param moon One of {@link #MOON_DARK}, {@link #MOON_GREY}, {@link #MOON_ANY}.
 */
public void
setMoon(int moon)
{
   if ((moon < 0) || (moon >= MOON_OPTIONS_LENGTH) || (moon == MOON_ANY)) {
      _avTable.rm(ATTR_MOON);
   }
   else {
      _avTable.set(ATTR_MOON, moon);
   }
}

/**
 * Get Moon index.
 *
 * @return One of {@link #MOON_DARK}, {@link #MOON_GREY}, {@link #MOON_ANY}.
 */
public int
getMoon()
{
   return _avTable.getInt(ATTR_MOON, MOON_ANY);
}


/**
 * Set Cloud index.
 *
 * @param cloud One of {@link #CLOUD_PHOTOMETRIC}, {@link #CLOUD_THIN_CIRRUS}, {@link #CLOUD_ANY}.
 *
 */
public void
setCloud(int cloud)
{
   if ((cloud < 0) || (cloud >= CLOUD_OPTIONS_LENGTH) || (cloud == CLOUD_ANY)) {
      _avTable.rm(ATTR_CLOUD);
   }
   else {
      _avTable.set(ATTR_CLOUD, cloud);
   }
}

/**
 * Get Cloud index.
 *
 * @return One of {@link #CLOUD_PHOTOMETRIC}, {@link #CLOUD_THIN_CIRRUS}, {@link #CLOUD_ANY}.
 */
public int
getCloud()
{
   return _avTable.getInt(ATTR_CLOUD, CLOUD_ANY);
}

protected void
processAvAttribute(String avAttr, String indent, StringBuffer xmlBuffer)
{
   if(avAttr.equals(ATTR_SEEING_ALLOCATED) && (!seeingAllocated())) {
      xmlBuffer.append("\n  "   + indent + "<"  + XML_SEEING  + ">");
      xmlBuffer.append("\n    " + indent + "<"  + XML_MIN     + ">" + getMinSeeing() + "</" + XML_MIN + ">");
      xmlBuffer.append("\n    " + indent + "<"  + XML_MAX     + ">" + getMaxSeeing() + "</" + XML_MAX + ">");
      xmlBuffer.append("\n  "   + indent + "</" + XML_SEEING  + ">");

      return;
   }

   if(avAttr.equals(ATTR_SEEING_MIN) || avAttr.equals(ATTR_SEEING_MAX) || avAttr.equals(ATTR_SEEING_ALLOCATED)) {
      return;
   }
   
   if(avAttr.equals(ATTR_TAU_BAND_ALLOCATED) && (!tauBandAllocated())) {
      xmlBuffer.append("\n  "   + indent + "<"  + XML_CSO_TAU + ">");
      xmlBuffer.append("\n    " + indent + "<"  + XML_MIN     + ">" + getMinTau() + "</" + XML_MIN + ">");
      xmlBuffer.append("\n    " + indent + "<"  + XML_MAX     + ">" + getMaxTau() + "</" + XML_MAX + ">");
      xmlBuffer.append("\n  "   + indent + "</" + XML_CSO_TAU + ">");

      return;
   }
  
   if(avAttr.equals(ATTR_CSO_TAU_MIN) || avAttr.equals(ATTR_CSO_TAU_MAX) || avAttr.equals(ATTR_TAU_BAND_ALLOCATED)) {
      return;
   }

   if(avAttr.equals(ATTR_MOON)) {
      xmlBuffer.append("\n  "   + indent + "<"  + XML_MOON    + ">" + getMoon()  + "</" + XML_MOON  + ">");

      return;
   }

   if(avAttr.equals(ATTR_CLOUD)) {
      xmlBuffer.append("\n  "   + indent + "<"  + XML_CLOUD   + ">" + getCloud() + "</" + XML_CLOUD + ">");

      return;
   }

   super.processAvAttribute(avAttr, indent, xmlBuffer);
}


public void
processXmlElementContent(String name, String value)
{

   if(name.equals(XML_SEEING)) {
      _previousXmlElement = name;
      return;
   }

   if(name.equals(XML_CSO_TAU)) {
     _previousXmlElement = name;
     return;
   }


   try {
      if(name.equals(XML_MAX)) {
         if(_previousXmlElement.equals(XML_SEEING)) {
            _avTable.noNotifySet(ATTR_SEEING_MAX, value, 0);
	    _avTable.noNotifySet(ATTR_SEEING_ALLOCATED, "false", 0);
	    return;
	 }

         if(_previousXmlElement.equals(XML_CSO_TAU)) {
            _avTable.noNotifySet(ATTR_CSO_TAU_MAX, value, 0);
	    _avTable.noNotifySet(ATTR_TAU_BAND_ALLOCATED, "false", 0);
	    return;
	 }
      }

      if(name.equals(XML_MIN)) {
         if(_previousXmlElement.equals(XML_SEEING)) {
            _avTable.noNotifySet(ATTR_SEEING_MIN, value, 0);
	    return;
	 }

         if(_previousXmlElement.equals(XML_CSO_TAU)) {
            _avTable.noNotifySet(ATTR_CSO_TAU_MIN, value, 0);
	    return;
	 }
      }

      if(name.equals(XML_MOON)) {
        _avTable.noNotifySet(ATTR_MOON, value, 0);
	return;
      }

      if(name.equals(XML_CLOUD)) {
        _avTable.noNotifySet(ATTR_CLOUD, value, 0);
	return;
      }
   }
   catch(Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Problem parsing Site Quality component (XML): " + e);
   }

   super.processXmlElementContent(name, value);
}

}

