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
 * This class differs from other instrument items in that its SpAvTable contains just one
 * entry: the XML representation of this instrument item.
 * <p>
 * <b>Other instruments items</b> use the method processAvAttribute() to convert the SpAvTable entries to XML
 * and the methods processXmlElement...() to convert XML to the SpAvTable entries.
 * <p>
 * <b>SpInstHeterodyne on the other hand</b> overrides these methods so that
 * {@link #processAvAttribute(java.lang.String,java.lang.String,java.lang.StringBuffer)} effectively pastes
 * the existing XML representation of its SpInstHeterodyne item (as stored in the SpAvTable
 * under the attribute ATTR_FREQ_EDITOR_XML) into the XML of the Science Program.<br>
 * Similarly the {@link #processXmlElementStart(java.lang.String) processXmlElement...()} are used to
 * bit by bit filter the XML for this SpInstHeterodyne item out of the Science Program XML and then
 * stores it in the SpAvTable under the attribute ATTR_FREQ_EDITOR_XML.
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
   */
  public void noNotifySetFreqEditorXml(String xml) {
    _avTable.noNotifySet(ATTR_FREQ_EDITOR_XML, xml.trim(), 0);
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

  /**
   * Pastes the existing XML representation of its SpInstHeterodyne item (as stored in the SpAvTable
   * under the attribute ATTR_FREQ_EDITOR_XML) into the XML of the Science Program.
   *
   * @see orac.jcmt.inst.SpInstHeterodyne General comments about this class.
   */
  protected void processAvAttribute(String avAttr, String indent, StringBuffer xmlBuffer) {
    if(avAttr.equals(ATTR_FREQ_EDITOR_XML)) {
      // Ignore indent for now.
      xmlBuffer.append("\n  " + indent + indent(_avTable.get(ATTR_FREQ_EDITOR_XML), "  " + indent));
    }
    else {
      super.processAvAttribute(avAttr, indent, xmlBuffer);
    }
  }

  /**
   * Filters out the XML for this SpInstHeterodyne item from the Science Program XML and then
   * stores it in the SpAvTable under the attribute ATTR_FREQ_EDITOR_XML.
   *
   * @see orac.jcmt.inst.SpInstHeterodyne General comments about this class.
   */
  public void processXmlElementStart(String name) {
    String prefix = "";

    if(name.equals("heterodyne")) {
      _freqEditorXmlBuffer.setLength(0);

      prefix = "";
    }

    if(name.equals("bandSystem")) {
      prefix = "  ";

      // Close <heterodyne> tag
      _freqEditorXmlBuffer.append(">\n");
    }

    if(name.equals("subSystem")) {
      prefix = "    ";

      // Close <bandSystem> tag
      _freqEditorXmlBuffer.append(">\n");
    }    

    _freqEditorXmlBuffer.append(prefix + "<" + name);    
  }

  /**
   * Filters out the XML for this SpInstHeterodyne item from the Science Program XML and then
   * stores it in the SpAvTable under the attribute ATTR_FREQ_EDITOR_XML.
   *
   * @see orac.jcmt.inst.SpInstHeterodyne General comments about this class.
   */
  public void processXmlElementEnd(String name) {
    if(name.equals("subSystem")) {
      // Close <subSystem> tag
      _freqEditorXmlBuffer.append("/>");
    }

    if(name.equals("bandSystem")) {
      // Add </bandSystem> tag
      _freqEditorXmlBuffer.append("\n  </bandSystem>");
    }

    if(name.equals("heterodyne")) {
      // Add </heterodyne> tag
      _freqEditorXmlBuffer.append("</heterodyne>\n");

      noNotifySetFreqEditorXml(_freqEditorXmlBuffer.toString());
    }
  }

  /**
   * Filters out the XML for this SpInstHeterodyne item from the Science Program XML and then
   * stores it in the SpAvTable under the attribute ATTR_FREQ_EDITOR_XML.
   *
   * @see orac.jcmt.inst.SpInstHeterodyne General comments about this class.
   */
  public void processXmlAttribute(String elementName, String attributeName, String value) {

    if(elementName.equals(_className)) {
      super.processXmlAttribute(elementName, attributeName, value);
    }
    else {
      if(elementName.equals("heterodyne")) {
        _freqEditorXmlBuffer.append("\n    " + attributeName + "=\"" + value + "\"");
      }
      else {
        _freqEditorXmlBuffer.append(" " + attributeName + "=\"" + value + "\"");
      }
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


