package om.console;
import java.util.*;
import java.math.BigDecimal;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.event.*;
import javax.swing.border.*;

/** IRCAM3Status class concerns showing IRCAM3 status.  For a different
    instrument, there will be a different class for the status display.
    Please note this is serialized for RMI calls even though I was told
    that I can't serialize a swing object, since it is host-dependent. 
    However, there is only one type of host anyway.

    @version 1.0 2000 March 23 / April 18
    @author M.Tan@roe.ac.uk, Malcolm J. Currie
*/
public final class IRCAM3Status extends instrumentStatusPanel
{
    String nCols = "0";
    String nRows = "0";
    String xSize = "0";
    String ySize = "0";
    double pixsiz = 0.0;
    int    nc =0;
    int    nr =0;

  /** public IRCAM3Status( ) is
    the class constructor.  The class has only one constructor so far.

    @param none
    @return  none
    @throws none
  */
  public IRCAM3Status( )
  {
      //Border border = BorderFactory.createMatteBorder(12, 12,
      //                12,12, Color.lightGray);

       Border border = BorderFactory.createMatteBorder(2, 2,
            2,2, Color.white); //a new border required by gsw on 2000 April 07

      setBorder(new TitledBorder(border, "IRCAM3 Status", 0, 0,
                new Font("Roman", Font.BOLD,14), Color.black));

      setLayout(new BorderLayout(10, 10));

      JPanel topPanel = new JPanel (new GridLayout(1, 2, 8, 8));

      top = new statusPanel("IRCAM3 State:","Unknown ...");

      topPanel.add(top);

      remaining = new statusPanel("Remaining:","");
      topPanel.add(remaining);

      add(topPanel,"South");

      dataPanel = new JPanel(new GridLayout(4,2,8,8));
      dataPanel.setBorder(new MatteBorder(4,4,4,4,Color.lightGray));

      st = new statusPanel[8];
      values = new String[8];

      for(int i=0; i<8;i++)
      {
        values[i] = new String();
        st[i] = new statusPanel(names[i],values[i]);
        dataPanel.add(st[i]);
      }
      add(dataPanel);

      bottom = new myTextField("");
      bottom.setColumns(32);

      JPanel mPanel = new JPanel();
      mPanel.setBorder(new TitledBorder(
                       new MatteBorder(4, 4, 4, 4, Color.lightGray),
                       "Message:",0,0,
                       new Font("Roman", Font.BOLD, 12), Color.black));

      mPanel.add(bottom);
      add(mPanel, "North");
   }

  /**  public void messageString (String u) is a public method.
    to return a text field.

    @param String u
    @return  none
    @throws none
  */
  public myTextField getStatusFrameValue (){ return top.getValu();}

   /**  public void messageString (String u) is a public method
    to repond to the incoming message.

    @param String u
    @return  none
    @throws none
  */
  public void messageString (String name, String value) {

    
    //System.out.println("MESS:"+u);
    //try {
      if(name.equals("MODE")) {
	st[1].getValu().setText(value);
	return;
      } else if(name.equals("EXPTIME")) {
	try {
	  BigDecimal et = new BigDecimal (value);
	  try {
	    BigDecimal et2 = et.setScale(2,BigDecimal.ROUND_HALF_DOWN);
	    String etStr = et2.toString();
	    st[2].getValu().setText(etStr);
	  }catch (Exception e){}
	}catch (NumberFormatException e) {}
	return;
      } else if(name.equals("NEXP_OBS")) {
	st[3].getValu().setText(value);
	return;
      } else if(name.equals("NPIXELS_C")) {
	nCols = value;
	st[4].getValu().setText(nCols+"x"+nRows);
	try {
	  nc = Integer.parseInt (nCols);
          BigDecimal psx = new BigDecimal (pixsiz*(double)nc);
	  xSize = (psx.setScale(2,BigDecimal.ROUND_HALF_DOWN)).toString();
	  st[7].getValu().setText(xSize+"x"+ySize); 
	}catch(Exception e){}
	return;	
      } else if(name.equals("NPIXELS_R")) {
	nRows = value;
	st[4].getValu().setText(nCols+"x"+nRows);
	try {
	  nr = Integer.parseInt (nRows);
          BigDecimal psy = new BigDecimal (pixsiz*(double)nr);
	  ySize = (psy.setScale(2,BigDecimal.ROUND_HALF_DOWN)).toString();
	  st[7].getValu().setText(xSize+"x"+ySize); 
	}catch(Exception e){}
	return;	
//       } else if(name.equals("FieldSize")) {
// 	st[7].getValu().setText(value);
// 	return;
      } else if(name.equals("COUNTDOWN")) {
	remaining.getValu().setText(value);
	return;
      } else if(name.equals("CONF_TYPE")) {
	st[5].getValu().setText(value);
	return;	
      } else if(name.equals("PIXEL_SIZE")) {
	try {
	  BigDecimal ps = new BigDecimal (value);
	  pixsiz = ps.doubleValue();
	  try {
	    BigDecimal ps2 = ps.setScale(4,BigDecimal.ROUND_HALF_DOWN);
	    String psStr = ps2.toString();
	    st[6].getValu().setText(psStr);
	  }catch (Exception e){}
	}catch (NumberFormatException e) {}
	try {
          BigDecimal psy = new BigDecimal (pixsiz*(double)nr);
	  ySize = (psy.setScale(2,BigDecimal.ROUND_HALF_DOWN)).toString();
          BigDecimal psx = new BigDecimal (pixsiz*(double)nc);
	  xSize = (psx.setScale(2,BigDecimal.ROUND_HALF_DOWN)).toString();
	  st[7].getValu().setText(xSize+"x"+ySize); 
	}catch(Exception e){}
	return;
      } else if(name.equals("FILTER_NAME")) {
	st[0].getValu().setText(value);
	return;
      } else if(name.equals("ALD_MESSAGE")) {
	bottom.setText(value);
	return;
      } else if(name.equals("AIM_MESSAGE")) {
	top.getValu().setText(value);
	return;	
      }

//    }catch (StringIndexOutOfBoundsException e) {
//      System.out.println ("Caught exception: string was "+ u);
//    }

  }

  /**  public void close () is a public method to disable the panel.

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
  private JPanel dataPanel;
  private statusPanel[] st;
  private String[] values;
  private String [] names = {"Filter:","Acq. Mode","Exp. Time","Coadds",
                             "Readout", "Config Type", "Pixel Size", 
                             "Science FOV"};
}

