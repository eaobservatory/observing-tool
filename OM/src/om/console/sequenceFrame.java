package om.console;

import om.sciProgram.AlertBox;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.*;

/** 
    final public class sequenceFrame is a major bit in this java package.
    it is the sequence console.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. But there is only one type of host anyway.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class sequenceFrame extends JFrame
  implements java.io.Serializable, Cloneable
{
/** 
    public sequenceFrame(String instrument, String t, sendCmds cmd) is
    the class constructor. The class has only two constructors so far.
    the two things are done during this construction.
    1) adding GUI bits
    2) adding one listener

    @param String instrument, String t, sendCmds cmd
    @return  none
    @throws ClassNotFoundException, InstantiationException
*/
  public sequenceFrame(String instrument, String t, sendCmds cmd)
  {
    inst=instrument; //name of the instrument
    observation=t;   //title of the observation
    comSent=cmd;     //command message out class

    setTitle("ORAC-OM sequence console for "+instrument);

    if (instrument.equals("UFTI")) {
      setSize(720,760);
    } else if (instrument.equals("CGS4")) {
      setSize(720,900);
    } else if(instrument.equals("Michelle")) {
      setSize(775, 900);
    } else if(instrument.equals("UIST")) {
      setSize(775, 900);
    } else {
      setSize(720,760);
    }

    setLocation(200,200);

    addWindowListener(new WindowAdapter()
		      {
			public void windowClosing(WindowEvent e)
			  {
			    comSent.setExit();
			  }

			public void windowSizeChanged(WindowEvent e)
			  {
			    validate();
			  }
		      });

    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout(/*20,20*/));

    //select an instrument status display class which must be
    //a subclass of JPanel
    try { 

      statusDisplay=Class.forName("om.console."+instrument+"Status");

    } catch (ClassNotFoundException e) {
      System.out.println ("Error finding status display class for "
			  +instrument);
      System.out.println(e);
    }

    try {
      status=(instrumentStatusPanel)statusDisplay.newInstance();
      
    } catch (InstantiationException e) {
      System.out.println ("Error instantiating status display class for "
			  +instrument);
      System.out.println(e);
    } catch  (IllegalAccessException f) {
      System.out.println ("Error accessing status display class for "
			  +instrument);
      System.out.println(f);
    }

    //control the look of buttons and menu items
    items=new itemsShown(comSent);

    sDisplay=new sequencePanel(t);
    contentPane.add(sDisplay,"Center");

    dhs=new dhsPanel();

    rightPanel rPanel=new rightPanel(dhs,status);
    contentPane.add(rPanel,"East");

    menus=new menuBar(comSent,items);
    
    setJMenuBar(menus);

    upper=new upperPanel(instrument);
    contentPane.add(upper,"North");

    commentPanel comments=new commentPanel(comSent);
    contentPane.add(comments,"South");

    comPanel=new commandPanel(comSent);
    contentPane.add(comPanel,"West");

    _movie=new movie(comSent, (instrument.equalsIgnoreCase("Michelle") || 
                               instrument.equalsIgnoreCase("UIST")));
    comSent.linkSequencePanel(sDisplay);
    comSent.linkMovieFrame(_movie);

    // Disable the movie button if inst. is IRCAM3 or CGS4
    if (instrument.equals("CGS4") || instrument.equalsIgnoreCase("IRCAM3")) {
      System.out.println ("Disabling movie");
      comPanel.getButton(6).setVisible(false);
      menus.getMenuItem(6).setVisible(false);
    }

  }


  /**
     public sequenceFrame(myFrameModel model) is
     the class constructor. The class has only two constructors so far.
     the two things are done during the construction.
     1) adding/creating GUI bits from the model
     2) adding one listener

     @param myFrameModel
     @return  none
     @throws ClassNotFoundException, InstantiationException
  */
  public sequenceFrame(myFrameModel model, commandSentInterface com)
  {
    inst=model.getInstrument();
    observation=model.getObservation();
    comSent=new sendCmds(com);

    setTitle("ORAC-OM sequence console for "+inst);

    if(inst.equals("UFTI")){
      setSize(720,760);
    } else if (inst.equals("CGS4")) {
      setSize(720,900);
    } else if (inst.equals("Michelle")) {
      setSize(775, 900);
    } else {
      setSize(720,900);
    }

    setLocation(200,200);

    addWindowListener(new WindowAdapter()
		      {
			public void windowClosing(WindowEvent e)
			  {
			    comSent.setExit();
			  }
			
			public void windowSizeChanged(WindowEvent e)
			  {
			    validate();
			  }
		      });
    
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout(/*20,20*/));

    //select an instrument status display class which must be
    //a subclass of JPanel
    try { 

      statusDisplay=Class.forName("om.console."+inst+"Status");

    } catch (ClassNotFoundException e) {
      System.out.println ("Error finding status display class for "
			  +inst);
      System.out.println(e);
    }

    try {
      status=(instrumentStatusPanel)statusDisplay.newInstance();
      
    } catch (InstantiationException e) {
      System.out.println ("Error instantiating status display class for "
			  +inst);
      System.out.println(e);
    } catch  (IllegalAccessException f) {
      System.out.println ("Error accessing status display class for "
			  +inst);
      System.out.println(f);
    }

    status.setModel(model.getStatusPanel());

    //control the look of buttons and menu items
    items=new itemsShown(comSent);

    sDisplay=new sequencePanel(observation);
    contentPane.add(sDisplay,"Center");

    dhs=new dhsPanel();
    dhs.setModel(model.getDhsPanel());
    rightPanel rPanel=new rightPanel(dhs,status);
    contentPane.add(rPanel,"East");
    
    menus=new menuBar(comSent,items);
    
    menus.setModel(model.getMenuBar());
    setJMenuBar(menus);
    
    upper=new upperPanel(inst);
    contentPane.add(upper,"North");
    upper.setModel(model.getUpperPanel());
    
    commentPanel comments=new commentPanel(comSent);
    contentPane.add(comments,"South");
    
    comPanel=new commandPanel(comSent);
    contentPane.add(comPanel,"West");
    
    _movie=new movie(comSent, (inst.equalsIgnoreCase("Michelle") ||
                               inst.equalsIgnoreCase("UIST")));
    comSent.linkSequencePanel(sDisplay);
    comSent.linkMovieFrame(_movie);

    // Disable the movie button if inst. is CGS4 or IRCAM3
    if (inst.equals("CGS4") || inst.equalsIgnoreCase("IRCAM3")) {
      System.out.println ("Disabling movie");
      comPanel.getButton(6).setVisible(false);
      menus.getMenuItem(6).setVisible(false);
    }

  }


  /** 
      public String getInstrument() is a public method
      to return an instrument name

      @param none
      @return  String
      @throws none
  */
  public String getInstrument() 
    {
      return inst;
    }


  /**
     public  sendCmds getSendCmds() is a public method
     to return a sendCmds object

     @param none
     @return   sendCmds
     @throws none
  */
  public  sendCmds getSendCmds()
  {
    return comSent;
  }
  
  /**
     public menuBar getMyMenuBar() is a public method
     to return a menuBar object
     
     @param none
     @return menuBar
     @throws none
     
  */
  public menuBar getMyMenuBar()
    {
      return menus;
    }
  
  /**
     public void setLinks (messageServerInterface m) is a public method
     to setup links between a messageServerInterface and other objects
     
     @param messageServerInterface
     @return none
     @throws none
     
  */
  public void setLinks (messageServerInterface m)
    {
      try {
	
	if (inst.equals("UFTI")) {
	  m.linkUFTIStatus( (UFTIStatus) status);
	}else if (inst.equals("CGS4")) {
	  m.linkCGS4Status( (CGS4Status) status);
	}else if (inst.equals("IRCAM3")) {
	  m.linkIRCAM3Status( (IRCAM3Status) status);
	}else if (inst.equals("Michelle")) {
          m.linkMichelleStatus( (MichelleStatus) status);
	}else if (inst.equals("UIST")) {
          m.linkUISTStatus( (UISTStatus) status);
	}

	m.linkCommandSent(comSent);
	m.linkItemsShown(items);
	m.linkSequencePanel(sDisplay);
	m.linkUpperPanel(upper);
	m.linkDhsPanel(dhs);
	
	m.linkMovieFrame(_movie);
	
      } catch (RemoteException re) {
	System.out.println ("Exception in sequenceFrame:"+re); }
      
      items.linkPanels(comPanel,sDisplay,menus,upper,_movie);
    }

  /**
     public void resetObs(String obs,String execName) is a public method
     to reset an observation.
     
     @param String obs,String execName
     @return none
     @throws none
     
  */
  public void resetObs(String obs,String execName)
    {

      // Can only reset the observation if the current sequence state is
      // "Idle" or stopped.
      String state = upper.getStatus().getText();
      if (state.equalsIgnoreCase("Idle") || state.equalsIgnoreCase("Stopped")) {
	
	JOptionPane dialog = new JOptionPane();
	int selection = dialog.showConfirmDialog
	  (this,"New Observation for: "+inst, "Comfirmation",
	   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null );
	
	if(selection==dialog.NO_OPTION) {
	  return;
	}
	
	if(selection==dialog.YES_OPTION) {
	  
	  sDisplay.setDataValid (false);
	  
	  comSent.setLoad(execName);
	  
	  // Get the size of the list : Not important to us, but this
	  // will block until the sequence data from the above load
	  // request has arrived
	  int size = sDisplay.getListData().size();
	  
	  // Now we know the data has arrived we can update the display
	  observation=obs;
	  sDisplay.setObsTitle(obs);
	  
	  sDisplay.getJScrollPane().getViewport().setViewPosition
	    (new Point(0,0));
	  sDisplay.getMyList().setSelectedIndex(0);
	  sDisplay.validate();
	}
	
      } else {
	new ErrorBox ("Sequencer must be stopped to send a new observation");
      }
    }

  /**
     public sequencePanel getSequencePanel () is a public method
     to return a sequencePanel
     
     @param none
     @return sequencePanel
     @throws none
     
  */
   public sequencePanel getSequencePanel() {return sDisplay;}

   public String getObservation() {return observation;}
   public sendCmds getSendComands () {return comSent;}
   public JPanel getStatusPanel() {return status;}
   public itemsShown getItemsShown () {return items;}
   public dhsPanel getDhsPanel () {return dhs;}
   public upperPanel getUpperPanel () {return upper;}
   public commandPanel getCommandPanel () {return comPanel;}
  

  public void setMovie(movie m) {_movie=m;}
  public movie getMovie() {return _movie;}


  /** 
      public void connectTCS() is a public method
      to connect the TCS

      @param none
      @return none
      @throws none
      
  */
  public void connectTCS()
    {
      comSent.setTCSconnected();
    }

  private menuBar menus;
  private dhsPanel dhs;
  private upperPanel upper;
  private itemsShown items;
  
  private commandPanel comPanel;
  private sequencePanel sDisplay;
  
  private sendCmds comSent;
  private Class statusDisplay;
  private instrumentStatusPanel status;
  private String inst,observation;
  private movie _movie;

  private boolean telescopeConnected=false;
}
