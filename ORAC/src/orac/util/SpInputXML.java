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

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpItem;
import gemini.sp.SpInsertData;
import gemini.sp.SpTreeMan;

import java.io.Reader;
import java.io.StringReader;
import java.io.FileReader;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;


/**
 * Utility class for reading in Science Programs etc from XML.
 *
 * This class is effectively a wrapper class around a SAX parser.
 * Together with the XML related methods in {@link gemini.sp.SpItem} it
 * make the classes {@link orac.util.SpItemDOM} and {@link orac.util.SpAvTableDOM}
 * obsolete.
 * <p>
 * There is a problem with (xerces) SAX parsers for which this class provides a work around.
 * The problem is that the method {@link #characters(char[],int,int)} is invoked once per XML element
 * most of the time to return the characters inside the element.
 * But some xerces versions seem to invoke it several times when newlines are encountered.
 * And even without newlines characters it is sometimes called twice with each of the calls containing
 * only part of the characters in the XML element. This is probably a bug in the xerces software.
 * To fix this consecutive calls to characters result
 * the new characters being appended to the old ones and in
 * {@link gemini.sp.SpItem.processXmlElementContent(java.lang.String,java.lang.String)}
 * to be called again with the old and appended new characters.<br>
 * A special case is the XML element <tt>&lt;value&gt;</tt> which is used to represent one of multible values
 * of one SpAvTable entry. Since <tt>"value"</tt> is a reserved String that should not be used as an SpAvTable
 * attribute name it can be assumed that a <tt>&lt;value&gt;</tt> element does not contain any further
 * XML elements inside it. So in this case of the <tt>&lt;value&gt;</tt> element
 *{@link gemini.sp.SpItem.processXmlElementContent(java.lang.String,java.lang.String,int)} is only invoked
 * in {@link endElement(java.lang.String,java.lang.String,java.lang.String)} at the end of the
 * <tt>&lt;value&gt;</tt> element.
 *
 * <ul>The following SpItem methods are invoked during the parsing of the SpItem XML
 *   <li> {@link gemini.sp.SpItem#processXmlElementStart(java.lang.String)}
 *   <li> {@link gemini.sp.SpItem#processXmlElementContent(java.lang.String,java.lang.String)}
 *   <li> {@link gemini.sp.SpItem#processXmlElementContent(java.lang.String,java.lang.String,int)}
 *   <li> {@link gemini.sp.SpItem#processXmlElementEnd(java.lang.String)}
 *   <li> {@link gemini.sp.SpItem#processXmlAttribute(java.lang.String,java.lang.String,java.lang.String)}
 * </ul>
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpInputXML extends DefaultHandler {

  public static final String XML_ATTR_TYPE    = "type";
  public static final String XML_ATTR_SUBTYPE = "subtype";

  private String _currentElement;
  private SpItem _currentSpItem;
  private boolean _ignoreCharacters = false;
  private String  _valueArrayElement = null;
  private int     _valueArrayPos = 0;

  /**
   * True if the first characters in side this element are encountered.
   *
   * The method {@link #characters(char[], int, int)} is sometimes called more than
   * once whithin one XML element. This seems to be dependend on the parser that is used.
   */
  private boolean _firstCharacters = false;

  private StringBuffer _characterBuffer = new StringBuffer();

  public void startElement(String namespaceURI,
                           String localName,
                           String qName,
                           Attributes atts) throws SAXException {

    _firstCharacters = true;

    if(_currentSpItem != null) {
      _currentSpItem.processXmlElementStart(qName);
    }

    // Check whether the element represents an SpItem.
    if(qName.startsWith("Sp") &&
       (atts.getValue(SpItem.XML_ATTR_TYPE) != null) &&
       (atts.getValue(SpItem.XML_ATTR_SUBTYPE) != null)) {

      _ignoreCharacters = true;

      SpItem spItem = SpFactory.createShallow(SpType.get(atts.getValue(XML_ATTR_TYPE), atts.getValue(XML_ATTR_SUBTYPE)));

      // Add the new element to the tree (unless it is the root item).
      if(_currentSpItem != null) {
        SpInsertData spID;
        if(_currentSpItem.childCount() < 1) {
          spID = SpTreeMan.evalInsertInside(spItem, _currentSpItem);

          if (spID == null) {
	    System.out.println("Could not insert " + spItem + " into " + _currentSpItem);
          }
	}
	else {
          spID = SpTreeMan.evalInsertAfter(spItem, _currentSpItem.lastChild());

          if (spID == null) {
	    System.out.println("Could not insert " + spItem + " after " + _currentSpItem.lastChild());
          }
	}

        if (spID != null) {
          SpTreeMan.insert(spID);
        }
      }

      _currentSpItem     = spItem;
    }
    else {
      _ignoreCharacters = false;
    }

    if(qName.equals("value")) {
      if(_valueArrayElement != null) {
        _valueArrayPos++;
      }
      else {
        _valueArrayPos = 0;
	_valueArrayElement = _currentElement;
      }
    }

    _currentElement = qName;


    // Deal with attributes of XML element.
    for(int i = 0; i < atts.getLength(); i++) {
      _currentSpItem.processXmlAttribute(qName, atts.getQName(i), atts.getValue(i));
    }
  }

  public void endElement(String uri, String localName, String qName) {
    if(qName.equals(_valueArrayElement)) {
      _valueArrayElement = null;
    }

    if(_valueArrayElement != null) {
      _currentSpItem.processXmlElementContent(_valueArrayElement, _characterBuffer.toString().trim(), _valueArrayPos);
      _characterBuffer.setLength(0);
    }

    _currentSpItem.processXmlElementEnd(qName);

    if(qName.equals(_currentSpItem.getXmlElementName())) {
      if(_currentSpItem.parent() != null) {
        _currentSpItem = _currentSpItem.parent();
      }
    }

    _currentElement = null;
  }
 

  public void characters(char[] ch, int start, int length) {

    if(_ignoreCharacters) {
      return;
    }

    if(_firstCharacters) {
      _characterBuffer.setLength(0);
      _firstCharacters = false;
    }

      _characterBuffer.append(new String(ch, start, length));


    //if((_currentElement != null) && (value.trim().length() > 0)){
    // values have to be "sent" in the endElement method because a multiple
    // call to characters() inside a <value> element would cause extra elements to
    // be added to the attribute _currentElement of the SpAvTable, each of them containing
    // partial Strings.
    if((_currentElement != null) && (_valueArrayElement == null)){

      _currentSpItem.processXmlElementContent(_currentElement, _characterBuffer.toString());
    }
  }

  public SpItem xmlToSpItem(String xml) throws Exception {
    return xmlToSpItem(new StringReader(xml));
  }

  public SpItem xmlToSpItem(Reader reader) throws Exception {
    XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();

    xmlReader.setContentHandler(this);

    xmlReader.parse(new InputSource(reader));

    return _currentSpItem;
  }

  /** Test method. */
  public static void main(String [] args) {
    try {
      SpItem spItem = (new SpInputXML()).xmlToSpItem(new FileReader(args[0]));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}

