package om.dramaSocket;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.lang.*;
import om.console.ErrorBox;


/**taskTest class tests ExecDtask class
  it has a main method where a program starts

  @version 1.0 1st June 1999
  @author M.Tan@roe.ac.uk
*/
public class taskTest
{

 public static void main(String[] args)
 {

     String[] proc=new String[4];
     for (int i=0;i<10;i++)
     {

        proc[0]=new String("/home/mt/orac/ufti/inst");

        proc[1]=new String("DSOCKET_"+i);
        proc[2]=new String();
        proc[3]=new String();
        ExecDtask task=new ExecDtask(proc);
        task.run();
      }

     for (int i=0;i<8;i++)
     {
        proc[0]=new String("/home/mt/orac/ufti/inst");

        proc[1]=new String("DSOCKET_"+i);
        proc[2]=new String();
        proc[3]=new String();
        ExecDtask task=new ExecDtask(proc);
        task.run();

        task.getProcess().destroy();

        try {
          task.getProcess().waitFor();
        } catch (InterruptedException e)
        {
          new ErrorBox("waitFor exception" +e);
        }
     }
 }

}

