package om.console;
import java.util.*;

import javax.swing.*;
import javax.swing.JPanel;


/** public class instrumentStatusPanel gives a panel for instrument status display.


    @version 1.0 22nd March 2000
    @author M.Tan@roe.ac.uk
*/
public abstract class instrumentStatusPanel extends JPanel implements messageInterface,java.io.Serializable
{
/** public instrumentStatusPanel () is
    the class constructor. The class has only one constructor so far.

    @param none
    @return  none
    @throws none
*/
    public instrumentStatusPanel ()
    {
    }

    public void messageString(String name, String value) {};
    public void close() {};

    public abstract Vector getModel ();
    public abstract void setModel(Vector m);

}

