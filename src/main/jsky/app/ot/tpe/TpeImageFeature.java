/*
 * Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jsky.app.ot.tpe;

import java.awt.Font;
import java.awt.Graphics;

import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.util.BasicPropertyList;

/**
 * TpeImageFeature is a class used by the Position Editor to manipulate the
 * many potential image features that may be drawn.
 *
 * The Position Editor classes use the TpeImageFeature interface rather than
 * having to know about individual subclasses.  For instance,
 * the TpeImageWidget has methods to add and remove TpeImageFeatures.
 * When the image widget is painted, it simply loops through its list of
 * image features calling the <code>draw()</code> method on each.
 */
public abstract class TpeImageFeature {
    /**
     * Size of items that don't depend upon the scale of the image.
     *
     * This is the size of the width and height, or radius, of the item.
     */
    public static final int MARKER_SIZE = 4;

    /** Font used to draw text items.  */
    public static final Font FONT = new Font("dialog", Font.ITALIC, 10);

    /** The image widget in which to draw. */
    protected TpeImageWidget _iw;

    /** Whether the feature is being drawn. */
    protected boolean _isVisible;

    /** The feature's name. */
    protected String _name;

    /** The feature's description. */
    protected String _description;

    /**
     * Instantiate a TpeImageFeature from a fully qualified class name.
     */
    public static TpeImageFeature createFeature(String className) {
        TpeImageFeature tif = null;

        try {
            Class<?> c = Class.forName(className);
            tif = (TpeImageFeature) c.newInstance();
        } catch (Exception ex) {
            System.out.println("Could not create a TpeImageFeature from: "
                    + className);
            System.out.println(ex);
        }

        return tif;
    }

    /**
     * Create with a short name and a longer description.
     */
    public TpeImageFeature(String name, String descr) {
        _name = name;
        _description = descr;
    }

    /**
     * Reinitialize.
     *
     * Override if additional initialization is required.
     */
    public void reinit(TpeImageWidget iw, FitsImageInfo fii) {
        _iw = iw;
        _isVisible = true;
    }

    /**
     * The position angle has been updated.
     *
     * Override if this is important to the feature.
     */
    public void posAngleUpdate(FitsImageInfo fii) {
        return;
    }

    /**
     * Receive notification that the feature has been unloaded.
     *
     * Subclasses should override if interested, but call super.unloaded().
     */
    public void unloaded() {
        _iw = null;
        _isVisible = false;
    }

    /**
     * Is this feature currently visible?
     */
    public boolean isVisible() {
        return _isVisible;
    }

    /**
     * Get the feature's name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Set the feature's name.
     *
     * This allows to set a telescope specific name.
     * Added by MFO (22 January 2002).
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Get the feature's description.
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Get the property set assoicated with this feature.
     *
     * Image features may support a property set, which can be used to provide
     * a means to configure their display.  Unless overriden, this method
     * simply returns null, indicating that there are no configurable
     * properties.
     *
     * @see BasicPropertyList
     */
    public BasicPropertyList getProperties() {
        return null;
    }

    /**
     * Draw the feature.
     */
    public abstract void draw(Graphics g, FitsImageInfo fii);
}
