package om.console;                              

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import java.io.File;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import om.util.OracColor;

/** itemsShown class is about
    how showing an items on both cmmandPanel and meunBar objects.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. But there is only one type of host anyway.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/

final public class itemsShown extends JComponent implements java.io.Serializable
{
/** itemsShown(sendCmds cs) is
    the class constructor. The class has only one constructor so far.

    @param sendCmds cs
    @return  none
    @throws none
*/
  public itemsShown(sendCmds cs)
  {
    cSent=cs;
  }

/**  public void Init() is  a public method to
     decide that the LOOK of the OM when the OOS is initialised.

    @param none
    @return  none
    @throws none
*/
  public void Init()
  {
     commP.getButton(0).setEnabled(true);
     commP.getButton(0).setBackground(OracColor.green);

     commP.getButton(1).setEnabled(true);
     commP.getButton(1).setBackground(OracColor.green);
     commP.getButton(6).setEnabled(true);

     mBar.getMenuItem(0).setEnabled(true);
     mBar.getMenuItem(1).setEnabled(true);
     mBar.getMenuItem(6).setEnabled(true);
  }

  /** public void Idle() is  a public method to
     decide that the LOOK of the OM when the OOS is idle.

    @param none
    @return  none
    @throws none
  */
  public void Idle()
  {
     commP.getButton(4).setText(" ");
     commP.getButton(4).setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"pause.gif"));
     mBar.getMenuItem(4).setText(mBar.getTitle(4));

     for(int i=0; i<7;i++)
     {
        commP.getButton(i).setBackground(Color.getHSBColor(204,204,204));
        commP.getButton(i).setEnabled(false);
        mBar.getMenuItem(i).setEnabled(false);
     }

  }

   /** public void Load() is  a public method to
     load a exec sequence.

    @param none
    @return  none
    @throws none
  */
  public void Load ()
  {
    getFile();
  }

  /** public void Exit() is a public method to
     exit the sequence console.

    @param none
    @return  none
    @throws none
  */
  public void Exit ()
  {
    JOptionPane dialog = new JOptionPane();
			  int selection=dialog.showConfirmDialog(this, "Exit this sequence console?",
					"Exit Comfirmation", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null );

        if(selection==dialog.YES_OPTION)
        {
          cSent.setExit();
        }

        if(selection==dialog.NO_OPTION)
        {
        }
  }

   /** public void BreakPoint() is a public method to
     setup the OM looking when a breakpoint is active.

    @param none
    @return none
    @throws none
  */
  public void BreakPoint ()
  {
    mBar.getMenuItem(2).setText("Delete Break");
    commP.getButton(2).setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"undo.gif"));
    commP.getButton(2).setBackground(Color.getHSBColor(204,204,204));
    commP.getButton(2).setText("");  //this is an indicator of the button status
  }

  /** public void CancelBreak() is a public method to
     setup the OM looking when a breakpoint is canceled.

    @param none
    @return none
    @throws none
  */
  public void CancelBreak()
  {
    commP.getButton(2).setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"stopAtBreak.gif"));
    commP.getButton(2).setBackground(Color.getHSBColor(204,204,204));
    commP.getButton(2).setText(" "); //this is an indicator of the button status
    mBar.getMenuItem(2).setText(mBar.getTitle(2));
  }

   /** public void Run() is a public method to
     setup the OM looking when the OOS is run.

    @param none
    @return none
    @throws none
  */

  public void Run ()
  {
    commP.getButton(0).setEnabled(false);
    commP.getButton(0).setBackground(Color.getHSBColor(204,204,204));
    commP.getButton(1).setEnabled(false);
    commP.getButton(1).setBackground(Color.getHSBColor(204,204,204));
    commP.getButton(6).setEnabled(false);

    if(mBar.getMenuItem(4).getText().equals("Continue"))   //this is in "continue" status
      Resume();

    mBar.getMenuItem(0).setEnabled(false);
    mBar.getMenuItem(1).setEnabled(false);
    mBar.getMenuItem(6).setEnabled(false);

    //enable all the middle buttons
    for(int i=2; i<6;i++)
    {
      commP.getButton(i).setEnabled(true);
      mBar.getMenuItem(i).setEnabled(true);
    }

    //disable the items in "File" menu of the menuBar
    for(int i=0; i<4;i++)
      mBar.getMenuFile(i).setEnabled(false);

  }

 /** public void Pause () is a public method to
     setup the OM looking when the OOS is paused.

    @param none
    @return none
    @throws none
  */
  public void Pause ()
  {

    commP.getButton(4).setText("");  //this turns into continue status
    commP.getButton(4).setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"continue.gif"));
    commP.getButton(4).setBackground(Color.yellow);
    mBar.getMenuItem(4).setText("Continue");

    //disbale the items in File menu of the menuBar
    for(int i=0; i<4;i++)
      mBar.getMenuFile(i).setEnabled(false);

    for(int i=2;i<7;i++)
    {
      commP.getButton(i).setEnabled(true);
      mBar.getMenuItem(i).setEnabled(true);
    }
  }

 /** public void Resume() is a public method to
     setup the OM looking when the OOS is resumed.

    @param none
    @return none
    @throws none
  */
  public void Resume ()
  {
    commP.getButton(4).setText(" ");
    commP.getButton(4).setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"pause.gif"));
    commP.getButton(4).setBackground(Color.getHSBColor(204,204,204));
    mBar.getMenuItem(4).setText(mBar.getTitle(4));
  }

  /** public void  About () is a public method to
     creat a "ABOUT" panel.

    @param none
    @return none
    @throws none
  */
  public void About ()
  {
    JOptionPane dialog = new JOptionPane();

    dialog.showConfirmDialog(this, " This is a ORAC sequence console, v1.0     \nPlease send comments to mt@roe.ac.uk\n   ",
					"About", JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,null);
  }

   /** public void Stopping() is a public method to
     setup the OM looking when the OOS is stopping

    @param none
    @return none
    @throws none
  */
  public void Stopping ()
  {
    commP.getButton(3).setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"stopping.gif"));
  }

  /** public void Stop() is a public method to
     setup the OM looking when the OOS has stopped.

    @param none
    @return none
    @throws none
  */
  public void Stop ()
  {
    commP.getButton(0).setEnabled(true);
    commP.getButton(0).setBackground(OracColor.green);
    commP.getButton(1).setEnabled(true);
    commP.getButton(1).setBackground(OracColor.green);
    commP.getButton(6).setEnabled(true);

    commP.getButton(4).setBackground(Color.getHSBColor(204,204,204));
    commP.getButton(2).setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"stopAtBreak.gif"));
    commP.getButton(4).setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"pause.gif"));
    mBar.getMenuItem(4).setText(mBar.getTitle(4));
    commP.getButton(3).setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"stopasap.gif"));
    mBar.getMenuItem(0).setEnabled(true);
    mBar.getMenuItem(1).setEnabled(true);
    mBar.getMenuItem(6).setEnabled(true);

    for(int i=2; i<5;i++)
    {
      commP.getButton(i).setEnabled(false);
      mBar.getMenuItem(i).setEnabled(false);
    }

    //enable the items in the menu File of the menuBar
    for(int i=0; i<4;i++)
      mBar.getMenuFile(i).setEnabled(true);

    //seqP.getMyList().setSelectedIndex(seqP.getListData().size());
  }

 /** public void EngExec() is a public method to
     setup a mode for engineering or non engineering
     exec sequence mode.

    @param boolean b
    @return none
    @throws none
  */
  public void EngExec(boolean b)
  {
     seqP.setMode(b);
  }

 /** private getFileName  is a private method to
     stripe off the filename extension "exec".

    @param String
    @return none
    @throws String
  */
  private String getFileName (String file)
  {
    int i=0;
    int j;

    if(file.length()>5)
    for(i=0;i<file.length()-5;i++)
    if(file.substring(i,i+5).equals(".exec"))
    break;

    for(j=i;j>0;j--)
    if(file.substring(j-1,j).equals("/"))
    break;

    return file.substring(j);
 }

 /** private getFile  is a private method to
     get a file from fileChooser panel.

    @param none
    @return none
    @throws none
  */
  private void getFile ()
  {
     // Create an instance of the file chooser dialog
     Properties temp=System.getProperties();

     String exec_path=temp.getProperty("EXEC_PATH");

     if(System.getProperty("DBUG_MESS").equals("ON"))
        System.out.println("EXEC_PATH:"+exec_path);

  		fileChooser= new myFileChooser(exec_path);

  		int returnValue = fileChooser.showOpenDialog(null);
			if( returnValue == 0 )  // User selected OK
			{
				// Figure out what file the user selected
        cSent.setLoad(getFileName(fileChooser.getSelectedFile().toString()));
			}
			else
			{
        if(System.getProperty("DBUG_MESS").equals("ON"))
				  System.out.println("User cancelled operation" );
			}

      fileChooser=null;
      System.gc();
  }

  /**public void linkPanels is a public method to
     set up links with other panels/objects.

    @param commandPanel c, sequencePanel s, menuBar m
    @return none
    @throws none
  */
  public void linkPanels(commandPanel c, sequencePanel s, menuBar m, upperPanel u,movie mov)
  {
    mBar=m; seqP=s; commP=c; upp=u; _movie=mov;
  }

  /**public void Movie is a public method to
     set up item display when the movie is on or off.

    @param boolean
    @return none
    @throws none
  */
  public void Movie(boolean movieon)
  {
    if (movieon) {
    // Movie is on, disable sequence control commands
      for (int i=0;i<7;i++) {
        commP.getButton(i).setEnabled(false);
        mBar.getMenuItem(i).setEnabled(false);
      }

    } else {
      // movie is off. Enable the commands that are allowed in the current
      // OOS state.

      if (upp.getStatus().getText().equals("Stopped")) {
	//back to stopped state
	Stop();
      } else {
	//back to paused state
        mBar.getMenuItem(0).setEnabled(false);
        mBar.getMenuItem(1).setEnabled(false);
        mBar.getMenuItem(6).setEnabled(false);
	
        commP.getButton(0).setEnabled(false);
        commP.getButton(1).setEnabled(false);
        commP.getButton(6).setEnabled(false);
	
        for(int i=2; i<6;i++) {
          commP.getButton(i).setEnabled(true);
          mBar.getMenuItem(i).setEnabled(true);
        }
      }
    }
  }

  private movie _movie;
  private FileInputStream fin;
  private commandPanel commP;
  private sequencePanel seqP;
  private menuBar mBar;
  private sendCmds cSent;
  private upperPanel upp;

  private myFileChooser fileChooser;
}



