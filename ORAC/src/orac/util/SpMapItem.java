/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// $Id$
package orac.util ;

/**
 * Iterators that implement the map feature
 * are displayed as "tiles" in the offset grid of the EdIterOffsetFeature
 * if they are inside the offset iterator.
 *
 * For instruments components something similar can be done by setting the
 * science area mode to SCI_AREA_ALL in {@link jsky.app.ot.editor.EdIterOffsetFeature}.
 *
 * This SpMapItem interface can be used for displaying iterators inside the offset iterator.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public interface SpMapItem
{
	public double getWidth() ;
	public double getHeight() ;
	public double getPosAngle() ;
}
