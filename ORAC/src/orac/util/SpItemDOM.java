package orac.util;

import gemini.sp.SpItem;
import gemini.sp.SpAvTable;
import gemini.sp.SpRootItem;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;
import gemini.sp.SpFactory;
import gemini.sp.SpInsertData;
import gemini.sp.SpTelescopePos;
import gemini.sp.obsComp.SpTelescopeObsComp;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.TextImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.StringWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;


/**
 * @author Martin Folger
 */
public class SpItemDOM {

  public static final String SP_ITEM_DATA_TAG = "ItemData";
  public static final String SP_ITEM_TYPE     = "type";
  public static final String SP_ITEM_SUBTYPE  = "subtype";
  public static final String SP_ITEM_NAME     = "name";
  public static final String SP_ITEM_PACKAGE  = "package";

  public static final String [] UKIRT_TARGET_TAGS = { "Base",    "GUIDE" };
  public static final String [] TCS_TARGET_TYPES  = { "science", "guide" };

  /**
   * The root of the DOM sub tree that represents the SpItem of this class.
   */
  protected ElementImpl _element;

  public SpItemDOM(SpItem spItem) throws Exception {
    this(spItem, new DocumentImpl());
  }

  public SpItemDOM(SpItem spItem, DocumentImpl ownerDoc) throws Exception {
  
  
    String classNameTag = spItem.getClass().getName().substring(spItem.getClass().getName().lastIndexOf(".") + 1);
    _element = (ElementImpl)ownerDoc.createElement(classNameTag);

    // Parse av table and write result into _element.
    (new SpAvTableDOM(spItem.getTable(), _element)).parseAvTable();
    
    // Append children
    Enumeration children = spItem.children();
    while (children.hasMoreElements()) {
      _element.appendChild((new SpItemDOM((SpItem) children.nextElement(), ownerDoc)).getElement());
    }

    // Create item data node
    ElementImpl itemData = (ElementImpl)ownerDoc.createElement(SP_ITEM_DATA_TAG);
    itemData.setAttribute(SP_ITEM_TYPE,    spItem.typeStr());
    itemData.setAttribute(SP_ITEM_SUBTYPE, spItem.subtypeStr());
    itemData.setAttribute(SP_ITEM_NAME,    spItem.name());
    itemData.setAttribute(SP_ITEM_PACKAGE, 
      spItem.getClass().getName().substring(0, spItem.getClass().getName().lastIndexOf(".")));
    
    // Insert item data node as first child.
    _element.insertBefore(itemData, _element.getFirstChild());

    if(spItem instanceof SpTelescopeObsComp) {
      if(System.getProperty("NO_TCS_XML") == null) {
        // This method is UKIRT specific. JCMT will probably have a different telescope observation component.
        convertToTcsXml(_element);
      }
      else {
        System.out.println("convertToTcsXml switched off. Remove system property NO_TCS_XML (flag -DNO_TCS_XML) to switch it on.");
      }
    }  
  }

  public SpItemDOM(Reader xmlReader) throws Exception { //SAXException, IOException {
    DOMParser parser = new DOMParser();

    /*MFO TODO: better error handling.*/
    //try {
    parser.parse(new InputSource(xmlReader));
    //}
    //catch(Exception e) {
    //  e.printStackTrace();
    //}

    _element = (ElementImpl)parser.getDocument().getDocumentElement();


    // This method is UKIRT specific. JCMT will probably have a different telescope observation component.
    if(System.getProperty("NO_TCS_XML") == null) {
      convertAllFromTcsXml(_element);
    }
    else {
      System.out.println("convertAllFromTcsXml switched off. Remove system property NO_TCS_XML (flag -DNO_TCS_XML) to switch it on.");
    }
  }

  ElementImpl getElement() {
    return _element;
  }

  public String toString(String indent) {
    try {
      OutputFormat    format  = new OutputFormat(_element.getOwnerDocument());   //Serialize DOM
      format.setIndenting(true);
      format.setIndent(2);
      format.setLineSeparator("\n" + indent);
      format.setOmitXMLDeclaration(false);

      StringWriter  stringOut = new StringWriter();        //Writer will be a String
      XMLSerializer    serial = new XMLSerializer( stringOut, format );
      serial.asDOMSerializer();                            // As a DOM Serializer

      serial.serialize(_element); //avTableToXmlElement(table, "TestTable") );
      //serial.serialize(_element.getOwnerDocument().getDocumentElement()); //avTableToXmlElement(table, "TestTable") );

      return indent + stringOut.toString(); //Spit out DOM as a String
    }
    catch(IOException exception) {
      exception.printStackTrace();
      return "";
    }
  }

  public String toString() {
    return toString("");
  }

  /**
   * @return DOM element representing &lt;SP_ITEM_DATA_TAG&gt; if there is one
   *         or null otherwise.
   */
  public ElementImpl getItemDataElement(ElementImpl element) {
    NodeList nodeList = element.getChildNodes();
    
    for(int i = 0; i < nodeList.getLength(); i++) {
      if((nodeList.item(i) instanceof ElementImpl) &&
         nodeList.item(i).getNodeName().equals(SP_ITEM_DATA_TAG)) {
        
	return (ElementImpl)nodeList.item(i);
      }
    }

    return null;
  }

  /**
   * Processes a subtree of the DOM beginning at the level of the direct children of
   * a DOM element representing an SpItem.
   * These subtrees represent the attributes of the SpItem. All the attributes are
   * assembled, and then the attributes and their values are added to the SpAvTable.
   *
   * @see #getAvAttrtibutes(org.apache.xerces.dom.ElementImpl);
   */
/*  protected addAttrtibuteValuePairs(ElementImpl element, SpAvTable avTab) {
    
    String avAttribute = null;
    Hashtable table = buildAvTable(element);

    for(int i = 0; i < avAttributeVector.size(); i++) {
      avAttribute = (String)table.get(i);
      if(avAttribute.startsWith(SP_ITEM_DATA_TAG)) {
        avAttribute = "." + avAttribute.substring(SP_ITEM_DATA_TAG.length());
      }
      avTab.set(avAttribute, );
    }
  }
*/
  /**
   * Is called recursivly.
   *
   * Each attribute or key "grows" from right to left. E.g.
   *       filename = some_file.sp
   *   gui.filename = some_file.sp
   * 
   */
/*  protected Hashtable buildAvTable(ElementImpl element) {
    Hashtable table = new Hashtable();
    Hashtable childTable = null
    NodeList nodeList = element.getChildNodes();

    for(int i = 0; i < nodeList.getLength(); i++) {
      if(nodeList.item(i) instanceof ElementImpl) {
        childTable = buildAvTable((ElementImpl)nodeList.item(i));
	Enumeration keys = childTable.keys();
	while(keys.hasMoreElements()) {
	  String key = (String)keys.nextElement();
          table.put(element.getNodeName() + "." + key, childTable.get(key));
	}
      }
      if(nodeList.item(i) instanceof TextImpl) {
        table.put(element.getNodeName(), ((TextImpl)nodeList.item(i)).getData());
      }
    }

    return table;
  }
*/
  public static void fillAvTable(String nameSoFar, ElementImpl remainingTree, SpAvTable avTab) {
    // Ignore ItemData
    if((remainingTree.getNodeName().equals("ItemData"))) {
      return;
    }

    String nodeName = remainingTree.getNodeName();
    if(remainingTree.getUserData() != null) {
      nodeName += remainingTree.getUserData();
    }

    String prefix;
    if((nameSoFar != null) && (nameSoFar.trim() != "")) {
      if(nameSoFar.equals(SpAvTableDOM.META_DATA_TAG)) {
        prefix = ".";
      }
      else {
        prefix = nameSoFar + ".";
      }	
    }
    else {
      prefix = "";
    }
    
    // Deal with attributes
    if(remainingTree.getAttributes() != null && remainingTree.getAttributes().getLength() > 0) {
      NamedNodeMap nodeMap = remainingTree.getAttributes();
      for(int i = 0; i < nodeMap.getLength(); i++) {
       avTab.set(prefix          + nodeName
                           + ":" + nodeMap.item(i).getNodeName(),
			           nodeMap.item(i).getNodeValue());
      }
    }

    // Deal with child nodes
    NodeList nodeList = remainingTree.getChildNodes();

    // Check whether there are siblings with the same node name (tag name) and set user data to
    // string "#" + number
    setNumbers(nodeList);

    Vector valueVector = new Vector();
    for(int i = 0; i < nodeList.getLength(); i++) {
      if(nodeList.item(i) instanceof ElementImpl) {
        if(nodeList.item(i).getNodeName().equals("value")) {
	  if((nodeList.item(i).getFirstChild() != null) && (nodeList.item(i).getFirstChild().getNodeValue() != null)) {
	    valueVector.add(nodeList.item(i).getFirstChild().getNodeValue());
          }
	  else {
            valueVector.add("");
	  }
	}
	else {
	  fillAvTable(prefix + nodeName,
                      (ElementImpl)nodeList.item(i),
		      avTab);
	}	      
      }
      else if(nodeList.item(i) instanceof TextImpl) {
        // Ignore extra items beginning with "\n" created by DOMParser.parse().
        if(!nodeList.item(i).getNodeValue().startsWith("\n")) {
          
	  avTab.set(prefix + nodeName,
	                         nodeList.item(i).getNodeValue());
	}
	else {
	}
      }
      else {
        /*MFO TODO: better error handling.*/ System.out.println("Unknown node type: " + nodeList.item(i).getClass().getName());
      }
    }  
    
    // adding value vector
    if(valueVector.size() > 0) {
      avTab.setAll(prefix + nodeName, valueVector);
    }
  
  }

  public SpRootItem getSpItem() {
    /*MFO TODO: better error handling.*/
    try {
      SpRootItem spRootItem = (SpRootItem)getSpItem(_element);
      
      // return deepCopy of spRootItem instead of spRootItem to prevent bug that cases
      // GUIDE row in target list table not to be displayed.
      return (SpRootItem)spRootItem.deepCopy();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @return SpItem if the DOM element represent an SpItem (i.e. has a &lt;SP_ITEM_DATA_TAG&gt;)
   *         or null otherwise.
   */
  protected SpItem getSpItem(ElementImpl element) { //throws Exception {
    ElementImpl itemDataElement = getItemDataElement(element);

    // If element has no item data child then it does not represent an SpItem.
    if(itemDataElement == null) {
      return null;
    }
    SpType spType = SpType.get(itemDataElement.getAttribute(SP_ITEM_TYPE),
                               itemDataElement.getAttribute(SP_ITEM_SUBTYPE));
    
    SpItem spItem = SpFactory.createShallow(spType);
    spItem.name(itemDataElement.getAttribute(SP_ITEM_NAME));


    NodeList nodeList = element.getChildNodes();
    ElementImpl childElement = null;
    Vector childV = new Vector();
    
    for(int i = 0; i < nodeList.getLength(); i++) {
      if(nodeList.item(i) instanceof ElementImpl) {
        childElement = (ElementImpl)nodeList.item(i);
        
	// Re-use itemDataElement for children.
        itemDataElement = getItemDataElement(childElement);

        // DOM element represents an SpItem.
        if(itemDataElement != null) {
          childV.addElement(getSpItem(childElement));
        }
	// DOM element represents an SpAvTable attribute.
	else {
	  fillAvTable("", childElement, spItem.getTable());
	  //addAttrtibuteValuePair(childElement, spItem.getTable());
	}
      }
    }  

    if ((childV != null) && (childV.size() > 0)) {
  
      SpItem[] spItemA = new SpItem[ childV.size() ];

      // Convert the vector into an array.
      for (int i=0; i<spItemA.length; ++i) {
        spItemA[i] = (SpItem) childV.elementAt(i);
      }

      SpInsertData spID = SpTreeMan.evalInsertInside(spItemA, spItem);
      if (spID == null) {
        //_problem = "Illegal program or plan.";
        /*MFO TODO: better error handling.*/ //throw new Exception("Illegal program or plan.");
        /*MFO TODO: better error handling.*/ System.out.println("Illegal program or plan.");
      }
      else {
        SpTreeMan.insert(spID);
      }	
    }
    
    return spItem;  
  }

  /**
   * For testing.
   */
  public static void main(String [] args) {
    if(args.length != 1) {
      System.out.println("Usage: AvTableToDom input_file");
      System.out.println("   input_file: contains key value pairs for initialising the test table");
      return;
    }

    java.util.Properties props = new java.util.Properties();
    try {
      props.load(new java.io.FileInputStream(args[0]));
    }
    catch(Exception e) {
      System.out.println("Problems loading properties form file " + args[0] + ": " + e);
    }
    
    Enumeration e = props.propertyNames();
    String key = null;
    String property = null;
    Vector vector = null;
    SpAvTable table = new SpAvTable();
    while(e.hasMoreElements()) {
      key = (String)e.nextElement();
      property = props.getProperty(key);
      
      if(property.startsWith("{") && property.endsWith("}")) {
        vector = new Vector();
        StringTokenizer stringTokenizer = new StringTokenizer(property, ";,{} ");
        
	while(stringTokenizer.hasMoreTokens()) {
          vector.add(stringTokenizer.nextToken());
        }

	table.setAll(key, vector);
      }
      else {
        table.set(key, property);
      }
      

    }
    //System.out.println((new AvTableToDom(table, "TestTable", new DocumentImpl())).toString("    "));
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
   * @see #convertFromTcsXml(org.apache.xerces.dom.ElementImpl)
   */
  private static void convertToTcsXml(ElementImpl element) throws Exception {
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
    
      for(int i = 0; i < UKIRT_TARGET_TAGS.length; i++) {
        targetListElement = (ElementImpl)element.getElementsByTagName(UKIRT_TARGET_TAGS[i]).item(0);
      
        if(targetListElement != null) {
          valueList = targetListElement.getElementsByTagName("value");

          // Don't confuse TCS tag "Base" (JCMT speak) with Ukirt tag "Base" (as in "Base vs GUIDE")
          baseElement = (ElementImpl)document.createElement("base");

          // Add target element and add type attribute.
          child = (ElementImpl)baseElement.appendChild(document.createElement("target"));
          child.setAttribute("type", TCS_TARGET_TYPES[i]);

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
      if(element.getElementsByTagName("chopping").item(0).getFirstChild().getNodeValue().equals("true")) {
        ElementImpl chopElement = (ElementImpl)document.createElement("chop");

        child = (ElementImpl)element.removeChild(element.getElementsByTagName("chopAngle").item(0));
        child.setAttribute("units", "degrees");
        chopElement.appendChild(child);

        child = (ElementImpl)element.removeChild(element.getElementsByTagName("chopThrow").item(0));
        child.setAttribute("units", "arcseconds");
        chopElement.appendChild(child);

        // Currently not needed
//        child = (ElementImpl)element.removeChild(element.getElementsByTagName("chopSystem").item(0));
//        chopElement.appendChild(child);

        element.appendChild(chopElement);
      }
      else {
        element.removeChild(element.getElementsByTagName("chopAngle").item(0));
        element.removeChild(element.getElementsByTagName("chopThrow").item(0));
	// Currently not needed
	//element.removeChild(element.getElementsByTagName("chopSystem").item(0));
      }

      element.removeChild(element.getElementsByTagName("chopping").item(0));
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
   * @see #convertToTcsXml(org.apache.xerces.dom.ElementImpl)
   */
  private static void convertFromTcsXml(ElementImpl element) throws Exception {
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

        if(type.equals(TCS_TARGET_TYPES[0])) {
          value = UKIRT_TARGET_TAGS[0];
        }
        else if(type.equals(TCS_TARGET_TYPES[1])) {
          value = UKIRT_TARGET_TAGS[1];
        }
        // The tag should always be one of TCS_TARGET_TYPES ("science", "guide") which
        // is then converted to the respective String in UKIRT_TARGET_TAGS ("Base", "GUIDE")
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
        value = baseElement.getElementsByTagName("targetName").item(0).getFirstChild().getNodeValue();
        targetListElement.appendChild(document.createElement("value")).appendChild(document.createTextNode(value));

        // RA
        value = ((ElementImpl)(baseElement.getElementsByTagName("hmsdegSystem").item(0)))
                                          .getElementsByTagName("c1").item(0).getFirstChild().getNodeValue();
        targetListElement.appendChild(document.createElement("value")).appendChild(document.createTextNode(value));

        // Dec
        value = ((ElementImpl)(baseElement.getElementsByTagName("hmsdegSystem").item(0)))
                                          .getElementsByTagName("c2").item(0).getFirstChild().getNodeValue();
        targetListElement.appendChild(document.createElement("value")).appendChild(document.createTextNode(value));

        // System
        value = ((ElementImpl)(baseElement.getElementsByTagName("hmsdegSystem").item(0))).getAttribute("type");
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

        // Currently not needed
//        child = (ElementImpl)chopElement.removeChild(chopElement.getElementsByTagName("chopSystem").item(0));
//        element.appendChild(child);

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

	// Currently not needed
//	child = (ElementImpl)element.appendChild(document.createElement("chopSystem"));
//	child.appendChild(document.createTextNode(""));
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
   * @see #convertToTcsXml(ElementImpl)
   */
  private static void convertAllToTcsXml(ElementImpl element) throws Exception {
    NodeList telescopeObsCompList = element.getElementsByTagName("SpTelescopeObsComp");
  
    for(int i = 0; i < telescopeObsCompList.getLength(); i++) {
      convertToTcsXml((ElementImpl)telescopeObsCompList.item(i));
    }
  }

  /**
   * Converts all child nodes that represent SpTelescopeObsComp's.
   *
   * @see #convertFromTcsXml(ElementImpl)
   */
  private static void convertAllFromTcsXml(ElementImpl element) throws Exception {
    NodeList telescopeObsCompList = element.getElementsByTagName("SpTelescopeObsComp");

    for(int i = 0; i < telescopeObsCompList.getLength(); i++) {
      convertFromTcsXml((ElementImpl)telescopeObsCompList.item(i));
    }
  }

  /**
   * Checks whether there are siblings with the same node name (tag name) and set user data to
   * string "#" + number so they can be save under different names in the av table.
   */
  public static void setNumbers(NodeList siblings) {
    if(siblings == null) {
      return;
    }

    Hashtable elementTable = new Hashtable();

    for(int i = 0; i < siblings.getLength(); i++) {
      if(elementTable.get(siblings.item(i).getNodeName()) == null) {
        elementTable.put(siblings.item(i).getNodeName(), new Vector());
      }

      ((Vector)(elementTable.get(siblings.item(i).getNodeName()))).add(siblings.item(i));
    }

    Enumeration tableEntries = elementTable.elements();
    Vector elementVector;
    while(tableEntries.hasMoreElements()) {
      elementVector = (Vector)tableEntries.nextElement();

      if(elementVector.size() > 1) {
        for(int i = 0; i < elementVector.size(); i++) {
          if(elementVector.get(i) instanceof ElementImpl) {
	    ((ElementImpl)elementVector.get(i)).setUserData("#" + i);
	  }
	}
      }
    }
  }
}

