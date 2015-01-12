/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package orac.util;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Various utility methods
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class OracUtilities {
    public static String secsToHHMMSS(double seconds) {
        int integerPart = (int) seconds;
        double fraction = seconds - integerPart;
        int remainder = (int) seconds;

        int hh = remainder / 3600;
        remainder -= (hh * 3600);

        int mm = remainder / 60;
        remainder -= (mm * 60);

        String hhStr = "" + hh;

        if (hh < 10) {
            hhStr = "0" + hhStr;
        }

        String mmStr = "" + mm;

        if (mm < 10) {
            mmStr = "0" + mmStr;
        }

        String remainderStr = "" + (remainder + fraction);

        if (remainder < 10) {
            remainderStr = "0" + remainderStr;
        }

        return hhStr + ":" + mmStr + ":" + remainderStr;
    }

    public static String secsToHHMMSS(double seconds, int decimalPlaces) {
        String fullString = secsToHHMMSS(seconds);

        if (fullString.lastIndexOf('.') < 0) {
            fullString += ".0";
        }

        String result = fullString.substring(0, fullString.lastIndexOf('.'));
        String decimalPlacesStr = fullString.substring(
                fullString.lastIndexOf('.'));

        if (decimalPlaces < 1) {
            return result;
        }

        int i;

        for (i = 0; i < decimalPlacesStr.length(); i++) {
            if (i > decimalPlaces) {
                break;
            }

            result += decimalPlacesStr.charAt(i);
        }

        for (; i < decimalPlaces; i++) {
            result += "0";
        }

        return result;
    }

    /**
     * Converts java.util.Date to date string (ISO8601 format).
     */
    public static String toISO8601(Date date) {
        StringBuffer result = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.YEAR) < 100) {
            result.append("20");
            if (calendar.get(Calendar.YEAR) < 10) {
                result.append("0");
            }
        }
        result.append(calendar.get(Calendar.YEAR));
        result.append("-");

        if ((calendar.get(Calendar.MONTH) + 1) < 10) {
            result.append("0");
        }

        result.append(calendar.get(Calendar.MONTH) + 1);
        result.append("-");

        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            result.append("0");
        }

        result.append(calendar.get(Calendar.DAY_OF_MONTH));
        result.append("T");

        if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
            result.append("0");
        }

        result.append(calendar.get(Calendar.HOUR_OF_DAY));
        result.append(":");

        if (calendar.get(Calendar.MINUTE) < 10) {
            result.append("0");
        }

        result.append(calendar.get(Calendar.MINUTE));
        result.append(":");

        if (calendar.get(Calendar.SECOND) < 10) {
            result.append("0");
        }

        result.append(calendar.get(Calendar.SECOND));

        return result.toString();
    }

    /**
     * Converts date string (ISO8601 format) to java.util.Date.
     */
    public static Date parseISO8601(String dateISO8601) {
        Calendar calendar = Calendar.getInstance();

        // Including ` ;`, `.`, `,`, ` ` among the deliminators causes lenient
        // parsing.
        StringTokenizer stringTokenizer = new StringTokenizer(dateISO8601,
                "-T: ;, ");

        int[] dateIntegers = {
                2000, // default year
                1,    // default month
                1,    // default day
                0,    // default hour
                0,    // default minute
                0,    // default second
        };

        try {
            for (int i = 0; i < dateIntegers.length; i++) {
                if (stringTokenizer.hasMoreTokens()) {
                    dateIntegers[i] = Integer.parseInt(
                            stringTokenizer.nextToken());
                } else {
                    break;
                }
            }

            calendar.set(
                    dateIntegers[0],
                    dateIntegers[1] - 1, // subtract 1 because Calendar.MONTH
                                         // range is 0..1
                    dateIntegers[2],
                    dateIntegers[3],
                    dateIntegers[4],
                    dateIntegers[5]);

            return calendar.getTime();

        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse \""
                    + dateISO8601 + "\": " + e);
        }
    }

    /**
     * Debug method.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(
                    "Usage: OracUtilities <number of seconds (double)>"
                    + " <decimal points>");
            return;
        }

        double s = Double.parseDouble(args[0]);
        int n = Integer.parseInt(args[1]);

        System.out.println("secsToHHMMSS(" + s + ") = " + secsToHHMMSS(s));
        System.out.println("secsToHHMMSS(" + s + ", " + n + ") = "
                + secsToHHMMSS(s, n));
    }
}
