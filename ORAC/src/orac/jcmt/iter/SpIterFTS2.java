package orac.jcmt.iter ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.util.MathUtil ;

public class SpIterFTS2 extends SpIterJCMTObs
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

	public static final String FOV = "FOV" ;
	public static final String SCAN_SPEED = "ScanSpeed" ;

	//	Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterFTS2() ) ;
	}
	
	public SpIterFTS2()
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

    public int getTrackingPort()
    {
	if( !_avTable.exists( TRACKING_PORT ) )
		_avTable.set( TRACKING_PORT , 1 ) ;
	return _avTable.getInt( TRACKING_PORT , 1 ) ;
    }

    public void setTrackingPort( int port )
    {
	if( port == 1 || port == 2 )
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
	return _avTable.getDouble( FOV , 4.39 ) ;
    }

    public void setFOV( double fov )
    {
	_avTable.set( FOV , fov ) ;
    }

	public double getResolution()
	{
		double FOV = getFOV() ;
		FOV *= FOV ;
		return 0.02425 * Math.sqrt( FOV / Math.PI ) ;
	}

    public double getScanSpeed()
    {
	double scanSpeed = _avTable.getDouble( SCAN_SPEED , 0.4 ) ;
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
}
