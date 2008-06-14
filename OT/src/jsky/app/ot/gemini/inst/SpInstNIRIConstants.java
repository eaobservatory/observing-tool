// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.inst ;

import gemini.sp.obsComp.SpInstConstants ;

public interface SpInstNIRIConstants extends SpInstConstants
{
	public static final String ATTR_CAMERA = "camera" ;
	public static final String ATTR_MASK = "mask" ;
	public static final String ATTR_DISPERSER = "disperser" ;
	public static final String ATTR_FILTER = "filter" ;
	public static final String ATTR_FILTER_TYPE = ".gui.filterType" ;
	public static final int CAMERA_F6 = 0 ;
	public static final int CAMERA_F14 = 1 ;
	public static final int CAMERA_F32 = 2 ;
	static String[] CAMERAS = { "f/6  (0.12 arcsec/pix)" , "f/14 (0.05 arcsec/pix)" , "f/32 (0.02 arcsec/pix)" } ;
	static String[] DISPERSERS = 
	{ 
		"none" , 
		"R=600  J-band" , 
		"R=600 H-band" , 
		"R=600 K-band" , 
		"R=2000 J-band" , 
		"R=2000 H-band" , 
		"R=2000 K-band" , 
		"R=1500 L-band" 
	} ;

	public static final String MASK_0_1 = "0.1 arcsec slit" ;

	public static final String MASK_0_2 = "0.2 arcsec slit" ;

	public static final String MASK_1_0 = "1.0 arcsec slit" ;

	static String[] MASKS = 
	{ 
		"none" , 
		MASK_0_1 , 
		MASK_0_2 , 
		MASK_1_0 , 
		"Wollaston mask" , 
		"Coronograph mask 1" , 
		"Coronograph mask 2" 
	} ;

	static String FILTER_NONE = "None" ;

	static String[][] BROAD_BAND_FILTERS = 
	{ 
		{ "Z" , "1.033" } , 
		{ "J" , "1.250" } , 
		{ "H" , "1.635" } , 
		{ "K'" , "2.120" } , 
		{ "K_s" , "2.150" } , 
		{ "K" , "2.200" } , 
		{ "K_l" , "2.240" } , 
		{ "L'" , "3.770" } , 
		{ "M'" , "4.680" } 
	} ;

	static String[][] NARROW_BAND_FILTERS = 
	{ 
		{ "He I_A" , "1.083" } , 
		{ "Pa-gamma" , "1.094" } , 
		{ "J-continuum" , "1.207" } , 
		{ "OII" , "1.237" } , 
		{ "Pa-beta" , "1.282" } , 
		{ "H-continuum" , "1.570" } , 
		{ "[FeII]" , "1.644" } , 
		{ "He I_B" , "2.059" } , 
		{ "H2 v=1-0 S(1)" , "2.122" } , 
		{ "Br-gamma" , "2.166" } , 
		{ "He 1_C" , "2.189" } , 
		{ "H2 v=2-1 S(1)" , "2.248" } , 
		{ "K-continuum" , "2.270" } , 
		{ "CO (2-0 bh)" , "2.294" } , 
		{ "CO (3-1 bh)" , "2.323" } , 
		{ "CO (4-2 bh)" , "2.353" } , 
		{ "H2O ice" , "3.050" } , 
		{ "hydrocarbon" , "3.295" } , 
		{ "Br-alpha cont" , "3.990" } , 
		{ "Br-alpha" , "4.052" } 
	} ;

	static String[][] SPECIAL_FILTERS = 
	{ 
		{ FILTER_NONE , "-----" } , 
		{ "Blank" , "-----" } , 
		{ "HK' Notch" , "1.935" } , 
		{ "CH4_s" , "1.580" } , 
		{ "CH4_l" , "1.690" } , 
		{ "User" , "x.xxx" } , 
		{ "ND1" , "1-5" } , 
		{ "ND2" , "1-5" } 
	} ;
}
