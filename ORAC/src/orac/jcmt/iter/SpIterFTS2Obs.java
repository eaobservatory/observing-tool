package orac.jcmt.iter ;

import orac.jcmt.util.Scuba2Noise ;
import orac.jcmt.util.Scuba2Time ;
import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.util.MathUtil ;

@SuppressWarnings( "serial" )
public class SpIterFTS2Obs extends SpIterJCMTObs
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "FTS2Obs" , "FTS-2" ) ;
	public static String[] JIGGLE_PATTERNS = {} ;
	public static final String SED = "SED" ;
	public static final String SPECTRAL_LINE = "Spectral Line" ;
	public static final String SPECTRAL_FLAT_FIELD = "Spectral Flatfield" ;
	public static final String ZPD = "ZPD" ;
	public static final String VARIABLE_MODE = "Variable Mode" ;
	public static String[] SPECIAL_MODES = { SED , SPECTRAL_LINE , SPECTRAL_FLAT_FIELD , ZPD , VARIABLE_MODE } ;

	public static final String SPECIAL_MODE = "SpecialMode" ;
	public static final String TRACKING_PORT = "TrackingPort" ;
	public static final String IS_DUAL_PORT = "isDualPort" ;

        public static final String PORT_8D = "8D";
        public static final String PORT_8C = "8C";

	public static final String FOV = "FOV" ;
	public static final String SCAN_SPEED = "ScanSpeed" ;
	public static final String SENSITIVITY = "Sensitivity" ;
	public static final String RESOLUTION = "resolution" ;

	// Approximate light speed
	private final static double c = 3 * Math.pow( 10 , 8 ) ;

	public final static double FOVScale = 100. ;
	public final static double minimumFOV = .44 ;
	public final static int minimumFOVScaled = ( int )Math.rint( minimumFOV * FOVScale ) ;
	public final static double maximumFOV = 7.362 ;
	public final static int maximumFOVScaled = ( int )Math.rint( maximumFOV * FOVScale ) ;

	public final static double resolutionScale = 1000. ;
	public final static double minimumResolution = .006 ;
	public final static int minimumResolutionScaled = ( int )Math.rint( minimumResolution * resolutionScale ) ;
	public final static double maximumResolution = .1 ;
	public final static int maximumResolutionScaled = ( int )Math.rint( maximumResolution * resolutionScale ) ;

	public final static double speedScale = 100. ;
	public final static double minimumSpeed = .4 ;
	public final static int minimumSpeedScaled = ( int )Math.rint( minimumSpeed * speedScale ) ;
	public final static double maximumSpeed = 8. ;
	public final static int maximumSpeedScaled = ( int )Math.rint( maximumSpeed * speedScale ) ;
	public final static double speedIncrement = .1 ;
	public final static int speedIncrementScaled = ( int )Math.rint( speedIncrement * speedScale ) ;

	public Scuba2Time s2time = new Scuba2Time() ;

	//	Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterFTS2Obs() ) ;
	}
	
	public SpIterFTS2Obs()
	{
		super( SP_TYPE ) ;
	}

	public String getTitle()
	{
		if( getTitleAttr() != null )
			return super.getTitle() ;

		return getSpecialMode() + " (" + getCount() + "X)" ;
	}

    public String getSpecialMode()
    {
    	if( !_avTable.exists( SPECIAL_MODE ) )
    		_avTable.set( SPECIAL_MODE , SPECIAL_MODES[ 0 ] ) ;
    	return _avTable.get( SPECIAL_MODE ) ;
    }

    public void setSpecialMode( String mode )
    {
    	for( String current : SPECIAL_MODES )
    	{
    		if( current.equals( mode ) )
    		{
    			_avTable.set( SPECIAL_MODE , mode ) ;
    			break ;
    		}
    	}
    }

    public String getTrackingPort()
    {
    	if( !_avTable.exists( TRACKING_PORT ) )
			_avTable.set( TRACKING_PORT , PORT_8D ) ;
		return _avTable.get( TRACKING_PORT ) ;
    }

    public void setTrackingPort( String port )
    {
    	if( PORT_8D.equals(port) || PORT_8C.equals(port) )
    		_avTable.set( TRACKING_PORT , port ) ;
    }

    public boolean isDualPort()
    {
    	if( !_avTable.exists( IS_DUAL_PORT ) )
    		_avTable.set( IS_DUAL_PORT , true ) ;
    	return _avTable.getBool( IS_DUAL_PORT ) ;
    }

    public void setIsDualPort( boolean dualPort )
    {
    	_avTable.set( IS_DUAL_PORT , dualPort ) ;
    }

    public double getFOV()
    {
    	double resolution = getResolution() ;

    	resolution = Math.pow( resolution , 1.83 ) ;
    	double FOV = 1871.74 * resolution ;
    	FOV = FOV < minimumFOV ? minimumFOV :FOV ;

    	return FOV > maximumFOV ? maximumFOV : FOV ;
    }

    public void setResolution( double resolution )
    {
    	_avTable.set( RESOLUTION , resolution ) ;
    }

	public double getResolution()
	{
		return _avTable.getDouble( RESOLUTION , minimumResolution ) ;
	}

	public double getResolutionInHz()
	{
		double resolution = getResolution() ;

		double wavelength = ( ( 1. / resolution ) * 1000000. ) / 100. ;
		double micrometres = wavelength * Math.pow( 10 , -6 ) ;
		double frequency = c / micrometres ;

		return frequency ;
	}

	public double getResolutionInMHz()
	{
		return getResolutionInHz() * Math.pow( 10 , -6 ) ;
	}

	public double getResolutionInGHz()
	{
		return getResolutionInHz() * Math.pow( 10 , -9 ) ;
	}

    public double getScanSpeed()
    {
    	double scanSpeed = _avTable.getDouble( SCAN_SPEED , 1.0 ) ;
    	scanSpeed = MathUtil.round( scanSpeed / 2.5 , 5 ) ;
    	return scanSpeed ;
    }

	public void setScanSpeed( double scanSpeed )
	{
		scanSpeed = MathUtil.round( scanSpeed * 2.5 , 5 ) ;
		_avTable.set( SCAN_SPEED , scanSpeed ) ;
	}

    public double getNyquist()
    {
    	return 250. / getScanSpeed() ;
    }

    public double getSensitivity450()
    {
    	return getSensitivity( Scuba2Noise.four50 ) ;
    }

    public double getSensitivity850()
    {
    	return getSensitivity( Scuba2Noise.eight50 ) ;
    }

    public double getSensitivity( String wavelength )
    {
    	double sensitivity = 0. ;
    	double integrationTime = this.getSampleTime() ;
    	sensitivity = s2time.ftsSensitivity( this , integrationTime , wavelength ) ;
    	return sensitivity ;
    }

	public double getElapsedTime()
	{
		double time = SCUBA2_STARTUP_TIME ;

		time = getSampleTime() ;

		return time ;
	}

	public void setupForSCUBA2()
	{
		super.setupForSCUBA2() ;
		setSampleTime( String.valueOf( getSampleTime() ) ) ;
	}
}
