package om.console;
import java.rmi.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;

/** class myMenuItem is about extending JMenuItem with a few extras.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/

final public class myMenuItem extends JMenuItem implements java.io.Serializable
{
  public myMenuItem (String text)
  {
    super(text);
  }
}

