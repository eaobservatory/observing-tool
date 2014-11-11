/*
 * Copyright 2002 United Kingdom Astronomy Technology Centre, an
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

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpItem;
import gemini.sp.SpInsertData;
import gemini.sp.SpTreeMan;

import java.io.Reader;
import java.io.StringReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
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
 * make the classes {@link orac.util.SpItemDOM} and
 * {@link orac.util.SpAvTableDOM} obsolete.
 *
 * There is a problem with (xerces) SAX parsers for which this class provides
 * a work around.  The problem is that the method
 * {@link #characters(char[],int,int)} is invoked once per XML element
 * most of the time to return the characters inside the element.
 * But some xerces versions seem to invoke it several times when newlines are
 * encountered.  And even without newlines characters it is sometimes called
 * twice with each of the calls containing only part of the characters in the
 * XML element. This is probably a bug in the xerces software.
 *
 * To fix this consecutive calls to characters result
 * the new characters being appended to the old ones and in
 * {@link gemini.sp.SpItem.processXmlElementContent(java.lang.String,java.lang.String)}
 * to be called again with the old and appended new characters.
 *
 * A special case is the XML element <tt>&lt;value&gt;</tt> which is used to
 * represent one of multible values of one SpAvTable entry. Since
 * <tt>"value"</tt> is a reserved String that should not be used as an
 * SpAvTable attribute name it can be assumed that a <tt>&lt;value&gt;</tt>
 * element does not contain any further XML elements inside it. So in this
 * case of the <tt>&lt;value&gt;</tt> element
 * {@link gemini.sp.SpItem.processXmlElementContent(java.lang.String,java.lang.String,int)}
 * is only invoked in
 * {@link endElement(java.lang.String,java.lang.String,java.lang.String)}
 * at the end of the * <tt>&lt;value&gt;</tt> element.
 *
 * <ul>The following SpItem methods are invoked during the parsing of the SpItem XML
 *   <li>{@link gemini.sp.SpItem#processXmlElementStart(java.lang.String)}
 *   <li>{@link gemini.sp.SpItem#processXmlElementContent(java.lang.String,java.lang.String)}
 *   <li>{@link gemini.sp.SpItem#processXmlElementContent(java.lang.String,java.lang.String,int)}
 *   <li>{@link gemini.sp.SpItem#processXmlElementEnd(java.lang.String)}
 *   <li>{@link gemini.sp.SpItem#processXmlAttribute(java.lang.String,java.lang.String,java.lang.String)}
 * </ul>
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpInputXML extends DefaultHandler {
    /**
     * Used to check whether XML format is old (SpItemDOM based) or new.
     *
     * First 200 characters of XML file are read into this buffer to check
     * format.
     */
    private static char[] _xmlBuffer = new char[200];
    public static final String XML_ATTR_TYPE = "type";
    public static final String XML_ATTR_SUBTYPE = "subtype";
    private String _currentElement;
    private SpItem _currentSpItem;
    private boolean _ignoreCharacters = false;
    private String _valueArrayElement = null;
    private int _valueArrayPos = 0;

    /*
     * The following is used to prevent attempts to insert SpTelescopeObsComp
     * into SurveyContainers.  In this case, the obsComp are stored internally
     * in the SurveyContainers.
     */
    protected boolean ignoreObsComp = false;

    /**
     * True if the first characters in side this element are encountered.
     *
     * The method {@link #characters(char[], int, int)} is sometimes called
     * more than once whithin one XML element. This seems to be dependend
     * on the parser that is used.
     */
    private boolean _firstCharacters = false;
    private String _characterBuffer = null;

    public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
        _firstCharacters = true;

        if (_currentSpItem != null) {
            _currentSpItem.processXmlElementStart(qName);
        }

        // Check whether the element represents an SpItem.
        if (qName.startsWith("Sp")
                && (atts.getValue(SpItem.XML_ATTR_TYPE) != null)
                && (atts.getValue(SpItem.XML_ATTR_SUBTYPE) != null)) {
            if (qName.equals("SpSurveyContainer")) {
                ignoreObsComp = true;
            }

            _ignoreCharacters = true;

            if (ignoreObsComp && qName.equals("SpTelescopeObsComp")) {
                return;
            }

            String xmlAttrType = atts.getValue(XML_ATTR_TYPE);
            String xmlAttrSubType = atts.getValue(XML_ATTR_SUBTYPE);
            SpType type = SpType.get(xmlAttrType, xmlAttrSubType);

            if (type == null) {
                throw new RuntimeException(xmlAttrType + " " + xmlAttrSubType
                        + " not found.");
            }

            SpItem spItem = SpFactory.createShallow(type);

            // Add the new element to the tree (unless it is the root item).
            if (_currentSpItem != null) {
                SpInsertData spID;

                if (_currentSpItem.childCount() < 1) {
                    spID = SpTreeMan.evalInsertInside(spItem, _currentSpItem);

                    if (spID == null) {
                        System.out.println("Could not insert " + spItem
                                + " into " + _currentSpItem);
                    }
                } else {
                    spID = SpTreeMan.evalInsertAfter(spItem,
                            _currentSpItem.lastChild());

                    if (spID == null) {
                        System.out.println("Could not insert " + spItem
                                + " after " + _currentSpItem.lastChild());
                    }
                }

                if (spID != null) {
                    SpTreeMan.insert(spID);
                }
            }

            _currentSpItem = spItem;

        } else {
            _ignoreCharacters = false;
        }

        if (qName.equals("value")) {
            if (_valueArrayElement != null) {
                _valueArrayPos++;

            } else {
                _valueArrayPos = 0;
                _valueArrayElement = _currentElement;
            }
        }

        _currentElement = qName;

        // Deal with attributes of XML element.
        for (int i = 0; i < atts.getLength(); i++) {
            _currentSpItem.processXmlAttribute(qName, atts.getQName(i),
                    atts.getValue(i));
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (qName.equals(_valueArrayElement)) {
            _valueArrayElement = null;
        }

        if (_characterBuffer != null) {
            if (_valueArrayElement != null) {
                _currentSpItem.processXmlElementContent(_valueArrayElement,
                        new String(_characterBuffer.trim()), _valueArrayPos);
                _characterBuffer = null;

            } else if ((_currentElement != null)
                    && (_characterBuffer.trim().length() != 0)) {
                _currentSpItem.processXmlElementContent(_currentElement,
                        new String(_characterBuffer.trim()));
                _characterBuffer = null;
            }
        }

        _currentSpItem.processXmlElementEnd(qName);

        if (qName.equals(_currentSpItem.getXmlElementName())) {
            if (_currentSpItem.parent() != null) {
                _currentSpItem = _currentSpItem.parent();
            }
        }

        if (qName.equals("SpSurveyContainer")) {
            ignoreObsComp = false;
        }

        _currentElement = null;
    }

    public void characters(char[] ch, int start, int length) {

        if (_ignoreCharacters) {
            return;
        }

        if (_firstCharacters) {
            _characterBuffer = null;
            _firstCharacters = false;
        }

        if (_characterBuffer == null) {
            _characterBuffer = new String(ch, start, length);
        } else {
            _characterBuffer = _characterBuffer + new String(ch, start, length);
        }
    }

    public SpItem xmlToSpItem(String xml) throws Exception {
        return xmlToSpItem(new StringReader(xml));
    }

    public SpItem xmlToSpItem(Reader reader) throws Exception {
        // A BufferedReader is only need to reset the stream inside
        // isOldXmlFormat(BufferedReader). Once there are no Science Programs
        // in the old XML format in circulation anymore things like
        // isOldXmlFormat, SpItemDOM can go and the normal. Reader can be
        // used instead of the BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(reader);

        if (isOldXmlFormat(bufferedReader)) {
            System.out.println("Converting from old XML format.");
            return (new SpItemDOM(bufferedReader)).getSpItem();
        }

        XMLReader xmlReader =
                SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(this);
        xmlReader.parse(new InputSource(bufferedReader));

        return _currentSpItem;
    }

    /**
     * Checks whether XML format is old (based on SpItemDOM conversion).
     *
     * @param reader buffered reader to read XML.
     *
     * @return true if file contains old XML format (based on SpItemDOM
     *         conversion)
     */
    public static boolean isOldXmlFormat(BufferedReader reader) {
        try {
            reader.mark(_xmlBuffer.length + 10);
            reader.read(_xmlBuffer, 0, _xmlBuffer.length);
            reader.reset();

            String xmlStart = new String(_xmlBuffer);

            if ((xmlStart != null) && (xmlStart.indexOf("ItemData") >= 0)) {
                return true;
            }
        } catch (IOException e) {
            System.out.println("Problem while checking XML format: " + e
                    + ". Using new XML format.");
        }

        return false;
    }

    /**
     * Test method.
     */
    public static void main(String[] args) {
        try {
            FileReader fr = new FileReader(args[0]);
            SpItem spItem = (new SpInputXML()).xmlToSpItem(fr);
            fr.close();
            FileWriter fw = new FileWriter(args[0]);
            fw.write(spItem.toXML());
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
