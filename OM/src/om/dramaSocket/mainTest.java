package om.dramaSocket;

import om.console.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.lang.*;
import java.rmi.*;

/**
  mainTest class tests DramaSocket class
  it has a main method where a program starts

  @version 1.0 1st June 1999
  @author M.Tan@roe.ac.uk
*/
public class mainTest
{

  public static void main(String[] args)
  {

      DramaSocket test = new DramaSocket(1234,null);
      test.start();
  }
}

