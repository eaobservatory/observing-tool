package om.console;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;

/** class myButton is about
    extending JButton with a few extras.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. but there is only ONE type of host anyway.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class myButton extends JButton   implements java.io.Serializable
{
  public myButton(String title)
  {
    super(title);
    setSize(new Dimension(4,1));
    setFont(new Font("Roman",Font.BOLD,12));
    setEnabled(false);

    setBorder(new BevelBorder(BevelBorder.RAISED,Color.lightGray,Color.darkGray));
    setContentAreaFilled(true);
    this.setBorderPainted(true);
  }

  public myButton()
  {
    super();
    //setSize(new Dimension(4,1));

    setEnabled(false);

    setBorder(new BevelBorder(BevelBorder.RAISED,Color.lightGray,Color.darkGray));
    setContentAreaFilled(true);
    this.setBorderPainted(true);
  }
}

