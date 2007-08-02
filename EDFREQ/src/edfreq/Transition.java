/* ============================================================== */
/*                                                              */
/* UK Astronomy Technology Centre */
/* Royal Observatory, Edinburgh */
/* Joint Astronomy Centre, Hilo */
/* Copyright (c) PPARC 2001 */
/*                                                              */
/* ============================================================== */
// $Id$
package edfreq;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class Transition
{
	public String name;
	public double frequency;

	public Transition( String name , double frequency )
	{
		this.name = name;
		this.frequency = frequency;
	}

	public String toString()
	{
		return name;
	}
}
