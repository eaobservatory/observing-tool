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

import orac.util.PreTranslator;
import gemini.sp.SpTelescopePos;
import java.util.Vector;
import org.apache.xerces.dom.ElementImpl;
import org.w3c.dom.NodeList;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.DocumentImpl;

/**
 * XML (DOM) PreTranslator for UKIRT and JCMT.
 *
 * Changes DOM elements of the {@link orac.util.SpItemDOM} that are related to
 * telescope control to/from TCS style xml.
 *
 * The TCS (Telescope Control System) is part of the
 * JAC Observation Control System project OCS/ICD/006.6.
 *
 * The pre-translater classes for UKIRT and JCMT are subclasses of this TcsPreTranslator.
 * If the OT is used for other telescopes which do not use JAC OCS TCS then
 * the interface {@link orac.util.PreTranslator} can be implemented directly.
 *
 * @author Martin Folger
 */
public abstract class TcsPreTranslator implements PreTranslator {
  protected String EXCECPTION_MESSAGE_PREFIX = "Problem pre-translating xml: ";

  /**
   * Telescope specific target types.
   */
  private static String [] TELESCOPE_TARGET_TAGS;

  /**
   * Target types used by the JAC OCS TCS.
   */
  protected abstract String [] getTcsTargetTypes();

  public TcsPreTranslator() throws Exception {
    // Set TELESCOPE_TARGET_TAGS
    TELESCOPE_TARGET_TAGS = new String[2];

    String baseTag = SpTelescopePos.getBaseTag();
    if((baseTag == null) || (baseTag.equals(""))) {
      throw new Exception(EXCECPTION_MESSAGE_PREFIX + "No base or science tag found.");
    }

    String [] guideStarTags = SpTelescopePos.getGuideStarTags();
    if((guideStarTags    == null) || (guideStarTags.length < 1) ||
       (guideStarTags[0] == null) || (guideStarTags[0].equals(""))) {

      throw new Exception(EXCECPTION_MESSAGE_PREFIX + "No guide star or reference tag found.");
    }

    TELESCOPE_TARGET_TAGS[0] = baseTag;
    TELESCOPE_TARGET_TAGS[1] = guideStarTags[0];
  }

  /**
   * @param  element DOM element from {@link orac.util.SpItemDOM}
   */
  public void translate(ElementImpl element) throws Exception {
    if((getTcsTargetTypes()  == null) || (getTcsTargetTypes().length  != 2) ||
       (TELESCOPE_TARGET_TAGS == null) || (TELESCOPE_TARGET_TAGS.length != 2)) {

      throw new Exception(EXCECPTION_MESSAGE_PREFIX +
                      "\n2 TCS target types and 2 telescope target tags needed for translation.");
    }

    translateAllTargetInformation(element);
  }

  /**
   * @param  element DOM element from {@link orac.util.SpItemDOM}
   */  
  public void reverse(ElementImpl element) throws Exception {
    if((getTcsTargetTypes()  == null) || (getTcsTargetTypes().length  != 2) ||
       (TELESCOPE_TARGET_TAGS == null) || (TELESCOPE_TARGET_TAGS.length != 2)) {

      throw new Exception(EXCECPTION_MESSAGE_PREFIX +
                      "\n2 TCS target types and 2 telescope target tags needed for reverse translation.");
    }

    reverseAllTargetInformation(element);
  }

  /**
   * Converts DOM/XML representaion of SpTelescopeObsComp.
   *
   * From: DOM Element based on XML generated from SpAvTable in SpTelescopeObsComp (UKIRT)
   * To:   DOM Element based on XML as used in the TCS.
   *
   * The DTD/XML used in the TCS is based on the Gemini Phase 1 Tool.
   * See document OCS/ICD/006.2 (Except: Element target contains (science | guide))
   *
   * This method is UKIRT specific. It will need adjusting for JCMT. But JCMT will eith have a
   * telescope observation component which produces the TCS style XML and does not need
   * this conversion or this method will need modification.
   *
   * @see #reverseTargetInformation(org.apache.xerces.dom.ElementImpl)
   */
  private void translateTargetInformation(ElementImpl element) throws Exception {
    // TODO: In case their is going to be a different SpTelescopeObsComp for JCMT (from another package)
    // which produces TCS style XML anyway, then the the following if should be more precise so that
    // the conversion would not be applied in such case.
    if(!element.getTagName().equals("SpTelescopeObsComp")) {
      return;
    }

    try {
      DocumentImpl document = (DocumentImpl)element.getOwnerDocument();

      // Elements before conversion.
      ElementImpl targetListElement;
      NodeList    valueList;
    
      // Converted elements.
      ElementImpl baseElement = null;
      ElementImpl child;
    
      for(int i = 0; i < TELESCOPE_TARGET_TAGS.length; i++) {
        targetListElement = (ElementImpl)element.getElementsByTagName(TELESCOPE_TARGET_TAGS[i]).item(0);
      
        if(targetListElement != null) {
          valueList = targetListElement.getElementsByTagName("value");

          // Don't confuse TCS tag "Base" (JCMT speak) with Ukirt tag "Base" (as in "Base vs GUIDE")
          baseElement = (ElementImpl)document.createElement("base");

          // Add target element and add type attribute.
          child = (ElementImpl)baseElement.appendChild(document.createElement("target"));
          child.setAttribute("type", getTcsTargetTypes()[i]);

          // Add targetName element with text node containing the target name.
          child = (ElementImpl)child.appendChild(document.createElement("targetName"));
          child.appendChild(document.createTextNode(valueList.item(SpTelescopePos.NAME_INDEX).getFirstChild().getNodeValue()));

          // Set child to target element again.
          child = (ElementImpl)child.getParentNode();

          // Add hmsdegSystem element and add type attribute.
          child = (ElementImpl)child.appendChild(document.createElement("hmsdegSystem"));
          String system = valueList.item(SpTelescopePos.COORD_SYS_INDEX).getFirstChild().getNodeValue();
	  if(system.equals("FK4 (B1950)")) system = "B1950";
	  if(system.equals("FK5 (J2000)")) system = "J2000";
	  child.setAttribute("type", system);

          // Add c1 target with text node containing RA.
          child = (ElementImpl)child.appendChild(document.createElement("c1"));
          child.appendChild(document.createTextNode(valueList.item(SpTelescopePos.XAXIS_INDEX).getFirstChild().getNodeValue()));

          // Set child to hmsdegSystem element again.
          child = (ElementImpl)child.getParentNode();

          // Add c2 target with text node containing Dec.
          child = (ElementImpl)child.appendChild(document.createElement("c2"));
          child.appendChild(document.createTextNode(valueList.item(SpTelescopePos.YAXIS_INDEX).getFirstChild().getNodeValue()));
    
          // Replace old target list element (UKIRT/Sp style value array)
          // with new base element (TCS XML)
          element.replaceChild(baseElement, targetListElement);
        }	
      }

      // Deal with chop parameters
      if(element.getElementsByTagName("chopping").item(0) != null) {
        if(element.getElementsByTagName("chopping").item(0).getFirstChild().getNodeValue().equals("true")) {
          ElementImpl chopElement = (ElementImpl)document.createElement("chop");

          if(element.getElementsByTagName("chopAngle").item(0) != null) {
	    child = (ElementImpl)element.removeChild(element.getElementsByTagName("chopAngle").item(0));
            child.setAttribute("units", "degrees");
            chopElement.appendChild(child);
          }  

          if(element.getElementsByTagName("chopThrow").item(0) != null) {
            child = (ElementImpl)element.removeChild(element.getElementsByTagName("chopThrow").item(0));
            child.setAttribute("units", "arcseconds");
            chopElement.appendChild(child);
	  }  

          if(element.getElementsByTagName("chopSystem").item(0) != null) {
            child = (ElementImpl)element.removeChild(element.getElementsByTagName("chopSystem").item(0));
            chopElement.appendChild(child);
          }

          element.appendChild(chopElement);
        }
        else {
          element.removeChild(element.getElementsByTagName("chopAngle").item(0));
          element.removeChild(element.getElementsByTagName("chopThrow").item(0));
	  element.removeChild(element.getElementsByTagName("chopSystem").item(0));
        }

        element.removeChild(element.getElementsByTagName("chopping").item(0));
      }
    }
    // Make sure RuntimeExceptions are not ignored.
    catch(Exception e) {
      e.printStackTrace();
      throw new Exception("Problem while converting target information to TCS XML format.");
    }
  }

  /**
   * Converts DOM/XML representaion of SpTelescopeObsComp.
   *
   * From: DOM Element based on XML as used in the TCS.
   * To:   DOM Element based on XML generated from SpAvTable in SpTelescopeObsComp (UKIRT)
   *
   * The DTD/XML used in the TCS is based on the Gemini Phase 1 Tool.
   * See document OCS/ICD/006.2 (Except: Element target contains (science | guide))
   * 
   * This method is UKIRT specific. It will need adjusting for JCMT. But JCMT will eith have a
   * telescope observation component which produces the TCS style XML and does not need
   * this conversion or this method will need modification.
   *
   * @see #translateTargetInformation(org.apache.xerces.dom.ElementImpl)
   */
  private void reverseTargetInformation(ElementImpl element) throws Exception {
    // TODO: In case their is going to be a different SpTelescopeObsComp for JCMT (from another package)
    // which produces TCS style XML anyway, then the the following if should be more precise so that
    // the conversion would not be applied in such case.
    if(!element.getTagName().equals("SpTelescopeObsComp")) {
      return;
    }

    try {
      DocumentImpl document = (DocumentImpl)element.getOwnerDocument();

      // Elements before conversion.
      NodeList baseNodes = element.getElementsByTagName("base");
      ElementImpl baseElement;

      // Converted elements.
      ElementImpl targetListElement = null;
      String value = null;
      String type  = null;
      Vector targetElementVector = new Vector();
    
      // There should be one base node of type "science" and optionally another one of type "guide".
      for(int i = 0; i < baseNodes.getLength(); i++) {
        baseElement = (ElementImpl)baseNodes.item(i);

        // tag
	type = ((ElementImpl)baseElement.getElementsByTagName("target").item(0)).getAttribute("type");

	// ?? MFO
        if(type.equals(getTcsTargetTypes()[0])) {
          value = TELESCOPE_TARGET_TAGS[0];
        }
        else if(type.equals(getTcsTargetTypes()[1])) {
          value = TELESCOPE_TARGET_TAGS[1];
        }
        // The tag should always be one of getTcsTargetTypes() ("science", "guide") which
        // is then converted to the respective String in TELESCOPE_TARGET_TAGS ("Base", "GUIDE")
        // If baseNodes.getAttribute("type") isn't either "science" or "guide" different then
        // use it as it is as UKIRT tag (without convcersion).
        else {
          value = type;
        }
	
	// Create Element, "Base" or "GUIDE".
	targetListElement = (ElementImpl)document.createElement(value);
        
	// Set text of first value element to "Base" or "GUIDE" too.
        targetListElement.appendChild(document.createElement("value")).appendChild(document.createTextNode(value));

        // target name
	try {
	  value = baseElement.getElementsByTagName("targetName").item(0).getFirstChild().getNodeValue();
	}
	catch(NullPointerException e) {
	  value = "";
	}
        targetListElement.appendChild(document.createElement("value")).appendChild(document.createTextNode(value));

        // RA
        try {
	  value = ((ElementImpl)(baseElement.getElementsByTagName("hmsdegSystem").item(0)))
                                            .getElementsByTagName("c1").item(0).getFirstChild().getNodeValue();
	}
	catch(NullPointerException e) {
	  value = "";
	}
        targetListElement.appendChild(document.createElement("value")).appendChild(document.createTextNode(value));

        // Dec
	try {
          value = ((ElementImpl)(baseElement.getElementsByTagName("hmsdegSystem").item(0)))
                                            .getElementsByTagName("c2").item(0).getFirstChild().getNodeValue();
	}
	catch(NullPointerException e) {
	  value = "";
	}
        targetListElement.appendChild(document.createElement("value")).appendChild(document.createTextNode(value));

        // System
	try {
          value = ((ElementImpl)(baseElement.getElementsByTagName("hmsdegSystem").item(0))).getAttribute("type");
	}
	catch(NullPointerException e) {
	  value = "";
	}
	if(value.equals("B1950")) value = "FK4 (B1950)";
	if(value.equals("J2000")) value = "FK5 (J2000)";
        targetListElement.appendChild(document.createElement("value")).appendChild(document.createTextNode(value));
    
	// Replacing baseElement element immediately with targetListElement)
	// seems to cause problems because all baseElement nodes seem to be removed as they all have
	// the same node name. So add targetListElement to targetElementVector and do the replacing later.
	targetElementVector.add(targetListElement);
      }

      // Remove all TCS XML "base" elements children
      while(baseNodes.getLength() > 0) {
        element.removeChild(baseNodes.item(0));
      }

      // Add UKIRT / Sp "Base" and "GUIDE" (if there is one) element.
      for(int i = 0; i < targetElementVector.size(); i++) {
        element.appendChild((ElementImpl)targetElementVector.get(i));
      }

      // Deal with chop parameters
      ElementImpl chopElement;
      ElementImpl child;

      if(element.getElementsByTagName("chop").getLength() > 0) {
        chopElement = (ElementImpl)element.getElementsByTagName("chop").item(0);
	
	child = (ElementImpl)chopElement.removeChild(chopElement.getElementsByTagName("chopAngle").item(0));
        child.removeAttribute("units");
        element.appendChild(child);

	child = (ElementImpl)chopElement.removeChild(chopElement.getElementsByTagName("chopThrow").item(0));
        child.removeAttribute("units");
        element.appendChild(child);

        child = (ElementImpl)chopElement.removeChild(chopElement.getElementsByTagName("chopSystem").item(0));
        element.appendChild(child);

        child = (ElementImpl)element.appendChild(document.createElement("chopping"));
	child.appendChild(document.createTextNode("true"));
      }
      else {
        child = (ElementImpl)element.appendChild(document.createElement("chopping"));
	child.appendChild(document.createTextNode("false"));
        child = (ElementImpl)element.appendChild(document.createElement("chopAngle"));
	child.appendChild(document.createTextNode(""));
        child = (ElementImpl)element.appendChild(document.createElement("chopThrow"));
	child.appendChild(document.createTextNode(""));
	child = (ElementImpl)element.appendChild(document.createElement("chopSystem"));
	child.appendChild(document.createTextNode(""));
      }
    }
    // Make sure RuntimeExceptions are not ignored.
    catch(Exception e) {
      e.printStackTrace();
      throw new Exception("Problem while converting TCS XML to value array.");
    }
  }

  /**
   * Converts all child nodes represent SpTelescopeObsComp's.
   * 
   * @see #translateTargetInformation(ElementImpl)
   */
  private void translateAllTargetInformation(ElementImpl element) throws Exception {
    NodeList telescopeObsCompList = element.getElementsByTagName("SpTelescopeObsComp");
  
    for(int i = 0; i < telescopeObsCompList.getLength(); i++) {
      translateTargetInformation((ElementImpl)telescopeObsCompList.item(i));
    }
  }

  /**
   * Converts all child nodes that represent SpTelescopeObsComp's.
   *
   * @see #reverseTargetInformation(ElementImpl)
   */
  private void reverseAllTargetInformation(ElementImpl element) throws Exception {
    NodeList telescopeObsCompList = element.getElementsByTagName("SpTelescopeObsComp");

    for(int i = 0; i < telescopeObsCompList.getLength(); i++) {
      reverseTargetInformation((ElementImpl)telescopeObsCompList.item(i));
    }
  }

}

