/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.jcmt.obsComp;

import gemini.sp.obsComp.SpObsComp;
import gemini.sp.SpFactory;
import gemini.sp.SpType;

/**
 * Site Quality observation component.
 */
public class SpSiteQualityObsComp extends SpObsComp
{

   public static final String ATTR_TAU_BAND_INDEX = "tauBandIndex";
   public static final String ATTR_SEEING        = "seeing";
   public static final int NO_VALUE              = 0;

   protected static final String XML_SEEING     = "seeing";
   protected static final String XML_CSO_TAU    = "csoTau";
   protected static final String XML_MAX        = "max";
   protected static final String XML_MIN        = "min";

   public static final double [][] SEEING_RANGES = {
      {0.0, 0.3},
      {0.3, 1.0},
      {1.0, 3.0},
      {3.0, Double.POSITIVE_INFINITY}
   };

   public static final double [][] CSO_TAU_RANGES = {
      {0.0,  0.05},
      {0.05, 0.08},
      {0.08, 0.12},
      {0.12, 0.2},
      {0.2,  Double.POSITIVE_INFINITY}
   };

   /** Value for "Don't care option in GUI."  */
   public static final int SEEING_ANY            = SEEING_RANGES.length;

   /** Used for XML parsing. @see #processXmlElementContent(java.lang.String,java.lang.String) */
   private String _previousXmlElement = "";
   

   public static final String SUBTYPE = "schedInfo";

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
   super(SP_TYPE);

   _avTable.noNotifySet(ATTR_TAU_BAND_INDEX, "" + (CSO_TAU_RANGES.length - 1), 0);
   _avTable.noNotifySet(ATTR_SEEING,   "" + SEEING_ANY,                  0);
}


/**
 * Set tau band index (tau band - 1).
 *
 * Tau band = tau band index + 1.
 */
public void
setTauBandIndex(int tauBandIndex)
{
   _avTable.set(ATTR_TAU_BAND_INDEX, tauBandIndex);
}

/**
 * Get the tau band index (tau band - 1).
 *
 * Tau band = tau band index + 1.
 */
public int
getTauBandIndex()
{
   return _avTable.getInt(ATTR_TAU_BAND_INDEX, NO_VALUE);
}


/**
 * Set seeing index.
 */
public void
setSeeing(int seeing)
{
   _avTable.set(ATTR_SEEING, seeing);
}

/**
 * Get seeing index.
 *
 * @return seeing state (not an index for an array of seeing options)
 */
public int
getSeeing()
{
   return _avTable.getInt(ATTR_SEEING, NO_VALUE);
}


/**
 * Derives the index of a tau range from a value.
 */
public static int
getTauBandIndexFromMax(double max) {
   for(int i = 0; i < CSO_TAU_RANGES.length; i++) {
      if((max > CSO_TAU_RANGES[i][0]) && (max <= CSO_TAU_RANGES[i][1])) {
         return i;
      }
   }

   return CSO_TAU_RANGES.length - 1;
}

/**
 * Derives the index of a seeing range from a value.
 */
public static int
getSeeingFromMax(double max) {
   for(int i = 0; i < SEEING_RANGES.length; i++) {
      if((max > SEEING_RANGES[i][0]) && (max <= SEEING_RANGES[i][1])) {
         return i;
      }
   }

   return SEEING_ANY;
}


protected void
processAvAttribute(String avAttr, String indent, StringBuffer xmlBuffer)
{
   if(avAttr.equals(ATTR_SEEING)) {
      // if getSeeing() == SEEING_ANY then the option "Don't Care" has been
      // chosen and no XML should be written for seeing.
      if(getSeeing() < SEEING_ANY) {
         xmlBuffer.append("\n  "   + indent + "<"  + XML_SEEING  + ">");
         xmlBuffer.append("\n    " + indent + "<"  + XML_MIN     + ">" + SEEING_RANGES[getSeeing()][0] + "</" + XML_MIN + ">");
         xmlBuffer.append("\n    " + indent + "<"  + XML_MAX     + ">" + SEEING_RANGES[getSeeing()][1] + "</" + XML_MAX + ">");
         xmlBuffer.append("\n  "   + indent + "</" + XML_SEEING  + ">");
      }

      return;
   }

   if(avAttr.equals(ATTR_TAU_BAND_INDEX)) {
      xmlBuffer.append("\n  "   + indent + "<"  + XML_CSO_TAU + ">");
      xmlBuffer.append("\n    " + indent + "<"  + XML_MIN     + ">" + CSO_TAU_RANGES[getTauBandIndex()][0] + "</" + XML_MIN + ">");
      xmlBuffer.append("\n    " + indent + "<"  + XML_MAX     + ">" + CSO_TAU_RANGES[getTauBandIndex()][1] + "</" + XML_MAX + ">");
      xmlBuffer.append("\n  "   + indent + "</" + XML_CSO_TAU + ">");

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
            double max = Double.POSITIVE_INFINITY;

            try {
               max = Double.parseDouble(value);
            }
            catch(Exception e) {
               // ignore
            }

            _avTable.noNotifySet(ATTR_SEEING, "" + getSeeingFromMax(max), 0);

	    return;
	 }

         if(_previousXmlElement.equals(XML_CSO_TAU)) {
            double max = Double.POSITIVE_INFINITY;

            try {
               max = Double.parseDouble(value);
            }
            catch(Exception e) {
               // ignore
            }

            _avTable.noNotifySet(ATTR_TAU_BAND_INDEX, "" + getTauBandIndexFromMax(max), 0);

	    return;
	 }
      }

      if(name.equals(XML_MIN)) {
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

