package om.console;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.event.*;
import javax.swing.border.*;

/** UFTIStatus class is the status display class for the UFTI instrument.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. But there is only one type of host anyway.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
public final class UFTIStatus extends instrumentStatusPanel
{
  /** public UFTIStatus( ) is
    the class constructor. The class has only one constructor so far.

    @param none
    @return  none
    @throws none
  */
  public UFTIStatus( )
  {

    Border border=BorderFactory.createMatteBorder(2, 2,
                                                  2,2, Color.white);

    setBorder(new TitledBorder(border,
                               "UFTI Status",0,0,
                               new Font("Roman",Font.BOLD,14),Color.black));

    setLayout(new BorderLayout(10,10));

    JPanel topPanel=new JPanel (new GridLayout(1,2,8,8));

    top=new statusPanel("UFTI State:","Unknown ...");

    topPanel.add(top);

    remaining=new statusPanel("Remaining:"," ");
    topPanel.add(remaining);

    add(topPanel,"South");

    st=new statusPanel[9];
    values =new String[9];

    dataPanel = new JPanel(new GridLayout (5,1,8,8));
    dataPanel.setBorder(new MatteBorder(4,4,4,12,Color.lightGray));

    dataPanel1 = new JPanel(new GridLayout(1,2,8,8));
    for(int i=0; i<2;i++) {
      values[i]=new String();
      st[i]=new statusPanel(names[i],values[i]);
      dataPanel1.add(st[i]);
    }
    dataPanel.add(dataPanel1);

    dataPanel2 = new JPanel(new GridLayout(1,2,8,8));
    for(int i=2; i<4;i++) {
      values[i]=new String();
      st[i]=new statusPanel(names[i],values[i]);
      dataPanel2.add(st[i]);
    }
    dataPanel.add(dataPanel2);

    dataPanel3 = new JPanel(new GridLayout(1,2,8,8));
    for(int i=4; i<6;i++) {
      values[i]=new String();
      st[i]=new statusPanel(names[i],values[i]);
      dataPanel3.add(st[i]);
    }
    dataPanel.add(dataPanel3);

    dataPanel4 = new JPanel(new GridLayout(1,1,8,8));

    values[6]=new String();
    st[6]=new statusPanel(names[6],values[6]);
    dataPanel4.add(st[6]);
    dataPanel.add(dataPanel4);

    dataPanel5 = new JPanel(new GridLayout(1,2,8,8));

    for(int i=7; i<9;i++) {
      values[i]=new String();
      st[i]=new statusPanel(names[i],values[i]);
      dataPanel5.add(st[i]);
    }
    dataPanel.add(dataPanel5);

    add (dataPanel, "Center");

    bottom=new myTextField(" ");
    bottom.setColumns(32);

    JPanel mPanel=new JPanel();
    mPanel.setBorder(new TitledBorder(new MatteBorder(4,4,4,4,Color.lightGray),
                                      "Message:",0,0,
                                      new Font("Roman",Font.BOLD,12),Color.black));
    
    mPanel.add(bottom);
    add(mPanel,"North");

   }

  /**  public void messageString (String u) is a public method.
    to retrun a textfield.

    @param String u
    @return  none
    @throws none
  */
  public myTextField getStatusFrameValue (){ return top.getValu();}

   /**  public void messageString (String u) is a public method.
    to reponse the incoming message.

    @param String u
    @return  none
    @throws none
  */
  public void messageString (String name, String value)
  {

    //System.out.println("MESS:"+u);

    if (name.equals("State")) {
      top.getValu().setText(value);
      return;
    }

    if (name.equals("Message")) {
      bottom.setText(value);
      return;
    }

    if (name.equals("Filter")) {
      st[0].getValu().setText(value);
      return;
    }

    if (name.equals("ConfigType")) {
      st[1].getValu().setText(value);
      return;
    }

    if (name.equals("expTime")) {
      st[2].getValu().setText(value);
      return;
    }

    if (name.equals("Coadds")) {
      st[3].getValu().setText(value);
      return;
    }

    if (name.equals("readArea")) {
      st[4].getValu().setText(value);
      return;
    }

    if (name.equals("FieldSize")) {
      st[5].getValu().setText(value);
      return;
    }

    if (name.equals("readMode")) {
      st[6].getValu().setText(value);
      return;
    }

    if (name.equals("FPZ")) {
      st[7].getValu().setText(value);
      return;
    }

    if (name.equals("irpol_ang")) {
      st[8].getValu().setText(value);
      return;
    }

    if (name.equals("Remaining")) {
      remaining.getValu().setText(value);
      return;
    }

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
    int i=0;
    for(i=0; i<8;i++)
    {
      st[i].getValu().setText((String)m.elementAt(i));
    }
    bottom.setText((String)m.elementAt(i));
    top.getValu().setText((String)m.elementAt(i+1));
    remaining.getValu().setText((String)m.elementAt(i+2));
  }


  /**  public Vector getModel ()
    to get the object status.

    @param none
    @return  Vector
    @throws none
  */
  public Vector getModel ()
  {
    for(int i=0; i<8;i++)
    {
      model.addElement(st[i].getValu().getText());
    }
    model.addElement(bottom.getText());
    model.addElement(top.getValu().getText());
    model.addElement(remaining.getValu().getText());

    return model;
  }

  private Vector model=new Vector();
  private myTextField bottom;
  private statusPanel top;
  private statusPanel remaining;
  private JPanel dataPanel, dataPanel1, dataPanel2,
    dataPanel3, dataPanel4, dataPanel5;
  private statusPanel[] st;
  private String[] values;
  private String [] names={"Filter:", "Config Type",
                           "Exp. Time", "Coadds",
                           "Readout", "Science Area",
                           "Acquisition Mode",
                           "FPZ", "Pol. Angle"};
}

