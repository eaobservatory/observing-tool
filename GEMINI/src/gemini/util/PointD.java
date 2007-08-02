// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util;

import java.awt.Point;

/**
 * Like a java.awt.Point, but instead of integer coordinates, doubles are used.
 */
public class PointD
{

	public double x;
	public double y;

	public PointD( double x , double y )
	{
		this.x = x;
		this.y = y;
	}

	public Point getAWTPoint()
	{
		return new Point( ( int )( x + 0.5 ) , ( int )( y + 0.5 ) );
	}

	public String toString()
	{
		return getClass().getName() + "[x=" + x + ", y=" + y + "]";
	}
}
