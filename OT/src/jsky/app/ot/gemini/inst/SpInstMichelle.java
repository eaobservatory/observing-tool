// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.inst;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpChopCapability;
import gemini.sp.obsComp.SpStareCapability;

/**
 * The Michelle instrument.
 */
public final class SpInstMichelle extends SpInstObsComp
                               implements SpInstMichelleConstants
{
   public static final SpType SP_TYPE = SpType.create(
               SpType.OBSERVATION_COMPONENT_TYPE, "inst.michelle", "Michelle");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpInstMichelle());
}

public SpInstMichelle()
{
   super(SP_TYPE);

   addCapability( new SpChopCapability()  );
   addCapability( new SpStareCapability() );

   String attr  = ATTR_CAMERA;
   String value = CAMERAS[0];
   _avTable.noNotifySet(attr, value, 0);

   attr  = ATTR_MODE;
   value = DEFAULT_MODE;
   _avTable.noNotifySet(attr, value, 0);

   attr  = ATTR_FILTER;
   value = FILTERS[0][0];
   _avTable.noNotifySet(attr, value, 0);

   attr  = ATTR_EXPOSURE_TIME;
   _avTable.noNotifySet(attr, "0", 0);

   attr  = ATTR_COADDS;
   _avTable.noNotifySet(attr, "1", 0);
}

/**
 * Get the chop capability.
 */
public SpChopCapability
getChopCapability()
{
   return (SpChopCapability) getCapability(SpChopCapability.CAPABILITY_NAME);
}

/**
 * Get the stare capability.
 */
public SpStareCapability
getStareCapability()
{
   return (SpStareCapability) getCapability(SpStareCapability.CAPABILITY_NAME);
}

/**
 * Return the science area based upon the current camera.
 */
public double[]
getScienceArea()
{
   double arcsecPerPix;

   int camera = getCameraIndex();
   if (camera == CAMERA_IMAGING) {
      arcsecPerPix = CAMERA_IMAGING_ARCSEC_PER_PIX;
   } else {
      arcsecPerPix = CAMERA_SPECTROSCOPY_ARCSEC_PER_PIX;
   }

   double[] size = new double[2];
   double      w = (double) getMaskWidthPixels();
   if (w == 0.0) {
      w = (double) DETECTOR_WIDTH;
   }
   size[0] = w * arcsecPerPix;
   size[1] = DETECTOR_HEIGHT * arcsecPerPix;
   return size;
}

/**
 * Is the instrument in "imaging" mode (in other words, is the "imaging"
 * camera selected instead of the spectroscopy camera).
 */
public boolean
isImaging()
{
   return (getCameraIndex() == CAMERA_IMAGING);
}

/**
 * Get the camera.
 */
public String
getCamera()
{
   String camera = _avTable.get(ATTR_CAMERA);
   if (camera == null) {
      camera = CAMERAS[0];
   }
   return camera;
}

/**
 * Get the camera index: one of CAMERA_F6, CAMERA_F14, or CAMERA_F32.
 */
public int
getCameraIndex()
{
   String camera = _avTable.get(ATTR_CAMERA);
   int index = CAMERA_IMAGING;
   if ((camera == null) || (camera.equals(CAMERAS[CAMERA_SPECTROSCOPY]))) {
      index = CAMERA_SPECTROSCOPY;
   }
   return index;
}

/**
 * Set the camera.
 */
public void
setCamera(String camera)
{
   _avTable.set(ATTR_CAMERA, camera);
}

/**
 * Get the disperser.
 */
public String
getDisperser()
{
   if (isImaging()) {
      return NO_VALUE;
   }

   String disperser = _avTable.get(ATTR_DISPERSER);
   if (disperser == null) {
      _avTable.noNotifySet(ATTR_DISPERSER, DEFAULT_DISPERSER, 0);
      disperser = DEFAULT_DISPERSER;
   }
   return disperser;
}

/**
 * Set the disperser.
 */
public void
setDisperser(String disperser)
{
   _avTable.set(ATTR_DISPERSER, disperser);
}

/**
 * Get the index of the current disperser.
 */
public int
getDisperserIndex()
{
   String disperser = getDisperser();
   for (int i=0; i<DISPERSERS.length; ++i) {
      if (disperser.equals(DISPERSERS[i])) return i;
   }
   return 0;
}

/**
 * Get the dispserser R.
 */
public int
getDisperserR()
{
   String disperser = getDisperser();
   if (disperser.equals(NO_VALUE)) return 0;

   if (!disperser.startsWith("R=")) return 0;

   int space = disperser.indexOf(' ');
   if (space == -1) space = disperser.length();
   
   String rString = disperser.substring(2, space);
   try {
      return Integer.parseInt(rString);
   } catch (NumberFormatException ex) {}
   return 0;
}

/**
 * Get the central wavelength.
 */
public double
getCentralWavelength()
{
   double cwl = _avTable.getDouble(ATTR_CENTRAL_WAVELENGTH, 0.0);
   if (cwl == 0.0) return getDefaultCentralWavelength();
   return cwl;
}

/**
 * Get the default central wavelength using the current disperser.
 */
public double
getDefaultCentralWavelength()
{
   return DEFAULT_CENTRAL_WAVELENGTH[ getDisperserIndex() ];
}

/**
 * Set the central wavelength.
 */
public void
setCentralWavelength(double cwl)
{
   _avTable.set(ATTR_CENTRAL_WAVELENGTH, cwl);
}

/**
 * Set the central wavelength as a string.
 */
public void
setCentralWavelength(String cwl)
{
   double d = 0.0;
   try {
     Double tmp = Double.valueOf(cwl);
     d = tmp.doubleValue();
   } catch (Exception ex) {}

   _avTable.set(ATTR_CENTRAL_WAVELENGTH, d);
}

/**
 * Use default Central Wavelength.
 */
public void
useDefaultCentralWavelength()
{
   _avTable.rm(ATTR_CENTRAL_WAVELENGTH);
}

/**
 * Calculate the wavelength coverage, based upon the disperser and
 * central wavelength.  0 is returned if there is no disperser selected.
 */
public double[]
getWavelengthCoverage()
{
   int r = getDisperserR();
   if (r == 0) return new double[] { 0.0, 0.0 };

   double centralWL    = getCentralWavelength();
   double coverage     = (DETECTOR_WIDTH/2.0) * centralWL / r;
   double halfCoverage = coverage/2.0;
   return new double[] { centralWL - halfCoverage, centralWL + halfCoverage };
}

/**
 * Get the mask.
 */
public String
getMask()
{
   if (isImaging()) {
      return NO_VALUE;
   }

   String mask = _avTable.get(ATTR_MASK);
   if (mask == null) {
      mask = DEFAULT_MASK;
   }
   return mask;
}

/**
 * Set the mask.
 */
public void
setMask(String mask)
{
   _avTable.set(ATTR_MASK, mask);
}

/**
 * Get the index of the current mask.
 */
public int
getMaskIndex()
{
   String mask = getMask();
   for (int i=0; i<MASKS.length; ++i) {
      if (mask.equals(MASKS[i])) return i;
   }
   return 0;
}

/**
 * Get the mask width for the current mask.
 */
public int
getMaskWidthPixels()
{
   return MASK_WIDTH_PIXELS[ getMaskIndex() ];
}

/**
 * Get the filter.
 */
public String
getFilter()
{
   String filter = _avTable.get(ATTR_FILTER);
   if (filter == null) filter = NO_VALUE;
   return filter;
}

/**
 * Set the filter.
 */
public void
setFilter(String filter)
{
   _avTable.set(ATTR_FILTER, filter);
}


/**
 * Get the mode.
 */
public String
getMode()
{
   String mode = _avTable.get(ATTR_MODE);
   if (mode == null) mode = DEFAULT_MODE; 
   return mode;
}

/**
 * Set the mode.
 */
public void
setMode(String mode)
{
   _avTable.set(ATTR_MODE, mode);
}

}
