package om.console;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JPanel;

/** final class targetPanel  gives a panel to show
    the telescope status. Note that it is NOT in use now.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. But there is only one type of host anyway.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final class targetPanel extends JPanel implements java.io.Serializable
{
  public targetPanel()
  {
    Border border=BorderFactory.createMatteBorder(6, 6,
            6,6, Color.lightGray);

    setBorder(new TitledBorder(border,
    "Observation Status",0,0,
      new Font("Roman",Font.BOLD,14),Color.black));

       setLayout(new BorderLayout(20,20));

      JPanel left=new JPanel();

      left.setLayout(new GridLayout(5,2,6,10));

      left.add(new myLabel("Time: "));
      left:add(timer);

      left.add(new myLabel("Target:"));
      left:add(target);

      left.add(new myLabel("Ra:"));
      left:add(ra);

      left.add(new myLabel("Dec:"));
      left:add(dec);

      left.add(new myLabel("Weather:"));
      left:add(weather);


      add(left);
    }

    private myLabel timer=new myLabel ("  ");
    private myLabel target=new myLabel ("  ");
    private myLabel ra=new myLabel ("  ");
    private myLabel dec=new myLabel ("  ");
    private myLabel weather=new myLabel ("  ");
}





