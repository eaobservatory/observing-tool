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
import javax.swing.JDesktopPane;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
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
import jsky.util.gui.DialogUtil;
import jsky.util.gui.LookAndFeelMenu;

import ot.News;
import orac.helptool.JHLauncher;

public class OT extends JFrame {

    /** Main window, when using internal frames */
    private static JDesktopPane desktop;

    /** Splash screen displayed on startup */
    private static SplashScreen _splash;


    /**
     * Create the OT application using internal frames.
     */
    public OT() {
	super("OT");
	makeLayout();

	// Clean up on exit
	addWindowListener(new BasicWindowMonitor());
    }
    
    /** Return the desktop, if using internal frames, otherwise null. */
    public static JDesktopPane getDesktop() {return desktop;}

    /**
     * Do the window layout using internal frames
     */
    protected void makeLayout() {
	// add main menubar
	setJMenuBar(makeMenuBar());

	// Add a LayeredPane object for the Internal frames
	desktop = new JDesktopPane();
	desktop.setBackground(Color.black);

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
	    new OtWindowFrame(new OtProgWindow());
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
	Component c = new OtWindowInternalFrame(new OtProgWindow((SpLibrary) SpFactory.create(SpType.LIBRARY)));
	desktop.add(c, JLayeredPane.DEFAULT_LAYER);
	desktop.moveToFront(c);
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
	    DialogUtil.error( "Could not open the standard library.");
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
        spItem = OtFileIO.fetchSp(r);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Could not open the standard library.", "Error", JOptionPane.ERROR_MESSAGE);
      } finally {
        try { if (r != null) r.close(); } catch (Exception ex) {}
      }

      if ((spItem != null) && (spItem instanceof SpLibrary)) {
	Component c = new OtWindowInternalFrame(new OtProgWindow((SpLibrary) spItem));
	desktop.add(c, JLayeredPane.DEFAULT_LAYER);
	desktop.moveToFront(c);
      }
    }

    /** 
     * Display a preferences dialog.
     */
    public void preferences() {
	// XXX allan Palette.instance().setVisible(true);
    }

    /** 
     * Exit the application with the given status.
     */
    public void exit() {
	// If the user wants to be prompted before closing when there are edited
	// programs, look for edited programs and prompt
	if (OtProps.isSaveShouldPrompt()) {
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

	// XXX allan saveProperties();

	System.exit(0);		// Must be running as a local application.
    }


    /** 
     * Fetch a science program from the database.
     */
    public void fetchProgram() {
	ProgListWindow.instance().setVisible(true);
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
  String[] args={"-helpset", System.getProperty("ot.cfgdir", "jsky/app/ot/cfg/") + "help/othelp.hs"};
  JHLauncher jhl = new JHLauncher(args);
  //System.out.println ("Help tool launched");
}


/**
 * Show the news (release notes).
 */
public void
showNews()
{
   URL url;
   try {
      url = new URL("file", "localhost", System.getProperty("ot.cfgdir", "jsky/app/ot/cfg/") + "news.txt");
   } catch (MalformedURLException ex) {
      return;
   }

   News.showNews(url);
}



// From ATC OT.java end

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

	OtCfg.init();

	if (internalFrames) {
	    new OT();
	}

	if (filename != null) {
	    OtFileIO.open(filename);
	}
	else {
	    OT.showSplashScreen();
	}
    }
}



