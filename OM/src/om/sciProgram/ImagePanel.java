package om.sciProgram;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** final class ImagePanel is to show the orac
    logo image in the login frame. A image file is located in the path
    "IMAG_PATH" read in from a configuration file.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk

*/
final class ImagePanel extends JPanel
{
/** public ImagePanel(String name) is
    the constructor. The class has only one constructor so far.

    @param   Strong name
    @return  none
    @throws none
*/

   public ImagePanel(String name)
   {
     image = Toolkit.getDefaultToolkit().getImage
         (System.getProperty("IMAG_PATH")+name);

      //System.out.println("TEMP:"+System.getProperty("IMAG_PATH")+name);
      MediaTracker tracker = new MediaTracker(this);
      tracker.addImage(image, 0);
      try { tracker.waitForID(0); }
      catch (InterruptedException e) {}
   }

 /**
  public void public void paintComponent(Graphics g) is a public method
  to define the way showing the image 

  @param Graphics g
  @return none
  @throws none
  */

   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      Dimension d = getSize();
      int clientWidth = d.width;
      int clientHeight = d.height;

      int imageWidth = image.getWidth(this);
      int imageHeight = image.getHeight(this);

      g.drawImage(image, 0, 0, this);
      for (int i = 0; i * imageWidth <= clientWidth; i++)
        for (int j = 0;
               j * imageHeight <= clientHeight; j++)
            if (i + j > 0)
               g.copyArea(0, 0, imageWidth, imageHeight,
                   i * imageWidth, j * imageHeight);
   }

   private Image image;
}
