package om.sciProgram;

import om.frameList.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;


/** public final class loginFrame is the main login frame
    which has logo image and a number of fields e.g. password.
    It is the first frame to be seen when the om starts.


    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/

public final class loginFrame extends JFrame
{

/**  public loginFrame() is
    the constructor. The class has only one constructor so far.

    @param  none
    @return none
    @throws none
*/

  public loginFrame()
  {
     construction();
  }


  public loginFrame(selectorFrame l)
  {
      existFrame=l;
      construction();
  }

  public loginPanel getLoginPanel() {return login;};
  private void construction ()
  {
     setTitle("ORAC Observation Manager (ORAC-OM)");
      setSize(440, 439);
      setLocation(400,200);
      setResizable(false);
      this.setBounds(400,200,440,439);

      addWindowListener(new WindowAdapter()
      {  public void windowClosing(WindowEvent e)
            {
              System.exit(0);
            }

             public void windowActivated(WindowEvent e)
            {
              setSize(440, 439);
              setResizable(false);
              validate();
            }

       } );

       Container contentPane = getContentPane();
       contentPane.setLayout(new BorderLayout(0,0));

       login=new loginPanel(this,existFrame);
       ImagePanel ukirt=new ImagePanel("ukirt.gif");
       aboutPanel about=new aboutPanel();
       menuSci menu=new menuSci();
       setJMenuBar(menu);

       contentPane.add(ukirt);

       contentPane.add(about,"East");
       contentPane.add(login, "South");
  }

  private selectorFrame existFrame;
  private loginPanel login;
}
