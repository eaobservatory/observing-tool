// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpType;

import java.util.Enumeration;


//
// Enumerater for the elements of the Sky iterator.
//
class SpIterSkyEnumeration extends SpIterEnumeration
{
   private int     _curCount = 0;
   private int     _maxCount;

SpIterSkyEnumeration(SpIterSky iterSky)
{
   super(iterSky);
   _maxCount    = iterSky.getCount();
}

protected boolean
_thisHasMoreElements()
{
   return (_curCount < _maxCount);
}
protected SpIterStep
_thisFirstElement()
{
   return _thisNextElement();
}

protected SpIterStep
_thisNextElement()
{
   return new SpIterStep("sky", _curCount++, _iterComp, (SpIterValue) null);
}
   
}


/**
 * A simple "Sky" iterator.
 */
public class SpIterSky extends SpIterObserveBase
{

    public static final String ATTR_SKY_NAME = "sky";
    public static final String ATTR_SCALE_FACTOR = "scaleFactor";
    public static final String ATTR_BOX_SIZE = "randomBoxSize";
    public static final String ATTR_FOLLOW_OFFSET = "followOffset";
    public static final String ATTR_USE_RANDOM = "useRandom";

   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "sky", "Sky");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterSky());
}

/**
 * Default constructor.
 */
public SpIterSky()
{
   super(SP_TYPE);
//   super(SpType.ITERATOR_COMPONENT_SKY);
}

/**
 * Override getTitle to return the sky count.
 */
public String
getTitle()
{
   if (getTitleAttr() != null) {
      return super.getTitle();
   }

   return "Sky (" + getCount() + "X)";
}

/**
 * Get the Enumation of the iteration steps.
 */
public SpIterEnumeration
elements()
{
   return new SpIterSkyEnumeration(this);
}

public void setSky( String name ) {
    _avTable.set(ATTR_SKY_NAME, name);
}

public String getSky() {
    if ( _avTable.exists(ATTR_SKY_NAME) ) {
        return _avTable.get(ATTR_SKY_NAME);
    }
    return "SKY";
}

public void setFollowOffset( boolean flag ) {
    if ( flag == false && _avTable.exists(ATTR_SCALE_FACTOR) ) {
        _avTable.noNotifyRm(ATTR_SCALE_FACTOR);
    }
    else if ( flag == true && _avTable.exists(ATTR_BOX_SIZE) ) {
        _avTable.noNotifyRm( ATTR_BOX_SIZE );
    }
        
    _avTable.set (ATTR_FOLLOW_OFFSET, flag );
}

public boolean getFollowOffset() {
    return _avTable.getBool( ATTR_FOLLOW_OFFSET );
}

public void setRandomPattern (boolean flag) {
    if ( flag == false && _avTable.exists(ATTR_BOX_SIZE) ) {
        _avTable.noNotifyRm(ATTR_BOX_SIZE);
    }
    else if ( flag == true && _avTable.exists(ATTR_SCALE_FACTOR) ) {
        _avTable.noNotifyRm( ATTR_SCALE_FACTOR );
    }
    _avTable.set(ATTR_USE_RANDOM, flag);
}

public boolean getRandomPattern() {
    return _avTable.getBool(ATTR_USE_RANDOM);
}

public void setScaleFactor( double scaleFactor ) {
    _avTable.set( ATTR_SCALE_FACTOR, scaleFactor );
}

public void setScaleFactor( String scaleFactor ) {
    double dFactor = 1.0;
    try {
        dFactor = Double.parseDouble(scaleFactor);
    }
    catch ( NumberFormatException nfe) {}
    setScaleFactor(dFactor);
}

public double getScaleFactor() {
    return _avTable.getDouble( ATTR_SCALE_FACTOR, 1.0 );
}

public void setBoxSize( double size ) {
    _avTable.set( ATTR_BOX_SIZE, size );
}

public void setBoxSize( String size ) {
    double dSize = 5.0;
    try {
        dSize = Double.parseDouble(size);
    }
    catch (NumberFormatException nfe ) {}
    setBoxSize(dSize);
}

public double getBoxSize() {
    return _avTable.getDouble( ATTR_BOX_SIZE, 5.0);
}

public String toString() {
    StringBuffer sb = new StringBuffer( "SpIterSky=[");
    sb = sb.append("tag=" + getSky() + ", ");
    sb = sb.append("repeat=" + getCount() + ", ");
    sb = sb.append("followOffset=" + getFollowOffset() + ", ");
    sb = sb.append("useRandomPattern=" + getRandomPattern() + ", ");
    if ( getFollowOffset() ) {
        sb = sb.append("scaleFactor=" + getScaleFactor() + "]");
    }
    else if ( getRandomPattern() ) {
        sb = sb.append("boxSize=" + getBoxSize() + "]");
    }
    else {
        sb = sb.replace( sb.lastIndexOf(","), sb.lastIndexOf(","), "]");
    }
    return sb.toString();
        
}

}
