package om.sciProgram;                                   
import om.console.*;
import om.frameList.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;

/** final class loginPanel is the main login panel
    which has a number of fields e.g. password.
    a spConnector object will be created after a successful login.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk

*/
final class loginPanel extends JPanel implements KeyListener,ActionListener
{
/** public loginPanel(JFrame m) is
    the class constructor. The class has only one constructor so far.
    a few thins are done during the construction. They are mainly about
    putting GUI bits into the panel and setting up listeners

    @param a JFrame
    @return  none
    @throws none

*/
  public loginPanel(JFrame m, selectorFrame s)
  {
    _loginFrame=m;
    _selectorFrame=s;
    Border border=BorderFactory.createMatteBorder(12, 12,
            12,12, Color.lightGray);

    setBorder(new TitledBorder(border,
      "Please Login here",0,0,
      new Font("Roman",Font.BOLD,14),Color.black));

    setLayout(new GridLayout(2,4,0,20));

    myLabel username=new myLabel("User ID :");
    myLabel password=new myLabel("Password:");
    user=new myTextField("");
    user.setEditable(true);

    user.setBorder(new BevelBorder(BevelBorder.LOWERED));
    pass=new JPasswordField();
    pass.setBorder(new BevelBorder(BevelBorder.LOWERED));

    ok=new JButton("Ok");
    ok.setBorder(new BevelBorder(BevelBorder.RAISED));
    ok.setBackground(Color.lightGray);

    cancel=new JButton("Cancel");
    cancel.setBorder(new BevelBorder(BevelBorder.RAISED));
    cancel.setBackground(Color.lightGray);

    user.setNextFocusableComponent (pass);
    pass.setNextFocusableComponent (ok);
    ok.setNextFocusableComponent (cancel);
    cancel.setNextFocusableComponent (user);

    add(username);
    add(user);
    add(new myLabel(" "));
    add(ok);
    add(password);
    add(pass);
    add(new myLabel(" "));
    add(cancel);

    user.addKeyListener(this);
    pass.addKeyListener(this);
    cancel.addActionListener(this);
    ok.addActionListener(this);
    user.requestFocus();
  }

  /** public void keyReleased(KeyEvent evt) is a public method
    to react key input.  Mostly the return key

    @param KeyEvent
    @return  none
    @throws none

  */
  public void keyReleased(KeyEvent evt)
  {

    if(pass.hasFocus())
    {
      if(evt.getKeyChar()=='\n')
      {

        if(user.getText().length()<1)
        {
           new ErrorBox("You have to login first.");
           return;
        }

         if(_spConnector==null)
          _spConnector=new spConnector(user.getText(),pass.getText(),_loginFrame,_selectorFrame);
        else
        {
          _spConnector.setPassword(pass.getText());
          _spConnector.setUsername(user.getText());
          _spConnector.fetchListing();
        }

        ok.requestFocus();
      }

    }

     if(user.hasFocus())
     if(evt.getKeyChar()=='\n')
     {
        pass.requestFocus();
        return;
     }

  }

  /** public void keyPressed(KeyEvent evt) is a public method
    to react key input.  Mostly the TAB key

    @param KeyEvent
    @return  none
    @throws none

  */

  public void keyPressed(KeyEvent evt)
  {

     //System.out.println(evt.toString());
     if(pass.hasFocus())
     if(evt.getKeyChar()=='\t')
     {
         if(user.getText().length()<1)
         {
           new ErrorBox("You have to login first.");
           return;
         } else
         {
            ok.requestFocus();
            return;
         }
     }


     if(user.hasFocus())
     if(evt.getKeyChar()=='\t')
     {
        pass.requestFocus();
        return;
     }


     if(ok.hasFocus())
     if(evt.getKeyChar()=='\t')
     {
        cancel.requestFocus();
        return;
     }

     if(cancel.hasFocus())
     if(evt.getKeyChar()=='\t')
     {
        user.requestFocus();
        return;
     }
  }


  public void keyTyped(KeyEvent evt) { //System.out.println(evt.toString());
  }

   /**  public void actionPerformed(ActionEvent evt) is a public method
    to react button actions

    @param ActionEvent
    @return  none
    @throws none

  */
  public void actionPerformed(ActionEvent evt)
  {
    Object source=evt.getSource();

    if(source==cancel)
    {
      user.setText("");
      pass.setText("");
      user.requestFocus();

    } else if (source==ok)
    {
      if(user.getText().length()>0)
      {

        //System.out.println("::"+user.getText()+"; "+pass.getText());
        //System.out.println("::"+_loginFrame.toString()+"; "+_selectorFrame.toString());

        if(_spConnector==null)
          _spConnector=new spConnector(user.getText(),pass.getText(),_loginFrame,_selectorFrame);
        else
        {
          _spConnector.setPassword(pass.getText());
          _spConnector.setUsername(user.getText());
          _spConnector.fetchListing();
        }

      }
      else
        new ErrorBox("You have to login first.");
    }
  }

  public spConnector _spConnector=null;
  public myTextField getUsername(){return user;}
  private myTextField user;
  private JPasswordField pass;
  private JButton cancel,ok;
  private selectorFrame _selectorFrame;
  private JFrame _loginFrame;
}





