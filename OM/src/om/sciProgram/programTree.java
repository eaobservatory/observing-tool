package om.sciProgram;                   

import om.console.*;      //this is for the sequence console
import om.frameList.*;    //import frameList Java bean
import om.dramaSocket.*;

import gemini.sp.*;
import gemini.sp.SpItem;
import gemini.sp.ipc.*;
import orac.ukirt.inst.*;
import orac.ukirt.iter.*;
import orac.ukirt.util.SpTranslator;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.border.*;

import java.rmi.*;
import java.net.*;


/**
   final public class programTree is a panel to select
   an observation from a JTree object

   @version 1.0 1st June 1999
   @author M.Tan@roe.ac.uk
*/
final public class programTree extends JPanel
  implements TreeSelectionListener,ActionListener,frameListListener
{

  /** 
      public programTree(menuSele m) is
      the constructor. The class has only one constructor so far.
      a few thing are done during the construction. They are mainly
      about adding a run button and setting up a listener
      
      @param  none
      @return none
      @throws none
  */
  public programTree(menuSele m)
    {
      Border border=BorderFactory.createMatteBorder(2, 2,
						    2,2, Color.white);
      setBorder(new TitledBorder(border,
				 "Fetched Science Program (SP)",0,0,
				 new Font("Roman",Font.BOLD,12),Color.black));
      
      setLayout( new BorderLayout() );
      
      menu=m;
      run=new JButton("Send for execution");
      run.setMargin(new Insets(5,10,5,10));
      run.setEnabled(false);
      
      run.addActionListener(this);
      add(run,"South");
      
      consoleFrames.addFrameListEventListener(this);

      // Create a popup menu 
      popup = new JPopupMenu();
      edit = new JMenuItem ("Edit Attributes...");
      edit.addActionListener(this);
      popup.add (edit);
      scale = new JMenuItem ("Scale Exposure Times...");
      scale.addActionListener(this);
      popup.add (scale);
      scaleAgain = new JMenuItem ("Re-do Scale Exposure Times");
      scaleAgain.addActionListener(this);
      scaleAgain.setEnabled(false);
      popup.add (scaleAgain);
      
    }
  
  /** public void actionPerformed(ActionEvent evt) is a public method
      to react button actions. The reaction is mainly about to start a
      SGML translation, then a "remote" frame to form a sequence console.
      
      @param ActionEvent
      @return  none
      @throws none
      
  */
  public void actionPerformed (ActionEvent evt)
    {
      
      Object source = evt.getSource();
      
      if (source == run) {

        if (path == null) {
          errorBox =new ErrorBox("You have not selected an observation!"+
                                 "\nPlease select an observation.");
          return;
        }

        execution();
	  
      } else if (source == edit) {
        editAttributes();
      } else if (source == scale) {
        scaleAttributes();
      } else if (source == scaleAgain) {
        rescaleAttributes();
        
      }
    }

  /**
   * private void disableRun()
   *
   * Disable the run option whilst other things are happening
   *
   **/
  private void disableRun() {
    run.setEnabled(false);
    run.setForeground(Color.white);
  }


  /**
   * private void enableRun()
   *
   * Enable the run option once other things have stopped
   *
   **/
  private void enableRun() {
    run.setEnabled(true);
    run.setForeground(Color.black);
  }


  /**
   * private void editAttributes()
   *
   * Invokes the attribute editor on the current item, as long as that
   * item is an observation.
   **/
  private void editAttributes() {
      
    SpItem item=findItem(_spItem,path.getLastPathComponent().toString());

    if (item == null) {
      return;
    }

    // Recheck that this is an observation
    if (item.type()==SpType.OBSERVATION) {

      disableRun();

      SpObs observation = (SpObs) item;

      if (!observation.equals(null)) {
        new AttributeEditor(observation, new javax.swing.JFrame(), true).show();
      } else {
        errorBox =new ErrorBox("Your selection: "+item.getTitle()+
                               " is not an observation");
      }
      enableRun();
    }
  }
 
  
  /**
   * private void scaleAttributes()
   *
   * Invokes the attribute scaler on the current item, as long as that
   * item is an observation.
   **/
  private void scaleAttributes() {
    SpItem item=findItem(_spItem,path.getLastPathComponent().toString());
    if (item == null) {
      return;
    }

    // Recheck that this is an observation
    if (item.type()==SpType.OBSERVATION) {

      disableRun();

      SpObs observation = (SpObs) item;
      if (!observation.equals(null)) {
	new AttributeEditor(observation, new javax.swing.JFrame(), true,
			    "EXPTIME",
			    haveScaled.contains(observation),
			    lastScaleFactor(),
			    false).show();
	double sf = AttributeEditor.scaleFactorUsed();
	if (sf > 0) {
	  haveScaled.addElement(observation);
	  scaleFactors.addElement(new Double(sf));
	  scaleAgain.setEnabled(true);
	  scaleAgain.setText("Re-do Scale Exposure Times (x" + sf + ")");
	}
      } else {
        errorBox = new ErrorBox("Your selection: " + item.getTitle() +
                               " is not an observation");
      }
      enableRun();
    }
  }
 
  
  /**
   * private void rescaleAttributes()
   *
   * Reinvokes the attribute scaler on the current item, as long as that
   * item is an observation.
   **/
  private void rescaleAttributes() {
    SpItem item=findItem(_spItem,path.getLastPathComponent().toString());
    if (item == null) {
      return;
    }

    // Recheck that this is an observation
    if (item.type()==SpType.OBSERVATION) {

      disableRun();

      SpObs observation = (SpObs) item;
      if (!observation.equals(null)) {
	new AttributeEditor(observation, new javax.swing.JFrame(), true,
			    "EXPTIME",
			    haveScaled.contains(observation),
			    lastScaleFactor(),
			    true).show();
	double sf = AttributeEditor.scaleFactorUsed();
	if (sf > 0) {
	  haveScaled.addElement(observation);
	  scaleFactors.addElement(new Double(sf));
	}
      } else {
        errorBox = new ErrorBox("Your selection: " + item.getTitle() +
                               " is not an observation");
      }
      enableRun();
    }
  }


  private Double lastScaleFactor() {
    if (scaleFactors.size() == 0) {
      return new Double(AttributeEditor.scaleFactorUsed());
    } else {
      return (Double)scaleFactors.elementAt(scaleFactors.size()-1);
    }
  }
  
  /**  private void execution() is a private method
       to start a sequence console. The execution is mainly about to start a
       SGML translation, then a "remote" frame to form a sequence console.
       
       @param none
       @return  none
       @throws none
       
  */
  private void execution()
    {
      
      SpItem item=findItem(_spItem,path.getLastPathComponent().toString());

      if (item == null) {
        
 	errorBox =new ErrorBox("You have not selected an observation!"+
 			       "\nPlease select an observation.");
 	return;
      }

      if(!item.typeStr().equals("ob"))
	{
	  
          errorBox =new ErrorBox("Your selection: "+item.getTitle()+
				 " is not an observation"+
				 "\nPlease select an observation.");
          return;
	  
	} else
	  {
	    run.setEnabled(false);
	    run.setForeground(Color.white);
	    
	    SpItem observation=item;
	    
	    if(!observation.equals(null))
	      {
		
		SpItem inst= (SpItem) SpTreeMan.findInstrument(item);
		
		translating tFlush =new translating();
		tFlush.start();
		
		String tname=trans(observation);
		
		tFlush.getFrame().dispose();
		tFlush.stop();

		// Catch null sequence names - probably means translation
		// failed:
		if (tname == null) {
		  errorBox = new ErrorBox
		    ("Translation failed. Please report this!");
		  run.setEnabled(true);
		  run.setForeground(Color.black);		    
		  return;
		}else{
		  System.out.println ("Trans OK");
		}

		// Prevent IRCAM3 and CGS4 from running together
		
		//figure out if the same inst. is already in use or
		//whether IRCAM3 and CGS4 would be running together
		sequenceFrame f;
		for(int i=0;i<consoleFrames.getList().size();i++) {
		  f = (sequenceFrame) 
		    consoleFrames.getList().elementAt(i);
		    
		  if(inst.type().getReadable().equals(f.getInstrument())) {
		    f.resetObs(observation.getTitle(),tname);
		    run.setEnabled(true);
		    run.setForeground(Color.black);
		    return;
		  }

		  if(inst.type().getReadable().equals("IRCAM3") && f.getInstrument().equals("CGS4")) {
                    new AlertBox ("IRCAM3 and CGS4 cannot run at the same time.");
		    run.setEnabled(true);
		    run.setForeground(Color.black);		    
		    return;
		  }
		  
		  if(inst.type().getReadable().equals("CGS4") && f.getInstrument().equals("IRCAM3")) {
                    new AlertBox ("CGS4 and IRCAM3 cannot run at the same time.");
		    run.setEnabled(true);
		    run.setForeground(Color.black);		    
		    return;
		  }
		}
		creatNewRemoteFrame(observation,inst);
	      }
	  }
    }
  

  /**  
       private void creatNewRemoteFrame(SpItem observation,SpItem inst) 
       is a private method to start a NEW sequence console. This is mainly
       about to start a "remote" frame to form a sequence console and 
       rebind it into a RMI registry.
       it also starts a set of drama task which is platform-dependent.
       
       @param SpItem observation,SpItem inst
       @return  none
       @throws RemoteException,MalformedURLException
       
  */
  
  private void creatNewRemoteFrame(SpItem observation,SpItem inst)
  {
    try {
      frame = new remoteFrame(inst.type().getReadable(),
			      observation.getTitle(),consoleFrames);
	  
      // Create a security manager (should replace by a policy file)
      //	System.setSecurityManager(new RMISecurityManager());

      String instStr = inst.type().getReadable();
      Naming.rebind(System.getProperty(instStr+"_OBJE"),frame);
      Naming.rebind(System.getProperty(instStr+"_OBJE")+"-COMM",
		    frame.getCommandSent());
      System.out.println("RMI Server for the "+instStr+" console is ready.");
	  
      if (System.getProperty("RMIS_MESS").equals("ON")) 
	frame.setLog(System.err);

    } catch (RemoteException re) {
      System.out.println ("Exception in programTree:"+re);
    } catch (MalformedURLException e) {
      System.out.println("MalformedURLException in programTree:" + e);
    }  catch (NullPointerException e) {
      System.out.println("NullPointerException in programTree:" + e);
    }
      
      
    //add inst into the instrument list on the OM frame
    if (menu.getActiveInstrumentList().getItemCount()>0) {
      String activeInst = 
	menu.getActiveInstrumentList().getItemAt(0).toString();
      if (activeInst.substring(0,4).equalsIgnoreCase("None") ||
	  activeInst.substring(0,4).equals("    ")) {
	menu.getActiveInstrumentList().removeAllItems();
      }
    }

    try {
	
      menu.getActiveInstrumentList().addItem(inst.type().getReadable());
      consoleFrames.addFrameList (frame.getFrame());
      loadDramaTasks (inst.type().getReadable());

      //connect it to the TCS if it is the first instrument
      if (consoleFrames.getList().size()==1) {
	sequenceFrame sf = (sequenceFrame) consoleFrames.getList().elementAt(0);
	sf.connectTCS();
      }
      if (inst.type().getReadable().equalsIgnoreCase("UFTI")) {
	new AlertBox ("Ask your TSS to datum filter wheels and shutter. Then open shutter");
      }
      run.setEnabled(true);
      run.setForeground(Color.black);
	
    } catch (NullPointerException e) {
      System.out.println("NullPointerException in programTree 2:" + e);
    }
    
  }
  
  
  /**
     String trans (SpItem observation) is a private method
     to translate an observation java object into an exec string
     and write it into a ascii file where is located in "EXEC_PATH"
     directory and has a name stored in "execFilename"
     
     @param SpItem observation
     @return  String a filename
     @throws RemoteException,MalformedURLException
     
  */
  
  private String trans (SpItem observation)
    {
      SpTranslator translation=new SpTranslator((SpObs)observation);
      translation.setSequenceDirectory(System.getProperty("EXEC_PATH"));
      translation.setConfigDirectory(System.getProperty("CONF_PATH"));
      
      Properties temp=System.getProperties();
      String tname = null;
      try {
	tname=translation.translate();
	temp.put(new String("execFilename"),tname);

      }catch (Exception e) {
	System.out.println ("Translation failed!, exception was "+e);
      }
      
      return tname;
    }
  
  
  /**
     public void addTree(String title,SpItem sp) is a public method
     to set up a JTree GUI bit for a science program object in the panel
     and to set up a listener too
     
     @param String title and SpItem sp
     @return  none
     @throws none
     
  */
  public void addTree(String title,SpItem sp)
    {
      _spItem=sp;
      
      // Create data for the tree
      root= new DefaultMutableTreeNode(sp);
      
      getItems(sp,root);
      
      // Create a new tree control
      DefaultTreeModel treeModel = new DefaultTreeModel(root);
      tree = new JTree( treeModel);
      
      // Tell the tree it is being rendered by our application
      tree.setCellRenderer( new treeCellRenderer() );
      tree.addTreeSelectionListener(this);
      MouseListener popupListener = new PopupListener();
      tree.addMouseListener (popupListener);

      // Add the listbox to a scrolling pane
      scrollPane.getViewport().removeAll();
      scrollPane.getViewport().add(tree);
      add(scrollPane);
      
      this.repaint();
      this.validate();
    }
  
   /**
      public void removeTree( ) is a public method
      to remove a JTree GUI bit for a science program object in the panel
      and to set up a listener too
      
      @param none
      @return  none
      @throws none
      
   */
  public void removeTree()
    {
      this.remove(scrollPane);
    }
  

  /**
     public void valueChanged( TreeSelectionEvent event) is a public method
     to handle tree item selections
     
     @param TreeSelectionEvent event
     @return  none
     @throws none
     
  */
  public void valueChanged(TreeSelectionEvent event)
    {
      if( event.getSource() == tree )
	{
	  // Display the full selection path
	  path = tree.getSelectionPath();

	  // The next section is with a view to possible
	  // exposure time changes. Don't use until we know want we want
	  // for sure.
// 	  if(path.getLastPathComponent().toString().length()>14) {
// 	    if(path.getLastPathComponent().toString().substring(0,14).equals("ot_ukirt.inst.")) {
// 	      new newExposureTime(_spItem);
// 	    }
// 	  }
	}
    }
  
  /** public void getItems (SpItem spItem,DefaultMutableTreeNode node)
      is a public method to add ALL the items of a sp object into the
      JTree *recursively*.
      
      @param SpItem spItem,DefaultMutableTreeNode node
      @return  none
      @throws none
      
  */
  private void getItems (SpItem spItem,DefaultMutableTreeNode node)
    {
      Enumeration children = spItem.children();
      while (children.hasMoreElements())
	{
	  SpItem  child = (SpItem) children.nextElement();
	  
	  DefaultMutableTreeNode temp
	    = new DefaultMutableTreeNode(child);
	  
	  node.add(temp);
	  getItems(child,temp);
	}
    }
  
  
  /** 
      public void getItems (SpItem spItem,DefaultMutableTreeNode node)
      is a public method to get an item in a sp.
      
      @param SpItem spItem, String name
      @return  SpItem
      @throws none
      
  */
  private SpItem findItem (SpItem spItem, String name)
    {
      int index = 0;
      Enumeration children = spItem.children();
      SpItem tmpItem = null;
      while (children.hasMoreElements())
	{
	  SpItem  child = (SpItem) children.nextElement();

	  
	  if(child.toString().equals(name))
	    return child;
	  
	  tmpItem = findItem(child,name);
	  
	  if(tmpItem != null)
	    return tmpItem;
	}
      
      return null;
    }
  
  /**
     Load a set of drama tasks.
     Please note it is platform-dependent.
     
     @param String name;
     @return none
     @throws none
     
  */
  private void loadDramaTasks(String name)
    {
      
      //starting the drama tasks
      String[] oos=new String[4];
      String[] moni=new String[4];
      String[] socket=new String[4];
      String[] script=new String[5];
      
      script[0]=System.getProperty("LOAD_DHSC");
      script[1]=new String(name);
      script[2]="-"+System.getProperty("QUICKLOOK", "noql");
      script[3]="-"+System.getProperty("SIMULATE","sim");
      script[4]="-"+System.getProperty("ENGINEERING","eng");
      System.out.println ("About to start script"+script[0]+" "+script[1]+" "+script[2]+" "+script[3]+" "+script[4]);
      ExecDtask task4=new ExecDtask(script);
      task4.setWaitFor(true);
      // Set the output depending on whether it is requested in the cfg file.
      boolean debug = System.getProperty("SCR_MESS").equalsIgnoreCase("ON");
      task4.setOutput(debug);
      task4.run();
      // Check for errors from the script
      int err = task4.getExitStatus();
      if (err != 0) {
      	errorBox = new ErrorBox
	  ("Error reported by instrument startup script, code was: "+err);
      }
      task4.stop();
      try {
	task4.join();
      }catch (InterruptedException e) {
	System.out.println ("Load task join interrupted! "+e.getMessage());
      }

      moni[0]=System.getProperty("MONI_CODE");
      moni[1]=new String("MONITOR_"+name);
      moni[2]=new String();
      moni[3]=new String();
      
      ExecDtask task2=new ExecDtask(moni);
      try {
        task2.sleep(6000);
      }catch (InterruptedException e) {
	System.out.println(e);
      }
      task2.setWaitFor(false);
      task2.setOutput(false);
      task2.run();
      frame.getDramaTasks().addElement(task2.getProcess());
      
      socket[0]=System.getProperty("SOCK_CODE");
      socket[1]=System.getProperty("SOCK_HOST");
      socket[2]=System.getProperty("SOCK_PORT");
      socket[3]=new String("SOCK_"+name);
      
      ExecDtask task3=new ExecDtask(socket);
      try {
        task3.sleep(6000);
      } catch (InterruptedException e) {
	System.out.println(e);
      }
      task3.setWaitFor(false);
      task3.setOutput(false);
      task3.run();
      frame.getDramaTasks().addElement(task3.getProcess());
      
      // wait for socket connection to be there
      while (frame.getDramaSocket().getInputThread()==null) {}

    }
  
  
  public void frameListAdded (frameListEvent f)
    {
    }

  /**
     Services requests to change the instrument that is connected to the 
     telescope.
     @param String name;
     @return none
     @throws none
     
  */
  public void connectedInstrumentChanged(frameListEvent f)
    {
      Object l=f.getSource();
      if(l==consoleFrames) {
	String connectedInstrument = 
	  new String(consoleFrames.getConnectedInstrument());

	if (!connectedInstrument.equals("NONE")) {
	  for (int i=0; i<menu.getActiveInstrumentList().getItemCount();i++) {
	    if (menu.getActiveInstrumentList().getItemAt(i).toString().equals
		(connectedInstrument)) {
	      menu.getActiveInstrumentList().setSelectedItem
		(menu.getActiveInstrumentList().getItemAt(i));
	      break;
	    }
	  }
	} else {
	  sequenceFrame temp;
	    
	  for(int i=0; i<consoleFrames.getList().size();i++) {
	    temp = (sequenceFrame) consoleFrames.getList().elementAt(i); 
	    if (temp.getInstrument().equals
		(menu.getActiveInstrumentList().getSelectedItem().toString())) {

	      temp.connectTCS();
	      break;
	    } 
	  }
	}
      }
    }
  

  public void frameListRemoved (frameListEvent f)
    {
      Object l=f.getSource();
      if (l==consoleFrames) {
	  
	menu.getActiveInstrumentList().removeAllItems();
	  
	for (int i=0; i<consoleFrames.getList().size();i++) {
	  sequenceFrame temp = 
	    (sequenceFrame) consoleFrames.getList().elementAt(i);  
	  menu.getActiveInstrumentList().addItem(temp.getInstrument());
	}
      }
    }
  
  class PopupListener extends MouseAdapter {

    public void mousePressed (MouseEvent e) {

      // If this was not the right button just return immediately.
      if (!((e.getModifiers() & InputEvent.BUTTON3_MASK)
            == InputEvent.BUTTON3_MASK)) {
        return;
      }

      SpItem item = null;
      try {
        item = findItem(_spItem,path.getLastPathComponent().toString());
      } catch (NullPointerException excep) {}

      // Catch any cases where somehow there isn't an item selected
      if (item == null) {
        return;
      }

      // If this is an observation then show the popup
      if (item.type()==SpType.OBSERVATION) {
          maybeShowPopup(e);
      }
  
    }

    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        popup.show (e.getComponent(), e.getX(), e.getY());
      }
    }

  }

  private boolean confirm(String message) {
    JFrame frame = new JFrame();
    JOptionPane dialog = new JOptionPane();

    return (dialog.showConfirmDialog(frame, message, "Confirm",
				     JOptionPane.YES_NO_OPTION,
				     JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION);
  }

  public FrameList getFrameList() {return consoleFrames;}
  public JButton getRunButton () {return run;}
  
  private JButton run;
  private JTree tree;
  private JScrollPane scrollPane= new JScrollPane();;
  private SpItem _spItem;
  private DefaultMutableTreeNode root;
  private TreePath path;
  private ErrorBox errorBox;
  private AlertBox alertBox;
  private FrameList consoleFrames=new FrameList();
  private remoteFrame frame;
  private menuSele menu;
  private JMenuItem edit;
  private JMenuItem scale;
  private JMenuItem scaleAgain;
  private Vector haveScaled   = new Vector(); 
  private Vector scaleFactors = new Vector(); 
  private JPopupMenu popup;
}

