// Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ClassLoader;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Vector;
import javax.swing.JDesktopPane;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import gemini.sp.SpFactory;
import gemini.sp.SpLibrary;
import gemini.sp.SpPlan;
import gemini.sp.SpProg;
import gemini.sp.SpRootItem;
import gemini.sp.SpType;
import jsky.app.ot.tpe.TpeManager;
import jsky.app.ot.util.CloseableApp;
import jsky.catalog.skycat.SkycatConfigFile;
import jsky.util.gui.BasicWindowMonitor;
import ot.util.DialogUtil;
import jsky.util.gui.LookAndFeelMenu;

import ot.News;
import ot.OtPreferencesDialog;
import ot.DatabaseDialog;
import orac.helptool.JHLauncher;
import gemini.sp.ipc.SpServer;

public class OT extends JFrame {

    /** Vector of all non-internal OtWindowFrame's */
    private static Vector _otWindowFrames = new Vector();

    /** Main window, when using internal frames */
    private static JDesktopPane desktop;

    /** Help launcher. */
    private static JHLauncher helpLauncher = null;

    /** Splash screen displayed on startup */
    private static SplashScreen _splash;

    /** Preferences Dialog */
    private static OtPreferencesDialog _preferencesDialog = new OtPreferencesDialog();

    /** Database Access */
    private static DatabaseDialog _databaseDialog = new DatabaseDialog();


    /**
     * Default save directory.
     *
     * This system property key can be used to specify a default directory
     * to which save by default or which is used as default directory for file
     * save/open dialogs.
     *
     * The actual key/name to be used to set the system property is <b>ot.userdir</b>.
     *
     * ot.userdir can be used to specify the users working directory from which the a script
     * is called. If the script changes the directory before starting java the original
     * working directory would not be accessible from within java. the system property
     * user.dir would point to the directory from which java was started.
     *
     * @see #getOracUserDir()
     */
    public static final String PROPERTY_OT_USERDIR = "ot.userdir";

    /**
     * @see #PROPERTY_OT_USERDIR
     * @see #getOtUserDir()
     */
    private static String _otUserDir = null;


    /**
     * Create the OT application using internal frames.
     */
    public OT() {
	this(true);
    }

    public OT(boolean internalFrames) {
	super("OT");

	if(!internalFrames) {
	    return;
	}

	makeLayout();

	// Clean up on exit (Commented out by MFO, 22 August 2001, other WindowListener is used.)
	//addWindowListener(new BasicWindowMonitor());
    }
    
    /** Return the desktop, if using internal frames, otherwise null. */
    public static JDesktopPane getDesktop() {return desktop;}

    /** Return the help launcher. */
    public static JHLauncher getHelpLauncher() {return helpLauncher;}

    /** Set help launcher. */
    public static void setHelpLauncher(JHLauncher jHLauncher) {
	helpLauncher = jHLauncher;
    }

    public static DatabaseDialog getDatabaseDialog() { return _databaseDialog; }


    /**
     * Do the window layout using internal frames
     */
    protected void makeLayout() {
	// add main menubar
	setJMenuBar(makeMenuBar());

	// Add a LayeredPane object for the Internal frames
	desktop = new JDesktopPane();

        // MFO: set desktop background
	setDesktopBackground();

        //Make dragging faster:
        desktop.putClientProperty("JDesktopPane.dragMode", "outline");
	
	TpeManager.setDesktop(desktop);

	// fill the whole screen
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	int w = (int)(screen.width - 10),
	    h = (int)(screen.height - 150);
	desktop.setPreferredSize(new Dimension(w, h));
	
	//getContentPane().add(desktop, BorderLayout.CENTER);
	setContentPane(desktop);

	// include this top level window in any future look and feel changes
	LookAndFeelMenu.addWindow(this);

	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    exit();
		}
	    });

	pack();
	setVisible(true);
    }

    /** Make and return the application menubar (used when internal frames are in use) */
    protected JMenuBar makeMenuBar() {
	return new OTMenuBar(this);
    }

    /** 
     * Open a new program.
     */
    public static void newProgram() {
	if (desktop == null) {
	    addOtWindowFrame(new OtWindowFrame(new OtProgWindow()));
	}
	else {
	    Component c = new OtWindowInternalFrame(new OtProgWindow());
	    desktop.add(c, JLayeredPane.DEFAULT_LAYER);
	    desktop.moveToFront(c);
	}
    }

    /** 
     * Make a new plan
     */
    public void newPlan() {
	Component c = new OtWindowInternalFrame(new OtProgWindow((SpPlan) SpFactory.create(SpType.SCIENCE_PLAN)));
	desktop.add(c, JLayeredPane.DEFAULT_LAYER);
	desktop.moveToFront(c);
    }

    /** 
     * Make a new library
     */
    public void newLibrary() {
	// Changed by MFO, 15 February 2002
	OtWindow.create((SpLibrary) SpFactory.create(SpType.LIBRARY), new FileInfo());
	//Component c = new OtWindowInternalFrame(new OtProgWindow((SpLibrary) SpFactory.create(SpType.LIBRARY)));
	//desktop.add(c, JLayeredPane.DEFAULT_LAYER);
	//desktop.moveToFront(c);
    }

    /** 
     * Open a new science program.
     */
    public void open() {
	OtFileIO.open();
    }

    /** 
     * Open a science program from the standard library
     */
    public void openStandardLibrary() {
	URL url = ClassLoader.getSystemClassLoader().getResource("jsky/app/ot/cfg/library.xml");
	Reader r = null;
	/** This probably isn't adequate -- just using fetchXMLSp.  Should
	 * be changed when it becomes a problem.
	 */
	SpRootItem spItem = null;
	try {
	    r = new InputStreamReader(url.openStream());
	    spItem = OtFileIO.fetchSp(r);
	} 
	catch (IOException ex) {
	    DialogUtil.error(this, "Could not open the standard library.");
	    return;
	} 
	finally {
	    try { if (r != null) r.close(); } catch (Exception ex) {}
	}

	if ((spItem != null) && (spItem instanceof SpLibrary)) {
	    Component c = new OtWindowInternalFrame(new OtProgWindow((SpLibrary) spItem));
	    desktop.add(c, JLayeredPane.DEFAULT_LAYER);
	    desktop.moveToFront(c);
	}
    }

    /**
     * Open a standard library.
     *
     * Method based on OT.openLibrary from old ATC OT.
     */
    public void openLibrary(String library) {
      SpRootItem spItem = null;
      //URL url = null;
      //try {
      String resourceCfgDir = System.getProperty("ot.resource.cfgdir", "ot/cfg/");
      URL url = ClassLoader.getSystemClassLoader().getResource(resourceCfgDir + library);
	
      //}
      //catch (MalformedURLException ex) {

      if(url == null) {
        JOptionPane.showMessageDialog(this,
                                      "Could not find standard library resource " + resourceCfgDir + library,
      	                              "Error",
                                       JOptionPane.ERROR_MESSAGE);

        return;
      }

      Reader r = null;
      try {
        r = new InputStreamReader(url.openStream());
	boolean ioXml = OtFileIO.isXML();
        OtFileIO.setXML(false);
        spItem = OtFileIO.fetchSp(r);
	OtFileIO.setXML(ioXml);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Could not open the standard library.", "Error", JOptionPane.ERROR_MESSAGE);
      } finally {
        try { if (r != null) r.close(); } catch (Exception ex) {}
      }

      if ((spItem != null) && (spItem instanceof SpLibrary)) {
	// Changed by MFO, 22 August 2001
	OtWindow.create(spItem, new FileInfo());
	//Component c = new OtWindowInternalFrame(new OtProgWindow((SpLibrary) spItem));
	//desktop.add(c, JLayeredPane.DEFAULT_LAYER);
	//desktop.moveToFront(c);
      }
    }

    /** 
     * Display a preferences dialog.
     */
    public static void preferences() {
      if(desktop != null) {
        _preferencesDialog.show(desktop);
      }
      else {
        _preferencesDialog.show();
      }
    }

    /** 
     * Exit the application with the given status.
     */
    public static void exit() {
	// If the user wants to be prompted before closing when there are edited
	// programs, look for edited programs and prompt
	if (OtProps.isSaveShouldPrompt()) {
	  if(desktop != null) {
	    JInternalFrame[] ar = desktop.getAllFrames();
	    for (int i = 0; i < ar.length; i++) {
		if (! (ar[i] instanceof CloseableApp)) 
		    continue;
		CloseableApp sa = (CloseableApp) ar[i];
		if (!sa.closeApp()) {
		    return;
		}
	    }
	  }
	  else {	    
	    int current_number_of_frames = _otWindowFrames.size();
	    for (int i = 0; i < current_number_of_frames; i++) {
		if(_otWindowFrames.get(0) != null) {
		    if(!((OtWindowFrame)_otWindowFrames.get(0)).getEditor().closeApp()) {
			return;
		    }
		}
		else {
		    System.out.println("Frames " + i + " does not exist anymore.");
		}
	    }
	  }
	}

	// XXX allan saveProperties();

	System.exit(0);		// Must be running as a local application.
    }


    /** 
     * Fetch a science program from the database.
     */
    public static void fetchProgram() {
      if(System.getProperty("OMP") != null) {
        if(desktop != null) {
          _databaseDialog.show(DatabaseDialog.ACCESS_MODE_FETCH, desktop);
        }
        else {
          _databaseDialog.show(DatabaseDialog.ACCESS_MODE_FETCH);
        }
      }
      else {
	ProgListWindow.instance().setVisible(true);
      }	
    }

    /**
     * Show the splash screen.
     */
    public static void showSplashScreen() {
	// changed my M.Folger@roe.ac.uk
	// As _splash is not actually set to null when _splash is dismissed (hideSplashScreen is NOT
	// called) the if condition would prevent _splash to be shown a second time.
	//if (_splash == null) {
	    String resourceCfgDir = System.getProperty("ot.resource.cfgdir", "ot/cfg/");
            URL url = ClassLoader.getSystemClassLoader().getResource(resourceCfgDir + "welcome.txt");
	    //URL url = ClassLoader.getSystemClassLoader().getResource("jsky/app/ot/cfg/welcome.txt");
	    if (url == null) {
		System.out.println("Warning: missing resource file: jsky/app/ot/cfg/welcome.txt");
		return;
	    }
	    if (desktop != null) {
		SplashInternalFrame f = new SplashInternalFrame(desktop, url);
		_splash = f.getSplash();
		desktop.add(f, JLayeredPane.MODAL_LAYER);
	    }
	    else {
		_splash = new SplashFrame(url).getSplash();
	    }
	//}
    }

    /**
     * Hide the splash screen.
     */
    public static void hideSplashScreen() {
	if (_splash != null) {
	    _splash.dismiss();
	    _splash = null;
	}
    }


// From ATC OT.java start

public void
launchHelp()
{
   if(OT.helpLauncher != null) {
	  OT.helpLauncher.launch();
   }
   else {
      String[] args={"-helpset", System.getProperty("ot.cfgdir", "jsky/app/ot/cfg/") + "help/othelp.hs"};
      OT.helpLauncher = new JHLauncher(args);
      //System.out.println ("Help tool launched");
   }
}


/**
 * Show the news (release notes).
 */
public void
showNews()
{
   URL url;
   try {
      url = new URL("file", "localhost", System.getProperty("ot.cfgdir") + "news.txt");
   } catch (MalformedURLException ex) {
      return;
   }

   News.showNews(url);
}

    /**
     * Method sets background.
     * Sets to background image to CFG_DIR/images/background.gif if there is one.
     * The background area not covered by the image (or the whole background if there is no image)
     * is set to light blue.
     * 
     * @author Martin Folger (M.Folger@roe.ac.uk)
     */
    protected void setDesktopBackground() {
        //MFO
	// set background to light blue.
	desktop.setBackground(new Color(210, 225, 255));
        // set desktop background
        
	try {
	  ImageIcon icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/background.gif"));
          JLabel label = new JLabel(icon);
          label.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
	  desktop.add(label, new Integer(Integer.MIN_VALUE));
	}
	catch(Exception e) {
          // An exception is thrown if no background image is found in images/background.gif. Ignore.
          // Background has been set to light blue anyway.
	}
    }

    /**
     * Select server.
     *
     * Code from FreeBongo OT. MFO 23 May 2001.
     */
    protected static void selectServer() {
      // Select the SpServer to user (default to DEPLOYED)
      // Added get of system property to determine server to use (AB for ORAC)
      int serverType = SpServer.DEPLOYED;
      String server = System.getProperty ("SERVER", "DEPLOYED");
      if (server.equalsIgnoreCase("ATC")) {
        serverType = SpServer.ATC_DEVELOPMENT;
      } else if (server.equalsIgnoreCase("HILO")) {
        serverType = SpServer.HILO_DEVELOPMENT;
      } else if (server.equalsIgnoreCase("HOME")) {
        serverType = SpServer.HOME_DEVELOPMENT;
      }
      SpServer.selectServer(serverType);
    }

    public static void addOtWindowFrame(OtWindowFrame frame) {
      _otWindowFrames.add(frame);
    }

    public static void removeOtWindowFrame(OtWindowFrame frame) {
      _otWindowFrames.remove(frame);
    }

// From ATC OT.java end

    /**
     * Get default user directory.
     *
     * Returns the directory specified by the system property PROPERTY_OT_USERDIR ("ot.userdir")
     * if it is specified and exists or the users home directory otherwise.
     *
     * @see #PROPERTY_OT_USERDIR
     */
    public static String getOtUserDir() {
      if(_otUserDir != null) {
        return _otUserDir;
      }

      _otUserDir = System.getProperty(PROPERTY_OT_USERDIR);

      if(_otUserDir != null) {
        File dir = new File(_otUserDir);

        if(!dir.isDirectory()) {
          _otUserDir = System.getProperty("user.home");
        }
      }
      else {
        _otUserDir = System.getProperty("user.home");
      }

      return _otUserDir;
   }


    /** 
     * Usage: java [-Djsky.catalog.skycat.config=$SKYCAT_CONFIG] OT [-[no]internalframes] [programFile]
     * <p>
     * The <em>jsky.catalog.skycat.config</em> property defines the Skycat style catalog config file to use.
     * (The default uses the user's ~/.skycat/skycat.cfg file, or the ESO Skycat config file, if found).
     * <p>
     * If -internalframes is specified, internal frames are used. 
     * The -nointernalframes option has the opposite effect.
     * (The default is to use internal frames under Windows only).
     * <p>
     * If a program filename is specified, it is loaded on startup.
     */
    public static void main(String args[]) {
	boolean internalFrames = (File.separatorChar == '\\');
	boolean ok = true;
	String filename = null;

	for (int i = 0; i < args.length; i++) {
	    if (args[i].charAt(0) == '-') {
		String opt = args[i];
		if (opt.equals("-internalframes")) {
		    internalFrames = true;
		    //DialogUtil.setUseInternalDialogs(); // MFO, 15 August 2001
		}
		else if (opt.equals("-nointernalframes")) {
		    internalFrames = false;
		}
		else {
		    System.out.println("Unknown option: " + opt);
		    ok = false;
		    break;
		}
	    }
	    else {
		if (filename != null) 
		    ok = false;
		else
		    filename = args[i];
	    }
	}
	
	if (!ok) {
	    System.out.println("Usage: java [-Djsky.catalog.skycat.config=$SKYCAT_CONFIG] OT [-[no]internalframes]");
	    System.exit(1);
	}

	try {
	  OtCfg.init();
	}
	catch(Exception e) {
	  JOptionPane.showMessageDialog(null, e.getMessage(), "Problem with OT configuration.",
	                                JOptionPane.ERROR_MESSAGE);
	  e.printStackTrace();
	}


        selectServer();

	if (internalFrames) {
	    new OT();
	}
	else {
	    // No internal frames:
	    // Create a small frame to contain the menus that would otherwise be in the big frame containing the
	    // desktop with the internal frames. (MFO, 17 August 2001)
	    JFrame menuFrame = new JFrame("OT");
	    menuFrame.setJMenuBar(new OTMenuBar(new OT(false)));

	    try {
	      ImageIcon icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/background_small.gif"));
              JLabel label = new JLabel(icon);
              label.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
	      menuFrame.getContentPane().add(label);
	    }
	    catch(Exception e) {
              // An exception is thrown if no background image is found in images/background.gif. Ignore.
              // Background has been set to light blue anyway.
	    }

	    menuFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	    menuFrame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    exit();
		}
	    });

	    menuFrame.pack();
	    menuFrame.setVisible(true);
	}

	if (filename != null) {
	    OtFileIO.open(filename);
	}
	else {
	    OT.showSplashScreen();
	}
    }
}



