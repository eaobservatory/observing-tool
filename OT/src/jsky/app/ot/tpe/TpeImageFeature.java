// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe;

import java.awt.Font;
import java.awt.Graphics;

import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.util.BasicPropertyList;

/**
 * TpeImageFeature is a class used by the Position Editor to manipulate the
 * many potential image features that may be drawn.  The Position Editor
 * classes use the TpeImageFeature interface rather than having to know
 * about individual subclasses.  For instance, the TpeImageWidget has
 * methods to add and remove TpeImageFeatures.  When the image widget is
 * painted, it simply loops through its list of image features calling the
 * <code>draw()</code> method on each.
 */
public abstract class TpeImageFeature
{
	/**
	 * Size of items that don't depend upon the scale of the image.
	 * This is the size of the width and height, or radius, of the item.
	 */
	public static final int MARKER_SIZE = 4;

	/** Font used to draw text items.  */
	public static final Font FONT = new Font( "dialog" , Font.ITALIC , 10 );

	/** The image widget in which to draw. */
	protected TpeImageWidget _iw;

	/** Whether the feature is being drawn. */
	protected boolean _isVisible;

	/** The feature's name. */
	protected String _name;

	/** The feature's description. */
	protected String _description;

	/**
	 * Instantiate a TpeImageFeature from a fully qualified class name.
	 */
	public static TpeImageFeature createFeature( String className )
	{
		TpeImageFeature tif = null;
		try
		{
			Class c = Class.forName( className );
			tif = ( TpeImageFeature )c.newInstance();
		}
		catch( Exception ex )
		{
			System.out.println( "Could not create a TpeImageFeature from: " + className );
			System.out.println( ex );
		}
		return tif;
	}

	/**
	 * Create with a short name and a longer description.
	 */
	public TpeImageFeature( String name , String descr )
	{
		_name = name;
		_description = descr;
	}

	/**
	 * Reinitialize.  Override if additional initialization is required.
	 */
	public void reinit( TpeImageWidget iw , FitsImageInfo fii )
	{
		_iw = iw;
		_isVisible = true;
	}

	/**
	 * The position angle has been updated.  Override if this is important
	 * to the feature.
	 */
	public void posAngleUpdate( FitsImageInfo fii )
	{
		return;
	}

	/**
	 * Receive notification that the feature has been unloaded.  Subclasses
	 * should override if interrested, but call super.unloaded().
	 */
	public void unloaded()
	{
		_iw = null;
		_isVisible = false;
	}

	/**
	 * Is this feature currently visible?
	 */
	public boolean isVisible()
	{
		return _isVisible;
	}

	/**
	 * Get the feature's name.
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * Set the feature's name.
	 *
	 * This allows to set a telescope specific name.
	 * Added by MFO (22 January 2002).
	 */
	public void setName( String name )
	{
		_name = name;
	}

	/**
	 * Get the feature's description.
	 */
	public String getDescription()
	{
		return _description;
	}

	/**
	 * Get the property set assoicated with this feature.  Image features
	 * may support a property set, which can be used to provide a means
	 * to configure their display.  Unless overriden, this method simply
	 * returns null, indicating that there are no configurable properties.
	 * 
	 * @see BasicPropertyList
	 */
	public BasicPropertyList getProperties()
	{
		return null;
	}

	/**
	 * Draw the feature.
	 */
	public abstract void draw( Graphics g , FitsImageInfo fii );
}
