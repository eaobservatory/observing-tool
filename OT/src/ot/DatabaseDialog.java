/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot;

import jsky.app.ot.OtProps;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Component;
import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jsky.app.ot.OT;
import jsky.app.ot.LoginInfo;
import jsky.app.ot.OtProgWindow;
import jsky.app.ot.OtWindowInternalFrame;
import jsky.app.ot.OtWindowFrame;
import omp.SpClient;
import gemini.sp.SpItem;
import gemini.sp.SpRootItem;
import gemini.sp.SpProg;

/**
 * Dialog for database access.
 *
 * OMP, fetchProgram, storeProgram.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class DatabaseDialog implements ActionListener {
  /**
   * This is a subclass of JPanel so it can be used for internal as well as other frames.
   */
  private DatabaseDialogGUI _w;

  /**
   * Is only used if the OT is started with internal frames.
   */
  private JInternalFrame _internalFrame;

  /**
   * Is only used if the OT is started with internal frames.
   */
  private static JDialog _dialogComponent;

  public static int ACCESS_MODE_FETCH = 0;
  public static int ACCESS_MODE_STORE = 1;

  //private static String [] ACTION_COMMANDS = {"Fetch", "Store"};

  private static int _mode = ACCESS_MODE_FETCH;

  private static SpItem _spItemToBeSaved = null;
  

  /**
   *
   */
  private String _title;

  public DatabaseDialog() {
    _title = "Fetch Program";
    /*_presSource  =*/
    _w = new DatabaseDialogGUI();
    //_description ="The preferences are set with this component.";

    _w.confirmButton.addActionListener(this);
    _w.cancelButton.addActionListener(this);
  }

  public void fetchProgram() {
    if(OT.getDesktop() != null) {
      show(DatabaseDialog.ACCESS_MODE_FETCH, OT.getDesktop());
    }
    else {
      show(DatabaseDialog.ACCESS_MODE_FETCH);
    }
  }

  public void storeProgram(SpItem spItem) {
    _spItemToBeSaved = spItem;
  
    if(OT.getDesktop() != null) {
      show(DatabaseDialog.ACCESS_MODE_STORE, OT.getDesktop());
    }
    else {
      show(DatabaseDialog.ACCESS_MODE_STORE);
    }
  }

  /**
   * For ise with internal frames.
   *
   * @param accessMode fetchProgram or storeProgram.
   */
  public void show(int accessMode, JDesktopPane desktop) {
    _mode = accessMode;
    if(accessMode == ACCESS_MODE_STORE) {
      _title = "Store Progam";
      _w.loginTextBox.setEditable(false);

      if((_spItemToBeSaved != null) && (_spItemToBeSaved instanceof SpProg)) {
        _w.loginTextBox.setText(((SpProg)_spItemToBeSaved).getProjectID());
      }
      else {
        _w.loginTextBox.setText("");
      }
    }
    else {
      _title = "Fetch Progam";
      _w.loginTextBox.setEditable(true);
    }
  
    if(desktop != null) {
      _internalFrame = new JInternalFrame(_title);
      _internalFrame.getContentPane().add(_w);
      desktop.add(_internalFrame, JLayeredPane.MODAL_LAYER);
      _w.setVisible(true);
      _internalFrame.setVisible(true);
      _internalFrame.setLocation(150, 150);
      _internalFrame.pack();
    }

    _w.setVisible(true);
  }

  /**
   * For use in no-internal-frames mode.
   */
  public void show(int accessMode) {
    _mode = accessMode;
    if(accessMode == ACCESS_MODE_STORE) {
      _title = "Store Progam";

      if((_spItemToBeSaved != null) && (_spItemToBeSaved instanceof SpProg)) {
        _w.loginTextBox.setText(((SpProg)_spItemToBeSaved).getProjectID());
      }
      else {
        _w.loginTextBox.setText("");
      }
      _w.loginTextBox.setEditable(false);
    }
    else {
      _title = "Fetch Progam";
      _w.loginTextBox.setEditable(true);
    }

    
    if(_dialogComponent == null) {
      if(OT.getDesktop() != null) {
        _dialogComponent = new JDialog();
      }
      else {
        _dialogComponent = new JDialog();
      }
      _dialogComponent.getContentPane().add(_w);
      _dialogComponent.pack();

      // center the window on the screen
      Dimension dim = _dialogComponent.getSize();
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      _dialogComponent.setLocation(screen.width/2 - dim.width/2, screen.height/2 - dim.height/2);
    }

    _dialogComponent.setTitle(_title);
    _dialogComponent.setVisible(true);
  }


  public void hide() {
    if(_internalFrame != null) {
      _internalFrame.dispose();
    }
    else {
      _dialogComponent.setVisible(false);
    }
  }

  /**
   * Set database access mode.
   *
   * Database access modes: {@link #ACCESS_MODE_FETCH} or {@link #ACCESS_MODE_STORE}.
   */
  public void setMode(int mode) {
    _mode = mode;
  }

  protected static void fetchProgram(String projectID, String password) {
    SpItem spItem = null;
   
    try {
      spItem = (new SpClient()).fetchProgram(projectID, password);
    }
    catch(Exception e) {
      JOptionPane.showMessageDialog(_dialogComponent, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    // database argument is not needed, 0 is just a dummy.
    LoginInfo li = new LoginInfo(projectID, 0, password);

    if (OT.getDesktop() == null) {
      new OtWindowFrame(new OtProgWindow((SpRootItem) spItem, li, null)); //_spProgKey));
    }
    else {
      Component c = new OtWindowInternalFrame(new OtProgWindow((SpRootItem) spItem, li, null)); // _spProgKey));
      OT.getDesktop().add(c, JLayeredPane.DEFAULT_LAYER);
      OT.getDesktop().moveToFront(c);
    }
  }

  protected static void storeProgram(String password) {
    String result = null;

    try {
      result = (new SpClient()).storeProgram(_spItemToBeSaved, password);
    }
    catch(Exception e) {
      JOptionPane.showMessageDialog(_dialogComponent, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    JOptionPane.showMessageDialog(_dialogComponent, result, "Database Message", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * The standard actionPerformed method to handle the "confirm" and "cancel"
   * buttons.
   */
  public void actionPerformed(ActionEvent evt) {
 
    Object w  = evt.getSource();

    if (w == _w.confirmButton) {
      if(_mode == ACCESS_MODE_STORE) {
        //  loginTextBox contains the proejctID aka Science Program name.
	storeProgram(_w.passwordTextBox.getText());
      }
      else {
        //  loginTextBox contains the proejctID aka Science Program name.
	fetchProgram(_w.loginTextBox.getText(), _w.passwordTextBox.getText());
      }

      hide();
      return;
    }

    if (w == _w.cancelButton) {
      hide();
      return;
    }
  }
}

