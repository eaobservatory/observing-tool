/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.util;

/**
 * Simple utility to convert special characters.
 *
 * Converts
 * <pre>
 * '&lt;' to and from "&amp;lt;"    <!-- '<' to and from "&lt;" -->
 * '&gt;' to and from "&amp;gt;"    <!-- '>' to and from "&gt;" -->
 * '&amp;' to and from "&amp;amp;"  <!-- '&' to and from "&amp;" -->
 * </pre>
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class XmlUtil  {

  public static String asciiToXml(String ascii) {
    StringBuffer result = new StringBuffer();

    for(int i = 0; i < ascii.length(); i++) {
      switch(ascii.charAt(i)) {
        case '<': result.append("&lt;");  break;
        case '>': result.append("&gt;");  break;
        case '&': result.append("&amp;"); break;
        default:  result.append(ascii.charAt(i));
      }
    }

    return result.toString();
  }

  /**
   * This method is not needed when an XML parser is used.
   *
   * The parser will perform the necessary conversion.
   */
  public static String xmlToAscii(String xml) {
    StringBuffer result = new StringBuffer();

    for(int i = 0; i < xml.length(); i++) {
      if(xml.charAt(i) == '&') {
        if(xml.substring(i).startsWith("&lt;")) {
          result.append("<");
	  i += 3;
	}

        if(xml.substring(i).startsWith("&gt;")) {
          result.append(">");
	  i += 3;
	}

        if(xml.substring(i).startsWith("&amp;")) {
          result.append("&");
	  i += 4;
	}
      }
      else {
        result.append(xml.charAt(i));
      }
    }

    return result.toString();
  }

  /** Test and debug method. */
  public static void main(String [] args) {
    if(args.length > 1) {
      if(args[0].equals("-toXml")) {
        System.out.println(asciiToXml(args[1]));
	return;
      }
      
      if(args[0].equals("-toAscii")) {
        System.out.println(xmlToAscii(args[1]));
	return;
      }
    }

    System.out.println("Usage: orac.util.XmlUtil (-toXml | toAscii) \"string\"");
  }

}
