/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot.tpe;

import java.awt.*;
import java.net.*;

import jsky.navigator.NavigatorImageDisplayControl;
import jsky.util.gui.StatusPanel;
import jsky.image.gui.DivaMainImageDisplay;

/**
 * Extends the NavigatorImageDisplayControl class by adding Gemini specific features.
 *
 * @version $Revision$
 * @author Allan Brighton
 */
public class TpeImageDisplayControl extends NavigatorImageDisplayControl {
    
    /**
     * Construct a TpeImageDisplayControl widget.
     *
     * @param parent the top level parent frame (or internal frame) used to close the window
     * @param statusPanel panel to use to display status info
     * @param size   the size (width, height) to use for the pan and zoom windows.
     */
    public TpeImageDisplayControl(Component parent, StatusPanel statusPanel, int size) {
	super(parent, statusPanel, size);
    }

    /** 
     * Make a TpeImageDisplayControl widget with the default settings.
     *
     * @param parent The top level parent frame (or internal frame) used to close the window
     * @param statusPanel panel to use to display status info
     */
    public TpeImageDisplayControl(Component parent, StatusPanel statusPanel) {
	super(parent, statusPanel);
    }


    /** 
     * Make a TpeImageDisplayControl widget with the default settings and display the contents
     * of the image file pointed to by the URL.
     *
     * @param parent The top level parent frame (or internal frame) used to close the window
     * @param statusPanel panel to use to display status info
     * @param url The URL for the image to load
     */
    public TpeImageDisplayControl(Component parent, StatusPanel statusPanel, URL url) {
	super(parent, statusPanel, url);
    }


    /** 
     * Make a TpeImageDisplayControl widget with the default settings and display the contents
     * of the image file.
     *
     * @param parent The top level parent frame (or internal frame) used to close the window
     * @param statusPanel panel to use to display status info
     * @param filename The image file to load
     */
    public TpeImageDisplayControl(Component parent, StatusPanel statusPanel, String filename) {
	super(parent, statusPanel, filename);
    }

    /** Make and return the image display window */
    protected DivaMainImageDisplay makeImageDisplay() {
	return new TpeImageWidget(parent, statusPanel);
    }
}

