package orac.validation;

import java.util.Vector;
import java.util.Enumeration;
import java.io.File;
import gemini.sp.SpProg;
import gemini.sp.SpObs;
import gemini.sp.SpMSB;
import gemini.sp.SpItem;
import gemini.sp.SpNote;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpSiteQualityObsComp;
import gemini.sp.obsComp.SpSchedConstObsComp;
import gemini.sp.obsComp.SpInstObsComp;
import orac.util.SpItemUtilities;

import org.apache.xerces.parsers.SAXParser;
// import org.w3c.dom.Document;
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

    checkSciProgramRecursively(spProg, report);
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

    if(SpTreeMan.findTargetList(spMSB) == null) {
      report.add(new ErrorMessage(ErrorMessage.ERROR,
                                  "MSB \"" + spMSB.getTitle() + "\"",
                                  "Target information is missing.")); 
    }

    checkMSBgeneric(spMSB, report);
  }

  protected void checkMSBgeneric(SpMSB spMSB,  Vector report) {
/*MFO DEBUG*/System.out.println("in SpValidation.checkMSBgeneric().");

    report.add("------------------------------------------------------\n");

    if(report == null) {
      report = new Vector();
    }

    if(SpTreeMan.findInstrument(spMSB) == null) {
      report.add(new ErrorMessage(ErrorMessage.ERROR,
                                  "MSB \"" + spMSB.getTitle() + "\"",
                                  "Instrument component is missing."));    
    }

    if(SpItemUtilities.findSiteQuality(spMSB) == null) {
      report.add(new ErrorMessage(ErrorMessage.ERROR,
                                  "MSB \"" + spMSB.getTitle() + "\"",
                                  "Site quality component list is missing."));
    }


    // Check whether there are more than one observe instruction notes.
    Vector notes = SpTreeMan.findAllItems(spMSB, SpNote.class.getName());

    if(notes.size() > 1) {
      int numberOfObserveInstructions = 0;

      for(int i = 0; i < notes.size(); i++) {
        if(((SpNote)notes.get(i)).isObserveInstruction()) {
          numberOfObserveInstructions++;

          if(numberOfObserveInstructions > 1) {
            report.add(new ErrorMessage(ErrorMessage.ERROR,
                                        "Note \"" + ((SpNote)notes.get(i)).getTitle() + "\" in MSB \"" + spMSB.getTitle() + "\"",
                                        "Each MSB can only contain one note that has \"Show to the Observer\" ticked."));
          }
        }
      }
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

    /**
     * Perform validation against a schema.  The SpItem class already writes out
     * a part of the default namesapce in its toXML method.  In this class we
     * apply the actual location of the schema.  A SAX parser is used to parse
     * the string.
     * @param xmlString  The current XML representation of the science project
     * @param schema     Fully qualified file name for the schema.
     * @return           A vector of strings containing the validation errors
     */
    public Vector schemaValidate(String xmlString, String schema) {
	// Make sure the schema exists
	File schemaFile = new File(schema);
	if (! schemaFile.exists()) {
	    Vector v = new Vector();
	    v.add("Specified schema file "+schema+" does not exist.  No schema validation performed.");
	    return v;
	}
	SAXParser parser = new SAXParser();
	SchemaErrorHandler handler = new SchemaErrorHandler();
        SchemaContentHandler contentHandler = new SchemaContentHandler();
	parser.setErrorHandler(handler);
	parser.setContentHandler(contentHandler);
// 	Document doc = null;
	try {
    	    parser.setFeature("http://xml.org/sax/features/validation", true);
   	    parser.setFeature("http://apache.org/xml/features/validation/schema", true);
	    parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
			       schema);
	    parser.parse(new InputSource(new StringReader(xmlString)));
// 	    doc = parser.getDocument();
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
	return handler.getErrors();
    }

    public class SchemaErrorHandler implements ErrorHandler {
	public Vector errorMessages = new Vector();

	public void error(SAXParseException e) {
	    String errorMessage = "Validation error in <"+SchemaContentHandler.getCurrentClass()+">: "+e.getMessage()+"\n";
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
	}
	public void characters (char [] ch, int start, int length){};
	public void endDocument() {};
	public void endElement (String namespace, String localName, String qName) {};
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
    }

    public static class ComponentLoader extends ClassLoader {
	
    }


}

