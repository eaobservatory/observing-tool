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
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
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
import jsky.app.ot.util.CoordSys;
import gemini.util.TelescopePos;
import gemini.util.TelescopePosWatcher;
import jsky.coords.WorldCoords;
import ot.util.DialogUtil;
import ot.OtConstants;
import ot.util.NameResolver;
import jsky.app.ot.OtCfg;


/**
 * This is the editor for the target list component.
 */
public final class EdCompTargetList extends OtItemEditor
    implements TelescopePosWatcher, TableWidgetWatcher, ActionListener, OtConstants {


    /**
     * Telescope string.
     * MFO 23 May 2001: Allows for telescope specific code.
     */
    protected String _telescope = UKIRT;

    /**
     * The text displayed by the "new" (Gemini) button in the target editor.
     *
     * MFO 23 May 2001: Currently this is just set to "Add GUIDE" (as required by UKIRT).
     * This will change when features for JCMT are implemented.
     */
    protected String newTargetButtonText = "Add GUIDE";

    protected String GUIDE_STRING        = "GUIDE";

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

    // Added by MFO (25 June 2001)
    private TelescopePosEditor _tpe = null;


    private NameResolverFeedback _nameResolverFeedback;
    private boolean _resolvingName = false;

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdCompTargetList() {
	_title       = "Telescope Targets";
	_presSource  = _w = new TelescopeGUI();
	_description = "Use this editor to enter the base position and targets.";

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



	// MFO 23 May 2001: Setting _telescope. Could be done somewhere more central like OtCfg.
	if(System.getProperty("ot.cfgdir").endsWith("jcmt" + File.separatorChar) ||
	   System.getProperty("ot.cfgdir").endsWith("jcmt")) {

          _telescope = JCMT;
	}
	else {
          _telescope = UKIRT;
	}

        // MFO 23 May 2001
	_makeTelescopeSpecificChanges();

	// *** buttons
	_w.newButton.addActionListener(this);
	_w.removeButton.addActionListener(this);
	_w.plotButton.addActionListener(this);
	_w.setBaseButton.addActionListener(this);
	_w.resolveButton.addActionListener(this);

        // chop mode tab added by MFO (3 August 2001)
	_w.chopping.addActionListener(this);


	// Get a reference to the "Tag" drop down, and initialize its choices
	_tag   = _w.tagDDLBW;
	String[] guideTags = SpTelescopePos.getGuideStarTags();

	// MFO 30 May 2001
	_tag.setChoices(guideTags);
	_tag.addChoice(SpTelescopePos.BASE_TAG);

	// MFO 23 May 2001
	// USER_TAG not used for UKIRT (see changes in FreeBongo OT, orac2)
	if(!(_telescope == UKIRT)) {
	  _tag.addChoice(SpTelescopePos.USER_TAG);
	}

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
			DialogUtil.error(_w, "You can't change the type of the Base Position.");
			_tag.setValue(SpTelescopePos.BASE_TAG);
			return;
		    }

		    // MFO 23 May 2001 bug fix. (This bug was never fixed in the FreeBongo OT for UKIRT)
		    if(_telescope == UKIRT) {
                      if(oldTag.equals(GUIDE_STRING)) {
		        DialogUtil.error(_w, "You can't change the type of the GUIDE Position.");
			_tag.setValue(GUIDE_STRING);
                        return;
		      }
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
		    _curPos.setXYFromString(tbwe.getText(), _curPos.getYaxisAsString());
		    _curPos.addWatcher(EdCompTargetList.this);

		    _resetPositionEditor();
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) { }
	    });

	_yaxis  = _w.yaxisTBW;
	_yaxis.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    _curPos.setXYFromString(_curPos.getXaxisAsString(), tbwe.getText());
		    _curPos.addWatcher(EdCompTargetList.this);
		    
		    _resetPositionEditor();
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) { }
	    });

	_system = _w.systemDDLBW;
	_system.clear();
	for (int i=0; i<CoordSys.COORD_SYS.length; ++i) {
	    _system.addChoice(CoordSys.COORD_SYS[i]);
	}
	_system.addWatcher( new DropDownListBoxWidgetWatcher() {
		public void
		    dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {
		}
		public void
		    dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
		    _updateCoordSystem();
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
	_tpTable.setColumnHeaders(new String[]{"Tag", "Name", "X Axis", "Y Axis"});
	_tpTable.setColumnWidths(new int[]{45, 85, 90, 90});
	_tpTable.setRowSelectionAllowed(true);
	_tpTable.setColumnSelectionAllowed(false);
	//_tpTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	_tpTable.addWatcher(this);
    
        //_makeTelescopeSpecificChanges();
    
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

	// Added for SCUBA (MFO, 29 October 2001)
	_w.chopSystem.setChoices(CoordSys.COORD_SYS);
	_w.chopSystem.addWatcher(new DropDownListBoxWidgetWatcher() {
		public void dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) { }

		public void dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
		    ((SpTelescopeObsComp)_spItem).setChopSystem(validateChopAngle(_w.chopAngle.getText()));
		}
	    });
    }


    /**
     * Do one-time initialization of the editor.  This includes adding watchers.
     */
    protected void _init() {
        // is now all done in constructor (MFO, 12 October 2001)
    }

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
	_xaxis.setValue(  tp.getXaxisAsString() );
	_yaxis.setValue(  tp.getYaxisAsString() );

	//_configureWidgets(tp);
	_setCoordSys(tp);


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

	//_showCoordSystemSetup(true);

	switch (sysIndex) {
	/*case CoordSys.APPARENT:  */
	case CoordSys.FK5:
	case CoordSys.FK4:
	    _setXYAxisBoxPrompts("RA", "Dec");

	    // Enable the folder widget
	    JTabbedPane fwe;
	    fwe = _w.extrasFolder;
	    
	    // MFO 23 May 2001: Keep choice between "Proper motion" and "Tracking Details" disabled for UKIRT.
	    if(_telescope != UKIRT) {
	      fwe.setEnabledAt(1, true);
	      fwe.setEnabledAt(2, true);
	    }  

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
	_tpl = ((SpTelescopeObsComp) _spItem).getPosList();
	_tpTable.reinit(_tpl);

	String seltag = _avTab.get(".gui.selectedTelescopePos");
	_tpTable.selectPos(seltag); 

        // Update table (MFO, 12 June 2001)
	try {
	  _curPos.setXYFromString(_w.xaxisTBW.getText(), _w.yaxisTBW.getText());
	}
	catch(Exception e) {
          System.out.println("Exception during _updateWidgets: " + e);
	  e.printStackTrace();
	}

	TelescopePosEditor tpe = TpeManager.get(_spItem);
	if (tpe != null) tpe.reset(_spItem);


        // chop mode tab added by MFO (3 August 2001)
	_w.chopping.setSelected( ((SpTelescopeObsComp)_spItem).isChopping() );
	_w.chopThrow.setValue(   ((SpTelescopeObsComp)_spItem).getChopThrowAsString() );
	_w.chopAngle.setValue(   ((SpTelescopeObsComp)_spItem).getChopAngleAsString() );
	
	if(((SpTelescopeObsComp)_spItem).getChopSystem() != null) {
	  _w.chopSystem.setValue(  ((SpTelescopeObsComp)_spItem).getChopSystem() );
	}  

	_w.chopThrow.setEnabled(_w.chopping.isSelected());
	_w.chopAngle.setEnabled(_w.chopping.isSelected());
	_w.chopSystem.setEnabled(_w.chopping.isSelected());
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
	_avTab.set(".gui.selectedTelescopePos", _curPos.getTag());
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
     * Needed to customize EdCompTargetList for different Telescopes.
     * 
     * @author Martin Folger (M.Folger@roe.ac.uk)
     */
    protected void _makeTelescopeSpecificChanges() {
      if(_telescope == UKIRT) {
        _w.newButton.setText(newTargetButtonText);

	_w.extrasFolder.setEnabledAt(1, false);
	_w.extrasFolder.setEnabledAt(2, false);

	_w.chopSystemLabel.setVisible(false);
	_w.chopSystem.setVisible(false);
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
	    // SpTelescopePos tp = _tpl.createBlankUserPosition();
            SpTelescopePos tp = _tpl.createPosition(GUIDE_STRING, base.getXaxis(), base.getYaxis());

	    return;
	}

	if (w == _w.removeButton) {
	    if (_curPos.getTag().equals(SpTelescopePos.BASE_TAG)) {
		DialogUtil.error(_w, "You can't remove the Base Position.");
		return;
	    }

	    _tpl.removePosition(_curPos);

	    return;
	}

	if (w == _w.plotButton) {
	    if(_tpe == null) {
	      _tpe = TpeManager.open(_spItem);
	    }
	    else {
	      _resetPositionEditor();
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
	    if(_resolvingName) {
		_nameResolverFeedback.deActivate();
		_resolvingName = false;
		_w.resolveButton.setText("Resolve");
		_w.resolveButton.setBackground(Color.lightGray);
		_w.resolveButton.setEnabled(true);
	    }
	    else {
		_resolvingName = true;
		_nameResolverFeedback = new NameResolverFeedback();
		_nameResolverFeedback.start();
	    }
	}

        // chop mode tab added by MFO (3 August 2001)
	if(w == _w.chopping) {
	    _w.chopThrow.setEnabled(_w.chopping.isSelected());
	    _w.chopAngle.setEnabled(_w.chopping.isSelected());
	    _w.chopSystem.setEnabled(_w.chopping.isSelected());

	    ((SpTelescopeObsComp)_spItem).setChopping(_w.chopping.isSelected());
	    ((SpTelescopeObsComp)_spItem).setChopThrow( _w.chopThrow.getText() );
	    ((SpTelescopeObsComp)_spItem).setChopAngle( _w.chopAngle.getText() );
	    ((SpTelescopeObsComp)_spItem).setChopSystem(_w.chopSystem.getStringValue());
	}
    }

    /**
     * Checks whether chop angle is in valid range.
     * 
     * Telescope specific.
     *
     * Added by MFO (12 October 2001)
     */
    public String validateChopAngle(String chopAngleString) {
      double chopAngle = Double.valueOf(chopAngleString).doubleValue();
	
        if(_telescope == UKIRT) {
          if(chopAngle < -90) {
            chopAngle = -90;
            DialogUtil.error(_w, "Valid range of chop angles: -90.0..90.0");
          }

         if(chopAngle > 90) {
           chopAngle = 90;
           DialogUtil.error(_w, "Valid range of chop angles: -90.0..90.0");
         }	
       }
 
//       if(_telescope == JCMT) {
//         if(chopAngle < -90) {
//           chopAngle = -90;
//           DialogUtil.error(_w, "Valid range of chop angles: -90.0..90.0");
//         }
//
//         if(chopAngle > 90) {
//           chopAngle = 90;
//           DialogUtil.error(_w, "Valid range of chop angles: -90.0..90.0");
//         }	
//       }

       return Double.toString(chopAngle);
    }

    /**
     * This class changes the color and text of the "Resolve" button that starts the name rsolver.
     *
     * During name resolving the button turns red and the text "Stop" replaces the string "Resolve".
     * This allows the user to interrupt the name resolving process if it takes too long.
     *
     * Note: When "Stop" is clickeed by the user, the NameResolverFeedback Thread continues as usual until the
     * NameResolver constructor returns. But then NameResolverFeedback.run() returns, ignoring the NameResolver
     * results and the GUI is not updated.
     *
     * Added by MFO (10 Oct 2001).
     */
    class NameResolverFeedback extends Thread {
	private boolean _active = true;

	public void deActivate() {
	    _active = false;
	}
    
	public void run() {
	  _w.resolveButton.setText("Stop");
	  _w.resolveButton.setBackground(Color.red);

          try {
	    NameResolver nameResolver = new NameResolver((String)_w.nameResolversDDLBW.getSelectedItem(), _w.nameTBW.getText());

	    if(_active == false) {
	      return;
	    }

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
	  catch(RuntimeException e) {
            if(System.getProperty("DEBUB") != null) {
              e.printStackTrace();
	    }

            DialogUtil.error(_w, "Error while trying to resolve name \"" + _w.nameTBW.getText() + "\"\n" + e.getMessage());
	  }

	  _w.resolveButton.setText("Resolve");
	  _w.resolveButton.setBackground(Color.lightGray);

	  _resolvingName = false;
	}
    }
}

