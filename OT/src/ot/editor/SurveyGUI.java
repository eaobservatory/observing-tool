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

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import jsky.app.ot.editor.TelescopeGUI;

/**
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SurveyGUI extends JPanel {

  private TelescopeGUI _telescopeGUI = null; //new TelescopeGUI();

  JComboBox remaining      = new JComboBox();
  JComboBox priority       = new JComboBox();
  JButton addButton        = new JButton("Add");
  JButton removeButton     = new JButton("Remove");
  JButton removeAllButton  = new JButton("Remove all");
  JButton loadButton       = new JButton("Load");
  JTabbedPane tabbedPane   = new JTabbedPane();

  /**
   * List of fiels.
   *
   * Each field is represented by a SpTelescopeObsComp item.
   */
  JTable fieldTable = new JTable();

  public SurveyGUI(TelescopeGUI telescopeGUI) {
    _telescopeGUI = telescopeGUI;

    priority.setFont(new java.awt.Font("Dialog", 0, 12));
    remaining.setFont(new java.awt.Font("Dialog", 0, 12));
    addButton.setFont(new java.awt.Font("Dialog", 0, 12));
    removeButton.setFont(new java.awt.Font("Dialog", 0, 12));
    removeAllButton.setFont(new java.awt.Font("Dialog", 0, 12));
    loadButton.setFont(new java.awt.Font("Dialog", 0, 12));
    tabbedPane.setFont(new java.awt.Font("Dialog", 0, 12));

    setLayout(new BorderLayout());

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(addButton);
    buttonPanel.add(removeButton);
    buttonPanel.add(removeAllButton);
    buttonPanel.add(loadButton);

    JPanel panel = new JPanel();

    JLabel label = new JLabel("Remaining");
    label.setFont(new java.awt.Font("Dialog", 0, 12));
    label.setForeground(Color.black);
    panel.add(label);
    panel.add(remaining);

    label = new JLabel("    Priority");
    label.setFont(new java.awt.Font("Dialog", 0, 12));
    label.setForeground(Color.black);
    panel.add(label);
    panel.add(priority);

    JPanel southPanel = new JPanel(new BorderLayout());
    southPanel.add(panel,       BorderLayout.NORTH);
    southPanel.add(buttonPanel, BorderLayout.SOUTH);

    JPanel surveyPanel       = new JPanel(new BorderLayout());
    surveyPanel.add(new JScrollPane(fieldTable), BorderLayout.CENTER);
    surveyPanel.add(southPanel, BorderLayout.SOUTH);
    surveyPanel.setBorder(new EtchedBorder());
    surveyPanel.setPreferredSize(new Dimension(100, 100));

    tabbedPane.add("Survey Targets",   surveyPanel);
    tabbedPane.add("Target Information", _telescopeGUI);

    add(tabbedPane, BorderLayout.CENTER);
  }

  public TelescopeGUI getTelescopeGUI() {
    return _telescopeGUI;
  }
}

