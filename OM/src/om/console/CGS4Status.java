package om.console;
import java.util.*;
import java.math.BigDecimal;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.event.*;
import javax.swing.border.*;

/** CGS4Status class is about
    showing CGS4 status.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent.
    It is this one casuing probelms in repeated RMI calls

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
public final class CGS4Status extends instrumentStatusPanel
{
  /** public CGS4Status( ) is
    the class constructor. The class has only one constructor so far.

    @param none
    @return  none
    @throws none
  */
  public CGS4Status( )
  {
      //Border border=BorderFactory.createMatteBorder(12, 12,
        //    12,12, Color.lightGray);


      Border border=BorderFactory.createMatteBorder(2, 2,
            2,2, Color.white); //a new border required by gsw on April 07, 2000

      setBorder(new TitledBorder(border,
      "CGS4 Status",0,0,
      new Font("Roman",Font.BOLD,14),Color.black));

  		setLayout( new BorderLayout(2,10) );


      JPanel topPanel=new JPanel (new GridLayout(1,2,8,8));

      top=new statusPanel("CGS4 State:","Unknown ...");

      topPanel.add(top);

      remaining=new statusPanel("Remaining:","");
      topPanel.add(remaining);

      add(topPanel,"South");

      dataPanel=new JPanel(new GridLayout(7,2,8,12));
      dataPanel.setBorder(new MatteBorder(4,4,4,4,Color.lightGray));

      st=new statusPanel[14];
      values =new String[14];

      for(int i=0; i<14;i++)
      {
        values[i]=new String();
        st[i]=new statusPanel(names[i],values[i]);

//         if(i==9)
//           dataPanel.add(new JLabel(""));
//         else
	dataPanel.add(st[i]);
      }
      add(dataPanel);

      bottom=new myTextField("");
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
  public void messageString (String name, String value) {
    //    System.out.println("MESS:"+u);

     try {
      if(name.equals("MODE")) {
	st[5].getValu().setText(value);
	return;
      }

      if(name.startsWith("NEXP") && !(name.substring(4,5).equals("_")) ) {
	      st[7].getValu().setText(value);
	      return;
      }

      if(name.equals("WL_MAX")) {
	      try {
	        BigDecimal wlm = new BigDecimal (value);
	        try {
	          BigDecimal wlmMax = wlm.setScale(3,BigDecimal.ROUND_HALF_DOWN);
	          wlMaxStr = wlmMax.toString();
	          st[4].getValu().setText(wlMinStr+"-"+wlMaxStr);
	      }catch (Exception e){}
	    }catch (NumberFormatException e) {}
	      return;
      }

      if(name.equals("WL_MIN")) {
	    try {
	      BigDecimal wlm = new BigDecimal (value);
	      try {
	        BigDecimal wlmMin = wlm.setScale(3,BigDecimal.ROUND_HALF_DOWN);
	        wlMinStr = wlmMin.toString();
	        st[4].getValu().setText(wlMinStr+"-"+wlMaxStr);
	    }catch (Exception e){}
	    }catch (NumberFormatException e) {}
	    return;
      }

      if(name.equals("CU_LAMP")) {
 	      st[13].getValu().setText(value);
 	      return;
      }

      if(name.equals("EXPTIME")) {
	    try {
	      BigDecimal et = new BigDecimal (value);
	    try {
	        BigDecimal et2 = et.setScale(2,BigDecimal.ROUND_HALF_DOWN);
	        String etStr = et2.toString();
	        st[3].getValu().setText(etStr);
	    }catch (Exception e){}
	    }catch (NumberFormatException e) {}
	    // 	st[3].getValu().setText(u.substring(14));
 	    return;

      //       } else if(u.substring(0,13).equals("INST@NEXP_OBS")) {
      // 	st[7].getValu().setText(u.substring(15));
      // 	return;

      }

      if(name.equals("SAMPLING")) {
	      st[12].getValu().setText(value);
	      return;
      }

      if(name.equals("INT_TOTAL")) {
	      st[7].getValu().setText(value);
	      return;
      }

      if(name.equals("INT_STATE")) {
	      top.getValu().setText(value);
	      return;
      }

      if(name.equals("GRAT_NAME")) {
	      st[0].getValu().setText(value);
	      return;
      }

      if(name.equals("CONF_TYPE")) {
	      st[11].getValu().setText(value);
	      return;
      }

      if(name.equals("COUNTDOWN")) {
	      System.out.println ("Remaining is "+value);
	      remaining.getValu().setText(value);
	      return;
      }


      if(name.equals("GRAT_ORDER")) {
	      st[1].getValu().setText(value);
	      return;
      }

      if(name.equals("CVF_OFFSET")) {
	      st[6].getValu().setText(value);
	      return;
      }

      if(name.equals("MOTOR_STATE")) {
	      top.getValu().setText(value);
	      return;
      }

      if(name.equals("ALD_MESSAGE")) {
	      bottom.setText(value);
	      return;
      }

      if(name.equals("AIM_MESSAGE")) {
	      top.getValu().setText(value);
	      return;
      }

      if(name.equals("POSITION_ANGLE")) {
	      try {
	        BigDecimal pa = new BigDecimal (value);
	        try {
	         BigDecimal pa2 = pa.setScale(2,BigDecimal.ROUND_HALF_DOWN);
	         String paStr = pa2.toString();
	         st[8].getValu().setText(paStr);
	        }catch (Exception e){}
	      }catch (NumberFormatException e) {}
      	//	st[8].getValu().setText(u.substring(21));
	      return;
      }

      if(name.equals("SLIT_WIDTH_PXL")) {
	      try {
	        BigDecimal sw = new BigDecimal (value);
	      try {
	        BigDecimal sw2 = sw.setScale(2,BigDecimal.ROUND_HALF_DOWN);
	        String swStr = sw2.toString();
	        st[10].getValu().setText(swStr);
	      }catch (Exception e){}
	    }catch (NumberFormatException e) {}
	    //	st[10].getValu().setText(u.substring(21));
    	return;
     }

     if(name.equals("GRAT_WAVELENGTH")) {
	      try {
	        BigDecimal gw = new BigDecimal (value);
	      try {
	        BigDecimal gw2 = gw.setScale(4,BigDecimal.ROUND_HALF_DOWN);
	        String gwStr = gw2.toString();
	        st[2].getValu().setText(gwStr);
	      }catch (Exception e){}
	    }catch (NumberFormatException e) {}
	      //	st[2].getValu().setText(u.substring(22));
	      return;
      }

    }catch (StringIndexOutOfBoundsException e) {
      System.out.println ("Caught exception: name was \"" + name + "\" and value was \"" + value + "\"");
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
    for(i=0; i<14;i++)
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
    for(int i=0; i<14;i++)
    {
      model.addElement(st[i].getValu().getText());
    }
    model.addElement(bottom.getText());
    model.addElement(top.getValu().getText());
    model.addElement(remaining.getValu().getText());

    return model;
  }

  private String wlMinStr=new String("null");
  private String wlMaxStr=new String("null");
  private Vector model=new Vector();
  private myTextField bottom;
  private statusPanel top;
  private statusPanel remaining;
  private JPanel dataPanel;
  private statusPanel[] st;
  private String[] values;
  private String [] names={"Grating","Order","\u03BB"+" Cen.","Exp.Time","Wave.Range","Acq.Mode","CVF Offset",
                            "Exp/Integ","Position Angle","Int/Observe","Slit Width","Config Type","Sampling","Cal Lamp"};
}

