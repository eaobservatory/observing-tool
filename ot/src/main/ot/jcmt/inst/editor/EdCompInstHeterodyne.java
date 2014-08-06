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

package ot.jcmt.inst.editor ;

import javax.swing.JRadioButton ;
import javax.swing.AbstractButton ;
import javax.swing.JComboBox ;
import javax.swing.JTextField ;
import javax.swing.JLabel ;
import javax.swing.JOptionPane ;
import javax.swing.DefaultComboBoxModel ;
import javax.swing.JTable ;
import javax.swing.event.AncestorListener ;
import javax.swing.event.AncestorEvent ;
import javax.swing.table.DefaultTableModel ;
import javax.swing.table.DefaultTableCellRenderer ;
import java.awt.event.ActionListener ;
import java.awt.event.ComponentAdapter ;
import java.awt.event.ComponentEvent ;
import java.awt.event.ActionEvent ;
import java.awt.event.KeyAdapter ;
import java.awt.event.KeyEvent ;
import java.awt.Color ;
import java.awt.Container ;
import java.awt.Component ;
import java.util.HashMap ;
import java.util.Vector ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Arrays ;
import java.io.InputStreamReader ;
import java.io.InputStream ;
import java.io.StringReader ;
import java.io.IOException ;
import java.net.URL ;

import gemini.sp.SpItem ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.obsComp.SpTelescopeObsComp ;
import orac.util.TelescopeUtil ;
import orac.jcmt.inst.SpInstHeterodyne ;
import edfreq.HeterodyneEditor ;
import edfreq.LineCatalog ;
import edfreq.SideBandDisplay ;
import edfreq.FrequencyEditorCfg ;
import edfreq.Receiver ;
import edfreq.BandSpec ;
import edfreq.Transition ;
import edfreq.SelectionList ;
import edfreq.LineDetails ;
import jsky.app.ot.editor.OtItemEditor ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetWatcher ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher ;
import jsky.app.ot.gui.TextBoxWidgetExt ;

import jsky.app.ot.gui.TextBoxWidgetWatcher ;

import org.apache.xerces.parsers.DOMParser ;
import org.xml.sax.InputSource ;
import org.xml.sax.SAXException ;
import org.xml.sax.SAXNotRecognizedException ;
import org.xml.sax.SAXNotSupportedException ;
import org.w3c.dom.Document ;
import org.w3c.dom.Node ;
import org.w3c.dom.NodeList ;

import gemini.sp.SpAvEditState ;
import gemini.util.ObservingToolUtilities ;

//------- NOTES -------------
//
// Transition Strings is GUI and SpInstHeterodyne
// 
// All the transitions Strings in the LineCatalog end with a white space " ".
// The XML methods of SpItem remove this white space when SpInstHeterodyne is
// saved to XML and read back in again. But when the setSelectedItem() method of
// the _w.transitionChoice JComboBox is called with this trimmed String it wound find
// the right String to select.
// It therefore important to make sure that an white space is added to the
// transition String obtained via _inst.getTransition(0) before it is used
// in the GUI, e.g.
//   _w.transitionChoice.setSelectedItem(getObject(_w.transitionChoice, _inst.getTransition(0) + " ")) ;
//
// And before a transition String fron the GUI is saved to the _inst the white space
// should be removed by trimming the String, e.g.
//  _inst.setTransition(_w.transitionChoice.getSelectedItem().toString().trim(), 0) ;
//
// --------------------------------------------------------

/**
 * Heterodyne Editor.
 *
 * This class implements the HeterodyneEditor interface. Its implementation is based
 * on the old edfreq.FrontEnd class which is not used anymore.
 *
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class EdCompInstHeterodyne extends OtItemEditor implements ActionListener , HeterodyneEditor
{
	private LineCatalog _lineCatalog ;
	private SideBandDisplay _frequencyEditor ;

	/**
	 * A static configuration object which can used by classes throughout this
	 * package to configure themselves.
	 */
	protected static FrequencyEditorCfg _cfg = FrequencyEditorCfg.getConfiguration() ;
	private SpInstHeterodyne _inst ;
	boolean _hidingFrequencyEditor = false ;
	private HeterodyneGUI _w ; // the GUI layout
	private Document doc = null ;

	// Arrays of component names
	HashMap<String,Integer> feWidgetNames = new HashMap<String,Integer>() ;
	HashMap<String,Integer> modeWidgetNames = new HashMap<String,Integer>() ;
	HashMap<String,Integer> regionWidgetNames = new HashMap<String,Integer>() ;
	HashMap<String,Integer> mixerWidgetNames = new HashMap<String,Integer>() ;
	HashMap<String,Integer> sidebandWidgetNames = new HashMap<String,Integer>() ;
	HashMap<String,Integer> freqPanelWidgetNames = new HashMap<String,Integer>() ;
	HashMap<String,Integer> vPanelWidgetNames = new HashMap<String,Integer>() ;

	// Current receiver
	Receiver _receiver ;

	// Information related to each possible spectral region
	@SuppressWarnings( "unchecked" )
	Vector<Object>[] _regionInfo = new Vector[ Integer.parseInt( HeterodyneGUI.SUBSYSTEMS[ HeterodyneGUI.SUBSYSTEMS.length - 1 ] ) ] ;
	boolean defaultToRadialVelocity = true ;
	String currentFrequency = "" ;
	Component[] components = null ;
	boolean configured = false ;
	static String LSB = "lsb" ;
	static String USB = "usb" ;
	static String BEST = "best" ;

	private static final Color myRed = new Color( 255 , 209 , 186 ) ;
	private static final Color myYellow = new Color( 255 , 249 , 182 ) ;
	private static final Color myBlue = new Color( 200 , 217 , 255 ) ;
	private static final Color myGreen = new Color( 200 , 255 , 200 ) ;

	public EdCompInstHeterodyne()
	{
		_title = "JCMT Heterodyne" ;
		_presSource = _w = new HeterodyneGUI() ;
		_description = "The Heterodyne instrument is configured with this component." ;

		components = _w.bandwidthsPanel.getComponents() ;

		try
		{
			_lineCatalog = LineCatalog.getInstance() ;
		}
		catch( Exception e ){}

		if( _frequencyEditor == null )
		{
			_frequencyEditor = new SideBandDisplay( this ) ;

			_frequencyEditor.addComponentListener( new ComponentAdapter()
			{
				public void componentHidden( ComponentEvent e )
				{
					// If the user has deliberately closed the window without using the hide button, this will
					// do the same except we won't get the latest configuration
					if( !_hidingFrequencyEditor )
					{
						enableNamedWidgets( true ) ;
						_updateWidgets() ;
					}
				}
			} ) ;
		}

		// Add listeners to stuff on the front end configuration panel
		for( int i = 0 ; i < _w.feSelector.getComponentCount() ; i++ )
		{
			if( _w.feSelector.getComponent( i ) instanceof JRadioButton )
			{
				(( JRadioButton )_w.feSelector.getComponent( i )).addActionListener( new ActionListener()
				{
					public void actionPerformed( ActionEvent e )
					{
						feAction( e ) ;
					}
				} ) ;
				feWidgetNames.put( _w.feSelector.getComponent( i ).getName() , new Integer( i ) ) ;
			}
		}

		for( int i = 0 ; i < _w.modeSelector.getComponentCount() ; i++ )
		{
			if( _w.modeSelector.getComponent( i ) instanceof JRadioButton )
			{
				(( JRadioButton )_w.modeSelector.getComponent( i )).addActionListener( new ActionListener()
				{
					public void actionPerformed( ActionEvent e )
					{
						modeAction( e ) ;
					}
				} ) ;
				modeWidgetNames.put( _w.modeSelector.getComponent( i ).getName() , new Integer( i ) ) ;
			}
		}

		for( int i = 0 ; i < _w.regionSelector.getComponentCount() ; i++ )
		{
			if( _w.regionSelector.getComponent( i ) instanceof JRadioButton )
			{
				(( JRadioButton )_w.regionSelector.getComponent( i )).addActionListener( new ActionListener()
				{
					public void actionPerformed( ActionEvent e )
					{
						regionAction( e ) ;
					}
				} ) ;
				regionWidgetNames.put( _w.regionSelector.getComponent( i ).getName() , new Integer( i ) ) ;
			}
		}

		for( int i = 0 ; i < _w.sbSelector.getComponentCount() ; i++ )
		{
			if( _w.sbSelector.getComponent( i ) instanceof JRadioButton )
			{
				(( JRadioButton )_w.sbSelector.getComponent( i )).addActionListener( new ActionListener()
				{
					public void actionPerformed( ActionEvent e )
					{
						sbSelectAction( e ) ;
					}
				} ) ;
				sidebandWidgetNames.put( _w.sbSelector.getComponent( i ).getName() , new Integer( i ) ) ;
			}
		}

		/*
		 * Add other listeners to the components of the frequency and button panels. 
		 * We don't need to add anything to the velocity panel since it is for show only.
		 */
		for( int componentIndex = 0 ; componentIndex < components.length ; componentIndex++ )
		{
			JComboBox component = ( JComboBox )components[ componentIndex ] ;
			component.addActionListener( this ) ;
		}
		for( int i = 0 ; i < _w.fPanel.getComponentCount() ; i++ )
		{
			// The fPanel contains a mix of JCombBoxes
			// buttons and textfields. We only register
			// interest in the first two.
			if( _w.fPanel.getComponent( i ) instanceof AbstractButton )
			{
				(( AbstractButton )_w.fPanel.getComponent( i )).addActionListener( this ) ;
				freqPanelWidgetNames.put( _w.fPanel.getComponent( i ).getName() , new Integer( i ) ) ;
			}
			else if( _w.fPanel.getComponent( i ) instanceof JComboBox )
			{
				(( JComboBox )_w.fPanel.getComponent( i )).addActionListener( this ) ;
				freqPanelWidgetNames.put( _w.fPanel.getComponent( i ).getName() , new Integer( i ) ) ;
			}
			else if( _w.fPanel.getComponent( i ) instanceof JTextField )
			{
				(( JTextField )_w.fPanel.getComponent( i )).addKeyListener( new KeyAdapter()
				{
					public void keyTyped( KeyEvent ke )
					{
						freqAction() ;
					}
				} ) ;
				freqPanelWidgetNames.put( _w.fPanel.getComponent( i ).getName() , new Integer( i ) ) ;
			}
		}

		_w.specialConfigs.setModel( getSpecialConfigsModel() ) ;
		_w.specialConfigs.addActionListener( this ) ;

		for( int i = 0 ; i < _w.vPanel.getComponentCount() ; i++ )
		{
			String name = _w.vPanel.getComponent( i ).getName() ;
			if( name != null )
				vPanelWidgetNames.put( name , new Integer( i ) ) ;
		}
		// Initially disable the accept button
		toggleEnabled( _w.fPanel , "Accept" , false ) ;

		//		 The button panel only contains buttons, so just add action listeners
		for( int i = 0 ; i < _w.bPanel.getComponentCount() ; i++ )
			(( AbstractButton )_w.bPanel.getComponent( i )).addActionListener( this ) ;

		// Disable the hide button...It should only be enabled when the frequency editor is visible
		toggleEnabled( _w.bPanel , "hide" , false ) ;

		// Add an ancestor listener. This will be invoked when a user changes to another component, and will force the accept button to be pressed.
		_w.addAncestorListener( new AncestorListener()
		{
			public void ancestorAdded( AncestorEvent e ){}

			public void ancestorMoved( AncestorEvent e ){}

			public void ancestorRemoved( AncestorEvent e )
			{
				clickButton( _w.fPanel , "Accept" ) ;
			}
		} ) ;

		_w.velocity.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbwe )
			{
				_inst.setVelocity( tbwe.getText() ) ;
				toggleEnabled( _w.fPanel , "Accept" , true ) ;
				_w.velocity.setForeground( Color.RED ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbwe ){}
		} ) ;

		_w.vDef.setChoices( TelescopeUtil.TCS_RV_DEFINITIONS ) ;
		_w.vDef.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String newTag )
			{
				_inst.setVelocityDefinition( newTag ) ;
				if( SpInstHeterodyne.RADIAL_VELOCITY_REDSHIFT.equals( newTag ) )
				{
					_w.velLabel.setText( "Velocity / Redshift" ) ;
					_inst.setVelocityFrame( SpInstHeterodyne.BARYCENTRIC_VELOCITY_FRAME ) ;
				}
				else
				{
					_w.velLabel.setText( "Velocity" ) ;

					if( SpInstHeterodyne.RADIAL_VELOCITY_RADIO.equals( newTag ) )
						_inst.setVelocityFrame( SpInstHeterodyne.LSRK_VELOCITY_FRAME ) ;
					else if( SpInstHeterodyne.RADIAL_VELOCITY_OPTICAL.equals( newTag ) )
						_inst.setVelocityFrame( SpInstHeterodyne.HELIOCENTRIC_VELOCITY_FRAME ) ;
				}
				moreSetUp() ;
				update() ;
			}
		} ) ;

		_w.vFrame.setChoices( TelescopeUtil.TCS_RV_FRAMES ) ;
		_w.vFrame.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String newTag )
			{
				_inst.setVelocityFrame( newTag ) ;
				update() ;
			}
		} ) ;

		_w.defaultToRadial.addWatcher( new CheckBoxWidgetWatcher()
		{
			public void checkBoxAction( CheckBoxWidgetExt cbwe )
			{
				defaultToRadialVelocity = cbwe.getBooleanValue() ;
				doCheckBox() ;
				moreSetUp() ;
				update() ;
			}
		} ) ;

		_w.table.setDefaultRenderer( Object.class , new TableRowRenderer() ) ;

	}

	private void doCheckBox()
	{
		_w.velLabel.setEnabled( !defaultToRadialVelocity ) ;
		_w.velocity.setEnabled( !defaultToRadialVelocity ) ;
		_w.vdLabel.setEnabled( !defaultToRadialVelocity ) ;
		_w.vDef.setEnabled( !defaultToRadialVelocity ) ;
		_w.vfLabel.setEnabled( !defaultToRadialVelocity ) ;
		_w.vFrame.setEnabled( !defaultToRadialVelocity ) ;

		if( defaultToRadialVelocity )
		{
			_inst.getTable().rm( SpInstHeterodyne.ATTR_VELOCITY ) ;
			_inst.getTable().rm( SpInstHeterodyne.ATTR_VELOCITY_DEFINITION ) ;
			_inst.getTable().rm( SpInstHeterodyne.ATTR_VELOCITY_FRAME ) ;
		}
		else
		{
			_inst.setVelocity( _w.velocity.getText() ) ;
			_inst.setVelocityDefinition( _w.vDef.getStringValue() ) ;
			_inst.setVelocityFrame( _w.vFrame.getStringValue() ) ;
		}
	}

	/** Initialises the default values in SpInstHeterodyne. */
	private void _initialiseInstHeterodyne()
	{
		String frontEndName = _cfg.frontEnds[ 1 ] ;
		_receiver = _cfg.receivers.get(frontEndName);
		BandSpec bandSpec = _receiver.bandspecs.get(0);

		// Get hold of the lines in the upper side-band that. Make sure it is not to close to
		// the edge of the range so that the IF can default to the front-end IF.
		// One of the lines should be a CO transition. Find it it use it as default line.
		Vector<SelectionList> moleculeVector = _lineCatalog.returnSpecies( _receiver.loMin + _receiver.feIF + _receiver.bandWidth , _receiver.loMax - _receiver.bandWidth ) ;
		String molecule = "CO" ;
		String transitionName = "" ;
		String frequency = "" ;

		Transition transition = null ;

		for( int i = 0 ; i < moleculeVector.size() ; i++ )
		{
			if( moleculeVector.get( i ).toString().trim().equals( molecule ) )
				transition = moleculeVector.get(i).objectList.get(0);
		}

		if( transition != null )
		{
			/*
			 * Whenever a transition is taken from the LineCatalog and consequently saved to
			 * it has to be trimmed in order to remove the trailing white space that each transition
			 * in the LineCatalog has.
			 */
			transitionName = transition.name.trim() ;
			frequency = "" + transition.frequency ;
		}

		_inst.initialiseValues( "acsis" , // Back end name
		frontEndName , // front end
		_cfg.frontEndTable.get( frontEndName)[0], // mode
		bandSpec.toString() , // band mode
		_cfg.frontEndMixers.get(frontEndName)[0], // mixer
		"" + bandSpec.defaultOverlaps[ 0 ] , // overlap
		BEST , // band
		"" + _receiver.feIF , // centre frequency
		"" + _receiver.feIF , // centre frequency
		"" + bandSpec.getDefaultOverlapBandWidths()[ 0 ] , // bandwidth
		molecule , // molecule
		transitionName , // transition
		frequency // rest frequency
		) ;
	}

	/**
	 * Override setup to store away a reference to the SpInst item.
	 */
	public void setup( SpItem spItem )
	{
		_inst = ( SpInstHeterodyne )spItem ;
		super.setup( spItem ) ;
		moreSetUp() ;
		initialisedRegion = false ;
	}

	private void moreSetUp()
	{
		setAvailableModes() ;
		setAvailableRegions() ;
		setAvailableMolecules() ;
		setAvailableTransitions() ;
		setAvailableSidebands() ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		boolean cachedConfigured = configured ;
		configured = false ;
		if( !_inst.valuesInitialised() )
		{
			_initialiseInstHeterodyne() ;
			// Make sure we click the front end button to set things up correctly
			clickButton( _w.feSelector , _inst.getFrontEnd() ) ;
			_initialiseRegionInfo() ;
		}

		defaultToRadialVelocity = !_inst.getTable().exists( SpInstHeterodyne.ATTR_VELOCITY ) ;
		_w.defaultToRadial.setValue( defaultToRadialVelocity ) ;

		int compNum = freqPanelWidgetNames.get( "frequency" ) ;
		JTextField tf = ( JTextField )_w.fPanel.getComponent( compNum ) ;
		currentFrequency = tf.getText() ;

		_receiver = _cfg.receivers.get(_inst.getFrontEnd());

		// Update the front end panel
		(( AbstractButton )_w.feSelector.getComponent( feWidgetNames.get( _inst.getFrontEnd() ) )).setSelected( true ) ;
		(( AbstractButton )_w.modeSelector.getComponent( modeWidgetNames.get( _inst.getMode() ) )).setSelected( true ) ;
		int integer = 0 ;
		String band = _inst.getBandMode() ;
		integer = Integer.parseInt( band ) ;
		if( integer > 0 )
			integer-- ;

		(( AbstractButton )_w.regionSelector.getComponent( integer )).setSelected( true ) ;

		(( AbstractButton )_w.sbSelector.getComponent( sidebandWidgetNames.get( _inst.getBand() ) )).setSelected( true ) ;

		// Update the bandwidth
		_updateBandwidths() ;

		int active = new Integer( band ) ;
		for( int componentIndex = 0 ; componentIndex < components.length ; componentIndex++ )
		{
			JComboBox component = ( JComboBox )components[ componentIndex ] ;
			component.setEnabled( componentIndex < active ) ;
		}

		// Update the summary panel
		for( int i = 0 ; i < _w.summaryPanel.getComponentCount() ; i++ )
		{
			Component c = _w.summaryPanel.getComponent( i ) ;
			String name = c.getName() ;
			if( name != null )
			{
				if( name.equals( "LowFreqLimit" ) )
				{
					(( JLabel )c).setText( "" + ( int )( _receiver.loMin * 1.E-9 ) ) ;
				}
				else if( name.equals( "HighFreqLimit" ) )
				{
					(( JLabel )c).setText( "" + ( int )( _receiver.loMax * 1.E-9 ) ) ;
				}
				else if( name.equals( "resolution" ) )
				{
					double resolution = ( _inst.getBandWidth( i ) * 1.E-3 ) / _inst.getChannels( i ) ;
					resolution = new Integer( _inst.getMixer() ) * resolution ;
					(( JLabel )c).setText( "" + ( int )Math.rint( resolution ) ) ;
				}
				else if( name.equals( "overlap" ) )
				{
					(( JLabel )c).setText( "" + ( _inst.getOverlap( i ) * 1.E-6 ) ) ;
				}
				else if( name.equals( "channel" ) )
				{
					(( JLabel )c).setText( "" + _inst.getChannels( i ) ) ;
				}
			}
		}

		// Update the velocity field
		// First we need to see if we can find a target component somewhere in scope
		SpTelescopeObsComp target = SpTreeMan.findTargetList( _inst ) ;
		String rv ;
		String rvDefn ;
		String rvFrame ;
		if( target != null && defaultToRadialVelocity )
		{
			SpTelescopePos tp = target.getPosList().getBasePosition();
			rv = tp.getTrackingRadialVelocity() ;
			rvDefn = tp.getTrackingRadialVelocityDefn() ;
			rvFrame = tp.getTrackingRadialVelocityFrame() ;
		}
		else
		{
			rv = "" + _inst.getVelocity() ;
			rvDefn = _inst.getVelocityDefinition() ;
			rvFrame = _inst.getVelocityFrame() ;
		}

		// Now get the components on the velocity panel and set them to the new values
		for( int i = 0 ; i < _w.vPanel.getComponentCount() ; i++ )
		{
			Component c = _w.vPanel.getComponent( i ) ;
			String name = c.getName() ;
			if( name != null )
			{
				if( name.equals( "velocity" ) )
					(( JTextField )c).setText( rv ) ;
				else if( name.equals( "definition" ) && rvDefn != null )
					_w.vDef.setSelectedItem( rvDefn ) ;
				else if( name.equals( "frame" ) && rvFrame != null )
					_w.vFrame.setSelectedItem( rvFrame ) ;
			}
		}

		String selectedConfig = ( String )_w.specialConfigs.getSelectedItem() ;
		String namedConfig = _inst.getNamedConfiguration() ;
		// if null set to first item which *should* be "None" 
		if( namedConfig == null )
			namedConfig = ( String )_w.specialConfigs.getItemAt( 0 ) ;
		if( !selectedConfig.equals( namedConfig ) )
		{
			_w.specialConfigs.removeActionListener( this ) ;
			_w.specialConfigs.setSelectedItem( namedConfig ) ;
			_w.specialConfigs.addActionListener( this ) ;
			cachedConfigured = true ;
		}

		doCheckBox() ;

		// Make sure the molecule is accessible
		try
		{
			_doVelocityChecks() ;
		}
		catch( Exception e )
		{
			// The veolcity is invalid, so set the text appropriately
			Component vWidget ;
			Iterator<String> iter = vPanelWidgetNames.keySet().iterator() ;
			while( iter.hasNext() )
			{
				vWidget = _w.vPanel.getComponent( vPanelWidgetNames.get( iter.next() ) ) ;
				if( vWidget instanceof JTextField )
					(( JTextField )vWidget).setText( "Invalid" ) ;
				else if( vWidget instanceof JLabel )
					(( JLabel )vWidget).setText( "Invalid" ) ;
			}
		}

		_updateMoleculeChoice() ;
		_adjustCentralFrequencies() ;

		_updateRegionInfo() ;

		_updateTable() ;

		_updateFrequencyText() ;

		configured = cachedConfigured ;
	}

	private void _checkEditsWhenConfigured()
	{
		if( configured )
		{
			_inst.removeNamedConfiguration() ;
			_w.specialConfigs.removeActionListener( this ) ;
			_w.specialConfigs.setSelectedIndex( 0 ) ;
			_w.specialConfigs.addActionListener( this ) ;
			configured = false ;
		}
	}

	public void actionPerformed( ActionEvent ae )
	{
		Object source = ae.getSource() ;

		if( source == _w.specialConfigs )
		{
			// If the user has selected None
			if( _w.specialConfigs.getSelectedIndex() == 0 )
			{
				configured = false ;
				_inst.removeNamedConfiguration() ;
				configureFrequencyEditor() ;
			}
			else
			{
				String selectedConfig = ( String )_w.specialConfigs.getSelectedItem() ;
				_inst.setNamedConfiguration( selectedConfig ) ;
				ConfigurationInformation ci = getConfigFor( selectedConfig ) ;
				if( ci == null )
					return ;

				clickButton( _w.feSelector , ci.$feName ) ;
				clickButton( _w.sbSelector , ci.$sideBand.toLowerCase() ) ;
				clickButton( _w.modeSelector , ci.$mode.toLowerCase() ) ;
				clickButton( _w.regionSelector , "" + ci.$subSystems ) ;
				for( int componentIndex = 0 ; componentIndex < components.length ; componentIndex++ )
				{
					JComboBox component = ( JComboBox )components[ componentIndex ] ;
					component.removeActionListener( this ) ;
					if( componentIndex < ci.$bandWidths.size() )
					{
						Object object = ci.$bandWidths.get( componentIndex ) ;
						if( object != null && ( object instanceof String ) )
						{
							String bandWidthString = ( String )object ;
							component.setSelectedItem( bandWidthString ) ;
							double bandwidth = new Double( bandWidthString ) ;
							if( bandwidth != 0. )
								_inst.setBandWidth( bandwidth / 1.E-6 , componentIndex ) ;
						}
						else
						{
							component.setSelectedIndex( 0 ) ;
						}
					}
					else
					{
						int index = component.getSelectedIndex() ;
						if( index != 0 )
							component.setSelectedIndex( 0 ) ;
					}
					component.addActionListener( this ) ;
				}

				if( ci.$species.size() > 0 )
				{
					for( int index = 0 ; index < ci.$subSystems ; index++ )
					{
						String species = ci.$species.get( index ) ;
						_inst.setMolecule( species , index ) ;
						String speciesTransition = ci.$transition.get( index ) ;
						_inst.setTransition( speciesTransition , index ) ;
						_updateTransitionChoice() ;

						Vector<SelectionList> moleculeVector = getSpecies() ;

						Transition transition = null ;

						for( int i = 0 ; i < moleculeVector.size() ; i++ )
						{
							SelectionList selectionList = moleculeVector.get( i ) ;
							if( selectionList.toString().trim().equals( species ) )
							{
								for( int j = 0 ; j < selectionList.objectList.size() ; j++ )
								{
									transition = selectionList.objectList.get(j);
									String transitionName = transition.name.trim() ;
									if( transitionName.equals( speciesTransition ) )
										break ;
									transition = null ;
								}
								break ;
							}
						}

						if( transition != null )
						{
							double transitionFrequency = transition.frequency ;
							_inst.setRestFrequency( transitionFrequency , index ) ;
						}
						checkSideband() ;
					}
					_updateRegionInfo() ;
				}
				else
				{
					// Set the rest frequency text...
					int freqCompNum = freqPanelWidgetNames.get( "frequency" ) ;
					JTextField tf = ( JTextField )_w.fPanel.getComponent( freqCompNum ) ;
					tf.setText( ci.$freq.toString() ) ;

					for( int index = 0 ; index < ci.$subSystems ; index++ )
					{
						double frequency = ci.$freq.elementAt( index ) ;
						_inst.setRestFrequency( frequency * 1.E9 , index ) ;
						_inst.setMolecule( NO_LINE , index ) ;
						_inst.setTransition( NO_LINE , index ) ;
					}
					checkSideband() ;
				}
				clickButton( _w.fPanel , "Accept" ) ;
				_updateCentralFrequenciesFromShifts( ci.$shifts ) ;
				configureFrequencyEditor( ci.$shifts ) ;
				configured = true ;
			}
			_updateWidgets() ;
			return ;
		}

		_checkEditsWhenConfigured() ;

		for( int componentIndex = 0 ; componentIndex < components.length ; componentIndex++ )
		{
			JComboBox component = ( JComboBox )components[ componentIndex ] ;
			if( source == component )
			{
				_inst.setBandWidth( Double.parseDouble( ( String )component.getSelectedItem() ) * 1.E6 , componentIndex ) ;
				setAvailableRegions() ;
				_updateRegionInfo() ;
				_updateWidgets() ;
				return ;
			}
		}

		try
		{
			String name = ( ( Component )source ).getName() ;
			if( name.equals( "molecule" ) )
			{
				// Set the current molecule and update the transitions
				for( int index = 0 ; index < _regionInfo.length ; index++ )
				{
					_inst.setCentreFrequency( _receiver.feIF , index ) ;
					_inst.setMolecule( (( JComboBox )ae.getSource()).getSelectedItem().toString() , index ) ;
				}
				_updateTransitionChoice() ;
				double restFreq = _inst.getRestFrequency( 0 ) ;
				for( int index = 0 ; index < _regionInfo.length ; index++ )
					_inst.setRestFrequency( restFreq , index ) ;
				checkSideband() ;
				_updateRegionInfo() ;
			}
			else if( name.equals( "transition" ) )
			{
				// Update the frequency information
				Object transition = (( JComboBox )ae.getSource()).getSelectedItem() ;
				if( transition instanceof Transition )
				{
					double frequency = (( Transition )transition).frequency ;
					_inst.setSkyFrequency( frequency / ( 1. + getRedshift() ) ) ;
					for( int index = 0 ; index < _regionInfo.length ; index++ )
					{
						// Set the current transition
						_inst.setCentreFrequency( _receiver.feIF , index ) ;
						_inst.setTransition( transition.toString() , index ) ;
						_inst.setRestFrequency( frequency , index ) ;
					}
					_updateFrequencyText( frequency / 1.E9 ) ;
					checkSideband() ;
				}
				_updateRegionInfo() ;
			}
			else if( name.equals( "Accept" ) )
			{
				// Set the current Rest Frequency
				// Get the text field widget
				int compNum = freqPanelWidgetNames.get( "frequency" ) ;
				JTextField tf = ( JTextField )_w.fPanel.getComponent( compNum ) ;
				String frequency = tf.getText() ;
				boolean changed = !currentFrequency.equals( frequency ) ;
				try
				{
					if( changed )
					{
						double f = Double.parseDouble( frequency ) * 1.E9 ;
						double obsmin = _receiver.loMin - _receiver.feIF - ( _receiver.bandWidth * .5 ) ;
						double obsmax = _receiver.loMax + _receiver.feIF + ( _receiver.bandWidth * .5 ) ;
						if( f >= obsmin && f <= obsmax )
						{
							_inst.setSkyFrequency( f / ( 1. + getRedshift() ) ) ;
							for( int index = 0 ; index < _regionInfo.length ; index++ )
							{
								_inst.setRestFrequency( f , index ) ;
								_inst.setCentreFrequency( _receiver.feIF , index ) ;
								// Set the molecule and trasition to NO_LINE
								_inst.setMolecule( NO_LINE , index ) ;
								_inst.setTransition( NO_LINE , index ) ;
							}
							checkSideband() ;
						}
					}
					_updateMoleculeChoice() ;
					_updateRegionInfo() ;
				}
				catch( Exception e ){}
				tf.setForeground( Color.BLACK ) ;
				_w.velocity.setForeground( Color.BLACK ) ;
				toggleEnabled( _w.fPanel , "Accept" , false ) ;
			}
			else if( name.equals( "show" ) )
			{
				checkSideband() ;
				configureFrequencyEditor() ;
				enableNamedWidgets( false ) ;
				_frequencyEditor.setVisible( true ) ;
			}
			else if( name.equals( "hide" ) )
			{
				hideFreqEditor() ;
			}
			else
			{
				System.out.println( "Unknown source for action: " + name ) ;
			}

		}
		catch( Exception e )
		{
			// Ignore for now
		}

		_updateWidgets() ;
	}

	public void hideFreqEditor()
	{
		_hidingFrequencyEditor = true ;
		getFrequencyEditorConfiguration() ;
		enableNamedWidgets( true ) ;
		_updateRegionInfo() ;
		_frequencyEditor.setVisible( false ) ;
		_hidingFrequencyEditor = false ;
		_updateTable() ;
	}

	private void _adjustCentralFrequencies()
	{
		String band = _inst.getBand() ;
		// Get current space of first system
		double mainline = _inst.getRestFrequency( 0 ) ;
		double centre = _inst.getCentreFrequency( 0 ) ;
		// Check if it is correctly located
		double halfReceverBandwidth = _receiver.bandWidth * .5 ;
		if( centre <= _receiver.feIF - halfReceverBandwidth || centre >= _receiver.feIF + halfReceverBandwidth )
		{
			centre = _receiver.feIF ;
			_inst.setCentreFrequency( centre , 0 ) ;
			// Make an educated guess as to wether this program has been just been opened
			if( _inst.getRootItem().getAvEditState() == SpAvEditState.UNEDITED )
				JOptionPane.showMessageDialog( null , "The Central Frequency for this set up was invalid.\n It is advised that you save this program before use." , "Central Frequency Reset" , JOptionPane.WARNING_MESSAGE ) ;
		}
		if( centre == _receiver.feIF )
		{
			double obsmin = _receiver.loMin - _receiver.feIF - halfReceverBandwidth ;
			double obsmax = _receiver.loMax + _receiver.feIF + halfReceverBandwidth ;
			if( LSB.equals( band ) && mainline < obsmin + halfReceverBandwidth && !( mainline < obsmin ) )
			{
				double currentPosition = obsmin + halfReceverBandwidth ;
				centre = centre - ( mainline - currentPosition ) ;
				_inst.setCentreFrequency( centre , 0 ) ;
			}
			else if( ( USB.equals( band ) || BEST.equals( band ) ) && mainline > obsmax - halfReceverBandwidth && !( mainline > obsmax ) )
			{
				double currentPosition = obsmax - halfReceverBandwidth ;
				centre = centre + ( mainline - currentPosition ) ;
				_inst.setCentreFrequency( centre , 0 ) ;
			}
		}
		// Adjust remaining systems accordingly
		int available = new Integer( _inst.getBandMode() ) ;
		for( int index = 1 ; index < available ; index++ )
		{
			double line = _inst.getRestFrequency( index ) ;
			if( USB.equals( band ) || BEST.equals( band ) )
				line = centre - ( mainline - line ) ;
			else
				line = centre + ( mainline - line ) ;
			if( line < 0. )
				line = -line ;

			double halfBandwidth = .5 * ( _receiver.bandWidth - _inst.getBandWidth( index ) ) ;
			boolean outOfBand = false;

			if( line > ( _receiver.feIF + halfBandwidth ) ) {
				line = ( _receiver.feIF + halfBandwidth ) ;
				outOfBand = true;
			}
			else if( line < ( _receiver.feIF - halfBandwidth ) ) {
				line = ( _receiver.feIF - halfBandwidth ) ;
				outOfBand = true;
			}

			_inst.setCentreFrequency( line , index ) ;

			if (outOfBand) {
				JOptionPane.showMessageDialog(null,
					"The central frequency for spectral region " + index
					+ " is outside the tuning range of the receiver.\nIt is advised that you reconfigure this spectral region and save this program before use.",
					"Central Frequency Reset", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	private void _updateCentralFrequenciesFromShifts( Vector<Double> shifts )
	{
		for( int index = 0 ; index < shifts.size() ; index++ )
		{
			double frequency = shifts.elementAt( index ) ;
			if( frequency != 0. )
				_inst.setCentreFrequency( "" + frequency , index ) ;
		}
	}

	public void feAction( ActionEvent ae )
	{
		_checkEditsWhenConfigured() ;

		if( ae != null )
		{
			// This has been called as a response to a user action
			String feSelected = ( ( JRadioButton )ae.getSource() ).getText() ;
			_receiver = _cfg.receivers.get(feSelected);
			_inst.setFrontEnd( feSelected ) ;
			_inst.setFeIF( _receiver.feIF ) ;
			_inst.setFeBandWidth( _receiver.bandWidth ) ;
			setAvailableModes() ;
			setAvailableRegions() ;
			setAvailableMolecules() ;
			setAvailableTransitions() ;
			setAvailableSidebands() ;
		}
		else
		{
			// This has been called as a result of a call from another action event
		}
		_updateWidgets() ;
	}

	public void modeAction( ActionEvent ae )
	{
		_checkEditsWhenConfigured() ;

		if( ae != null )
		{
			// User action
			String mode = ( ( JRadioButton )ae.getSource() ).getText() ;
			_inst.setMode( mode ) ;

			// Update the band width choices
		}
		_updateWidgets() ;
	}

	public void sbSelectAction( ActionEvent ae )
	{
		_checkEditsWhenConfigured() ;

		if( ae != null )
		{
			String sb = (( JRadioButton )ae.getSource()).getText() ;

			if( !validSideband( sb ) )
				JOptionPane.showMessageDialog( null , "Frequency cannot be reached from " + sb + "." , "Frequency no longer valid for Sideband!" , JOptionPane.WARNING_MESSAGE ) ;
			
			// If we are switching between upper and lower sidebands we need to alter the centre frequency for higher level subsytems
			if( Integer.parseInt( _inst.getBandMode() ) > 1 )
			{
				String currentBand = _inst.getBand() ;
				if( currentBand.equals( BEST ) && sb.equals( USB ) || currentBand.equals( USB ) && sb.equals( BEST ) )
				{
					// Do nothing since 'best' is equivalent to 'usb' as far as the OT is concerned
				}
				else
				{
					double receverfeIF = _receiver.feIF ;
					for( int i = 0 ; i < Integer.parseInt( _inst.getBandMode() ) ; i++ )
					{
						double topCentreFreq = _inst.getCentreFrequency( i ) ;
						topCentreFreq = receverfeIF + ( receverfeIF - topCentreFreq ) ;
						_inst.setCentreFrequency( topCentreFreq , i ) ;
					}
				}
			}
			_inst.setBand( sb ) ;
		}
		_updateWidgets() ;
	}

	public void regionAction( ActionEvent ae )
	{
		_checkEditsWhenConfigured() ;

		if( ae != null )
		{
			String regions = (( JRadioButton )ae.getSource()).getText() ;
			_inst.setBandMode( regions ) ;
			int active = new Integer( regions ) ;

			for( int componentIndex = 0 ; componentIndex < components.length ; componentIndex++ )
			{
				JComboBox component = ( JComboBox )components[ componentIndex ] ;
				component.setEnabled( componentIndex < active ) ;
			}
		}
		_updateRegionInfo() ;
		_updateWidgets() ;
	}

	private void freqAction()
	{
		_checkEditsWhenConfigured() ;

		// Called when user changes the frequency manually
		toggleEnabled( _w.fPanel , "Accept" , true ) ;
		int compNum = freqPanelWidgetNames.get( "frequency" ) ;
		JTextField tf = ( JTextField )_w.fPanel.getComponent( compNum ) ;
		tf.setForeground( Color.RED ) ;
	}

	// Added by MFO (8 January 2002)
	/**
	 * Returns "usb" (Upper Side Band) or "lsb" (Lower Side Band).
	 *
	 * Needed by {@link edfreq.SideBand} to shift LO1 when top SideBands are changed.
	 */
	public String getFeBand()
	{
		return _inst.getBand() ;
	}

	public String getMode()
	{
		return _inst.getBandMode() ;
	}

	private void _updateBandwidths()
	{
		/*
		 *  We need to get the current bandspec
		 *  from the region selector
		 */
		Vector<BandSpec> bandSpecs = _receiver.bandspecs ;
		BandSpec currentBandSpec = null ;
		BandSpec activeBandSpec = null ;
		String bandMode = _inst.getBandMode() ;
		int active = new Integer( _inst.getBandMode() ) ;

		currentBandSpec = bandSpecs.get( active - 1 ) ;
		if( !currentBandSpec.toString().equals( bandMode ) )
			currentBandSpec = bandSpecs.get( 0 ) ;

		BandSpec otherBandSpec = null ;
		if( active == 3 )
			otherBandSpec = bandSpecs.get( 3 ) ;

		double[] values = null ;
		boolean showDialog = false ;
		
		/*
		 * Index into the new list to allow us to make sure 
		 * that it gets reselected if available
		 */
		int index = -1 ;
		double feOverlap = 0. ;

		double currentBandwidth ;

		for( int componentIndex = 0 ; componentIndex < components.length ; componentIndex++ )
		{
			// hack
			if( active == 3 )
			{
				if( otherBandSpec != null && componentIndex < 2 )
					activeBandSpec = otherBandSpec ;
				else
					activeBandSpec = currentBandSpec ;
			}
			else
			{
				if( componentIndex == 0 )
					activeBandSpec = currentBandSpec ;
			}
			values = activeBandSpec.getDefaultOverlapBandWidths() ;

			JComboBox component = ( JComboBox )components[ componentIndex ] ;
			component.removeActionListener( this ) ;
			int originalIndex = component.getSelectedIndex() ;

			component.removeAllItems() ;
			currentBandwidth = _inst.getBandWidth( componentIndex ) ;
			
			double lowestContestant = 3000. ;
			int notableIndex = -1 ;
			
			// Set the new bandwidths
			for( int i = 0 ; i < values.length ; i++ )
			{
				double value = Math.rint( values[ i ] * 1.E-6 ) ;
				if( values[ i ] == currentBandwidth )
				{
					index = i ;
					feOverlap = activeBandSpec.defaultOverlaps[ i ] ;
					_inst.setOverlap( feOverlap , componentIndex ) ;
					_inst.setChannels( activeBandSpec.getDefaultOverlapChannels()[ i ] , componentIndex ) ;
				}
				else
				{
					double contestant = Math.rint( currentBandwidth * 1.E-6 ) - value ;
					if( Math.abs( contestant ) < lowestContestant )
					{
						lowestContestant = contestant ;
						notableIndex = i ;
					}
				}
				component.addItem( "" + value ) ;
			}
			if( index != -1 )
			{
				component.setSelectedIndex( index ) ;
			}
			else if( notableIndex > -1 )
			{
				component.setSelectedIndex( notableIndex ) ;
				_inst.setBandWidth( values[ notableIndex ] , componentIndex ) ;
				_inst.setOverlap( activeBandSpec.defaultOverlaps[ 0 ] , componentIndex ) ;
				_inst.setChannels( activeBandSpec.getDefaultOverlapChannels()[ 0 ] , componentIndex ) ;
				if( componentIndex < active )
					JOptionPane.showMessageDialog( null , "Previous bandwidth for subsystem " + ( componentIndex + 1 ) + " not available ;\n Using " + component.getSelectedItem() + " as it is closest to previous value of " + Math.rint( currentBandwidth * 1.E-6 ) , "Bandwidth Reset" , JOptionPane.WARNING_MESSAGE ) ;
			}
			else
			{
				component.setSelectedIndex( 0 ) ;
				_inst.setBandWidth( values[ 0 ] , componentIndex ) ;
				_inst.setOverlap( activeBandSpec.defaultOverlaps[ 0 ] , componentIndex ) ;
				_inst.setChannels( activeBandSpec.getDefaultOverlapChannels()[ 0 ] , componentIndex ) ;
				if( originalIndex != 0 && currentBandwidth != 0. )
					showDialog = true ;
			}
			component.addActionListener( this ) ;
			index = -1 ;
		}
		if( showDialog )
			JOptionPane.showMessageDialog( null , "Previous bandwidth not available with new settings ;\n resetting to default" , "Bandwidth Reset" , JOptionPane.WARNING_MESSAGE ) ;			
	}

	private void _updateMoleculeChoice()
	{
		JComboBox molBox = null ;
		// First get the molecule checkbox from fPanel
		for( int i = 0 ; i < _w.fPanel.getComponentCount() ; i++ )
		{
			if( "molecule".equals( _w.fPanel.getComponent( i ).getName() ) )
			{
				molBox = ( JComboBox )_w.fPanel.getComponent( i ) ;
				break ;
			}
		}
		if( molBox == null )
			return ;

		// Remove the current actionlistener
		molBox.removeActionListener( this ) ;
		Object currentSelection = getObject( molBox , _inst.getMolecule( 0 ) ) ;
		String currentSpecies = null ;

		if( currentSelection != null )
			currentSpecies = currentSelection.toString() ;
		else
			currentSpecies = _inst.getMolecule( 0 ) ;

		// Get a new model to add to the molBox
		DefaultComboBoxModel specModel = new DefaultComboBoxModel( getSpecies() ) ;
		molBox.setModel( specModel ) ;
		// Add a"NO LINE option if one does not already exist
		if( ( ( DefaultComboBoxModel )molBox.getModel() ).getIndexOf( NO_LINE ) == -1 )
			molBox.addItem( NO_LINE ) ;

		// Go through the new model and see if:
		// (a) the same combination of species/transition is available or
		// (b) the same species is available.
		// In addition, we will actually set things up on the box
		// instead of leaving it to update widgets, since it means
		// we do not need to get the species list again
		if( currentSelection != null && specModel.getIndexOf( currentSelection ) >= 0 )
		{
			molBox.setSelectedIndex( specModel.getIndexOf( currentSelection ) ) ;
		}
		else if( NO_LINE.equals( currentSpecies ) )
		{
			molBox.setSelectedItem( NO_LINE ) ;
		}
		else
		{
			boolean match = false ;
			// See if the current species is available ;
			// don't use the last element of the model since this is just the no-line option
			if( currentSpecies != null )
			{
				for( int i = 0 ; i < specModel.getSize() - 1 ; i++ )
				{
					if( ( ( SelectionList )specModel.getElementAt( i ) ).toString().equals( currentSpecies ) )
					{
						match = true ;
						molBox.setSelectedIndex( i ) ;
						break ;
					}
				}
			}
			if( !match )
			{
				// Set the molecule to the first available one
				JOptionPane.showMessageDialog( _w , "Selecting new species ; old selection (" + currentSpecies + ") out of range" , "Molecule changed" , JOptionPane.WARNING_MESSAGE ) ;
				int integer = 0 ;
				try
				{
					integer = Integer.parseInt( _inst.getBandMode() ) ;
				}
				catch( NumberFormatException nfe ){}
				for( int i = 0 ; i < integer ; i++ )
					_inst.setMolecule( ( ( SelectionList )specModel.getElementAt( 0 ) ).toString() , i ) ;
				molBox.setSelectedIndex( 0 ) ;
			}
		}

		molBox.addActionListener( this ) ;
		_updateTransitionChoice() ;
	} // End of method

	private void _updateTransitionChoice()
	{
		JComboBox transBox = null ;
		// First get the molecule checkbox from fPanel
		for( int i = 0 ; i < _w.fPanel.getComponentCount() ; i++ )
		{
			if( "transition".equals( _w.fPanel.getComponent( i ).getName() ) )
			{
				transBox = ( JComboBox )_w.fPanel.getComponent( i ) ;
				break ;
			}
		}
		if( transBox == null )
			return ;

		// Remove the current actionlistener
		transBox.removeActionListener( this ) ;

		String currentTransition = _inst.getTransition( 0 ) ;
		if( currentTransition != null )
			currentTransition = currentTransition.trim() ;
		String currentMolecule = _inst.getMolecule( 0 ) ;
		if( currentMolecule == null )
		{
			transBox.addActionListener( this ) ;
			return ;
		}

		if( NO_LINE.equals( currentMolecule ) )
		{
			if( ( ( DefaultComboBoxModel )transBox.getModel() ).getIndexOf( NO_LINE ) == -1 )
				transBox.addItem( NO_LINE ) ;
			transBox.setSelectedItem( NO_LINE ) ;
			transBox.addActionListener( this ) ;
			for( int index = 0 ; index < _regionInfo.length ; index++ )
				_inst.setTransition( NO_LINE , index ) ;
			return ;
		}

		/*
		 * We need to get the current SelectionList based on the molecule.
		 * First get all the available species
		 * Get the new model
		 */
		Vector<SelectionList> speciesList = getSpecies() ;

		// Loop through this list to get the element corresponding to the current molecule
		SelectionList currentSpecies = null ;
		for( int i = 0 ; i < speciesList.size() ; i++ )
		{
			SelectionList currentSelectionList = speciesList.get( i ) ;
			if( currentSelectionList.toString().equals( currentMolecule ) )
			{
				currentSpecies = currentSelectionList ;
				break ;
			}
		}

		if( currentSpecies != null )
		{
			DefaultComboBoxModel specModel = new DefaultComboBoxModel( currentSpecies.objectList ) ;
			transBox.setModel( specModel ) ;

			// Add the no line option
			if( specModel.getIndexOf( NO_LINE ) == -1 )
				transBox.addItem( NO_LINE ) ;
		}

		// Check if the previous transition is still in range
		boolean inRange = false ;
		if( currentTransition != null )
		{
			for( int i = 0 ; i < transBox.getItemCount() ; i++ )
			{
				if( transBox.getItemAt( i ).toString().trim().equals( currentTransition ) )
				{
					inRange = true ;
					transBox.setSelectedIndex( i ) ;
					break ;
				}
			}
		}

		boolean transChanged = false ;
		if( !inRange )
		{
			// We need to set the transition to the first available
			// for the current species
			JOptionPane.showMessageDialog( null , "Transition Changed: " + currentTransition + " out of range." , "Transition Changed!" , JOptionPane.PLAIN_MESSAGE ) ;
			for( int index = 0 ; index < _regionInfo.length ; index++ )
			{
				_inst.setMolecule( currentMolecule , index ) ;
				Transition transition = ( Transition )transBox.getItemAt( 0 ) ;
				if( transition != null )
				{
					_inst.setTransition( transition.toString() , index ) ;
					_inst.setRestFrequency( transition.frequency , index ) ;
					_inst.setCentreFrequency( _receiver.feIF , index ) ;
					transBox.setSelectedIndex( 0 ) ;
				}
			}
			transChanged = true ;
		}

		double frequency = 0. ;
		if( transBox.getSelectedItem() instanceof Transition )
			frequency = ( ( Transition )transBox.getSelectedItem() ).frequency ;
		else
			frequency = _inst.getRestFrequency( 0 ) ;

		_inst.setRestFrequency( frequency , 0 ) ;
		_inst.setSkyFrequency( frequency / ( 1. + getRedshift() ) ) ;

		_updateFrequencyText( frequency / 1.E9 ) ;

		if( transChanged )
			_updateRegionInfo() ;

		transBox.addActionListener( this ) ;
	} // End of method

	private Vector<SelectionList> getSpecies()
	{
		double obsmin = _receiver.loMin - _receiver.feIF - ( _receiver.bandWidth * .5 ) ;
		double obsmax = _receiver.loMax + _receiver.feIF + ( _receiver.bandWidth * .5 ) ;
		Vector<SelectionList> molecules = _lineCatalog.returnSpecies( obsmin * ( 1. + getRedshift() ) , obsmax * ( 1. + getRedshift() ) ) ;
		return molecules ;
	}

	private void _updateFrequencyText()
	{
		_updateFrequencyText( _inst.getRestFrequency( 0 ) / 1.E9 ) ;
	}

	private void _updateFrequencyText( double f )
	{
		JTextField freq = null ;
		Iterator<String> iter = freqPanelWidgetNames.keySet().iterator() ;
		while( iter.hasNext() )
		{
			String name = iter.next() ;
			if( "frequency".equals( name ) )
			{
				// This is the component we want
				freq = ( JTextField )_w.fPanel.getComponent( freqPanelWidgetNames.get( name ) ) ;
				break ;
			}
		}
		if( freq == null )
			return ;

		freq.setText( "" + f ) ;
	}

	// See edfreq.HeterodyneEditor for documentation
	public double getRestFrequency( int subsystem )
	{
		return 0. ;
	}

	// See edfreq.HeterodyneEditor for documentation
	public double getObsFrequency( int subsystem )
	{
		return 0. ;
	}

	/** Get receiver's central IF. */
	public double getFeIF()
	{
		return 0. ;
	}

	public void updateCentreFrequency( double centre , int subsystem ){}

	public void updateBandWidth( double width , int subsystem ){}

	public void updateChannels( int channels , int subsystem ){}

	public void updateLineDetails( LineDetails lineDetails , int subsystem ){}

	public void updateLO1( double lo1 ){}

	public double getRedshift()
	{
		double velocity = 0. ;

		Component component = _w.vPanel.getComponent( vPanelWidgetNames.get( "velocity" ) ) ;
		// Get the velocity information
		String vText = "0.0" ;
		if( component instanceof JTextField )
			vText = ( ( JTextField )component ).getText() ;
		try
		{
			velocity = Double.parseDouble( vText ) ;
		}
		catch( NumberFormatException nfe )
		{
			return 0. ;
		}

		double c = SpInstHeterodyne.LIGHTSPEED ;

		component = _w.vPanel.getComponent( vPanelWidgetNames.get( "definition" ) ) ;
		String vDefn = "" ;
		if( component instanceof DropDownListBoxWidgetExt )
			vDefn = ( ( DropDownListBoxWidgetExt )component ).getStringValue() ;

		// if radio, else if optical
		if( vDefn.equals( TelescopeUtil.TCS_RV_DEFINITIONS[ 0 ] ) )
			velocity = ( c / ( c - velocity ) ) - 1. ;
		else if( vDefn.equals( TelescopeUtil.TCS_RV_DEFINITIONS[ 1 ] ) )
			velocity /= c ;

		return velocity ;
	}

	public double getCurrentBandwidth( int subsystem )
	{
		return _inst.getBandWidth( subsystem ) ;
	}

	private DefaultComboBoxModel getSpecialConfigsModel()
	{
		DefaultComboBoxModel model = new DefaultComboBoxModel() ;
		// Default special config...
		model.addElement( "None" ) ;

		// Try to open the special configs file
		String fileName = System.getProperty( "ot.cfgdir" ) ;
		if( !fileName.endsWith( "/" ) )
			fileName += '/' ;
		fileName += "ACSISModes.xml" ;
		URL url = ObservingToolUtilities.resourceURL( fileName ) ;
		if( url != null )
		{
			try
			{
				InputStream is = url.openStream() ;
				InputStreamReader reader = new InputStreamReader( is ) ;
				int readLength = 0 ;
				char[] chars = new char[ 1024 ] ;
				StringBuffer buffer = new StringBuffer() ;
				while( !reader.ready() )
					;
				while( ( readLength = reader.read( chars ) ) != -1 )
					buffer.append( chars , 0 , readLength ) ;
				reader.close() ;
				is.close() ;
				String buffer_z = buffer.toString() ;
				DOMParser parser = new DOMParser() ;
				parser.setFeature( "http://xml.org/sax/features/validation" , false ) ;
				parser.setFeature( "http://apache.org/xml/features/dom/include-ignorable-whitespace" , false ) ;
				parser.parse( new InputSource( new StringReader( buffer_z ) ) ) ;
				doc = parser.getDocument() ;

				if( doc != null )
				{
					NodeList nl = doc.getElementsByTagName( "name" ) ;
					for( int j = 0 ; j < nl.getLength() ; j++ )
					{
						String name = nl.item( j ).getFirstChild().getNodeValue().trim() ;
						model.addElement( name ) ;
					}
				}
			}
			catch( SAXNotRecognizedException snre )
			{
				System.out.println( "Unable to ignore white-space text." ) ;
			}
			catch( SAXNotSupportedException snse )
			{
				System.out.println( "Unable to ignore white-space text." ) ;
			}
			catch( SAXException sex )
			{
				System.out.println( "SAX Exception on parse." ) ;
			}
			catch( IOException ioe )
			{
				System.out.println( "IO Exception on parse." ) ;
			}
		}
		return model ;
	}

	private ConfigurationInformation getConfigFor( String name )
	{
		if( doc == null )
			return null ;

		ConfigurationInformation ci = new ConfigurationInformation() ;
		// Get the correct config item from the document
		Node nodeToUse = null ;
		NodeList nl = doc.getElementsByTagName( "configuration" ) ;
		for( int i = 0 ; i < nl.getLength() ; i++ )
		{
			nodeToUse = nl.item( i ) ;
			// Get the name associated with this
			NodeList children = nodeToUse.getChildNodes() ;
			String nodeName = "none" ;
			for( int j = 0 ; j < children.getLength() ; j++ )
			{
				Node child = children.item( j ) ;
				if( child.getNodeName().equals( "name" ) )
				{
					nodeName = child.getFirstChild().getNodeValue().trim() ;
					break ;
				}
			}
			if( nodeName.equals( name ) )
				break ;
		}
		// We now have the correct node hopefully, so fill in the ci structure
		ci.$shifts.clear() ;
		if( nodeToUse != null )
		{
			NodeList children = nodeToUse.getChildNodes() ;
			for( int i = 0 ; i < children.getLength() ; i++ )
			{
				Node child = children.item( i ) ;
				String childName = child.getNodeName() ;
				Node firstChild = child.getFirstChild() ;
				if( firstChild == null )
					continue ;
				String childValue = firstChild.getNodeValue() ;
				if( childValue == null || "".equals( childValue ) )
					continue ;
				childValue = childValue.trim() ;
				if( childName.equals( "name" ) )
					ci.$name = childValue ;
				else if( childName.equals( "frontEnd" ) )
					ci.$feName = childValue.toUpperCase() ;
				else if( childName.equals( "sideband" ) )
					ci.$sideBand = childValue.toUpperCase() ;
				else if( childName.equals( "mode" ) )
					ci.$mode = childValue.toUpperCase() ;
				else if( childName.equals( "frequency" ) )
					ci.$freq.add( new Double( childValue ) ) ;
				else if( childName.equals( "mixers" ) )
					ci.$mixers = new Integer( childValue ) ;
				else if( childName.equals( "systems" ) )
					ci.$subSystems = new Integer( childValue ) ;
				else if( childName.equals( "species" ) )
					ci.$species.add( childValue ) ;
				else if( childName.equals( "transition" ) )
					ci.$transition.add( childValue ) ;
				else if( childName.equals( "bandwidth" ) )
					ci.$bandWidths.add( childValue ) ;
				else if( childName.equals( "shift" ) )
					ci.$shifts.add( new Double( childValue ) ) ;
			}
		}
		return ci ;
	}

	private void setAvailableModes()
	{
		List<String> availableModes = Arrays.asList(_cfg.frontEndTable.get(_inst.getFrontEnd()));
		String currentMode = _inst.getMode() ;
		boolean changeMode = false ;

		// We need to see compare the available modes with the list of
		// total modes, and disable any that are not currently available
		Iterator<String> iter = modeWidgetNames.keySet().iterator() ;
		while( iter.hasNext() )
		{
			String str = iter.next() ;
			if( availableModes.contains( str ) )
			{
				// Make sure the widget is enabled
				toggleEnabled( _w.modeSelector , str , true ) ;
			}
			else
			{
				// disable the widget and optionally chnage the current mode
				if( currentMode != null && currentMode.equalsIgnoreCase( str ) )
					changeMode = true ;

				toggleEnabled( _w.modeSelector , str , false ) ;
			}
		}
		if( changeMode )
			_inst.setMode( availableModes.get( 0 ) ) ;
	}

	private void setAvailableRegions()
	{
		Vector<BandSpec> bandspecs = _receiver.bandspecs ;
		Vector<String> available = new Vector<String>() ;
		for( int i = 0 ; i < bandspecs.size() ; i++ )
			available.add( bandspecs.get( i ).toString() ) ;

		String current = _inst.getBandMode() ;
		boolean change = false ;
		Iterator<String> iter = regionWidgetNames.keySet().iterator() ;

		while( iter.hasNext() )
		{
			String str = iter.next() ;
			if( available.contains( str ) )
			{
				// Make sure the widget is enabled
				toggleEnabled( _w.regionSelector , str , true ) ;
			}
			else
			{
				// disable the widget and optionally change the current mode
				if( current != null && current.equalsIgnoreCase( str ) )
					change = true ;
				toggleEnabled( _w.regionSelector , str , false ) ;
			}
		}
		if( change )
		{
			String defaultRegion = available.get( 0 ) ;
			_inst.setBandMode( defaultRegion ) ;
			clickButton( _w.regionSelector , defaultRegion ) ;
		}
	}

	private void setAvailableMolecules(){}

	private void setAvailableTransitions(){}

	private void setAvailableSidebands(){}

	// Disable a specific button in the specified container
	private void toggleEnabled( Container c , String name , boolean enabled )
	{
		for( int i = 0 ; i < c.getComponentCount() ; i++ )
		{
			if( name.equals( c.getComponent( i ).getName() ) )
			{
				c.getComponent( i ).setEnabled( enabled ) ;
				break ;
			}
		}
	}

	private void clickButton( Container c , String name )
	{
		for( int i = 0 ; i < c.getComponentCount() ; i++ )
		{
			Component component = c.getComponent( i ) ;
			String cName = component.getName() ;
			boolean configuredState = configured ;
			if( cName != null && cName.equals( name ) && component instanceof AbstractButton )
			{
				configured = false ;
				(( AbstractButton )c.getComponent( i )).doClick() ;
				configured = configuredState ;
				break ;
			}
		}
	}

	private Object getObject( JComboBox comboBox , String name )
	{
		for( int i = 0 ; i < comboBox.getItemCount() ; i++ )
		{
			if( comboBox.getItemAt( i ).toString().equalsIgnoreCase( name ) )
				return comboBox.getItemAt( i ) ;
		}

		return null ;
	}

	private void _updateTable()
	{
		// Just collect together all the information for each spectral regions
		try
		{
			DefaultTableModel tbm = new DefaultTableModel() ;
			tbm.setColumnIdentifiers( _w.COLUMN_NAMES ) ;
			for( int i = 0 ; i < Integer.parseInt( _inst.getBandMode() ) ; i++ )
			{
				Vector<Object> row = new Vector<Object>( _regionInfo[ i ] ) ;
				row.add( 0 , new Integer( i ) ) ;
				tbm.addRow( row ) ;
			}
			_w.table.setModel( tbm ) ;
		}
		catch( Exception e ){}
	}

	boolean initialisedRegion = false ;

	/*
	 * This routine initialises the region info. 
	 * It assumes that the zeroth component is set and it just copies the zeroth values for now. 
	 * The vector contains the following elements related to each spectral region: 
	 * 1. Molecule 
	 * 2. Transition 
	 * 3. Rest Frequency 
	 * 4. Central Frequency 
	 * 5. Band width 
	 * 6. Resolution 
	 * 7. number of channels
	 */
	private void _initialiseRegionInfo()
	{
		for( int i = 0 ; i < _regionInfo.length ; i++ )
		{
			if( _regionInfo[ i ] == null )
				_regionInfo[ i ] = new Vector<Object>() ;
			else
				_regionInfo[ i ].clear() ;
			if( _inst.getMolecule( i ) == null )
			{
				_inst.setMolecule( _inst.getMolecule( 0 ) , i ) ;
				_regionInfo[ i ].add( _inst.getMolecule( i ) ) ;
			}
			else
			{
				_regionInfo[ i ].add( _inst.getMolecule( i ) ) ;
			}
			if( _inst.getTransition( i ) == null )
			{
				_inst.setTransition( _inst.getTransition( 0 ) , i ) ;
				_regionInfo[ i ].add( _inst.getTransition( i ) ) ;
			}
			else
			{
				_regionInfo[ i ].add( _inst.getTransition( i ) ) ;
			}
			if( _inst.getRestFrequency( i ) == 0. )
			{
				double restFreq = _inst.getRestFrequency( 0 ) ;
				_inst.setRestFrequency( restFreq , i ) ;
				_inst.setSkyFrequency( restFreq / ( 1. + getRedshift() ) ) ;
				_regionInfo[ i ].add( new Double( _inst.getRestFrequency( i ) / 1.E9 ) ) ;
			}
			else
			{
				_regionInfo[ i ].add( new Double( _inst.getRestFrequency( i ) / 1.E9 ) ) ;
			}
			if( _inst.getCentreFrequency( i ) == 0. )
			{
				_inst.setCentreFrequency( _inst.getCentreFrequency( 0 ) , i ) ;
				_regionInfo[ i ].add( new Double( _inst.getCentreFrequency( i ) ) ) ;
			}
			else
			{
				_regionInfo[ i ].add( new Double( _inst.getCentreFrequency( i ) ) ) ;
			}
			if( _inst.getBandWidth( i ) == 0. )
			{
				_inst.setBandWidth( _inst.getBandWidth( 0 ) , i ) ;
				double bandwidth = _inst.getBandWidth( i ) ;
				_regionInfo[ i ].add( new Double( bandwidth ) ) ;
				int resolution = ( int )Math.rint( ( bandwidth * 1.E-3 ) / _inst.getChannels( 0 ) ) ;
				_regionInfo[ i ].add( new Integer( resolution ) ) ; // Need to add resolution here
			}
			else
			{
				double bandwidth = _inst.getBandWidth( i ) ;
				_regionInfo[ i ].add( new Double( bandwidth ) ) ;
				int resolution = ( int )Math.rint( ( bandwidth * 1.E-3 ) / _inst.getChannels( i ) ) ;
				_regionInfo[ i ].add( new Integer( resolution ) ) ; // Need to add resolution here
			}
			if( _inst.getChannels( i ) == 0 )
			{
				// unlikely to happen
				_inst.setChannels( _inst.getChannels( 0 ) , i ) ;
				_regionInfo[ i ].add( new Integer( _inst.getChannels( i ) ) ) ;
			}
			else
			{
				_regionInfo[ i ].add( new Integer( _inst.getChannels( i ) ) ) ;
			}
		}
		initialisedRegion = true ;
	}

	private void _updateRegionInfo()
	{
		if( !initialisedRegion )
			_initialiseRegionInfo() ;

		Vector<BandSpec> bandSpecs = _receiver.bandspecs ;
		BandSpec currentBandSpec = null ;

		String bandMode = _inst.getBandMode() ;
		int active = new Integer( _inst.getBandMode() ) ;

		currentBandSpec = bandSpecs.get( active - 1 ) ;
		if( !currentBandSpec.toString().equals( bandMode ) )
			currentBandSpec = bandSpecs.get( 0 ) ;

		BandSpec otherBandSpec = null ;
		if( active == 3 )
			otherBandSpec = bandSpecs.get( 3 ) ;

		BandSpec activeBandSpec = null ;

		double[] availableBandWidths = null ;

		for( int i = 0 ; i < _regionInfo.length ; i++ )
		{
			if( active == 3 )
			{
				if( i < 2 )
					activeBandSpec = otherBandSpec ;
				else
					activeBandSpec = currentBandSpec ;
				availableBandWidths = activeBandSpec.getDefaultOverlapBandWidths() ;
			}
			else
			{
				activeBandSpec = currentBandSpec ;
				if( i == 0 )
					availableBandWidths = activeBandSpec.getDefaultOverlapBandWidths() ;
			}

			if( _regionInfo[ i ] == null )
				_regionInfo[ i ] = new Vector<Object>() ;
			else
				_regionInfo[ i ].clear() ;
			_regionInfo[ i ].add( _inst.getMolecule( i ) ) ;
			_regionInfo[ i ].add( _inst.getTransition( i ) ) ;
			_regionInfo[ i ].add( new Double( _inst.getRestFrequency( i ) / 1.E9 ) ) ;
			_regionInfo[ i ].add( new Double( _inst.getCentreFrequency( i ) ) ) ;
			_regionInfo[ i ].add( new Double( _inst.getBandWidth( i ) ) ) ;
			// Get the overlap and overlap based on the current b/w
			for( int j = 0 ; j < availableBandWidths.length ; j++ )
			{
				if( availableBandWidths[ j ] == _inst.getBandWidth( i ) )
				{
					double overlap = activeBandSpec.defaultOverlaps[ j ] ;
					int channels = activeBandSpec.getDefaultOverlapChannels()[ j ] ;
					int resolution = ( int )Math.rint( ( _inst.getBandWidth( i ) * 1.E-3 ) / channels ) ;
					_regionInfo[ i ].add( new Integer( resolution ) ) ;
					_regionInfo[ i ].add( new Double( activeBandSpec.defaultOverlaps[ j ] * 1.E-6 ) ) ;
					_regionInfo[ i ].add( new Integer( channels ) ) ;
					_inst.setOverlap( overlap , i ) ;
					_inst.setChannels( channels , i ) ;
					break ;
				}
			}
			_regionInfo[ i ].add( new Integer( _inst.getChannels( i ) ) ) ;
		}
	}

	private void configureFrequencyEditor()
	{
		configureFrequencyEditor( new Vector<Double>() ) ;
	}

	private void configureFrequencyEditor( Vector<Double> shifts )
	{
		// First get the current bandspec from the mode selection
		Vector<BandSpec> bandSpecs = _receiver.bandspecs ;
		BandSpec currentBandSpec = bandSpecs.get( 0 ) ;
		for( int i = 0 ; i < bandSpecs.size() ; i++ )
		{
			BandSpec bandSpec = bandSpecs.get( i ) ;
			if( bandSpec.toString().equals( _inst.getBandMode() ) )
			{
				currentBandSpec = bandSpec ;
				break ;
			}
		}

		// int subbandCount = currentBandSpec.numBands ;
		int subbandCount = Integer.parseInt( _inst.getBandMode() ) ;
		int mixerCount = 1 ;
		try
		{
			mixerCount = Integer.parseInt( _inst.getMixer() ) ;
		}
		catch( NumberFormatException nfe ){}

		_frequencyEditor.updateDisplay( _inst.getFrontEnd() , _receiver.loMin , _receiver.loMax , _receiver.feIF , _receiver.bandWidth , mixerCount , getRedshift() , currentBandSpec.getDefaultOverlapBandWidths() , currentBandSpec.getDefaultOverlapChannels() , subbandCount ) ;

		for( int i = 0 ; i < Integer.parseInt( _inst.getBandMode() ) ; i++ )
		{
			// Set the centre frequencies
			_frequencyEditor.setCentreFrequency( _inst.getCentreFrequency( i ) , i ) ;
			_frequencyEditor.setBandWidth( _inst.getBandWidth( i ) , i ) ;
			_frequencyEditor.setLineText( _inst.getMolecule( i ) + "  " + _inst.getTransition( i ) + "  " + ( _inst.getRestFrequency( i ) / 1.E6 ) , i ) ;
		}

		// Configure the frequency editor
		_frequencyEditor.resetModeAndBand( _inst.getMode() , _inst.getBand() ) ;

		// Need to deal with LO1...
		double obsFreq = _inst.getRestFrequency( 0 ) / ( 1. + getRedshift() ) ;

		String band = _inst.getBand() ;
		if( BEST.equals( band ) || USB.equals( band ) )
			_frequencyEditor.setLO1( obsFreq - _frequencyEditor.getTopSubSystemCentreFrequency() ) ;
		else
			_frequencyEditor.setLO1( obsFreq + _frequencyEditor.getTopSubSystemCentreFrequency() ) ;
		_frequencyEditor.setMainLine( _inst.getRestFrequency( 0 ) ) ;

		for( int i = 0 ; i < shifts.size() ; i++ )
		{
			_frequencyEditor.moveSlider( _inst.getBand() , _inst.getCentreFrequency( i ) , subbandCount - 1 ) ;
			if( i > 0 )
				_frequencyEditor.setLineText( "No Line" , subbandCount - 1 ) ;
		}

		_frequencyEditor.setCallback( this , "hideFreqEditor" ) ;
	}

	private void getFrequencyEditorConfiguration()
	{
		Vector<Object>[] configs = _frequencyEditor.getCurrentConfiguration() ;
		for( int i = 0 ; i < configs.length ; i++ )
		{
			_inst.setMolecule( configs[ i ].get( 0 ).toString() , i ) ;
			_inst.setTransition( configs[ i ].get( 1 ).toString() , i ) ;
			_inst.setRestFrequency( configs[ i ].get( 2 ).toString() , i ) ;
			_inst.setCentreFrequency( configs[ i ].get( 3 ).toString() , i ) ;
			_inst.setBandWidth( configs[ i ].get( 4 ).toString() , i ) ;
			_inst.setChannels( Integer.parseInt( configs[ i ].get( 5 ).toString() ) , i ) ;
		}
	}

	private void enableNamedWidgets( boolean enabled )
	{
		// Disable all named widgets except the hide button on the bPanel
		Iterator<String> iter ;

		iter = feWidgetNames.keySet().iterator() ;
		while( iter.hasNext() )
			toggleEnabled( _w.feSelector , iter.next() , enabled ) ;
		iter = modeWidgetNames.keySet().iterator() ;
		while( iter.hasNext() )
			toggleEnabled( _w.modeSelector , iter.next() , enabled ) ;
		iter = regionWidgetNames.keySet().iterator() ;
		while( iter.hasNext() )
			toggleEnabled( _w.regionSelector , iter.next() , enabled ) ;
		iter = sidebandWidgetNames.keySet().iterator() ;
		while( iter.hasNext() )
			toggleEnabled( _w.sbSelector , iter.next() , enabled ) ;

		iter = freqPanelWidgetNames.keySet().iterator() ;
		while( iter.hasNext() )
		{
			// Keep the Accept button disabled...
			String widget = iter.next() ;
			if( widget.equalsIgnoreCase( "accept" ) && enabled )
				 ; // Do nothing since we don't want to enable it
			else
				toggleEnabled( _w.fPanel , widget , enabled ) ;
		}

		int active = new Integer( _inst.getBandMode() ) ;
		boolean setEnabled = enabled ;
		for( int componentIndex = 0 ; componentIndex < components.length ; componentIndex++ )
		{
			setEnabled = enabled && componentIndex < active ;
			JComboBox component = ( JComboBox )components[ componentIndex ] ;
			component.setEnabled( setEnabled ) ;
		}

		_w.specialConfigs.setEnabled( enabled ) ;

		// Finally deal with the show and hide buttons
		for( int i = 0 ; i < _w.bPanel.getComponentCount() ; i++ )
		{
			String widget = _w.bPanel.getComponent( i ).getName() ;
			if( widget != null )
			{
				if( widget.equals( "show" ) )
					toggleEnabled( _w.bPanel , widget , enabled ) ;
				if( widget.equals( "hide" ) )
					toggleEnabled( _w.bPanel , widget , ( !enabled ) ) ;
			}
		}

		if( enabled )
		{
			// Disable anything that should not be available
			setAvailableModes() ;
			setAvailableRegions() ;
			setAvailableMolecules() ;
			setAvailableTransitions() ;
			setAvailableSidebands() ;
		}

	}

	private void checkSideband()
	{
		String sideband = _inst.getBand() ;
		
		if( !validSideband( sideband ) )
		{
			JOptionPane.showMessageDialog( null , "Using " + sideband + " in order to reach line." , "Changing Sideband!" , JOptionPane.WARNING_MESSAGE ) ;
			clickButton( _w.sbSelector , sideband ) ;
		}
	}
	
	private boolean validSideband( String sideband )
	{
		boolean valid = true ;
		double freq = _inst.getRestFrequency( 0 ) ;

		// Convert to sky frequency
		freq /= ( 1. + getRedshift() ) ;

		if( LSB.equals( sideband ) )
			valid = !( ( freq + _inst.getCentreFrequency( 0 ) ) > _receiver.loMax ) ;
		else
			valid = !( ( freq - _inst.getCentreFrequency( 0 ) ) < _receiver.loMin ) ;
		return valid ;
	}

	private void _doVelocityChecks() throws Exception
	{
		if( getSpecies().size() < 1 )
		{
			String message = "Specified velocity results in frequency range that exceeds the line catalog, " + "\nEither change the front end or change the velocity of the target" ;

			JOptionPane.showMessageDialog( null , message , "Invalid Velocity!" , JOptionPane.ERROR_MESSAGE ) ;
			throw new Exception( "Invalid velocity" ) ;
		}
	}

	class ConfigurationInformation
	{
		public String $name ;
		public String $feName ;
		public Vector<Double> $freq = new Vector<Double>() ;
		public String $sideBand ;
		public String $mode ;
		public Integer $mixers ;
		public Integer $subSystems ;
		public Vector<String> $species = new Vector<String>() ;
		public Vector<String> $transition = new Vector<String>() ;
		public Vector<String> $bandWidths = new Vector<String>() ;
		public Vector<Double> $shifts = new Vector<Double>() ;
		
		public void print()
		{
			System.out.println( "name       = " + $name ) ;
			System.out.println( "feName     = " + $feName ) ;
			System.out.println( "frequency  = " + $freq ) ;
			System.out.println( "sideBand   = " + $sideBand ) ;
			System.out.println( "mode       = " + $mode ) ;
			System.out.println( "mixers     = " + $mixers ) ;
			System.out.println( "subSystems = " + $subSystems ) ;
			System.out.println( "species    = " + $species ) ;
			System.out.println( "transition = " + $transition ) ;
			System.out.println( "bandwidth  = " + $subSystems ) ;
		}
	}

	@SuppressWarnings( "serial" )
    class TableRowRenderer extends DefaultTableCellRenderer
	{
		public TableRowRenderer()
		{
			super() ;
		}

		public Component getTableCellRendererComponent( JTable table , Object value , boolean isSelected , boolean hasFocus , int row , int column )
		{
			switch( row )
			{
				case 0 :
				default :
					setBackground( myRed ) ;
					break ;
				case 1 :
					setBackground( myYellow ) ;
					break ;
				case 2 :
					setBackground( myBlue ) ;
					break ;
				case 3 :
					setBackground( myGreen ) ;
					break ;
			}
			super.getTableCellRendererComponent( table , value , isSelected , hasFocus , row , column ) ;
			return this ;
		}
	}
}
