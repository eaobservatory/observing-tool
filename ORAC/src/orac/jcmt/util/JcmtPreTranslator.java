/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package orac.jcmt.util ;

import orac.util.TcsPreTranslator ;

/**
 * XML PreTranslator for JCMT.
 *
 * Applies/removes JCMT specific changes to/from {@link orac.util.SpItemDOM} xml.
 *
 * @author Martin Folger
 */
public class JcmtPreTranslator extends TcsPreTranslator
{
	private static final String[] TCS_TARGET_TYPES = { "science" , "reference" } ;

	public JcmtPreTranslator( String scienceTag , String referenceTag ) throws Exception
	{
		super( scienceTag , referenceTag ) ;
	}

	/**
	 * Target types used by the JAC OCS TCS for JCMT XML.
	 *
	 * The target types are "science" and "reference".
	 */
	protected String[] getTcsTargetTypes()
	{
		return TCS_TARGET_TYPES ;
	}
}
