package om.console;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.event.*;
import javax.swing.border.*;

/** MicheleStatus class is about
    showing Michelle status.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. but there is only ONE type of host anyway

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
public final class MichelleStatus extends instrumentStatusPanel
{
  /** public MichelleStatus() is
    the class constructor. The class has only one constructor so far.

    @param none
    @return  none
    @throws none
  */
  public MichelleStatus()
  {
    Border border=BorderFactory.createMatteBorder(2, 2, 2, 2, Color.white);

    setBorder(new TitledBorder(border,
			       "Michelle Status",0,0,
			       new Font("Roman",Font.BOLD,14),Color.black));
    
    setLayout(new BorderLayout(10,10));

    JPanel topPanel=new JPanel (new GridLayout(1,2,8,8));

    top=new statusPanel("Michelle State:","Unknown ...");

    topPanel.add(top);

    remaining=new statusPanel("Remaining:","");
    topPanel.add(remaining);

    add(topPanel,"South");

    dataPanel=new JPanel(new GridLayout(10,2,8,8));
    dataPanel.setBorder(new MatteBorder(4,4,4,4,Color.lightGray));

    st=new statusPanel[names.length];
    values =new String[names.length];

    for(int i=0; i<names.length;i++) {
      values[i]=new String();
      st[i]=new statusPanel(names[i],values[i]);
      dataPanel.add(st[i]);
    }
    add(dataPanel);

    bottom=new myTextField("");
    bottom.setColumns(32);

    JPanel mPanel=new JPanel();
    mPanel.setBorder(new TitledBorder(new MatteBorder(4,4,4,4,Color.lightGray),
				      "Message:",0,0,
				      new Font("Roman",Font.BOLD,12), 
				      Color.black));

    mPanel.add(bottom);
    add(mPanel,"North");
  }


  /**  public void messageString (String u) is a public method.
       to return a textfield.
       
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

    if (name.equals("mchState")) {
      String state = "UNKNOWN";
      try {
	int code = Integer.parseInt (value);
	switch (code) {
	case 0:
	  state = "IDLE";
	  break;
	case 1:
	  state = "PAUSED";
	  break;
	case 2:
	  state = "BUSY";
	  break;
	case 3:
	  state = "ERROR";
	  break;
	default:
	  state = "UNKNOWN";
	  break;
	}
      } catch (NumberFormatException e) {}
      top.getValu().setText(state);
      return;
    }

    if(name.equals("message")) {
      bottom.setText(value);
      return;
    }

    for(int i = 0; i < dramaParameters.length; i++) {
      if(name.equals(dramaParameters[i])) {
        st[i].setValue(value);
      }
    }
    
    if(name.equals("camera")) {
      if(value.equals("imaging")) {
        setImaging();
      }
      else {
        setSpectroscopy();
      }
    }

    if(name.equals("polarimetry")) {
      if(value.equals("no")) {
        setPolarimetry(false);
      }
      else {
        setPolarimetry(true);
      }
    }
    
    if(name.equals("countdown")) {
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
    try {
      for(i=0; i<names.length;i++)
      {
        st[i].getValu().setText((String)m.elementAt(i));
      }
    
      bottom.setText((String)m.elementAt(i));
      top.getValu().setText((String)m.elementAt(i+1));
      remaining.getValu().setText((String)m.elementAt(i+2));
    }
    catch(ArrayIndexOutOfBoundsException e) {
      System.err.println(e + ": Model provided values only for parameters 0 .. " + i);
    }      
  }


  /**  public Vector getModel ()
    to get the object status.

    @param none
    @return  Vector
    @throws none
  */
  public Vector getModel ()
  {
    Vector model=new Vector();
    
    for(int i=0; i<names.length;i++)
    {
      model.addElement(st[i].getValu().getText());
    }
    model.addElement(bottom.getText());
    model.addElement(top.getValu().getText());
    model.addElement(remaining.getValu().getText());

    return model;
  }

  protected void setImaging() {
    st[3].setEnabled(false);
    st[4].setEnabled(false);
    st[5].setEnabled(false);
    st[6].setEnabled(false);
    st[7].setEnabled(false);
    st[12].setEnabled(false);
    st[19].setEnabled(false);
  }
  
  protected void setSpectroscopy() {
    st[3].setEnabled(true);
    st[4].setEnabled(true);
    st[5].setEnabled(true);
    st[6].setEnabled(true);
    st[7].setEnabled(true);
    st[12].setEnabled(true);
    st[19].setEnabled(true);
  }

  protected void setPolarimetry(boolean pol) {
    st[18].setEnabled(pol);
  }
 
  private myTextField bottom;
  private statusPanel top;
  private statusPanel remaining;
  private JPanel dataPanel;
  private statusPanel[] st;
  private String[] values;

  /**
   * List of parameter labels used in GUI.
   *
   * In terms of number and order of elements the names array has to correspond to {@link dramaParameters}.
   */
  private String [] names={"Camera",
                           "Polarimetry",
                           "Filter",
                           "Grating",
                           "Wavelength",
                           "Focal Plane Mask",
                           "Position Angle",
                           "Pixel Sampling",
                           "Pixel FOV",
                           "Coadds",
                           "Config Type",
                           "Science FOV",
                           "Spectral Cover",
                           "Chop Frequency",
                           "Exposure Time",
                           "Obs Time",
                           "Acq Mode",
                           "Det Duty Cycle",
                           "Waveplate Pos.",
                           "Detector Pos."};

  /**
   * List of parameter strings used in DRAMA tasks.
   *
   * In terms of number and order of elements the dramaParameters array has to correspond to {@link names}.
   */
  private String [] dramaParameters =
                          {"camera",		   // "Camera",
                           "polarimetry",          // "Polarimetry",
                           "filtName",	           // "Filter",
                           "gratName",	           // "Grating",
                           "gratPos",	           // "Wavelength",
                           "slitName",	           // "Focal Plane Mask",
                           "posAngle",             // "Position Angle",
                           "pixelSampling",        // "Pixel Sampling",
                           "pixelSize",	           // "Pixel Size",
                           "numExp",	           // "Coadds",
                           "obsType",              // "Observation Type",
                           "fieldOfView",          // "Science Field of View",
                           "specCoverage",         // "Spectral Coverage",
                           "chopFrequency",        // "Chop Frequency",
                           "expTime",	           // "Exposure Time",
                           "obsTime",	           // "Observation Time",
                           "acqMode",	           // "Acquisition Mode",
                           "dutyCycle",            // "Detector Duty Cycle"
                           "wavepos",              // Position of waveplate
                           "tPixel"};              // Position of detector
			   
}

