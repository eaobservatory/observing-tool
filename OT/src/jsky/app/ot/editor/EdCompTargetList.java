// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.TableWidgetExt;
import jsky.app.ot.gui.TableWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import gemini.sp.SpItem;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTelescopePosList;
import gemini.sp.obsComp.SpTelescopeObsComp;
import jsky.app.ot.tpe.TelescopePosEditor;
import jsky.app.ot.tpe.TpeManager;
import jsky.app.ot.util.Assert;
import gemini.util.CoordSys;
import gemini.util.TelescopePos;
import gemini.util.TelescopePosWatcher;
import jsky.coords.WorldCoords;
import jsky.util.gui.ProgressException;
import ot.util.DialogUtil;
import ot.OtConstants;
import ot.util.NameResolver;
import jsky.app.ot.OtCfg;


// MFO, June 06, 2002:
//   At the moment the only supported type is MAJOR. So the DropDownListBoxWidgetExt namedSystemType
//   has been commented out for now. I have not removed it completely from the code in case it is
//   needed in the future.
//   A DropDownListBoxWidgetExt with the available named target choices has been added.

// Conic System (Orbital Elements), Named System (named target such as planets, sun , moon etc)
// and Target specification using offsets added.
// Martin Folger (M.Folger@roe.ac.uk) February 27, 2002
/**
 * This is the editor for the target list component.
 */
public final class EdCompTargetList extends OtItemEditor
    implements TelescopePosWatcher, TableWidgetWatcher, ActionListener,
               ChangeListener, TextBoxWidgetWatcher, DropDownListBoxWidgetWatcher, OtConstants {

    /** String used in DropDownListBoxWidgetExt namedTarget. */
    private static final String SELECT_TARGET = "<Select Target>";

    public static final String LABEL_RA     = "Ra";
    public static final String LABEL_DEC    = "Dec";

    // Frequently used widgets
    private DropDownListBoxWidgetExt _tag;	// Object ID/Type
    private TextBoxWidgetExt         _name;	// Object Name
    private TextBoxWidgetExt         _xaxis;	// RA/Az
    private TextBoxWidgetExt         _yaxis;	// Del/El
    private DropDownListBoxWidgetExt  _system;	// Coordinate System

    private TelescopePosTableWidget _tpTable;

    private TelescopeGUI             _w;         // the GUI layout panel

    private SpTelescopePos     _curPos;	// Position being edited
    private SpTelescopePosList _tpl;	// List of positions being edited

    private NameResolverFeedback _nameResolverFeedback;

    private boolean _targetSystemsChange = false;

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdCompTargetList() {
	_title       = "Target Information";
	_presSource  = _w = new TelescopeGUI();
	_description = "Use this editor to enter the target information.";

        // Init name resolver drop down (MFO, May29, 2001)
	String [] catalogs = OtCfg.getNameResolvers();

	// Adding catalogs as follows:
	//   available   catalog 1
	//   available   catalog 2
	//   ...
	//   ---------------------
	//   unavailable catalog 1
	//   unavailable catalog 2
	//   ...
	if(catalogs != null) { 
	  for(int i = 0; i < catalogs.length; i++) {
            if(NameResolver.isAvailableAsCatalog(catalogs[i])) {
	      _w.nameResolversDDLBW.addItem(catalogs[i]);
	    }
	  }

	  //_w.nameResolversDDLBW.addItem("-------------");
	  //
	  //for(int i = 0; i < catalogs.length; i++) {
	  //  if(!NameResolver.isAvailableAsCatalog(catalogs[i])) {
	  //    _w.nameResolversDDLBW.addItem(catalogs[i]);
	  //  }
	  //}
	}

        if(!OtCfg.telescopeUtil.supports(OtCfg.telescopeUtil.FEATURE_TARGET_INFO_PROP_MOTION)) {
          _w.extrasFolder.setEnabledAt(0, false);
          _w.extrasFolder.setSelectedIndex(1);
        }

        if(!OtCfg.telescopeUtil.supports(OtCfg.telescopeUtil.FEATURE_TARGET_INFO_TRACKING)) {
          _w.extrasFolder.setEnabledAt(1, false);
          _w.extrasFolder.setSelectedIndex(2);
        }

        if(!OtCfg.telescopeUtil.supports(OtCfg.telescopeUtil.FEATURE_TARGET_INFO_CHOP)) {
          _w.extrasFolder.setEnabledAt(2, false);
          _w.extrasFolder.setSelectedIndex(0);
        }

        _w.newButton.setText("Add " + OtCfg.telescopeUtil.getAdditionalTarget());

	_w.setBaseButton.setText("Set " + OtCfg.telescopeUtil.getBaseTag() + " To Image Centre");

	if(_w.setBaseButton.getText().length() > 24) {
	  _w.setBaseButton.setFont(new java.awt.Font("Dialog", 0, 10));
	}

        // UKIRT does not need the chopSystem choice and JCMT does not use the
	// Chop Settings tab. (MFO, 21 January 2002)
	_w.chopSystemLabel.setVisible(false);
	_w.chopSystem.setVisible(false);

        // Set Tool Tip text.
	_w.anode.setToolTipText("Longitude of the ascending node");
        _w.aorq.setToolTipText("Mean distance (a) or perihelion distance (q)");
        _w.e.setToolTipText("Orbital Eccentricity");
        _w.perih.setToolTipText("Argument or Longitude of perihelion");
        _w.orbinc.setToolTipText("Inclination of the orbit ");
        _w.epoch.setToolTipText("Epoch of the orbital elements or epoch of perihelion");
        _w.l_or_m.setToolTipText("Longitude or mean anomaly");
        _w.dm.setToolTipText("Daily motion");

        _w.conicSystemType.setChoices(SpTelescopePos.CONIC_SYSTEM_TYPES_DESCRIPTION);
        // _w.namedSystemType.setChoices(SpTelescopePos.NAMED_SYSTEM_TYPES_DESCRIPTION);
	_w.namedTarget.setChoices(OtCfg.getNamedTargets());
	_w.namedTarget.addChoice(SELECT_TARGET);

	// *** buttons
	_w.newButton.addActionListener(this);
	_w.removeButton.addActionListener(this);
	_w.plotButton.addActionListener(this);
	_w.setBaseButton.addActionListener(this);
	_w.resolveButton.addActionListener(this);

        // chop mode tab added by MFO (3 August 2001)
	_w.chopping.addActionListener(this);
	_w.offsetCheckBox.addActionListener(this);

        //
	_w.targetSystemsTabbedPane.addChangeListener(this);

        _w.epoch.addWatcher(this);
        _w.orbinc.addWatcher(this);
        _w.anode.addWatcher(this);
        _w.perih.addWatcher(this);
        _w.aorq.addWatcher(this);
        _w.e.addWatcher(this);
        _w.l_or_m.addWatcher(this);
        _w.dm.addWatcher(this);
        _w.conicSystemType.addWatcher(this);
        // _w.namedSystemType.addWatcher(this);
        _w.namedTarget.addWatcher(this);

	// Get a reference to the "Tag" drop down, and initialize its choices
	_tag   = _w.tagDDLBW;
	//String[] guideTags = SpTelescopePos.getGuideStarTags();

	// MFO 30 May 2001
	//_tag.setChoices(guideTags);
	//_tag.addChoice(SpTelescopePos.BASE_TAG);
	_tag.setChoices(OtCfg.telescopeUtil.getTargetTags());

	// Currently it is not allowed to change the tag of a target. (MFO, April 24, 2002)
	_tag.setEnabled(false);

	// User tags are not used at the moment. (MFO, 19 Decemtber 2001)
	//_tag.addChoice(SpTelescopePos.USER_TAG);

	_tag.addWatcher(new DropDownListBoxWidgetWatcher() {
		public void dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

		public void dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String newTag) {
		    String oldTag = _curPos.getTag();

		    if (oldTag.startsWith(SpTelescopePos.USER_TAG)) {
			oldTag = SpTelescopePos.USER_TAG;
		    }

		    if (oldTag.equals(newTag)) return;

		    // Don't allow changes from BASE_TAG to anything else.  We always
		    // want to have a Base.
		    if (oldTag.equals(SpTelescopePos.BASE_TAG)) {
			DialogUtil.error(_w, "You can't change the tag of the " + SpTelescopePos.BASE_TAG + " Position.");
			_tag.setValue(SpTelescopePos.BASE_TAG);
			return;
		    }

		    // MFO 23 May 2001 bug fix. (This bug was never fixed in the FreeBongo OT for UKIRT)
                    if(oldTag.equals(OtCfg.telescopeUtil.getAdditionalTarget())) {
		      DialogUtil.error(_w, "You can't change the tag of the " +
			                   OtCfg.telescopeUtil.getAdditionalTarget() +
					   " Position.");
		      _tag.setValue(OtCfg.telescopeUtil.getAdditionalTarget());
                      return;
		    }

		    _tpl.changeTag(_curPos, newTag);
		}
	    });

	_name = _w.nameTBW;
	_name.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    _curPos.setName( tbwe.getText() );
		    _curPos.deleteWatcher(EdCompTargetList.this);
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) {}
	    });


	_xaxis  = _w.xaxisTBW;
	_xaxis.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);

		    if(_curPos.isOffsetPosition() || ((_curPos.getCoordSys() != CoordSys.FK5) &&
		                                      (_curPos.getCoordSys() != CoordSys.FK4))) {
		      double xAxis = 0.0;
		      double yAxis = 0.0;

		      try {
		        xAxis = Double.parseDouble(_xaxis.getText());
		      }
		      catch(Exception e) {
		        System.out.println("Could not parse x axis: " + _xaxis.getText());
		      }

		      try {
			yAxis = Double.parseDouble(_yaxis.getText());
		      }
		      catch(Exception e) {
		        System.out.println("Could not parse y axis: " + _yaxis.getText());
		      }

		      _curPos.setXY(xAxis, yAxis);
		    }
		    else {
		      _curPos.setXYFromString(tbwe.getText(), _curPos.getYaxisAsString());
		    }

		    _curPos.addWatcher(EdCompTargetList.this);

		    _resetPositionEditor();
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) { }
	    });

	_yaxis  = _w.yaxisTBW;
	_yaxis.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);

		    if(_curPos.isOffsetPosition() || ((_curPos.getCoordSys() != CoordSys.FK5) &&
		                                      (_curPos.getCoordSys() != CoordSys.FK4))) {
		      double xAxis = 0.0;
		      double yAxis = 0.0;

		      try {
		        xAxis = Double.parseDouble(_xaxis.getText());
		      }
		      catch(Exception e) { 
		        System.out.println("Could not parse x axis: " + _xaxis.getText());
		      }

		      try {
			yAxis = Double.parseDouble(_yaxis.getText());
		      }
		      catch(Exception e) { 
		        System.out.println("Could not parse y axis :" + _yaxis.getText());
		      }

		      _curPos.setXY(xAxis, yAxis);

		    }
		    else {
		      _curPos.setXYFromString(_curPos.getXaxisAsString(), tbwe.getText());
		    }

		    _curPos.addWatcher(EdCompTargetList.this);
		    
		    _resetPositionEditor();
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) { }
	    });

	_system = _w.systemDDLBW;
	_system.setChoices(OtCfg.telescopeUtil.getCoordSys());
	_system.addWatcher( new DropDownListBoxWidgetWatcher() {
		public void
		    dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {
		}
		public void
		    dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {

                    _w.RA_Az_STW.setText(CoordSys.X_AXIS_LABEL[i]);
                    _w.Dec_El_STW.setText(CoordSys.Y_AXIS_LABEL[i]);

		    _updateCoordSystem();

		    // The call _curPos.setXY() triggers a base position observer
		    // notification so that the position editor gets updated
		    // and it causes the String representation in the SpAvTable
		    // of _curPos to be reformatted depending on the coordinate system
		    // and whether _curPos is an offset position.
		    _curPos.setXY(_curPos.getXaxis(), _curPos.getYaxis());

		    _updateXYUnitsLabels();

		    _resetPositionEditor();
		}
	    });


	JTabbedPane              fwe;
	CommandButtonWidgetExt   cbwe;
	DropDownListBoxWidgetExt ddlbwe;
	TextBoxWidgetExt         tbwe;

	// *** The "extras" folder
	fwe = _w.extrasFolder;

	// --- Proper Motion Page
	tbwe = _w.propMotionRATBW;
	tbwe.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    _curPos.setPropMotionRA( tbwe.getText() );
		    _curPos.deleteWatcher(EdCompTargetList.this);
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) {}
	    });
   
	tbwe = _w.propMotionDecTBW;
	tbwe.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    _curPos.setPropMotionDec( tbwe.getText() );
		    _curPos.deleteWatcher(EdCompTargetList.this);
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) {}
	    });
   

	// --- Tracking Details Page

	ddlbwe = _w.detailsSystemDDLBW;
	ddlbwe.clear();
	for (int i=0; i<CoordSys.COORD_SYS.length; ++i) {
	    ddlbwe.addChoice(CoordSys.COORD_SYS[i]);
	}
	ddlbwe.addWatcher( new DropDownListBoxWidgetWatcher() {
		public void
		    dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {
		}

		public void
		    dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    _curPos.setTrackingSystem( val );
		    _curPos.deleteWatcher(EdCompTargetList.this);

		    JTabbedPane xFWE;
		    xFWE = _w.extrasFolder;

		    TextBoxWidgetExt xTBWE;
		    xTBWE = _w.detailsEpochTBW;
		    if (val.equals(CoordSys.COORD_SYS[CoordSys.FK4])) {
			xTBWE.setText("1950");
		    } else {
			xTBWE.setText("2000");
		    }
		}
	    });

	tbwe = _w.detailsEpochTBW;
	tbwe.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    _curPos.setTrackingEpoch( tbwe.getText() );
		    _curPos.deleteWatcher(EdCompTargetList.this);
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) {}
	    });
   
	tbwe = _w.detailsParallaxTBW;
	tbwe.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    _curPos.setTrackingParallax( tbwe.getText() );
		    _curPos.deleteWatcher(EdCompTargetList.this);
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) {}
	    });
   
	tbwe = _w.detailsRadVelTBW;
	tbwe.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    _curPos.setTrackingRadialVelocity( tbwe.getText() );
		    _curPos.deleteWatcher(EdCompTargetList.this);
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) {}
	    });
   
	tbwe = _w.detailsEffWavelengthTBW;
	tbwe.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    _curPos.setTrackingEffectiveWavelength( tbwe.getText() );
		    _curPos.deleteWatcher(EdCompTargetList.this);
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) {}
	    });
   
	cbwe = _w.detailsAutoCBW;
	cbwe.addWatcher( new CommandButtonWidgetWatcher() {
		public void commandButtonAction(CommandButtonWidgetExt cbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    _curPos.setTrackingEffectiveWavelength( "auto" );
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    showPos( _curPos );
		}
	    });

	// *** Position Table
	_tpTable = _w.positionTable;

        // If there are more then 2 systems (FK5/FK4) then display the system of a target
	// in a separate column in the target list table.
	if((OtCfg.telescopeUtil.getCoordSys() != null) && (OtCfg.telescopeUtil.getCoordSys().length > 2)) {
	    _tpTable.setColumnHeaders(new String[]{"Tag", "Name", "X Axis", "Y Axis", "System"});
	    _tpTable.setColumnWidths(new int[]{45, 85, 90, 90, 90});
	}
	else {
	    _tpTable.setColumnHeaders(new String[]{"Tag", "Name", "X Axis", "Y Axis"});
	    _tpTable.setColumnWidths(new int[]{45, 85, 90, 90});
	}
	_tpTable.setRowSelectionAllowed(true);
	_tpTable.setColumnSelectionAllowed(false);
	//_tpTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	_tpTable.addWatcher(this);
    
    
        // chop mode tab added by MFO (3 August 2001)
	_w.chopThrow.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    // MFO TODO: see TODO in SpTelescopeObsComp.
		    //_curPos.deleteWatcher(EdCompTargetList.this);
		    ((SpTelescopeObsComp)_spItem).setChopThrow( _w.chopThrow.getText() );
		    // MFO TODO: see TODO in SpTelescopeObsComp. Check whether deleteWatcher or addWatcher
		    // is needed. deleteWatcher is used twice in all the cases but that is probably a bug.
		    //_curPos.deleteWatcher(EdCompTargetList.this);
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) {}
	    });

	_w.chopAngle.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    // MFO TODO: see TODO in SpTelescopeObsComp.
		    //_curPos.deleteWatcher(EdCompTargetList.this);

		    // chop angle range is checked here rather than in
		    // gemini.sp.obsComp.SpTelescopeObsComp.setChopAngle() because it is telescope specific.
		    // SpTelescopeObsComp however is generic and does not contain telescope specific code.
		    // EdCompTargetList on the other hand contains telescope specific code already.
		    ((SpTelescopeObsComp)_spItem).setChopAngle(validateChopAngle(_w.chopAngle.getText()));

		    // MFO TODO: see TODO in SpTelescopeObsComp.
		    //_curPos.deleteWatcher(EdCompTargetList.this);
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) {}
	    });
    }


    /**
     * Do one-time initialization of the editor.  This includes adding watchers.
     */
//    protected void _init() {
//        // is now all done in constructor (MFO, 12 October 2001)
//    }


    /**
     * Show the given SpTelescopePos.
     */
    public void showPos( SpTelescopePos tp ) {
	if (tp.getTag().startsWith(SpTelescopePos.USER_TAG)) {
	    _tag.setValue( SpTelescopePos.USER_TAG );
	} else {
	    _tag.setValue( tp.getTag() );
	}


//	// display the coordinates in the selected system (FK4/FK5), but store them as J2000
//	double equinox = 2000.;
//	if (tp.getCoordSys() == CoordSys.FK4)
//	    equinox = 1950.;
//	WorldCoords pos = new WorldCoords(tp.getXaxis(), tp.getYaxis(), 2000.);
//	String[] radec = pos.format(equinox);
//
//	_name.setValue(tp.getName());
//
//	//_xaxis.setValue(tp.getXaxisAsString());
//	_xaxis.setValue(radec[0]);
//
//	//_yaxis.setValue(tp.getYaxisAsString());
//	_yaxis.setValue(radec[1]);


	_name.setValue(   tp.getName()          );

	if(tp.isOffsetPosition()) {
	  _xaxis.setValue(tp.getXaxis());
	  _yaxis.setValue(tp.getYaxis());
	}
	else {
	  _xaxis.setValue(tp.getXaxisAsString());
	  _yaxis.setValue(tp.getYaxisAsString());
	}

	_w.offsetCheckBox.setValue(tp.isOffsetPosition());

//	_xaxis.setValue(tp.getXaxisAsString());
//	_yaxis.setValue(tp.getYaxisAsString());	

	//_configureWidgets(tp);
	_setCoordSys(tp);

	if(tp.getSystemType() != SpTelescopePos.SYSTEM_SPHERICAL) {
          return;
	}

	// *** The "extras" folder
	JTabbedPane fwe = _w.extrasFolder;
	DropDownListBoxWidgetExt ddlbwe;
	TextBoxWidgetExt         tbwe;

	// --- Proper Motion Page
	tbwe = _w.propMotionRATBW;
	tbwe.setValue( tp.getPropMotionRA() );

	tbwe = _w.propMotionDecTBW;
	tbwe.setValue( tp.getPropMotionDec() );

	// --- Tracking Page
	ddlbwe = _w.detailsSystemDDLBW;
	ddlbwe.setValue( tp.getTrackingSystem() );

	tbwe = _w.detailsEpochTBW;
	tbwe.setValue( tp.getTrackingEpoch() );

	tbwe = _w.detailsParallaxTBW;
	tbwe.setValue( tp.getTrackingParallax() );

	tbwe = _w.detailsRadVelTBW;
	tbwe.setValue( tp.getTrackingRadialVelocity() );

	tbwe = _w.detailsEffWavelengthTBW;
	tbwe.setValue( tp.getTrackingEffectiveWavelength() );

    }

    //
    // Configure the text prompts on the x and y axis boxes.
    //
    private void _setXYAxisBoxPrompts(String xPrompt, String yPrompt) {
	JLabel stw;

	stw = _w.RA_Az_STW;
	stw.setText(xPrompt);

	stw = _w.Dec_El_STW;
	stw.setText(yPrompt);
    }

    //
    // Show the coordinate system and update the other widgets based upon it.
    //
    private void _setCoordSys(SpTelescopePos tp) {
	int sysIndex = tp.getCoordSys();
	_system.setValue( sysIndex );

	if(tp.isOffsetPosition()) {
	  _setXYAxisBoxPrompts(CoordSys.X_AXIS_LABEL[_system.getIntegerValue()],
	                       CoordSys.Y_AXIS_LABEL[_system.getIntegerValue()]);
	  return;
	}

	switch (sysIndex) {
	/*case CoordSys.APPARENT:  */
	case CoordSys.FK5:
	case CoordSys.FK4:
	    _setXYAxisBoxPrompts(LABEL_RA, LABEL_DEC);

	    // Enable the folder widget
	    JTabbedPane fwe;
	    fwe = _w.extrasFolder;
	    

	    // Set the Equinox and Proper Motion
	    TextBoxWidgetExt tbw;

	    if (sysIndex == CoordSys.FK4) {
		tbw = _w.propMotionRATBW;
		tbw.setText("fictitious");
		tbw = _w.propMotionDecTBW;
		tbw.setText("fictitious");
	    } else {
		tbw = _w.propMotionRATBW;
		tbw.setText("0");
		tbw = _w.propMotionDecTBW;
		tbw.setText("0");
	    }
	    tbw = _w.detailsRadVelTBW;
	    tbw.setText("0");
	    break;
	/*
	case CoordSys.AZ_EL: 
	    _setXYAxisBoxPrompts("Az", "El");

	    JTabbedPane fwe;
	    fwe = _w.extrasFolder;
	    fwe.setEnabledAt(1, false);
	    fwe.setEnabledAt(2, false);
	    break;
	*/
	}
    }


    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
        if(!_targetSystemsChange) {
	   _tpl = ((SpTelescopeObsComp) _spItem).getPosList();
	   _tpTable.reinit(_tpl);

	   String seltag = _avTab.get(".gui.selectedTelescopePos");
	   _tpTable.selectPos(seltag);

           _selectTargetSystemTab(_curPos);
	}

        if(_curPos.getSystemType() == SpTelescopePos.SYSTEM_SPHERICAL) {
          _w.offsetCheckBox.setValue(_curPos.isOffsetPosition());
          // Update table (MFO, 12 June 2001)
//	  try {
//	    _curPos.setXYFromString(_w.xaxisTBW.getText(), _w.yaxisTBW.getText());
//	  }
//	  catch(Exception e) {
//            System.out.println("Exception during _updateWidgets: " + e);
//	    e.printStackTrace();
//	  }
	}
	else {
          // update the conic or named system widgets depending on selection.
	  _updateTargetSystemPane(_curPos);
	}
 

	TelescopePosEditor tpe = TpeManager.get(_spItem);
	if (tpe != null) tpe.reset(_spItem);


        // chop mode tab added by MFO (3 August 2001)
	_w.chopping.setSelected( ((SpTelescopeObsComp)_spItem).isChopping() );
	_w.chopThrow.setValue(   ((SpTelescopeObsComp)_spItem).getChopThrowAsString() );
	_w.chopAngle.setValue(   ((SpTelescopeObsComp)_spItem).getChopAngleAsString() );
	_w.chopThrow.setEnabled(_w.chopping.isSelected());
	_w.chopAngle.setEnabled(_w.chopping.isSelected());

	_updateXYUnitsLabels();

	// update remove button
	if(_curPos.getTag().equals(OtCfg.telescopeUtil.getBaseTag())) {
	  _w.removeButton.setEnabled(false);
	}
	else {
	  _w.removeButton.setEnabled(true);
	}

	// update add button
	if(_tpl.size() < 2) {
	  _w.newButton.setEnabled(true);
	}
	else {
	  _w.newButton.setEnabled(false);
	}
    }

    /**
     * Watch TableWidget row selections to make the selected row the currently
     * displayed position.
     *
     * @see TableWidgetWatcher
     */
    public void tableRowSelected(TableWidgetExt twe, int rowIndex) {
	if (twe != _tpTable) {
	    return;		// shouldn't happen
	}

	// Show the selected position
	if (_curPos != null) {
	    _curPos.deleteWatcher(this);
	}
	_curPos = _tpTable.getSelectedPos();
	_curPos.addWatcher(this);
	showPos(_curPos);
	_selectTargetSystemTab(_curPos);
        _updateTargetSystemPane(_curPos);
	_avTab.set(".gui.selectedTelescopePos", _curPos.getTag());

        if(OtCfg.telescopeUtil.isOffsetTarget(_curPos.getTag())) {
	  _w.offsetCheckBox.setValue(_curPos.isOffsetPosition());
          _w.offsetCheckBox.setVisible(true);
        }
	else {
          _w.offsetCheckBox.setVisible(false);
	}

	_updateXYUnitsLabels();

	if(_curPos.getTag().equals(OtCfg.telescopeUtil.getBaseTag())) {
	  _w.removeButton.setEnabled(false);
	}
	else {
	  _w.removeButton.setEnabled(true);
	}

	if(_tpl.size() < 2) {
	  _w.newButton.setEnabled(true);
	}
	else {
	  _w.newButton.setEnabled(false);
	}
    }

    /**
     * As part of the TableWidgetWatcher interface, we must watch table
     * actions, though not interested in them.
     *
     * @see TableWidgetWatcher
     */
    public void	tableAction(TableWidgetExt twe, int colIndex, int rowIndex) {}


    /**
     * The current position location has changed.
     * @see TelescopePosWatcher
     */
    public void	telescopePosLocationUpdate(TelescopePos tp) {
	telescopePosGenericUpdate(tp);
    }
 
    /**
     * The current position has changed in some way.
     * @see TelescopePosWatcher
     */
    public void	telescopePosGenericUpdate(TelescopePos tp) {
	if (tp != _curPos) {
	    // This shouldn't happen ...
	    System.out.println(getClass().getName() + ": received a position " +
			       " update for a position other than the current one: " + tp);
	    return;
	}
	showPos(_curPos);
    }
 
    //
    // Update the state based upon the current coordinate system.
    //
    private void _updateCoordSystem() {
	String coordSys = _system.getStringValue();

	int sysInt = CoordSys.getSystem(coordSys);
	Assert.notFalse(sysInt != -1);

	_curPos.setCoordSys( sysInt );

	showPos(_curPos);
    }

    /**
     * Set the labels for the x and y coordinate text boxes.
     *
     * <pre><tt>
     * Coordinate System FK5/FK4     : "" (no label)
     * Other Coordinate Systems      : "(degrees)"
     * Offset (any Coordinate System): "(arcsecs)"
     * </tt></pre>
     */
    private void _updateXYUnitsLabels() {
	if(_curPos.isOffsetPosition()) {
	    _w.xUnitsLabel.setText("(arcsecs)");
	    _w.yUnitsLabel.setText("(arcsecs)");
	}
	else {
	    if((_curPos.getCoordSys() != CoordSys.FK5) && (_curPos.getCoordSys() != CoordSys.FK4)) {
	        _w.xUnitsLabel.setText("(degrees)");
	        _w.yUnitsLabel.setText("(degrees)");
	    }
	    else {
	        _w.xUnitsLabel.setText("");
	        _w.yUnitsLabel.setText("");
	    }
	}
    }

    /**
     * Updates the labels of Orbital Elements on the Conic System Tab.
     *
     * The labels are updated according to the selected Conic System type
     * (Major, Minor, Comet).
     *
     *  @param conicSystemType Use SpTelescopePos.TYPE_MAJOR,
     *                             SpTelescopePos.TYPE_MINOR,
     *                             SpTelescopePos.TYPE_COMET
     */
    private void _setConicSystemType(int conicSystemType) {
      switch(conicSystemType) {
        case SpTelescopePos.TYPE_MAJOR:
	       // Epoch of the elements and mean distance
	       _w.epochLabel.setText("t0");
	       _w.aorqLabel.setText("a");

               // Longitude of perihelion
	       _w.perihLabel.setText("\u03D6");

	       // Longitude and daily motion
	       _w.l_or_m.setValue(_curPos.getConicSystemLorM());
	       _w.l_or_mLabel.setText("L");
	       _w.l_or_m.setVisible(true);
	       _w.l_or_mLabel.setVisible(true);
	       _w.l_or_mUnitsLabel.setVisible(true);

	       _w.dm.setValue(_curPos.getConicSystemDailyMotion());
	       _w.dm.setVisible(true);
	       _w.dmLabel.setVisible(true);
	       _w.dmUnitsLabel.setVisible(true);

	       break;

        case SpTelescopePos.TYPE_MINOR:
	       // Epoch of the elements and mean distance
	       _w.epochLabel.setText("t0");
	       _w.aorqLabel.setText("a");

               // Argument of perihelion
	       _w.perihLabel.setText("\u03C9");

	       // Mean Anomaly
	       _w.l_or_m.setValue(_curPos.getConicSystemLorM());
	       _w.l_or_mLabel.setText("M");
	       _w.l_or_m.setVisible(true);
	       _w.l_or_mLabel.setVisible(true);
	       _w.l_or_mUnitsLabel.setVisible(true);

	       // No daily motion
	       _w.dm.setVisible(false);
	       _w.dmLabel.setVisible(false);
	       _w.dmUnitsLabel.setVisible(false);

               break;

        // Comet
	default:
	       // Epoch of perihelion and perihelion distance
	       _w.epochLabel.setText("T");
	       _w.aorqLabel.setText("q");

               // Argument of perihelion
	       _w.perihLabel.setText("\u03C9");

               // No longitude, mean anomaly, daily motion
	       _w.l_or_m.setVisible(false);
	       _w.l_or_mLabel.setVisible(false);
	       _w.l_or_mUnitsLabel.setVisible(false);
	       _w.dm.setVisible(false);	       
	       _w.dmLabel.setVisible(false);	       
	       _w.dmUnitsLabel.setVisible(false);	       
      }
    }


    /**
     * Updates the the conic or named system widgets
     * depending on the target system of the selected target.
     */
    private void _updateTargetSystemPane(SpTelescopePos tp) {
      switch(tp.getSystemType()) {
        case SpTelescopePos.SYSTEM_CONIC:
          _w.epoch.setValue(tp.getConicSystemEpochAsString());
	  _w.epoch.setCaretPosition(0);
          _w.orbinc.setValue(tp.getConicSystemInclination());
          _w.anode.setValue(tp.getConicSystemAnode());
          _w.perih.setValue(tp.getConicSystemPerihelion());
          _w.aorq.setValue(tp.getConicSystemAorQ());
          _w.e.setValue(tp.getConicSystemE());
          _w.l_or_m.setValue(tp.getConicSystemLorM());
          _w.dm.setValue(tp.getConicSystemDailyMotion());
          _w.conicSystemType.setValue(tp.getConicOrNamedTypeDescription());

	  _setConicSystemType(_w.conicSystemType.getIntegerValue());
	  break;

        case SpTelescopePos.SYSTEM_NAMED:
          //_w.namedSystemType.setValue(tp.getConicOrNamedTypeDescription());

	  if(Arrays.asList(OtCfg.getNamedTargets()).contains(tp.getName())) {
	    _w.namedTarget.setValue(tp.getName());
	  }
	  else {
	    _w.namedTarget.setValue(SELECT_TARGET);
	  }

	  break;
      }
    }

    /**
     * Updates the target system pane selection
     * depending on the target system of the selected target.
     */
    private void _selectTargetSystemTab(SpTelescopePos tp) {
      switch(tp.getSystemType()) {
        case SpTelescopePos.SYSTEM_CONIC:
          _w.targetSystemsTabbedPane.setSelectedComponent(_w.conicSystemPanel);
	  break;

        case SpTelescopePos.SYSTEM_NAMED:
          _w.targetSystemsTabbedPane.setSelectedComponent(_w.namedSystemPanel);
	  break;

        // SpTelescopePos.SYSTEM_SPHERICAL:
	default:
          _w.targetSystemsTabbedPane.setSelectedComponent(_w.objectGBW);
	  break;
      }
    }


    private void _resetPositionEditor() {
	// MFO: copied from ot-1.0.1 (18 June 2001)
	// If this is the base position, update the position editor
	if (_curPos.isBasePosition()) {
	    TelescopePosEditor tpe = TpeManager.get(_spItem);
	    if (tpe != null) {
	        tpe.loadImage(TelescopePosEditor.ANY_SERVER);
	    }
	}
    }


    /**
     * Method to handle button actions.
     */
    public void actionPerformed(ActionEvent evt) {
	Object w  = evt.getSource();

	if (w == _w.newButton) {
	    SpTelescopePos base = _tpl.getBasePosition();
	    if (base == null) {
		return;
	    }

            // UKIRT-ORAC: Instead of user position try just creating a
            // guide position AB 26Apr00 / MFO 23 May 2001
	    // UKIRT/JCMT: Instead of creating a guide position create an additional target according to the telescope
	    // used ("GUIDE" for UKIRT, "Reference" for JCMT), MFO 21 January 2002.
	    // SpTelescopePos tp = _tpl.createBlankUserPosition();
            SpTelescopePos tp = _tpl.createPosition(OtCfg.telescopeUtil.getAdditionalTarget(), base.getXaxis(), base.getYaxis());

            if(OtCfg.telescopeUtil.isOffsetTarget(tp.getTag())) {
                 _w.offsetCheckBox.setValue(false);
                 _w.offsetCheckBox.setVisible(true);
            }
	    else {
                 _w.offsetCheckBox.setVisible(false);
	    }

            // Select HMSDEG/DEGDEG pane.
            _w.targetSystemsTabbedPane.setSelectedComponent(_w.objectGBW);

            _w.newButton.setEnabled(false);
	    _w.removeButton.setEnabled(true);

	    return;
	}

	if (w == _w.removeButton) {
	    if (_curPos.getTag().equals(SpTelescopePos.BASE_TAG)) {
		DialogUtil.error(_w, "You can't remove the Base Position.");
		return;
	    }

	    _tpl.removePosition(_curPos);

            _w.newButton.setEnabled(true);
	    _w.removeButton.setEnabled(false);

	    return;
	}

	if (w == _w.plotButton) {
	    try {
		TpeManager.open(_spItem);
	    }
	    catch(Exception e) {
		DialogUtil.error(e);
	    }

	    return;
	}

	if (w == _w.setBaseButton) {
	    TelescopePosEditor tpe = TpeManager.get(_spItem);
	    if (tpe == null) {
		DialogUtil.message(_w, "The Position Editor must be opened for this feature to work.");
		return;
	    }

	    double[] raDec = tpe.getImageCenterLocation();
	    if (raDec == null) {
		DialogUtil.message(_w, "Couldn't determine the image center.");
		return;
	    }

	    SpTelescopePos base = _tpl.getBasePosition();
	    if (base == null) {
		return;
	    }

	    base.setXY(raDec[0], raDec[1]);
	    return;
	}

	// Added by MFO
	if(w == _w.resolveButton) {
	    _nameResolverFeedback = new NameResolverFeedback();
	    _nameResolverFeedback.start();
	}

        // chop mode tab added by MFO (3 August 2001)
	if(w == _w.chopping) {
	    _w.chopThrow.setEnabled(_w.chopping.isSelected());
	    _w.chopAngle.setEnabled(_w.chopping.isSelected());

	    ((SpTelescopeObsComp)_spItem).setChopping(_w.chopping.isSelected());
	    ((SpTelescopeObsComp)_spItem).setChopThrow( _w.chopThrow.getText() );
	    ((SpTelescopeObsComp)_spItem).setChopAngle( _w.chopAngle.getText() );
	}

	if(w == _w.offsetCheckBox) {
	  if(_w.offsetCheckBox.getBooleanValue()) {
	    _curPos.setOffsetPosition(true);
	  }
	  else {
	    _curPos.setOffsetPosition(false);
	  }

	  _curPos.setXY(0.0, 0.0);

	  _updateXYUnitsLabels();
	}
    }

    public void stateChanged(ChangeEvent e) {

      if(e.getSource() == _w.targetSystemsTabbedPane) {

        if(_w.targetSystemsTabbedPane.getSelectedComponent() == _w.objectGBW) {
          _curPos.setSystemType(SpTelescopePos.SYSTEM_SPHERICAL);
	}

        if(_w.targetSystemsTabbedPane.getSelectedComponent() == _w.conicSystemPanel) {
          _curPos.setSystemType(SpTelescopePos.SYSTEM_CONIC);
	}

        if(_w.targetSystemsTabbedPane.getSelectedComponent() == _w.namedSystemPanel) {
          _curPos.setSystemType(SpTelescopePos.SYSTEM_NAMED);
	}

        _targetSystemsChange = true;
        _updateWidgets();

        _curPos.deleteWatcher(EdCompTargetList.this);
        _curPos.setConicOrNamedType(SpTelescopePos.NAMED_SYSTEM_TYPES[SpTelescopePos.TYPE_MAJOR]);

        _curPos.setName("");
	_name.setValue("");

        _curPos.addWatcher(EdCompTargetList.this);

	_targetSystemsChange = false;
      }
    }

    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
      if(tbwe == _w.epoch) {
        _curPos.setConicSystemEpoch(_w.epoch.getValue());
        return;
      }

      if(tbwe == _w.orbinc) {
        _curPos.setConicSystemInclination(_w.orbinc.getValue());
        return;
      }

      if(tbwe == _w.anode) {
        _curPos.setConicSystemAnode(_w.anode.getValue());
        return;
      }

      if(tbwe == _w.perih) {
        _curPos.setConicSystemPerihelion(_w.perih.getValue());
        return;
      }

      if(tbwe == _w.aorq) {
        _curPos.setConicSystemAorQ(_w.aorq.getValue());
        return;
      }

      if(tbwe == _w.e) {
        _curPos.setConicSystemE(_w.e.getValue());
        return;
      }

      if(tbwe == _w.l_or_m) {
        _curPos.setConicSystemLorM(_w.l_or_m.getValue());
        return;
      }

      if(tbwe == _w.dm) {
        _curPos.setConicSystemDailyMotion(_w.dm.getValue());
        return;
      }
    }

    public void textBoxAction(TextBoxWidgetExt tbwe) { }


    public void dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) { }

    public void dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
      if(dd == _w.conicSystemType) {
        _curPos.setConicOrNamedType(SpTelescopePos.CONIC_SYSTEM_TYPES[i]);

        _setConicSystemType(i);

	return;
      }

//      if(dd == _w.namedSystemType) {
//        _curPos.setConicOrNamedType(SpTelescopePos.NAMED_SYSTEM_TYPES[i]);
//	return;
//      }

      if(dd == _w.namedTarget) {
        _curPos.deleteWatcher(EdCompTargetList.this);
        _curPos.setConicOrNamedType(SpTelescopePos.NAMED_SYSTEM_TYPES[SpTelescopePos.TYPE_MAJOR]);

        if(val.equals(SELECT_TARGET)) {
          _curPos.setName("");
	  _name.setValue("");
        }
        else {
          _curPos.setName(val);
	  _name.setValue(val);
        }

        _curPos.addWatcher(EdCompTargetList.this);

	return;
      }
    }

    /**
     * Checks whether chop angle is in valid range.
     * 
     * The validity check applies to UKIRT. JCMT does not use the "Chop Settings" tab.
     *
     * Added by MFO (12 October 2001)
     */
    public String validateChopAngle(String chopAngleString) {
      double chopAngle = Double.valueOf(chopAngleString).doubleValue();
	
      if(chopAngle < -90) {
        chopAngle = -90;
        DialogUtil.error(_w, "Valid range of chop angles: -90.0..90.0");
      }

      if(chopAngle > 90) {
        chopAngle = 90;
        DialogUtil.error(_w, "Valid range of chop angles: -90.0..90.0");
      }	

      return Double.toString(chopAngle);
    }

    /**
     * Thread for name resolving.
     *
     * Added by MFO (10 Oct 2001).
     */
    class NameResolverFeedback extends Thread {
    
	public void run() {

          try {
	    NameResolver nameResolver = new NameResolver((String)_w.nameResolversDDLBW.getSelectedItem(), _w.nameTBW.getText());

	    _w.nameTBW.setText(nameResolver.getId());
	    _w.xaxisTBW.setText(nameResolver.getRa());
	    _w.yaxisTBW.setText(nameResolver.getDec());
	    _w.systemDDLBW.setValue(CoordSys.FK5);

            _curPos.deleteWatcher(EdCompTargetList.this);
	    try {
	      _curPos.setXYFromString(_w.xaxisTBW.getText(), _w.yaxisTBW.getText());
	    }
	    // In case an exception is thrown here if the new position is out of view (in the
	    // position editor)
	    // The new position will be "in view" after the call _resetPositionEditor.
	    // But _resetPositionEditor can only be called after _cur has been set to the new
	    // position. Otherwise. it would not pick up the right position.
	    catch(Exception exception) {
              // print stack trace for debugging.
	      exception.printStackTrace();
	    }
	    _curPos.setName(_w.nameTBW.getText() );
	    _curPos.setCoordSys(CoordSys.FK5);
	    _curPos.addWatcher(EdCompTargetList.this);

            _resetPositionEditor();
	  }
	  catch(ProgressException e) {
            // User has interrupted query. Do nothing.
            System.out.println("Query interrupted by OT user.");
	  }
	  catch(RuntimeException e) {
            if(System.getProperty("DEBUB") != null) {
              e.printStackTrace();
	    }

            DialogUtil.error(_w, "Error while trying to resolve name \"" + _w.nameTBW.getText() + "\"\n" + e.getMessage());
	  }
	}
    }
}

