package om.dramaSocket;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


final public class ErrorBox extends JFrame
{

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





