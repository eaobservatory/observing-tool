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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import ot.jcmt.inst.editor.edfreq.FrontEnd;

/**
 * Panel containing frequency editor and data reduction editor.
 * 
 * @author M.Folger@roe.ac.uk
 */
public class HeterodyneGUI extends JPanel {
  HeterodyneGUI() { //FrontEnd frontEnd, DataReductionScreen dataReductionScreen) {
    //FrontEnd frontEnd = new FrontEnd();
    //frontEnd.setBorder(new TitledBorder("Frequency"));

    //DataReductionScreen dataReductionScreen = new DataReductionScreen();
    //dataReductionScreen.setBorder(new TitledBorder("Data Reduction"));

    setLayout(new BorderLayout());

    add(new JScrollPane(new FrontEnd()), BorderLayout.CENTER);
    add(new JLabel("Layout will be tidied up."), BorderLayout.SOUTH);
    //add(dataReductionScreen, BorderLayout.SOUTH);
  }
}
