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

import java.util.Vector ;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class Receiver
{

	public String name; // receiver name
	public double loMin; // minimum value of local oscillator (Hz)
	public double loMax; // maximum value of local oscillator (Hz)
	public double feIF; // central IF delivered (Hz)
	public double bandWidth; // bandwidth of IF (Hz)
	public Vector bandspecs; // list of possible band specs

	public Receiver( String name , double loMin , double loMax , double feIF , double bandWidth )
	{
		this.name = name;
		this.loMin = loMin;
		this.loMax = loMax;
		this.feIF = feIF;
		this.bandWidth = bandWidth;
		bandspecs = new Vector();
	}

	public void setBandSpecs( Vector v )
	{
		bandspecs = v;
	}

	public String toString()
	{
		String rtn = "[name=" + name + "; loMin=" + loMin + "; loMax=" + loMax + "; feIF=" + feIF + "; bandWidth=" + bandWidth + "; bandSpecs=" + bandspecs + "]";
		return rtn;
	}

}
