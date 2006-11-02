/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.jcmt.inst;

import java.io.IOException ;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

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

    // The values of the following String variables were
    // taken from the corresponding XML elements in <cube_list>
    // when possible.
    // However, the variable names and method names in which the variable
    // are more in accordance with naming conventions used elsewhere in the OT.
    public static final String ATTR_WINDOW_TYPE           = "window_type";

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
		super( SP_TYPE );

		// Read in the config file
		String baseDir = System.getProperty( "ot.cfgdir" );
		String cfgFile = baseDir + "drrecipe.cfg";
		_readCfgFile( cfgFile );

		_avTable.noNotifySet( ATTR_WINDOW_TYPE , WINDOW_TYPES[ 0 ] , 0 );
	}
    
    private void _readCfgFile( String filename )
	{

		InstCfgReader instCfg = null;
		InstCfg instInfo = null;
		String block = null;

		instCfg = new InstCfgReader( filename );
		try
		{
			while( ( block = instCfg.readBlock() ) != null )
			{
				instInfo = new InstCfg( block );
				if( InstCfg.matchAttr( instInfo , "heterodyne" ) )
					HETERODYNE = instInfo.getValueAsLUT();
				else if( InstCfg.matchAttr( instInfo , "scuba" ) )
					SCUBA = instInfo.getValueAsLUT();
				else if( InstCfg.matchAttr( instInfo , WINDOW_TYPES_TAG ) )
					WINDOW_TYPES = instInfo.getValueAsArray();
				else if( InstCfg.matchAttr( instInfo , PROJECTION_TYPES_TAG ) )
					PROJECTION_TYPES = instInfo.getValueAsArray();
				else if( InstCfg.matchAttr( instInfo , GRID_FUNCTION_TYPES_TAG ) )
					GRID_FUNCTION_TYPES = instInfo.getValueAsArray();
				else if( InstCfg.matchAttr( instInfo , "DEFAULT_BASELINE_FRACTION" ) )
					DEFAULT_BASELINE = Double.parseDouble( instInfo.getValue() );
			}
		}
		catch( IOException e )
		{
			System.out.println( "Error reading DRRECIPE cfg file" );
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

  public String getWindowType() {
    return _avTable.get(ATTR_WINDOW_TYPE);
  }

  public void setWindowType(String value) {
    _avTable.set(ATTR_WINDOW_TYPE, value);
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

}
