package om.console;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/** menuBar class is about
    providing a menuBar with a number of items.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent.  But there is only one type of host anyway.
    It is this one casuing probelms in repeated RMI calls.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class menuBar extends JMenuBar
  implements ActionListener, java.io.Serializable
{
/** public menuBar(sendCmds cs, itemsShown iS) is
    the class constructor. The class has only one constructor so far.
    the two things are done during the construction.
    1) adding GUI butons in the menuBar
    2) adding listeners

    @param none
    @return  none
    @throws none
*/
    public menuBar(sendCmds cs, itemsShown iS)
    {

      commSent=cs; iShown=iS;

      Border border=BorderFactory.createMatteBorder(8, 8,
           8,8, Color.lightGray);

      setBorder(new TitledBorder(border,
      "",0,0,
      new Font("Roman",Font.BOLD,14),Color.black));


      menuFile=new myMenu("File");
      this.add(menuFile);

      fileItem[0]=new myMenuItem("Init");
      fileItem[1]=new myMenuItem("Load");
      fileItem[2]=new myMenuItem("Clear");
      fileItem[3]=new myMenuItem("Exit");

      for(int i=0;i<4;i++)
        menuFile.add(fileItem[i]);

      menuCommands=new myMenu("Commands");
      add(menuCommands);

      menuOptions=new myMenu("Options");
      add(menuOptions);


      hideExec=new myCheckBoxMenuItem("Hide eng.exec");
      menuOptions.add(hideExec);

      officerOnly=new myCheckBoxMenuItem("TO's control only");
      menuOptions.add(officerOnly);


      for(int i=0; i<7; i++)
      {
        menuComm[i]=new myMenuItem(title[i]);
        menuCommands.add(menuComm[i]);
        menuComm[i].setEnabled(false);
      }

      menuHelp=new myMenu("Help");
      add(menuHelp);

      menuAbout=new myMenuItem("About");
      menuContents=new myMenuItem("Contents");

      menuHelp.add(menuAbout);
      menuHelp.add(menuContents);

      for(int i=0; i<7; i++)
      menuComm[i].addActionListener(this);

      for(int i=0; i<4; i++)
        fileItem[i].addActionListener(this);

      menuAbout.addActionListener(this);

      menuContents.addActionListener(this);
      hideExec.addActionListener(this);
      officerOnly.addActionListener(this);

      hideExec.setState(true);
      officerOnly.setEnabled(false);
   }

  /**  public void actionPerformed(ActionEvent evt) is a public method
    to react menu actions i.e. sending commands out

    @param ActionEvent
    @return  none
    @throws none
  */
  public void actionPerformed (ActionEvent evt)
  {
      Object source = evt.getSource();

      if (source == fileItem[3])
        iShown.Exit();
      else if (source ==fileItem[1])
        iShown.Load();
      else if (source == menuAbout)
        iShown.About();
      else if (source == menuContents)
        commSent.Content();
      else if (source == fileItem[0])
        commSent.setInit();
      else if(source==hideExec)
      {
        iShown.EngExec(hideExec.getState());
      }
      else if(source==officerOnly)
      {
         if(officerOnly.getState())
           commSent.setControl(1);
         else
           commSent.setControl(0);
      }
      else if (source == fileItem[2])
        commSent.setClear();
      else if (source == menuComm[0])
      {
        commSent.setRun();
      }
      else if (source == menuComm[3])
      {
        commSent.setStop();
      }
      else if (source == menuComm[4])
      {
        if(menuComm[3].getText().equals("Pause      "))
        {
          commSent.setPause();
        }  else
        {
          commSent.setResume();
        }
      }
      else if (source == menuComm[2])
      {
        if(menuComm[2].getText().equals("Stop At Break"))
        {
          commSent.setBreakPoint();
        }  else
        {
          commSent.setCancelBreak();
        }
      }
      else if (source == menuComm[5])
        commSent.setAbort();
      else if (source == menuComm[6])
        commSent.setMovie();
  }


  /**  public myMenuItem getMenuItem (int i) is a public method
    to return a object of myMenuItem

    @param int i
    @return myMenuItem
    @throws none

  */
    public myMenuItem getMenuItem (int i) {return menuComm[i];}
    public myMenuItem getMenuFile (int i) {return fileItem[i];}
    public myCheckBoxMenuItem getTOonly(){return officerOnly;};
    public myCheckBoxMenuItem getHideExec(){return hideExec;};

 /**  public String getTitle(int i) is a public method
    to return a string object of a button title

    @param int i
    @return  String
    @throws none

  */
  public String getTitle(int i) {return title[i];}


  /**  public void setModel (Vector m)
    to recover the object status.

    @param Vector
    @return  none
    @throws none
  */
  public void setModel (Vector m)
  {
    for(int i=0;i<7;i++)
    {
      if(m.elementAt(i).toString().equals("true"))
        menuComm[i].setEnabled(true);
      else
        menuComm[i].setEnabled(false);
    }

    if(m.elementAt(7).toString().equals("true"))
      hideExec.setSelected(true);
    else
      hideExec.setSelected(false);

  }


  /**  public Vector getModel ()
    to get the object status.

    @param none
    @return  Vector
    @throws none
  */
  public Vector getModel ()
  {
    for(int i=0; i<7; i++)
    {
      if(menuComm[i].isEnabled())
        model.addElement("true");
      else
        model.addElement("fasle");
    }

    if(hideExec.isSelected())
      model.addElement("true");
    else
      model.addElement("false");

    return model;
  }

   private sendCmds commSent;
   private itemsShown iShown;
   private myMenu menuFile, menuHelp, menuCommands,menuOptions;
   private myMenuItem menuAbout, menuContents;
   private myCheckBoxMenuItem hideExec;
   private myCheckBoxMenuItem officerOnly;
   private myMenuItem fileItem[]=new myMenuItem[5];
   private myMenuItem menuComm[]=new myMenuItem[7];
   private String title[]={"Run","Run From Highlight ","Stop At Break","Stop asap","Pause      ","Abort","Movie"};
   private Vector model=new Vector();
}
