/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.inst.editor.edfreq;

import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class Edmenu extends JMenuBar implements ActionListener
{
   public JMenuItem save;

   public Edmenu ( ActionListener target )
   {
      super();

      JMenu fileMenu = new JMenu ( "File" );
      JMenuItem save = new JMenuItem ( "Save" );
      save.addActionListener ( target );
      fileMenu.add ( save );
      add ( fileMenu );
   }

   public void actionPerformed ( ActionEvent e )
   {
System.out.println ( "Edmenu: got event" );
   }
}
