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


import java.awt.Component;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class SideBandGui extends JScrollBar
{

   private int lowLimit;
   private int highLimit;
   private int bandWidth;
   private SideBand sideBand;

   public SideBandGui ( SideBand sideBand, int lowLimit, int highLimit, 
     int bandWidth )
   {
     super ( JScrollBar.HORIZONTAL, (lowLimit+highLimit)/2, bandWidth,
       lowLimit, highLimit );
     this.lowLimit = lowLimit;
     this.highLimit = lowLimit;
     this.bandWidth = bandWidth;
     this.sideBand = sideBand;
   }

   public void setSideBand ( SideBand value )
   {
      this.sideBand = value;
   }

   public void updateValue ( )
   {
      setValue ( sideBand.getScaledCentre() );
      setVisibleAmount ( sideBand.getScaledWidth() );
   }

   public void updateSideBand()
   {
      sideBand.setScaledCentre ( getValue() );
   }


}
