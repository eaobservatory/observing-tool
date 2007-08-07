// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import java.awt.Component;
import jsky.app.ot.gui.StopActionWatcher;

/**
 * A window that is displayed while an ODB access is going on from
 * an OT program window.  Presents a StopActionWidget to allow the
 * user to abort the access.
 */
final class OtODBAccessWindow extends OdbAccessGUI
{
	private StopActionWatcher _watcher;

	// parent JFrame or JInternalFrame
	private Component _parentFrame;

	OtODBAccessWindow( StopActionWatcher saw )
	{
		_watcher = saw;
		_init();
	}

	private void _init()
	{
		stopAction.addWatcher( _watcher );
	}

	void startActions()
	{
		stopAction.actionsStarted();
	}

	void stopActions()
	{
		stopAction.actionsFinished();
	}

	/** Return the parent JFrame or JInternalFrame */
	Component getParentFrame()
	{
		return _parentFrame;
	}

	/** Set the parent JFrame or JInternalFrame */
	void setParentFrame( Component f )
	{
		_parentFrame = f;
	}
}
