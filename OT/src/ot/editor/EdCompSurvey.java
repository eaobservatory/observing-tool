// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2003                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;
import java.util.Observer;
import java.util.Observable;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import gemini.sp.SpItem;
import gemini.sp.SpTelescopePos;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.obsComp.SpSurveyObsComp;
import jsky.app.ot.editor.EdCompTargetList;
import jsky.app.ot.tpe.TelescopePosEditor;
import jsky.app.ot.tpe.TpeManager;

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
public final class EdCompSurvey extends EdCompTargetList
  implements ListSelectionListener, Observer {

  private static final String [] COLUMN_NAMES = { "Name", "X Axis", "Y Axis", "Coord System", "Remaining", "Priority" };

  private SurveyGUI               _surveyGUI;   // the GUI layout panel

  private SpSurveyObsComp    _surveyObsComp = null;
  private SpTelescopeObsComp _telescopeObsComp = null;

  private boolean _ignoreEvents = false;

  /**
   * Only used for GUIs that display list of survey targets and the
   * target information editor side by side (rather than using a JTabbedPane).
   *
   * If the GUI is changed such that the list of survey targets and the
   * target information editor are displayed side by side (rather than
   * using a JTabbedPane) then all occurrances of _doNotUpdateSurveyWidgets
   * should be uncommented in the code and all occurrances of _doNotUpdateWidgets
   * should be commented out.
   */
//  private boolean _doNotUpdateSurveyWidgets = false;

  /**
   * Flag is used to delay _updateWidgets until the "Edit Target" tab has been selected.
   *
   * This flag is only needed due a java bug: When a target is selected on the
   * "Survey Targets" tab then some of the "Edit Target" tab widgets become visible although
   * "Edit Target" tab is not selected and should stay in the background.
   *
   * If the GUI is changed such that the list of survey targets and the
   * target information editor are displayed side by side (rather than
   * using a JTabbedPane) then all occurrances of _doNotUpdateSurveyWidgets
   * should be uncommented in the code and all occurrances of _doNotUpdateWidgets
   * should be commented out.
   */
  private boolean _doNotUpdateWidgets = false;

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdCompSurvey() {
    _title       = "Survey Information";
    _presSource  = _surveyGUI = new SurveyGUI(_w);
    _description = "Use this editor to enter the survey information.";

    for(int i = 0; i < 100; i++) {
      _surveyGUI.remaining.addItem("" + i);
      _surveyGUI.priority.addItem("" + i);
    }

    _surveyGUI.fieldTable.getSelectionModel().addListSelectionListener(this);
    _surveyGUI.addButton.addActionListener(this);
    _surveyGUI.removeButton.addActionListener(this);
    _surveyGUI.removeAllButton.addActionListener(this);
    _surveyGUI.loadButton.addActionListener(this);
    _surveyGUI.remaining.addActionListener(this);
    _surveyGUI.priority.addActionListener(this);

    _surveyGUI.fieldTable.setModel(new DefaultTableModel() {
      public boolean isCellEditable(int row, int column) {
       return false;
      }	
    });
    _surveyGUI.fieldTable.setCellSelectionEnabled(false);
    _surveyGUI.fieldTable.setColumnSelectionAllowed(false);
    _surveyGUI.fieldTable.setRowSelectionAllowed(true);
    
    TelescopePosWidgetWatcher telescopePosWidgetWatcher = new TelescopePosWidgetWatcher();

    _name.addWatcher(telescopePosWidgetWatcher);
    _xaxis.addWatcher(telescopePosWidgetWatcher);
    _yaxis.addWatcher(telescopePosWidgetWatcher);
    _system.addWatcher(telescopePosWidgetWatcher);

    _surveyGUI.tabbedPane.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        _updateWidgets();
      }
    });

    // This should not be user editable for now (at least not for WFCAM).
    // If a the survey component is scheduled as part of an MBS then
    // remaining count and priority settings will probably be taken care of
    // by the MSB and the could removed from the survey component completely.
    _surveyGUI.remaining.setEnabled(false);
    _surveyGUI.priority.setEnabled(false);
  }

  public void setup( SpItem spItem ) {
    _surveyObsComp = (SpSurveyObsComp)spItem;

    _otItemEditorWindow.getUndoButton().addActionListener(this);
    _otItemEditorWindow.getShowEditPencilButton().addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        _undo();
      }

      public void mouseReleased(MouseEvent e) {
        _undo();
      }
    });


    if(_surveyObsComp.size() < 1) {
      _surveyObsComp.addSpTelescopeObsComp();

      _surveyObsComp.setRemaining(0, 0);
      _surveyObsComp.setPriority( 0, 0);
    }

    super.setup(_surveyObsComp.getSpTelescopeObsComp(_surveyObsComp.getSelectedTelObsComp()));
  }


  protected void _updateWidgets() {
    if(_doNotUpdateWidgets) {
      return;
    }

    _ignoreEvents = true;


//    if(!_doNotUpdateSurveyWidgets) {
      _updateFieldTable();
      _ignoreEvents = true;
      _surveyGUI.fieldTable.setRowSelectionInterval(_surveyObsComp.getSelectedTelObsComp(),
                                                    _surveyObsComp.getSelectedTelObsComp());

      _surveyGUI.remaining.setSelectedIndex(_surveyObsComp.getRemaining(_surveyGUI.fieldTable.getSelectedRow()));
      _surveyGUI.priority.setSelectedIndex(_surveyObsComp.getPriority(_surveyGUI.fieldTable.getSelectedRow()));
//    }


    if(_surveyObsComp.size() == 0) {
      _w.setVisible(false);
    }
    else {
      _w.setVisible(true);
      super._updateWidgets();
    }
    
    _ignoreEvents = false;
  }

  private void _updateFieldTable() {
    _updateFieldTable(_surveyObsComp.getSelectedTelObsComp());
  }

  private void _updateFieldTable(int selectedRow) {
    _ignoreEvents = true;

    String [][] data = new String[_surveyObsComp.size()][];

    for(int i = 0; i < data.length; i++) {
      data[i] = _getRowData(_surveyObsComp.getSpTelescopeObsComp(i).getPosList().getBasePosition(), i);
    }

    ((DefaultTableModel)_surveyGUI.fieldTable.getModel()).setDataVector(data, COLUMN_NAMES);

    if(_surveyGUI.fieldTable.getColumnModel().getColumn(4).getCellRenderer() == null) {
      _surveyGUI.fieldTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer());
    }

    if(_surveyGUI.fieldTable.getColumnModel().getColumn(5).getCellRenderer() == null) {
      _surveyGUI.fieldTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer());
    }

    ((DefaultTableCellRenderer)_surveyGUI.fieldTable.getColumnModel().getColumn(4).getCellRenderer()).setBackground(_w.getBackground());
    ((DefaultTableCellRenderer)_surveyGUI.fieldTable.getColumnModel().getColumn(4).getCellRenderer()).setForeground(Color.gray);
    ((DefaultTableCellRenderer)_surveyGUI.fieldTable.getColumnModel().getColumn(5).getCellRenderer()).setBackground(_w.getBackground());
    ((DefaultTableCellRenderer)_surveyGUI.fieldTable.getColumnModel().getColumn(5).getCellRenderer()).setForeground(Color.gray);

    if(selectedRow < 0) {
      _surveyGUI.fieldTable.setRowSelectionInterval(0, 0);
    }
    else {
      if(selectedRow >= _surveyGUI.fieldTable.getRowCount()) {
        _surveyGUI.fieldTable.setRowSelectionInterval(_surveyGUI.fieldTable.getRowCount() - 1,
                                                      _surveyGUI.fieldTable.getRowCount() - 1);
      }
      else {
        _surveyGUI.fieldTable.setRowSelectionInterval(selectedRow, selectedRow);
      }
    }

    _ignoreEvents = false;
  }


  private String [] _getRowData(SpTelescopePos tp) {
    Vector v = new Vector();
    v.addElement(tp.getName());
	
    if(tp.getSystemType() == SpTelescopePos.SYSTEM_SPHERICAL) {
      if(tp.isOffsetPosition()) {
        v.addElement("" + tp.getXaxis() + " (\u2206)");
        v.addElement("" + tp.getYaxis() + " (\u2206)");
      }
      else {
        v.addElement(tp.getXaxisAsString());
        v.addElement(tp.getYaxisAsString());
      }
    }
    else {
      v.addElement("  - - -");
      v.addElement("  - - -");
    }

    switch(tp.getSystemType()) {
      case SpTelescopePos.SYSTEM_CONIC:
        v.addElement("Orb. Elem.");
        break;
      case SpTelescopePos.SYSTEM_NAMED:
        v.addElement("Planets etc.");
        break;
      case SpTelescopePos.SYSTEM_SPHERICAL:
        v.addElement(tp.getCoordSysAsString());
        break;
    }

    v.addElement("0");
    v.addElement("0");

    String [] result = new String[COLUMN_NAMES.length];
    v.toArray(result);

    return result;
  }

  private String [] _getRowData(SpTelescopePos tp, int index) {
    String [] result = _getRowData(tp);
    result[4] = "" + _surveyObsComp.getRemaining(index);
    result[5] = "" + _surveyObsComp.getPriority(index);

    return result;
  }


  public void valueChanged(ListSelectionEvent e) {
    if(_ignoreEvents) {
      return;
    }

    _surveyTargetSelectionChanged();
  }

  private void _surveyTargetSelectionChanged() {
    _telescopeObsComp = _surveyObsComp.getSpTelescopeObsComp(_surveyGUI.fieldTable.getSelectedRow());
//    _surveyObsComp.setTable(_telescopeObsComp.getTable());
    _telescopeObsComp.getAvEditFSM().addObserver(this);

    // If the GUI is changed such that the list of survey targets and the
    // target information editor are displayed side by side (rather than
    // using a JTabbedPane) then all occurrances of _doNotUpdateSurveyWidgets
    // should be uncommented in the code and all occurrances of _doNotUpdateWidgets
    // should be commented out.
    _doNotUpdateWidgets = true;
//    _doNotUpdateSurveyWidgets = true;
    super.setup(_telescopeObsComp);

    TelescopePosEditor tpe = TpeManager.get(_telescopeObsComp);
    if (tpe != null) tpe.reset(_telescopeObsComp);

//    _doNotUpdateSurveyWidgets = false;
    _doNotUpdateWidgets = false;

    _surveyObsComp.setSelectedTelObsComp(_surveyGUI.fieldTable.getSelectedRow());

    _surveyGUI.remaining.setSelectedIndex(_surveyObsComp.getRemaining(_surveyGUI.fieldTable.getSelectedRow()));
    _surveyGUI.priority.setSelectedIndex( _surveyObsComp.getPriority( _surveyGUI.fieldTable.getSelectedRow()));
  }


  public void actionPerformed(ActionEvent e) {
    if(_ignoreEvents) {
      return;
    }


    if(e.getSource() == _surveyGUI.addButton) {
      _ignoreEvents = true;
      ((DefaultTableModel)_surveyGUI.fieldTable.getModel()).addRow(
        _getRowData(_surveyObsComp.addSpTelescopeObsComp().getPosList().getBasePosition()));

      _surveyObsComp.setRemaining(0, _surveyGUI.fieldTable.getRowCount() - 1);
      _surveyObsComp.setPriority( 0, _surveyGUI.fieldTable.getRowCount() - 1);

      _ignoreEvents = false;

      return;
    }

    if(e.getSource() == _surveyGUI.removeButton) {
      _surveyObsComp.removeSpTelescopeObsComp(_surveyGUI.fieldTable.getSelectedRow());

      _updateFieldTable();

      _surveyTargetSelectionChanged();

      return;
    }

    if(e.getSource() == _surveyGUI.removeAllButton) {
      _surveyObsComp.removeAllSpTelescopeObsComponents();
      _updateFieldTable(0);
      return;
    }

    if(e.getSource() == _surveyGUI.loadButton) {
      _loadFields();
      return;
    }

    if(e.getSource() == _surveyGUI.remaining) {
      _surveyObsComp.setRemaining(_surveyGUI.remaining.getSelectedIndex(), _surveyGUI.fieldTable.getSelectedRow());
      ((DefaultTableModel)_surveyGUI.fieldTable.getModel()).setValueAt("" + _surveyGUI.remaining.getSelectedIndex(),
                                                                       _surveyGUI.fieldTable.getSelectedRow(), 4);
      return;
    }

    if(e.getSource() == _surveyGUI.priority) {
      _surveyObsComp.setPriority(_surveyGUI.priority.getSelectedIndex(), _surveyGUI.fieldTable.getSelectedRow());
      ((DefaultTableModel)_surveyGUI.fieldTable.getModel()).setValueAt("" + _surveyGUI.priority.getSelectedIndex(),
                                                                       _surveyGUI.fieldTable.getSelectedRow(), 5);
      return;
    }

    if(e.getSource() == _otItemEditorWindow.getUndoButton()) {
      _undo();
      return;
    }

    if(e.getSource() == _otItemEditorWindow.getShowEditPencilButton()) {
      return;
    }

    super.actionPerformed(e);
  }


  public void update(Observable o, Object arg) {
    _surveyObsComp.getTable().edit();
  }


  private void _loadFields() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.showOpenDialog(_surveyGUI);
    File file = fileChooser.getSelectedFile();

    if(file == null) {
      return;
    }

    try {
      _surveyObsComp.load(file.getPath());
      _updateFieldTable();
    }
    catch(Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(_surveyGUI, e, "Could not load survey targets", JOptionPane.WARNING_MESSAGE);
    }
  }

  private void _undo() {
    if(_telescopeObsComp != null) {
      _telescopeObsComp.getAvEditFSM().undo();
      _updateWidgets();
    }
  }

  /** Watches name, xaxis, yaxis and coordinate system widgets and updates the list of survey fields accordingly. */
  class TelescopePosWidgetWatcher implements TextBoxWidgetWatcher, DropDownListBoxWidgetWatcher {
    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
      if(!_curPos.isBasePosition()) {
        return;
      }
    
      if(tbwe == _name) {
        ((DefaultTableModel)_surveyGUI.fieldTable.getModel()).setValueAt(_name.getValue(), _surveyGUI.fieldTable.getSelectedRow(), 0);
      }

      if(tbwe == _xaxis) {
        ((DefaultTableModel)_surveyGUI.fieldTable.getModel()).setValueAt(_xaxis.getValue(), _surveyGUI.fieldTable.getSelectedRow(), 1);
      }


      if(tbwe == _yaxis) {
        ((DefaultTableModel)_surveyGUI.fieldTable.getModel()).setValueAt(_yaxis.getValue(), _surveyGUI.fieldTable.getSelectedRow(), 2);
      }
    }

    public void textBoxAction(TextBoxWidgetExt tbwe) { }

    public void dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
      if(!_curPos.isBasePosition()) {
        return;
      }

      if(dd == _system) {
        ((DefaultTableModel)_surveyGUI.fieldTable.getModel()).setValueAt(val, _surveyGUI.fieldTable.getSelectedRow(), 3);
      }
    }

    public void dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) { }
  }
}

