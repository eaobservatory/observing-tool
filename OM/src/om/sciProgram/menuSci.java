package om.sciProgram;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import orac.helptool.JHLauncher;
import om.util.OracColor;

/** final class menuSci is the menu in the login frame
    which has two items. one is "exit" and the other is "help"

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk

*/
final class menuSci extends JMenuBar implements ActionListener
{

/**  public menuSci() is
    the class constructor. The class has only one constructor so far.
    a few thins are done during the construction. They are mainly about
    putting GUI bits into the menu and setting up listeners

    @param none
    @return  none
    @throws none

*/ public menuSci()
   {
      Border border=BorderFactory.createMatteBorder(10, 10,
           10,10, Color.white);

      setBorder(new TitledBorder(border,
      "",0,0,
      new Font("Roman",Font.BOLD,14),Color.black));

      setBackground(Color.white);
      setLayout(new BorderLayout(20,20));


      menuExit=new JMenuItem("Exit");
      menuExit.setBorder(new BevelBorder(BevelBorder.RAISED));
      menuExit.setMargin(new Insets(10,0,10,0));
      menuExit.setBackground(OracColor.red);
      add(menuExit,"West");

      menuHelp=new JMenuItem("Help");
      menuHelp.setBorder(new BevelBorder(BevelBorder.RAISED));
      menuHelp.setMargin(new Insets(10,0,10,0));
      menuHelp.setBackground(Color.cyan);
      add(menuHelp,"East");

      menuExit.addActionListener(this);
      menuHelp.addActionListener(this);
    }

 /**  public void actionPerformed(ActionEvent evt) is a public method
    to react button actions

    @param ActionEvent
    @return  none
    @throws none

  */
   public void actionPerformed (ActionEvent evt)
   {
      Object source = evt.getSource();

      if (source == menuExit)
        Exit();
      else if (source == menuHelp)
        Help();
   }

  /**   private void Help() is a private method
    to launch a help frame with a help file

    @param ActionEvent
    @return  none
    @throws none
  */
   private void Help()
   {
    String[] args={"-helpset","om.hs"};

    JHLauncher l=new JHLauncher(args);

    System.out.println("JHL:"+l);
   }

  /** private void Exit() is a private method
    to exit the system

    @param ActionEvent
    @return  none
    @throws none

  */

  private void Exit ()
  {
    int selection;
    selection=JOptionPane.showConfirmDialog(menuExit, "Quit the Observing Manager?",
    "Exit Comfirmation", JOptionPane.YES_NO_OPTION,
		JOptionPane.QUESTION_MESSAGE);

    if(selection==JOptionPane.YES_OPTION)
        System.exit(0);

  }

  private JMenuItem menuExit,menuHelp;
}
