package om.console;
import javax.swing.JFrame;
import java.awt.*;

/** class myFrame is extended from JFrame
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. This is a just a class for testing

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class myFrame extends JFrame implements java.io.Serializable
{

    public myFrame ()
    {
     setTitle("TEST FRAME");
     setSize(100,300);

     setJMenuBar(new myMenuBar());
    }
}
