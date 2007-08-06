/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package orac.util;

import org.apache.xerces.dom.ElementImpl;

/**
 * PreTranslator.
 *
 * Applies/removes telescope specific changes to {@link orac.util.SpItemDOM} DOM element.
 *
 * @author Martin Folger
 */
public interface PreTranslator
{
	/**
	 * Applies telescope specific changes.
	 *
	 * @param  element DOM element from {@link orac.util.SpItemDOM}
	 */
	public void translate( ElementImpl element ) throws Exception;

	/**
	 * Removes telescope specific changes.
	 *
	 * @param  element DOM element from {@link orac.util.SpItemDOM}
	 */
	public void reverse( ElementImpl element ) throws Exception;
}
