// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
//import jsky.app.ot.util.TelescopePos;
//import jsky.app.ot.util.TelescopePosWatcher;
import gemini.util.TelescopePos;
import gemini.util.TelescopePosWatcher;
import jsky.coords.WorldCoords;
import jsky.util.gui.DialogUtil;


/**
 * This is the editor for the target list component.
 */
public final class EdCompTargetList extends OtItemEditor
    implements TelescopePosWatcher, TableWidgetWatcher, ActionListener {

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

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdCompTargetList() {
	_title       = "Telescope Targets";
	_presSource  = _w = new TelescopeGUI();
	_description = "Use this editor to enter the base positon, WFS stars, and targets.";

	// *** buttons
	_w.newButton.addActionListener(this);
	_w.removeButton.addActionListener(this);
	_w.plotButton.addActionListener(this);
	_w.setBaseButton.addActionListener(this);
    }

    /**
     * Do one-time initialization of the editor.  This includes adding watchers.
     */
    protected void _init() {
	// Get a reference to the "Tag" drop down, and initialize its choices
	_tag   = _w.tagDDLBW;
	String[] guideTags = SpTelescopePos.getGuideStarTags();
	_tag.addChoice(SpTelescopePos.BASE_TAG);
	for(int i = 0; i < guideTags.length; i++)
	    _tag.addChoice(guideTags[i]);
	_tag.addChoice(SpTelescopePos.USER_TAG);

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
			DialogUtil.error("You can't change the type of the Base Position.");
			_tag.setValue(SpTelescopePos.BASE_TAG);
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
		    _curPos.setXYFromString(tbwe.getText(), _curPos.getYaxisAsString());
		    _curPos.addWatcher(EdCompTargetList.this);
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) {}
	    });

	_yaxis  = _w.yaxisTBW;
	_yaxis.addWatcher( new TextBoxWidgetWatcher() {
		public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
		    _curPos.deleteWatcher(EdCompTargetList.this);
		    _curPos.setXYFromString(_curPos.getXaxisAsString(), tbwe.getText());
		    _curPos.addWatcher(EdCompTargetList.this);
		}
		public void textBoxAction(TextBoxWidgetExt tbwe) {}
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

	// display the coordinates in the selected system (FK4/FK5), but store them as J2000
	double equinox = 2000.;
	if (tp.getCoordSys() == CoordSys.FK4)
	    equinox = 1950.;
	WorldCoords pos = new WorldCoords(tp.getXaxis(), tp.getYaxis(), 2000.);
	String[] radec = pos.format(equinox);

	_name.setValue(tp.getName());

	//_xaxis.setValue(tp.getXaxisAsString());
	_xaxis.setValue(radec[0]);

	//_yaxis.setValue(tp.getYaxisAsString());
	_yaxis.setValue(radec[1]);

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
	    fwe.setEnabledAt(0, true);
	    fwe.setEnabledAt(1, true);

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
	    fwe.setEnabledAt(0, false);
	    fwe.setEnabledAt(1, false);
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

	TelescopePosEditor tpe = TpeManager.get(_spItem);
	if (tpe != null) tpe.reset(_spItem);
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

	    SpTelescopePos tp = _tpl.createBlankUserPosition();
	    return;
	}

	if (w == _w.removeButton) {
	    if (_curPos.getTag().equals(SpTelescopePos.BASE_TAG)) {
		DialogUtil.error("You can't remove the Base Position.");
		return;
	    }

	    _tpl.removePosition(_curPos);

	    return;
	}

	if (w == _w.plotButton) {
	    TelescopePosEditor tpe = TpeManager.open(_spItem);
	    return;
	}

	if (w == _w.setBaseButton) {
	    TelescopePosEditor tpe = TpeManager.get(_spItem);
	    if (tpe == null) {
		DialogUtil.message("The Position Editor must be opened for this feature to work.");
		return;
	    }

	    double[] raDec = tpe.getImageCenterLocation();
	    if (raDec == null) {
		DialogUtil.message("Couldn't determine the image center.");
		return;
	    }

	    SpTelescopePos base = _tpl.getBasePosition();
	    if (base == null) {
		return;
	    }

	    base.setXY(raDec[0], raDec[1]);
	    return;
	}
    }
}

