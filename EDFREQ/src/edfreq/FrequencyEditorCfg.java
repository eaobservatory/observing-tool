package edfreq;

import java.util.Hashtable;
import java.io.InputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import JSX.ObjOut;
import JSX.ObjIn;

/**
 * XML serialised instances of this class are used as Frequency Editor configuration files/resources.
 *
 * This class provides static methods for (de)serialisation using the package JSX.
 * If no xml configuration file is available then the default constructor can be used
 * to create a default configuration object (ACSIS setup).
 * <p>
 * The {@link edfreq.FrontEnd} class hold a public static reference to an FrequencyEditorCfg object
 * ({@link edfreq.FrontEnd.cfg}) which can be used throughout the frequency editor code.
 *
 * @author Martin Folger
 */
public class FrequencyEditorCfg {

  public String[] frontEnds;
  public String[] frontEndModes;
  public boolean centreFrequenciesAdjustable;
  public Hashtable receivers;
  
  public FrequencyEditorCfg() {
    frontEnds     = new String[] { "A3", "B3", "WC", "WD", "HARP-B" };
    frontEndModes = new String[] { "ssb", "dsb" };
    centreFrequenciesAdjustable = true;
    receivers = ReceiverList.getReceiverTable(); 
  }

  protected static FrequencyEditorCfg getCfg(InputStream inputStream) {
    try {
      ObjIn objIn = new ObjIn(inputStream);
      return (FrequencyEditorCfg)objIn.readObject();
    }
    catch(Exception e) {
      System.out.println("Could not create a FrequencyEditorCfg object from the input stream provided.");
      System.out.println("Creating default FrequencyEditorCfg object.");
      
      return new FrequencyEditorCfg();
    }
  }

  public static void main(String [] args) {
    if(args.length > 0) {
      File cfgFile = new File(args[0]);

      if(cfgFile.exists()) {
        System.out.println("Cannot create xml cfg file " + args[0] + ". File exists.");
	return;
      }
      else {
        createDefaultCfgFile(cfgFile);
      }
    }
  }

  public static void createDefaultCfgFile(File file) {
    try {
      ObjOut objOut = new ObjOut(false, new FileWriter(file));
      FrequencyEditorCfg frequencyEditorCfg = new FrequencyEditorCfg();
      objOut.writeObject(frequencyEditorCfg);
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }
}


