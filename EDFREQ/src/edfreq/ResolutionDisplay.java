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

import javax.swing.JLabel ;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class ResolutionDisplay extends JLabel implements SamplerWatcher
{

	private int channels;
	private int resolution;
	private double width;
	private int nMixers;

	public ResolutionDisplay( int channels , double width , int nMixers )
	{
		super();
		setHorizontalAlignment( CENTER );
		this.channels = channels;
		this.width = width;
		this.nMixers = nMixers;

		resolution = nMixers * ( int )( 1.0E-3 * width / ( double )channels );
		setText( String.valueOf( resolution ) );
	}

	public void setChannels( int channels )
	{
		this.channels = channels;
		resolution = nMixers * ( ( int )( 1.0E-3 * width / ( double )channels ) );
		System.out.println( "Setting text of ResolutionDisplay to " + resolution );
		setText( String.valueOf( resolution ) );
		repaint();
	}

	public void updateSamplerValues( double centre , double width , int channels )
	{
		this.width = width;
		this.channels = channels;
		resolution = ( int )Math.rint( nMixers * ( ( width * 1.0E-3 ) / ( double )channels ) );
		setText( String.valueOf( resolution ) );
	}
}
