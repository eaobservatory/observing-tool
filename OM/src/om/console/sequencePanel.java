package om.console;

import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/**
   final public class sequencePanel describes the sequence panel.
   Please note this is serialized for RMI calls
   even I was told that I can't serialize a swing object.
   since it is host-dependent. But there is only one type of host anyway
   
   @version 1.0 1st June 1999
   @author M.Tan@roe.ac.uk
*/
final public class sequencePanel extends JScrollPane
  implements java.io.Serializable, ListSelectionListener
{
  /** 
      public sequencePanel(String title) is
      the class constructor. The class has only one constructor so far.
      the two things are done during the construction.
      1) adding GUI bits
      2) adding one listener
      
      @param String
      @return  none
      @throws none
  */
  public sequencePanel(String title)
    {
      //border=BorderFactory.createMatteBorder(12, 12,
      //      12,12, Color.lightGray);
      
      
      border=BorderFactory.createMatteBorder(2, 2,
					     2,2, Color.white);
      
      setBorder(new TitledBorder(border,
				 "Sequence: "+title,0,0,
				 new Font("Roman",Font.BOLD,14),Color.black));

      this.setMinimumSize(new Dimension(600,120));
      
      data=new Vector();
      list=new myList();
      
      list.setCellRenderer(new CustomCellRenderer());
      list.setSelectionMode(0);
      list.setVisibleRowCount(22);
      
      list.addListSelectionListener(this);
      this.getViewport().add(list);
      
    }
  
  /** 
      public void setSequenceData (Vector d) is a public method to
      set the sequence data.
      
      @param Vector
      @return  none
      @throws none
  */
//   public void setSequenceData (Vector d) {
//     data=d;
//   }

  /** 
      set an observation title.
      
      @param String
      @return  none
      @throws none
  */
  public void setObsTitle (String t)
    {
      setBorder(new TitledBorder(border,
				 "Sequence: "+t,0,0,
				 new Font("Roman",Font.BOLD,14),Color.black));
    }
  
  /** 
      nothing is done here at the moment.
      @param ListSelectionEvent even
      @return  none
      @throws none
  */
  public void valueChanged( ListSelectionEvent event )
    {
      if( event.getSource() == list)
	{
	  
	}
    }
  
  /**
     set a data list for the sequence panel
     
     @param Vector
     @return  none
     @throws none
  */
  public synchronized void setDataList (Vector d)
    {

      dataValid = false;

      data=d;
      map=null;
      map = new int[data.size()];
      
      if (mode) {
	if (data.size()>0) {
	      
	  temp=new Vector();
	      
	  for (int i=0; i<data.size();i++)
	    temp.addElement(data.elementAt(i));
	      
	  int ind = 0;
	  for (int i=0; i<data.size();i++) {
	    if(temp.elementAt(ind).toString().substring(0,1).equals("-")) {
	      temp.removeElementAt(ind);
	    }else{
	      ind++;
	    }
	  }
	  list.setListData(temp);
	      
	  // Create a map relating the displayed list to the actual
	  // list for the case where "hidden" commands are indeed hidden.
	  int shift=0;
	  for (int i=0;i<data.size();i++) {
	    if (data.elementAt(i).toString().substring(0,1).equals("-")) {
	      shift++;
	    }
	    // trap special cases of hidden commands at ends
	    if ( (i-shift)<0) {
	      map[i]=i;
	    }else if (i==data.size()) {
	      map[i]=i-shift;		
	    }else{
	      map[i]=i-shift;
	    }
	  }
	} else {
	  list.setListData (new Vector());
	}
      } else {

	list.setListData(data);
      }

      dataValid = true;
      notify();
      return;
      
    }
  
  /**
     set a display mode for the sequence panel
     
     @param boolean
     @return  none
     @throws none
  */
  public void setMode(boolean b)
    {
      mode=b;
      
      if(data.size()>0) {
	
	temp.removeAllElements();
	
	for(int i=0; i<data.size();i++)
	  temp.addElement(data.elementAt(i));
	
	int ind = 0;
	for(int i=0;i<data.size();i++) {
	  if (b) {
	    if(temp.elementAt(ind).toString().substring(0,1).equals("-")) {
	      temp.removeElementAt(ind);
	    }else{
	      ind++;
	    }
	  }
	}
	
	list.setListData(temp);
	
	int shift=0;
	for(int i=0;i<data.size();i++) {
	  if(data.elementAt(i).toString().substring(0,1).equals("-"))
	    shift++;

	  // trap special cases of hidden commands at ends
	  if ( (i-shift)<0) {
	    map[i]=i;
	  }else if (i==data.size()) {
	    map[i]=i-shift;		
	  }else{
	    map[i]=i-shift;
	  }
	}	    
	indexReSet();
      }else {
	list.setListData (new Vector());
      }
    }
  
   /**
      reset a selected index in the data list due to the mode changing
      
      @param   none
      @return  none
      @throws none
   */
  private void indexReSet()
    {
      
      if(mode)
	{
	  index=map[index];
	}

      
      list.setSelectedIndex(index);
      validate();
    }

  /**
     Declare the curren tlist data as valid or inValid.
  */
  public synchronized void setDataValid (boolean valid)
    {
      dataValid = valid;
    }

  public synchronized Vector getListData ()
    {
      if (!dataValid) {
	try {
	  wait();
	} catch (InterruptedException e) {}
      }
      return temp;
    }


  public JScrollPane getJScrollPane() {return this;}
  public myList getMyList() {return list;}

  public Vector getExecData(){return data;}
  public int[] getIndexMap () {return map;}
  public boolean getMode () {return mode;}
  public void setIndex(int i){index=i;}
  
  private boolean dataValid = false;
  private boolean mode=true;
  private Vector data,temp;
  private int map[];
  private myList list;
  private Border border;
  private int index;
}


