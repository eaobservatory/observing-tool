// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter ;

public interface SpCalUnitConstants
{
	public static final String ATTR_LAMP = "lamp" ;
	public static final String ATTR_FILTER = "filter" ;
	public static final String ATTR_MODE = "acqMode" ;
	public static final String ATTR_EXPOSURE_TIME = "exposureTime" ;
	public static final String ATTR_COADDS = "coadds" ;
	public String[] MODES = { "STARE" , "NDSTARE" , "CHOP" , "NDCHOP" } ;
	static String[] CGS4_FLAT_LAMPS = 
	{ 
		"Black Body (0.5)" , 
		"Black Body (0.8)" , 
		"Black Body (1.3)" , 
		"Black Body (1.8)" , 
		"Black Body (2.4)" , 
		"Black Body (3.2)" , 
		"Black Body (5.0)" , 
		"Tungsten-Halogen (50W)" 
	} ;
	static String[] CGS4_ARC_LAMPS = { "Xenon Arc (75W)" , "Argon Arc" , "Krypton Arc" } ;
	static String[] CGS4_FILTERS = { "B1" , "B2" , "B3" , "N1" , "N2" , "N3" , "N4" , "N5" , "N6" , "?" } ;
	static String[] MICHELLE_FLAT_LAMPS = { "Black Body" , "Tungsten-Halogen (50W)" } ;
	static String[] MICHELLE_ARC_LAMPS = { "10um arc" , "20um Arc" , "echelle Arc" } ;
	static String[][] MICHELLE_FILTERS = 
	{ 
		{ "B1" , "1.1" } , 
		{ "B2" , "2.2" } , 
		{ "B3" , "3.3" } , 
		{ "N1" , "7.9" } , 
		{ "N2" , "8.8" } , 
		{ "N3" , "9.8" } , 
		{ "N4" , "10.3" } , 
		{ "N5" , "11.7" } , 
		{ "N6" , "12.5" } , 
		{ "?" , "10.0" } 
	} ;
}
