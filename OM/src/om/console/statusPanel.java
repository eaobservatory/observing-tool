package om.console;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.border.*;

/** final public class statusPanel gives a panel with two fields.
    one is a Label field, the other is a TextField.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. But there is only one type of host anyway

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class statusPanel extends JPanel  implements java.io.Serializable
{
/** public statusPanel (String n, String v) is
    the class constructor. The class has only one constructor so far.

    @param String, String
    @return  none
    @throws none
*/
    public statusPanel (String n, String v)
    {

      setLayout(new GridLayout(1,2,0,10));

      name.setText(n);
      value.setText(v);
      value.setBackground(Color.white);
      value.setColumns(6);

      add(name);
      add(value);
    }

    /**
     * Returns text field with value rather than value as string.
     *
     * IMPORTANT: Do not confuse with method {@link #getValue()}.
     *
     * @deprecated  replaced by {@link #getValueField()}
     */
    public myTextField getValu(){return value; }

    /**
     * Returns text field with value rather than value as string.
     *
     * IMPORTANT: Do not confuse with method {@link #getName()}.
     * 
     * @deprecated  replaced by {@link #getNameLabel()} 
     */
    public myLabel getNam() {return name; }

   
    /**
     * Returns parameter / name string.
     */
    public String getParameter() {
      return name.getText();
    }
    
    /**
     * Sets parameter / name.
     */
    public void setParameter(String str) {
      name.setText(str);
    }
    
    /**
     * Returns value string.
     * 
     * IMPORTANT: Do not confuse with deprecated method {@link #getValu()}.
     */
    public String getValue() {
      return value.getText();
    }
    
    /**
     * 
     */
    public void setValue(String str) {
      value.setText(str);
    }
 
    public void setEnabled(boolean enabled) {
      name.setEnabled(enabled);
      value.setEnabled(enabled);
    }

    public void setEditable(boolean editable) {
      value.setEditable(editable);
    }

    private myTextField value=new myTextField("");
    private myLabel name=new myLabel("");
}

