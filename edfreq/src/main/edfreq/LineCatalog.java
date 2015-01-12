/*
 * Copyright 2000-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * Copyright (C) 2003-2013 Science and Technology Facilities Council.
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
 *
 * This class was originally based on the Submillimeter Line Catalog in
 * Java format by Merce Crosas which included lines from the JPL catalog.
 * It has since been re-written to read the line catalog from an XML file.
 *
 * History:
 *   01-Aug-1997: original (Merce Crosas, mcrosas@cfa.harvard.edu)
 *   15-Feb-2000: added returnLines() (Dennis Kelly, bdk@roe.ac.uk)
 *   23-Feb-2000: added returnSpecies() (Dennis Kelly, bdk@roe.ac.uk)
 *   05-Jun-2003: removed the original line catalog and instead read
 *                the line catalog data from an XML file (Shaun De Witt,
 *                Joint Astronomy Centre)
 */

package edfreq;

import gemini.util.ObservingToolUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author Dennis Kelly (bdk@roe.ac.uk)
 */
public class LineCatalog {

    public static final String lineCatalogFile = "lineCatalog.xml";
    TreeMap<String, TreeMap<Double, String>> catalog;
    private static LineCatalog lineCatalog = null;

    protected LineCatalog() throws Exception {
        // make sure we can get to the line catalogue file
        String catalogFile = System.getProperty("ot.cfgdir");

        if (!catalogFile.endsWith("/")) {
            catalogFile += "/";
        }

        catalogFile += lineCatalogFile;

        initialiseFromFile(catalogFile);
    }

    public void initialiseFromFile(String catalogFile) throws Exception {
        URL url = ObservingToolUtilities.resourceURL(catalogFile);
        if (url == null) {
            new Exception("Can not open line catalog file " + catalogFile);
        }

        InputStream is = url.openStream();

        // If we get here, construct a SAXParser to read the file
        LocalContentHandler handler = new LocalContentHandler();
        try {
            SAXParser parser = new SAXParser();

            parser.setContentHandler(handler);
            parser.parse(new InputSource(is));

            is.close();
        } catch (IOException e) {
            System.out.println("Unable to read string");
        } catch (SAXException e) {
            System.out.println("Unable to create document: " + e.getMessage());
        }

        catalog = handler.getCatalog();
    }

    /**
     * Search for all lines between minFreq and maxFreq and record their
     * details in lineRef given a linear scaling between index numbers and
     * frequency.
     *
     * This overloads returnLines to provide the previous behaviour (no
     * lineRefExtra parameter) for code which depends on it.
     */
    public void returnLines(double minFreq, double maxFreq, int listSize,
            LineDetails lineRef[]) {
        returnLines(minFreq, maxFreq, listSize, lineRef, null);
    }

    /**
     * Search for all lines betwen minFreq and maxFreq and return their
     * details in lineRef.
     *
     * (See returnLines for more information.)
     *
     * If a line would overwrite a line already in the list, it is
     * added to the lineRefExtra table.  Null can be provided instead
     * of a table to disable this functionality.
     */
    public void returnLines(double minFreq, double maxFreq, int listSize,
            LineDetails lineRef[],
            Hashtable<Integer, ArrayList<LineDetails>> lineRefExtra) {
        int pix;
        double invRange;

        minFreq = minFreq * 1.0E-6;
        maxFreq = maxFreq * 1.0E-6;
        invRange = 1.0 / (maxFreq - minFreq);

        Iterator<String> molecules = catalog.keySet().iterator();
        while (molecules.hasNext()) {
            String molName = molecules.next();
            TreeMap<Double, String> currentMap = catalog.get(molName);

            // Get a submap of the keys between min and max frequecny
            TreeMap<Double, String> submap = new TreeMap<Double, String>(
                    currentMap.subMap(new Double(minFreq),
                            new Double(maxFreq)));

            // Now fill the values based on this submap
            Iterator<Double> iter = submap.keySet().iterator();
            while (iter.hasNext()) {
                Double currentFreq = iter.next();
                String currentName = submap.get(currentFreq);
                pix = (int) (((double) listSize) * (currentFreq - minFreq)
                        * invRange);
                LineDetails details = new LineDetails(molName, currentName,
                        currentFreq);

                if (lineRef[pix] != null && lineRefExtra != null) {
                    ArrayList<LineDetails> extras;

                    if (lineRefExtra.containsKey(pix)) {
                        extras = lineRefExtra.get(pix);
                    } else {
                        extras = new ArrayList<LineDetails>();
                        lineRefExtra.put(pix, extras);
                    }

                    extras.add(details);

                } else {
                    lineRef[pix] = details;
                }
            }
        }
    }

    public Vector<SelectionList> returnSpecies(double minFreq, double maxFreq) {
        Vector<SelectionList> v = new Vector<SelectionList>();
        SelectionList species;

        // Search for all lines between minFreq and maxFreq and record their
        // details.

        minFreq = minFreq * 1.0E-6;
        maxFreq = maxFreq * 1.0E-6;

        Iterator<String> mol = catalog.keySet().iterator();

        while (mol.hasNext()) {
            species = null;
            String currentSpecies = mol.next();

            TreeMap<Double, String> specMap = catalog.get(currentSpecies);
            TreeMap<Double, String> submap = new TreeMap<Double, String>(
                    specMap.subMap(new Double(minFreq), new Double(maxFreq)));

            // Iterate over the frequencies
            if (submap.size() > 0) {
                Iterator<Double> iter = submap.keySet().iterator();

                while (iter.hasNext()) {
                    if (species == null) {
                        species = new SelectionList(currentSpecies);
                        v.add(species);
                    }

                    Double key = iter.next();
                    species.objectList.add(new Transition(submap.get(key),
                            1.0e6 * key));
                }
            }
        }

        return v;
    }

    public synchronized static LineCatalog getInstance() throws Exception {
        if (lineCatalog == null) {
            try {
                lineCatalog = new LineCatalog();
            } catch (Exception e) {
                throw e;
            }
        }

        return lineCatalog;
    }

    static class LocalContentHandler implements ContentHandler {
        String currentSpecies = null;

        TreeMap<String, TreeMap<Double, String>> speciesTable =
                new TreeMap<String, TreeMap<Double, String>>();

        TreeMap<Double, String> freqTransMap;

        public void startElement(String namespace, String localName,
                String qName, Attributes attr) {
            if (qName.equals("species")) {
                currentSpecies = attr.getValue(attr.getIndex("name"));
                freqTransMap = new TreeMap<Double, String>();
            }

            if (qName.equals("transition")) {
                String name = attr.getValue(attr.getIndex("name"));
                Double freq = new Double(attr.getValue(
                        attr.getIndex("frequency")));
                freqTransMap.put(freq, name);
            }
        }

        public void characters(char[] ch, int start, int length) {
        }

        public void endElement(String namespace, String localName,
                String qName) {
            if (qName.equals("species")) {
                speciesTable.put(currentSpecies, freqTransMap);
            }
        }

        public void endPrefixMapping(String prefix) {
        };

        public void ignorableWhitespace(char[] ch, int start, int length) {
        };

        public void processingInstruction(String target, String data) {
        };

        public void setDocumentLocator(Locator l) {
        };

        public void skippedEntity(String name) {
        };

        public void startDocument() {
        };

        public void startPrefixMapping(String prefix, String uri) {
        };

        public void endDocument() {
        };

        public TreeMap<String, TreeMap<Double, String>> getCatalog() {
            return speciesTable;
        }
    }
}
