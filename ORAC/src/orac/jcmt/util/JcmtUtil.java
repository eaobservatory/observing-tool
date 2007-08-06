/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package orac.jcmt.util;

import gemini.util.CoordSys;
import orac.util.TelescopeUtil;
import orac.util.SpItemDOM;
import orac.validation.SpValidation;
import orac.jcmt.validation.JcmtSpValidation;
import orac.jcmt.SpJCMTConstants;

/**
 * Used for JCMT specific features.
 *
 * @author Martin Folger
 */
public class JcmtUtil implements TelescopeUtil
{

	public static final String CHOP = "chop";

	// There is no proper JCMT validation class yet. Use the Generic class instead.
	private JcmtSpValidation _spValidation = new JcmtSpValidation();

	private String[] _targetTags = { "SCIENCE" , "REFERENCE" };

	public SpValidation getValidationTool()
	{
		return _spValidation;
	}

	public String getBaseTag()
	{
		return _targetTags[ 0 ];
	}

	/**
	 * @return always false for UKIRT
	 */
	public boolean isOffsetTarget( String targetTag )
	{
		return ( ( targetTag != null ) && ( targetTag.equalsIgnoreCase( "reference" ) ) ) ;
	}

	public boolean supports( int feature )
	{
		switch( feature )
		{
			case FEATURE_TARGET_INFO_CHOP :
				return false;
			case FEATURE_FLAG_AS_STANDARD :
				return false;
			case FEATURE_TARGET_INFO_PROP_MOTION :
				return true;
			case FEATURE_TARGET_INFO_TRACKING :
				return true;
			case FEATURE_OFFSET_GRID_PA :
				return true;
			default :
				return false;
		}
	}

	/**
	 * Sets PreTranslator in SpItemDOM.
	 *
	 * Make sure at the time this method is called SpTelescopePos.BASE_TAG and
	 * SpTelescopePos.GUIDE_TAGS[0] are set to correct values.
	 */
	public void installPreTranslator() throws Exception
	{
		SpItemDOM.setPreTranslator( new JcmtPreTranslator( _targetTags[ 0 ] , _targetTags[ 1 ] ) );
	}

	public String[] getCoordSys()
	{
		return CoordSys.COORD_SYS;
	}

	public String[] getCoordSysFor( String purpose )
	{
		if( purpose.equals( CHOP ) )
			return SpJCMTConstants.CHOP_SYSTEMS;

		return null;
	}
}
