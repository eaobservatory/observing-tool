package om.dramaSocket;

import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import om.console.ErrorBox;

/**
  ExecDtask is for staring a drama task to the client connection
  it has one constructor only.
  This is a platform-dependent class running under Solaris

  @version 1.0 11 June 1999
  @author M.Tan@roe.ac.uk
*/
final public class ExecDtask extends Thread implements java.io.Serializable
{

/**
  public ExecDtask(String task) is
  the class constructor. The class has only one constructor so far.
  the two things are done during the construction.
*/
  public ExecDtask(String[] task)
  {
    _task=task;
    temp=Runtime.getRuntime();
    this.setPriority(NORM_PRIORITY-1);
  }


/**
  public void run() is a public method
  it starts an external process on a Solaris machine
  This process is a drama task for the ORAC OM in our case

  @param none
  @return none
  @throws IOException
*/
  public void run()
  {

    try
    {
      System.out.println("TASK:"+_task[0]);
      p=temp.exec(_task);

      //add in new features to control task execution

      // redirect output from a Java "shell"to a terminal screen via
      // a separate thread.  This seems to work!
      if (output) {
        final InputStream in=p.getInputStream();
	pt = new Thread() {
	  public void run() {
	    int c;
	    System.out.println("Stdout Thread started");
	    this.setPriority(MAX_PRIORITY);
	    try {
	      while ((c=in.read()) !=-1) {
		System.out.print((char)(c));
		yield();
	      }
	      System.out.println ("Stdout: Got "+c+" !");
	    }catch (IOException e){
	      System.out.println ("Stdout got IO exception:"+e.getMessage());
	    }
	    System.out.println("Stdout Thread stopped");
	  }
	};
	pt.start();
      }
      // See if we need to wait for completion, and if so also get the
      // completion status. Added by AB 17-Apr-00
      if (waitFor) {
	try {
	  p.waitFor();
	  exitValue = p.exitValue();
	  System.out.println ("Exit value was "+exitValue);
	  p.destroy();
	}catch (InterruptedException e) {
	  System.out.println ("Load process was interrupted!");
	}
      }
      if (output && waitFor) {
	pt.stop();
	pt.join();
      }
      return;
    } catch (IOException e)
    {
      new ErrorBox(_task[0]+" has failed to start due to: "+e.toString()+"\n");
      return;
    } catch (Exception e)
    {
      new ErrorBox(_task[0]+" has failed to start due to: "+e.toString()+"\n");
      return;
    }
  }

/**
  public Process getProcess () is a public method
  it returns a process object
  This process is a drama task for the ORAC OM in our case

  @param none
  @return Process
  @throws IOException
*/
  public Process getProcess () {return p;}
  public int  getExitStatus(){return exitValue;}
  public void setOutput(boolean o){output=o;}
  public void setWaitFor(boolean w){waitFor=w;}

  protected void finalize ()
  {
    if(this.isAlive())
      this.stop();
  }

  private String[] _task;
  private Process p;
  private Runtime temp;
  private boolean output=true;
  private boolean waitFor=true;
  private int exitValue=0;
  private Thread pt;
}

