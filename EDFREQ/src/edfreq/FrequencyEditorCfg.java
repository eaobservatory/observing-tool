package edfreq;

import java.util.Hashtable;
import java.io.InputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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

  public static final String FREQ_EDITOR_CFG_PROPERTY = "FREQ_EDITOR_CFG";

  public String[] frontEnds;
  public String[] frontEndModes;
  public boolean centreFrequenciesAdjustable;
  public Hashtable receivers;

  private static FrequencyEditorCfg _frequencyEditorCfg = null;

  public FrequencyEditorCfg() {
    frontEnds     = new String[] { "A3", "B3", "WC", "WD", "HARP-B" };
    frontEndModes = new String[] { "ssb", "dsb" };
    centreFrequenciesAdjustable = true;
    receivers = ReceiverList.getReceiverTable();
  }

  public static FrequencyEditorCfg getConfiguration() {
    if(_frequencyEditorCfg == null) {
      String freqEditorCfgFile = System.getProperty(FREQ_EDITOR_CFG_PROPERTY);
      URL freqEditorCfgUrl = null;

      if(freqEditorCfgFile != null) {
        freqEditorCfgUrl = ClassLoader.getSystemClassLoader().getResource(freqEditorCfgFile);
      }

      if(freqEditorCfgUrl != null) {
        try {
          _frequencyEditorCfg = getConfiguration(freqEditorCfgUrl.openStream());
        }
        catch(IOException e) {
          System.out.println("Using default FrequencyEditorCfg object.");
          _frequencyEditorCfg = new FrequencyEditorCfg();
        }
      }
      else {
        System.out.println("Using default FrequencyEditorCfg object.");
        _frequencyEditorCfg = new FrequencyEditorCfg();
      }
    }

    return _frequencyEditorCfg;
  }

  public static FrequencyEditorCfg getConfiguration(InputStream inputStream) {
    try {
      ObjIn objIn = new ObjIn(inputStream);
      return (FrequencyEditorCfg)objIn.readObject();
    }
    catch(Exception e) {
      System.out.println("Could not create a FrequencyEditorCfg object from the input stream provided.");
      e.printStackTrace();
      
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


