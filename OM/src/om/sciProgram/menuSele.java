package om.sciProgram;

import om.console.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import orac.helptool.JHLauncher;
import java.rmi.registry.*;
import java.rmi.Remote.*;
import om.util.OracColor;

/** 
    final class  menuSele is the menu in the sleector frame
    which has two items. one is "exit" and the other is "help"
    
    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/

final class menuSele extends JMenuBar implements ActionListener
{
  /** 
      public menuSele(spConnector sp,selectorFrame frame) is
      the class constructor. The class has only one constructor so far.
      a few thins are done during the construction. They are mainly about
      putting GUI bits into the menu and setting up listeners
      
      @param spConnector, selectorFrame
      @return  none
      @throws none
      
  */  
  public menuSele(spConnector sp,selectorFrame frame)
    {
      f=frame;
      Border border=BorderFactory.createMatteBorder(10, 10,
						    10,10, Color.lightGray);
      setBorder(new TitledBorder(border,
				 "",0,0,
				 new Font("Roman",Font.BOLD,14),Color.black));
      
      //setBackground(Color.white);
      setLayout(new BorderLayout(30,20));
      
      menuExit=new JMenuItem("Exit");
      menuExit.setBorder(new BevelBorder(BevelBorder.RAISED));
      menuExit.setMargin(new Insets(10,0,10,0));
      menuExit.setBackground(OracColor.red);
      add(menuExit,"West");
      
      menuHelp=new JMenuItem("Help");
      menuHelp.setBorder(new BevelBorder(BevelBorder.RAISED));
      menuHelp.setMargin(new Insets(10,10,10,10));
      menuHelp.setBackground(Color.cyan);
      add(menuHelp,"East");

      JPanel tcsPanel = new JPanel();
      tcsPanel.setLayout(new BorderLayout());
      tcsPanel.add (new JLabel ("TCS Connection:  "),"West");
      tcsConnection=new JComboBox(instrument);
      tcsConnection.setEditable(false);
      
      tcsConnection.setEnabled(true);
      tcsConnection.addActionListener(this);
      tcsPanel.add(tcsConnection);
      add (tcsPanel);

      menuExit.addActionListener(this);
      menuHelp.addActionListener(this);
    }

  /**  
       public void actionPerformed(ActionEvent evt) is a public method
       to react button actions
       
       @param ActionEvent
       @return  none
       @throws none
       
  */
  public void actionPerformed (ActionEvent evt)
    {
      Object source = evt.getSource();
      
      if (source == menuExit) {
        Exit();

      } else if (source == menuHelp) {
        Help();

      } else if (source == tcsConnection) {
        if (tcsConnection.getItemCount()>1) {
	  if (!tcsConnection.getSelectedItem().toString().equals
	      (f.getTree().getFrameList().getConnectedInstrument())) {
	    
	    int selection;
	    
	    selection = JOptionPane.showConfirmDialog
	      (this, "Connect "+tcsConnection.getSelectedItem().toString()+
	       " with the telescope?", "TCS Connection", 
	       JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    
	    if (selection==JOptionPane.YES_OPTION) {
	      connectedInstrument = 
		f.getTree().getFrameList().getConnectedInstrument();
	      for(int i=0;i<f.getTree().getFrameList().getList().size();i++) {
                sequenceFrame temp = 
		  (sequenceFrame) f.getTree().getFrameList().getList().elementAt(i);
		
		//check the old console is not active
                if (connectedInstrument.equals(temp.getInstrument())) {
                  if (temp.getUpperPanel().getStatus().getText().substring(0,3).equalsIgnoreCase("Run")) {
                    tcsConnection.setSelectedItem(tcsConnection.getItemAt(i));
                    new AlertBox 
		      (temp.getInstrument()+
		       " is running, you are not able to change the telescope connection now.");
                    return;
                  }
                }
              }

	      //connecting the new instrument
	      for (int i=0;i<f.getTree().getFrameList().getList().size();i++) {
                sequenceFrame temp = 
		  (sequenceFrame) f.getTree().getFrameList().getList().elementAt(i);

                if ( tcsConnection.getSelectedItem().toString().equals(temp.getInstrument())) {
                  temp.connectTCS();
                  break;
                }
              }
              return;
	    } else if (selection==JOptionPane.NO_OPTION) {
	      for(int i=0; i<tcsConnection.getItemCount();i++) {
		connectedInstrument = 
		  f.getTree().getFrameList().getConnectedInstrument();
		if (tcsConnection.getItemAt(i).toString().equals
		    (connectedInstrument)) {
		  tcsConnection.setSelectedItem(tcsConnection.getItemAt(i));
		  break;
		}
		
	      }
	    }
	  }
        }
      }
    }

   /** private void Help() is a private method
    to launch a help frame with a help file

    @param ActionEvent
    @return  none
    @throws none
  */
   private void Help()
   {
    String[] args={"-helpset","om.hs"};

    JHLauncher l=new JHLauncher(args);

    System.out.println("JHL:"+l);
   }

  /** private void Exit() is a private method
    to exit the system

    @param ActionEvent
    @return  none
    @throws none
  */
  private void Exit ()
  {
    int selection;
    selection=JOptionPane.showConfirmDialog(menuExit, "Quit this Observing Manager? Click 'No' for login again",
    "Exit Comfirmation", JOptionPane.YES_NO_CANCEL_OPTION,
		JOptionPane.QUESTION_MESSAGE);

    if(selection==JOptionPane.YES_OPTION)
    {
      if(f.getTree().getFrameList().getList().size()>0)
      {
        new ErrorBox("Please exit all the sequence consoles first!");
        return;
      }

      System.exit(0);
    }
    else if(selection==JOptionPane.NO_OPTION) //an user is able to login again in a different user account
    {
       f.setVisible(false);
       loginFrame frame = new loginFrame(f);

       frame.show();
       frame.getLoginPanel().getUsername().requestFocus();
       frame.setResizable(false);
    }
  }

  public JComboBox getActiveInstrumentList () {return tcsConnection;}
  private selectorFrame f;
  private JMenuItem menuExit,menuHelp;
  private String instrument[]={"None"};
  private JComboBox tcsConnection;
  private JTextField tcsTitle;
  private String connectedInstrument;
  private boolean flag=true; //a work around to stop more that one copy of slection
}

