package om.console;
import java.rmi.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;

/** class myLabel is about
    extending JLabel with a few extras.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/

final public class myLabel extends JLabel implements java.io.Serializable
{
  public myLabel (String text)
  {
    super(text);
    this.setFont(new Font("Roman",Font.BOLD,12));
    this.setForeground(Color.black);
    Insets m=new Insets(5,5,5,5);
    this.setBorder(new EmptyBorder(m));
  }
}

