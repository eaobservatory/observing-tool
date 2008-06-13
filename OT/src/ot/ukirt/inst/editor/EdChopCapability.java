// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.inst.editor ;

import gemini.sp.obsComp.SpInstObsComp ;
import gemini.sp.obsComp.SpChopCapability ;

import javax.swing.JComponent ;
import jsky.app.ot.editor.OtItemEditor ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

/**
 * Support for exposures/chop pos, chop cycles/nod, and nod cycles/obs.
 */
public class EdChopCapability
{
	/**
	 * Get the SpChopCapability from an item editor.
	 */
	private SpChopCapability _getChopCap( OtItemEditor itemEditor )
	{
		SpInstObsComp spInst = ( SpInstObsComp )itemEditor.getCurrentSpItem() ;
		String name = SpChopCapability.CAPABILITY_NAME ;
		return ( SpChopCapability )spInst.getCapability( name ) ;
	}

	/**
	 * This method initializes the widgets in the presentation to reflect the
	 * current values of the chopping attributes.
	 */
	protected void _init( final EdCompInstBase gw , final OtItemEditor itemEditor )
	{
		TextBoxWidgetExt tbwe ;

		tbwe = ( TextBoxWidgetExt )getWidget( gw , "expPerChopPos" ) ;
		tbwe.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbwe )
			{
				_getChopCap( itemEditor ).setExposuresPerChopPosition( tbwe.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbwe ){} // ignore
		} ) ;

		tbwe = ( TextBoxWidgetExt )getWidget( gw , "cyclesPerObs" ) ;
		tbwe.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbwe )
			{
				_getChopCap( itemEditor ).setCyclesPerObserve( tbwe.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbwe ){} // ignore
		} ) ;
	}

	/**
	 * Override _updateWidgets to show the value of the chopping attributes.
	 */
	protected void _updateWidgets( EdCompInstBase gw , SpChopCapability chopCap )
	{
		TextBoxWidgetExt tbwe ;
		tbwe = ( TextBoxWidgetExt )getWidget( gw , "expPerChopPos" ) ;
		tbwe.setText( chopCap.getExposuresPerChopPositionAsString() ) ;
		tbwe = ( TextBoxWidgetExt )getWidget( gw , "cyclesPerObs" ) ;
		tbwe.setText( chopCap.getCyclesPerObserveAsString() ) ;
	}

	/**
	 * Helper method.
	 *
	 * @see ot.ukirt.inst.editor.EdDRRecipe#getWidget(String, String)
	 */
	protected JComponent getWidget( EdCompInstBase gw , String widgetName )
	{
		try
		{
			return ( JComponent )( gw.getClass().getDeclaredField( widgetName ).get( gw ) ) ;
		}
		catch( NoSuchFieldException e )
		{
			if( ( System.getProperty( "DEBUG" ) != null ) && System.getProperty( "DEBUG" ).equalsIgnoreCase( "ON" ) )
				System.out.println( "Could not find widget / component \"" + widgetName + "\"." ) ;

			return null ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
			return null ;
		}
	}
}
