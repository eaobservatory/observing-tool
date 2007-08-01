// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//

package jsky.app.ot.util;

/**
 * A data type representing an RA and Dec pair in degrees.
 */
public class RADec
{
	/** The coordinate system in which the RA and Dec are represented. */
	public int coordSystem = CoordSys.FK5;

	/** Right ascension in degrees. */
	public double ra = 0.0;

	/** Declination in degrees. */
	public double dec = 0.0;

	/** Proper motion in right ascension. */
	public double rapm = 0.0;

	/** Proper motion in declination. */
	public double decpm = 0.0;

	/**
	 * Default constructor.
	 */
	public RADec(){}

	/**
	 * Construct with most of the fields.
	 */
	public RADec( int sys , double ra , double dec )
	{
		switch( sys )
		{
			case CoordSys.FK5 :
			case CoordSys.FK4 :
				this.coordSystem = sys;
				break;
			default :
				this.coordSystem = CoordSys.FK5;
		}
		this.ra = ra;
		this.dec = dec;
	}

	/**
	 * Construct with all of the fields.
	 */
	public RADec( int sys , double ra , double dec , double rapm , double decpm )
	{
		this( sys , ra , dec );
		this.rapm = rapm;
		this.decpm = decpm;
	}

}
