/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot.tpe;

import javax.swing.*;

import jsky.navigator.NavigatorImageDisplayMenuBar;
import jsky.image.gui.DivaMainImageDisplay;
import jsky.util.gui.GenericToolBar;

/**
 * Extends the image display menubar by adding Gemini position editor features.
 *
 * @version $Revision$
 * @author Allan Brighton
 */
public class TpeImageDisplayMenuBar extends NavigatorImageDisplayMenuBar {

    /**
     * Create the menubar for the given main image display.
     *
     * @param imageDisplay the target image display
     * @param toolBar the toolbar associated with this menubar (shares some actions)
     */
    public TpeImageDisplayMenuBar(DivaMainImageDisplay imageDisplay, 
				     GenericToolBar toolBar) {
	super(imageDisplay, toolBar);
    }
}
