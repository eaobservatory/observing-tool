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
import gemini.util.CoordSys;


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
   /** TCS XML constants. */
   private static final String TX_BASE                 = "BASE";
   private static final String TX_TYPE                 = "TYPE";
   private static final String TX_TARGET               = "target";
   private static final String TX_TARGET_NAME          = "targetName";
   private static final String TX_SPHERICAL_SYSTEM     = "spherSystem";
   private static final String TX_CONIC_SYSTEM         = "conicSystem";
   private static final String TX_NAMED_SYSTEM         = "namedSystem";
   private static final String TX_SYSTEM               = "SYSTEM";
   private static final String TX_CONIC_NAMED_TYPE     = "TYPE";
   private static final String TX_OLD_CONIC_NAMED_TYPE     = "type";
   private static final String TX_C1                   = "c1";
   private static final String TX_C2                   = "c2";
   private static final String TX_OFFSET               = "OFFSET";
   private static final String TX_DC1                  = "DC1";
   private static final String TX_DC2                  = "DC2";

   /** TCS XML constants: Orbital elements. */
   private static final String TX_EPOCH                = "epoch";
   private static final String TX_EPOCH_PERIH          = "epochPerih";
   private static final String TX_INCLINATION          = "inclination";
   private static final String TX_ANODE                = "anode";
   private static final String TX_PERIHELION           = "perihelion";
   private static final String TX_AORQ                 = "aorq";
   private static final String TX_E                    = "e";
   private static final String TX_LORM                 = "LorM";
   private static final String TX_N                    = "n";
   
   /** TCS XML constant: proper motion, x axis. */
   private static final String TX_PM1                  = "pm1";

   /** TCS XML constant: proper motion, y axis. */
   private static final String TX_PM2                  = "pm2";

   /** TCS XML constant: Radial Velocity. */
   private static final String TX_RV                   = "rv";
   
   private static final String TX_PARALAX              = "parallax";

   private static final String TX_TYPE_SCIENCE         = "SCIENCE";
   private static final String TX_J2000                = "J2000";
   private static final String TX_B1950                = "B1950";

   // CHOP MODE parameters (added by MFO, 6 August 2001)
   public static final String ATTR_CHOPPING	= "chopping";
   public static final String ATTR_CHOP_THROW	= "chopThrow";
   public static final String ATTR_CHOP_ANGLE	= "chopAngle";
   public static final String ATTR_CHOP_SYSTEM	= "chopSystem";


   protected SpTelescopePosList _posList;


   /** Needed for XML parsing. */
   private SpTelescopePos _currentPosition = null;

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


/**
 * Get chop system.
 *
 * Added by MFO (29 October 2001)
 */
public String
getChopSystem()
{
   return _avTable.get(ATTR_CHOP_SYSTEM);
}

/**
 * Set chop system.
 *
 * Added by MFO (29 October 2001)
 */
public void
setChopSystem(String chopSystem)
{
   _avTable.set(ATTR_CHOP_SYSTEM, chopSystem);
   // MFO TODO: _notifyOfGenericUpdate or equivalent has to be implemented
   // (for TelescopesPosWatcher or TelescopePosListWatcher).
   //_notifyOfGenericUpdate();
}

/**
 * This method creates JAC TCS XML from those attributes of the
 * SpAvTable that equal a target tag such as BASE, SCIENCE, GUIDE, REFERENCE.
 *
 * For the processing of all other SpAvTable attributes the processAvAttribute
 * method of the super class is used.
 */
protected void
processAvAttribute(String avAttr, String indent, StringBuffer xmlBuffer)
{
   SpTelescopePos tp = null;

   // Check whether avAttr is a telescope position.
   if(avAttr != null) {
     tp = (SpTelescopePos)getPosList().getPosition(avAttr);
   }  

   // If avAttr is not a telescope position then let the
   // super class deal with it and return.
   if(tp == null) {
     super.processAvAttribute(avAttr, indent, xmlBuffer);
     return;
   }

   xmlBuffer.append("\n  " + indent + "<" + TX_BASE + " " + TX_TYPE + "=\"" + avAttr + "\">");
   xmlBuffer.append("\n    " + indent + "<" + TX_TARGET + ">");
   xmlBuffer.append("\n      " + indent + "<" + TX_TARGET_NAME + ">" + tp.getName() + "</" + TX_TARGET_NAME + ">");

   // If the telescope position currently written to XML (tp) is an offset position then
   // targetPos is set to the base postion (OT speak) aka SCIENCE target (TCS XML speak).
   // Otherwise targetPos is just set to  telescope position currently written to XML (tp).
   SpTelescopePos targetPos = tp;
   if(tp.isOffsetPosition()) {
     targetPos = getPosList().getBasePosition();
   }

   String system = targetPos.getCoordSysAsString();

   if(system.equals(CoordSys.COORD_SYS[CoordSys.FK5])) {
     system = TX_J2000;
   }

   if(system.equals(CoordSys.COORD_SYS[CoordSys.FK4])) {
     system = TX_B1950;
   }

   // Check whether it is a spherical system. Only the spherical systems are saved using
   // there tag name as attribute name for the av table.
   switch(targetPos.getSystemType()) {
     // Conic system (orbital elements)
     case SpTelescopePos.SYSTEM_CONIC:
       xmlBuffer.append("\n      " + indent + "<" + TX_CONIC_SYSTEM + " " + TX_CONIC_NAMED_TYPE + "=\""
                                                  + targetPos.getConicOrNamedType() + "\">");
       xmlBuffer.append("\n        " + indent + "<"  + TX_EPOCH + " units=\"days\">" + targetPos.getConicSystemEpoch()
                                              + "</" + TX_EPOCH + ">");

       if(targetPos.getConicOrNamedType().equals(SpTelescopePos.CONIC_SYSTEM_TYPES[SpTelescopePos.TYPE_COMET])) {
         xmlBuffer.append("\n        " + indent + "<"  + TX_EPOCH_PERIH + " units=\"days\">" + targetPos.getConicSystemEpochPerih()
                                                + "</" + TX_EPOCH_PERIH + ">");
       }

       xmlBuffer.append("\n        " + indent + "<"  + TX_INCLINATION + " units=\"degrees\">" + targetPos.getConicSystemInclination()
                                              + "</" + TX_INCLINATION + ">");
       xmlBuffer.append("\n        " + indent + "<"  + TX_ANODE + " units=\"degrees\">" + targetPos.getConicSystemAnode()
                                              + "</" + TX_ANODE + ">");
       xmlBuffer.append("\n        " + indent + "<"  + TX_PERIHELION + " units=\"degrees\">" + targetPos.getConicSystemPerihelion()
                                              + "</" + TX_PERIHELION + ">");
       xmlBuffer.append("\n        " + indent + "<"  + TX_AORQ + " units=\"au\">" + targetPos.getConicSystemAorQ()
                                              + "</" + TX_AORQ + ">");
       xmlBuffer.append("\n        " + indent + "<"  + TX_E + ">" + targetPos.getConicSystemE()
                                              + "</" + TX_E + ">");
       xmlBuffer.append("\n        " + indent + "<"  + TX_LORM + " units=\"degrees\">" + targetPos.getConicSystemLorM()
                                              + "</" + TX_LORM + ">");
       xmlBuffer.append("\n        " + indent + "<"  + TX_N + " units=\"degrees/day\">" + targetPos.getConicSystemDailyMotion()
                                              + "</" + TX_N + ">");
       xmlBuffer.append("\n      " + indent + "</" + TX_CONIC_SYSTEM + ">");
       break;

     // Named system (planet, sun, moon etc.)
     case SpTelescopePos.SYSTEM_NAMED:
       xmlBuffer.append("\n      " + indent + "<" + TX_NAMED_SYSTEM + " " + TX_CONIC_NAMED_TYPE + "=\""
                                                  + targetPos.getConicOrNamedType() + "\"/>");
       break;

     // Default: Sperical system (HMSDEG or DEGDEG: RA/Dec, Az/El etc.)
     default:
     
       xmlBuffer.append("\n      " + indent + "<" + TX_SPHERICAL_SYSTEM + " " + TX_SYSTEM + "=\"" + system + "\">");
       xmlBuffer.append("\n        " + indent + "<" + TX_C1 + ">" + targetPos.getXaxisAsString() + "</" + TX_C1 + ">");
       xmlBuffer.append("\n        " + indent + "<" + TX_C2 + ">" + targetPos.getYaxisAsString() + "</" + TX_C2 + ">");

       double pm1 = 0.0;
       double pm2 = 0.0;

       try {
         pm1 = Double.parseDouble(targetPos.getPropMotionRA())  / 1000.0;
       }
       catch(Exception e) {
         // ignore
       }

       try {
         pm2 = Double.parseDouble(targetPos.getPropMotionDec()) / 1000.0;
       }
       catch(Exception e) {
         // ignore
       }       

       if((pm1 != 0.0) || (pm2 != 0.0)) {
         xmlBuffer.append("\n        " + indent + "<" + TX_PM1 + " units=\"arcsecs/year\">" + pm1 + "</" + TX_PM1 + ">");
         xmlBuffer.append("\n        " + indent + "<" + TX_PM2 + " units=\"arcsecs/year\">" + pm2 + "</" + TX_PM2 + ">");
       }

       try {
         double rv = Double.parseDouble(targetPos.getTrackingRadialVelocity());

         if(rv != 0.0) {
           xmlBuffer.append("\n        " + indent + "<" + TX_RV + " units=\"km/sec\">" + rv + "</rv>");
         }
       } 
       catch(Exception e) {
         // ignore
       }

       xmlBuffer.append("\n      " + indent + "</" + TX_SPHERICAL_SYSTEM + ">");
       break;
   }  
   
   xmlBuffer.append("\n    " + indent + "</" + TX_TARGET + ">");

   if(tp.isOffsetPosition()) {

     String offsetSystem = tp.getCoordSysAsString();

     if(offsetSystem.equals(CoordSys.COORD_SYS[CoordSys.FK5])) {
       offsetSystem = TX_J2000;
     }

     if(offsetSystem.equals(CoordSys.COORD_SYS[CoordSys.FK4])) {
       offsetSystem = TX_B1950;
     }

     xmlBuffer.append("\n    " + indent + "<" + TX_OFFSET + " " + TX_SYSTEM + "=\"" + offsetSystem + "\">");
     xmlBuffer.append("\n      " + indent + "<" + TX_DC1 + ">" + tp.getXaxisAsString() + "</" + TX_DC1 + ">");
     xmlBuffer.append("\n      " + indent + "<" + TX_DC2 + ">" + tp.getYaxisAsString() + "</" + TX_DC2 + ">");
     xmlBuffer.append("\n    " + indent + "</" + TX_OFFSET + ">");
   }
   
   xmlBuffer.append("\n  " + indent + "</" + TX_BASE + ">");
}

/**
 * Parses JAC TCS XML.
 */
public void
processXmlElementContent(String name, String value)
{

   // All the hard wired strings are taken from the JAC TCS XML.
   // They are left hard wired as they are unlikely to be needed outside this class.

   if(_currentPosition == null) {
      super.processXmlElementContent(name, value);
      return;
   }

   if(name.equals(TX_BASE) || name.equals(TX_TARGET)) {
      return;
   }

   if(name.equals(TX_TARGET_NAME)) {
      _currentPosition.setName(value);
      return;
   }

   if(name.equals(TX_SPHERICAL_SYSTEM)) {
      _currentPosition.setSystemType(SpTelescopePos.SYSTEM_SPHERICAL);
      return;
   }

   if(name.equals(TX_CONIC_SYSTEM)) {
      _currentPosition.setSystemType(SpTelescopePos.SYSTEM_CONIC);
      return;
   }

   if(name.equals(TX_NAMED_SYSTEM)) {
      // Ignore here and set system type in processXmlAttribute.
      // processXmlAttribute might not be called because the
      // XML element TX_NAMED_SYSTEM does not contain PCDATA.
      return;
   }

   if ( name.equals(TX_PM1) ) {
       _currentPosition.setPropMotionRA(value);
       return;
   }

   if ( name.equals(TX_PM2) ) {
       _currentPosition.setPropMotionDec(value);
       return;
   }


// ---- Sperical System --------------------------------------------------------

   // Coordinate 1 (x axis)
   if(name.equals(TX_C1)) {
      _currentPosition.setXYFromString(value, _currentPosition.getYaxisAsString());
      return;
   }

   // Coordinate 2 (y axis)
   if(name.equals(TX_C2)) {
      _currentPosition.setXYFromString(_currentPosition.getXaxisAsString(), value);
      return;
   }


// ---- Conic System (Orbital Elements) ----------------------------------------

   if(name.equals(TX_EPOCH)) {
      _currentPosition.setConicSystemEpoch(value);
      return;
   }

   if(name.equals(TX_EPOCH_PERIH)) {
      _currentPosition.setConicSystemEpochPerih(value);
      return;
   }

   if(name.equals(TX_INCLINATION)) {
      _currentPosition.setConicSystemInclination(value);
      return;
   }

   if(name.equals(TX_ANODE)) {
      _currentPosition.setConicSystemAnode(value);
      return;
   }

   if(name.equals(TX_PERIHELION)) {
      _currentPosition.setConicSystemPerihelion(value);
      return;
   }

   if(name.equals(TX_AORQ)) {
      _currentPosition.setConicSystemAorQ(value);
      return;
   }

   if(name.equals(TX_E)) {
      _currentPosition.setConicSystemE(value);
      return;
   }

   if(name.equals(TX_LORM)) {
      _currentPosition.setConicSystemLorM(value);
      return;
   }

   if(name.equals(TX_N)) {
      _currentPosition.setConicSystemDailyMotion(value);
      return;
   }


// ---- Named System -----------------------------------------------------------

   // A named system only has a name (see top of this method)
   // and a type (see method processXmlAttribute).


// ---- Offset -----------------------------------------------------------------

   if(name.equals(TX_OFFSET)) {
      _currentPosition.setOffsetPosition(true);
      return;
   }

   // Offset, coordinate 1
   if(name.equals(TX_DC1)) {
      try {
	  _currentPosition.setOffsetPosition(true);
         double dc1 = Double.parseDouble(value);
         _currentPosition.setXY(dc1, _currentPosition.getYaxis());
      }
      catch(Exception e) {
         // ignore
      }

      return;
   }

   // Offset, coordinate 2
   if(name.equals(TX_DC2)) {
      try {
	  _currentPosition.setOffsetPosition(true);
         double dc2 = Double.parseDouble(value);
         _currentPosition.setXY(_currentPosition.getXaxis(), dc2);
      }
      catch(Exception e) {
         // ignore
      }

      return;
   }


// ---- Details ----------------------------------------------------------------

   // Radial Velocity
   if(name.equals(TX_RV)) {
      _currentPosition.setTrackingRadialVelocity(value);
      return;
   }

   // Paralax
   if(name.equals(TX_PARALAX)) {
      _currentPosition.setTrackingParallax(value);
      return;
   }

   super.processXmlElementContent(name, value);
}

/**
 * Parses JAC TCS XML.
 */
public void
processXmlElementEnd(String name)
{
   if(name.equals(TX_BASE)) {
      _currentPosition = null;

      // save() just means reset() in this context.
      getAvEditFSM().save();

      return;
   }  

   super.processXmlElementEnd(name);
}

/**
 * Parses JAC TCS XML.
 */
public void
processXmlAttribute(String elementName, String attributeName, String value)
{
   // Fix so that we can translate REFERNCE to SKY for UKIRT
   if ( (value.equals("REFERENCE")) && (System.getProperty("ot.cfgdir").indexOf("ukirt") != -1) ) {
       value = "SKY";
   }

   if(elementName.equals(TX_BASE) && attributeName.equals(TX_TYPE)) {
      // TCS XML element SCIENCE is the SpTelescopePos BASE_TAG
      // TCS XML element BASE is something else.
      if(value.equals(TX_TYPE_SCIENCE)) {
         SpTelescopePos.createDefaultBasePosition(_avTable);
         _currentPosition = getPosList().getBasePosition();
      }
      else {
         _currentPosition = getPosList().createPosition(value, 0.0, 0.0);
      }

      return;
   }

   if((elementName.equals(TX_SPHERICAL_SYSTEM) || elementName.equals(TX_OFFSET)) && attributeName.equals(TX_SYSTEM)) {
      if(value.equals(TX_J2000)) {
         _currentPosition.setCoordSys(CoordSys.COORD_SYS[CoordSys.FK5]);
	 return;
      }

      if(value.equals(TX_B1950)) {
         _currentPosition.setCoordSys(CoordSys.COORD_SYS[CoordSys.FK4]);
	 return;
      }

      _currentPosition.setCoordSys(value);
      return;
   }

   // Type of conic or name system (comet, major, minor etc.)
   if((elementName.equals(TX_CONIC_SYSTEM) || elementName.equals(TX_NAMED_SYSTEM)) && 
      (attributeName.equals(TX_CONIC_NAMED_TYPE) || attributeName.equals(TX_OLD_CONIC_NAMED_TYPE))) {
        if (_currentPosition != null) {
	   _currentPosition.setConicOrNamedType(value);
	   
	   if(elementName.equals(TX_NAMED_SYSTEM)) {
	       _currentPosition.setSystemType(SpTelescopePos.SYSTEM_NAMED);
	   }
	   
	   return;
        }
   }

   if(elementName.equals(TX_EPOCH)       ||
      elementName.equals(TX_EPOCH_PERIH) ||
      elementName.equals(TX_INCLINATION) ||
      elementName.equals(TX_ANODE)       ||
      elementName.equals(TX_PERIHELION)  ||
      elementName.equals(TX_AORQ)        ||
      elementName.equals(TX_E)           ||
      elementName.equals(TX_LORM)        ||
      elementName.equals(TX_N)           ||
      elementName.equals(TX_PM1)         ||
      elementName.equals(TX_PM2)) {

      // ignore
      return;
   }

   super.processXmlAttribute(elementName, attributeName, value);
}

}

