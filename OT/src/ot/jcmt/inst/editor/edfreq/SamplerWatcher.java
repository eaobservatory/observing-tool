/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.inst.editor.edfreq;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
interface SamplerWatcher
{
   public void updateSamplerValues ( double centre, double width, 
   int channels );
}
