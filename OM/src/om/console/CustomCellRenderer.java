package om.console;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import om.util.OracColor;

/** 
    CustomCellRenderer class is about
    Rendering items in the sequence panel to show an exec sequence
    Please note this is serialized for RMI calls
    
    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class CustomCellRenderer extends JLabel
  implements ListCellRenderer, java.io.Serializable
{
/**
   the constructor. The class has only one constructor so far.
   This is important to have this for show the behaviors of the sequence panel
   
   @param  none
   @return none
   @throws none
*/
  public CustomCellRenderer()
    {
      setOpaque(true);
    }
  
  /** 
      this renders an item in a myList
      
      @param  many
      @return none
      @throws none
  */
  public Component getListCellRendererComponent(JList list, Object value,
						int index, boolean isSelected,
						boolean cellHasFocus )
    {
      
      Border border=BorderFactory.createMatteBorder(0,6,
						    0,0, Color.white);
      this.setBorder(border);

      // Display the text for this item
      setText(value.toString());
      // Default values
      setFont( new Font( "Roman", Font.BOLD, 10) );
      setBackground (Color.white);
      setForeground (Color.black);

      // Draw the correct colors and font
      if( isSelected ) {

	// Set the color and font for a selected item
	setBackground(Color.cyan);
	setFont(new Font( "Roman", Font.BOLD, 12));

	try {
	  
	  // Look for hidden sequence commands
	  if(getText().substring(0,1).equals("-")) {
	    setText(getText().substring(1));
	  }

	  // look for the "title" command & display its parameter
	  if(getText().length()>6) {
	    if(getText().substring(0,6).equals("title ")) {
	      setText(getText().substring(6));
	    }
	  }

	} catch (StringIndexOutOfBoundsException e) {
	  System.out.println ("CustomCellRenderer: Caught exception: string was "+ getText());
	}

      } else {
	
	try {
	  
	  // Look for hidden sequence commands
	  if(getText().substring(0,1).equals("-")) {
	    setText(getText().substring(1));
	  }

	  // look for the "title" command & display its parameter
	  if(getText().length()>6) {
	    if(getText().substring(0,6).equals("title ")) {
	      setText(getText().substring(6));
	      setFont(new Font("Roman",Font.ITALIC,12));
	    }
	  }

	  // enabled breakpoint commands	    
	  if((getText().length()>17)) {
	    if(getText().substring(0,17).equals("*****************")) {
	      setForeground( OracColor.red);
	      setFont( new Font( "Arial", Font.BOLD, 10) );
	    }
	  }

	  // and a disabled breakpoint
	  if((getText().length()>19)) {
	    if(getText().substring(0,19).equals("_ _ _ _ _ _ _ _ _ _")) {
	      setForeground(OracColor.green);
	      setFont( new Font( "Arial", Font.BOLD,10) );
	    }

	  }

	}catch (StringIndexOutOfBoundsException e) {
	  System.out.println ("CustomCellRenderer: Caught exception: string was "+ getText());
	}
      }
	  
      return this;
    }
}
