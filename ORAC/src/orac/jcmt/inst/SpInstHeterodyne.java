/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.jcmt.inst;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

/**
 * The Heterodyne instrument Observation Component.
 *
 * Note that ot.jcmt.inst.editor.EdCompInstHeterodyne manipulates the _avTable of the
 * SpInstHeterodyne item directly (via getTable()). That's why SpInstHeterodyne
 * does not have any proper functionality of its own at the moment. This might
 * change in the future.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public final class SpInstHeterodyne extends SpJCMTInstObsComp {

  public static String [] JIGGLE_PATTERNS = { "5 Point", "Jiggle", "Rotation" };

  /** This attribute records the entire XML representation of the frequency editor settings. */
  public static String ATTR_FREQ_EDITOR_XML = "edfreqXml";

  private StringBuffer _freqEditorXmlBuffer = new StringBuffer();

  private String _defaultFreqEditorXml = null;

  public static final SpType SP_TYPE =
    SpType.create( SpType.OBSERVATION_COMPONENT_TYPE, "inst.Heterodyne", "Het Setup" );

//Register the prototype.
  static {
    SpFactory.registerPrototype( new SpInstHeterodyne() );
  }

  public SpInstHeterodyne() {
    super( SP_TYPE );
  }


  /**
   * Appends front end name to title.
   */
  public String getTitle() {

    String freqEditorXml = getFreqEditorXml();
    if(freqEditorXml == null) {
      return super.getTitle();
    }

    int a = freqEditorXml.indexOf("feName=\"");

    if(a < 0) {
      return super.getTitle();

    }

    // Set a to '"' in "feName=\""
    a += 8;

    int b = freqEditorXml.indexOf("\"", a);

    if(b < 0) {
      return super.getTitle();
    }

    return super.getTitle() + " (" + freqEditorXml.substring(a, b) + ")";

  }

  /**
   */
  public String getFreqEditorXml() {
    return _avTable.get(ATTR_FREQ_EDITOR_XML);
  }

  /**
   */
  public void setFreqEditorXml(String xml) {
    _avTable.set(ATTR_FREQ_EDITOR_XML, xml.trim());
  }


  /**
   * Get jiggle pattern options.
   *
   * @return String array of jiggle pattern options.
   */
  public String [] getJigglePatterns() {
    return JIGGLE_PATTERNS;
  }

  /** Not properly implemented yet. Returns 0.0. */
  public double getDefaultScanVelocity() {
    return 0.0;
  }

  /** Not properly implemented yet. Returns 0.0. */
  public double getDefaultScanDy() {
    return 0.0;
  }


  protected void processAvAttribute(String avAttr, String indent, StringBuffer xmlBuffer) {
    if(avAttr.equals(ATTR_FREQ_EDITOR_XML)) {
      // Ignore indent for now.
      xmlBuffer.append("\n  " + indent + indent(_avTable.get(ATTR_FREQ_EDITOR_XML), "  " + indent));
    }
    else {
      super.processAvAttribute(avAttr, indent, xmlBuffer);
    }
  }

  public void processXmlElementStart(String name) {
    String prefix = "";

    if(name.equals("heterodyne")) {
      _freqEditorXmlBuffer.setLength(0);

      prefix = "  ";
    }

    if(name.equals("bandSystem")) {
      prefix = "    ";

      // Close <heterodyne> tag
      _freqEditorXmlBuffer.append(">\n");
    }

    if(name.equals("subSystem")) {
      prefix = "      ";

      // Close <bandSystem> tag
      _freqEditorXmlBuffer.append(">\n");
    }    

    _freqEditorXmlBuffer.append(prefix + "<" + name);    
  }

  public void processXmlElementEnd(String name) {
    if(name.equals("subSystem")) {
      // Close <subSystem> tag
      _freqEditorXmlBuffer.append("/>\n");
    }

    if(name.equals("bandSystem")) {
      // Add </bandSystem> tag
      _freqEditorXmlBuffer.append("</bandSystem>\n");
    }

    if(name.equals("heterodyne")) {
      // Add </heterodyne> tag
      _freqEditorXmlBuffer.append("</heterodyne>\n");

      setFreqEditorXml(_freqEditorXmlBuffer.toString());
    }
  }

  public void processXmlAttribute(String elementName, String attributeName, String value) {

    if(elementName.equals(_className)) {
      super.processXmlAttribute(elementName, attributeName, value);
    }
    else {
      _freqEditorXmlBuffer.append(" " + attributeName + "=\"" + value + "\"");
    }
  }

  private static String indent(String xml, String indent) {
    StringBuffer xmlBuffer = new StringBuffer(xml);

    for(int i = 0; i < xmlBuffer.length(); i++) {
      if(xmlBuffer.charAt(i) == '\n') {
        xmlBuffer.insert(i + 1, indent);
      }
    }

    return xmlBuffer.toString();
  }
}


