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

package jsky.app.ot.gui.image;

import java.awt.event.MouseEvent;

/**
 * A mouse event that occurred in a ViewportImageWidget.
 *
 * This structure contains fields that describe the type of mouse event,
 * and the location relative to the image being viewed by the
 * ViewportImageWidget.
 *
 * The x and y fields are stored as doubles since the image may be scaled.
 */
public class ViewportMouseEvent {
    /** Event id, same as java.awt.Event.  */
    public int id;

    /** Event source.  */
    public ViewportImageWidget source;

    /** X pos of event relative to the real image being viewed.  */
    public double xView;

    /** Y pos of event relative to the real image being viewed.  */
    public double yView;

    /** X pos of event relative to the widget.  */
    public int xWidget;

    /** Y pos of event relative to the widget.  */
    public int yWidget;

    /**
     * Permit construction before all the fields are known.
     */
    public ViewportMouseEvent() {
    }

    /**
     * Construct with all the fields.
     */
    public ViewportMouseEvent(int id, ViewportImageWidget viw, double xView,
            double yView, int xWidget, int yWidget) {
        this.id = id;
        this.source = viw;
        this.xView = xView;
        this.yView = yView;
        this.xWidget = xWidget;
        this.yWidget = yWidget;
    }

    /**
     * Returns a human-readable string describing the contents of
     * the ViewportMouseEvent.
     */
    public String toString() {
        String event = "<unknown: " + id + ">";

        switch (id) {
            case MouseEvent.MOUSE_PRESSED:
                event = "MOUSE_PRESSED";
                break;

            case MouseEvent.MOUSE_RELEASED:
                event = "MOUSE_RELEASED";
                break;

            case MouseEvent.MOUSE_MOVED:
                event = "MOUSE_MOVED";
                break;

            case MouseEvent.MOUSE_DRAGGED:
                event = "MOUSE_DRAGGED";
                break;

        }

        return "ViewportMouseEvent[id=" + event
                + ", xView=" + xView
                + ", yView=" + yView
                + ", xWidget=" + xWidget
                + ", yWidget=" + yWidget
                + "]";
    }
}
