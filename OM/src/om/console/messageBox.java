package om.console;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.rmi.*;

/** messageBox class is about
    showing a message.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. But there is only one type of host anyway.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class messageBox extends JFrame implements java.io.Serializable
{
/** public messageBox(String _m) is
    the class constructor. The class has only one constructor so far.
    One thing is done during construction i.e. creation of a JOptionPane


    @param Stirng m
    @return  none
    @throws none
*/
  public messageBox(String _m)
  {
    m=_m;
    dialog= new JOptionPane();
    dialog.showMessageDialog(this,m, "Message Box",
					JOptionPane.PLAIN_MESSAGE);

  }

  private JOptionPane dialog;
  private String m=new String();
}





