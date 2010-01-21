// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
/*
 * Gary Cornell and Cay S. Horstmann, Core Java (Book/CD-ROM) Published By
 * SunSoft Press/Prentice-Hall Copyright (C) 1996 Sun Microsystems Inc. All
 * Rights Reserved. ISBN 0-13-565755-5
 * 
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for NON-COMMERCIAL purposes and without fee is hereby granted
 * provided that this copyright notice appears in all copies.
 * 
 * THE AUTHORS AND PUBLISHER MAKE NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHORS AND PUBLISHER SHALL NOT
 * BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
 * MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * A class for formatting numbers that follows printf conventions. Also
 * implements C-like atoi and atof functions
 * 
 * @version 1.01 15 Feb 1996
 * @author Cay Horstmann
 */
package gemini.util ;

public class Format
{
	// Added by Martin Folger (26 February 2002)
	/**
     * Helper method.
     * 
     * @return double value of doubleStr if it can be parsed to double, 0.0
     *         otherwise.
     */
	public static double toDouble( String doubleStr )
	{
		double result = 0. ;

		try
		{
			result = Double.valueOf( doubleStr ).doubleValue() ;
		}
		catch( Exception ex ){}

		return result ;
	}

	// Added by Martin Folger (26 February 2002)
	/**
     * Helper method.
     * 
     * @return int value of intStr if it can be parsed to int, 0 otherwise.
     */
	public static int toInt( String intStr )
	{
		int result = 0 ;

		try
		{
			result = Integer.valueOf( intStr ).intValue() ;
		}
		catch( Exception ex ){}

		return result ;
	}
}
