package om.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Utility class for getting the current date in UT format (YYYYMMDD).
 * 
 * @author M.Folger@roe.ac.uk
 */
public class UT {

  /**
   * Returns the current date in UT format (YYYYMMDD).
   */
  public static String getUT() {
    //Calendar calendar = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    DateFormat dateFormatter = DateFormat.getTimeInstance(DateFormat.MEDIUM);
    System.out.println("TIME: "+dateFormatter.format(calendar.getTime()));

    String dayString = null;
    if(calendar.get(Calendar.DAY_OF_MONTH) < 10) {
      dayString = "0" + calendar.get(Calendar.DAY_OF_MONTH);
    }
    else {
      dayString = ""  + calendar.get(Calendar.DAY_OF_MONTH);
    }

    String monthString = null;
    if((calendar.get(Calendar.MONTH)+1) < 10) {
      monthString = "0" + (calendar.get(Calendar.MONTH)+1);
    }
    else {
      monthString = ""  + (calendar.get(Calendar.MONTH)+1);
    }

    return "" + calendar.get(Calendar.YEAR) + monthString + dayString;
  }

  /**
   * Prints the current date in UT format (YYYYMMDD) to stdout.
   */
  public static void main(String [] args) {
    System.out.println(getUT());
  }
}
