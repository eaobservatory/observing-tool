package om.sciProgram;
import om.console.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;

/** aboutPanel class is to show information about this program
    This is located in the login frame at the om starts
    It has one constructor so far and no method
    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/

final class aboutPanel extends JPanel
{
/** public aboutPanel() is
    the class constructor. The class has only one constructor so far.
    a few things are done during the construction.
    They are all about putting GUI bits into the panel.

    @param   none
    @return  none
    @throws none
*/

  public aboutPanel()
  {
    Border border=BorderFactory.createMatteBorder(0, 0,
            0,0, Color.white);

    setLayout(new BorderLayout());

    //this is the orac logo
    ImagePanel orac=new ImagePanel("orac.gif");
    JTextArea title=new JTextArea();
    title.setFont(new Font("Roman",Font.BOLD,12));
    title.setText("\n    Astronomy "+" Technology Centre    \n"
                  +"        V1.1 2000 (c) UK PPARC \n");
    title.setEditable(false);
    title.setRequestFocusEnabled(false);
    add(orac);
    add(title,"South");
    setRequestFocusEnabled(false);
  }
}





