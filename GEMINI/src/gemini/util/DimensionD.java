// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util;

import java.awt.Dimension;

/**
 * Like a java.awt.Dimension, but instead of integer coordinates,
 * doubles are used.
 */
public class DimensionD
{
   public double height;
   public double width;

   public DimensionD()
   {
   }

   public DimensionD(Dimension d)
   {
      height = (double) d.height;
      width  = (double) d.width;
   }

   public DimensionD(DimensionD d)
   {
      height = d.height;
      width  = d.width;
   }

   public DimensionD(double width, double height)
   {
      this.height = height;
      this.width  = width;
   }

   public Dimension
   getAWTDimension()
   {
      return new Dimension((int)(width + 0.5), (int)(height + 0.5));
   }

   public String
   toString()
   {
      return getClass().getName() + "[width="+ width +", height="+ height+"]";
   }
}

