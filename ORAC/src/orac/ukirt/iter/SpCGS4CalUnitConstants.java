// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter ;

public interface SpCGS4CalUnitConstants
{
	public static final String ATTR_CALTYPE = "calType" ;
	public static final String ATTR_LAMP = "lamp" ;
	public static final String ATTR_FILTER = "filter" ;
	public static final String ATTR_MODE = "acqMode" ;
	public static final String ATTR_EXPOSURE_TIME = "exposureTime" ;
	public static final String ATTR_COADDS = "coadds" ;
	public static final String ATTR_CVF_WAVELENGTH = "cvfWavelength" ;
	public static final String ATTR_FLAT_SAMPLING = "flatSampling" ;
	public static final String ATTR_NEUTRAL_DENSITY = "neutralDensity" ;
	public String[] CALTYPES = { "Arc" , "Flat" , } ;
}
