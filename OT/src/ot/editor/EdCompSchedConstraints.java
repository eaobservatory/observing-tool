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

import java.util.Calendar;
import java.util.Date;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;


/**
 * This is the editor for Site Quality component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdCompSchedConstraints extends OtItemEditor implements TextBoxWidgetWatcher,
									  ActionListener {

  private SchedConstraintsGUI _w;       // the GUI layout panel

  private SpSchedConstObsComp _schedConstObsComp;

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdCompSchedConstraints() {
    _title       ="Scheduling Constraints";
    _presSource  = _w = new SchedConstraintsGUI();
    _description ="Observing constraints set here are used to schedule the telescope.";

    ButtonGroup meridianApproachButtons = new ButtonGroup();
    meridianApproachButtons.add(_w.meridianApproachRising);
    meridianApproachButtons.add(_w.meridianApproachSetting);
    meridianApproachButtons.add(_w.meridianApproachAny);

    _w.earliest.addWatcher(this);
    _w.latest.addWatcher(this);
    _w.minElevation.addWatcher(this);
    _w.maxElevation.addWatcher(this);
    _w.period.addWatcher(this);
    _w.meridianApproachRising.addActionListener(this);
    _w.meridianApproachSetting.addActionListener(this);
    _w.meridianApproachAny.addActionListener(this);
  }

  public void setup(SpItem spItem) {
    _schedConstObsComp = (SpSchedConstObsComp)spItem;
    super.setup(spItem);

    if(_schedConstObsComp.getEarliest().equals(SpSchedConstObsComp.NO_VALUE)) {
      _schedConstObsComp.initEarliest(OracUtilities.toISO8601(new Date()));
      _updateWidgets();
    }

    if(_schedConstObsComp.getLatest().equals(SpSchedConstObsComp.NO_VALUE)) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 30);
      _schedConstObsComp.initLatest(OracUtilities.toISO8601(calendar.getTime()));
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
      _w.minElevation.setValue(_schedConstObsComp.getMinElevation());
      _w.maxElevation.setValue(_schedConstObsComp.getMaxElevation());
      _w.period.setValue(_schedConstObsComp.getPeriod());

      if(_schedConstObsComp.getMeridianApproach() != null) {
        if(_schedConstObsComp.getMeridianApproach().equals(SpSchedConstObsComp.SOURCE_RISING)) {
          _w.meridianApproachRising.setValue(true);
        }
	else {
          _w.meridianApproachSetting.setValue(true);
        }
      }
      else {
        _w.meridianApproachAny.setValue(true);
      }
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
        String latest   = OracUtilities.toISO8601(OracUtilities.parseISO8601(_w.latest.getText()));
        _schedConstObsComp.setLatest(latest);
      }

      if(tbw == _w.minElevation) {
        _schedConstObsComp.setMinElevation(_w.minElevation.getValue());
      }

      if(tbw == _w.maxElevation) {
        _schedConstObsComp.setMaxElevation(_w.maxElevation.getValue());
      }

      if(tbw == _w.period) {
        _schedConstObsComp.setPeriod(_w.period.getValue());
      }
    }
    catch(Exception e) {
      DialogUtil.error(_w, e.getMessage());
    }
  }
 
  /**
   * Text box action.
   */
  public void textBoxAction(TextBoxWidgetExt tbw) {
    _updateWidgets();
  }

  /**
   * Radio button action.
   */
  public void actionPerformed(ActionEvent evt) {
    Object w = evt.getSource();

    if (w == _w.meridianApproachRising) {
      _schedConstObsComp.setMeridianApproach(SpSchedConstObsComp.SOURCE_RISING);
    }

    if (w == _w.meridianApproachSetting) {
      _schedConstObsComp.setMeridianApproach(SpSchedConstObsComp.SOURCE_SETTING);
    }

    if (w == _w.meridianApproachAny) {
      _schedConstObsComp.setMeridianApproach(null);
    }
  }
}
