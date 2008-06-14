// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe.feat ;

import java.awt.Graphics ;
import jsky.app.ot.fits.gui.FitsImageInfo ;
import jsky.app.ot.tpe.TpeImageFeature ;
import jsky.app.ot.tpe.TpeImageWidget ;
import jsky.catalog.gui.TablePlotter ;
import jsky.navigator.Navigator ;

public class TpeCatalogFeature extends TpeImageFeature
{
	/**
	 * Construct the feature with its name and description. 
	 */
	public TpeCatalogFeature()
	{
		super( "Catalog" , "Plot of any catalog query results." ) ;
	}

	/**
	 * Receive notification that the feature has been unloaded.  Subclasses
	 * should override if interrested, but call super.unloaded().
	 */
	public void unloaded()
	{
		Navigator nav = _iw.getNavigator() ;
		if( nav != null )
		{
			TablePlotter p = nav.getPlotter() ;
			if( p != null )
				p.setVisible( false ) ;
		}
		super.unloaded() ;
	}

	/**
	 * Reinitialize.  Override if additional initialization is required.
	 */
	public void reinit( TpeImageWidget iw , FitsImageInfo fii )
	{
		super.reinit( iw , fii ) ;
		Navigator nav = _iw.getNavigator() ;
		if( nav != null )
		{
			TablePlotter p = nav.getPlotter() ;
			if( p != null )
				p.setVisible( true ) ;
		}
	}

	/**
	 */
	public void draw( Graphics g , FitsImageInfo fii ){}
}
