package om.console;
import om.sciProgram.ErrorBox;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.rmi.Remote;

/** commentPanel class is about
    sending comments out to the drama dhs system
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. It seems that it is not a problem for the OM
    as both server and client run the solaris machines

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class commentPanel extends JPanel implements ActionListener,java.io.Serializable,KeyListener
{
 /**  public commentPanel(sendCmds c) is
    the constructor. The class has only one constructor so far.
    a few thing are done during the construction. They are mainly
    about to add GUI bits and setup listeners. the panel is located in the bottom of sequence console frame

    @param  sendCmds c
    @return none
    @throws none
*/
  public commentPanel(sendCmds c)
  {
      comSent=c;
      setLayout(new BorderLayout(10,10));

      //Border border=BorderFactory.createMatteBorder(12, 12,
        //    12,12, Color.lightGray);

      Border border=BorderFactory.createMatteBorder(2, 2,
            2,2, Color.white); //a new border required by gsw on April 07, 2000

      setBorder(new TitledBorder(border,
      "You may enter comments here",0,0,
      new Font("Roman",Font.BOLD,14),Color.black));

      //JScrollPane scrollPane =new JScrollPane();
      //scrollPane.getViewport().add(text);
      //scrollPane.setBounds(10,10,280,80);
      //scrollPane.setMaximumSize(new Dimension (200,2));
      //this is removed becasue of a problem when a rmi call is made.

      text.setBorder(new BevelBorder(BevelBorder.LOWERED));
      text.setColumns(56);
      //text.setSize(80,1);
      text.setFont(new Font("Arial",Font.PLAIN,14));
      JPanel textPanel=new JPanel(new FlowLayout());
      //textPanel.setBounds();
      textPanel.add(text);



      Clear =new JButton("Clear");
      Submit=new JButton("Submit");

      add(Clear,"East");
      add(textPanel,"North");
      add(Submit,"West");

      Clear.addActionListener(this);
      Submit.addActionListener(this);
      text.addKeyListener(this);

  }

   /**  public void actionPerformed(ActionEvent evt) is a public method
    to react button actions i.e. submit comments out

    @param ActionEvent
    @return  none
    @throws none

  */
  public void actionPerformed(ActionEvent evt)
  {
     Object source=evt.getSource();

     if(source==Clear)
     {
        text.setText("");
        text.requestFocus();
     } else if(source==Submit)
     {
        comSent.setComment(text.getText());
        text.setText("");   //reset the text field
        text.requestFocus();
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

  public void keyTyped(KeyEvent evt) { //System.out.println(evt.toString());
  }

  public void keyReleased(KeyEvent evt)
  {
  }

  private sendCmds comSent;
  private JTextField text=new JTextField();
  private JButton Submit,Clear;
}






