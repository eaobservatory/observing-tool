package om.sciProgram;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/** final public class ErrorBox is to bring up a box
    with a string to show errors
    Please note this is shown in a seperate frame
    and there is no error handling treatment here.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk

*/
final public class ErrorBox extends JFrame
{

/**  public ErrorBox(String _m) is
    the constructor. The class has only one constructor so far.

    @param   Strong m 
    @return  none
    @throws none
*/

  public ErrorBox(String _m)
  {
    m=_m;
    dialog= new JOptionPane();
    dialog.showMessageDialog(this,m, "Error Message",
					JOptionPane.ERROR_MESSAGE);
  }

  private JOptionPane dialog;
  private String m=new String();
}





