package om.sciProgram;

import om.console.*;
import om.frameList.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import gemini.sp.*;
import gemini.sp.SpItem;
import gemini.sp.ipc.*;
import orac.ukirt.inst.*;
import orac.ukirt.iter.*;
import orac.ukirt.util.SpTranslator;
import gemini.sp.SpAvTable;


/** public final class newExposureTime is a JFrame to redefine
    a exposure time for a instrument

    @version 1.0 24 May 2000
    @author M.Tan@roe.ac.uk
*/

public final class newExposureTime extends JFrame  implements ActionListener
{

/** public newExposureTime() is
    the constructor. The class has only one constructor so far.

    @param  none
    @return none
    @throws none
*/

  public newExposureTime(SpItem inst)
  {

     String instrument=new String(inst.type().getReadable());

     setTitle("Exposure Time for "+ instrument);
     setSize(260, 150);
     setLocation(400,200);

     setResizable(false);

     Border border=BorderFactory.createMatteBorder(12, 12,
            12,12, Color.lightGray);

      addWindowListener(new WindowAdapter()
      {  public void windowClosing(WindowEvent e)
            {
            }

             public void windowActivated(WindowEvent e)
            {
              validate();
            }

       } );

       Container contentPane = getContentPane();
       contentPane.setLayout(new BorderLayout(20,20));

       JPanel top=new JPanel(new GridLayout(1,2,20,20));
       top.setBorder(border);
       myLabel title=new myLabel("Exposure Time:");
       expTime=new myTextField("");
       expTime.setEditable(true);

       top.add(title);
       top.add(expTime);

       contentPane.add(top);

       JPanel buttons=new JPanel(new GridLayout(1,2,20,20));
       buttons.setBorder(border);

       ok=new myButton("OK");
       ok.setEnabled(true);

       cancel=new myButton("Cancel");
       cancel.setEnabled(true);

       buttons.add(ok);
       buttons.add(cancel);

       ok.addActionListener(this);
       cancel.addActionListener(this);

       contentPane.add(buttons,"South");
       show();
  }


  /**  public void actionPerformed(ActionEvent evt) is a public method
      to react button push actions

    @param ActionEvent
    @return  none
    @throws none

  */
  public void actionPerformed (ActionEvent evt)
  {
     Object source = evt.getSource();

     if (source ==ok)   //sending out a run command
     {
       System.out.println("TEST:::"+expTime.getText());
     }

     if(source==cancel)
      this.dispose();


  }

  private myButton ok, cancel;
  private myTextField expTime;
}
