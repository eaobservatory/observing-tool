package om.sciProgram;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/** final public class AlertBox is to bring up a box
    with a string to alert users of items.
    Please note this is shown in a seperate frame
    and there is no error handling treatment here.

    @version 1.0 28th April 2000
    @author A.Bridger@roe.ac.uk

*/
final public class AlertBox extends JFrame
{

/**  public AlertBox(String message) is
     the constructor. The class has only one constructor so far.

     @param   String message 
     @return  none
     @throws none
*/

  public AlertBox(String message)
  {
    m = message;
    dialog= new JOptionPane();
    dialog.showMessageDialog(this,m, "Alert",
			     JOptionPane.INFORMATION_MESSAGE);
  }

  private JOptionPane dialog;
  private String m=new String();
}



