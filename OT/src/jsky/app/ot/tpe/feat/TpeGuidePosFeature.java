// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe.feat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.fits.gui.FitsMouseEvent;
import jsky.app.ot.fits.gui.FitsPosMapEntry;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTelescopePosList;
import jsky.app.ot.OtCfg;
import jsky.app.ot.tpe.TpeCreateableFeature;
import jsky.app.ot.tpe.TpeImageWidget;
import jsky.app.ot.tpe.TpePositionMap;
import jsky.app.ot.util.BasicPropertyList;
import jsky.app.ot.util.CoordSys;
import jsky.app.ot.util.PropertyWatcher;
import jsky.app.ot.util.RADecMath;

public class TpeGuidePosFeature extends TpePositionFeature
    implements TpeCreateableFeature, PropertyWatcher {

    private static final String PROP_SHOW_TAGS = "Show Tags";
    private static BasicPropertyList _props;

    static {
	// Initialize the properties supported by the TpeGuidePosFeature.

	_props = new BasicPropertyList();
	_props.setBoolean(PROP_SHOW_TAGS, true);
    }


    /**
     * Construct the feature with its name and description. 
     */
    public TpeGuidePosFeature() {
	super("Guide", "Location(s) of the guide stars.");
    }


    public void reinit(TpeImageWidget iw, FitsImageInfo fii) {
	super.reinit(iw, fii);

	_props.addWatcher(this);
 
	// Tell the position map that the guide star choices are visible.
	TpePositionMap pm = TpePositionMap.getMap(iw);
	pm.setFindGuideStars(true);
    }
 
    public void unloaded() {
	// Tell the position map that the guide star choices are no longer visible.
	TpePositionMap pm = TpePositionMap.getExistingMap(_iw);
	if (pm != null) 
	    pm.setFindGuideStars(false);

	_props.deleteWatcher(this);

	super.unloaded();
    }

    /**
     * A property has changed.
     * 
     * @see PropertyWatcher
     */
    public void propertyChange(String propName) {
	_iw.repaint(this);
    }

    /**
     * Override getProperties to return the properties supported by this
     * feature.
     */
    public BasicPropertyList getProperties() {
	return _props;
    }

    /**
     * Turn on/off the drawing of position tags.
     */
    public void setDrawTags(boolean drawTags) {
	_props.setBoolean(PROP_SHOW_TAGS, drawTags);
    }

    /**
     * Get the "draw position tags" property.
     */
    public boolean getDrawTags() {
	return _props.getBoolean(PROP_SHOW_TAGS, true);
    }

    /**
     */
    public String[] getCreateButtonLabels() {
	return SpTelescopePos.getGuideStarTags();
    }

    /**
     */
    public boolean create(FitsMouseEvent fme, FitsImageInfo fii, String label) {
	SpTelescopePosList tpl = getSpTelescopePosList();
	if (tpl == null) return false;
 
	String   tag       = null;
	String[] guideTags = SpTelescopePos.getGuideStarTags();
	for (int i=0; i<guideTags.length; ++i) {
	    if (label.equals(guideTags[i])) {
		tag = label;
		break;
	    }
	}
	if (tag == null) return false;

	SpTelescopePos tp;
	tp = (SpTelescopePos) tpl.getPosition(tag);
 
	// Try to find a catalog star near this one.

	/* XXX allan: need to reimplement this

	   TpeCatMap      cm  = TpeCatMap.getMap(_iw);
	   TpeCatMapEntry cme = cm.locateCatMapEntry(fme.xWidget, fme.yWidget);
	   if (cme == null) {
	*/
	if (tp != null) {
	    tp.setOffsetPosition(false);
	    tp.setCoordSys(CoordSys.FK5);
	    tp.setXY(fme.ra, fme.dec);
	    String name = tp.getName();
	    if ((name != null) && !name.equals("")) {
		tp.setName("");
	    }
	} else {
	    tp = tpl.createPosition(tag, fme.ra, fme.dec);
	}
	return true;
      
	/* XXX allan
	   }
 
	   // Found a catalog star near the mouse position, so snap to it and
	   // take its name.
	   CatalogEntry ce = cme.catalogEntry;
	   double[] pos = RADecMath.string2Degrees(ce.ra, ce.dec, CoordSys.FK5);
	   if (tp != null) {
	   tp.setXY(pos[0], pos[1]);
	   } else {
	   tp = tpl.createPosition(tag, pos[0], pos[1]);
	   }
	   tp.setName(ce.id);
	   return true;
	   XXX */
    }


    /**
     */
    public boolean erase(FitsMouseEvent fme) {
	TpePositionMap pm = TpePositionMap.getMap(_iw);
	SpTelescopePosList tpl = (SpTelescopePosList) pm.getTelescopePosList();
	if (tpl == null) return false;

	int x = fme.xWidget;
	int y = fme.yWidget;

	FitsPosMapEntry pme;

	String[] guideTags = SpTelescopePos.getGuideStarTags();
	for (int i=0; i<guideTags.length; ++i) {
	    pme = pm.getPositionMapEntry( guideTags[i] );
	    if ((pme != null) && (positionIsClose( pme, x, y ))) {
		tpl.removePosition( (SpTelescopePos) pme.telescopePos );
		return true;
	    }
	}

	return false;
    }

    /**
     * @see jsky.app.ot.tpe.TpeSelectableFeature
     */
    public Object select(FitsMouseEvent fme) {
	TpePositionMap pm = TpePositionMap.getMap(_iw);

	int x = fme.xWidget;
	int y = fme.yWidget;

	FitsPosMapEntry pme;
	String[] guideTags = SpTelescopePos.getGuideStarTags();
	for (int i=0; i<guideTags.length; ++i) {
	    pme = pm.getPositionMapEntry( guideTags[i] );
	    if ((pme != null) && (positionIsClose( pme, x, y ))) {
		pme.telescopePos.select();
		return pme.telescopePos;
	    }
	}
	return null;
    }

    /**
     */
    private final void _drawGuideStar(Graphics g, Point2D.Double p, int size, String tag) {
	g.setColor(Color.green);
	g.drawRect((int)(p.x - size/2), (int)(p.y - size/2), size, size);
 
	if (getDrawTags()) {
	    // Draw the tag--should use font metrics to position the tag
	    g.setFont(FONT);
	    g.drawString(tag, (int)(p.x + size), (int)(p.y + size));
	}
    }

    /**
     */
    public void draw(Graphics g, FitsImageInfo fii) {
	TpePositionMap pm = TpePositionMap.getMap(_iw);

	Point2D.Double base = pm.getLocationFromTag( SpTelescopePos.BASE_TAG );
	if (base == null) {
	    return;
	}

	// How many pixels do 10 arcsecs take?
	int size = (int) (10 * fii.pixelsPerArcsec);
 
	Point2D.Double p;
	String[] guideTags = SpTelescopePos.getGuideStarTags();
	for (int i=0; i<guideTags.length; ++i) {
	    p = pm.getLocationFromTag( guideTags[i] );
	    if (p != null) {
		_drawGuideStar(g, p, size, guideTags[i] );
	    }
	}
    }

    /**
     */
    public boolean dragStart(FitsMouseEvent fme, FitsImageInfo fii) {
	TpePositionMap pm = TpePositionMap.getMap(_iw);
	FitsPosMapEntry pme;

	int x = fme.xWidget;
	int y = fme.yWidget;

	String[] guideTags = SpTelescopePos.getGuideStarTags();
	for (int i=0; i<guideTags.length; ++i) {
	    pme = pm.getPositionMapEntry( guideTags[i] );
	    if ((pme != null) && (positionIsClose( pme, x, y ))) {
		_dragObject = pme;

		SpTelescopePos tp = (SpTelescopePos) _dragObject.telescopePos;
		tp.setOffsetPosition(false);
		tp.setCoordSys(CoordSys.FK5);
		tp.setXY(fme.ra, fme.dec);

		return true;
	    }
	}

	return false;
    }


    /**
     */
    public void dragStop(FitsMouseEvent fme) {
	if (_dragObject == null) {
	    return;
	}

	SpTelescopePos tp = (SpTelescopePos) _dragObject.telescopePos;

	// See if we can snap to a catalog star
	boolean snappedToCatStar = false;

	/* XXX allan: need to reimplement this
	   TpeCatMap      cm  = TpeCatMap.getMap(_iw);
	   TpeCatMapEntry cme = cm.locateCatMapEntry(fme.xWidget, fme.yWidget);

	   if (cme != null) {
	   CatalogEntry ce = cme.catalogEntry;


	   double[] pos= RADecMath.string2Degrees(ce.ra, ce.dec, CoordSys.FK5);
	   Point2D.Double p = _iw.raDecToImageWidget(pos[0], pos[1]);

	   if (p != null) {
	   try {

	   fme.ra      = pos[0];
	   fme.dec     = pos[1]; 
	   fme.raStr   = ce.ra;
	   fme.decStr  = ce.dec;
	   fme.xWidget = (int)p.x;
	   fme.yWidget = (int)p.y;

	   super.dragStop(fme);
	   tp.setName(ce.id);
	   snappedToCatStar = true;

	   } catch (Exception ex) { }
	   }
	   }
	   XXX allan */
	if (!snappedToCatStar) {
	    super.dragStop(fme);
	    tp.setName("");
	}

	_dragObject = null;
    }


    /**
     * Get the feature's name.
     */
    public String getName() {
        try {
            return OtCfg.telescopeUtil.getAdditionalTarget();
	}
	catch(Exception e) {
            return super.getName();
	}
    }
}

