package edfreq;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import java.io.File;
import java.io.IOException;

import orac.util.InstCfg;
import orac.util.InstCfgReader;

/**
 * XML serialised instances of this class are used as Frequency Editor configuration files/resources.
 *
 * This class provides static methods for (de)serialisation using the package JSX.
 * If no xml configuration file is available then the default constructor can be used
 * to create a default configuration object (ACSIS setup).
 * <p>
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

  public String[] frontEnds;
  public Hashtable frontEndTable  = new Hashtable();
  public Hashtable frontEndMixers = new Hashtable();
  public String [] velocityFrames = { "LSRK", "Geocentric", "Heliocentric" };
  public boolean centreFrequenciesAdjustable;
  public Hashtable receivers;

  private static FrequencyEditorCfg _frequencyEditorCfg = null;

  /**
   * Constructor used if no xml configuration file is specified or found.
   */
  public FrequencyEditorCfg() {
    frontEnds     = new String[] { "A3", "B3", "WC", "WD", "HARP-B" };

    // Put the default mode (dsb or ssb) first in the array.
    frontEndTable.put("A3",     new String[]{ "dsb" });
    frontEndTable.put("B3",     new String[]{ "ssb", "dsb" });
    frontEndTable.put("WC",     new String[]{ "ssb", "dsb" });
    frontEndTable.put("WD",     new String[]{ "ssb", "dsb" });
    frontEndTable.put("HARP-B", new String[]{ "ssb" });

    frontEndMixers.put("A3",     new String[]{ "Single Mixer" });
    frontEndMixers.put("B3",     new String[]{ "Single Mixer", "Dual Mixer" });
    frontEndMixers.put("WC",     new String[]{ "Single Mixer", "Dual Mixer" });
    frontEndMixers.put("WD",     new String[]{ "Single Mixer", "Dual Mixer" });
    frontEndMixers.put("HARP-B", new String[]{ "Single Mixer" });

    centreFrequenciesAdjustable = true;
    receivers = ReceiverList.getReceiverTable();
  }

  /**
	 * Returns a FrequencyEditorCfg based on the value of the system poperty FREQ_EDITOR_CFG_PROPERTY, which should point ot the cfg file.
	 */
	public static FrequencyEditorCfg getConfiguration()
	{
		if( _frequencyEditorCfg == null )
		{
			String acsisCfgFile = System.getProperty( "ot.cfgdir" ) + File.separator + "acsis.cfg";
			_frequencyEditorCfg = getConfiguration( acsisCfgFile );
		}
    return _frequencyEditorCfg;
  }

	public static FrequencyEditorCfg getConfiguration( String fileName )
	{

		InstCfgReader rdr = new InstCfgReader( fileName );
		InstCfg instInfo = null;
		String block = null;

		String[] myFrontEnds = null;
		Hashtable myReceivers = null;

		if( _frequencyEditorCfg == null )
		{
			_frequencyEditorCfg = new FrequencyEditorCfg();
		}

		try
		{
			while( ( block = rdr.readBlock() ) != null )
			{
				instInfo = new InstCfg( block );
				if( InstCfg.matchAttr( instInfo , "receivers" ) )
				{
					String[][] recList = instInfo.getValueAs2DArray();
					myFrontEnds = new String[ recList.length ];
					myReceivers = new Hashtable( recList.length );
					try
					{
						for( int i = 0 ; i < recList.length ; i++ )
						{
							myFrontEnds[ i ] = recList[ i ][ 0 ];
							double loMin = Double.parseDouble( recList[ i ][ 1 ] );
							double loMax = Double.parseDouble( recList[ i ][ 2 ] );
							double feIF = Double.parseDouble( recList[ i ][ 3 ] );
							double bw = Double.parseDouble( recList[ i ][ 4 ] );
							myReceivers.put( recList[ i ][ 0 ] , new Receiver( myFrontEnds[ i ] , loMin , loMax , feIF , bw ) );
						}
						_frequencyEditorCfg.receivers = myReceivers;
						_frequencyEditorCfg.frontEnds = myFrontEnds;
					}
					catch( Exception e )
					{
						e.printStackTrace();
					}
				}
				else if( InstCfg.matchAttr( instInfo , "modes" ) )
				{
					String[][] modes = instInfo.getValueAs2DArray();
					Hashtable myFrontEndTable = new Hashtable( modes.length );
					for( int i = 0 ; i < modes.length ; i++ )
					{
						myFrontEndTable.put( myFrontEnds[ i ] , modes[ i ] );
					}
					_frequencyEditorCfg.frontEndTable = myFrontEndTable;
				}
				else if( InstCfg.matchAttr( instInfo , "mixers" ) )
				{
					String[][] mixers = instInfo.getValueAs2DArray();
					Hashtable myFrontEndMixers = new Hashtable( mixers.length );
					for( int i = 0 ; i < mixers.length ; i++ )
					{
						myFrontEndMixers.put( myFrontEnds[ i ] , mixers[ i ] );
					}
					_frequencyEditorCfg.frontEndMixers = myFrontEndMixers;
				}
				else if( InstCfg.matchAttr( instInfo , "velocity_frames" ) )
				{
					String[] myVelocityFrames = instInfo.getValueAsArray();
				}
				else if( InstCfg.likeAttr( instInfo , "bandspecs" ) )
				{
					String[][] specs = instInfo.getValueAs2DArray();
					_decodeBandSpecs( instInfo , specs );
				}
			}
		}
		catch( IOException ioe )
		{
			ioe.printStackTrace();
		}
		return _frequencyEditorCfg;
	}

  private static void _decodeBandSpecs(InstCfg instInfo, String [][] specs ) {
      String name = "";
      Vector bsVector = new Vector();

      ArrayList bandwidths  = new ArrayList();
      ArrayList overlaps    = new ArrayList();
      ArrayList channels    = new ArrayList();
      ArrayList hybSubbands = new ArrayList();
      for ( int i=0; i<specs.length; i++ ) {
          // See if we are dealing with the same name of bandspec
          if (name.equals(specs[i][0])) {
              // We are adding to the current arrays lists
              bandwidths.add(specs[i][1]);
              overlaps.add(specs[i][2]);
              channels.add(specs[i][3]);
              hybSubbands.add(specs[i][4]);
          }
          else {
              if (name.equals("")) {
                  // First time through the loop - just add the bits
                  name =specs[i][0]; 
                  bandwidths.add(specs[i][1]);
                  overlaps.add(specs[i][2]);
                  channels.add(specs[i][3]);
                  hybSubbands.add(specs[i][4]);
              }
              else {
                  // Convert the ArrayLists to arrays
                  double [] bwArray = new double [bandwidths.size()];
                  double [] olArray = new double [overlaps.size()];
                  int    [] chArray = new int [channels.size()];
                  int    [] hyArray = new int [hybSubbands.size()];
                  // For simplicity, assume they are all the same size
                  for ( int j=0; j<bwArray.length; j++ ) {
                      bwArray[j] = Double.parseDouble((String)bandwidths.get(j));
                      olArray[j] = Double.parseDouble((String)overlaps.get(j));
                      chArray[j] = Integer.parseInt((String)channels.get(j));
                      hyArray[j] = Integer.parseInt((String)hybSubbands.get(j));
                  }
                  // Create a new BandSpec and add it to the vector for the current receiver
                  BandSpec bs = new BandSpec(name, bandwidths.size(), bwArray, olArray, chArray, hyArray);
                  bsVector.add(bs);

                  // Now reset name and empty array lists, then add the new components
                  name = specs[i][0];
                  bandwidths.clear();
                  overlaps.clear();
                  channels.clear();
                  hybSubbands.clear();

                  bandwidths.add(specs[i][1]);
                  overlaps.add(specs[i][2]);
                  channels.add(specs[i][3]);
                  hybSubbands.add(specs[i][4]);
              }
          }
      }
      // Now add any stragglers
      double [] bwArray = new double [bandwidths.size()];
      double [] olArray = new double [overlaps.size()];
      int    [] chArray = new int [channels.size()];
      int    [] hyArray = new int [hybSubbands.size()];
      for ( int j=0; j<bwArray.length; j++ ) {
          bwArray[j] = Double.parseDouble((String)bandwidths.get(j));
          olArray[j] = Double.parseDouble((String)overlaps.get(j));
          chArray[j] = Integer.parseInt((String)channels.get(j));
          hyArray[j] = Integer.parseInt((String)hybSubbands.get(j));
      }
      // Create a new BandSpec and add it to the vector for the current receiver
      BandSpec bs = new BandSpec(name, bandwidths.size(), bwArray, olArray, chArray, hyArray);
      bsVector.add(bs);

      // Finally link the receiver and bandspecs
      boolean foundReceiver = false;
      for ( int i=0; i<_frequencyEditorCfg.frontEnds.length; i++ ) {
          if ( InstCfg.likeAttr (instInfo, _frequencyEditorCfg.frontEnds[i].replaceAll("[^a-zA-Z0-9]", "")) ) {
              foundReceiver = true;
              ((Receiver)_frequencyEditorCfg.receivers.get(_frequencyEditorCfg.frontEnds[i])).setBandSpecs(bsVector);
              break;
          }
      }
      if (!foundReceiver) {
          System.out.println("Failed to find match for keyword " + instInfo.getKeyword());
      }
  }

  public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("[\n");
      sb.append("\tnames=[");
      for ( int i=0; i<frontEnds.length; i++ ) {
          sb.append(frontEnds[i] + ";");
      }
      sb.append("]\n");
      sb.append("\tfrontEndTable=[" + frontEndTable + "]\n");
      sb.append("\tfrontEndMixers=[" + frontEndMixers + "]\n");
      sb.append("\treceivers=[" + receivers + "]\n");
      sb.append("]");
      return sb.toString();
  }
}


