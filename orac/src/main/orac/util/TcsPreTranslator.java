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

package orac.util;

import orac.util.PreTranslator;
import gemini.sp.SpTelescopePos;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
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
 * The pre-translater classes for UKIRT and JCMT are subclasses of this
 * TcsPreTranslator.  If the OT is used for other telescopes which do not use
 * JAC OCS TCS then the interface {@link orac.util.PreTranslator} can be
 * implemented directly.
 *
 * @author Martin Folger
 */
public abstract class TcsPreTranslator implements PreTranslator {
    protected String EXCECPTION_MESSAGE_PREFIX =
            "Problem pre-translating xml: ";

    /**
     * Telescope specific target types.
     */
    private static String[] TELESCOPE_TARGET_TAGS;

    /**
     * Target types used by the JAC OCS TCS.
     */
    protected abstract String[] getTcsTargetTypes();

    public TcsPreTranslator(String telescopeTarget1, String telescopeTarget2)
            throws Exception {
        TELESCOPE_TARGET_TAGS = new String[]{
                telescopeTarget1,
                telescopeTarget2,
        };
    }

    /**
     * @param  element DOM element from {@link orac.util.SpItemDOM}
     */
    public void translate(ElementImpl element) throws Exception {
        if ((getTcsTargetTypes() == null) || (getTcsTargetTypes().length != 2)
                || (TELESCOPE_TARGET_TAGS == null)
                || (TELESCOPE_TARGET_TAGS.length != 2)) {
            throw new Exception(EXCECPTION_MESSAGE_PREFIX
                    + "\n2 TCS target types and 2 telescope target tags"
                    + " needed for translation.");
        }

        translateAllTargetInformation(element);
        translateAllOffsetIterators(element);
    }

    /**
     * @param  element DOM element from {@link orac.util.SpItemDOM}
     */
    public void reverse(ElementImpl element) throws Exception {
        if ((getTcsTargetTypes() == null) || (getTcsTargetTypes().length != 2)
                || (TELESCOPE_TARGET_TAGS == null)
                || (TELESCOPE_TARGET_TAGS.length != 2)) {
            throw new Exception(EXCECPTION_MESSAGE_PREFIX
                    + "\n2 TCS target types and 2 telescope target tags"
                    + " needed for reverse translation.");
        }

        reverseAllTargetInformation(element);
        reverseAllOffsetIterators(element);
    }

    /**
     * Converts DOM/XML representaion of SpTelescopeObsComp.
     *
     * From: DOM Element based on XML generated from SpAvTable in
     *       SpTelescopeObsComp
     * To:   DOM Element based on XML as used in the TCS.
     *
     * The DTD/XML used in the TCS is based on the Gemini Phase 1 Tool.
     * See document OCS/ICD/006.2.
     *
     * @see #reverseTargetInformation(org.apache.xerces.dom.ElementImpl)
     */
    private void translateTargetInformation(ElementImpl element)
            throws Exception {

        if (!element.getTagName().equals("SpTelescopeObsComp")) {
            return;
        }

        try {
            DocumentImpl document = (DocumentImpl) element.getOwnerDocument();

            // Elements before conversion.
            ElementImpl targetListElement;
            NodeList valueList;

            // Converted elements.
            ElementImpl baseElement = null;
            ElementImpl child;

            for (int i = 0; i < TELESCOPE_TARGET_TAGS.length; i++) {
                targetListElement = (ElementImpl) element.getElementsByTagName(
                        TELESCOPE_TARGET_TAGS[i]).item(0);

                if (targetListElement != null) {
                    boolean translationNeeded = true;

                    // If conicSystem or namedSystem then do not translate
                    // this target.
                    NodeList
                    targetSystemList = targetListElement.getElementsByTagName(
                            "conicSystem");
                    if ((targetSystemList != null)
                            && (targetSystemList.getLength() > 0)) {
                        translationNeeded = false;
                    }

                    targetSystemList = targetListElement.getElementsByTagName(
                            "namedSystem");
                    if ((targetSystemList != null)
                            && (targetSystemList.getLength() > 0)) {
                        translationNeeded = false;
                    }

                    if (translationNeeded) {
                        valueList = targetListElement.getElementsByTagName(
                                "value");

                        // Don't confuse OCS TCS element <base> with UKIRT
                        // tag "Base".
                        baseElement = (ElementImpl) document.createElement(
                                "base");

                        // Add target element and add type attribute.
                        child = (ElementImpl) baseElement.appendChild(
                                document.createElement("target"));
                        child.setAttribute("type", getTcsTargetTypes()[i]);

                        // Add targetName element with text node containing
                        // the target name.
                        child = (ElementImpl) child.appendChild(
                                document.createElement("targetName"));
                        child.appendChild(document.createTextNode(
                                valueList.item(SpTelescopePos.NAME_INDEX)
                                .getFirstChild().getNodeValue()));

                        // Set child to target element again.
                        child = (ElementImpl) child.getParentNode();

                        // Add hmsdegSystem element and add type attribute.
                        child = (ElementImpl) child.appendChild(
                                document.createElement("hmsdegSystem"));
                        String system = valueList.item(
                                SpTelescopePos.COORD_SYS_INDEX)
                                .getFirstChild().getNodeValue();

                        if (system.equals("FK4 (B1950)")) {
                            system = "B1950";
                        }

                        if (system.equals("FK5 (J2000)")) {
                            system = "J2000";
                        }

                        child.setAttribute("type", system);

                        // Add c1 target with text node containing RA.
                        child = (ElementImpl) child.appendChild(
                                document.createElement("c1"));
                        child.appendChild(document.createTextNode(
                                valueList.item(SpTelescopePos.XAXIS_INDEX)
                                .getFirstChild().getNodeValue()));

                        // Set child to hmsdegSystem element again.
                        child = (ElementImpl) child.getParentNode();

                        // Add c2 target with text node containing Dec.
                        child = (ElementImpl) child.appendChild(
                                document.createElement("c2"));
                        child.appendChild(document.createTextNode(
                                valueList.item(SpTelescopePos.YAXIS_INDEX)
                                .getFirstChild().getNodeValue()));

                        // Replace old target list element (Sp style value
                        // array) with new base element (TCS XML)
                        element.replaceChild(baseElement, targetListElement);
                    }
                }
            }

            // Deal with chop parameters
            if (element.getElementsByTagName("chopping").item(0) != null) {
                if (element.getElementsByTagName("chopping").item(0)
                        .getFirstChild().getNodeValue().equals("true")) {
                    ElementImpl chopElement =
                            (ElementImpl) document.createElement("chop");

                    if (element.getElementsByTagName("chopAngle").item(0) != null) {
                        child = (ElementImpl) element.removeChild(
                                element.getElementsByTagName("chopAngle").item(0));
                        child.setAttribute("units", "degrees");
                        chopElement.appendChild(child);
                    }

                    if (element.getElementsByTagName("chopThrow").item(0) != null) {
                        child = (ElementImpl) element.removeChild(
                                element.getElementsByTagName("chopThrow").item(0));
                        child.setAttribute("units", "arcseconds");
                        chopElement.appendChild(child);
                    }

                    if (element.getElementsByTagName("chopSystem").item(0) != null) {
                        child = (ElementImpl) element.removeChild(
                                element.getElementsByTagName("chopSystem").item(0));
                        chopElement.appendChild(child);
                    }

                    element.appendChild(chopElement);

                } else {
                    element.removeChild(element.getElementsByTagName(
                            "chopAngle").item(0));
                    element.removeChild(element.getElementsByTagName(
                            "chopThrow").item(0));

                    try {
                        element.removeChild(element.getElementsByTagName(
                                "chopSystem").item(0));
                    } catch (Exception e) {
                        // Throws a NullPointerException if there is no
                        // chopSystem. Ignore.
                    }
                }

                element.removeChild(
                        element.getElementsByTagName("chopping").item(0));
            }

        } catch (Exception e) {
            // Make sure RuntimeExceptions are not ignored.

            e.printStackTrace();
            throw new Exception(
                    "Problem while converting target information"
                    + " to TCS XML format.");
        }
    }

    /**
     * Converts DOM/XML representaion of SpTelescopeObsComp.
     *
     * From: DOM Element based on XML as used in the TCS.
     * To:   DOM Element based on XML generated from SpAvTable in
     *       SpTelescopeObsComp
     *
     * The DTD/XML used in the TCS is based on the Gemini Phase 1 Tool.
     * See document OCS/ICD/006.2.
     *
     * @see #translateTargetInformation(org.apache.xerces.dom.ElementImpl)
     */
    private void reverseTargetInformation(ElementImpl element) throws Exception {

        if (!element.getTagName().equals("SpTelescopeObsComp")) {
            return;
        }

        try {
            DocumentImpl document = (DocumentImpl) element.getOwnerDocument();

            // Elements before conversion.
            NodeList baseNodes = element.getElementsByTagName("base");
            ElementImpl baseElement;

            // Converted elements.
            ElementImpl targetListElement = null;
            String value = null;
            String type = null;
            Vector<ElementImpl> targetElementVector = new Vector<ElementImpl>();

            // There should be one or two <base> elements (one for each
            // telescope target tag).
            for (int i = 0; i < baseNodes.getLength(); i++) {
                baseElement = (ElementImpl) baseNodes.item(i);

                // tag
                type = ((ElementImpl) baseElement
                        .getElementsByTagName("target").item(0))
                        .getAttribute("type");

                // ?? MFO
                // If baseNodes.getAttribute("type") isn't one of
                // getTcsTargetTypes() then use it directly without converting
                // it.
                if (type.equals(getTcsTargetTypes()[0])) {
                    value = TELESCOPE_TARGET_TAGS[0];
                } else if (type.equals(getTcsTargetTypes()[1])) {
                    value = TELESCOPE_TARGET_TAGS[1];
                } else {
                    value = type;
                }

                // Create Element for telescope target tags.
                targetListElement = (ElementImpl) document.createElement(value);

                // Set text of first value element to the telescope target tag too.
                targetListElement.appendChild(
                        document.createElement("value")).appendChild(
                                document.createTextNode(value));

                // target name
                try {
                    value = baseElement.getElementsByTagName("targetName")
                            .item(0).getFirstChild().getNodeValue();
                } catch (NullPointerException e) {
                    value = "";
                }

                targetListElement.appendChild(
                        document.createElement("value")).appendChild(
                                document.createTextNode(value));

                // RA
                try {
                    value = ((ElementImpl) (baseElement
                            .getElementsByTagName("hmsdegSystem").item(0)))
                            .getElementsByTagName("c1").item(0).getFirstChild()
                            .getNodeValue();
                } catch (NullPointerException e) {
                    value = "";
                }

                targetListElement.appendChild(
                        document.createElement("value")).appendChild(
                                document.createTextNode(value));

                // Dec
                try {
                    value = ((ElementImpl) (baseElement
                            .getElementsByTagName("hmsdegSystem").item(0)))
                            .getElementsByTagName("c2").item(0).getFirstChild()
                            .getNodeValue();
                } catch (NullPointerException e) {
                    value = "";
                }

                targetListElement.appendChild(
                        document.createElement("value")).appendChild(
                                document.createTextNode(value));

                // System
                try {
                    value = ((ElementImpl) (baseElement
                            .getElementsByTagName("hmsdegSystem").item(0)))
                            .getAttribute("type");
                } catch (NullPointerException e) {
                    value = "";
                }

                if (value.equals("B1950")) {
                    value = "FK4 (B1950)";
                }

                if (value.equals("J2000")) {
                    value = "FK5 (J2000)";
                }

                targetListElement.appendChild(
                        document.createElement("value")).appendChild(
                                document.createTextNode(value));

                // Replacing baseElement element immediately with
                // targetListElement) seems to cause problems because all
                // baseElement nodes seem to be removed as they all have
                // the same node name. So add targetListElement to
                // targetElementVector and do the replacing later.
                targetElementVector.add(targetListElement);
            }

            // Remove all TCS XML "base" elements children
            while (baseNodes.getLength() > 0) {
                element.removeChild(baseNodes.item(0));
            }

            // Add telescope target tag elements.
            for (int i = 0; i < targetElementVector.size(); i++) {
                element.appendChild(targetElementVector.get(i));
            }

            // Deal with chop parameters
            ElementImpl chopElement;
            ElementImpl child;

            if (element.getElementsByTagName("chop").getLength() > 0) {
                chopElement = (ElementImpl)
                        element.getElementsByTagName("chop").item(0);

                child = (ElementImpl) chopElement.removeChild(
                        chopElement.getElementsByTagName("chopAngle").item(0));
                child.removeAttribute("units");
                element.appendChild(child);

                child = (ElementImpl) chopElement.removeChild(
                        chopElement.getElementsByTagName("chopThrow").item(0));
                child.removeAttribute("units");
                element.appendChild(child);

                try {
                    child = (ElementImpl) chopElement.removeChild(
                            chopElement.getElementsByTagName("chopSystem").item(0));
                    element.appendChild(child);
                } catch (Exception e) {
                    // Throws a NullPointerException if there is no chopSystem.
                    // Ignore.
                }

                child = (ElementImpl) element.appendChild(
                        document.createElement("chopping"));
                child.appendChild(document.createTextNode("true"));
            } else {
                child = (ElementImpl) element.appendChild(
                        document.createElement("chopping"));
                child.appendChild(document.createTextNode("false"));
                child = (ElementImpl) element.appendChild(
                        document.createElement("chopAngle"));
                child.appendChild(document.createTextNode(""));
                child = (ElementImpl) element.appendChild(
                        document.createElement("chopThrow"));
                child.appendChild(document.createTextNode(""));
                child = (ElementImpl) element.appendChild(
                        document.createElement("chopSystem"));
                child.appendChild(document.createTextNode(""));
            }

        } catch (Exception e) {
            // Make sure RuntimeExceptions are not ignored.

            e.printStackTrace();

            throw new Exception(
                    "Problem while converting TCS XML to value array.");
        }
    }

    /**
     * Converts all child nodes represent SpTelescopeObsComp's.
     *
     * @see #translateTargetInformation(ElementImpl)
     */
    private void translateAllTargetInformation(ElementImpl element)
            throws Exception {
        NodeList telescopeObsCompList =
                element.getElementsByTagName("SpTelescopeObsComp");

        for (int i = 0; i < telescopeObsCompList.getLength(); i++) {
            translateTargetInformation(
                    (ElementImpl) telescopeObsCompList.item(i));
            }
    }

    /**
     * Converts all child nodes that represent SpTelescopeObsComp's.
     *
     * @see #reverseTargetInformation(ElementImpl)
     */
    private void reverseAllTargetInformation(ElementImpl element)
            throws Exception {
        NodeList telescopeObsCompList =
                element.getElementsByTagName("SpTelescopeObsComp");

        for (int i = 0; i < telescopeObsCompList.getLength(); i++) {
            reverseTargetInformation(
                    (ElementImpl) telescopeObsCompList.item(i));
        }
    }

    /**
     * Converts DOM/XML representaion of SpIterOffset.
     *
     * From: DOM Element based on XML generated from SpAvTable in SpIterOffset
     * To:   DOM Element based on XML as used in the JAC OCS TCS.
     *
     * The DTD/XML used in the TCS is based on the Gemini Phase 1 Tool.
     * See document OCS/ICD/006.2.
     *
     * @see #reverseOffsetIterator(org.apache.xerces.dom.ElementImpl)
     */
    private void translateOffsetIterator(ElementImpl element) throws Exception {
        if (!element.getTagName().equals("SpIterOffset")) {
            return;
        }

        try {
            DocumentImpl document = (DocumentImpl) element.getOwnerDocument();

            // Science Program XML elements

            // <offsetPositions>
            ElementImpl sp_offsetPositions;

            // <OffsetN> where N is an integer at the end of the tag name
            // string
            ElementImpl sp_OffsetN;

            // <value>   These are the <value> elements in
            //           sp_offsetPositions (<offsetPositions>)
            //           There is one such element for each offset position.
            NodeList sp_offsetPositions_values;

            // <value>   These are the <value> elements in
            //           sp_OffsetN_values (<OffsetN>)
            //           There is one such element for p and one for q.
            NodeList sp_OffsetN_values;

            // OffsetN name String.
            String sp_OffsetN_name;

            // Array of OffsetN name Strings (used for sorting).
            String[] sp_OffsetN_nameArray;

            // TCS XML elements

            // <obsArea>
            ElementImpl tcs_obsArea;

            // <offset>
            ElementImpl tcs_offset;

            // Hashtable containing tcs_offsets so that they can be sorted.
            Hashtable<String, ElementImpl> tcs_offset_table =
                    new Hashtable<String, ElementImpl>();

            // Get hold of the offset positions.
            sp_offsetPositions = (ElementImpl) element.getElementsByTagName(
                    "offsetPositions").item(0);

            if (sp_offsetPositions == null) {
                throw new Exception(EXCECPTION_MESSAGE_PREFIX
                        + "\n\nCould not find offset positions"
                        + " in offset iterator.");
            }

            // Get hold of the values in sp_offsetPositions list.
            sp_offsetPositions_values =
                    sp_offsetPositions.getElementsByTagName("value");

            // If there is only one offset position then it is directly inside
            // <offsetPositions> (no <value> tag).
            if ((sp_offsetPositions_values == null)
                    || (sp_offsetPositions_values.getLength() < 1)) {
                // Put the single offset position inside <offsetPositions>
                // into a <value> element.
                sp_offsetPositions.appendChild(
                        document.createElement("value")).appendChild(
                                document.createTextNode(
                                        sp_offsetPositions.getFirstChild()
                                                .getNodeValue()));

                // Now try again
                sp_offsetPositions_values =
                        sp_offsetPositions.getElementsByTagName("value");
            }

            // Create obsArea element for TCS XML.
            tcs_obsArea = (ElementImpl) document.createElement("obsArea");
            sp_OffsetN_nameArray =
                    new String[sp_offsetPositions_values.getLength()];

            for (int i = 0; i < sp_offsetPositions_values.getLength(); i++) {
                tcs_offset = (ElementImpl) document.createElement("offset");
                tcs_offset.setAttribute("id", "offset" + (i + 1));
                sp_OffsetN_name = sp_offsetPositions_values.item(i)
                        .getFirstChild().getNodeValue();
                tcs_offset_table.put(sp_OffsetN_name, tcs_offset);
                sp_OffsetN_nameArray[i] = sp_OffsetN_name;

                sp_OffsetN = (ElementImpl) element.getElementsByTagName(
                        sp_OffsetN_name).item(0);

                if (sp_OffsetN != null) {
                    sp_OffsetN_values =
                            sp_OffsetN.getElementsByTagName("value");

                    tcs_offset.appendChild(
                            document.createElement("dc1")).appendChild(
                                    document.createTextNode(
                                            sp_OffsetN_values.item(0)
                                            .getFirstChild().getNodeValue()));

                    tcs_offset.appendChild(
                            document.createElement("dc2")).appendChild(
                                    document.createTextNode(
                                            sp_OffsetN_values.item(1)
                                            .getFirstChild().getNodeValue()));

                    // Remove sp_OffsetN <OffsetN>.
                    element.removeChild(sp_OffsetN);
                }
            }

            // Sort offsets and append them to tcs_obsArea <obsArea>.
            Arrays.sort(sp_OffsetN_nameArray);

            for (int i = 0; i < sp_OffsetN_nameArray.length; i++) {
                tcs_obsArea.appendChild(
                        tcs_offset_table.get(sp_OffsetN_nameArray[i]));
            }

            // Remove sp_offsetPositions <offsetPositions>.
            element.removeChild(sp_offsetPositions);

            // Append tcs_obsArea <obsArea>.
            element.appendChild(tcs_obsArea);

        } catch (Exception e) {
            // Make sure RuntimeExceptions are not ignored.

            e.printStackTrace();

            throw new Exception(EXCECPTION_MESSAGE_PREFIX
                    + "\n\nProblem converting offset iterator"
                    + " to TCS XML format.\n\n" + e);
        }
    }

    /**
     * Converts all child nodes represent SpTelescopeObsComp's.
     *
     * @see #translateOffsetIterator(ElementImpl)
     */
    private void translateAllOffsetIterators(ElementImpl element)
            throws Exception {
        NodeList offsetIteratorList =
                element.getElementsByTagName("SpIterOffset");

        for (int i = 0; i < offsetIteratorList.getLength(); i++) {
            translateOffsetIterator((ElementImpl) offsetIteratorList.item(i));
        }
    }

    /**
     * Converts DOM/XML representaion of SpTelescopeObsComp.
     *
     * From: DOM Element based on XML as used in the TCS.
     * To:   DOM Element based on XML generated from SpAvTable in SpIterOffset
     *
     * The DTD/XML used in the TCS is based on the Gemini Phase 1 Tool.
     * See document OCS/ICD/006.2.
     *
     * @see #translateOffsetIterator(org.apache.xerces.dom.ElementImpl)
     */
    private void reverseOffsetIterator(ElementImpl element) throws Exception {
        if (!element.getTagName().equals("SpIterOffset")) {
            return;
        }

        try {
            DocumentImpl document = (DocumentImpl) element.getOwnerDocument();

            // Science Program XML elements

            // <offsetPositions>
            ElementImpl sp_offsetPositions;

            // <OffsetN> where N is an integer at the end of the tag name
            // string
            ElementImpl sp_OffsetN;

            // <value>   <value> element in sp_OffsetN_values (<OffsetN>)
            //           There is one such element for p and one for q.
            ElementImpl sp_OffsetN_value;

            // TCS XML elements

            // <obsArea>
            ElementImpl tcs_obsArea;

            // <offset>
            NodeList tcs_offsets;

            // Hashtable containing tcs_offsets so that they can be sorted.
            Hashtable<String, ElementImpl> tcs_offset_table =
                    new Hashtable<String, ElementImpl>();

            // "offsetN" in <offset id="offsetN"> where N is the number of
            // this offset
            String tcs_offset_id;

            // Array of tcs_offset_id Strings (used for sorting).
            String[] tcs_offset_id_array;

            // N in <offset id="offsetN">
            int tcs_offset_number;
            String tcs_dc1;
            String tcs_dc2;

            // Get tcs_obsArea (<obsArea>).
            tcs_obsArea = (ElementImpl)
                    element.getElementsByTagName("obsArea").item(0);

            // Get <offset> elements.
            tcs_offsets = tcs_obsArea.getElementsByTagName("offset");

            // Append sp_offsetPositions (<offsetPositions>) to element
            // (<SpIterOffset>).
            sp_offsetPositions = (ElementImpl) element.appendChild(
                    document.createElement("offsetPositions"));

            tcs_offset_id_array = new String[tcs_offsets.getLength()];

            for (int i = 0; i < tcs_offsets.getLength(); i++) {
                // Get the offset id string.
                tcs_offset_id =
                        ((ElementImpl) tcs_offsets.item(i)).getAttribute("id");
                tcs_offset_id_array[i] = tcs_offset_id;
                tcs_offset_table.put(tcs_offset_id,
                        (ElementImpl) tcs_offsets.item(i));
            }

            // Sort offset id array.
            Arrays.sort(tcs_offset_id_array);

            for (int i = 0; i < tcs_offsets.getLength(); i++) {
                // Parse the offset number at the end of the offset id string.
                // This relies on the offset string being of the form
                // "offsetN" where N is an integer.
                tcs_offset_number = Integer.parseInt(
                        tcs_offset_id_array[i].substring(6));

                tcs_dc1 = tcs_offset_table.get(tcs_offset_id_array[i])
                        .getElementsByTagName("dc1").item(0)
                        .getFirstChild().getNodeValue();

                tcs_dc2 = tcs_offset_table.get(tcs_offset_id_array[i])
                        .getElementsByTagName("dc2").item(0)
                        .getFirstChild().getNodeValue();

                // Create sp_OffsetN (<OffsetN>) where N is
                // tcs_offset_number - 1.
                sp_OffsetN = (ElementImpl) element.appendChild(
                        document.createElement(
                                "Offset" + (tcs_offset_number - 1)));

                // Create <value> element for dc1.
                sp_OffsetN_value = (ElementImpl) sp_OffsetN.appendChild(
                        document.createElement("value"));
                sp_OffsetN_value.appendChild(document.createTextNode(tcs_dc1));

                // Create <value> element for dc2.
                sp_OffsetN_value = (ElementImpl) sp_OffsetN.appendChild(
                        document.createElement("value"));
                sp_OffsetN_value.appendChild(document.createTextNode(tcs_dc2));

                // Append tag name of sp_OffsetN (<OffsetN>) to offset list
                // sp_offsetPositions (<offsetPositions>).
                sp_offsetPositions.appendChild(
                        document.createElement("value")).appendChild(
                                document.createTextNode(
                                        "Offset" + (tcs_offset_number - 1)));
            }

            // Remove tcs_obsArea (<obsArea>).
            element.removeChild(tcs_obsArea);

        } catch (Exception e) {
            // Make sure RuntimeExceptions are not ignored.

            e.printStackTrace();

            throw new Exception(EXCECPTION_MESSAGE_PREFIX
                    + "Problem converting offset iterator from TCS XML.");
        }
    }

    /**
     * Converts all child nodes that represent SpTelescopeObsComp's.
     *
     * @see #reverseOffsetIterator(ElementImpl)
     */
    private void reverseAllOffsetIterators(ElementImpl element)
            throws Exception {
        NodeList offsetIteratorList =
                element.getElementsByTagName("SpIterOffset");

        for (int i = 0; i < offsetIteratorList.getLength(); i++) {
            reverseOffsetIterator((ElementImpl) offsetIteratorList.item(i));
        }
    }
}
