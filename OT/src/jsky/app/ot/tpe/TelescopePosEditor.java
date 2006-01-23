/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot.tpe;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.lang.ClassLoader;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import jsky.app.jskycat.JSkyCat;
import jsky.app.ot.fits.gui.FitsMouseEvent;
import jsky.app.ot.gui.image.ViewportImageWidget;
import jsky.app.ot.gui.image.ViewportMouseEvent;
import jsky.app.ot.gui.image.ViewportMouseObserver;
import gemini.util.CoordSys;
import gemini.sp.SpHierarchyChangeObserver;
import gemini.sp.SpItem;
import gemini.sp.SpObsContextItem;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTelescopePosList;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.obsComp.SpSurveyObsComp;
import jsky.app.ot.OtCfg;
import jsky.app.ot.tpe.feat.TpeCatalogFeature;
import jsky.app.ot.util.BasicPropertyList;
import jsky.app.ot.util.DDMMSS;
import jsky.app.ot.util.HHMMSS;
import jsky.catalog.skycat.SkycatConfigFile;
import jsky.navigator.NavigatorImageDisplayFrame;
import jsky.navigator.NavigatorImageDisplayInternalFrame;
import jsky.util.Preferences;
import jsky.util.TclUtil;
import jsky.util.gui.BasicWindowMonitor;
import jsky.util.gui.DialogUtil;
import jsky.coords.wcscon;


/**
 * Implements a telescope position editor for the Gemini telescope
 * and instruments. This class displays an image and/or graphics overlays for guide
 * stars and instruments and allows the user to edit the positions to use
 * for an observation.
 *
 * @version $Revision$ $Date$
 * @author Allan Brighton
 */
public class TelescopePosEditor extends JSkyCat 
    implements ViewportMouseObserver, SpHierarchyChangeObserver {

    // Lists of fully qualified image feature class names.  These are
    // instantiated when a new TelescopePosEditor is created.
    private static Vector _targetListFeatureClasses = new Vector();
    private static Vector _instrumentFeatureClasses = new Vector();
    private static Vector _otherFeatureClasses      = new Vector();

    private static final String BLANK_IMAGE = "Blank Image";
    public static final String ANY_SERVER = "any";
    public static final String FILE_IMAGE = "file";

    private TpeImageFeature BASE_FEATURE;
    private TpeImageFeature CATALOG_FEATURE;

    private Vector _targetListFeatures = new Vector();
    private Vector _instrumentFeatures = new Vector();


    private SpTelescopePosList	_posList;
    private SpItem		_spItem;
    private SpObsContextItem	_spContext;

    //private Hashtable		_imgProps;

    // TpeWatchers
    private Vector               _watchers;

    // The imaeg widget
    private TpeImageWidget	_iw;

    private boolean              _targetWidgetsOff = false;
    private boolean		_instWidgetsOff   = false;

    // Tool button helper
    private TpeEditorTools       _editorTools;
    private TpeFeatureManager    _featureMan;

    private Hashtable _featureVisibleState = new Hashtable();

    // image frame toolbar (on the side with toggle buttons)
    private TelescopePosEditorToolBar _tpeToolBar;

    // image frame menubar
    private TpeImageDisplayMenuBar _tpeMenuBar;

    /** Used in {@link #loadImage(java.lang.String)}. */
    private Point2D.Double _convertedPosition = new Point2D.Double();

    private static int _widthIncrement = 0;

    static {
	// Register the standard target list features.

	registerTargetListFeature("jsky.app.ot.tpe.feat.TpeBasePosFeature");
	registerTargetListFeature("jsky.app.ot.tpe.feat.TpeGuidePosFeature");
	// MFO, 23 January 2002: User targets disabled.
	//registerTargetListFeature("jsky.app.ot.tpe.feat.TpeTargetPosFeature");
	registerInstrumentFeature("jsky.app.ot.tpe.feat.TpeSciAreaFeature");
	registerFeature("jsky.app.ot.tpe.feat.TpeCatalogFeature");

	// Set the default catalog config file URL (override with -Djsky.catalog.skycat.config=...)
	ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	URL url = classLoader.getResource("jsky/app/ot/cfg/skycat.cfg");
	SkycatConfigFile.setConfigFile(url);
    }

    /**
     * Create the application class and display the contents of the
     * given image file or URL, if not null.
     *
     * @param imageFileOrUrl an image file or URL to display 
     * @param internalFrames if true, use internal frames
     * @param showNavigator if true, display the catalog navigator on startup
     */
    public TelescopePosEditor(String imageFileOrUrl, boolean internalFrames, boolean showNavigator) {
	super(imageFileOrUrl, internalFrames, showNavigator);

	// get the TPE toolbar handle
	Component parent = _iw.getParentFrame();
	if (parent instanceof  TpeImageDisplayFrame) {
	    _tpeToolBar = ((TpeImageDisplayFrame)parent).getTpeToolBar();
	    _tpeMenuBar = (TpeImageDisplayMenuBar)((TpeImageDisplayFrame)parent).getJMenuBar();
	}
	else if (parent instanceof  TpeImageDisplayInternalFrame) {
	    _tpeToolBar = ((TpeImageDisplayInternalFrame)parent).getTpeToolBar();
	    _tpeMenuBar = (TpeImageDisplayMenuBar)((TpeImageDisplayInternalFrame)parent).getJMenuBar();
	}
	else throw new RuntimeException("internal error");

	_editorTools = new TpeEditorTools(this);

	//
	// Create and add the standard TpeImageFeatures
	//
	_iw.addMouseObserver(this);
	_featureMan = new TpeFeatureManager(this, _iw);



	// Target list features
	_targetListFeatures = _createFeatures(_targetListFeatureClasses);
	for (int i=0; i<_targetListFeatures.size(); ++i) {
	    _addFeature((TpeImageFeature) _targetListFeatures.elementAt(i));
	}
	
	if(_targetListFeatures.firstElement() != null) {
	    BASE_FEATURE = (TpeImageFeature) _targetListFeatures.firstElement();
	}
	else {
	    DialogUtil.error("No base/science feature found.");
	}

	// Instrument features
	_instrumentFeatures = _createFeatures(_instrumentFeatureClasses);
	for (int i=0; i<_instrumentFeatures.size(); ++i) {
	    _addFeature((TpeImageFeature) _instrumentFeatures.elementAt(i));
	}

	// Other features
	Vector v = _createFeatures(_otherFeatureClasses);
	for (int i=0; i<v.size(); ++i) {
	    TpeImageFeature tif = (TpeImageFeature) v.elementAt(i);
	    _addFeature(tif);
	    if (tif instanceof TpeCatalogFeature) {
		CATALOG_FEATURE = tif;
	    }
	}

	// Select the "Browse" tool.
	_editorTools.gotoBrowseMode();

	_widthIncrement = _tpeToolBar.getPreferredSize().width - _tpeToolBar.getWidth();
    }

    /**
     * Method used to prevent the bug which causes the TelescopePosEditor window
     * to shrink every time the OT is started.
     *
     * This bug is more severe if longer TpeImageFeature names are used
     * such as "WFCAM Autoguider" because that results in larger buttons
     * in the TPE tool bar which cause the problem.
     */
	public static void adjustWidthPreference() 
	{
		try 
		{
	    		// Get hold of dimensions stored in preferences.
	    		String [] imageDisplayControlDimension = TclUtil.splitList( Preferences.get( TpeImageDisplayControl.class.getName() + ".size" ) ) ;

	    		if( imageDisplayControlDimension != null )
	    		{
	    			String widthAsString = imageDisplayControlDimension[ 0 ] ;
	    			if( widthAsString != null  )
	    			{
	    				int width = Integer.parseInt( widthAsString ) ;
	    				// Increment width.
	    				width += _widthIncrement ;
	    				// Set to new value.
	    				imageDisplayControlDimension[ 0 ] = "" + width ;
	    				Preferences.set( TpeImageDisplayControl.class.getName() + ".size" , TclUtil.makeList( imageDisplayControlDimension ) ) ;
	    			}	
	    		}
		}
		catch( Exception e ) 
		{
	    		e.printStackTrace() ;
		}
    	}

    /**
     * Create the application class and display the contents of the
     * given image file or URL, if not null.
     *
     * @param imageFileOrUrl an image file or URL to display 
     */
    public TelescopePosEditor(String imageFileOrUrl) {
	this(imageFileOrUrl, false, false);
    }

    /**
     * Create the application class and display the contents of the
     * SpItem.
     *
     * @see gemini.sp.SpItem
     *
     * @param spItem an SpItem containing coordinates 
     */
    public TelescopePosEditor(SpItem spItem) {
	this(null, false, false);

	reset(_getPosList(spItem), spItem);
	
	if (_posList != null) {
	    // Make the "Base" position feature (which should be the first
	    // target list dependent feature) visible.
	    TpeImageFeature tif =(TpeImageFeature) _targetListFeatures.firstElement();
	    _featureMan.setVisible( tif, true );
	}
    }


    /** 
     * Make and return an internal frame for displaying the given image (may be null). 
     *
     * @param desktop used to display the internal frame
     * @param imageFileOrUrl specifies the iamge file or URL to display
     */
    protected NavigatorImageDisplayInternalFrame makeNavigatorImageDisplayInternalFrame(JDesktopPane desktop, String imageFileOrUrl) {
	TpeImageDisplayInternalFrame frame = new TpeImageDisplayInternalFrame(desktop, imageFileOrUrl);
	_iw = (TpeImageWidget)frame.getImageDisplayControl().getImageDisplay();
	return frame;
    }

    /** 
     * Make and return a frame for displaying the given image (may be null). 
     *
     * @param imageFileOrUrl specifies the iamge file or URL to display
     */
    protected NavigatorImageDisplayFrame makeNavigatorImageDisplayFrame(String imageFileOrUrl) {
	TpeImageDisplayFrame frame = new TpeImageDisplayFrame(imageFileOrUrl);
	_iw = (TpeImageWidget)frame.getImageDisplayControl().getImageDisplay();
	return frame;
    }

    /** Return the telescope position editor toolbar (the one with the toggle buttons) */
    public TelescopePosEditorToolBar getTpeToolBar() {
	return _tpeToolBar;
    }

    /** Set the current cursor (was a Bongo Widget method) */
    public void setCurrentMouseCursor(java.awt.Cursor currentMouseCursor) {
	setCursor(currentMouseCursor);
	_iw.setCursor(currentMouseCursor);
    }

    //
    // Disable/enable the target widgets (to handle the absence of the
    // target list component in the current item's scope.)
    // Update the widgets for the case in which there is no target component
    // in the current item's scope.
    //
    private void _setTargetWidgetsDisabled(boolean disabled) {
	if (_targetWidgetsOff == disabled) return;
	_targetWidgetsOff = disabled;

	_editorTools.setCreateToolsDisabled(_targetListFeatures, disabled);
	_featureMan.setDisabled(_targetListFeatures, disabled);

	/* XXX allan
	_menus.setEditMenuDisabled(disabled);
	_menus.setImageMenuDisabled(disabled);
	_menus.setCatalogMenuDisabled(disabled);
	XXX allan */
    }

    //
    // Update the widgets for the case in which there is no instrument component
    // in the current item's scope.
    //
    private void _setInstWidgetsDisabled(boolean disabled)  {
	if (_instWidgetsOff == disabled) return;
	_instWidgetsOff = disabled;
	_featureMan.setDisabled(_instrumentFeatures, disabled);
    }

    /**
     * Register a TargetList dependent feature.  These features will be
     * created when a TelescopePositionEditor is created and are marked
     * as being dependent on the existence of a target list component.
     * When a target list component doesn't exist in the current scope,
     * the feature and any of its creational tools will be disabled.
     */
    public static void registerTargetListFeature(String className) {
	if (!_targetListFeatureClasses.contains(className)) {
	    _targetListFeatureClasses.addElement(className);
	}
    }

    /**
     * Register an instrument dependent feature.  These features will be
     * created when a TelescopePositionEditor is created and are marked
     * as being dependent on the existence of an instrument component.
     * When an instrument component doesn't exist in the current scope,
     * the feature and any of its creational tools will be disabled.
     */
    public static void registerInstrumentFeature(String className) {
	if (!_instrumentFeatureClasses.contains(className)) {
	    _instrumentFeatureClasses.addElement(className);
	}
    }

    /**
     * Register an independent feature.  These features will be
     * created when a TelescopePositionEditor is created.
     */
    public static void registerFeature(String className) {
	if (!_otherFeatureClasses.contains(className)) {
	    _otherFeatureClasses.addElement(className);
	}
    }

    //
    // Instantiate all the TpeImageFeatures indicated in the given Vector.
    //
    private static Vector _createFeatures(Vector classNames) {
	Vector v = new Vector();
	for (int i=0; i<classNames.size(); ++i) {
	    String className = (String) classNames.elementAt(i);
	    v.addElement( TpeImageFeature.createFeature(className) );
	}
	return v;
    }

    /**
     * Add a feature to the set of image features available for display.
     * If the feature has never been added or was visible when last removed,
     * it will be made visible again.
     */
    public void	addFeature(TpeImageFeature tif) {
	// See whether the feature was visible when last removed.
	Boolean b = (Boolean) _featureVisibleState.get(tif.getClass().getName());
	if (b == null) {
	    b = Boolean.TRUE;
	    _featureVisibleState.put(tif.getClass().getName(), b);
	}

	addFeature(tif, b.booleanValue());
    }

    /**
     * Add a feature to the set of image features available for display.
     * Make the feature visible if the boolean argument is true.
     */
    public void	addFeature(TpeImageFeature tif, boolean visible) {
	if (_addFeature(tif)) {
	    // Make sure the current visibility state of the feature matches
	    // the desired state.
	    if (visible != tif.isVisible()) {
		_featureMan.setVisible(tif, visible);
	    }
	}
    }

    //
    // Do the work of adding a feature without worrying about whether
    // it should be visible or not.
    // 
    private boolean _addFeature(TpeImageFeature tif) {
	if (_featureMan.isFeaturePresent(tif)) {
	    return false;   // already being displayed
	}

	_featureMan.addFeature(tif);

	// If this feature has properties, show them in the "View" menu.
	final BasicPropertyList pl = tif.getProperties();
	if (pl != null) {
	    /* XXX allan
	    PopupMenuExt           pme = _menus.getMenuBar().getPopupMenu("View");
	    final MenuButtonWidget mbw = _menus.getMenuBar().getMenuButton("View");
	    final String         title = tif.getName() + " Display";
	    pme.add(_getPropertyEditorMenuLabel(tif), new MenuCommand() {
		    public void execute(String s) {
			if (_propEditor == null) {
			    // Figure out where it should display
			    MenuBar mb = _menus.getMenuBar();
			    int x = TelescopePosEditor.this.getX() + mbw.getX() + mb.getX();
			    int y = TelescopePosEditor.this.getX() + mb.getY() + mb.getHeight();

			    _propEditor = new PropertyEditorWindow();
			    _propEditor.setLocation(x, y);
			    addPopup(_propEditor, POPUP_ANYWHERE);
			}
			//System.out.println("TelescopePosEditor: prop edit: title = " + title + ", list = " + list);
			_propEditor.setTitle(title);
			_propEditor.setPropertyList(pl);
			showPopup(_propEditor);
		    }
		});
		XXX allan */
	}

	_editorTools.addCreateTools(tif);
	return true;
    }


    /**
     * Add a TpeWatcher.
     */
    public synchronized void addWatcher(TpeWatcher tw) {
	if (_watchers == null) {
	    _watchers = new Vector();
	} else if (_watchers.contains(tw)) {
	    return;
	}
	_watchers.addElement(tw);
    }
 
    /**
     * Remove a TpeWatcher.
     */
    public synchronized void deleteWatcher(TpeWatcher tw) {
	if (_watchers == null) {
	    return;
	}
	_watchers.removeElement(tw);
    }

    /**
     * Remove all TpeWatchers.
     */
    public synchronized void deleteWatchers() {
	if (_watchers == null) {
	    return;
	}
	_watchers.removeAllElements();
    }

    /**
     * Copy the watchers list.
     */
    private final synchronized Vector _getWatchers() {
	if (_watchers == null) {
	    return null;
	}
 
	return (Vector) _watchers.clone();
    }

    /**
     * Notify that the tpe has closed
     */
    private void _notifyOfClose() {
	Vector v = _getWatchers();
	if (v == null) return;
	for (int i=0; i<v.size(); ++i) {
	    TpeWatcher tw = (TpeWatcher) v.elementAt(i);
	    tw.tpeClosed(this);
	}
	deleteWatchers();
    }


    /**
     * Stop user interactions, except for the stop button.
     */
    public void freeze() {
	/* XXX allan
	setBusy();
	repaint();
	XXX allan */
    }

    /**
     * Allow all user interactions.
     */
    public void thaw() {
	/* XXX allan
	setIdle();

	_editorTools.resetCursor();
	XXX allan */
    }

    /**
     * Update the position list's base position to coincide with the location
     * of the center of the current image.
     */
    public void setBasePositionFromImage()  {
	if (_posList == null) {
	    DialogUtil.error("There is no target list!");
	    return;
	}

	SpTelescopePos tp = _posList.getBasePosition();
	if (tp == null) {
	    DialogUtil.error("There is no base position!");
	    return;
	}

	double[] raDec = getImageCenterLocation();
	tp.setXY(raDec[0], raDec[1]);
    }


    /**
     * Display a file dialog box, take the user's file choice and load/display it
     * as a FITS image.
     */
    public void loadFileImage()  {
	open();
    }

    /**
     * Load an image from the given server, using the base position as the
     * image center.
     */
    public void	loadImage(String server)  {
	double ra  = 0.0;
	double dec = 0.0;
	if (_posList != null) {
	    // Get the RA and Dec from the pos list.
	    SpTelescopePos tp = _posList.getBasePosition();

	    _convertedPosition.x = tp.getXaxis();
	    _convertedPosition.y = tp.getYaxis();

	    switch(tp.getCoordSys()) {
	      case CoordSys.FK4:
	        wcscon.fk425(_convertedPosition);
	        break;
	      case CoordSys.GAL:
	        wcscon.gal2fk5(_convertedPosition);
	        break;
	    }

	    ra  = _convertedPosition.x;
	    dec = _convertedPosition.y;
	}

	if (server.equals(ANY_SERVER)) {
	    _iw.loadCachedImage(ra, dec);
	} 
	else if (server.equals(BLANK_IMAGE)) {
	    _iw.blankImage(ra, dec);
	} 
	else if (server.equals(FILE_IMAGE)) {
	    loadFileImage();
	} else {
	    // XXX allan
	    throw new RuntimeException("XXX fetch image from image server: " + server + ": not implemented");
	}
    }

    /**
     * Show the cut level editor.
     */
    public void	showCutLevelEditor()  {
	_iw.editCutLevels();
    }


    /**
     * Write the current image to a file of the user's choice.
     */
    public void saveCurrentImage() {
	throw new RuntimeException("not implemented");
	/* XXX allan
	// Get the RA and Dec (in degrees) of the center of the image
	double[] raDec = getImageCenterLocation();
	if (raDec == null) return;

	// Get the cached image at that location
	FitsImage fimg = _imgManager.getCachedFitsImage(raDec[0], raDec[1]);
	if (fimg == null) {
	    DialogUtil.error("There is no image to save.");
	    return;
	}

	// Select the file to save to
	FileDialog fd = FileBox.getFileDialog(FileDialog.SAVE);
	File        f = FileBox.chooseFile(fd);
	if (f == null) return;
	String      dir = f.getParent();
	String filename = f.getName();

	// Make sure we aren't going to over-write an existing file.
	if (f.exists()) {
	    if (!f.canWrite()) {
		DialogUtil.error("Can't write to `" + dir + filename + "'");
		return;
	    }

	    if (!f.isFile()) {
		DialogUtil.error("`" + dir + filename + "' isn't an ordinary file.");
		return;
	    }

	    String m = "'" + dir + filename + "' already exists.  Replace it?";
	    AlertBox ab = new AlertBox(m, "Replace", "Cancel");
	    if (ab.getChoice().equals("Cancel")) {
		return;
	    }
	} 

	FitsOutput.writeImage(fimg, f);
	XXX allan */
    }

    /**
     * Print the image.
     */
    public void	printCurrentImage()  {
	throw new RuntimeException("not implemented");
	/* XXX allan 
	java.awt.PrintJob pj;
	pj = java.awt.Toolkit.getDefaultToolkit().getPrintJob((java.awt.Frame)getTopLevelAncestor(), "OT Image", null);

	if (pj == null) {
	    System.out.println("Print was cancelled.");
	    return;
	}

	System.out.println("Printing ...");
	Graphics g = pj.getGraphics();
	_iw.paint(g, 0, 0, _iw.getWidth(), _iw.getHeight());
	pj.end();
	g.dispose();
	XXX allan */
    }


    /**
     * Get the location of the center of the image being displayed.
     */
    public double[] getImageCenterLocation() {
	if (! _iw.getCoordinateConverter().isWCS()) {
	    return null;
	}

	Point2D.Double p = _iw.getCoordinateConverter().getWCSCenter();
	return new double[]{p.x, p.y};
    }


    /**
     * Called when the TPE is shutting down to enable/speed garbage collection.
     */
    public void	remove() {
	//_piUpdater.free();

	TpePositionMap pm = TpePositionMap.removeMap(_iw);
	if (pm != null) pm.free();

	_iw.free();
	_iw = null;

	if (_spItem != null) {
	    _spItem.getEditFSM().deleteHierarchyChangeObserver(this);
	}
	_spItem    = null;
	_spContext = null;

	//_paniw.free();
	//_zoomiw.free();

	if (imageFrame instanceof JFrame)
	    ((JFrame)imageFrame).dispose();
	else if (imageFrame instanceof JInternalFrame)
	    ((JInternalFrame)imageFrame).dispose();
	
	_notifyOfClose();
    }


    /**
     * The standard action() method.
     */
    public void action() {
	if (_editorTools.getImageFeature() != null) {
	    _featureMan.setVisible(_editorTools.getImageFeature(), true);
	}
    }


    /**
     * Get the observation context in which the given item lives.  For items
     * that are SpObsContextItems, that'll be their parent (except for the
     * root node of course).
     */
    private SpObsContextItem _getContext(SpItem spItem)  {
	SpObsContextItem spContext;
	if (spItem instanceof SpObsContextItem) {
	    if (spItem.parent() == null) {
		spContext = (SpObsContextItem) spItem;
	    } else {
		spContext = (SpObsContextItem) spItem.parent();
	    }
	} else {
	    spContext = SpTreeMan.findObsContext(spItem);
	}
	return spContext;
    }


    //
    // Get the SpTelescopePosList that is defined in the current SpItem's
    // context.
    //
    private SpTelescopePosList _getPosList(SpItem spItem) {
	// Get the telescope target list associated with the item
	SpTelescopeObsComp targetComp = SpTreeMan.findTargetList(spItem);
	if (targetComp == null) {
	    return null;
	}
	return targetComp.getPosList();
    }


    //
    // Is there an instrument in the scope of the given SpItem?
    //
    private boolean _instrumentInScope(SpItem spItem) {
	return (SpTreeMan.findInstrument(spItem) != null);
    }

    //
    // Add the item's associated image feature (if any).  This method
    // is called when the current SpItem is changed to show any
    // image features associated with it.
    //
    private void _addItemImageFeature(SpItem spItem) {
	Object o = spItem.getClientData();
	if (o instanceof TpeFeatureClientData) {
	    TpeImageFeature tif = ((TpeFeatureClientData) o).getImageFeature();
	    if (tif != null) {
		addFeature(tif);
	    }
	}
    }

    //
    // Remove the item's associated image feature (if any).
    //
    private void _deleteItemImageFeature(SpItem spItem) {
	Object o = spItem.getClientData();
	if (o instanceof TpeFeatureClientData) {
	    TpeImageFeature tif = ((TpeFeatureClientData) o).getImageFeature();
	    if (tif != null) {
		deleteFeature(tif);
	    }
	}
    }

    /**
     * Reset the current SpItem to be the given SpItem.  This operation
     * potentially changes the context in which the Position Editor operates.
     * If so, a new image may be substituted for the current image.
     */
    public void reset(SpItem spItem) {
	if(spItem instanceof SpSurveyObsComp) {
	  SpSurveyObsComp spSurveyObsComp = (SpSurveyObsComp)spItem;
	  spItem = spSurveyObsComp.getSpTelescopeObsComp(spSurveyObsComp.getSelectedTelObsComp());
	}

	// Find the telescope position list in the spItem's scope, if any, and
	// pass it to reset().
	reset(_getPosList(spItem), spItem);
    }

    /**
     * Do all the reset() work.
     */
    void reset(SpTelescopePosList posList, SpItem spItem) {
	// Turn off old watchers, remove old image features etc.
	if ((_spItem != null) && (_spItem != spItem)) {
	    // Delete any features associated with the item
	    if (! (_spItem instanceof SpInstObsComp)) {
		_deleteItemImageFeature(_spItem);
	    }

	    // Delete any image features assocated with the instrument
	    // in the old scope.
	    SpInstObsComp oldInst = SpTreeMan.findInstrument(_spItem);
	    SpInstObsComp newInst = SpTreeMan.findInstrument(spItem);
	    if ((oldInst != newInst) && (oldInst != null)) {
		_deleteItemImageFeature(oldInst);
	    }

	    // Watch the spItem hierarchy to know when telescope comps or inst
	    // comps are inserted into or removed from its scope.
	    if (TpeManager.findRootItem(_spItem) != TpeManager.findRootItem(spItem)) {
		_spItem.getEditFSM().deleteHierarchyChangeObserver(this);
	    }
	}
	spItem.getEditFSM().addHierarchyChangeObserver(this);

	// If switching position lists (or if this is the first call to reset),
	// then get a new image.
	if ((_posList != posList) || (_spItem == null)) {
	    _posList = posList;
	    loadImage(ANY_SERVER);
	}

	_spItem    = spItem;
	_spContext = _getContext(spItem);


	// If there is no position list, disable the target related items.
	// Otherwise, make sure they are enabled.
	_setTargetWidgetsDisabled(_posList == null);

	// If there is no instrument in this scope, make sure the instrument
	// related items are disabled.  Otherwise, make sure they are enabled.
	SpInstObsComp newInst = SpTreeMan.findInstrument(spItem);
	if (newInst == null) {
	    _setInstWidgetsDisabled(true);
	} else {
	    _setInstWidgetsDisabled(false);
	}

	// Reset the position map with the new position list to work from.
	TpePositionMap pm = TpePositionMap.getMap(_iw);
	pm.reset(_posList);

	// Tell the image widget that it has a new base item.
	_iw.reset(_spItem);

	// Add any image features associated with this item, and add any
	// image features associated with instruments in its scope.
	_addItemImageFeature(_spItem);
	if (newInst != null) {
	    _addItemImageFeature(newInst);
	}

	_iw.repaint();
    }


    /**
     *
     */
    void _downloadFinished(String message, boolean doThaw) {
	/* XXX allan 
	_stopAction.actionsFinished();
	if (message != null) _piUpdater.reset(message);
	if (doThaw) thaw();
	XXX allan */
    }


    /**
     * Delete a feature from the set of image features available for display.
     */
    public void	deleteFeature(TpeImageFeature tif) {
	// Remember whether the feature was visible or not when last removed.
	if (tif.isVisible()) {
	    _featureVisibleState.put(tif.getClass().getName(), Boolean.TRUE);
	} else {
	    _featureVisibleState.put(tif.getClass().getName(), Boolean.FALSE);
	}

	/* XXX allan
	// If this feature has properties, remove them from the "View" menu.
	final BasicPropertyList pl = tif.getProperties();
	if (pl != null) {
	    PopupMenuExt pme = _menus.getMenuBar().getPopupMenu("View");
	    pme.remove(_getPropertyEditorMenuLabel(tif));
	}
	XXX allan */

	_editorTools.deleteCreateTools(tif);
	_featureMan.deleteFeature(tif);
    }


    /**
     * Watch for changes to the structure of the SpItem associated with
     * the editor.
     */
    public void	spHierarchyChange() {
        repaint(); // MFO (April 19, 2002)

	SpObsContextItem spContext = _getContext(_spItem);
	if (spContext == null) return;

	SpTelescopePosList posList = _getPosList(_spItem);

	if (spContext != _spContext) {
	    // The item's context has changed, so reset
	    //System.out.println(" ***** obs context is different, resetting! ");
	    reset(posList, _spItem);

	} else if (posList != _posList) {
	    // The position list is different, so reset
	    //System.out.println(" ***** posList is different, resetting! ");
	    reset(posList, _spItem);

	} else {
	    _setInstWidgetsDisabled( !_instrumentInScope(_spItem) );
	}
    }


    /**
     * Implement spItemsAdded method.
     * @see SpHierarchyChangeObserver
     */
    public void spItemsAdded(SpItem parent, SpItem[] children, SpItem afterChild) {
	spHierarchyChange();
    }

    /**
     * Implement spItemsRemoved method.
     * @see SpHierarchyChangeObserver
     */
    public void spItemsRemoved(SpItem parent, SpItem[] children) {
	spHierarchyChange();
    }

    /**
     * Implement spItemsMoved method.
     * @see SpHierarchyChangeObserver
     */
    public void spItemsMoved(SpItem oldParent, SpItem[] children,
			     SpItem newParent, SpItem afterChild) {
	spHierarchyChange();
    }


    /**
     * Recieve a mouse event in the image widget.
     */
    public void viewportMouseEvent(ViewportImageWidget iw, ViewportMouseEvent vme) {
	// Fits Mouse Event update

	if (!(vme instanceof FitsMouseEvent)) {
	    return;
	}

	FitsMouseEvent mouseEvent = (FitsMouseEvent) vme;

	// Shouldn't happen otherwise, but make sure the update came from the
	// TpeImageWidget
	if (mouseEvent.source != _iw) {
	    return;
	}

	int x = mouseEvent.xWidget;
	int y = mouseEvent.yWidget;

	if (mouseEvent.id == MouseEvent.MOUSE_PRESSED) {
	    switch (_editorTools.getMode()) {
	    case TpeEditorTools.MODE_BROWSE:
		break;

	    case TpeEditorTools.MODE_DRAG: 
		_iw.dragStart(mouseEvent);
		break;
	    
	    case TpeEditorTools.MODE_ERASE: 
		_iw.erase(mouseEvent);
		break;

	    default:
	    }

	    _iw.select(mouseEvent);

	    return;
	}


	if (mouseEvent.id == MouseEvent.MOUSE_CLICKED) {
	    switch (_editorTools.getMode()) {
	    case TpeEditorTools.MODE_ERASE: 
		_iw.erase(mouseEvent);
		break;

	    case TpeEditorTools.MODE_CREATE: 
		TpeImageFeature tif;
		tif = (TpeImageFeature) _editorTools.getImageFeature();
		_iw.create(mouseEvent, tif, _editorTools.getCurrentButtonLabel());
		break;
	    }
	    return;
	}


	if (mouseEvent.id == MouseEvent.MOUSE_DRAGGED) {
	    switch (_editorTools.getMode()) {
	    case TpeEditorTools.MODE_DRAG: {
		_iw.drag(mouseEvent);
		break;
	    }
	    case TpeEditorTools.MODE_ERASE: {
		_iw.erase(mouseEvent);
		break;
	    }
	    }
	    return;
	}

	if (mouseEvent.id == MouseEvent.MOUSE_RELEASED) {
	    if (_editorTools.getMode() == TpeEditorTools.MODE_DRAG) {
		_iw.dragStop(mouseEvent);
	    }
	    return;
	}

	if (mouseEvent.id == MouseEvent.MOUSE_MOVED) {
	    return;
	}
    }

    /**
     * XXX allan
    public boolean handleEvent(Event evt) {
	if (evt.id == Event.WINDOW_DESTROY) {
	    System.exit(0);
	}

	if (evt.id == Event.MOUSE_ENTER) {
	    _editorTools.resetCursor();
	} else if (evt.id == Event.MOUSE_EXIT) {
	    setCurrentMouseCursor( java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR) );
	}

	return super.handleEvent(evt);
    }
    XXX allan */

    /** Set the image window frame's title. */
    public void setTitle(String s) {
	_iw.setTitle(s);
    }


    /** Return the image window frame's title. */
    public String getTitle() {
	return _iw.getTitle();
    }

    /**
     * Test main:
     * 
     * Usage: java [-Djsky.catalog.skycat.config=$SKYCAT_CONFIG] TelescopePosEditort [-internalframes] 
     *             [-shownavigator] [imageFileOrUrl]
     * <p>
     * The <em>jsky.catalog.skycat.config</em> property defines the Skycat style catalog config file to use.
     * (The default uses the ESO Skycat config file).
     * <p>
     * If -internalframes is specified, internal frames are used. 
     * <p>
     * If -shownavigator is specified, the catalog navigator window is displayed on startup. 
     * <p>
     * The imageFileOrUrl argument may be an image file or URL to load.
     */
    public static void main(String[] args) {
	String imageFileOrUrl = null;
	boolean internalFrames = false;
	boolean showNavigator = false;
	boolean ok = true;

	for (int i = 0; i < args.length; i++) {
	    if (args[i].charAt(0) == '-') {
		String opt = args[i];
		if (opt.equals("-internalframes")) {
		    internalFrames = true;
		}
		else if (opt.equals("-shownavigator")) {
		    showNavigator = true;
		}
		else {
		    System.out.println("Unknown option: " + opt);
		    ok = false;
		    break;
		}
	    }
	    else {
		if (imageFileOrUrl != null) {
		    System.out.println("Please specify one image file or URL: " + imageFileOrUrl);
		    ok = false;
		    break;
		}
		imageFileOrUrl = args[i];
	    }
	}
	
	if (!ok) {
	    System.out.println("Usage: java [-Djsky.catalog.skycat.config=$SKYCAT_CONFIG] TelescopePosEditor [-internalframes] [-shownavigator] [imageFileOrUrl]");
	    System.exit(1);
	}

	new TelescopePosEditor(imageFileOrUrl, internalFrames, showNavigator);
    }

    /**
     * MFO: Added to cause repaint to call _iw.repaint();
     */
    public void repaint() {
      super.repaint();
      _iw.updateImage();
    }

    public void open(String fileOrUrl) {
	super.open(fileOrUrl);
	_iw.updateImage();
    }

    public boolean isVisible() {
        return imageFrame.isVisible();
    }
}
