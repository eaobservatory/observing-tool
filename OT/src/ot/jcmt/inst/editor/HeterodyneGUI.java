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

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.BoxLayout;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;

import edfreq.FrequencyEditorCfg;

/**
 * This GUI class is based on the GUI part of the edfreq.FrontEnd class which is not used anymore.
 *
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class HeterodyneGUI extends JPanel {
    
    // Get the front end configuration
    private static FrequencyEditorCfg _cfg = FrequencyEditorCfg.getConfiguration();

    /** Available sideband modes */
    public static final String [] SIDEBAND_MODES= {"ssb", "dsb"};

    /** Subsytems available */
    public static final String [] SUBSYSTEMS = {"1", "2", "4"};

    /** Number of mixers */
    public static final String [] MIXERS = {"1", "2"};

    /** Sideband choices */
    public static final String [] SIDEBAND_SELECTIONS = {"best", "usb", "lsb"};

    // Table column headings
    final String [] COLUMN_NAMES = {"Region", "Species", "Trans.", "Rest. Freq.", 
	"Centre Freq.", "BW", "res", "overlap", "channels" };

    // FE Panels
    JPanel feSelector;
    JPanel modeSelector;
    JPanel regionSelector;
    JPanel mixerSelector;
    JPanel sbSelector;
    JPanel vPanel;
    JPanel fPanel;
    JPanel bPanel;
    JPanel summaryPanel;
    JComboBox bandwidths;
    JTable table;

    public HeterodyneGUI() {
	try {
	    jbInit();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    
    private void jbInit () throws Exception {
	
	setLayout ( new GridBagLayout() );
        //
        // Front-end panel and its elements
        //
        JPanel fePanel = new JPanel();
        fePanel.setLayout ( new GridBagLayout() );
        fePanel.setBorder (BorderFactory.createTitledBorder ( 
		    BorderFactory.createBevelBorder(BevelBorder.LOWERED),
		    "Front End Configuration"));
    

	JLabel feLabel = new JLabel("Front End:");
	feLabel.setForeground(Color.BLACK);
	feLabel.setFont( new Font ("dialog", 0, 12) );
	
        feSelector = makeFEGroup();

	JLabel modeLabel = new JLabel("Mode:");
	modeLabel.setForeground(Color.BLACK);
	modeLabel.setFont( new Font ("dialog", 0, 12) );

        modeSelector = makeSidebandConfigGroup();

	JLabel subsystemLabel = new JLabel("Sp. Regions:");
	subsystemLabel.setForeground(Color.BLACK);
	subsystemLabel.setFont( new Font ("dialog", 0, 12) );

        regionSelector = makeSubsystemGroup();

	JLabel mixerLabel = new JLabel("Mixers:");
	mixerLabel.setForeground(Color.BLACK);
	mixerLabel.setFont( new Font ("dialog", 0, 12) );

        mixerSelector = makeMixerGroup();

	JLabel sbLabel = new JLabel("Sideband:");
	sbLabel.setForeground(Color.BLACK);
	sbLabel.setFont( new Font ("dialog", 0, 12) );

        sbSelector = makeSBSelectionGroup();

	JLabel specialConfigLabel = new JLabel("Special Configs:");
	specialConfigLabel.setForeground(Color.BLACK);
	specialConfigLabel.setFont( new Font ("dialog", 0, 12) );
	
	JComboBox specialConfigs = new JComboBox();
	specialConfigs.setForeground(Color.BLACK);
	specialConfigs.setFont( new Font ("dialog", 0, 12) );

	JLabel bwLabel = new JLabel ("Bandwidth:");
	bwLabel.setForeground(Color.BLACK);
	bwLabel.setFont( new Font ("dialog", 0, 12) );

	bandwidths = new JComboBox();
	bandwidths.setForeground(Color.BLACK);
	bandwidths.setFont( new Font ("dialog", 0, 12) );
   

        fePanel.add (feLabel, new GridBagConstraints (0, 0, 1, 1, 1.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.NONE,
		    new Insets (5, 0, 5, 0), 0, 0));
        fePanel.add (feSelector, new GridBagConstraints (1, 0, 4, 1, 1.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
		    new Insets (5, 0, 5, 0), 0, 0));
        fePanel.add (subsystemLabel, new GridBagConstraints (0, 1, 1, 1, 0.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.NONE,
		    new Insets (0, 0, 5, 0), 0, 0));
        fePanel.add (regionSelector, new GridBagConstraints (1, 1, 1, 1, 0.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.NONE,
		    new Insets (0, 0, 5, 0), 0, 0));
	fePanel.add (specialConfigLabel, new GridBagConstraints (2, 1, 1, 1, 0.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.NONE,
		    new Insets (0, 20, 5, 0), 0, 0 ));
	fePanel.add ( specialConfigs, new GridBagConstraints (3, 1, 1, 1, 1.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
		    new Insets(0, 5, 5, 5), 0, 0 ));
        fePanel.add (mixerLabel, new GridBagConstraints (0, 2, 1, 1, 1.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.NONE,
		    new Insets (0, 0, 5, 0), 0, 0));
        fePanel.add (mixerSelector, new GridBagConstraints (1, 2, 1, 1, 1.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.NONE,
		    new Insets (0, 0, 5, 0), 0, 0));
	fePanel.add (bwLabel, new GridBagConstraints ( 2, 2, 1, 1, 0.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.NONE,
		    new Insets (0, 20, 5, 0), 0, 0 ));
	fePanel.add ( bandwidths, new GridBagConstraints (3, 2, 1, 1, 1.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
		    new Insets(0, 5, 5, 5), 0, 0 ));
        fePanel.add (modeLabel, new GridBagConstraints (0, 3, 1, 1, 1.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.NONE,
		    new Insets (0, 0, 5, 0), 0, 0));
        fePanel.add (modeSelector, new GridBagConstraints (1, 3, 1, 1, 1.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.NONE,
		    new Insets (0, 0, 5, 0), 0, 0));
        fePanel.add ( sbLabel, new GridBagConstraints ( 0, 4, 1, 1, 1.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.NONE,
		    new Insets (0, 0, 5, 0), 0, 0));
        fePanel.add ( sbSelector, new GridBagConstraints ( 1, 4, 1, 1, 1.0, 0.0,
		    GridBagConstraints.WEST, GridBagConstraints.NONE,
		    new Insets (0, 0, 5, 0), 0, 0));

	// Front End summary panel
	summaryPanel = new JPanel();
        summaryPanel.setLayout ( new GridLayout(0, 2) );
        summaryPanel.setBorder (BorderFactory.createTitledBorder ( 
		    BorderFactory.createBevelBorder(BevelBorder.LOWERED),
		    "Front End Summary"));

	JLabel lowLimitLabel = new JLabel ("Low limit (GHz):");
	lowLimitLabel.setForeground(Color.BLACK);
	lowLimitLabel.setFont( new Font ("dialog", 0, 12) );

	JLabel lowFreq = new JLabel ( "215" );
	lowFreq.setFont(new Font("Dialog", 0, 12));
	lowFreq.setForeground(Color.black);
	lowFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
	lowFreq.setMinimumSize(lowFreq.getPreferredSize());
	lowFreq.setName("LowFreqLimit");
	
	JLabel hiLimitLabel = new JLabel ("High limit (GHz):");
	hiLimitLabel.setForeground(Color.BLACK);
	hiLimitLabel.setFont( new Font ("dialog", 0, 12) );

	JLabel highFreq = new JLabel ( "272" );
	highFreq.setFont(new Font("Dialog", 0, 12));
	highFreq.setForeground(Color.black);
	highFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
	highFreq.setMinimumSize(highFreq.getPreferredSize());
	highFreq.setName("HighFreqLimit");


	JLabel resLabel = new JLabel ("Resolution (kHz):");
	resLabel.setForeground(Color.BLACK);
	resLabel.setFont( new Font ("dialog", 0, 12) );

	JLabel resolution = new JLabel();
	resolution.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
	resolution.setFont(new Font("Dialog", 0, 12));
	resolution.setForeground(Color.black);
	resolution.setMinimumSize( resolution.getPreferredSize() );
	resolution.setName( "resolution" );

	JLabel overlapLabel = new JLabel ("Overlap (MHz):");
	overlapLabel.setForeground(Color.BLACK);
	overlapLabel.setFont( new Font ("dialog", 0, 12) );

	JLabel overlap = new JLabel();
	overlap.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
	overlap.setFont(new Font("Dialog", 0, 12));
	overlap.setForeground(Color.black);
	overlap.setMinimumSize( overlap.getPreferredSize() );
	overlap.setName( "overlap" );

	JLabel channelLabel = new JLabel ("Channels:");
	channelLabel.setForeground(Color.BLACK);
	channelLabel.setFont( new Font ("dialog", 0, 12) );

	JLabel channel = new JLabel();
	channel.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
	channel.setFont(new Font("Dialog", 0, 12));
	channel.setForeground(Color.black);
	channel.setMinimumSize( channel.getPreferredSize() );
	channel.setName( "channel" );

	summaryPanel.add (lowLimitLabel);
	summaryPanel.add (lowFreq);
	summaryPanel.add (hiLimitLabel);
	summaryPanel.add (highFreq);
	/*
	summaryPanel.add (resLabel);
	summaryPanel.add (resolution);
	summaryPanel.add (overlapLabel);
	summaryPanel.add (overlap);
	summaryPanel.add (channelLabel);
	summaryPanel.add (channel);
	*/

	// Frequency Panel
	// To maintain the layout, we put the volocity and frequency and
	// buttons into separate, hidden, panels
	JPanel freqPanel = new JPanel();
        freqPanel.setLayout ( new BorderLayout() );
        freqPanel.setBorder (BorderFactory.createTitledBorder ( 
		    BorderFactory.createBevelBorder(BevelBorder.LOWERED),
		    "Frequncy Setup"));

	vPanel = new JPanel( new GridBagLayout() );
	fPanel = new JPanel( new GridBagLayout() );
	bPanel = new JPanel( new GridBagLayout() );

	JLabel velLabel = new JLabel ("Velocity");
	velLabel.setForeground(Color.BLACK);
	velLabel.setFont( new Font ("dialog", 0, 12) );

	JLabel velocity = new JLabel("unset");
	velocity.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
	velocity.setFont(new Font("Dialog", 0, 12));
	velocity.setForeground(Color.black);
	velocity.setMinimumSize( velocity.getPreferredSize() );
	velocity.setName( "velocity" );

	JLabel vfLabel = new JLabel ("Frame");
	vfLabel.setForeground(Color.BLACK);
	vfLabel.setFont( new Font ("dialog", 0, 12) );

	JLabel vFrame = new JLabel("unset");
	vFrame.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
	vFrame.setFont(new Font("Dialog", 0, 12));
	vFrame.setForeground(Color.black);
	vFrame.setMinimumSize( vFrame.getPreferredSize() );
	vFrame.setName( "frame" );

	JLabel vdLabel = new JLabel ("Definition");
	vdLabel.setForeground(Color.BLACK);
	vdLabel.setFont( new Font ("dialog", 0, 12) );

	JLabel vDef = new JLabel("unset");
	vDef.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
	vDef.setFont(new Font("Dialog", 0, 12));
	vDef.setForeground(Color.black);
	vDef.setMinimumSize( vDef.getPreferredSize() );
	vDef.setName( "definition" );

	JComboBox  moleculeBox = new JComboBox();
	moleculeBox.setForeground(Color.BLACK);
	moleculeBox.setFont( new Font ("dialog", 0, 12) );
	moleculeBox.setName("molecule");

	JComboBox  transitionBox = new JComboBox();
	transitionBox.setForeground(Color.BLACK);
	transitionBox.setFont( new Font ("dialog", 0, 12) );
	transitionBox.setName("transition");

	JButton acceptButton = new JButton ("Accept");
	acceptButton.setForeground(Color.RED);
	acceptButton.setFont( new Font ("dialog", 0, 12) );
	acceptButton.setBorder( new BevelBorder (BevelBorder.RAISED) );
	acceptButton.setName( "Accept" );

	JButton showButton = new JButton ("Show Frequency Editor");
	showButton.setForeground(Color.BLACK);
	showButton.setFont( new Font ("dialog", 0, 12) );
	showButton.setBorder( new BevelBorder (BevelBorder.RAISED) );
	showButton.setName( "show" );

	JButton hideButton = new JButton ("Hide Frequency Editor");
	hideButton.setForeground(Color.BLACK);
	hideButton.setFont( new Font ("dialog", 0, 12) );
	hideButton.setBorder( new BevelBorder (BevelBorder.RAISED) );
	hideButton.setName( "hide" );
	 
	JTextField  freqText = new JTextField();
	freqText.setForeground(Color.RED);
	freqText.setFont( new Font ("dialog", 0, 12) );
	freqText.setName("frequency");

	JLabel unitsLabel = new JLabel ("GHz");
	unitsLabel.setForeground(Color.BLACK);
	unitsLabel.setFont( new Font ("dialog", 0, 12) );

	vPanel.add ( velLabel, 
		new GridBagConstraints ( 0, 0, 1, 1, 0.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets (10, 5, 5, 5), 0, 0));
	vPanel.add ( velocity, 
		new GridBagConstraints ( 1, 0, 1, 1, 1.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets (10, 0, 5, 5), 0, 0));
	vPanel.add ( vdLabel, 
		new GridBagConstraints ( 2, 0, 1, 1, 0.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets (10, 0, 5, 5), 0, 0));
	vPanel.add ( vDef, 
		new GridBagConstraints ( 3, 0, 1, 1, 1.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets (10, 0, 5, 5), 0, 0));
	vPanel.add ( vfLabel, 
		new GridBagConstraints ( 4, 0, 1, 1, 0.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets (10, 0, 5, 5), 0, 0));
	vPanel.add ( vFrame, 
		new GridBagConstraints ( 5, 0, 1, 1, 1.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets (10, 0, 5, 5), 0, 0));
	fPanel.add ( moleculeBox, 
		new GridBagConstraints ( 0, 0, 1, 1, 1.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
		new Insets (5, 5, 5, 5), 0, 0));
	fPanel.add ( transitionBox, 
		new GridBagConstraints ( 1, 0, 1, 1, 1.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
		new Insets (5, 0, 5, 5), 0, 0));
	fPanel.add ( freqText, 
		new GridBagConstraints ( 2, 0, 1, 1, 1.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
		new Insets (5, 0, 5, 5), 0, 0));
	fPanel.add ( unitsLabel, 
		new GridBagConstraints ( 3, 0, 1, 1, 0.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
		new Insets (5, 0, 5, 5), 0, 0));
	fPanel.add ( acceptButton, 
		new GridBagConstraints ( 4, 0, 1, 1, 0.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets (5, 0, 5, 5), 0, 0));
	bPanel.add ( showButton, 
		new GridBagConstraints ( 0, 0, 1, 1, 1.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets (5, 50, 5, 5), 0, 0));
	bPanel.add ( hideButton, 
		new GridBagConstraints ( 1, 0, 1, 1, 1.0, 0.0,
		GridBagConstraints.EAST, GridBagConstraints.NONE,
		new Insets (5, 0, 5, 50), 0, 0));

	freqPanel.add ( vPanel, BorderLayout.NORTH );
	freqPanel.add ( fPanel, BorderLayout.CENTER );
	freqPanel.add ( bPanel, BorderLayout.SOUTH );

	// The frequency summary information
	JPanel  tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder (BorderFactory.createTitledBorder ( 
		    BorderFactory.createBevelBorder(BevelBorder.LOWERED),
		    "Frequency Configuration"));

	table = new JTable (new DefaultTableModel(COLUMN_NAMES, 4));
	table.setEnabled(false);
	table.setBackground( getBackground() );
	JScrollPane scrollPane = new JScrollPane(table);
	//table.setPreferredScrollableViewportSize(new Dimension(500, 70));

	tablePanel.add(scrollPane, BorderLayout.CENTER);
	
	
	// Add all the panels to this object
        add (fePanel, new GridBagConstraints (0, 0, 1, 1, 1.0, 0.0,
		    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
		    new Insets (0, 0, 5, 0), 0, 0));
        add (summaryPanel, new GridBagConstraints (1, 0, 1, 1, 1.0, 0.0,
		    GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
		    new Insets (0, 0, 5, 5), 0, 0));
        add (freqPanel, new GridBagConstraints (0, 1, 2, 1, 1.0, 0.0,
		    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
		    new Insets (0, 0, 5, 5), 0, 0));
        add (tablePanel, new GridBagConstraints (0, 2, 2, 2, 1.0, 1.0,
		    GridBagConstraints.NORTH, GridBagConstraints.BOTH,
		    new Insets (0, 0, 5, 5), 0, 0));
      }

      private JPanel makeFEGroup() {
	JPanel jPanel = new JPanel();
	jPanel.setBorder (BorderFactory.createTitledBorder (
		    BorderFactory.createEtchedBorder()));
	jPanel.setLayout (new GridLayout(1, 0, 5, 0));

	ButtonGroup bg = new ButtonGroup();

	for ( int i=0; i < _cfg.frontEnds.length; i++ ) {
	    JRadioButton b = new JRadioButton(_cfg.frontEnds[i].trim());
	    b.setForeground(Color.BLACK);
	    b.setFont (new Font ("Dialog", 0, 10));
	    b.setName(_cfg.frontEnds[i].trim());
	    bg.add(b);
	    jPanel.add(b);
	}
	return jPanel;
      }

      private JPanel makeSidebandConfigGroup() {
	JPanel jPanel = new JPanel();
	jPanel.setBorder (BorderFactory.createTitledBorder (
		    BorderFactory.createEtchedBorder()));
	jPanel.setLayout (new GridLayout(1, 0));

	ButtonGroup bg = new ButtonGroup();

	for ( int i=0; i < SIDEBAND_MODES.length; i++ ) {
	    JRadioButton b = new JRadioButton(SIDEBAND_MODES[i]);
	    b.setForeground(Color.BLACK);
	    b.setFont (new Font ("Dialog", 0, 10));
	    b.setName(SIDEBAND_MODES[i]);
	    bg.add(b);
	    jPanel.add(b);
	}
	return jPanel;
      }

      private JPanel makeSubsystemGroup() {
	JPanel jPanel = new JPanel();
	jPanel.setBorder (BorderFactory.createTitledBorder (
		    BorderFactory.createEtchedBorder()));
	jPanel.setLayout (new GridLayout(1, 0));

	ButtonGroup bg = new ButtonGroup();

	for ( int i=0; i < SUBSYSTEMS.length; i++ ) {
	    JRadioButton b = new JRadioButton(SUBSYSTEMS[i]);
	    b.setForeground(Color.BLACK);
	    b.setFont (new Font ("Dialog", 0, 10));
	    b.setName(SUBSYSTEMS[i]);
	    bg.add(b);
	    jPanel.add(b);
	}
	return jPanel;
      }

      private JPanel makeMixerGroup() {
	JPanel jPanel = new JPanel();
	jPanel.setBorder (BorderFactory.createTitledBorder (
		    BorderFactory.createEtchedBorder()));
	jPanel.setLayout (new GridLayout(1, 0));

	ButtonGroup bg = new ButtonGroup();

	for ( int i=0; i < MIXERS.length; i++ ) {
	    JRadioButton b = new JRadioButton(MIXERS[i]);
	    b.setForeground(Color.BLACK);
	    b.setFont (new Font ("Dialog", 0, 10));
	    b.setName(MIXERS[i]);
	    bg.add(b);
	    jPanel.add(b);
	}
	return jPanel;
      }

      private JPanel makeSBSelectionGroup() {
	JPanel jPanel = new JPanel();
	jPanel.setBorder (BorderFactory.createTitledBorder (
		    BorderFactory.createEtchedBorder()));
	jPanel.setLayout (new GridLayout(1, 0));

	ButtonGroup bg = new ButtonGroup();

	for ( int i=0; i < SIDEBAND_SELECTIONS.length; i++ ) {
	    JRadioButton b = new JRadioButton(SIDEBAND_SELECTIONS[i]);
	    b.setForeground(Color.BLACK);
	    b.setFont (new Font ("Dialog", 0, 10));
	    b.setName(SIDEBAND_SELECTIONS[i]);
	    bg.add(b);
	    jPanel.add(b);
	}
	return jPanel;
      }


}

