/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package edfreq;

import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class Edmenu extends JMenuBar //implements ActionListener
{
   public JMenuItem save;

   public Edmenu ( ActionListener target )
   {
      super();

      JMenu fileMenu   = new JMenu ( "File" );
      JMenuItem open   = new JMenuItem ( "Open" );
      JMenuItem save   = new JMenuItem ( "Save" );
      JMenuItem saveAs = new JMenuItem ( "Save As ..." );
      JMenuItem exit   = new JMenuItem ( "Exit" );
      open.addActionListener   ( target );
      save.addActionListener   ( target );
      saveAs.addActionListener ( target );
      exit.addActionListener   ( target );
      fileMenu.add ( open );
      fileMenu.add ( save );
      fileMenu.add ( saveAs );
      fileMenu.add ( new JSeparator() );
      fileMenu.add ( exit );

      add ( fileMenu );
   }

//   public void actionPerformed ( ActionEvent e )
//   {
//System.out.println ( "Edmenu: got event" );
//   }
}
