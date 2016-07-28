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

package jsky.app.ot;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import jsky.app.ot.gui.StopActionWatcher;
import jsky.app.ot.gui.StopActionWidget;
import gemini.sp.SpFactory;
import gemini.sp.SpRootItem;
import gemini.sp.SpType;

/**
 * The program hierarchy edit OtWindow subclass for science programs,
 * science plans and libraries.
 *
 * Most of the program and plan specific features implemented in this subclass
 * have to do with the Observing Database (ODB).
 */
@SuppressWarnings("serial")
public final class OtProgWindow extends OtWindow implements StopActionWatcher {
    /**
     * Default constructor.
     *
     * Creates a new empty science program.
     */
    public OtProgWindow() {
        this((SpRootItem) SpFactory.create(SpType.SCIENCE_PROGRAM));
        OtProps.setSaveShouldPrompt(true);
    }

    /**
     * Construct with a brand new program.
     */
    public OtProgWindow(SpRootItem spItem) {
        super(spItem);
        OtProps.setSaveShouldPrompt(false);
    }

    /**
     * Construct from an SpItem read from a file described by the given
     * FileInfo.
     */
    public OtProgWindow(SpRootItem spItem, FileInfo fileInfo) {
        super(spItem, fileInfo);
        OtProps.setSaveShouldPrompt(false);
    }

    public OtProgWindow(SpRootItem spItem, LoginInfo loginInfo) {
        this(spItem);

        OtProps.setSaveShouldPrompt(false);
        _progInfo.login = loginInfo;
    }

    /**
     * Do one-time only initialization of the window.
     */
    protected void _init(SpRootItem spItem, FileInfo fileInfo) {
        super._init(spItem, fileInfo);

        _progInfo.isPlan = false;
        SpType type = spItem.type();

        if (type.equals(SpType.SCIENCE_PLAN)) {
            _progInfo.isPlan = true;
        } else if (type.equals(SpType.LIBRARY)) {
            libFolderAction.setEnabled(true);
        }
    }

    /**
     * Set the top level parent frame for this window.
     *
     * (Override here to set the frame icon).
     */
    public void setParentFrame(JFrame p) {
        super.setParentFrame(p);

        if (_curItem != null) {
            SpType type = _curItem.type();

            if (type.equals(SpType.SCIENCE_PROGRAM)) {
                p.setIconImage(new ImageIcon(getClass().getResource(
                        "images/ngc104.gif")).getImage());

            } else if (type.equals(SpType.SCIENCE_PLAN)) {
                p.setIconImage(new ImageIcon(getClass().getResource(
                        "images/comet.gif")).getImage());

            } else if (type.equals(SpType.LIBRARY)) {
                p.setIconImage(new ImageIcon(getClass().getResource(
                        "images/libIcon.gif")).getImage());
            }
        }
    }

    /**
     * Return true if the SP type is LIBRARY.
     */
    public boolean isLibrary() {
        return _curItem.type().equals(SpType.LIBRARY);
    }

    /**
     * Add a library folder to the tree.
     */
    public void addLibFolder() {
        _tw.addItem(SpFactory.create(SpType.LIBRARY_FOLDER));
    }

    /**
     * Return true if online.
     */
    public boolean isOnline() {
        return _progInfo.online;
    }

    /**
     * Implementation of the StopActionWatcher interface.
     */
    public synchronized void stopAction(StopActionWidget saw) {
    }

    /**
     * Store the current science program to an online database.
     */
    public void storeToOnlineDatabase() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                OT.getDatabaseDialog().storeProgram(getItem());
            }
        });

        t.start();
    }
}
