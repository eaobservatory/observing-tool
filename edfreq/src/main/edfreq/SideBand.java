/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package edfreq ;

import java.awt.event.AdjustmentListener ;
import java.awt.event.AdjustmentEvent ;
import java.awt.event.MouseListener ;
import java.awt.event.MouseEvent ;
import javax.swing.JScrollBar ;
import javax.swing.SwingUtilities ;
import java.awt.Color ;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger
 *         (M.Folger@roe.ac.uk)
 */
public class SideBand implements AdjustmentListener , SamplerWatcher , MouseListener
{
	double lowLimit ;
	double highLimit ;
	double subBandWidth ;
	double subBandCentre ;
	Sampler sampler ;
	JScrollBar sideBandGui ;
	AdjustmentListener _adjustmentListener = null ;
	private static Color _scrollBarKnobColor = new Color( 156 , 154 , 206 ) ;
	Color _scrollBarBackground = null ;

	/**
     * The lineButton argument has been so that its text can be reset to "No
     * Line" when the side band JScrollBar of this Sideband is changed.
     */
	double pixratio ;
	EmissionLines emissionLines ;
	SideBandDisplay sideBandDisplay = null ; // Added by MFO (8 January 2002)
	HeterodyneEditor hetEditor = null ;
	private int _currentSideBandGuiValue ;
	private int _currentSideBandGuiExtend ;
	private int _currentSideBandGuiMinimum ;
	private int _currentSideBandGuiMaximum ;

	/**
     * Indicates whether LO1 should be changed while the top subsystem sideband
     * IF is changed so that the sidband stays centred over the line.
     */
	private static boolean _lineClamped = true ;
	private boolean _bandWidthExceedsRange = false ;

	/**
     * SideBand constructor.
     * 
     * The lineButton argument has been so that its text can be reset to "No
     * Line" when the side band JScrollBar of this Sideband is changed.
     */
	public SideBand( double lowLimit , double highLimit , double subBandWidth , double subBandCentre , Sampler sampler , JScrollBar sideBandGui , double pixratio , EmissionLines emissionLines )
	{
		this.lowLimit = lowLimit ;
		this.highLimit = highLimit ;
		this.subBandWidth = subBandWidth ;
		this.subBandCentre = subBandCentre ;
		this.sampler = sampler ;
		this.sideBandGui = sideBandGui ;
		this.pixratio = pixratio ;
		this.emissionLines = emissionLines ;
		sideBandGui.addAdjustmentListener( this ) ;
		sideBandGui.addMouseListener( this ) ;
		_scrollBarBackground = sideBandGui.getBackground() ;

		if( !FrequencyEditorCfg.getConfiguration().centreFrequenciesAdjustable )
		{
			_currentSideBandGuiValue = sideBandGui.getValue() ;
			_currentSideBandGuiExtend = sideBandGui.getVisibleAmount() ;
			_currentSideBandGuiMinimum = sideBandGui.getMinimum() ;
			_currentSideBandGuiMaximum = sideBandGui.getMaximum() ;
		}
	}

	public void setSubBandCentre( double subBandCentre )
	{
		this.subBandCentre = subBandCentre ;
	}

	public double getSubBandCentre()
	{
		if( highLimit < 0.0 )
			subBandCentre = -sampler.getCentreFrequency() ;
		else
			subBandCentre = sampler.getCentreFrequency() ;
		return subBandCentre ;
	}

	public void setScaledCentre( int v )
	{
		double mySubBandCentre = ( ( double )v ) / pixratio + ( 0.5 * subBandWidth ) ;
		setSubBandCentre( mySubBandCentre ) ;

		sampler.setCentreFrequency( Math.abs( subBandCentre ) ) ;
	}

	public void updateCentreFrequency()
	{
		sampler.setCentreFrequency( Math.abs( subBandCentre ) ) ;
	}

	public int getScaledCentre()
	{
		return ( int )Math.rint( ( getSubBandCentre() - ( 0.5 * subBandWidth ) ) * pixratio ) ;
	}

	public int getScaledWidth()
	{
		return ( int )Math.rint( pixratio * subBandWidth ) ;
	}

	public void setSubBandWidth( double subBandWidth )
	{
		this.subBandWidth = subBandWidth ;
	}

	public void adjustmentValueChanged( AdjustmentEvent e )
	{
		if( !FrequencyEditorCfg.getConfiguration().centreFrequenciesAdjustable )
		{
			sideBandGui.setValues( _currentSideBandGuiValue , _currentSideBandGuiExtend , _currentSideBandGuiMinimum , _currentSideBandGuiMaximum ) ;
			return ;
		}

		setScaledCentre( sideBandGui.getValue() ) ;

		if( _adjustmentListener != null )
			_adjustmentListener.adjustmentValueChanged( e ) ;
	}

	public void updateSamplerValues( double centre , double width , int channels )
	{
		/*
         * If the SideBand is one of the top SideBands and the line should be
         * clamped then adjust LO1 accordingly.
         */
		if( isTopSideBand() && _lineClamped && ( width == subBandWidth ) )
		{
			String band ;
			if( hetEditor != null )
				band = hetEditor.getFeBand() ;
			else
				band = "usb" ;

			if( band.equals( "lsb" ) )
			{
				if( highLimit < 0.0 )
					sideBandDisplay.setLO1( sideBandDisplay.getLO1() + ( subBandCentre + centre ) ) ;
				else
					sideBandDisplay.setLO1( sideBandDisplay.getLO1() - ( subBandCentre - centre ) ) ;
			}
			else
			{
				if( highLimit < 0.0 )
					sideBandDisplay.setLO1( sideBandDisplay.getLO1() - ( subBandCentre + centre ) ) ;
				else
					sideBandDisplay.setLO1( sideBandDisplay.getLO1() + ( subBandCentre - centre ) ) ;
			}
		}

		int sc ;

		if( highLimit < 0.0 )
			subBandCentre = -centre ;
		else
			subBandCentre = centre ;

		subBandWidth = width ;

		sideBandGui.removeAdjustmentListener( this ) ;

		int sw = getScaledWidth() ;

		if( ( sw >= ( pixratio * ( highLimit - lowLimit ) ) ) && sideBandGui.isEnabled() )
		{
			sideBandGui.setBackground( _scrollBarKnobColor ) ;
			_bandWidthExceedsRange = true ;
		}
		else
		{
			sideBandGui.setBackground( _scrollBarBackground ) ;
			_bandWidthExceedsRange = false ;
		}

		sc = getScaledCentre() ;
		int pixTimesLow = ( int )Math.rint( pixratio * lowLimit ) ;
		int pixTimesHigh = ( int )Math.rint( pixratio * highLimit ) ;
		sideBandGui.setValues( sc , sw , pixTimesLow , pixTimesHigh ) ;

		if( !FrequencyEditorCfg.getConfiguration().centreFrequenciesAdjustable )
		{
			_currentSideBandGuiValue = sc ;
			_currentSideBandGuiExtend = sw ;
			_currentSideBandGuiMinimum = pixTimesLow ;
			_currentSideBandGuiMaximum = pixTimesHigh ;
		}
		sideBandGui.addAdjustmentListener( this ) ;
	}

	protected void connectTopSideBand( SideBandDisplay sideBandDisplay , HeterodyneEditor hetEditor )
	{
		this.sideBandDisplay = sideBandDisplay ;
		this.hetEditor = hetEditor ;
	}

	protected boolean isTopSideBand()
	{
		return( sideBandDisplay != null ) ;
	}

	public void on()
	{
		sideBandGui.setEnabled( true ) ;

		if( _bandWidthExceedsRange )
			sideBandGui.setBackground( _scrollBarKnobColor ) ;
	}

	public void off()
	{
		sideBandGui.setEnabled( false ) ;
		sideBandGui.setBackground( _scrollBarBackground ) ;
	}

	public void mouseClicked( MouseEvent e ){}

	public void mouseEntered( MouseEvent e ){}

	public void mouseExited( MouseEvent e ){}

	public void mousePressed( MouseEvent e )
	{
		if( SwingUtilities.isRightMouseButton( e ) )
			_lineClamped = false ;
	}

	public void mouseReleased( MouseEvent e )
	{
		_lineClamped = true ;
	}

	/**
     * Adds one AdjustmentListener.
     * 
     * The SideBand can only have a single AdjustmentListener. The
     * AdjustmentListener is only notified of an adjustmentValueChanged if the
     * adjustment is a change of the IF (moving scroll bar) and not if it is a
     * change of bandwidth (change of width of the scroll bar). This is achieved
     * because this AdjustmentListener (the SideBand) is removed from its scroll
     * bar while updateSamplerValues is executed.
     */
	public void addAdjustmentListener( AdjustmentListener adjustmentListener )
	{
		_adjustmentListener = adjustmentListener ;
	}

	public static Color getSideBandColor()
	{
		return _scrollBarKnobColor ;
	}
}
