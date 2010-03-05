package ot.ukirt.inst.editor ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import javax.swing.DefaultComboBoxModel ;

import gemini.sp.SpItem ;

import orac.ukirt.inst.SpInstWFS ;

import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

/**
 * This is the editor for the Wavefront Sensor instrument.
 */

public final class EdCompInstWFS extends EdCompInstBase implements ActionListener
{
	private SpInstWFS _inst ;
	private WfsGUI _w ;
	boolean _ignoreActions = false ;

	public EdCompInstWFS()
	{
		_title = "UKIRT Wavefront Sensor" ;
		_presSource = _w = new WfsGUI() ;
		_description = "The WFS instrument is configured with this component" ;

		_w.expTimeTextBox.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				_inst.setExpTime( _w.expTimeTextBox.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){}
		} ) ;

		_w.coaddsTextBox.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				_inst.setCoadds( _w.coaddsTextBox.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){}
		} ) ;

		DefaultComboBoxModel cModel = new DefaultComboBoxModel( SpInstWFS.LENS_POS ) ;
		_w.lensPosComboBox.setModel( cModel ) ;
		_w.lensPosComboBox.addActionListener( this ) ;
	}

	public void setup( SpItem item )
	{
		_inst = ( SpInstWFS )item ;
		super.setup( item ) ;
	}

	protected void _updateWidgets()
	{
		_ignoreActions = true ;

		_w.expTimeTextBox.setText( "" + _inst.getExpTime() ) ;
		_w.coaddsTextBox.setText( "" + _inst.getCoadds() ) ;

		_w.lensPosComboBox.setSelectedItem( _inst.getLensPos() ) ;

		super._updateWidgets() ;

		_ignoreActions = false ;
	}

	public TextBoxWidgetExt getCoaddsTextBox()
	{
		return _w.coaddsTextBox ;
	}

	public TextBoxWidgetExt getExposureTimeTextBox()
	{
		return _w.expTimeTextBox ;
	}

	public TextBoxWidgetExt getPosAngleTextBox()
	{
		return new TextBoxWidgetExt() ;
	}

	public void actionPerformed( ActionEvent e )
	{
		if( !_ignoreActions )
		{
			if( e.getSource() == _w.lensPosComboBox )
				_inst.setLensPos( ( String )_w.lensPosComboBox.getSelectedItem() ) ;
			else
				System.out.println( "actionPerformed on unknown widget" ) ;
		}
	}
}
