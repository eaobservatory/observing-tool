package orac.jcmt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.String;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import orac.jcmt.iter.SpIterJCMTObs;
import orac.jcmt.iter.SpIterRasterObs;
import orac.jcmt.inst.SpInstHeterodyne;

/**
 * Class for caalculation of the system noise of the heterodyne recievers.
 * This class is based on the perl model JCMT::HITEC which is comprised of
 * code used to create the integration time calculator 
 */
public class HeterodyneNoise {
    static Vector feNames   = new Vector();
    static Vector trxValues = new Vector();
    static Vector nu_tel    = new Vector();
    static HashMap dasModes = new HashMap();

    static double kappa = 1.15;
    private static final String receiverFile = "receiver.info";


    private static void init() {
	String inputLine;

	if (feNames.size() != 0) return;

	// Initialise the DAS modes -  this is needed sine at the moment the OT
	// occassionally reports the wrong overlap.  Pity since if ity didn't it would
	// save some space
	dasModes.put(new Integer(125), new Double (95e3/2.) );
	dasModes.put(new Integer(250), new Double (189e3/2.) );
	dasModes.put(new Integer(500), new Double (378e3/2.) );
	dasModes.put(new Integer(760), new Double (756e3/2.) );
	dasModes.put(new Integer(920), new Double (756e3/2.) );
	dasModes.put(new Integer(1840), new Double (1513e3/2.) );

	// Read the reciever file - first get its directory and add the file name
	String fileName = System.getProperty("ot.cfgdir");
	fileName = fileName + receiverFile;
	// Open the file ready for reading
	File rvrFile = new File (fileName);
	try {
	    BufferedReader in = new BufferedReader( new FileReader(rvrFile) );
	    // Read the fron end names
	    while ( (inputLine = in.readLine()) != null) {
		if (inputLine.equals("") ) break;
		feNames.add(inputLine);
	    }
	    // Now loop through the rest of the file and get the receiver temperature
	    int index;
	    while ( (inputLine = in.readLine()) != null) {
		if ( (index = feNames.indexOf(inputLine)) != -1) {
		    // Start reading the data
		    TreeMap currentMap = new TreeMap();
		    int nLines = Integer.valueOf(in.readLine()).intValue();
		    for (int i=0; i<nLines; i++) {
			String values = in.readLine();
			StringTokenizer st = new StringTokenizer(values);
			Integer frequency = new Integer(st.nextToken());
			Integer trx = new Integer(st.nextToken());
			currentMap.put(frequency, trx);
		    }
		    trxValues.add(index, currentMap);
		    Double nu = new Double(in.readLine());
		    nu_tel.add(index, nu);
		}
	    }
	}
	catch (Exception e) {
	    System.out.println("Error reading receiver info file");
	    e.printStackTrace();
	}
    }



    public static double getTrx(String fe, double freq) {
	double trx = 0.0;
	int index;

	/* Now see if this is in our available list of front ends */
	if ( (index = feNames.indexOf(fe)) >= 0) {
	    // There is a match, now extract the appropriate tree map
// 	    System.out.println("Front end "+fe+" is at vector location "+index);
// 	    System.out.println("trxValues has "+trxValues.size()+" element");
	    TreeMap thisMap = (TreeMap) trxValues.elementAt(index);
	    int nextTrxFrequency = 0;
	    int lastTrxFrequency = 0;
	    if (thisMap.size() > 1 ) {
		// We are going to do a linear interpolation between the 
		// surrounding frequency values, so we need to find the 
		// values at the appropriate keys
		Iterator iter = ((Set) thisMap.keySet()).iterator();
		while (iter.hasNext()) {
		    int trxFrequency = ((Integer)iter.next()).intValue();
		    lastTrxFrequency = nextTrxFrequency;
		    nextTrxFrequency = trxFrequency;
		    if (freq < nextTrxFrequency && freq >= lastTrxFrequency) break; // We have the info we need
		}
		// Now get the corresponding Trx values
		int lastTrxValue = ( (Integer)thisMap.get(new Integer(lastTrxFrequency)) ).intValue();
		int nextTrxValue = ( (Integer)thisMap.get(new Integer(nextTrxFrequency)) ).intValue();
		// Now interpolate...
		double slope = (double)(nextTrxValue - lastTrxValue)/(double)(nextTrxFrequency - lastTrxFrequency);
		trx = slope*freq + lastTrxValue - slope*lastTrxFrequency;
	    }
	    else {
		trx = ( (Integer)thisMap.get(thisMap.firstKey()) ).intValue();
	    }
	}
// 	System.out.println("TRX = "+trx);
	return trx;
    }

    public static double getTsys(String fe,        // The front end name
			  double tau,       // The noise calculation tau
			  double airmass,   // The airmass to use
			  double freq,      // Required frequency
			  boolean ssb)      // Whether or not we are using SSB of DSB
    {
	int index;
	double nuTel = 0.0;
	double tSys = 0.0;

	// Get the transmission curve curve
	TreeMap atmTau = getAtmosphereData(tau);

	if ( (index = feNames.indexOf(fe)) != -1) {
	    nuTel = ( (Double)nu_tel.elementAt(index) ).doubleValue();
	}

	// Now do a linear interp across the atmospheric data at the
	// required frequency
	Iterator iter = atmTau.keySet().iterator();
	double lowerF=0.0;
	double upperF=0.0;
	double lowerT=0.0;
	double upperT=0.0;
	while (iter.hasNext()) {
	    lowerF=upperF;
	    upperF= ((Double)iter.next()).doubleValue();
	    if (freq < upperF && freq >= lowerF) break;
	}
	lowerT = ( (Double)atmTau.get(new Double(lowerF)) ).doubleValue();
	upperF = ( (Double)atmTau.get(new Double(upperF)) ).doubleValue();
	double slope = (double)(upperT-lowerT)/(upperF-lowerF);
	double t = slope*freq + lowerT -slope*lowerF;
// 	System.out.println("Estimated tau: "+t);

	// Nowe find Tsys
	double nuSky = Math.exp( -t*airmass );
	double tSky  = 260.0-nuSky*260.0;
	double tTel  = 265.0-nuTel*265.0;
// 	System.out.println("nuSky = "+nuSky);
// 	System.out.println("nuTel = "+nuTel);
	if (ssb) {
	    tSys = (2.0*getTrx(fe, freq) + nuTel*tSky + tTel + 35)/
		(nuSky*nuTel);
	}
	else {
	    tSys = 2*(getTrx(fe, freq) + nuTel*tSky + tTel) / 
		(nuSky*nuTel);
	}
// 	System.out.println("TSYS = "+tSys);
	return tSys;
    }

    private static TreeMap getAtmosphereData (double tau) {
	double [] availableBands = {0.03, 0.065, 0.1, 0.16, 0.2};
	TreeMap tauMap = new TreeMap();
	int index=0;
	
	// Find the tau band we are in
	if (tau > availableBands[availableBands.length - 1]) {
	    index = availableBands.length - 1;
	}
	else if (tau < availableBands[0]) {
	    index = 0;
	}
	else {
	    for (int i=1; i<availableBands.length; i++) {
		if (tau > availableBands[i-1] && tau < availableBands[i]) break;
		index++;
	    }
	}

	// We now have the index so open the file
	String fileName = System.getProperty("ot.cfgdir") + "/tau";
	String tmp = Double.toString(availableBands[index]);
	StringTokenizer st = new StringTokenizer(tmp,".");
	st.nextToken();
	tmp = st.nextToken()+".dat";
	fileName = fileName+tmp;
	File tauFile = new File(fileName);
	try {
	    BufferedReader fileReader = new BufferedReader(new FileReader(tauFile));
	    String inputLine;
	    while ( (inputLine = fileReader.readLine() ) != null) {
		st = new StringTokenizer(inputLine);
		Double frequency = new Double(st.nextToken());
		Double depth     = new Double(st.nextToken());
		tauMap.put(frequency, depth);
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return tauMap;
    }

    private static double getNoise( SpIterJCMTObs    obs,
			     SpInstHeterodyne inst,
			     double tSys ) {

	double time;
	int    noOfRows      = 1;
	int    samplesPerRow = 1;


	if (obs instanceof SpIterRasterObs ) {
	    // Make this the total time to do the map
	    double width      = ((SpIterRasterObs)obs).getWidth();
	    double height     = ((SpIterRasterObs)obs).getHeight();
	    double sampleDx   = ((SpIterRasterObs)obs).getScanDx();
	    double sampleDy   = ((SpIterRasterObs)obs).getScanDy();
	    double sampleTime = ((SpIterRasterObs)obs).getSampleTime();
	    samplesPerRow = (int)(Math.ceil(width/sampleDx));
	    if (samplesPerRow%2 == 0) samplesPerRow++;
	    noOfRows = (int) Math.ceil( height/sampleDy );
	    double timeOnRow  = (double)samplesPerRow * sampleTime;
	    double timeOffRow = Math.sqrt((double)samplesPerRow) * sampleTime;
	    time = (timeOnRow + timeOffRow) * noOfRows;
// 	    System.out.println("Calculating RASTER time based on:");
// 	    System.out.println("\tWidth       : "+width);
// 	    System.out.println("\tHeight      : "+height);
// 	    System.out.println("\tdx          : "+sampleDx);
// 	    System.out.println("\tdy          : "+sampleDy);
// 	    System.out.println("\tSample Time : "+sampleTime);

	}
	else {
	    time = (double)obs.getSecsPerCycle();
	}

	// Scale by number of obervations
	time = time/(noOfRows*samplesPerRow);

	// Multiply this time by the number of integrations to give a total time
	time = time*obs.getIntegrations();

	// There seems to be a bug in the overlap in the OT, so for now get the bandwidth
	// and use the overlap values fro, Hitec
// 	double overlap = inst.getOverlap(0);
	int bandwidth = (int) (inst.getBandWidth(0)/1.e6);
	double overlap = ( (Double)dasModes.get(new Integer(bandwidth))).doubleValue();
	
	
	// Multiply by the number of mixers
	if (inst.getMixer().startsWith("Dual")) {
	    time = 2.*time;
	    overlap = 2*overlap;
	}
	
	// Handle the different types of obervation...
	if (obs instanceof SpIterRasterObs) {
 	    time = 2.*time/(1.+1./Math.sqrt(samplesPerRow));
	}
	else {
	    // Get the switching mode
	    if (obs.getSwitchingMode().equalsIgnoreCase("Position") ||
		obs.getSwitchingMode().equalsIgnoreCase("Beam")) {
		time = time/2.0;
	    }
	    else if (obs.getSwitchingMode().startsWith("Frequency")) {
	    }
	    else {
	    }
	}
	
	double rmsNoise = Math.sqrt(1./(overlap*time)) * tSys * kappa;
	return rmsNoise;
    }

    public static double getHeterodyneNoise (SpIterJCMTObs    obs, 
					     SpInstHeterodyne instrument,
					     double           noiseCalculationTau, 
					     double           airmass) {
	// Set up all the vectors required
 	init ();

// 	System.out.println("airmass = "+airmass);
	
	// Get the system temperaure
	double tSys = getTsys( instrument.getFrontEnd(),
			       noiseCalculationTau,
			       airmass,
			       (instrument.getRestFrequency(0))/1.0e9,
			       instrument.getMode().equalsIgnoreCase("ssb"));

	//Now calulate the noise based on this
	double noise = getNoise(obs, instrument, tSys);
	return noise;
    }

    public static void main (String [] args) {
    }

}
