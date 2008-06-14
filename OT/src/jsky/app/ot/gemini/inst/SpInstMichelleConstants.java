// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.inst ;

import gemini.sp.obsComp.SpInstConstants ;

public interface SpInstMichelleConstants extends SpInstConstants
{
	String ATTR_CAMERA = "camera" ;
	String ATTR_MASK = "mask" ;
	String ATTR_DISPERSER = "disperser" ;
	String ATTR_CENTRAL_WAVELENGTH = "centralWavelength" ;
	String ATTR_FILTER = "filter" ;
	String ATTR_MODE = "mode" ;
	String NO_VALUE = "none" ;
	int DETECTOR_WIDTH = 320 ;
	int DETECTOR_HEIGHT = 240 ;
	int CAMERA_IMAGING = 0 ;
	int CAMERA_SPECTROSCOPY = 1 ;
	double CAMERA_IMAGING_ARCSEC_PER_PIX = 0.10 ;
	double CAMERA_SPECTROSCOPY_ARCSEC_PER_PIX = 0.18 ;
	String[] CAMERAS = { "imaging" , "spectroscopy" } ;
	String DEFAULT_DISPERSER = "R=200 N-band" ;
	String[] DISPERSERS = { NO_VALUE , "R=165 Q-band" , DEFAULT_DISPERSER , "R=1000" , "R=3000" , "R=20000" } ;

	// Default central wavelength for each of the dispersers above
	double[] DEFAULT_CENTRAL_WAVELENGTH = { 0. , 19.5 , 10.5 , 10.5 , 10.5 , 11.28 } ;

	String DEFAULT_MASK = "0.36 arcsec slit (2 pix)" ;
	String[] MASKS = 
	{ 
		NO_VALUE , 
		"0.18 arcsec slit (1 pix)" , 
		DEFAULT_MASK , 
		"0.54 arcsec slit (3 pix)" , 
		"0.72 arcsec slit (4 pix)" , 
		"1.44 arcsec slit (8 pix)" , 
		"2.88 arcsec slit (16 pix)" 
	} ;

	int[] MASK_WIDTH_PIXELS = { 0 , 1 , 2 , 3 , 4 , 8 , 16 } ;

	String[][] FILTERS = 
	{ 
		{ NO_VALUE , "----" } , 
		{ "user" , "----" } , 
		{ "N" , "10.8" } , 
		{ "Q" , "20.8" } , 
		{ "Silicate 1" , "7.9" } , 
		{ "Silicate 2" , "8.8" } , 
		{ "Silicate 3" , "9.8" } , 
		{ "Silicate 4" , "10.3" } , 
		{ "Silicate 5" , "11.7" } , 
		{ "Silicate 5" , "12.5" } , 
		{ "F-P etalon" , "10.0" } 
	} ;

	public String CHOP_MODE = "Chop" ;
	public String STARE_MODE = "Stare" ;
	public String DEFAULT_MODE = CHOP_MODE ;
	String[] MODES = { CHOP_MODE , STARE_MODE } ;
}
