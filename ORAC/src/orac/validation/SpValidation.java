package orac.validation;

import java.util.Vector;
import java.util.Enumeration;
import java.io.File;
import gemini.sp.SpProg;
import gemini.sp.SpObs;
import gemini.sp.SpMSB;
import gemini.sp.SpItem;
import gemini.sp.SpNote;
import gemini.sp.SpTelescopePosList;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.obsComp.SpSiteQualityObsComp;
import gemini.sp.obsComp.SpSchedConstObsComp;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.util.TelescopePos;
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
import java.io.IOException;
import java.io.StringReader;
import java.lang.Class;
import java.lang.ClassLoader;
import java.lang.Object;
import java.util.HashMap;

/**
 * Validation Tool.
 * 
 * This class is used for checking whether the values and settings in a Science Program or
 * Observation are sensible.
 *
 * Errors and warnings are issued otherwise.
 *
 * The class contains a main method and can be used as a stand alone command line tool to validate science programs.
 *
 * @author M.Folger@roe.ac.uk UKATC
 */
public class SpValidation {
  public void checkSciProgram(SpProg spProg, Vector report) {
    if(report == null) {
      report = new Vector();
    }

    // Change the SpProg to a document, since this is more easily checked
    String xmlStr = spProg.toXML();
    DOMParser parser = new DOMParser();
    Document doc = null;
    try {
	// Turn off validation
	parser.setFeature("http://xml.org/sax/features/validation", false);
	parser.parse(new InputSource (new StringReader(xmlStr)));
	doc = parser.getDocument();
    }
    catch (IOException e) {
	System.out.println("Unable to read string");
    }
    catch (SAXException e) {
	System.out.println("Unable to create document: "+e.getMessage());
    }

    checkSciProgramRecursively(spProg, report);

    if (doc != null) {
	// I use one method for each 'rule'
	// Rule 1: Each SpObs and MSB must either contain the following
	//         or inherit it from higher up:
	//         SpInst...
        //         SpSiteQualityObsComp
        //         SptelescopeObsComp
 	checkInheritedComponents(doc, "SpMSB", "SpTelescopeObsComp", "Target Information", report);
	checkInheritedComponents(doc, "SpMSB", "SpSiteQualityObsComp", "Site Quality", report);
	checkInheritedComponents(doc, "SpMSB", "SpInst", "Instrument", report);
	checkInheritedComponents(doc, "SpObs", "SpTelescopeObsComp", "Target Information", report);
	checkInheritedComponents(doc, "SpObs", "SpSiteQualityObsComp", "Site Quality", report);
	checkInheritedComponents(doc, "SpObs", "SpInst", "Instrument", report);

	// Rule2: Each iterator must contain either another iterator or an observe eye
	//        Exception: For UKIRT, the offset iterator does not have this requirement
	checkIterator(doc, "SpIterFolder", "Sequence Iterator",report);
	checkIterator(doc, "SpIterChop", "Chop Iterator", report);
	checkIterator(doc, "SpIterPOL", "POL Iterator", report);
        checkIterator(doc, "SpIterRepeat", "Repeat Iterator", report);
	if (System.getProperty("ot.cfgdir").indexOf("ukirt") == -1) {
	    checkIterator(doc, "SpIterOffset", "Offset Iterator",report);
	}
	checkIterator(doc, "SpIterFrequency", "Frequency Iterator", report);

	// Other rules:
	//    SpAND must contain an SpObs or SpMSB but NOT and SpOR
	//    SpOR must contain and SpObs, SpMSB or SpOR
	//    SpMSB must contain an SpObs
	//    SpObs must contain only 1 sequence
	Vector mandatoryChildren = new Vector();
	Vector excludedChildren  = new Vector();
	mandatoryChildren.add("SpObs");
	mandatoryChildren.add("SpMSB");
	excludedChildren.add("SpOR");
	excludedChildren.add("SpAND");
	checkForChildren(doc, "SpAND", mandatoryChildren, excludedChildren, report);
	mandatoryChildren.add("SpAND");
	excludedChildren.clear();
	excludedChildren.add("SpOR");
	checkForChildren(doc, "SpOR", mandatoryChildren, excludedChildren, report);
	mandatoryChildren.clear();
	mandatoryChildren.add("SpObs");
	checkForChildren(doc, "SpMSB", mandatoryChildren, null, report);
    }
  }

  protected void checkSciProgramRecursively(SpItem spItem, Vector report) {
    Enumeration children = spItem.children();
    SpItem child;

    while(children.hasMoreElements()) {
      child = (SpItem)children.nextElement();

      if(child instanceof SpMSB) {
        checkMSB((SpMSB)child, report);
      }
      else {
        checkSciProgramRecursively(child, report);
      }
    }
  }

  public void checkMSB(SpMSB spMSB,  Vector report) {

    // Check for observation components (target information, instrument,
    // site quality, scheduling constraints

//     if(SpTreeMan.findTargetList(spMSB) == null) {
//       report.add(new ErrorMessage(ErrorMessage.ERROR,
//                                   "MSB \"" + spMSB.getTitle() + "\"",
//                                   "Target information is missing.")); 
//     }

    checkMSBgeneric(spMSB, report);
  }

  protected void checkMSBgeneric(SpMSB spMSB,  Vector report) {
      /*MFO DEBUG*///System.out.println("in SpValidation.checkMSBgeneric().");

    report.add("------------------------------------------------------\n");

    if(report == null) {
      report = new Vector();
    }

    if(SpItemUtilities.findSiteQuality(spMSB) == null) {
      report.add(new ErrorMessage(ErrorMessage.ERROR,
                                  "MSB \"" + spMSB.getTitle() + "\"",
                                  "Site quality component list is missing."));
    }


    // Check whether there are more than one observe instruction notes.
    Vector notes = SpTreeMan.findAllItems(spMSB, SpNote.class.getName());

    boolean observeNoteFound = false;
    int     numberOfObserveInstructions = 0;

    // How may notes are inside the MSB and are any of then observer instructions
    if (notes.size() > 0) {
	for (int i = 0; i < notes.size(); i++) {
	    if(((SpNote)notes.get(i)).isObserveInstruction()) {
		numberOfObserveInstructions++;
		observeNoteFound = true;
	    }
	}
    }
    // If we havent found an observer note, go through the parents and check if there is one there
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

    if (numberOfObserveInstructions > 1) {
	report.add(new ErrorMessage(ErrorMessage.ERROR,
				    "MSB \"" + spMSB.getTitle() + "\" has >1 observer note associated with it.",
				    "Each MSB can only contain one note that has \"Show to the Observer\" ticked."));
    }
    if (!observeNoteFound) {
	report.add(new ErrorMessage (ErrorMessage.WARNING,
				     "MSB \"" + spMSB.getTitle() + "\" does not have an Show-to Observer Note",
				     "Recommend MSBs have an observe instruction"));
    }


    if(spMSB instanceof SpObs) {
      checkObservation((SpObs)spMSB, report);
    }
    else {

      Enumeration children = spMSB.children();
      SpItem child;

      while(children.hasMoreElements()) {
        child = (SpItem)children.nextElement();

        if(child instanceof SpObs) {
          checkObservation((SpObs)child, report);
        }  
      }
    }
  }

  public void checkObservation(SpObs spObs,  Vector report) {
    if(report == null) {
      report = new Vector();
    }

    Enumeration children;
    SpItem child;

    if(SpTreeMan.findInstrument(spObs) == null) {
      report.add(new ErrorMessage(ErrorMessage.ERROR,
                                  "Observation \"" + spObs.getTitle() + "\"",
                                  "Instrument component is missing."));    
    }

    checkTargetList(SpTreeMan.findTargetList(spObs), report);

    if(!spObs.isMSB()) {
      children = spObs.children();

      while(children.hasMoreElements()) {
        child = (SpItem)children.nextElement();
        if(child instanceof SpSiteQualityObsComp) {
          report.add(new ErrorMessage(ErrorMessage.ERROR,
                                      "Observation \"" + spObs.getTitle() + "\"",
                                      "Observations inside MSBs must use the site quality component of the parent MSB."));
	}

        if(child instanceof SpSchedConstObsComp) {
          report.add(new ErrorMessage(ErrorMessage.ERROR,
                                      "Observation \"" + spObs.getTitle() + "\"",
                                      "Observations inside MSBs must use the scheduling constraints component of the parent MSB."));
	}
      }
    }
  }

    private void checkInheritedComponents(Document doc, 
					  String folderName,
					  String componentName,
					  String description,
					  Vector report) {
	String title = null;
	// Check all the children of the top level element
	Element root = doc.getDocumentElement();
	// Each MSB and SpObs must contain an SpTelescopeObsComp, or inherit it from a parent.
	// Check each one for this
	NodeList nl = root.getElementsByTagName(folderName);
	boolean  found = false;
	for (int i=0; i<nl.getLength(); i++) {
	    Node thisNode = nl.item(i);
	    // For each MSB check if we have the required info.
	    NodeList requiredTag = ((Element)thisNode).getElementsByTagName("*");
	    title = null;
	    if (requiredTag != null) {
		for (int j=0; j<requiredTag.getLength(); j++) {
		    if (requiredTag.item(j).getNodeName().equalsIgnoreCase("title")) {
			title = requiredTag.item(j).getFirstChild().getNodeValue();
// 			System.out.println("Title of folder = "+title);
		    }
		    if (requiredTag.item(j).getNodeName().startsWith(componentName)) {
			found = true;
		    }
		}
	    }
	    if (!found) {
		// Check whether any of the proginy contain an autoTarget flag.  In this case, we have a calibration,
		// so we are OK
		NodeList autoTargets = ((Element)thisNode).getElementsByTagName("autoTarget");
		if (autoTargets != null && autoTargets.getLength() > 0) {
		    found = true;
		}
	    }
	    if (!found) {
		// We don't so recuse up the document tree to see if any parents have it
		while ((thisNode = thisNode.getParentNode()) != null && !found) {
		    NodeList childList = thisNode.getChildNodes();
		    for (int child=0; child<childList.getLength(); child++) {
			if ( childList.item(child).getNodeName().startsWith(componentName)) {
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
	    }
	    else {
		if (title != null) {
		    report.add(new ErrorMessage (ErrorMessage.ERROR,
						 "MSB or Observation",
						 "MSB/Observation "+title+" does not contain or inherit a "+description));
		}
		else {
		    report.add(new ErrorMessage (ErrorMessage.ERROR,
						 "MSB or Observation",
						 "Unnamed MSB/Observation does not contain or inherit a "+description));
		}
		break;
	    }
	}
    }

    private void checkIterator(Document doc, String iteratorType, String description, Vector report) {
	boolean found=false;
	// Get the root element
	Element root = doc.getDocumentElement();
	// Get all the nodes which we are going to check
	NodeList nl = root.getElementsByTagName(iteratorType);
	// For each of these, get all of its children
	for (int i=0; i<nl.getLength(); i++) {
	    NodeList children = nl.item(i).getChildNodes();
	    // Check each child looking for another SpIter...
	    for (int j=0; j<children.getLength(); j++) {
		if (children.item(j).getNodeName().startsWith("SpIter")) {
		    found = true;
		    break;
		}
	    }
	    if (found) {
		found = false;
		continue;
	    }
	    else {
		report.add (new ErrorMessage(ErrorMessage.ERROR,
					    description,
					    "A "+description+" does not contain another iterator or observe"));
		break;
	    }
	}
    }

    private void checkForChildren (Document  doc,
				   String    component, // Tag name of child to check
				   Vector    mandatory, // The tag must have at least one of these types
				   Vector    excluded,  // List of children that are prohibited from appearing in the tag
				   Vector    report     // Errors returned through this
				   ) {
	boolean mandatoryElementFound = false;
	boolean excludedElementFound  = false;

	Element root = doc.getDocumentElement();
	// Het all of the required components
	NodeList nl = root.getElementsByTagName(component);
	// For each of these, make sure we have at least one mandatory tag
	// as a direct child, and that the exclusion does not appear
	for (int i=0; i<nl.getLength(); i++) {
	    NodeList children = nl.item(i).getChildNodes();
	    for (int j=0; j<children.getLength(); j++) {
		// Check if this is a madatory tag
		if (mandatory != null) {
		    for (int tag=0; tag<mandatory.size(); tag++) {
			if (children.item(j).getNodeName().equals((String)mandatory.elementAt(tag))) {
			    mandatoryElementFound = true;
			    break;
			}
		    }
		}
		if (excluded != null) {
		    for (int tag=0; tag<excluded.size(); tag++) {
			if (children.item(j).getNodeName().equals((String)excluded.elementAt(tag))) {
			    excludedElementFound = true;
			}
		    }
		}
	    }
	    if (mandatoryElementFound && !excludedElementFound) {
		// This is OK, so reset and go to the next in the list
		mandatoryElementFound = false;
		excludedElementFound  = false;
		continue;
	    }
	    else {
		// Something is wrong
		if (!mandatoryElementFound) {
		    String message = component.substring(2)+" does not contain at least one of { ";
		    for (int tag=0; tag<mandatory.size(); tag++) {
			message = message + ((String)mandatory.elementAt(tag)).substring(2)+" ";
		    }
		    message = message + "}";
		    report.add(new ErrorMessage(ErrorMessage.ERROR,
						component,
						message));
		    break;
		}
		else {
		    String message = component.substring(2)+" contains one of { ";
		    for (int tag=0; tag<excluded.size(); tag++) {
			message = message + ((String)excluded.elementAt(tag)).substring(2)+" ";
		    }
		    message = message + "} which is not allowed";
		    report.add(new ErrorMessage(ErrorMessage.ERROR,
						component,
						message));
		    break;
		}
	    }
	}
    }

    private void checkTargetList (SpTelescopeObsComp obsComp, Vector report) {
	if ( obsComp == null ) {
	    report.add ( new ErrorMessage (ErrorMessage.ERROR, "Observation has no target component", "") );
	    return;
	}

	SpTelescopePosList list = obsComp.getPosList();
	TelescopePos [] position = list.getAllPositions();
	double [] values = null;
	double deg, min, sec;

	for ( int i=0; i<position.length; i++ ) {
	    SpTelescopePos pos = (SpTelescopePos)position[i];
	    if ( (pos.getSystemType() == SpTelescopePos.SYSTEM_SPHERICAL) && (pos.getXaxis() == 0) && (pos.getYaxis() == 0) ) {
		report.add ( new ErrorMessage ( ErrorMessage.WARNING,
			                       "Telescope target " + pos.getName(),
			                       "Both Dec and RA are 0:00:00"));
	    }
	}
	return;
    }
			

    /**
     * Perform validation against a schema.  The SpItem class already writes out
     * a part of the default namesapce in its toXML method.  In this class we
     * apply the actual location of the schema.  A SAX parser is used to parse
     * the string.
     * @param xmlString  The current XML representation of the science project
     * @param schema     Fully qualified file name for the schema.
     * @return           A vector of strings containing the validation errors
     */
    public void schemaValidate(String xmlString, String schemaURL, String schema, Vector report) {
	// Make sure the schema exists
	if ( schema == null ) {
	    report.add( new ErrorMessage(ErrorMessage.WARNING,
					 "Schema Validation not performed",
					 "Unable to locate both web service and local version"));
	    return;
	}
	String schemaLoc = new String (schemaURL + " " + schema);
	SAXParser parser = new SAXParser();
	SchemaErrorHandler handler = new SchemaErrorHandler();
        SchemaContentHandler contentHandler = new SchemaContentHandler();
	parser.setErrorHandler(handler);
	parser.setContentHandler(contentHandler);
// 	Document doc = null;
	try {
    	    parser.setFeature("http://xml.org/sax/features/validation", true);
   	    parser.setFeature("http://apache.org/xml/features/validation/schema", true);
   	    parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
	    parser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", schemaLoc );
	    parser.parse(new InputSource(new StringReader(xmlString)));
	}
	catch (SAXNotRecognizedException e) {
	    System.out.println("Unrecognized feature");
	}
	catch (SAXNotSupportedException e) {
	    System.out.println("Unsupported feature");
	}
	catch (IOException e) {
	    System.out.println("Unable to read string");
	}
	catch (SAXException e) {
	    System.out.println("Unable to create document: "+e.getMessage());
	}

	Vector schemaErrors = handler.getErrors();
	for (int i=0; i<schemaErrors.size(); i++) {
	    report.add (new ErrorMessage (ErrorMessage.ERROR,
					  "Schema validation error",
					  (String)schemaErrors.get(i)));
	}
	return;
    }

    public class SchemaErrorHandler implements ErrorHandler {
	public Vector errorMessages = new Vector();

	public void error(SAXParseException e) {
// 	    String errorMessage = "Validation error in <"+SchemaContentHandler.getCurrentClass()+">: "+e.getMessage()+"\n";
	    String errorMessage = "Validation error in MSB<"+SchemaContentHandler.getCurrentMSB()+">, Obs:<"+SchemaContentHandler.getCurrentObs()+">, component<"+SchemaContentHandler.getCurrentClass()+">: message:<"+e.getMessage()+">\n";
	    errorMessages.add(errorMessage);
	}
	public void fatalError(SAXParseException e) {
	    String errorMessage = "Fatal parse error in <"+e.getSystemId()+">: "+e.getMessage()+"\n";
	    errorMessages.add(errorMessage);
	}
	public void warning(SAXParseException e) {
	    String errorMessage = "Validation warning in <"+e.getSystemId()+">: "+e.getMessage()+"\n";
	    errorMessages.add(errorMessage);
	}

	public Vector getErrors() {
	    return errorMessages;
	}
    }

    public static class SchemaContentHandler implements ContentHandler {
	public static String currentClass;
	public ClassLoader loader = new ComponentLoader();
	private HashMap classPathMap = new HashMap();
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

	    classPathMap.put("SpInstSCUBA", "orac.jcmt.inst.");
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
	    classPathMap.put("SpIterFrequency", "orac.jcmt.iter.");
	    classPathMap.put("SpIterChop", "gemini.sp.iter.");

	    classPathMap.put("SpIterFocusObs", "orac.jcmt.iter.");
	    classPathMap.put("SpIterJiggleObs", "orac.jcmt.iter.");
	    classPathMap.put("SpIterNoiseObs", "orac.jcmt.iter.");
	    classPathMap.put("SpIterStareObs", "orac.jcmt.iter.");
	    classPathMap.put("SpIterPointingObs", "orac.jcmt.iter.");
	    classPathMap.put("SpIterRasterObs", "orac.jcmt.iter.");
	    classPathMap.put("SpIterSkydipObs", "orac.jcmt.iter.");
	}

	public void startElement(String namespace,
				 String localName,
				 String qName,
				 Attributes attr) {
	    if ( localName.equals("SpMSB") ) {
		currentMSB = null;
		currentObs = null;
		readingMSB = true;
	    }
	    else if ( localName.equals("SpObs") ) {
		currentObs = null;
		readingObs = true;
	    }
	    if (localName.startsWith("Sp")) {
		currentClass = localName;
		try {
		    SpItem myClass = (SpItem)loader.loadClass((String)classPathMap.get(localName)+localName).newInstance();
		    String readableName = myClass.type().getReadable();
		    currentClass = readableName;
		}
		catch (java.lang.ClassNotFoundException e) {
// 		    System.out.println("Class not found: "+localName);
		}
		catch (java.lang.InstantiationException e) {
// 		    System.out.println("Unable to instantiate "+localName);
		}
		catch (java.lang.IllegalAccessException e) {
// 		    System.out.println("Unable to access class "+localName);
		}
	    }
	    else if ( localName.equals("title") && (readingMSB || readingObs ) ) {
		getContents = true;
	    }
	}
	public void characters (char [] ch, int start, int length){
	    if ( getContents ) {
		if ( readingMSB ) {
		    if ( currentMSB == null ) {
			currentMSB = new StringBuffer ();
		    }
		    currentMSB.append(ch, start, length);
		}
		else if ( readingObs ) {
		    if ( currentObs == null ) {
			currentObs = new StringBuffer () ;
		    }
		    currentObs.append( ch, start, length );
		}
	    }
	};
	public void endDocument() {};
	public void endElement (String namespace, String localName, String qName) {
	    if ( getContents && localName.equals("title") ) {
		getContents = false;
		if ( readingMSB ) {
		    readingMSB = false;
		}
		if ( readingObs ) {
		    readingObs = false;
		}
	    }
	};
        public void endPrefixMapping(String prefix) {};
	public void ignorableWhitespace(char [] ch, int start, int length){};
	public void processingInstruction(String target, String data) {};
        public void setDocumentLocator(Locator l) {};
	public void skippedEntity(String name) {};
        public void startDocument() {};
        public void startPrefixMapping(String prefix, String uri) {};
	public static String getCurrentClass() {
	    return currentClass;
	}
	public static String getCurrentMSB () {
	    if ( currentMSB != null ) {
		return currentMSB.toString();
	    }
	    return "";
	}
	public static String getCurrentObs () {
	    if ( currentObs != null ) {
		return currentObs.toString();
	    }
	    return "";
	}
    }

    public static class ComponentLoader extends ClassLoader {
	
    }


}

