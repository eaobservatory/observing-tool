package om.console;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.rmi.*;

/** ErrorBox class is about
    showing an error message.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class ErrorBox extends JFrame implements java.io.Serializable
{
/** public ErrorBox(String _m) is
    the class constructor. The class has only one constructor so far.
    One thing is done during construction i.e. creation of a JOptionPane


    @param Stirng m
    @return  none
    @throws none
*/
  public ErrorBox(String _m)
  {
    m=_m;
    dialog= new JOptionPane();
    dialog.showMessageDialog(this,m, "Error Message",
					JOptionPane.ERROR_MESSAGE);

  }

  private JOptionPane dialog;
  private String m=new String();
}





