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
 * <h3>How to create a new xml configuration file</h3>
 *
 * The xml configuration files currently used are <tt>EDFREQ/cfg/edfreq/acsisCfg.xml</tt> and
 * <tt>EDFREQ/cfg/edfreq/dasCfg.xml</tt>.<p>
 *
 * If this FrequencyEditorCfg class or any classes that are used as fields of
 * FrequencyEditorCfg are significanlty changed, i.e. if fields are added or
 * removed from them, then the xml configuration file which is an xml
 * serialisation of an FrequencyEditorCfg object will probably have to be
 * recreated. This can be done using the method
 * {@link #createDefaultCfgFile(java.io.File)} which is used in the main
 * method of FrequencyEditorCfg. So a new xml configuration can be created on
 * the command line using the commands
 *
 * <pre><tt>
 *
 *   cd cvsWorkingDirectory/ot
 *   mv -i EDFREQ/cfg/edfreq/acsisCfg.xml EDFREQ/cfg/edfreq/old_acsisCfg.xml
 *   java -classpath EDFREQ/tools/JSX1.0.1.1.jar:EDFREQ/install/classes edfreq.FrequencyEditorCfg EDFREQ/cfg/edfreq/acsisCfg.xml
 *
 * </tt></pre>
 *
 * If only the values of the fields of FrequencyEditorCfg (or the values of
 * the objects which are themselves fields of FrequencyEditorCfg) have
 * changed. Then the xml configuration file can be editied manually. <b>Make
 * sure you invent appropriate <tt>alias-ID</tt> attributes for each added xml
 * element.</b> I think alias-IDs must not be duplicated. I am not sure
 * whether all alias-ID taken together must form a contiguous. I do not think
 * they have to appear in the right order in the xml configuration file.<p>
 *
 * If no configuration xml file is specified (see {@link #FREQ_EDITOR_CFG_PROPERTY}) or found
 * then the {@link #FrequencyEditorCfg() default constructor} is used.
 *
 * @author Martin Folger
 */
public class FrequencyEditorCfg {

  /**
   * System property containing the location of the xml configuration file
   * as a resource in the classpath.
   */
  public static final String FREQ_EDITOR_CFG_PROPERTY = "FREQ_EDITOR_CFG";

  public Hashtable frontEndTable = new Hashtable();
  public String [] velocityFrames = { "LSR", "Geocentric", "Heliocentric" };
  public boolean centreFrequenciesAdjustable;
  public Hashtable receivers;

  private static FrequencyEditorCfg _frequencyEditorCfg = null;

  /**
   * Constructor used if no xml configuration file is specified or found.
   */
  public FrequencyEditorCfg() {

    // Put the default mode (dsb or ssb) first in the array.
    frontEndTable.put("A3",     new String[]{ "dsb" });
    frontEndTable.put("B3",     new String[]{ "ssb", "dsb" });
    frontEndTable.put("WC",     new String[]{ "ssb", "dsb" });
    frontEndTable.put("WD",     new String[]{ "ssb", "dsb" });
    frontEndTable.put("HARP-B", new String[]{ "ssb" });

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


