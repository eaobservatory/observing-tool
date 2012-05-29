/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot ;

import jsky.app.ot.OtProps ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import javax.swing.ButtonGroup ;
import javax.swing.JFrame ;
import javax.swing.JOptionPane ;

/**
 * The general preferences page.
 *
 * This class uses code from ot.Palette of the FreeBongo OT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class OtPreferencesDialog implements ActionListener
{
	/**
	 * This is a subclass of JPanel so it can be used for internal as well as other frames.
	 */
	private OtPreferencesGUI _w ;

	/**
	 * Is only used if the OT is started without internal frames.
	 */
	private JFrame _preferencesDialogFrame ;

	// Keys to use for proxy server settings
	private static final String PROXY_HOST = "http.proxyHost" ;
	private static final String PROXY_PORT = "http.proxyPort" ;
	private static final String NON_PROXY_HOSTS = "http.nonProxyHosts" ;

	public OtPreferencesDialog()
	{
		_w = new OtPreferencesGUI() ;

		ButtonGroup grp = new ButtonGroup() ;
		grp.add( _w.closePromptOption ) ;
		grp.add( _w.closeNoSaveOption ) ;

		_w.okButton.addActionListener( this ) ;
		_w.applyButton.addActionListener( this ) ;
		_w.cancelButton.addActionListener( this ) ;
	}

	/**
	 * For use in no-internal-frames mode.
	 */
	public void show()
	{
		boolean saveShouldPrompt = OtProps.isSaveShouldPrompt() ;
		_w.closePromptOption.setSelected( saveShouldPrompt ) ;
		_w.closeNoSaveOption.setSelected( !saveShouldPrompt ) ;

		_w.proxyServerField.setText( System.getProperty( PROXY_HOST ) ) ;
		_w.proxyPortField.setText( System.getProperty( PROXY_PORT ) ) ;

		if( _preferencesDialogFrame == null )
		{
			_preferencesDialogFrame = new JFrame( "OT Preferences" ) ;
			_preferencesDialogFrame.add( _w ) ;
			_preferencesDialogFrame.setLocation( 100 , 100 ) ;
			_preferencesDialogFrame.pack() ;
		}

		_preferencesDialogFrame.setVisible( true ) ;
		_preferencesDialogFrame.setState( JFrame.NORMAL ) ;
	}

	public void apply()
	{
		if( _w.jTabbedPane1.getSelectedIndex() == 0 )
			OtProps.setSaveShouldPrompt( !_w.closeNoSaveOption.isSelected() ) ;
		else if( _w.jTabbedPane1.getSelectedIndex() == 1 )
			setProxyHost() ;
	}

	public void hide()
	{
		_preferencesDialogFrame.setVisible( false ) ;
	}

	/**
	 * The standard actionPerformed method to handle the "ok", "apply", and "cancel"
	 * buttons.
	 */
	public void actionPerformed( ActionEvent evt )
	{
		Object w = evt.getSource() ;

		if( w == _w.okButton )
		{
			apply() ;
			hide() ;
		}
		else if( w == _w.applyButton )
		{
			apply() ;
		}
		else if( w == _w.cancelButton )
		{
			hide() ;
		}
	}

	private void setProxyHost()
	{
		String host = _w.proxyServerField.getText() ;
		if( host == null || host.length() == 0 )
		{
			System.getProperties().remove( PROXY_HOST ) ;
			System.getProperties().remove( PROXY_PORT ) ;
			System.getProperties().remove( NON_PROXY_HOSTS ) ;
			return ;
		}
		int port = 0 ;
		String s = _w.proxyPortField.getText() ;
		if( s != null && s.length() != 0 )
		{
			try
			{
				port = Integer.parseInt( s ) ;
			}
			catch( Exception e )
			{
				JOptionPane.showMessageDialog( null , "Invalid proxy port number. Will assume 80." , "InvalidPort" , JOptionPane.WARNING_MESSAGE ) ;
				port = 80 ;
			}
		}
		else
		{
			port = 80 ;
		}
		String nonProxyHosts = _w.nonProxyHostsField.getText() ;

		System.setProperty( PROXY_HOST , host ) ;
		System.setProperty( PROXY_PORT , String.valueOf( port ) ) ;
		System.setProperty( NON_PROXY_HOSTS , nonProxyHosts ) ;
	}
}
