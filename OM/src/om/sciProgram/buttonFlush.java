package om.sciProgram;
import java.awt.*;
import javax.swing.*;
import om.util.OracColor;

/** buttonFlush class is to flush a button
    At the moment, we only use it to flush the fetch button
    Please note this is run at a different thread

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk

*/

final public class buttonFlush extends Thread
{
/**  public buttonFlush(JButton b) is
    the constructor. The class has only one constructor so far.
    one thing is done during the construction.
    This is to get a JButton to be flushed

    @param   JButton
    @return  none
    @throws none
*/
  public buttonFlush(JButton b)
  {
    button=b;
  }

/** public void run () is a public method
    One thing is done here.
    This is to change the color of the button

    @param   none
    @return  none
    @throws none
*/
  public void run ()
  {
    while(true)
    {
      try
      {
        sleep(1000);
        if(index%2<1)
          button.setForeground(OracColor.red);
        else
          button.setForeground(Color.black);

        index++;

      } catch (InterruptedException e)
      {
        System.out.println("buttonFlush:"+e);
      }

    }

  }

  private long index=0;
  private JButton button;
}

