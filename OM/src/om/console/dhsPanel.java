package om.console;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JPanel;
import om.util.OracColor;

/**  dhsPanel class is about
    Showing status of the drama dhs.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final class dhsPanel extends JPanel implements messageInterface,java.io.Serializable
{

/** public dhsPanel() is
    the class constructor. The class has only one constructor so far.
    the panel is located in the right upper of the sequence console Frame

    @param none
    @return  none
    @throws none
*/
  public dhsPanel()
  {
      //Border border1=BorderFactory.createMatteBorder(12, 12,
        //    12,12, Color.lightGray);

      Border border1=BorderFactory.createMatteBorder(2, 2,
            2,2, Color.white); //a new border required by gsw on April 07, 2000


      setBorder(new TitledBorder(border1,
      "Data Taking and Filing Status",0,0,
      new Font("Roman",Font.BOLD,14),Color.black));

      setLayout(new BorderLayout(20,20));

      dataPanel=new JPanel();
      dataPanel.setLayout(new GridLayout(3,2,8,8));

      Border border=BorderFactory.createMatteBorder(6, 6,
            6,6, Color.lightGray);

      dataPanel.setBorder(border);

      filePrefix=new statusPanel(names[0], "");
      dataPanel.add(filePrefix);

      lastTaken=new statusPanel(names[1], "");
      dataPanel.add(lastTaken);

      lastSaved=new statusPanel(names[2], "");
      dataPanel.add(lastSaved);

      receiving=new statusPanel(names[3], "");
      dataPanel.add(receiving);

      saving=new statusPanel(names[4], "");
      dataPanel.add(saving);

      received=new statusPanel(names[5], "");
      dataPanel.add(received);

      add(dataPanel);

      JPanel bottom=new JPanel();
      bottom.setLayout(new GridLayout(2,1,6,10));
      bottom.setBorder(border);

      groupNo=new statusPanel(names[6], "");
      bottom.add(groupNo);

      taking=new statusPanel(names[7], "");
      taking.getNam().setForeground(OracColor.blue);
      bottom.add(taking);

      add(bottom,"South");
  }

  /** private String _getParamValue (String msg)
      Return the value part of a DHS parameter value message string
    @param String msg
    @return String
    @throws none
  */
/*  private String _getParamValue (String msg)
    {
      String temp = "";
      try {
        int colonpos = msg.indexOf("::");
	temp = msg.substring(colonpos+2).trim();
      } catch (IndexOutOfBoundsException e) {
	System.out.println ("Problem decoding value from "+msg);
      }
      return temp;
    }
*/
  /**  public void messageString (String u) is  a public method.
    to reponse an incoming message. Put the incoming string into the dhs panel

    @param String u
    @return  none
    @throws none
  */
  public void messageString (String name, String value)
  {
    
//    try {
      if(name.equals("groupN")) {
	    //	    String temp=new String("000000000"+u.substring(12));
	    //String temp = _getParamValue (u);
	    //	    groupNo.getValu().setText(temp.substring(temp.length()-5));
	groupNo.getValu().setText(value);
	return;
      }
            
      if(name.equals("obsTaken")) {
	    String num = "     ";
	    String prefix = " ";
	    try {
	      int pos = value.indexOf("_");
	      num = value.substring(pos+1).trim();
	      if (pos >=0) {
		prefix = value.substring (0,pos+1);
		filePrefix.getValu().setText(prefix);
	      }
	    } catch (IndexOutOfBoundsException e) {
	      System.out.println ("Problem decoding obs number from "+value);
	    }
	    lastTaken.getValu().setText(num);
	    return;
      }

      if(name.equals("obsFiled"))
	  {
	    String num = "     ";
	    try {
	      int pos = value.indexOf("_");
	      num = value.substring(pos+1).trim();
	    } catch (IndexOutOfBoundsException e) {
	      System.out.println ("Problem decoding obs number from "+value);
	    }
	    lastSaved.getValu().setText(num);
	    return;
	  }
      
      if(name.equals("obsTaking"))
	  {
	    String num = "     ";
	    String prefix = " ";
	    try {
	      int pos = value.indexOf("_");
	      num = value.substring(pos+1).trim();
	      if (pos>=0) {
		prefix = value.substring (0,pos+1);
		filePrefix.getValu().setText(prefix);
	      }
	    } catch (IndexOutOfBoundsException e) {
	      System.out.println ("Problem decoding obs number from "+value);
	    }
	    taking.getValu().setText(num);

	    return;

// 	    if(u.length()>22) {
// 	      String prefix = u.substring (15);
// 	      String number = u.substring (u.length()-5);
// 	      filePrefix.getValu().setText(u.substring(15,u.length()-5));
// 	      taking.getValu().setText(u.substring(u.length()-5));
// 	    }else{
// 	      taking.getValu().setText("");
// 	    }
// 	    return;
      }
      
      if(name.equals("obsFiling:"))
	  {
	    String num = "     ";
	    try {
	      int pos = value.indexOf("_");
	      num = value.substring(pos+1).trim();
	    } catch (IndexOutOfBoundsException e) {
	      System.out.println ("Problem decoding obs number from "+value);
	    }
	    saving.getValu().setText(num);
	    return;
	  }
      
      if(name.equals("obsReceiving:"))
	  {    
	    String num = "     ";
	    try {
	      int pos = name.indexOf("_");
	      num = name.substring(pos+1).trim();
	    } catch (IndexOutOfBoundsException e) {
	      System.out.println ("Problem decoding obs number from "+name);
	    }
	    receiving.getValu().setText(num);
	    return;
	  }
      
      if(name.equals("intReceivedN:"))
	  {
	    received.getValu().setText(value.substring(value.length()-1));
	    return;
	  }
      
//    }catch (StringIndexOutOfBoundsException e) {
//      System.out.println ("Caught exception: string was "+ u);
//    }
  }

  public void close () { this.disable();}
  
  /**  public void setModel (Vector m)
    to recover the object status.

    @param Vector
    @return  none
    @throws none
  */
  public void setModel (Vector m)
  {
    filePrefix.getValu().setText((String)m.elementAt(0));
    lastTaken.getValu().setText((String)m.elementAt(1));
    lastSaved.getValu().setText((String)m.elementAt(2));
    receiving.getValu().setText((String)m.elementAt(3));
    saving.getValu().setText((String)m.elementAt(4));
    received.getValu().setText((String)m.elementAt(5));
    groupNo.getValu().setText((String)m.elementAt(6));
    taking.getValu().setText((String)m.elementAt(7));
  }


  /**  public Vector getModel ()
    to get the object status.

    @param none
    @return  Vector
    @throws none
  */
  public Vector getModel ()
  {
    model.addElement(filePrefix.getValu().getText());
    model.addElement(lastTaken.getValu().getText());
    model.addElement(lastSaved.getValu().getText());
    model.addElement(receiving.getValu().getText());
    model.addElement(saving.getValu().getText());
    model.addElement(received.getValu().getText());
    model.addElement(groupNo.getValu().getText());
    model.addElement(taking.getValu().getText());

    return model;
  }


  private JPanel dataPanel;
  private statusPanel filePrefix;
  private statusPanel lastTaken;
  private statusPanel lastSaved;
  private statusPanel receiving;
  private statusPanel saving;
  private statusPanel received;
  private statusPanel groupNo;
  private statusPanel taking;
  private Vector model=new Vector();
  private String[ ] names={"File Prefix:","Last taken:","Last Saved:","Receiving:","Saving:",
                          "Received:", "Current group number:", "Currently taking observation:"};
}





