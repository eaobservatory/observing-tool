package om.console;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.border.*;
import om.util.OracColor;

/** upperPanel class is about showing the oos status on the top panel.
    There are serveral satatus dir the oos stored in a drama parameter.
    They are "idle", "paused", "running" and stop
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. But there is only one type of host anyway.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class upperPanel extends JPanel implements messageInterface,java.io.Serializable
{
  /** public upperPanel(String instName) is
    the class constructor. The class has only one constructor so far.

    @param String
    @return  none
    @throws none
  */

  static String bell = "\u0007";

  public upperPanel(String instName)
  {
    //Border border=BorderFactory.createMatteBorder(12, 12,
      //      12,12, Color.lightGray);

     Border border=BorderFactory.createMatteBorder(2, 2,
            2,2, Color.white); //a new border required by gsw on April 07, 2000


    setBorder(new TitledBorder(border,
      "",0,0,
      new Font("Roman",Font.BOLD,14),Color.black));

    setLayout(new GridLayout(1,8,0,6));

    myLabel seq=new myLabel("Sequence");
    seq.setFont(new Font("Roman",Font.BOLD,12));
    myLabel ins=new myLabel("Instrument");
    ins.setFont(new Font("Roman",Font.BOLD,12));

    running=new myTextField("Idle");
    running.setFont(new Font("Roman",Font.BOLD,12));
    myTextField name=new myTextField(instName);
    name.setFont(new Font("Roman",Font.BOLD,12));
    telescopeConnection=new myTextField("Disconnected");
    telescopeConnection.setFont(new Font("Roman",Font.BOLD,11));

    add(seq);
    add(running);
    add(new myLabel(""));
    add(new myLabel("Telescope"));
    add(telescopeConnection);
    add(new myLabel(""));
    add(ins);
    add(name);
  }

  /**public void messageString (String u) is a public method.
     to reponse an incoming message of the oos status.

    @param String
    @return  none
    @throws none
  */
  public void messageString (String name, String value)
  {
    running.setText(value);

    if(value.equals("Running"))
      running.setBackground(OracColor.green);

    if(value.equals("Stopped")) {
      running.setBackground(OracColor.red);
      System.out.println (bell+bell);
      System.out.println (bell+bell);
    }
    if(value.equals("Paused")) {
      running.setBackground(Color.yellow);
      System.out.println (bell);
    }
    if(value.equals("Idle"))
      running.setBackground(Color.white);
  }

   /**  public void close () is a public method.
    to disable the panel.

    @param none
    @return  none
    @throws none
  */
  public void close () { this.disable();}



  /**  public void setModel (Vector m)
    to recover the object status.

    @param Vector
    @return  none
    @throws none
  */
  public void setModel (Vector m)
  {
    running.setText((String)m.elementAt(0));
    telescopeConnection.setText((String)m.elementAt(1));
  }


  /**  public Vector getModel ()
    to get the object status.

    @param none
    @return  Vector
    @throws none
  */
  public Vector getModel ()
  {
    model.addElement(running.getText());
    model.addElement(telescopeConnection.getText());

    return model;
  }


  public myTextField getTCSconnection() {return telescopeConnection;}

  public myTextField getStatus () {return running;}
  private myTextField running, telescopeConnection;
  private Vector model=new Vector();
}
