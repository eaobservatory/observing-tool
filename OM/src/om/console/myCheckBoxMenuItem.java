package om.console;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;

/** class myCheckBoxMenuItem  is aboutextending JCheckBoxMenuItem.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. But there is only one type of host anyway.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class myCheckBoxMenuItem extends JCheckBoxMenuItem implements java.io.Serializable
{
  public myCheckBoxMenuItem(String title)
  {
    super(title);
  }
}

