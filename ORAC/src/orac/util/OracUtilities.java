/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.util;

/**
 * Various utility methods
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class OracUtilities {
  public static String secsToHHMMSS(double seconds) {
    int integerPart = (int)seconds;
    double fraction = seconds - integerPart;
    int remainder   = (int)seconds;

    int hh = remainder / 3600;
    remainder -= (hh * 3600);

    int mm = remainder / 60;
    remainder -= (mm * 60);

    double secs = remainder / 60;

    String hhStr = "" + hh;
    if(hh < 10) hhStr = "0" + hhStr;

    String mmStr = "" + mm;
    if(mm < 10) mmStr = "0" + mmStr;

    String remainderStr = "" + (remainder + fraction);
    if(remainder < 10) remainderStr = "0" + remainderStr;

    return hhStr + ":" + mmStr + ":" + remainderStr;
  }

  public static String secsToHHMMSS(double seconds, int decimalPlaces) {
    String fullString       = secsToHHMMSS(seconds);
    String result           = fullString.substring(0, fullString.lastIndexOf('.'));
    String decimalPlacesStr = fullString.substring(fullString.lastIndexOf('.'));

    if(decimalPlaces < 1) {
      return result;
    }

    int i;
    for(i = 0; i < decimalPlacesStr.length(); i++) {
      if(i > decimalPlaces) {
        break;
      }

      result += decimalPlacesStr.charAt(i);
    }

    for(; i < decimalPlaces; i++) {
      result += "0";
    }

    return result;
  }

  /**
   * Debug method.
   */
  public static void main(String [] args) {
    if(args.length != 2) {
      System.out.println("Usage: OracUtilities <number of seconds (double)> <decimal points>");
      return;
    }

    double s = Double.parseDouble(args[0]);
    int    n = Integer.parseInt(args[1]);

    System.out.println("secsToHHMMSS(" + s + ") = " + secsToHHMMSS(s));
    System.out.println("secsToHHMMSS(" + s + ", " + n + ") = " + secsToHHMMSS(s, n));
  }
}

