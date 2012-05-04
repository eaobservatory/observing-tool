// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot ;

import java.awt.Component ;
import javax.swing.ImageIcon ;
import javax.swing.JFrame ;
import jsky.app.ot.gui.StopActionWatcher ;
import jsky.app.ot.gui.StopActionWidget ;
import gemini.sp.SpFactory ;
import gemini.sp.SpRootItem ;
import gemini.sp.SpType ;

/**
 * The program hierarchy edit OtWindow subclass for science programs,
 * science plans and libraries.  Most of the program and plan specific
 * features implemented in this subclass have to do with the Observing
 * Database (ODB).  
 */
@SuppressWarnings( "serial" )
public final class OtProgWindow extends OtWindow implements StopActionWatcher
{
	/**
	 * Default constructor.  Creates a new empty science program.
	 */
	public OtProgWindow()
	{
		this( ( SpRootItem )SpFactory.create( SpType.SCIENCE_PROGRAM ) ) ;
		OtProps.setSaveShouldPrompt( true ) ;
	}

	/**
	 * Construct with a brand new program.
	 */
	public OtProgWindow( SpRootItem spItem )
	{
		super( spItem ) ;
		OtProps.setSaveShouldPrompt( false ) ;
	}

	/**
	 * Construct from an SpItem read from a file described by the given
	 * FileInfo.
	 */
	public OtProgWindow( SpRootItem spItem , FileInfo fileInfo )
	{
		super( spItem , fileInfo ) ;
		OtProps.setSaveShouldPrompt( false ) ;
	}

	public OtProgWindow( SpRootItem spItem , LoginInfo loginInfo )
	{
		this( spItem ) ;

		OtProps.setSaveShouldPrompt( false ) ;
		_progInfo.login = loginInfo ;
	}

	/**
	 * Do one-time only initialization of the window.
	 */
	protected void _init( SpRootItem spItem , FileInfo fileInfo )
	{
		super._init( spItem , fileInfo ) ;

		_progInfo.isPlan = false ;
		SpType type = spItem.type() ;
		if( type.equals( SpType.SCIENCE_PLAN ) )
			_progInfo.isPlan = true ;
		else if( type.equals( SpType.LIBRARY ) )
			libFolderAction.setEnabled( true ) ;
	}

	/**
	 * Set the top level parent frame for this window.
	 * (Override here to set the frame icon).
	 */
	public void setParentFrame(JFrame p) {
		super.setParentFrame(p);

		if (_curItem != null) {
			SpType type = _curItem.type();
			if (type.equals(SpType.SCIENCE_PROGRAM))
				p.setIconImage(new ImageIcon(getClass().getResource("images/ngc104.gif")).getImage());
			else if (type.equals(SpType.SCIENCE_PLAN))
				p.setIconImage(new ImageIcon(getClass().getResource("images/comet.gif")).getImage());
			else if (type.equals(SpType.LIBRARY))
				p.setIconImage(new ImageIcon(getClass().getResource("images/libIcon.gif")).getImage());
		}
	}

	/** Return true if the SP type is LIBRARY */
	public boolean isLibrary()
	{
		return _curItem.type().equals( SpType.LIBRARY ) ;
	}

	/** Add a library folder to the tree. */
	public void addLibFolder()
	{
		_tw.addItem( SpFactory.create( SpType.LIBRARY_FOLDER ) ) ;
	}

	/** Return true if online */
	public boolean isOnline()
	{
		return _progInfo.online ;
	}

	/**
	 * Implementation of the StopActionWatcher interface.
	 */
	public synchronized void stopAction( StopActionWidget saw ){}

	/** 
	 * Fetch a science program from an online database.
	 */
	public void fetchFromOnlineDatabase()
	{
		Thread t = new Thread( new Runnable()
		{
			public void run()
			{
				OT.getDatabaseDialog().fetchProgram() ;
			}
		} ) ;
		t.start() ;
	}

	/** 
	 * Store the current science program to an online database.
	 */
	public void storeToOnlineDatabase()
	{
		Thread t = new Thread( new Runnable()
		{
			public void run()
			{
				OT.getDatabaseDialog().storeProgram( getItem() ) ;
			}
		} ) ;
		t.start() ;
	}
}
