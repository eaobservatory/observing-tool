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


protected void
processAvAttribute(String avAttr, String indent, StringBuffer xmlBuffer)
{
   SpTelescopePos tp = null;
   
   if(avAttr != null) {
     tp = (SpTelescopePos)_posList.getPosition(avAttr);
   }  

   if(tp == null) {
     super.processAvAttribute(avAttr, indent, xmlBuffer);
     return;
   }

   String system = _avTable.get(avAttr, SpTelescopePos.COORD_SYS_INDEX);

   if(system.equals(CoordSys.COORD_SYS[CoordSys.FK5])) {
     system = "J2000";
   }

   if(system.equals(CoordSys.COORD_SYS[CoordSys.FK4])) {
     system = "B1950";
   }

   xmlBuffer.append("\n  " + indent + "<BASE TYPE=\"" + avAttr + "\">");
   xmlBuffer.append("\n    " + indent + "<target>");

   // Check whether it is a spherical system. Only the spherical systems are saved using
   // there tag name as attribute name for the av table.
   if(_avTable.exists(avAttr)) {
     xmlBuffer.append("\n      " + indent + "<targetName>" + _avTable.get(avAttr, SpTelescopePos.NAME_INDEX) + "</targetName>");
     xmlBuffer.append("\n      " + indent + "<spherSystem SYSTEM=\"" + system + "\">");
     xmlBuffer.append("\n        " + indent + "<c1>" + _avTable.get(avAttr, SpTelescopePos.XAXIS_INDEX) + "</c1>");
     xmlBuffer.append("\n        " + indent + "<c2>" + _avTable.get(avAttr, SpTelescopePos.YAXIS_INDEX) + "</c2>");

     double pm1 = 0.0;
     double pm2 = 0.0;

     try {
       pm1 = Double.parseDouble(tp.getPropMotionRA())  / 1000.0;
     }
     catch(Exception e) {
       e.printStackTrace();
     }

     try {
       pm2 = Double.parseDouble(tp.getPropMotionDec()) / 1000.0;
     }
     catch(Exception e) {
       e.printStackTrace();
     }       

     if((pm1 != 0.0) || (pm2 != 0.0)) {
       xmlBuffer.append("\n        " + indent + "<pm1 units=\"arcsecs/year\">" + pm1 + "</pm1>");
       xmlBuffer.append("\n        " + indent + "<pm2 units=\"arcsecs/year\">" + pm2 + "</pm2>");
     }

     try {
       double rv = Double.parseDouble(tp.getTrackingRadialVelocity());

       if(rv != 0.0) {
         xmlBuffer.append("\n        " + indent + "<rv units=\"km/sec\">" + rv + "</rv>");
       }
     } 
     catch(Exception e) {
       e.printStackTrace();
     }

     xmlBuffer.append("\n      " + indent + "</spherSystem>");
   }
   
   xmlBuffer.append("\n    " + indent + "</target>");
   xmlBuffer.append("\n  " + indent + "</BASE>");
}


public void
processXmlElementContent(String name, String value)
{

   if(_currentPosition == null) {
      return;
   }

   if(name.equals("BASE") || name.equals("target")) {
      return;
   }

   if(name.equals("targetName")) {
      _currentPosition.setName(value);
      return;
   }

   if(name.equals("spherSystem")) {
      _currentPosition.setTargetSystem(SpTelescopePos.TARGET_SYSTEM_HMSDEG_DEGDEG);
      return;
   }

   if(name.equals("conicSystem")) {
      _currentPosition.setTargetSystem(SpTelescopePos.TARGET_SYSTEM_CONIC);
      return;
   }

   if(name.equals("namedSystem")) {
      _currentPosition.setTargetSystem(SpTelescopePos.TARGET_SYSTEM_NAMED);
      return;
   }

   if(name.equals("c1")) {
      _currentPosition.setXYFromString(value, _currentPosition.getYaxisAsString());
      return;
   }

   if(name.equals("c2")) {
      _currentPosition.setXYFromString(_currentPosition.getXaxisAsString(), value);
      return;
   }

   super.processXmlElementContent(name, value);

// } catch(Exception e) { e.printStackTrace(); print(); }
}

public void
processXmlElementEnd(String name)
{
   if(name.equals("BASE")) {
      _currentPosition = null;

      // save() just means reset() in this context.
      getAvEditFSM().save();

      return;
   }   

   super.processXmlElementEnd(name);
}


public void
processXmlAttribute(String elementName, String attributeName, String value)
{
   if(elementName.equals("BASE") && attributeName.equals("TYPE")) {
      // TCS XML element SCIENCE is the SpTelescopePos BASE_TAG
      // TCS XML element BASE is something else.
      if(value.equals("SCIENCE")) {
         SpTelescopePos.createDefaultBasePosition(_avTable);
         _currentPosition = getPosList().getBasePosition();
      }
      else {
         _currentPosition = getPosList().createPosition(value, 0.0, 0.0);
      }

      return;
   }

   if(elementName.equals("spherSystem") && attributeName.equals("SYSTEM")) {
      if(value.equals("J2000")) {
         _currentPosition.setCoordSys(CoordSys.COORD_SYS[CoordSys.FK5]);
	 return;
      }

      if(value.equals("B1950")) {
         _currentPosition.setCoordSys(CoordSys.COORD_SYS[CoordSys.FK4]);
	 return;
      }

      _currentPosition.setCoordSys(value);
      return;
   }

   super.processXmlAttribute(elementName, attributeName, value);
}

}

