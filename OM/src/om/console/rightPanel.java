package om.console;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JPanel;

/** rightPanel contains the dhs and instrumnet status panel
    and sits on the right side of the sequence console.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final class rightPanel extends JPanel implements java.io.Serializable
{
/** public rightPanel(dhsPanel dhs, JPanel status) is
    the class constructor. The class has two constructora so far.
    the two thinga are done during the construction.
    1) adding two panels
    2) adding a border

    @param dhsPanel dhs, JPanel status
    @return  none
    @throws none
*/
  public rightPanel(dhsPanel dhs, JPanel status)
  {

    setLayout(new BorderLayout(0,20));

    add(dhs,"North");
    add(status,"Center");
   }

/** public rightPanel(dhsPanel dhs) is
    the class constructor. The class has two constructora so far.
    the two thinga are done during the construction.
    1) adding dhs panel
    2) adding a border

    @param dhsPanel dhs
    @return  none
    @throws none
*/
  public rightPanel(dhsPanel dhs)
  {
    setLayout(new BorderLayout(20,20));
    add(dhs,"North");
  }
}


