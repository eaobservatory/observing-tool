// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

import java.awt.event.KeyEvent;

/**
 * An interface supported by clients interrested in KeyPressEvents.
 */
public interface KeyPressWatcher {
   /**
    * A key was pressed.
    */
   public void keyPressed(KeyEvent evt);
}
