// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import gemini.sp.SpItem;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * A TreeNodeWidget for items that may contain of observations.  A special
 * subclass of OtTreeNodeWidget is used in order to draw the chains between
 * observations correctly.
 */
public class OtObsContainerTreeNodeWidget extends OtTreeNodeWidget {


    public OtObsContainerTreeNodeWidget(OtTreeWidget tree) {
	super(tree);
    }
   
    public OtObsContainerTreeNodeWidget() {
    }
   
    
    /**
     * Implement copy() to make this class concrete.
     */
    public OtTreeNodeWidget copy() {
	OtObsContainerTreeNodeWidget newTNW = new OtObsContainerTreeNodeWidget((OtTreeWidget)tree);
	super.copyInto(newTNW);
	return newTNW;
    }

    /*
    private void _paintChain(Graphics g, int x, int y1, int y2) {
	int chainW = 4;
	int chainH = 6;

	x -= chainW/2;
	for (int y = y1; y<y2; y += chainH) {
	    g.drawOval(x, y, chainW, chainH);
	}
    }
    */

    /**
     * Override paint to draw the chain link between chained observations.
     *
    public void	paint(Graphics g, int x, int y, int width, int height) {
	if (!collapsed && (getWidgetCount() > 0)) {
	    int         ny = getLabelHeight();
	    FontMetrics fm = getFontMetrics(getFont());
	    int         ht = fm.getHeight() + 4;
	    int         nx = INDENT1;

	    for (int i=0; i<getWidgetCount() -1; ++i) {
		Widget w = getWidget(i);

		// If this isn't a observation tree node, continue
		if (!(w instanceof OtObsTreeNodeWidget)) {
		    continue;
		}

		// If the observation isn't chained, continue
		OtObsTreeNodeWidget tnw1 = (OtObsTreeNodeWidget) w;
		if (!tnw1.getObsChained()) {
		    continue;
		}

		ny = tnw1.getY() + ht/2;
		if (ny + 5 < y) {
		    continue;
		}
		if (ny > y + height + 5) {
		    break;
		}

		w = getWidget(i+1);

		_paintChain(g, nx, tnw1.getY() + ht/2, w.getY() + ht/2);
	    }
	}

	super.paint(g, x, y, width, height);
    }
    */

}

