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

import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import jsky.app.ot.gui.MultiSelTreeNodeWidget;
import gemini.sp.SpAvEditState;
import gemini.sp.SpItem;
import gemini.util.Assert;

/**
 * A MultiSelTreeNodeWidget that contains an SpItem.
 *
 * OtTreeNodeWidget not only maintains a reference to an associated SpItem,
 * but also provides support for dragging and dropping items in the
 * Science Program tree.
 */
@SuppressWarnings("serial")
public abstract class OtTreeNodeWidget extends MultiSelTreeNodeWidget
        implements Observer, OtGuiAttributes {
    /** The font for unedited items.  */
    public static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);

    /** The font for edited items.  */
    public static final Font EDITED_FONT = new Font("Dialog", Font.ITALIC, 12);

    /** The SpItem associated one-to-one with this tree node widget.  */
    protected SpItem _spItem = null;

    /** Constructor */
    public OtTreeNodeWidget(OtTreeWidget tree) {
        super(tree);
    }

    /** Constructor */
    public OtTreeNodeWidget() {
    }

    /**
     * Copy the tree node widget.
     *
     * Each subclass must implement this routine.
     * Maybe someday org.freebongo.gui.Widget.clone() will work and this won't
     * be needed anymore...
     */
    public abstract OtTreeNodeWidget copy();

    /**
     * Set the Science Program item to edit.
     *
     * This method should be called once, before the object is used.
     */
    public void setItem(SpItem spItem) {
        Assert.notFalse(_spItem == null);

        _spItem = spItem;
        _spItem.getAvEditFSM().addObserver(this);
        update(_spItem.getAvEditFSM(), null);
    }

    /**
     * Get the item represented by this tree node.
     */
    public SpItem getItem() {
        return _spItem;
    }

    /**
     * Override collapse(boolean) to store the "collapsed" state in the
     * associated SpItem.
     *
     * This information is used when a program is loaded from disk or fetched
     * from the database in order to display the tree as it was when last
     * saved.
     */
    public void setCollapsed(boolean collapsed) {
        super.setCollapsed(collapsed);

        if (_spItem != null) {
            _spItem.getTable().set(GUI_COLLAPSED, collapsed);
        }
    }

    /**
     * Has the associated SpItem been edited?
     */
    public boolean isEdited() {
        return (_spItem.getAvEditState() == SpAvEditState.EDITED);
    }

    /**
     * Implements the update method from the Observer interface in order to
     * show whether the associated SpItem has been edited.
     */
    public void update(Observable o, Object arg) {
        SpAvEditState fsm = (SpAvEditState) o;

        if (fsm.getState() == SpAvEditState.EDITED) {
            setFont(EDITED_FONT);
        } else {
            setFont(DEFAULT_FONT);
        }

        // See if there's a new title
        String title = _spItem.getTitle();
        if (getText() != title) {
            setText(title);
        }
    }
}
