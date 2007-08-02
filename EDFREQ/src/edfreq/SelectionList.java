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
public class SelectionList
{

	public Vector objectList;

	public String name;

	public SelectionList( String name )
	{
		this.name = name;
		objectList = new Vector();
	}

	public String toString()
	{
		return name;
	}

}
