package om.console;
import javax.swing.JFileChooser;

/** class myFileChooser is about extending  myFileChooser
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class myFileChooser extends JFileChooser implements java.io.Serializable
{

    public myFileChooser ()
    {
      super();
    }

    public myFileChooser (String path)
    {
       super(path);
    }

}
