/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
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

package orac.validation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import gemini.sp.SpProg;
import gemini.sp.SpObs;
import gemini.sp.SpMSB;
import gemini.sp.SpItem;
import gemini.sp.SpNote;
import gemini.sp.SpSurveyContainer;
import gemini.sp.SpTelescopePosList;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.obsComp.SpSiteQualityObsComp;
import gemini.sp.obsComp.SpSchedConstObsComp;
import gemini.util.CoordSys;
import gemini.util.DDMMSS;
import gemini.util.HHMMSS;
import gemini.util.Format;
import gemini.util.RADec;
import gemini.util.TelescopePos;

import orac.util.CoordConvert;
import orac.util.SpItemUtilities;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.InputSource;
import org.xml.sax.ErrorHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import orac.util.OracUtilities;
import orac.util.TimeUtils;
import java.util.Date;

/**
 * Validation Tool.
 *
 * This class is used for checking whether the values and settings in a
 * Science Program or Observation are sensible.
 *
 * Errors and warnings are issued otherwise.
 *
 * @author M.Folger@roe.ac.uk UKATC
 */
public class SpValidation {
    protected static String separator =
            "------------------------------------------------------\n";

    public void checkSciProgram(SpProg spProg, Vector<ErrorMessage> report) {
        if (report == null) {
            report = new Vector<ErrorMessage>();
        }

        // Change the SpProg to a document, since this is more easily checked
        byte[] xml = spProg.toXML();
        DOMParser parser = new DOMParser();
        Document doc = null;
        try {
            // Turn off validation
            parser.setFeature("http://xml.org/sax/features/validation", false);
            parser.parse(new InputSource(new ByteArrayInputStream(xml)));
            doc = parser.getDocument();

        } catch (IOException e) {
            System.out.println("Unable to read string");

        } catch (SAXException e) {
            System.out.println("Unable to create document: " + e.getMessage());

        }

        checkSciProgramRecursively(spProg, report);
        checkSurveyContainer(spProg, report);

        if (doc != null) {
            // I use one method for each 'rule'
            // Rule 1: Each SpObs and MSB must either contain the following
            // or inherit it from higher up:
            // SpInst...
            // SpSiteQualityObsComp
            // SptelescopeObsComp
            checkInheritedComponents(doc, "SpMSB",
                    "SpSiteQualityObsComp", "Site Quality", report);
            checkInheritedComponents(doc, "SpMSB",
                    "SpInst", "Instrument", report);
            checkInheritedComponents(doc, "SpObs",
                    "SpSiteQualityObsComp", "Site Quality", report);
            checkInheritedComponents(doc, "SpObs",
                    "SpInst", "Instrument", report);

            // Rule2: Each iterator must contain either another iterator or
            // an observe eye
            // Exception: For UKIRT, the offset iterator does not have this
            // requirement
            checkIterator(doc, "SpIterFolder", "Sequence Iterator", report);
            checkIterator(doc, "SpIterChop", "Chop Iterator", report);
            checkIterator(doc, "SpIterPOL", "POL Iterator", report);
            checkIterator(doc, "SpIterRepeat", "Repeat Iterator", report);
            checkIterator(doc, "SpIterOffset", "Offset Iterator", report);

            // Other rules:
            // SpAND must contain an SpObs or SpMSB but NOT and SpOR
            // SpOR must contain and SpObs, SpMSB or SpOR
            // SpMSB must contain an SpObs
            // SpObs must contain only 1 sequence
            Vector<String> mandatoryChildren = new Vector<String>();
            Vector<String> excludedChildren = new Vector<String>();
            mandatoryChildren.add("SpObs");
            mandatoryChildren.add("SpMSB");
            excludedChildren.add("SpOR");
            excludedChildren.add("SpAND");
            checkForChildren(doc, "SpAND",
                    mandatoryChildren, excludedChildren, report);
            mandatoryChildren.add("SpAND");
            excludedChildren.clear();
            excludedChildren.add("SpOR");
            checkForChildren(doc, "SpOR",
                    mandatoryChildren, excludedChildren, report);
        }
    }

    protected void checkSciProgramRecursively(SpItem spItem,
            Vector<ErrorMessage> report) {
        Enumeration<SpItem> children = spItem.children();

        while (children.hasMoreElements()) {
            SpItem child = children.nextElement();

            if (child instanceof SpMSB) {
                checkMSB((SpMSB) child, report);
            } else if (!checkCommonItem(child, report)) {
                checkSciProgramRecursively(child, report);
            }
        }
    }

    /**
     * Attempt to check common science programme items.
     *
     * Currently this only checks scheduling constraints.
     *
     * The reason for having this method is that both
     * checkSciProgramRecursively and checkMSB loop over thier
     * children.  We want to handle the items which could be
     * in either context without having to repeat the code.
     *
     * TODO: the proper way to do this would be to have a validate method
     * on the SpItems rather than having to use instanceof.
     *
     * @return true if the item was checked, otherwise the caller
     *         should continue to recurse inside.
     */
    protected boolean checkCommonItem(SpItem child,
            Vector<ErrorMessage> report) {
        if (child instanceof SpSchedConstObsComp) {
            checkSchedComp((SpSchedConstObsComp) child, report);
            return true;
        }

        return false;
    }

    public void checkMSB(SpMSB spMSB, Vector<ErrorMessage> report) {
        if (report == null) {
            System.err.println(
                    "SpValidation.checkMSB: recieved null report,"
                    + " validation messages will be lost!");
            report = new Vector<ErrorMessage>();
        }

        SpSiteQualityObsComp siteQuality = SpItemUtilities.findSiteQuality(spMSB);
        if (siteQuality == null) {
            report.add(new ErrorMessage(ErrorMessage.ERROR,
                    "MSB \"" + spMSB.getTitle() + "\"",
                    "Site quality component is missing."));
        }
        else {
            if (! siteQuality.tauBandAllocated()) {
                if (siteQuality.getMinTau() > siteQuality.getMaxTau()) {
                    report.add(new ErrorMessage(ErrorMessage.ERROR,
                        "MSB \"" + spMSB.getTitle() + "\"",
                        "Site quality has minimum tau > maximum tau."));
                }
            }
        }

        // Check whether there are more than one observe instruction notes.
        Vector<SpItem> notes = SpTreeMan.findAllItems(spMSB,
                SpNote.class.getName());

        boolean observeNoteFound = false;
        int numberOfObserveInstructions = 0;

        // How may notes are inside the MSB and are any of then observer
        // instructions
        if (notes.size() > 0) {
            for (int i = 0; i < notes.size(); i++) {
                if (((SpNote) notes.get(i)).isObserveInstruction()) {
                    numberOfObserveInstructions++;
                    observeNoteFound = true;
                }
            }
        }

        // If we havent found an observer note, go through the parents and
        // check if there is one there
        if (!observeNoteFound) {
            SpItem parent = spMSB.parent();

            while (parent != null) {
                SpNote note = SpTreeMan.findObserverNoteInContext(parent);

                if (note != null) {
                    observeNoteFound = true;
                    break;
                }

                parent = parent.parent();
            }
        }

        if (!observeNoteFound) {
            report.add(new ErrorMessage(ErrorMessage.WARNING,
                    "MSB \"" + spMSB.getTitle()
                            + "\" does not have an Show-to Observer Note",
                    "Recommend MSBs have an observe instruction"));
        }

        // Add a check for MSB's > 2 hours long
        if (spMSB.getElapsedTime() > 2.0 * 3600) {
            report.add(new ErrorMessage(ErrorMessage.WARNING,
                    "MSB \"" + spMSB.getTitle()
                            + "\" is more than 2 hours long",
                    "MSBs should ideally be < 1.5 hours long"));
        }

        // Following check added - if the MSB parent is a survey container, and
        // the MSB has a TargetComponent as a child, this is an error
        if (spMSB.parent() instanceof SpSurveyContainer
                && !(spMSB instanceof SpObs)) {
            Enumeration<SpItem> children = spMSB.children();

            while (children.hasMoreElements()) {
                SpItem child = children.nextElement();

                if (child instanceof SpTelescopeObsComp) {
                    report.add(new ErrorMessage(
                            ErrorMessage.ERROR,
                            "An MSB within a Survey Container may not contain"
                                    + " a Target Component",
                            "SC/MSB/Target not allowed"));

                    break;
                }
            }
        }

        if (spMSB instanceof SpObs) {
            checkObservation((SpObs) spMSB, report);

        } else {
            Enumeration<SpItem> children = spMSB.children();

            while (children.hasMoreElements()) {
                SpItem child = children.nextElement();

                if (child instanceof SpObs) {
                    checkObservation((SpObs) child, report);

                } else {
                    checkCommonItem(child, report);
                }
            }
        }
    }

    public static String titleString(SpItem spItem) {
        String returnString = "";
        SpItem parent = spItem;

        while (parent != null) {
            if (parent instanceof SpObs) {
                returnString += "\"" + ((SpObs) parent).getTitle() + "\"";

            } else if (parent instanceof SpMSB) {
                returnString += " in MSB \'"
                        + ((SpMSB) parent).getTitleAttr() + "\'";

                break;
            }

            parent = parent.parent();
        }
        return returnString;
    }

    public void checkObservation(SpObs spObs, Vector<ErrorMessage> report) {
        if (report == null) {
            report = new Vector<ErrorMessage>();
        }

        String titleString = titleString(spObs);

        Enumeration<SpItem> children;
        SpItem child;

        if (SpTreeMan.findInstrument(spObs) == null) {
            report.add(new ErrorMessage(ErrorMessage.ERROR,
                    "Observation " + titleString,
                    "Instrument component is missing."));
        }

        if (!spObs.isOptional()
                && SpTreeMan.findSurveyContainerInContext(spObs) == null) {
            checkTargetList(SpTreeMan.findTargetList(spObs), report);
        }

        if (!spObs.isMSB()) {
            children = spObs.children();

            while (children.hasMoreElements()) {
                child = children.nextElement();

                if (child instanceof SpSiteQualityObsComp) {
                    report.add(new ErrorMessage(
                            ErrorMessage.ERROR,
                            "Observation " + titleString,
                            "Observations inside MSBs must use the"
                                    + " site quality component of"
                                    + " the parent MSB."));
                }

                if (child instanceof SpSchedConstObsComp) {
                    report.add(new ErrorMessage(
                            ErrorMessage.ERROR,
                            "Observation " + titleString,
                            "Observations inside MSBs must use the"
                                    + " scheduling constraints component"
                                    + " of the parent MSB."));
                }
            }
        }
    }

    private void checkInheritedComponents(Document doc, String folderName,
            String componentName, String description,
            Vector<ErrorMessage> report) {
        String title = null;

        // Check all the children of the top level element
        Element root = doc.getDocumentElement();

        // Each MSB and SpObs must contain an SpTelescopeObsComp, or inherit
        // it from a parent.
        // Check each one for this
        NodeList nl = root.getElementsByTagName(folderName);
        boolean found = false;

        for (int i = 0; i < nl.getLength(); i++) {
            Node thisNode = nl.item(i);

            // For each MSB check if we have the required info.
            NodeList requiredTag =
                    ((Element) thisNode).getElementsByTagName("*");
            title = null;

            if (requiredTag != null) {
                for (int j = 0; j < requiredTag.getLength(); j++) {
                    Node currentNode = requiredTag.item(j);

                    if (currentNode != null) {
                        String nodeName = currentNode.getNodeName();

                        if (nodeName == null) {
                            nodeName = "";
                        }

                        if (nodeName.equalsIgnoreCase("title")) {
                            // if we have reached here requiredTag.item( j ) != null
                            Node tempNode = currentNode.getFirstChild();

                            if (tempNode != null) {
                                title = tempNode.getNodeValue();
                            }

                        } else if (!found
                                && nodeName.startsWith(componentName)) {
                            // I am sure this should be `else if` and not
                            // `if` ...
                            found = true;
                        }
                    }
                }
            }

            if (!found) {
                // Check whether any of the proginy contain an autoTarget flag.
                // In this case, we have a calibration, so we are OK
                NodeList autoTargets =
                        ((Element) thisNode).getElementsByTagName("autoTarget");

                if (autoTargets != null && autoTargets.getLength() > 0) {
                    found = true;
                }
            }

            if (!found) {
                // We don't so recurse up the document tree to see if any
                // parents have it
                while ((thisNode = thisNode.getParentNode()) != null
                        && !found) {
                    NodeList childList = thisNode.getChildNodes();

                    for (int child = 0; child < childList.getLength();
                            child++) {
                        if (childList.item(child).getNodeName().startsWith(
                                componentName)) {
                            found = true;

                            break;
                        }
                    }
                }
            }

            if (found) {
                // Check the next one
                found = false;
                continue;

            } else {
                if (title != null) {
                    report.add(new ErrorMessage(ErrorMessage.ERROR,
                            "MSB or Observation",
                            "MSB/Observation \"" + title
                                    + "\" does not contain or inherit a "
                                    + description));
                } else {
                    report.add(new ErrorMessage(ErrorMessage.ERROR,
                            "MSB or Observation",
                            "Unnamed MSB/Observation does not"
                                    + " contain or inherit a "
                                    + description));
                }

                break;
            }
        }
    }

    private void checkIterator(Document doc, String iteratorType,
            String description, Vector<ErrorMessage> report) {
        boolean found = false;

        // Get the root element
        Element root = doc.getDocumentElement();

        // Get all the nodes which we are going to check
        NodeList nl = root.getElementsByTagName(iteratorType);

        // For each of these, get all of its children
        for (int i = 0; i < nl.getLength(); i++) {
            NodeList children = nl.item(i).getChildNodes();

            // Check each child looking for another SpIter...
            for (int j = 0; j < children.getLength(); j++) {
                if (children.item(j).getNodeName().startsWith("SpIter")) {
                    found = true;

                    break;
                }
            }

            if (found) {
                found = false;

                continue;

            } else {
                report.add(new ErrorMessage(
                        ErrorMessage.ERROR,
                        description,
                        "A " + description
                                + " does not contain another"
                                + " iterator or observe"));

                break;
            }
        }
    }

    /**
     * @param component Tag name of child to check
     * @param mandatory The tag must have at least one of these types
     * @param excluded  List of children that are prohibited from appearing in
     *                  the tag
     * @param report    Errors returned through this
     */
    private void checkForChildren(Document doc, String component,
            Vector<String> mandatory, Vector<String> excluded,
            Vector<ErrorMessage> report) {
        boolean mandatoryElementFound = false;
        boolean excludedElementFound = false;

        Element root = doc.getDocumentElement();

        // Het all of the required components
        NodeList nl = root.getElementsByTagName(component);

        // For each of these, make sure we have at least one mandatory tag
        // as a direct child, and that the exclusion does not appear
        for (int i = 0; i < nl.getLength(); i++) {
            NodeList children = nl.item(i).getChildNodes();

            for (int j = 0; j < children.getLength(); j++) {
                // Check if this is a madatory tag
                if (mandatory != null) {
                    for (int tag = 0; tag < mandatory.size(); tag++) {
                        if (children.item(j).getNodeName()
                                .equals(mandatory.elementAt(tag))) {
                            mandatoryElementFound = true;

                            break;
                        }
                    }
                }

                if (excluded != null) {
                    for (int tag = 0; tag < excluded.size(); tag++) {
                        if (children.item(j).getNodeName()
                                .equals(excluded.elementAt(tag))) {
                            excludedElementFound = true;
                        }
                    }
                }
            }

            if (mandatoryElementFound && !excludedElementFound) {
                // This is OK, so reset and go to the next in the list
                mandatoryElementFound = false;
                excludedElementFound = false;

                continue;

            } else {
                // Something is wrong
                if (!mandatoryElementFound) {
                    String message = component.substring(2)
                            + " does not contain at least one of { ";
                    for (int tag = 0; tag < mandatory.size(); tag++) {
                        message = message
                                + mandatory.elementAt(tag).substring(2) + " ";
                    }

                    message = message + "}";
                    report.add(new ErrorMessage(ErrorMessage.ERROR, component,
                            message));

                    break;

                } else {
                    String message = component.substring(2)
                            + " contains one of { ";

                    for (int tag = 0; tag < excluded.size(); tag++) {
                        message = message
                                + excluded.elementAt(tag).substring(2) + " ";
                    }

                    message = message + "} which is not allowed";

                    report.add(new ErrorMessage(ErrorMessage.ERROR, component,
                            message));

                    break;
                }
            }
        }
    }

    protected void checkTargetList(SpTelescopeObsComp obsComp,
            Vector<ErrorMessage> report) {
        if (obsComp != null) {
            String titleString = titleString(obsComp);
            SpTelescopePosList list = obsComp.getPosList();
            TelescopePos[] position = list.getAllPositions();

            for (int i = 0; i < position.length; i++) {
                SpTelescopePos pos = (SpTelescopePos) position[i];
                String itemString = "Telescope target " + pos.getName()
                        + " in " + titleString;

                Double declination = null;

                if (pos.getSystemType() == SpTelescopePos.SYSTEM_SPHERICAL) {
                    double Xaxis = pos.getXaxis();
                    double Yaxis = pos.getYaxis();

                    if (Xaxis == 0 && Yaxis == 0) {
                        report.add(new ErrorMessage(ErrorMessage.WARNING,
                                itemString, "Both Dec and RA are 0:00:00"));
                    }

                    int coordSystem = pos.getCoordSys();
                    String converted = "";

                    if (coordSystem == CoordSys.FK4) {
                        RADec raDec = CoordConvert.Fk45z(Xaxis, Yaxis);
                        Xaxis = raDec.ra;
                        Yaxis = raDec.dec;
                        converted = " converted from FK4";

                    } else if (coordSystem == CoordSys.GAL) {
                        RADec raDec = CoordConvert.gal2fk5(Xaxis, Yaxis);
                        Xaxis = raDec.ra;
                        Yaxis = raDec.dec;
                        converted = " converted from Galactic";
                    }

                    String hhmmss = HHMMSS.valStr(Xaxis);
                    String ddmmss = DDMMSS.valStr(Yaxis);

                    if (!HHMMSS.validate(hhmmss)) {
                        report.add(new ErrorMessage(ErrorMessage.ERROR,
                                itemString,
                                "RA" + converted,
                                "range 0:00:00 ... 24:00:00", hhmmss));
                    }

                    if (!DDMMSS.validate(ddmmss, -50, 90)) {
                        report.add(new ErrorMessage(ErrorMessage.ERROR,
                                itemString,
                                "Dec" + converted,
                                "range -50:00:00 ... 90:00:00", ddmmss));
                    }

                    declination = new Double(Yaxis);
                }

                double pmRA = Format.toDouble(pos.getPropMotionRA());
                double pmDec = Format.toDouble(pos.getPropMotionDec());

                if ((pmRA != 0.0) || (pmDec != 0.0)) {
                    String pmEpoch = pos.getTrackingEpoch();

                    if (pmEpoch == null || "".equals(pmEpoch)) {
                        report.add(new ErrorMessage(ErrorMessage.ERROR,
                                itemString,
                                "Proper motion epoch is not specified"));

                    } else {
                        try {
                            double epoch = Double.parseDouble(pmEpoch);

                            if (epoch < 1950 || epoch > 2050) {
                                report.add(new ErrorMessage(
                                        ErrorMessage.WARNING, itemString,
                                        "Proper motion epoch not in range"
                                                + " 1950-2050."));
                            }

                        } catch (NumberFormatException e) {
                            report.add(new ErrorMessage(ErrorMessage.ERROR,
                                    itemString,
                                    "Could not parse proper motion epoch."));
                        }

                    }

                    double pmParallax = Format.toDouble(pos.getTrackingParallax());

                    if (pmParallax == 0.0) {
                        report.add(new ErrorMessage(ErrorMessage.ERROR,
                                itemString,
                                "Parallax not specified with proper motion."));

                    } else {
                        if (declination == null) {
                            report.add(new ErrorMessage(
                                    ErrorMessage.WARNING,
                                    itemString,
                                    "Declination not available - cannot compute real " +
                                    "proper motion for speed check."));
                        }
                        else {
                            // Convert from coordinate angle to real angle.
                            pmRA *= Math.cos(Math.toRadians(declination));
                        }

                        double pmCombined = Math.sqrt(
                            Math.pow(pmRA, 2.0) +
                            Math.pow(pmDec, 2.0));

                        // Current ERFA limits PM to 0.5c and min parallax
                        // to 1E-4 milli-arcsec.  (3.162 milli-arcsec/year at that distance.  Check
                        // here for 2.0 milli-arcsec/year to give a margin of safety.)
                        if (pmCombined > (2.0E4 * pmParallax)) {
                            report.add(new ErrorMessage(
                                    ErrorMessage.WARNING,
                                    itemString,
                                    "Proper motion is very large for " +
                                    "specified parallax distance.  It " +
                                    "may be ignored."));
                        }
                    }
                }
            }
        } else {
            report.add(new ErrorMessage(ErrorMessage.ERROR,
                    "Observation has no target component", ""));
        }
    }

    private void checkSurveyContainer(SpProg prog, Vector<ErrorMessage> report) {
        Vector<SpItem> containers = SpTreeMan.findAllInstances(prog,
                SpSurveyContainer.class.getName());

        for (int count = 0; count < containers.size(); count++) {
            SpSurveyContainer container =
                    (SpSurveyContainer) containers.get(count);

            for (int i = 0; i < container.size(); i++) {
                checkTargetList(container.getSpTelescopeObsComp(i), report);
            }

            // Check that survey containers do not include bare SpObs
            // unless they are in an MSB already.
            if (!container.hasMSBParent()) {
                Enumeration<SpItem> children = container.children();

                while (children.hasMoreElements()) {
                    SpItem child = children.nextElement();

                    if (child instanceof SpObs) {
                        report.add(new ErrorMessage(
                                ErrorMessage.ERROR,
                                container.getTitle()
                                        + " includes a bare SpObs",
                                ""));
                    }
                }
            }
        }
    }

    /**
     * Perform validation against a schema.
     *
     * The SpItem class already writes out a part of the default namesapce
     * in its toXML method.  In this class we apply the actual location of
     * the schema.  A SAX parser is used to parse the string.
     *
     * @param xml        The current XML representation of the science project
     * @param schemaURL  Fully qualified file name for the schema.
     * @param schemaLocation Location of schema.
     *
     * @return           A vector of strings containing the validation errors
     */
    public void schemaValidate(byte[] xml, String schemaURL,
            String schemaLocation, Vector<ErrorMessage> report) {
        // Make sure the schema exists
        if (schemaLocation == null) {
            report.add(new ErrorMessage(ErrorMessage.WARNING,
                    "Schema validation not performed",
                    "Unable to locate the XML schema."));
            return;
        }

        if (schemaURL == null) {
            report.add(new ErrorMessage(ErrorMessage.WARNING,
                    "Schema validation not performed",
                    "The XML schema URL is not configured."));
            return;
        }

        SAXParser parser = new SAXParser();
        SchemaErrorHandler handler = new SchemaErrorHandler(
            new String(xml, StandardCharsets.UTF_8));
        SchemaContentHandler contentHandler = new SchemaContentHandler();
        parser.setErrorHandler(handler);
        parser.setContentHandler(contentHandler);
        try {
            parser.setFeature(
                    "http://xml.org/sax/features/validation", true);
            parser.setFeature(
                    "http://apache.org/xml/features/validation/schema", true);
            parser.setFeature(
                    "http://apache.org/xml/features/validation/schema-full-checking",
                    true);
            parser.setProperty(
                    "http://apache.org/xml/properties/schema/external-schemaLocation",
                    schemaURL + " " + schemaLocation);
            parser.parse(new InputSource(new ByteArrayInputStream(xml)));

        } catch (SAXNotRecognizedException e) {
            System.out.println("Unrecognized feature");

        } catch (SAXNotSupportedException e) {
            System.out.println("Unsupported feature");

        } catch (IOException e) {
            System.out.println("Unable to read string");

        } catch (SAXException e) {
            System.out.println("Unable to create document: " + e.getMessage());
        }

        Vector<String> schemaErrors = handler.getErrors();

        for (int i = 0; i < schemaErrors.size(); i++) {
            report.add(new ErrorMessage(ErrorMessage.ERROR,
                    "Schema validation error", schemaErrors.get(i)));
        }
    }

    public class SchemaErrorHandler implements ErrorHandler {
        public Vector<String> errorMessages = new Vector<String>();

        private String[] xml = null;

        public SchemaErrorHandler(String xmlString) {
            xml = xmlString.split("\n");
        }

        public void error(SAXParseException e) {
            int lineNumber = e.getLineNumber() - 1;

            String errorMessage = separator;
            errorMessage += "Validation error in MSB<"
                    + SchemaContentHandler.getCurrentMSB() + ">\n";
            errorMessage += "XML line number : " + (lineNumber + 1)
                    + " column " + e.getColumnNumber() + "\n";

            errorMessage += "\n";
            errorMessage += xml[lineNumber - 1] + "\n";
            errorMessage += "--> " + xml[lineNumber] + "  <--\n";

            if (lineNumber + 1 < xml.length) {
                errorMessage += xml[lineNumber + 1] + "\n";
            }

            errorMessage += "\n";

            errorMessage += "Obs:<" + SchemaContentHandler.getCurrentObs()
                    + "> \n";
            errorMessage += "component<"
                    + SchemaContentHandler.getCurrentClass() + ">: \n";
            errorMessage += "message:<" + e.getMessage() + ">\n";

            errorMessages.add(errorMessage);
        }

        public void fatalError(SAXParseException e) {
            String errorMessage = "Fatal parse error in <" + e.getSystemId()
                    + ">: " + e.getMessage() + "\n";

            errorMessages.add(errorMessage);
        }

        public void warning(SAXParseException e) {
            String errorMessage = "Validation warning in <" + e.getSystemId()
                    + ">: " + e.getMessage() + "\n";

            errorMessages.add(errorMessage);
        }

        public Vector<String> getErrors() {
            return errorMessages;
        }
    }

    public static class SchemaContentHandler implements ContentHandler {
        public static String currentClass;
        public ClassLoader loader = new ComponentLoader();
        private HashMap<String, String> classPathMap =
                new HashMap<String, String>();
        public static StringBuffer currentMSB = null;
        public static StringBuffer currentObs = null;
        boolean readingMSB = false;
        boolean readingObs = false;
        boolean getContents = false;

        public SchemaContentHandler() {
            initClasspaths();
        }

        private void initClasspaths() {
            classPathMap.put("SpProg", "gemin.sp.");

            classPathMap.put("SpTelescopeObsComp", "gemini.sp.obsComp.");
            classPathMap.put("SpSchedConstObsComp", "gemini.sp.obsComp.");
            classPathMap.put("SpSiteQualityObsComp", "gemini.sp.obsComp.");

            classPathMap.put("SpAnd", "gemini.sp.");
            classPathMap.put("SpOR", "gemini.sp.");
            classPathMap.put("SpMSB", "gemini.sp.");
            classPathMap.put("SpObs", "gemini.sp.");
            classPathMap.put("SpNote", "gemini.sp.");

            classPathMap.put("SpIterFolder", "gemini.sp.iter.");
            classPathMap.put("SpIterRepeat", "gemini.sp.iter.");
            classPathMap.put("SpIterPOL", "orac.jcmt.iter.");
            classPathMap.put("SpIterOffset", "gemini.sp.iter.");
            classPathMap.put("SpIterChop", "gemini.sp.iter.");

            classPathMap.put("SpIterFocusObs", "orac.jcmt.iter.");
            classPathMap.put("SpIterJiggleObs", "orac.jcmt.iter.");
            classPathMap.put("SpIterNoiseObs", "orac.jcmt.iter.");
            classPathMap.put("SpIterStareObs", "orac.jcmt.iter.");
            classPathMap.put("SpIterPointingObs", "orac.jcmt.iter.");
            classPathMap.put("SpIterRasterObs", "orac.jcmt.iter.");
            classPathMap.put("SpIterSkydipObs", "orac.jcmt.iter.");
        }

        public void startElement(String namespace, String localName,
                String qName, Attributes attr) {
            if (localName.equals("SpMSB")) {
                currentMSB = null;
                currentObs = null;
                readingMSB = true;
            } else if (localName.equals("SpObs")) {
                currentObs = null;
                readingObs = true;
            }

            if (localName.startsWith("Sp")) {
                currentClass = localName;

                try {
                    SpItem myClass = (SpItem) loader.loadClass(
                            classPathMap.get(localName) + localName)
                            .newInstance();
                    String readableName = myClass.type().getReadable();
                    currentClass = readableName;
                } catch (ClassNotFoundException e) {
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                }

            } else if (localName.equals("title")
                    && (readingMSB || readingObs)) {
                getContents = true;
            }
        }

        public void characters(char[] ch, int start, int length) {
            if (getContents) {
                if (readingMSB) {
                    if (currentMSB == null) {
                        currentMSB = new StringBuffer();
                    }

                    currentMSB.append(ch, start, length);

                } else if (readingObs) {
                    if (currentObs == null) {
                        currentObs = new StringBuffer();
                    }

                    currentObs.append(ch, start, length);
                }
            }
        }

        public void endDocument() {
        }

        public void endElement(String namespace, String localName,
                String qName) {
            if (getContents && localName.equals("title")) {
                getContents = false;

                if (readingMSB) {
                    readingMSB = false;
                }

                if (readingObs) {
                    readingObs = false;
                }
            }
        }

        public void endPrefixMapping(String prefix) {
        }

        public void ignorableWhitespace(char[] ch, int start, int length) {
        }

        public void processingInstruction(String target, String data) {
        }

        public void setDocumentLocator(Locator l) {
        }

        public void skippedEntity(String name) {
        }

        public void startDocument() {
        }

        public void startPrefixMapping(String prefix, String uri) {
        }

        public static String getCurrentClass() {
            return currentClass;
        }

        public static String getCurrentMSB() {
            if (currentMSB != null) {
                return currentMSB.toString();
            }

            return "";
        }

        public static String getCurrentObs() {
            if (currentObs != null) {
                return currentObs.toString();
            }

            return "";
        }
    }

    public static class ComponentLoader extends ClassLoader {
    }

    public void checkSchedComp(SpSchedConstObsComp schedule,
            Vector<ErrorMessage> report) {
        checkStartEndTimes(schedule, report);
        maxGreaterThanMin(schedule, report);
    }

    public void maxGreaterThanMin(SpSchedConstObsComp schedule,
            Vector<ErrorMessage> report) {
        String min = schedule.getMinElevation();
        String max = schedule.getMaxElevation();

        try {
            double minEl = new Double(min);
            double maxEl = new Double(max);

            if (minEl > maxEl) {
                report.add(new ErrorMessage(
                        ErrorMessage.ERROR,
                        "Min elevation in schedule constraint is greater"
                                + " than max elevation",
                        ""));
            }

        } catch (NumberFormatException nfe) {
            report.add(new ErrorMessage(ErrorMessage.ERROR,
                    "Values for elevation in schedule constraint not numeric.",
                    ""));

        } catch (NullPointerException npe) {
            report.add(new ErrorMessage(ErrorMessage.ERROR,
                    "No values for elevation in schedule constraint.", ""));
        }
    }

    public void checkStartEndTimes(SpSchedConstObsComp schedule,
            Vector<ErrorMessage> report) {
        String earliest = schedule.getEarliest();
        String latest = schedule.getLatest();
        Date before = OracUtilities.parseISO8601(earliest);
        Date after = OracUtilities.parseISO8601(latest);

        if (before.after(after)) {
            report.add(new ErrorMessage(
                    ErrorMessage.WARNING,
                    "The earliest scheduled start time is after the latest"
                            + " scheduled end time",
                    ""));
        }

        Calendar latestCalendar = Calendar.getInstance();
        latestCalendar.set(Calendar.YEAR, 2035);
        latestCalendar.set(Calendar.MONTH, Calendar.JANUARY);
        latestCalendar.set(Calendar.DAY_OF_MONTH, 1);
        latestCalendar.set(Calendar.HOUR_OF_DAY, 1);
        latestCalendar.set(Calendar.MINUTE, 0);
        latestCalendar.set(Calendar.SECOND, 0);

        if (after.before(TimeUtils.getCurrentUTCDate())) {
            report.add(new ErrorMessage(
                    ErrorMessage.ERROR,
                    "The latest scheduled end time is before the"
                            + " current UTC date",
                    ""));
        }

        if (after.after(latestCalendar.getTime())) {
            report.add(new ErrorMessage(
                    ErrorMessage.ERROR,
                    "The latest scheduled end time is after the"
                            + " latest schedulable end time",
                    ""));
        }
    }
}
