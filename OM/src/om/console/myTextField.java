package om.console;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;

/** class myTextField is about extending JTextField with a few extras.
    Please note this is serialized for RMI calls
    
    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class myTextField extends JTextField // implements java.io.Serializable
{
  public myTextField (String text)
 {
    super(text);
    this.setFont(new Font("Roman",Font.BOLD,12));
    this.setForeground(Color.black);
    this.setBackground(Color.white);
    Insets m=new Insets(5,5,5,5);
    this.setBorder(new EmptyBorder(m));
    this.setEditable(false);
 }
}

