package om.console;

import javax.swing.*;
import java.util.*;

/** class myList is about extending JList with a few extras.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class myList extends JList implements java.io.Serializable
{

    public myList ()
    {
      super();
    }

    public myList (Vector v)
    {
      super(v);
    }

}
