package om.sciProgram;

import gemini.sp.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import java.awt.*;
import java.lang.*;

/** final public class treeCellRenderer renders
    a JTree object in a selection frame

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class treeCellRenderer extends JLabel
  implements TreeCellRenderer
{
  /** public treeCellRenderer() is
    the constructor. The class has only one constructor so far.
    one thing is done during the construction. This is
    about getting a number of icon gif files

    @param  none
    @return none
    @throws none
  */
  public treeCellRenderer()
    {

    Border border=BorderFactory.createMatteBorder(0,6,0,0, Color.white);
    setBorder(border);

    icon=new ImageIcon[6];
    icon[0]=new ImageIcon(System.getProperty("IMAG_PATH")+"note-tiny.gif");
    icon[1]=new ImageIcon(System.getProperty("IMAG_PATH")+"observation.gif");
    icon[2]=new ImageIcon(System.getProperty("IMAG_PATH")+"iterComp.gif");
    icon[3]=new ImageIcon(System.getProperty("IMAG_PATH")+"iterObs.gif");
    icon[4]=new ImageIcon(System.getProperty("IMAG_PATH")+"component.gif");
    icon[5]=new ImageIcon(System.getProperty("IMAG_PATH")+"archiv_small.gif");
	}


  /** 
      public Component getTreeCellRendererComponent( JTree tree,
      Object value, boolean bSelected, boolean bExpanded,
      boolean bLeaf, int iRow, boolean bHasFocus )
      is a public method to render the JTree object. This is mainly about
      to attach a icon to a related item in the JTree

      @param many
      @return  Component
      @throws none

  */
  public Component getTreeCellRendererComponent 
      (JTree tree, Object value, boolean bSelected, boolean bExpanded,
       boolean bLeaf, int iRow, boolean bHasFocus )
    {

    // Find out which node we are rendering and get its text
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
      SpItem item= (SpItem)node.getUserObject();

      String text=item.getTitle();
      String type=item.typeStr();

      setFont( new Font( "Roman", Font.PLAIN, 12));

      if(type.equals("no"))
	setIcon(icon[0]);
      else if(type.equals("ob"))
	setIcon(icon[1]);
      else if(type.equals("if"))
	setIcon(icon[2]);
      else if(type.equals("ic"))
	setIcon(icon[3]);
      else if(type.equals("oc"))
	setIcon(icon[4]);
      else if(type.equals("pr"))
	{
	  setIcon(icon[5]);
	  setFont( new Font( "Roman", Font.BOLD, 12));
	}

      setText(text);
      setForeground(Color.black);
      
      this.setIconTextGap(10);
      tree.setRowHeight(20);
      
      this.bSelected = bSelected;
      return this;
    }

  /** public void paint( Graphics g )
      is a hack to paint the background.  Normally a JLabel can
      paint its own background, but due to an apparent bug or
      limitation in the TreeCellRenderer, the paint method is
      required to handle this.

      @param Graphics g
      @return  none
      @throws none
  */
  public void paint( Graphics g )
    {
      Color		bColor;
      Icon		currentI = getIcon();
      
      // Set the correct background color
      bColor = bSelected ? Color.cyan : Color.white;
      g.setColor( bColor );
      
      // Draw a rectangle in the background of the cell
      g.fillRect( 0, 0, getWidth() - 1, getHeight() - 1 );
      
      super.paint( g );
    }
  
  private ImageIcon[] icon;
  private boolean     bSelected;
}
