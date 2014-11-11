/*
 * @opyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
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

package jsky.app.ot.gui;

/**
 * This interface should be supported by objects that are interrested in
 * being notified of selection, and action changes.
 *
 * Watchers register themselves with the TreeWidgetExt and are then
 * informed of any of the above events involving TreeNodeWidgetExt items
 * within the tree.  Notifications are <i>not</i> sent for ordinary
 * TreeNodeWidgets and other widgets within the tree.
 *
 * @see TreeWidgetExt
 * @see TreeNodeWidgetExt
 */
public interface TreeWidgetWatcher {
    /**
     * Receive notification that a node is selected.
     */
    public void nodeSelected(TreeWidgetExt tw, TreeNodeWidgetExt tnw);

    /**
     * Receive notification that a node has been acted upon.
     */
    public void nodeAction(TreeWidgetExt tw, TreeNodeWidgetExt tnw);
}
