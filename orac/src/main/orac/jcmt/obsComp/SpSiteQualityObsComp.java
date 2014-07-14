/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package orac.jcmt.obsComp ;

import gemini.sp.SpFactory ;
import gemini.util.Format ;

/**
 * Site Quality observation component.
 */
@SuppressWarnings( "serial" )
public class SpSiteQualityObsComp extends gemini.sp.obsComp.SpSiteQualityObsComp
{
	public static final String ATTR_TAU_BAND_ALLOCATED = "tauBandAllocated" ;
	public static final String ATTR_MIN_TAU = "minTau" ;
	public static final String ATTR_MAX_TAU = "maxTau" ;
	public static final String ATTR_NOISE_CALCULATION_TAU = "noiseCalculationTau" ;
	public static final String ATTR_SEEING = "seeing" ;
	public static final int NO_VALUE = 0 ;
	protected static final String XML_SEEING = "seeing" ;
	protected static final String XML_CSO_TAU = "csoTau" ;
	protected static final String XML_MAX = "max" ;
	protected static final String XML_MIN = "min" ;

	public static final double[][] SEEING_RANGES = 
	{ 
		{ 0. , .5 } , 
		{ 0. , 1. } , 
		{ 0. , 3. } , 
		{ 0. , Double.POSITIVE_INFINITY } // not used
	} ;

	/** Value for "Don't care option in GUI."  */
	public static final int SEEING_ANY = SEEING_RANGES.length ;

	/** Used for XML parsing. @see #processXmlElementContent(java.lang.String,java.lang.String) */
	private String _previousXmlElement = "" ;

	// Register the prototype. This replaces gemini.sp.obsComp.SpSiteQualityObsComp with orac.jcmt.obsComp.SpSiteQualityObsComp
	static
	{
		SpFactory.registerPrototype( new SpSiteQualityObsComp() ) ;
	}

	/**
	 * Default constructor.  Initialize the component type.
	 */
	public SpSiteQualityObsComp()
	{
		_avTable.noNotifySet( ATTR_TAU_BAND_ALLOCATED , "true" , 0 ) ;
		_avTable.noNotifySet( ATTR_SEEING , "" + SEEING_ANY , 0 ) ;
		_avTable.noNotifySet( ATTR_NOISE_CALCULATION_TAU , "0.08" , 0 ) ;
	}

	/**
	 * Set minimum tau.
	 */
	public void setTauBandAllocated( boolean value )
	{
		_avTable.set( ATTR_TAU_BAND_ALLOCATED , value ) ;
	}

	/**
	 * Get minimum tau.
	 */
	public boolean tauBandAllocated()
	{
		return _avTable.getBool( ATTR_TAU_BAND_ALLOCATED ) ;
	}

	/**
	 * Set minimum tau.
	 */
	public void setMinTau( double value )
	{
		_avTable.set( ATTR_MIN_TAU , value ) ;
	}

	/**
	 * Set minimum tau from String.
	 */
	public void setMinTau( String value )
	{
		setMinTau( Format.toDouble( value ) ) ;
	}

	/**
	 * Get minimum tau.
	 */
	public double getMinTau()
	{
		return _avTable.getDouble( ATTR_MIN_TAU , NO_VALUE ) ;
	}

	/**
	 * Set maximum tau.
	 */
	public void setMaxTau( double value )
	{
		_avTable.set( ATTR_MAX_TAU , value ) ;
	}

	/**
	 * Set minimum tau from String.
	 */
	public void setMaxTau( String value )
	{
		setMaxTau( Format.toDouble( value ) ) ;
	}

	/**
	 * Get maximum tau.
	 */
	public double getMaxTau()
	{
		return _avTable.getDouble( ATTR_MAX_TAU , NO_VALUE ) ;
	}

	/**
	 * Set cso tau for noise calculation.
	 */
	public void setNoiseCalculationTau( double value )
	{
		_avTable.set( ATTR_NOISE_CALCULATION_TAU , value ) ;
	}

	/**
	 * Set cso tau for noise calculation from String.
	 */
	public void setNoiseCalculationTau( String value )
	{
		setNoiseCalculationTau( Format.toDouble( value ) ) ;
	}

	/**
	 * Get cso tau for noise calculation.
	 */
	public double getNoiseCalculationTau()
	{
		return _avTable.getDouble( ATTR_NOISE_CALCULATION_TAU , NO_VALUE ) ;
	}

	/**
	 * Set seeing index.
	 */
	public void setSeeing( int seeing )
	{
		_avTable.set( ATTR_SEEING , seeing ) ;
	}

	/**
	 * Get seeing index.
	 *
	 * @return seeing state (not an index for an array of seeing options)
	 */
	public int getSeeing()
	{
		return _avTable.getInt( ATTR_SEEING , NO_VALUE ) ;
	}

	/**
	 * Derives the index of a seeing range from a value.
	 */
	public static int getSeeingFromMax( double max )
	{
		for( int i = 0 ; i < SEEING_RANGES.length ; i++ )
		{
			if( ( max > SEEING_RANGES[ i ][ 0 ] ) && ( max <= SEEING_RANGES[ i ][ 1 ] ) )
				return i ;
		}

		return SEEING_ANY ;
	}

	protected void processAvAttribute( String avAttr , String indent , StringBuffer xmlBuffer )
	{
		if( avAttr.equals( ATTR_SEEING ) )
		{
			// if getSeeing() == SEEING_ANY then the option "Don't Care" has been chosen and no XML should be written for seeing.
			if( getSeeing() < SEEING_ANY )
			{
				xmlBuffer.append( "\n  " + indent + "<" + XML_SEEING + ">" ) ;
				xmlBuffer.append( "\n    " + indent + "<" + XML_MIN + ">" + SEEING_RANGES[ getSeeing() ][ 0 ] + "</" + XML_MIN + ">" ) ;
				xmlBuffer.append( "\n    " + indent + "<" + XML_MAX + ">" + SEEING_RANGES[ getSeeing() ][ 1 ] + "</" + XML_MAX + ">" ) ;
				xmlBuffer.append( "\n  " + indent + "</" + XML_SEEING + ">" ) ;
			}
		}
		else if( avAttr.equals( ATTR_TAU_BAND_ALLOCATED ) && ( !tauBandAllocated() ) )
		{
			xmlBuffer.append( "\n  " + indent + "<" + XML_CSO_TAU + ">" ) ;
			xmlBuffer.append( "\n    " + indent + "<" + XML_MIN + ">" + getMinTau() + "</" + XML_MIN + ">" ) ;
			xmlBuffer.append( "\n    " + indent + "<" + XML_MAX + ">" + getMaxTau() + "</" + XML_MAX + ">" ) ;
			xmlBuffer.append( "\n  " + indent + "</" + XML_CSO_TAU + ">" ) ;
		}
		else if( avAttr.equals( ATTR_MIN_TAU ) || ( avAttr.equals( ATTR_MAX_TAU ) || avAttr.equals( ATTR_TAU_BAND_ALLOCATED ) ) )
		{
			// Ignore. Has been processed with ATTR_TAU_BAND_ALLOCATED attribute.
			;
		}
		else
		{
			super.processAvAttribute( avAttr , indent , xmlBuffer ) ;
		}
	}

	public void processXmlElementStart( String name )
	{
		if( name.equals( XML_SEEING ) )
		{
			_previousXmlElement = name ;
		}
		else if( name.equals( XML_CSO_TAU ) )
		{
			_previousXmlElement = name ;
			_avTable.noNotifySet( ATTR_TAU_BAND_ALLOCATED , "false" , 0 ) ;
		}
		else
		{
			super.processXmlElementStart( name ) ;
		}
	}

	public void processXmlElementContent( String name , String value )
	{
		if( name.equals( XML_SEEING ) )
		{
			_previousXmlElement = name ;
		}
		else if( name.equals( XML_CSO_TAU ) )
		{
			_previousXmlElement = name ;
			_avTable.noNotifySet( ATTR_TAU_BAND_ALLOCATED , "false" , 0 ) ;
		}
		else if( name.equals( XML_MAX ) )
		{
			if( _previousXmlElement.equals( XML_SEEING ) )
			{
				double max = Double.POSITIVE_INFINITY ;

				try
				{
					max = Double.parseDouble( value ) ;
				}
				catch( Exception e )
				{
					// ignore
				}

				_avTable.noNotifySet( ATTR_SEEING , "" + getSeeingFromMax( max ) , 0 ) ;

				return ;
			}
			if( _previousXmlElement.equals( XML_CSO_TAU ) )
			{
				double max = Double.POSITIVE_INFINITY ;

				try
				{
					max = Double.parseDouble( value ) ;
				}
				catch( Exception e )
				{
					// ignore
				}

				_avTable.noNotifySet( ATTR_MAX_TAU , "" + max , 0 ) ;

				return ;
			}
		}
		else if( name.equals( XML_MIN ) )
		{
			if( _previousXmlElement.equals( XML_CSO_TAU ) )
			{
				double min = 0. ;

				try
				{
					min = Double.parseDouble( value ) ;
				}
				catch( Exception e )
				{
					// ignore
				}

				_avTable.noNotifySet( ATTR_MIN_TAU , "" + min , 0 ) ;

				return ;
			}
		}
		else
		{
			super.processXmlElementContent( name , value ) ;
		}
	}
}
