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

    public static final String ATTR_FOLLOW_PATTERN   = "followPattern";
    public static final String ATTR_OFFSET_SCALE     = "offsetScaleFactor";
    public static final String ATTR_OFFSET_BOX_SIZE  = "offsetBounds";

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
	_avTable.noNotifySet(ATTR_FOLLOW_PATTERN, "true", 0);
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

    /**
     * Set whether we are following the current offset pattern for the observes
     */
    public void setFollowPattern(boolean value) {
	_avTable.set(ATTR_FOLLOW_PATTERN, value);
    }

    /**
     * Get whether "FollowPattern" is set
     */
    public boolean getFollowPattern() {
	return _avTable.getBool(ATTR_FOLLOW_PATTERN);
    }

    /**
     * Set the scaling of the offset values for the sky relative to the offset size
     * of the target.
     */
    public void setOffsetScale(String value) {
	_avTable.set(ATTR_OFFSET_SCALE, value);
    }

    /**
     * Delete the offste scale factor
     */
    public void unsetOffsetScale() {
	_avTable.rm(ATTR_OFFSET_SCALE);
    }

    /**
     * Get the offset scaling factor.
     */
    public String getOffsetScale() {
 	String rtn = "";
	if ( _avTable.get( ATTR_OFFSET_SCALE ) != null ) {
	    rtn =  _avTable.get( ATTR_OFFSET_SCALE, 0 );
	}
	return rtn;
    }

    /**
     * Set the box size for pseudo-random offset patterns
     */
    public void setOffsetBoxSize(String value) {
	_avTable.set(ATTR_OFFSET_BOX_SIZE, value);
    }

    /**
     * Delete the offste box size
     */
    public void unsetOffsetBoxSize() {
	_avTable.rm(ATTR_OFFSET_BOX_SIZE);
    }

    /**
     * Get the offset scaling factor.
     */
    public String getOffsetBoxSize() {
	String rtn = "";
	if ( _avTable.get( ATTR_OFFSET_BOX_SIZE ) != null ) {
	    rtn =  _avTable.get( ATTR_OFFSET_BOX_SIZE, 0 );
	}
	return rtn;
    }
}
