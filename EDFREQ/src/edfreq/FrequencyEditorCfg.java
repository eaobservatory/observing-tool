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
 * @author Martin Folger
 */
public class FrequencyEditorCfg {

//  public static FrequencyEditorCfg instance = _readCfg();

  //public static String CFG_FILE = System.getProperty("FREQ_EDITOR_CFG", "freqEditorCfg.xml");

  public String[] frontEnds;
  public String[] frontEndModes;
//  public static String[] frontEndBands;
  public boolean centreFrequenciesAdjustable;
  public Hashtable receivers;
  
  public FrequencyEditorCfg() {
    frontEnds     = new String[] { "A3", "B3", "WC", "WD", "HARP-B" };
    frontEndModes = new String[] { "ssb", "dsb" };
    centreFrequenciesAdjustable = true;
//    frontEndBands = new String[] { "usb", "lsb", "optimum" };

    // Deal with receivers
    receivers = new Hashtable(); 
    Receiver r;

    // Create details of all known receivers and record them in the hashtable

    r = new Receiver ( "HARP-B", 325.0E9, 375.0E9, 5.0E9, 2.0E9 );

    r.bandspecs.add ( new BandSpec ( "1-system", 1, 0.25E9, 8192, 
        1.0E9, 2048 ) );
    r.bandspecs.add ( new BandSpec ( "2-system", 2, 0.25E9, 4096,
        1.0E9, 1024 ) );
    r.bandspecs.add ( new BandSpec ( "hybrid", 1, 0.5E9, 8192,
        2.0E9, 2048 ) );

    receivers.put ( "HARP-B", r );


    r = new Receiver ( "A3", 215.0E9, 272.0E9, 4.0E9, 1.8E9 );

    r.bandspecs.add ( new BandSpec ( "4-system", 4, 0.25E9, 8192,
        1.0E9, 2048 ) );
    r.bandspecs.add ( new BandSpec ( "8-system", 8, 0.25E9, 4096,
        1.0E9, 1024 ) );
    r.bandspecs.add ( new BandSpec ( "hybrid", 1, 1.0E9, 32768,
        4.0E9, 8192 ) );

    receivers.put ( "A3", r );


    r = new Receiver ( "B3", 322.0E9, 373.0E9, 4.0E9, 1.8E9 );

    r.bandspecs.add ( new BandSpec ( "4-system", 4, 0.25E9, 8192,
        1.0E9, 2048 ) );
    r.bandspecs.add ( new BandSpec ( "8-system", 8, 0.25E9, 4096,
        1.0E9, 1024 ) );
    r.bandspecs.add ( new BandSpec ( "hybrid", 1, 1.0E9, 32768,
        4.0E9, 8192 ) );

    receivers.put ( "B3", r );


    r = new Receiver ( "WC", 430.0E9, 510.0E9, 4.0E9, 1.8E9 );

    r.bandspecs.add ( new BandSpec ( "4-system", 4, 0.25E9, 8192,
        1.0E9, 2048 ) );
    r.bandspecs.add ( new BandSpec ( "8-system", 8, 0.25E9, 4096,
        1.0E9, 1024 ) );
    r.bandspecs.add ( new BandSpec ( "hybrid", 1, 1.0E9, 32768,
        4.0E9, 8192 ) );

    receivers.put ( "WC", r );


    r = new Receiver ( "WD", 630.0E9, 710.0E9, 4.0E9, 1.8E9 );

    r.bandspecs.add ( new BandSpec ( "4-system", 4, 0.25E9, 8192,
        1.0E9, 2048 ) );
    r.bandspecs.add ( new BandSpec ( "8-system", 8, 0.25E9, 4096,
        1.0E9, 1024 ) );
    r.bandspecs.add ( new BandSpec ( "hybrid", 1, 1.0E9, 32768,
        4.0E9, 8192 ) );

    receivers.put ( "WD", r );
  }

  protected static FrequencyEditorCfg getCfg(InputStream inputStream) {
    try {
      ObjIn objIn = new ObjIn(inputStream); //new FileReader(CFG_FILE));
      return (FrequencyEditorCfg)objIn.readObject();
    }
    catch(Exception e) {
      System.out.println("Could not create a FrequencyEditorCfg object from the input stream provided.");
      System.out.println("Creating default FrequencyEditorCfg object.");
      
      return new FrequencyEditorCfg();
    }
  }

/**
  public static FrequencyEditorCfg getInstance() {
    if(_instance == null) {
      _readCfg();
    }

    return _instance;
  }
*/
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
    
//    init();

  }

  /**
   * @author Dennis Kelly ( bdk@roe.ac.uk ),
             turned into inner class of FrequencyEditorCfg by Martin Folger ( M.Folger@roe.ac.uk )
   */
/*  public class BandSpec {
    public String name;
    public int numBands;
    public double loBandWidth;
    public int loChannels;
    public double hiBandWidth;
    public int hiChannels;

    public BandSpec ( String name, int numBands, double loBandWidth, 
                      int loChannels, double hiBandWidth, int hiChannels ) {

      this.name = name;
      this.numBands = numBands;
      this.loBandWidth = loBandWidth;
      this.loChannels = loChannels;
      this.hiBandWidth = hiBandWidth;
      this.hiChannels = hiChannels;
    }

    public String toString() {
      return name;
    }
  }*/
}


