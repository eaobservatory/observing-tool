package om.console;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.event.*;
import javax.swing.border.*;

/** This class implements the UIST status display
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. but there is only ONE type of host anyway

    @version 0.0 5th June 2001
    @author ab@roe.ac.uk
*/
public final class UISTStatus extends instrumentStatusPanel
{
  /** public UISTStatus() is
    the class constructor. The class has only one constructor so far.

    @param none
    @return  none
    @throws none
  */
  public UISTStatus() {

    Border border=BorderFactory.createMatteBorder(2, 2, 2, 2, Color.white);

    setBorder(new TitledBorder(border,
			       "UIST Status",0,0,
			       new Font("Roman",Font.BOLD,14),Color.black));
    
    setLayout(new BorderLayout(10,10));

    JLabel jl = new JLabel ("This space for rent...");
    add(jl);
  }


  /**  public void close () is a public method.
    to disable the panel.

    @param none
    @return  none
    @throws none
  */
  public void close () { }

   /**  public void setModel (Vector m)
    to recover the object status.

    @param Vector
    @return  none
    @throws none
  */
  public void setModel (Vector m)
  {

  }


  /**  public Vector getModel ()
    to get the object status.

    @param none
    @return  Vector
    @throws none
  */
  public Vector getModel ()
  {
    return new Vector();
  }

}

