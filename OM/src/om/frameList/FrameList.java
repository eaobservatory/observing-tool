/**
  FrameList.java 1999/04/28

  Copyright 2000 by PPARC UK,
  Personnel Min Tan UK ATC Edinburgh EH9 3HJ.
  Email:mt@roe.ac.uk
  All rights reserved.

  This package has my first Java bean to produce a active console list
  for ORAC OM. It includes the following three class

  1) FrameList is the main class in this package to provide a list of
  the consoles active within the ORAC OM. This is a Java Bean
  2) frameListEvent is not a java bean but an auxiliary event class
  3) frameListListener is interface as auxiliary
*/

package om.frameList;

import java.util.*;

/** FrameList class is my first Java bean to provide a list of the active
    console within the OM. The main reason to have this bean is to use the event
    handling mechnaism of Java beans rather than use of its visual manpulation stuff

    @version 1.0 April 28, 2000
    @author M.Tan@roe.ac.uk
*/

final public class FrameList extends Object implements java.io.Serializable
{
/**  public FrameList() is
    the class constructor. The class has only one constructor so far.

    @param none
    @return  none
    @throws none
*/
  public FrameList () //constructor
  {
    List=new Vector();
  }

/** public void setList (Vector v)
    follows Java bean name convention
    @param Vector v
    @return none
    @throws none
*/
  public void setList  (Vector v)
  {
    List=v;
  }


/** public Vector getList ()
    follows Java bean name convention
    @param none
    @return Vector
    @throws none
*/
  public Vector getList()
  {
    return List;
  }


  public synchronized void addFrameListEventListener(frameListListener l)
  {
    listeners.addElement(l);
  }

  public synchronized void removeFrameListEventListener(frameListListener l)
  {
    listeners.removeElement(l);
  }


  public void addFrameList(Object o)
  {
    List.addElement(o);
    generateBeanEventAdded();
  }


  public synchronized void removeFrameList(Object o)
  {
    List.removeElement(o);
    generateBeanEventRemoved();
  }

  public synchronized void setConnectedInstrument(Object o)
  {
    connectedInstrument=new String(o.toString());
    generateBeanEventConnectedInstrument();
  }

  public synchronized String getConnectedInstrument()
  {
    return connectedInstrument;
  }



  private synchronized void generateBeanEventAdded()
  {
    //here is the callbacks by generating a new event
    frameListEvent event= new frameListEvent(this); //generate a new bean event
    synchronized (this)
    {
      frameListListener ls;
      Enumeration e=listeners.elements();
        while(e.hasMoreElements())
        {
          ls=(frameListListener)e.nextElement();
          ls.frameListAdded(event);
        }
     }
  }


  private synchronized void generateBeanEventRemoved()
  {
    //here is the callbacks by generating a new event
    frameListEvent event= new frameListEvent(this); //generate a new bean event
    synchronized (this)
    {
      frameListListener ls;
      Enumeration e=listeners.elements();
        while(e.hasMoreElements())
        {
          ls=(frameListListener)e.nextElement();
          ls.frameListRemoved(event);
        }
    }
  }

  private synchronized void generateBeanEventConnectedInstrument()
  {

    //here is the callbacks by generating a new event
     //here is the callbacks by generating a new event
    frameListEvent event= new frameListEvent(this); //generate a new bean event
    synchronized (this)
    {
      frameListListener ls;
      Enumeration e=listeners.elements();
        while(e.hasMoreElements())
        {
          ls=(frameListListener)e.nextElement();
          ls.connectedInstrumentChanged(event);
        }
    }

  }


  private Vector listeners=new Vector();
  private Vector List;  //this is a bean properity
  private String connectedInstrument=new String("NONE"); //this stores information which instrument is connected to the TCS

}



