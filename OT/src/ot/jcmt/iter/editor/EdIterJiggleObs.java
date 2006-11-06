/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.iter.editor;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;

import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.SpJCMTConstants;
import orac.jcmt.inst.SpJCMTInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.inst.SpInstSCUBA;
import orac.jcmt.iter.SpIterJiggleObs;
import orac.jcmt.util.ScubaNoise;

/**
 * This is the editor for Jiggle Observe Mode iterator component.
 * 
 * @author modified by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterJiggleObs extends EdIterJCMTGeneric implements CommandButtonWidgetWatcher {

  private IterJiggleObsGUI _w;       // the GUI layout panel

  private SpIterJiggleObs _iterObs;

  private String [] _noJigglePatterns = { "No Instrument in scope." };

  // Over-ride the switching modes since only beam and 
  // frequency will initially be offered by ACSIS
  protected static String [] SWITCHING_MODES = {
      SpJCMTConstants.SWITCHING_MODE_BEAM,
      SpJCMTConstants.SWITCHING_MODE_FREQUENCY_S,
      SpJCMTConstants.SWITCHING_MODE_FREQUENCY_F,
      SpJCMTConstants.SWITCHING_MODE_NONE };

  /**
   * The constructor initializes the title, description, and presentation source.
   */
	public EdIterJiggleObs()
	{
		super( new IterJiggleObsGUI() );

		_title = "Jiggle";
		_presSource = _w = ( IterJiggleObsGUI ) super._w;
		_description = "Jiggle Observation Mode";

		_w.coordSys.setChoices( SpIterJiggleObs.JIGGLE_SYSTEMS );

		_w.jigglePattern.addWatcher( this );
		_w.scaleFactor.addWatcher( this );
		_w.contModeCB.addWatcher( this );
		_w.defaultButton.addWatcher( this );
		_w.paTextBox.addWatcher( this );
		_w.coordSys.addWatcher( this );
	}

  /**
	 * Override setup to store away a reference to the Focus Iterator.
	 */
	public void setup( SpItem spItem )
	{
		_iterObs = ( SpIterJiggleObs ) spItem;
		super.setup( spItem );
	}

  	protected void _updateWidgets()
	{
		SpJCMTInstObsComp instObsComp = ( SpJCMTInstObsComp ) SpTreeMan.findInstrument( _iterObs );

		if( instObsComp != null )
		{
			_w.jigglePattern.setChoices( instObsComp.getJigglePatterns() );

			// Select jiggle pattern.
			boolean jigglePatternSet = false;
			String jigglePattern = _iterObs.getJigglePattern();
			for( int i = 0 ; i < _w.jigglePattern.getItemCount() ; i++ )
			{
				if( jigglePattern == null )
				{
					break;
				}
				else if( jigglePattern.equals( _w.jigglePattern.getItemAt( i ) ) )
				{
					_w.jigglePattern.setValue( _w.jigglePattern.getItemAt( i ) );
					jigglePatternSet = true;
					break;
				}
			}

			if( !jigglePatternSet )
				_iterObs.setJigglePattern( ( String ) _w.jigglePattern.getValue() );

			if( instObsComp instanceof SpInstHeterodyne )
			{
				if( _iterObs.getSwitchingMode() == null )
					_iterObs.setSwitchingMode( SpJCMTConstants.SWITCHING_MODE_NONE );
				_w.contModeCB.setSelected( _iterObs.isContinuum() );
				_w.scaleFactor.setValue( _iterObs.getScaleFactor() );
			}
		}
		else
		{
			_w.jigglePattern.setChoices( _noJigglePatterns );
			_iterObs.setJigglePattern( "" );
		}

		_w.paTextBox.setValue( _iterObs.getPosAngle() );
		_w.coordSys.setValue( _iterObs.getCoordSys() );
		
		_w.scaleFactor.setEnabled( !isHarp() ) ;

		super._updateWidgets();
	}

	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		if( tbwe == _w.paTextBox )
			_iterObs.setPosAngle( tbwe.getValue() );
		else if( tbwe == _w.scaleFactor )
			_iterObs.setScaleFactor( tbwe.getDoubleValue( 1.0 ) );
		else
			super.textBoxKeyPress( tbwe );
	}

  	public void dropDownListBoxAction( DropDownListBoxWidgetExt ddlbwe , int index , String val )
	{
		if( ddlbwe == _w.jigglePattern )
		{
			_iterObs.setJigglePattern( val );

			if( SpTreeMan.findInstrument( _iterObs ) instanceof SpInstHeterodyne )
			{
				if( isHarp() )
				{
					double scaleFactor = _iterObs.getScaleFactor() ;
					String[] split = val.split( "HARP" ) ;
					if( "4".equals( split[ 1 ] ) )
						scaleFactor = 7.5 ;
					else
						scaleFactor = 6. ;
					_w.scaleFactor.setValue( scaleFactor ) ;
					_iterObs.setScaleFactor( scaleFactor ) ;
				}
			}
			_updateWidgets() ;
		}
		else if( ddlbwe == _w.coordSys )
		{
			_iterObs.setCoordSys( val );
		}
		else
		{
			super.dropDownListBoxAction( ddlbwe , index , val );
		}
	}

  	private boolean isHarp()
  	{
  		String pattern = _iterObs.getJigglePattern() ;
		boolean isHarp = false ;
		if( pattern != null )
			isHarp = pattern.startsWith( "HARP" ) ;  
		return isHarp ;
  	}
  	
	public void checkBoxAction( CheckBoxWidgetExt cbwe )
	{
		if( cbwe == _w.contModeCB )
			_iterObs.setContinuumMode( _w.contModeCB.isSelected() );

		super.checkBoxAction( cbwe );
	}

	public void commandButtonAction( CommandButtonWidgetExt cbwe )
	{
		if( cbwe == _w.defaultButton )
			_iterObs.setAcsisDefaults();

		_updateWidgets();
	}

	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		super.setInstrument( spInstObsComp );

		if( ( spInstObsComp != null ) && ( spInstObsComp instanceof SpInstHeterodyne ) )
		{
			_w.switchingMode.setChoices( SWITCHING_MODES );
			if( _iterObs == null )
				_w.switchingMode.setValue( SpJCMTConstants.SWITCHING_MODE_BEAM );
			else
				_w.switchingMode.setValue( _iterObs.getSwitchingMode() );
			_w.acsisPanel.setVisible( true );
		}
		else
		{
			_w.acsisPanel.setVisible( false );
		}
	}

  	protected double calculateNoise( int integrations , double wavelength , double nefd , int[] status )
	{
		String mode = "JIG16";

		SpJCMTInstObsComp instObsComp = ( SpJCMTInstObsComp ) SpTreeMan.findInstrument( _iterObs );
		if( ( instObsComp != null ) && ( _iterObs.isJIG64( ( SpInstSCUBA ) instObsComp ) ) )
			mode = "JIG64";
		return ScubaNoise.noise_level( integrations , wavelength , mode , nefd , status );
	}
}

