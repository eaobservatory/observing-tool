// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

import java.util.Vector;

/**
 * An extension of the TreeWidgetWatcher to support notification of
 * multiple tree node selection.
 *
 * @see MultiSelTreeNodeWidget
 * @see MultiSelTreeWidget
 */
public interface MultiSelTreeWidgetWatcher extends TreeWidgetWatcher
{
   /**
    * Receive notification that multiple nodes have been selected.
    */
   public void multiNodeSelect(TreeWidgetExt tw, Vector nodes);
}
