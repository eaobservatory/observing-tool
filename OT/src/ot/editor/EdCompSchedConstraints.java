/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.editor;

import gemini.sp.SpItem;
import gemini.sp.obsComp.SpSchedConstObsComp;
import orac.util.OracUtilities;
import ot.util.DialogUtil;

import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.editor.OtItemEditor;

import java.util.Date;


/**
 * This is the editor for Site Quality component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdCompSchedConstraints extends OtItemEditor implements TextBoxWidgetWatcher {

  private SchedConstraintsGUI _w;       // the GUI layout panel

  private SpSchedConstObsComp _schedConstObsComp;

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdCompSchedConstraints() {
    _title       ="Scheduling Constraints";
    _presSource  = _w = new SchedConstraintsGUI();
    _description ="Observing constraints set here are used to schedule the telescope.";

    _w.earliest.addWatcher(this);
    _w.latest.addWatcher(this);
  }

  public void setup(SpItem spItem) {
    _schedConstObsComp = (SpSchedConstObsComp)spItem;
    super.setup(spItem);

    if(_schedConstObsComp.getEarliest().equals(SpSchedConstObsComp.NO_VALUE)) {
      _schedConstObsComp.initEarliest(OracUtilities.toISO8601(new Date()));
      _updateWidgets();
    }

    if(_schedConstObsComp.getLatest().equals(SpSchedConstObsComp.NO_VALUE)) {
      _schedConstObsComp.initLatest(OracUtilities.toISO8601(new Date()));
      _updateWidgets();
    }
  }


  /**
   * Implements the _updateWidgets method from OtItemEditor in order to
   * setup the widgets to show the current values of the item.
   */
  protected void _updateWidgets() {
      _w.earliest.setValue(_schedConstObsComp.getEarliest());
      _w.latest.setValue(_schedConstObsComp.getLatest());
  }

  /**
   * Watch changes to the "Earliest" and "Latest" text boxes.
   */
  public void textBoxKeyPress(TextBoxWidgetExt tbw) {
    // By converting the string to Date and back to a an ISO8601 format string
    // it gets tidied up in case the user made minor mistakes regarding the format.

    try {
      if(tbw == _w.earliest) {
        String earliest = OracUtilities.toISO8601(OracUtilities.parseISO8601(_w.earliest.getText()));
        _schedConstObsComp.setEarliest(earliest);
      }

      if(tbw == _w.latest) {
        String latest   = OracUtilities.toISO8601(OracUtilities.parseISO8601(_w.earliest.getText()));
        _schedConstObsComp.setLatest(latest);
      }
    }
    catch(Exception e) {
      DialogUtil.error(_w, e.getMessage());
    }
  }
 
  /**
   * Text box action, ignored.
   */
  public void textBoxAction(TextBoxWidgetExt tbw) {
    _updateWidgets();
  }

}
