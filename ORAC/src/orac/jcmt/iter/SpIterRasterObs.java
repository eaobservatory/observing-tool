/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package orac.jcmt.iter ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpPosAngleObserver ;
import gemini.sp.iter.SpIterOffset ;
import gemini.sp.obsComp.SpInstObsComp ;
import gemini.util.Format ;
import orac.jcmt.inst.SpJCMTInstObsComp ;
import orac.jcmt.inst.SpInstSCUBA2 ;
import orac.jcmt.inst.SpInstHeterodyne ;
import orac.util.SpMapItem ;

import orac.jcmt.SpJCMTConstants ;
import orac.jcmt.util.Scuba2Time ;

import java.util.Vector ;
import java.util.StringTokenizer ;

import java.math.MathContext ;
import java.math.BigDecimal ;

/**
 * Raster Iterator for ACSIS/JCMT.
 *
 * The Raster iterator (ACSIS) and the Scan iterator share a lot of functionality
 * and should in future be either made the same class or share code by other
 * means such as inheritance.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class SpIterRasterObs extends SpIterJCMTObs implements SpPosAngleObserver , SpMapItem , SpJCMTConstants
{

	/** TCS XML constants. */
	private static final String TX_OBS_AREA = "obsArea" ;
	private static final String TX_SCAN_AREA = "SCAN_AREA" ;
	private static final String TX_AREA = "AREA" ;
	private static final String TX_SCAN = "SCAN" ;
	private static final String TX_PA = "PA" ;
	private static final String TX_HEIGHT = "HEIGHT" ;
	private static final String TX_WIDTH = "WIDTH" ;
	private static final String TX_SCAN_VELOCITY = "VELOCITY" ;
	private static final String TX_SCAN_SYSTEM = "SYSTEM" ;
	private static final String TX_SCAN_DY = "DY" ;

	/** Default values for rosw/ref and rows/cal */
	private final String ROWS_PER_REF_DEFAULT = "1" ;
	private final String ROWS_PER_CAL_DEFAULT = "1" ;

	/** Needed for XML parsing. */
	private String _xmlPaAncestor ;
	
	public static final double HARP_SAMPLE = 7.2761 ;
	public static final double HARP_FULL_ARRAY = 116.4171 ;
	
	private Scuba2Time s2time ;

	public static final MathContext context = new MathContext( 13 ) ;

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "rasterObs" , "Scan" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterRasterObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterRasterObs()
	{
		super( SP_TYPE ) ;

		_avTable.noNotifySet( ATTR_SCANAREA_WIDTH , "180.0" , 0 ) ;
		_avTable.noNotifySet( ATTR_SCANAREA_HEIGHT , "180.0" , 0 ) ;

		_avTable.noNotifySet( ATTR_SCANAREA_SCAN_SYSTEM , SCAN_SYSTEMS[ 0 ] , 0 ) ;
	}

	/** Get area width (map width). */
	public double getWidth()
	{
		return _avTable.getDouble( ATTR_SCANAREA_WIDTH , 0. ) ;
	}

	/** Set area width (map width). */
	public void setWidth( double width )
	{
		_avTable.set( ATTR_SCANAREA_WIDTH , Math.rint( width * 10. ) / 10. ) ;
	}

	/** Set area width (map width). */
	public void setWidth( String widthStr )
	{
		setWidth( Format.toDouble( widthStr ) ) ;
	}

	/** Get area height (map height). */
	public double getHeight()
	{
		return _avTable.getDouble( ATTR_SCANAREA_HEIGHT , 0. ) ;
	}

	/** Set area height (map height). */
	public void setHeight( double height )
	{
		_avTable.set( ATTR_SCANAREA_HEIGHT , Math.rint( height * 10. ) / 10. ) ;
	}

	/** Set area height (map height). */
	public void setHeight( String heightStr )
	{
		setHeight( Format.toDouble( heightStr ) ) ;
	}

        /** Get average size of map area from height and width */
        public double getAvgMapSize()
	{
            double height = getHeight();
            double width = getWidth();
            return (height + width) / 2.0;
	}

	/**
	 * Get area position angle (map position angle).
	 */
	public double getPosAngle()
	{
		return _avTable.getDouble( ATTR_SCANAREA_PA , 0. ) ;
	}

	/**
	 * Set area position angle (map position angle).
	 */
	public void setPosAngle( double theta )
	{
		theta %= 180. ;
		_avTable.set( ATTR_SCANAREA_PA , theta ) ;

		if( _parent instanceof SpIterOffset )
			( ( SpIterOffset )_parent ).setPosAngle( getPosAngle() ) ;
	}

	/**
	 * Set area position angle (map position angle).
	 */
	public void setPosAngle( String thetaStr )
	{
		setPosAngle( Format.toDouble( thetaStr ) ) ;
	}

	/**
	 * Get n<sup>th<sup>scan angle.
	 */
	public double getScanAngle( int n )
	{
		return _avTable.getDouble( ATTR_SCANAREA_SCAN_PA , n , 0. ) ;
	}

	/**
	 * Get scan angle.
	 */
	public Vector<String> getScanAngles()
	{
		return _avTable.getAll( ATTR_SCANAREA_SCAN_PA ) ;
	}

	/**
	 * Set n<sup>th<sup> scan angle.
	 */
	public void setScanAngle( double theta , int n )
	{
		theta %= 180. ;
		_avTable.set( ATTR_SCANAREA_SCAN_PA , theta , n ) ;
	}

	/**
	 * Set scan angle.
	 */
	public void setScanAngles( String thetaStr )
	{
		if( thetaStr == null )
		{
			_avTable.rm( ATTR_SCANAREA_SCAN_PA ) ;
		}
		else
		{
			StringTokenizer stringTokenizer = new StringTokenizer( thetaStr , ", ; " ) ;
			int i = 0 ;
			while( stringTokenizer.hasMoreTokens() )
			{
				setScanAngle( Format.toDouble( stringTokenizer.nextToken() ) , i ) ;
				i++ ;
			}
		}
	}

	/** Get scan velocity. */
	public double getScanVelocity()
	{
		SpInstObsComp inst = SpTreeMan.findInstrument( this ) ;
                // Calculate from average width/height. We always do this even if
                // there is a number cached.
		if ( inst != null && inst instanceof SpInstSCUBA2 )
		{
                    double scanVelocity = ( ( SpJCMTInstObsComp )inst ).getDefaultScanVelocity() ;
                    double avgSize = getAvgMapSize();
                    if (avgSize <= 600.0) {
                        // About 2 seconds of scanning
                        scanVelocity = avgSize / 2.0;
                    } else if (avgSize <= 1200.0) {
                        scanVelocity = 280.0;
                    } else if (avgSize <= 2200.0) {
                        scanVelocity = 400.0;
                    } else if (avgSize <= 4800.0) {
                        scanVelocity = 600.0;
                    } else {
                        // Fastest speed for biggest maps
                        scanVelocity = 600.0;
                    }
                    _avTable.noNotifySet( ATTR_SCANAREA_SCAN_VELOCITY , "" + scanVelocity , 0 ) ;
		}
		return _avTable.getDouble( ATTR_SCANAREA_SCAN_VELOCITY , 0. ) ;
	}

	/** Set scan velocity. */
	public boolean setScanVelocity( double value )
	{
		boolean valid = true ;
		if( value > 0. && value <= 600. )
			_avTable.set( ATTR_SCANAREA_SCAN_VELOCITY , value ) ;
		else 
			valid = false ;
		return valid ;
	}

	/** Set scan velocity. */
	public boolean setScanVelocity( String value )
	{
		return setScanVelocity( Format.toDouble( value ) ) ;
	}

	/**
	 * Get scan dx.
	 *
	 * Calculates scan dx in an instrument specific way.
	 *
	 * @throws java.lang.UnsupportedOperationException No instrument in scope.
	 */
	public double getScanDx() throws UnsupportedOperationException
	{
		SpInstObsComp inst = SpTreeMan.findInstrument( this ) ;
		if( inst == null )
		{
			throw new UnsupportedOperationException( "Could not find instrument in scope.\n" + "Needed for calculation of sample spacing." ) ;
		}
		else
		{
			double dx = 0. ;
			if( inst instanceof SpInstHeterodyne )
			{
				BigDecimal velocity = new BigDecimal( getScanVelocity() ) ;
				BigDecimal sampleTime = new BigDecimal( getSampleTime() ) ;
				BigDecimal deeEx = velocity.multiply( sampleTime , context ) ;
				dx = deeEx.doubleValue() ;
			}
			else if( inst instanceof SpInstSCUBA2 )
			{
				dx = getScanVelocity() / 200. ;
			}
			
			return dx ;
		}
	}

	/**
	 * Set scan dx.
	 *
	 * Sets scan in an instrument specific way.

	 * @throws java.lang.UnsupportedOperationException No instrument in scope.
	 */
	public boolean setScanDx( double dx ) throws UnsupportedOperationException
	{
		boolean valid = false ;
		SpInstObsComp inst = SpTreeMan.findInstrument( this ) ;
		if( inst == null )
			throw new UnsupportedOperationException( "Could not find instrument in scope.\n" + "Needed for calculation of scan velocity." ) ;
		else if( inst instanceof SpInstSCUBA2 )
			valid = setScanVelocity( dx * 200. ) ;
		else if( inst instanceof SpInstHeterodyne )
			valid = setScanVelocity( dx / getSampleTime() ) ;
		return valid ;
	}

	/**
	 * Set scan dx.
	 *
	 * Sets scan in an instrument specific way.
	 *
	 * @throws java.lang.UnsupportedOperationException No instrument in scope.
	 */
	public void setScanDx( String dx ) throws UnsupportedOperationException
	{
		setScanDx( Format.toDouble( dx ) ) ;
	}

	/** Get scan dy. */
	public double getScanDy()
	{
		SpInstObsComp inst = SpTreeMan.findInstrument( this ) ;
                // Calculate from average width/height. We always do this even if
                // there is a number cached.
		if( inst != null && inst instanceof SpInstSCUBA2 )
		{
                    double scanDy = ( ( SpJCMTInstObsComp )inst ).getDefaultScanDy() ;
                    double avgSize = getAvgMapSize();
                    if (avgSize <= 600.0) {
                        // About 2 seconds of scanning
                        scanDy = 30.0;
                    } else if (avgSize <= 1200.0) {
                        scanDy = 30.0;
                    } else if (avgSize <= 2200.0) {
                        scanDy = 60.0;
                    } else if (avgSize <= 4800.0) {
                        scanDy = 180.0;
                    } else {
                        // Largest maps
                        scanDy = 360.0;
                    }
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_DY , "" + scanDy , 0 ) ;
		}
		return _avTable.getDouble( ATTR_SCANAREA_SCAN_DY , 10. ) ;
	}

	/** Set scan dy. */
	public void setScanDy( double dy )
	{
		_avTable.set( ATTR_SCANAREA_SCAN_DY , dy ) ;
	}

	/** Set scan dy. */
	public void setScanDy( String dy )
	{
		_avTable.set( ATTR_SCANAREA_SCAN_DY , Format.toDouble( dy ) ) ;
	}

	/**
	 * Get Scan System.
	 *
	 * Refers to TCS XML:
	 * <pre>
	 * &lt ;SCAN_AREA&gt ;
	 *   &lt ;SCAN <b>SYSTEM="FPLANE"</b>&gt ;
	 *   &lt ;/SCAN&gt ;
	 * &lt ;SCAN_AREA&gt ;
	 * </pre>
	 */
	public String getScanSystem()
	{
		return _avTable.get( ATTR_SCANAREA_SCAN_SYSTEM ) ;
	}

	/**
	 * Set Scan System.
	 *
	 * Refers to TCS XML:
	 * <pre>
	 * &lt ;SCAN_AREA&gt ;
	 *   &lt ;SCAN <b>SYSTEM="FPLANE"</b>&gt ;
	 *   &lt ;/SCAN&gt ;
	 * &lt ;SCAN_AREA&gt ;
	 * </pre>
	 */
	public void setScanSystem( String system )
	{
		if( system == null )
			_avTable.rm( ATTR_SCANAREA_SCAN_SYSTEM ) ;
		else
			_avTable.set( ATTR_SCANAREA_SCAN_SYSTEM , system ) ;
	}

	public String getRasterMode()
	{
		return _avTable.get( ATTR_RASTER_MODE ) ;
	}

	public void setRasterMode( String value )
	{
		_avTable.set( ATTR_RASTER_MODE , value ) ;
	}

	public String getRowsPerCal()
	{
		if( !( _avTable.exists( ATTR_ROWS_PER_CAL ) ) )
			setRowsPerCal( ROWS_PER_CAL_DEFAULT ) ;

		return _avTable.get( ATTR_ROWS_PER_CAL ) ;
	}

	public void setRowsPerCal( String value )
	{
		_avTable.set( ATTR_ROWS_PER_CAL , value ) ;
	}

	public String getRowsPerRef()
	{
		return _avTable.get( ATTR_ROWS_PER_REF ) ;
	}

	public void setRowsPerRef( String value )
	{
		_avTable.set( ATTR_ROWS_PER_REF , value ) ;
	}

	public boolean getRowReversal()
	{
		return _avTable.getBool( ATTR_ROW_REVERSAL ) ;
	}

	public void setRowReversal( boolean value )
	{
		_avTable.set( ATTR_ROW_REVERSAL , value ) ;
	}

	public void setSampleTime( String value )
	{
		SpInstObsComp inst = SpTreeMan.findInstrument( this ) ;
		if( inst instanceof SpInstHeterodyne )
		{
			// leave the old value
			if( Format.toDouble( value ) > 0. )
				setScanVelocity( getScanDx() / Format.toDouble( value ) ) ;
			else return ;
		}
		super.setSampleTime( value ) ;
	}

	public void posAngleUpdate( double posAngle )
	{
		/*
		 * Do not use setPosAngle(posAngle) here as it would reset the posAngle of the class
		 * calling posAngleUpdate(posAngle) which would then call posAngleUpdate(posAngle)
		 * again an so on causing an infinite loop.
		 */
		_avTable.set( ATTR_SCANAREA_PA , posAngle ) ;
	}

	public double getSecsPerRow()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this ) ;
		if( instrument instanceof SpInstHeterodyne )
		{
			double rowOverhead = 17.14 ;
			double samplesPerRow = numberOfSamplesPerRow() ;

			double timeOnRow = samplesPerRow * getSampleTime() ;
			double timeOffRow = Math.sqrt( samplesPerRow ) * getSampleTime() ;
			return 1.05 * ( timeOnRow + timeOffRow + rowOverhead ) ;
		}
		return 0. ;
	}

	/**
	 * Convenience method.
	 * Gives the number of samples per row, which means the longest edge.
	 * @return
	 * @throws java.lang.UnsupportedOperationException No instrument in scope
	 */
	public double numberOfSamplesPerRow() throws UnsupportedOperationException
	{
		return numberOfSamplesOnSide( true ) ;
	}

	/**
	 * Convenience method.
	 * Gives the number of samples per column, which means the shortest edge.
	 * @return
	 * @throws java.lang.UnsupportedOperationException No instrument in scope
	 */
	public double numberOfSamplesPerColumn() throws UnsupportedOperationException
	{
		return numberOfSamplesOnSide( false ) ;
	}

	/**
	 * 
	 * @param row 
	 * 	true = number of samples per row,
	 *  false = number of samples per column
	 * @return
	 * @throws java.lang.UnsupportedOperationException No instrument in scope
	 */
	protected double numberOfSamplesOnSide( boolean row ) throws UnsupportedOperationException
	{
		double samplesPerRow = getWidth() ;
		double samplesPerColumn = getHeight() ;

		if( swap() )
		{
			double temp = samplesPerRow ;
			samplesPerRow = samplesPerColumn ;
			samplesPerColumn = temp ;
		}

		samplesPerRow = ( Math.floor( samplesPerRow / getScanDx() ) ) + 1. ;
		samplesPerColumn = ( Math.floor( samplesPerColumn / getScanDy() ) ) + 1. ;

		if( ( ( ( int )samplesPerRow ) & 1 ) == 0 )
			samplesPerRow++ ;

		SpInstObsComp instrument = SpTreeMan.findInstrument( this ) ;
		if( instrument == null )
		{
			throw new UnsupportedOperationException( "Could not find instrument in scope.\n" ) ;
		}
		else if( instrument instanceof SpInstHeterodyne )
		{
			String frontend = (( SpInstHeterodyne )instrument).getFrontEnd() ;
			if( "HARP".equals( frontend ) )
				samplesPerRow += 16 ;
		}
		
		if( row )
			return samplesPerRow ;
		else
			return samplesPerColumn ;
	}

	public boolean swap()
	{
		boolean swap = false ;

		double samplesPerRow = getWidth() ;
		double samplesPerColumn = getHeight() ;

		// if AUTOMATIC and height > width
		// else if USER DEF and abs( scan angle - map angle ) is < 45 or > 135
		boolean columnGreaterThanRow = samplesPerColumn > samplesPerRow ;
		double normalisedAngle = Math.abs( ( normalise( getScanAngle( 0 ) ) ) - ( normalise( getPosAngle() ) ) ) ;
		if( ( getScanAngles() == null ) || ( getScanAngles().size() == 0 ) )
			swap = columnGreaterThanRow ;
		else if( normalisedAngle < 45. || normalisedAngle > 135. )
			swap = true ;

		return swap ;
	}

	private double normalise( double angle )
	{
		double returnable = angle ;

			while( returnable < 0 )
				returnable += 180. ;
			while( returnable > 180. )
				returnable -= 180. ;		

		return returnable ;
	}
	
	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this ) ;
		if( instrument instanceof SpInstHeterodyne )
		{
			/*
			 * Based on real timing data 
			 * http://wiki.jach.hawaii.edu/staff_wiki-bin/wiki/20060925_jcmtfco
			 */
			double samplesPerRow = numberOfSamplesPerRow() ;
			double samplesPerColumn = numberOfSamplesPerColumn() ;

			if( ( ( ( int )samplesPerRow ) & 1 ) == 0 )
				samplesPerRow++ ;
			double time = getSecsPerRow() * samplesPerColumn + 80. ;
			return time ;
		}
		else if( instrument instanceof SpInstSCUBA2 )
		{
			double time = SCUBA2_STARTUP_TIME ;

			if( s2time == null )
				s2time = new Scuba2Time() ;

			time = s2time.totalIntegrationTime( this ) ;

			return time ;
		}
		return 0. ;
	}

	public void setScanStrategy( String strategy )
	{
		if( strategy != null || !"".equals( strategy ) )
			_avTable.set( ATTR_SCAN_STRATEGY , strategy , 0 ) ;
	}

	public String getScanStrategy()
	{
		return _avTable.get( ATTR_SCAN_STRATEGY , 0 ) ;
	}

	public void setIntegrations( int integrations )
	{
		if( _avTable.exists( ATTR_SCAN_STRATEGY ) )
			_avTable.set( ATTR_SCAN_INTEGRATIONS , integrations , 0 ) ;
	}

	public String getIntegrations()
	{
		return _avTable.get( ATTR_SCAN_INTEGRATIONS , 0 ) ;
	}
	
	public void rmIntegrations()
	{
		_avTable.rm( ATTR_SCAN_INTEGRATIONS ) ;
	}
	
	public void rmSampleTime()
	{
		_avTable.rm( ATTR_SAMPLE_TIME ) ;
	}
	
	/** Creates JAC TCS XML. */
	protected void processAvAttribute( String avAttr , String indent , StringBuffer xmlBuffer )
	{
		// ATTR_SCANAREA_HEIGHT is an AV attribute that occurs once in a SpIterOffset's AV table
		// When processAvAttribute is called with ATTR_SCANAREA_HEIGHT as avAttr then append the entire
		// TCS XML representation of this item to the xmlBuffer.
		// For all other calls to processAvAttribute ignore the AV attributes, except meta attribues
		// such as ".gui.collapsed" which are delegated to the super class.
		if( avAttr.equals( ATTR_SCANAREA_HEIGHT ) )
		{
			// Append <obsArea> element.
			xmlBuffer.append( "\n" + indent + "  <" + TX_OBS_AREA + ">" ) ;
			xmlBuffer.append( "\n" + indent + "    <" + TX_PA + ">" + getPosAngle() + "</" + TX_PA + ">" ) ;

			xmlBuffer.append( "\n" + indent + "    <" + TX_SCAN_AREA + ">" ) ;
			xmlBuffer.append( "\n" + indent + "      <" + TX_AREA + " " + TX_HEIGHT + "=\"" + getHeight() + "\" " + TX_WIDTH + "=\"" + getWidth() + "\"/>" ) ;
			xmlBuffer.append( "\n" + indent + "      <" + TX_SCAN + " " + TX_SCAN_DY + "=\"" + getScanDy() + "\" " + TX_SCAN_VELOCITY + "=\"" + getScanVelocity() + "\" " + TX_SCAN_SYSTEM + "=\"" + getScanSystem() + "\">" ) ;
			if( getScanAngles() != null )
			{
				for( int i = 0 ; i < getScanAngles().size() ; i++ )
					xmlBuffer.append( "\n" + indent + "        <" + TX_PA + ">" + getScanAngle( i ) + "</" + TX_PA + ">" ) ;
			}
			xmlBuffer.append( "\n" + indent + "      </" + TX_SCAN + ">" ) ;
			xmlBuffer.append( "\n" + indent + "    </" + TX_SCAN_AREA + ">" ) ;

			xmlBuffer.append( "\n" + indent + "  </" + TX_OBS_AREA + ">" ) ;
		}
		else if( avAttr.startsWith( TX_SCAN_AREA ) )
		{
			// Ignore. Dealt with in <obsArea> element (see above).
			;
		}
		else
		{
			super.processAvAttribute( avAttr , indent , xmlBuffer ) ;
		}
	}

	/** JAC TCS XML parsing. */
	public void processXmlElementStart( String name )
	{
		if( ( name != null ) && ( !name.equals( TX_PA ) ) )
			_xmlPaAncestor = name ;

		super.processXmlElementStart( name ) ;
	}

	/** JAC TCS XML parsing. */
	public void processXmlElementContent( String name , String value )
	{

		// Ignore XML elements whose do not contain characters themselves but only
		// XML attributes or XML child elements.
		if( name.equals( TX_OBS_AREA ) || name.equals( TX_SCAN_AREA ) || name.equals( TX_AREA ) || name.equals( TX_SCAN ) )
		{
			;
		}
		else if( name.equals( TX_PA ) )
		{
			if( ( _xmlPaAncestor != null ) && _xmlPaAncestor.equals( TX_SCAN ) )
			{
				if( getScanAngles() == null )
					setScanAngle( Format.toDouble( value ) , 0 ) ;
				else
					setScanAngle( Format.toDouble( value ) , getScanAngles().size() ) ;
			}
			else
			{
				setPosAngle( value ) ;
			}
		}
		else
		{
			super.processXmlElementContent( name , value ) ;
		}
	}

	/** JAC TCS XML parsing. */
	public void processXmlElementEnd( String name )
	{
		// save() just means reset() in this context.
		if( name.equals( TX_OBS_AREA ) )
			getAvEditFSM().save() ;
		else
			super.processXmlElementEnd( name ) ;
	}

	/** JAC TCS XML parsing. */
	public void processXmlAttribute( String elementName , String attributeName , String value )
	{
		if( elementName.equals( TX_AREA ) )
		{
			if( attributeName.equals( TX_HEIGHT ) )
				setHeight( value ) ;
			else if( attributeName.equals( TX_WIDTH ) )
				setWidth( value ) ;
		}
		else if( elementName.equals( TX_SCAN ) )
		{
			if( attributeName.equals( TX_SCAN_DY ) )
				setScanDy( value ) ;
			else if( attributeName.equals( TX_SCAN_VELOCITY ) )
				setScanVelocity( value ) ;
			else  if( attributeName.equals( TX_SCAN_SYSTEM ) )
				setScanSystem( value ) ;
		}
		else
		{
			super.processXmlAttribute( elementName , attributeName , value ) ;
		}
	}

	public void setDefaults()
	{
		setContinuumMode( false ) ;
		setRowsPerRef( ROWS_PER_REF_DEFAULT ) ;
		// Set the rows per cal to give about 10 minutes between calibrations.
		double rowsPerRef = ( 10 * 60 ) / getSecsPerRow() ;
		if( rowsPerRef < 1 )
			rowsPerRef = 1. ;
		else if( rowsPerRef > Math.ceil( getHeight() / getScanDy() ) )
			rowsPerRef = Math.ceil( getHeight() / getScanDy() ) ;

		int iRowsPerRef = ( int )Math.rint( rowsPerRef ) ;
		setRowsPerCal( "" + iRowsPerRef ) ;
	}

	public void setupForHeterodyne()
	{
		if( _avTable.get( ATTR_SWITCHING_MODE ) == null || _avTable.get( ATTR_SWITCHING_MODE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SWITCHING_MODE , SWITCHING_MODE_POSITION , 0 ) ;

		if( _avTable.get( ATTR_ROWS_PER_CAL ) == null || _avTable.get( ATTR_ROWS_PER_CAL ).equals( "" ) )
			_avTable.noNotifySet( ATTR_ROWS_PER_CAL , ROWS_PER_CAL_DEFAULT , 0 ) ;

		if( _avTable.get( ATTR_ROWS_PER_REF ) == null || _avTable.get( ATTR_ROWS_PER_REF ).equals( "" ) )
			_avTable.noNotifySet( ATTR_ROWS_PER_REF , ROWS_PER_REF_DEFAULT , 0 ) ;

		if( _avTable.get( ATTR_SAMPLE_TIME ) == null || _avTable.get( ATTR_SAMPLE_TIME ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SAMPLE_TIME , "4" , 0 ) ;

		if( _avTable.get( ATTR_CONTINUUM_MODE ) == null || _avTable.get( ATTR_CONTINUUM_MODE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_CONTINUUM_MODE , "false" , 0 ) ;

		if( _avTable.get( ATTR_SCANAREA_SCAN_SYSTEM ) == null || _avTable.get( ATTR_SCANAREA_SCAN_SYSTEM ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_SYSTEM , SCAN_SYSTEMS[ 0 ] , 0 ) ;

		if( _avTable.get( ATTR_SCANAREA_SCAN_VELOCITY ) == null || _avTable.get( ATTR_SCANAREA_SCAN_VELOCITY ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_VELOCITY , "0.0" , 0 ) ;

		if( _avTable.get( ATTR_SCANAREA_SCAN_DY ) == null || _avTable.get( ATTR_SCANAREA_SCAN_DY ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_DY , "10.0" , 0 ) ;

		_avTable.noNotifyRm( ATTR_SCAN_STRATEGY ) ;
		_avTable.noNotifySet( ATTR_SCAN_STRATEGY , SCAN_PATTERN_BOUS , 0 ) ;
	}

	public void setupForSCUBA2()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE ) ;
		_avTable.noNotifyRm( ATTR_ROWS_PER_CAL ) ;
		_avTable.noNotifyRm( ATTR_ROWS_PER_REF ) ;
		_avTable.noNotifyRm( ATTR_CONTINUUM_MODE ) ;
		_avTable.noNotifySet( ATTR_SCANAREA_SCAN_VELOCITY , "" + ( ( SpJCMTInstObsComp )SpTreeMan.findInstrument( this ) ).getDefaultScanVelocity() , 0 ) ;

		String strategy = _avTable.get( ATTR_SCAN_STRATEGY ) ;
		if( strategy == null || strategy.equals( "" ) )
		{
			strategy = SCAN_STRATEGIES_SCUBA2[ 0 ] ;
			_avTable.noNotifySet( ATTR_SCAN_STRATEGY , SCAN_STRATEGIES_SCUBA2[ 0 ] , 0 ) ;
		}
		
		if( strategy.equals( SCAN_PATTERN_POINT ) )
		{
			_avTable.noNotifyRm( ATTR_SCAN_INTEGRATIONS ) ;
			if( _avTable.get( ATTR_SAMPLE_TIME ) == null || _avTable.get( ATTR_SAMPLE_TIME ).equals( "" ) )
				_avTable.noNotifySet( ATTR_SAMPLE_TIME , "4." , 0 ) ;
		}
		else
		{
			_avTable.noNotifyRm( ATTR_SAMPLE_TIME ) ;
			if( _avTable.get( ATTR_SCAN_INTEGRATIONS ) == null || _avTable.get( ATTR_SCAN_INTEGRATIONS ).equals( "" ) )
				_avTable.noNotifySet( ATTR_SCAN_INTEGRATIONS , "1" , 0 ) ;
		}
		
		if( _avTable.get( ATTR_SCANAREA_SCAN_DY ) == null || _avTable.get( ATTR_SCANAREA_SCAN_DY ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_DY , "" + ( ( SpJCMTInstObsComp )SpTreeMan.findInstrument( this ) ).getDefaultScanDy() , 0 ) ;
	}

	public String[] getSwitchingModeOptions()
	{
		return new String[] { SWITCHING_MODE_POSITION , SWITCHING_MODE_CHOP } ;
	}
}
