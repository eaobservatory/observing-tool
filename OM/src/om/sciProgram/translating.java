package om.sciProgram;
import java.awt.*;
import javax.swing.*;
import om.util.OracColor;

/** translating class is to show the translation in process
    Please note this will be run at a different thread

    @version 1.0 1st Dec 1999
    @author M.Tan@roe.ac.uk

*/

final public class translating extends Thread
{
/**  public translating() is
    the constructor. The class has only one constructor so far.
    one thing is done during the construction.
    This is to get a JButton to be flushed

    @param   none
    @return  none
    @throws none
*/
  public translating()
  {
    frame=new JFrame("Translation is in progress.");
    frame.setSize(300,60);
    frame.setLocation(400,200);
    frame.show();
  }

/** public void run () is a public method
    One thing is done here.
    This is to change the color of the background

    @param   none
    @return  none
    @throws none
*/
  public void run()
  {
    while(true)
    {
      try
      {
        sleep(600);
        if(index%2<1)
          frame.setBackground(OracColor.red);
        else
          frame.setBackground(Color.black);

        index++;

      } catch (InterruptedException e)
      {
        System.out.println("translating frame:"+e);
      }

    }
  }

  public JFrame getFrame() {return frame;}

  private long index=0;
  private JFrame frame;
}

