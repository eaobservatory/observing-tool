// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.obsComp;

import gemini.sp.SpType;

/**
 * Site Quality observation component.
 */
public class SpSiteQualityObsComp extends SpObsComp
{
   public static final String ATTR_IMAGE_QUALITY = "imageQuality";
   public static final String ATTR_IR_BACKGROUND = "irBackground";
   public static final String ATTR_MOON          = "moon";
   public static final String ATTR_SKY           = "sky";

   public static final int IMAGE_QUALITY_20     = 0;
   public static final int IMAGE_QUALITY_50     = 1;
   public static final int IMAGE_QUALITY_IGNORE = 2;

   public static final String[] IMAGE_QUALITY_OPTIONS = {
      "20", "50", "ignore"
   };

   public static final int IR_BACKGROUND_20     = 0;
   public static final int IR_BACKGROUND_50     = 1;
   public static final int IR_BACKGROUND_IGNORE = 2;

   public static final String[] IR_BACKGROUND_OPTIONS = {
      "20", "50", "ignore"
   };

   public static final int MOON_DARK   = 0;
   public static final int MOON_BRIGHT = 1;
   public static final int MOON_IGNORE = 2;

   public static final String[] MOON_OPTIONS = {
      "dark", "bright", "ignore"
   };

   public static final int SKY_PHOTOMETRIC   = 0;
   public static final int SKY_SPECTROSCOPIC = 1;
   public static final int SKY_IGNORE        = 2;

   public static final String[] SKY_OPTIONS = {
      "photometric", "spectroscopic", "ignore"
   };

/**
 * Default constructor.  Initialize the component type.
 */
public SpSiteQualityObsComp()
{
   super( SpType.OBSERVATION_COMPONENT_SITE_QUALITY );

   _avTable.noNotifySet(ATTR_IMAGE_QUALITY, IMAGE_QUALITY_OPTIONS[IMAGE_QUALITY_IGNORE], 0);
   _avTable.noNotifySet(ATTR_IR_BACKGROUND, IR_BACKGROUND_OPTIONS[IR_BACKGROUND_IGNORE], 0);
   _avTable.noNotifySet(ATTR_MOON, MOON_OPTIONS[MOON_IGNORE], 0);
   _avTable.noNotifySet(ATTR_SKY,  SKY_OPTIONS[SKY_IGNORE],   0);
}

/**
 * Set the image quality.
 */
public void
setImageQuality(int iq)
{
   if ((iq<0) || (iq>=IMAGE_QUALITY_OPTIONS.length)) {
      return;
   }
   _avTable.set(ATTR_IMAGE_QUALITY, IMAGE_QUALITY_OPTIONS[iq]);
}

/**
 * Get the image quality.
 */
public int
getImageQuality()
{
   String iq = _avTable.get(ATTR_IMAGE_QUALITY);
   if (iq == null) return IMAGE_QUALITY_IGNORE;

   for (int i=0; i<IMAGE_QUALITY_OPTIONS.length; ++i) {
      if (iq.equals(IMAGE_QUALITY_OPTIONS[i])) {
         return i;
      }
   }
   return IMAGE_QUALITY_IGNORE;
}

/**
 * Get the image quality as a string.
 */
public String
getImageQualityAsString()
{
   String iq = _avTable.get(ATTR_IMAGE_QUALITY);
   if (iq == null) iq = IMAGE_QUALITY_OPTIONS[ IMAGE_QUALITY_IGNORE ];
   return iq;
}

/**
 * Set the ir background.
 */
public void
setIRBackground(int ir)
{
   if ((ir<0) || (ir>=IR_BACKGROUND_OPTIONS.length)) {
      return;
   }
   _avTable.set(ATTR_IR_BACKGROUND, IR_BACKGROUND_OPTIONS[ir]);
}

/**
 * Get the ir background.
 */
public int
getIRBackground()
{
   String ir = _avTable.get(ATTR_IR_BACKGROUND);
   if (ir == null) return IR_BACKGROUND_IGNORE;

   for (int i=0; i<IR_BACKGROUND_OPTIONS.length; ++i) {
      if (ir.equals(IR_BACKGROUND_OPTIONS[i])) {
         return i;
      }
   }
   return IR_BACKGROUND_IGNORE;
}

/**
 * Get the ir background as a string.
 */
public String
getIRBackgroundAsString()
{
   String ir = _avTable.get(ATTR_IR_BACKGROUND);
   if (ir == null) ir = IR_BACKGROUND_OPTIONS[ IR_BACKGROUND_IGNORE ];
   return ir;
}

/**
 * Set the moon.
 */
public void
setMoon(int moon)
{
   if ((moon<0) || (moon>=MOON_OPTIONS.length)) {
      return;
   }
   _avTable.set(ATTR_MOON, MOON_OPTIONS[moon]);
}

/**
 * Get the moon.
 */
public int
getMoon()
{
   String moon = _avTable.get(ATTR_MOON);
   if (moon == null) return MOON_IGNORE;

   for (int i=0; i<MOON_OPTIONS.length; ++i) {
      if (moon.equals(MOON_OPTIONS[i])) {
         return i;
      }
   }
   return MOON_IGNORE;
}

/**
 * Get the moon as a string.
 */
public String
getMoonAsString()
{
   String moon = _avTable.get(ATTR_MOON);
   if (moon == null) moon = MOON_OPTIONS[ MOON_IGNORE ];
   return moon;
}

/**
 * Set the sky.
 */
public void
setSky(int sky)
{
   if ((sky<0) || (sky>=SKY_OPTIONS.length)) {
      return;
   }
   _avTable.set(ATTR_SKY, SKY_OPTIONS[sky]);
}

/**
 * Get the sky.
 */
public int
getSky()
{
   String sky = _avTable.get(ATTR_SKY);
   if (sky == null) return SKY_IGNORE;

   for (int i=0; i<SKY_OPTIONS.length; ++i) {
      if (sky.equals(SKY_OPTIONS[i])) {
         return i;
      }
   }
   return SKY_IGNORE;
}

/**
 * Get the sky as a string.
 */
public String
getSkyAsString()
{
   String sky = _avTable.get(ATTR_SKY);
   if (sky == null) sky = SKY_OPTIONS[ SKY_IGNORE ];
   return sky;
}

}

