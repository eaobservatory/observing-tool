/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot.tpe;

import javax.swing.JDesktopPane;

import jsky.navigator.NavigatorImageDisplayInternalFrame;
import jsky.image.gui.DivaMainImageDisplay;
import jsky.image.gui.ImageDisplayControl;
import jsky.image.gui.ImageDisplayMenuBar;
import jsky.util.gui.GenericToolBar;


/**
 * Extends NavigatorImageDisplayInternalFrame to add OT/TPE specific features.
 *
 * @version $Revision$
 * @author Allan Brighton
 */
public class TpeImageDisplayInternalFrame extends NavigatorImageDisplayInternalFrame {

    /** Tool bar with OT/TPE specific commands */
    TelescopePosEditorToolBar tpeToolBar;


    /**
     * Create a top level window containing an ImageDisplayControl panel.
     *
     * @param size   the size (width, height) to use for the pan and zoom windows.
     */
    public TpeImageDisplayInternalFrame(JDesktopPane desktop, int size) {
	super(desktop, size);
    }

    /**
     * Create a top level window containing an ImageDisplayControl panel
     * with the default settings.
     */
    public TpeImageDisplayInternalFrame(JDesktopPane desktop) {
	super(desktop);
    }


    /**
     * Create a top level window containing an ImageDisplayControl panel.
     *
     * @param desktop The JDesktopPane to add the frame to.
     * @param size   the size (width, height) to use for the pan and zoom windows.
     * @param fileOrUrl The file name or URL of an image to display.
     */
    public TpeImageDisplayInternalFrame(JDesktopPane desktop, int size, String fileOrUrl) {
	super(desktop, size, fileOrUrl);
    }

    /**
     * Create a top level window containing an ImageDisplayControl panel.
     *
     * @param desktop The JDesktopPane to add the frame to.
     * @param fileOrUrl The file name or URL of an image to display.
     */
    public TpeImageDisplayInternalFrame(JDesktopPane desktop, String fileOrUrl) {
	super(desktop, fileOrUrl);
    }

    /** Make and return the menubar */
    protected ImageDisplayMenuBar makeMenuBar(DivaMainImageDisplay mainImageDisplay, GenericToolBar toolBar) {
	return new TpeImageDisplayMenuBar(mainImageDisplay, toolBar);
    }

    /**
     * Make and return the image display control frame.
     *
     * @param size the size (width, height) to use for the pan and zoom windows.
     */
    protected ImageDisplayControl makeImageDisplayControl(int size) {
	return new TpeImageDisplayControl(this, statusPanel, size);
    }

    /** Make and return the toolbar */
    protected GenericToolBar makeToolBar(DivaMainImageDisplay mainImageDisplay) {
	// add the Tpe tool bar while we are at it...
	addTpeToolBar();
	
	// Dragging can cause problems with two tool bars...
	GenericToolBar toolBar = super.makeToolBar(mainImageDisplay);
	toolBar.setFloatable(false);
	return toolBar;
    }
    
    
    /** Add a tool bar for OT/TPE specific operations. */
    protected void addTpeToolBar() {
	TpeImageWidget imageDisplay = (TpeImageWidget)imageDisplayControl.getImageDisplay();
	tpeToolBar = new TelescopePosEditorToolBar(imageDisplay);
        getContentPane().add("West", tpeToolBar);
    }

    /** Return the Tool bar with OT/TPE specific commands */
    TelescopePosEditorToolBar getTpeToolBar() {return tpeToolBar;}
}
 
