package om.console;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.border.*;
import javax.swing.event.*;

/** commandPanel class is a major bit of UI to let an observer
    sending commands out during an observation
    Please note this is serialized for RMI calls

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class commandPanel extends JPanel implements ActionListener,java.io.Serializable
{
/** public commandPanel(sendCmds cs) is
    the class constructor. The class has only one constructor so far.
    the two things are done during the construction.
    1) adding GUI butons in the panel
    2) adding listeners

    @param sendCmds cs
    @return  none
    @throws none
*/
  public commandPanel(sendCmds cs)
  {
      comSent=cs;

      Border border=BorderFactory.createMatteBorder(2, 2,
            2,2, Color.white); //a new border required by gsw on April 07, 2000

      //Border border=BorderFactory.createMatteBorder(12, 12,
           // 12,12, Color.lightGray);

      setBorder(new TitledBorder(border,
      "Control",0,0,
      new Font("Roman",Font.BOLD,14),Color.black));

      setLayout(new GridLayout(6,1,26,26)); 

      for(int i=0;i<7;i++) //there are seven buttons on this panel
      {

          button[i]=new myButton();

          if(i==1)
            button[i].setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"run.gif"));
          else if (i==2){
              button[i].setText(" "); //this is the stop at break status  
              button[i].setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"stopAtBreak.gif"));
          }
          else if (i==3)
              button[i].setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"stopasap.gif"));
          else if (i==4)
          {   button[i].setText(" "); //this is the Pause status
              button[i].setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"pause.gif"));
          }
          else if (i==5)
              button[i].setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"abort.gif"));
          else if (i==6)
              button[i].setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"ql.gif"));


        button[i].setBackground(Color.getHSBColor(204,204,204));

        button[i].addActionListener(this);

        // if(i>0) //remove the run button from the panel
        //  add(button[i]);
      }

      add(button[1]);
      add(button[2]);
      add(button[4]);
      add(button[3]);
      add(button[5]);
      add(button[6]);
   }

  /**  public void actionPerformed(ActionEvent evt) is a public method
    to react button push actions i.e. sending commands out

    @param ActionEvent
    @return  none
    @throws none

  */
  public void actionPerformed (ActionEvent evt)
  {
     Object source = evt.getSource();

     if (source == button[0])   //sending out a run command
     {
        comSent.setRun();
     }

     else if (source == button[1])  //sending out a run special command
     {
        comSent.setRunSpecial();
     }

     else if(source==button[3]) //sending out a stop command
     {
       comSent.setStop();
       button[3].setIcon(new ImageIcon(System.getProperty("IMAG_PATH")+"stopping.gif"));
     }


     else if(source==button[4]) //sending out a pause/continue commend
     {
        if(button[4].getText().equals(" "))
        {
          comSent.setPause();
        }  else
        {
          comSent.setResume();
        }
     }

     else if(source==button[2])  //sending out a break/cancel command
     {
       if(button[2].getText().equals(" "))
       {
          comSent.setBreakPoint();
       }  else
       {
          comSent.setCancelBreak();
       }
     }

     else if(source==button[5])  //sending out an abort command
       comSent.setAbort();

     else if(source == button[6]) //sending out a Movie command
       comSent.setMovie();
     else
     ;

  }

  /**  public myButton getButton(int i) is a public method
    to return a button object

    @param int i
    @return  myButton
    @throws none

  */
  public myButton getButton(int i) {return button[i];}

  private sendCmds comSent;
  private myButton button[]=new myButton[7];
 
}


