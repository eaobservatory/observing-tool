package om.console;
import javax.swing.*;
import java.awt.*;
import java.lang.*;

/** class myMenuBar about extending  JMenuBar
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class myMenuBar extends JMenuBar implements java.io.Serializable
{
    public myMenuBar ()
    {
      super();
      this.setMaximumSize(new Dimension(100,60));
    }
}