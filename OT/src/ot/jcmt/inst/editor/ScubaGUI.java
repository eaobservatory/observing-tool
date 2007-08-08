/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.inst.editor;

import java.awt.BorderLayout ;
import java.awt.GridLayout ;
import java.awt.FlowLayout ;
import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Color ;
import java.awt.Dimension ;
import javax.swing.JPanel ;
import javax.swing.JLabel ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.ListBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;

/**
 * @author Martin Folger ( M.Folger@roe.ac.uk )
 */
public class ScubaGUI extends JPanel
{
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	JPanel jPanel2 = new JPanel();
	GridLayout gridLayout1 = new GridLayout();
	GridLayout gridLayout2 = new GridLayout();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	ListBoxWidgetExt filterList = new ListBoxWidgetExt();
	JPanel jPanel3 = new JPanel();
	CommandButtonWidgetExt editBolometers = new CommandButtonWidgetExt();
	FlowLayout flowLayout1 = new FlowLayout();
	JPanel jPanel4 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JLabel jLabel3 = new JLabel();
	TextBoxWidgetExt primaryBolometer = new TextBoxWidgetExt();
	ListBoxWidgetExt additionalBolometers = new ListBoxWidgetExt();
	JLabel jLabel4 = new JLabel();

	public ScubaGUI()
	{
		try
		{
			jbInit();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		this.setLayout( borderLayout1 );
		jPanel1.setLayout( gridLayout1 );
		gridLayout1.setColumns( 3 );
		gridLayout1.setHgap( 5 );
		jPanel2.setLayout( gridLayout2 );
		gridLayout2.setColumns( 3 );
		gridLayout2.setHgap( 5 );
		jLabel1.setForeground( Color.black );
		jLabel1.setPreferredSize( new Dimension( 28 , 30 ) );
		jLabel1.setText( "Filter" );
		jLabel2.setForeground( Color.black );
		jLabel2.setText( "Bolometers" );
		editBolometers.setText( "Edit Bolometers" );
		jPanel3.setLayout( flowLayout1 );
		flowLayout1.setAlignment( FlowLayout.RIGHT );
		jPanel4.setLayout( gridBagLayout1 );
		jLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel3.setForeground( Color.black );
		jLabel3.setText( "Reference Bolometer" );
		additionalBolometers.setEnabled( false );
		additionalBolometers.setPreferredSize( new Dimension( 50 , 50 ) );
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel4.setForeground( Color.black );
		jLabel4.setText( "Bolometers" );
		primaryBolometer.setBackground( Color.white );
		primaryBolometer.setEnabled( false );
		primaryBolometer.setDisabledTextColor( Color.black );
		primaryBolometer.setEditable( false );
		this.add( jPanel1 , BorderLayout.NORTH );
		jPanel1.add( jLabel1 , null );
		jPanel1.add( jLabel2 , null );
		this.add( jPanel2 , BorderLayout.CENTER );
		jPanel2.add( filterList , null );
		jPanel2.add( jPanel4 , null );
		jPanel4.add( jLabel3 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) );
		jPanel4.add( primaryBolometer , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		jPanel4.add( additionalBolometers , new GridBagConstraints( 0 , 3 , 1 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		jPanel4.add( jLabel4 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.SOUTHWEST , GridBagConstraints.NONE , new Insets( 10 , 0 , 0 , 0 ) , 0 , 0 ) );
		this.add( jPanel3 , BorderLayout.SOUTH );
		jPanel3.add( editBolometers , null );
	}

}
