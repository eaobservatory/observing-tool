package orac.jcmt.util;

import java.util.Hashtable;
import orac.util.DrUtil;

/**
 * Noise calculations for SCUBA.
 *
 * This Java class is based on the perl module JCMT::SCUBA which
 * is comprised of code originally used to create the Integration
 * Time Calculator (http://www.jach.hawaii.edu/jcmt_sw/bin/itc.pl). The functions
 * that are supplied can be used to find the NEFD of SCUBA at different 
 * wavelengths and sky transmissions, to find the integration time required in a
 * certain mode to attain a desired noise level, and to perform the inverse 
 * calculation where an attainable noise level may be determined from an 
 * integration time. There is also a function to determine the overhead time for
 * a given integration time.
 *
 * @author     Martin Folger (M.Folger@roe.ac.uk):        Java version
 * @author <br>Edward Chapin (echapin@jach.hawaii.edu):   Perl version and documentation
 * @author <br>Tim Jenness   (t.jenness@jach.hawaii.edu): Perl version and documentation
 */
public class ScubaNoise {

  /** Table which stores constant NEFD values for different filters. */
  public static final Hashtable nefd_table = new Hashtable();

  static {
    nefd_table.put("2000", new Double(120.0));
    nefd_table.put("1350", new Double( 60.0));
    nefd_table.put("1100", new Double( 60.0));
  }


  /**
   * Find the NEFD of SCUBA at a certain transmission and wavelength.
   *
   * @param wavelength Wavelength in microns, which can be '350', '450',
   *                   '750', '850', '1350' or '2000'.
   * @param trans Sky transmission coefficient at the given wavelength.
                  This may be derived using JCMT::Tau::transmission from sky opacity
		  and airmass.<br>
   *              Note: Only the average NEFD is known for the photometry pixels at 1350 and 
   *              2000 microns, so the transmission will have no effect on the values at these 
   *              wavelengths.
   *
   * @return NEFD coefficient at the indicated wavelength.
   */
  public static double scunefd(double wavelength, double trans, int [] status) {

    // Some of the fits use a 10th order polynomial and some use a simple
    // power law.

    //Hashtable USEPOWER = new Hashtable();
    Hashtable POWERLAW = new Hashtable();
    
    // Lists whether we have a power law version of the fit
    //USEPOWER.put( "850", new Boolean(true));
    //USEPOWER.put( "450", new Boolean(true));
    //USEPOWER.put( "350", new Boolean(false));
    //USEPOWER.put( "750", new Boolean(false));
    //USEPOWER.put("2000", new Boolean(false));
    //USEPOWER.put("1350", new Boolean(false));

    // Store these as parameters of a simple power law  NEFD = a x^b
    POWERLAW.put("850", new double[]{ 62.13036,  -1.19450 }); // wideband
    POWERLAW.put("450", new double[]{ 265.22623, -0.88198 }); // wideband


    // The coefficients stuff needs to be hacked to make it a bit more
    // obvious -- should probably convert them all to power laws
    // Store the coefficients of the 10th order fits for the NEFD vs transmission
    // curves. Fit is good on the interval (0.042,0.641) for 450 microns,
    // (0.038,0.937) for 850 microns, (0.038,0.437) for 350 microns, (0.038,0.875)
    // for 750 microns. Each curve has two fits - one for the curvey part at the
    // beginning, and another for the straight part later. This was introduced to
    // reduce the waviness caused later by the initial curviness. How scientific.

    // 450: < .25
    double [] COEFF450_1 = {31481.3782, -1443452.4, 37587422.2, -622243777, 
		    6.89288759e+09, -5.22809174e+10, 2.7230432e+11, 
		    -9.57251741e+11, 2.16914012e+12, -2.85909821e+12,
		    1.66471902e+12};

    // 450: >= .25
    double [] COEFF450_2 = {1398.42405, 47297.8604, -687552.05, 4106666.55, 
		    -13058035.6, 20679439.3, -2303458.01, -52848760.7, 
		    96780620.5, -75797136.7, 23242534};

    // # 850: < .4
    double [] COEFF850_1 = {12437.1852, -460404.695, 9321936.98, -116452272, 949460006,
		    -5.19307203e+09, 1.91830323e+10, -4.71851636e+10,
		    7.3996495e+10, -6.68951861e+10, 2.65178399e+10};

    // 850: >= .4
    double [] COEFF850_2 = {1916.91829, -12209.0652, 39905.7003, -72395.673, 
		    58575.4075, 24271.7059, -93325.9314, 64243.6412, 
		    5940.05505, -25228.7225, 8364.3555};

    // 350: < .2
    double [] COEFF350_1 = {48781.2086, -2257045.6, 57372483.6, -876500659, 
		    8.02746219e+09, -3.79248127e+10, -2.99837363e+09,
		    1.1429173e+12, -6.59462154e+12, 1.67617625e+13,
		    -1.68313616e+13};

    // 350: >= .2
    double [] COEFF350_2 = {4098.53825, 44223.7807, -878256.106, 5201060.54, 
		    -12535618.9, 1480625.25, 35235905.7, 50099615, 
		    -423756300, 705809349, -393468673};

    // 750: < .4
    double [] COEFF750_1 = {16767.0584, -630783.391, 13015863.5, -166075120, 
		    1.38549852e+09, -7.76512034e+09, 2.94254867e+10,
		    -7.43149661e+10, 1.1974169e+11, -1.11282628e+11, 
		    4.53684768e+10};

    // 750: >= .4
    double [] COEFF750_2 = {2324.86396, -12506.7929, 27839.396, -6118.9046, 
		    -97065.4346, 168324.253, 22767.7548, -398552.827, 
		    533122.588, -310784.71, 70736.76};

    // Calculate the NEFD as a function of transmission. First parameter is 
    // wavelength (450, 850, 350 or 750 microns) and second parameter is sky 
    // transmission coeff. 
/*
  my ($i,$val,$thisarray);
  my $status = 0;
*/
    double val;
    double [] thisarray = null;
    status[0] = DrUtil.STATUS_SUCCESSFUL;
    
    int wavelengthInt = (int)wavelength;

    // Handle constant NEFD cases
    if (nefd_table.containsKey("" + wavelengthInt)) {
      return ((Double)nefd_table.get("" + wavelengthInt)).doubleValue();
    }

    // See if transmission is reasonable (i.e. between 0 and 1)
    if(trans < 0.0) {
      status[0] = DrUtil.STATUS_INVALID_PARAMETERS;
      return 0.0;
    }


    // Decide which coefficients to use

    switch(wavelengthInt) {
      case 450:
        if (trans < .25) { thisarray = COEFF450_1; }
        else             { thisarray = COEFF450_2; }

        if (POWERLAW.containsKey("" + wavelengthInt)) {
	  thisarray = (double[])POWERLAW.get("" + wavelengthInt);
        }

        // See if transmission on interval of fit

        if ( trans<.042 || trans>.641 ) {
	  status[0] = DrUtil.STATUS_OUT_OF_RANGE_OF_FIT;
        }

      //last SWITCH;
      break;

      case 850:
      
        if (trans < .4) {	thisarray = COEFF850_1; }
        else { thisarray = COEFF850_2; }

        if (POWERLAW.containsKey("" + wavelengthInt)) {
	  thisarray = (double[])POWERLAW.get("" + wavelengthInt);
        }

        // See if transmission on interval of fit

        if ( trans<.038 || trans>.937 ) {
	  status[0] = DrUtil.STATUS_OUT_OF_RANGE_OF_FIT;
        }

        //last SWITCH;
	break;

      case 350:

        if (trans < .2) {	thisarray = COEFF350_1; }
        else { thisarray = COEFF350_2; }

        if (POWERLAW.containsKey("" + wavelengthInt)) {
	  thisarray = (double[])POWERLAW.get("" + wavelengthInt);
        }

        // See if transmission on interval of fit

        if ( trans<.038 || trans>.437 ) {
	  status[0] = DrUtil.STATUS_OUT_OF_RANGE_OF_FIT;
        }

        //last SWITCH;
	break;

      case 750:

        if (trans < .4) {	thisarray = COEFF750_1; }
        else { thisarray = COEFF750_2; }

        if (POWERLAW.containsKey("" + wavelengthInt)) {
	  thisarray = (double[])POWERLAW.get("" + wavelengthInt);
        }

        // See if transmission on interval of fit

        if ( trans<.038 || trans>.875 ) {
	  status[0] = DrUtil.STATUS_OUT_OF_RANGE_OF_FIT;
        }

        //last SWITCH;
	break;

      default:
        status[0] = DrUtil.STATUS_INVALID_PARAMETERS;
        //throw new ScubaNoiseException("Invalid wavelength", ScubaNoiseException.ERROR_INVALID_PARAMETERS);
    }

    // now evaluate the power series or the powerlaw

    if (POWERLAW.containsKey("" + wavelengthInt)) {
      val = thisarray[0] * (exp(trans,thisarray[1]));
    } else {
      val = 0;
      for (int i=0; i<=10; i++) {
        val += thisarray[i]*exp(trans,i);
      }
    }

    return val;
  }

  public static double scunefd(double wavelength, double airmass, double csoTau, int [] status) {
    double tau   = csoTau2Tau(wavelength, csoTau, status);
    if(status[0] != DrUtil.STATUS_SUCCESSFUL) {
      return 0.0;
    }

    double trans = DrUtil.transmission(airmass, tau, status);
    if(status[0] != DrUtil.STATUS_SUCCESSFUL) {
      return 0.0;
    }

    return scunefd(wavelength, trans, status);
  }

  /** Find the ammount of overhead in seconds for an observation when you know the
   *  number of integrations.

  ($overhead,$status) = overhead($integrations,$mode);

=head2 Parameters

=over

=item 1.

The first parameter is the number of integrations. 

=item 2.

The second parameter is the observation mode, which may be one of the 
following:

  'phot'  = photometry
  'jig16' = 16 point jiggle map
  'jig64' = 64 point jiggle map

Presently scan mapping is not supported by this function because we do not 
know how to properly estimate the overhead, and also it is difficult to attach
meaning to 'integrations' in this mode. It should be noted however that a 
rough estimate for overhead in scan mapping is 50% of the integration time.

Similarly, polarimetry is not supported. A rough guess is 50% overhead.

=back

=head2 Return Values

overhead returns a 2-element list. The first element is the over head in 
number of seconds. The second is a scalar containing the exit status of the 
function:

  status = 0: successful
          -1: failure due to invalid parameters

=cut

#------------------------------------------------------------------------------
  */
//  public static double overhead(int integrations, String mode, int [] status) {
//    throw new RuntimeException("The method overhead() is not implented.");
/*
  my $integration = $_[0];
  my $mode = uc $_[1];
  my $overhead;

  # want a positive number of integrations

  if ($integration <= 0) {
    return (0,-1);
  }

  CASE: {
    
    # --- First Case: Photometry ---

    if ($mode eq 'PHOT') {

      # note: number of exposures = number of integrations
      
      my $switch = $integration*2;
     
      $overhead = 40 + $switch*2.5 + $integration*3;
     
      last CASE;
    }
   
    # --- Second Case: 64 Pt. Jiggle ---
   
    if ($mode eq 'JIG64') {
     
      my $exposure = $integration*4;
      my $switch = $exposure*2;
      
      $overhead = 40 + $switch*3 + $exposure*3;
      
      last CASE;
    }
    
    # --- Third Case: 16 Pt. Jiggle ---
    
    if ($mode eq 'JIG16') {
      
      # note: number of exposures = number of integrations
      
      my $switch = $integration*2;
      
      $overhead = 40 + $switch*3 + $integration*3;
      
      last CASE;
    }
    
    # Bad exit status if none of the instruments matched
    
    return (0,-1);
  }
 
  # Good exit status

  return ($overhead,0);
*/
//}

/** Noise Level calculation.
 *
 * Calculates the kind of noise level you can achieve with a certain integration time.
 * <p>
 * The first case is for jiggle mapping, photometry and polarimetry. The second 
 * is for scan mapping, where map dimensions must be specified. 

  ($noise,$status) = noise_level($int,$filter,$mode,$nefd);

    or

  ($noise,$status) = 
    noise_level($int,$filter,$mode,$nefd,$length,$width);


=head2 Parameters

=over

=item 1.

The first parameter is the integration time. 

=item 2.

The second parameter is the filter wavelength in microns, which is one of 
'350','450','750','850','1350' or '2000'

=item 3.

The third parameter is the observation mode, which may be one of the following:

  'phot'  = photometry
  'jig16' = 16 point jiggle map
  'jig64' = 64 point jiggle map
  'scan'  = scan mapping 
  'pol'   = polarimetry

Scan mapping requires additional parameters. See 5th and 6th parameters.

=item 4.

The fourth parameter is the NEFD of SCUBA for a given atmospheric transmission
and filter wavelength. (see the function JCMT::SCUBA::scunefd).

=item 5. & 6.

If scan mapping, the last two parameters are the length and width of the map in
in arcseconds. Length in this case means in the direction of the scan.

=back

=head2 Return Values

noise_level returns a 2-element list. The first element is the noise level in
mJy. The second is a scalar containing the exit status of the function:

  status = 0: successful
          -1: failure due to invalid parameters
          -2: incorrect number of parameters

=cut

#------------------------------------------------------------------------------
*/
  public static double noise_level(double integrations, double wavelength, String mode, double nefd, int [] status) {
    
    if (mode.equalsIgnoreCase("SCAN")) {
      status[0] = DrUtil.STATUS_INCORRECT_ARGUMENT_LIST;
      return 0.0;
    }
    else {
      // If mode is not SCAN then the length and width parameters will be ignored in
      // the following call.
      return noise_level(integrations, wavelength, mode, nefd, status, 0.0, 0.0);
    }
  }


  public static double noise_level(double integrations, double wavelength, String mode, double nefd,
                                   int [] status, double length, double width) {

      // This is really the integration time per beam for phot and jiggle
      // For scan map it is a bit more complicated
      double integrationTime = integrations;
/*
  my $int = $_[0];
  my $filter = $_[1];
  my $mode = uc $_[2];
  my $nefd = $_[3];
  my ($length,$width,$status,$noise);
*/
    // Need map dimensions if scan mapping

    if (mode.equalsIgnoreCase("SCAN")) {

      // now check dimensions

      if (length <= 0 || width <= 0) {
        status[0] = DrUtil.STATUS_INVALID_PARAMETERS;
        return 0.0;
      }
    }

    // Make sure we have positive integration time

    if(integrations <= 0) {
      status[0] = DrUtil.STATUS_INVALID_PARAMETERS;
      return 0.0;
    }
  
    // Check NEFD value

    if(nefd <= 0) {
      status[0] = DrUtil.STATUS_INVALID_PARAMETERS;
      return 0.0;
    }

    // First need to translate the mode and integrations number
    // into an actual time.
    if (mode.equalsIgnoreCase("PHOT")) {
	// Assume a 9pt jiggle pattern
	integrationTime = 18.0 * integrations;
    } else if (mode.equalsIgnoreCase("JIG16")) {
	integrationTime = 32 * integrations;
    } else if (mode.equalsIgnoreCase("JIG64")) {
	integrationTime = 128 * integrations;
    }


    // Basic noise calculation

    double noise = nefd / StrictMath.sqrt(integrationTime);

    // Now we have special factors for different modes:

    // --- 1st Case ---  Photometry (no factor)

    if (mode.equalsIgnoreCase("PHOT")) {
      // do nothing
    }
    else {	    
      // --- 2nd Case ---  Jiggle Mapping (factor of 4)
    
      if (mode.equalsIgnoreCase("JIG16") || mode.equalsIgnoreCase("JIG64")) {
        noise *= 4;
      }
      else {
    
        // --- 3rd Case --- Scan Map 
    
        if (mode.equalsIgnoreCase("SCAN")) {
      
          noise *= StrictMath.sqrt((138.0+length)*width/9.0);
      
          if ((wavelength == 350) || (wavelength == 450)) {
	    noise /= StrictMath.sqrt(91);
          }
          else {
            if ((wavelength == 750) || (wavelength == 850)) {
	      noise /= StrictMath.sqrt(37.0*4.0);
	    }
          }
        }
	else {

          // --- 4th Case --- Polarimetry 
	
          if (mode.equalsIgnoreCase("POL")) {
            noise *= StrictMath.sqrt(18.0);
          }
	  else {

            // --- if no match bad exit status ---

            status[0] = DrUtil.STATUS_INVALID_PARAMETERS;
            return 0.0;
	  }
	}
      }
    }
    
    status[0] = DrUtil.STATUS_SUCCESSFUL;
    return noise;
  }
/*
  public static double noise_level(double integrations, double wavelength, String mode,
                                   double dec, double latitude, double csoTau,
                                   int [] status) {

    double tau   = csoTau2Tau(wavelength, csoTau, status);
    if(status[0] != DrUtil.STATUS_SUCCESSFUL) {
      return 0.0;
    }

    double trans = DrUtil.transmission(airmass(dec, latitude), tau, status);
    if(status[0] != DrUtil.STATUS_SUCCESSFUL) {
      return 0.0;
    }

    double nefd  = scunefd(wavelength, trans, status);
    if(status[0] != DrUtil.STATUS_SUCCESSFUL) { // && (status[0] != DrUtil.STATUS_OUT_OF_RANGE_OF_FIT)) {
      return 0.0;
    }

    return noise_level(integrations, wavelength, mode, nefd, status, 0.0, 0.0);
  }

  public static double noise_level(double integrations, double wavelength, String mode,
                                   double dec, double latitude, double csoTau,
                                   int [] status, double length, double width) {

    double tau   = csoTau2Tau(wavelength, csoTau, status);
    if(status[0] != DrUtil.STATUS_SUCCESSFUL) {
      return 0.0;
    }

    double trans = DrUtil.transmission(airmass(dec, latitude), tau, status);
    if(status[0] != DrUtil.STATUS_SUCCESSFUL) {
      return 0.0;
    }

    double nefd  = scunefd(wavelength, trans, status);
    if(status[0] != DrUtil.STATUS_SUCCESSFUL) { // && (status[0] != DrUtil.STATUS_OUT_OF_RANGE_OF_FIT)) {
      return 0.0;
    }

    return noise_level(integrations, wavelength, mode, nefd, status, length, width);
  }
*/
/* #------------------------------------------------------------------------------ */


/*
#------------------------------------------------------------------------------
# End of PERL code and documentation footer.
#------------------------------------------------------------------------------
1;

=head1 NOTES

Although limited types of mapping may be performed with the photometry
pixels at 1350 and 2000 microns, this module will only support the
'phot' and 'pol' modes at these wavelengths at present.

It is likely that the transmission of the atmosphere is not known at any
given time. Usually CSO Tau is known however, so to derive the transmission
see JCMT::Tau::get_tau and JCMT::Tau::transmission.

Whenever 'noise' is referred to, it is the RMS noise level, which is normally
the (source-flux)/(sigma). In the case of polarimetry, is the polarised RMS 
noise level which includes the polarisation factor: 
(polarisation)(source-flux)/(sigma).

=head1 AUTHOR

Module created by Edward Chapin, echapin@jach.hawaii.edu
(with help from Tim Jenness, timj@jach.hawaii.edu)

=cut
*/

  public static double exp(double base, double x) {
    return StrictMath.exp(x * StrictMath.log(base));
  }

  public static void main(String [] args) {
    try {
      int    integrations = Integer.parseInt(args[0]);
      String mode         = args[1];
      double wavelength   = Double.parseDouble(args[2]);
      double csoTau          = Double.parseDouble(args[3]);
      double airmass      = Double.parseDouble(args[4]);

      double length       = 0.0;
      double width        = 0.0;

      if(args.length >= 8) {
        length            = Double.parseDouble(args[5]);
        width             = Double.parseDouble(args[6]);
      }
      
//my $int          = shift;
//my $mode         = uc shift;
//my $wavelength   = shift;
//my $tau          = shift;
//my $airmass      = shift;

      System.out.println("Using:");
      System.out.println("  int          = " + integrations);
      System.out.println("  mode         = " + mode        );
      System.out.println("  wavelength   = " + wavelength  );
      System.out.println("  csoTau       = " + csoTau         );
      System.out.println("  airmass      = " + airmass     );
      System.out.println("  length       = " + length      );
      System.out.println("  width        = " + width       );

      System.out.println("Calculated:");

      int [] status = { 0 };
      double trans = 0.0;
      double nefd  = 0.0;
      double noise = 0.0;
      // Get transmission

      trans = DrUtil.transmission(airmass, csoTau, status);
      System.out.println("  transmission = " + trans + "\t\t(status " + status[0] + ")");

      // Get nefd
      nefd = scunefd(wavelength, trans, status);
      System.out.println("  nefd         = " + nefd + "\t\t(status " + status[0] + ")");

      // Get noise
      noise = noise_level(integrations, wavelength, mode, nefd, status, length, width);
      System.out.println("  noise        = " + noise + "\t\t(status " + status[0] +  ")");
    }
    catch(Exception e) {
      e.printStackTrace();
      System.out.println("Usage: ScubaNoise integrations mode wavelength csoTau airmass [length width]"); 
    }
  }


  /**
   * Calculates tau for a certain wavelength from cso tau.
   *
   * This class is based on the get_tau(desiredTau,sourceTau,tauValue) in JCMT::Tau.
   * <p>
   * It is often the case that the zenith sky opacity at 450 or 850 microns is 
   * unknown, but the opacity at 225 GHz is available from the Caltech Submillimeter
   * Observatory (http://puuoo.caltech.edu/index.html). The empirical relationships 
   * between 450 Tau and 850 Tau with CSO Tau have been derived from past skydips at
   * the JCMT. Similar relations have also been derived for 350 and 750 microns, 
   * although there is currently very little data to support them. This module 
   * presently contains two functions: get_tau for retrieving sky opacity at 450, 
   * 850, 350 and 750  microns and transmission for calculating the atmospheric 
   * transmission coefficient at a given airmass and sky opacity.
   *
   * @param  wavelength Allowed values: 450, 850, 350, 750
   * @param  csoTau     cso tau.
   * @param  status     Method exit status.
   *
   * @return tau for specified wavelength and csoTau
   */
  public static double csoTau2Tau(double wavelength, double csoTau, int [] status) {

    // The actual coefficients of the relation y = a(x + b) where x and y are zenith
    // sky opacities at different wavelengths are stored in a hash which is imported
    // when the module is used, called %Tau_Relation. The keys are of the form 'x:y',
    // and each element of the hash is an array containing a and b. For instance:
    // 
    //   'CSO:450' => [25, -.011] 
    // 
    // where y=450, x=CSO, a=25, b=-.011, and 'CSO:450' is a key for %Tau_Relation
    // ---------------------------------------------------------------------------
    // First define a hash which contains the current best guesses for each
    // relation. Each key is of the form x:y where x and y are opacity values in
    // the relation y = a(x + b), and each element of the hash is an array
    // containing a and b.
    // The reverse relationships are calculated immediately afterwards
    Hashtable tauRelation = new Hashtable();
    tauRelation.put( "450", new double[]{26.2,  -0.014});  // wideband filters
    tauRelation.put( "850", new double[]{ 4.02, -0.001});  // wideband filter
    tauRelation.put( "350", new double[]{28,    -0.012});
    tauRelation.put( "750", new double[]{ 9.3,  -0.007});
    tauRelation.put("1100", new double[]{ 1.4,   0.0 });
    tauRelation.put("1350", new double[]{ 1.4,   0.0 });
    tauRelation.put("2000", new double[]{ 0.9,   0.0 });


    //  # Check to see if arg 3 is defined and is a number
    //  # First see if source Tau value is reasonable:
    //  unless ( defined $_[2] && number($_[2]) && $_[2]>=0) {
    //    return (0,-1);
    //  }
    if(csoTau < 0.0) {
      status[0] = DrUtil.STATUS_INVALID_PARAMETERS;
      return 0.0;
    }


    //  # If good parameters, find the return value of tau
    //
    //  if ( exists $Tau_Relation{$name} ) {
    //    return $Tau_Relation{$name}[0]*($_[2] + $Tau_Relation{$name}[1]),0;
    //  }
    if(tauRelation.containsKey("" + ((int)wavelength))) {
      status[0] = DrUtil.STATUS_SUCCESSFUL;

      double [] coefficients = (double[])tauRelation.get("" + ((int)wavelength));

      return coefficients[0] * (csoTau + coefficients[1]);
    }


    //  # If we haven't returned a good value yet, the parameters are bad
    //  # so return -1 status.
    //  return (0,-1);
    status[0] = DrUtil.STATUS_INVALID_PARAMETERS;
    return 0.0;
  }
}

