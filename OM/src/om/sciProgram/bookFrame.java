package om.sciProgram;
import java.io.*;
import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import om.console.*;

/** bookFrame class is to write the bookKeeping information about
    an observation. The information is currently put into the local
    directory and will be sent to the dhs system in a later version
    it has one constructor, one public mehtod and one private method

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/

final public class bookFrame extends JFrame  implements ActionListener, KeyListener
{

/** public bookFrame(String name, spConnector sp) is
    the class constructor. The class has only one constructor so far.
    A few thing are done during the construction.
    They are all about to put GUI bits into the frame

    @param   String and spConnector
    @return  none
    @throws none
*/
 public bookFrame(String name, spConnector sp)
 {
      user=name;
      username.setText(name);
      setTitle("Night log information");
      setSize(500, 360);
      setLocation(400,200);
      addWindowListener(new WindowAdapter()
         {  public void windowClosing(WindowEvent e)
            {
            }
         } );

      Border border=BorderFactory.createMatteBorder(12, 12,
            12,12, Color.lightGray);

      Container contentPane = getContentPane();

      JPanel topPanel=new JPanel(new BorderLayout());
      topPanel.setBorder(border);

      JPanel top1=new JPanel(new GridLayout(1,4,10,10));
      top1.setBorder(border);
      top1.add(new myLabel("Login name:"));
      top1.add(new myLabel(name));
      top1.add(new myLabel("UT Date:"));

      Calendar cal=Calendar.getInstance();
      cal.setTimeZone(TimeZone.getTimeZone("GMT"));
      cal.setTime(cal.getTime());

      String day = "00";
      String month = "00";
      if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
	day = "0" +String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
      }else{
	day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
      }
      if (cal.get(Calendar.MONTH) < 10) {
	month = "0" +String.valueOf(cal.get(Calendar.MONTH)+1);
      }else{
	month = String.valueOf(cal.get(Calendar.MONTH)+1);
      }

      top1.add(new myLabel(String.valueOf(cal.get(Calendar.YEAR))+
        month+day));


      JPanel top23=new JPanel(new GridLayout(2,1,10,10));
      top23.setBorder(new TitledBorder(border,
      "Enter the observer names and run number below",0,0,
      new Font("Roman",Font.BOLD,12),Color.black));

      JPanel top2=new JPanel(new FlowLayout());
      top2.add(new myLabel("Observers:"));
      username.setEditable(true);
      username.setBorder(new BevelBorder(BevelBorder.LOWERED));
      username.setColumns(14);
      top2.add(username);

      bookCombo=new JComboBox(title);
      bookCombo.setEditable(false);
      bookCombo.addActionListener(this);

      userPatt.setEditable(true);
      userPatt.setBorder(new BevelBorder(BevelBorder.LOWERED));
      userPatt.setColumns(14);

      JPanel top3=new JPanel(new FlowLayout());
      top3.add(bookCombo);
      top3.add(userPatt);

      top23.add(top2);
      top23.add(top3);
      topPanel.add(top1,"North");
      topPanel.add(top23,"South");

      contentPane.add(topPanel,"North");

      JPanel textPanel=new JPanel(new BorderLayout());
      textPanel.setBorder(new TitledBorder(border,
      "Enter any start of night comments, e.g. weather conditions",0,0,
      new Font("Roman",Font.BOLD,14),Color.black));

      text.setBorder(new BevelBorder(BevelBorder.LOWERED));
      text.setLineWrap(true);
      text.setFont(new Font("Arial",Font.PLAIN,14));

      textPanel.add(text);

      contentPane.add(textPanel);

      JPanel exitPanel=new JPanel(new GridLayout(1,3));
      exitPanel.setBorder(border);
      Ok=new JButton("Ok");
      Cancel=new JButton("Clear comments");
      Dismiss=new JButton("Dismiss");

      exitPanel.add(Ok);
      exitPanel.add(Cancel);
      //exitPanel.add(Dismiss);

      contentPane.add(exitPanel,"South");
      Cancel.addActionListener(this);
      Ok.addActionListener(this);
      Dismiss.addActionListener(this);

      // Set focus management
      username.setNextFocusableComponent(bookCombo);
      bookCombo.setNextFocusableComponent(userPatt);
      userPatt.setNextFocusableComponent(Ok);
      Ok.setNextFocusableComponent(Cancel);
      Cancel.setNextFocusableComponent(username);

      if (!isVisible()) {
      show();

      userPatt.requestFocus();
      text.addKeyListener(this);
      userPatt.addKeyListener(this);
      username.addKeyListener(this);
   }
  }  /**
  public void actionPerformed(ActionEvent evt) is a public method
  it is called by the action events from GUIs.

  @param ActionEvent evt
  @return none
  @throws none
  */
  public void actionPerformed(ActionEvent evt)
  {
     Object source=evt.getSource();

     if(source==Cancel)
     {
        text.setText("");
        text.requestFocus();
     } else if(source==Ok)
     {
        if(username.getText().equals(""))
        {
            new ErrorBox("Please enter observer names");
            return;
        }
        else if(userPatt.getText().equals(""))
        {
            new ErrorBox("Please enter a PATT reference");
            return;
        }
        else
	      {
	        writeComments(user,text.getText());
          Properties temp=System.getProperties();
	        temp.put(new String("observerNames"),username.getText());
	        temp.put(new String("observingType"),bookCombo.getSelectedItem()+
		      userPatt.getText());
        }
     } else if(source==Dismiss)
       this.dispose();
  }

  /**
  private void writeComments(String n,String c) is a private method
  it is called by the actionPerformed (ActionEvent evt) method
  Currlently it writes a text string into a local file and
  it will write a text string into somewhere in the dhs system

  @param String n, String c
  @return none
  @throws IOException
  */
  private void writeComments(String n,String c)
  {

    try {

      String dir = System.getProperty ("user.home");
      String sep = System.getProperty ("file.separator");
      String linesep = System.getProperty ("line.separator");
         PrintStream ps = new PrintStream(
            new FileOutputStream(dir+sep+"NightComments-"+n+".txt",true));
	 ps.println(linesep);
         ps.println(new Date());
         ps.println(n+" wrote the following comments:");
         ps.println(c);
         ps.close();
         this.dispose();

    } catch (IOException e) {
        new ErrorBox("Error writing comments file: "+e);
    }

  }

    /** public void keyPressed(KeyEvent evt) is a public method
    to react key input.

    @param KeyEvent
    @return  none
    @throws none

  */

  public void keyPressed(KeyEvent evt)
  {
     if(text.getText().length()<80)
     ;
     else
     {
        new ErrorBox(new String("Your input string length is longer than 80"));
        text.setText(text.getText().substring(0,text.getText().length()-1));
     }
  }

  public void keyTyped(KeyEvent evt)
  {
     if(userPatt.getText().length()<18)
     ;
     else
     {
        new ErrorBox(new String("Your input string length is longer than 18"));
        userPatt.setText(userPatt.getText().substring(0,userPatt.getText().length()-1));
     }

  }

  public void keyReleased(KeyEvent evt)
  {
     if(username.getText().length()<18)
     ;
     else
     {
        new ErrorBox(new String("Your input string length is longer than 18"));
        username.setText(username.getText().substring(0,username.getText().length()-1));
     }

  }

  private myTextField username=new myTextField("");
  private myTextField userPatt=new myTextField("");
  private JTextArea text=new JTextArea();
  private JButton Ok, Cancel, Dismiss;
  private String user=new String();
  private JComboBox bookCombo;
  private String title[]={"PATT","UH","ENG","WFS","SERVICE","DDT","other"};

}






