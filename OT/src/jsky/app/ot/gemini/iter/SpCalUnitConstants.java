// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.iter;

public interface SpCalUnitConstants
{
   public static final String ATTR_LAMP          = "lamp";
   public static final String ATTR_FILTER        = "filter";
   public static final String ATTR_DIFFUSER      = "diffuser";
   public static final String ATTR_EXPOSURE_TIME = "exposureTime";
   public static final String ATTR_COADDS        = "coadds";

   static String[] LAMPS = {
	"Black Body (1173K)",
	"Quartz Halogen (50W)",
	"Xenon Arc (75W)",
	"Hollow Cathode",
	"Lpg (Ar)",
	"Lpg (Xe)",
	"Lpg (Kr)",
	"Hollow Cathode Fixed"
   };

   static String[] FLAT_LAMPS = {
      LAMPS[0], LAMPS[1]
   };

   static String[] ARC_LAMPS = {
      LAMPS[2], LAMPS[3], LAMPS[4], LAMPS[5], LAMPS[6], LAMPS[7]
   };

   static String[] FILTERS = {
	"none",
	"x10 NDF",
	"x30 NDF",
	"x100 NDF",
	"x300 NDF",
	"x1000 NDF",
	"CBF",
	"x300 NDF + CBF"
   };

   static String[] DIFFUSERS = {
	"nir",
	"visible"
   };
}

 
