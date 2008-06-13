// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter ;

public interface SpUISTCalConstants
{
	public static final String ATTR_CALTYPE = "calType" ;
	public static final String ATTR_FILTER = "filter" ;
	public static final String ATTR_MODE = "mode" ;
	public static final String ATTR_EXPOSURE_TIME = "exposureTime" ;

	public static final String ATTR_NREADS = "nreads" ;

	public static final String ATTR_READ_INTERVAL = "readInterval" ;

	public static final String ATTR_DUTY_CYCLE = "dutyCycle" ;
	public static final String ATTR_CHOP_FREQUENCY = "chopFrequency" ;
	public static final String ATTR_CHOP_DELAY = "chopDelay" ;
	public static final String ATTR_COADDS = "coadds" ;
	public static final String ATTR_FLAT_SOURCE = "flatSource" ;
	public static final String ATTR_ARC_SOURCE = "arcSource" ;
	public static final String ATTR_FOCUS = "focus" ;
	public static final String ATTR_ORDER = "order" ;
	public static final String ATTR_CENTRAL_WAVELENGTH = "centralWavelength" ;
	public static final String ATTR_OBSERVATION_TIME = "observationTime" ;
	public static final String ATTR_EXPTIME_OT = "expTimeOT" ;

	public static final String DEFAULT_FLAT_OBSERVATION_TIME = "20.0" ;
	public static final String DEFAULT_ARC_OBSERVATION_TIME = "20.0" ;
}
