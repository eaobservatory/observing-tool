/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.jcmt.inst;

import java.io.*;
import java.util.*;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.util.Format;
import gemini.util.CoordSys;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.obsComp.SpDRObsComp;

/**
 * This class defines the DRRecipe Observation Component for JCMT.
 *
 * The component allows the specification of an ORACDR recipe.<p>
 *
 * Additionally it is used to specify ACSIS DR information.
 *
 * @author Alan Bridger, UKATC, modified for JCMT OT by Martin Folger (M.Folger@roe.ac.uk)
 */
public final class SpDRRecipe extends SpDRObsComp
{
    public static final String WINDOW_TYPES_TAG        = "window types";
    public static final String PROJECTION_TYPES_TAG    = "projection types";
    public static final String GRID_FUNCTION_TYPES_TAG = "grid_function types";

    public static final String ATTR_OBJECT_RECIPE  = "objectRecipe";


    public static final String ATTR_POLYNOMIAL_ORDER      = "polynomialOrder";
    public static final String ATTR_TRUNCATION_CHANNELS   = "truncationChannels";
    public static final String ATTR_BASELINE_FITTING      = "baseliningMethod";
    public static final String ATTR_BASELINE_REGION_MIN   = "baselineRegionMin";
    public static final String ATTR_BASELINE_REGION_MAX   = "baselineRegionMax";
    public static final String ATTR_BASELINE_REGION_UNITS = "baselineRegionUnits";
    public static final String ATTR_AUTO_BASELINE_REGION  = "baselineFraction";
    public static final String ATTR_LINE_REGION_MIN       = "lineRegionMin";
    public static final String ATTR_LINE_REGION_MAX       = "lineRegionMax";
    public static final String ATTR_CHANNEL_BINNING       = "channelBinning";
    public static final String ATTR_REGRIDDING_METHOD     = "regriddingMethod";

    // The values of the following String variables were
    // taken from the corresponding XML elements in <cube_list>
    // when possible.
    // However, the variable names and method names in which the variable
    // are more in accordance with naming conventions used elsewhere in the OT.
    public static final String ATTR_WINDOW_TYPE           = "window_type";
    public static final String ATTR_PROJECTION            = "projection";
    public static final String ATTR_GRID_FUNCTION         = "grid_function";
    public static final String ATTR_SMOOTHING_RAD         = "smoothing_rad";
    public static final String ATTR_PIXEL_SIZE_X          = "x_pixel_size";
    public static final String ATTR_PIXEL_SIZE_Y          = "y_pixel_size";
    public static final String ATTR_OFFSET_X              = "x_offset";
    public static final String ATTR_OFFSET_Y              = "y_offset";


    /**
     * Channel spacings in kHz.
     *
     * The values 1000, 2000, 4000, 8000, 16000 are not exact.
     * <p>
     * There are other settings depending on the bandwidth and the front end.
     */
    public static final String [] CHANNEL_BINNINGS = { "1", "2", "4", "8", "16", "32", "64", "128",  "256" };

    /**
     * The choice has been restricted to "MHz" because the spectral region editor used to
     * specify baseline fit regions and line regions uses Hz (GHz) rather than "km.s-1" or "pixel".
     */
    public static final String [] BASELINE_REGION_UNITS = { "km.s-1", "MHz" /* , "pixel" */ };

    public static final String [] REGRIDDING_METHODS = { "Linear", "Bessel" };

    public static final String [] BASELINE_SELECTION = { "None",
	"Automatic", "Manual" };

    public static String [] WINDOW_TYPES        = null;
    public static String [] PROJECTION_TYPES    = null;
    public static String [] GRID_FUNCTION_TYPES = null;
    public static String [] POLYNOMIALS = null;
    private double DEFAULT_BASELINE = 0;

    public static LookUpTable HETERODYNE;
    public static LookUpTable SCUBA;

    public static final String OBJECT_RECIPE_DEFAULT            = "DEFAULT";
    public static final String SCUBA_OBJECT_RECIPE_DEFAULT      = OBJECT_RECIPE_DEFAULT; // "scubaDefault";
    public static final String HETERODYNE_OBJECT_RECIPE_DEFAULT = OBJECT_RECIPE_DEFAULT; // "heterodyneDefault";
    public static final String [] DR_RECIPES = { OBJECT_RECIPE_DEFAULT };
    
    public static final SpType SP_TYPE =
	SpType.create(SpType.OBSERVATION_COMPONENT_TYPE, "DRRecipe", "DRRecipe");

    private int _nRegions = 0;
    private boolean _readingRegion = false;
    
    // Register the prototype.
    static {
	SpFactory.registerPrototype(new SpDRRecipe());
    }
    
    /**
     * Default constructor.
     */
    public SpDRRecipe()
    {
	super(SP_TYPE);
	
	// Read in the config file
	String baseDir = System.getProperty("ot.cfgdir");
	String cfgFile = baseDir + "drrecipe.cfg";
	_readCfgFile (cfgFile);

	_avTable.noNotifySet(ATTR_OBJECT_RECIPE, OBJECT_RECIPE_DEFAULT, 0);
	_avTable.noNotifySet(ATTR_CHANNEL_BINNING, "" + CHANNEL_BINNINGS[0], 0);
	_avTable.noNotifySet(ATTR_BASELINE_FITTING, BASELINE_SELECTION[1], 0);
	_avTable.noNotifySet(ATTR_BASELINE_REGION_UNITS, BASELINE_REGION_UNITS[0], 0);
// 	_avTable.noNotifySet(ATTR_REGRIDDING_METHOD, REGRIDDING_METHODS[0], 0);
	_avTable.noNotifySet(ATTR_WINDOW_TYPE, WINDOW_TYPES[0], 0);
// 	_avTable.noNotifySet(ATTR_PROJECTION, PROJECTION_TYPES[0], 0);
// 	_avTable.noNotifySet(ATTR_GRID_FUNCTION, GRID_FUNCTION_TYPES[0], 0);
// 	_avTable.noNotifySet(ATTR_SMOOTHING_RAD, "1.0", 0);
// 	_avTable.noNotifySet(ATTR_PIXEL_SIZE_X, "1.0", 0);
// 	_avTable.noNotifySet(ATTR_PIXEL_SIZE_Y, "1.0", 0);
// 	_avTable.noNotifySet(ATTR_OFFSET_X, "0.0", 0);
// 	_avTable.noNotifySet(ATTR_OFFSET_Y, "0.0", 0);
	_avTable.noNotifySet(ATTR_TRUNCATION_CHANNELS, "0", 0);
    }
    
    private void _readCfgFile (String filename) {
	
	InstCfgReader instCfg = null;
	InstCfg instInfo = null;
	String block = null;
	
	instCfg = new InstCfgReader (filename);
	try {
	    while ((block = instCfg.readBlock()) != null) {
		instInfo = new InstCfg (block);
		if (InstCfg.matchAttr (instInfo, "heterodyne")) {
		    HETERODYNE = instInfo.getValueAsLUT();
		}
		else if (InstCfg.matchAttr (instInfo, "scuba")) {
		    SCUBA = instInfo.getValueAsLUT();
		}
		else if (InstCfg.matchAttr (instInfo, WINDOW_TYPES_TAG)) {
		    WINDOW_TYPES = instInfo.getValueAsArray();
		}
		else if (InstCfg.matchAttr (instInfo, PROJECTION_TYPES_TAG)) {
		    PROJECTION_TYPES = instInfo.getValueAsArray();
		}
		else if (InstCfg.matchAttr (instInfo, GRID_FUNCTION_TYPES_TAG)) {
		    GRID_FUNCTION_TYPES = instInfo.getValueAsArray();
		}
		else if ( InstCfg.matchAttr ( instInfo, "DEFAULT_BASELINE_FRACTION" ) ) {
		    DEFAULT_BASELINE = Double.parseDouble(instInfo.getValue());
		}
		else if ( InstCfg.matchAttr (instInfo, ATTR_POLYNOMIAL_ORDER) ) {
		    POLYNOMIALS = instInfo.getValueAsArray();
		}
	    }
	}catch (IOException e) {
	    System.out.println ("Error reading DRRECIPE cfg file");
	}
    }
    
    /**
     * Override getTitle to return the title attribute.
     */
    public String
	getTitle()
    {
	String title     = type().getReadable();
	String titleAttr = getTitleAttr();
	if ((titleAttr != null) && !(titleAttr.equals(""))) {
	    title = title + ": " + titleAttr;
	}
	return title;
    }
    
    
    /**
     * Set the DR recipe name.
     */
    public void
	setObjectRecipeName(String text)
    {
	_avTable.set(ATTR_OBJECT_RECIPE, text);
    }
    
    /**
     * Get the DR recipe name.
     */
    public String
	getObjectRecipeName()
    {
	String recipe = _avTable.get(ATTR_OBJECT_RECIPE);
	return recipe;
    }
    
    
    /**
     * Get the DR recipe name. Retained for compatibility
     */
    public String
	getRecipeName()
    {
	String recipe = _avTable.get(ATTR_OBJECT_RECIPE);
	return recipe;
    }
    
    /**
     * Get the default recipe (METHOD  REQUIRED...)
     */
    public String
	getDefaultRecipe()
    {
	// MFO TODO: How about heterodyne?
	return OBJECT_RECIPE_DEFAULT;
    }
    
    
    /**
     * Use default recipe
     */
    public void
	useDefaults(String instName)
    {
	if (instName.equalsIgnoreCase("heterodyne")) {
	    _avTable.noNotifySet(ATTR_OBJECT_RECIPE, HETERODYNE_OBJECT_RECIPE_DEFAULT, 0);
	    
	    setTitleAttr(HETERODYNE_OBJECT_RECIPE_DEFAULT);

        }
	else if (instName.equalsIgnoreCase("scuba")) {
	    _avTable.noNotifySet(ATTR_OBJECT_RECIPE, SCUBA_OBJECT_RECIPE_DEFAULT, 0);
	    
	    setTitleAttr(SCUBA_OBJECT_RECIPE_DEFAULT);

        }
    }


  public String getPolynomialOrder() {
      if ( !_avTable.exists(ATTR_POLYNOMIAL_ORDER) ) {
	  setPolynomialOrder( POLYNOMIALS[0] );
      }
      return _avTable.get(ATTR_POLYNOMIAL_ORDER);
  }

  public void setPolynomialOrder(String value) {
    _avTable.set(ATTR_POLYNOMIAL_ORDER, value);
  }


  public int getTruncationChannels() {
    return _avTable.getInt(ATTR_TRUNCATION_CHANNELS, 0);
  }

  public void setTruncationChannels(int value) {
    _avTable.set(ATTR_TRUNCATION_CHANNELS, value);
  }

  public void setTruncationChannels(String value) {
    setTruncationChannels(Format.toInt(value));
  }


  public String getBaselineFitting() {
      if ( !_avTable.exists(ATTR_BASELINE_FITTING) ) {
	  return BASELINE_SELECTION[1]; // 
      }
      return _avTable.get(ATTR_BASELINE_FITTING);
  }

  public void setBaselineFitting(String value) {
    _avTable.set(ATTR_BASELINE_FITTING, value);
  }


  public double getBaselineRegionMin(int index) {
    return _avTable.getDouble(ATTR_BASELINE_REGION_MIN, index, 0.0);
  }

  public void setBaselineRegionMin(double value, int index) {
    _avTable.set(ATTR_BASELINE_REGION_MIN, value, index);
  }

  public void setBaselineRegionMin(String value, int index) {
    setBaselineRegionMin(Format.toDouble(value), index);
  }


  public double getBaselineRegionMax(int index) {
    return _avTable.getDouble(ATTR_BASELINE_REGION_MAX, index, 0.0);
  }

  public void setBaselineRegionMax(double value, int index) {
    _avTable.set(ATTR_BASELINE_REGION_MAX, value, index);
  }

  public void setBaselineRegionMax(String value, int index) {
    setBaselineRegionMax(Format.toDouble(value), index);
  }

  public void setBaselineRegions(String value) {
    _avTable.noNotifyRm(ATTR_BASELINE_REGION_MIN);
    _avTable.noNotifyRm(ATTR_BASELINE_REGION_MAX);

    StringTokenizer textTokenizer = new StringTokenizer(value, "\n");
    StringTokenizer lineTokenizer = null;
    String token = null;

    int i = 0;
    while(textTokenizer.hasMoreTokens()) {
      lineTokenizer = new StringTokenizer(textTokenizer.nextToken(), ",; \t");

      token = "0.0";
      if(lineTokenizer.hasMoreTokens()) {
        token = lineTokenizer.nextToken();
      }
      setBaselineRegionMin(token, i);

      token = "0.0";
      if(lineTokenizer.hasMoreTokens()) {
        token = lineTokenizer.nextToken();
      }
      setBaselineRegionMax(token, i);
    
      i++;
    }
  }


  public void removeAllBaselineRegions() {
    _avTable.rm(ATTR_BASELINE_REGION_MIN);
    _avTable.rm(ATTR_BASELINE_REGION_MAX);
  }

  public void setBaselineFraction( double value ) {
      _avTable.set(ATTR_AUTO_BASELINE_REGION, value);
  }

  public void setBaselineFraction( String value ) {
      double val = DEFAULT_BASELINE;
      try {
	  val = Double.parseDouble( value );
      }
      catch (NumberFormatException e) {}
      setBaselineFraction(val);
  }

  public double getBaselineFraction() {
      if ( !_avTable.exists(ATTR_AUTO_BASELINE_REGION) ) {
	  setBaselineFraction(DEFAULT_BASELINE);
      }
      return _avTable.getDouble(ATTR_AUTO_BASELINE_REGION, DEFAULT_BASELINE);
  }


  public int getNumBaselineRegions() {
    return _avTable.size(ATTR_BASELINE_REGION_MIN);
  }


  public String getBaselineRegionUnits() {
    return _avTable.get(ATTR_BASELINE_REGION_UNITS);
  }

  public void setBaselineRegionUnits(String value) {
    _avTable.set(ATTR_BASELINE_REGION_UNITS, value);
  }


  public double getLineRegionMin(int index) {
    return _avTable.getDouble(ATTR_LINE_REGION_MIN, index, 0.0);
  }

  public void setLineRegionMin(double value, int index) {
    _avTable.set(ATTR_LINE_REGION_MIN, value, index);
  }

  public void setLineRegionMin(String value, int index) {
    setLineRegionMin(Format.toDouble(value), index);
  }


  public double getLineRegionMax(int index) {
    return _avTable.getDouble(ATTR_LINE_REGION_MAX, index, 0.0);
  }

  public void setLineRegionMax(double value, int index) {
    _avTable.set(ATTR_LINE_REGION_MAX, value, index);
  }

  public void setLineRegionMax(String value, int index) {
    setLineRegionMax(Format.toDouble(value), index);
  }

  public void setLineRegions(String value) {
    _avTable.noNotifyRm(ATTR_LINE_REGION_MIN);
    _avTable.noNotifyRm(ATTR_LINE_REGION_MAX);

    StringTokenizer textTokenizer = new StringTokenizer(value, "\n");
    StringTokenizer lineTokenizer = null;
    String token = null;

    int i = 0;
    while(textTokenizer.hasMoreTokens()) {
      lineTokenizer = new StringTokenizer(textTokenizer.nextToken(), ",; \t");
      token = "0.0";
      if(lineTokenizer.hasMoreTokens()) {
        token = lineTokenizer.nextToken();
      }
      setLineRegionMin(token, i);

      token = "0.0";
      if(lineTokenizer.hasMoreTokens()) {
        token = lineTokenizer.nextToken();
      }
      setLineRegionMax(token, i);
    
      i++;
    }
  }


  public void removeAllLineRegions() {
    _avTable.rm(ATTR_LINE_REGION_MIN);
    _avTable.rm(ATTR_LINE_REGION_MAX);
  }


  public int getNumLineRegions() {
    return _avTable.size(ATTR_LINE_REGION_MIN);
  }


  public int getChannelBinning() {
    return _avTable.getInt(ATTR_CHANNEL_BINNING, 0);
  }

  public void setChannelBinning(int value) {
    _avTable.set(ATTR_CHANNEL_BINNING, value);
  }

  public void setChannelBinning(String value) {
    setChannelBinning(Format.toInt(value));
  }


  public String getRegriddingMethod() {
    return _avTable.get(ATTR_REGRIDDING_METHOD);
  }

  public void setRegriddingMethod(String value) {
    _avTable.set(ATTR_REGRIDDING_METHOD, value);
  }


  public String getWindowType() {
    return _avTable.get(ATTR_WINDOW_TYPE);
  }

  public void setWindowType(String value) {
    _avTable.set(ATTR_WINDOW_TYPE, value);
  }


  /*
  public String getProjection(int cubeIndex) {
    return _avTable.get(ATTR_PROJECTION, cubeIndex);
  }

  public void setProjection(String value, int cubeIndex) {
    _avTable.set(ATTR_PROJECTION, value, cubeIndex);
  }
  */


  public String getGridFunction(int cubeIndex) {
    return _avTable.get(ATTR_GRID_FUNCTION, cubeIndex);
  }

  public void setGridFunction(String value, int cubeIndex) {
    _avTable.set(ATTR_GRID_FUNCTION, value, cubeIndex);
  }


  public String getSmootingRad(int cubeIndex) {
    return _avTable.get(ATTR_SMOOTHING_RAD, cubeIndex);
  }

  public void setSmoothingRad(double value, int cubeIndex) {
    _avTable.set(ATTR_SMOOTHING_RAD, value, cubeIndex);
  }

  public void setSmoothingRad(String value, int cubeIndex) {
    setSmoothingRad(Format.toDouble(value), cubeIndex);
  }


  public int getPixelSizeX(int cubeIndex) {
    return _avTable.getInt(ATTR_PIXEL_SIZE_X, cubeIndex, 0);
  }

  public void setPixelSizeX(int value, int cubeIndex) {
    _avTable.set(ATTR_PIXEL_SIZE_X, value, cubeIndex);
  }

  public void setPixelSizeX(String value, int cubeIndex) {
    setPixelSizeX(Format.toInt(value), cubeIndex);
  }


  public int getPixelSizeY(int cubeIndex) {
    return _avTable.getInt(ATTR_PIXEL_SIZE_Y, cubeIndex, 0);
  }

  public void setPixelSizeY(int value, int cubeIndex) {
    _avTable.set(ATTR_PIXEL_SIZE_Y, value, cubeIndex);
  }

  public void setPixelSizeY(String value, int cubeIndex) {
    setPixelSizeY(Format.toInt(value), cubeIndex);
  }


  public int getOffsetX(int cubeIndex) {
    return _avTable.getInt(ATTR_OFFSET_X, cubeIndex, 0);
  }

  public void setOffsetX(int value, int cubeIndex) {
    _avTable.set(ATTR_OFFSET_X, value, cubeIndex);
  }

  public void setOffsetX(String value, int cubeIndex) {
    setOffsetX(Format.toInt(value), cubeIndex);
  }


  public int getOffsetY(int cubeIndex) {
    return _avTable.getInt(ATTR_OFFSET_Y, cubeIndex, 0);
  }

  public void setOffsetY(int value, int cubeIndex) {
    _avTable.set(ATTR_OFFSET_Y, value, cubeIndex);
  }

  public void setOffsetY(String value, int cubeIndex) {
    setOffsetY(Format.toInt(value), cubeIndex);
  }


  /**
   * Returns the number of data cubes.
   */
  public int getNumDataCubes() {
    return _avTable.size(ATTR_PIXEL_SIZE_X);
  }

  /**
   * Creates &lt;baseline_fit&gt; element for ACSIS/OCS XML.
   *
   * This method is used by the temporary ACSIS translator
   * and might become obsolete once the final ACSIS translator is in place.
   */
  public String get_baseline_fit(String indent) {

    if(getBaselineFitting().equals(BASELINE_SELECTION[2]) ) {
      StringBuffer result = new StringBuffer();

      result.append(indent + "<baseline_fit>\n");

      for(int i = 0; i < getNumLineRegions(); i++) {
        result.append(indent + "  <line_region>\n");
        result.append(indent + "    <range units=\"" + getBaselineRegionUnits() + "\">\n");
        result.append(indent + "      <min>" + getLineRegionMin(i) + "</min>\n");
        result.append(indent + "      <max>" + getLineRegionMax(i) + "</max>\n");
        result.append(indent + "    </range>\n");
        result.append(indent + "  </line_region>\n");
      }

      result.append(indent + "  <fit_polynomial degree=\"" + getPolynomialOrder() + "\"/>\n");

      for(int i = 0; i < getNumBaselineRegions(); i++) {
        result.append(indent + "  <fit_region>\n");
        result.append(indent + "    <range units=\"" + getBaselineRegionUnits() + "\">\n");
        result.append(indent + "      <min>" + getBaselineRegionMin(i) + "</min>\n");
        result.append(indent + "      <max>" + getBaselineRegionMax(i) + "</max>\n");
        result.append(indent + "    </range>\n");
        result.append(indent + "  </fit_region>\n");
      }

      result.append(indent + "</baseline_fit>\n");

      return result.toString();
    }
    return "";
  }

  /**
   * Creates &lt;ms_truncation&gt; element for ACSIS/OCS XML.
   *
   * This method is used by the temporary ACSIS translator
   * and might become obsolete once the final ACSIS translator is in place.
   */
  public String get_ms_truncation(String indent) {
    StringBuffer result = new StringBuffer();

    result.append(indent + "<ms_truncation>\n");
    result.append(indent + "  Currently not used. ???\n");
//    result.append(indent + "  <range units=\"km.s-1\">\n");
//    result.append(indent + "    <min>???</min>\n");
//    result.append(indent + "    <max>???</max>\n");
//    result.append(indent + "  </range>\n");
    result.append(indent + "</ms_truncation>\n");

    return result.toString();
  }

  /**
   * Creates &lt;cubet&gt; element for ACSIS/OCS XML.
   *
   * This method is used by the temporary ACSIS translator
   * and might become obsolete once the final ACSIS translator is in place.
   */
  public String get_cube(String indent, SpTelescopePos groupCentre, double mapWidth, double mapHeight, int cubeIndex) {
    String xAxis       = "";
    String yAxis       = "";
    String coordSystem = "";

    if(groupCentre != null) {
      xAxis       = groupCentre.getXaxisAsString();
      yAxis       = groupCentre.getYaxisAsString();
      coordSystem = CoordSys.getSystem(groupCentre.getCoordSys());
    }

    return
      indent + "<cube id=\"CUBE" + (cubeIndex + 1) + "\">\n" +
      indent + "  <group_centre>\n" +
      indent + "    <spherSystem SYSTEM=\"" + coordSystem + "\">\n" +
      indent + "      <c1>" + xAxis + "</c1>\n" +
      indent + "      <c2>" + yAxis + "</c2>\n" +
      indent + "    </spherSystem>\n" +
      indent + "  </group_centre>\n" +
//       indent + "  <x_pix_size units=\"arcsec\">" + getPixelSizeX(cubeIndex) + "</x_pix_size>\n" +
//       indent + "  <y_pix_size units=\"arcsec\">" + getPixelSizeY(cubeIndex) + "</y_pix_size>\n" +
      indent + "  <data_source>\n" +
      indent + "    <spw_ref ref=\"SPW" + (cubeIndex + 1) + "\"/>\n" +
      indent + "    <range units=\"???\">\n" +
      indent + "      <min > ??? </min>\n" +
      indent + "      <max > ??? </max>\n" +
      indent + "    </range>\n" +
      indent + "  </data_source>\n" +
//       indent + "  <x_offset>" + getOffsetX(cubeIndex) + "</x_offset>\n" +
//       indent + "  <y_offset>" + getOffsetY(cubeIndex) + "</y_offset>\n" +
//       indent + "  <!-- ??? x_npix calculation is currently only implemented for scan maps -->\n" +
//       indent + "  <x_npix>" + (mapWidth  / getPixelSizeX(cubeIndex)) + "</x_npix>\n" +
//       indent + "  <!-- ??? y_npix calculation is currently only implemented for scan maps -->\n" +
//       indent + "  <y_npix>" + (mapHeight / getPixelSizeY(cubeIndex)) + "</y_npix>\n" +
//       indent + "  <projection type=\"" + getProjection(cubeIndex) + "\"/>\n" +
//       indent + "  <grid_function type=\"" + getGridFunction(cubeIndex) + "\"/>\n" +
      indent + "  <tcs_coord type=\"TRACKING\"/>\n" +
//       indent + "  <smoothing_rad>" + getPixelSizeX(cubeIndex) + "</smoothing_rad>\n" +
      indent + "</cube>\n";
  }

  /**
   * Creates &lt;cube_list&gt; element for ACSIS/OCS XML.
   *
   * This method is used by the temporary ACSIS translator
   * and might become obsolete once the final ACSIS translator is in place.<p>
   *
   * Note that STRING in <tt>&lt;cube id="STRING"&gt;</tt>     is set to "CUBE" + (cubeIndex + 1),
   * i.e. CUBE1, CUBE2, ...<br>
   * and  that STRING in <tt>&lt;spw_ref ref="STRING"&gt;</tt> is set to "SPW"  + (cubeIndex + 1),
   * i.e. SPW1, SPW2, ...
   *
   */
  public String get_cube_list(SpTelescopePos groupCentre, double mapWidth, double mapHeight, String indent) {
    StringBuffer result = new StringBuffer();

    result.append(indent + "<cube_list>\n");

    for(int i = 0; i < getNumDataCubes(); i++) {
      result.append(get_cube(indent + "  ", groupCentre, mapWidth, mapHeight, i));
    }

    result.append(indent + "</cube_list>\n");

    return result.toString();
  }

  /**
    * Writes out the XML for the baseline regions of acsis.
    */
  protected void toXML(String indent, StringBuffer buffer) {
      StringBuffer baselineXML = new StringBuffer();
      
      baselineXML.append(indent + "\n\n" + indent + "<!-- **************************** -->\n");
      baselineXML.append(indent +     "<!-- Baseline selection for ACSIS -->\n");
      baselineXML.append(indent +     "<!-- **************************** -->\n");
      baselineXML.append(indent + "<baseline>\n");
      baselineXML.append(indent + "  <" + ATTR_BASELINE_FITTING + ">" +
	      getBaselineFitting() +
	      "</" + ATTR_BASELINE_FITTING + ">\n");
      if ( getBaselineFitting().equals(BASELINE_SELECTION[1]) ) {
	  // Automatic fitting
	  baselineXML.append(indent + "  <" + ATTR_AUTO_BASELINE_REGION + ">" +
		  getBaselineFraction() +
		  "</" + ATTR_AUTO_BASELINE_REGION + ">\n");
      }
      else if ( getBaselineFitting().equals(BASELINE_SELECTION[2]) ) {
	  // Manual Fitting
	  for(int i = 0; i < getNumBaselineRegions(); i++) {
	      baselineXML.append(indent + "  <fit_region>\n");
	      baselineXML.append(indent + "    <range units=\"" + getBaselineRegionUnits() + "\">\n");
	      baselineXML.append(indent + "      <min>" + getBaselineRegionMin(i) + "</min>\n");
	      baselineXML.append(indent + "      <max>" + getBaselineRegionMax(i) + "</max>\n");
	      baselineXML.append(indent + "    </range>\n");
	      baselineXML.append(indent + "  </fit_region>\n");
	  }
      }
      else {
	  // Assume no line fitting
      }
      baselineXML.append(indent + "</baseline>\n");
      // Delete the things we don't want to get written out using the standard method,
      // and then add them again after writing the baseline
      Vector method = _avTable.getAll(ATTR_BASELINE_FITTING);
      _avTable.rm(ATTR_BASELINE_FITTING);
      Vector regionMax = _avTable.getAll(ATTR_BASELINE_REGION_MAX);
      _avTable.rm(ATTR_BASELINE_REGION_MAX);
      Vector regionMin = _avTable.getAll(ATTR_BASELINE_REGION_MIN);
      _avTable.rm(ATTR_BASELINE_REGION_MIN);
      Vector fraction = _avTable.getAll(ATTR_AUTO_BASELINE_REGION);
      _avTable.rm(ATTR_AUTO_BASELINE_REGION);
      Vector units = _avTable.getAll(ATTR_BASELINE_REGION_UNITS);
      _avTable.rm(ATTR_BASELINE_REGION_UNITS);

      super.toXML(indent, buffer);

      if (method != null && method.size() > 0 ) _avTable.noNotifySetAll(ATTR_BASELINE_FITTING, method);
      if (regionMax != null && regionMax.size() > 0 ) _avTable.noNotifySetAll(ATTR_BASELINE_REGION_MAX, regionMax);
      if (regionMin != null && regionMin.size() > 0 ) _avTable.noNotifySetAll(ATTR_BASELINE_REGION_MIN, regionMin);
      if (fraction != null && fraction.size() > 0 ) _avTable.noNotifySetAll(ATTR_AUTO_BASELINE_REGION, fraction);
      if (units != null && units.size() > 0 ) _avTable.noNotifySetAll(ATTR_BASELINE_REGION_UNITS, units);

      int offset = buffer.length() - (indent.length() + _className.length() + 4 );
      buffer.insert(offset, baselineXML.toString());

  }

  public void processXmlElementStart(String name) {
      if ( name.equals("baseline") ) {
	  _nRegions = 0;
	  return;
      }

      if ( name.equals("fit_region") ) {
	  _readingRegion = true;
	  return;
      }

      if ( name.equals("range") && _readingRegion ) {
	  return;
      }

      super.processXmlElementStart(name);
  }

  public void processXmlElementEnd(String name) {
      if ( name.equals("fit_region") ) {
	  System.out.println("Incrementing number of regions");
	  _nRegions++;
	  _readingRegion = false;
	  return;
      }
      if ( name.equals("baseline") ) {
	  _nRegions = 0;
	  return;
      }
      super.processXmlElementEnd(name);
  }

  public void processXmlAttribute( String element,
	  String attribute, String value) {
      if ( element.equals("range") && attribute.equals("units") ) {
	 setBaselineRegionUnits(value);
	 return;
      }
      super.processXmlAttribute(element, attribute, value);
  }

  public void processXmlElementContent( String name, String value ) {
      if (_readingRegion) {
	  if ( name.equals("max") ) {
	      System.out.println("Setting max for region "+_nRegions);
	      setBaselineRegionMax( value, _nRegions );
	      return;
	  }
	  if ( name.equals("min") ) {
	      System.out.println("Setting min for region "+_nRegions);
	      setBaselineRegionMin( value, _nRegions );
	      return;
	  }
      }
      super.processXmlElementContent(name, value);
  }

}
