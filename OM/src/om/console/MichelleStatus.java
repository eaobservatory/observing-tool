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
      Border border=BorderFactory.createMatteBorder(2, 2,
            2,2, Color.white);


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

      dataPanel=new JPanel(new GridLayout(9,2,8,8));
      dataPanel.setBorder(new MatteBorder(4,4,4,4,Color.lightGray));

      st=new statusPanel[names.length];
      values =new String[names.length];

      for(int i=0; i<names.length;i++)
      {
        values[i]=new String();
        st[i]=new statusPanel(names[i],values[i]);
        dataPanel.add(st[i]);
      }
      add(dataPanel);

      bottom=new myTextField("");
      bottom.setColumns(32);

      /**
       * mfo: for testing and debugging.
       */
      bottom.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent evt) {
	    MichelleStatusTest.setParameters(MichelleStatus.this);
	  }
	}
      );



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
    if(name.equals("State"))
    {
      top.getValu().setText(value);
      return;
    }

    if(name.equals("Message"))
    {
      bottom.setText(value);
      return;
    }

    for(int i = 0; i < parameters.length; i++) {
      if(name.equals(parameters[i])) {
        st[i].setValue(value);
      }
    }

    if(name.equals("Camera")) {
      if(value.equals("imaging")) {
        setImaging();
      }
      else {
        setSpectroscopy();
      }
    }
/*    
    if(name.equals("Filter"))
    {
      st[0].getValu().setText(value);
      return;
    }

    if(name.equals("readMode"))
    {
      st[1].getValu().setText(value);
      return;
    }

    if(name.equals("expTime"))
    {
      st[2].getValu().setText(value);
      return;
    }

    if(name.equals("Coadds"))
    {
      st[3].getValu().setText(value);
      return;
    }

    if(name.equals("readArea"))
    {
      st[4].getValu().setText(value);
      return;
    }

    if(name.equals("ConfigType"))
    {
      st[5].getValu().setText(value);
      return;
    }

    if(name.equals("PixelScale"))
    {
      st[6].getValu().setText(value);
      return;
    }

    if(name.equals("FieldSize"))
    {
      st[7].getValu().setText(value);
      return;
    }
*/
    
    if(name.equals("Remaining"))
    {
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
    st[5].setEnabled(false);
    st[6].setEnabled(false);
    st[7].setEnabled(false);
  }
  
  protected void setSpectroscopy() {
    st[3].setEnabled(true);
    st[5].setEnabled(true);
    st[6].setEnabled(true);
    st[7].setEnabled(true);
  }
 
  private myTextField bottom;
  private statusPanel top;
  private statusPanel remaining;
  private JPanel dataPanel;
  private statusPanel[] st;
  private String[] values;

  /**
   * List of parameters used in GUI.
   *
   * In terms of number and order of elements the names array has to correspond to {@link parameters}.
   */
  private String [] names={"Camera",
                           "Polarimetry",
                           "Filter",
                           "Grating",
                           "Wavelength",
                           "Focal Plane Mask",
                           "Position Angle",
                           "Pixel Sampling",
                           "Pixel Size",
                           "Coadds",
                           "Config Type",
                           "Science FOV",
                           "Spectral Cov",
                           "Chop Frequency",
                           "Exposure Time",
                           "Obs Time",
                           "Acq Mode",
                           "Det Duty Cycle"};

  /**
   * List of parameters used in DRAMA tasks.
   *
   * In terms of number and order of elements the parameters array has to correspond to {@link names}.
   */
  private String [] parameters=
                          {"Camera",			// "Camera",
                           "Polarimetry",		// "Polarimetry",
                           "Filter",			// "Filter",
                           "Grating",			// "Grating",
                           "Wavelength",		// "Wavelength",
                           "FocalPlaneMask",		// "Focal Plane Mask",
                           "PositionAngle",		// "Position Angle",
                           "PixelSampling",		// "Pixel Sampling",
                           "PixelSize",			// "Pixel Size",
                           "Coadds",			// "Coadds",
                           "ConfigurationType", 	// "Configuration Type",
                           "ScienceFieldOfView",	// "Science Field of View",
                           "SpectralCoverage",		// "Spectral Coverage",
                           "ChopFrequency",		// "Chop Frequency",
                           "expTime",			// "Exposure Time",
                           "ObservationTime",		// "Observation Time",
                           "AcquisitionMode",		// "Acquisition Mode",
                           "DetectorDutyCycle"};	// "Detector Duty Cycle"};

			   
			   //"","Acquisition Mode","Exposure Time","Coadds","Readout area",
                           //"Config Type", "Pixel Size:",   "Science FOV",""};
}

