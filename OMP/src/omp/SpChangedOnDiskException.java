/* ============================================================== */
/*                                                              */
/* UK Astronomy Technology Centre */
/* Royal Observatory, Edinburgh */
/* Joint Astronomy Centre, Hilo */
/* Copyright (c) PPARC 2001 */
/*                                                              */
/* ============================================================== */
// $Id$
package omp ;

/**
 * This Exception is thrown when the Server throws a Server.SpChangedOnDisk
 * Exception during storeProgram.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpChangedOnDiskException extends Exception
{
	SpChangedOnDiskException( String message )
	{
		super( message ) ;
	}
}
